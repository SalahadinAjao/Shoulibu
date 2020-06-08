package org.example.resolver;

import org.example.entity.UserEntity;
import org.example.annotations.LoginUser;
import org.example.interceptor.AuthorizationInterceptor;
import org.example.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午6:24
 * @email 437547058@qq.com
 * @Version 1.0
 * 已登录用户处理器方法参数解析器，用于将有特定注解的方法注入当前登录的用户
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private ApiUserService userService;

    public void setUserService(ApiUserService userService){
        this.userService=userService;
    }

    /**
     * 此方法用于判定是否需要处理该参数解析，返回true为需要，并会去调用下面的方法resolveArgument
     * 只有当supportsParameter返回true时，才会调用resolveArgument方法
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /**
         * 通过isAssignableFrom()方法判断是否为某个类的父类
         */
        return parameter.getParameterType().isAssignableFrom(UserEntity.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    /**
     * 这里才是真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object requestAttribute = webRequest.getAttribute(AuthorizationInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);

        if (requestAttribute == null) {
            return null;
        }

        //获取用户信息
        UserEntity user = userService.queryObject((Long) requestAttribute);

        return user;
    }
}
