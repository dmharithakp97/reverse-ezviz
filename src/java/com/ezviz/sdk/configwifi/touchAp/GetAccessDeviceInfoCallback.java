/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.touchAp;

import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.http.model.AccessDeviceInfo;

public interface GetAccessDeviceInfoCallback {
    public void onSuccess(AccessDeviceInfo var1);

    public void onError(EzConfigWifiException var1);
}

