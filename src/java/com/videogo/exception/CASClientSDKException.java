/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;

public class CASClientSDKException
extends BaseException {
    private static final long serialVersionUID = 1L;
    public static final int CASCLIENT_NO_ERROR = 380000;
    public static final int CASCLIENT_NOINIT = 389998;
    public static final int CASCLIENT_MSG_PU_NO_RESOURCE = 380045;
    public static final int CASCLIENT_CAS_TALK_CHANNEL_BUSY = 380077;
    public static final int CASCLIENT_CAS_PU_OPEN_PRIVACY = 380127;
    public static final int CASCLIENT_CAS_STREAM_SEND_ERROR = 381103;
    public static final int CASCLIENT_CAS_STREAM_RECV_ERROR = 381102;
    public static final int CASCLIENT_CAS_NO_DATA = 389997;
    public static final int CASCLIENT_STREAM_ERROR = 381000;
    public static final int CASCLIENT_AUDIO_ERROR = 382000;
    public static final int CASCLIENT_PLAYBACK_ERROR = 383000;

    public CASClientSDKException(String msg, ErrorInfo errorInfo) {
        super(msg, errorInfo != null ? errorInfo.errorCode : 0, errorInfo);
    }

    public CASClientSDKException(String msg, ErrorInfo errorInfo, int retryCount) {
        super(msg, errorInfo != null ? errorInfo.errorCode : 0, retryCount, errorInfo);
    }
}

