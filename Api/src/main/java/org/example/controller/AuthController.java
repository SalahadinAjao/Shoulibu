package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.StringUtils;
import org.apache.log4j.Logger;
import org.example.Entity.AllUserInfo;
import org.example.Entity.BaseUserInfo;
import org.example.Entity.UserEntity;
import org.example.Util.CharUtil;
import org.example.Util.CommonTool;
import org.example.annotations.SkipAuth;
import org.example.service.ApiUserService;
import org.example.service.TokenService;
import org.example.utils.ApiBaseAction;
import org.example.utils.UserWeiXinTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author: houlintao
 * @Date:2020/6/5 上午8:19
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController extends ApiBaseAction {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;

    @SkipAuth
    @RequestMapping("login_by_weixin")
    public Object loginByWeiXin() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        //获取Json请求对象
        JSONObject jsonRequest = this.getJsonRequest();
        AllUserInfo allUserInfo = null;
        /**
         * 调用微信登录的第一步是通过微信开放接口wx.login获取由微信
         * 平台提供的临时登录凭证，这个凭证是临时的，每次登录都会生成一个
         * 然后前端会把这个临时凭证发送给开发者服务器；
         * 这里的code就是保存的由前端发送的临时凭证；
         */
        String code = "";
        String requestCode = jsonRequest.getString("code");

        if (!StringUtils.isNullOrEmpty(requestCode)){
            code = jsonRequest.getString("code");
        }
        String info = jsonRequest.getString("userInfo");
        //如果可以从json请求对象中获取到userInfo字符串
        if (!StringUtils.isNullOrEmpty(info)){
            //将userInfo字符串实例化为AllUserInfo对象
           allUserInfo = jsonRequest.getObject("userInfo", AllUserInfo.class);
        }
        if (allUserInfo == null){
            return toResponsFail("登录失败");
        }
        //如果allUserInfo不为空，则准备获取openId
        HashMap<String, Object> resultMap = new HashMap<>();
        BaseUserInfo baseUserInfo = allUserInfo.getBaseUserInfo();

        //微信服务器的授权第三方应用获取用户web接入地址
        String webAccessUrl = UserWeiXinTools.getWebAccess(code);
        //使用https请求微信相关服务器地址以获取由微信返回的session数据
        JSONObject sessionData = CommonTool.sendHttpsRequest(webAccessUrl, "GET", null);
        if (sessionData==null || StringUtils.isNullOrEmpty(sessionData.getString("openid")))){
          return toResponsFail("登录失败");
        }
        //若sessionData！= null
        String sha1_string = CommonTool.getSha1(allUserInfo.getRawData() + sessionData.getString("session_key"));
           if (!allUserInfo.getSignature().equals(sha1_string)){
               return toResponsFail("登录失败");
           }
        Date currTime = new Date();
        UserEntity userEntity = userService.queryByOPenId(sessionData.getString("openid"));

        if (userEntity==null){
            userEntity=new UserEntity();
            userEntity.setUsername("微信用户"+ CharUtil.getRandomString(12));
            userEntity.setRegister_time(currTime);
            userEntity.setRegister_ip(this.getClientIp());
            userEntity.setLast_login_ip(userEntity.getRegister_ip());
            userEntity.setLast_login_time(userEntity.getRegister_time());
            userEntity.setWeixin_openid(sessionData.getString("opedid"));
            userEntity.setAvatar(baseUserInfo.getAvatarUrl());
            userEntity.setGender(baseUserInfo.getGender());
            userEntity.setNickname(baseUserInfo.getNickName());

            userService.save(userEntity);
        }else {
            userEntity.setLast_login_ip(userEntity.getRegister_ip());
            userEntity.setLast_login_time(currTime);

            userService.update(userEntity);
        }
    }

}
