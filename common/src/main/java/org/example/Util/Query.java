package org.example.Util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午2:51
 * @email 437547058@qq.com
 * @Version 1.0
 * 查询参数类，继承一个LinkedHashMap，是一个有序哈希表结构，用于在mybatis框架下在查询
 * 的时候保存一些查询参数
 */
public class Query extends LinkedHashMap<String,Object> {

    public static final long serialVersionUID = 1L;
    //当前页码，第X页
    private int page;
    //每页查询条数
    private int limit;

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    //构造函数传入一个哈系表参数
    public Query(Map<String,Object> map){
        //将map中的键值对存入一个有序哈希表中
        this.putAll(map);
        //使用传入的参数map中的page和limit键对应的值给query对象的page和limit赋值
        this.page = Integer.parseInt(map.get("page").toString());
        this.limit = Integer.parseInt(map.get("limit").toString());

        this.put("offset",(page-1)*limit);
        this.put("page",page);
        this.put("limit",limit);

        //由于sidx、order是通过拼接SQL实现排序的，会有SQL注入风险，因此在此处需要防止SQL注入
        String sidx = map.get("sidx").toString();
        String order = map.get("order").toString();
        //调用SqlFilter防止sql注入
        this.put("sidx",SqlFilter.sqlInject(sidx));
        this.put("order",SqlFilter.sqlInject(order));
    }

}
