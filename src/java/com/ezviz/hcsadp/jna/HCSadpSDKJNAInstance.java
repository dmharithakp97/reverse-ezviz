/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 */
package com.ezviz.hcsadp.jna;

import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import com.sun.jna.Native;

public enum HCSadpSDKJNAInstance {
    CLASS;

    private static HCSadpSDKByJNA sadpSdk;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static HCSadpSDKByJNA getInstance() {
        if (null != sadpSdk) return sadpSdk;
        Class<HCSadpSDKByJNA> clazz = HCSadpSDKByJNA.class;
        synchronized (HCSadpSDKByJNA.class) {
            sadpSdk = (HCSadpSDKByJNA)Native.loadLibrary((String)"sadp", HCSadpSDKByJNA.class);
            // ** MonitorExit[var0] (shouldn't be in output)
            return sadpSdk;
        }
    }

    static {
        sadpSdk = null;
    }
}

