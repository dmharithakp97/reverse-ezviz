/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.keyprotect;

public class KeyProtect {
    static KeyProtect keyProtect = null;

    static {
        System.loadLibrary("encryptprotect");
    }

    public static synchronized KeyProtect getInstance() {
        if (keyProtect == null) {
            keyProtect = new KeyProtect();
        }
        return keyProtect;
    }

    public native int ENCRYPT_GetKey(byte[] var1, int var2, byte[] var3, int var4);
}

