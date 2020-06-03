package org.example.service;

import org.example.entity.SysMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/3 下午4:53
 * @email 437547058@qq.com
 * @Version 1.0
 */
public interface SysMenuService {

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId   父菜单ID
     * @param userMenuIdList 用户菜单id列表
     */
    List<SysMenuEntity> queryListParentId(Long parentId, List<Long> userMenuIdList);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);


    /**
     * 根据菜单id查询菜单实体
     */
    SysMenuEntity queryObject(Long menuId);

    /**
     * 查询菜单列表
     */
    List<SysMenuEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存菜单
     */
    void save(SysMenuEntity menu);

    /**
     * 修改
     */
    void update(SysMenuEntity menu);

    /**
     * 批量删除菜单
     */
    void deleteBatch(Long[] menuIds);

    /**
     * 查询用户的权限列表
     */
    List<SysMenuEntity> queryUserPermList(Long userId);
}
