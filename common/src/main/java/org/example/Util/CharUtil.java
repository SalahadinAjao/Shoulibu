package org.example.Util;

import java.util.Random;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午7:05
 * @email 437547058@qq.com
 * @Version 1.0
 * 字符处理工具类
 */
public class CharUtil {

    //获取随即字符串
    public static String getRandomString(Integer num){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i=0;i<num;i++){
            int anInt = random.nextInt(base.length());
            stringBuffer.append(base.charAt(anInt));
        }

        return stringBuffer.toString();
    }

    //获取随机字符串
    public static String getRandomNum(Integer num){
        String base = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i=0;i<num;i++){
            int anInt = random.nextInt(base.length());
            stringBuffer.append(base.charAt(anInt));
        }

        return stringBuffer.toString();
    }


}
