package org.example.Util;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Author: houlintao
 * @Date:2020/5/19 上午9:47
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class R extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;


    /**
     *@date: 2020/5/19 上午10:06
     *@param:
     *@return:
     *@Description:
     */
    public R(){
        put("code",0);
    }

    /**
     *@date: 2020/5/19 上午10:09
     *@param:
     *@return:
     *@Description:
     */
    public static R erro(int code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static R error(String msg) {
        return erro(500,msg);
    }

    public static R error(){
        return erro(500,"未知异常");
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    /**
     *@date: 2020/5/20 上午6:55
     *@param:
     *@return:
     *@Description:
     */
    public static R ok(HashMap<String,Object> map){
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok(){
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
