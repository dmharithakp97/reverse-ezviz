/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.touchAp;

import com.ezviz.http.exception.EzConfigWifiException;
import com.ezviz.http.model.EzWifiInfo;
import java.util.List;

public interface GetDeviceWifiListCallback {
    public void onSuccess(List<EzWifiInfo> var1);

    public void onError(EzConfigWifiException var1);
}

