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
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private SysUrlMapper urlMapper;

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
        //获取角色权限集合
        List<SysPermission> permissionList = userExtendMapper.getPermissionByRoleFlag(vo.getRoleFlag());
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
    public List<Long> funPermissionIds(List<SysPermission> permissionList) {
        List<Long> permissionIds = new ArrayList<>();
        for(SysPermission permission:permissionList){
            BaseDTO dto = new BaseDTO(new SysPermission());
            dto.andFind("objectParent",permission.getObjectId().toString());
            dto.andFind("type",permission.getType());
            List<SysPermission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if(permissionList1.size() <= 0){
                permissionIds.add(permission.getPermissionId());
            }
        }
        return permissionIds;
    }

    @Override
    @Transactional
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

    //根据菜单id获取权限
    public SysPermission getPermissionByObjectId(Long objectId,PermissionType permissionType){
        BaseDTO dto1 = new BaseDTO(new SysPermission());
        dto1.andFind("objectId",objectId.toString());
        dto1.andFind("type",permissionType.getKey());
        SysPermission permission = permissionMapper.selectOneByExample(dto1.getExample());
        return permission;
    }

    @Override
    public BaseVO getPermission(PermissionGetDTO dto) {
        BaseVO vo = new BaseVO();
        Map map = new HashMap();
        map.put("menu",new SysMenu());
        map.put("url",new SysUrl());
        //菜单类型
        if(PermissionType.MENU.getKey().equals(dto.getType())){
            SysMenu menu = menuMapper.selectByPrimaryKey(dto.getObjectId());
            map.put("menu",menu);
            map.put("permissionType",PermissionType.MENU.getKey());
        }else if(PermissionType.URL.getKey().equals(dto.getType())){
            //菜单类型
            SysUrl url = urlMapper.selectByPrimaryKey(dto.getObjectId());
            map.put("url",url);
            map.put("permissionType",PermissionType.URL.getKey());
        }
        String permissionFlag = permissionMapper.selectByPrimaryKey(dto.getPermissionId()).getPermissionFlag();
        map.put("permissionFlag",permissionFlag);
        vo.setMap(map);
        return vo;
    }

    @Override
    public Long savePermission(PermissionUpdateDTO dto) {
        SysPermission permission = new SysPermission();
        PermissionType permissionType = null;
        if(PermissionType.MENU.getKey().equals(dto.getType())){
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
            savePermissionByType(dto,permissionType,permission);
        }else if(PermissionType.URL.getKey().equals(dto.getType())){
            //接口url类型
            permissionType = PermissionType.URL;
            SysUrl url = dto.getUrl();
            url.setUrlId(null);
            url.setGmtCreate(new Date());
            url.setGmtModified(new Date());
            urlMapper.insertSelective(url);
            permission.setPermissionName("【" + permissionType.getValue() + "】" + url.getUrlName());
            permission.setObjectId(url.getUrlId());
            savePermissionByType(dto,permissionType,permission);
        }
        return permission.getPermissionId();
    }

    /**
     * 权限表中新增菜单权限记录
     * @param dto
     * @param permissionType
     * @param permission
     */
    private void savePermissionByType(PermissionUpdateDTO dto,PermissionType permissionType,SysPermission permission) {
        permission.setGmtCreate(new Date());
        permission.setGmtModified(new Date());
        permission.setPermissionFlag(dto.getPermissionFlag());
        permission.setType(permissionType.getKey());
        permission.setObjectParent(dto.getObjectId());
        permissionMapper.insertSelective(permission);
        //添加权限表的数据库id路径,如果不为空则有父节点
        BaseDTO baseDTO = new BaseDTO();
        if (dto.getObjectId()!=null) {
            baseDTO.andFind(new SysPermission(),"objectId",dto.getObjectId().toString());
            baseDTO.andFind("type",permissionType.getKey());
            SysPermission permissionParent = permissionMapper.selectOneByExample(baseDTO.getExample());
            permission.setDataPath(permissionParent.getDataPath()+ "/" + permission.getPermissionId());
        } else {
            permission.setObjectParent(0L);
            permission.setDataPath(permission.getPermissionId().toString());
        }
        permissionMapper.updateByPrimaryKey(permission);
        //每次新增权限都添加到管理员角色中
        baseDTO.andFind(new SysRole(),"roleFlag","admin");
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
        if(PermissionType.MENU.getKey().equals(dto.getType())){
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
            permission = getPermissionByObjectId(menu.getMenuId(),permissionType);
            permission.setPermissionName("【" + permissionType.getValue() + "】" + menu.getName());
            permission.setPermissionFlag(dto.getPermissionFlag());
            permission.setGmtModified(new Date());
            permissionMapper.updateByPrimaryKey(permission);
        }else if(PermissionType.URL.getKey().equals(dto.getType())){
            //接口url类型
            permissionType = PermissionType.URL;
            SysUrl url = urlMapper.selectByPrimaryKey(dto.getObjectId());
            if(null == url){
                throw new BusinessException("获取接口url信息失败");
            }
            url.setGmtModified(new Date());
            BeanUtil.copyPropertys(dto.getUrl(),url);
            urlMapper.updateByPrimaryKeySelective(url);
            //修改权限对应信息
            permission = getPermissionByObjectId(url.getUrlId(),permissionType);
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
        dto.likeFind(new SysPermission(),"dataPath",permission.getDataPath()+"/%");
        List<SysPermission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //把子树和自己放进权限数组中，循环删除权限及其子权限
        permissionList.add(permission);
        for(SysPermission permission1 :permissionList){
            if(PermissionType.MENU.getKey().equals(permission1.getType())){
                //菜单类型
                permissionMapper.deleteByPrimaryKey(permission1.getPermissionId());
                menuMapper.deleteByPrimaryKey(permission1.getObjectId());
            }else if(PermissionType.URL.getKey().equals(permission1.getType())){
                //接口url类型
                permissionMapper.deleteByPrimaryKey(permission1.getPermissionId());
                urlMapper.deleteByPrimaryKey(permission1.getObjectId());
            }
            dto.andFind(new SysRolePermission(),"permissionId",permission1.getPermissionId().toString());
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
        dto.andFind("objectParent","0");
        List<SysPermission> permissionList = permissionMapper.selectByExample(dto.getExample());
        //通过递归方法获取权限树形结构
        List<PermissionTreeVO> voList = funPermissionChild(permissionList);
        return voList;
    }

    /**
     * 通过递归方法获取权限树形结构
     * @param permissionList
     * @return
     */
    public List<PermissionTreeVO> funPermissionChild(List<SysPermission> permissionList) {
        List<PermissionTreeVO> voList = new ArrayList<>();
        for (SysPermission permission : permissionList) {
            PermissionTreeVO vo = new PermissionTreeVO();
            BeanUtil.copyPropertys(permission,vo);
            BaseDTO dto = new BaseDTO(new SysPermission());
            dto.andFind("objectParent",vo.getObjectId().toString());
            dto.andFind("type",vo.getType());
            List<SysPermission> permissionList1 = permissionMapper.selectByExample(dto.getExample());
            if(permissionList1.size() > 0){
                vo.setChildren(funPermissionChild(permissionList1));
            }
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
        for (SysPermission permission : permissionList) {
            String permissionFlag = permission.getPermissionFlag();
            //判断是否有权限
            if(!SecurityUtils.getSubject().isPermitted(permissionFlag) && !StringUtils.isByteTrue(user.getIsAdmin())){
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
            dto.likeFind(new SysPermission(), "dataPath", permission.getDataPath() + "/%");
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