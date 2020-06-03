package org.example.entity;

import org.terracotta.modules.ehcache.store.nonstop.LocalReadsOnTimeoutStore;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/3 下午5:04
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysRoleMenuEntity implements Serializable {
    public static final long serialVersionUID=1L;

    private Long id;
    //角色id
    /**
     * 系统中什么样的角色就有什么样的权限和菜单
     * 一个用户对应一个或多个角色；
     * 一个角色对应一个或多个菜单；
     */
    private Long roleId;
    //菜单id
    private Long menuId;

    public Long getId() {
        return id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
