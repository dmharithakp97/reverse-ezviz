/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.finder;

public interface DeviceFindCallbackInterface {
    public void onFind(String var1);

    public void onTimeout(String var1);

    public void onError(int var1, String var2);
}

