/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.model.resp;

import com.videogo.exception.BaseException;
import com.videogo.openapi.bean.EZServerInfo;
import com.videogo.openapi.model.BaseResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class GetServersInfoResp
extends BaseResponse {
    public static final String SERVERRESP = "serverResp";
    public static final String STUN1ADDR = "stun1Addr";
    public static final String STUN1PORT = "stun1Port";
    public static final String STUN2ADDR = "stun2Addr";
    public static final String STUN2PORT = "stun2Port";
    public static final String TTSADDR = "ttsAddr";
    public static final String TTSPORT = "ttsPort";
    public static final String VTMADDR = "vtmAddr";
    public static final String VTMPORT = "vtmPort";
    public static final String AUTHADDR = "authAddr";
    public static final String PUSHADDR = "pushAddr";
    public static final String PUSHHTTPPORT = "pushHttpPort";
    public static final String PUSHHTTPSPORT = "pushHttpsPort";
    public static final String MICRO_CLOUD_MODE = "microCloudMode";
    public static final String LOG_ADDR = "logAddr";
    public static final String LOG_OAS_ADDR = "oasLogAddr";
    public static final String AIMEDIAADDR = "aiMediaAddr";
    public static final String AIMEDIAPORT = "aiMediaPort";

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        if (this.parseCode(reponse)) {
            JSONObject all = new JSONObject(reponse);
            JSONObject object = all.getJSONObject(SERVERRESP);
            EZServerInfo serverInfo = new EZServerInfo();
            serverInfo.setStun1Addr(object.optString(STUN1ADDR));
            serverInfo.setStun1Port(object.optInt(STUN1PORT));
            serverInfo.setStun2Addr(object.optString(STUN2ADDR));
            serverInfo.setStun2Port(object.optInt(STUN2PORT));
            serverInfo.setVtmAddr(object.optString(VTMADDR));
            serverInfo.setVtmPort(object.optInt(VTMPORT));
            serverInfo.setMicroCloudMode(object.optBoolean(MICRO_CLOUD_MODE));
            serverInfo.setLogAddr(object.optString(LOG_ADDR));
            serverInfo.setOasLogAddr(object.optString(LOG_OAS_ADDR));
            serverInfo.setAuthAddr(object.optString(AUTHADDR));
            serverInfo.setPushAddr(object.optString(PUSHADDR));
            serverInfo.setPushHttpPort(object.optInt(PUSHHTTPPORT));
            serverInfo.setAiMediaAddr(object.optString(AIMEDIAADDR));
            serverInfo.setAiMediaPort(object.optInt(AIMEDIAPORT));
            return serverInfo;
        }
        return null;
    }
}

