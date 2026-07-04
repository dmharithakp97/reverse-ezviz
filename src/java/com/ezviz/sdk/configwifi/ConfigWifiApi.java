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
import com.ezviz.sdk.configwifi.EZWiFiConfigApi;
import com.ezviz.sdk.configwifi.ap.ApConfigExecutor;
import com.ezviz.sdk.configwifi.ap.ApConfigParam;
import com.ezviz.sdk.configwifi.apHttp.ApHttpConfigExecutor;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigExecutor;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigExecutorOld;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParam;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParamOld;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;

public class ConfigWifiApi {
    private static final String TAG = EZWiFiConfigApi.class.getSimpleName();

    private ConfigWifiApi() {
    }

    public static void startAPConfig(Application app, ApConfigParam param, EZConfigWifiCallback callback) throws Exception {
        Log.i((String)TAG, (String)"Enter startAPConfig");
        ApConfigExecutor executor = ApConfigExecutor.getInstance();
        executor.init((Context)app);
        executor.setParam(param);
        executor.setCallback(callback);
        executor.start();
    }

    public static void stopAPConfig() {
        Log.i((String)TAG, (String)"Enter stopAPConfig");
        ApConfigExecutor.getInstance().stop();
    }

    public static void startAPHttpConfig(Application app, ApConfigParam param, EZConfigWifiCallback callback) throws Exception {
        Log.i((String)TAG, (String)"Enter startAPHttpConfig");
        ApHttpConfigExecutor executor = ApHttpConfigExecutor.getInstance();
        executor.init((Context)app);
        executor.setParam(param);
        executor.setCallback(callback);
        executor.start();
    }

    public static void stopAPHttpConfig() {
        Log.i((String)TAG, (String)"Enter stopAPHttpConfig");
        ApHttpConfigExecutor.getInstance().stop();
    }

    public static void startMixedConfig(Application app, MixedConfigParam param, EZConfigWifiCallback callback) throws Exception {
        Log.i((String)TAG, (String)"Enter startMixedConfig with EZStartConfigWifiCallback");
        MixedConfigExecutor executor = MixedConfigExecutor.getInstance();
        executor.init((Context)app);
        executor.setParam(param);
        executor.setCallback(callback);
        executor.start();
    }

    public static void startMixedConfigOld(Application app, MixedConfigParamOld param, DeviceDiscoveryListener callback) throws Exception {
        Log.i((String)TAG, (String)"Enter startMixedConfig with DeviceDiscoveryListener");
        MixedConfigExecutorOld executor = MixedConfigExecutorOld.getInstance();
        executor.init((Context)app);
        executor.setParam(param);
        executor.setCallback(callback);
        executor.start();
    }

    public static void stopMixedConfig() {
        Log.i((String)TAG, (String)"Enter stopMixedConfig");
        MixedConfigExecutor.getInstance().stop();
        MixedConfigExecutorOld.getInstance().stop();
    }
}

