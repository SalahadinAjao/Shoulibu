package org.example.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        StringBuffer xmlStr = new StringBuffer();
          if (param!=null){
              xmlStr.append("<xml>");
              //把param中的key取出来
              Set<Object> keySet = param.keySet();
              Iterator<Object> keySetIterator = keySet.iterator();

              while (keySetIterator.hasNext()){
                  String key = (String) keySetIterator.next();
                  String value = String.valueOf(param.get(key));
                  xmlStr.append("<");
                  xmlStr.append(key);
                  xmlStr.append(">");
                  xmlStr.append(value);
                  xmlStr.append("</");
                  xmlStr.append(key);
                  xmlStr.append(">");
              }
              xmlStr.append("</xml>");
          }
          return xmlStr.toString();
    }

}
