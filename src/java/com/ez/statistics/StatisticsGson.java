/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.ExclusionStrategy
 *  com.google.gson.FieldAttributes
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package com.ez.statistics;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StatisticsGson {
    static ExclusionStrategy myExclusionStrategy = new ExclusionStrategy(){

        public boolean shouldSkipField(FieldAttributes fa) {
            return fa.getName().startsWith("__");
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };
    private static Gson gsonCustom = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy[]{myExclusionStrategy}).create();
    private static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gsonCustom.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return (T)gson.fromJson(json, clazz);
    }
}

