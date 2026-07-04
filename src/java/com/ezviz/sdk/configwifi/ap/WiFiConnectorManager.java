/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 */
package com.ezviz.sdk.configwifi.ap;

import android.content.Context;
import android.os.Build;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorAbstract;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorFor28AndBelow;
import com.ezviz.sdk.configwifi.ap.WiFiConnectorFor29AndAbove;

public class WiFiConnectorManager {
    public static WiFiConnectorAbstract getWiFiConnectorByOsVersion(Context context, String ssid, String password) {
        int osVersion = Build.VERSION.SDK_INT;
        WiFiConnectorAbstract connector = osVersion >= 29 ? new WiFiConnectorFor29AndAbove() : new WiFiConnectorFor28AndBelow();
        connector.setParam(context, ssid, password);
        return connector;
    }
}

