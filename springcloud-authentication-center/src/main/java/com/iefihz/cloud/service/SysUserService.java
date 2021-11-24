package com.iefihz.cloud.service;

import com.iefihz.cloud.entity.SysMenu;
import com.iefihz.cloud.entity.SysRole;
import com.iefihz.cloud.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface SysUserService extends UserDetailsService {

    /**
     * 用户名获取用户
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    /**
     * 是否为管理员
     * @param username
     * @return
     */
    Boolean isAdmin(String username);

    /**
     * 获取用户的角色
     * @param username
     * @return
     */
    List<SysRole> getRolesByUsername(String username);

    /**
     * 获取用户的菜单权限
     * @param username
     * @return
     */
    List<SysMenu> getMenusByUsername(String username);

}
