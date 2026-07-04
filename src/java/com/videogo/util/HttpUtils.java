/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 */
package com.videogo.util;

import android.app.Application;
import android.content.Context;
import com.videogo.restful.NameValuePair;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static int mTimeOut = 20000;
    private static final String CLIENT_AGREEMENT = "TLS";
    private static final String CLIENT_TRUST_MANAGER = "X509";
    private static final String CLIENT_CERTIFICATE_ALIAS = "ca";
    private Context mContext = null;
    private static HttpUtils mHttpUtils = null;

    public static void init(Application application) {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils(application);
        }
    }

    public static HttpUtils getInstance() {
        return mHttpUtils;
    }

    private HttpUtils(Application application) {
        this.mContext = application.getApplicationContext();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String sendPostRequest(String path) {
        String response = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(mTimeOut);
            conn.setReadTimeout(mTimeOut);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            response = Utils.inputStreamToString(in);
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return response;
    }

    public static byte[] getPostParam(List<NameValuePair> nvp) {
        if (nvp == null) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder();
        if (nvp.size() > 0) {
            try {
                for (NameValuePair pair : nvp) {
                    stringBuffer.append(pair.getName()).append("=").append(pair.getValue()).append("&");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                LogUtil.d(TAG, "getRequestData = " + stringBuffer.toString());
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        try {
            String ret = URLEncoder.encode(stringBuffer.toString().trim(), "UTF-8");
            return stringBuffer.toString().trim().getBytes();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

