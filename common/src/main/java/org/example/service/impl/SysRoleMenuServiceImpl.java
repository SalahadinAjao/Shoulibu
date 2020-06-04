package org.example.service.impl;

import org.example.service.SysRoleMenuService;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/3 下午5:12
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    /**
     *@date: 2020/6/4 上午7:52
     * 根据角色id保存或更新对应的菜单id列表
     */
    @Override
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {

    }

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return null;
    }
}
