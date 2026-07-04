/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$State
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 */
package com.videogo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.videogo.util.LogUtil;

public class ConnectionDetector {
    public static final int NO_NETWORK = -1;
    public static final int CMNET = 1;
    public static final int CMWAP = 2;
    public static final int WIFI = 3;

    public static int getConnectionType(Context context) {
        NetworkInfo curNetwork;
        int netType = -1;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivity != null && (curNetwork = connectivity.getActiveNetworkInfo()) != null) {
            int nType = curNetwork.getType();
            if (nType == 0) {
                String extraInfo = curNetwork.getExtraInfo();
                if (extraInfo != null) {
                    String sType = curNetwork.getExtraInfo().toLowerCase();
                    netType = sType.equals("cmnet") ? 1 : 2;
                }
            } else if (nType == 1) {
                netType = 3;
            }
        }
        return netType;
    }

    public static boolean isNetworkFor2G(Context context) {
        int nType;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo curNetwork = connectivity.getActiveNetworkInfo();
        return curNetwork != null && curNetwork.getType() == 0 && ((nType = curNetwork.getSubtype()) == 1 || nType == 4 || nType == 2);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; ++i) {
                if (info[i].getState() != NetworkInfo.State.CONNECTED) continue;
                return true;
            }
        }
        return false;
    }

    public static String getTypeName(Context context) {
        NetworkInfo curNetwork;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivity != null && (curNetwork = connectivity.getActiveNetworkInfo()) != null) {
            int nType = curNetwork.getType();
            if (nType == 1) {
                return curNetwork.getTypeName();
            }
            if (nType == 0) {
                return curNetwork.getSubtypeName();
            }
            return "UNKNOWN";
        }
        return "UNKNOWN";
    }

    public static String getWifiMacAddress(Context context) {
        WifiManager mWifi = (WifiManager)context.getSystemService("wifi");
        if (mWifi != null && mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            String netName = wifiInfo.getSSID();
            String netMac = wifiInfo.getBSSID();
            String localMac = wifiInfo.getMacAddress();
            LogUtil.d("getWifiMacAddress", "---netName:" + netName);
            LogUtil.d("getWifiMacAddress", "---netMac:" + netMac);
            LogUtil.d("getWifiMacAddress", "---localMac:" + localMac);
            return netMac;
        }
        return null;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }
}

