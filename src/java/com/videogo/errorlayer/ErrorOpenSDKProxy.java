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
import com.videogo.errorlayer.ErrorProxy;
import com.videogo.errorlayer.InnerErrorLayer;
import com.videogo.errorlayer.NPCClientErrorLayer;
import com.videogo.errorlayer.StreamCASErrorLayer;
import com.videogo.errorlayer.StreamClientErrorLayer;
import com.videogo.errorlayer.StreamNewTtsErrorLayer;
import com.videogo.errorlayer.StreamRtspErrorLayer;
import com.videogo.errorlayer.StreamSdkErrorLayer;
import com.videogo.errorlayer.StreamTtsErrorLayer;
import com.videogo.errorlayer.WebErrorLayer;
import com.videogo.exception.EZOpenSDKErrorInfo;
import com.videogo.exception.EZOpenSDKErrorManager;

public class ErrorOpenSDKProxy
extends ErrorProxy {
    private static ErrorOpenSDKProxy proxy;

    public static ErrorOpenSDKProxy getInstance() {
        if (proxy == null) {
            proxy = new ErrorOpenSDKProxy();
        }
        return proxy;
    }

    @Override
    ErrorInfo getErrorLayer(int operationType, int errorCode) {
        ErrorLayer errorLayer = null;
        switch (operationType) {
            case -1: {
                errorLayer = new WebErrorLayer();
                break;
            }
            case 1: {
                errorLayer = new WebErrorLayer();
                break;
            }
            case 2: {
                errorLayer = new InnerErrorLayer();
                break;
            }
            case 33: {
                errorLayer = new StreamCASErrorLayer();
                break;
            }
            case 32: {
                errorLayer = new StreamClientErrorLayer();
                break;
            }
            case 34: {
                errorLayer = new StreamRtspErrorLayer();
                break;
            }
            case 35: {
                errorLayer = new StreamTtsErrorLayer();
                break;
            }
            case 36: {
                errorLayer = new StreamNewTtsErrorLayer();
                break;
            }
            case 31: {
                errorLayer = new StreamSdkErrorLayer();
                break;
            }
            case 50: {
                errorLayer = new NPCClientErrorLayer();
                break;
            }
        }
        if (errorLayer == null) {
            return null;
        }
        ErrorInfo errorInfo = errorLayer.getErrorInfo(errorCode);
        errorLayer.checkTransf(errorInfo);
        EZOpenSDKErrorInfo errorInfo1 = EZOpenSDKErrorManager.getMamager().getEZOpenSDKErrorInfo(String.valueOf(errorInfo.errorCode), operationType != -1);
        if (errorInfo1 != null && !TextUtils.isEmpty((CharSequence)errorInfo1.description)) {
            errorInfo.description = errorInfo1.description;
            errorInfo.moduleCode = errorInfo1.moduleCode;
            errorInfo.solution = errorInfo1.solution;
        }
        return errorInfo;
    }
}

