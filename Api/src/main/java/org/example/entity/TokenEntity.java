package org.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午6:45
 * token实体类，用于封装系统Token数据
 */
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    //通过userId与用户绑定
    private Long userId;
    private String token;
    /**token过期时刻*/
    private Date finalTime;
    //token更新时间
    private Date updateTime;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFinalTime(Date finalTime) {
        this.finalTime = finalTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Date getFinalTime() {
        return finalTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }
}
