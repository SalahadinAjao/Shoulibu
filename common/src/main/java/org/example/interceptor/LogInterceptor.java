package org.example.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.Util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午11:01
 * @email 437547058@qq.com
 * @Version 1.0
 *
 * 系统日志拦截器
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static final Log log = LogFactory.getLog(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            request.setAttribute("REQUEST_START_TIME", new Date());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }


    private StringBuilder getRequestInfo(HttpServletRequest request){
        StringBuilder requestINfo = new StringBuilder();

        UrlPathHelper pathHelper = new UrlPathHelper();
        String lookupPathForRequest = pathHelper.getLookupPathForRequest(request);

        requestINfo.append("请求路径="+lookupPathForRequest);
        requestINfo.append("请求来源IP = "+ RequestUtil.getIpAddressByRequest(request));
        return requestINfo;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        Date request_start_time = (Date) request.getAttribute("REQUEST_START_TIME");
        Date end = new Date();

        log.info("本次请求耗时：" + (end.getTime() - request_start_time.getTime()) + "毫秒；" + getRequestInfo(request).toString());
    }
}
