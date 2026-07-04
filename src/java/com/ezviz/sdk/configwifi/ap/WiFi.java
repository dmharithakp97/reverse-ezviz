/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiConfiguration
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.text.TextUtils
 */
package com.ezviz.sdk.configwifi.ap;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.common.LogUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WiFi {
    public static final String WPA2 = "WPA2";
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String OPEN = "Open";
    public static final String WPA_EAP = "WPA-EAP";
    public static final String IEEE8021X = "IEEE8021X";
    public static final String[] EAP_METHOD = new String[]{"PEAP", "TLS", "TTLS"};
    public static final int WEP_PASSWORD_AUTO = 0;
    public static final int WEP_PASSWORD_ASCII = 1;
    public static final int WEP_PASSWORD_HEX = 2;
    private static final String TAG = "Wifi Connecter";
    private static final int MAX_PRIORITY = 99999;
    static final String[] SECURITY_MODES = new String[]{"WEP", "WPA", "WPA2", "WPA-EAP", "IEEE8021X"};

    public static boolean changePasswordAndConnect(WifiManager wifiMgr, WifiConfiguration config, String newPassword) {
        WiFi.setupSecurity(config, WiFi.getWifiConfigurationSecurity(config), newPassword);
        int networkId = wifiMgr.updateNetwork(config);
        if (networkId == -1) {
            int netId = wifiMgr.addNetwork(config);
            if (netId != -1) {
                return wifiMgr.enableNetwork(netId, true);
            }
            return false;
        }
        return WiFi.connectToConfiguredNetwork(wifiMgr, config, true);
    }

    public static boolean connectToNewNetwork(WifiManager wifiMgr, ScanResult scanResult, String password) {
        WifiConfiguration config;
        String security = WiFi.getScanResultSecurity(scanResult);
        if (security.equals(OPEN)) {
            int numOpenNetworksKept = 10;
            WiFi.checkForExcessOpenNetworkAndSave(wifiMgr, 10);
        }
        if ((config = WiFi.getWifiConfiguration(wifiMgr, scanResult, security)) != null && !wifiMgr.removeNetwork(config.networkId)) {
            return WiFi.changePasswordAndConnect(wifiMgr, config, password);
        }
        config = new WifiConfiguration();
        config.SSID = WiFi.convertToQuotedString(scanResult.SSID);
        config.BSSID = scanResult.BSSID;
        WiFi.setupSecurity(config, security, password);
        int id = wifiMgr.addNetwork(config);
        if (id == -1) {
            return false;
        }
        if (!wifiMgr.saveConfiguration()) {
            return false;
        }
        if ((config = WiFi.getWifiConfiguration(wifiMgr, config, security)) == null) {
            return false;
        }
        return WiFi.connectToConfiguredNetwork(wifiMgr, config, true);
    }

    public static boolean connectToConfiguredNetwork(WifiManager wifiMgr, WifiConfiguration config, boolean reassociate) {
        boolean connect;
        String security = WiFi.getWifiConfigurationSecurity(config);
        int oldPri = config.priority;
        int newPri = WiFi.getMaxPriority(wifiMgr) + 1;
        if (newPri > 99999) {
            newPri = WiFi.shiftPriorityAndSave(wifiMgr);
            if ((config = WiFi.getWifiConfiguration(wifiMgr, config, security)) == null) {
                return false;
            }
        }
        config.priority = newPri;
        int networkId = wifiMgr.updateNetwork(config);
        if (networkId != -1) {
            if (!wifiMgr.enableNetwork(networkId, false)) {
                config.priority = oldPri;
                return false;
            }
            if (!wifiMgr.saveConfiguration()) {
                config.priority = oldPri;
                return false;
            }
            if ((config = WiFi.getWifiConfiguration(wifiMgr, config, security)) == null) {
                return false;
            }
        }
        if (!wifiMgr.enableNetwork(config.networkId, true)) {
            return false;
        }
        boolean bl = connect = reassociate ? wifiMgr.reassociate() : wifiMgr.reconnect();
        return connect;
    }

    private static void sortByPriority(List<WifiConfiguration> configurations) {
        Collections.sort(configurations, new Comparator<WifiConfiguration>(){

            @Override
            public int compare(WifiConfiguration object1, WifiConfiguration object2) {
                return object1.priority - object2.priority;
            }
        });
    }

    private static boolean checkForExcessOpenNetworkAndSave(WifiManager wifiMgr, int numOpenNetworksKept) {
        List configurations = wifiMgr.getConfiguredNetworks();
        if (configurations == null) {
            return false;
        }
        WiFi.sortByPriority(configurations);
        boolean modified = false;
        int tempCount = 0;
        for (int i = configurations.size() - 1; i >= 0; --i) {
            WifiConfiguration config = (WifiConfiguration)configurations.get(i);
            if (!WiFi.getWifiConfigurationSecurity(config).equals(OPEN) || ++tempCount < numOpenNetworksKept) continue;
            modified = true;
            wifiMgr.removeNetwork(config.networkId);
        }
        if (modified) {
            return wifiMgr.saveConfiguration();
        }
        return true;
    }

    private static int shiftPriorityAndSave(WifiManager wifiMgr) {
        List configurations = wifiMgr.getConfiguredNetworks();
        if (configurations == null) {
            return 0;
        }
        WiFi.sortByPriority(configurations);
        int size = configurations.size();
        int i = 0;
        while (i < size) {
            WifiConfiguration config = (WifiConfiguration)configurations.get(i);
            config.priority = i++;
            wifiMgr.updateNetwork(config);
        }
        wifiMgr.saveConfiguration();
        return size;
    }

    private static int getMaxPriority(WifiManager wifiManager) {
        List configurations = wifiManager.getConfiguredNetworks();
        int pri = 0;
        if (configurations == null) {
            return pri;
        }
        for (WifiConfiguration config : configurations) {
            if (config.priority <= pri) continue;
            pri = config.priority;
        }
        return pri;
    }

    public static WifiConfiguration getWifiConfiguration(WifiManager wifiMgr, ScanResult hotsopt, String hotspotSecurity) {
        List configurations;
        String ssid = WiFi.convertToQuotedString(hotsopt.SSID);
        if (ssid.length() == 0) {
            return null;
        }
        String bssid = hotsopt.BSSID;
        if (bssid == null) {
            return null;
        }
        if (hotspotSecurity == null) {
            hotspotSecurity = WiFi.getScanResultSecurity(hotsopt);
        }
        if ((configurations = wifiMgr.getConfiguredNetworks()) == null) {
            return null;
        }
        for (WifiConfiguration config : configurations) {
            String configSecurity;
            if (config.SSID == null || !ssid.equals(config.SSID) || config.BSSID != null && !config.BSSID.equals("any") && !bssid.equals(config.BSSID) || !hotspotSecurity.equals(configSecurity = WiFi.getWifiConfigurationSecurity(config))) continue;
            return config;
        }
        return null;
    }

    public static WifiConfiguration getWifiConfiguration(WifiManager wifiMgr, WifiConfiguration configToFind, String security) {
        List configurations;
        String ssid = configToFind.SSID;
        if (ssid.length() == 0) {
            return null;
        }
        String bssid = configToFind.BSSID;
        if (security == null) {
            security = WiFi.getWifiConfigurationSecurity(configToFind);
        }
        if ((configurations = wifiMgr.getConfiguredNetworks()) == null) {
            return null;
        }
        for (WifiConfiguration config : configurations) {
            String configSecurity;
            if (config.SSID == null || !ssid.equals(config.SSID) || config.BSSID != null && bssid != null && !config.BSSID.equals("any") && !bssid.equals(config.BSSID) || !security.equals(configSecurity = WiFi.getWifiConfigurationSecurity(config))) continue;
            return config;
        }
        return null;
    }

    public static String getWifiConfigurationSecurity(WifiConfiguration wifiConfig) {
        if (wifiConfig.allowedKeyManagement.get(0)) {
            if (!wifiConfig.allowedGroupCiphers.get(3) && (wifiConfig.allowedGroupCiphers.get(0) || wifiConfig.allowedGroupCiphers.get(1))) {
                return WEP;
            }
            return OPEN;
        }
        if (wifiConfig.allowedProtocols.get(1)) {
            return WPA2;
        }
        if (wifiConfig.allowedKeyManagement.get(2)) {
            return WPA_EAP;
        }
        if (wifiConfig.allowedKeyManagement.get(3)) {
            return IEEE8021X;
        }
        if (wifiConfig.allowedProtocols.get(0)) {
            return WPA;
        }
        LogUtil.w(TAG, "Unknown security type from WifiConfiguration, falling back on open.");
        return OPEN;
    }

    private static void setupSecurity(WifiConfiguration config, String security, String password) {
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        if (TextUtils.isEmpty((CharSequence)security)) {
            security = OPEN;
            LogUtil.w(TAG, "Empty security, assuming open");
        }
        if (security.equals(WEP)) {
            boolean wepPasswordType = false;
            if (!TextUtils.isEmpty((CharSequence)password)) {
                config.wepKeys[0] = !wepPasswordType ? (WiFi.isHexWepKey(password) ? password : WiFi.convertToQuotedString(password)) : (wepPasswordType ? WiFi.convertToQuotedString(password) : password);
            }
            config.wepTxKeyIndex = 0;
            config.allowedAuthAlgorithms.set(0);
            config.allowedAuthAlgorithms.set(1);
            config.allowedKeyManagement.set(0);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
        } else if (security.equals(WPA) || security.equals(WPA2)) {
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(3);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(2);
            config.allowedPairwiseCiphers.set(1);
            config.allowedProtocols.set(security.equals(WPA2) ? 1 : 0);
            if (!TextUtils.isEmpty((CharSequence)password)) {
                config.preSharedKey = password.length() == 64 && WiFi.isHex(password) ? password : WiFi.convertToQuotedString(password);
            }
        } else if (security.equals(OPEN)) {
            config.allowedKeyManagement.set(0);
        } else if (security.equals(WPA_EAP) || security.equals(IEEE8021X)) {
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(3);
            if (security.equals(WPA_EAP)) {
                config.allowedKeyManagement.set(2);
            } else {
                config.allowedKeyManagement.set(3);
            }
            if (!TextUtils.isEmpty((CharSequence)password)) {
                config.preSharedKey = WiFi.convertToQuotedString(password);
            }
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        int len = wepKey.length();
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return WiFi.isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; --i) {
            char c = key.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f') continue;
            return false;
        }
        return true;
    }

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
            String quotedString = WiFi.convertToQuotedString(ssid);
            ssidEquals = bySsidIgnoreCase ? quotedString.equalsIgnoreCase(SSID) : quotedString.equals(SSID);
        }
        return ssidEquals;
    }

    public static String getSSIDSecurity(Context context, String ssid) {
        WifiManager wifiMgr = (WifiManager)context.getSystemService("wifi");
        if (wifiMgr == null) {
            return "";
        }
        List results = wifiMgr.getScanResults();
        if (results != null) {
            for (ScanResult result : results) {
                boolean ssidEquals = WiFi.isSsidEquals(ssid, result.SSID, true);
                if (!ssidEquals) continue;
                return WiFi.getScanResultSecurity(result);
            }
        }
        return "";
    }

    public static boolean configWifiInfo(WifiManager mWifiManager, String SSID, String password, ScanResult scanResult) {
        int type = WiFi.getType(scanResult);
        WifiConfiguration config = null;
        if (mWifiManager != null) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                mWifiManager.disableNetwork(wifiInfo.getNetworkId());
                mWifiManager.disconnect();
            }
            List existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig == null) continue;
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    config = existingConfig;
                    break;
                }
                LogUtil.d(TAG, "disable wifi " + existingConfig.SSID + " mWifiManager isEnable : " + mWifiManager.isWifiEnabled());
            }
        }
        if (config == null) {
            config = new WifiConfiguration();
        }
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (type == 0) {
            config.allowedKeyManagement.set(0);
        } else if (type == 1) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        } else if (type == 2) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }
        int netId = config.networkId;
        if (netId == -1) {
            netId = mWifiManager.addNetwork(config);
        }
        return mWifiManager.enableNetwork(netId, true);
    }

    private static int getType(ScanResult scanResult) {
        int type = 0;
        type = scanResult.capabilities.contains(WPA) ? 2 : (scanResult.capabilities.contains(WEP) ? 1 : 0);
        return type;
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
}

