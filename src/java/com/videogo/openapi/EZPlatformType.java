/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi;

public enum EZPlatformType {
    EZPlatformTypeNULL(0),
    EZPlatformTypeOPENSDK(1),
    EZPlatformTypeGLOBALSDK(2);

    private int platformType;

    private EZPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getPlatformType() {
        return this.platformType;
    }
}

