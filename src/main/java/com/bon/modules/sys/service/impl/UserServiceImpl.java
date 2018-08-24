package com.bon.modules.sys.service.impl;


import com.bon.common.domain.dto.BaseDTO;
import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.common.domain.vo.PageVO;
import com.bon.modules.sys.dao.*;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.*;
import com.bon.modules.sys.domain.enums.PermissionType;
import com.bon.modules.sys.domain.vo.*;
import com.bon.modules.sys.service.UserService;
import com.bon.common.util.*;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.javassist.runtime.DotClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @program: bon-bon基础项目
 * @description: 用户、角色、权限信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final MyLog log = MyLog.getLog(UserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserExtendMapper userExtendMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    /**
     * 用户
     * @param id
     * @return
     */
    @Override
    public UserVO getUser(Long id) {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new BusinessException(ExceptionType.USERNAME_NULL_PASSWORD_ERROR);
        }
        UserVO vo = new UserVO();
        vo = BeanUtil.copyPropertys(user, vo);
//        //放入用户角色id列表信息
        vo.setRoleIds(getUserRoleIds(user.getUserId()));
        return vo;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUser(),"username",username);

        return userMapper.selectOneByExample(dto.getExample());
    }

    @Override
    public void saveUser(UserDTO dto) {
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new BusinessException(ExceptionType.PASSWORD_NULL_ERROR);
        }

        dto.andFind("username", dto.getUsername());
        List<SysUser> userList = userMapper.selectByExample(dto.getExample());
        if (userList.size() > 0) {
            throw new BusinessException("用户名重复");
        }
        SysUser user = new SysUser();
        BeanUtil.copyPropertys(dto, user);
        user.setUserId(null);
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        //随机生成盐
        String salt = UUID.randomUUID().toString().replace("-", "");
        user.setPassword(ShiroUtil.md5encode(dto.getPassword(), salt));
        user.setSalt(salt);
        userMapper.insertSelective(user);
        //保存用户角色
        saveUserRole(dto.getRoleIds(), user.getUserId());
    }

    @Override
    public void updateUser(UserDTO dto) {
        SysUser user = userMapper.selectByPrimaryKey(dto.getUserId());
        if (StringUtils.isNotBlank(dto.getPassword())) {
            dto.setPassword(ShiroUtil.md5encode(dto.getPassword(),user.getSalt()));
        } else {
            dto.setPassword(null);
        }
        if (user == null) {
            throw new BusinessException(ExceptionType.USERNAME_NULL_PASSWORD_ERROR);
        }
        user = BeanUtil.copyPropertys(dto, user);
        user.setGmtModified(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        //保存用户角色
        saveUserRole(dto.getRoleIds(), user.getUserId());
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteByPrimaryKey(id);
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUserRole(),"userId",id.toString());
        userRoleMapper.deleteByExample(dto.getExample());
    }

    @Override
    public PageVO listUser(UserListDTO userListDTO) {
        PageHelper.startPage(userListDTO);
        List<SysUser> list = userMapper.selectByExample(userListDTO.createExample());
        PageVO pageVO = new PageVO(list);
        List<UserVO> voList = new ArrayList<>();
        for (SysUser user : list) {
            UserVO vo = new UserVO();
            BeanUtil.copyPropertys(user, vo);
            //放入用户角色id列表信息
            vo.setRoleIds(getUserRoleIds(user.getUserId()));
            voList.add(vo);
        }
        pageVO.setList(voList);
        return pageVO;
    }

    @Override
    public List<UserVO> getAllUser() {
        List<SysUser> list = userMapper.selectAll();
        List<UserVO> voList = new ArrayList<>();
        for (SysUser user : list) {
            UserVO vo = new UserVO();
            BeanUtil.copyPropertys(user, vo);
            //放入用户角色id列表信息
            vo.setRoleIds(getUserRoleIds(user.getUserId()));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<MenuRouterVO> getMenuRouter(String username) {
        //只查询根节点菜单
        List<SysPermission> permissionList = userExtendMapper.getMenuByUsername(username);
        List<MenuRouterVO> voList = funMenuChild(permissionList);
        return voList;
    }

    /**
     * @Author: Bon
     * @Description: 递归获取子菜单，组装成菜单
     * @param menuList
     * @return: java.util.List<com.bon.domain.vo.MenuRouterVO>
     * @Date: 2018/6/6 15:04
     */
    public List<MenuRouterVO> funMenuChild(List<SysPermission> permissionList) {
        SysUser user = userExtendMapper.getByUsername(SecurityUtils.getSubject().getPrincipal().toString());
        List<MenuRouterVO> voList = new ArrayList<>();
        Collections.sort(permissionList);//对列表进行排序
        for (SysPermission permission : permissionList) {
            String permissionFlag = permission.getPermissionFlag();
            //判断是否有权限
            if(!SecurityUtils.getSubject().isPermitted(permissionFlag)){
                continue;
            }
            SysMenu menu = menuMapper.selectByPrimaryKey(permission.getObjectId());
            //转化为路由菜单
            MenuRouterVO vo = new MenuRouterVO();
            MenuRouterVO.Meta meta = vo.new Meta();
            vo.setAlwaysShow(menu.getAlwaysShow());
            vo.setComponent(menu.getComponent());
            vo.setHidden(menu.getHidden());
            vo.setName(menu.getName());
            vo.setPath(menu.getPath());
            vo.setRedirect(menu.getRedirect());
            meta.setIcon(menu.getIcon());
            meta.setTitle(menu.getTitle());
            vo.setMeta(meta);

            //判断如果还有子菜单就递归调用继续查找子菜单
            BaseDTO dto = new BaseDTO();
            dto.andFind(new SysPermission(), "parentId", permission.getPermissionId().toString());
            dto.andFind("type",PermissionType.MENU.getKey());
            List<SysPermission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if (permissionList1.size() > 0) {
                vo.setChildren(funMenuChild(permissionList1));
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void saveUserRole(List<Long> roleIds, Long userId) {
        //删除用户角色
        BaseDTO<SysUserRole> dto = new BaseDTO();
        dto.andFind(new SysUserRole(), "userId", userId + "");
        userRoleMapper.deleteByExample(dto.getExample());
        //插入角色
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setGmtCreate(new Date());
            userRole.setGmtModified(new Date());
            userRoleMapper.insertSelective(userRole);
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        //查找用户所有角色
        BaseDTO dto = new BaseDTO();
        dto.andFind(new SysUserRole(), "userId", userId + "");
        List<SysUserRole> userRoleList = userRoleMapper.selectByExample(dto.getExample());
        List<Long> voList = new ArrayList<>();
        for (SysUserRole userRole : userRoleList) {
            voList.add(userRole.getRoleId());
        }
        return voList;
    }

}
