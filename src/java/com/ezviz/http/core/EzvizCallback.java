/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.http.core;

import com.ezviz.http.exception.EzConfigWifiException;

public interface EzvizCallback {
    public void onSuccess(String var1);

    public void onException(EzConfigWifiException var1);
}

