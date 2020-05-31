package org.example.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/5/30 下午5:48
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统短信发送配置实体类
 */
@Data
public class SmsConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统短信类型 1：腾讯云
     */
    @Range(min = 1, max = 3, message = "类型错误")
    private Integer type;

    /**
     * appid
     */
    private int appid;

    /**
     * key
     */
    private String appkey;

    /**
     * 签名
     */
    private String sign;


    public void setType(Integer type) {
        this.type = type;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getType() {
        return type;
    }

    public int getAppid() {
        return appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getSign() {
        return sign;
    }
}
