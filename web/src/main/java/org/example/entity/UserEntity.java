package org.example.entity;

/**
 * @Author: houlintao
 * @Date:2020/5/18 上午4:35
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class UserEntity {

    private int id;
    private String userName;
    private String passWord;
    private String teleNumber;
    private String estabTime;

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public String getEstabTime() {
        return estabTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }

    public void setEstabTime(String estabTime) {
        this.estabTime = estabTime;
    }
}
