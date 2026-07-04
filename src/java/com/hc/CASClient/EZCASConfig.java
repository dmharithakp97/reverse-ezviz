/*
 * Decompiled with CFR 0.152.
 */
package com.hc.CASClient;

public class EZCASConfig {

    public static enum StringType {
        CONFIG_CLIENT_NATTYPE(0),
        CONFIG_DYNAMIC_INFO(1),
        CONFIG_CLIENT_VERSION(6),
        CONFIG_APP_LOCALIP(7);

        private int type;

        private StringType(int type) {
            this.type = type;
        }

        int getType() {
            return this.type;
        }
    }

    public static enum IntType {
        CONFIG_CLIENT_NATTYPE(0),
        CONFIG_SSLCONNECT_TRYCOUNT(2),
        CONFIG_MAX43PUNCH_DEVICES(3),
        CONFIG_REVERSEDIRECT_CHECKTTYPE(4),
        CONFIG_CLIENT_TYPE(5);

        private int type;

        private IntType(int type) {
            this.type = type;
        }

        int getType() {
            return this.type;
        }
    }
}

