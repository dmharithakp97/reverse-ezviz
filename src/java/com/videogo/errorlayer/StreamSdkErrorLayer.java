/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.videogo.errorlayer;

import android.text.TextUtils;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.EZOpenSDKErrorInfo;
import com.videogo.exception.EZOpenSDKErrorManager;

public class StreamSdkErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        switch (errorCode / 10000) {
            case 1: {
                int tranferCode = errorCode % 10000;
                if (tranferCode > 1000) {
                    tranferCode = 1000 + tranferCode % 1000;
                }
                errorInfo = ErrorLayer.getErrorLayer(33, tranferCode);
                break;
            }
            case 2: {
                int tranferCode = errorCode % 10000;
                errorInfo = ErrorLayer.getErrorLayer(32, tranferCode);
                break;
            }
            case 3: {
                int tranferCode = errorCode % 30000;
                errorInfo = ErrorLayer.getErrorLayer(35, tranferCode);
                break;
            }
            case 4: {
                int tranferCode = errorCode % 40000;
                errorInfo = ErrorLayer.getErrorLayer(36, tranferCode);
                break;
            }
            default: {
                errorInfo.errorCode = 320000 + errorCode;
                errorInfo.description = this.getDescription(errorCode);
                EZOpenSDKErrorInfo errorInfo1 = EZOpenSDKErrorManager.getMamager().getEZOpenSDKErrorInfo(String.valueOf(errorInfo.errorCode), true);
                if (errorInfo1 == null || TextUtils.isEmpty((CharSequence)errorInfo1.description)) break;
                errorInfo.description = errorInfo1.description;
                errorInfo.moduleCode = errorInfo1.moduleCode;
                errorInfo.solution = errorInfo1.solution;
            }
        }
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        String description = "";
        switch (errorCode) {
            case 320001: {
                description = "\u672a\u77e5\u9519\u8bef";
                break;
            }
            case 320002: {
                description = "\u53c2\u6570\u9519\u8bef";
                break;
            }
            case 320003: {
                description = "\u53d6\u6d41\u7c7b\u578b\u9519\u8bef\uff0c\u662f\u5426\u4f7f\u7528\u4e86\u9884\u89c8\u7684\u7c7b\u5bf9\u8c61\u5904\u7406\u56de\u653e\u4e86\uff1f";
                break;
            }
            case 320004: {
                description = "\u5185\u5b58\u4e0d\u591f";
                break;
            }
            case 320005: {
                description = "\u521b\u5efaCAS Session\u51fa\u9519";
                break;
            }
            case 320006: {
                description = "\u521b\u5efa\u4e91\u56de\u653eSession\u51fa\u9519";
                break;
            }
            case 320007: {
                description = "\u53d6\u6d41token\u5931\u6548\uff0c\u8bf7\u91cd\u65b0\u8bbe\u7f6e\u53d6\u6d41token";
                break;
            }
            case 320008: {
                description = "\u672a\u4f20\u5165\u53d6\u6d41token";
                break;
            }
            case 320009: {
                description = "\u9700\u8981reset initParam\u53c2\u6570";
                break;
            }
            case 320010: {
                description = "\u8bf7\u91cd\u8bd5";
                break;
            }
            case 320011: {
                description = "sleep 500\u6beb\u79d2\u540e\u518d\u91cd\u8bd5";
                break;
            }
            case 320012: {
                description = "token\u6c60\u5df2\u6ee1";
                break;
            }
            case 320013: {
                description = "P2P\u6570\u91cf\u8d85\u8fc7\u9650\u5236";
                break;
            }
            case 320014: {
                description = "SDK\u672a\u521d\u59cb\u5316";
                break;
            }
            default: {
                description = "";
            }
        }
        return description;
    }
}

