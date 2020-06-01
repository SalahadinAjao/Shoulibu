package org.example.service;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/1 上午6:09
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysUserRoleService {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

    List<Long> queryRoleIdList(Long userId);

    void delete(Long userId);
}
