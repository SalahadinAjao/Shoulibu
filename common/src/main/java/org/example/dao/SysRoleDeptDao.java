package org.example.dao;

import org.example.entity.SysRoleDeptEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午10:30
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysRoleDeptDao extends BaseDao<SysRoleDeptEntity> {
    List<Long> queryDeptIdList(Long roleId);

    /**
     * 根据用户ID获取权限部门列表
     *
     * @param userId
     * @return
     */
    List<Long> queryDeptIdListByUserId(Long userId);
}
