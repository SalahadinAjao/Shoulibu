package org.example.Util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.example.entity.SysUserEntity;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午9:27
 * @email 437547058@qq.com
 * @Version 1.0
 *
 * Shiro工具类，负责与Shiro框架打交道
 */
public class ShiroUtils {



    public static Session getSession(){
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static SysUserEntity getUserEntity(){
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId(){
        return getUserEntity().getUserId();
    }

    public static void setSessionAttribute(Object key,Object value){
        getSession().setAttribute(key,value);
    }

    public static String getCaptche(String key){
        //从shiro的session中获取验证码
        String captche = getSession().getAttribute(key).toString();
        //从session中清除已获取的验证码
        getSession().removeAttribute(key);

        return captche;
    }

}
