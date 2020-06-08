package org.example.utils.weChat;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: houlintao
 * @Date:2020/6/7 下午7:52
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class MD5 {

    public MD5() {
    }

    public static String getMessageDigest(String rawData){
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md5Instance = MessageDigest.getInstance("MD5");
            byte[] bytes = md5Instance.digest(rawData.getBytes("UTF-8"));

            for (Byte item : bytes){
                //为什么byte类型的item要&0xff再赋值给int类型?
                //本质原因还是想保持二进制补码的一致性
                /**
                 * 当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，
                 * &0xff可以将高的24位置为0，低8位保持原样。这样做的目的就是为了保证二进制数据的一致性。
                 */
                builder.append(Integer.toHexString((item & 0xff) | 0x100).substring(1,3));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          return builder.toString().toUpperCase();
    }
}
