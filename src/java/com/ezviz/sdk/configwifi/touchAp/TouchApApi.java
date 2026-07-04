/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.text.TextUtils
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.FormBody$Builder
 *  okhttp3.MediaType
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.ezviz.sdk.configwifi.touchAp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.ezviz.http.bean.request.ConfigNewApRequest;
import com.ezviz.http.bean.resp.GetDeviceWifiListResp;
import com.ezviz.http.bean.resp.GetTokenResp;
import com.ezviz.http.bean.resp.NewApConfigResp;
import com.ezviz.http.core.EzvizCallback;
import com.ezviz.http.core.EzvizHttpClient;
import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.http.model.AccessDeviceInfo;
import com.ezviz.http.model.DeviceTokenInfo;
import com.ezviz.http.model.EzWifiInfo;
import com.ezviz.sdk.configwifi.Config;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.ezviz.utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class TouchApApi {
    private static TouchApApi mTouchApApi;
    private static EzvizHttpClient mEzvizHttpClient;
    public static String TAG;
    public static String devRouteDomain;
    public static String responseData;

    private TouchApApi(Application application) {
        mEzvizHttpClient = new EzvizHttpClient((Context)application);
        devRouteDomain = "http:/192.168.4.1:80";
    }

    public static synchronized void init(Application application) {
        if (mTouchApApi == null) {
            mTouchApApi = new TouchApApi(application);
        }
    }

    private static String commonParameter(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> en = map.entrySet();
        for (Map.Entry<String, Object> entry : en) {
            sb.append("&");
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public static void getToken(String accessToken, Map<String, Object> map, final GetTokenCallback callback) {
        String baseUrl = Config.baseUrl + "/api/service/device/add/userToken?accessToken=" + accessToken;
        if (map != null && map.size() > 0) {
            baseUrl = baseUrl + TouchApApi.commonParameter(map);
        }
        final String url = baseUrl;
        LogUtil.d(TAG, "Request  >>>  " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        mEzvizHttpClient.ezEnqueue(requestBuilder.build(), new EzvizCallback(){

            @Override
            public void onSuccess(String data) {
                LogUtil.d(TAG, "Response <<<  " + url + "\n" + data);
                responseData = data;
                GetTokenResp getTokenResp = JsonUtils.fromJson(data, GetTokenResp.class);
                if (getTokenResp.deviceTokenInfo != null && getTokenResp.deviceTokenInfo.token != null) {
                    callback.onSuccess(getTokenResp.deviceTokenInfo);
                } else {
                    try {
                        JSONObject rootObj = new JSONObject(data);
                        JSONObject dataObj = rootObj.getJSONObject("data");
                        DeviceTokenInfo tokenInfo = new DeviceTokenInfo();
                        if (dataObj.has("token")) {
                            tokenInfo.token = dataObj.getString("token");
                        }
                        if (dataObj.has("registerUrl")) {
                            tokenInfo.registerUrl = dataObj.getString("registerUrl");
                        }
                        callback.onSuccess(tokenInfo);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onException(EzConfigWifiException ezOpenException) {
                callback.onError(ezOpenException);
            }
        });
    }

    public static void queryPlatformBindStatus(String accessToken, String deviceSerial, Map<String, Object> map, boolean isGlobal, final QueryPlatformBindStatusCallback callback) {
        Request.Builder requestBuilder;
        String urlString = isGlobal ? "/api/lapp/device/info" : "/api/service/device/bind/status";
        final String baseUrl = Config.baseUrl + urlString;
        LogUtil.d(TAG, "Request  >>>  " + baseUrl);
        if (isGlobal) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            formBodyBuilder.add("deviceSerial", deviceSerial);
            LogUtil.d(TAG, "deviceSerial: " + deviceSerial);
            if (!TextUtils.isEmpty((CharSequence)accessToken)) {
                formBodyBuilder.add("accessToken", accessToken);
                LogUtil.d(TAG, "accessToken: " + accessToken);
            }
            for (String key : map.keySet()) {
                if (!TextUtils.isEmpty((CharSequence)accessToken) && key.equals("accessToken")) continue;
                LogUtil.d(TAG, key + ": " + map.get(key));
                formBodyBuilder.add(key, String.valueOf(map.get(key)));
            }
            requestBuilder = new Request.Builder().url(baseUrl).post((RequestBody)formBodyBuilder.build());
        } else {
            String url = baseUrl + "?deviceSerial=" + deviceSerial + "&accessToken=" + accessToken;
            if (map != null && map.size() > 0) {
                url = url + TouchApApi.commonParameter(map);
            }
            requestBuilder = new Request.Builder().url(url).get();
        }
        mEzvizHttpClient.ezEnqueue(requestBuilder.build(), new EzvizCallback(){

            @Override
            public void onSuccess(String data) {
                LogUtil.d(TAG, "Response <<<  " + baseUrl + "\n" + data);
                responseData = data;
                callback.onSuccess(true);
            }

            @Override
            public void onException(EzConfigWifiException ezOpenException) {
                ezOpenException.printStackTrace();
                callback.onError(ezOpenException);
            }
        });
    }

    public static void getAccessDeviceInfo(final GetAccessDeviceInfoCallback callback) {
        final String url = devRouteDomain + "/AccessDevInfo?format=json";
        LogUtil.d(TAG, "Request  >>>  " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        mEzvizHttpClient.newCall(requestBuilder.build()).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onError(new EzConfigWifiException(EZConfigWifiErrorEnum.NETWORK_EXCEPTION.code, e.getMessage()));
            }

            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                LogUtil.d(TAG, "Response <<<  " + url + "\n" + responseData);
                if (response.isSuccessful()) {
                    AccessDeviceInfo accessDeviceInfo = JsonUtils.fromJson(responseData, AccessDeviceInfo.class);
                    callback.onSuccess(accessDeviceInfo);
                } else {
                    callback.onError(new EzConfigWifiException(response.code(), "server exception"));
                }
            }
        });
    }

    public static void getAccessDeviceWifiList(final GetDeviceWifiListCallback callback) {
        final String url = devRouteDomain + "/PreNetwork/SecurityAndAccessPoint?format=json";
        LogUtil.d(TAG, "Request  >>>  " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        mEzvizHttpClient.newCall(requestBuilder.build()).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onError(new EzConfigWifiException(EZConfigWifiErrorEnum.NETWORK_EXCEPTION.code, e.getMessage()));
            }

            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                LogUtil.d(TAG, "Response <<<  " + url + "\n" + responseData);
                if (response.isSuccessful()) {
                    GetDeviceWifiListResp getDeviceWifiListResp = JsonUtils.fromJson(responseData, GetDeviceWifiListResp.class);
                    List<EzWifiInfo> ezWifiInfoList = getDeviceWifiListResp.ezWifiInfoList;
                    callback.onSuccess(ezWifiInfoList);
                } else {
                    callback.onError(new EzConfigWifiException(response.code(), "server exception"));
                }
            }
        });
    }

    public static void startNewApConfigWithToken(String token, String ssid, String password, String lbsDomain, final StartNewApConfigCallback callback) {
        final String url = devRouteDomain + "/PreNetwork/WifiConfig?format=json";
        ConfigNewApRequest.WifiInfo wifiInfo = new ConfigNewApRequest.WifiInfo();
        wifiInfo.ssid = ssid;
        wifiInfo.password = password;
        ConfigNewApRequest request = new ConfigNewApRequest();
        request.token = token;
        request.lbsDomain = lbsDomain;
        request.ezWifiInfo = wifiInfo;
        String postBody = JsonUtils.toJson(request);
        RequestBody requestBody = RequestBody.create((MediaType)MediaType.parse((String)"application/json; charset=utf-8"), (String)postBody);
        Request requestBuilder = new Request.Builder().url(url).put(requestBody).build();
        LogUtil.d(TAG, "Request  >>>  " + url + "\n" + postBody);
        mEzvizHttpClient.newCall(requestBuilder).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onError(new EzConfigWifiException(EZConfigWifiErrorEnum.NETWORK_EXCEPTION.code, e.getMessage()));
            }

            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                LogUtil.d(TAG, "Response <<<  " + url + "\n" + responseData);
                if (response.isSuccessful()) {
                    NewApConfigResp newApConfigResp = JsonUtils.fromJson(responseData, NewApConfigResp.class);
                    callback.onResponse(newApConfigResp.statusCode, newApConfigResp.statusDesc);
                } else {
                    callback.onError(new EzConfigWifiException(response.code(), "server exception"));
                }
            }
        });
    }

    public static void setDevRouteDomain(String domain) {
        devRouteDomain = domain;
    }

    static {
        TAG = "TouchApApi";
    }
}

