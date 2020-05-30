package org.example.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午7:52
 * @email 437547058@qq.com
 * @Version 1.0
 * 注解，当方法中有此注解时可以不需要权限验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipAuth {
}
