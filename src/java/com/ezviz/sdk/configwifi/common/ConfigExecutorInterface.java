/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.common;

import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;

public interface ConfigExecutorInterface {
    public void start() throws Exception;

    public void stop();

    public void setCallback(EZConfigWifiCallback var1);

    public boolean isExecuting();
}

