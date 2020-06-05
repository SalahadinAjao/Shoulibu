package org.example.utils;

import org.example.Util.ResourceTool;

/**
 * @Author: houlintao
 * @Date:2020/6/5 上午9:34
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class UserWeiXinTools {
    //获取系统配置文件的微信临时登录凭证信息
    public static String getCode(String AppId,String Redirect_Uri,String Scope){
        /**
         * 我们会事先在文件系统提供微信相关配置信息，如：
         * 小程序id，小程序密钥等；
         * 通过ResourceTool就可以通过名称获取到这些数据
         */
        return String.format(ResourceTool.getConfigPropertyByName("wx.getCode"),AppId,Redirect_Uri,Scope);
    }
    /**
     * 返回用户在微信的信息，其中wx.userMessage就是我们的服务器请求微信获取用户
     * 信息的地址
     */
    public static String getUserWeiXinInfo(String access_token, String openid){
        return String.format(ResourceTool.getConfigPropertyByName("wx.userMessage"), access_token, openid);
    }

    /**
     * 返回格式化后的Web_access_tokenhttps的请求地址
     */
    public static String getWebAccess(String code){
        return String.format(ResourceTool.getConfigPropertyByName("wx.webAccessTokenhttps"),
                ResourceTool.getConfigPropertyByName("wx.appId"),
                ResourceTool.getConfigPropertyByName("wx.secret"),
                code);
    }
}
