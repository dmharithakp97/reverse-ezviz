/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import java.util.UUID;

public class UUIDUtil {
    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        if ((uuid = uuid.replaceAll("\\-", "")).length() > 32) {
            uuid = uuid.substring(0, 32);
        }
        return uuid;
    }
}

