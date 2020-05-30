package org.example.entity;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午9:56
 * @email 437547058@qq.com
 * @Version 1.0
 * 封装了系统配置信息
 */
public class SysConfigEntity {
    private Long id;
    @NotBlank(message = "参数名不能为空")
    private String key;
    @NotBlank(message = "参数名不能为空")
    private String value;
    private String note;

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getNote() {
        return note;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
