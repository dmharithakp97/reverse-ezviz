/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.finder;

import com.ezviz.sdk.configwifi.finder.DeviceFindCallback;
import com.ezviz.sdk.configwifi.finder.DeviceFindParam;

public interface DeviceFinderInterface {
    public void setCallback(DeviceFindCallback var1);

    public void start(DeviceFindParam var1);

    public void stop();

    public boolean isFinding();
}

