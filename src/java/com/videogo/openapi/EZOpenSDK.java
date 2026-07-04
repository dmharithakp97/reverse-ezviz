/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.text.TextUtils
 *  com.ez.stream.EZStreamClientManager
 *  com.ezviz.sdk.configwifi.Config
 *  com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback
 *  com.ezviz.sdk.configwifi.touchAp.GetTokenCallback
 *  com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback
 *  com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback
 *  com.hikvision.wifi.configuration.DeviceDiscoveryListener
 */
package com.videogo.openapi;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import com.ez.stream.EZStreamClientManager;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.videogo.auth.EZAuthApi;
import com.videogo.bandwidthcheck.EZBWCheckManager;
import com.videogo.constant.Config;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.errorlayer.ErrorOpenSDKProxy;
import com.videogo.exception.BaseException;
import com.videogo.main.AppManager;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.EZPlatformType;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.openapi.bean.EZAlarmInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.openapi.bean.EZDeviceRecordInfo;
import com.videogo.openapi.bean.EZDeviceUpgradeStatus;
import com.videogo.openapi.bean.EZDeviceVersion;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.openapi.bean.EZStorageStatus;
import com.videogo.openapi.bean.EZUserInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.Utils;
import com.videogo.wificonfig.APWifiConfig;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EZOpenSDK {
    private static final String TAG = "EZOpenSDK";
    public static String API_URL = "https://open.ys7.com";
    public static String WEB_URL = "https://openauth.ys7.com";
    protected static EZOpenSDK mEZOpenSDK = null;
    protected Application mApplication = null;
    protected EzvizAPI mEzvizAPI = null;
    private EZOpenSDKListener.LogCallback mLogCallback;
    private static boolean urlStub = false;
    private static String _mAppKey;
    private static String _mAPIURL;
    private static String _mWEBURL;

    private EZOpenSDK() {
    }

    protected EZOpenSDK(Application application, String appKey) {
        LogUtil.i(TAG, "construct EZOpenSDK, version:" + EZOpenSDK.getVersion());
        this.mEzvizAPI = EzvizAPI.getInstance();
        AppManager.getInstance().getEZSDKConfigurationSyn();
        this.mApplication = application;
    }

    public static EZOpenSDK getInstance() {
        return mEZOpenSDK;
    }

    @Deprecated
    public static boolean initLib(Application application, String appKey, String loadLibraryAbsPath) {
        return EZOpenSDK.initLib(application, appKey);
    }

    public static boolean initLib(Application application, String appKey) {
        LogUtil.i(TAG, "Enter initLib");
        if (TextUtils.isEmpty((CharSequence)appKey)) {
            LogUtil.e(TAG, "initLib EZOpenSDK, appKey is null");
            return false;
        }
        ErrorLayer.setErrorProxy(ErrorOpenSDKProxy.getInstance());
        LocalInfo.init(application, appKey);
        EzvizAPI.init(application, appKey, false);
        EzvizAPI.getInstance().setServerUrl(API_URL, WEB_URL);
        EzvizAPI.getInstance().preSetToken();
        EZAuthApi.platformType = EZPlatformType.EZPlatformTypeOPENSDK;
        mEZOpenSDK = new EZOpenSDK(application, appKey);
        EZOpenSDK.showSDKLog(Config.LOGGING);
        return true;
    }

    public void setServerUrl(String apiUrl, String webUrl) {
        this.mEzvizAPI.setServerUrl(apiUrl, webUrl);
    }

    public static void finiLib() {
        LogUtil.i(TAG, "FiniLib EZOpenSDK, version:" + EZOpenSDK.getVersion());
        AppManager.getInstance().finiLibs();
        EzvizAPI.getInstance().releaseSDK();
    }

    public static void showSDKLog(boolean showLog) {
        Config.LOGGING = showLog;
        com.ezviz.sdk.configwifi.Config.LOGGING = showLog;
        if (mEZOpenSDK != null && EZOpenSDK.mEZOpenSDK.mApplication != null) {
            EZStreamClientManager streamClientManager = EZStreamClientManager.create((Context)EZOpenSDK.mEZOpenSDK.mApplication.getApplicationContext());
            streamClientManager.setLogPrintEnable(showLog, false, Config.playCtrlLogLevel);
        }
    }

    public static void setDebugStreamEnable(boolean enable) {
        Config.STREAMDEBUGGING = enable;
    }

    public void setLogCallback(EZOpenSDKListener.LogCallback callback) {
        this.mLogCallback = callback;
        if (this.mLogCallback != null) {
            LogUtil.setLogCallback(new LogUtil.LogCallback(){

                @Override
                public void onLog(String tag, String msg) {
                    if (EZOpenSDK.this.mLogCallback != null) {
                        EZOpenSDK.this.mLogCallback.onLog(tag, msg);
                    }
                }
            });
        } else {
            LogUtil.setLogCallback(null);
        }
    }

    public void logout() {
        this.mEzvizAPI.logout();
    }

    public static String getVersion() {
        return "v5.27.3.20260319";
    }

    public boolean isLogin() {
        return EZAuthApi.isLogin();
    }

    public void setAccessToken(String accessToken) {
        this.mEzvizAPI.setAccessToken(accessToken);
    }

    public EZAccessToken getEZAccessToken() {
        return this.mEzvizAPI.getEZAccessToken();
    }

    public void openLoginPage() {
        this.mEzvizAPI.gotoLoginPage(false, -1, -1);
    }

    public void openLoginPage(int flag) {
        this.mEzvizAPI.gotoLoginPage(false, -1, flag);
    }

    public void setVparamForLoginPage(String vParam) {
        this.mEzvizAPI.setVparamForLoginPage(vParam);
    }

    @Deprecated
    public void openCloudPage(String deviceSerial) throws BaseException {
        this.mEzvizAPI.openCloudPage(deviceSerial, 1);
    }

    public void openCloudPage(String deviceSerial, int cameraNo) throws BaseException {
        this.mEzvizAPI.openCloudPage(deviceSerial, cameraNo);
    }

    public void openChangePasswordPage() {
        this.mEzvizAPI.openChangePasswordPage();
    }

    public static void enableSDKWithTKToken(boolean enable) {
        Config.ENABLE_SDK_TKTOKEN = enable;
    }

    public static boolean isEnableSDKWithTKToken() {
        return Config.ENABLE_SDK_TKTOKEN;
    }

    public void setHttpToken(String httpToken) {
        this.mEzvizAPI.setHttpToken(httpToken);
    }

    public void setDeviceToken(String deviceSerial, String deviceToken) {
        this.mEzvizAPI.setDeviceToken(deviceSerial, deviceToken);
    }

    public void setDeviceToken(String deviceSerial, int cameraNo, String deviceGlobalToken, String deviceVideoToken) {
        this.mEzvizAPI.setDeviceToken(deviceSerial, cameraNo, deviceGlobalToken, deviceVideoToken);
    }

    public EZPlayer createPlayer(String deviceSerial, int cameraNo) {
        return this.mEzvizAPI.createPlayer(deviceSerial, cameraNo);
    }

    public EZPlayer createPlayer(String deviceSerial, int cameraNo, boolean useSubStream) {
        return this.mEzvizAPI.createPlayerWithDeviceSerial(deviceSerial, cameraNo, useSubStream);
    }

    public EZPlayer createPlayerWithUrl(String url) {
        return this.mEzvizAPI.createPlayerWithUrl(url);
    }

    public EZPlayer createPlayerWithUserId(int userId, int cameraNo, int streamType) {
        return this.mEzvizAPI.createPlayerWithUserId(userId, cameraNo, streamType);
    }

    public void releasePlayer(EZPlayer player) {
        this.mEzvizAPI.releasePlayer(player);
    }

    public static void enableDirectInner(boolean bEnable) {
        Config.ENABLE_DirectInner = bEnable;
    }

    public static void enableP2P(boolean bEnable) {
        Config.ENABLE_P2P = bEnable;
    }

    public static void enableVTDU(boolean bEnable) {
        Config.ENABLE_VTDU = bEnable;
    }

    public void clearStreamInfoCache() {
        this.mEzvizAPI.clearStreamInfoCache();
    }

    public ArrayList<String> getAllProcessedPreconnectSerials() {
        return EZStreamClientManager.create((Context)EZOpenSDK.mEZOpenSDK.mApplication.getApplicationContext()).getAllProcessedPreconnectSerials();
    }

    public ArrayList<String> getAllToDoPreconnectSerials() {
        return EZStreamClientManager.create((Context)EZOpenSDK.mEZOpenSDK.mApplication.getApplicationContext()).getAllToDoPreconnectSerials();
    }

    public boolean isPreConnectionSucceed(String deviceSerial) {
        return EZStreamClientManager.create((Context)EZOpenSDK.mEZOpenSDK.mApplication.getApplicationContext()).isPreConnectionSucceed(deviceSerial);
    }

    public void startP2PPreconnect(String deviceSerial) {
        if (deviceSerial == null || deviceSerial.length() == 0) {
            LogUtil.e(TAG, "startP2PPreconnect error: deviceSerial is null.");
            return;
        }
        DeviceManager.getInstance().startPreConnect(deviceSerial);
    }

    public void clearP2PPreconnect(String deviceSerial) {
        if (deviceSerial == null || deviceSerial.length() == 0) {
            LogUtil.e(TAG, "clearP2PPreconnect error: deviceSerial is null.");
            return;
        }
        EZStreamClientManager.create((Context)EZOpenSDK.mEZOpenSDK.mApplication.getApplicationContext()).clearPreconnectInfo(deviceSerial);
    }

    public void setExtendStreamParam(Map<String, String> map) {
        this.mEzvizAPI.setExtendStreamParam(map);
    }

    public List<EZCloudRecordFile> searchRecordFileFromCloud(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromCloudEx(deviceSerial, cameraNo, startTime, endTime);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDevice(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDevice(deviceSerial, cameraNo, startTime, endTime);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDevice(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, EZConstants.EZVideoRecordType videoRecordType) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDevice(deviceSerial, cameraNo, startTime, endTime, videoRecordType.recordType);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDeviceEx(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, EZConstants.EZVideoRecordTypeEx videoRecordTypeEx) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDeviceEx(deviceSerial, cameraNo, startTime, endTime, String.valueOf(videoRecordTypeEx.recordType));
    }

    public EZDeviceRecordInfo searchRecordFileFromDeviceEx(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, EZConstants.EZVideoRecordTypeEx videoRecordTypeEx, boolean isQueryFromNvr, EZConstants.EZVideoRecordLocation videoRecordLocation, int pageSize) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDeviceEx(deviceSerial, cameraNo, startTime, endTime, videoRecordTypeEx.recordType, isQueryFromNvr, videoRecordLocation.location, pageSize);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromCVR(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromCVR(deviceSerial, cameraNo, startTime, endTime);
    }

    public List<EZCloudRecordFile> searchRecordFileFromSDKCloud(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, String spaceId) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromSDKCloud(deviceSerial, cameraNo, startTime, endTime, spaceId);
    }

    public boolean addDevice(String deviceSerial, String verifyCode) throws BaseException {
        return this.mEzvizAPI.addDeviceBySerialNonTransfer(deviceSerial, verifyCode);
    }

    public List<EZDeviceInfo> getDeviceList(int pageIndex, int pageSize) throws BaseException {
        return this.mEzvizAPI.getDeviceList(pageIndex, pageSize, false);
    }

    public List<EZDeviceInfo> getDeviceListEx(int pageIndex, int pageSize) throws BaseException {
        return this.mEzvizAPI.getDeviceList(pageIndex, pageSize, true);
    }

    public List<EZDeviceInfo> getSharedDeviceList(int pageIndex, int pageSize) throws BaseException {
        return this.mEzvizAPI.getSharedDeviceList(pageIndex, pageSize);
    }

    public List<EZDeviceInfo> getTrustDeviceList(int pageIndex, int ezpageSize) throws BaseException {
        return this.mEzvizAPI.getTrustDeviceList(pageIndex, ezpageSize);
    }

    public EZDeviceInfo getDeviceInfo(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceInfo(deviceSerial, false);
    }

    public EZDeviceInfo getDeviceInfoEx(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceInfo(deviceSerial, true);
    }

    public EZDeviceVersion getDeviceVersion(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceVersion(deviceSerial);
    }

    public boolean setDeviceEncryptStatus(String deviceSerial, String validateCode, boolean encrypt) throws BaseException {
        return this.mEzvizAPI.setDeviceEncryptStatus(deviceSerial, validateCode, encrypt);
    }

    public boolean setDeviceName(String deviceSerial, String deviceName) throws BaseException {
        return this.mEzvizAPI.updateDeviceName(deviceSerial, deviceName);
    }

    public boolean deleteDevice(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.deleteDeviceNonTransfer(deviceSerial);
    }

    public List<EZStorageStatus> getStorageStatus(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getStorageStatus(deviceSerial);
    }

    public boolean formatStorage(String deviceSerial, int partitionIndex) throws BaseException {
        return this.mEzvizAPI.formatStorage(deviceSerial, partitionIndex);
    }

    public EZDeviceUpgradeStatus getDeviceUpgradeStatus(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceUpgradeStatus(deviceSerial);
    }

    public void upgradeDevice(String deviceSerial) throws BaseException {
        this.mEzvizAPI.upgradeDevice(deviceSerial);
    }

    public String captureCamera(String deviceSerial, int cameraNo) throws BaseException {
        return this.mEzvizAPI.capturePicture(deviceSerial, cameraNo);
    }

    public boolean setVideoLevel(String deviceSerial, int cameraNo, int videoLevel) throws BaseException {
        return this.mEzvizAPI.setDeviceVideoLevel(deviceSerial, cameraNo, videoLevel);
    }

    public boolean setVideoLevelAuto(String deviceSerial, int cameraNo, int videoLevel) throws BaseException {
        return this.mEzvizAPI.setDeviceVideoLevelAuto(deviceSerial, cameraNo, videoLevel);
    }

    public boolean setDefence(String deviceSerial, EZConstants.EZDefenceStatus defence) throws BaseException {
        return this.mEzvizAPI.setDefence(deviceSerial, defence);
    }

    public void refreshDeviceDetailInfo(String deviceSerial, int cameraNo) throws BaseException {
        this.mEzvizAPI.refreshDeviceDetailInfoEx(deviceSerial, cameraNo);
    }

    public boolean controlPTZ(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int speed) throws BaseException {
        return this.mEzvizAPI.controlPTZ(deviceSerial, cameraNo, command, action, speed);
    }

    public boolean controlPTZEx(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int newSpeed) throws BaseException {
        return this.mEzvizAPI.controlPTZEx(deviceSerial, cameraNo, command, action, newSpeed, "");
    }

    public boolean controlPTZMix(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int newSpeed) throws BaseException {
        return this.mEzvizAPI.controlPTZMix(deviceSerial, cameraNo, command, action, newSpeed);
    }

    public boolean controlPTZViaP2P(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int newSpeed) throws BaseException {
        return this.mEzvizAPI.controlPTZViaP2P(deviceSerial, cameraNo, command, action, newSpeed, "");
    }

    @Deprecated
    public void controlVideoFlip(String deviceSerial, int cameraNo, EZConstants.EZPTZDisplayCommand command) throws BaseException {
        this.mEzvizAPI.controlVideoFlip(deviceSerial, cameraNo, command);
    }

    public List<EZAlarmInfo> getAlarmList(String deviceSerial, int pageIndex, int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.getAlarmList(deviceSerial, pageIndex, pageSize, beginTime, endTime);
    }

    public byte[] decryptData(byte[] inputData, String verifyCode) {
        return this.mEzvizAPI.decryptData(inputData, verifyCode, 1);
    }

    public byte[] decryptData(byte[] inputData, String verifyCode, int cryptType) {
        return this.mEzvizAPI.decryptData(inputData, verifyCode, cryptType);
    }

    public boolean setAlarmStatus(List<String> alarmIdList, EZConstants.EZAlarmStatus alarmStatus) throws BaseException {
        return this.mEzvizAPI.setAlarmStatus(alarmIdList, alarmStatus);
    }

    public boolean deleteAlarm(List<String> alarmIdList) throws BaseException {
        return this.mEzvizAPI.deleteAlarm(alarmIdList);
    }

    public int getUnreadMessageCount(String deviceSerial, EZConstants.EZMessageType messageType) throws BaseException {
        return this.mEzvizAPI.getUnreadMessageCount(deviceSerial, messageType);
    }

    @Deprecated
    public List<EZLeaveMessage> getLeaveMessageList(String deviceSerial, int pageIndex, int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.getLeaveMessageList(deviceSerial, pageIndex, pageSize, beginTime, endTime);
    }

    @Deprecated
    public boolean setLeaveMessageStatus(List<String> msgIdList, EZConstants.EZMessageStatus messageStatus) throws BaseException {
        return this.mEzvizAPI.setLeaveMessageStatus(msgIdList, messageStatus);
    }

    @Deprecated
    public boolean deleteLeaveMessages(List<String> msgIdList) throws BaseException {
        return this.mEzvizAPI.deleteLeaveMessages(msgIdList);
    }

    @Deprecated
    public void getLeaveMessageData(Handler handler, EZLeaveMessage msg, EZOpenSDKListener.EZLeaveMessageFlowCallback callback) {
        this.mEzvizAPI.getLeaveMessageData(handler, msg, callback);
    }

    public boolean inviteDeviceEnterMeeting(String appId, String ertcToken, String roomId, String deviceSerial, int cameraNo, String account) throws BaseException {
        return this.mEzvizAPI.videoTalkAction("request", appId, ertcToken, roomId, deviceSerial, cameraNo, account);
    }

    public boolean cancelInviteDeviceEnterMeeting(String appId, String ertcToken, String roomId, String deviceSerial, int cameraNo, String account) throws BaseException {
        return this.mEzvizAPI.videoTalkAction("cancel", appId, ertcToken, roomId, deviceSerial, cameraNo, account);
    }

    public boolean rejectVideoCallReqFromDevice(String appId, String ertcToken, String roomId, String deviceSerial, int cameraNo, String account) throws BaseException {
        return this.mEzvizAPI.videoTalkAction("reject", appId, ertcToken, roomId, deviceSerial, cameraNo, account);
    }

    @Deprecated
    public EZProbeDeviceInfo probeDeviceInfo(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.probeDeviceInfo(deviceSerial);
    }

    public EZProbeDeviceInfoResult probeDeviceInfo(String deviceSerial, String deviceType) {
        return this.mEzvizAPI.probeDeviceInfo(deviceSerial, deviceType);
    }

    public void startConfigWifi(Context context, String deviceSerial, String ssid, String password, EZOpenSDKListener.EZStartConfigWifiCallback back) {
        this.startConfigWifi(context, deviceSerial, ssid, password, EZConstants.EZWiFiConfigMode.EZWiFiConfigSmart, back);
    }

    public void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, EZOpenSDKListener.EZStartConfigWifiCallback back) {
        this.mEzvizAPI.startConfigWifi(context, deviceSerial, ssid, password, mode, null, back);
    }

    public void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, String apiUrl, EZOpenSDKListener.EZStartConfigWifiCallback back) {
        this.mEzvizAPI.startConfigWifi(context, deviceSerial, ssid, password, mode, apiUrl, back);
    }

    @Deprecated
    public boolean startConfigWifi(Context context, String ssid, String password, DeviceDiscoveryListener l) {
        return this.mEzvizAPI.startConfigWifi(context, ssid, password, l);
    }

    public boolean stopConfigWiFi() {
        return this.mEzvizAPI.stopConfigWiFi();
    }

    public void startAPConfigWifiWithSsid(String wifiSsid, String wifiPwd, String deviceSerial, String deviceVerifyCode, APWifiConfig.APConfigCallback callback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(wifiSsid, wifiPwd, deviceSerial, deviceVerifyCode, callback);
    }

    public void startAPConfigWifiWithSsid(String wifiSsid, String wifiPwd, String deviceSerial, String deviceVerifyCode, String deviceHotspotName, String deviceHotspotPwd, boolean autoConnectToDeviceHotSpot, APWifiConfig.APConfigCallback callback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(wifiSsid, wifiPwd, deviceSerial, deviceVerifyCode, deviceHotspotName, deviceHotspotPwd, autoConnectToDeviceHotSpot, callback);
    }

    public void startAPConfigWifiWithSsid(String wifiSsid, String wifiPwd, String deviceSerial, String deviceVerifyCode, String deviceHotspotName, String deviceHotspotPwd, boolean autoConnectToDeviceHotSpot, String apiUrl, APWifiConfig.APConfigCallback callback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(wifiSsid, wifiPwd, deviceSerial, deviceVerifyCode, deviceHotspotName, deviceHotspotPwd, autoConnectToDeviceHotSpot, apiUrl, callback);
    }

    public void stopAPConfigWifiWithSsid() {
        this.mEzvizAPI.stopAPConfigWifiWithSsid();
    }

    public void getNewApConfigToken(GetTokenCallback callback) {
        this.mEzvizAPI.getNewApConfigToken(callback);
    }

    public void startNewApConfigWithToken(String token, String ssid, String password, String lbsDomain, StartNewApConfigCallback callback) {
        this.mEzvizAPI.startNewApConfigWithToken(token, ssid, password, lbsDomain, callback);
    }

    public void getAccessDeviceInfo(GetAccessDeviceInfoCallback callback) {
        this.mEzvizAPI.getAccessDeviceInfo(callback);
    }

    public void getAccessDeviceWifiList(GetDeviceWifiListCallback callback) {
        this.mEzvizAPI.getAccessDeviceWifiList(callback);
    }

    public void queryPlatformBindStatus(String deviceSerial, QueryPlatformBindStatusCallback callback) {
        this.mEzvizAPI.queryPlatformBindStatus(deviceSerial, callback);
    }

    public void setDevRouteDomain(String devRouteDomain) {
        this.mEzvizAPI.setDevRouteDomain(devRouteDomain);
    }

    public void startBWCheck(int bwCheckType, String deviceSerial, int cameraNo, int checkTime, String bwCheckToken, EZBWCheckManager.EZBWCheckResultCallback callback) throws BaseException {
        EZBWCheckManager.getInstance().startBWCheck(bwCheckType, deviceSerial, cameraNo, checkTime, bwCheckToken, callback);
    }

    public void stopBWCheck() {
        EZBWCheckManager.getInstance().stopBWCheck();
    }

    public EZUserInfo getUserInfo() throws BaseException {
        return this.mEzvizAPI.getEZUserInfo();
    }

    public Application getApplication() {
        return this.mApplication;
    }

    public String getTerminalId() {
        return this.mEzvizAPI.getTerminalId();
    }

    public String uploadSDKCloudRecordImage(Bitmap bitmap) throws BaseException {
        return this.mEzvizAPI.uploadSDKCloudRecordPicture(bitmap);
    }

    private Map<String, String> getHTTPPublicParam() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("clientType", String.valueOf(13));
        result.put("featureCode", LocalInfo.getInstance().getHardwareCode());
        result.put("osVersion", Build.VERSION.RELEASE);
        result.put("netType", EzvizAPI.getInstance().getNetType());
        result.put("sdkVersion", "v5.27.3.20260319");
        result.put("appKey", EzvizAPI.getInstance().getAppKey());
        result.put("appID", LocalInfo.getInstance().getPackageName());
        result.put("appName", LocalInfo.getInstance().getAppName());
        return result;
    }

    private String getHTTPPublicParam(String key) {
        if (TextUtils.equals((CharSequence)key, (CharSequence)"clientType")) {
            return String.valueOf(13);
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"featureCode")) {
            return LocalInfo.getInstance().getHardwareCode();
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"osVersion")) {
            return Build.VERSION.RELEASE;
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"netType")) {
            return EzvizAPI.getInstance().getNetType();
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"sdkVersion")) {
            return "v5.27.3.20260319";
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"appKey")) {
            return EzvizAPI.getInstance().getAppKey();
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"appID")) {
            return LocalInfo.getInstance().getPackageName();
        }
        if (TextUtils.equals((CharSequence)key, (CharSequence)"appName")) {
            return LocalInfo.getInstance().getAppName();
        }
        return null;
    }

    private static void getVariants() {
        if (urlStub && Config.ENABLE_STUB) {
            String filePath = "/sdcard/videogo_test_cfg";
            HashMap<String, String> mMap = new HashMap<String, String>();
            Utils.parseTestConfigFile("/sdcard/videogo_test_cfg", mMap);
            _mAppKey = (String)mMap.get("APP_KEY");
            _mAPIURL = (String)mMap.get("API_URL");
            _mWEBURL = (String)mMap.get("WEB_URL");
        }
    }

    public String getKey() {
        return _mAppKey;
    }

    public static void setPlayCtrlLogLevel(int logLevel) {
        Config.playCtrlLogLevel = logLevel;
    }
}

