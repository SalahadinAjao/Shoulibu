package org.example.service;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/3 下午5:09
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统角色与菜单的对应关系实体
 */
public interface SysRoleMenuService {

    /**
     *@date: 2020/6/3 下午5:10
     *@param roleId 用户角色id
     *@param menuIdList roleId对应的菜单id列表
     *@return:
     *@Description:
     */
    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);
}
