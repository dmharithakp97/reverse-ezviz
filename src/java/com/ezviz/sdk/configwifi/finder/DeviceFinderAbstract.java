/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.finder;

import com.ezviz.sdk.configwifi.common.LogUtil;
import com.ezviz.sdk.configwifi.finder.DeviceFindCallback;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;
import com.ezviz.sdk.configwifi.finder.DeviceFinderInterface;

public abstract class DeviceFinderAbstract
implements DeviceFinderInterface {
    private static final String TAG = DeviceFinderAbstract.class.getSimpleName();
    protected DeviceFindCallback mCallback;
    protected DeviceFindParam mParam;
    protected boolean isFinding;

    @Override
    public void setCallback(DeviceFindCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void start(DeviceFindParam param) {
        LogUtil.i(TAG, "start");
        this.isFinding = true;
        this.mParam = param;
    }

    @Override
    public void stop() {
        LogUtil.i(TAG, "stop");
        this.isFinding = false;
        this.mCallback = null;
    }

    @Override
    public boolean isFinding() {
        return this.isFinding;
    }
}

