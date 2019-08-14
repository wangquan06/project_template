package com.moft.sysauth.infrastructure.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BeanMapperUtil {
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
}
