/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

public class EZPMPlayPrivateTokenInfo {
    private boolean enable;
    private String token;

    public EZPMPlayPrivateTokenInfo(boolean enable, String token) {
        this.enable = enable;
        this.token = token;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

