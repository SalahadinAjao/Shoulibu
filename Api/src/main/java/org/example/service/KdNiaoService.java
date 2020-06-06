package org.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/6 下午12:20
 * 物流追踪服务类，内部使用快递鸟接口
 */
@Service
public class KdNiaoService {

    //具体使用举例
    public static void main(String[] args) {
        KdNiaoService kdNiaoService = new KdNiaoService();

    }
    //电商ID
    private String EBusinessID = "";
    //电商加密私钥，快递鸟提供
    private String AppKey = "";
    //请求的url
    private String ReqURL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";
    //base64加密字符集共64个
    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    //base64加密
    public static String base64Encode(byte[] data){
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1,b2,b3;
        while (i<len){
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    private String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }

    //加密
    private String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null){
            return base64(MD5(content+keyValue,charset),charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**MD5加密*/
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            /**
             * 0xFF的二进制表示就是：1111 1111,高24位补0：0000 0000 0000 0000 0000 0000 1111 1111;
             * result[i] & 0xff表示result[i]和0xff的与操作；
             * val就是result[i] 与 0xff与操作的结果；
             */
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     *@date: 2020/6/6 下午12:26
     *@param expCode 快递代码，如顺丰SF，德邦DB，申通ST
     *@param expNo   快递单号
     * 以json格式追踪快递
     */
    public List TraceOrderWithJson(String expCode,String expNo) throws Exception {
        ArrayList resultObj = new ArrayList();

        String requestData = "{'OrderCode':'','ShipperCode':'\" + expCode + \"','LogisticCode':'\" + expNo + \"'}";
        HashMap<String, String> params = new HashMap<>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1002");

        String dataSign = encrypt(requestData, AppKey, "UTF-8");

        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendPost(ReqURL, params);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        if (jsonObject != null) {
            JSONArray traces = jsonObject.getJSONArray("Traces");
            for (Object object : traces) {
                JSONObject jsonObj = (JSONObject) object;
                Map<String, Object> temp = new HashMap();
                //接收快件时间
                temp.put("AcceptTime", jsonObj.getString("AcceptTime"));
                //接受快件站点
                temp.put("AcceptStation", jsonObj.getString("AcceptStation"));
                resultObj.add(temp);
            }
        }
        return resultObj;
    }




    //使用字符集charset将字符串str转换为一个application/x-www-form-urlencoded MIME 格式
    public String urlEncoder(String str,String charset) throws UnsupportedEncodingException {
        String urlEncodeResult = URLEncoder.encode(str, charset);
        return urlEncodeResult;
    }

    //发送post请求
    public String sendPost(String url, Map<String, String> param) throws IOException {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URL urlReal = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlReal.openConnection();
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // POST方法
            connection.setRequestMethod("POST");
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.connect();

            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            //发送请求参数
            if (param != null) {
                StringBuilder params = new StringBuilder();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    if (params.length() > 0) {
                        params.append("&");
                    }
                    params.append(entry.getKey());
                    params.append("=");
                    params.append(entry.getValue());
                    /**
                     * 最后params的值类似于：“a”="CAR"&"b"="bike"&"c"="truck"......这样的
                     */
                }
                out.write(params.toString());
            }
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out!=null){
                    out.close();
                }
                if (in!=null){
                    in.close();
                }
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}
