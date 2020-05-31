package org.example.service;

import org.example.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:29
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysRoleService {

    SysRoleEntity queryObject(Long roleId);

    List<SysRoleEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);

    List<Long> queryRoleIdList(Long creatorId);
}
