/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.ez.stream.DownloadCloudParam
 *  com.ez.stream.InitParam
 */
package com.videogo.stream;

import android.text.TextUtils;
import com.ez.stream.DownloadCloudParam;
import com.ez.stream.InitParam;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.constant.Config;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.main.AppManager;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.CloudTicketInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.openapi.bean.EZPlayURLParams;
import com.videogo.openapi.bean.EZServerInfo;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.openapi.bean.P2pDeviceInfo;
import com.videogo.openapi.bean.resp.EZDevicePlayInfo;
import com.videogo.stream.DevProtoEnum;
import com.videogo.stream.EZP2PPermissionManager;
import com.videogo.util.Base64;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import java.util.Date;
import java.util.Map;

public class EZStreamParamHelp {
    private final String TAG = "EZStreamParamHelp";
    protected String deviceSerial = null;
    protected int cameraNo = 1;
    private String cameraNoStr = null;
    private String bizType = null;
    private String platformId = null;
    protected long requestCostTime = -1L;
    private boolean forceVtmStream = false;
    protected CameraInfoEx cameraInfo;
    protected DeviceInfoEx deviceInfo;
    private EZPlayURLParams mEZPlayURLParams;
    private int mStreamType = 0;
    public boolean isRealCameraNo;
    public int iQosTalkVersion = 0;
    public String szQosTaklIP = "";
    public int iQosTakPort = 0;
    private int iQosRspTimeout = 16;
    public boolean useSystemAEC = true;
    public String voiceTalkCallingID;
    private boolean isDeviceTalkBack = true;
    private boolean isUseSubStream;
    public boolean isPlayerDisableP2P;
    public boolean isAutoDeviceVideoLevel;
    public EZConstants.EZVideoQuality videoQuality = EZConstants.EZVideoQuality.VIDEO_PRIORITY_NONE;
    public String streamToken;
    private boolean isAIPlayback;
    private EZConstants.EZVideoRecordTypeEx videoRecordTypeEx;
    private int frameInterval;
    public long spaceId;
    public boolean isMultiChannelDevice;
    public boolean isIPCRecordDirectQuery;

    public void setIsDeviceTalkBack(boolean isDeviceTalkBack) {
        this.isDeviceTalkBack = isDeviceTalkBack;
    }

    public boolean isDeviceTalkBack() {
        return this.isDeviceTalkBack;
    }

    public boolean isUseSubStream() {
        return this.isUseSubStream;
    }

    public void setUseSubStream(boolean useSubStream) {
        this.isUseSubStream = useSubStream;
    }

    public void setAIPlayback(boolean isAIPlayback) {
        this.isAIPlayback = isAIPlayback;
    }

    public void setCompressVideoRecordParams(EZConstants.EZVideoRecordTypeEx videoRecordTypeEx, int frameInterval) {
        this.videoRecordTypeEx = videoRecordTypeEx;
        this.frameInterval = frameInterval;
    }

    public void setiQosRspTimeout(int iQosRspTimeout) {
        this.iQosRspTimeout = iQosRspTimeout < 10 ? 10 : (iQosRspTimeout > 65 ? 65 : iQosRspTimeout);
    }

    public int getiQosRspTimeout() {
        return this.iQosRspTimeout;
    }

    public EZPlayURLParams getEZPlayURLParams() {
        return this.mEZPlayURLParams;
    }

    public DeviceInfoEx getDeviceInfo() {
        return this.deviceInfo;
    }

    public EZStreamParamHelp(String deviceSerial, int cameraNo) {
        this.initEZStreamParamHelp(deviceSerial, cameraNo, true);
    }

    public EZStreamParamHelp(EZPlayURLParams playURLParams) {
        this.mEZPlayURLParams = playURLParams;
        if (playURLParams != null) {
            this.bizType = playURLParams.bizType;
            this.platformId = playURLParams.platformId;
            boolean changedToIntChannel = false;
            try {
                this.cameraNo = Integer.parseInt(playURLParams.cameraNo);
                changedToIntChannel = true;
            }
            catch (Exception e) {
                LogUtil.i("EZStreamParamHelp", "failed to trans to int channel, origin str channel is " + playURLParams.cameraNo);
                this.cameraNoStr = playURLParams.cameraNo;
            }
            if (changedToIntChannel) {
                this.initEZStreamParamHelp(this.mEZPlayURLParams.deviceSerial, this.cameraNo, true);
            } else {
                this.initEZStreamParamHelp(this.mEZPlayURLParams.deviceSerial, this.cameraNoStr, true);
            }
        }
    }

    private void initEZStreamParamHelp(String deviceSerial, String cameraNoStr, boolean isRealCameraNo) {
        this.deviceSerial = deviceSerial;
        this.cameraNoStr = cameraNoStr;
        this.isRealCameraNo = isRealCameraNo;
    }

    private void initEZStreamParamHelp(String deviceSerial, int cameraNo, boolean isRealCameraNo) {
        this.deviceSerial = deviceSerial;
        this.cameraNo = cameraNo;
        this.isRealCameraNo = isRealCameraNo;
    }

    public EZStreamParamHelp(String deviceSerial, int camerNo, boolean isRealCamerNo) {
        this.initEZStreamParamHelp(deviceSerial, camerNo, isRealCamerNo);
    }

    public void setForceVtmStream(boolean force) {
        this.forceVtmStream = force;
    }

    public void setStreamType(int mStreamType) {
        this.mStreamType = mStreamType;
    }

    public boolean isRtmpPlay() {
        if (this.cameraInfo != null && !TextUtils.isEmpty((CharSequence)this.cameraInfo.getRtmpUrl())) {
            long time = System.currentTimeMillis();
            Date currentData = new Date(time);
            String day = DateTimeUtil.formatTimeToString(time, "yyyy-MM-dd");
            String rtmpTimeSpace = this.cameraInfo.getRtmpTimespan();
            if (TextUtils.isEmpty((CharSequence)rtmpTimeSpace)) {
                return true;
            }
            String retTime = "";
            String[] strings = rtmpTimeSpace.split(",");
            for (int i = 0; i < strings.length; ++i) {
                if (TextUtils.isEmpty((CharSequence)strings[i]) || strings[i].split("~").length != 2) continue;
                String begin = strings[i].split("~")[0];
                String end = strings[i].split("~")[1];
                if (TextUtils.isEmpty((CharSequence)begin) || TextUtils.isEmpty((CharSequence)end)) continue;
                if (begin.split(" ").length == 1) {
                    begin = day + " " + begin;
                }
                if (end.split(" ").length == 1) {
                    end = day + " " + end;
                }
                if (TextUtils.isEmpty((CharSequence)begin) || TextUtils.isEmpty((CharSequence)end) || TextUtils.isEmpty((CharSequence)begin) || TextUtils.isEmpty((CharSequence)end)) continue;
                Date beginDate = DateTimeUtil.parseStringToDate(begin, "yyyy-MM-dd HH:mm:ss");
                Date endDate = DateTimeUtil.parseStringToDate(end, "yyyy-MM-dd HH:mm:ss");
                if (!currentData.after(beginDate) || !currentData.before(endDate)) continue;
                return true;
            }
        }
        return false;
    }

    public void readyParamInfo() throws BaseException {
        this.readyParamInfo(false);
    }

    public void readyParamInfo(boolean isRealPlay) throws BaseException {
        this.requestCostTime = -1L;
        if (this.cameraInfo == null && this.deviceInfo == null) {
            if (TextUtils.isEmpty((CharSequence)this.deviceSerial)) {
                ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
                throw new InnerException("device param is null", errorInfo);
            }
            this.cameraInfo = CameraManager.getInstance().getAddedCamera(this.deviceSerial, this.cameraNo, this.cameraNoStr);
            this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExById(this.deviceSerial);
            if (this.cameraInfo == null || this.deviceInfo == null) {
                this.updateDeviceAndCameraInfo(true);
                return;
            }
        }
        if (!isRealPlay || !this.needResetVideoLevel()) {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        EZStreamParamHelp.this.updateDeviceAndCameraInfo(false);
                    }
                    catch (BaseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void updateDeviceAndCameraInfo(boolean updateRequestTime) throws BaseException {
        long beginTime = System.currentTimeMillis();
        DeviceManager.getInstance().getDeviceInfoExFromOnlineToLocal(this.bizType, this.platformId, this.deviceSerial, this.cameraNo, this.cameraNoStr);
        if (updateRequestTime) {
            this.requestCostTime = System.currentTimeMillis() - beginTime;
        }
        this.cameraInfo = CameraManager.getInstance().getAddedCamera(this.deviceSerial, this.cameraNo, this.cameraNoStr);
        this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExById(this.deviceSerial);
    }

    protected void readyParamInfoByDeviceSerial() throws BaseException {
        if (this.deviceInfo == null) {
            this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExById(this.deviceSerial);
            if (this.deviceInfo == null) {
                long b = System.currentTimeMillis();
                EZDevicePlayInfo devicePlayInfo = PlayAPI.getInstance().getDevicePlayInfo(this.deviceSerial);
                this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExFromEZPlayInfo(devicePlayInfo);
                DeviceManager.getInstance().addDevice(this.deviceInfo, true);
                this.requestCostTime = System.currentTimeMillis() - b;
            }
        } else {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        EZDevicePlayInfo devicePlayInfo = PlayAPI.getInstance().getDevicePlayInfo(EZStreamParamHelp.this.deviceSerial);
                        EZStreamParamHelp.this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExFromEZPlayInfo(devicePlayInfo);
                        DeviceManager.getInstance().addDevice(EZStreamParamHelp.this.deviceInfo, true);
                    }
                    catch (BaseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void updateCameraInfoEx() {
        if (this.cameraInfo != null) {
            this.cameraInfo = CameraManager.getInstance().getAddedCamera(this.deviceSerial, this.cameraNo, this.cameraNoStr);
        }
    }

    public int getDClogVideoLevel() {
        if (this.cameraInfo != null) {
            if (this.mEZPlayURLParams != null) {
                return this.mEZPlayURLParams.videoLevel;
            }
            return this.cameraInfo.getVideoLevel();
        }
        return 1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized InitParam getInitParam(int streamSource) throws BaseException {
        if (this.deviceInfo == null || !this.checkCameraInfo()) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400032);
            throw new InnerException("can not get camera info.", errorInfo);
        }
        if (!(streamSource != 2 && streamSource != 8 && streamSource != 5 || this.deviceInfo.getBelongDevice() == null || this.deviceInfo.getBelongDevice().getDeviceStatus() != 1 || TextUtils.isEmpty((CharSequence)this.deviceInfo.getBelongSerial()) || this.isIPCRecordDirectQuery)) {
            this.deviceInfo = DeviceManager.getInstance().getDeviceInfoExBelongDevice(this.deviceInfo);
            if (this.cameraInfo != null) {
                this.cameraInfo.setChannelNo(this.deviceInfo.getBelongNo());
            } else {
                this.cameraNo = this.deviceInfo.getBelongNo();
            }
        }
        InitParam param = new InitParam();
        if (this.deviceInfo != null) {
            DeviceInfoEx deviceInfoEx = this.deviceInfo;
            synchronized (deviceInfoEx) {
                switch (streamSource) {
                    case 9: {
                        this.appendCloudStreamTicket(param);
                        break;
                    }
                }
                param.iStreamSource = streamSource;
                if (this.cameraInfo != null && this.cameraInfo.getForceStreamType() == 3) {
                    this.forceVtmStream = true;
                }
                LogUtil.d("EZStreamParamHelp", "init param: enablep2p? " + Config.ENABLE_P2P + " forceVtmStream? " + this.forceVtmStream);
                int streamType = 1;
                if (this.cameraInfo != null) {
                    streamType = this.mEZPlayURLParams != null ? (this.cameraInfo.getStreamType(this.mEZPlayURLParams.videoLevel) == 0 ? 1 : 2) : (this.isUseSubStream ? (this.cameraInfo.getMinimumStreamType() == 0 ? 1 : 2) : (this.cameraInfo.getStreamType() == 0 ? 1 : 2));
                }
                param.iStreamType = streamType;
                if (Config.ENABLE_SDK_TKTOKEN) {
                    param.szStreamToken = this.streamToken;
                    LogUtil.i("EZStreamParamHelp", String.format("set streamToken: %s", this.streamToken));
                }
                this.forbidSomeStreamWay(streamSource, param);
                this.initP2pParam(param);
                LogUtil.d("EZStreamParamHelp", "init param: iStreamInhibit1 " + param.iStreamInhibit);
                param.szDevIP = this.deviceInfo.getDeviceIP();
                param.szDevLocalIP = this.deviceInfo.getLocalDeviceIp();
                param.iDevCmdPort = this.deviceInfo.getCmdPort();
                param.iDevCmdLocalPort = this.deviceInfo.getLocalCmdPort();
                param.iDevStreamPort = this.deviceInfo.getStreamPort();
                param.iDevStreamLocalPort = this.deviceInfo.getLocalStreamPort();
                String string = param.szChnlIndex = this.cameraInfo != null ? this.cameraInfo.getSzChnlIndex() : this.cameraNoStr;
                if (TextUtils.isEmpty((CharSequence)param.szChnlIndex)) {
                    int n = param.iChannelNumber = this.cameraInfo != null ? this.cameraInfo.getChannelNo() : this.cameraNo;
                    if (this.isMultiChannelDevice && this.deviceInfo.isSupportMultiChannel()) {
                        param.iChannelNumber = 0;
                        param.iInterlaceFlag = 1;
                    }
                }
                param.szDevSerial = !TextUtils.isEmpty((CharSequence)this.deviceInfo.getDevlogicId()) ? this.deviceInfo.getDevlogicId() : (this.deviceInfo.getDeviceID() != null ? this.deviceInfo.getDeviceID() : this.deviceSerial);
                String string2 = param.szChnlSerial = this.cameraInfo != null ? this.cameraInfo.getDeviceID() : this.deviceSerial;
                param.iVoiceChannelNumber = this.isRealCameraNo ? (this.isDeviceTalkBack ? 0 : this.cameraNo) : this.cameraNo;
                param.szHardwareCode = LocalInfo.getInstance().getHardwareCode();
                param.szTtsIP = this.deviceInfo.getTtsIp();
                param.iTtsPort = this.deviceInfo.getTtsPort();
                param.szClientSession = LocalInfo.getInstance().getEZAccesstoken().getDeviceToken(this.deviceSerial);
                param.szPermanetkey = TextUtils.isEmpty((CharSequence)this.deviceInfo.getEncryptPwd()) ? "12345" : this.deviceInfo.getEncryptPwd();
                param.szCasServerIP = this.deviceInfo.getCasIp();
                param.iCasServerPort = this.deviceInfo.getCasPort();
                if (AppManager.getInstance().getServerInfo() != null) {
                    param.szStunIP = AppManager.getInstance().getServerInfo().getStun1Addr();
                    param.iStunPort = AppManager.getInstance().getServerInfo().getStun1Port();
                }
                param.iClnType = 13;
                param.iSupportNAT34 = this.deviceInfo.getSupportNatPass();
                param.support_new_talk = this.deviceInfo.getSupportNewTTS();
                if (this.iQosTakPort != 0) {
                    param.iQosTakPort = this.iQosTakPort;
                    param.iQosTalkVersion = this.iQosTalkVersion;
                    param.szQosTaklIP = this.szQosTaklIP;
                }
                String vtdu = null;
                String[] vtms = null;
                String vtmIP = "";
                int vtmport = -1;
                if (this.getEZPlayURLParams() != null && !TextUtils.isEmpty((CharSequence)(vtdu = this.getEZPlayURLParams().vtdu))) {
                    vtms = vtdu.split(":");
                    if (vtms != null && vtms.length == 2) {
                        vtmIP = vtms[0];
                        vtmport = Integer.valueOf(vtms[1]);
                    }
                    param.iVtmPort = vtmport;
                    param.szVtmIP = vtmIP;
                }
                if (TextUtils.isEmpty((CharSequence)vtmIP) || vtmport < 0) {
                    EZServerInfo serverInfo = AppManager.getInstance().getServerInfo();
                    if (serverInfo != null && serverInfo.isMicroCloudMode()) {
                        param.iVtmPort = AppManager.getInstance().getServerInfo().getVtmPort();
                        param.szVtmIP = AppManager.getInstance().getServerInfo().getVtmAddr();
                    } else {
                        param.iVtmPort = this.deviceInfo.getVtmPort();
                        param.szVtmIP = this.deviceInfo.getVtmIp();
                    }
                    if (serverInfo != null && this.isAIPlayback && (streamSource == 2 || streamSource == 8)) {
                        param.iVtmPort = AppManager.getInstance().getServerInfo().getAiMediaPort();
                        param.szVtmIP = AppManager.getInstance().getServerInfo().getAiMediaAddr();
                    }
                }
                param.iStreamTimeOut = 15000;
                StringBuffer buffer = new StringBuffer();
                if (this.cameraInfo != null) {
                    buffer.append("biz=").append(this.cameraInfo.getStreamBiz()).append("&");
                    if (this.cameraInfo.getStreamBizUrl() != null) {
                        buffer.append(this.cameraInfo.getStreamBizUrl()).append("&");
                    }
                } else {
                    buffer.append("biz=").append(5).append("&");
                }
                buffer.append("hardwarecode=").append(LocalInfo.getInstance().getHardwareCode());
                Map<String, String> map = PlayAPI.getInstance().getExtendStreamParam();
                if (map != null) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        buffer.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                    }
                }
                param.szExtensionParas = buffer.toString();
                param.szLid = LocalInfo.getInstance().getHardwareCode();
                param.iSupportPlayBackEndFlag = this.deviceInfo.getDirectPlayback_EndFlag();
                switch (streamSource) {
                    case 0: {
                        param.iLinkEncryptV2 = this.deviceInfo.getLinkEncryptByStreamSource("1");
                        break;
                    }
                    case 2: 
                    case 5: 
                    case 8: {
                        param.iLinkEncryptV2 = this.deviceInfo.getLinkEncryptByStreamSource("2");
                        break;
                    }
                    case 6: {
                        param.iLinkEncryptV2 = this.deviceInfo.getLinkEncryptByStreamSource("3");
                        if (this.voiceTalkCallingID == null || this.voiceTalkCallingID.length() <= 0) break;
                        param.iTalkType = 3;
                        param.szCallingId = this.voiceTalkCallingID;
                    }
                }
                if (param.iLinkEncryptV2 == 1 && !TextUtils.isEmpty((CharSequence)this.deviceInfo.getPublicKey()) && this.deviceInfo.getPublicKeyVersion() != 0) {
                    param.vtduServerKeyVersion = this.deviceInfo.getPublicKeyVersion();
                    param.vtduServerPublicKey = Base64.decode(this.deviceInfo.getPublicKey());
                } else {
                    param.iLinkEncryptV2 = 0;
                }
                if (streamSource == 2 || streamSource == 8) {
                    if (this.frameInterval > 0) {
                        param.iFrameInterval = this.frameInterval;
                    }
                    if (this.videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_AUTO || this.videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_CMR || this.videoRecordTypeEx == EZConstants.EZVideoRecordTypeEx.EZ_VIDEO_RECORD_TYPE_COMPRESS_MANUAL) {
                        param.iSDCardVideoType = this.videoRecordTypeEx.recordType;
                    }
                }
            }
        }
        return param;
    }

    private void appendCloudStreamTicket(InitParam param) {
        try {
            param.szTicketToken = this.spaceId > 0L ? PlayAPI.getInstance().getSDKCloudTicketInfo((String)this.deviceSerial, (int)this.cameraNo, (long)this.spaceId).ticket : PlayAPI.getInstance().getCloudTicketInfo((String)this.deviceSerial, (int)this.cameraNo).ticket;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forbidSomeStreamWay(int streamSource, InitParam param) {
        LogUtil.i("EZStreamParamHelp", "forbid reverse direct stream by default!!!");
        this.forbidMore(param, 8);
        if (this.forceVtmStream) {
            LogUtil.i("EZStreamParamHelp", "force vtm stream!!!");
            this.forbidMore(param, 15);
            if (this.mStreamType > 0) {
                param.iStreamType = this.mStreamType;
            }
        }
        if (!Config.ENABLE_DirectInner) {
            LogUtil.i("EZStreamParamHelp", "forbid directInner stream by user!!!");
            this.forbidMore(param, 1);
        }
        if (!Config.ENABLE_P2P) {
            LogUtil.i("EZStreamParamHelp", "forbid p2p stream by user!!!");
            this.forbidMore(param, 4);
        }
        if (!Config.ENABLE_VTDU) {
            LogUtil.i("EZStreamParamHelp", "forbid private stream by user!!!");
            this.forbidMore(param, 16);
        }
        if (this.isPlayerDisableP2P) {
            LogUtil.i("EZStreamParamHelp", "forbid player p2p stream by user!!!");
            this.forbidMore(param, 4);
        }
        if (this.getP2pVersion() == 0) {
            LogUtil.i("EZStreamParamHelp", "forbid p2p stream because device is not support!!!");
            this.forbidMore(param, 4);
        }
        if (this.getP2pVersion() >= 3) {
            boolean supportCurrentStreamSource = false;
            switch (streamSource) {
                case 0: {
                    if (this.deviceInfo.isSupportP2pV3()) {
                        if (this.deviceInfo.getP2pDeviceInfo() != null) {
                            supportCurrentStreamSource = true;
                            break;
                        }
                        LogUtil.i("EZStreamParamHelp", "support P2PV3 preview, without P2PDeviceInfo");
                        break;
                    }
                    LogUtil.i("EZStreamParamHelp", "not support P2PV3 preview");
                    break;
                }
                case 2: 
                case 8: {
                    if (this.deviceInfo.getV3Playback() == 1) {
                        supportCurrentStreamSource = true;
                        break;
                    }
                    LogUtil.i("EZStreamParamHelp", "not support V3Playback");
                    break;
                }
                case 6: {
                    if (this.deviceInfo.getV3Talk() == 1) {
                        supportCurrentStreamSource = true;
                        break;
                    }
                    LogUtil.i("EZStreamParamHelp", "not support V3Talk");
                    break;
                }
                case 5: {
                    if (this.deviceInfo.getV3Download() == 1) {
                        supportCurrentStreamSource = true;
                        break;
                    }
                    LogUtil.i("EZStreamParamHelp", "not support V3Download");
                    break;
                }
            }
            if (!supportCurrentStreamSource) {
                this.forbidMore(param, 4);
            }
        }
        LogUtil.i("EZStreamParamHelp", "forbidSomeStreamWay: " + param.iStreamInhibit);
    }

    private void forbidMore(InitParam param, int forbidWays) {
        param.iStreamInhibit |= forbidWays;
    }

    private synchronized void initP2pParam(InitParam param) {
        param.iP2PVersion = this.getP2pVersion();
        if (param.iP2PVersion >= 3 && (param.iStreamInhibit & 4) != 4) {
            LogUtil.i("EZStreamParamHelp", "start init p2p param, used for p2p v3 or above.");
            try {
                param.szUserID = LocalInfo.getInstance().getUserCode();
                P2pDeviceInfo p2pDeviceInfo = this.deviceInfo.getP2pDeviceInfo();
                if (p2pDeviceInfo != null) {
                    param.p2pServerList = p2pDeviceInfo.getTranslatedP2pServerInfoArray();
                    byte[] p2pLinkKeyBytes = p2pDeviceInfo.defaultKey.getBytes();
                    System.arraycopy(p2pLinkKeyBytes, 0, param.szP2PLinkKey, 0, 32);
                    param.usP2PKeyVer = p2pDeviceInfo.defaultKeyVer;
                    LogUtil.i("EZStreamParamHelp", "initP2pPram: finished");
                } else {
                    LogUtil.e("EZStreamParamHelp", "p2p pram is null, failed to get p2p pram, used for p2p v3 or above.");
                }
            }
            catch (Exception e) {
                LogUtil.e("EZStreamParamHelp", "failed to get p2p pram, used for p2p v3 or above.");
                e.printStackTrace();
            }
        }
    }

    private int getP2pVersion() {
        if (this.deviceInfo == null) {
            return 0;
        }
        int ver = 0;
        if (this.deviceInfo.isSupportP2pV3()) {
            ver = 3;
        } else if (this.deviceInfo.getSupporP2P()) {
            ver = 2;
        }
        return ver;
    }

    private boolean checkCameraInfo() {
        if (DevProtoEnum.GB28181.getProtoName().equals(this.bizType)) {
            return this.cameraInfo != null;
        }
        return true;
    }

    public InitParam paramAddCloudInfo(InitParam param, EZCloudRecordFile cloudRecordFile) {
        String[] subDownLoadPath;
        if (cloudRecordFile.getDownloadPath() != null && !cloudRecordFile.getDownloadPath().equals("") && (subDownLoadPath = cloudRecordFile.getDownloadPath().split(":")).length == 2) {
            param.szCloudServerIP = !Utils.isIp(subDownLoadPath[0]) ? AppManager.getInetAddress(subDownLoadPath[0]) : subDownLoadPath[0];
            if (Utils.isNumeric(subDownLoadPath[1])) {
                param.iCloudServerPort = Integer.parseInt(subDownLoadPath[1]);
            }
        }
        if (cloudRecordFile != null) {
            param.szPermanetkey = cloudRecordFile.getEncryption();
        }
        return param;
    }

    public boolean isCameraEncrypt() {
        if (this.deviceInfo == null) {
            return false;
        }
        return this.deviceInfo.getIsEncrypt() == 1;
    }

    public void setCycleVerifyPermissionCallback(int streamSource, EZP2PPermissionManager.PermissionVerifyCallBack callback) {
        if (this.deviceInfo == null) {
            return;
        }
        if (!this.deviceInfo.isSupportP2pV3() && this.deviceInfo.getIsOwner() == -1 && this.deviceInfo.needCycleVerifyPermission() == 1) {
            boolean isPlayback = streamSource == 2 || streamSource == 8;
            String playType = isPlayback ? "Replay" : "Real";
            EZP2PPermissionManager.getInstance().addVerification(this.deviceSerial, this.cameraNo, playType, callback);
        }
    }

    public void removeCycleVerifyPermissionCallback() {
        if (this.deviceInfo == null) {
            return;
        }
        if (!this.deviceInfo.isSupportP2pV3() && this.deviceInfo.getIsOwner() == -1 && this.deviceInfo.needCycleVerifyPermission() == 1) {
            EZP2PPermissionManager.getInstance().removeVerification(this.deviceSerial, this.cameraNo);
        }
    }

    public boolean verifyPermission(int streamSource) {
        if (this.deviceInfo == null) {
            return false;
        }
        boolean ret = true;
        if (!this.deviceInfo.isSupportP2pV3() && this.deviceInfo.getIsOwner() == -1) {
            try {
                boolean isPlayback = streamSource == 2 || streamSource == 8;
                String playType = isPlayback ? "Replay" : "Real";
                ret = PlayAPI.getInstance().getP2pPermission(this.deviceSerial, this.cameraNo, playType);
            }
            catch (BaseException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public boolean needResetVideoLevel() {
        if (this.cameraInfo == null) {
            return false;
        }
        if (this.videoQuality == EZConstants.EZVideoQuality.VIDEO_PRIORITY_NONE) {
            return false;
        }
        return !(this.videoQuality == EZConstants.EZVideoQuality.VIDEO_QUALITY_PRIORITY ? this.getHighestVideoLevel() == this.cameraInfo.getVideoLevel() : this.videoQuality == EZConstants.EZVideoQuality.VIDEO_PERFORMANCE_PRIORITY && this.getLowestVideoLevel() == this.cameraInfo.getVideoLevel());
    }

    public void setVideoQuality() {
        if (this.cameraInfo == null) {
            return;
        }
        int videoLevel = 0;
        if (this.videoQuality == EZConstants.EZVideoQuality.VIDEO_QUALITY_PRIORITY) {
            videoLevel = this.getHighestVideoLevel();
        } else if (this.videoQuality == EZConstants.EZVideoQuality.VIDEO_PERFORMANCE_PRIORITY) {
            videoLevel = this.getLowestVideoLevel();
        }
        boolean ret = true;
        if (videoLevel > 0) {
            try {
                ret = PlayAPI.getInstance().setDeviceVideoLevel(this.deviceSerial, this.cameraNo, videoLevel);
            }
            catch (BaseException e) {
                e.printStackTrace();
                LogUtil.e("EZStreamParamHelp", "failed to setVideoQuality");
            }
            if (ret) {
                this.cameraInfo.setVideoLevel(videoLevel);
                LogUtil.i("EZStreamParamHelp", "setVideoQuality success, videoLevel: " + videoLevel);
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            EZStreamParamHelp.this.updateDeviceAndCameraInfo(false);
                        }
                        catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        this.videoQuality = EZConstants.EZVideoQuality.VIDEO_PRIORITY_NONE;
    }

    public int getHighestVideoLevel() {
        int targetVideoLevel = this.cameraInfo.getVideoLevel();
        if (this.cameraInfo != null && this.cameraInfo.videoQualityInfos != null) {
            for (int i = 0; i < this.cameraInfo.videoQualityInfos.size(); ++i) {
                int videoLevel = ((EZVideoQualityInfo)this.cameraInfo.videoQualityInfos.get(i)).getVideoLevel();
                if (videoLevel <= targetVideoLevel) continue;
                targetVideoLevel = videoLevel;
            }
        }
        return targetVideoLevel;
    }

    public int getLowestVideoLevel() {
        int targetVideoLevel = this.cameraInfo.getVideoLevel();
        if (this.cameraInfo != null && this.cameraInfo.videoQualityInfos != null) {
            for (int i = 0; i < this.cameraInfo.videoQualityInfos.size(); ++i) {
                int videoLevel = ((EZVideoQualityInfo)this.cameraInfo.videoQualityInfos.get(i)).getVideoLevel();
                if (videoLevel >= targetVideoLevel) continue;
                targetVideoLevel = videoLevel;
            }
        }
        return targetVideoLevel;
    }

    public int supportTalkType() {
        if (this.deviceInfo == null) {
            return 0;
        }
        return this.deviceInfo.getSupportTalk();
    }

    public CameraInfoEx getCameraInfo() {
        return this.cameraInfo;
    }

    public static DownloadCloudParam getDownloadParam(EZLeaveMessage leaveMessage) {
        DownloadCloudParam downloadParam = new DownloadCloudParam();
        downloadParam.szAuthorization = null;
        downloadParam.szClientSession = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
        downloadParam.szTicketToken = null;
        downloadParam.iFrontType = 2;
        downloadParam.szFileID = leaveMessage.getMsgId();
        downloadParam.szCamera = leaveMessage.getDeviceSerial();
        downloadParam.szBeginTime = null;
        downloadParam.szEndTime = null;
        downloadParam.iFileType = leaveMessage.getContentType() == 1 ? 5 : 4;
        downloadParam.iStreamType = 1;
        downloadParam.iPlayType = 2;
        downloadParam.szServerIP = leaveMessage.getCloudServerUrl();
        String[] cloudServerPath = leaveMessage.getCloudServerUrl().split(":");
        if (cloudServerPath.length == 2) {
            downloadParam.szServerIP = !Utils.isIp(cloudServerPath[0]) ? AppManager.getInetAddress(cloudServerPath[0]) : cloudServerPath[0];
            if (Utils.isNumeric(cloudServerPath[1])) {
                downloadParam.iServerPort = Integer.parseInt(cloudServerPath[1]);
            }
        }
        downloadParam.iStreamTimeOut = 15;
        return downloadParam;
    }

    public static DownloadCloudParam getDownloadCloudRecordParam(EZCloudRecordFile recordFile, boolean isMultiChannelDevice) {
        DownloadCloudParam downloadParam = new DownloadCloudParam();
        downloadParam.szAuthorization = "";
        try {
            CloudTicketInfo ticketInfo = PlayAPI.getInstance().getCloudTicketInfo(recordFile.getDeviceSerial(), recordFile.getCameraNo());
            downloadParam.szTicketToken = ticketInfo.ticket;
        }
        catch (BaseException e) {
            throw new RuntimeException(e);
        }
        downloadParam.iFrontType = 2;
        downloadParam.szFileID = recordFile.getFileId();
        downloadParam.szCamera = recordFile.getDeviceSerial();
        downloadParam.iFileType = 1;
        downloadParam.iStreamType = 1;
        downloadParam.iPlayType = 2;
        downloadParam.szServerIP = recordFile.getDownloadPath();
        downloadParam.iChannelNumber = recordFile.getCameraNo();
        if (isMultiChannelDevice) {
            downloadParam.iChannelNumber = 0;
        }
        downloadParam.szBeginTime = DateTimeUtil.calendarToYMDTHMSZString(recordFile.getStartTime());
        downloadParam.szEndTime = DateTimeUtil.calendarToYMDTHMSZString(recordFile.getStopTime());
        String[] cloudServerPath = recordFile.getDownloadPath().split(":");
        if (cloudServerPath.length == 2) {
            downloadParam.szServerIP = !Utils.isIp(cloudServerPath[0]) ? AppManager.getInetAddress(cloudServerPath[0]) : cloudServerPath[0];
            if (Utils.isNumeric(cloudServerPath[1])) {
                downloadParam.iServerPort = Integer.parseInt(cloudServerPath[1]);
            }
        }
        downloadParam.iStreamTimeOut = 15;
        downloadParam.iStorageVersion = recordFile.getiStorageVersion();
        downloadParam.iVideoType = recordFile.getVideoType();
        return downloadParam;
    }
}

