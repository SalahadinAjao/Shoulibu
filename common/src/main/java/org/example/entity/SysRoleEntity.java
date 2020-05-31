package org.example.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午6:19
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统角色实体，在一个系统中系统角色类似于Linux系统里的超级管理员（介于超级管理员与普通用户之间），不同的用户
 * 具备不同的角色和权限。
 */
public class SysRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    //备注信息
    private String note;
    //角色的创建者id
    private Long creatorId;
    //菜单id列表
    private List<Long> menuIdList;

    /**
     * 角色的创建时间
     */
    private Date createTime;
    /**
     * 角色所属的部门ID
     */
    @NotNull(message = "部门不能为空")
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;
    //角色所属部门id列表
    private List<Long> deptIdList;

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeptIdList(List<Long> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getNote() {
        return note;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public List<Long> getDeptIdList() {
        return deptIdList;
    }
}
