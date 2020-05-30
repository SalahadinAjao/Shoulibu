package org.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: houlintao
 * @Date:2020/5/30 下午1:38
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SysSmsLogEntity implements Serializable {
    public static final long serialVersionUID = 1L;

    private String id;
    /**
     * 操作人id
     */
    private Long userId;
    /**
     * 必填，手机号码，多个号码以英文逗号隔开
     */
    private String mobile;
    /**
     * 发送时间
     */
    private Date sendtime;
    /**
     * 模板ID
     */
    private int templateId;
    /**
     * 验证码
     */
    private String captcheCode;
    /**
     * 必填,用户签名
     */
    private String userSign;
    /**
     * 发送状态，1成功 0失败
     */
    private Integer sendStatus;
    /**
     * 发送编号
     */
    private String sendId;
    /**
     * 成功提交数
     */
    private Integer successNum;
    /**
     * 返回消息
     */
    private String returnMsg;
    /**
     * 操作人
     */
    private String userName;


    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public void setCaptcheCode(String captcheCode) {
        this.captcheCode = captcheCode;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getCaptcheCode() {
        return captcheCode;
    }

    public String getUserSign() {
        return userSign;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public String getSendId() {
        return sendId;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public String getUserName() {
        return userName;
    }
}
