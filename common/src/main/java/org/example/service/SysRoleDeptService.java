package org.example.service;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午10:27
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysRoleDeptService {

    void saveOrUpdate(Long roleId, List<Long> deptIdList);

    /**
     * 根据角色ID，获取部门ID列表
     */
    List<Long> queryDeptIdList(Long roleId);

    /**
     * 根据用户ID获取权限部门列表
     *
     * @param userId
     * @return
     */
    List<Long> queryDeptIdListByUserId(Long userId);
}
