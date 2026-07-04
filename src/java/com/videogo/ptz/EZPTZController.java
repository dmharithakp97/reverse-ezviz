/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Message
 *  android.text.TextUtils
 *  com.ez.jna.EZStreamSDKJNA
 *  com.ez.jna.EZStreamSDKJNA$EZ_P2PTRANSREQ_INFO$ByReference
 *  com.ez.jna.EZStreamSDKJNA$EZ_P2PTRANSRSP_INFO$ByReference
 *  com.ez.stream.JsonUtils
 *  com.hc.CASClient.CASClient
 *  com.hc.CASClient.ST_DEV_INFO
 *  com.hc.CASClient.ST_DISPLAY_INFO
 *  com.hc.CASClient.ST_PTZ_INFO
 *  com.hc.CASClient.ST_SERVER_INFO
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.ptz;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.stream.JsonUtils;
import com.hc.CASClient.CASClient;
import com.hc.CASClient.ST_DEV_INFO;
import com.hc.CASClient.ST_DISPLAY_INFO;
import com.hc.CASClient.ST_PTZ_INFO;
import com.hc.CASClient.ST_SERVER_INFO;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.main.AppManager;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.bean.P2pDeviceInfo;
import com.videogo.ptz.P2pPtzRequest;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class EZPTZController {
    private static final String TAG = "EZPTZController";
    private CASClient mCASClient = null;
    private EZStreamSDKJNA streamSdk;
    private long casClientId = -1L;
    private String mHardwareCode = null;
    private LocalInfo mLocalInfo = null;
    private DeviceInfoEx mDeviceInfoEx = null;
    private CameraInfoEx mCameraInfoEx = null;
    private String mDeviceSerial = null;
    private int mCameraNo = 1;

    public int getCameraNo() {
        return this.mCameraNo;
    }

    public void setCameraNo(int cameraNo) {
        this.mCameraNo = cameraNo;
    }

    public String getDeviceSerial() {
        return this.mDeviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.mDeviceSerial = deviceSerial;
    }

    private void initData() {
        if (TextUtils.isEmpty((CharSequence)this.mDeviceSerial)) {
            return;
        }
        try {
            this.mCameraInfoEx = CameraManager.getInstance().getAddedCamera(this.mDeviceSerial, this.mCameraNo);
            this.mDeviceInfoEx = DeviceManager.getInstance().getDeviceInfoExById(this.mDeviceSerial);
            if (this.mCameraInfoEx == null || this.mDeviceInfoEx == null) {
                DeviceManager.getInstance().getDeviceInfoExFromOnlineToLocal(this.mDeviceSerial, this.mCameraNo);
                this.mCameraInfoEx = CameraManager.getInstance().getAddedCamera(this.mDeviceSerial, this.mCameraNo);
                this.mDeviceInfoEx = DeviceManager.getInstance().getDeviceInfoExById(this.mDeviceSerial);
            }
            this.casClientId = this.streamSdk.ezstream_createEZCASClient(false);
        }
        catch (BaseException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public EZPTZController(String deviceSerial, int cameraNo) {
        this.mDeviceSerial = deviceSerial;
        this.mCameraNo = cameraNo;
        this.mLocalInfo = LocalInfo.getInstance();
        this.mCASClient = AppManager.getInstance().getCASClientSDKInstance();
        this.streamSdk = EZStreamSDKJNA.sEZStreamSDKJNA;
        this.mHardwareCode = this.mLocalInfo.getHardwareCode();
    }

    public int ptzControl(int command, EZConstants.EZPTZAction action, int newSpeed, String uuid) throws BaseException {
        LogUtil.d(TAG, "ptzControl:command=" + command + ", szAction=" + action.getAction() + ", speed=" + newSpeed);
        this.initData();
        if (this.mCameraInfoEx == null || this.mDeviceInfoEx == null || this.mCASClient == null) {
            LogUtil.e(TAG, "mCameraInfoEx or mDeviceInfoEx or mCASClient is null, cant do ptzControl");
            return -1;
        }
        if (!this.mDeviceInfoEx.getSupportPtzViaP2pv3()) {
            LogUtil.e(TAG, "device is not support ptzViap2pV3");
            return -1;
        }
        EZStreamSDKJNA.EZ_P2PTRANSREQ_INFO.ByReference in = new EZStreamSDKJNA.EZ_P2PTRANSREQ_INFO.ByReference();
        byte[] deviceSerial = this.mDeviceSerial.getBytes();
        System.arraycopy(deviceSerial, 0, in.szDevSerial, 0, deviceSerial.length);
        in.iDevChannel = this.mCameraNo;
        P2pDeviceInfo p2pDeviceInfo = DeviceManager.getInstance().getP2pDeviceInfo(this.mDeviceSerial);
        in.usP2PKeyVer = p2pDeviceInfo.defaultKeyVer;
        byte[] secretKey = p2pDeviceInfo.defaultKey.getBytes();
        if (secretKey.length > 32) {
            byte[] secretKeyTmp = new byte[32];
            System.arraycopy(secretKey, 0, secretKeyTmp, 0, 32);
            secretKey = secretKeyTmp;
        }
        System.arraycopy(secretKey, 0, in.szP2PLinkKey, 0, secretKey.length);
        String p2pServers = p2pDeviceInfo.getDeviceP2pServerString();
        if (p2pServers != null) {
            byte[] server = p2pServers.getBytes();
            System.arraycopy(server, 0, in.szServerGroup, 0, server.length);
        }
        String userCode = LocalInfo.getInstance().getUserCode();
        byte[] userId = userCode.getBytes();
        System.arraycopy(userId, 0, in.szUserId, 0, userId.length);
        String reqContent = JsonUtils.toJson((Object)new P2pPtzRequest(this.mCameraNo, command, action == EZConstants.EZPTZAction.EZPTZActionSTART ? 0 : 1, newSpeed, uuid));
        byte[] data = reqContent.getBytes();
        System.arraycopy(data, 0, in.szContent, 0, data.length);
        in.iContentLen = data.length;
        LogUtil.d(TAG, String.format("p2p ptzControl\nszDevSerial:%s \niDevChannel:%d \nusP2PKeyVer:%d \nszServerGroup:%s \nszP2PLinkKey:%s \nszUserId:%s \nszContent:%s \niContentLen:%d", this.mDeviceSerial, this.mCameraNo, in.usP2PKeyVer, p2pServers, p2pDeviceInfo.defaultKey, userCode, reqContent, in.iContentLen));
        in.write();
        EZStreamSDKJNA.EZ_P2PTRANSRSP_INFO.ByReference out = new EZStreamSDKJNA.EZ_P2PTRANSRSP_INFO.ByReference();
        int errorCode = this.streamSdk.ezstream_transferViaP2P(this.casClientId, in, out);
        LogUtil.d(TAG, "ptz p2p resultCode " + errorCode);
        out.read();
        String content = new String(out.szContent, 0, out.iContentLen);
        int result = 0;
        if (!TextUtils.isEmpty((CharSequence)content) && errorCode == 0) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(content);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject contentObject = jsonObject.optJSONObject("content");
            errorCode = contentObject != null ? contentObject.optInt("resultCode") : jsonObject.optInt("resultCode");
            if (errorCode != 0) {
                result = errorCode;
            }
        } else {
            result = errorCode;
        }
        if (result != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, result);
            throw new BaseException("", errorInfo);
        }
        return result;
    }

    public int ptzControl(int command, String szAction, int speed, int presetIndex) {
        LogUtil.d(TAG, "ptzControl:command=" + command + ", szAction=" + szAction + ", speed=" + speed + ", presetIndex=" + presetIndex);
        this.initData();
        if (this.mCameraInfoEx == null || this.mDeviceInfoEx == null || this.mCASClient == null) {
            LogUtil.d(TAG, "mCameraInfoEx or mDeviceInfoEx or mCASClient is null, cant do ptzControl");
            return 400001;
        }
        String mSessionID = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
        int retryCount = 0;
        while (retryCount <= 3) {
            ++retryCount;
            ArrayList devInfoList = new ArrayList();
            ST_SERVER_INFO mSerVerInfo = new ST_SERVER_INFO();
            mSerVerInfo.szServerIP = this.mDeviceInfoEx.getCasIp();
            mSerVerInfo.nServerPort = this.mDeviceInfoEx.getCasPort();
            if (this.mDeviceInfoEx.getOperationCode() == null || this.mDeviceInfoEx.getEncryptKey() == null) {
                LogUtil.d(TAG, "ptzControl:operatinCode or encryptKey is null, " + retryCount);
                int errorCode = 0;
                for (int i = 0; i < 3; ++i) {
                    boolean result = this.mCASClient.getDevOperationCodeEx(mSerVerInfo, mSessionID, this.mHardwareCode, new String[]{this.mDeviceInfoEx.getDeviceID()}, 1, devInfoList);
                    ErrorInfo errorInfo = ErrorLayer.getErrorLayer(33, this.mCASClient.getLastError());
                    errorCode = errorInfo.errorCode;
                    LogUtil.d(TAG, "ptzControl:getDevOperationCodeEx return:" + result + " errorCode:" + errorCode);
                    if (!result || devInfoList.size() <= 0) continue;
                    this.mDeviceInfoEx.setOperationCode(((ST_DEV_INFO)devInfoList.get((int)0)).szOperationCode);
                    this.mDeviceInfoEx.setEncryptKey(((ST_DEV_INFO)devInfoList.get((int)0)).szKey);
                    this.mDeviceInfoEx.setEncryptType(((ST_DEV_INFO)devInfoList.get((int)0)).enEncryptType);
                    break;
                }
                if (this.mDeviceInfoEx.getOperationCode() == null || this.mDeviceInfoEx.getEncryptKey() == null) {
                    LogUtil.d(TAG, "ptzControl:still cant getDevOperationCodeEx or getEncryptKeyafter 3 times, ptzControl fail, return " + errorCode);
                    return errorCode;
                }
            }
            ST_DEV_INFO mDevInfo = new ST_DEV_INFO();
            mDevInfo.szDevSerial = this.mDeviceInfoEx.getDeviceID();
            mDevInfo.szOperationCode = this.mDeviceInfoEx.getOperationCode();
            mDevInfo.szKey = this.mDeviceInfoEx.getEncryptKey();
            int mSupportPtzmodel = this.mDeviceInfoEx.getSupportPtzModel();
            boolean flag = true;
            LogUtil.d("PTZ control info", "ability " + mSupportPtzmodel + "  inLan  " + this.mDeviceInfoEx.getInLan());
            if (mSupportPtzmodel == 1) {
                flag = false;
                mSerVerInfo.szServerIP = this.mDeviceInfoEx.getLocalDeviceIp();
                mSerVerInfo.nServerPort = this.mDeviceInfoEx.getLocalCmdPort();
            } else if (mSupportPtzmodel == 0 && this.mDeviceInfoEx.getInLan() == 1) {
                flag = false;
                mSerVerInfo.szServerIP = this.mDeviceInfoEx.getLocalDeviceIp();
                mSerVerInfo.nServerPort = this.mDeviceInfoEx.getLocalCmdPort();
            }
            boolean ret = false;
            switch (command) {
                case 4: {
                    ST_DISPLAY_INFO mDisplayInfo = new ST_DISPLAY_INFO();
                    mDisplayInfo.szCommand = szAction;
                    mDisplayInfo.iChannel = this.mCameraInfoEx.getChannelNo();
                    mDisplayInfo.szRes = "";
                    ret = this.mCASClient.displayCtrl(mSessionID, mSerVerInfo, mDevInfo, mDisplayInfo, true);
                    break;
                }
                default: {
                    ST_PTZ_INFO mPtzInfo = new ST_PTZ_INFO();
                    mPtzInfo.szCommand = EZPTZController.getCasCommand(command);
                    mPtzInfo.iChannel = this.mCameraInfoEx.getChannelNo();
                    mPtzInfo.szAction = szAction;
                    mPtzInfo.iSpeed = speed;
                    mPtzInfo.iPresetIndex = presetIndex;
                    ret = this.mCASClient.ptzCtrl(mSessionID, mSerVerInfo, mDevInfo, mPtzInfo, true);
                    LogUtil.d(TAG, "ptzControl: call mCASClient.ptzCtrl result:" + ret + " mSessionId:" + mSessionID + " servInfo.IP:" + mSerVerInfo.szServerIP + " port:" + mSerVerInfo.nServerPort + " devInfo.serial:" + mDevInfo.szDevSerial + " devInfo.opCode:" + mDevInfo.szOperationCode + " devInfo.szKey:" + mDevInfo.szKey + " encryptType:" + mDevInfo.enEncryptType + " ptzInfo:szCommand:" + mPtzInfo.szCommand + " iChannel:" + mPtzInfo.iChannel + " szAction:" + mPtzInfo.szAction + " iSpeed:" + mPtzInfo.iSpeed + " presetIndex:" + mPtzInfo.iPresetIndex + " flag:" + flag);
                }
            }
            if (!ret) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(33, this.mCASClient.getLastError());
                int errorCode = errorInfo.errorCode;
                if (errorCode == 380042 || errorCode == 380003) {
                    this.mDeviceInfoEx.setOperationCode(null);
                    this.mDeviceInfoEx.setEncryptKey(null);
                    if (retryCount <= 3) continue;
                    LogUtil.d(TAG, "ptzControl: operation fail");
                    return errorCode;
                }
                if (retryCount <= 3) continue;
                return errorCode;
            }
            return 100;
        }
        return 400100;
    }

    public static String getCasCommand(int command) {
        switch (command) {
            case 0: {
                return "UP";
            }
            case 1: {
                return "DOWN";
            }
            case 2: {
                return "LEFT";
            }
            case 3: {
                return "RIGHT";
            }
            case 5: {
                return "ZOOMIN";
            }
            case 6: {
                return "ZOOMOUT";
            }
            case 7: {
                return "SET_PRESET";
            }
            case 8: {
                return "CLE_PRESET";
            }
            case 9: {
                return "GOTO_PRESET";
            }
        }
        return "";
    }

    public void sendMessage(Handler handler, int msg, int arg1, int arg2) {
        if (handler != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.arg1 = arg1;
            message.arg2 = arg2;
            handler.sendMessage(message);
        }
    }
}

