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
public interface RoleService {

    RoleVO getRole(Long id);
    void saveRole(RoleDTO dto);
    void updateRole(RoleDTO dto);
    void deleteRole(Long id);
    PageVO listRole(RoleListDTO dto);
    List<RoleVO> getAllRole();

    /**
     * 保存角色权限
     * @param roleIds
     * @param userId
     */
    void saveRolePermission(List<Long> permissionIds, Long roleId);


}
