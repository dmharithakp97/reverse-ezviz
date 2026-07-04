/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorProxy;

public abstract class ErrorLayer {
    private static ErrorProxy mErrorProxy;

    public static void setErrorProxy(ErrorProxy proxy) {
        mErrorProxy = proxy;
    }

    public static ErrorInfo getErrorLayer(int operationType, int errorCode) {
        return mErrorProxy.getErrorLayer(operationType, errorCode);
    }

    protected void checkTransf(ErrorInfo errorInfo) {
        switch (errorInfo.errorCode) {
            case 120007: 
            case 361012: 
            case 380121: 
            case 395404: {
                errorInfo.description = "\u8bbe\u5907\u4e0d\u5728\u7ebf(\u8f6c\u4e49\u524d\u9519\u8bef\u7801" + errorInfo.errorCode + ")";
                errorInfo.errorCode = 400901;
                break;
            }
            case 110002: 
            case 110003: 
            case 380049: 
            case 380146: 
            case 380253: 
            case 395412: {
                errorInfo.description = "AccessToken\u5f02\u5e38\uff0c\u53ef\u80fd\u662f\u8fc7\u671f\u6216\u8005\u6821\u9a8c\u5931\u8d25\uff0c\u91cd\u65b0\u83b7\u53d6accessToken\u540e\u53ef\u91cd\u8bd5(\u8f6c\u4e49\u524d\u9519\u8bef\u7801" + errorInfo.errorCode + ")";
                errorInfo.errorCode = 400902;
                break;
            }
            case 120031: 
            case 380128: {
                errorInfo.description = "\u8d26\u53f7\u5f00\u542f\u4e86\u7ec8\u7aef\u7ed1\u5b9a\uff0c\u53ea\u5141\u8bb8\u6307\u5b9a\u8bbe\u5907\u767b\u5f55\uff0c\u8bf7\u767b\u5f55i.ys7.com\u89e3\u9664\u7ec8\u7aef\u7ed1\u5b9a(\u8f6c\u4e49\u524d\u9519\u8bef\u7801" + errorInfo.errorCode + ")";
                errorInfo.errorCode = 400903;
                break;
            }
            case 361010: 
            case 380077: {
                errorInfo.description = "\u8bbe\u5907\u6b63\u5728\u5bf9\u8bb2\u4e2d\uff0c\u5173\u95ed\u5bf9\u8bb2\u540e\u8bbe\u5907\u9700\u8981\u4e00\u5c0f\u6bb5\u65f6\u95f4\u91ca\u653e\u8d44\u6e90\uff0c\u5173\u95ed\u5bf9\u8bb2\u540e\u7acb\u523b\u5f00\u542f\u5bf9\u8bb2\u53ef\u80fd\u4f1a\u62a5\u6b64\u9519\u8bef\u7801(\u8f6c\u4e49\u524d\u9519\u8bef\u7801" + errorInfo.errorCode + ")";
                errorInfo.errorCode = 400904;
                break;
            }
            case 360013: 
            case 361013: 
            case 380127: 
            case 395409: {
                errorInfo.description = "\u8bbe\u5907\u5f00\u542f\u4e86\u9690\u79c1\u4fdd\u62a4\uff0c\u4e0d\u5141\u8bb8\u9884\u89c8\u3001\u5bf9\u8bb2\u7b49(\u8f6c\u4e49\u524d\u9519\u8bef\u7801" + errorInfo.errorCode + ")";
                errorInfo.errorCode = 395409;
                break;
            }
        }
    }

    public abstract ErrorInfo getErrorInfo(int var1);

    public abstract String getDescription(int var1);
}

