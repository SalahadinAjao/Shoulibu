package org.example.Util;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;

/**
 * @Author: houlintao
 * @Date:2020/5/30 下午6:19
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class SmsTool {

    /**
     * 指定模板ID群发，这里是调用腾讯云的api实现的
     * 签名参数未提供或者为空时，会使用默认签名发送短信
     */

    public static SmsMultiSenderResult CreatSendSms(int appid, String appkey, String[] phoneNumbers,
                                                    int templateId, String[] params, String smsSign){
        SmsMultiSenderResult result = new SmsMultiSenderResult();
        SmsMultiSender sender = new SmsMultiSender(appid, appkey);
        try {
            result = sender.sendWithParam("86",phoneNumbers,templateId,params,smsSign,"","");
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //短信单独发送
    /**
     * params是个字符串数组，在使用的时候可以发送验证码
     */
    public static SmsSingleSenderResult CreatSendSms(int appid, String appkey, String phoneNumber, int templateId,
                                                     String[] params, String smsSign){
        SmsSingleSenderResult result = new SmsSingleSenderResult();
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, smsSign, "", "");
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result;
    }

}
