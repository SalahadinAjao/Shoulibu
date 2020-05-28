package org.example.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午11:11
 * @email 437547058@qq.com
 * @Version 1.0
 * Http请求处理工具类
 */
public class RequestUtil {

    private static final Logger LOGGER = Logger.getLogger(RequestUtil.class.getName());

    /**
     *@date: 2020/5/28 上午11:21
     *@param:
     *@return:
     *@Description:将请求参数封装成Map
     */
    public static Map<String,Object> getRequestParas(HttpServletRequest request,boolean printLog){
        Enumeration<String> parameterNames = request.getParameterNames();
        HashMap<String, Object> map = new HashMap<>();
        /**遍历parameterNames并分别从request对象中获取key ,value对象*/
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);

            map.put(key,value);
            if (printLog){
                LOGGER.info(key + "==>" + value);
            }
        }
        if (map.get(Constant.SORT_ORDER)!=null){
            map.put(Constant.SORT_ORDER,"asc");
        }
        return map;
    }

    /**
     *@date: 2020/5/28 上午11:42
     *@param:
     *@return:
     *@Description:获取请求方IP地址
     */
    public static String getIpAddressByRequest(HttpServletRequest request){

        /**X-Forwarded-For 是一个 HTTP 扩展头部,用来表示 HTTP 请求端真实 IP,
         * 如果一个 HTTP 请求到达服务器之前，经过了三个代理 Proxy1、Proxy2、Proxy3，
         * IP 分别为 IP1、IP2、IP3，用户真实 IP 为 IP0，那么按照 XFF 标准，
         * 服务端最终会收到以下信息：X-Forwarded-For: IP0, IP1, IP2
         * 因此通过它获取的是真实的ip地址*/
        String ip = request.getHeader("x-forwarded-for");

        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            /**在apache+WebLogic整合系统中，apache会对request对象进行来再包装，
             * 附加一些WLS要用的头信息。这源种情况下，直接用知request.getRemoteAddr()是
             * 无法取到真正的客户IP的。
             　apache会增加下列头信息：
             　　X-Forwarded-For=211.161.1.239
             　　WL-Proxy-Client-IP=211.161.1.239
             */
            ip=request.getHeader("Proxy-Client-IP");
        }
        /**接着判断，若ip还是不满足*/
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            //重新给ip赋值
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        //再判断
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
