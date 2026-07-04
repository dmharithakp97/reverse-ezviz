/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.stream.EZStreamCallback
 */
package com.videogo.realplay;

import com.ez.stream.EZStreamCallback;
import com.videogo.util.LogUtil;

public class EZRealPlayDataCtrl
implements EZStreamCallback {
    private final String TAG = "EZRealPlayDataCtrl";

    public void onDataCallBack(int var1, byte[] var2, int var3) {
        LogUtil.v("EZRealPlayDataCtrl", "onDataCallBack. var1:" + var1 + " var2:" + var2 + " var3:" + var3);
    }

    public void onMessageCallBack(int var1, int var2) {
        LogUtil.v("EZRealPlayDataCtrl", "onMessageCallBack. var1:" + var1 + " var2:" + var2);
    }

    public void onStatisticsCallBack(int var1, String var2) {
        LogUtil.v("EZRealPlayDataCtrl", "onStatisticsCallBack. var1:" + var1 + " var2:" + var2);
    }
}

