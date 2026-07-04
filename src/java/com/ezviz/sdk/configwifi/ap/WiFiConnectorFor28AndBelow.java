/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.net.wifi.WifiInfo
 *  android.util.Log
 */
package com.ezviz.sdk.configwifi.ap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.util.Log;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.ap.WiFiConnecter;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorAbstract;

@SuppressLint(value={"NewApi"})
public class WiFiConnectorFor28AndBelow
extends WiFiConnectorAbstract {
    private static final String TAG = WiFiConnectorFor28AndBelow.class.getSimpleName();
    private WiFiConnecter mWiFiConnector;

    @Override
    public void setParam(Context context, String ssid, String password) {
        super.setParam(context, ssid, password);
        this.mWiFiConnector = new WiFiConnecter(this.mContext.getApplicationContext());
    }

    @Override
    public void start() {
        this.mWiFiConnector.connectStrongMode(this.mSsid, this.mPassword, new WiFiConnecter.ActionListener(){

            @Override
            public void onSuccess(WifiInfo info) {
                Log.d((String)TAG, (String)"WiFiConnecter onSuccess");
                if (WiFiConnectorFor28AndBelow.this.mListener == null) {
                    return;
                }
                WiFiConnectorFor28AndBelow.this.mListener.onSuccess();
            }

            @Override
            public void onFailure(int errorCode) {
                Log.e((String)TAG, (String)("WiFiConnecter onFailure errorCode = " + errorCode));
                if (WiFiConnectorFor28AndBelow.this.mListener == null) {
                    return;
                }
                WiFiConnectorFor28AndBelow.this.mListener.onSuccess();
            }

            @Override
            public void onFailure(EZConfigWifiErrorEnum error) {
                Log.e((String)TAG, (String)("WiFiConnecter onFailure = " + error.toString()));
                if (WiFiConnectorFor28AndBelow.this.mListener == null) {
                    return;
                }
                WiFiConnectorFor28AndBelow.this.mListener.onFailure(error.code);
            }
        });
    }

    @Override
    public void stop() {
        this.mWiFiConnector.destroy();
    }

    @Override
    public void pause() {
        super.pause();
        this.mWiFiConnector.onPause();
    }

    @Override
    public void resume() {
        super.resume();
        this.mWiFiConnector.onResume();
    }

    @Override
    public boolean isPaused() {
        return this.mWiFiConnector.isPaused;
    }
}

