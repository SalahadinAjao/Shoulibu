package org.example.Util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.example.Util.cache.My509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Author: houlintao
 * @Date:2020/6/5 上午10:08
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class CommonTool {

    private static Logger logger = Logger.getLogger(CommonTool.class);

    public static JSONObject sendHttpsRequest(String requestUrl, String requestMethod, String outputStr) throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException, IOException {
        JSONObject jsonObject = null;
        //创建SSLContext对象，并使用自定义的信任管理器初始化
        TrustManager[] trustManagers = {new My509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, trustManagers, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(ssf);

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        // 设置请求方式（GET/POST）
        conn.setRequestMethod(requestMethod);

        // 当outputStr不为null时向输出流写数据
        if (null != outputStr) {
            //获取输出流对象
            OutputStream outputStream = conn.getOutputStream();
            // 注意编码格式
            outputStream.write(outputStr.getBytes("UTF-8"));
            outputStream.close();
        }

        // 从输入流读取返回内容
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }

        // 释放资源
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        conn.disconnect();
        jsonObject = JSONObject.parseObject(buffer.toString());

        return jsonObject;
    }

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            /**
             * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
             * 信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
             * 一句话就是MessageDigest原理和区块链的哈希算法类似；
             */
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
