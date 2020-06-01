package org.example.Util;

import org.apache.commons.lang.StringUtils;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午4:20
 * @email 437547058@qq.com
 * @Version 1.0
 * SQL过滤器，用于过滤拼接sql，防止sql注入
 */
public class SqlFilter {

    public static String sqlInject(String str){
        if (StringUtils.isBlank(str)){
            return null;
        }
        /**
         * sql注入非法字符
         */
        //去掉'|"|;|\字符
        str = StringUtils.replace(str,"'","");
        str = StringUtils.replace(str,"\"","");
        str = StringUtils.replace(str,";","");
        str = StringUtils.replace(str,"\\","");

        //转换成小写
        str = str.toLowerCase();

        /**
         * sql注入非法字符
         */
        String[] illegalCharacters = {"master", "truncate", "insert",
                "select", "delete", "update", "declare", "alert", "drop"};

        /**
         *  判断是否包含非法字符
         *  遍历illegaiCharacters的每一个元素，拿str去查找每一个
         *  illegaiCharacters元素是否在其中
         */
        for (String illegalChaeacter:illegalCharacters){
            if (str.indexOf(illegalChaeacter)!=-1){
                throw new RRException("包含非法字符");
            }
        }
            return str;
    }
}
