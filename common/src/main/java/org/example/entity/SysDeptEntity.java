package org.example.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/4 上午8:02
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysDeptEntity implements Serializable {

    public static final long serialVersionUID=1L;

    //部门ID
    private Long deptId;
    //上级部门ID，一级部门为0
    private Long parentId;
    //部门名称
    private String name;
    //上级部门名称
    private String parentName;
    //排序
    private Integer orderNum;
    /**
     * ztree属性
     */
    private Boolean open;

    private List<?> list;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Long getDeptId() {
        return deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public Boolean getOpen() {
        return open;
    }

    public List<?> getList() {
        return list;
    }
}
