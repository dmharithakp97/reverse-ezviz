/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import android.content.Context;
import com.ezviz.sdk.configwifi.common.ConfigExecutorAbstract;
import com.ezviz.sdk.configwifi.mixedconfig.EZBonjourController;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigMode;
import com.ezviz.sdk.configwifi.mixedconfig.MixedConfigParamOld;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;

public class MixedConfigExecutorOld
extends ConfigExecutorAbstract {
    private static EZBonjourController mEZBonjourController;
    private static final MixedConfigExecutorOld mInstance;
    private MixedConfigParamOld mParam;
    private DeviceDiscoveryListener mCallback;

    public static MixedConfigExecutorOld getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public void setCallback(DeviceDiscoveryListener callback) {
        this.mCallback = callback;
    }

    public void setParam(MixedConfigParamOld param) {
        super.setCommonParam(param);
        this.mParam = param;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void start() throws Exception {
        super.start();
        if (mEZBonjourController == null) {
            mEZBonjourController = new EZBonjourController(this.mContext, this.mParam.routerWifiSsid, this.mParam.routerWifiPwd, this.mCallback);
        }
        EZBonjourController eZBonjourController = mEZBonjourController;
        synchronized (eZBonjourController) {
            if (mEZBonjourController != null) {
                mEZBonjourController.startConfigWifi(MixedConfigMode.EZWiFiConfigSmart);
                this.isExecuting = true;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void stop() {
        if (mEZBonjourController != null) {
            EZBonjourController eZBonjourController = mEZBonjourController;
            synchronized (eZBonjourController) {
                if (mEZBonjourController != null) {
                    mEZBonjourController.stopConfig();
                    mEZBonjourController = null;
                }
            }
        }
        this.isExecuting = false;
    }

    @Override
    public boolean isExecuting() {
        return this.isExecuting;
    }

    static {
        mInstance = new MixedConfigExecutorOld();
    }
}

