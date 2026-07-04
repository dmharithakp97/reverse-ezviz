/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;

public class InnerException
extends BaseException {
    private static final long serialVersionUID = 1L;
    public static final int INNER_NO_ERROR = 400000;
    public static final int INNER_PARAM_NULL = 400001;
    public static final int INNER_PARAM_ERROR = 400002;
    public static final int INNER_DEVICE_NOT_EXIST = 400003;
    public static final int INNER_NETWORK_CONNECT_FAIL = 400022;

    public InnerException(String msg, ErrorInfo errorInfo) {
        super(msg, errorInfo != null ? errorInfo.errorCode : 0, errorInfo);
    }

    public InnerException(String msg) {
        super(msg, (ErrorInfo)null);
    }
}

