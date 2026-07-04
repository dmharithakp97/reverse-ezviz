/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package com.ezviz.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.lang.reflect.Type;

public class JsonUtils {
    private static Gson gson;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Gson getGson() {
        if (gson != null) return gson;
        Class<JsonUtils> clazz = JsonUtils.class;
        synchronized (JsonUtils.class) {
            if (gson != null) return gson;
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").enableComplexMapKeySerialization().serializeNulls().setPrettyPrinting().disableHtmlEscaping();
            gson = builder.create();
            // ** MonitorExit[var0] (shouldn't be in output)
            return gson;
        }
    }

    public static String toJson(Object object) {
        return JsonUtils.getGson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return (T)JsonUtils.getGson().fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return (T)JsonUtils.getGson().fromJson(json, type);
    }

    public static <T> T fromJson(Reader json, Class<T> clazz) {
        return (T)JsonUtils.getGson().fromJson(json, clazz);
    }

    public static <T> T fromJson(Reader json, Type type) {
        return (T)JsonUtils.getGson().fromJson(json, type);
    }
}

