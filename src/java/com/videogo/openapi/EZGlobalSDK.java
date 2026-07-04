/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.os.Handler
 *  android.text.TextUtils
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
import android.os.Handler;
import android.text.TextUtils;
import com.ezviz.sdk.configwifi.touchAp.GetAccessDeviceInfoCallback;
import com.ezviz.sdk.configwifi.touchAp.GetDeviceWifiListCallback;
import com.ezviz.sdk.configwifi.touchAp.GetTokenCallback;
import com.ezviz.sdk.configwifi.touchAp.QueryPlatformBindStatusCallback;
import com.ezviz.sdk.configwifi.touchAp.StartNewApConfigCallback;
import com.hikvision.wifi.configuration.DeviceDiscoveryListener;
import com.videogo.auth.EZAuthApi;
import com.videogo.constant.Config;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.errorlayer.ErrorOpenSDKProxy;
import com.videogo.exception.BaseException;
import com.videogo.main.AppManager;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.EZPlatformType;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.EZAccessToken;
import com.videogo.openapi.bean.EZAlarmInfo;
import com.videogo.openapi.bean.EZAreaInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceCloudServiceInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.openapi.bean.EZDeviceUpgradeStatus;
import com.videogo.openapi.bean.EZDeviceVersion;
import com.videogo.openapi.bean.EZHiddnsDeviceInfo;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.openapi.bean.EZProbeDeviceInfo;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.videogo.openapi.bean.EZStorageStatus;
import com.videogo.openapi.bean.EZUserInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.wificonfig.APWifiConfig;
import java.util.Calendar;
import java.util.List;

public final class EZGlobalSDK
extends EZOpenSDK {
    private static final String TAG = "EZGlobalSDK";
    private static final String API_URL = "https://open.ezvizlife.com";
    private static final String WEB_URL = "https://openauth.ezvizlife.com";

    private EZGlobalSDK(Application application, String appKey) {
        super(application, appKey);
        LogUtil.i(TAG, "construct EZGlobalSDK, version: " + EZGlobalSDK.getVersion());
    }

    public static EZGlobalSDK getInstance() {
        return (EZGlobalSDK)mEZOpenSDK;
    }

    @Deprecated
    public static boolean initLib(Application application, String appKey, String loadLibraryAbsPath) {
        return EZGlobalSDK.initLib(application, appKey);
    }

    public static boolean initLib(Application application, String appKey) {
        if (TextUtils.isEmpty((CharSequence)appKey)) {
            LogUtil.e(TAG, "initLib EZOpenSDK, appKey is null");
            return false;
        }
        ErrorLayer.setErrorProxy(ErrorOpenSDKProxy.getInstance());
        LocalInfo.init(application, appKey);
        LocalInfo.getInstance().setServAddr(API_URL);
        LocalInfo.getInstance().setAuthServAddr(WEB_URL);
        EzvizAPI.init(application, appKey, true);
        EzvizAPI.getInstance().setServerUrl(API_URL, WEB_URL);
        EzvizAPI.getInstance().preSetToken();
        EZAuthApi.platformType = EZPlatformType.EZPlatformTypeGLOBALSDK;
        AppManager.getInstance().getEZSDKConfigurationSyn();
        mEZOpenSDK = new EZGlobalSDK(application, appKey);
        EZGlobalSDK.showSDKLog(Config.LOGGING);
        return true;
    }

    @Override
    @Deprecated
    public void openCloudPage(String deviceSerial) throws BaseException {
        this.mEzvizAPI.openCloudPage(deviceSerial, 1);
    }

    @Override
    public void openCloudPage(String deviceSerial, int cameraNo) throws BaseException {
        this.mEzvizAPI.openCloudPage(deviceSerial, cameraNo);
    }

    public static void finiLib() {
        LogUtil.i(TAG, "FiniLib EZGlobalSDK, version: " + EZGlobalSDK.getVersion());
    }

    public static String getVersion() {
        return "v5.27.3.20260319";
    }

    public static void enableP2P(boolean bEnable) {
        Config.ENABLE_P2P = bEnable;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.mEzvizAPI.setAccessToken(accessToken);
    }

    @Override
    public EZAccessToken getEZAccessToken() {
        return this.mEzvizAPI.getEZAccessToken();
    }

    public List<EZAreaInfo> getAreaList() throws BaseException {
        return this.mEzvizAPI.getAreaList();
    }

    @Override
    public void openLoginPage(int areaId) {
        this.mEzvizAPI.gotoLoginPage(false, areaId, -1);
    }

    public void openLoginPage(int areaId, int flag) {
        this.mEzvizAPI.gotoLoginPage(false, areaId, flag);
    }

    @Override
    public void logout() {
        this.mEzvizAPI.logout();
    }

    @Override
    public List<EZDeviceInfo> getDeviceList(int pageIndex, int pageSize) throws BaseException {
        return this.mEzvizAPI.getDeviceList(pageIndex, pageSize, false);
    }

    @Override
    @Deprecated
    public EZProbeDeviceInfo probeDeviceInfo(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.probeDeviceInfo(deviceSerial);
    }

    @Override
    public EZProbeDeviceInfoResult probeDeviceInfo(String deviceSerial, String deviceType) {
        return this.mEzvizAPI.probeDeviceInfo(deviceSerial, deviceType);
    }

    public EZProbeDeviceInfoResult probeDeviceInfo(String deviceSerial, String deviceType, String apiUrl) {
        return this.mEzvizAPI.probeDeviceInfo(deviceSerial, deviceType, apiUrl);
    }

    @Override
    public List<EZAlarmInfo> getAlarmList(String deviceSerial, int pageIndex, int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.getAlarmList(deviceSerial, pageIndex, pageSize, beginTime, endTime);
    }

    @Override
    public boolean setAlarmStatus(List<String> alarmIdList, EZConstants.EZAlarmStatus alarmStatus) throws BaseException {
        return this.mEzvizAPI.setAlarmStatus(alarmIdList, alarmStatus);
    }

    @Override
    public boolean deleteAlarm(List<String> alarmIdList) throws BaseException {
        return this.mEzvizAPI.deleteAlarm(alarmIdList);
    }

    @Override
    public List<EZDeviceRecordFile> searchRecordFileFromDevice(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDevice(deviceSerial, cameraNo, startTime, endTime);
    }

    @Override
    public List<EZDeviceRecordFile> searchRecordFileFromDeviceEx(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, EZConstants.EZVideoRecordTypeEx videoRecordTypeEx) throws BaseException {
        return this.mEzvizAPI.searchRecordFileFromDeviceEx(deviceSerial, cameraNo, startTime, endTime, String.valueOf(videoRecordTypeEx.recordType));
    }

    @Override
    public boolean addDevice(String deviceSerial, String verifyCode) throws BaseException {
        return this.mEzvizAPI.addDeviceBySerialNonTransfer(deviceSerial, verifyCode);
    }

    @Override
    public boolean deleteDevice(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.deleteDeviceNonTransfer(deviceSerial);
    }

    @Override
    public boolean controlPTZ(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int speed) throws BaseException {
        return this.mEzvizAPI.controlPTZ(deviceSerial, cameraNo, command, action, speed);
    }

    @Override
    @Deprecated
    public void controlVideoFlip(String deviceSerial, int cameraNo, EZConstants.EZPTZDisplayCommand command) throws BaseException {
        this.mEzvizAPI.controlVideoFlip(deviceSerial, cameraNo, command);
    }

    @Override
    public byte[] decryptData(byte[] inputData, String verifyCode) {
        return this.mEzvizAPI.decryptData(inputData, verifyCode, 1);
    }

    @Override
    public byte[] decryptData(byte[] inputData, String verifyCode, int cryptType) {
        return this.mEzvizAPI.decryptData(inputData, verifyCode, cryptType);
    }

    @Override
    public boolean setDefence(String deviceSerial, EZConstants.EZDefenceStatus defence) throws BaseException {
        return this.mEzvizAPI.setDefence(deviceSerial, defence);
    }

    @Override
    public void refreshDeviceDetailInfo(String deviceSerial, int cameraNo) throws BaseException {
        this.mEzvizAPI.refreshDeviceDetailInfoEx(deviceSerial, cameraNo);
    }

    @Override
    public String captureCamera(String deviceSerial, int channelNo) throws BaseException {
        return this.mEzvizAPI.capturePicture(deviceSerial, channelNo);
    }

    @Override
    public EZDeviceVersion getDeviceVersion(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceVersion(deviceSerial);
    }

    @Override
    public boolean setDeviceEncryptStatus(String deviceSerial, String validateCode, boolean encrypt) throws BaseException {
        return this.mEzvizAPI.setDeviceEncryptStatus(deviceSerial, validateCode, encrypt);
    }

    @Override
    public boolean setDeviceName(String deviceSerial, String deviceName) throws BaseException {
        return this.mEzvizAPI.updateDeviceName(deviceSerial, deviceName);
    }

    @Override
    public EZUserInfo getUserInfo() throws BaseException {
        return this.mEzvizAPI.getEZUserInfo();
    }

    @Override
    public int getUnreadMessageCount(String deviceSerial, EZConstants.EZMessageType messageType) throws BaseException {
        return this.mEzvizAPI.getUnreadMessageCount(deviceSerial, messageType);
    }

    @Override
    public List<EZLeaveMessage> getLeaveMessageList(String deviceSerial, int pageIndex, int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        return this.mEzvizAPI.getLeaveMessageList(deviceSerial, pageIndex, pageSize, beginTime, endTime);
    }

    @Override
    public boolean setLeaveMessageStatus(List<String> msgIdList, EZConstants.EZMessageStatus messageStatus) throws BaseException {
        return this.mEzvizAPI.setLeaveMessageStatus(msgIdList, messageStatus);
    }

    @Override
    public boolean deleteLeaveMessages(List<String> msgIdList) throws BaseException {
        return this.mEzvizAPI.deleteLeaveMessages(msgIdList);
    }

    @Override
    public void getLeaveMessageData(Handler handler, EZLeaveMessage msg, EZOpenSDKListener.EZLeaveMessageFlowCallback callback) {
        this.mEzvizAPI.getLeaveMessageData(handler, msg, callback);
    }

    @Override
    public List<EZStorageStatus> getStorageStatus(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getStorageStatus(deviceSerial);
    }

    @Override
    public boolean formatStorage(String deviceSerial, int partitionIndex) throws BaseException {
        return this.mEzvizAPI.formatStorage(deviceSerial, partitionIndex);
    }

    @Override
    public void upgradeDevice(String deviceSerial) throws BaseException {
        this.mEzvizAPI.upgradeDevice(deviceSerial);
    }

    @Override
    public EZDeviceUpgradeStatus getDeviceUpgradeStatus(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceUpgradeStatus(deviceSerial);
    }

    @Override
    public void openChangePasswordPage() {
        this.mEzvizAPI.openChangePasswordPage();
    }

    @Override
    public void startConfigWifi(Context context, String deviceSerial, String ssid, String password, int mode, EZOpenSDKListener.EZStartConfigWifiCallback back) {
        this.mEzvizAPI.startConfigWifi(context, deviceSerial, ssid, password, mode, null, back);
    }

    @Override
    @Deprecated
    public boolean startConfigWifi(Context context, String ssid, String password, DeviceDiscoveryListener l) {
        return this.mEzvizAPI.startConfigWifi(context, ssid, password, l);
    }

    @Override
    public boolean stopConfigWiFi() {
        return this.mEzvizAPI.stopConfigWiFi();
    }

    @Override
    public void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, APWifiConfig.APConfigCallback apConfigCallback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, apConfigCallback);
    }

    @Override
    public void startAPConfigWifiWithSsid(String ssid, String password, String deviceSerial, String verifyCode, String routerNamePre, String routerPasswordPre, boolean isAutoConnectDeviceHotSpot, APWifiConfig.APConfigCallback apConfigCallback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(ssid, password, deviceSerial, verifyCode, routerNamePre, routerPasswordPre, isAutoConnectDeviceHotSpot, apConfigCallback);
    }

    @Override
    public void startAPConfigWifiWithSsid(String wifiSsid, String wifiPwd, String deviceSerial, String deviceVerifyCode, String deviceHotspotName, String deviceHotspotPwd, boolean autoConnectToDeviceHotSpot, String apiUrl, APWifiConfig.APConfigCallback callback) {
        this.mEzvizAPI.startAPConfigWifiWithSsid(wifiSsid, wifiPwd, deviceSerial, deviceVerifyCode, deviceHotspotName, deviceHotspotPwd, autoConnectToDeviceHotSpot, apiUrl, callback);
    }

    @Override
    public void stopAPConfigWifiWithSsid() {
        this.mEzvizAPI.stopAPConfigWifiWithSsid();
    }

    @Override
    public void getNewApConfigToken(GetTokenCallback callback) {
        this.mEzvizAPI.getNewApConfigToken(callback);
    }

    @Override
    public void startNewApConfigWithToken(String token, String ssid, String password, String lbsDomain, StartNewApConfigCallback callback) {
        this.mEzvizAPI.startNewApConfigWithToken(token, ssid, password, lbsDomain, callback);
    }

    @Override
    public void getAccessDeviceInfo(GetAccessDeviceInfoCallback callback) {
        this.mEzvizAPI.getAccessDeviceInfo(callback);
    }

    @Override
    public void getAccessDeviceWifiList(GetDeviceWifiListCallback callback) {
        this.mEzvizAPI.getAccessDeviceWifiList(callback);
    }

    @Override
    public void queryPlatformBindStatus(String deviceSerial, QueryPlatformBindStatusCallback callback) {
        this.mEzvizAPI.queryPlatformBindStatus(deviceSerial, callback);
    }

    @Override
    public void setDevRouteDomain(String devRouteDomain) {
        this.mEzvizAPI.setDevRouteDomain(devRouteDomain);
    }

    public void startAPHttpConfigWifiWithToken(String configToken, String ssid, String password, String routerName, String routerPassword, String lbsDomain, boolean isAutoConnectDeviceHotSpot, APWifiConfig.APConfigCallback apConfigCallback) {
        this.mEzvizAPI.startAPHttpConfigWifiWithToken(configToken, ssid, password, routerName, routerPassword, lbsDomain, isAutoConnectDeviceHotSpot, apConfigCallback);
    }

    public void stopAPHttpConfigWifiWithToken() {
        this.mEzvizAPI.stopAPHttpConfigWifiWithToken();
    }

    @Override
    public EZPlayer createPlayerWithUrl(String url) {
        return this.mEzvizAPI.createPlayerWithUrl(url);
    }

    @Override
    public EZPlayer createPlayerWithUserId(int userId, int cameraNo, int streamType) {
        return this.mEzvizAPI.createPlayerWithUserId(userId, cameraNo, streamType);
    }

    @Override
    public EZPlayer createPlayer(String deviceSerial, int cameraNo) {
        return this.mEzvizAPI.createPlayer(deviceSerial, cameraNo);
    }

    @Override
    public EZPlayer createPlayer(String deviceSerial, int cameraNo, boolean useSubStream) {
        return this.mEzvizAPI.createPlayerWithDeviceSerial(deviceSerial, cameraNo, useSubStream);
    }

    @Override
    public void releasePlayer(EZPlayer player) {
        this.mEzvizAPI.releasePlayer(player);
    }

    @Override
    public List<EZDeviceInfo> getSharedDeviceList(int pageIndex, int pageSize) throws BaseException {
        return this.mEzvizAPI.getSharedDeviceList(pageIndex, pageSize);
    }

    @Override
    public boolean setVideoLevel(String deviceSerial, int cameraNo, int videoLevel) throws BaseException {
        return this.mEzvizAPI.setDeviceVideoLevel(deviceSerial, cameraNo, videoLevel);
    }

    @Override
    public EZDeviceInfo getDeviceInfo(String deviceSerial) throws BaseException {
        return this.mEzvizAPI.getDeviceInfo(deviceSerial, false);
    }

    @Override
    public String getTerminalId() {
        return this.mEzvizAPI.getTerminalId();
    }

    @Override
    public void clearStreamInfoCache() {
        this.mEzvizAPI.clearStreamInfoCache();
    }

    public EZHiddnsDeviceInfo getDDNSWithDeviceSerial(String deviceSerial, String domain) throws BaseException {
        return this.mEzvizAPI.getDDNSWithDeviceSerial(deviceSerial, domain);
    }

    public void setDeviceDoamin(String deviceSerial, String domain) throws BaseException {
        this.mEzvizAPI.setDeviceDoamin(deviceSerial, domain);
    }

    public void setDDNSAutomatic(String deviceSerial) throws BaseException {
        this.mEzvizAPI.setDDNSAutomatic(deviceSerial);
    }

    public void setDDNSManual(String deviceSerial, int cmdPort, int httpPort) throws BaseException {
        this.mEzvizAPI.setDDNSManual(deviceSerial, cmdPort, httpPort);
    }

    public List<EZHiddnsDeviceInfo> getDDNSDeviceList(int pageSize, int pageStart) throws BaseException {
        return this.mEzvizAPI.getDDNSDeviceList(pageSize, pageStart);
    }

    public void ShareDDNSDeviceList(String deviceSerial, String account) throws BaseException {
        this.mEzvizAPI.shareDDNSDeviceList(deviceSerial, account);
    }

    public List<EZHiddnsDeviceInfo> getShareDDNSDeviceList(int pageSize, int pageStart) throws BaseException {
        return this.mEzvizAPI.getShareDDNSDeviceList(pageSize, pageStart);
    }

    @Override
    public boolean isLogin() {
        return EZAuthApi.isLogin();
    }

    @Override
    public void setVparamForLoginPage(String vParam) {
        this.mEzvizAPI.setVparamForLoginPage(vParam);
    }

    @Override
    public void setServerUrl(String apiUrl, String webUrl) {
        this.mEzvizAPI.setServerUrl(apiUrl, webUrl);
    }

    public boolean isSupportCloundService() throws BaseException {
        return this.mEzvizAPI.isSupportCloundService();
    }

    public EZDeviceCloudServiceInfo getCloundDevicePackageInfo(String deviceSerial, int cameraNo) throws BaseException {
        return this.mEzvizAPI.getCloundDevicePackageInfo(deviceSerial, cameraNo);
    }

    public boolean setCloundServiceActive(String deviceSerial, int cameraNo, boolean enable) throws BaseException {
        return this.mEzvizAPI.setCloundServiceActive(deviceSerial, cameraNo, enable);
    }

    public List<String> getCloudVideoDays(String deviceSerial, int cameraNo, String month) throws BaseException {
        return this.mEzvizAPI.getCloudVideoDays(deviceSerial, cameraNo, month);
    }

    public boolean deleteAllCloudVideo(String deviceSerial, int cameraNo) throws BaseException {
        return this.mEzvizAPI.deleteAllCloudVideo(deviceSerial, cameraNo);
    }

    public List<String> getIncrCloudVideos(String deviceSerial, int cameraNo, EZConstants.EZCloudVideoType videoType, String searchDate, String maxCreateTime) throws BaseException {
        return this.mEzvizAPI.getIncrCloudVideos(deviceSerial, cameraNo, videoType, searchDate, maxCreateTime);
    }

    public List<EZCloudRecordFile> getCloudVideoDetails(String deviceSerial, int cameraNo, List<String> videos) throws BaseException {
        return this.mEzvizAPI.getCloudVideoDetails(deviceSerial, cameraNo, videos);
    }

    public boolean deleteCloudVideoFragment(String deviceSerial, int cameraNo, List<String> videos) throws BaseException {
        return this.mEzvizAPI.deleteCloudVideoFragment(deviceSerial, cameraNo, videos);
    }
}

