/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.ezviz.http.bean.resp;

import com.ezviz.http.model.EzWifiInfo;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetDeviceWifiListResp {
    @SerializedName(value="access_point_list")
    public List<EzWifiInfo> ezWifiInfoList;
}

