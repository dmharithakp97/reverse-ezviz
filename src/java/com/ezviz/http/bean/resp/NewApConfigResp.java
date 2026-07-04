/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.bean.resp;

import com.google.gson.annotations.SerializedName;

public class NewApConfigResp {
    @SerializedName(value="status_code")
    public int statusCode;
    @SerializedName(value="status_string")
    public String statusDesc;
}

