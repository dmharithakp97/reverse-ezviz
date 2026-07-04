/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.net.ConnectivityManager
 *  android.net.ConnectivityManager$NetworkCallback
 *  android.net.Network
 *  android.net.NetworkRequest
 *  android.net.NetworkRequest$Builder
 *  android.net.NetworkSpecifier
 *  android.net.wifi.WifiManager
 *  android.net.wifi.WifiNetworkSpecifier
 *  android.net.wifi.WifiNetworkSpecifier$Builder
 */
package com.ezviz.sdk.configwifi.ap;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorAbstract;
import com.ezviz.sdk.configwifi.common.LogUtil;

@SuppressLint(value={"NewApi"})
public class WiFiConnectorFor29AndAbove
extends WiFiConnectorAbstract {
    private static final String TAG = WiFiConnectorFor29AndAbove.class.getSimpleName();
    private ConnectivityManager connectivityManager;
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){

        public void onAvailable(Network network) {
            if (WiFiConnectorFor29AndAbove.this.mListener == null) {
                return;
            }
            WiFiConnectorFor29AndAbove.this.mListener.onSuccess();
        }

        public void onUnavailable() {
            if (WiFiConnectorFor29AndAbove.this.mListener == null) {
                return;
            }
            WiFiConnectorFor29AndAbove.this.mListener.onFailure(EZConfigWifiErrorEnum.USER_REFUSED_CONNECTION_REQUEST.code);
        }
    };

    @Override
    public void start() {
        this.connectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
        this.isWifiEnable();
        if (!this.isWifiEnable()) {
            LogUtil.e(TAG, "start: wifi is disable!");
            this.mListener.onFailure(EZConfigWifiErrorEnum.FAILED_TO_AUTO_ENABLE_WIFI.code);
            return;
        }
        this.doConnectToTargetWifi();
    }

    @SuppressLint(value={"MissingPermission"})
    private boolean isWifiEnable() {
        WifiManager wifiManager = (WifiManager)this.mContext.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            LogUtil.e(TAG, "openWifiSwitch: wifiManager is null, can not detect wifi state rightly.");
            return true;
        }
        return wifiManager.isWifiEnabled();
    }

    @Override
    public void stop() {
        this.connectivityManager.unregisterNetworkCallback(this.networkCallback);
    }

    private void doConnectToTargetWifi() {
        LogUtil.i(TAG, "doConnectToTargetWifi");
        WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder().setSsid(this.mSsid).setWpa2Passphrase(this.mPassword).build();
        NetworkRequest request = new NetworkRequest.Builder().addTransportType(1).removeCapability(12).setNetworkSpecifier((NetworkSpecifier)specifier).build();
        this.connectivityManager.requestNetwork(request, this.networkCallback);
    }
}

