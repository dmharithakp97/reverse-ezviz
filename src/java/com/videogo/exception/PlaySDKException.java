/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;

public class PlaySDKException
extends BaseException {
    private static final long serialVersionUID = 1L;
    public static final int PLAYSDK_NO_ERROR = 320000;
    public static final int PLAYSDK_NOINIT = 320100;
    public static final int PLAYSDK_GET_PORT_FAIL = 320101;
    public static final int PLAYSDK_SET_FILEENDCB_FAIL = 320102;

    public PlaySDKException(String msg, ErrorInfo errorInfo) {
        super(msg, errorInfo != null ? errorInfo.errorCode : 0, errorInfo);
    }
}

