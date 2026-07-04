/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi.model;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.model.BaseResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class EZAlarmDeleteMultipleAlarmsResp
extends BaseResponse {
    @Override
    public boolean parseCode(String reponse) throws BaseException, JSONException {
        JSONObject jsonObject = new JSONObject(reponse);
        JSONObject result = jsonObject.getJSONObject("result");
        this.resultCode = result.optInt("code", 400030);
        String resultDesc = result.optString("description", "Resp Error:" + this.resultCode);
        if (this.resultCode == 200) {
            return true;
        }
        if (this.resultCode == 400030) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
        }
        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
        if (errorInfo.description == null || errorInfo.description.length() == 0) {
            errorInfo.description = resultDesc;
        }
        throw new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
    }

    @Override
    public Object parse(String reponse) throws BaseException, JSONException {
        return this.parseCode(reponse);
    }
}

