package org.example.Entity;

import java.io.Serializable;

/**
 * @Author: houlintao
 * @Date:2020/5/30 下午5:33
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SmsLogEntity implements Serializable {
    public static final long serialVersionUID = 1L;

    private Long id;

    private Long user_id;

    private String phone;

    private Long log_date;

    private int sms_code;
    // 1成功 0失败
    private Integer send_status;
    //
    private String sms_text;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLog_date(Long log_date) {
        this.log_date = log_date;
    }

    public void setSms_code(int sms_code) {
        this.sms_code = sms_code;
    }

    public void setSend_status(Integer send_status) {
        this.send_status = send_status;
    }

    public void setSms_text(String sms_text) {
        this.sms_text = sms_text;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getPhone() {
        return phone;
    }

    public Long getLog_date() {
        return log_date;
    }

    public int getSms_code() {
        return sms_code;
    }

    public Integer getSend_status() {
        return send_status;
    }

    public String getSms_text() {
        return sms_text;
    }
}
