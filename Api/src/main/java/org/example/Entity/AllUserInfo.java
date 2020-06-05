package org.example.Entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/6/5 上午8:29
 */
public class AllUserInfo implements Serializable {

    public static final long serialVersionUID=1L;

    private String errMsg;
    //原始数据
    private String rawData;
    //用户基本信息
    private BaseUserInfo baseUserInfo;
    //加密后的数据
    private String encryptedData;
    //iv
    private String iv;
    //签名
    private String signature;

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public void setBaseUserInfo(BaseUserInfo baseUserInfo) {
        this.baseUserInfo = baseUserInfo;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getRawData() {
        return rawData;
    }

    public BaseUserInfo getBaseUserInfo() {
        return baseUserInfo;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public String getSignature() {
        return signature;
    }
}
