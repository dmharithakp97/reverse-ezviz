/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

public class PtzUtils {
    public static int convertBytesToInt(byte[] bytes, int begin, int count) {
        int value = 0;
        for (int i = begin; i < begin + count && i < bytes.length; ++i) {
            value += (bytes[i] & 0xFF) << 8 * (i - begin);
        }
        return value;
    }
}

