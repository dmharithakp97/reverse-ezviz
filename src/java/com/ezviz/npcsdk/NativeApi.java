/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.npcsdk;

import com.ezviz.npcsdk.OnNpcListener;

public class NativeApi {
    public static native long createNPC(String var0, int var1, boolean var2, OnNpcListener var3);

    public static native void destroyNPC(long var0);

    static {
        try {
            System.loadLibrary("NPClient");
            System.loadLibrary("MediaPlatform");
            System.loadLibrary("npcplayer");
            System.loadLibrary("PlayCtrl");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

