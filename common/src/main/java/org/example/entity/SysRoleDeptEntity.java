package org.example.entity;

import org.terracotta.modules.ehcache.store.nonstop.LocalReadsOnTimeoutStore;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午7:59
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysRoleDeptEntity implements Serializable {

    public static final long serialVersionUID=1L;

    private Long id;

    private Long roleId;

    private Long deptId;


    public Long getId() {
        return id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
