/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 *  com.sun.jna.Library
 *  com.sun.jna.Pointer
 *  com.sun.jna.Structure
 */
package com.ezviz.hcsadp.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public interface HCSadpSDKByJNA
extends Library {
    public static final int SADP_GET_DEVICE_CODE = 1;
    public static final int SADP_GET_ENCRYPT_STRING = 2;
    public static final int SADP_GET_GUID = 5;
    public static final int SADP_GET_SECURITY_QUESTION = 6;
    public static final int SADP_SET_SECURITY_QUESTION = 7;
    public static final int SADP_SET_HCPLATFORM_STATUS = 8;
    public static final int SADP_SET_VERIFICATION_CODE = 9;
    public static final int SADP_GET_BIND_LIST = 12;
    public static final int SADP_SET_BIND_LIST = 13;
    public static final int SADP_RESTORE_INACTIVE = 14;
    public static final int SADP_SET_WIFI_REGION = 15;
    public static final int SADP_SET_CHANNEL_DEFAULT_PASSWORD = 16;
    public static final int SADP_EHOME_ENABLE = 19;
    public static final int SADP_SET_USER_MAILBOX = 20;
    public static final int SADP_GET_QR_CODES = 21;
    public static final int SADP_GET_EZVIZ_UNBIND_STATUS = 24;
    public static final int SADP_EZVIZ_UNBIND = 25;
    public static final int SADP_GET_PASSWORD_RESET_TYPE = 27;
    public static final int SADP_SET_EZVIZ_USER_TOKEN = 28;
    public static final int SADP_GET_PHONE_QR_CODES = 29;
    public static final int SADP_GET_DEVICE_CODE_V31 = 30;
    public static final int SADP_GET_ENCRYPT_STRING_V31 = 31;
    public static final int SADP_GET_GUID_V31 = 32;
    public static final int SADP_GET_QR_CODES_V31 = 33;
    public static final int SADP_GET_MANAGER_PHONE_NUMBER = 34;
    public static final int SADP_SET_MANAGER_PHONE_NUMBER = 35;
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
    public static final int SADP_BIND_ERROR_DEV = 2034;
    public static final int SADP_EXTED_MAX_BIND_NUM = 2035;
    public static final int SADP_MAILBOX_NOT_EXIST = 2036;
    public static final int SADP_MAILBOX_NOT_SET = 2038;
    public static final int SADP_INVALID_RESET_CODE = 2039;
    public static final int SADP_NO_PERMISSION = 2040;
    public static final int SADP_GET_EXCHANGE_CODE_ERROR = 2041;
    public static final int SADP_CREATE_RSA_KEY_ERROR = 2042;
    public static final int SADP_BASE64_ENCODE_ERROR = 2043;
    public static final int SADP_BASE64_DECODE_ERROR = 2044;
    public static final int SADP_AES_ENCRYPT_ERROR = 2045;
    public static final int SADP_PHONE_NOT_SET = 2046;
    public static final int SADP_NOENOUGH_BUF = 2047;
    public static final int SADP_INVALID_SUBNET_IP = 2048;
    public static final int SADP_DISPLAY_ALL = 0;
    public static final int SADP_FILTER_EZVIZ = 1;
    public static final int SADP_FILTER_OEM = 2;
    public static final int SADP_FILTER_EZVIZ_OEM = 3;
    public static final int SADP_ONLY_DISPLAY_OEM = -3;
    public static final int SADP_ONLY_DISPLAY_EZVIZ = -2;
    public static final int SADP_WIFICONFIG_START_SUCESS = 2;
    public static final int SADP_WIFICONFIG_PARAM_ERROR = 3;

    public int SADP_Start_V30(DeviceFindCallBack var1, int var2, Pointer var3);

    public int SADP_Start_V40(DeviceFindCallBack_V40 var1, int var2, Pointer var3);

    public int SADP_Start_V50(Pointer var1);

    public int SADP_SendInquiry();

    public int SADP_InquirySpecificSubnet(Pointer var1);

    public int SADP_InquirySpecificSubnetAllDevice(Pointer var1);

    public int SADP_GetInquirySpecificSubnetAllDeviceStatus(Pointer var1);

    public int SADP_StopInquirySpecificSubnetAllDevice();

    public int SADP_Stop();

    public int SADP_Clearup();

    public int SADP_ActivateDevice(Pointer var1, Pointer var2);

    public int SADP_SetDeviceConfig(Pointer var1, int var2, Pointer var3, int var4, Pointer var5, int var6);

    public int SADP_GetDeviceConfig(Pointer var1, int var2, Pointer var3, int var4, Pointer var5, int var6);

    public int SADP_ModifyDeviceNetParam_V40(Pointer var1, Pointer var2, Pointer var3, Pointer var4, int var5);

    public int SADP_ModifyDeviceNetParam(Pointer var1, Pointer var2, Pointer var3);

    public int SADP_ResetPasswd(Pointer var1, Pointer var2);

    public int SADP_ResetPasswd_V40(Pointer var1, Pointer var2, Pointer var3);

    public int SADP_ResetPasswd_V50(Pointer var1, Pointer var2, Pointer var3);

    public int SADP_GetSadpVersion();

    public int SADP_SetLogToFile(int var1, Pointer var2, int var3);

    public int SADP_GetLastError();

    public int SADP_SetAutoRequestInterval(int var1);

    public int SADP_SetDeviceFilterRule(int var1, Pointer var2, int var3);

    public int SADP_ParseData(Pointer var1, int var2);

    public int SADP_Set_DataCB(DataCallBack var1, Pointer var2);

    public int SADP_Set_AdapterInfo(Pointer var1);

    public static interface DataCallBack
    extends Callback {
        public void invoke(SADP_DATA_INFO var1);
    }

    public static interface SubnetDeviceFindCallBack_V20
    extends Callback {
        public void invoke(SADP_SUBNET_DEVICE_INFO_V20 var1);
    }

    public static interface SubnetDeviceFindCallBack
    extends Callback {
        public void invoke(SADP_SUBNET_DEVICE_INFO var1);
    }

    public static interface DeviceFindCallBack_V40
    extends Callback {
        public void invoke(SADP_DEVICE_INFO_V40 var1);
    }

    public static interface DeviceFindCallBack
    extends Callback {
        public void invoke(SADP_DEVICE_INFO var1);
    }

    public static class ADAPTER_INFO_LIST
    extends Structure {
        public int dwCount;
        public INFO_ADAPTER[] struAdapterInfo = new INFO_ADAPTER[32];

        public ADAPTER_INFO_LIST() {
            for (int i = 0; i < 32; ++i) {
                this.struAdapterInfo[i] = new INFO_ADAPTER();
            }
        }

        protected List getFieldOrder() {
            return Arrays.asList("dwCount", "struAdapterInfo");
        }
    }

    public static class INFO_ADAPTER
    extends Structure {
        public byte[] szDeviceName = new byte[128];
        public byte[] szIPAddrStr = new byte[16];
        public byte[] szHWAddrStr = new byte[18];
        public int iIndex;
        public int dwDhcpEnabled;
        public byte[] byIPv6 = new byte[16];
        public byte[] szIPv6 = new byte[64];
        public byte[] szAdapterDesc = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("szDeviceName", "szIPAddrStr", "szHWAddrStr", "iIndex", "dwDhcpEnabled", "byIPv6", "szIPv6", "szAdapterDesc");
        }
    }

    public static class SADP_SUBNET_STATUS
    extends Structure {
        public int dwSize;
        public byte byStatus;
        public byte byProgress;
        public byte[] byRes = new byte[6];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "byStatus", "byProgress", "byRes");
        }
    }

    public static class SADP_SUBNET_INFO_V20
    extends Structure {
        public int dwSize;
        public byte byIPType;
        public byte byIPProbeEnable;
        public byte byPortProbeEnable;
        public byte byRes2;
        public short wSDKPort;
        public short wSDKOverTlsPort;
        public short wHttpPort;
        public short wHttpsPort;
        public short wStartPort;
        public short wStopPort;
        public byte[] byRes1 = new byte[4];
        public byte[] szStartSubnetIP = new byte[48];
        public byte[] szStopSubnetIP = new byte[48];
        public int dwIPProbeThreadNum;
        public int dwPortProbeThreadNum;
        public int dwIPProbeInterval;
        public int dwPortProbeInterval;
        public int dwIPProbeTimeout;
        public int dwPortProbeConnectTimeout;
        public int dwProtocolProbeTimeout;
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "byIPType", "byIPProbeEnable", "byPortProbeEnable", "byRes2", "wSDKPort", "wSDKOverTlsPort", "wHttpPort", "wHttpsPort", "wStartPort", "wStopPort", "byRes1", "szStartSubnetIP", "szStopSubnetIP", "dwIPProbeThreadNum", "dwPortProbeThreadNum", "dwIPProbeInterval", "dwPortProbeInterval", "dwIPProbeTimeout", "dwPortProbeConnectTimeout", "dwProtocolProbeTimeout", "byRes");
        }
    }

    public static class SADP_PHONE_NUMBER_PARAM
    extends Structure {
        public int dwSize;
        public byte[] szPassword = new byte[16];
        public byte[] szPhoneNo = new byte[16];
        public SADP_DEV_LOCK_INFO struLockInfo = new SADP_DEV_LOCK_INFO();
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szPassword", "szPhoneNo", "struLockInfo", "byRes");
        }
    }

    public static class SADP_PHONE_QR_CODES
    extends Structure {
        public int dwSize;
        public byte[] szDomainName = new byte[256];
        public byte[] szDevModel = new byte[32];
        public byte[] szQrCodes = new byte[1024];
        public byte[] szPhoneNo = new byte[16];
        public int dwValidTime;
        public byte[] byRes = new byte[108];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szDomainName", "szDevModel", "szQrCodes", "szPhoneNo", "dwValidTime", "byRes");
        }
    }

    public static class SADP_DATA_INFO
    extends Structure {
        public int dwDataLen;
        public byte[] szCommandType = new byte[64];
        public byte[] szSrcIP = new byte[16];
        public byte[] szDstIP = new byte[16];
        public SADP_TIME struTime = new SADP_TIME();

        protected List getFieldOrder() {
            return Arrays.asList("dwDataLen", "szCommandType", "szSrcIP", "szDstIP", "struTime");
        }
    }

    public static class SADP_TIME
    extends Structure {
        public int tm_usec;
        public int tm_sec;
        public int tm_min;
        public int tm_hour;
        public int tm_mday;
        public int tm_mon;
        public int tm_year;
        public int tm_wday;
        public int tm_yday;
        public int tm_isdst;
        public int tm_gmtoff;

        protected List getFieldOrder() {
            return Arrays.asList("tm_usec", "tm_sec", "tm_min", "tm_hour", "tm_mday", "tm_mon", "tm_year", "tm_wday", "tm_yday", "tm_isdst", "tm_gmtoff");
        }
    }

    public static class SADP_START_PARAM
    extends Structure {
        public DeviceFindCallBack_V40 fnDevCB;
        public SubnetDeviceFindCallBack fnSubnetDevCB;
        public Pointer pUserData;
        public byte byAdapterMode;
        public byte[] byRes1 = new byte[7];
        public SubnetDeviceFindCallBack_V20 fnSubnetDevCBV20;
        public byte[] byRes = new byte[1012];

        protected List getFieldOrder() {
            return Arrays.asList("fnDevCB", "fnSubnetDevCB", "pUserData", "byAdapterMode", "byRes1", "fnSubnetDevCBV20", "byRes");
        }
    }

    public static class SADP_SUBNET_DEVICE_INFO_V20
    extends Structure {
        public byte byProtocolType;
        public byte[] byRes1 = new byte[7];
        public byte[] szIPv4Address = new byte[16];
        public int dwPort;
        public byte[] byRes = new byte[996];

        protected List getFieldOrder() {
            return Arrays.asList("byProtocolType", "byRes1", "szIPv4Address", "dwPort", "byRes");
        }
    }

    public static class SADP_SUBNET_DEVICE_INFO
    extends Structure {
        public int dwDeviceType;
        public byte[] szDevDesc = new byte[64];
        public byte[] szSerialNO = new byte[128];
        public byte[] szIPv4Address = new byte[16];
        public byte[] szIPv4SubnetMask = new byte[16];
        public byte[] szIPv4Gateway = new byte[16];
        public byte[] szIPv6Address = new byte[46];
        public byte[] szIPv6Gateway = new byte[46];
        public byte byIPv6MaskLen;
        public byte bySupportIPv6;
        public byte bySupportModifyIPv6;
        public byte bySupportDhcp;
        public byte byDhcpEnabled;
        public byte[] byRes1 = new byte[3];
        public int dwCommandPort;
        public int dwSDKOverTLSPort;
        public int dwHttpPort;
        public int dwHttpsPort;
        public byte[] byRes = new byte[1008];

        protected List getFieldOrder() {
            return Arrays.asList("dwDeviceType", "szDevDesc", "szSerialNO", "szIPv4Address", "szIPv4SubnetMask", "szIPv4Gateway", "szIPv6Address", "szIPv6Gateway", "byIPv6MaskLen", "bySupportIPv6", "bySupportModifyIPv6", "bySupportDhcp", "byDhcpEnabled", "byRes1", "dwCommandPort", "dwSDKOverTLSPort", "dwHttpPort", "dwHttpsPort", "byRes");
        }
    }

    public static class SADP_SUBNET_INFO
    extends Structure {
        public int dwSize;
        public byte byIPType;
        public byte[] byRes1 = new byte[3];
        public byte[] szStartSubnetIP = new byte[48];
        public byte[] szStopSubnetIP = new byte[48];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "byIPType", "byRes1", "szStartSubnetIP", "szStopSubnetIP", "byRes");
        }
    }

    public static class SADP_DISPLAY_OEM_CFG
    extends Structure {
        public int dwDisplayOEM;
        public byte[] byRes = new byte[32];

        protected List getFieldOrder() {
            return Arrays.asList("dwDisplayOEM", "byRes");
        }
    }

    public static class SADP_EHOME_ENABLE_PARAM
    extends Structure {
        public int dwSize;
        public byte[] szDevID = new byte[16];
        public byte[] szEhomeKey = new byte[16];
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[64];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szDevID", "szEhomeKey", "szPassword", "byRes");
        }
    }

    public static class SADP_CHANNEL_DEFAULT_PASSWORD
    extends Structure {
        public byte[] szPassword = new byte[16];
        public byte[] szChannelDefaultPassword = new byte[16];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("szPassword", "szChannelDefaultPassword", "byRes");
        }
    }

    public static class SADP_WIFI_REGION_INFO
    extends Structure {
        public byte byMode;
        public byte byWifiRegion;
        public byte byWifiEnhancementEnabled;
        public byte byRes;
        public byte[] szPassword = new byte[16];
        public byte[] byRes2 = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("byMode", "byWifiRegion", "byWifiEnhancementEnabled", "byRes", "szPassword", "byRes2");
        }
    }

    public static class SADP_INACTIVE_INFO
    extends Structure {
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("szPassword", "byRes");
        }
    }

    public static class SADP_VERIFICATION_CODE_INFO
    extends Structure {
        public int dwSize;
        public byte[] szVerificationCode = new byte[12];
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szVerificationCode", "szPassword", "byRes");
        }
    }

    public static class SADP_BIND_LIST
    extends Structure {
        public SADP_BIND_INFO[] struBindInfo = new SADP_BIND_INFO[32];
        public byte[] szPassword = new byte[16];
        public byte byUnbindAll;
        public byte[] byRes = new byte[127];

        protected List getFieldOrder() {
            return Arrays.asList("struBindInfo", "szPassword", "byUnbindAll", "byRes");
        }
    }

    public static class SADP_BIND_INFO
    extends Structure {
        public byte[] szSerialNO = new byte[64];
        public byte byiBind;
        public byte[] byRes = new byte[127];

        protected List getFieldOrder() {
            return Arrays.asList("szSerialNO", "byiBind", "byRes");
        }
    }

    public static class SADP_HCPLATFORM_STATUS_INFO
    extends Structure {
        public int dwSize;
        public byte byEnableHCPlatform;
        public byte[] byRes = new byte[3];
        public byte[] szPassword = new byte[16];
        public byte[] byRes2 = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "byEnableHCPlatform", "byRes", "szPassword", "byRes2");
        }
    }

    public static class SADP_QR_CODES_V31
    extends Structure {
        public int dwCodeSize;
        public int dwMailBoxSize;
        public int dwServiceMailBoxSize;
        public byte[] szQrCodes = new byte[1024];
        public byte[] szMailBoxAddr = new byte[128];
        public byte[] szServiceMailBoxAddr = new byte[128];
        public byte[] byRes = new byte[256];

        protected List getFieldOrder() {
            return Arrays.asList("dwCodeSize", "dwMailBoxSize", "dwServiceMailBoxSize", "szQrCodes", "szMailBoxAddr", "szServiceMailBoxAddr", "byRes");
        }
    }

    public static class SADP_QR_CODES
    extends Structure {
        public int dwCodeSize;
        public int dwMailBoxSize;
        public int dwServiceMailBoxSize;
        public byte[] szQrCodes = new byte[256];
        public byte[] szMailBoxAddr = new byte[128];
        public byte[] szServiceMailBoxAddr = new byte[128];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwCodeSize", "dwMailBoxSize", "dwServiceMailBoxSize", "szQrCodes", "szMailBoxAddr", "szServiceMailBoxAddr", "byRes");
        }
    }

    public static class SADP_USER_MAILBOX
    extends Structure {
        public int dwSize;
        public byte[] szPassword = new byte[16];
        public byte[] szMailBoxAddr = new byte[128];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szPassword", "szMailBoxAddr", "byRes");
        }
    }

    public static class SADP_GUID_FILE_V31
    extends Structure {
        public int dwGUIDSize;
        public byte[] szGUID = new byte[512];
        public SADP_DEV_LOCK_INFO struDevLockInfo = new SADP_DEV_LOCK_INFO();
        public byte[] byRes = new byte[256];

        protected List getFieldOrder() {
            return Arrays.asList("dwGUIDSize", "szGUID", "struDevLockInfo", "byRes");
        }
    }

    public static class SADP_GUID_FILE
    extends Structure {
        public int dwGUIDSize;
        public byte[] szGUID = new byte[128];
        public byte byRetryGUIDTime;
        public byte bySurplusLockTime;
        public byte[] byRes = new byte[254];

        protected List getFieldOrder() {
            return Arrays.asList("dwGUIDSize", "szGUID", "byRetryGUIDTime", "bySurplusLockTime", "byRes");
        }
    }

    public static class SADP_GUID_FILE_COND
    extends Structure {
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("szPassword", "byRes");
        }
    }

    public static class SADP_SAFE_CODE_V31
    extends Structure {
        public int dwCodeSize;
        public byte[] szDeviceCode = new byte[512];
        public byte[] byRes = new byte[512];

        protected List getFieldOrder() {
            return Arrays.asList("dwCodeSize", "szDeviceCode", "byRes");
        }
    }

    public static class SADP_SAFE_CODE
    extends Structure {
        public int dwCodeSize;
        public byte[] szDeviceCode = new byte[128];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwCodeSize", "szDeviceCode", "byRes");
        }
    }

    public static class SADP_DEV_LOCK_INFO
    extends Structure {
        public byte byRetryTime;
        public byte bySurplusLockTime;
        public byte[] byRes = new byte[126];

        protected List getFieldOrder() {
            return Arrays.asList("byRetryTime", "bySurplusLockTime", "byRes");
        }
    }

    public static class SADP_EZVIZ_USER_TOKEN_PARAM
    extends Structure {
        public int dwSize;
        public byte[] szToken = new byte[16];
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[256];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szToken", "szPassword", "byRes");
        }
    }

    public static class SADP_EZVIZ_UNBIND_DEL_USER_PARAM
    extends Structure {
        public int dwSize;
        public byte[] szCode = new byte[256];
        public byte[] byRes = new byte[512];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szCode", "byRes");
        }
    }

    public static class SADP_EZVIZ_UNBIND_PARAM
    extends Structure {
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[256];

        protected List getFieldOrder() {
            return Arrays.asList("szPassword", "byRes");
        }
    }

    public static class SADP_EZVIZ_UNBIND_STATUS
    extends Structure {
        public byte byResult;
        public byte[] byRes = new byte[127];

        protected List getFieldOrder() {
            return Arrays.asList("byResult", "byRes");
        }
    }

    public static class SADP_ENCRYPT_STRING_V31
    extends Structure {
        public int dwEncryptStringSize;
        public byte[] szEncryptString = new byte[1024];
        public byte[] byRes = new byte[512];

        protected List getFieldOrder() {
            return Arrays.asList("dwEncryptStringSize", "szEncryptString", "byRes");
        }
    }

    public static class SADP_ENCRYPT_STRING
    extends Structure {
        public int dwEncryptStringSize;
        public byte[] szEncryptString = new byte[256];
        public byte[] byRes = new byte[128];

        protected List getFieldOrder() {
            return Arrays.asList("dwEncryptStringSize", "szEncryptString", "byRes");
        }
    }

    public static class SADP_SINGLE_SECURITY_QUESTION_CFG
    extends Structure {
        public int dwSize;
        public int dwId;
        public byte[] szAnswer = new byte[256];
        public byte byMark;
        public byte[] byRes = new byte[127];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "dwId", "szAnswer", "byMark", "byRes");
        }
    }

    public static class SADP_SECURITY_QUESTION
    extends Structure {
        public byte byRetryAnswerTime;
        public byte bySurplusLockTime;
        public byte[] byRes = new byte[254];

        protected List getFieldOrder() {
            return Arrays.asList("byRetryAnswerTime", "bySurplusLockTime", "byRes");
        }
    }

    public static class SADP_SECURITY_QUESTION_CFG
    extends Structure {
        public int dwSize;
        public SADP_SINGLE_SECURITY_QUESTION_CFG[] struSecurityQuestionCfg = new SADP_SINGLE_SECURITY_QUESTION_CFG[32];
        public byte[] szPassword = new byte[16];
        public byte[] byRes = new byte[512];

        public SADP_SECURITY_QUESTION_CFG() {
            for (int i = 0; i < 32; ++i) {
                this.struSecurityQuestionCfg[i] = new SADP_SINGLE_SECURITY_QUESTION_CFG();
            }
        }

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "struSecurityQuestionCfg", "szPassword", "byRes");
        }
    }

    public static class SADP_RET_RESET_PARAM_V40
    extends Structure {
        public byte byRetryGUIDTime;
        public byte bySurplusLockTime;
        public byte bRetryTimeValid;
        public byte bLockTimeValid;
        public byte[] byRes = new byte[252];

        protected List getFieldOrder() {
            return Arrays.asList("byRetryGUIDTime", "bySurplusLockTime", "bRetryTimeValid", "bLockTimeValid", "byRes");
        }
    }

    public static class SADP_RESET_PARAM_V50
    extends Structure {
        public int dwSize;
        public byte[] szPassword = new byte[128];
        public byte[] szCode = new byte[1024];
        public byte[] szAuthFile = new byte[260];
        public byte[] szGUID = new byte[512];
        public SADP_SECURITY_QUESTION_CFG struSecurityQuestionCfg = new SADP_SECURITY_QUESTION_CFG();
        public byte byResetType;
        public byte byEnableSyncIPCPW;
        public short wGUIDLen;
        public byte[] byRes = new byte[508];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "szPassword", "szCode", "szAuthFile", "szGUID", "struSecurityQuestionCfg", "byResetType", "byEnableSyncIPCPW", "wGUIDLen", "byRes");
        }
    }

    public static class SADP_RESET_PARAM_V40
    extends Structure {
        public int dwSize;
        public byte byResetType;
        public byte byEnableSyncIPCPW;
        public byte[] byRes2 = new byte[2];
        public byte[] szPassword = new byte[16];
        public byte[] szCode = new byte[256];
        public byte[] szAuthFile = new byte[260];
        public byte[] szGUID = new byte[128];
        public SADP_SECURITY_QUESTION_CFG struSecurityQuestionCfg = new SADP_SECURITY_QUESTION_CFG();
        public byte[] byRes = new byte[512];

        protected List getFieldOrder() {
            return Arrays.asList("dwSize", "byResetType", "byEnableSyncIPCPW", "byRes2", "szPassword", "szCode", "szAuthFile", "szGUID", "struSecurityQuestionCfg", "byRes");
        }
    }

    public static class SADP_RESET_PARAM
    extends Structure {
        public byte[] szCode = new byte[256];
        public byte[] szAuthFile = new byte[260];
        public byte[] szPassword = new byte[16];
        public byte byEnableSyncIPCPW;
        public byte[] byRes = new byte[511];

        protected List getFieldOrder() {
            return Arrays.asList("szCode", "szAuthFile", "szPassword", "byEnableSyncIPCPW", "byRes");
        }
    }

    public static class SADP_DEV_RET_NET_PARAM
    extends Structure {
        public byte byRetryModifyTime;
        public byte bySurplusLockTime;
        public byte[] byRes = new byte[126];

        public String toString() {
            return "SADP_DEV_RET_NET_PARAM.byRes: " + new String(this.byRes) + "\n";
        }

        protected List getFieldOrder() {
            return Arrays.asList("byRetryModifyTime", "bySurplusLockTime", "byRes");
        }
    }

    public static class SADP_DEV_NET_PARAM
    extends Structure {
        public byte[] szIPv4Address = new byte[16];
        public byte[] szIPv4SubNetMask = new byte[16];
        public byte[] szIPv4Gateway = new byte[16];
        public byte[] szIPv6Address = new byte[128];
        public byte[] szIPv6Gateway = new byte[128];
        public short wPort;
        public byte byIPv6MaskLen;
        public byte byDhcpEnable;
        public short wHttpPort;
        public int dwSDKOverTLSPort;
        public byte[] byRes = new byte[122];

        public String toString() {
            return "SADP_DEV_NET_PARAM.szIPv4Address: " + new String(this.szIPv4Address) + "\nSADP_DEV_NET_PARAM.szIPv4SubNetMask: " + new String(this.szIPv4SubNetMask) + "\nSADP_DEV_NET_PARAM.szIPv4Gateway: " + new String(this.szIPv4Gateway) + "\nSADP_DEV_NET_PARAM.szIPv6Address: " + new String(this.szIPv6Address) + "\nSADP_DEV_NET_PARAM.szIPv6Gateway: " + new String(this.szIPv6Gateway) + "\nSADP_DEV_NET_PARAM.byRes: " + new String(this.byRes) + "\n";
        }

        protected List getFieldOrder() {
            return Arrays.asList("szIPv4Address", "szIPv4SubNetMask", "szIPv4Gateway", "szIPv6Address", "szIPv6Gateway", "wPort", "byIPv6MaskLen", "byDhcpEnable", "wHttpPort", "dwSDKOverTLSPort", "byRes");
        }
    }

    public static class BYTE_ARRAY
    extends Structure {
        public byte[] byValue;

        public BYTE_ARRAY(int iLen) {
            this.byValue = new byte[iLen];
        }

        protected List<String> getFieldOrder() {
            return Arrays.asList("byValue");
        }
    }

    public static class SADP_DEVICE_INFO_V40
    extends Structure {
        public SADP_DEVICE_INFO struSadpDeviceInfo = new SADP_DEVICE_INFO();
        public byte byLicensed;
        public byte bySystemMode;
        public byte byControllerType;
        public byte[] szEhmoeVersion = new byte[16];
        public byte bySpecificDeviceType;
        public int dwSDKOverTLSPort;
        public byte bySecurityMode;
        public byte bySDKServerStatus;
        public byte bySDKOverTLSServerStatus;
        public byte[] szUserName = new byte[33];
        public byte[] szWifiMAC = new byte[20];
        public byte byDataFromMulticast;
        public byte bySupportEzvizUnbind;
        public byte bySupportCodeEncrypt;
        public byte bySupportPasswordResetType;
        public byte byEZVIZBindStatus;
        public byte[] szPhysicalAccessVerification = new byte[16];
        public short wHttpsPort;
        public byte bySupportEzvizUserToken;
        public byte[] szDevDescEx = new byte[64];
        public byte[] szSerialNOEx = new byte[128];
        public byte[] szManufacturer = new byte[32];
        public byte[] byRes = new byte[184];

        protected List getFieldOrder() {
            return Arrays.asList("struSadpDeviceInfo", "byLicensed", "bySystemMode", "byControllerType", "szEhmoeVersion", "bySpecificDeviceType", "dwSDKOverTLSPort", "bySecurityMode", "bySDKServerStatus", "bySDKOverTLSServerStatus", "szUserName", "szWifiMAC", "byDataFromMulticast", "bySupportEzvizUnbind", "bySupportCodeEncrypt", "bySupportPasswordResetType", "byEZVIZBindStatus", "szPhysicalAccessVerification", "wHttpsPort", "bySupportEzvizUserToken", "szDevDescEx", "szSerialNOEx", "szManufacturer", "byRes");
        }
    }

    public static class SADP_DEVICE_INFO
    extends Structure {
        public byte[] szSeries = new byte[12];
        public byte[] szSerialNO = new byte[48];
        public byte[] szMAC = new byte[20];
        public byte[] szIPv4Address = new byte[16];
        public byte[] szIPv4SubnetMask = new byte[16];
        public int dwDeviceType;
        public int dwPort;
        public int dwNumberOfEncoders;
        public int dwNumberOfHardDisk;
        public byte[] szDeviceSoftwareVersion = new byte[48];
        public byte[] szDSPVersion = new byte[48];
        public byte[] szBootTime = new byte[48];
        public int iResult;
        public byte[] szDevDesc = new byte[24];
        public byte[] szOEMinfo = new byte[24];
        public byte[] szIPv4Gateway = new byte[16];
        public byte[] szIPv6Address = new byte[46];
        public byte[] szIPv6Gateway = new byte[46];
        public byte byIPv6MaskLen;
        public byte bySupport;
        public byte byDhcpEnabled;
        public byte byDeviceAbility;
        public short wHttpPort;
        public short wDigitalChannelNum;
        public byte[] szCmsIPv4 = new byte[16];
        public short wCmsPort;
        public byte byOEMCode;
        public byte byActivated;
        public byte[] szBaseDesc = new byte[24];
        public byte bySupport1;
        public byte byHCPlatform;
        public byte byEnableHCPlatform;
        public byte byEZVIZCode;
        public int dwDetailOEMCode;
        public byte byModifyVerificationCode;
        public byte byMaxBindNum;
        public short wOEMCommandPort;
        public byte bySupportWifiRegion;
        public byte byEnableWifiEnhancement;
        public byte byWifiRegion;
        public byte bySupport2;

        protected List getFieldOrder() {
            return Arrays.asList("szSeries", "szSerialNO", "szMAC", "szIPv4Address", "szIPv4SubnetMask", "dwDeviceType", "dwPort", "dwNumberOfEncoders", "dwNumberOfHardDisk", "szDeviceSoftwareVersion", "szDSPVersion", "szBootTime", "iResult", "szDevDesc", "szOEMinfo", "szIPv4Gateway", "szIPv6Address", "szIPv6Gateway", "byIPv6MaskLen", "bySupport", "byDhcpEnabled", "byDeviceAbility", "wHttpPort", "wDigitalChannelNum", "szCmsIPv4", "wCmsPort", "byOEMCode", "byActivated", "szBaseDesc", "bySupport1", "byHCPlatform", "byEnableHCPlatform", "byEZVIZCode", "dwDetailOEMCode", "byModifyVerificationCode", "byMaxBindNum", "wOEMCommandPort", "bySupportWifiRegion", "byEnableWifiEnhancement", "byWifiRegion", "bySupport2");
        }
    }
}

