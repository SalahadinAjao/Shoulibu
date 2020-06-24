package org.example.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.example.Util.StringUtils;

/**
 * @Author: houlintao
 * @Date:2020/6/12 下午3:30
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class XmlUtils {


    /**
     * 将xml的字符串转换为bean
     */
    public static Object xmlStrToBean(String xmlStr,Class clazz){
        Object object = null;
        try {
            //先将xml字符串转换为map
            Map<String, Object> map = xmlStrToMap(xmlStr);
            //再将map转换为bean
            object = mapToBean(map,clazz);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return object;
    }




    /**
     * 将xml字符串封装进map
     * @param xmlStr xml字符串
     */
    public static Map<String,Object> xmlStrToMap(String xmlStr) throws DocumentException {

        if (StringUtils.isNullOrEmpty(xmlStr)){
            return null;
        }
        //声明一个map用于存放xml字符串数据
        Map<String,Object> map = new HashMap<String, Object>();
        //将xml字符串数据转换为Document对象
        /**
         * xmlStr中的数据是类似于 <xml><a>huj</a><b>gikuh</b><c>lll</c></xml> 这样的
         * Document可以将它们解析成对应的element
         */
        Document document = DocumentHelper.parseText(xmlStr);
        //获取根元素
        Element rootElement = document.getRootElement();
        //根元素下的所有子元素
        List elements = rootElement.elements();

        if (elements!=null && elements.size()>0){
            for (int i=0;i<elements.size();i++){
                Element element = (Element) elements.get(i);
                map.put(element.getName(),element.getTextTrim());
            }
        }
        return map;
    }



    /**
     * 将xml中的字符串数据封装进一个java bean中
     * @param xmlStr 待封装的xml字符串
     * @param clazz 创建java bean的类对象
     */
    public static Object xmlStrToJavaBean(String xmlStr,Class clazz){
        if (StringUtils.isNullOrEmpty(xmlStr)){
            return null;
        }
        Object object = null;
        Map<String,Object> map = new HashMap<String, Object>();
        /**将xml格式的字符串转换为Document对象*/
        Document document;
        try {
            document= DocumentHelper.parseText(xmlStr);
            //获取根节点
            Element rootElement = document.getRootElement();
            //调用elementToMap将此节点转换为map
            elementToMap(rootElement,map);
            //调用mapToBean将map对象的数据封装进bean
            object = mapToBean(map,clazz);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return object;
    }



    /**
     * 将Dome4j的element元素转换为一个map(也就是将element数据封装进map)
     * @param element 待转换的element元素
     * @param map 目标map
     */
    public static Map<String,Object> elementToMap(Element element,Map<String,Object> map){
         if (element==null || map==null){
             return null;
         }
        List children = element.elements();
        if (children != null && children.size()>0){
            for (int i=0;i<children.size();i++){
                Element child = (Element) children.get(i);
                //如果element元素内部还有子元素，就递归调用本函数
                if (child.elements()!=null && child.elements().size()>0){
                    elementToMap(child,map);
                }else {
                    //直接将元素的名称和值存入map
                    map.put(child.getName(),child.getTextTrim());
                }
            }
        }

        return map;
    }

   /**
    * 将给定的map转化为bean，本质还是通过给定的clazz初始化一个实例，然后通过解析map对象的数据填充
    * 此实例；
    * 思路是首先基于clazz在堆内存创建一个初始的instance对象，然后从map中取出其中的entry，通过此对象获取对应的key和value
    * @param map 存放数据的map对象
    * @param clazz 待转换的class
    */
   public static Object mapToBean(Map<String,Object> map,Class clazz) throws IllegalAccessException, InstantiationException {
       //根据clazz实例化一个初始对象
       Object instance = clazz.newInstance();
       if (map != null && map.size()>0){
           for (Map.Entry<String,Object> entry : map.entrySet()){

               String propertyName = entry.getKey();
               Object value = entry.getValue();
               //方法名
               String setMethodName = "set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
               //类的属性名
               Field field = getClassField(clazz, propertyName);
               if (field != null){
                   Class fieldType = field.getType();
                   //调用convertValType方法将map中取出的value转换为bean对象中属性值
                   value = convertValType(value,fieldType);

                   try {
                       //反射调用clazz的set方法为instance对应的属性赋值value
                       clazz.getMethod(setMethodName,field.getType()).invoke(instance,value);
                   } catch (InvocationTargetException e) {
                       e.printStackTrace();
                   } catch (NoSuchMethodException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
       return instance;
   }


    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     * @param value 原Object类型值
     * @param fieldTypeClass bean属性的类型
     */
    public static Object convertValType(Object value,Class fieldTypeClass){
        Object retValue=null;
        /**
         *  如果value是Long类型
         */
       if (fieldTypeClass.getName().equals(Long.class.getName()) || fieldTypeClass.getName().equals(long.class.getName())){
           retValue = Long.parseLong(value.toString());
           //如果value是Integer类型
       }else if (fieldTypeClass.getName().equals(Integer.class.getName()) || fieldTypeClass.getName().equals(int.class.getName())){
           retValue = Integer.parseInt(value.toString());
           //如果value是Float类型
       }else if (fieldTypeClass.getName().equals(Float.class.getName()) || fieldTypeClass.getName().equals(float.class.getName())){
           retValue = Float.parseFloat(value.toString());
           //如果value是Double类型
       }else if (fieldTypeClass.getName().equals(Double.class.getName()) || fieldTypeClass.getName().equals(double.class.getName())){
           retValue = Double.parseDouble(value.toString());
       }else {
           retValue = value;
       }
       return retValue;
    }

    /**
     * 获取指定类中的指定field，由于类之间存在继承，因此需考虑到
     * 继承的情况；
     * @param clazz 指定的类；
     * @param fieldName 指定的field名称；
     */
    public static Field getClassField(Class clazz , String fieldName){

        //如果clazz是Object类型
        if (clazz.getName().equals(Object.class.getName())){
            return null;
        }
        //若不是Object类型,获取此clazz对象中的所有声明的field，不包含继承来的field
        Field[] declaredFields = clazz.getDeclaredFields();
        //遍历数组，拿数组中的每一项都同给出的filedName对比，如果是则直接返回
        for (Field field : declaredFields){
            if (field.getName().equals(fieldName)){
                return field;
            }
        }
        //如果是通过继承，就在其父类中查找
        Class superclass = clazz.getSuperclass();
        if (superclass!=null){
            //递归调用此方法在其父类中查找
            return getClassField(superclass,fieldName);
        }
        //如果父类为null，则直接返回null
        return null;
    }

}
