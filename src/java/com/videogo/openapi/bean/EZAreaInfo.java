/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;

public class EZAreaInfo {
    @Serializable(name="id")
    private int mId = -1;
    @Serializable(name="name")
    private String mName = null;
    @Serializable(name="region")
    private String mRegion = null;
    @Serializable(name="telphoneCode")
    private String mTelphoneCode;

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getRegion() {
        return this.mRegion;
    }

    public void setRegion(String region) {
        this.mRegion = region;
    }

    public String getTelphoneCode() {
        return this.mTelphoneCode;
    }

    public void setTelphoneCode(String telphoneCode) {
        this.mTelphoneCode = telphoneCode;
    }
}

