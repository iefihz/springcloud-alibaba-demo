package com.iefihz.cloud.dao;

import com.iefihz.cloud.entity.SysMenu;
import com.iefihz.cloud.plugins.tkmybatis.BaseMapper;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getMenusByUsername(String username);
}

