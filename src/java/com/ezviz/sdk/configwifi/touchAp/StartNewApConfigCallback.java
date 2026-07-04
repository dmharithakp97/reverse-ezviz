/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.touchAp;

import com.ezviz.http.exception.EzConfigWifiException;

public interface StartNewApConfigCallback {
    public void onResponse(int var1, String var2);

    public void onError(EzConfigWifiException var1);
}

