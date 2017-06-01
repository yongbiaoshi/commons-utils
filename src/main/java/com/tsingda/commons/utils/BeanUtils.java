package com.tsingda.commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的javaBean与map的相互转换功能
 * 
 * @author shiyongbiao
 *
 */
public class BeanUtils {

    /**
     * java bean 转 map
     * 
     * @param obj
     *            java bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> bean2Map(Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String name = property.getName();
            Method reader = property.getReadMethod();
            Object value = reader.invoke(obj);
            map.put(name, value);
        }
        return map;
    }

    /**
     * map转java bean
     * 
     * @param map
     *            map
     * @param type
     *            需要转的类型
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> type) throws InstantiationException,
            IllegalAccessException, IntrospectionException, IllegalArgumentException, InvocationTargetException {
        if (map == null) {
            return null;
        }
        T obj = type.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }
        return obj;
    }
}
