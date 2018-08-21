package com.bon.modules.sys.service.impl;


import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.domain.vo.PageVO;
import com.bon.common.exception.BusinessException;
import com.bon.common.util.BeanUtil;
import com.bon.common.util.MyLog;
import com.bon.common.util.ShiroUtil;
import com.bon.common.util.StringUtils;
import com.bon.modules.sys.dao.*;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.*;
import com.bon.modules.sys.domain.enums.PermissionType;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.RoleService;
import com.bon.modules.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: bon-bon基础项目
 * @description: 用户、角色、权限信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final MyLog log = MyLog.getLog(RoleServiceImpl.class);

    @Autowired
    private SysUserExtendMapper userExtendMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    /**
     * 角色
     * @param id
     * @return
     */
    @Override
    public RoleVO getRole(Long id) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        RoleVO vo = new RoleVO();
        BeanUtil.copyPropertys(role, vo);
        //获取id数组(不含父节点)
        List<Long> permissionIds = userExtendMapper.getPermissionIdsByRoleFlag(vo.getRoleFlag());
        vo.setPermissionIds(permissionIds);
        return vo;
    }

    @Override
    public void saveRole(RoleDTO dto) {
        SysRole role = new SysRole();
        role.setRoleId(null);
        role.setGmtCreate(new Date());
        role.setGmtModified(new Date());
        BeanUtil.copyPropertys(dto, role);
        roleMapper.insertSelective(role);
        //保存角色权限
        List<Long> permissionIds = dto.getPermissionIds();
        for (Long permissionId:permissionIds){
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setGmtCreate(new Date());
            rolePermission.setGmtModified(new Date());
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(role.getRoleId());
            rolePermissionMapper.insertSelective(rolePermission);
        }
    }

    @Override
    public void updateRole(RoleDTO dto) {
        SysRole role = roleMapper.selectByPrimaryKey(dto.getRoleId());
        if (role == null) {
            throw new BusinessException("获取角色失败");
        }
        role.setGmtModified(new Date());
        BeanUtil.copyPropertys(dto, role);
        roleMapper.updateByPrimaryKeySelective(role);
        //保存角色权限
        saveRolePermission(dto.getPermissionIds(),dto.getRoleId());
    }

    @Override
    public void deleteRole(Long id) {
        roleMapper.deleteByPrimaryKey(id);
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUserRole(),"roleId",id.toString());
        userRoleMapper.deleteByExample(dto.getExample());
        dto.andFind(new SysRolePermission(),"roleId",id.toString());
        rolePermissionMapper.deleteByExample(dto.getExample());
    }

    @Override
    public PageVO listRole(RoleListDTO listDTO) {
        PageHelper.startPage(listDTO);
        List<SysRole> list = roleMapper.selectByExample(listDTO.createExample());
        PageVO pageVO = new PageVO(list);
        List<RoleVO> voList = new ArrayList<>();
        for (SysRole role : list) {
            RoleVO vo = new RoleVO();
            BeanUtil.copyPropertys(role, vo);
            voList.add(vo);
        }
        pageVO.setList(voList);
        return pageVO;
    }

    @Override
    public List<RoleVO> getAllRole() {
        List<SysRole> list = roleMapper.selectAll();
        List<RoleVO> voList = new ArrayList<>();
        for (SysRole role : list) {
            RoleVO vo = new RoleVO();
            BeanUtil.copyPropertys(role, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void saveRolePermission(List<Long> permissionIds, Long roleId) {
        //删除角色权限
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysRolePermission(), "roleId", roleId + "");
        rolePermissionMapper.deleteByExample(dto.getExample());
        //插入角色权限
        for (Long permissionId : permissionIds) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            rolePermission.setGmtCreate(new Date());
            rolePermission.setGmtModified(new Date());
            rolePermissionMapper.insertSelective(rolePermission);
        }
    }

}
