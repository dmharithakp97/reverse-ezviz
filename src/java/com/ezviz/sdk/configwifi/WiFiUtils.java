/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.ezviz.sdk.configwifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.ezviz.sdk.configwifi.common.LogUtil;
import java.util.List;

@SuppressLint(value={"MissingPermission"})
public class WiFiUtils {
    private static final String TAG = WiFiUtils.class.getSimpleName();
    public static final String WPA2 = "WPA2";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String OPEN = "Open";
    public static final String WPA_EAP = "WPA-EAP";
    public static final String IEEE8021X = "IEEE8021X";
    static final String[] SECURITY_MODES = new String[]{"WEP", "WPA", "WPA2", "WPA-EAP", "IEEE8021X"};

    public static String getScanResultSecurity(ScanResult scanResult) {
        String cap = scanResult.capabilities;
        for (int i = SECURITY_MODES.length - 1; i >= 0; --i) {
            if (!cap.contains(SECURITY_MODES[i])) continue;
            return SECURITY_MODES[i];
        }
        return OPEN;
    }

    public static boolean isSsidEquals(String ssid, String SSID, boolean bySsidIgnoreCase) {
        boolean ssidEquals;
        if (TextUtils.isEmpty((CharSequence)ssid) || TextUtils.isEmpty((CharSequence)SSID)) {
            return false;
        }
        boolean bl = ssidEquals = bySsidIgnoreCase ? ssid.equalsIgnoreCase(SSID) : ssid.equals(SSID);
        if (!ssidEquals) {
            String quotedString = WiFiUtils.convertToQuotedString(ssid);
            ssidEquals = bySsidIgnoreCase ? quotedString.equalsIgnoreCase(SSID) : quotedString.equals(SSID);
        }
        return ssidEquals;
    }

    public static String getSSIDSecurity(Context context, String ssid) {
        WifiManager wifiMgr = (WifiManager)context.getApplicationContext().getSystemService("wifi");
        if (wifiMgr == null) {
            return "";
        }
        List results = wifiMgr.getScanResults();
        if (results != null) {
            for (ScanResult result : results) {
                boolean ssidEquals = WiFiUtils.isSsidEquals(ssid, result.SSID, true);
                if (!ssidEquals) continue;
                return WiFiUtils.getScanResultSecurity(result);
            }
        }
        return "";
    }

    public static String convertToQuotedString(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return "";
        }
        int lastPos = string.length() - 1;
        if (lastPos < 0 || string.charAt(0) == '\"' && string.charAt(lastPos) == '\"') {
            return string;
        }
        return "\"" + string + "\"";
    }

    public static String getWifiMacAddress(Context context) {
        WifiInfo wifiInfo = WiFiUtils.getWifiInfo(context);
        if (wifiInfo != null) {
            return wifiInfo.getBSSID();
        }
        return "";
    }

    public static WifiInfo getWifiInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            WifiManager mWifi = (WifiManager)context.getApplicationContext().getSystemService("wifi");
            if (mWifi != null && mWifi.isWifiEnabled()) {
                WifiInfo wifiInfo = mWifi.getConnectionInfo();
                String netName = wifiInfo.getSSID();
                String netMac = wifiInfo.getBSSID();
                String localMac = wifiInfo.getMacAddress();
                Log.d((String)"getWifiMacAddress", (String)("---netName:" + netName));
                Log.d((String)"getWifiMacAddress", (String)("---netMac:" + netMac));
                Log.d((String)"getWifiMacAddress", (String)("---localMac:" + localMac));
                return wifiInfo;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public static String getWifiSSID(Context ctx) {
        WifiManager wifi_service = (WifiManager)ctx.getSystemService("wifi");
        WifiInfo connectionInfo = wifi_service.getConnectionInfo();
        String ssid = connectionInfo.getSSID();
        if (Build.VERSION.SDK_INT >= 17 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    public static String byteArray2String(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder bld = new StringBuilder();
        for (byte b : data) {
            if (b == 0) break;
            bld.append((char)b);
        }
        return bld.toString();
    }

    public static boolean isConnectedToTargetWifi(Context context, String targetSsid) {
        String currentSsid = WiFiUtils.getCurrentWifiSsid(context);
        LogUtil.i(TAG, "ssid of target wifi is: " + targetSsid);
        LogUtil.i(TAG, "ssid of current wifi is: " + currentSsid);
        if (currentSsid != null) {
            return currentSsid.replace("\"", "").equals(targetSsid);
        }
        return false;
    }

    public static boolean isConnectedToAnyValidWifi(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null) {
            return activeNetworkInfo.getType() == 1;
        }
        return false;
    }

    public static ScanResult getTargetWifiScanInfo(Context context, String targetSsid) {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService("wifi");
        for (ScanResult scanResult : wifiManager.getScanResults()) {
            if (!scanResult.SSID.replace("\"", "").equals(targetSsid)) continue;
            return scanResult;
        }
        return null;
    }

    public static String getCurrentWifiSsid(Context context) {
        WifiInfo winfo;
        WifiManager wm = (WifiManager)context.getApplicationContext().getSystemService("wifi");
        if (wm != null && (winfo = wm.getConnectionInfo()) != null) {
            String ssid = winfo.getSSID();
            if (!TextUtils.isEmpty((CharSequence)(ssid = WiFiUtils.removeQuotesOfSsid(ssid)))) {
                return ssid;
            }
        }
        return "<unknown ssid>";
    }

    public static String removeQuotesOfSsid(String originSsid) {
        if (originSsid.startsWith("\"")) {
            originSsid = originSsid.substring(1, originSsid.length());
        }
        if (originSsid.endsWith("\"")) {
            originSsid = originSsid.substring(0, originSsid.length() - 1);
        }
        return originSsid;
    }
}

