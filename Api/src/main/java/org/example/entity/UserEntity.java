package org.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午6:28
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    //用户名称
    private String username;
    //用户密码
    private String password;
    //性别
    private Integer gender;
    //出生日期
    private Date birthday;
    //注册时间
    private Date register_time;
    //最后登录时间
    private Date last_login_time;
    //最后登录Ip
    private String last_login_ip;
    //会员等级
    private Integer user_level_id;
    //别名
    private String nickname;
    //手机号码
    private String mobile;
    //注册Ip
    private String register_ip;
    //头像
    private String avatar;
    //微信Id
    private String weixin_openid;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public void setUser_level_id(Integer user_level_id) {
        this.user_level_id = user_level_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setRegister_ip(String register_ip) {
        this.register_ip = register_ip;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public Integer getUser_level_id() {
        return user_level_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRegister_ip() {
        return register_ip;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }
}
