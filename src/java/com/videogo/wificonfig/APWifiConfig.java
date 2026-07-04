/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum
 *  com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum
 *  com.ezviz.sdk.configwifi.common.EZConfigWifiCallback
 */
package com.videogo.wificonfig;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.videogo.wificonfig.ConfigWifiErrorEnum;

public class APWifiConfig {

    public static abstract class APConfigCallback
    extends EZConfigWifiCallback
    implements APConfigCallbackInterface {
        @Override
        public void onErrorNew(ConfigWifiErrorEnum error) {
        }

        public void reportInfo(EZConfigWifiInfoEnum info) {
            super.reportInfo(info);
            if (info == EZConfigWifiInfoEnum.CONNECTING_SENT_CONFIGURATION_TO_DEVICE) {
                this.onSuccess();
            }
        }

        public void reportError(EZConfigWifiErrorEnum error) {
            super.reportError(error);
            if (error == EZConfigWifiErrorEnum.CONFIG_TIMEOUT) {
                this.OnError(error.code);
                this.onErrorNew(ConfigWifiErrorEnum.CONFIG_TIMEOUT);
            }
        }
    }

    public static interface APConfigCallbackInterface {
        public void onSuccess();

        public void OnError(int var1);

        public void onErrorNew(ConfigWifiErrorEnum var1);
    }
}

