/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.model;

import com.videogo.openapi.bean.BaseInfo;
import com.videogo.restful.NameValuePair;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRequset {
    public List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    public static final String ACCESSTOKEN = "accessToken";
    private static final String CLIENTTYPE = "clientType";
    private static final String FEATURECODE = "featureCode";
    private static final String OSVERSION = "osVersion";
    private static final String SDKVERSION = "sdkVersion";
    private static final String NETTYPE = "netType";
    private static final String APPKEY = "appKey";
    private static final String APPNAME = "appName";
    private static final String APPID = "appID";

    public abstract List<NameValuePair> buidParams(BaseInfo var1);

    public void addPublicParams(BaseInfo info) {
        this.nvps.add(new NameValuePair(ACCESSTOKEN, info.getAccessToken()));
        this.nvps.add(new NameValuePair(CLIENTTYPE, info.getClientType()));
        this.nvps.add(new NameValuePair(FEATURECODE, info.getFeatureCode()));
        this.nvps.add(new NameValuePair(OSVERSION, info.getOsVersion()));
        this.nvps.add(new NameValuePair(SDKVERSION, "v5.27.3.20260319"));
        this.nvps.add(new NameValuePair(NETTYPE, info.getNetType()));
        this.nvps.add(new NameValuePair(APPKEY, info.getAppKey()));
        this.nvps.add(new NameValuePair(APPNAME, info.getAppName()));
        this.nvps.add(new NameValuePair(APPID, info.getAppID()));
    }
}

