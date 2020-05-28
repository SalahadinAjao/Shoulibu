package org.example.entity;

import org.example.validator.group.AddGroup;
import org.example.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 这里的这个系统用户与UserEntity的区别是系统用户SysUserEntity代表的是包括UserEntity在内的
 * 各个用户分类的描述，定义不同类型用户的权限，能够操作的菜单等
 * @Author: houlintao
 * @Date:2020/5/24 上午11:15
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysUserEntity implements Serializable {
    public static final long serialVersionUID = 1L;

    //用户id
    private Long userId;
    //用户名
    @NotBlank(message = "用户名不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String userName;

    //密码，不需要进行序列化，故在这里用transient修饰
    private transient String password;

    //手机号码
    private String mobile;

    /**
     * 用户账号的状态，1：正常  0：禁用
     */
    private Integer status;
    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    /**
     * 创建者ID，这个创建者可以是自己，也可以是管理员
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;
    //所属的部门id
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }


    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public Long getCreateUserId() {
        return createUserId;
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
}
