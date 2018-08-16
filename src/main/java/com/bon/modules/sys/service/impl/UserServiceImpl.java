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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: bon-bon基础项目
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
        User user = new User();
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
        User user = userMapper.selectByPrimaryKey(dto.getUserId());
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
        dto.andFind(new UserRole(),"userId",id.toString());
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
        //获取角色权限集合
        List<Permission> permissionList = userExtendMapper.getPermissionByRoleFlag(vo.getRoleFlag());
        //获取id数组(不含父节点)
        List<Long> permissionIds = funPermissionIds(permissionList);
        vo.setPermissionIds(permissionIds);
        return vo;
    }
    /**
     * 通过递归方法获取权限子节点权限id（因前端不可包含有子节点的父节点id）
     * @param permissionList
     * @return
     */
    public List<Long> funPermissionIds(List<Permission> permissionList) {
        List<Long> permissionIds = new ArrayList<>();
        for(Permission permission:permissionList){
            BaseDTO dto = new BaseDTO(new Permission());
            dto.andFind("objectParent",permission.getObjectId().toString());
            dto.andFind("type",permission.getType());
            List<Permission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if(permissionList1.size() <= 0){
                permissionIds.add(permission.getPermissionId());
            }
        }
        return permissionIds;
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
        BaseDTO dto = new BaseDTO();
        dto.andFind(new UserRole(),"roleId",id.toString());
        userRoleMapper.deleteByExample(dto.getExample());
        dto.andFind(new RolePermission(),"roleId",id.toString());
        rolePermissionMapper.deleteByExample(dto.getExample());
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

    //根据菜单id获取权限
    public Permission getPermissionByMenuId(Long menuId){
        BaseDTO dto1 = new BaseDTO(new Permission());
        dto1.andFind("objectId",menuId.toString());
        dto1.andFind("type",PermissionType.MENU.getKey());
        Permission permission = permissionMapper.selectOneByExample(dto1.getExample());
        return permission;
    }

    @Override
    public BaseVO getPermission(PermissionGetDTO dto) {
        BaseVO vo = new BaseVO();
        Map map = new HashMap();
        //菜单类型
        if(PermissionType.MENU.getKey().equals(dto.getType())){
            Menu menu = menuMapper.selectByPrimaryKey(dto.getObjectId());
            map = MapUtil.objectToMap(menu);
            String permissionFlag = permissionMapper.selectByPrimaryKey(dto.getPermissionId()).getPermissionFlag();
            map.put("permissionFlag",permissionFlag);
        }
        vo.setMap(map);
        return vo;
    }

    @Override
    public void savePermission(PermissionUpdateDTO dto) {
        //菜单类型
        if(PermissionType.MENU.getKey().equals(dto.getType())){
            saveMenu(dto);
        }
    }

    @Override
    public void updatePermission(PermissionUpdateDTO dto) {
        //菜单类型
        if(PermissionType.MENU.getKey().equals(dto.getType())){
            updateMenu(dto);
        }
    }

    @Override
    public void deletePermission(Long id) {
        BaseDTO dto = new BaseDTO();
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        dto.likeFind(new Permission(),"dataPath",permission.getDataPath()+"/%");
        List<Permission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //把子树和自己放进权限数组中，循环删除权限及其子权限
        permissionList.add(permission);
        for(Permission permission1 :permissionList){
            //菜单类型
            if(PermissionType.MENU.getKey().equals(permission1.getType())){
                permissionMapper.deleteByPrimaryKey(permission1.getPermissionId());
                menuMapper.deleteByPrimaryKey(permission1.getObjectId());
            }
            dto.andFind(new RolePermission(),"permissionId",permission1.getPermissionId().toString());
            rolePermissionMapper.deleteByExample(dto.getExample());
        }
    }

    private void saveMenu(PermissionUpdateDTO dto) {
        Menu menu = new Menu();
        BeanUtil.copyPropertys(dto, menu);
        menu.setMenuId(null);
        menu.setGmtCreate(new Date());
        menu.setGmtModified(new Date());
        menuMapper.insertSelective(menu);
        menuMapper.updateByPrimaryKey(menu);
        //权限表中新增菜单权限记录
        Permission permission = new Permission();
        permission.setGmtCreate(new Date());
        permission.setGmtModified(new Date());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setPermissionName("【" + PermissionType.MENU.getValue() + "】" + menu.getName());
        permission.setType(PermissionType.MENU.getKey());
        permission.setObjectId(menu.getMenuId());
        permission.setObjectParent(dto.getObjectId());
        permissionMapper.insertSelective(permission);
        //添加权限表的数据库id路径,如果不为空则有父节点
        if (dto.getObjectId()!=null) {
            BaseDTO baseDTO = new BaseDTO();
            baseDTO.andFind(new Permission(),"objectId",dto.getObjectId().toString());
            baseDTO.andFind("type",PermissionType.MENU.getKey());
            Permission permissionParent = permissionMapper.selectOneByExample(baseDTO.getExample());
            permission.setDataPath(permissionParent.getDataPath()+ "/" + permission.getPermissionId());
        } else {
            permission.setObjectParent(0L);
            permission.setDataPath(permission.getPermissionId().toString());
        }
        permissionMapper.updateByPrimaryKey(permission);
    }

    private void updateMenu(PermissionUpdateDTO dto) {
        Menu menu = menuMapper.selectByPrimaryKey(dto.getObjectId());
        if (menu == null) {
            throw new BusinessException("获取菜单失败");
        }
        menu.setGmtModified(new Date());
        BeanUtil.copyPropertys(dto, menu);
        menuMapper.updateByPrimaryKeySelective(menu);
        //修改权限对应信息
        Permission permission = getPermissionByMenuId(menu.getMenuId());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setPermissionName("【" + PermissionType.MENU.getValue() + "】" + menu.getName());
        permission.setGmtModified(new Date());
        permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public List<PermissionVO> getAllPermission() {
        List<PermissionVO> voList = userExtendMapper.getAllPermission();
        return voList;
    }

    @Override
    public List<PermissionTreeVO> getAllPermissionTree() {
        //只查询根节点权限
        BaseDTO dto = new BaseDTO(new Permission());
        dto.andFind("objectParent","0");
        List<Permission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //通过递归方法获取权限树形结构
        List<PermissionTreeVO> voList = funPermissionChild(permissionList);
        return voList;
    }

    /**
     * 通过递归方法获取权限树形结构
     * @param permissionList
     * @return
     */
    public List<PermissionTreeVO> funPermissionChild(List<Permission> permissionList) {
        List<PermissionTreeVO> voList = new ArrayList<>();
        for (Permission permission : permissionList) {
            PermissionTreeVO vo = new PermissionTreeVO();
            BeanUtil.copyPropertys(permission,vo);
            BaseDTO dto = new BaseDTO(new Permission());
            dto.andFind("objectParent",vo.getObjectId().toString());
            dto.andFind("type",vo.getType());
            List<Permission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if(permissionList1.size() > 0){
                vo.setChildren(funPermissionChild(permissionList1));
            }
            voList.add(vo);
        }
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
    public List<Role> getRoleByUsername(String username) {
        return userExtendMapper.getRoleByUsername(username);
    }

    @Override
    public List<Permission> getPermissionByRoleFlag(String roleFlag) {
        return userExtendMapper.getPermissionByRoleFlag(roleFlag);
    }

    @Override
    public List<MenuRouterVO> getMenuRouter(String username) {
        //只查询根节点菜单
        List<Permission> permissionList = userExtendMapper.getMenuByUsername(username);
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
    public List<MenuRouterVO> funMenuChild(List<Permission> permissionList) {
        List<MenuRouterVO> voList = new ArrayList<>();
        for (Permission permission : permissionList) {
            String permissionFlag = permission.getPermissionFlag();
            //判断是否有权限
            if(!SecurityUtils.getSubject().isPermitted(permissionFlag)){
                continue;
            }
            Menu menu = menuMapper.selectByPrimaryKey(permission.getObjectId());
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
            dto.likeFind(new Permission(), "dataPath", permission.getDataPath() + "/%");
            dto.andFind("type",PermissionType.MENU.getKey());
            List<Permission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if (permissionList1.size() > 0) {
                vo.setChildren(funMenuChild(permissionList1));
            }
            voList.add(vo);
        }
        return voList;
    }


}
