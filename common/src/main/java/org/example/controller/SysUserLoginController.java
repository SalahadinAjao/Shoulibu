package org.example.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.example.Util.ResponseMap;
import org.example.Util.ShiroUtils;
import org.example.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午8:09
 * @email 437547058@qq.com
 * @Version 1.0
 * 这个是系统用户（与普通用户对应，如普通的购物者就是普通用户，而系统用户是卖方）登录
 * 控制器
 */
@Controller
public class SysUserLoginController {

    @Autowired
    private Producer producer;

    /**
     *@date: 2020/5/30 上午8:20
     *@param:
     *@return:
     *@Description:获取验证码
     */
    @RequestMapping("captcha.jpg")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        /**
         * Cache-Control指定请求和响应遵循的缓存机制；
         * no-cache指示请求或响应消息不能缓存；
         * no-store用于防止重要的信息被无意的发布，在请求消息中发送将使得请求和响应消息都不使用缓存；
         */
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //具体的验证码生成逻辑
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        //将验证码text保存到session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY,text);

        //获取输出流
        ServletOutputStream responseOutputStream = response.getOutputStream();
        //使用ImageIO流将image数据写入输出流responseOutputStream
        ImageIO.write(image,"jpg",responseOutputStream);
    }


    /**
     *@date: 2020/5/30 上午8:33
     *@param: userName:用户名   passWord:密码   captche:验证码
     *@return:R
     *@Description:
     */
    @SystemLog
    @ResponseBody
    @RequestMapping(value = "/sys/login",method = RequestMethod.POST)
    public ResponseMap login(String userName, String passWord, String captche){
        //从shiro获取已存储的验证码
        String captche1 = ShiroUtils.getCaptche(Constants.KAPTCHA_SESSION_KEY);
        if (captche1==null){
            return ResponseMap.error("登录超时，验证码已失效");
        }
        //若验证码不为空，则对比传入的验证码和从shiro中获取的y验证码
        if (!captche1.equalsIgnoreCase(captche)){
            return ResponseMap.error("验证码不正确");
        }

        try {
            //验证码正确，就从shiro中获取subject
            Subject subject = ShiroUtils.getSubject();
            //将原密码进行sha256加密处理
            passWord = new Sha256Hash(passWord).toHex();
            //获取token
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, passWord);
            subject.login(usernamePasswordToken);
        }catch (UnknownAccountException e){
            return ResponseMap.error(e.getMessage());
        }catch (LockedAccountException e){
            return ResponseMap.error(e.getMessage());
        }catch (AuthenticationException e){
            return ResponseMap.error("账户没有通过验证");
        }
        return ResponseMap.ok();
    }

    public String logout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
