package com.iefihz.cloud.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 封装json工具类
 *
 * @author He Zhifei
 * @date 2020/7/18 0:28
 */
public class JsonTools {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Java对象转字Json字符
     * @param o
     * @return
     */
    public static String toString(Object o) {
        if (o instanceof String) return o.toString();
        try {
            return MAPPER.writeValueAsString(o);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把InputStream的内容转为JavaBean
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return MAPPER.readValue(inputStream, tClass);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Json字符串转JavaBean
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return MAPPER.readValue(json, tClass);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Json字符串转List
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static  <T> List<T> toList(String json, Class<T> tClass) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Json字符串转Map
     * @param json
     * @param kClass
     * @param vClass
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (Exception e) {
        }
        return null;
    }
}
