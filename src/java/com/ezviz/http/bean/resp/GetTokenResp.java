/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.bean.resp;

import com.ezviz.http.bean.base.BaseResp;
import com.ezviz.http.model.DeviceTokenInfo;
import com.google.gson.annotations.SerializedName;

public class GetTokenResp
extends BaseResp {
    @SerializedName(value="deviceTokenInfo")
    public DeviceTokenInfo deviceTokenInfo;
}

