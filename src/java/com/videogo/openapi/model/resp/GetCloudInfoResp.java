/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.model.resp;

import com.videogo.exception.BaseException;
import com.videogo.main.IspInfo;
import com.videogo.main.StreamServer;
import com.videogo.main.StreamServerData;
import com.videogo.openapi.model.BaseResponse;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCloudInfoResp
extends BaseResponse {
    public static final String STREAMSERVER = "streamServer";
    public static final String S1 = "s1";
    public static final String S2 = "s2";
    public static final String EXTERNAL_CMD_PORT = "external_cmd_port";
    public static final String EXTERNAL_DATA_PORT = "external_data_port";
    public static final String INDEX = "index";
    public static final String INTERNAL_CMD_PORT = "internal_cmd_port";
    public static final String INTERNAL_DATA_PORT = "internal_data_port";
    public static final String ISPINFO = "ispinfo";
    public static final String EXTERNALIP = "externalIp";
    public static final String ISPCODE = "ispcode";
    public static final String LOADING = "loading";
    public static final String TYPE = "type";

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        if (this.parseCode(reponse)) {
            JSONObject all = new JSONObject(reponse);
            String streamServer = all.optString(STREAMSERVER);
            JSONObject streamObject = new JSONObject(streamServer);
            StreamServerData streamServerData = new StreamServerData();
            StreamServer s1 = new StreamServer();
            boolean isS1Null = streamObject.isNull(S1);
            if (!isS1Null) {
                JSONObject s1Object = streamObject.getJSONObject(S1);
                JSONArray s1IspInfoArray = s1Object.getJSONArray(ISPINFO);
                s1.setExternalCmdPort(s1Object.optInt(EXTERNAL_CMD_PORT));
                s1.setExternalDataPort(s1Object.optInt(EXTERNAL_DATA_PORT));
                s1.setIndex(s1Object.optString(INDEX));
                s1.setInternalCmdPort(s1Object.optInt(INTERNAL_CMD_PORT));
                s1.setInternalDataPort(s1Object.optInt(INTERNAL_DATA_PORT));
                s1.setLoading(s1Object.optInt(LOADING));
                s1.setType(s1Object.optInt(TYPE));
                ArrayList<IspInfo> s1IspInfos = new ArrayList<IspInfo>();
                for (int i = 0; i < s1IspInfoArray.length(); ++i) {
                    JSONObject ispObject = s1IspInfoArray.getJSONObject(i);
                    IspInfo ispInfo = new IspInfo();
                    ispInfo.setExternalIp(ispObject.optString(EXTERNALIP));
                    ispInfo.setIspcode(ispObject.optLong(ISPCODE));
                    s1IspInfos.add(ispInfo);
                }
                s1.setIspInfos(s1IspInfos);
                streamServerData.setS1(s1);
            }
            StreamServer s2 = new StreamServer();
            boolean isS2NULL = streamObject.isNull(S2);
            if (!isS2NULL) {
                JSONObject s2Object = streamObject.getJSONObject(S2);
                JSONArray s2IspInfoArray = s2Object.getJSONArray(ISPINFO);
                s2.setExternalCmdPort(s2Object.optInt(EXTERNAL_CMD_PORT));
                s2.setExternalDataPort(s2Object.optInt(EXTERNAL_DATA_PORT));
                s2.setIndex(s2Object.optString(INDEX));
                s2.setInternalCmdPort(s2Object.optInt(INTERNAL_CMD_PORT));
                s2.setInternalDataPort(s2Object.optInt(INTERNAL_DATA_PORT));
                s2.setLoading(s2Object.optInt(LOADING));
                s2.setType(s2Object.optInt(TYPE));
                ArrayList<IspInfo> s2IspInfos = new ArrayList<IspInfo>();
                for (int i = 0; i < s2IspInfoArray.length(); ++i) {
                    JSONObject ispObject = s2IspInfoArray.getJSONObject(i);
                    IspInfo ispInfo = new IspInfo();
                    ispInfo.setExternalIp(ispObject.optString(EXTERNALIP));
                    ispInfo.setIspcode(ispObject.optLong(ISPCODE));
                    s2IspInfos.add(ispInfo);
                }
                s2.setIspInfos(s2IspInfos);
                streamServerData.setS2(s2);
            }
            return streamServerData;
        }
        return null;
    }
}

