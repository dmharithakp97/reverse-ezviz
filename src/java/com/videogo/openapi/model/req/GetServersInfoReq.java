/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model.req;

import com.videogo.constant.Config;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.model.BaseRequset;
import com.videogo.restful.NameValuePair;
import java.util.List;

public class GetServersInfoReq
extends BaseRequset {
    public static final String URL = "/api/server/info";

    @Override
    public List<NameValuePair> buidParams(BaseInfo info) {
        if (Config.ENABLE_SDK_TKTOKEN) {
            info.fixHttpToken();
        }
        this.addPublicParams(info);
        return this.nvps;
    }
}

