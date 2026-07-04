/*
 * Decompiled with CFR 0.152.
 */
package com.hc.CASClient;

public interface CASClientCallback {
    public void onDataCallBack(int var1, int var2, int var3, byte[] var4, int var5);

    public void onMessageCallBack(int var1, int var2, int var3, int var4, int var5, int var6);

    public void onP2PStatus(int var1, int var2);
}

