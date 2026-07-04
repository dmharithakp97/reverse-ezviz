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
import com.videogo.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseResponse {
    private static final String TAG = "BaseResponse";
    public static final String NETWORK_EXCEPTION_ERROR_MSG = "IO Error";
    public static final int RESULT_SUSCCEED = 200;
    public static final String RESULT_CODE = "resultCode";
    public static final String RESULT_DESC = "resultDesc";
    protected int resultCode = 200;

    public abstract Object parse(String var1) throws BaseException, JSONException;

    public boolean parseCode(String response) throws BaseException, JSONException {
        JSONObject jsonObject = new JSONObject(response);
        this.resultCode = jsonObject.optInt(RESULT_CODE, 400030);
        String resultDesc = jsonObject.optString(RESULT_DESC, "Resp Error:" + this.resultCode);
        if (this.resultCode == 200) {
            return true;
        }
        if (this.resultCode == 400030) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            throw new BaseException(NETWORK_EXCEPTION_ERROR_MSG, errorInfo.errorCode, errorInfo);
        }
        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
        if (errorInfo.description == null || errorInfo.description.length() == 0) {
            errorInfo.description = resultDesc;
        }
        throw new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
    }

    public boolean parseCodeHttp(String response) throws BaseException {
        try {
            JSONObject jsonObject = new JSONObject(response);
            this.resultCode = jsonObject.optInt("code", 400030);
            String resultDesc = jsonObject.optString("description", "Resp Error:" + this.resultCode);
            if (this.resultCode == 200) {
                return true;
            }
            if (this.resultCode == 400030) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
                BaseException baseException = new BaseException(NETWORK_EXCEPTION_ERROR_MSG, errorInfo.errorCode, errorInfo);
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
            BaseException exception = this.parseHttpJsonResult(response);
            if (exception != null) {
                throw exception;
            }
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            BaseException baseException = new BaseException("JSON\u89e3\u6790\u9519\u8bef", errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            throw baseException;
        }
    }

    private BaseException parseHttpJsonResult(String response) {
        Throwable baseException = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            this.resultCode = jsonObject.getInt("code");
            String resultDesc = jsonObject.optString("description", "Resp Error:" + this.resultCode);
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, this.resultCode);
            if (errorInfo.description.length() == 0) {
                errorInfo.description = resultDesc;
            }
            baseException = new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            return baseException;
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, baseException.fillInStackTrace());
            return null;
        }
    }
}

