/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum
 *  com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum
 *  com.ezviz.sdk.configwifi.EZWiFiConfigManager
 *  com.ezviz.sdk.configwifi.ap.ApConfigParam
 *  com.ezviz.sdk.configwifi.common.ConfigParamAbstract
 *  com.ezviz.sdk.configwifi.common.EZConfigWifiCallback
 *  com.ezviz.sdk.configwifi.finder.DeviceFindCallback
 *  com.ezviz.sdk.configwifi.finder.DeviceFindParam
 *  com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParam
 *  com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParamOld
 *  com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetTokenCallback
 *  com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback
 *  com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 */
package com.videogo.wificonfig;

import android.app.Application;
import android.content.Context;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfigManager;
import com.ezviz.sdk.configwifi.ap.ApConfigParam;
import com.ezviz.sdk.configwifi.common.ConfigParamAbstract;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.finder.DeviceFindCallback;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParam;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParamOld;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.ReflectionUtils;
import com.videogo.wificonfig.APWifiConfig;
import com.videogo.wificonfig.DeviceFinderFromPlatform;

public class ConfigWifiSdkManager {
    private static final String TAG = ConfigWifiSdkManager.class.getSimpleName();

    public static void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, APWifiConfig.APConfigCallback apConfigCallback) {
        LogUtil.i(TAG, "Enter startAPConfigWifiWithSsid 1");
        ConfigWifiSdkManager.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, "EZVIZ_" + deviceSerial, "EZVIZ_" + verifyCode, true, null, apConfigCallback);
    }

    public static void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, String deviceWifiSsid, String deviceWifiPwd, boolean isAutoConnectDeviceHotSpot, final String apiUrl, final APWifiConfig.APConfigCallback callback) {
        LogUtil.i(TAG, "Enter startAPConfigWifiWithSsid 0");
        final ApConfigParam param = new ApConfigParam();
        param.routerWifiSsid = ssid;
        param.routerWifiPwd = password;
        param.deviceSerial = deviceSerial;
        param.deviceVerifyCode = verifyCode;
        param.deviceHotspotSsid = deviceWifiSsid;
        param.deviceHotspotPwd = deviceWifiPwd;
        param.autoConnect = isAutoConnectDeviceHotSpot;
        EZWiFiConfigManager.startAPConfig((Application)BaseAPI.mApplication, (ApConfigParam)param, (EZConfigWifiCallback)new EZConfigWifiCallback(){

            public void onInfo(int code, String message) {
                super.onInfo(code, message);
                if (code == EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE.code) {
                    callback.reportInfo(EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE);
                    this.startQueryDeviceStatusFromPlatform((ConfigParamAbstract)param);
                }
            }

            public void onError(int code, String description) {
                super.onInfo(code, description);
                callback.OnError(code);
                if (code == EZConfigWifiErrorEnum.CONFIG_TIMEOUT.code) {
                    DeviceFinderFromPlatform.getInstance().stop();
                }
            }

            private void startQueryDeviceStatusFromPlatform(ConfigParamAbstract configParam) {
                DeviceFindParam findParam = new DeviceFindParam();
                findParam.serial = configParam.deviceSerial;
                findParam.wifiName = configParam.routerWifiSsid;
                findParam.wifiPwd = configParam.routerWifiPwd;
                DeviceFinderFromPlatform.getInstance().setCallback(new DeviceFindCallback(){

                    public void onFind(String deviceSerial) {
                        callback.reportInfo(EZConfigWifiInfoEnum.CONNECTED_TO_PLATFORM);
                    }

                    public void onTimeout(String deviceSerial) {
                        callback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
                    }
                });
                DeviceFinderFromPlatform.getInstance().setApiUrl(apiUrl);
                DeviceFinderFromPlatform.getInstance().start(findParam);
            }
        });
    }

    public static void stopAPConfigWifiWithSsid() {
        LogUtil.i(TAG, "Enter stopAPConfigWifiWithSsid");
        EZWiFiConfigManager.stopAPConfig();
    }

    public static void startAPHttpConfigWifiWithToken(String configToken, String ssid, String password, String deviceWifiSsid, String deviceWifiPwd, String lbsDomain, boolean isAutoConnectDeviceHotSpot, final APWifiConfig.APConfigCallback callback) {
        LogUtil.i(TAG, "Enter startAPHttpConfigWifiWithToken 0");
        ApConfigParam param = new ApConfigParam();
        param.configToken = configToken;
        param.routerWifiSsid = ssid;
        param.routerWifiPwd = password;
        param.deviceHotspotSsid = deviceWifiSsid;
        param.deviceHotspotPwd = deviceWifiPwd;
        param.lbsDomain = lbsDomain;
        param.autoConnect = isAutoConnectDeviceHotSpot;
        EZWiFiConfigManager.startAPHttpConfig((Application)BaseAPI.mApplication, (ApConfigParam)param, (EZConfigWifiCallback)new EZConfigWifiCallback(){

            public void onInfo(int code, String message) {
                super.onInfo(code, message);
                if (code == EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE.code) {
                    callback.reportInfo(EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE);
                }
            }

            public void onError(int code, String description) {
                super.onInfo(code, description);
                callback.OnError(code);
                if (code == EZConfigWifiErrorEnum.CONFIG_TIMEOUT.code) {
                    DeviceFinderFromPlatform.getInstance().stop();
                }
            }
        });
    }

    public static void stopAPHttpConfigWifiWithToken() {
        LogUtil.i(TAG, "Enter stopAPHttpConfigWifiWithToken");
        EZWiFiConfigManager.stopAPHttpConfig();
    }

    public static void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, EZOpenSDKListener.EZStartConfigWifiCallback callback) {
        ConfigWifiSdkManager.startConfigWifi(context, deviceSerial, ssid, password, mode, null, callback);
    }

    public static void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, final String apiUrl, final EZOpenSDKListener.EZStartConfigWifiCallback callback) {
        LogUtil.i(TAG, "Enter startConfigWifi  EZConfigWifiCallback");
        final MixedConfigParam param = new MixedConfigParam();
        param.routerWifiSsid = ssid;
        param.routerWifiPwd = password;
        param.deviceSerial = deviceSerial;
        param.mode = mode;
        EZWiFiConfigManager.startMixedConfig((Application)BaseAPI.mApplication, (MixedConfigParam)param, (EZConfigWifiCallback)new EZConfigWifiCallback(){

            public void onInfo(int code, String message) {
                super.onInfo(code, message);
                if (code == EZConfigWifiInfoEnum.CONNECTED_TO_WIFI.code) {
                    callback.onStartConfigWifiCallback(param.deviceSerial, EZConstants.EZWifiConfigStatus.DEVICE_WIFI_CONNECTED);
                    this.startQueryDeviceStatusFromPlatform((ConfigParamAbstract)param);
                }
            }

            public void onError(int code, String description) {
                super.onError(code, description);
                callback.onError(code, description);
                if (code == EZConfigWifiErrorEnum.CONFIG_TIMEOUT.code) {
                    DeviceFinderFromPlatform.getInstance().stop();
                    callback.onStartConfigWifiCallback(param.deviceSerial, EZConstants.EZWifiConfigStatus.TIME_OUT);
                }
            }

            private void startQueryDeviceStatusFromPlatform(final ConfigParamAbstract configParam) {
                DeviceFindParam findParam = new DeviceFindParam();
                findParam.serial = configParam.deviceSerial;
                findParam.wifiName = configParam.routerWifiSsid;
                findParam.wifiPwd = configParam.routerWifiPwd;
                DeviceFinderFromPlatform.getInstance().setCallback(new DeviceFindCallback(){

                    public void onFind(String deviceSerial) {
                        callback.onStartConfigWifiCallback(configParam.deviceSerial, EZConstants.EZWifiConfigStatus.DEVICE_PLATFORM_REGISTED);
                    }

                    public void onTimeout(String deviceSerial) {
                        callback.onStartConfigWifiCallback(configParam.deviceSerial, EZConstants.EZWifiConfigStatus.TIME_OUT);
                    }
                });
                DeviceFinderFromPlatform.getInstance().setApiUrl(apiUrl);
                DeviceFinderFromPlatform.getInstance().start(findParam);
            }
        });
    }

    public static boolean startConfigWifi(Context context, String ssid, String password, DeviceDiscoveryListener listener) {
        LogUtil.i(TAG, "Enter startConfigWifi  DeviceDiscoveryListener");
        MixedConfigParamOld param = new MixedConfigParamOld();
        param.routerWifiSsid = ssid;
        param.routerWifiPwd = password;
        EZWiFiConfigManager.startMixedConfigOld((Application)BaseAPI.mApplication, (MixedConfigParamOld)param, (DeviceDiscoveryListener)listener);
        return true;
    }

    public static void stopConfigWifi() {
        LogUtil.i(TAG, "Enter stopConfigWifi");
        EZWiFiConfigManager.stopMixedConfig();
    }

    public static void getNewApConfigToken(GetTokenCallback callback) {
        BaseInfo beanInfo = new BaseInfo();
        beanInfo.fixHttpToken();
        EZWiFiConfigManager.setApiUrl((String)BaseAPI.getInstance().getServerUrl());
        EZWiFiConfigManager.requestConfigToken((Application)BaseAPI.mApplication, (String)beanInfo.getAccessToken(), ReflectionUtils.convObjectToMap(beanInfo), (GetTokenCallback)callback);
    }

    public static void startNewApConfigWithToken(String token, String ssid, String password, String lbsDomain, StartNewApConfigCallback callback) {
        EZWiFiConfigManager.startNewApConfigWithToken((Application)BaseAPI.mApplication, (String)token, (String)ssid, (String)password, (String)lbsDomain, (StartNewApConfigCallback)callback);
    }

    public static void getAccessDeviceInfo(GetAccessDeviceInfoCallback callback) {
        EZWiFiConfigManager.getAccessDeviceInfo((Application)BaseAPI.mApplication, (GetAccessDeviceInfoCallback)callback);
    }

    public static void getAccessDeviceWifiList(GetDeviceWifiListCallback callback) {
        EZWiFiConfigManager.getAccessDeviceWifiList((Application)BaseAPI.mApplication, (GetDeviceWifiListCallback)callback);
    }

    public static void queryPlatformBindStatus(String deviceSerial, QueryPlatformBindStatusCallback callback) {
        BaseInfo beanInfo = new BaseInfo();
        beanInfo.fixDeviceToken(deviceSerial);
        EZWiFiConfigManager.setApiUrl((String)BaseAPI.getInstance().getServerUrl());
        EZWiFiConfigManager.queryPlatformBindStatus((Application)BaseAPI.mApplication, (String)beanInfo.getAccessToken(), (String)deviceSerial, ReflectionUtils.convObjectToMap(beanInfo), (boolean)BaseAPI.getInstance().isUsingGlobalSDK(), (QueryPlatformBindStatusCallback)callback);
    }

    public static void setDevRouteDomain(String devRouteDomain) {
        EZWiFiConfigManager.setDevRouteDomain((Application)BaseAPI.mApplication, (String)devRouteDomain);
    }
}

