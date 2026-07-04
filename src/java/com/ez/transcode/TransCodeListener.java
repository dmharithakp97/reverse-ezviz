/*
 * Decompiled with CFR 0.152.
 */
package com.ez.transcode;

public interface TransCodeListener {
    public void onDataOutput(byte[] var1, int var2, int var3, long var4);

    public void onError(int var1, String var2);
}

