package org.example.utils;

import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/8 下午4:36
 * Map工具类，获取map中值,自动进行类型转换
 */
public class MapUtils {

    public static String getString(String key, Map<String,Object> map){

        if (key==null || map==null){
            throw new IllegalArgumentException();
        }
        if (!map.containsKey(key)){
            return null;
        }
        Object value = map.get(key);
        if (value==null){
            return null;
        }

        return value.toString();
    }

    public static Integer getInteger(String key,Map<String,Object> map){
        if (key==null || map==null){
            throw new IllegalArgumentException();
        }
        if (!map.containsKey(key)){
            return null;
        }
        Object value = map.get(key);
        if (value==null){
            return null;
        }

    }

    public static String convertMap2Xml(Map<Object,Object> param){

    }

}
