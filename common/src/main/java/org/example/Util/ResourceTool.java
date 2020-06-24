package org.example.Util;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午6:20
 * @email 437547058@qq.com
 * @Version 1.0
 * java资源包，参数处理工具类
 */
public class ResourceTool {
    private static ResourceTool RESOURCE_TOOL=null;
    private static ResourceBundle BUNDLE= java.util.ResourceBundle.getBundle("Shoulibu");

    public ResourceTool(){}

    /**
     *@date: 2020/6/1 下午6:28
     *@param properties 配置文件属性
     *@return:
     *@Description:工厂模式实现读取配置文件
     */
    public static ResourceTool getInstance(String properties){
        if (RESOURCE_TOOL == null){
            RESOURCE_TOOL = new ResourceTool();
        }
        if (properties != null){
            BUNDLE = java.util.ResourceBundle.getBundle(properties);
        }
        return RESOURCE_TOOL;
    }

    public static ResourceTool getInstance() {
        if (RESOURCE_TOOL == null) {
            RESOURCE_TOOL = new ResourceTool();
        }
        return RESOURCE_TOOL;
    }
    /**
     *@date: 2020/6/1 下午6:43
     *@param name 配置文件参数
     *@return:
     *@Description:根据名称获取配置文件参数
     */
    public static String getConfigPropertyByName(String name){
        String value = "";
        try {
            value = new String(BUNDLE.getString(name).getBytes("iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 取得分隔符
     * @return 分隔符
     */
    public static String getSeparator() {
        return System.getProperty("file.separator");
    }

}
