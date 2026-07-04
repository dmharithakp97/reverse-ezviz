/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.errorlayer;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;

public class StreamRtspErrorLayer
extends ErrorLayer {
    @Override
    public ErrorInfo getErrorInfo(int errorCode) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode < 0 ? 360000 + errorCode : 350000 + errorCode;
        errorInfo.description = this.getDescription(errorInfo.errorCode);
        return errorInfo;
    }

    @Override
    public String getDescription(int errorCode) {
        return "\u8bf7\u67e5\u770b\u9519\u8bef\u7801\u6587\u6863";
    }
}

