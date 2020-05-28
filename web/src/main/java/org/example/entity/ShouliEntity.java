package org.example.entity;

/**
 * @Author: houlintao
 * @Date:2020/5/19 上午10:56
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class ShouliEntity {

    private int id;
    private Long userId;

    /**
     * 1.婚礼 2.生子 3.金榜题名 4.乔迁新居 5.喜得贵子 6.仕途高升 7.故去
    */
    private String sort;
    private String title;//例如  李寻欢乔迁收礼簿
    private String Fname;//出礼人姓名
    private String buildTime;//出礼时间
    private int money;//出礼多少钱
    private String otherGift;//其它，比如三斤鸡蛋这样的
    private String note;//备注信息
    private String Sname;//受礼的人姓名，用于记录谁给录入的

    public void setId(int id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setOtherGift(String otherGift) {
        this.otherGift = otherGift;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSname(String sname) {
        Sname = sname;
    }

    public int getId() {
        return id;
    }

    public String getSort() {
        return sort;
    }

    public String getTitle() {
        return title;
    }

    public String getFname() {
        return Fname;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public int getMoney() {
        return money;
    }

    public String getOtherGift() {
        return otherGift;
    }

    public String getNote() {
        return note;
    }

    public String getSname() {
        return Sname;
    }
}
