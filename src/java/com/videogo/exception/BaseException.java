/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

import com.videogo.errorlayer.ErrorInfo;

public class BaseException
extends Exception {
    private int mErrorCode;
    private int mRetryCount = 1;
    private ErrorInfo mErrorInfo;

    public BaseException(String msg, ErrorInfo obg) {
        this(msg, 0, 0, obg);
    }

    public BaseException(String msg, int errorCode) {
        super(msg);
        this.mErrorCode = errorCode;
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.errorCode = errorCode;
        errorInfo.description = msg;
        this.mErrorInfo = errorInfo;
    }

    public BaseException(String msg, int errorCode, ErrorInfo object) {
        this(msg, errorCode, 0, object);
    }

    public BaseException(String msg, int errorCode, int retryCount, ErrorInfo errorInfo) {
        super(msg);
        this.mErrorCode = errorCode;
        this.mRetryCount = retryCount;
        this.mErrorInfo = errorInfo;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public void setErrorInfo(ErrorInfo object) {
        this.mErrorInfo = object;
    }

    public ErrorInfo getErrorInfo() {
        return this.mErrorInfo;
    }

    @Deprecated
    public ErrorInfo getObject() {
        return this.mErrorInfo;
    }
}

