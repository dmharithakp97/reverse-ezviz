/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.ezdclog.params;

import com.videogo.openapi.annotation.HttpParam;

public class BaseParams {
    @HttpParam(name="appId")
    public String appId;
    @HttpParam(name="macId")
    public String macId;
    @HttpParam(name="sdkVer")
    public String sdkVer;
    @HttpParam(name="ver")
    public String ver;
    @HttpParam(name="platAddr")
    public String platAddr;
    @HttpParam(name="startTime")
    public String startTime;
    @HttpParam(name="exterVer")
    public String exterVer;
    @HttpParam(name="cltType")
    public int cltType;
}

