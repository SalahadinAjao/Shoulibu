package org.example.interceptor;

import org.apache.commons.lang.StringUtils;
import org.example.Entity.TokenEntity;
import org.example.Util.ApiRRException;
import org.example.annotations.SkipAuth;
import org.example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

/**
 * @Author: houlintao
 * @Date:2020/5/29 上午6:42
 * @email 437547058@qq.com
 * @Version 1.0
 */
/**
 * 权限(Token)验证
 * HandlerInterceptorAdapter拦截所有来自浏览器的请求，使用一个类继承它就可以拦截所有来自浏览器的请求
 * 这个拦截器被配置为拦截所有诸如“/api/**”这样的路径
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;

    public static final String LOGIN_USER_KEY="LOGIN_USER_KEY";
    public static final String LOGIN_TOKEN_KEY="X_SHOULI_TOKEN";

    public AuthorizationInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,X-Nideshop-Token,X-URL-PATH");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        SkipAuth skipAuth;
        /**
         * springMvc框架中的handler可以是一个自定义的类，也可以是一个controller类的方法，如果是控制器的方法
         * 那么它就是HandlerMethod类型的,如果是这个类型的就代表一个controller方法对象，可以通过它使用反射
         * 技术获取方法信息
         */
        if (handler instanceof MethodHandle){
            //如果handler是控制器方法，那么就可以使用反射技术获取此方法上的注解
            skipAuth = ((HandlerMethod) handler).getMethodAnnotation(SkipAuth.class);
            /**这里是不是需要作一个判断：判断annotation对象是否为空？如果为null就需要作相应处理*/

            /**--------------------------------存疑-------------------------------------*/
            /**不需要判断，因为这里是判断此annotation对象是不是HandlerMethod类型的，如果是才执行上面的
             * 逻辑
             * 如果不是就说明此注解annotation是个类，无法直接通过反射获取，直接返回true，无需判断是否为空，
             * 因为下面有判断*/
        }else {
            return true;
        }
        //如果方法上有ignoreAuth注解，则不需要验证，直接返回ture
        if (skipAuth !=null){
            return true;
        }
        //若没有此注解，需要验证
        //首先从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        //判断token是否存在，如果header中不存在token，则需要从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_TOKEN_KEY);
        }
        //token为空
        if (StringUtils.isBlank(token)) {
            throw new ApiRRException("请先登录", 401);
        }
        TokenEntity tokenEntity = tokenService.queryByToken(token);
        if (tokenEntity==null || tokenEntity.getFinalTime().getTime()<System.currentTimeMillis()){
            throw new ApiRRException("Token已过期，请重新登录",401);
        }
            request.setAttribute(LOGIN_USER_KEY,tokenEntity.getUserId());

        return true;
    }
}
