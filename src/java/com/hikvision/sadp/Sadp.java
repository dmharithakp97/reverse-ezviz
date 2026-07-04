/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

import com.hikvision.sadp.DeviceFindCallBack;
import com.hikvision.sadp.DeviceFindCallBack_V40;
import com.hikvision.sadp.SADP_CONDITION;
import com.hikvision.sadp.SADP_CONFIG;
import com.hikvision.sadp.SADP_DEVICE_FILTER_RULE;
import com.hikvision.sadp.SADP_DEV_NET_PARAM;

public class Sadp {
    static Sadp sadp = null;
    public static final int SADP_ADD = 1;
    public static final int SADP_UPDATE = 2;
    public static final int SADP_DEC = 3;
    public static final int SADP_RESTART = 4;
    public static final int SADP_UPDATEFAIL = 5;
    public static final int SADP_GET_DEVICE_CODE = 1;
    public static final int SADP_GET_ENCRYPT_STRING = 2;
    public static final int SADP_GET_DEVICE_TYPE_UNLOCK_CODE = 3;
    public static final int SADP_SET_DEVICE_CUSTOM_TYPE = 4;
    public static final int SADP_GET_GUID = 5;
    public static final int SADP_GET_SECURITY_QUESTION = 6;
    public static final int SADP_SET_SECURITY_QUESTION = 7;
    public static final int SADP_SET_HCPLATFORM_STATUS = 8;
    public static final int SADP_SET_VERIFICATION_CODE = 9;
    public static final int MAX_DEVICE_CODE = 128;
    public static final int MAX_EXCHANGE_CODE = 256;
    public static final int MAX_PASS_LEN = 16;
    public static final int MAX_FILE_PATH_LEN = 260;
    public static final int MAX_ENCRYPT_CODE = 256;
    public static final int MAX_UNLOCK_CODE_RANDOM_LEN = 256;
    public static final int MAX_UNLOCK_CODE_KEY = 256;
    public static final int MAX_GUID_LEN = 128;
    public static final int MAX_QUESTION_LIST_LEN = 32;
    public static final int MAX_ANSWER_LEN = 256;
    public static final int SADP_MAX_VERIFICATION_CODE_LEN = 12;
    public static final int SADP_ERROR_BASE = 2000;
    public static final int SADP_NOERROR = 0;
    public static final int SADP_ALLOC_RESOURCE_ERROR = 2001;
    public static final int SADP_NOT_START_ERROR = 2002;
    public static final int SADP_NO_ADAPTER_ERROR = 2003;
    public static final int SADP_GET_ADAPTER_FAIL_ERROR = 2004;
    public static final int SADP_PARAMETER_ERROR = 2005;
    public static final int SADP_OPEN_ADAPTER_FAIL_ERROR = 2006;
    public static final int SADP_SEND_PACKET_FAIL_ERROR = 2007;
    public static final int SADP_SYSTEM_CALL_ERROR = 2008;
    public static final int SADP_DENY_OR_TIMEOUT_ERROR = 2009;
    public static final int SADP_NPF_INSTALL_FAILED = 2010;
    public static final int SADP_TIMEOUT = 2011;
    public static final int SADP_CREATE_SOCKET_ERROR = 2012;
    public static final int SADP_BIND_SOCKET_ERROR = 2013;
    public static final int SADP_JOIN_MULTI_CAST_ERROR = 2014;
    public static final int SADP_NETWORK_SEND_ERROR = 2015;
    public static final int SADP_NETWORK_RECV_ERROR = 2016;
    public static final int SADP_XML_PARSE_ERROR = 2017;
    public static final int SADP_LOCKED = 2018;
    public static final int SADP_NOT_ACTIVATED = 2019;
    public static final int SADP_RISK_PASSWORD = 2020;
    public static final int SADP_HAS_ACTIVATED = 2021;
    public static final int SADP_EMPTY_ENCRYPT_STRING = 2022;
    public static final int SADP_EXPORT_FILE_OVERDUE = 2023;
    public static final int SADP_PASSWORD_ERROR = 2024;
    public static final int SADP_LONG_SECURITY_ANSWER = 2025;
    public static final int SADP_INVALID_GUID = 2026;
    public static final int SADP_ANSWER_ERROR = 2027;
    public static final int SADP_QUESTION_NUM_ERR = 2028;
    public static final int SADP_LOAD_WPCAP_FAIL = 2030;
    public static final int SADP_ILLEGAL_VERIFICATION_CODE = 2033;
    public static final int SADP_DISPLAY_ALL = 0;
    public static final int SADP_FILTER_EZVIZ = 1;
    public static final int SADP_FILTER_OEM = 2;
    public static final int SADP_FILTER_EZVIZ_OEM = 3;
    public static final int SADP_ONLY_DISPLAY_OEM = -3;
    public static final int SADP_ONLY_DISPLAY_EZVIZ = -2;
    public static final int SADP_WIFICONFIG_START_SUCESS = 2;
    public static final int SADP_WIFICONFIG_PARAM_ERROR = 3;

    static {
        System.loadLibrary("sadp");
    }

    public static synchronized Sadp getInstance() {
        if (sadp == null) {
            sadp = new Sadp();
        }
        return sadp;
    }

    public native boolean SADP_Start_V30(DeviceFindCallBack var1);

    public native boolean SADP_Start_V40(DeviceFindCallBack_V40 var1);

    public native boolean SADP_Stop();

    public native int SADP_ModifyDeviceNetParam(String var1, String var2, SADP_DEV_NET_PARAM var3);

    public native int SADP_GetLastError();

    public native int SADP_SendInquiry();

    public native boolean SADP_SetLogToFile(int var1, String var2, boolean var3);

    public native int SADP_Clearup();

    public native void SADP_SetAutoRequestInterval(int var1);

    public native int SADP_ActivateDevice(String var1, String var2);

    public native boolean SADP_SetDeviceFilterRule(int var1, SADP_DEVICE_FILTER_RULE var2);

    public native boolean SADP_SetDeviceConfig(String var1, int var2, SADP_CONDITION var3, SADP_CONFIG var4);
}

