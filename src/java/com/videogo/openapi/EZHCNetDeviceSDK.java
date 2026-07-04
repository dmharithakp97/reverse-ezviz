/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Log
 *  com.hikvision.keyprotect.KeyProtect
 *  com.hikvision.netsdk.HCNetSDK
 *  com.hikvision.netsdk.INT_PTR
 *  com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30
 *  com.hikvision.netsdk.NET_DVR_SDKLOCAL_CFG
 *  com.sun.jna.Memory
 *  com.sun.jna.Pointer
 */
package com.videogo.openapi;

import android.text.TextUtils;
import android.util.Log;
import com.ezviz.hcnetsdk.EZLoginDeviceInfo;
import com.ezviz.hcnetsdk.EZSADPDeviceInfo;
import com.ezviz.hcsadp.Interface.JavaInterface;
import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import com.hikvision.keyprotect.KeyProtect;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_SDKLOCAL_CFG;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.videogo.exception.BaseException;
import com.videogo.util.LogUtil;
import com.videogo.util.MD5Util;
import java.io.File;
import java.util.Locale;

public class EZHCNetDeviceSDK {
    static EZHCNetDeviceSDK mEZHCNetSDKApi = null;
    private static EZEncryptType mType;
    private static boolean LOGGING;
    private static String logFilePath;

    public void setEncryptType(EZEncryptType type) {
        mType = type;
        this.setConfig();
    }

    private EZHCNetDeviceSDK() {
        JavaInterface javaInterface = JavaInterface.getInstance();
        if (javaInterface == null) {
            // empty if block
        }
        if (HCNetSDK.getInstance() == null) {
            javaInterface.SADP_Clearup();
        }
        HCNetSDK.getInstance().NET_DVR_Init();
        this.setConfig();
        HCNetSDK.getInstance().NET_DVR_SetConnectTime(5000);
    }

    public static EZHCNetDeviceSDK getInstance() {
        if (mEZHCNetSDKApi != null) {
            return mEZHCNetSDKApi;
        }
        return new EZHCNetDeviceSDK();
    }

    private void setConfig() {
        NET_DVR_SDKLOCAL_CFG struSdkCfg = new NET_DVR_SDKLOCAL_CFG();
        HCNetSDK.getInstance().NET_DVR_GetSDKLocalConfig(struSdkCfg);
        struSdkCfg.byEnableAbilityParse = 1;
        if (mType == EZEncryptType.EZEncryptType_OEMBlue) {
            byte[] bule_byScrkey = new byte[]{72, -106, 104, -93, 97, -65, 110, -75, 103, -51, 122, -2, 104, -54, 111, -34, 117, 73, 75, 55, 92};
            KeyProtect.getInstance().ENCRYPT_GetKey(bule_byScrkey, 21, struSdkCfg.byProtectKey, 128);
        } else if (mType == EZEncryptType.EZEncryptType_OEMGreen) {
            byte[] green_byScrkey = new byte[]{72, -105, 104, -93, 97, -65, 110, -75, 103, -51, 122, -2, 104, -54, 111, -34, 117, 73, 75, 57, 92};
            KeyProtect.getInstance().ENCRYPT_GetKey(green_byScrkey, 21, struSdkCfg.byProtectKey, 128);
        }
        HCNetSDK.getInstance().NET_DVR_SetSDKLocalConfig(struSdkCfg);
    }

    public void setDebugLogEnable(boolean enable, String logFilePath) {
        LOGGING = enable;
        EZHCNetDeviceSDK.logFilePath = logFilePath;
    }

    public boolean startLocalSearch(SadpDeviceFoundListener listener) {
        boolean ret = false;
        Log.d((String)"NetSDK", (String)"startLocalSearch");
        if (LOGGING && !TextUtils.isEmpty((CharSequence)logFilePath)) {
            File file = new File(logFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            HCSadpSDKByJNA.BYTE_ARRAY sPath = new HCSadpSDKByJNA.BYTE_ARRAY(logFilePath.length() + 1);
            System.arraycopy(logFilePath.getBytes(), 0, sPath.byValue, 0, logFilePath.length());
            sPath.write();
            JavaInterface.getInstance().SADP_SetLogToFile(3, sPath, 1);
        }
        HCSadpSDKByJNA.SADP_START_PARAM struStartParam = new HCSadpSDKByJNA.SADP_START_PARAM();
        struStartParam.fnDevCB = struDeviceInfo_v40 -> {
            EZSADPDeviceInfo deviceInfo = new EZSADPDeviceInfo(struDeviceInfo_v40.struSadpDeviceInfo);
            listener.onDeviceFound(deviceInfo);
        };
        struStartParam.pUserData = Pointer.NULL;
        boolean iRet = JavaInterface.getInstance().SADP_Start_V50(struStartParam);
        JavaInterface.getInstance().SADP_SetAutoRequestInterval(30);
        JavaInterface.getInstance().SADP_SendInquiry();
        return iRet;
    }

    public boolean stopLocalSearch() {
        boolean ret = false;
        if (JavaInterface.getInstance() != null) {
            ret = JavaInterface.getInstance().SADP_Stop();
        }
        return ret;
    }

    public int activeDeviceWithSerial(String serail, String pwd) {
        Log.d((String)"NetSDK", (String)String.format(Locale.CHINA, "activeDeviceWithSerial [%s][%s]", serail, pwd));
        Memory serailMem = new Memory((long)(serail.length() + 1));
        serailMem.setString(0L, serail);
        Memory serailPtr = serailMem;
        Memory pwdMem = new Memory((long)(pwd.length() + 1));
        pwdMem.setString(0L, pwd);
        Memory pwdMemPtr = pwdMem;
        boolean iFlag = JavaInterface.getInstance().SADP_ActivateDevice((Pointer)serailPtr, (Pointer)pwdMemPtr);
        return iFlag ? 1 : 0;
    }

    public EZLoginDeviceInfo loginDeviceWithUerName(String userName, String pwd, String deviceip, int port) throws BaseException {
        int userId = -1;
        HCNetSDK mHCNetSDK = HCNetSDK.getInstance();
        if (mHCNetSDK == null || TextUtils.isEmpty((CharSequence)deviceip) || port < 1) {
            LogUtil.e("EZHCNetSDK", "mHCNetSDK is null or deviceip is null or port Less than 1");
            return null;
        }
        int errorCode = 0;
        NET_DVR_DEVICEINFO_V30 devInfo = new NET_DVR_DEVICEINFO_V30();
        userId = mHCNetSDK.NET_DVR_Login_V30(deviceip, port, "EZ_LOCAL_USER", MD5Util.getMD5String16(pwd), devInfo);
        if (userId < 0) {
            errorCode = mHCNetSDK.NET_DVR_GetLastError();
            LogUtil.e("EZHCNetSDK", "EZ_LOCAL_USER NET_DVR_Login is failed!err:" + errorCode);
            if (errorCode == 7 || errorCode == 10) {
                return null;
            }
        } else {
            LogUtil.d("EZHCNetSDK", "EZ_LOCAL_USER login success");
        }
        if (userId < 0) {
            userId = mHCNetSDK.NET_DVR_Login_V30(deviceip, port, userName, pwd, devInfo);
            if (userId < 0) {
                errorCode = mHCNetSDK.NET_DVR_GetLastError();
                INT_PTR ptt = new INT_PTR();
                ptt.iValue = errorCode;
                String errorMsg = HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptt);
                LogUtil.e("EZHCNetSDK", "admin NET_DVR_Login is failed!err:" + errorCode);
                throw new BaseException(errorMsg, errorCode, null);
            }
            LogUtil.d("EZHCNetSDK", "admin login success");
        }
        if (userId >= 0) {
            EZLoginDeviceInfo ezLoginDeviceInfo = new EZLoginDeviceInfo(devInfo, userId);
            return ezLoginDeviceInfo;
        }
        return null;
    }

    public boolean logoutDeviceWithUserId(int userId) {
        boolean ret = false;
        if (HCNetSDK.getInstance() != null && userId != -1) {
            ret = HCNetSDK.getInstance().NET_DVR_Logout_V30(userId);
        }
        return ret;
    }

    static {
        LOGGING = false;
    }

    public static enum EZEncryptType {
        EZEncryptType_normal,
        EZEncryptType_OEMGreen,
        EZEncryptType_OEMBlue;

    }

    public static interface SadpDeviceFoundListener {
        public void onDeviceFound(EZSADPDeviceInfo var1);
    }

    public static enum PTZAction {
        PTZ_ACTION_START(0),
        PTZ_ACTION_STOP(1);

        private int value;

        private PTZAction(int ret) {
            this.value = ret;
        }

        public int getValue() {
            return this.value;
        }
    }
}

