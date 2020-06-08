package org.example.utils.weChat;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.Util.CharUtil;
import org.example.Util.ResourceTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: houlintao
 * @Date:2020/6/7 下午4:06
 * 微信退款工具类
 */
public class weChatTool {

    private static Log log = LogFactory.getLog(weChatTool.class);

    /**
     * 充值客户端类型--微信公众号
     */
    public static Integer CLIENTTYPE_WX = 2;
    /**
     * 充值客户端类型--app
     */
    public static Integer CLIENTTYPE_APP = 1;

    public static WechatRefundApiResult wxRefund(String out_trade_no,Double orderMoney,Double reFundMoney){
        BigDecimal bdOrderMoney = new BigDecimal(orderMoney, MathContext.DECIMAL32);
        BigDecimal bdRefundMoney = new BigDecimal(reFundMoney, MathContext.DECIMAL32);

        Map<Object, Object> param = buildRequsetMapParam(out_trade_no, bdOrderMoney, bdRefundMoney);

        String mapToXml = M
    }
    /**
     * 向微信发送https请求
     */
    public static String sendSSLToWx(String mapToXml, SSLConnectionSocketFactory socketFactory) throws IOException {
        log.info("*******退款（WX Request：" + mapToXml);
        HttpPost post = new HttpPost(ResourceTool.getConfigPropertyByName("wx.refundUrl"));
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Accept", "*/*");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setHeader("Host", "api.mch.weixin.qq.com");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("Cache-Control", "max-age=0");
        post.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");

        post.setEntity(new StringEntity(mapToXml,"UTF-8"));
        CloseableHttpClient closeableHttpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        CloseableHttpResponse response = null;

        try {
            response = closeableHttpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String Xmlstring = EntityUtils.toString(entity,"UTF-8");
            log.info("*******退款（WX Response：" + Xmlstring);
            return Xmlstring;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (response!=null){
                response.close();
            }
        }
    }


    /**
     * 构建微信请求参数，参数是个Map
     * @param out_trade_no 商户侧传给微信的订单号;
     *
     */
    public static Map<Object,Object> buildRequsetMapParam(String out_trade_no, BigDecimal bdOrderMoney, BigDecimal bdRefundMoney){
        Map<Object, Object> params = new HashMap<>();
        //微信分配的公众账号ID（企业号corpid即为此appId）
        params.put("appid", ResourceTool.getConfigPropertyByName("wx.appId"));
        //微信支付分配的商户号
        params.put("mch_id",ResourceTool.getConfigPropertyByName("wx.mchId"));
        //随机字符串，不长于32位。推荐随机数生成算法
        params.put("nonce_str", CharUtil.getRandomString(16));
        //商户侧传给微信平台的订单号
        params.put("out_trade_no", out_trade_no);
        //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        params.put("out_refund_no", getBundleId());
        //订单总金额，单位为分，只能为整数
        params.put("total_fee", bdOrderMoney.multiply(new BigDecimal(100)).intValue());
        //退款总金额，订单总金额，单位为分，只能为整数
        params.put("refund_fee", bdRefundMoney.multiply(new BigDecimal(100)).intValue());
        //签名前必须要参数全部写在前面
        params.put("sign", arraySign(params, ResourceTool.getConfigPropertyByName("wx.paySignKey")));

        return params;
    }

    /**
     * 获取交易单号
     */
    public static String getBundleId(){
        //构建一个时间格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //声明一个交易单号对象tradno并使用日期格式化对象以当前日期创建一个字符串
        String tradeno = dateFormat.format(new Date());
        //声明一个随机字符串
        String randomStr = "000000" + (int) (Math.random() * 1000000);
        tradeno = tradeno+randomStr.substring(randomStr.length()-6);
        return tradeno;
    }

    /**
     * @param params map参数
     * @param paySignKey 支付签名的key
     */
    public static String arraySign(Map<Object,Object> params,String paySignKey){
        //是否加密的触发flag
        boolean encode = false;
        //将传入的Map类型参数的key放进set集合中
        Set<Object> keySet = params.keySet();
        //将set集合转换为数组
        Object[] keysArray = keySet.toArray();
        //给array数组内的对象进行升序排序
        Arrays.sort(keysArray);

        StringBuffer temp = new StringBuffer();

        boolean first = true;

        for (Object key:keysArray){
            //如果first值为true
            if (first){
                //将first值取反
                first=false;
            }else {//如果first值为false
                //向temp中添加"&"
                temp.append("&");
            }
            //此时temp中是形如 &key= 这样的内容
            temp.append(key).append("=");
            //params是个Map，get(key)返回的是Object
            Object value = params.get(key);
            //valueString就是从get(key)返回的value的字符串表示
            String valueString = "";
            //若params中根据key可以找到value
            if (value!=null){
                //直接将此value转换为字符串
                valueString = value.toString();
            }//encode=true需要转化
            if (encode){
                try {//此时temp中的内容变成了 &key1=XXXXX&key2=YYYY&key3=XZZZ 这样
                    temp.append(URLEncoder.encode(valueString,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {//否则直接存储
                temp.append(valueString);
            }
        }

        //最后将paySignKey加上，temp中就是 &key1=XXXXX&key2=YYYY&key3=XZZZ.....&key=paysignkey
        temp.append("&key=");
        temp.append(paySignKey);
        System.out.println(temp.toString());
        String packageSign = MD5.getMessageDigest(temp.toString());
        return packageSign;
    }

}
