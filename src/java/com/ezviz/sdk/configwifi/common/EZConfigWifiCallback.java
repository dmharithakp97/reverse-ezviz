/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.common;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.common.ConfigWifiCallbackInterface;
import com.ezviz.sdk.configwifi.common.LogUtil;

public abstract class EZConfigWifiCallback
implements ConfigWifiCallbackInterface {
    private static final String TAG = EZConfigWifiCallback.class.getSimpleName();

    @Override
    public void onInfo(int code, String message) {
        this.printMsg(code, message);
    }

    @Override
    public void onError(int code, String description) {
        this.printMsg(code, description);
    }

    private void printMsg(int code, String description) {
        LogUtil.i(TAG, "receive new info or error");
        LogUtil.i(TAG, "code is " + code + ", description is " + description);
    }

    public void reportInfo(EZConfigWifiInfoEnum info) {
        this.onInfo(info.code, info.description);
    }

    public void reportError(EZConfigWifiErrorEnum error) {
        this.onError(error.code, error.description);
    }
}

