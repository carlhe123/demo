package com.carl.util.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private final static Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.DisableCircularReferenceDetect
    };
    private static SerializeConfig config;

    static {
        config = new SerializeConfig();
        // JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormatSerializer df = new SimpleDateFormatSerializer(
                "yyyy-MM-dd HH:mm:ss");
        config.put(java.util.Date.class, df);
        config.put(java.sql.Date.class, df);
        config.put(Timestamp.class, df);
    }

    /***
     * 序列化数据
     *
     * @param object
     * @return
     */
    public static String toJSON(Object object) {
        return JSON.toJSONString(object, features);
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseNotThrowException(String json, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return t;
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArrayNotThrowException(String json,
                                                          Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;
    }

    // 处理结果字符串，转换为map
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> res(String result) {
        List<Map<String, Object>> resList = null;
        try {
            resList = JsonUtil.parse(result, List.class);
        } catch (Exception e) {
            log.error("解析返回Json发生异常,原因:{}",e.getMessage());
        }
        return resList;
    }
}