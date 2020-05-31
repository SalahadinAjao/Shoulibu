package org.example.service.impl;

import org.example.entity.SysRoleEntity;
import org.example.service.SysRoleService;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:32
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysRoleServiceImpl implements SysRoleService {
    @Override
    public SysRoleEntity queryObject(Long roleId) {
        return null;
    }

    @Override
    public List<SysRoleEntity> queryList(Map<String, Object> map) {
        return null;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return 0;
    }

    @Override
    public void save(SysRoleEntity role) {

    }

    @Override
    public void update(SysRoleEntity role) {

    }

    @Override
    public void deleteBatch(Long[] roleIds) {

    }

    @Override
    public List<Long> queryRoleIdList(Long creatorId) {
        return null;
    }
}
