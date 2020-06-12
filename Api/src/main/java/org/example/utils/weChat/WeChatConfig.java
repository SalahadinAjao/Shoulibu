package org.example.utils.weChat;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.example.Util.ResourceTool;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @Author: houlintao
 * @Date:2020/6/12 下午2:05
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class WeChatConfig {
    /**
     * 出现并发怎么办？如果这个对象在第一次创建后下边这个sslcsf就不为null了，那么有没有这样一种情况：在同一时刻有n个调用者？
     * 答案是：不怕。
     * 在这个sslcsf实例中维护的是固定的变量：wx.mchId，wx.certName等，即使并发了也没有值的改变；
     */
    private static SSLConnectionSocketFactory sslcsf=null;

    public static SSLConnectionSocketFactory getSslcsf() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, IOException {

        if (sslcsf==null){
            setSslcsf();
        }
        return sslcsf;
    }

    public static void setSslcsf() throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        try {
            //KeyStore类是个密码学领域的key和证书的存储设施
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            /**
             * 为了避免在项目中加载不到本项目中静态资源文件，调用静态资源的类加载器时最好
             * 使用Thread.currentThread().getContextClassLoader()方法来获取，因为一般同一个项目中java代码和其静
             * 态资源文件都是同一个classLoader来加载的，以此确保通过此classLoader也能加载到本项目中的资源文件;
             * 原文链接：https://blog.csdn.net/puhaiyang/java/article/details/82913871
             */
            Thread.currentThread().getContextClassLoader();
            //读取本地的证书
            InputStream inputStream = new WechatRefundApiResult().getClass().getResourceAsStream(ResourceTool.getConfigPropertyByName("wx.certName"));

            keyStore.load(inputStream,ResourceTool.getConfigPropertyByName("wx.mchId").toCharArray());
            inputStream.close();

            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, ResourceTool.getConfigPropertyByName("wx.mchId").toCharArray()).build();
            sslcsf=new SSLConnectionSocketFactory(sslContext,new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }
}
