package org.example.entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:40
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统用户与角色对应关系实体
 */
public class SysUserRoleEntity implements Serializable {
    public static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long roleId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }
}
