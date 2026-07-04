/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.ezviz.sdk.configwifi.apHttp;

import android.content.Context;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfigImp;
import com.ezviz.sdk.configwifi.ap.ApConfigParam;
import com.ezviz.sdk.configwifi.common.ConfigExecutorAbstract;
import com.ezviz.sdk.configwifi.common.ConfigParamAbstract;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.common.LogUtil;

public class ApHttpConfigExecutor
extends ConfigExecutorAbstract {
    private static final String TAG = ApHttpConfigExecutor.class.getSimpleName();
    private static ApHttpConfigExecutor mInstance = new ApHttpConfigExecutor();
    private EZConfigWifiCallback mCallback;
    private ApConfigParam mParam;

    public static ApHttpConfigExecutor getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public void setParam(ApConfigParam param) {
        super.setCommonParam(param);
        this.mParam = param;
    }

    @Override
    public void setCallback(EZConfigWifiCallback callback) {
        super.setCallback(callback);
        this.mCallback = callback;
    }

    @Override
    public void start() throws Exception {
        super.start();
        if (!this.checkParam(this.mParam)) {
            this.reportError(EZConfigWifiErrorEnum.WRONG_CONFIG_PARAM);
            this.isExecuting = false;
            return;
        }
        EZWiFiConfigImp.getInstance(this.mContext).setParams(this.mParam.deviceSerial, this.mParam.routerWifiSsid, this.mParam.routerWifiPwd);
        EZWiFiConfigImp.getInstance(this.mContext).setAPHttpConfigWifiCallback(this.mCallback);
        EZWiFiConfigImp.getInstance(this.mContext).startAPHttpConfigWifi(this.mParam.configToken, this.mParam.deviceHotspotSsid, this.mParam.deviceHotspotPwd, this.mParam.lbsDomain, this.mParam.autoConnect);
    }

    @Override
    public void stop() {
        super.stop();
        if (this.mContext != null) {
            EZWiFiConfigImp.getInstance(this.mContext).stopAPConfigWifi();
        }
    }

    @Override
    protected boolean checkParam(ConfigParamAbstract param) {
        boolean ret = true;
        ApConfigParam apConfigParam = (ApConfigParam)param;
        LogUtil.i(TAG, "start check config wifi param...");
        if (this.isEmptyText(param.routerWifiSsid)) {
            LogUtil.e(TAG, "routerWifiSsid is invalid, value is empty");
            ret = false;
        }
        if (this.isEmptyText(apConfigParam.configToken)) {
            LogUtil.e(TAG, "configToken is invalid, value is empty");
            ret = false;
        }
        if (this.isEmptyText(apConfigParam.lbsDomain)) {
            LogUtil.e(TAG, "lbsDomain is invalid, value is empty");
            ret = false;
        }
        return ret;
    }
}

