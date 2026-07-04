/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.util.Log
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 */
package com.ezviz.sdk.configwifi;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.ezviz.sdk.configwifi.Config;
import com.ezviz.sdk.configwifi.ConfigWifiApi;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.ap.ApConfigParam;
import com.ezviz.sdk.configwifi.common.ConfigWifiTaskManager;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.finder.DeviceFindCallback;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;
import com.ezviz.sdk.configwifi.finder.DeviceFinderFromLan;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigMode;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParam;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParamOld;
import com.ezviz.sdk.configwifi.smartconfig.SmartConfigParam;
import com.ezviz.sdk.configwifi.soundwave.SoundWaveConfigParam;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.ezviz.sdk.configwifi.touchAp.TouchApApi;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EZWiFiConfigManager {
    private static final String TAG = EZWiFiConfigManager.class.getSimpleName();
    private static DeviceFinderFromLan mDeviceFinderFromLan;
    private static Timer mConfigTimer;
    private static boolean isInit;
    private static long mTimeoutMs;

    private EZWiFiConfigManager() {
    }

    public static void startAPConfig(Application app, ApConfigParam param, final EZConfigWifiCallback callback) {
        Log.i((String)TAG, (String)"Enter startAPConfig");
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        boolean failedToStart = false;
        try {
            ConfigWifiApi.startAPConfig(app, param, new EZConfigWifiCallback(){

                @Override
                public void onInfo(int code, String message) {
                    super.onInfo(code, message);
                    callback.onInfo(code, message);
                    if (code == EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE.code) {
                        EZWiFiConfigManager.stopAPConfig();
                    }
                }

                @Override
                public void onError(int code, String description) {
                    super.onError(code, description);
                    callback.onError(code, description);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
        }
        if (failedToStart) {
            LogUtil.e(TAG, "failed to call startAPConfig");
            return;
        }
        EZWiFiConfigManager.resetTimer(mTimeoutMs, new Runnable(){

            @Override
            public void run() {
                LogUtil.e(TAG, "ap config timeout");
                EZWiFiConfigManager.stopAPConfig();
                callback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
            }
        });
    }

    public static void stopAPConfig() {
        Log.i((String)TAG, (String)"Enter stopAPConfig");
        if (isInit) {
            EZWiFiConfigManager.unInit();
        }
        ConfigWifiApi.stopAPConfig();
    }

    public static void startAPHttpConfig(Application app, ApConfigParam param, final EZConfigWifiCallback callback) {
        Log.i((String)TAG, (String)"Enter startAPHttpConfig");
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        boolean failedToStart = false;
        try {
            ConfigWifiApi.startAPHttpConfig(app, param, new EZConfigWifiCallback(){

                @Override
                public void onInfo(int code, String message) {
                    super.onInfo(code, message);
                    callback.onInfo(code, message);
                    if (code == EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE.code) {
                        EZWiFiConfigManager.stopAPHttpConfig();
                    }
                }

                @Override
                public void onError(int code, String description) {
                    super.onError(code, description);
                    callback.onError(code, description);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
        }
        if (failedToStart) {
            LogUtil.e(TAG, "failed to call startAPHttpConfig");
            return;
        }
        EZWiFiConfigManager.resetTimer(mTimeoutMs, new Runnable(){

            @Override
            public void run() {
                LogUtil.e(TAG, "apHttp config timeout");
                EZWiFiConfigManager.stopAPHttpConfig();
                callback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
            }
        });
    }

    public static void stopAPHttpConfig() {
        Log.i((String)TAG, (String)"Enter stopAPHttpConfig");
        if (isInit) {
            EZWiFiConfigManager.unInit();
        }
        ConfigWifiApi.stopAPHttpConfig();
    }

    public static void startSmartConfig(Application app, SmartConfigParam param, EZConfigWifiCallback callback) {
        Log.i((String)TAG, (String)"Enter startSmartConfig");
        MixedConfigParam realParam = new MixedConfigParam(param);
        realParam.mode = MixedConfigMode.EZWiFiConfigSmart;
        EZWiFiConfigManager.startMixedConfig(app, realParam, callback);
    }

    public static void stopSmartConfig() {
        Log.i((String)TAG, (String)"Enter stopSmartConfig");
        EZWiFiConfigManager.stopMixedConfig();
    }

    public static void startSoundWaveConfig(Application app, SoundWaveConfigParam param, EZConfigWifiCallback callback) {
        Log.i((String)TAG, (String)"Enter startSoundWaveConfig");
        MixedConfigParam realParam = new MixedConfigParam(param);
        realParam.mode = MixedConfigMode.EZWiFiConfigWave;
        EZWiFiConfigManager.startMixedConfig(app, realParam, callback);
    }

    public static void stopSoundWaveConfig() {
        Log.i((String)TAG, (String)"Enter stopSoundWaveConfig");
        EZWiFiConfigManager.stopMixedConfig();
    }

    public static void startMixedConfig(Application app, MixedConfigParam param, final EZConfigWifiCallback callback) {
        Log.i((String)TAG, (String)"Enter startMixedConfig with EZStartConfigWifiCallback");
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        boolean failedToStart = false;
        try {
            ConfigWifiApi.startMixedConfig(app, param, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
        }
        if (failedToStart) {
            LogUtil.e(TAG, "failed to call startMixedConfig");
            return;
        }
        DeviceFindParam findParam = new DeviceFindParam();
        findParam.wifiName = param.routerWifiSsid;
        findParam.wifiPwd = param.routerWifiPwd;
        findParam.serial = param.deviceSerial;
        mDeviceFinderFromLan.setCallback(new DeviceFindCallback(){

            @Override
            public void onFind(String deviceSerial) {
                EZWiFiConfigManager.stopMixedConfig();
                callback.reportInfo(EZConfigWifiInfoEnum.CONNECTED_TO_WIFI);
                LogUtil.i(TAG, "callback thread pid is " + Thread.currentThread().getId());
            }
        });
        mDeviceFinderFromLan.start(findParam);
        EZWiFiConfigManager.resetTimer(EZWiFiConfigManager.getTimeoutSecond() * 1000L, new Runnable(){

            @Override
            public void run() {
                LogUtil.e(TAG, "mixed config timeout");
                EZWiFiConfigManager.stopMixedConfig();
                callback.reportError(EZConfigWifiErrorEnum.CONFIG_TIMEOUT);
            }
        });
    }

    public static void stopMixedConfig() {
        Log.i((String)TAG, (String)"Enter stopMixedConfig");
        if (isInit) {
            EZWiFiConfigManager.unInit();
        }
        ConfigWifiApi.stopMixedConfig();
    }

    public static void startMixedConfigOld(Application app, MixedConfigParamOld param, DeviceDiscoveryListener callback) {
        Log.i((String)TAG, (String)"Enter startMixedConfig with DeviceDiscoveryListener");
        boolean failedToStart = false;
        try {
            ConfigWifiApi.startMixedConfigOld(app, param, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
            failedToStart = true;
        }
        if (failedToStart) {
            LogUtil.e(TAG, "failed to call startMixedConfigOld");
            return;
        }
    }

    public static void stopMixedConfigOld() {
        Log.i((String)TAG, (String)"Enter stopMixedConfig");
        ConfigWifiApi.stopMixedConfig();
    }

    public static void setApiUrl(String url) {
        Config.baseUrl = url;
    }

    public static void requestConfigToken(Application app, String accessToken, GetTokenCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.getToken(accessToken, null, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestConfigToken(Application app, String accessToken, Map<String, Object> paramMap, GetTokenCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.getToken(accessToken, paramMap, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryPlatformBindStatus(Application app, String accessToken, String deviceSerial, boolean isGlobal, QueryPlatformBindStatusCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.queryPlatformBindStatus(accessToken, deviceSerial, null, isGlobal, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryPlatformBindStatus(Application app, String accessToken, String deviceSerial, Map<String, Object> paramMap, boolean isGlobal, QueryPlatformBindStatusCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.queryPlatformBindStatus(accessToken, deviceSerial, paramMap, isGlobal, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAccessDeviceInfo(Application app, GetAccessDeviceInfoCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.getAccessDeviceInfo(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAccessDeviceWifiList(Application app, GetDeviceWifiListCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.getAccessDeviceWifiList(callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startNewApConfigWithToken(Application app, String token, String ssid, String password, String lbsDomain, StartNewApConfigCallback callback) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.startNewApConfigWithToken(token, ssid, password, lbsDomain, callback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDevRouteDomain(Application app, String devRouteDomain) {
        if (!isInit) {
            EZWiFiConfigManager.init(app);
        }
        try {
            TouchApApi.setDevRouteDomain(devRouteDomain);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getTimeoutSecond() {
        return mTimeoutMs / 1000L;
    }

    public static void showLog(boolean enable) {
        Config.LOGGING = enable;
    }

    private static void init(Application app) {
        isInit = true;
        ConfigWifiTaskManager.init();
        DeviceFinderFromLan.init((Context)app);
        mDeviceFinderFromLan = DeviceFinderFromLan.getInstance();
        TouchApApi.init(app);
    }

    private static void unInit() {
        isInit = false;
        EZWiFiConfigManager.stopTimer();
        if (mDeviceFinderFromLan != null) {
            mDeviceFinderFromLan.stop();
        }
        ConfigWifiTaskManager.unInit();
    }

    private static void resetTimer(long timeoutMs, final Runnable timeoutTask) {
        if (mConfigTimer != null) {
            mConfigTimer.cancel();
        }
        mConfigTimer = new Timer();
        mConfigTimer.schedule(new TimerTask(){

            @Override
            public void run() {
                timeoutTask.run();
                mConfigTimer.cancel();
                mConfigTimer = null;
            }
        }, timeoutMs);
    }

    private static void stopTimer() {
        LogUtil.i(TAG, "stopTimer");
        if (mConfigTimer != null) {
            mConfigTimer.cancel();
        }
    }

    static {
        mTimeoutMs = Config.mTimeoutSecond * 1000;
    }
}

