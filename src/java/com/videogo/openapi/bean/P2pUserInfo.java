/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.videogo.openapi.bean;

import com.google.gson.annotations.SerializedName;
import com.videogo.util.LogUtil;

public class P2pUserInfo {
    public String userId;
    @SerializedName(value="data")
    private String p2pLinkKey;
    public int saltIndex;
    @SerializedName(value="version")
    public int saltVersion;

    public short[] getTranslatedP2pLinkKey() {
        if (this.p2pLinkKey == null) {
            LogUtil.e("P2pUserInfo", "invalid origin p2pLinkKey");
            return null;
        }
        short[] shorts = null;
        try {
            String[] originStrKeys = this.p2pLinkKey.replace("{", "").replace("}", "").split(",");
            shorts = new short[originStrKeys.length];
            for (int i = 0; i < originStrKeys.length; ++i) {
                shorts[i] = Short.parseShort(originStrKeys[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return shorts;
    }
}

