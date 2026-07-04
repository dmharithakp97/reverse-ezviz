/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Process
 *  android.text.TextUtils
 *  android.util.Log
 *  com.ez.stream.JsonUtils
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.ezdclog;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.ez.stream.JsonUtils;
import com.videogo.ezdclog.params.BaseParams;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.ReflectionUtils;
import com.videogo.util.SharedPreferencesUtils;
import com.videogo.util.Utils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class EZDcLogManager {
    private static final String TAG = EZDcLogManager.class.getSimpleName();
    private static final String DEMO_BUNDLEID = "ezviz.ezopensdk";
    private String EZLOG_URL;
    private static EZDcLogManager dcLogManager;
    private ExecutorService cachedThreadPool = Executors.newSingleThreadExecutor();
    private boolean isNeedUpLoadLog = false;
    private boolean isUsingGlobalSDK;

    public void setNeedUpLoadLog(boolean needUpLoadLog) {
        this.isNeedUpLoadLog = needUpLoadLog;
    }

    public void setEZLOG_URL(String EZLOG_URL) {
        this.EZLOG_URL = EZLOG_URL;
    }

    public void setUsingGlobalSDK(boolean usingGlobalSDK) {
        this.isUsingGlobalSDK = usingGlobalSDK;
    }

    public static EZDcLogManager getInstance() {
        if (dcLogManager == null) {
            dcLogManager = new EZDcLogManager();
        }
        return dcLogManager;
    }

    private EZDcLogManager() {
    }

    public void submit(final BaseParams baseParams) {
        if (baseParams == null) {
            LogUtil.d(TAG, "submit baseParams = null");
            return;
        }
        String bundleId = LocalInfo.getInstance().getPackageName();
        if (DEMO_BUNDLEID.equals(bundleId)) {
            LogUtil.d(TAG, "app bundleId:" + bundleId + ",data report exit");
            return;
        }
        LogUtil.d(TAG, "isNeedUpLoadLog =" + this.isNeedUpLoadLog);
        if (!this.isNeedUpLoadLog) {
            return;
        }
        if (TextUtils.isEmpty((CharSequence)this.EZLOG_URL)) {
            LogUtil.d(TAG, "EZLOG_URL is null");
            return;
        }
        LogUtil.d(TAG, "EZLOG_URL = " + this.EZLOG_URL);
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                Process.setThreadPriority((int)19);
                try {
                    EZDcLogManager.this.ezlogPost(baseParams);
                }
                catch (Exception e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
            }
        });
    }

    public void submit(final BaseParams baseParams, final Object object) {
        if (baseParams == null) {
            LogUtil.d(TAG, "submit baseParams = null");
            return;
        }
        String bundleId = LocalInfo.getInstance().getPackageName();
        if (DEMO_BUNDLEID.equals(bundleId)) {
            LogUtil.d(TAG, "app bundleId:" + bundleId + ",data report exit");
            return;
        }
        LogUtil.d(TAG, "isNeedUpLoadLog =" + this.isNeedUpLoadLog);
        if (!this.isNeedUpLoadLog) {
            return;
        }
        if (TextUtils.isEmpty((CharSequence)this.EZLOG_URL)) {
            LogUtil.d(TAG, "EZLOG_URL is null");
            return;
        }
        LogUtil.d(TAG, "EZLOG_URL = " + this.EZLOG_URL);
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                Process.setThreadPriority((int)19);
                try {
                    EZDcLogManager.this.ezlogPost(baseParams, object);
                }
                catch (Exception e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
            }
        });
    }

    public void submit(final BaseParams baseParams, final String json) {
        if (baseParams == null || TextUtils.isEmpty((CharSequence)json)) {
            LogUtil.d(TAG, "submit baseParams = null or json is null");
            return;
        }
        String bundleId = LocalInfo.getInstance().getPackageName();
        if (DEMO_BUNDLEID.equals(bundleId)) {
            LogUtil.d(TAG, "app bundleId:" + bundleId + ",data report exit");
            return;
        }
        LogUtil.d(TAG, "isNeedUpLoadLog =" + this.isNeedUpLoadLog);
        if (!this.isNeedUpLoadLog) {
            return;
        }
        if (TextUtils.isEmpty((CharSequence)this.EZLOG_URL)) {
            LogUtil.d(TAG, "EZLOG_URL is null");
            return;
        }
        LogUtil.d(TAG, "EZLOG_URL = " + this.EZLOG_URL);
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                Process.setThreadPriority((int)19);
                try {
                    EZDcLogManager.this.ezlogPost(baseParams, json);
                }
                catch (Exception e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
            }
        });
    }

    private String ezlogPost(BaseParams param) {
        if (param == null) {
            return null;
        }
        this.addCommonParams(param);
        try {
            byte[] data;
            URL url = new URL(this.EZLOG_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            if (this.isUsingGlobalSDK) {
                String postJsonString = this.postRequestData(param);
                if (postJsonString == null || TextUtils.isEmpty((CharSequence)postJsonString)) {
                    return null;
                }
                data = postJsonString.getBytes();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            } else {
                StringBuffer stringBuffer = this.getRequestData(param);
                if (stringBuffer == null || TextUtils.isEmpty((CharSequence)stringBuffer.toString())) {
                    return null;
                }
                data = stringBuffer.toString().getBytes();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                LogUtil.d(TAG, "submit HTTP_OK");
                InputStream inptStream = httpURLConnection.getInputStream();
                String submitResponse = this.dealResponseResult(inptStream);
                LogUtil.i(TAG, "submitResponse is: " + submitResponse);
                return submitResponse;
            }
            LogUtil.d(TAG, "submit error, response code: " + response);
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        return null;
    }

    private String ezlogPost(BaseParams param, Object object) {
        if (param == null && object == null) {
            return null;
        }
        this.addCommonParams(param);
        try {
            byte[] data;
            URL url = new URL(this.EZLOG_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            if (this.isUsingGlobalSDK) {
                String postJsonString = this.postRequestData(param, object);
                if (postJsonString == null || TextUtils.isEmpty((CharSequence)postJsonString)) {
                    return null;
                }
                data = postJsonString.getBytes();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            } else {
                StringBuffer stringBuffer = this.getRequestData(param, object);
                if (stringBuffer == null || TextUtils.isEmpty((CharSequence)stringBuffer.toString())) {
                    return null;
                }
                data = stringBuffer.toString().getBytes();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                LogUtil.d(TAG, "submit HTTP_OK");
                InputStream inptStream = httpURLConnection.getInputStream();
                String submitResponse = this.dealResponseResult(inptStream);
                LogUtil.i(TAG, "submitResponse is: " + submitResponse);
                return submitResponse;
            }
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        return null;
    }

    private String ezlogPost(BaseParams param, String json) {
        if (param == null && TextUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        this.addCommonParams(param);
        try {
            byte[] data;
            URL url = new URL(this.EZLOG_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            if (this.isUsingGlobalSDK) {
                String postJsonString = this.postRequestData(param, json);
                if (postJsonString == null || TextUtils.isEmpty((CharSequence)postJsonString)) {
                    return null;
                }
                data = postJsonString.getBytes();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            } else {
                StringBuffer stringBuffer = this.getRequestData(param, json);
                if (stringBuffer == null || TextUtils.isEmpty((CharSequence)stringBuffer.toString())) {
                    return null;
                }
                data = stringBuffer.toString().getBytes();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                LogUtil.d(TAG, "submit HTTP_OK");
                InputStream inptStream = httpURLConnection.getInputStream();
                String submitResponse = this.dealResponseResult(inptStream);
                LogUtil.i(TAG, "submitResponse is: " + submitResponse);
                return submitResponse;
            }
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        return null;
    }

    private void addCommonParams(BaseParams param) {
        param.appId = PlayAPI.getInstance().getAppKey();
        param.macId = LocalInfo.getInstance().getHardwareCode();
        param.cltType = 13;
        param.sdkVer = "v5.27.3.20260319";
        param.ver = "v5.27.3.20260319";
        param.platAddr = LocalInfo.getInstance().getServAddr();
        param.exterVer = PlayAPI.getExterVer();
        param.startTime = DateTimeUtil.formatTimeToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    private StringBuffer getRequestData(BaseParams param) {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> result = this.convObjectToMaps(param);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                LogUtil.d(TAG, "getRequestData = " + stringBuffer.toString());
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return stringBuffer;
    }

    private StringBuffer getRequestData(BaseParams baseParams, Object object) {
        if (baseParams == null || object == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> result = this.convObjectToMaps(baseParams);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                Map<String, String> result1 = this.toMap(object);
                for (Map.Entry<String, String> entry : result1.entrySet()) {
                    if ("systemName".equalsIgnoreCase(entry.getKey())) continue;
                    stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                LogUtil.d(TAG, "getRequestData = " + stringBuffer.toString());
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return stringBuffer;
    }

    private StringBuffer getRequestData(BaseParams param, String json) {
        if (param == null || TextUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String> result = this.toMap(param);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        try {
            JSONObject jasonObject = new JSONObject(json);
            Iterator ite = jasonObject.keys();
            while (ite.hasNext()) {
                String key = ite.next().toString();
                String value = jasonObject.get(key).toString();
                if ("systemName".equalsIgnoreCase(key)) continue;
                stringBuffer.append(key).append("=").append(value).append("&");
            }
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        int index = stringBuffer.lastIndexOf("&");
        if (index == stringBuffer.length() - 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        LogUtil.d(TAG, "getRequestData = " + stringBuffer.toString());
        return stringBuffer;
    }

    private String postRequestData(BaseParams param) {
        JSONObject jsonObject = new JSONObject();
        Map<String, String> result = this.convObjectToMaps(param);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    jsonObject.put(entry.getKey(), (Object)entry.getValue());
                }
                LogUtil.d(TAG, "postRequestData = " + jsonObject.toString());
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return jsonObject.toString();
    }

    private String postRequestData(BaseParams baseParams, Object object) {
        if (baseParams == null || object == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Map<String, String> result = this.convObjectToMaps(baseParams);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    jsonObject.put(entry.getKey(), (Object)entry.getValue());
                }
                Map<String, String> result1 = this.toMap(object);
                for (Map.Entry<String, String> entry : result1.entrySet()) {
                    if ("systemName".equalsIgnoreCase(entry.getKey())) continue;
                    jsonObject.put(entry.getKey(), (Object)entry.getValue());
                }
                LogUtil.d(TAG, "postRequestData = " + jsonObject.toString());
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return jsonObject.toString();
    }

    private String postRequestData(BaseParams param, String json) {
        if (param == null || TextUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Map<String, String> result = this.toMap(param);
        if (result != null && result.size() > 0) {
            try {
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    jsonObject.put(entry.getKey(), (Object)entry.getValue());
                }
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        try {
            JSONObject jasonObject = new JSONObject(json);
            Iterator ite = jasonObject.keys();
            while (ite.hasNext()) {
                String key = ite.next().toString();
                String value = jasonObject.get(key).toString();
                if ("systemName".equalsIgnoreCase(key)) continue;
                jsonObject.put(key, (Object)value);
            }
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        LogUtil.d(TAG, "postRequestData = " + jsonObject.toString());
        return jsonObject.toString();
    }

    private Map<String, String> convObjectToMaps(Object object) {
        HashMap<String, String> result = new HashMap<String, String>();
        for (Field field : ReflectionUtils.getClassInfo(object.getClass()).fields) {
            HttpParam annotation = field.getAnnotation(HttpParam.class);
            if (annotation == null || TextUtils.isEmpty((CharSequence)annotation.name())) continue;
            String paramName = annotation.name();
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value == null) continue;
                result.put(paramName, String.valueOf(value));
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return result;
    }

    public Map<String, String> toMap(Object param) {
        String string = JsonUtils.toJson((Object)param);
        JSONObject jasonObject = null;
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            jasonObject = new JSONObject(string);
            Iterator ite = jasonObject.keys();
            while (ite.hasNext()) {
                String key = ite.next().toString();
                String value = jasonObject.get(key).toString();
                data.put(key, value);
            }
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        return data;
    }

    private String dealResponseResult(InputStream inputStream) {
        String resultData = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    private JSONObject post(String serverUrl, String url, Map maps, String auth) throws Exception {
        URL httpUrl = null;
        StringBuffer params = null;
        if (TextUtils.isEmpty((CharSequence)serverUrl)) {
            LogUtil.d("HttpUtils", "serverUrl is null ");
            return null;
        }
        if (TextUtils.isEmpty((CharSequence)url)) {
            LogUtil.d("HttpUtils", "url is null ");
            return null;
        }
        LogUtil.d("HttpUtils", "url  = " + url);
        httpUrl = new URL(serverUrl + url);
        if (httpUrl != null) {
            String line;
            LogUtil.d("HttpUtils", httpUrl.toString());
            if (null != maps && maps.size() > 0) {
                params = new StringBuffer();
                for (Map.Entry element : maps.entrySet()) {
                    params.append(element.getKey());
                    params.append("=");
                    params.append(element.getValue());
                    params.append("&");
                }
                if (params.length() > 0) {
                    params.deleteCharAt(params.length() - 1);
                }
            }
            if (params != null) {
                LogUtil.d("HttpUtils", "url param = " + params.toString());
            }
            HttpURLConnection conn = null;
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;
            JSONObject jsonObject = null;
            StringBuffer responseResult = new StringBuffer();
            conn = (HttpURLConnection)httpUrl.openConnection();
            if (!TextUtils.isEmpty((CharSequence)auth)) {
                conn.setRequestProperty("Authorization", auth);
            }
            conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            conn.setDoOutput(true);
            printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.write(params.toString());
            printWriter.flush();
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }
            LogUtil.d("HttpUtils", "responseResult  = " + responseResult.toString());
            jsonObject = new JSONObject(responseResult.toString().trim());
            return jsonObject;
        }
        return null;
    }

    public void uploadLogFiles(final Context context, final String appkey) {
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                if (SharedPreferencesUtils.isUploadLog(context) && !TextUtils.isEmpty((CharSequence)LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken())) {
                    File file = new File(context.getExternalCacheDir(), "ezxlog");
                    File[] files = file.listFiles();
                    EZDcLogManager.this.uploadFile(context, files, appkey);
                }
            }
        });
    }

    private void uploadFile(Context context, File[] uploadFilePath, String appkey) {
        if (uploadFilePath == null || uploadFilePath.length <= 0) {
            Log.d((String)TAG, (String)"uploadFilePath is null");
            return;
        }
        int ommId = (int)((Math.random() * 9.0 + 1.0) * 100000.0);
        Log.d((String)TAG, (String)("uploadFile ommId =  " + ommId));
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";
        InputStream in = null;
        StringBuilder sb2 = null;
        int TIME_OUT = 10000;
        String CHARSET = "utf-8";
        try {
            URL url = new URL(LocalInfo.getInstance().getServAddr() + "/investigation/info/upload");
            Log.d((String)TAG, (String)("uploadFile URL =  " + url));
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            HashMap<String, String> map = new HashMap<String, String>();
            String token = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
            map.put("accessToken", TextUtils.isEmpty((CharSequence)token) ? "" : token);
            map.put("ommUuid", String.valueOf(ommId));
            map.put("appKey", appkey);
            map.put("clientType", String.valueOf(13));
            map.put("clientVersion", Build.MODEL);
            String mNetType = Utils.getNetTypeName(context);
            if (TextUtils.isEmpty((CharSequence)mNetType)) {
                mNetType = "UNKNOWN";
            }
            map.put("netType", mNetType);
            map.put("osVersion", Build.VERSION.RELEASE);
            map.put("sdkVersion", "v5.27.3.20260319");
            StringBuffer stringBuffer1 = new StringBuffer();
            for (Map.Entry entry : map.entrySet()) {
                Log.d((String)TAG, (String)("uploadFile param  " + (String)entry.getKey() + " = " + (String)entry.getValue()));
                stringBuffer1.append(PREFIX);
                stringBuffer1.append(BOUNDARY);
                stringBuffer1.append(LINE_END);
                stringBuffer1.append("Content-Disposition: form-data; name=\"" + (String)entry.getKey() + "\"" + LINE_END);
                stringBuffer1.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                stringBuffer1.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                stringBuffer1.append(LINE_END);
                stringBuffer1.append((String)entry.getValue());
                stringBuffer1.append(LINE_END);
            }
            dos.write(stringBuffer1.toString().getBytes(CHARSET));
            for (int i = 0; i < uploadFilePath.length; ++i) {
                String fileName = uploadFilePath[i].getName();
                if (!fileName.endsWith(".xlog")) continue;
                StringBuilder stringBuilder = new StringBuilder(fileName.substring(0, fileName.lastIndexOf(".")));
                stringBuilder.append("_").append(String.valueOf(System.currentTimeMillis())).append(".xlog");
                fileName = stringBuilder.toString();
                Log.d((String)TAG, (String)("uploadFile fileName =  " + fileName));
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINE_END);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + LINE_END);
                sb1.append("Content-Type: application/octet-stream;chartset=" + CHARSET + LINE_END);
                sb1.append(LINE_END);
                dos.write(sb1.toString().getBytes());
                FileInputStream is = new FileInputStream(uploadFilePath[i]);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = ((InputStream)is).read(buffer)) != -1) {
                    dos.write(buffer, 0, len);
                }
                ((InputStream)is).close();
                dos.write(LINE_END.getBytes());
            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            int res = conn.getResponseCode();
            Log.d((String)TAG, (String)("uploadFile getResponseCode =  " + res));
            if (res == 200) {
                int ch;
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                sb2 = new StringBuilder();
                while ((ch = reader.read()) != -1) {
                    sb2.append((char)ch);
                }
                Log.d((String)TAG, (String)("uploadFile getResponseCode =  " + sb2.toString()));
                try {
                    JSONObject jsonObject = new JSONObject(sb2.toString());
                    if (jsonObject.optInt("code") == 200) {
                        SharedPreferencesUtils.setUploadLog(context, false);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            conn.disconnect();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

