package org.example.dao;

import org.example.entity.SysUserRoleEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/1 上午6:06
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysUserRoleDao extends BaseDao<SysUserRoleEntity> {

    //根据用户id查询此id下的所有相关roleId列表
    List<Long> queryRoleIdList(Long userId);
}
