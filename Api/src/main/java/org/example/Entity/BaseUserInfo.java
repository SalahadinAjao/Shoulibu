package org.example.Entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/5 上午8:30
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class BaseUserInfo implements Serializable {
    public static final long serialVersionUID=1L;

    //头像url
    private String avatarUrl;
    //所在城市
    private String city;
    //性别
    private Integer gender;
    //昵称
    private String nickName;
    //所在省份
    private String province;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getCity() {
        return city;
    }

    public Integer getGender() {
        return gender;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
