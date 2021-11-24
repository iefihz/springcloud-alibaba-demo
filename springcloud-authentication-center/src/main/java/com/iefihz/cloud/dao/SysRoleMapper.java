package com.iefihz.cloud.dao;

import com.iefihz.cloud.entity.SysRole;
import com.iefihz.cloud.plugins.tkmybatis.BaseMapper;

import java.util.List;
import java.util.Set;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> getRolesByUsername(String username);
    Set<Long> selectRoleIdsByUsername(String username);
}
