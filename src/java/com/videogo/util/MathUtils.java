/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

public class MathUtils {
    public static int cryptHexToInt(char cHex) {
        if (cHex >= '0' && cHex <= '9') {
            return cHex - 48;
        }
        if (cHex >= 'a' && cHex <= 'f') {
            return cHex - 97 + 10;
        }
        if (cHex >= 'A' && cHex <= 'F') {
            return cHex - 65 + 10;
        }
        return 0;
    }

    public static short byteToShort(byte[] b) {
        if (b == null) {
            return -1;
        }
        short s = 0;
        short s0 = (short)(b[0] & 0xFF);
        short s1 = (short)(b[1] & 0xFF);
        s0 = (short)(s0 << 8);
        s = (short)(s0 | s1);
        return s;
    }

    public static int getLength(byte[] src) {
        if (src == null) {
            return -1;
        }
        return (src[0] & 0xFF) << 24 | (src[1] & 0xFF) << 16 | (src[2] & 0xFF) << 8 | src[3] & 0xFF;
    }
}

