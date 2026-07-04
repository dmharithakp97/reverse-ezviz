/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  com.sun.jna.Pointer
 */
package com.ezviz.hcsadp.Interface;

import android.util.Log;
import com.ezviz.hcsadp.Interface.IPRefresh;
import com.ezviz.hcsadp.Interface.MulticastServer;
import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import com.ezviz.hcsadp.jna.HCSadpSDKJNAInstance;
import com.sun.jna.Pointer;

public class JavaInterface {
    private static JavaInterface m_pJavaInterface = null;
    private MulticastServer m_pReceiveData = null;
    private IPRefresh m_pIPRefresh = null;
    public boolean bReceive = true;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static JavaInterface getInstance() {
        if (null != m_pJavaInterface) return m_pJavaInterface;
        Class<JavaInterface> clazz = JavaInterface.class;
        synchronized (JavaInterface.class) {
            m_pJavaInterface = new JavaInterface();
            // ** MonitorExit[var0] (shouldn't be in output)
            return m_pJavaInterface;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void Init() {
        if (!this.bReceive) {
            return;
        }
        if (null == this.m_pReceiveData) {
            Class<MulticastServer> clazz = MulticastServer.class;
            // MONITORENTER : com.ezviz.hcsadp.Interface.MulticastServer.class
            this.m_pReceiveData = new MulticastServer();
            // MONITOREXIT : clazz
        }
        this.m_pReceiveData.start();
    }

    private void Fini() {
        if (this.m_pReceiveData != null) {
            this.m_pReceiveData.stop();
        }
    }

    public boolean SADP_Start_V30(HCSadpSDKByJNA.DeviceFindCallBack callBack, int bInstallNPF, Pointer pUserData) {
        if (null == callBack) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V30 Failed!");
            return false;
        }
        if (HCSadpSDKJNAInstance.getInstance().SADP_Start_V30(callBack, bInstallNPF, pUserData) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V30 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Start_V30 Success!");
        return true;
    }

    public boolean SADP_Start_V40(HCSadpSDKByJNA.DeviceFindCallBack_V40 callBack, int bInstallNPF, Pointer pUserData) {
        if (null == callBack) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V40 Failed!");
            return false;
        }
        if (HCSadpSDKJNAInstance.getInstance().SADP_Start_V40(callBack, bInstallNPF, pUserData) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V40 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Start_V40 Success!");
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean SADP_Start_V50(HCSadpSDKByJNA.SADP_START_PARAM struStartParam) {
        if (null == struStartParam) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V50 Failed!");
            return false;
        }
        if (null == this.m_pIPRefresh) {
            Class<IPRefresh> clazz = IPRefresh.class;
            // MONITORENTER : com.ezviz.hcsadp.Interface.IPRefresh.class
            this.m_pIPRefresh = new IPRefresh();
            // MONITOREXIT : clazz
        }
        this.m_pIPRefresh.SetAdapterInfo();
        struStartParam.write();
        if (HCSadpSDKJNAInstance.getInstance().SADP_Start_V50(struStartParam.getPointer()) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Start_V50 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Start_V50 Success!");
        return true;
    }

    public boolean SADP_Stop() {
        this.Fini();
        if (HCSadpSDKJNAInstance.getInstance().SADP_Stop() == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Stop Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Stop Success!");
        return true;
    }

    public boolean SADP_Clearup() {
        if (HCSadpSDKJNAInstance.getInstance().SADP_Clearup() == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Clearup Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Clearup Success!");
        return true;
    }

    public boolean SADP_SendInquiry() {
        if (HCSadpSDKJNAInstance.getInstance().SADP_SendInquiry() == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SendInquiry Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SendInquiry Success!");
        return true;
    }

    public boolean SADP_InquirySpecificSubnet(HCSadpSDKByJNA.SADP_SUBNET_INFO struSubnetInfo) {
        if (null == struSubnetInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnet Failed!");
            return false;
        }
        struSubnetInfo.write();
        if (HCSadpSDKJNAInstance.getInstance().SADP_InquirySpecificSubnet(struSubnetInfo.getPointer()) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnet Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnet Success!");
        return true;
    }

    public boolean SADP_InquirySpecificSubnetAllDevice(HCSadpSDKByJNA.SADP_SUBNET_INFO_V20 struSubnetInfo_V20) {
        if (null == struSubnetInfo_V20) {
            Log.e((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnetAllDevice Failed!");
            return false;
        }
        struSubnetInfo_V20.write();
        if (HCSadpSDKJNAInstance.getInstance().SADP_InquirySpecificSubnetAllDevice(struSubnetInfo_V20.getPointer()) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnetAllDevice Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_InquirySpecificSubnetAllDevice Success!");
        return true;
    }

    public boolean SADP_GetInquirySpecificSubnetAllDeviceStatus(HCSadpSDKByJNA.SADP_SUBNET_STATUS struStatus) {
        if (null == struStatus) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetInquirySpecificSubnetAllDeviceStatus Failed!");
            return false;
        }
        struStatus.write();
        if (HCSadpSDKJNAInstance.getInstance().SADP_GetInquirySpecificSubnetAllDeviceStatus(struStatus.getPointer()) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetInquirySpecificSubnetAllDeviceStatus Failed!");
            return false;
        }
        struStatus.read();
        Log.i((String)"[JavaInterface]", (String)"SADP_GetInquirySpecificSubnetAllDeviceStatus Success!");
        return true;
    }

    public boolean SADP_StopInquirySpecificSubnetAllDevice() {
        if (HCSadpSDKJNAInstance.getInstance().SADP_StopInquirySpecificSubnetAllDevice() == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_StopInquirySpecificSubnetAllDevice Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_StopInquirySpecificSubnetAllDevice Success!");
        return true;
    }

    public boolean SADP_ModifyDeviceNetParam(Pointer sMAC, Pointer sPassword, HCSadpSDKByJNA.SADP_DEV_NET_PARAM struNetParam) {
        if (null == sMAC || null == sPassword || null == struNetParam) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam Failed!");
            return false;
        }
        struNetParam.write();
        if (HCSadpSDKJNAInstance.getInstance().SADP_ModifyDeviceNetParam(sMAC, sPassword, struNetParam.getPointer()) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam Success!");
        return true;
    }

    public boolean SADP_ModifyDeviceNetParam_V40(Pointer sMAC, Pointer sPassword, HCSadpSDKByJNA.SADP_DEV_NET_PARAM struNetParam, HCSadpSDKByJNA.SADP_DEV_RET_NET_PARAM struRetParam) {
        if (null == sMAC || null == sPassword || null == struNetParam || null == struRetParam) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam_V40 Failed!");
            return false;
        }
        struNetParam.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_ModifyDeviceNetParam_V40(sMAC, sPassword, struNetParam.getPointer(), struRetParam.getPointer(), struRetParam.size());
        struRetParam.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam_V40 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_ModifyDeviceNetParam_V40 Success!");
        return true;
    }

    public boolean SADP_ActivateDevice(Pointer sDevSerialNO, Pointer sCommand) {
        if (null == sDevSerialNO || null == sCommand) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ActivateDevice Failed!");
            return false;
        }
        if (HCSadpSDKJNAInstance.getInstance().SADP_ActivateDevice(sDevSerialNO, sCommand) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ActivateDevice Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_ActivateDevice Success!");
        return true;
    }

    public void SADP_SetAutoRequestInterval(int dwInterval) {
        HCSadpSDKJNAInstance.getInstance().SADP_SetAutoRequestInterval(dwInterval);
    }

    public boolean SADP_SetDeviceFilterRule(int dwFilterRule, Pointer lpInBuff, int dwInBuffLen) {
        if (HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceFilterRule(dwFilterRule, lpInBuff, dwInBuffLen) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetDeviceFilterRule Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetDeviceFilterRule Success!");
        return true;
    }

    public boolean SADP_ResetPasswd_V40(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_RESET_PARAM_V40 struResetParamV40, HCSadpSDKByJNA.SADP_RET_RESET_PARAM_V40 struRetRestParamV40) {
        if (null == sDevSerialNO || null == struResetParamV40 || null == struRetRestParamV40) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V40 Failed!");
            return false;
        }
        struResetParamV40.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_ResetPasswd_V40(sDevSerialNO, struResetParamV40.getPointer(), struRetRestParamV40.getPointer());
        struRetRestParamV40.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V40 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V40 Success!");
        return true;
    }

    public boolean SADP_ResetPasswd_V50(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_RESET_PARAM_V50 struResetParamV50, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struResetParamV50 || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V50 Failed!");
            return false;
        }
        struResetParamV50.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_ResetPasswd_V50(sDevSerialNO, struResetParamV50.getPointer(), struDevLockInfo.getPointer());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V50 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_ResetPasswd_V50 Success!");
        return true;
    }

    public boolean SADP_GetDeviceCode(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_SAFE_CODE struSafeCode) {
        if (null == sDevSerialNO || null == struSafeCode) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetDeviceCode Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 1, Pointer.NULL, 0, struSafeCode.getPointer(), struSafeCode.size());
        struSafeCode.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetDeviceCode Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetDeviceCode Success!");
        return true;
    }

    public boolean SADP_GetDeviceCode_V31(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_SAFE_CODE_V31 struSafeCode) {
        if (null == sDevSerialNO || null == struSafeCode) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetDeviceCode_V31 Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 30, Pointer.NULL, 0, struSafeCode.getPointer(), struSafeCode.size());
        struSafeCode.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetDeviceCode_V31 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetDeviceCode_V31 Success!");
        return true;
    }

    public boolean SADP_GetEncryptString(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_ENCRYPT_STRING struEncryptString) {
        if (null == sDevSerialNO || null == struEncryptString) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEncryptString Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 2, Pointer.NULL, 0, struEncryptString.getPointer(), struEncryptString.size());
        struEncryptString.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEncryptString Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetEncryptString Success!");
        return true;
    }

    public boolean SADP_GetEncryptString_V31(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_ENCRYPT_STRING_V31 struEncryptString) {
        if (null == sDevSerialNO || null == struEncryptString) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEncryptString_V31 Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 31, Pointer.NULL, 0, struEncryptString.getPointer(), struEncryptString.size());
        struEncryptString.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEncryptString_V31 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetEncryptString_V31 Success!");
        return true;
    }

    public boolean SADP_GetGUID(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_GUID_FILE_COND struGuidFileCond, HCSadpSDKByJNA.SADP_GUID_FILE struGuidFile) {
        if (null == sDevSerialNO || null == struGuidFileCond || null == struGuidFile) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetGUID Failed!");
            return false;
        }
        struGuidFileCond.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 5, struGuidFileCond.getPointer(), struGuidFileCond.size(), struGuidFile.getPointer(), struGuidFile.size());
        struGuidFile.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetGUID Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetGUID Success!");
        return true;
    }

    public boolean SADP_GetGUID_V31(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_GUID_FILE_COND struGuidFileCond, HCSadpSDKByJNA.SADP_GUID_FILE_V31 struGuidFile) {
        if (null == sDevSerialNO || null == struGuidFileCond || null == struGuidFile) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetGUID_V31 Failed!");
            return false;
        }
        struGuidFileCond.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 32, struGuidFileCond.getPointer(), struGuidFileCond.size(), struGuidFile.getPointer(), struGuidFile.size());
        struGuidFile.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetGUID_V31 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetGUID_V31 Success!");
        return true;
    }

    public boolean SADP_GetSecurityQuestion(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_SECURITY_QUESTION_CFG struSecurityQuestionCfg) {
        if (null == sDevSerialNO || null == struSecurityQuestionCfg) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetSecurityQuestion Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 6, Pointer.NULL, 0, struSecurityQuestionCfg.getPointer(), struSecurityQuestionCfg.size());
        struSecurityQuestionCfg.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetSecurityQuestion Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetSecurityQuestion Success!");
        return true;
    }

    public boolean SADP_SetSecurityQuestion(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_SECURITY_QUESTION_CFG struSecurityQuestionCfg, HCSadpSDKByJNA.SADP_SECURITY_QUESTION struSecurityQuestion) {
        if (null == sDevSerialNO || null == struSecurityQuestionCfg || null == struSecurityQuestion) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetSecurityQuestion Failed!");
            return false;
        }
        struSecurityQuestionCfg.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 7, struSecurityQuestionCfg.getPointer(), struSecurityQuestionCfg.size(), struSecurityQuestion.getPointer(), struSecurityQuestion.size());
        struSecurityQuestion.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetSecurityQuestion Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetSecurityQuestion Success!");
        return true;
    }

    public boolean SADP_SetUserMailBox(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_USER_MAILBOX struUserMailBox, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struUserMailBox || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetUserMailBox Failed!");
            return false;
        }
        struUserMailBox.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 20, struUserMailBox.getPointer(), struUserMailBox.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetUserMailBox Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetUserMailBox Success!");
        return true;
    }

    public boolean SADP_GetQrCodes(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_QR_CODES struQrCodes) {
        if (null == sDevSerialNO || null == struQrCodes) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetQrCodes Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 21, Pointer.NULL, 0, struQrCodes.getPointer(), struQrCodes.size());
        struQrCodes.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetQrCodes Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetQrCodes Success!");
        return true;
    }

    public boolean SADP_GetQrCodes_V31(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_QR_CODES_V31 struQrCodes) {
        if (null == sDevSerialNO || null == struQrCodes) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetQrCodes_V31 Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 33, Pointer.NULL, 0, struQrCodes.getPointer(), struQrCodes.size());
        struQrCodes.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetQrCodes_V31 Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetQrCodes_V31 Success!");
        return true;
    }

    public boolean SADP_SetHCPlatFormStatus(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_HCPLATFORM_STATUS_INFO struHCPlatFormStatus, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struHCPlatFormStatus || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetHCPlatFormStatus Failed!");
            return false;
        }
        struHCPlatFormStatus.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 8, struHCPlatFormStatus.getPointer(), struHCPlatFormStatus.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetHCPlatFormStatus Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetHCPlatFormStatus Success!");
        return true;
    }

    public boolean SADP_SetVerificationCode(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_VERIFICATION_CODE_INFO struVerificationCode, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struVerificationCode || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetVerificationCode Failed!");
            return false;
        }
        struVerificationCode.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 9, struVerificationCode.getPointer(), struVerificationCode.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetVerificationCode Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetVerificationCode Success!");
        return true;
    }

    public boolean SADP_SetEzvizUserToken(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_EZVIZ_USER_TOKEN_PARAM struEzvizUserToken, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struEzvizUserToken || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetEzvizUserToken Failed!");
            return false;
        }
        struEzvizUserToken.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 28, struEzvizUserToken.getPointer(), struEzvizUserToken.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetEzvizUserToken Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetEzvizUserToken Success!");
        return true;
    }

    public boolean SADP_GetEzvizUnbindStatus(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_EZVIZ_UNBIND_STATUS struEzvizUnbindStatus) {
        if (null == sDevSerialNO || null == struEzvizUnbindStatus) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEzvizUnbindStatus Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 24, null, 0, struEzvizUnbindStatus.getPointer(), struEzvizUnbindStatus.size());
        struEzvizUnbindStatus.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetEzvizUnbindStatus Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetEzvizUnbindStatus Success!");
        return true;
    }

    public boolean SADP_SetEzvizUnbind(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_EZVIZ_UNBIND_PARAM struEzvizUnbindParam, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struEzvizUnbindParam || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetEzvizUnbind Failed!");
            return false;
        }
        struEzvizUnbindParam.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 25, struEzvizUnbindParam.getPointer(), struEzvizUnbindParam.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetEzvizUnbind Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetEzvizUnbind Success!");
        return true;
    }

    public boolean SADP_RestoreInactive(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_INACTIVE_INFO struInactiveInfo, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struInactiveInfo || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_RestoreInactive Failed!");
            return false;
        }
        struInactiveInfo.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 14, struInactiveInfo.getPointer(), struInactiveInfo.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_RestoreInactive Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_RestoreInactive Success!");
        return true;
    }

    public boolean SADP_SetWifiRegion(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_WIFI_REGION_INFO struWifiRegionInfo, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struWifiRegionInfo || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetWifiRegion Failed!");
            return false;
        }
        struWifiRegionInfo.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 15, struWifiRegionInfo.getPointer(), struWifiRegionInfo.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetWifiRegion Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetWifiRegion Success!");
        return true;
    }

    public boolean SADP_SetChannelDefaultPassword(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_CHANNEL_DEFAULT_PASSWORD struChannelDefPWD, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struChannelDefPWD || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetChannelDefaultPassword Failed!");
            return false;
        }
        struChannelDefPWD.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 16, struChannelDefPWD.getPointer(), struChannelDefPWD.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetChannelDefaultPassword Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetChannelDefaultPassword Success!");
        return true;
    }

    public boolean SADP_EhomeEnable(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_EHOME_ENABLE_PARAM struEhomeEnableParam, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struDevLockInfo) {
        if (null == sDevSerialNO || null == struEhomeEnableParam || null == struDevLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_EhomeEnable Failed!");
            return false;
        }
        struEhomeEnableParam.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 19, struEhomeEnableParam.getPointer(), struEhomeEnableParam.size(), struDevLockInfo.getPointer(), struDevLockInfo.size());
        struDevLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_EhomeEnable Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_EhomeEnable Success!");
        return true;
    }

    public boolean SADP_GetPhoneQRCodes(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_PHONE_QR_CODES struQrCodes) {
        if (null == sDevSerialNO || null == struQrCodes) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetPhoneQRCodes Failed!");
            return false;
        }
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 29, null, 0, struQrCodes.getPointer(), struQrCodes.size());
        struQrCodes.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetPhoneQRCodes Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetPhoneQRCodes Success!");
        return true;
    }

    public boolean SADP_GetManagerPhoneNumber(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_PHONE_NUMBER_PARAM struPhoneNumberIn, HCSadpSDKByJNA.SADP_PHONE_NUMBER_PARAM struPhoneNumberOut) {
        if (null == sDevSerialNO || null == struPhoneNumberIn || null == struPhoneNumberOut) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetManagerPhoneNumber Failed!");
            return false;
        }
        struPhoneNumberIn.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_GetDeviceConfig(sDevSerialNO, 34, struPhoneNumberIn.getPointer(), struPhoneNumberIn.size(), struPhoneNumberOut.getPointer(), struPhoneNumberOut.size());
        struPhoneNumberOut.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_GetManagerPhoneNumber Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_GetManagerPhoneNumber Success!");
        return true;
    }

    public boolean SADP_SetManagerPhoneNumber(Pointer sDevSerialNO, HCSadpSDKByJNA.SADP_PHONE_NUMBER_PARAM struPhoneNumber, HCSadpSDKByJNA.SADP_DEV_LOCK_INFO struLockInfo) {
        if (null == sDevSerialNO || null == struPhoneNumber || null == struLockInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetManagerPhoneNumber Failed!");
            return false;
        }
        struPhoneNumber.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_SetDeviceConfig(sDevSerialNO, 35, struPhoneNumber.getPointer(), struPhoneNumber.size(), struLockInfo.getPointer(), struLockInfo.size());
        struLockInfo.read();
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetManagerPhoneNumber Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetManagerPhoneNumber Success!");
        return true;
    }

    public int SADP_GetSadpVersion() {
        return HCSadpSDKJNAInstance.getInstance().SADP_GetSadpVersion();
    }

    public boolean SADP_SetLogToFile(int nLogLevel, HCSadpSDKByJNA.BYTE_ARRAY sPath, int bAutoDel) {
        if (HCSadpSDKJNAInstance.getInstance().SADP_SetLogToFile(nLogLevel, sPath.getPointer(), bAutoDel) == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_SetLogToFile Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_SetLogToFile Success!");
        return true;
    }

    public int SADP_GetLastError() {
        return HCSadpSDKJNAInstance.getInstance().SADP_GetLastError();
    }

    public boolean SADP_Set_AdapterInfo(HCSadpSDKByJNA.ADAPTER_INFO_LIST struAdapterInfo) {
        if (null == struAdapterInfo) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Set_AdapterInfo Failed!");
            return false;
        }
        struAdapterInfo.write();
        int iRet = HCSadpSDKJNAInstance.getInstance().SADP_Set_AdapterInfo(struAdapterInfo.getPointer());
        if (iRet == 0) {
            Log.e((String)"[JavaInterface]", (String)"SADP_Set_AdapterInfo Failed!");
            return false;
        }
        Log.i((String)"[JavaInterface]", (String)"SADP_Set_AdapterInfo Success!");
        return true;
    }
}

