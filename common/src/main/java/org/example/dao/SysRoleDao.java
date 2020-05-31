package org.example.dao;

import org.example.entity.SysRoleEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:27
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    //根据创建者id查询其所创建的角色id列表
    List<Long> queryRoleIdList(Long creatorId);
}
