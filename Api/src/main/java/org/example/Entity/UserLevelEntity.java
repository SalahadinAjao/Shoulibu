package org.example.Entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午6:45
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class UserLevelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //名称
    private String name;
    //描述
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
