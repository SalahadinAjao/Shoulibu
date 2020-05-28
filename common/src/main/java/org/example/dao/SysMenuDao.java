package org.example.dao;

import org.example.entity.SysMenuEntity;

import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/27 下午6:59
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
    /**
     *@date: 2020/5/28 上午7:07
     *@param:
     *@return:
     *@Description:通过parentId查询其子菜单列表
     */
    List<SysMenuEntity> queryMenuByParentId(Long parentId);


}
