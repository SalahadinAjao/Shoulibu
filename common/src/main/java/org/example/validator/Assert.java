package org.example.validator;

import org.apache.commons.lang.StringUtils;
import org.example.Util.RRException;

/**
 * @Author: houlintao
 * @Date:2020/5/31 下午5:02
 * @email 437547058@qq.com
 * @Version 1.0
 */
public abstract class Assert {
    public static void isBlank(String str,String msg){
        if (StringUtils.isBlank(str)){
            throw new RRException(msg);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}
