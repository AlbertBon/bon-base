package com.bon.service.impl;


import com.bon.common.dto.BaseDTO;
import com.bon.common.enums.ExceptionType;
import com.bon.common.exception.BusinessException;
import com.bon.dao.MenuMapper;
import com.bon.dao.UserExtendMapper;
import com.bon.dao.UserMapper;
import com.bon.domain.entity.Menu;
import com.bon.domain.entity.Permission;
import com.bon.domain.entity.Role;
import com.bon.domain.entity.User;
import com.bon.domain.vo.MenuRouterVO;
import com.bon.domain.vo.UserVO;
import com.bon.service.UserService;
import com.bon.util.BeanUtil;
import com.bon.util.MyLog;
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
    private MenuMapper menuMapper;

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
//        vo.setRoleIds(getUserRoleIds(user.getUserId()));
        return vo;
    }

    @Override
    public User getUserByUsername(String username) {
        BaseDTO dto = new BaseDTO();
        dto.andFind(new User(),"username",username);

        return userMapper.selectOneByExample(dto.getExample());
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
     * @return: java.util.List<com.bon.wx.domain.vo.MenuRouterVO>
     * @Date: 2018/6/6 15:04
     */
    public List<MenuRouterVO> funMenuChild(List<Menu> menuList) {
        List<MenuRouterVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
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
