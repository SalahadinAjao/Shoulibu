package org.example.utils;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.example.service.TokenService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.Addressing;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/5/30 上午11:40
 * @email 437547058@qq.com
 * @Version 1.0
 * 此类是所有API的控制器的父类
 */
public class ApiBaseAction {
    protected Logger logger = Logger.getLogger(getClass());

    //request对象
    @Autowired
    protected HttpServletRequest request;
    //response对象
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected TokenService tokenService;


    //构建统一格式的返回对象，此对象是基本的返回对象，其他的返回方法会调用此对象
    public Map<String,Object> toResponsObject(int requestCode,String msg,Object data){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("errno",requestCode);
        hashMap.put("errmsg",msg);
        /**
         * if判断后面如果没有{}，则if下面的第一行归if管；
         * if判断后面如果有{}，则{}里的内容都归if管
         */
        if (data!=null){
            hashMap.put("data",data);
        }
        return hashMap;
    }

    //成功时候的返回
    public Map<String,Object> toResponseSuccess(Object data){
         Map<String, Object> map = toResponsObject(0,"执行成功", data);
        return map;
    }

    public Map<String,Object> toResponseMsgSuccess(String msg){
        return toResponsObject(0, msg, "");
    }

    public Map<String, Object> toResponsSuccessForSelect(Object data) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("list", data);
        return toResponsObject(0, "执行成功", result);
    }

    public Map<String, Object> toResponsFail(String msg) {
        return toResponsObject(1, msg, null);
    }

    /**
     * initBinder 初始化绑定 <br>
     * 这里处理了3种类型<br>
     * 1、字符串自动 trim 去掉前后空格 <br>
     * 2、java.util.Date 转换为 "yyyy-MM-dd HH:mm:ss" 格式<br>
     * 3、java.sql.Date 转换为 "yyyy-MM-dd" 格式<br>
     * 4、java.util.Timestamps 时间转换
     *
     * @param binder  WebDataBinder 要注册的binder
     * @param request 前端请求
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {

        // 字符串自动Trim
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    /**
     * 获取请求端主机ip地址
     */
    public String getClientIp(){
        /**
         *  X-Real-IP，一般只记录真实发出请求的客户端IP
         */
        String header = request.getHeader("X-Real-IP");
        if (header !=null){
            return header;
        }
        /**
         *  X-Forwarded-For是用于记录代理信息的，每经过一级代理(匿名代理除外)，代理服务器
         *  都会把这次请求的来源IP追加在X-Forwarded-For中
         */
        header = request.getHeader("x-forwarded-for");
        if (header==null){
            //免费DNS服务器的ip地址
            return "8.8.8.8";
        }
        return header;
    }



    //获取Json请求
    public JSONObject getJsonRequest() throws IOException {
        JSONObject result = null;
        StringBuilder stringBuilder = new StringBuilder();

        //获取前端发来的请求数据流
        BufferedReader reader = request.getReader();

        char[] buff = new char[1024];
        int length;

        while ((length=reader.read(buff))!=-1){
            /**
             * stringBuilder的append(buff,0,length)方法的作用是将buff中的数据写入stringBUilder中
             * 从buff的0号字符位开始写，写的长度是length。这里的0和length是buff的，不是stringBuilder的
             * 将读取的前端请求数据存入stringBuilder
             */
            stringBuilder.append(buff,0,length);
        }
        result = JSONObject.parseObject(stringBuilder.toString());
        return result;
    }



    @ExceptionHandler({BindException.class, MissingServletRequestParameterException.class, UnauthorizedException.class, TypeMismatchException.class})
    @ResponseBody
    public Map<String,Object> bindException(Exception e){
        if (e instanceof BindException){
            return toResponsObject(1, "参数绑定异常", e.getMessage());
        }else if (e instanceof UnauthorizedException) {
            return toResponsObject(1, "无访问权限", e.getMessage());
        }
        return toResponsObject(1, "处理异常", e.getMessage());
    }

}
