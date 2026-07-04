/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package com.videogo.util;

import com.google.gson.Gson;
import com.videogo.util.LogUtil;

public class JsonTools {
    private static final String TAG = JsonTools.class.getSimpleName();

    public static <T> T fromJson(String jsonString, Class<T> targetClass) {
        Object target = null;
        try {
            target = new Gson().fromJson(jsonString, targetClass);
        }
        catch (Exception e) {
            LogUtil.e(TAG, "find wrong json string which can not match " + targetClass.getSimpleName());
            LogUtil.e(TAG, "wrong json string is as follow \n" + jsonString);
            e.printStackTrace();
        }
        return (T)target;
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}

