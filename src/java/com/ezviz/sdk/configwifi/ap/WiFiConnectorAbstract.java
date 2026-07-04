/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.ezviz.sdk.configwifi.ap;

import android.content.Context;
import com.ezviz.sdk.configwifi.ap.WiFiConnectResultListener;
import com.ezviz.sdk.configwifi.common.Controllable;

public abstract class WiFiConnectorAbstract
implements Controllable {
    protected Context mContext;
    protected String mSsid;
    protected String mPassword;
    protected WiFiConnectResultListener mListener;
    private boolean hasPaused;

    public void setParam(Context context, String ssid, String password) {
        this.mContext = context;
        this.mSsid = ssid;
        this.mPassword = password;
    }

    public void setListener(WiFiConnectResultListener listener) {
        this.mListener = listener;
    }

    @Override
    public void pause() {
        this.hasPaused = true;
    }

    @Override
    public void resume() {
        this.hasPaused = false;
    }

    public boolean isPaused() {
        return this.hasPaused;
    }
}

