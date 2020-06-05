package org.example.Util;

import java.util.HashMap;

/**
 * @Author: houlintao
 * @Date:2020/5/19 上午9:47
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class ResponseMap
        extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;


    /**
     *@date: 2020/5/19 上午10:06
     *@param:
     *@return:
     *@Description:
     */
    public ResponseMap(){
        put("code",0);
    }

    /**
     *@date: 2020/5/19 上午10:09
     *@param:
     *@return:
     *@Description:
     */
    public static ResponseMap erro(int code, String msg){
        ResponseMap r = new ResponseMap();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static ResponseMap error(String msg) {
        return erro(500,msg);
    }

    public static ResponseMap error(){
        return erro(500,"未知异常");
    }

    public static ResponseMap ok(String msg) {
        ResponseMap r = new ResponseMap();
        r.put("msg", msg);
        return r;
    }

    /**
     *@date: 2020/5/20 上午6:55
     *@param:
     *@return:
     *@Description:
     */
    public static ResponseMap ok(HashMap<String,Object> map){
        ResponseMap r = new ResponseMap();
        r.putAll(map);
        return r;
    }

    public static ResponseMap ok(){
        return new ResponseMap();
    }


    public ResponseMap put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
