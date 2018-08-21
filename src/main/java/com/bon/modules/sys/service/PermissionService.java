package com.bon.modules.sys.service;


import com.bon.common.domain.vo.PageVO;
import com.bon.modules.sys.domain.dto.*;
import com.bon.modules.sys.domain.entity.SysUser;
import com.bon.modules.sys.domain.vo.*;

import java.util.List;

/**
 * @program: bon-bon基础项目
 * @description: 用户、角色、权限管理模块
 * @author: Bon
 * @create: 2018-04-27 17:47
 **/
public interface PermissionService {

    BaseVO getPermission(PermissionGetDTO dto);
    Long savePermission(PermissionUpdateDTO dto);
    void updatePermission(PermissionUpdateDTO dto);
    void deletePermission(Long id);

    /**
     * 获取所有权限
     * @return
     */
    List<PermissionVO> getAllPermission();

    /**
     * 获取所有权限
     * @return
     */
    List<PermissionTreeVO> getAllPermissionTree();

    void generateMenuToVue(String tableName,String modules);

}
