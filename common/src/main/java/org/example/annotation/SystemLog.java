package org.example.annotation;

import java.lang.annotation.*;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午8:37
 * @email 437547058@qq.com
 * @Version 1.0
 * 系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    String value() default "系统操作日志";
}
