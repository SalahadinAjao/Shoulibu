package org.example.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午9:52
 * @email 437547058@qq.com
 * @Version 1.0
 *
 * Shiro权限标签验证器
 */
public class PermsValidator {

    public boolean hasPermission(String permission){
        Subject subject = SecurityUtils.getSubject();
        return subject.isPermitted(permission) && subject!=null;
    }

}
