package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.example.entity.SmsLogEntity;
import org.example.entity.UserEntity;
import org.example.Util.CharUtil;
import org.example.Util.Constant;
import org.example.Util.SmsTool;
import org.example.Util.StringUtils;
import org.example.annotations.LoginUser;
import org.example.entity.SmsConfigEntity;
import org.example.service.ApiUserService;
import org.example.service.SysConfigService;
import org.example.utils.ApiBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午9:53
 * @email 437547058@qq.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private SysConfigService configService;


    @PostMapping("sendsms")
    public Object sendSMS(@LoginUser UserEntity user) throws IOException {
        //获取前端发来的Json请求
        JSONObject jasonPara = getJsonRequest();
        //从json请求中获取相关的数据
        String telephone = jasonPara.getString("phone");

        SmsLogEntity smsLogEntity = userService.querySmsCodeByUserId(user.getUserId());
        if (null!=smsLogEntity && (System.currentTimeMillis()/1000-smsLogEntity.getLog_date()<1*60)){
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String yanzhengma = CharUtil.getRandomNum(4);

        SmsConfigEntity configObject = configService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfigEntity.class);
        if (StringUtils.isNullOrEmpty(configObject)){
            return toResponsFail("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(configObject.getAppid())) {
            return toResponsFail("请先配置短信平台APPID");
        }
        if (StringUtils.isNullOrEmpty(configObject.getAppkey())) {
            return toResponsFail("请先配置短信平台KEY");
        }
        if (StringUtils.isNullOrEmpty(configObject.getSign())) {
            return toResponsFail("请先配置短信平台签名");
        }
        //发送短信，SmsSingleSenderResult是腾讯对外开源的接口
        SmsSingleSenderResult result;
        int templateId = 23;
        SmsSingleSenderResult senderResult = SmsTool.CreatSendSms(configObject.getAppid(), configObject.getAppkey(), telephone, templateId, new String[]{yanzhengma}, "");

              if (senderResult.result==0){
                  SmsLogEntity logEntity = new SmsLogEntity();
                  logEntity.setLog_date(System.currentTimeMillis()/1000);
                  logEntity.setUser_id(user.getUserId());
                  logEntity.setPhone(telephone);
                  logEntity.setSms_code(templateId);
                  logEntity.setSms_text(yanzhengma);
                  /**将短信信息保存进数据库以便后面再取出来验证*/
                  userService.saveSmsCodeLog(logEntity);

                  return toResponseSuccess("短信发送成功");
              }else {
                  return toResponsFail("短信发送失败");
              }
    }

    /**
     * 绑定手机号
     * 绑定手机号的逻辑是用户先在前端表单中输入手机号，点击获取验证码，然后后端会调用sendSms方法生成一个验证码发送给
     * 前端，与此同时会在后端生成一个记录短信信息的SmsLogEntity对象并保存进数据库；
     * 点击提交后将手机号，验证码在request对象中一并提交；
     * 后端首先需要对比request中的验证码和smsLogEntity中的对应的验证码是否相同；
     */
    @PostMapping("bindMobile")
    public Object bindMobile(@LoginUser UserEntity entity) throws IOException {
        JSONObject jsonRequest = getJsonRequest();
        //
        SmsLogEntity smsLogEntity = userService.querySmsCodeByUserId(entity.getUserId());

        String mobile_code = jsonRequest.getString("mobile_code");
        String mobile = jsonRequest.getString("mobile");

        //如果request中的验证码与smsLogEntity中记录的不一致说明验证码错误
        if (!mobile_code.equals(smsLogEntity.getSms_code())){
            return toResponsFail("验证码错误");
        }
        //验证码正确就更新userEntity
        UserEntity userEntity = userService.queryObject(entity.getUserId());
        userEntity.setMobile(mobile);

        userService.update(userEntity);

        return toResponseSuccess("手机号绑定成功");
    }

}
