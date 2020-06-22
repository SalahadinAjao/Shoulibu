package org.example.entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/22 下午3:35
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class CouponInfoEntity implements Serializable {
    public static final long serialVersionUID=1L;

    private String msg; // 显示信息
    private Integer type = 0; // 是否凑单 0否 1是

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
