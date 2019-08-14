package com.moft.sysauth.infrastructure.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by EalenXie on 2018/8/16 10:21.
 * (枚举单例常用工具类)Map与Java对象互转(包含处理和不处理驼峰)
 */
public enum MapBeanUtil {
    getMapUtil;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final Gson gson = create();

    private final static Gson create() {
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Date date = null;
                if(json.getAsJsonPrimitive().isNumber()) {
                    date = new Date(json.getAsJsonPrimitive().getAsLong());
                } else if (json.getAsJsonPrimitive().isString()) {
                    String strDate = json.getAsJsonPrimitive().getAsString();
                    try {
                        SimpleDateFormat formatter = null;
                        if(strDate.length() <= 10) {
                            formatter = new SimpleDateFormat(DATE_FORMAT);
                        } else {
                            formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
                        }
                        date = formatter.parse(strDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return date;
            }
        });

        return builder.setDateFormat(DATE_TIME_FORMAT).create();
    }

    public static <T> String convertToJson(T bean) {
        return gson.toJson(bean);
    }

    public static String convertToMap(Map map) {
        return gson.toJson(map);
    }

    public static <T> T convertToBean(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T, V> V convertToBean(T bean, Class<V> classOfT) {
        return gson.fromJson(gson.toJson(bean), classOfT);
    }

    public static <T, V> List<V> convertToBeanList(List<T> list, Class<V> classOfT) {
        String jsonStr = gson.toJson(list);

        Type listType = new TypeToken<List<V>>(){}.getType();
        return gson.fromJson(jsonStr, listType);
    }

    /**
     * @param clazz 要传成的JavaBean对象(完全映射转换)
     * @param map   要转的Map(包含Key,Value)
     * @return 将一个Map转换成一个JavaBean(Map中的Key和传出来的JavaBean的属性名完全一致)
     */
    public static <T> T mapToBean(Class<T> clazz, Map map) {
        T result = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            result = clazz.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    if ("".equals(value)) value = null;
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(result, args);
                }
            }
        } catch (IllegalAccessException | InstantiationException | IntrospectionException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param clazz 要传成的JavaBean对象(数据Map中的key包含下划线,转成JavaBean的驼峰字段属性)
     * @param map   要转的Map(包含Key,Value)
     * @return 将一个Map转换成一个JavaBean(Map中的Key包含下划线, 下划线转成驼峰)
     */
    public static <T> T mapCamelToBean(Class<T> clazz, Map map) {
        T result = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            result = clazz.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                String camelToUnderlinePropertyName = camelToUnderline(propertyName);
                if (map.containsKey(propertyName) || map.containsKey(camelToUnderlinePropertyName)) {
                    Object value = map.get(propertyName);
                    if (value == null) value = map.get(camelToUnderlinePropertyName);
                    if ("".equals(value)) value = null;
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(result, args);
                }
            }
        } catch (IllegalAccessException | InstantiationException | IntrospectionException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param bean 要转化的JavaBean 对象
     * @return 将一个 JavaBean 对象转化为一个 Map(属性与Map的key完全一致)
     */
    public static Map beanToMap(Object bean) {
        Map<Object, Object> resultMap = new HashMap<>();
        try {
            Class<?> clazz = bean.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    if (null != result) result = result.toString();
                    resultMap.put(propertyName, result);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @param bean 要转化的JavaBean对象
     * @return 将一个JavaBean对象转化为一个Map(驼峰属性被转成带下划线的key)
     */
    public static Map beanToMapUnderLine(Object bean) {
        Map<Object, Object> resultMap = new HashMap<>();
        try {
            Class<?> clazz = bean.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    if (null != result) result = result.toString();
                    resultMap.put(camelToUnderline(propertyName), result);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @return 将驼峰字符串转换成下划线 如: userId 转成 user_id
     */
    public static String camelToUnderline(String origin) {
        return stringProcess(origin, (prev, c) -> {
                    if (Character.isLowerCase(prev) && Character.isUpperCase(c)) return "" + "_" + Character.toLowerCase(c);
                    return "" + c;
                }
        );
    }

    /**
     * @return 将下划线转成驼峰字符串 如: user_id转成userId
     */
    public static String underlineToCamel(String origin) {
        return stringProcess(origin, (prev, c) -> {
                    if (prev == '_' && Character.isLowerCase(c)) return "" + Character.toUpperCase(c);
                    if (c == '_') return "";
                    return "" + c;
                }
        );
    }

    public static String stringProcess(String origin, BiFunction<Character, Character, String> convertFunc) {
        if (origin == null || "".equals(origin.trim())) return "";
        String newOrigin = "0" + origin;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newOrigin.length() - 1; i++) {
            char prev = newOrigin.charAt(i);
            char c = newOrigin.charAt(i + 1);
            sb.append(convertFunc.apply(prev, c));
        }
        return sb.toString();
    }

}
