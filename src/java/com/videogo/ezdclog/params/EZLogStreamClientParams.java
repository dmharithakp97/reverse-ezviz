/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package com.videogo.ezdclog.params;

import com.google.gson.annotations.Expose;
import com.videogo.ezdclog.params.BaseParams;
import com.videogo.openapi.annotation.HttpParam;

public class EZLogStreamClientParams
extends BaseParams {
    @HttpParam(name="systemName")
    public String systemName = "opensdk_mobile_play_main";
    @HttpParam(name="opId")
    public String opId;
    @HttpParam(name="serial")
    public String serial;
    @HttpParam(name="channel")
    public int channel;
    @HttpParam(name="enc")
    public int enc;
    @HttpParam(name="plTp")
    public int plTp;
    @HttpParam(name="sync")
    public int sync = 0;
    @HttpParam(name="vdLv")
    public int vdLv;
    @HttpParam(name="errCd")
    public int errCd = -2;
    @HttpParam(name="prepCt")
    public int prepCt;
    @HttpParam(name="requestCt")
    public int requestCt = -1;
    @HttpParam(name="time")
    public String time;
    @HttpParam(name="cost")
    public int cost;
    @HttpParam(name="via")
    public int via = -1;
    @HttpParam(name="start_t")
    public long start_t = -1L;
    @HttpParam(name="stop_t")
    public long stop_t = -1L;
    @HttpParam(name="cnt")
    public int cnt;
    @HttpParam(name="dnt")
    public int dnt;
    @Expose
    public long timebyLong;
}

