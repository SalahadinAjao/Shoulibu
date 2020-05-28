package org.example.Util.cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;
import org.example.Util.Constant;
import org.example.Util.StringUtils;

import java.util.Collection;

/**
 * @Author: houlintao
 * @Date:2020/5/28 上午8:21
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class J2CacheTool {
    /**
     * 系统缓存，内部使用的是一个J2Cache对象
     */
    private static CacheChannel cache = J2Cache.getChannel();


    //系统缓存flag
    private static String SYS_CACHE_NAME = "sysCache";

    /**
     *@date: 2020/5/28 上午8:38
     *@param:
     *@return:void
     *@Description:向SYS_CACHE_NAME缓存中写入数据
     */
    public static void  put(String key,Object value){
        put(SYS_CACHE_NAME,key,value);
    }


    /**
     *@date: 2020/5/28 上午8:36
     *@param:
     *@return:
     *@Description:根据key获取SYS_CACHE_NEME下对应的的缓存数据
     */
    public static Object get(String key){
        return getCache(SYS_CACHE_NAME,key);
    }
    /**
     *@date: 2020/5/28 上午8:40
     *@param:
     *@return:void
     *@Description:将key对应缓存从SYS_CACHE_NAME中移除
     */
    public static void delete(String key){
        delete(SYS_CACHE_NAME,key);
    }

    /**
     *@date: 2020/5/28 上午8:25
     *@param:
     *@return:
     *@Description:获取缓存对象，内部使用的是一个J2Cache
     */
    public static Object getCache(String cacheName,String key){
        return cache.get(cacheName,key).getValue();
    }

    /**
     *@date: 2020/5/28 上午8:30
     *@param:
     *@return:
     *@Description:将数据放入缓存
     */
    public static void put(String cacheName,String key,Object value){
        cache.set(cacheName,key,value);
    }
    /**
     *@date: 2020/5/28 上午8:32
     *@param:
     *@return:
     *@Description:将数据从缓存删除
     */
    public static void delete(String cacheName,String key){
        cache.evict(cacheName,key);
    }
    /**获取所有的key*/
    public static Collection<String> getAllKeys(){
        return cache.keys(SYS_CACHE_NAME);
    }

    public static Collection<String> getAllKeys(String cacheName){
        return cache.keys(cacheName);
    }

    /**清理缓存*/
    public static void clear(String cacheName) {
        cache.clear(cacheName);
    }

    /**
     *@date: 2020/5/28 上午8:46
     *@param:
     *@return:0：不存在   1：存在于一级缓存  2：存在于二级缓存
     *@Description:判断特定的key存在于哪一级缓存
     */
    public static int check(String region, String key) {
        return cache.check(region, key);
    }

    public static boolean exists(String region, String key) {
        return check(region, key) > 0;
    }

    public static void delByClass(String className, String methodName) {
        String key = StringUtils.keyGenerater(Constant.SYS_CACHE, className, methodName);

        delete(key);
    }
}
