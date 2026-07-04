/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model.req;

import com.videogo.constant.Config;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.bean.req.GetStreamServer;
import com.videogo.openapi.model.BaseRequset;
import com.videogo.restful.NameValuePair;
import java.util.List;

public class GetCloudInfoReq
extends BaseRequset {
    public static final String URL = "/api/cloud/get";
    public static final String ISPTYPE = "ispType";
    private GetStreamServer getStreamServer;

    @Override
    public List<NameValuePair> buidParams(BaseInfo info) {
        if (Config.ENABLE_SDK_TKTOKEN) {
            info.fixHttpToken();
        }
        this.addPublicParams(info);
        this.getStreamServer = (GetStreamServer)info;
        this.nvps.add(new NameValuePair(ISPTYPE, this.getStreamServer.getISPType() + ""));
        return this.nvps;
    }
}

