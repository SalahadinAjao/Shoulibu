package org.example.Util;

import java.util.HashMap;

/**
 * @Author: houlintao
 * @Date:2020/5/19 上午9:47
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class ResponseTool extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;


    /**
     *@date: 2020/5/19 上午10:06
     *@param:
     *@return:
     *@Description:
     */
    public ResponseTool(){
        put("code",0);
    }

    /**
     *@date: 2020/5/19 上午10:09
     *@param:
     *@return:
     *@Description:
     */
    public static ResponseTool erro(int code, String msg){
        ResponseTool r = new ResponseTool();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static ResponseTool error(String msg) {
        return erro(500,msg);
    }

    public static ResponseTool error(){
        return erro(500,"未知异常");
    }

    public static ResponseTool ok(String msg) {
        ResponseTool r = new ResponseTool();
        r.put("msg", msg);
        return r;
    }

    /**
     *@date: 2020/5/20 上午6:55
     *@param:
     *@return:
     *@Description:
     */
    public static ResponseTool ok(HashMap<String,Object> map){
        ResponseTool r = new ResponseTool();
        r.putAll(map);
        return r;
    }

    public static ResponseTool ok(){
        return new ResponseTool();
    }


    public ResponseTool put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
