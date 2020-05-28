package org.example.Util;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午8:52
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class StringUtils {

    public static String keyGenerater(String prefix,String className,String methodName){
        return prefix+"userId_"+
                ShiroUtils.getUserId()+
                "_"+
                className+
                "." +
                methodName;
    }
}
