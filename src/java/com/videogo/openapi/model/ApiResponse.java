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
import com.videogo.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ApiResponse
extends BaseResponse {
    private static final String TAG = "ApiResponse";
    public static final String RESULT = "result";
    public static final int SUSCCEED = 200;
    public static final String META = "meta";
    public static final String CODE = "code";
    public static final String MSG = "description";
    public static final String MSG2 = "msg";
    public static final String DATA = "data";

    @Override
    public abstract Object parse(String var1) throws BaseException, JSONException;

    public boolean parseMetaCode(String response) throws BaseException {
        boolean isSuc = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject metaObject = jsonObject.optJSONObject(META);
            if (metaObject != null) {
                String resultCode = metaObject.optString(CODE);
                isSuc = String.valueOf(200).equals(resultCode);
                if (!isSuc) {
                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, metaObject.optInt(CODE));
                    throw new BaseException(metaObject.optString("message"), errorInfo);
                }
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return isSuc;
    }

    @Override
    public boolean parseCode(String response) throws BaseException {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject(RESULT);
            this.resultCode = result.optInt(CODE, 400030);
            String resultDesc = result.optString(MSG, "Resp Error:" + this.resultCode);
            if (this.resultCode == 200) {
                return true;
            }
            if (this.resultCode == 400030) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                BaseException baseException = new BaseException("IO Error", errorInfo.errorCode, errorInfo);
                LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
                throw baseException;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
            if (errorInfo.description == null || errorInfo.description.length() <= 0) {
                errorInfo.description = resultDesc;
            }
            if (errorInfo.description == null || errorInfo.description.length() <= 0) {
                errorInfo.description = result.optString(MSG2, "Resp Error:" + this.resultCode);
            }
            BaseException baseException = new BaseException(errorInfo.description, errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            BaseException exception = this.parseOldJsonResult(response);
            if (exception != null) {
                throw exception;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            BaseException baseException = new BaseException("JSON\u89e3\u6790\u9519\u8bef", errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
    }

    @Override
    public boolean parseCodeHttp(String response) throws BaseException {
        try {
            JSONObject jsonObject = new JSONObject(response);
            this.resultCode = jsonObject.optInt(CODE, 400030);
            String resultDesc = jsonObject.optString(MSG, "Resp Error:" + this.resultCode);
            if (this.resultCode == 200) {
                return true;
            }
            if (this.resultCode == 400030) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                BaseException baseException = new BaseException("IO Error", errorInfo.errorCode, errorInfo);
                LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
                throw baseException;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
            if (errorInfo.description.length() <= 0) {
                errorInfo.description = resultDesc;
            }
            BaseException baseException = new BaseException(errorInfo.description, errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
        catch (JSONException e) {
            BaseException exception = this.parseOldJsonResult(response);
            if (exception != null) {
                throw exception;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            BaseException baseException = new BaseException("JSON\u89e3\u6790\u9519\u8bef", errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
    }

    public boolean paserCodeHttpNoReport(String response) throws BaseException {
        try {
            JSONObject jsonObject = new JSONObject(response);
            this.resultCode = jsonObject.optInt(CODE, 400030);
            String resultDesc = jsonObject.optString(MSG, "Resp Error:" + this.resultCode);
            if (this.resultCode == 200) {
                return true;
            }
            if (this.resultCode == 400030) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(-1, 400030);
                BaseException baseException = new BaseException("IO Error", errorInfo.errorCode, errorInfo);
                LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
                throw baseException;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(-1, this.resultCode);
            if (errorInfo.description.length() <= 0) {
                errorInfo.description = resultDesc;
            }
            BaseException baseException = new BaseException(errorInfo.description, errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
        catch (JSONException e) {
            BaseException exception = this.parseOldJsonResult(response);
            if (exception != null) {
                throw exception;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(-1, 400030);
            BaseException baseException = new BaseException("JSON\u89e3\u6790\u9519\u8bef", errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
    }

    private BaseException parseOldJsonResult(String response) {
        BaseException baseException = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            this.resultCode = jsonObject.getInt("resultCode");
            String resultDesc = jsonObject.optString("resultDesc", "Resp Error:" + this.resultCode);
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
            if (errorInfo.description.length() == 0) {
                errorInfo.description = resultDesc;
            }
            baseException = new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            return baseException;
        }
        catch (JSONException jSONException) {
            return null;
        }
    }
}

