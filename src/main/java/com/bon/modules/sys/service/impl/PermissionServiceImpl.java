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
import com.bon.modules.sys.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private static final MyLog log = MyLog.getLog(PermissionServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserExtendMapper userExtendMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private SysUrlMapper urlMapper;

    //根据菜单id获取权限
    public SysPermission getPermissionByObjectId(Long objectId, PermissionType permissionType) {
        BaseDTO dto1 = new BaseDTO(new SysPermission());
        dto1.andFind("objectId", objectId.toString());
        dto1.andFind("type", permissionType.getKey());
        SysPermission permission = permissionMapper.selectOneByExample(dto1.getExample());
        return permission;
    }

    @Override
    public BaseVO getPermission(PermissionGetDTO dto) {
        BaseVO vo = new BaseVO();
        Map map = new HashMap();
        map.put("menu", new SysMenu());
        map.put("url", new SysUrl());
        //菜单类型
        if (PermissionType.MENU.getKey().equals(dto.getType())) {
            SysMenu menu = menuMapper.selectByPrimaryKey(dto.getObjectId());
            map.put("menu", menu);
            map.put("permissionType", PermissionType.MENU.getKey());
        } else if (PermissionType.URL.getKey().equals(dto.getType())) {
            //菜单类型
            SysUrl url = urlMapper.selectByPrimaryKey(dto.getObjectId());
            map.put("url", url);
            map.put("permissionType", PermissionType.URL.getKey());
        }
        String permissionFlag = permissionMapper.selectByPrimaryKey(dto.getPermissionId()).getPermissionFlag();
        map.put("permissionFlag", permissionFlag);
        vo.setMap(map);
        return vo;
    }

    @Override
    public Long savePermission(PermissionUpdateDTO dto) {
        SysPermission permission = new SysPermission();
        PermissionType permissionType = null;
        if (PermissionType.MENU.getKey().equals(dto.getType())) {
            //菜单类型
            permissionType = PermissionType.MENU;
            SysMenu menu = dto.getMenu();
            menu.setMenuId(null);
            menu.setGmtCreate(new Date());
            menu.setGmtModified(new Date());
            menuMapper.insertSelective(menu);
            //权限表中新增菜单权限记录
            permission.setPermissionName("【" + permissionType.getValue() + "】" + menu.getName());
            permission.setObjectId(menu.getMenuId());
            savePermissionByType(dto, permissionType, permission);
        } else if (PermissionType.URL.getKey().equals(dto.getType())) {
            //接口url类型
            permissionType = PermissionType.URL;
            SysUrl url = dto.getUrl();
            url.setUrlId(null);
            url.setGmtCreate(new Date());
            url.setGmtModified(new Date());
            urlMapper.insertSelective(url);
            permission.setPermissionName("【" + permissionType.getValue() + "】" + url.getUrlName());
            permission.setObjectId(url.getUrlId());
            savePermissionByType(dto, permissionType, permission);
        }
        return permission.getPermissionId();
    }

    /**
     * 权限表中新增权限记录
     *
     * @param dto
     * @param permissionType
     * @param permission
     */
    private void savePermissionByType(PermissionUpdateDTO dto, PermissionType permissionType, SysPermission permission) {
        BaseDTO baseDTO = new BaseDTO();
        permission.setGmtCreate(new Date());
        permission.setGmtModified(new Date());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setType(permissionType.getKey());
        permissionMapper.insertSelective(permission);
        //添加权限表的数据库id路径,如果不为空则有父节点
        if (dto.getParentId() != null) {
            //根据父id获取权限对应id
            SysPermission permissionParent = permissionMapper.selectByPrimaryKey(dto.getParentId());
            permission.setParentId(permissionParent.getPermissionId());
            permission.setDataPath(permissionParent.getDataPath() + "/" + permission.getPermissionId());
        } else {
            permission.setParentId(0L);
            permission.setDataPath(permission.getPermissionId().toString());
        }
        permissionMapper.updateByPrimaryKey(permission);
        //每次新增权限都添加到管理员角色中
        baseDTO.andFind(new SysRole(), "roleFlag", "admin");
        SysRole adminRole = roleMapper.selectOneByExample(baseDTO.getExample());
        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setGmtCreate(new Date());
        rolePermission.setGmtModified(new Date());
        rolePermission.setRoleId(adminRole.getRoleId());
        rolePermission.setPermissionId(permission.getPermissionId());
        rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public void updatePermission(PermissionUpdateDTO dto) {
        SysPermission permission = new SysPermission();
        PermissionType permissionType = null;
        if (PermissionType.MENU.getKey().equals(dto.getType())) {
            //菜单类型
            permissionType = PermissionType.MENU;
            SysMenu menu = menuMapper.selectByPrimaryKey(dto.getObjectId());
            if (menu == null) {
                throw new BusinessException("获取菜单失败");
            }
            menu.setGmtModified(new Date());
            BeanUtil.copyPropertys(dto.getMenu(), menu);
            menuMapper.updateByPrimaryKeySelective(menu);
            //修改权限对应信息
            permission = getPermissionByObjectId(menu.getMenuId(), permissionType);
            permission.setPermissionName("【" + permissionType.getValue() + "】" + menu.getName());
            permission.setPermissionFlag(dto.getPermissionFlag());
            permission.setGmtModified(new Date());
            permissionMapper.updateByPrimaryKey(permission);
        } else if (PermissionType.URL.getKey().equals(dto.getType())) {
            //接口url类型
            permissionType = PermissionType.URL;
            SysUrl url = urlMapper.selectByPrimaryKey(dto.getObjectId());
            if (null == url) {
                throw new BusinessException("获取接口url信息失败");
            }
            url.setGmtModified(new Date());
            BeanUtil.copyPropertys(dto.getUrl(), url);
            urlMapper.updateByPrimaryKeySelective(url);
            //修改权限对应信息
            permission = getPermissionByObjectId(url.getUrlId(), permissionType);
            permission.setPermissionName("【" + permissionType.getValue() + "】" + url.getUrlName());
            permission.setPermissionFlag(dto.getPermissionFlag());
            permission.setGmtModified(new Date());
            permissionMapper.updateByPrimaryKey(permission);
        }
    }

    @Override
    public void deletePermission(Long id) {
        BaseDTO dto = new BaseDTO();
        SysPermission permission = permissionMapper.selectByPrimaryKey(id);
        dto.likeFind(new SysPermission(), "dataPath", permission.getDataPath() + "/%");
        List<SysPermission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //把子树和自己放进权限数组中，循环删除权限及其子权限
        permissionList.add(permission);
        for (SysPermission permission1 : permissionList) {
            if (PermissionType.MENU.getKey().equals(permission1.getType())) {
                //菜单类型
                permissionMapper.deleteByPrimaryKey(permission1.getPermissionId());
                menuMapper.deleteByPrimaryKey(permission1.getObjectId());
            } else if (PermissionType.URL.getKey().equals(permission1.getType())) {
                //接口url类型
                permissionMapper.deleteByPrimaryKey(permission1.getPermissionId());
                urlMapper.deleteByPrimaryKey(permission1.getObjectId());
            }
            dto.andFind(new SysRolePermission(), "permissionId", permission1.getPermissionId().toString());
            rolePermissionMapper.deleteByExample(dto.getExample());
        }
    }


    @Override
    public List<PermissionVO> getAllPermission() {
        List<PermissionVO> voList = userExtendMapper.getAllPermission();
        return voList;
    }

    @Override
    public List<PermissionTreeVO> getAllPermissionTree() {
        //只查询根节点权限
        BaseDTO dto = new BaseDTO(new SysPermission());
        dto.andFind("parentId", "0");
        List<SysPermission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //通过递归方法获取权限树形结构
        List<PermissionTreeVO> voList = funPermissionChild(permissionList);
        return voList;
    }

    /**
     * 通过递归方法获取权限树形结构
     *
     * @param permissionList
     * @return
     */
    public List<PermissionTreeVO> funPermissionChild(List<SysPermission> permissionList) {
        List<PermissionTreeVO> voList = new ArrayList<>();
        for (SysPermission permission : permissionList) {
            PermissionTreeVO vo = new PermissionTreeVO();
            BeanUtil.copyPropertys(permission, vo);
            BaseDTO dto = new BaseDTO(new SysPermission());
            dto.andFind("parentId", vo.getPermissionId().toString());
            dto.andFind("type", vo.getType());
            List<SysPermission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if (permissionList1.size() > 0) {
                vo.setChildren(funPermissionChild(permissionList1));
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void generateMenuToVue(String tableName, String modules) {
        String parentPermissionFlag = "menu:" + modules + ":main";
        BaseDTO baseDTO = new BaseDTO();
        baseDTO.andFind(new SysPermission(), "permissionFlag", parentPermissionFlag);
        SysPermission parentPermission = permissionMapper.selectOneByExample(baseDTO.getExample());
        if (parentPermission == null) {
            //如果父节点为空则新增父节点
            PermissionUpdateDTO parentDTO = new PermissionUpdateDTO();
            parentDTO.setPermissionFlag(parentPermissionFlag);
            parentDTO.setType(PermissionType.MENU.getKey());
            SysMenu parentMenu = new SysMenu();
            parentMenu.setName(modules + "模块");
            parentMenu.setPath("/" + modules);
            parentMenu.setComponent("/layout/Layout");
            parentMenu.setTitle(modules + "模块");
            parentMenu.setIcon("fa fa-list");
            parentDTO.setMenu(parentMenu);
            Long parentId = savePermission(parentDTO);
            parentPermission = permissionMapper.selectByPrimaryKey(parentId);
        }
        PermissionUpdateDTO dto = new PermissionUpdateDTO();
        dto.setPermissionFlag("menu:" + modules + ":" + tableName);
        dto.setType(PermissionType.MENU.getKey());
        dto.setParentId(parentPermission.getPermissionId());
        SysMenu menu = new SysMenu();
        menu.setName(tableName);
        menu.setPath(tableName);
        menu.setComponent("/" + modules + "/" + StringUtils.upperCase(tableName));
        menu.setTitle(tableName);
        menu.setIcon("fa fa-file-o");
        dto.setMenu(menu);
        savePermission(dto);

    }

}
