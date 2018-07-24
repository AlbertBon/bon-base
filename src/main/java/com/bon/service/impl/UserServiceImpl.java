package com.bon.service.impl;


import com.bon.common.dto.BaseDTO;
import com.bon.common.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.common.vo.PageVO;
import com.bon.dao.*;
import com.bon.domain.dto.*;
import com.bon.domain.entity.*;
import com.bon.domain.enums.PermissionType;
import com.bon.domain.vo.*;
import com.bon.service.UserService;
import com.bon.util.BeanUtil;
import com.bon.util.MD5Util;
import com.bon.util.MyLog;
import com.bon.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @program: bon-dubbo
 * @description: 用户信息管理模块
 * @author: Bon
 * @create: 2018-04-27 18:00
 **/
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final MyLog log = MyLog.getLog(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserExtendMapper userExtendMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 用户
     * @param id
     * @return
     */
    @Override
    public UserVO getUser(Long id) {
        Subject subject = SecurityUtils.getSubject();
        User user = userMapper.selectByPrimaryKey(id);
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
    public User getUserByUsername(String username) {
        BaseDTO dto = new BaseDTO();
        dto.andFind(new User(),"username",username);

        return userMapper.selectOneByExample(dto.getExample());
    }

    @Override
    public void saveUser(UserDTO dto) {
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new BusinessException(ExceptionType.PASSWORD_NULL_ERROR);
        }

        dto.andFind("username", dto.getUsername());
        List<User> userList = userMapper.selectByExample(dto.getExample());
        if (userList.size() > 0) {
            throw new BusinessException("用户名重复");
        }

        dto.setPassword(MD5Util.encode(dto.getPassword()));
        User user = new User();
        BeanUtil.copyPropertys(dto, user);
        user.setUserId(null);
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        userMapper.insertSelective(user);
        //保存用户角色
        saveUserRole(dto.getRoleIds(), user.getUserId());
    }

    @Override
    public void updateUser(UserDTO dto) {
        if (StringUtils.isNotBlank(dto.getPassword())) {
            dto.setPassword(MD5Util.encode(dto.getPassword()));
        } else {
            dto.setPassword(null);
        }

        User user = userMapper.selectByPrimaryKey(dto.getUserId());
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
        dto.andFind(new UserRole(),"userId",id+"");
        userRoleMapper.deleteByExample(dto.getExample());
    }

    @Override
    public PageVO listUser(UserListDTO userListDTO) {
        PageHelper.startPage(userListDTO);
        List<User> list = userMapper.selectByExample(userListDTO.createExample());
        PageVO pageVO = new PageVO(list);
        List<UserVO> voList = new ArrayList<>();
        for (User user : list) {
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
        List<User> list = userMapper.selectAll();
        List<UserVO> voList = new ArrayList<>();
        for (User user : list) {
            UserVO vo = new UserVO();
            BeanUtil.copyPropertys(user, vo);
            //放入用户角色id列表信息
            vo.setRoleIds(getUserRoleIds(user.getUserId()));
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 角色
     * @param id
     * @return
     */

    @Override
    public RoleVO getRole(Long id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        RoleVO vo = new RoleVO();
        BeanUtil.copyPropertys(role, vo);
        //获取角色权限id集合
        List<Long> permissionIds = new ArrayList<>();
        List<Permission> permissionList = userExtendMapper.getPermissionByRoleFlag(vo.getRoleFlag());
        for(Permission permission: permissionList){
            permissionIds.add(permission.getPermissionId());
        }
        vo.setPermissionIds(permissionIds);
        return vo;
    }

    @Override
    public void saveRole(RoleDTO dto) {
        Role role = new Role();
        role.setRoleId(null);
        role.setGmtCreate(new Date());
        role.setGmtModified(new Date());
        BeanUtil.copyPropertys(dto, role);
        roleMapper.insertSelective(role);
        //保存角色权限
        List<Long> permissionIds = dto.getPermissionIds();
        for (Long permissionId:permissionIds){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setGmtCreate(new Date());
            rolePermission.setGmtModified(new Date());
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(role.getRoleId());
            rolePermissionMapper.insertSelective(rolePermission);
        }
    }

    @Override
    public void updateRole(RoleDTO dto) {
        Role role = roleMapper.selectByPrimaryKey(dto.getRoleId());
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
    }

    @Override
    public PageVO listRole(RoleListDTO listDTO) {
        PageHelper.startPage(listDTO);
        List<Role> list = roleMapper.selectByExample(listDTO.createExample());
        PageVO pageVO = new PageVO(list);
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : list) {
            RoleVO vo = new RoleVO();
            BeanUtil.copyPropertys(role, vo);
            voList.add(vo);
        }
        pageVO.setList(voList);
        return pageVO;
    }

    @Override
    public List<RoleVO> getAllRole() {
        List<Role> list = roleMapper.selectAll();
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : list) {
            RoleVO vo = new RoleVO();
            BeanUtil.copyPropertys(role, vo);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 菜单
     * @param id
     * @return
     */

    @Override
    public MenuVO getMenu(Long id) {
        MenuVO vo = new MenuVO();
        Menu menu = menuMapper.selectByPrimaryKey(id);
        BeanUtil.copyPropertys(menu,vo);
        //获取父菜单名称
        Menu parentMenu = menuMapper.selectByPrimaryKey(vo.getParent());
        if(null!=parentMenu){
            vo.setParentName(parentMenu.getName());
        }
        //获取权限标识
        String permissionFlag = getPermissionByMenuId(id).getPermissionFlag();
        vo.setPermissionFlag(permissionFlag);
        return vo;
    }

    //根据菜单id获取权限
    public Permission getPermissionByMenuId(Long menuId){
        BaseDTO dto1 = new BaseDTO(new Permission());
        dto1.andFind("objectId",menuId.toString());
        dto1.andFind("type",PermissionType.MENU.getKey());
        Permission permission = permissionMapper.selectOneByExample(dto1.getExample());
        return permission;
    }

    @Override
    public void saveMenu(MenuDTO dto) {
        Menu menu = new Menu();
        BeanUtil.copyPropertys(dto, menu);
        menu.setMenuId(null);
        menu.setGmtCreate(new Date());
        menu.setGmtModified(new Date());
        menuMapper.insertSelective(menu);
        //添加数据库id路径,如果不为空则有父节点
        if (dto.getMenuId() != null) {
            Menu m = menuMapper.selectByPrimaryKey(dto.getMenuId());
            menu.setDataPath(m.getDataPath() + "/" + menu.getMenuId());
            menu.setParent(m.getMenuId());
        } else {
            menu.setDataPath(menu.getMenuId()+"");
            menu.setParent(0L);
        }
        menuMapper.updateByPrimaryKey(menu);
        //权限表中新增菜单权限记录
        Permission permission = new Permission();
        permission.setGmtCreate(new Date());
        permission.setGmtModified(new Date());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setPermissionName("【" + PermissionType.MENU.getValue() + "】" + menu.getName());
        permission.setType(PermissionType.MENU.getKey());
        permission.setObjectId(menu.getMenuId());
        permissionMapper.insertSelective(permission);
    }

    @Override
    public void updateMenu(MenuDTO dto) {
        Menu menu = menuMapper.selectByPrimaryKey(dto.getMenuId());
        if (menu == null) {
            throw new BusinessException("获取菜单失败");
        }
        menu.setGmtModified(new Date());
        BeanUtil.copyPropertys(dto, menu);
        menuMapper.updateByPrimaryKeySelective(menu);
        //修改权限对应信息
        Permission permission = getPermissionByMenuId(menu.getMenuId());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setGmtModified(new Date());
        permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public void deleteMenu(Long menuId) {
        menuMapper.deleteByPrimaryKey(menuId);
        //删除权限表对应记录
        BaseDTO dto = new BaseDTO();
        dto.createExample(new Permission());
        dto.andFind("type", PermissionType.MENU.getKey());
        dto.andFind("objectId", String.valueOf(menuId));
        permissionMapper.deleteByExample(dto.getExample());
    }

    @Override
    public PageVO listMenu(MenuListDTO dto) {
        PageHelper.startPage(dto);
        List<Menu> list = menuMapper.selectByExample(dto.createExample());
        PageVO pageVO = new PageVO(list);
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : list) {
            MenuVO vo = new MenuVO();
            BeanUtil.copyPropertys(menu, vo);
            if(menu.getParent()!=0){
                vo.setParentName(menuMapper.selectByPrimaryKey(menu.getParent()).getName());
            }
            voList.add(vo);
        }
        pageVO.setList(voList);
        return pageVO;
    }

    @Override
    public List<MenuVO> getAllMenu() {
        //此处sql查询会去除根菜单
        List<MenuVO> voList = userExtendMapper.getAllMenu();
        return voList;
    }

    @Override
    public List<PermissionVO> getAllPermission() {
        //此处sql查询会去除根菜单
        List<PermissionVO> voList = userExtendMapper.getAllPermission();
        return voList;
    }

    @Override
    public void saveUserRole(List<Long> roleIds, Long userId) {
        //删除用户角色
        BaseDTO<UserRole> dto = new BaseDTO();
        dto.andFind(new UserRole(), "userId", userId + "");
        userRoleMapper.deleteByExample(dto.getExample());
        //插入角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
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
        dto.andFind(new UserRole(), "userId", userId + "");
        List<UserRole> userRoleList = userRoleMapper.selectByExample(dto.getExample());
        List<Long> voList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            voList.add(userRole.getRoleId());
        }
        return voList;
    }

    @Override
    public void saveRolePermission(List<Long> permissionIds, Long roleId) {
        //删除角色权限
        BaseDTO dto = new BaseDTO();
        dto.andFind(new RolePermission(), "roleId", roleId + "");
        rolePermissionMapper.deleteByExample(dto.getExample());
        //插入角色权限
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            rolePermission.setGmtCreate(new Date());
            rolePermission.setGmtModified(new Date());
            rolePermissionMapper.insertSelective(rolePermission);
        }
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        //查找角色所有权限
        BaseDTO dto = new BaseDTO();
        dto.andFind(new RolePermission(), "roleId", roleId + "");
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectByExample(dto.getExample());
        List<Long> voList = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissionList) {
            voList.add(rolePermission.getRoleId());
        }
        return voList;
    }

    @Override
    public List<Role> getRoleByUsername(String username) {
        return userExtendMapper.getRoleByUsername(username);
    }

    @Override
    public List<Permission> getPermissionByRoleFlag(String roleFlag) {
        return userExtendMapper.getPermissionByRoleFlag(roleFlag);
    }

    @Override
    public List<MenuRouterVO> getMenuRouter(String username) {
        List<Menu> menuList = userExtendMapper.getMenuByUsername(username);
        List<MenuRouterVO> voList = funMenuChild(menuList);
        return voList;
    }


    /**
     * @Author: Bon
     * @Description: 递归获取子菜单，组装成菜单
     * @param menuList
     * @return: java.util.List<com.bon.domain.vo.MenuRouterVO>
     * @Date: 2018/6/6 15:04
     */
    public List<MenuRouterVO> funMenuChild(List<Menu> menuList) {
        List<MenuRouterVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            String permissionFlag = getPermissionByMenuId(menu.getMenuId()).getPermissionFlag();
            //判断是否有权限
            if(!SecurityUtils.getSubject().isPermitted(permissionFlag)){
                continue;
            }
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
            dto.likeFind(new Menu(), "dataPath", menu.getDataPath() + "/%");
            List<Menu> menuList1 = menuMapper.selectByExample(dto.getExample());
            if (menuList1.size() > 0) {
                vo.setChildren(funMenuChild(menuList1));
            }
            voList.add(vo);
        }
        return voList;
    }


}
