package com.lb.im.common.cache.distribute.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;

import java.util.Collection;

/**
 * 类型转换工具类
 */
public class TypeConversion {

    /**
     * 判断对象是否为集合类型
     * @param t 待判断的对象
     * @return 如果是集合类型返回true，否则返回false
     */
    public static <T> boolean isCollectionType(T t){
        return t instanceof Collection;
    }

    /**
     * 判断对象是否为简单类型
     * 简单字符串和基本类型统称为简单类型
     * @param t 待判断的对象
     * @return 如果是简单类型返回true，否则返回false
     */
    public static <T> boolean isSimpleType(T t){
        return isSimpleString(t) || isInt(t) || isLong(t) || isDouble(t) || isFloat(t) || isChar(t) || isBoolean(t) || isShort(t) || isByte(t);
    }

    /**
     * 判断字符串是否为简单字符串
     * 简单字符串定义为非JSON格式的字符串
     * @param t 待判断的对象
     * @return 如果是简单字符串返回true，否则返回false
     */
    public static <T> boolean isSimpleString(T t){
        if (t == null || !isString(t)){
            return false;
        }
        return !JSONUtil.isJson(t.toString());
    }

    /**
     * 判断对象是否为字符串类型
     * @param t 待判断的对象
     * @return 如果是字符串类型返回true，否则返回false
     */
    public static <T> boolean isString(T t) {
        return t instanceof String;
    }

    /**
     * 判断对象是否为字节类型
     * @param t 待判断的对象
     * @return 如果是字节类型返回true，否则返回false
     */
    public static <T> boolean isByte(T t) {
        return t instanceof Byte;
    }

    /**
     * 判断对象是否为短整型
     * @param t 待判断的对象
     * @return 如果是短整型返回true，否则返回false
     */
    public static <T> boolean isShort(T t) {
        return t instanceof Short;
    }

    /**
     * 判断对象是否为整型
     * @param t 待判断的对象
     * @return 如果是整型返回true，否则返回false
     */
    public static <T> boolean isInt(T t) {
        return t instanceof Integer;
    }

    /**
     * 判断对象是否为长整型
     * @param t 待判断的对象
     * @return 如果是长整型返回true，否则返回false
     */
    public static <T> boolean isLong(T t) {
        return t instanceof Long;
    }

    /**
     * 判断对象是否为字符类型
     * @param t 待判断的对象
     * @return 如果是字符类型返回true，否则返回false
     */
    public static <T> boolean isChar(T t) {
        return t instanceof Character;
    }

    /**
     * 判断对象是否为浮点型
     * @param t 待判断的对象
     * @return 如果是浮点型返回true，否则返回false
     */
    public static <T> boolean isFloat(T t) {
        return t instanceof Float;
    }

    /**
     * 判断对象是否为双精度浮点型
     * @param t 待判断的对象
     * @return 如果是双精度浮点型返回true，否则返回false
     */
    public static <T> boolean isDouble(T t) {
        return t instanceof Double;
    }

    /**
     * 判断对象是否为布尔类型
     * @param t 待判断的对象
     * @return 如果是布尔类型返回true，否则返回false
     */
    public static <T> boolean isBoolean(T t) {
        return t instanceof Boolean;
    }

    /**
     * 获取对象的类类型
     * @param t 对象
     * @return 对象的类类型
     */
    public static <T> Class<?> getClassType(T t) {
        return t.getClass();
    }

    /**
     * 将字符串转换为指定类型
     * @param str 待转换的字符串
     * @param type 目标类型
     * @param <R> 目标类型的泛型参数
     * @return 转换后的对象
     */
    public static <R> R convertor(String str, Class<R> type){
        return Convert.convert(type, str);
    }

}
