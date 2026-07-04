/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.videogo.util;

import android.text.TextUtils;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReflectionUtils {
    public static final String TAG = ReflectionUtils.class.getSimpleName();
    private static Map<String, ClassInfo> mClassInfoMap = new HashMap<String, ClassInfo>();

    public static ClassInfo getClassInfo(Class<?> clazz) {
        ClassInfo classInfo = mClassInfoMap.get(clazz.getName());
        if (classInfo == null) {
            classInfo = new ClassInfo();
            classInfo.fields = ReflectionUtils.getFields(clazz);
            mClassInfoMap.put(clazz.getName(), classInfo);
        }
        return classInfo;
    }

    public static void convJSONToObject(JSONObject jsonObject, Object object) {
        for (Field field : ReflectionUtils.getClassInfo(object.getClass()).fields) {
            String jsonName;
            Serializable annotation = field.getAnnotation(Serializable.class);
            if (annotation == null || TextUtils.isEmpty((CharSequence)annotation.name()) || !jsonObject.has(jsonName = annotation.name()) || jsonObject.isNull(jsonName)) continue;
            field.setAccessible(true);
            try {
                if (field.getType() == String.class) {
                    field.set(object, jsonObject.optString(jsonName));
                    continue;
                }
                if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    field.set(object, jsonObject.optInt(jsonName));
                    continue;
                }
                if (field.getType() == Long.TYPE || field.getType() == Long.class) {
                    field.set(object, jsonObject.optLong(jsonName));
                    continue;
                }
                if (field.getType() == Double.TYPE || field.getType() == Double.class) {
                    field.set(object, jsonObject.optDouble(jsonName));
                    continue;
                }
                if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                    field.set(object, jsonObject.optBoolean(jsonName));
                    continue;
                }
                if (field.getType() == Short.TYPE || field.getType() == Short.class) {
                    field.set(object, (short)jsonObject.optInt(jsonName));
                    continue;
                }
                annotation = field.getType().getAnnotation(Serializable.class);
                if (annotation != null) {
                    Object subObject = field.getType().newInstance();
                    ReflectionUtils.convJSONToObject(jsonObject.optJSONObject(jsonName), subObject);
                    field.set(object, subObject);
                    continue;
                }
                LogUtil.i(TAG, field.getName() + " " + field.toString());
                field.set(object, jsonObject.opt(jsonName));
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
    }

    public static JSONObject convObjectToJSON(Object object) {
        JSONObject result = new JSONObject();
        for (Field field : ReflectionUtils.getClassInfo(object.getClass()).fields) {
            Serializable annotation = field.getAnnotation(Serializable.class);
            if (annotation == null || TextUtils.isEmpty((CharSequence)annotation.name())) continue;
            String paramName = annotation.name();
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value == null) continue;
                if (value instanceof Iterable) {
                    Iterable iterable = (Iterable)value;
                    JSONArray array = new JSONArray();
                    for (Object obj : iterable) {
                        annotation = obj.getClass().getAnnotation(Serializable.class);
                        if (annotation != null) {
                            array.put((Object)ReflectionUtils.convObjectToJSON(obj));
                            continue;
                        }
                        array.put(obj);
                    }
                    result.put(paramName, (Object)array);
                    continue;
                }
                annotation = field.getType().getAnnotation(Serializable.class);
                if (annotation != null) {
                    result.put(paramName, (Object)ReflectionUtils.convObjectToJSON(value));
                    continue;
                }
                result.put(paramName, value);
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return result;
    }

    public static List<NameObjectParam> convObjectToParams(Object object) {
        ArrayList<NameObjectParam> result = new ArrayList<NameObjectParam>();
        for (Field field : ReflectionUtils.getClassInfo(object.getClass()).fields) {
            HttpParam annotation = field.getAnnotation(HttpParam.class);
            if (annotation == null || TextUtils.isEmpty((CharSequence)annotation.name())) continue;
            String paramName = annotation.name();
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value == null) continue;
                if (value instanceof Iterable) {
                    Iterable iterable = (Iterable)value;
                    int i = 0;
                    for (Object obj : iterable) {
                        result.add(new NameObjectParam(paramName + '[' + i + ']', obj));
                        ++i;
                    }
                    continue;
                }
                result.add(new NameObjectParam(paramName, value));
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return result;
    }

    public static Map<String, Object> convObjectToMap(Object object) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (NameObjectParam objectParam : ReflectionUtils.convObjectToParams(object)) {
            map.put(objectParam.name, objectParam.object);
        }
        return map;
    }

    private static final List<Field> getFields(Class<?> cls) {
        Field[] fields;
        ArrayList<Field> list = new ArrayList<Field>();
        for (Field field : fields = cls.getDeclaredFields()) {
            list.add(field);
        }
        Class superClass = (Class)cls.getGenericSuperclass();
        if (superClass != null) {
            list.addAll(ReflectionUtils.getFields(superClass));
        }
        return list;
    }

    public static Object fromJsonToJava(JSONObject json, Class pojo) throws Exception {
        Field[] fields = pojo.getDeclaredFields();
        Object obj = pojo.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                json.get(name);
            }
            catch (Exception ex) {
                LogUtil.printErrStackTrace(TAG, ex.fillInStackTrace());
                continue;
            }
            if (json.get(name) == null || "".equals(json.getString(name))) continue;
            if (field.getType().equals(Long.class) || field.getType().equals(Long.TYPE)) {
                field.set(obj, Long.parseLong(json.getString(name)));
                continue;
            }
            if (field.getType().equals(String.class)) {
                field.set(obj, json.getString(name));
                continue;
            }
            if (field.getType().equals(Double.class) || field.getType().equals(Double.TYPE)) {
                field.set(obj, Double.parseDouble(json.getString(name)));
                continue;
            }
            if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                field.set(obj, Integer.parseInt(json.getString(name)));
                continue;
            }
            if (!field.getType().equals(Date.class)) continue;
            field.set(obj, Date.parse(json.getString(name)));
        }
        return obj;
    }

    public static JSONObject fromJavaToJson(Object object) throws Exception {
        JSONObject result = new JSONObject();
        List<Field> fields = ReflectionUtils.getFields(object.getClass());
        for (Field field : fields) {
            String paramName = field.getName();
            if (TextUtils.isEmpty((CharSequence)paramName)) continue;
            field.setAccessible(true);
            Object value = field.get(object);
            if (value == null) continue;
            if (value instanceof Iterable) {
                Iterable iterable = (Iterable)value;
                int i = 0;
                for (Object obj : iterable) {
                    result.put(paramName + '[' + i + ']', obj);
                    ++i;
                }
                continue;
            }
            result.put(paramName, value);
        }
        return result;
    }

    public static class ClassInfo {
        public List<Field> fields;
    }

    public static class NameObjectParam {
        public String name;
        public Object object;

        public NameObjectParam(String name, Object object) {
            this.name = name;
            if (object instanceof Date) {
                Date date = (Date)object;
                SimpleDateFormat df = DateTimeUtil.getEZDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                this.object = df.format(date);
            } else {
                this.object = object;
            }
        }

        public String toString() {
            return this.name + "=" + this.object;
        }
    }
}

