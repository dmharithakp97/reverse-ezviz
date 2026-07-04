/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.ezviz.sdk.configwifi.common;

import android.content.Context;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.WiFiUtils;
import com.ezviz.sdk.configwifi.common.ConfigExecutorInterface;
import com.ezviz.sdk.configwifi.common.ConfigParamAbstract;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.exception.ExecuteConfigException;

public abstract class ConfigExecutorAbstract
implements ConfigExecutorInterface {
    private static final String TAG = ConfigExecutorAbstract.class.getSimpleName();
    protected boolean isExecuting = false;
    protected Context mContext;
    private ConfigParamAbstract mCommonParam;
    private EZConfigWifiCallback mCommonCallback;

    @Override
    public void start() throws Exception {
        if (this.isExecuting) {
            this.reportError(EZConfigWifiErrorEnum.LAST_CONFIG_EXECUTING);
            throw new ExecuteConfigException();
        }
        if (!WiFiUtils.isConnectedToTargetWifi(this.mContext, this.mCommonParam.routerWifiSsid)) {
            this.reportError(EZConfigWifiErrorEnum.PHONE_NOT_CONNECTED_TO_TARGET_WIFI);
        }
        this.isExecuting = true;
    }

    @Override
    public void stop() {
        this.isExecuting = false;
    }

    @Override
    public void setCallback(EZConfigWifiCallback callback) {
        this.mCommonCallback = callback;
    }

    @Override
    public boolean isExecuting() {
        return this.isExecuting;
    }

    public void setCommonParam(ConfigParamAbstract param) {
        this.mCommonParam = param;
    }

    protected boolean checkParam(ConfigParamAbstract param) {
        boolean ret = true;
        LogUtil.i(TAG, "start check config wifi param...");
        if (this.isEmptyText(param.routerWifiSsid)) {
            LogUtil.e(TAG, "routerWifiSsid is invalid, value is empty");
            ret = false;
        }
        if (this.isEmptyText(param.deviceSerial)) {
            LogUtil.e(TAG, "deviceSerial is invalid, value is empty");
            ret = false;
        }
        return ret;
    }

    protected void reportInfo(EZConfigWifiInfoEnum info) {
        if (this.mCommonCallback != null && info != null) {
            this.mCommonCallback.onInfo(info.code, info.description);
        }
    }

    protected void reportError(EZConfigWifiErrorEnum error) {
        if (this.mCommonCallback != null && error != null) {
            this.mCommonCallback.onError(error.code, error.description);
        }
    }

    protected boolean isEmptyText(String param) {
        return TextUtils.isEmpty((CharSequence)param);
    }
}

