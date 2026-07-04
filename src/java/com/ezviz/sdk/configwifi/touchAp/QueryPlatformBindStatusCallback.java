/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.touchAp;

import com.ezviz.http.exception.EzConfigWifiException;

public interface QueryPlatformBindStatusCallback {
    public void onSuccess(boolean var1);

    public void onError(EzConfigWifiException var1);
}

