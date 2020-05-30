package org.example.Util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午8:52
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class StringUtils {

    public static final String EMPTY = "";
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static String keyGenerater(String prefix,String className,String methodName){
        return prefix+"userId_"+
                ShiroUtils.getUserId()+
                "_"+
                className+
                "." +
                methodName;
    }
    /**
     * 判断字符串是否不为空，不为空返回true
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        }
        return false;
    }
    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     */

    public static boolean isNullOrEmpty(Object object){
        //对象为null，直接返回true
        if (object==null){
            return true;
        }
        //如果对象是字符序列或者字符序列的子类对象
        if (object instanceof CharSequence){
            /**
             * 因为不确定是不是字符序列的子类，因此这里需要使用强转将object转换为
             * 字符序列，这样不论其是字符序列子类或者字符序列这个类本身都可以调用length方法；
             * 直接调用length方法判断长度。
             */
            return ((CharSequence)object).length()==0;
        }
        //是个集合类或者其子类的时侯
        if (object instanceof Collection){
            return ((Collection) object).isEmpty();
        }
        if (object instanceof Map){
            return ((Map) object).isEmpty();
        }
        //如果是个对象数组的时候
        if (object instanceof Object[]){
            Object[] obj = (Object[]) object;
            if (obj.length==0){
                return true;
            }
            boolean empty = true;
            for (int i=0;i<obj.length;i++){
                //递归调用
                if (!isNullOrEmpty(obj[i])){
                    empty=false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

}
