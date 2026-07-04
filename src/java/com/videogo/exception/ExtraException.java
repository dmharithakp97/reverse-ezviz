/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.exception;

import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;

public class ExtraException
extends BaseException {
    private static final long serialVersionUID = 1L;
    public static final int USER_NAME_IS_NULL = 410001;
    public static final int USER_NAME_TOO_SHORT = 410002;
    public static final int USER_NAME_ALL_UNDER_LINE = 410003;
    public static final int USER_NAME_TOO_LOOG = 410004;
    public static final int USER_NAME_IS_ILLEGAL = 410005;
    public static final int USER_NAME_ALL_DIGIT = 410006;
    public static final int PASSWORD_IS_NULL = 410007;
    public static final int PASSWORD_TOO_SHORT = 410008;
    public static final int PASSWORD_TOO_LONG = 410009;
    public static final int PASSWORD_SAME_CHARACTER = 410010;
    public static final int PASSWORD_IS_ILLEGAL = 410011;
    public static final int NEW_PASSWORD_IS_NULL = 410012;
    public static final int NEW_PASSWORD_TOO_SHORT = 410013;
    public static final int NEW_PASSWORD_TOO_LONG = 410014;
    public static final int NEW_PASSWORD_SAME_CHARACTER = 410015;
    public static final int NEW_PASSWORD_IS_ILLEGAL = 410016;
    public static final int COMFIRM_PASSWORD_IS_NULL = 410017;
    public static final int PASSWORDS_NOT_EQUALS = 410018;
    public static final int PHONE_NUMBER_IS_NULL = 410019;
    public static final int PHONE_NUMBER_IS_ILLEGAL = 410020;
    public static final int MOBILE_PHONE_NUMBER_IS_NULL = 410021;
    public static final int MOBILE_PHONE_NUMBER_IS_ILLEGAL = 410022;
    public static final int VERIFY_CODE_IS_NULL = 410023;
    public static final int CAMERA_NAME_IS_NULL = 410024;
    public static final int CAMERA_NAME_IS_ILLEGAL = 410025;
    public static final int SERIALNO_IS_NULL = 410026;
    public static final int CAMERA_IS_NULL = 410027;
    public static final int DEVICE_SN_IS_NULL = 410028;
    public static final int CAMERA_PASSWORD_IS_NULL = 410029;
    public static final int DEVICE_IS_NULL = 410033;
    public static final int SERIALNO_IS_ILLEGAL = 410030;
    public static final int MOBILE_EMAIL_IS_ILLEGAL = 410031;
    public static final int MOBILE_EMAIL_IS_NULL = 410032;

    public ExtraException(ErrorInfo errorInfo) {
        super(errorInfo != null ? errorInfo.description : "", errorInfo != null ? errorInfo.errorCode : 0, errorInfo);
    }

    public ExtraException(String desc, ErrorInfo errorInfo) {
        super(desc, errorInfo != null ? errorInfo.errorCode : 0, errorInfo);
    }
}

