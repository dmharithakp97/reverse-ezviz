/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.stream.EZEcdhKeyInfo
 */
package com.videogo.ecdh;

import com.ez.stream.EZEcdhKeyInfo;
import com.videogo.util.Base64;

public class EcdhKeyInfo {
    private String szPBKey;
    private int iPBKeyLen;
    private String szPRKey;
    private int iPRKeyLen;
    private long time = 0L;

    public static EcdhKeyInfo create(EZEcdhKeyInfo ezEcdhKeyInfo) {
        EcdhKeyInfo ecdhKeyInfo = new EcdhKeyInfo();
        ecdhKeyInfo.iPBKeyLen = ezEcdhKeyInfo.iPBKeyLen;
        ecdhKeyInfo.iPRKeyLen = ezEcdhKeyInfo.iPRKeyLen;
        ecdhKeyInfo.szPBKey = Base64.encode(ezEcdhKeyInfo.szPBKey);
        ecdhKeyInfo.szPRKey = Base64.encode(ezEcdhKeyInfo.szPRKey);
        ecdhKeyInfo.time = System.currentTimeMillis();
        return ecdhKeyInfo;
    }

    public EZEcdhKeyInfo toEZEcdhKeyInfo() {
        EZEcdhKeyInfo ezEcdhKeyInfo = new EZEcdhKeyInfo();
        ezEcdhKeyInfo.iPBKeyLen = this.iPBKeyLen;
        ezEcdhKeyInfo.iPRKeyLen = this.iPRKeyLen;
        ezEcdhKeyInfo.szPBKey = new byte[this.iPBKeyLen];
        ezEcdhKeyInfo.szPBKey = Base64.decode(this.szPBKey);
        ezEcdhKeyInfo.szPRKey = Base64.decode(this.szPRKey);
        return ezEcdhKeyInfo;
    }

    public long getTime() {
        return this.time;
    }
}

