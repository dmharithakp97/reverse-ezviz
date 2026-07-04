/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 *  android.content.Context
 *  android.text.TextUtils
 *  com.ez.stream.JsonUtils
 *  com.ezviz.http.exception.EzConfigWifiException
 *  com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetTokenCallback
 *  com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback
 *  com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.ez.stream.JsonUtils;
import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.opensdk.data.EZDatabaseHelper;
import com.ezviz.opensdk.data.EZDatabaseManager;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.EZOpenSDKErrorManager;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.openapi.model.ApiResponse;
import com.videogo.openapi.model.req.WebLoginReq;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.wificonfig.APWifiConfig;
import com.videogo.wificonfig.ConfigWifiSdkManager;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint(value={"MissingPermission"})
public class EzvizAPI
extends PlayAPI {
    public static final String TAG = "EzvizAPI";
    private static EzvizAPI mEzvizAPI = null;

    public EzvizAPI(Application application, String appKey, boolean isUsingGlobal) {
        super(application, appKey, isUsingGlobal);
        EZDatabaseManager.initializeInstance(new EZDatabaseHelper(application.getApplicationContext()));
    }

    public static synchronized void init(Application application, String appKey, boolean isUsingGlobal) {
        if (mEzvizAPI == null) {
            mEzvizAPI = new EzvizAPI(application, appKey, isUsingGlobal);
        } else {
            if (!appKey.equalsIgnoreCase(mEzvizAPI.getAppKey())) {
                EzvizAPI.getInstance().setAccessToken("");
            }
            mEzvizAPI.setUsingGlobalSDK(isUsingGlobal);
            mEzvizAPI.setAppKey(appKey);
        }
    }

    public static EzvizAPI getInstance() {
        return mEzvizAPI;
    }

    @Override
    protected void releaseSDK() {
        super.releaseSDK();
        mEzvizAPI = null;
    }

    @Override
    public void initErrorInfoList() {
        EZOpenSDKErrorManager.getMamager().getEZOpenSDKErrorInfoList(EZDatabaseManager.getInstance().getErrorTableVersion());
    }

    public void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, String apiUrl, EZOpenSDKListener.EZStartConfigWifiCallback callback) {
        LogUtil.i(TAG, "Enter startConfigWifi  EZConfigWifiCallback");
        ConfigWifiSdkManager.startConfigWifi(context, deviceSerial, ssid, password, mode, apiUrl, callback);
    }

    public boolean startConfigWifi(Context context, String ssid, String password, DeviceDiscoveryListener l) {
        LogUtil.i(TAG, "Enter startConfigWifi  DeviceDiscoveryListener");
        ConfigWifiSdkManager.startConfigWifi(context, ssid, password, l);
        return true;
    }

    public boolean stopConfigWiFi() {
        LogUtil.i(TAG, "Enter stopConfigWiFi");
        ConfigWifiSdkManager.stopConfigWifi();
        return true;
    }

    public void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, APWifiConfig.APConfigCallback apConfigCallback) {
        ConfigWifiSdkManager.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, apConfigCallback);
    }

    public void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, String routerName, String routerPassword, boolean isAutoConnectDeviceHotSpot, APWifiConfig.APConfigCallback apConfigCallback) {
        ConfigWifiSdkManager.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, routerName, routerPassword, isAutoConnectDeviceHotSpot, null, apConfigCallback);
    }

    public void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, String routerName, String routerPassword, boolean isAutoConnectDeviceHotSpot, String apiUrl, APWifiConfig.APConfigCallback apConfigCallback) {
        ConfigWifiSdkManager.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, routerName, routerPassword, isAutoConnectDeviceHotSpot, apiUrl, apConfigCallback);
    }

    public void stopAPConfigWifiWithSsid() {
        ConfigWifiSdkManager.stopAPConfigWifiWithSsid();
    }

    public void startAPHttpConfigWifiWithToken(String configToken, String ssid, String password, String routerName, String routerPassword, String lbsDomain, boolean isAutoConnectDeviceHotSpot, APWifiConfig.APConfigCallback apConfigCallback) {
        ConfigWifiSdkManager.startAPHttpConfigWifiWithToken(configToken, ssid, password, routerName, routerPassword, lbsDomain, isAutoConnectDeviceHotSpot, apConfigCallback);
    }

    public void stopAPHttpConfigWifiWithToken() {
        ConfigWifiSdkManager.stopAPHttpConfigWifiWithToken();
    }

    public void getNewApConfigToken(GetTokenCallback callback) {
        ConfigWifiSdkManager.getNewApConfigToken(callback);
    }

    public void startNewApConfigWithToken(String token, String ssid, String password, String lbsDomain, StartNewApConfigCallback callback) {
        ConfigWifiSdkManager.startNewApConfigWithToken(token, ssid, password, lbsDomain, callback);
    }

    public void getAccessDeviceInfo(GetAccessDeviceInfoCallback callback) {
        ConfigWifiSdkManager.getAccessDeviceInfo(callback);
    }

    public void getAccessDeviceWifiList(GetDeviceWifiListCallback callback) {
        ConfigWifiSdkManager.getAccessDeviceWifiList(callback);
    }

    public void queryPlatformBindStatus(String deviceSerial, QueryPlatformBindStatusCallback callback) {
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            if (callback != null) {
                callback.onError(new EzConfigWifiException(errorInfo.errorCode, "\u53c2\u6570\u9519\u8bef!"));
            }
            return;
        }
        ConfigWifiSdkManager.queryPlatformBindStatus(deviceSerial, callback);
    }

    public void setDevRouteDomain(String devRouteDomain) {
        ConfigWifiSdkManager.setDevRouteDomain(devRouteDomain);
    }

    @Override
    public void setVparamForLoginPage(String vParam) {
        WebLoginReq.setmVparam(vParam);
    }

    public EZProbeDeviceInfoResult probeDeviceInfo(final String deviceSerial, String deviceType) {
        LogUtil.i(TAG, "Enter probeDeviceInfo");
        EZProbeDeviceInfoResult ezProbeDeviceInfoResult = new EZProbeDeviceInfoResult();
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
            ezProbeDeviceInfoResult.setBaseException(new BaseException("\u53c2\u6570\u9519\u8bef!", errorInfo));
            return ezProbeDeviceInfoResult;
        }
        final String modelType = TextUtils.isEmpty((CharSequence)deviceType) ? "" : deviceType;
        Object result = null;
        try {
            String urlString = "/api/device/searchDeviceInfo";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="deviceSerial")
                private String mDeviceSerial;
                @HttpParam(name="model")
                private String model;
                {
                    this.mDeviceSerial = deviceSerial;
                    this.model = modelType;
                }
            };
            baseInfo.fixDeviceToken(deviceSerial);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String resultresponse) {
                    EZProbeDeviceInfoResult ezProbeDeviceInfoResult = new EZProbeDeviceInfoResult();
                    if (resultresponse == null) {
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                        ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
                        ezProbeDeviceInfoResult.setBaseException(new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                        return ezProbeDeviceInfoResult;
                    }
                    JSONObject response = null;
                    try {
                        response = new JSONObject(resultresponse);
                        String result = response.optString("result");
                        JSONObject jsonObjectresult = null;
                        jsonObjectresult = new JSONObject(result);
                        if (jsonObjectresult != null) {
                            int resultCode = jsonObjectresult.optInt("code");
                            String resultDesc = jsonObjectresult.optString("description");
                            if (resultCode == 200) {
                                JSONObject data = jsonObjectresult.optJSONObject("data");
                                if (data == null) {
                                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                                    ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
                                    ezProbeDeviceInfoResult.setBaseException(new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                                    return ezProbeDeviceInfoResult;
                                }
                                EZProbeDeviceInfo probeDeviceInfo = (EZProbeDeviceInfo)JsonUtils.fromJson((String)data.toString(), EZProbeDeviceInfo.class);
                                ezProbeDeviceInfoResult.setEZProbeDeviceInfo(probeDeviceInfo);
                                ezProbeDeviceInfoResult.setBaseException(null);
                                return ezProbeDeviceInfoResult;
                            }
                            if (resultCode == 400030) {
                                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                                ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
                                ezProbeDeviceInfoResult.setBaseException(new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                                return ezProbeDeviceInfoResult;
                            }
                            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, resultCode);
                            if (TextUtils.isEmpty((CharSequence)errorInfo.description)) {
                                errorInfo.description = resultDesc;
                            }
                            BaseException baseException = new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
                            ezProbeDeviceInfoResult.setBaseException(baseException);
                            JSONObject data = jsonObjectresult.optJSONObject("data");
                            if (data != null) {
                                EZProbeDeviceInfo probeDeviceInfo = (EZProbeDeviceInfo)JsonUtils.fromJson((String)data.toString(), EZProbeDeviceInfo.class);
                                ezProbeDeviceInfoResult.setEZProbeDeviceInfo(probeDeviceInfo);
                            }
                            return ezProbeDeviceInfoResult;
                        }
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                        ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
                        ezProbeDeviceInfoResult.setBaseException(new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                        return ezProbeDeviceInfoResult;
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                        ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
                        ezProbeDeviceInfoResult.setBaseException(new BaseException("IO Error", errorInfo.errorCode, errorInfo));
                        return ezProbeDeviceInfoResult;
                    }
                }
            });
        }
        catch (BaseException e) {
            e.printStackTrace();
            ezProbeDeviceInfoResult.setEZProbeDeviceInfo(null);
            ezProbeDeviceInfoResult.setBaseException(e);
            return ezProbeDeviceInfoResult;
        }
        if (result != null) {
            LogUtil.i(TAG, ((EZProbeDeviceInfoResult)result).toString());
            return (EZProbeDeviceInfoResult)result;
        }
        return null;
    }

    public EZProbeDeviceInfoResult probeDeviceInfo(String deviceSerial, String deviceType, String apiUrl) {
        String serverUrl = BaseAPI.getInstance().getServerUrl();
        if (apiUrl != null && apiUrl.length() > 0) {
            LocalInfo.getInstance().setServAddr(apiUrl);
        }
        EZProbeDeviceInfoResult result = this.probeDeviceInfo(deviceSerial, deviceType);
        LocalInfo.getInstance().setServAddr(serverUrl);
        return result;
    }
}

