/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model.req;

import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.bean.req.BatchGetTokens;
import com.videogo.openapi.model.BaseRequset;
import com.videogo.restful.NameValuePair;
import java.util.List;

public class BatchGetTokensReq
extends BaseRequset {
    public static final String URL = "/api/user/token";
    private static final String COUNT = "count";
    private static final String THRID_TOKEN = "thridToken";
    private BatchGetTokens batchGetTokens;

    @Override
    public List<NameValuePair> buidParams(BaseInfo info) {
        this.addPublicParams(info);
        this.batchGetTokens = (BatchGetTokens)info;
        this.nvps.add(new NameValuePair(COUNT, Integer.toString(this.batchGetTokens.getCount())));
        this.nvps.add(new NameValuePair(THRID_TOKEN, PlayAPI.getInstance().getThridToken()));
        return this.nvps;
    }
}

