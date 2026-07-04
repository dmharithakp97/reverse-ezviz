/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.finder;

import com.ezviz.sdk.configwifi.finder.DeviceFindCallbackInterface;

public abstract class DeviceFindCallback
implements DeviceFindCallbackInterface {
    @Override
    public void onTimeout(String deviceSerial) {
    }

    @Override
    public void onError(int code, String msg) {
    }
}

