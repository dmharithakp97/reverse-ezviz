/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.os.Handler
 *  android.text.TextUtils
 *  android.util.Log
 *  com.ez.stream.EZStreamClientManager
 *  com.google.gson.Gson
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.openapi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.ez.stream.EZStreamClientManager;
import com.ezviz.opensdk.data.FileCacheDeviceInfoManager;
import com.google.gson.Gson;
import com.videogo.alarm.AlarmLogInfoManager;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.constant.Config;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.BaseSdkRuntimeException;
import com.videogo.main.AppManager;
import com.videogo.main.EzvizWebViewActivity;
import com.videogo.main.StreamServerData;
import com.videogo.openapi.BaseAPI;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.annotation.HttpParam;
import com.videogo.openapi.bean.BaseHeaderInfo;
import com.videogo.openapi.bean.BaseInfo;
import com.videogo.openapi.bean.CloudTicketInfo;
import com.videogo.openapi.bean.EZAlarmInfo;
import com.videogo.openapi.bean.EZBWCheckAddressInfo;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZCloudServicePackageInfo;
import com.videogo.openapi.bean.EZDetectorInfo;
import com.videogo.openapi.bean.EZDeviceCloudServiceInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.openapi.bean.EZDeviceRecordInfo;
import com.videogo.openapi.bean.EZDeviceUpgradeStatus;
import com.videogo.openapi.bean.EZDeviceVersion;
import com.videogo.openapi.bean.EZHiddnsDeviceInfo;
import com.videogo.openapi.bean.EZLeaveMessage;
import com.videogo.openapi.bean.EZPlayURLParams;
import com.videogo.openapi.bean.EZQosParams;
import com.videogo.openapi.bean.EZRelayServeInfo;
import com.videogo.openapi.bean.EZSDKConfiguration;
import com.videogo.openapi.bean.EZServerInfo;
import com.videogo.openapi.bean.EZStorageStatus;
import com.videogo.openapi.bean.EZStreamLimitInfo;
import com.videogo.openapi.bean.EZSubDeviceInfo;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.openapi.bean.EZVtmBackupInfo;
import com.videogo.openapi.bean.P2pDeviceInfo;
import com.videogo.openapi.bean.P2pUserInfo;
import com.videogo.openapi.bean.req.BaseAlarmInfo;
import com.videogo.openapi.bean.req.BatchGetTokens;
import com.videogo.openapi.bean.req.GetAlarmListReq;
import com.videogo.openapi.bean.req.GetStreamServer;
import com.videogo.openapi.bean.resp.CloudFile;
import com.videogo.openapi.bean.resp.EZDevicePlayInfo;
import com.videogo.openapi.model.ApiResponse;
import com.videogo.openapi.model.BaseResponse;
import com.videogo.openapi.model.EZAlarmDeleteMultipleAlarmsInfo;
import com.videogo.openapi.model.EZAlarmDeleteMultipleAlarmsReq;
import com.videogo.openapi.model.EZAlarmDeleteMultipleAlarmsResp;
import com.videogo.openapi.model.EZDeviceAddDeviceInfo;
import com.videogo.openapi.model.EZDeviceAddDeviceReq;
import com.videogo.openapi.model.EZDeviceAddDeviceResp;
import com.videogo.openapi.model.req.BatchGetTokensReq;
import com.videogo.openapi.model.req.GetCloudInfoReq;
import com.videogo.openapi.model.req.GetServersInfoReq;
import com.videogo.openapi.model.resp.BatchGetTokensResp;
import com.videogo.openapi.model.resp.GetCloudInfoResp;
import com.videogo.openapi.model.resp.GetServersInfoResp;
import com.videogo.openapi.model.resp.SetAlarmReadResp;
import com.videogo.ptz.EZPTZController;
import com.videogo.remoteplayback.CloudFileEx;
import com.videogo.remoteplayback.SDKCloudFileEx;
import com.videogo.stream.EZStreamDownload;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.EZOpenSDKConvertUtil;
import com.videogo.util.JsonTools;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.PlayUtils;
import com.videogo.util.ReflectionUtils;
import com.videogo.util.RestfulUtils;
import com.videogo.util.UUIDUtil;
import com.videogo.util.Utils;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint(value={"MissingPermission"})
public abstract class PlayAPI
extends BaseAPI {
    public static final String TAG = "PlayAPI";
    public static final String STR_NATIVE_PARAM_ERROR = "\u53c2\u6570\u9519\u8bef!";
    private static PlayAPI mPlayAPI = null;
    private AppManager mAppManager = null;
    private AlarmLogInfoManager mAlarmLogInfoManager = null;
    private List<String> mTokenList = null;
    private long mBatchTokensTime = 0L;
    private EZPTZController mEZPTZController = null;
    public static boolean isNetConnect = false;
    private static boolean isInit = false;
    private EZNetworkChangeListener mNetworkChangeListener;
    private Map<String, String> extendStreamParam;
    private long mLastPressTime;
    private int stub_storage_call_times = 0;
    private boolean streamStub = false;
    private int mStartUpgrade = 0;
    private int mUpgradeProgress = 0;

    public PlayAPI(Application application, String appKey, boolean isUsingGlobal) {
        super(application, appKey, isUsingGlobal);
        mPlayAPI = this;
        this.mAppManager = AppManager.getInstance();
        this.mAlarmLogInfoManager = AlarmLogInfoManager.getInstance();
        this.registerNetReceiver();
        FileCacheDeviceInfoManager.getDeviceCacheDetailInfo();
    }

    private void registerNetReceiver() {
        if (this.mNetworkChangeListener == null) {
            this.mNetworkChangeListener = new EZNetworkChangeListener();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            filter.addAction("android.net.wifi.STATE_CHANGE");
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mApplication.registerReceiver((BroadcastReceiver)this.mNetworkChangeListener, filter);
        }
    }

    public static PlayAPI getInstance() {
        return mPlayAPI;
    }

    @Override
    protected void releaseSDK() {
        super.releaseSDK();
        if (this.mNetworkChangeListener != null) {
            mApplication.unregisterReceiver((BroadcastReceiver)this.mNetworkChangeListener);
            this.mNetworkChangeListener = null;
        }
        mPlayAPI = null;
    }

    @Override
    protected void initParams() {
        super.initParams();
        new Thread(new Runnable(){

            @Override
            public void run() {
                if (!Config.ENABLE_SDK_TKTOKEN) {
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                if (EZStreamClientManager.create((Context)mApplication.getApplicationContext()).getLeftTokenCount() == 0) {
                                    LogUtil.d(PlayAPI.TAG, "no left token, need to set tokens");
                                    List<String> tokenList = DeviceManager.getInstance().getStreamToken();
                                    if (tokenList != null && tokenList.size() > 0) {
                                        EZStreamClientManager.create((Context)mApplication.getApplicationContext()).setTokens(tokenList.toArray(new String[tokenList.size()]));
                                    }
                                }
                            }
                            catch (BaseException e) {
                                LogUtil.printErrStackTrace(PlayAPI.TAG, e.fillInStackTrace());
                            }
                        }
                    }).start();
                }
                try {
                    AppManager.getInstance().getServerInfo();
                }
                catch (BaseException e) {
                    LogUtil.printErrStackTrace(PlayAPI.TAG, e.fillInStackTrace());
                }
                AppManager.getInstance().refreshP2pUserInfo();
                if (LocalInfo.getInstance().isPublicServAddr()) {
                    EZStreamClientManager.create((Context)BaseAPI.mApplication.getApplicationContext()).enableStreamClientETP();
                }
            }
        }).start();
    }

    @Override
    protected void clearCacheData() {
        super.clearCacheData();
        LogUtil.i(TAG, "Enter clearCacheData: ");
        this.mAppManager.clearServerInfo();
        this.clearVtduTokens();
        DeviceManager.getInstance().clearPreConnect();
        DeviceManager.getInstance().clearDevice();
        DeviceManager.getInstance().clearDeviceToken();
        CameraManager.getInstance().clearCamera();
        this.mAlarmLogInfoManager.clearAlarmListFromNotifier();
        this.mAlarmLogInfoManager.clearDeviceOfflineAlarmList();
        this.mAlarmLogInfoManager.clearAllOutsideAlarmList();
        this.mEZPTZController = null;
        this.mAppManager.clearAllStreamServer();
        DeviceManager.getInstance().clearDeviceToken();
        EZStreamClientManager.create((Context)mApplication).clearTokens();
        FileCacheDeviceInfoManager.clearDevice();
        FileCacheDeviceInfoManager.clearCamera();
    }

    @Override
    protected void clearStreamTokens() {
        super.clearStreamTokens();
        if (mApplication != null) {
            EZStreamClientManager.create((Context)mApplication).clearTokens();
        }
    }

    public List<CloudFile> getCloudFileList(final String deviceSerial, final int cameraNo, Calendar startTime, Calendar endTime, int pageStart, int pageSize) throws BaseException {
        String endTimeStr;
        LogUtil.i(TAG, "Enter getCloudFileList: ");
        if (TextUtils.isEmpty((CharSequence)deviceSerial) || pageStart < 0 || pageSize < 0) {
            LogUtil.i(TAG, "getCloudFileList: invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/cloud/list";
        final String startTimeStr = DateTimeUtil.calendarToYMDHMSString(startTime);
        BaseInfo baseInfo = new BaseInfo(endTimeStr = DateTimeUtil.calendarToYMDHMSString(endTime), pageStart, pageSize){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="cameraNo")
            private int mCameraNo;
            @HttpParam(name="startTime")
            private String startTime;
            @HttpParam(name="endTime")
            private String endTime;
            @HttpParam(name="pageStart")
            private int mPageStart;
            @HttpParam(name="pageSize")
            private int mPageSize;
            @HttpParam(name="version")
            private String version;
            final /* synthetic */ String val$endTimeStr;
            final /* synthetic */ int val$pageStart;
            final /* synthetic */ int val$pageSize;
            {
                this.val$endTimeStr = string3;
                this.val$pageStart = n2;
                this.val$pageSize = n3;
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
                this.startTime = startTimeStr;
                this.endTime = this.val$endTimeStr;
                this.mPageStart = this.val$pageStart;
                this.mPageSize = this.val$pageSize;
                this.version = "2.0";
            }
        };
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONArray dataArrayObj;
                boolean isOk = this.parseCode(reponse);
                if (isOk && (dataArrayObj = (resultObj = (rootObj = new JSONObject(reponse)).getJSONObject("result")).optJSONArray("data")) != null) {
                    ArrayList<CloudFile> retList = new ArrayList<CloudFile>();
                    int len = dataArrayObj.length();
                    for (int i = 0; i < len; ++i) {
                        CloudFile info = new CloudFile();
                        JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                        ReflectionUtils.convJSONToObject(jsonObj, info);
                        retList.add(info);
                    }
                    return retList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            for (CloudFile info : ret) {
                LogUtil.d(TAG, "getCloudFileList: " + new Gson().toJson((Object)info));
            }
            return ret;
        }
        return null;
    }

    public static boolean parserCode(String reponse) throws BaseException, JSONException {
        String RESULT = "result";
        int SUSCCEED = 200;
        String CODE = "code";
        String MSG = "description";
        String MSG2 = "msg";
        JSONObject jsonObject = new JSONObject(reponse);
        JSONObject result = jsonObject.getJSONObject("result");
        int resultCode = result.optInt("code", 400030);
        String resultDesc = result.optString("description", null);
        if (resultDesc == null) {
            resultDesc = result.optString("msg", "Resp Error:" + resultCode);
        }
        if (resultCode == 200) {
            return true;
        }
        if (resultCode == 400030) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
        }
        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, resultCode);
        if (errorInfo.description == null || errorInfo.description.length() == 0) {
            errorInfo.description = resultDesc;
        }
        throw new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
    }

    public EZServerInfo getServerInfo() throws BaseException {
        return (EZServerInfo)this.mRestfulUtils.postData(new GetServersInfoReq().buidParams(new BaseInfo()), "/api/server/info", new GetServersInfoResp());
    }

    public StreamServerData getStreamServer(int ispType) throws BaseException {
        GetStreamServer getStreamServer = new GetStreamServer();
        getStreamServer.setISPType(ispType == -1 ? 0 : ispType);
        return (StreamServerData)this.mRestfulUtils.postData(new GetCloudInfoReq().buidParams(getStreamServer), "/api/cloud/get", new GetCloudInfoResp());
    }

    public void getBatchTokens() throws BaseException {
        this.mBatchTokensTime = System.currentTimeMillis();
        BatchGetTokens batchGetTokens = new BatchGetTokens();
        batchGetTokens.setCount(10);
        this.mTokenList = (List)this.mRestfulUtils.postData(new BatchGetTokensReq().buidParams(batchGetTokens), "/api/user/token", new BatchGetTokensResp());
    }

    public String getVtduToken() {
        if (this.mBatchTokensTime > 0L && Math.abs(this.mBatchTokensTime - System.currentTimeMillis()) > 86400000L) {
            this.clearVtduTokens();
            return null;
        }
        if (this.mTokenList != null && this.mTokenList.size() > 0) {
            return this.mTokenList.remove(0);
        }
        return null;
    }

    public void clearVtduTokens() {
        if (this.mTokenList != null) {
            this.mTokenList.clear();
        }
        this.mBatchTokensTime = 0L;
    }

    public boolean setAlarmRead(String alarmId) throws BaseException {
        BaseAlarmInfo baseAlarmInfo = new BaseAlarmInfo();
        baseAlarmInfo.setAlarmId(alarmId);
        baseAlarmInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseAlarmInfo, "/api/alarm/sdk/setRead", new SetAlarmReadResp());
        return this.resultForAPICall(result);
    }

    public boolean deleteAlarm(List<String> alarmIdList) throws BaseException {
        if (alarmIdList == null || alarmIdList.size() <= 0) {
            LogUtil.e(TAG, "Enter deleteAlarm: alarmIdList is null or size is 0");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        LogUtil.d(TAG, "Enter deleteAlarm,  alarmIdList size : " + alarmIdList.size());
        EZAlarmDeleteMultipleAlarmsInfo info = new EZAlarmDeleteMultipleAlarmsInfo();
        StringBuilder sb = new StringBuilder();
        if (alarmIdList.size() == 1) {
            sb.append(alarmIdList.get(0));
        } else {
            sb.append(alarmIdList.get(0));
            for (int i = 1; i < alarmIdList.size(); ++i) {
                sb.append(",").append(alarmIdList.get(i));
            }
        }
        info.setDeleteString(sb.toString());
        boolean fakeDel = false;
        if (!fakeDel) {
            this.mRestfulUtils.postData(new EZAlarmDeleteMultipleAlarmsReq().buidParams(info), "/api/alarm/sdk/deleteAlarm", new EZAlarmDeleteMultipleAlarmsResp());
        }
        return true;
    }

    public boolean setAlarmStatus(List<String> alarmIdList, EZConstants.EZAlarmStatus alarmStatus) throws BaseException {
        if (alarmIdList == null || alarmIdList.size() <= 0) {
            LogUtil.e(TAG, "Enter setAlarmStatus, alarmIdList is null size:");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        LogUtil.d(TAG, "Enter setAlarmStatus, alarmIdList size:" + alarmIdList.size());
        StringBuilder sb = new StringBuilder();
        if (alarmIdList.size() == 1) {
            sb.append(alarmIdList.get(0));
        } else {
            sb.append(alarmIdList.get(0));
            for (int i = 1; i < alarmIdList.size(); ++i) {
                sb.append(",").append(alarmIdList.get(i));
            }
        }
        String alarmId = sb.toString();
        BaseAlarmInfo baseAlarmInfo = new BaseAlarmInfo();
        baseAlarmInfo.setAlarmId(alarmId);
        baseAlarmInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseAlarmInfo, "/api/alarm/sdk/setRead", new SetAlarmReadResp());
        return this.resultForAPICall(result);
    }

    public String getSdCoverRelayTicket(final String deviceSerial, final int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter getSdCoverRelayTicket");
        String urlString = "/api/service/media/streaming/relay/ticket";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="channelNo")
            private int mCameraNo;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    String ticket = rootObj.optJSONObject("data").optString("ticket");
                    return ticket;
                }
                return null;
            }
        });
        return (String)result;
    }

    public EZRelayServeInfo getSdCoverRelayServeInfo(final String deviceSerial, final int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter getSdCoverRelayServeInfo");
        String urlString = "/api/service/media/streaming/relay/server";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="channelNo")
            private int mCameraNo;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    EZRelayServeInfo serveInfo = new EZRelayServeInfo();
                    JSONObject rootObj = new JSONObject(response);
                    JSONObject dataObject = rootObj.getJSONObject("data");
                    JSONObject publicKeyObject = dataObject.getJSONObject("publicKey");
                    serveInfo.setDomain(dataObject.getString("domain"));
                    serveInfo.setPort(dataObject.getInt("port"));
                    serveInfo.setKey(publicKeyObject.getString("key"));
                    serveInfo.setVersion(publicKeyObject.getInt("version"));
                    return serveInfo;
                }
                return null;
            }
        });
        if (result != null) {
            return (EZRelayServeInfo)result;
        }
        return null;
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDevice(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.searchRecordFileFromDevice("/api/sdk/device/local/video", deviceSerial, cameraNo, startTime, endTime, null, 1);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDevice(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, String videoRecordType) throws BaseException {
        return this.searchRecordFileFromDevice("/api/v3/sdk/device/local/video", deviceSerial, cameraNo, startTime, endTime, videoRecordType, 1);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromDeviceEx(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, String videoRecordType) throws BaseException {
        String url = this.isUsingGlobalSDK() ? "/api/v3/device/local/video" : "/api/sdk/device/local/video";
        return this.searchRecordFileFromDevice(url, deviceSerial, cameraNo, startTime, endTime, videoRecordType, 1);
    }

    public List<EZDeviceRecordFile> searchRecordFileFromCVR(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        return this.searchRecordFileFromDevice("/api/sdk/device/local/video", deviceSerial, cameraNo, startTime, endTime, null, 2);
    }

    private List<EZDeviceRecordFile> searchRecordFileFromDevice(String urlString, String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, String videoRecordType, int location) throws BaseException {
        LogUtil.i(TAG, "Enter searchRecordFileFromDevice");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.e(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            LogUtil.e(TAG, "startTime and endTime must be not null, and startTime cannot be greater than endTime");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        final String fStartTime = DateTimeUtil.calendarToYMDHMSString(startTime);
        final String fEndTime = DateTimeUtil.calendarToYMDHMSString(endTime);
        final String fVideoRecordType = videoRecordType;
        final int fLocation = location;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String deviceSerial;
            @HttpParam(name="cameraNo")
            private int cameraNo;
            @HttpParam(name="startTime")
            private String startTime;
            @HttpParam(name="endTime")
            private String endTime;
            @HttpParam(name="recordType")
            private String recordType;
            @HttpParam(name="location")
            private int location;
            {
                this.deviceSerial = fDeviceSerial;
                this.cameraNo = fCameraNo;
                this.startTime = fStartTime;
                this.endTime = fEndTime;
                this.recordType = fVideoRecordType;
                this.location = fLocation;
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        BaseHeaderInfo baseHeaderInfo = new BaseHeaderInfo();
        baseHeaderInfo.setAccessToken(baseInfo.getAccessToken());
        Object result = this.mRestfulUtils.post(baseHeaderInfo, baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean bRet = PlayAPI.parserCode(response);
                if (!bRet) {
                    return null;
                }
                JSONObject jsonObject = new JSONObject(response);
                JSONObject resultObject = jsonObject.optJSONObject("result");
                JSONArray dataArray = resultObject.optJSONArray("data");
                ArrayList<EZDeviceRecordFile> deviceRecordFileList = new ArrayList<EZDeviceRecordFile>();
                for (int i = 0; i < dataArray.length(); ++i) {
                    JSONObject data = dataArray.getJSONObject(i);
                    EZDeviceRecordFile recordFile = new EZDeviceRecordFile();
                    ReflectionUtils.convJSONToObject(data, recordFile);
                    deviceRecordFileList.add(recordFile);
                }
                return deviceRecordFileList;
            }
        });
        if (result != null) {
            LogUtil.d(TAG, new Gson().toJson(result));
            return (List)result;
        }
        return null;
    }

    public EZDeviceRecordInfo searchRecordFileFromDeviceEx(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, int videoRecordType, final boolean isQueryFromNvr, int videoRecordLocation, int pageSize) throws BaseException {
        LogUtil.i(TAG, "Enter searchRecordFileFromDevice");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.e(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            LogUtil.e(TAG, "startTime and endTime must be not null, and startTime cannot be greater than endTime");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (pageSize < 1 || pageSize > 200) {
            LogUtil.e(TAG, "pageSize must be between 1 and 200");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/v3/device/local/video/unify/query";
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("startTime=").append(startTime.getTimeInMillis() / 1000L).append("&").append("endTime=").append(endTime.getTimeInMillis() / 1000L).append("&").append("location=").append(videoRecordLocation).append("&").append("isQueryByNvr=").append(isQueryFromNvr ? 1 : 0).append("&").append("pageSize=").append(pageSize);
        if (videoRecordType > 0) {
            urlBuilder.append("&").append("recordType=").append(videoRecordType);
        }
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String deviceSerial;
            @HttpParam(name="localIndex")
            private int cameraNo;
            {
                this.deviceSerial = fDeviceSerial;
                this.cameraNo = fCameraNo;
            }
        };
        baseInfo.fixAppName2Empty();
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.get(urlBuilder.toString(), baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean bRet = this.parseMetaCode(response);
                if (!bRet) {
                    return null;
                }
                JSONObject jsonObject = new JSONObject(response);
                JSONObject resultObject = jsonObject.optJSONObject("data");
                JSONArray dataArray = resultObject.optJSONArray("records");
                EZDeviceRecordInfo deviceRecordInfo = new EZDeviceRecordInfo();
                if (dataArray != null) {
                    ArrayList<EZDeviceRecordFile> deviceRecordFileList = new ArrayList<EZDeviceRecordFile>();
                    for (int i = 0; i < dataArray.length(); ++i) {
                        JSONObject data = dataArray.getJSONObject(i);
                        EZDeviceRecordFile recordFile = new EZDeviceRecordFile();
                        recordFile.setBegin(DateTimeUtil.formatTimeToYMDTHMSString(data.getLong("startTime") * 1000L));
                        recordFile.setEnd(DateTimeUtil.formatTimeToYMDTHMSString(data.getLong("endTime") * 1000L));
                        recordFile.setType(-1);
                        recordFile.setIPCRecordDirectQuery(!isQueryFromNvr);
                        deviceRecordFileList.add(recordFile);
                    }
                    deviceRecordInfo.setDeviceRecordFileList(deviceRecordFileList);
                }
                deviceRecordInfo.setHasMore(resultObject.optBoolean("hasMore"));
                long nextFileTime = resultObject.optLong("nextFileTime");
                Calendar nextStartTime = DateTimeUtil.parseYMDHMSStringToCalendar(DateTimeUtil.formatTimeToYMDHMSString(nextFileTime * 1000L));
                deviceRecordInfo.setNextFileTime(nextStartTime);
                deviceRecordInfo.setFromNvr(resultObject.optBoolean("fromNvr"));
                deviceRecordInfo.setDeviceSerial(resultObject.optString("deviceSerial"));
                return deviceRecordInfo;
            }
        });
        if (result != null) {
            LogUtil.d(TAG, new Gson().toJson(result));
            return (EZDeviceRecordInfo)result;
        }
        return null;
    }

    public List<EZCloudRecordFile> searchRecordFileFromSDKCloud(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, String spaceId) throws BaseException {
        List cloudFileList;
        LogUtil.i(TAG, "Enter searchRecordFileFromSDKCloudRecord");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/service/cloudrecord/video/info/list";
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        String fStartTime = DateTimeUtil.calendarToYMDHMSString(startTime);
        String fEndTime = DateTimeUtil.calendarToYMDHMSString(endTime);
        String fSpaceId = spaceId != null ? spaceId : "";
        String url = urlString + "?startTime=" + fStartTime + "&endTime=" + fEndTime + "&spaceId=" + fSpaceId;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String deviceSerial;
            @HttpParam(name="localIndex")
            private int cameraNo;
            {
                this.deviceSerial = fDeviceSerial;
                this.cameraNo = fCameraNo;
            }
        };
        baseInfo.fixAppName2Empty();
        baseInfo.fixDeviceGlobalToken(deviceSerial, cameraNo);
        Object result = this.get(url, baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean bRet = this.parseMetaCode(response);
                if (!bRet) {
                    return null;
                }
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArrayObj = jsonObject.optJSONArray("data");
                ArrayList<SDKCloudFileEx> retList = new ArrayList<SDKCloudFileEx>();
                if (dataArrayObj != null) {
                    for (int i = 0; i < dataArrayObj.length(); ++i) {
                        SDKCloudFileEx info = new SDKCloudFileEx();
                        JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                        ReflectionUtils.convJSONToObject(jsonObj, info);
                        retList.add(info);
                    }
                    return retList;
                }
                return retList;
            }
        });
        ArrayList<EZCloudRecordFile> ezCloudFileList = new ArrayList<EZCloudRecordFile>();
        if (result != null && (cloudFileList = (List)result) != null && cloudFileList.size() > 0) {
            for (SDKCloudFileEx cloudItem : cloudFileList) {
                EZCloudRecordFile outItem = new EZCloudRecordFile();
                EZOpenSDKConvertUtil.convertSDKCloudFileEx2EZCloudFile(outItem, cloudItem);
                ezCloudFileList.add(outItem);
            }
        }
        return ezCloudFileList;
    }

    public List<EZCloudRecordFile> searchRecordFileFromCloud(final String deviceSerial, final int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        LogUtil.i(TAG, "Enter searchRecordFileFromCloud, deviceSerial:" + deviceSerial + " startTime:" + (startTime != null ? startTime.getTime() : "null") + " endTime:" + (endTime != null ? endTime.getTime() : "null"));
        List cloudFileList = null;
        ArrayList<EZCloudRecordFile> ezCloudFileList = new ArrayList<EZCloudRecordFile>();
        final String startTimeStr = DateTimeUtil.calendarToYMDHMSString(startTime);
        final String endTimeStr = DateTimeUtil.calendarToYMDHMSString(endTime);
        String urlString = "/api/cloud/files/get";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="cameraNo")
            private int mCameraNo;
            @HttpParam(name="startTime")
            private String startTime;
            @HttpParam(name="endTime")
            private String endTime;
            @HttpParam(name="fileType")
            private int fileType;
            @HttpParam(name="version")
            private String version;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
                this.startTime = startTimeStr;
                this.endTime = endTimeStr;
                this.fileType = 1;
                this.version = "2.0";
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        for (int retryCount = 0; retryCount < 3; ++retryCount) {
            try {
                Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                    @Override
                    public Object parse(String reponse) throws BaseException, JSONException {
                        boolean isOk = this.parseCode(reponse);
                        if (isOk) {
                            JSONObject rootObj = new JSONObject(reponse);
                            JSONObject resultObj = rootObj.getJSONObject("result");
                            JSONArray dataArrayObj = resultObj.optJSONArray("data");
                            ArrayList<CloudFileEx> retList = new ArrayList<CloudFileEx>();
                            if (dataArrayObj != null) {
                                int len = dataArrayObj.length();
                                for (int i = 0; i < len; ++i) {
                                    CloudFileEx info = new CloudFileEx();
                                    JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                                    ReflectionUtils.convJSONToObject(jsonObj, info);
                                    retList.add(info);
                                }
                                return retList;
                            }
                            return retList;
                        }
                        return null;
                    }
                });
                if (result == null) break;
                cloudFileList = (List)result;
                for (CloudFileEx info : cloudFileList) {
                    LogUtil.d(TAG, "searchRecordFileFromCloud: " + new Gson().toJson((Object)info));
                }
                break;
            }
            catch (BaseException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                continue;
            }
        }
        if (cloudFileList != null && cloudFileList.size() > 0) {
            for (CloudFileEx cloudItem : cloudFileList) {
                EZCloudRecordFile outItem = new EZCloudRecordFile();
                EZOpenSDKConvertUtil.convertCloudFileEx2EZCloudFile(outItem, cloudItem);
                ezCloudFileList.add(outItem);
            }
        }
        return ezCloudFileList;
    }

    public List<EZCloudRecordFile> searchRecordFileFromCloudEx(final String deviceSerial, final int cameraNo, Calendar startTime, Calendar endTime) throws BaseException {
        LogUtil.i(TAG, "Enter searchRecordFileFromCloud, deviceSerial:" + deviceSerial + " startTime:" + (startTime != null ? startTime.getTime() : "null") + " endTime:" + (endTime != null ? endTime.getTime() : "null"));
        List cloudFileList = null;
        ArrayList<EZCloudRecordFile> ezCloudFileList = new ArrayList<EZCloudRecordFile>();
        final String startTimeStr = DateTimeUtil.calendarToYMDHMSString(startTime);
        final String endTimeStr = DateTimeUtil.calendarToYMDHMSString(endTime);
        String urlString = "/api/cloud/files/get";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="cameraNo")
            private int mCameraNo;
            @HttpParam(name="startTime")
            private String startTime;
            @HttpParam(name="endTime")
            private String endTime;
            @HttpParam(name="fileType")
            private int fileType;
            @HttpParam(name="version")
            private String version;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
                this.startTime = startTimeStr;
                this.endTime = endTimeStr;
                this.fileType = 1;
                this.version = "3.0";
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        for (int retryCount = 0; retryCount < 3; ++retryCount) {
            try {
                Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                    @Override
                    public Object parse(String reponse) throws BaseException, JSONException {
                        boolean isOk = this.parseCode(reponse);
                        if (isOk) {
                            JSONObject rootObj = new JSONObject(reponse);
                            JSONObject resultObj = rootObj.getJSONObject("result");
                            JSONArray dataArrayObj = resultObj.optJSONArray("data");
                            ArrayList<CloudFileEx> retList = new ArrayList<CloudFileEx>();
                            if (dataArrayObj != null) {
                                int len = dataArrayObj.length();
                                for (int i = 0; i < len; ++i) {
                                    CloudFileEx info = new CloudFileEx();
                                    JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                                    ReflectionUtils.convJSONToObject(jsonObj, info);
                                    retList.add(info);
                                }
                                return retList;
                            }
                            return retList;
                        }
                        return null;
                    }
                });
                if (result == null) break;
                cloudFileList = (List)result;
                break;
            }
            catch (BaseException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                continue;
            }
        }
        if (cloudFileList != null && cloudFileList.size() > 0) {
            for (CloudFileEx cloudItem : cloudFileList) {
                EZCloudRecordFile outItem = new EZCloudRecordFile();
                EZOpenSDKConvertUtil.convertCloudFileEx2EZCloudFile(outItem, cloudItem);
                ezCloudFileList.add(outItem);
            }
        }
        return ezCloudFileList;
    }

    public List<EZAlarmInfo> getAlarmList(String deviceSerial, int cameraNo, Calendar startTime, Calendar endTime, EZConstants.EZAlarmType alarmType, EZConstants.EZAlarmStatus alarmStatus, int pageStart, int pageSize) throws BaseException {
        if (pageStart < 0 || pageSize <= 0) {
            LogUtil.e(TAG, "getAlarmListBySerial, invalid parameters page ;pageStart= " + pageStart + " pageSize = " + pageSize);
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            LogUtil.i(TAG, "getAlarmListBySerial, invalid parameters, begin and end time");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/alarm/list";
        GetAlarmListReq getAlarmListReq = new GetAlarmListReq();
        getAlarmListReq.setDeviceSerial(deviceSerial);
        getAlarmListReq.setCameraNo(cameraNo);
        getAlarmListReq.setStartTime(DateTimeUtil.calendarToYMDHMSString(startTime));
        getAlarmListReq.setEndTime(DateTimeUtil.calendarToYMDHMSString(endTime));
        getAlarmListReq.setStatus(alarmStatus.getAlarmStatus());
        getAlarmListReq.setPageStart(pageStart);
        getAlarmListReq.setPageSize(pageSize);
        getAlarmListReq.setAlarmType(alarmType.getTypeId());
        getAlarmListReq.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(getAlarmListReq, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                int length;
                JSONObject rootObj;
                JSONObject resultObj;
                JSONArray arrayObj;
                boolean ret = this.parseCode(response);
                ArrayList<EZAlarmInfo> ezAlarmInfos = null;
                if (ret && (arrayObj = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONArray("data")) != null && (length = arrayObj.length()) > 0) {
                    ezAlarmInfos = new ArrayList<EZAlarmInfo>();
                    for (int i = 0; i < length; ++i) {
                        JSONObject diskObj = arrayObj.getJSONObject(i);
                        EZAlarmInfo ezAlarmInfo = new EZAlarmInfo();
                        ReflectionUtils.convJSONToObject(diskObj, ezAlarmInfo);
                        ezAlarmInfos.add(ezAlarmInfo);
                    }
                }
                return ezAlarmInfos;
            }
        });
        if (result != null) {
            List resultList = (List)result;
            LogUtil.d(TAG, "getAlarmList EZAlarmInfo:" + resultList);
            return resultList;
        }
        return null;
    }

    public EZPlayer createPlayer(String deviceSerial, int cameraNo) {
        return this.createPlayer(deviceSerial, cameraNo, false);
    }

    public EZPlayer createPlayer(String deviceSerial, int cameraNo, EZQosParams ezQosParams) {
        return this.createPlayer(deviceSerial, cameraNo, false, ezQosParams);
    }

    public EZPlayer createPlayerWithUserId(int userId, int cameraNo, int streamType) {
        return new EZPlayer(userId, cameraNo, streamType);
    }

    public EZPlayer createPlayerWithUrl(String url) {
        return this.createPlayer(url, 0, true);
    }

    public EZPlayer createPlayer(EZPlayURLParams ezPlayURLParams) {
        if (ezPlayURLParams != null) {
            EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(ezPlayURLParams);
            return new EZPlayer(streamParamHelp);
        }
        return null;
    }

    private EZPlayer createPlayer(String deviceSerialOrUrl, int cameraNo, boolean isUrl) {
        return this.createPlayer(deviceSerialOrUrl, cameraNo, isUrl, null);
    }

    private EZPlayer createPlayer(String deviceSerialOrUrl, int cameraNo, boolean isUrl, EZQosParams ezQosParams) {
        LogUtil.d(TAG, "Enter createPlayer, ");
        if (TextUtils.isEmpty((CharSequence)deviceSerialOrUrl)) {
            return null;
        }
        LogUtil.d(TAG, "Enter createPlayer, deviceSerialOrUrl = " + deviceSerialOrUrl + "  cameraNo = " + cameraNo + " isUrl = " + isUrl);
        if (isUrl) {
            if (Utils.isEZOpenProtocol(deviceSerialOrUrl)) {
                EZPlayURLParams ezPlayURLParams = null;
                ezPlayURLParams = PlayUtils.getEZPlayURLParams(deviceSerialOrUrl);
                EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(ezPlayURLParams);
                return new EZPlayer(streamParamHelp);
            }
            return new EZPlayer(deviceSerialOrUrl);
        }
        EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(deviceSerialOrUrl, cameraNo);
        if (ezQosParams != null) {
            streamParamHelp.iQosTakPort = ezQosParams.iQosTakPort;
            streamParamHelp.iQosTalkVersion = ezQosParams.iQosTalkVersion;
            streamParamHelp.szQosTaklIP = ezQosParams.szQosTaklIP;
            streamParamHelp.setiQosRspTimeout(ezQosParams.qosRepTimeout);
        }
        return new EZPlayer(streamParamHelp);
    }

    public void releasePlayer(EZPlayer player) {
        LogUtil.d(TAG, "Enter releasePlayer, ");
        if (player == null) {
            return;
        }
        player.release();
        player = null;
    }

    public boolean addDevice(String deviceSerial) throws BaseException {
        LogUtil.d(TAG, "Enter addDevice");
        EZDeviceAddDeviceInfo info = new EZDeviceAddDeviceInfo();
        if (TextUtils.isEmpty((CharSequence)deviceSerial)) {
            return false;
        }
        LogUtil.d(TAG, "Enter addDevice,deviceSerial=  " + deviceSerial);
        info.setDeviceSN(deviceSerial);
        info.fixDeviceToken(deviceSerial);
        this.mRestfulUtils.postData(new EZDeviceAddDeviceReq().buidParams(info), "device/sdk/addDevice", new EZDeviceAddDeviceResp());
        return true;
    }

    public static boolean parserTransferApiCode(String reponse) throws BaseException {
        ErrorInfo errorInfo;
        String RESULT = "result";
        int SUSCCEED = 200;
        String CODE = "code";
        String MSG = "description";
        int resultCode = 200;
        String resultDesc = "";
        try {
            JSONObject jsonObject = new JSONObject(reponse);
            JSONObject result = jsonObject.getJSONObject("result");
            resultCode = result.optInt("code", 400030);
            resultDesc = result.optString("description", "Resp Error:" + resultCode);
        }
        catch (JSONException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            resultCode = 400030;
        }
        if (resultCode == 200) {
            return true;
        }
        if (resultCode == 400030) {
            errorInfo = ErrorLayer.getErrorLayer(2, 400030);
            throw new BaseException("IO Error", errorInfo.errorCode, errorInfo);
        }
        errorInfo = ErrorLayer.getErrorLayer(1, resultCode);
        if (errorInfo.description == null || errorInfo.description.length() == 0) {
            errorInfo.description = resultDesc;
        }
        throw new BaseException(resultDesc, errorInfo.errorCode, errorInfo);
    }

    public boolean controlPTZ(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int speed) throws BaseException {
        Object result;
        if (this.mLastPressTime == 0L) {
            this.mLastPressTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        LogUtil.e(TAG, "controlPTZ time diff: " + (currentTime - this.mLastPressTime));
        this.mLastPressTime = currentTime;
        LogUtil.d(TAG, "Enter controlPTZ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.e(TAG, "setDeviceEnryptStatusEx, invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        final int fDirection = command.getCommand();
        final int fSpeed = speed;
        if (action == EZConstants.EZPTZAction.EZPTZActionSTART) {
            String urlString = "/api/device/ptz/start";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="direction")
                private int direction;
                @HttpParam(name="speed")
                private int speed;
                @HttpParam(name="deviceSerial")
                private String deviceSerial;
                @HttpParam(name="channelNo")
                private int channelNo;
                {
                    this.direction = fDirection;
                    this.speed = fSpeed;
                    this.deviceSerial = fDeviceSerial;
                    this.channelNo = fCameraNo;
                }
            };
            baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    return PlayAPI.parserCode(response);
                }
            });
        } else {
            String urlString = "/api/device/ptz/stop";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="direction")
                private int direction;
                @HttpParam(name="deviceSerial")
                private String deviceSerial;
                @HttpParam(name="channelNo")
                private int channelNo;
                {
                    this.direction = fDirection;
                    this.deviceSerial = fDeviceSerial;
                    this.channelNo = fCameraNo;
                }
            };
            baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    return PlayAPI.parserCode(response);
                }
            });
        }
        return this.resultForAPICall(result);
    }

    public boolean controlPTZEx(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int newSpeed, String uuid) throws BaseException {
        Object result;
        if (this.mLastPressTime == 0L) {
            this.mLastPressTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        LogUtil.e(TAG, "controlPTZ time diff: " + (currentTime - this.mLastPressTime));
        this.mLastPressTime = currentTime;
        LogUtil.d(TAG, "Enter controlPTZ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.e(TAG, "setDeviceEnryptStatusEx, invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        final int fDirection = command.getCommand();
        final int fNewSpeed = newSpeed;
        final String fUuid = uuid;
        if (action == EZConstants.EZPTZAction.EZPTZActionSTART) {
            String urlString = "/api/device/ptz/start";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="direction")
                private int direction;
                @HttpParam(name="newSpeed")
                private int newSpeed;
                @HttpParam(name="deviceSerial")
                private String deviceSerial;
                @HttpParam(name="channelNo")
                private int channelNo;
                @HttpParam(name="uuid")
                private String uuid;
                {
                    this.direction = fDirection;
                    this.newSpeed = fNewSpeed;
                    this.deviceSerial = fDeviceSerial;
                    this.channelNo = fCameraNo;
                    this.uuid = fUuid;
                }
            };
            baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    return PlayAPI.parserCode(response);
                }
            });
        } else if (action == EZConstants.EZPTZAction.EZPTZActionSTOP && TextUtils.isEmpty((CharSequence)fUuid)) {
            String urlString = "/api/device/ptz/stop";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="direction")
                private int direction;
                @HttpParam(name="deviceSerial")
                private String deviceSerial;
                @HttpParam(name="channelNo")
                private int channelNo;
                {
                    this.direction = fDirection;
                    this.deviceSerial = fDeviceSerial;
                    this.channelNo = fCameraNo;
                }
            };
            baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    return PlayAPI.parserCode(response);
                }
            });
        } else {
            String urlString = "/api/v3/sdk/device/ptz/stop";
            BaseInfo baseInfo = new BaseInfo(){
                @HttpParam(name="direction")
                private int direction;
                @HttpParam(name="deviceSerial")
                private String deviceSerial;
                @HttpParam(name="channelNo")
                private int channelNo;
                @HttpParam(name="uuid")
                private String uuid;
                {
                    this.direction = fDirection;
                    this.deviceSerial = fDeviceSerial;
                    this.channelNo = fCameraNo;
                    this.uuid = fUuid;
                }
            };
            baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
            result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

                @Override
                public Object parse(String response) throws BaseException, JSONException {
                    return PlayAPI.parserCode(response);
                }
            });
        }
        return this.resultForAPICall(result);
    }

    public boolean controlPTZMix(final String deviceSerial, final int cameraNo, final EZConstants.EZPTZCommand command, final EZConstants.EZPTZAction action, final int newSpeed) throws BaseException {
        final String uuid = UUIDUtil.getRandomUUID();
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    PlayAPI.this.controlPTZViaP2P(deviceSerial, cameraNo, command, action, newSpeed, uuid);
                }
                catch (BaseException e) {
                    e.printStackTrace();
                    LogUtil.e(PlayAPI.TAG, "controlPTZViaP2P failed.");
                }
            }
        }).start();
        return this.controlPTZEx(deviceSerial, cameraNo, command, action, newSpeed, uuid);
    }

    public boolean controlPTZViaP2P(String deviceSerial, int cameraNo, EZConstants.EZPTZCommand command, EZConstants.EZPTZAction action, int newSpeed, String uuid) throws BaseException {
        LogUtil.i(TAG, "Enter controlPTZViaP2P");
        if (TextUtils.isEmpty((CharSequence)deviceSerial)) {
            throw new BaseException("", 400002);
        }
        if (this.mEZPTZController == null) {
            this.mEZPTZController = new EZPTZController(deviceSerial, cameraNo);
        } else {
            this.mEZPTZController.setDeviceSerial(deviceSerial);
            this.mEZPTZController.setCameraNo(cameraNo);
        }
        long before = System.currentTimeMillis();
        int result = this.mEZPTZController.ptzControl(command.getCommand(), action, newSpeed, uuid);
        long after = System.currentTimeMillis();
        LogUtil.d(TAG, String.format("controlPTZViaP2P action:%s result:%d timeDiff:%d", action.getAction(), result, after - before));
        return result == 0;
    }

    public void controlVideoFlip(String deviceSerial, int cameraNo, EZConstants.EZPTZDisplayCommand command) throws BaseException {
        LogUtil.i(TAG, "Enter controlVideoFlip");
        if (TextUtils.isEmpty((CharSequence)deviceSerial)) {
            throw new BaseException("", 400002);
        }
        if (this.mEZPTZController == null || TextUtils.isEmpty((CharSequence)this.mEZPTZController.getDeviceSerial()) || !this.mEZPTZController.getDeviceSerial().equalsIgnoreCase(deviceSerial) || this.mEZPTZController.getCameraNo() != cameraNo) {
            this.mEZPTZController = new EZPTZController(deviceSerial, cameraNo);
        }
        this.mEZPTZController = new EZPTZController(deviceSerial, cameraNo);
        int ret = 0;
        if (command.getCommand() == 4) {
            ret = this.mEZPTZController.ptzControl(command.getCommand(), "CENTER", 0, 0);
        }
        if (ret != 100) {
            throw new BaseException("", ret);
        }
    }

    public String capturePicture(final String deviceSerial, final int channelNo) throws BaseException {
        LogUtil.i(TAG, "Enter capturePicture");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (channelNo < 0) {
            LogUtil.i(TAG, "capturePicture, invalid parameters channelNo");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/capturePicture";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="channelNo")
            private int channelNumber;
            {
                this.devSerial = deviceSerial;
                this.channelNumber = channelNo;
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, channelNo);
        String url = (String)this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public String parse(String reponse) throws BaseException, JSONException {
                JSONObject rootObject;
                JSONObject resultObject;
                boolean isOk = this.parseCode(reponse);
                if (!isOk) {
                    return null;
                }
                if (isOk && (resultObject = (rootObject = new JSONObject(reponse)).getJSONObject("result")) != null) {
                    String url = resultObject.optString("data");
                    return url;
                }
                return null;
            }
        });
        return url;
    }

    public EZDeviceVersion getDeviceVersion(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceVersion");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/getVersionInfo";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            {
                this.devSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object ret = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                JSONObject dataObject;
                boolean isOk = this.parseCode(reponse);
                if (!isOk) {
                    return null;
                }
                JSONObject rootObject = new JSONObject(reponse);
                JSONObject resultObject = rootObject.getJSONObject("result");
                if (resultObject != null && (dataObject = resultObject.optJSONObject("data")) != null) {
                    EZDeviceVersion deviceVersion = new EZDeviceVersion();
                    ReflectionUtils.convJSONToObject(dataObject, deviceVersion);
                    return deviceVersion;
                }
                return null;
            }
        });
        if (ret != null && ret instanceof EZDeviceVersion) {
            EZDeviceVersion version = (EZDeviceVersion)ret;
            LogUtil.i(TAG, "getDeviceVersion: " + version);
            return (EZDeviceVersion)ret;
        }
        return null;
    }

    public boolean setDeviceEncryptStatus(final String deviceSerial, final String validateCode, final boolean encrypt) throws BaseException {
        DeviceInfoEx deviceInfoEx;
        LogUtil.i(TAG, "Enter setDeviceEnryptStatusEx");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (!encrypt && (nRet = this.mLocalValidate.localValidateParam(validateCode)) != 0) {
            LogUtil.i(TAG, "setDeviceEnryptStatusEx, invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/encrypt/set";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="isEncrypt")
            private int mIsEncrypt;
            @HttpParam(name="validateCode")
            private String mValidateCode;
            {
                this.mDeviceSerial = deviceSerial;
                this.mIsEncrypt = encrypt ? 1 : 0;
                this.mValidateCode = validateCode;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseCode(response);
                return ret;
            }
        });
        boolean ret = this.resultForAPICall(result);
        if (ret && (deviceInfoEx = DeviceManager.getInstance().getDeviceInfoExById(deviceSerial)) != null) {
            deviceInfoEx.setIsEncrypt(encrypt ? 1 : 0);
        }
        return ret;
    }

    public boolean updateDeviceName(final String deviceSerial, final String deviceName) throws BaseException {
        LogUtil.i(TAG, "Enter updateDeviceName");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (TextUtils.isEmpty((CharSequence)deviceName)) {
            LogUtil.i(TAG, "updateDeviceName, invalid parameters:deviceName is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/updateDeviceName";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="deviceName")
            private String devName;
            {
                this.devSerial = deviceSerial;
                this.devName = deviceName;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                return this.parseCode(response);
            }
        });
        return this.resultForAPICall(result);
    }

    public int getUnreadMessageCount(String deviceSerial, final EZConstants.EZMessageType messageType) throws BaseException {
        LogUtil.i(TAG, "Enter getUnreadMessageCount");
        String serial = deviceSerial;
        if (deviceSerial == null) {
            serial = "";
        }
        final String devSerial = serial;
        String urlString = "/api/message/unreadCount";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mSerial;
            @HttpParam(name="type")
            private int mType;
            {
                this.mSerial = devSerial;
                this.mType = messageType.getMessageType();
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        int count = (Integer)this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean isOk = this.parseCode(response);
                if (isOk && (dataObj = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    int count = dataObj.optInt("unreadCount", 0);
                    return count;
                }
                return -1;
            }
        });
        LogUtil.i(TAG, "getUnreadMessageCount returns. count:" + count);
        return count;
    }

    public List<EZLeaveMessage> getLeaveMessageList(String deviceSerial, final int pageIndex, final int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        LogUtil.i(TAG, "Enter getLeaveMessageList");
        String serial = deviceSerial;
        if (deviceSerial == null) {
            serial = "";
        }
        final String devSerial = serial;
        if (pageIndex < 0 || pageSize <= 0) {
            LogUtil.i(TAG, "getLeaveMessageList, invalid parameters page");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (beginTime == null || endTime == null) {
            LogUtil.i(TAG, "getLeaveMessageList, invalid parameters, begin or end time is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (beginTime.after(endTime)) {
            LogUtil.i(TAG, "getLeaveMessageList, invalid parameters, begin after end time");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/message/leaves/get";
        final String strBeginTime = DateTimeUtil.calendarToYMDHMSString(beginTime);
        final String strEndTime = DateTimeUtil.calendarToYMDHMSString(endTime);
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="pageStart")
            private int mPageIndex;
            @HttpParam(name="pageSize")
            private int mPageSize;
            @HttpParam(name="startTime")
            private String mBeginTime;
            @HttpParam(name="endTime")
            private String mEndTime;
            @HttpParam(name="status")
            private int mStatus;
            @HttpParam(name="contentType")
            private int mContentType;
            {
                this.mDeviceSerial = devSerial;
                this.mPageIndex = pageIndex;
                this.mPageSize = pageSize;
                this.mBeginTime = strBeginTime;
                this.mEndTime = strEndTime;
                this.mStatus = 0;
                this.mContentType = 1;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object ret = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isOk = this.parseCode(response);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONArray dataArrayObj = resultObj.optJSONArray("data");
                    if (dataArrayObj != null && dataArrayObj.length() > 0) {
                        ArrayList<EZLeaveMessage> leaveMsgList = new ArrayList<EZLeaveMessage>();
                        int size = dataArrayObj.length();
                        for (int i = 0; i < size; ++i) {
                            JSONObject item = dataArrayObj.getJSONObject(i);
                            if (item == null) continue;
                            EZLeaveMessage leaveMsg = new EZLeaveMessage();
                            ReflectionUtils.convJSONToObject(item, leaveMsg);
                            Date createDate = DateTimeUtil.parseYMDHMSStringToDate(leaveMsg.getInternalCreateTime());
                            Date updateDate = DateTimeUtil.parseYMDHMSStringToDate(leaveMsg.getInternalUpdateTime());
                            Calendar createTime = Calendar.getInstance();
                            createTime.setTime(createDate);
                            Calendar updateTime = Calendar.getInstance();
                            updateTime.setTime(updateDate);
                            leaveMsg.setCreateTime(createTime);
                            leaveMsg.setUpdateTime(updateTime);
                            LogUtil.v(PlayAPI.TAG, leaveMsg.toString());
                            leaveMsgList.add(leaveMsg);
                        }
                        return leaveMsgList;
                    }
                    return null;
                }
                return null;
            }
        });
        if (ret != null) {
            LogUtil.i(TAG, "" + (List)ret);
            return (List)ret;
        }
        return null;
    }

    public boolean setLeaveMessageStatus(List<String> msgIdList, EZConstants.EZMessageStatus messageStatus) throws BaseException {
        LogUtil.i(TAG, "Enter setLeaveMessageStatus");
        final String msgId = this.getCommaSeprateStrFromList(msgIdList);
        if (TextUtils.isEmpty((CharSequence)msgId)) {
            LogUtil.i(TAG, "setLeaveMessageStatus, invalid parameters msgIdList");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/message/leave/operate";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="messageId")
            private String mMsgIds;
            @HttpParam(name="type")
            private String mType;
            {
                this.mMsgIds = msgId;
                this.mType = "1";
            }
        };
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                return this.parseCode(reponse);
            }
        });
        return this.resultForAPICall(result);
    }

    public boolean deleteLeaveMessages(List<String> msgIdList) throws BaseException {
        LogUtil.i(TAG, "Enter deleteLeaveMessages");
        final String msgId = this.getCommaSeprateStrFromList(msgIdList);
        if (TextUtils.isEmpty((CharSequence)msgId)) {
            LogUtil.i(TAG, "deleteLeaveMessages, invalid parameters msgIdList");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/message/leave/operate";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="messageId")
            private String mMsgIds;
            @HttpParam(name="type")
            private String mType;
            {
                this.mMsgIds = msgId;
                this.mType = "2";
            }
        };
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                return this.parseCode(reponse);
            }
        });
        return this.resultForAPICall(result);
    }

    public boolean formatStorage(final String deviceSerial, final int partitionIndex) throws BaseException {
        LogUtil.i(TAG, "Enter formatStorage");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (partitionIndex < 0) {
            LogUtil.i(TAG, "formatStorage, invalid parameters partitionIndex");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/format/disk";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="diskIndex")
            private int mDiskIndex;
            {
                this.mDeviceSerial = deviceSerial;
                this.mDiskIndex = partitionIndex;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseCode(response);
                return ret;
            }
        });
        return this.resultForAPICall(result);
    }

    public void getLeaveMessageData(Handler handler, EZLeaveMessage msg, EZOpenSDKListener.EZLeaveMessageFlowCallback callback) {
        EZStreamDownload ezStreamDownload = new EZStreamDownload();
        ezStreamDownload.setHandler(handler);
        ezStreamDownload.setLeaveMessageFlowCallback(callback);
        ezStreamDownload.start(msg);
    }

    public List<EZStorageStatus> getStorageStatus(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter getStorageStatus");
        ++this.stub_storage_call_times;
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/format/status";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean ret = this.parseCode(response);
                if (ret && (dataObj = (resultObj = (rootObj = new JSONObject(response)).optJSONObject("result")).optJSONObject("data")) != null) {
                    JSONArray arrayObj = dataObj.optJSONArray("storageStatus");
                    if (arrayObj == null) {
                        return null;
                    }
                    int length = arrayObj.length();
                    if (length > 0) {
                        ArrayList<EZStorageStatus> storageStatusList = new ArrayList<EZStorageStatus>();
                        for (int i = 0; i < length; ++i) {
                            JSONObject diskObj = arrayObj.getJSONObject(i);
                            EZStorageStatus storageStatus = new EZStorageStatus();
                            storageStatus.setIndex(diskObj.optInt("index"));
                            storageStatus.setName(diskObj.optString("name"));
                            int status = diskObj.optInt("status");
                            int formatRate = diskObj.optInt("formattingRate");
                            storageStatus.setStatus(status);
                            if (status == 3) {
                                storageStatus.setFormatRate(formatRate);
                            }
                            storageStatusList.add(storageStatus);
                        }
                        return storageStatusList;
                    }
                }
                return null;
            }
        });
        if (result != null) {
            List resultList = (List)result;
            LogUtil.i(TAG, "getStorageStatus:" + resultList);
            return resultList;
        }
        return null;
    }

    public boolean deleteDeviceNonTransfer(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter deleteDeviceNonTransfer");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/deleteDevice";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                return this.parseCode(reponse);
            }
        });
        boolean ret = this.resultForAPICall(result);
        if (ret) {
            DeviceManager.getInstance().deleteDevice(deviceSerial);
        }
        return ret;
    }

    public EZDeviceUpgradeStatus getDeviceUpgradeStatus(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceUpgradeStatus");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/upgrade/status";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean isOk = this.parseCode(reponse);
                if (isOk && (dataObj = (resultObj = (rootObj = new JSONObject(reponse)).getJSONObject("result")).optJSONObject("data")) != null) {
                    EZDeviceUpgradeStatus info = new EZDeviceUpgradeStatus();
                    int upgradeStatus = dataObj.optInt("upgradeStatus");
                    int upgradeProgress = dataObj.optInt("upgradeProgress");
                    info.setUpgradeProgress(upgradeProgress);
                    info.setUpgradeStatus(upgradeStatus);
                    return info;
                }
                return null;
            }
        });
        if (result != null) {
            EZDeviceUpgradeStatus ret = (EZDeviceUpgradeStatus)result;
            LogUtil.d(TAG, "getDeviceUpgradeStatus: " + ret);
            return ret;
        }
        return null;
    }

    public void upgradeDevice(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter upgradeDevice");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/upgrade";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                return isOk;
            }
        });
    }

    public void openCloudPage(String deviceSerial, int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter openCloudPage");
        if (Config.ENABLE_SDK_TKTOKEN) {
            return;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Intent intent = new Intent((Context)mApplication, EzvizWebViewActivity.class);
        intent.setFlags(0x10000000);
        intent.putExtra("com.videogo.EXTRA_WEBVIEW_ACTION", 3);
        intent.putExtra("serial", deviceSerial);
        intent.putExtra("channel", cameraNo);
        mApplication.startActivity(intent);
    }

    public EZDevicePlayInfo getDevicePlayInfo(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter getDevicePlayInfo: ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/sdk/detail";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            {
                this.mDeviceSerial = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean isOk = this.parseCode(reponse);
                if (isOk && (dataObj = (resultObj = (rootObj = new JSONObject(reponse)).getJSONObject("result")).optJSONObject("data")) != null) {
                    EZDevicePlayInfo info = new EZDevicePlayInfo();
                    ReflectionUtils.convJSONToObject(dataObj, info);
                    return info;
                }
                return null;
            }
        });
        if (result != null) {
            EZDevicePlayInfo ret = (EZDevicePlayInfo)result;
            LogUtil.d(TAG, "getDevicePlayInfo: return:" + ret);
            return ret;
        }
        LogUtil.i(TAG, "getDevicePlayInfo: return: null");
        return null;
    }

    public boolean addDeviceBySerialNonTransfer(final String deviceSerial, final String verifyCode) throws BaseException {
        LogUtil.i(TAG, "Enter addDeviceBySerialNonTransfer: ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (TextUtils.isEmpty((CharSequence)verifyCode)) {
            LogUtil.i(TAG, "addDeviceBySerialNonTransfer, invalid parameters deviceCode");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/addDevice";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="validateCode")
            private String mVerifyCode;
            {
                this.mDeviceSerial = deviceSerial;
                this.mVerifyCode = verifyCode;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new BaseResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                return this.parseCode(reponse);
            }
        });
        return this.resultForAPICall(result);
    }

    public EZPlayer createPlayerWithDeviceSerial(String deviceSerial, int channelNo, int forceStreamType) {
        LogUtil.i(TAG, "Enter createPlayerWithDeviceSerial: ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            return null;
        }
        if (channelNo < 0) {
            LogUtil.i(TAG, "createPlayerWithDeviceSerial, invalid parameters channelNo");
            return null;
        }
        EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(deviceSerial, channelNo, false);
        streamParamHelp.setStreamType(forceStreamType);
        return new EZPlayer(streamParamHelp);
    }

    public EZPlayer createPlayerWithDeviceSerial(String deviceSerial, int channelNo, boolean useSubStream) {
        LogUtil.i(TAG, "Enter createPlayerWithDeviceSerial: ");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            return null;
        }
        if (channelNo < 0) {
            LogUtil.i(TAG, "createPlayerWithDeviceSerial, invalid parameters channelNo");
            return null;
        }
        EZStreamParamHelp streamParamHelp = new EZStreamParamHelp(deviceSerial, channelNo, true);
        streamParamHelp.setUseSubStream(useSubStream);
        return new EZPlayer(streamParamHelp);
    }

    private CameraInfoEx getCameraInfoExFromEZPlayInfo(EZDevicePlayInfo info, int channelNo) {
        CameraInfoEx cameraInfoEx = new CameraInfoEx();
        cameraInfoEx.setDeviceSN(info.getDeviceSerial());
        cameraInfoEx.setChannelNo(channelNo);
        return cameraInfoEx;
    }

    private EZStreamLimitInfo getStreamLimitInfo() throws BaseException {
        LogUtil.i(TAG, "Enter getStreamLimitInfo: ");
        if (Config.ENABLE_STUB && this.streamStub) {
            LogUtil.i(TAG, "getStreamLimitInfo: Enable stub !!!");
            String filePath = "/sdcard/videogo_test_cfg";
            HashMap<String, String> mMap = new HashMap<String, String>();
            Utils.parseTestConfigFile("/sdcard/videogo_test_cfg", mMap);
            String streamType = (String)mMap.get("streamType");
            String limitTime = (String)mMap.get("limitTime");
            String streamTimeLimitSwitch = (String)mMap.get("streamTimeLimitSwitch");
            int stream_type = 0;
            int limit_time = 0;
            int limit_switch = 0;
            try {
                stream_type = Integer.parseInt(streamType);
                limit_time = Integer.parseInt(limitTime);
                limit_switch = Integer.parseInt(streamTimeLimitSwitch);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
            EZStreamLimitInfo info = new EZStreamLimitInfo();
            EZStreamLimitInfo.StreamLimitInfoEntity streamDetail = new EZStreamLimitInfo.StreamLimitInfoEntity();
            info.setStreamType(stream_type);
            streamDetail.setLimitTime(limit_time);
            streamDetail.setStreamTimeLimitSwitch(limit_switch);
            info.setStreamLimitInfo(streamDetail);
            LogUtil.i(TAG, "getStreamLimitInfo: " + info);
            return info;
        }
        String urlString = "/api/stream/getSwitchInfo";
        BaseInfo baseInfo = new BaseInfo(){};
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean isOk = this.parseCode(reponse);
                if (isOk && (dataObj = (resultObj = (rootObj = new JSONObject(reponse)).getJSONObject("result")).optJSONObject("data")) != null) {
                    EZStreamLimitInfo info = new EZStreamLimitInfo();
                    EZStreamLimitInfo.StreamLimitInfoEntity streamDetail = new EZStreamLimitInfo.StreamLimitInfoEntity();
                    ReflectionUtils.convJSONToObject(dataObj, info);
                    JSONObject detailStreamObj = dataObj.getJSONObject("streamLimitInfo");
                    if (detailStreamObj != null) {
                        ReflectionUtils.convJSONToObject(detailStreamObj, streamDetail);
                        info.setStreamLimitInfo(streamDetail);
                    }
                    return info;
                }
                return null;
            }
        });
        if (result != null) {
            EZStreamLimitInfo info = (EZStreamLimitInfo)result;
            LogUtil.d(TAG, "getStreamLimitInfo: " + info);
            return info;
        }
        return null;
    }

    public void refreshNetwork() {
        boolean isNetworkAvailable = ConnectionDetector.isNetworkAvailable((Context)mApplication);
        if (isNetworkAvailable) {
            LocalInfo localInfo = LocalInfo.getInstance();
            Context context = localInfo.getContext();
            if (localInfo.getIsLogin()) {
                LogUtil.d(TAG, "\u7f51\u7edc\u72b6\u6001\u6539\u53d8");
                String wifiMacAddress = ConnectionDetector.getWifiMacAddress(context);
                if (!TextUtils.equals((CharSequence)wifiMacAddress, (CharSequence)this.mAppManager.getWifiMacAddress())) {
                    this.mAppManager.setWifiMacAddress(wifiMacAddress);
                    DeviceManager.getInstance().clearPreConncetInfo();
                    DeviceManager.getInstance().clearDevicePlayType();
                    new Thread(){

                        @Override
                        public void run() {
                            LogUtil.d(PlayAPI.TAG, "onReceive start getServerInfo");
                            try {
                                PlayAPI.this.mAppManager.refreshNetInfo();
                            }
                            catch (BaseException e) {
                                LogUtil.printErrStackTrace(PlayAPI.TAG, e.fillInStackTrace());
                            }
                        }
                    }.start();
                }
            }
            this.setNetType(Utils.getNetTypeName((Context)mApplication));
        } else {
            DeviceManager.getInstance().clearPreConncetInfo();
            DeviceManager.getInstance().clearDevicePlayType();
        }
    }

    private String getCommaSeprateStrFromList(List<String> msgIdList) {
        if (msgIdList == null || msgIdList.size() <= 0) {
            return null;
        }
        if (msgIdList != null) {
            LogUtil.i(TAG, " msgIdList size:" + msgIdList.size());
            if (msgIdList.size() == 0) {
                return null;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (msgIdList.size() == 1) {
            sb.append(msgIdList.get(0));
        } else {
            sb.append(msgIdList.get(0));
            for (int i = 1; i < msgIdList.size(); ++i) {
                sb.append(",").append(msgIdList.get(i));
            }
        }
        return sb.toString();
    }

    public List<EZDeviceInfo> getSharedDeviceList(final int pageIndex, final int pageSize) throws BaseException {
        LogUtil.i(TAG, "Enter getSharedDeviceList: ");
        if (pageIndex < 0 || pageSize <= 0) {
            LogUtil.i(TAG, "getSharedDeviceList: invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (pageIndex == 0) {
            DeviceManager.getInstance().clearDevice();
            CameraManager.getInstance().clearCamera();
        }
        String urlString = "/api/device/sharelist";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="pageSize")
            private int mPageSize;
            @HttpParam(name="pageStart")
            private int mPageStart;
            @HttpParam(name="version")
            private String mVersion;
            {
                this.mPageSize = pageSize;
                this.mPageStart = pageIndex;
                this.mVersion = "2.0";
            }
        };
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(reponse);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONArray dataArrayObj = resultObj.optJSONArray("data");
                    ArrayList<EZDeviceInfo> retList = new ArrayList<EZDeviceInfo>();
                    if (dataArrayObj != null) {
                        int len = dataArrayObj.length();
                        for (int i = 0; i < len; ++i) {
                            EZDeviceInfo info = new EZDeviceInfo();
                            JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(jsonObj, info);
                            JSONArray cameraInfoArrayObj = jsonObj.optJSONArray("cameraInfo");
                            if (cameraInfoArrayObj != null && cameraInfoArrayObj.length() > 0) {
                                ArrayList<EZCameraInfo> ezCameraInfos = new ArrayList<EZCameraInfo>();
                                int len_cameraInfos = cameraInfoArrayObj.length();
                                for (int y = 0; y < len_cameraInfos; ++y) {
                                    EZCameraInfo ezCameraInfo = new EZCameraInfo();
                                    JSONObject jsonObject = cameraInfoArrayObj.getJSONObject(y);
                                    ReflectionUtils.convJSONToObject(jsonObject, ezCameraInfo);
                                    JSONArray cameraInfoVideoQualityArrayObj = jsonObject.optJSONArray("videoQualityInfos");
                                    if (cameraInfoVideoQualityArrayObj != null && cameraInfoVideoQualityArrayObj.length() > 0) {
                                        ArrayList<EZVideoQualityInfo> ezVideoQualityInfos = new ArrayList<EZVideoQualityInfo>();
                                        int len_ezVideoQualityInfos = cameraInfoVideoQualityArrayObj.length();
                                        for (int t = 0; t < len_ezVideoQualityInfos; ++t) {
                                            EZVideoQualityInfo ezVideoQualityInfo = new EZVideoQualityInfo();
                                            JSONObject jsonObject_videoquality = cameraInfoVideoQualityArrayObj.getJSONObject(t);
                                            ReflectionUtils.convJSONToObject(jsonObject_videoquality, ezVideoQualityInfo);
                                            ezVideoQualityInfos.add(ezVideoQualityInfo);
                                        }
                                        ezCameraInfo.setVideoQualityInfos(ezVideoQualityInfos);
                                    }
                                    ezCameraInfos.add(ezCameraInfo);
                                }
                                if (ezCameraInfos.size() > 0) {
                                    info.setCameraInfoList(ezCameraInfos);
                                }
                            }
                            retList.add(info);
                        }
                    }
                    return retList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            for (EZDeviceInfo info : ret) {
                LogUtil.d(TAG, "getDeviceList: " + info);
            }
            return ret;
        }
        return null;
    }

    public List<EZDeviceInfo> getDeviceList(final int pageIndex, final int ezpageSize, boolean isSupportSubDevice) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceList: ");
        if (pageIndex < 0 || ezpageSize <= 0) {
            LogUtil.i(TAG, "getDeviceList: invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = isSupportSubDevice ? "/api/service/sdk/device/list" : "/api/device/list";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="pageSize")
            private int pageSize;
            @HttpParam(name="pageStart")
            private int pageStart;
            @HttpParam(name="version")
            private String version;
            {
                this.pageSize = ezpageSize;
                this.pageStart = pageIndex;
                this.version = this.getApiVersion();
            }

            @Override
            public String getApiVersion() {
                if (PlayAPI.this.isUsingGlobalSDK()) {
                    return "4.0";
                }
                return "2.0";
            }
        };
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(reponse);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONArray dataArrayObj = resultObj.optJSONArray("data");
                    ArrayList<EZDeviceInfo> retList = new ArrayList<EZDeviceInfo>();
                    if (dataArrayObj != null) {
                        int len = dataArrayObj.length();
                        for (int i = 0; i < len; ++i) {
                            EZDeviceInfo info = new EZDeviceInfo();
                            JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(jsonObj, info);
                            PlayAPI.this.parseCameraInfo(jsonObj, info);
                            PlayAPI.this.parseDetectorInfo(jsonObj, info);
                            PlayAPI.this.parseSubDeviceInfo(jsonObj, info);
                            retList.add(info);
                        }
                    }
                    return retList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            for (EZDeviceInfo info : ret) {
                LogUtil.d(TAG, "getDeviceList: " + new Gson().toJson((Object)info));
            }
            return ret;
        }
        return null;
    }

    private void parseCameraInfo(JSONObject jsonObj, EZDeviceInfo info) throws JSONException {
        JSONArray cameraInfoArrayObj = jsonObj.optJSONArray("cameraInfo");
        if (cameraInfoArrayObj != null && cameraInfoArrayObj.length() > 0) {
            ArrayList<EZCameraInfo> ezCameraInfos = new ArrayList<EZCameraInfo>();
            int len_cameraInfos = cameraInfoArrayObj.length();
            for (int y = 0; y < len_cameraInfos; ++y) {
                EZCameraInfo ezCameraInfo = new EZCameraInfo();
                JSONObject jsonObject = cameraInfoArrayObj.getJSONObject(y);
                ReflectionUtils.convJSONToObject(jsonObject, ezCameraInfo);
                JSONArray cameraInfoVideoQualityArrayObj = jsonObject.optJSONArray("videoQualityInfos");
                if (cameraInfoVideoQualityArrayObj != null && cameraInfoVideoQualityArrayObj.length() > 0) {
                    ArrayList<EZVideoQualityInfo> ezVideoQualityInfos = new ArrayList<EZVideoQualityInfo>();
                    int len_ezVideoQualityInfos = cameraInfoVideoQualityArrayObj.length();
                    for (int t = 0; t < len_ezVideoQualityInfos; ++t) {
                        EZVideoQualityInfo ezVideoQualityInfo = new EZVideoQualityInfo();
                        JSONObject jsonObject_videoquality = cameraInfoVideoQualityArrayObj.getJSONObject(t);
                        ReflectionUtils.convJSONToObject(jsonObject_videoquality, ezVideoQualityInfo);
                        ezVideoQualityInfos.add(ezVideoQualityInfo);
                    }
                    ezCameraInfo.setVideoQualityInfos(ezVideoQualityInfos);
                }
                ezCameraInfos.add(ezCameraInfo);
            }
            if (ezCameraInfos.size() > 0) {
                info.setCameraInfoList(ezCameraInfos);
            }
        }
    }

    private void parseSubDeviceInfo(JSONObject jsonObj, EZDeviceInfo info) throws JSONException {
        JSONArray subDeviceInfoArrayObj = jsonObj.optJSONArray("subDeviceInfo");
        if (subDeviceInfoArrayObj != null && subDeviceInfoArrayObj.length() > 0) {
            ArrayList<EZSubDeviceInfo> ezSubDeviceInfos = new ArrayList<EZSubDeviceInfo>();
            int len_cameraInfos = subDeviceInfoArrayObj.length();
            for (int y = 0; y < len_cameraInfos; ++y) {
                EZSubDeviceInfo ezSubDeviceInfo = new EZSubDeviceInfo();
                JSONObject jsonObject = subDeviceInfoArrayObj.getJSONObject(y);
                ReflectionUtils.convJSONToObject(jsonObject, ezSubDeviceInfo);
                JSONArray cameraInfoVideoQualityArrayObj = jsonObject.optJSONArray("videoQualityInfos");
                if (cameraInfoVideoQualityArrayObj != null && cameraInfoVideoQualityArrayObj.length() > 0) {
                    ArrayList<EZVideoQualityInfo> ezVideoQualityInfos = new ArrayList<EZVideoQualityInfo>();
                    int len_ezVideoQualityInfos = cameraInfoVideoQualityArrayObj.length();
                    for (int t = 0; t < len_ezVideoQualityInfos; ++t) {
                        EZVideoQualityInfo ezVideoQualityInfo = new EZVideoQualityInfo();
                        JSONObject jsonObject_videoquality = cameraInfoVideoQualityArrayObj.getJSONObject(t);
                        ReflectionUtils.convJSONToObject(jsonObject_videoquality, ezVideoQualityInfo);
                        ezVideoQualityInfos.add(ezVideoQualityInfo);
                    }
                    ezSubDeviceInfo.setVideoQualityInfos(ezVideoQualityInfos);
                }
                ezSubDeviceInfos.add(ezSubDeviceInfo);
            }
            if (ezSubDeviceInfos.size() > 0) {
                info.setSubDeviceInfoList(ezSubDeviceInfos);
            }
        }
    }

    private void parseDetectorInfo(JSONObject jsonObj, EZDeviceInfo info) throws JSONException {
        JSONArray detectorInfoArrayObj = jsonObj.optJSONArray("detectorInfo");
        if (detectorInfoArrayObj != null && detectorInfoArrayObj.length() > 0) {
            ArrayList<EZDetectorInfo> ezDetectorInfos = new ArrayList<EZDetectorInfo>();
            int len_cameraInfos = detectorInfoArrayObj.length();
            for (int y = 0; y < len_cameraInfos; ++y) {
                EZDetectorInfo ezDetectorInfo = new EZDetectorInfo();
                JSONObject jsonObject = detectorInfoArrayObj.getJSONObject(y);
                ReflectionUtils.convJSONToObject(jsonObject, ezDetectorInfo);
                ezDetectorInfos.add(ezDetectorInfo);
            }
            if (ezDetectorInfos.size() > 0) {
                info.setDetectorInfoList(ezDetectorInfos);
            }
        }
    }

    public List<EZDeviceInfo> getTrustDeviceList(final int pageIndex, final int ezpageSize) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceList: ");
        if (pageIndex < 0 || ezpageSize <= 0) {
            LogUtil.i(TAG, "getDeviceList: invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseSdkRuntimeException(STR_NATIVE_PARAM_ERROR);
        }
        if (pageIndex == 0) {
            // empty if block
        }
        Object result = null;
        String urlString = "/api/lapp/trust/device/list";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="pageSize")
            private int pageSize;
            @HttpParam(name="pageStart")
            private int pageStart;
            @HttpParam(name="version")
            private String version;
            {
                this.pageSize = ezpageSize;
                this.pageStart = pageIndex;
                this.version = "2.0";
            }
        };
        baseInfo.fixHttpToken();
        result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(reponse);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONArray dataArrayObj = resultObj.optJSONArray("data");
                    ArrayList<EZDeviceInfo> retList = new ArrayList<EZDeviceInfo>();
                    if (dataArrayObj != null) {
                        int len = dataArrayObj.length();
                        for (int i = 0; i < len; ++i) {
                            JSONArray detectorInfoArrayObj;
                            EZDeviceInfo info = new EZDeviceInfo();
                            JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(jsonObj, info);
                            JSONArray cameraInfoArrayObj = jsonObj.optJSONArray("cameraInfo");
                            if (cameraInfoArrayObj != null && cameraInfoArrayObj.length() > 0) {
                                ArrayList<EZCameraInfo> ezCameraInfos = new ArrayList<EZCameraInfo>();
                                int len_cameraInfos = cameraInfoArrayObj.length();
                                for (int y = 0; y < len_cameraInfos; ++y) {
                                    EZCameraInfo ezCameraInfo = new EZCameraInfo();
                                    JSONObject jsonObject = cameraInfoArrayObj.getJSONObject(y);
                                    ReflectionUtils.convJSONToObject(jsonObject, ezCameraInfo);
                                    JSONArray cameraInfoVideoQualityArrayObj = jsonObject.optJSONArray("videoQualityInfos");
                                    if (cameraInfoVideoQualityArrayObj != null && cameraInfoVideoQualityArrayObj.length() > 0) {
                                        ArrayList<EZVideoQualityInfo> ezVideoQualityInfos = new ArrayList<EZVideoQualityInfo>();
                                        int len_ezVideoQualityInfos = cameraInfoVideoQualityArrayObj.length();
                                        for (int t = 0; t < len_ezVideoQualityInfos; ++t) {
                                            EZVideoQualityInfo ezVideoQualityInfo = new EZVideoQualityInfo();
                                            JSONObject jsonObject_videoquality = cameraInfoVideoQualityArrayObj.getJSONObject(t);
                                            ReflectionUtils.convJSONToObject(jsonObject_videoquality, ezVideoQualityInfo);
                                            ezVideoQualityInfos.add(ezVideoQualityInfo);
                                        }
                                        ezCameraInfo.setVideoQualityInfos(ezVideoQualityInfos);
                                    }
                                    ezCameraInfos.add(ezCameraInfo);
                                }
                                if (ezCameraInfos.size() > 0) {
                                    info.setCameraInfoList(ezCameraInfos);
                                }
                            }
                            if ((detectorInfoArrayObj = jsonObj.optJSONArray("detectorInfo")) != null && detectorInfoArrayObj.length() > 0) {
                                ArrayList<EZDetectorInfo> ezDetectorInfos = new ArrayList<EZDetectorInfo>();
                                int len_cameraInfos = detectorInfoArrayObj.length();
                                for (int y = 0; y < len_cameraInfos; ++y) {
                                    EZDetectorInfo ezDetectorInfo = new EZDetectorInfo();
                                    JSONObject jsonObject = detectorInfoArrayObj.getJSONObject(y);
                                    ReflectionUtils.convJSONToObject(jsonObject, ezDetectorInfo);
                                    ezDetectorInfos.add(ezDetectorInfo);
                                }
                                if (ezDetectorInfos.size() > 0) {
                                    info.setDetectorInfoList(ezDetectorInfos);
                                }
                            }
                            retList.add(info);
                        }
                    }
                    return retList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            for (EZDeviceInfo info : ret) {
                LogUtil.d(TAG, "getDeviceList: " + new Gson().toJson((Object)info));
            }
            return ret;
        }
        return null;
    }

    public List<EZAlarmInfo> getAlarmList(String deviceSerial, final int pageIndex, final int pageSize, Calendar beginTime, Calendar endTime) throws BaseException {
        LogUtil.i(TAG, "Enter getAlarmList: ");
        if (pageIndex < 0 || pageSize <= 0) {
            LogUtil.i(TAG, "getAlarmListBySerial, invalid parameters page");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (beginTime != null && endTime != null && beginTime.after(endTime)) {
            LogUtil.i(TAG, "getAlarmListBySerial, invalid parameters, begin after end time");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/alarm/getList";
        String serial = "";
        serial = TextUtils.isEmpty((CharSequence)deviceSerial) ? "" : deviceSerial;
        final String devSerial = serial;
        String begin = beginTime == null ? "" : DateTimeUtil.calendarToYMDHMSString(beginTime);
        String end = endTime == null ? "" : DateTimeUtil.calendarToYMDHMSString(endTime);
        final String beginT = begin;
        final String endT = end;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="pageStart")
            private int mPageIndex;
            @HttpParam(name="pageSize")
            private int mPageSize;
            @HttpParam(name="startTime")
            private String mBeginTime;
            @HttpParam(name="endTime")
            private String mEndTime;
            @HttpParam(name="alarmType")
            private int mAlarmType;
            @HttpParam(name="status")
            private int mStatus;
            {
                this.mDeviceSerial = devSerial;
                this.mPageIndex = pageIndex;
                this.mPageSize = pageSize;
                this.mBeginTime = beginT;
                this.mEndTime = endT;
                this.mAlarmType = -1;
                this.mStatus = 2;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(reponse);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONArray dataArrayObj = resultObj.optJSONArray("data");
                    ArrayList<EZAlarmInfo> retList = new ArrayList<EZAlarmInfo>();
                    if (dataArrayObj != null && dataArrayObj.length() > 0) {
                        int size = dataArrayObj.length();
                        for (int i = 0; i < size; ++i) {
                            EZAlarmInfo info = new EZAlarmInfo();
                            JSONObject jsonObj = dataArrayObj.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(jsonObj, info);
                            retList.add(info);
                        }
                    }
                    return retList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            if (ret.size() > 0) {
                for (EZAlarmInfo info : ret) {
                    LogUtil.d(TAG, "getAlarmListBySerial returns: " + new Gson().toJson((Object)info));
                }
            }
            return ret;
        }
        return null;
    }

    public boolean setDefence(final String deviceSerial, final EZConstants.EZDefenceStatus defence) throws BaseException {
        LogUtil.i(TAG, "Enter setDefence");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/device/updateDefence";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="isDefence")
            private int bDefence;
            {
                this.devSerial = deviceSerial;
                this.bDefence = defence.getStatus();
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                return this.parseCode(reponse);
            }
        });
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "setDeviceDefence return " + ret);
        return ret;
    }

    public boolean isSupportTalkVoicePermission(String deviceSerial, int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter isSupportTalkVoicePermission");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/service/media/talk/voice/permission/status";
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.fixAppName2Empty();
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        String url = urlString + "?deviceSerial=" + deviceSerial + "&localIndex=" + cameraNo;
        Object result = RestfulUtils.getInstance().get(url, baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException {
                return this.parseMetaCode(response);
            }
        }, true);
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "isSupportTalkVoicePermission return " + ret);
        return ret;
    }

    public void refreshDeviceDetailInfoEx(String deviceSerial, int cameraNo) throws BaseException {
        DeviceManager.getInstance().getDeviceInfoExFromOnlineToLocal(deviceSerial, cameraNo);
    }

    public EZSDKConfiguration getConfiguration() throws BaseException {
        LogUtil.i(TAG, "Enter getConfiguration: ");
        String urlString = "/api/config/info";
        BaseInfo baseInfo = new BaseInfo(){};
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject dataObj;
                boolean isOk = this.parseCode(response);
                if (isOk && (dataObj = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    EZSDKConfiguration config = new EZSDKConfiguration();
                    ReflectionUtils.convJSONToObject(dataObj, config);
                    JSONObject streamLimitInfoObj = dataObj.getJSONObject("streamLimitInfo");
                    if (streamLimitInfoObj != null) {
                        EZSDKConfiguration.StreamLimitInfoEntity entity = new EZSDKConfiguration.StreamLimitInfoEntity();
                        ReflectionUtils.convJSONToObject(streamLimitInfoObj, entity);
                        config.setStreamLimitInfo(entity);
                    }
                    return config;
                }
                return null;
            }
        });
        if (result != null) {
            EZSDKConfiguration ret = (EZSDKConfiguration)result;
            LogUtil.d(TAG, new Gson().toJson((Object)ret));
            return ret;
        }
        return null;
    }

    public EZDeviceUpgradeStatus getDeviceUpgradeStatus_stub(String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceUpgradeStatus_stub");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (!Config.ENABLE_STUB) {
            LogUtil.i(TAG, "getDeviceUpgradeStatus_stub: Config.ENABLE_STUB not open");
            return null;
        }
        EZDeviceUpgradeStatus status = new EZDeviceUpgradeStatus();
        if (this.mStartUpgrade == 0) {
            status.setUpgradeStatus(-1);
            status.setUpgradeProgress(0);
            return status;
        }
        Random rand = new Random();
        int n = rand.nextInt(10);
        this.mUpgradeProgress += n;
        if (this.mUpgradeProgress >= 100) {
            status.setUpgradeProgress(100);
            status.setUpgradeStatus(2);
            this.mStartUpgrade = 0;
            this.mUpgradeProgress = 0;
        } else {
            status.setUpgradeStatus(0);
            status.setUpgradeProgress(this.mUpgradeProgress);
        }
        return status;
    }

    public void upgradeDevice_stub(String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter upgradeDevice_stub");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (!Config.ENABLE_STUB) {
            LogUtil.i(TAG, "getDeviceUpgradeStatus_stub: Config.ENABLE_STUB not open");
            return;
        }
        this.mStartUpgrade = 1;
    }

    public List<String> getStreamTokenList() throws BaseException {
        List tokenList = null;
        BatchGetTokens batchGetTokens = new BatchGetTokens();
        batchGetTokens.setCount(100);
        tokenList = (List)this.mRestfulUtils.postData(new BatchGetTokensReq().buidParams(batchGetTokens), "/api/user/token", new BatchGetTokensResp());
        return tokenList;
    }

    public DeviceInfoEx getDeviceInfoEx(String deviceSerial, int cameraNo) throws BaseException {
        return this.getDeviceInfoEx(null, null, deviceSerial, cameraNo, null);
    }

    public DeviceInfoEx getDeviceInfoEx(String bizType, String platformId, String deviceSerial, int cameraNo, String cameraNoStr) throws BaseException {
        String urlString = "/api/device/detail";
        final String bitTypeStr = bizType;
        final String deviceSerialStr = deviceSerial;
        final int cameraNoInt = cameraNo;
        final String cameraNoSz = cameraNoStr;
        final String platformIdStr = platformId;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="bizType")
            private String bizType;
            @HttpParam(name="deviceSerial")
            private String deviceSerial;
            @HttpParam(name="platFormId")
            private String platformId;
            @HttpParam(name="cameraNo")
            private String cameraNo;
            @HttpParam(name="version")
            private String version;
            @HttpParam(name="isNewVersion")
            private String isNewVersion;
            {
                this.bizType = bitTypeStr;
                this.deviceSerial = deviceSerialStr;
                this.platformId = platformIdStr;
                this.cameraNo = cameraNoSz != null ? cameraNoSz : String.valueOf(cameraNoInt);
                this.version = "4.0";
                this.isNewVersion = "true";
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject jsonObject;
                boolean ret = this.parseCode(response);
                DeviceInfoEx deviceInfoEx = null;
                DeviceInfoEx belongDeviceinfoEx = null;
                CameraInfoEx cameraInfoEx = null;
                if (ret && (jsonObject = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    JSONObject cameraJs;
                    JSONObject devicejs = jsonObject.optJSONObject("device");
                    if (devicejs != null) {
                        JSONArray vtmBackupArrayObj;
                        deviceInfoEx = new DeviceInfoEx();
                        ReflectionUtils.convJSONToObject(devicejs, deviceInfoEx);
                        deviceInfoEx.setPublicKeyVersion(devicejs.optInt("version"));
                        JSONObject belongdevicejs = devicejs.optJSONObject("belongDevice");
                        if (belongdevicejs != null) {
                            belongDeviceinfoEx = new DeviceInfoEx();
                            ReflectionUtils.convJSONToObject(belongdevicejs, belongDeviceinfoEx);
                            deviceInfoEx.setBelongDevice(belongDeviceinfoEx);
                        }
                        if ((vtmBackupArrayObj = devicejs.optJSONArray("vtmBackups")) != null && vtmBackupArrayObj.length() > 0) {
                            ArrayList<EZVtmBackupInfo> vtmBackupInfos = new ArrayList<EZVtmBackupInfo>();
                            int len_vtmBackupInfos = vtmBackupArrayObj.length();
                            for (int i = 0; i < len_vtmBackupInfos; ++i) {
                                EZVtmBackupInfo ezVtmBackupInfo = new EZVtmBackupInfo();
                                JSONObject jsonObject_vtmBackup = vtmBackupArrayObj.getJSONObject(i);
                                ReflectionUtils.convJSONToObject(jsonObject_vtmBackup, ezVtmBackupInfo);
                                vtmBackupInfos.add(ezVtmBackupInfo);
                            }
                            deviceInfoEx.vtmBackups = vtmBackupInfos;
                        }
                    }
                    if (deviceInfoEx != null && (cameraJs = jsonObject.optJSONObject("camera")) != null) {
                        cameraInfoEx = new CameraInfoEx();
                        ReflectionUtils.convJSONToObject(cameraJs, cameraInfoEx);
                        JSONArray cameraInfoVideoQualityArrayObj = cameraJs.optJSONArray("videoQualityInfos");
                        if (cameraInfoVideoQualityArrayObj != null && cameraInfoVideoQualityArrayObj.length() > 0) {
                            ArrayList<EZVideoQualityInfo> ezVideoQualityInfos = new ArrayList<EZVideoQualityInfo>();
                            int len_ezVideoQualityInfos = cameraInfoVideoQualityArrayObj.length();
                            for (int t = 0; t < len_ezVideoQualityInfos; ++t) {
                                EZVideoQualityInfo ezVideoQualityInfo = new EZVideoQualityInfo();
                                JSONObject jsonObject_videoquality = cameraInfoVideoQualityArrayObj.getJSONObject(t);
                                ReflectionUtils.convJSONToObject(jsonObject_videoquality, ezVideoQualityInfo);
                                ezVideoQualityInfos.add(ezVideoQualityInfo);
                            }
                            cameraInfoEx.videoQualityInfos = ezVideoQualityInfos;
                        }
                        deviceInfoEx.setCameraInfoEx(cameraInfoEx);
                    }
                }
                return deviceInfoEx;
            }
        });
        if (result != null) {
            LogUtil.d(TAG, new Gson().toJson(result));
            return (DeviceInfoEx)result;
        }
        return null;
    }

    public boolean setDeviceVideoLevel(final String deviceSerial, final int cameraNo, final int videoLevel) throws BaseException {
        CameraInfoEx cameraInfoEx;
        LogUtil.i(TAG, "Enter setDeviceVideoLevel");
        String urlString = "/api/device/setVideoLevel";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="cameraNo")
            private int mCameraNo;
            @HttpParam(name="videoLevel")
            private int mVideoLevel;
            @HttpParam(name="version")
            private String version;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
                this.mVideoLevel = videoLevel;
                this.version = "2.0";
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseCode(response);
                return ret;
            }
        });
        boolean ret = this.resultForAPICall(result);
        if (ret && (cameraInfoEx = CameraManager.getInstance().getAddedCamera(deviceSerial, cameraNo)) != null) {
            cameraInfoEx.setVideoLevel(videoLevel);
        }
        return ret;
    }

    public boolean setDeviceVideoLevelAuto(String deviceSerial, int cameraNo, final int videoLevel) throws BaseException {
        CameraInfoEx cameraInfoEx;
        LogUtil.i(TAG, "Enter setDeviceVideoLevelAuto");
        String urlString = "/api/v3/device/setVideoLevel";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="videoLevel")
            private int mVideoLevel;
            @HttpParam(name="clientLid")
            private String mClientLid;
            {
                this.mVideoLevel = videoLevel;
                this.mClientLid = LocalInfo.getInstance().getHardwareCode();
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        BaseHeaderInfo baseHeaderInfo = new BaseHeaderInfo();
        baseHeaderInfo.setAccessToken(baseInfo.getAccessToken());
        baseHeaderInfo.setDeviceSerial(deviceSerial);
        baseHeaderInfo.setLocalIndex(String.valueOf(cameraNo));
        Object result = this.mRestfulUtils.post(baseHeaderInfo, baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseMetaCode(response);
                return ret;
            }
        });
        boolean ret = this.resultForAPICall(result);
        if (ret && (cameraInfoEx = CameraManager.getInstance().getAddedCamera(deviceSerial, cameraNo)) != null) {
            cameraInfoEx.setVideoLevel(videoLevel);
        }
        return ret;
    }

    public EZDeviceInfo getDeviceInfo(String deviceSerial, boolean isSupportSubDevice) throws BaseException {
        LogUtil.i(TAG, "Enter getDeviceInfo");
        final String devSerial = deviceSerial;
        Object result = null;
        String urlString = isSupportSubDevice ? "/api/service/sdk/device/info" : "/api/sdk/device/info";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="version")
            private String version = this.getApiVersion();
            @HttpParam(name="deviceSerial")
            private String deviceSerial = devSerial;

            @Override
            public String getApiVersion() {
                if (PlayAPI.this.isUsingGlobalSDK()) {
                    return "2.0";
                }
                return null;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCode(reponse);
                if (isOk) {
                    JSONObject rootObj = new JSONObject(reponse);
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    JSONObject jsonObj = resultObj.optJSONObject("data");
                    EZDeviceInfo info = null;
                    if (jsonObj != null) {
                        info = new EZDeviceInfo();
                        ReflectionUtils.convJSONToObject(jsonObj, info);
                        PlayAPI.this.parseCameraInfo(jsonObj, info);
                        PlayAPI.this.parseDetectorInfo(jsonObj, info);
                        PlayAPI.this.parseSubDeviceInfo(jsonObj, info);
                    }
                    return info;
                }
                return null;
            }
        });
        if (result != null) {
            LogUtil.d(TAG, "getDeviceInfo returns: " + new Gson().toJson(result));
            return (EZDeviceInfo)result;
        }
        return null;
    }

    public void clearStreamInfoCache() {
        DeviceManager.getInstance().clearDevice();
        CameraManager.getInstance().clearCamera();
    }

    public EZHiddnsDeviceInfo getDDNSWithDeviceSerial(final String deviceSerial, final String domain) throws BaseException {
        LogUtil.i(TAG, "Enter getDDNSWithDeviceSerial");
        if (TextUtils.isEmpty((CharSequence)deviceSerial) && TextUtils.isEmpty((CharSequence)domain)) {
            LogUtil.i(TAG, "deviceSerial and domain cannot be empty at the same time");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 380168);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        EZHiddnsDeviceInfo ezHiddnsDeviceInfo = (EZHiddnsDeviceInfo)this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="domain")
            private String mDomain;
            {
                this.devSerial = deviceSerial;
                this.mDomain = domain;
            }
        }, "/api/lapp/ddns/get", new ApiResponse(){

            @Override
            public EZHiddnsDeviceInfo parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return null;
                }
                if (isOk) {
                    JSONObject rootObject = new JSONObject(reponse);
                    JSONObject resultObject = rootObject.optJSONObject("data");
                    EZHiddnsDeviceInfo ezHiddnsDeviceInfo = null;
                    if (resultObject != null) {
                        ezHiddnsDeviceInfo = new EZHiddnsDeviceInfo();
                        ReflectionUtils.convJSONToObject(resultObject, ezHiddnsDeviceInfo);
                    }
                    return ezHiddnsDeviceInfo;
                }
                return null;
            }
        });
        if (ezHiddnsDeviceInfo != null) {
            LogUtil.d(TAG, "getDDNSWithDeviceSerial returns: " + new Gson().toJson((Object)ezHiddnsDeviceInfo));
        }
        return ezHiddnsDeviceInfo;
    }

    public void setDeviceDoamin(final String deviceSerial, final String domain) throws BaseException {
        LogUtil.i(TAG, "Enter setDeviceDoamin");
        if (TextUtils.isEmpty((CharSequence)deviceSerial) || TextUtils.isEmpty((CharSequence)domain)) {
            LogUtil.i(TAG, "deviceSerial and domain cannot be empty");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 380168);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="domain")
            private String mDomain;
            {
                this.devSerial = deviceSerial;
                this.mDomain = domain;
            }
        }, "/api/lapp/ddns/setDomain", new ApiResponse(){

            @Override
            public Boolean parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return false;
                }
                if (isOk) {
                    return true;
                }
                return false;
            }
        });
    }

    public void setDDNSAutomatic(final String deviceSerial) throws BaseException {
        LogUtil.i(TAG, "Enter setDDNSAutomatic");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            {
                this.devSerial = deviceSerial;
            }
        }, "/api/lapp/ddns/mode/setAutomatic", new ApiResponse(){

            @Override
            public Boolean parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return false;
                }
                if (isOk) {
                    return true;
                }
                return false;
            }
        });
    }

    public void setDDNSManual(final String deviceSerial, final int cmdPort, final int httpPort) throws BaseException {
        LogUtil.i(TAG, "Enter setDDNSManual");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="cmdPort")
            private int cmdport;
            @HttpParam(name="httpPort")
            private int httpport;
            {
                this.devSerial = deviceSerial;
                this.cmdport = cmdPort;
                this.httpport = httpPort;
            }
        }, "/api/lapp/ddns/mode/setManual", new ApiResponse(){

            @Override
            public Boolean parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return false;
                }
                if (isOk) {
                    return true;
                }
                return false;
            }
        });
    }

    public List<EZHiddnsDeviceInfo> getDDNSDeviceList(final int pageSize, final int pageStart) throws BaseException {
        LogUtil.i(TAG, "Enter getDDNSDeviceList");
        Object object = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="pageSize")
            private int pagesize;
            @HttpParam(name="pageStart")
            private int pagestart;
            {
                this.pagesize = pageSize;
                this.pagestart = pageStart;
            }
        }, "/api/lapp/ddns/list", new ApiResponse(){

            @Override
            public List<EZHiddnsDeviceInfo> parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return null;
                }
                if (isOk) {
                    JSONObject rootObject = new JSONObject(reponse);
                    JSONArray resultObject = rootObject.optJSONArray("data");
                    ArrayList<EZHiddnsDeviceInfo> ezHiddnsDeviceInfos = new ArrayList<EZHiddnsDeviceInfo>();
                    if (resultObject != null && resultObject.length() > 0) {
                        int count = resultObject.length();
                        for (int y = 0; y < count; ++y) {
                            EZHiddnsDeviceInfo ezHiddnsDeviceInfo = new EZHiddnsDeviceInfo();
                            JSONObject jsonObject = resultObject.optJSONObject(y);
                            ReflectionUtils.convJSONToObject(jsonObject, ezHiddnsDeviceInfo);
                            ezHiddnsDeviceInfos.add(ezHiddnsDeviceInfo);
                        }
                    }
                    return ezHiddnsDeviceInfos;
                }
                return null;
            }
        });
        if (object != null) {
            List ezHiddnsDeviceInfos = (List)object;
            if (ezHiddnsDeviceInfos.size() > 0) {
                for (EZHiddnsDeviceInfo info : ezHiddnsDeviceInfos) {
                    LogUtil.d(TAG, "getDDNSDeviceList returns: " + new Gson().toJson((Object)info));
                }
            }
            return ezHiddnsDeviceInfos;
        }
        return null;
    }

    public void shareDDNSDeviceList(final String deviceSerial, final String account) throws BaseException {
        LogUtil.i(TAG, "Enter shareDDNSDeviceList");
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String devSerial;
            @HttpParam(name="account")
            private String account1;
            {
                this.devSerial = deviceSerial;
                this.account1 = account;
            }
        }, "/api/lapp/ddns/share", new ApiResponse(){

            @Override
            public Boolean parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return false;
                }
                if (isOk) {
                    return true;
                }
                return false;
            }
        });
    }

    public List<EZHiddnsDeviceInfo> getShareDDNSDeviceList(final int pageSize, final int pageStart) throws BaseException {
        LogUtil.i(TAG, "Enter getShareDDNSDeviceList");
        Object object = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="pageSize")
            private int pagesize;
            @HttpParam(name="pageStart")
            private int pagestart;
            {
                this.pagesize = pageSize;
                this.pagestart = pageStart;
            }
        }, "/api/lapp/ddns/share/list", new ApiResponse(){

            @Override
            public List<EZHiddnsDeviceInfo> parse(String reponse) throws BaseException, JSONException {
                boolean isOk = this.parseCodeHttp(reponse);
                if (!isOk) {
                    return null;
                }
                if (isOk) {
                    JSONObject rootObject = new JSONObject(reponse);
                    JSONArray resultObject = rootObject.optJSONArray("data");
                    ArrayList<EZHiddnsDeviceInfo> ezHiddnsDeviceInfos = new ArrayList<EZHiddnsDeviceInfo>();
                    if (resultObject != null && resultObject.length() > 0) {
                        int count = resultObject.length();
                        for (int y = 0; y < count; ++y) {
                            EZHiddnsDeviceInfo ezHiddnsDeviceInfo = new EZHiddnsDeviceInfo();
                            JSONObject jsonObject = resultObject.optJSONObject(y);
                            ReflectionUtils.convJSONToObject(jsonObject, ezHiddnsDeviceInfo);
                            ezHiddnsDeviceInfos.add(ezHiddnsDeviceInfo);
                        }
                    }
                    return ezHiddnsDeviceInfos;
                }
                return null;
            }
        });
        if (object != null) {
            List ezHiddnsDeviceInfos = (List)object;
            return ezHiddnsDeviceInfos;
        }
        return null;
    }

    public P2pUserInfo getP2pUserInfo() throws BaseException {
        String urlString = "/api/sdk/p2p/user/info/get";
        BaseInfo baseInfo = new BaseInfo(){};
        baseInfo.fixHttpToken();
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject jsonObject;
                boolean ret = this.parseCode(response);
                P2pUserInfo p2pUserInfo = null;
                if (ret && (jsonObject = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    p2pUserInfo = JsonTools.fromJson(jsonObject.toString(), P2pUserInfo.class);
                }
                return p2pUserInfo;
            }
        });
        try {
            return (P2pUserInfo)result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public P2pDeviceInfo getP2pDeviceInfo(final String deviceSerial) throws BaseException {
        String urlString = "/api/sdk/p2p/dev/info/get";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String deviceSerialStr;
            {
                this.deviceSerialStr = deviceSerial;
            }
        };
        baseInfo.fixDeviceToken(deviceSerial);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject jsonObject;
                boolean ret = this.parseCode(response);
                P2pDeviceInfo p2pDeviceInfo = null;
                if (ret && (jsonObject = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    p2pDeviceInfo = JsonTools.fromJson(jsonObject.toString(), P2pDeviceInfo.class);
                }
                return p2pDeviceInfo;
            }
        });
        try {
            return (P2pDeviceInfo)result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getP2pPermission(final String deviceSerial, final int cameraNo, final String playType) throws BaseException {
        String urlString = "/api/service/auth/device/user/permission";
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="cameraNo")
            private int mCameraNo;
            @HttpParam(name="permission")
            private String mPermission;
            {
                this.mDeviceSerial = deviceSerial;
                this.mCameraNo = cameraNo;
                this.mPermission = playType;
            }
        };
        baseInfo.fixDeviceVideoToken(deviceSerial, cameraNo);
        Object result = this.mRestfulUtils.post(baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject resultObj;
                JSONObject jsonObject;
                boolean ret = this.parseCode(response);
                if (ret && (jsonObject = (resultObj = (rootObj = new JSONObject(response)).getJSONObject("result")).optJSONObject("data")) != null) {
                    int hasPermission = jsonObject.getInt("hasPermission");
                    return hasPermission == 1;
                }
                return true;
            }
        });
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "getP2pPermission return " + ret);
        return ret;
    }

    public CloudTicketInfo getCloudTicketInfo(String deviceSerial, int channelNo) throws BaseException {
        String urlString = "/api/route/userdevicetob/v3/cameras/ticketInfo";
        String accessToken = LocalInfo.getInstance().getEZAccesstoken().getDeviceVideoToken(deviceSerial, channelNo);
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("deviceSerial=").append(deviceSerial).append("&").append("channelNo=").append(channelNo).append("&").append("accessToken=").append(accessToken);
        return (CloudTicketInfo)this.get(urlBuilder.toString(), new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject neededJson;
                boolean ret = this.parseCode(response);
                CloudTicketInfo ticketInfo = null;
                if (ret && (neededJson = (rootObj = new JSONObject(response)).optJSONObject("ticketInfo")) != null) {
                    ticketInfo = new CloudTicketInfo();
                    ReflectionUtils.convJSONToObject(neededJson, ticketInfo);
                }
                return ticketInfo;
            }

            @Override
            public boolean parseCode(String response) throws BaseException {
                boolean isSuc;
                block5: {
                    isSuc = false;
                    try {
                        isSuc = super.parseCode(response);
                    }
                    catch (BaseException e) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject metaObject = jsonObject.optJSONObject("meta");
                            if (metaObject != null) {
                                String resultCode = metaObject.optString("code");
                                isSuc = String.valueOf(200).equals(resultCode);
                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        if (isSuc) break block5;
                        throw e;
                    }
                }
                return isSuc;
            }
        });
    }

    public CloudTicketInfo getSDKCloudTicketInfo(String deviceSerial, int channelNo, long spaceId) throws BaseException {
        String urlString = "/api/service/cloud/ticket";
        String accessToken = LocalInfo.getInstance().getEZAccesstoken().getDeviceGlobalToken(deviceSerial, channelNo);
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("spaceId=").append(spaceId).append("&").append("accessToken=").append(accessToken);
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = channelNo;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String deviceSerial;
            @HttpParam(name="localIndex")
            private int cameraNo;
            {
                this.deviceSerial = fDeviceSerial;
                this.cameraNo = fCameraNo;
            }
        };
        baseInfo.fixAppName2Empty();
        baseInfo.fixDeviceGlobalToken(deviceSerial, channelNo);
        return (CloudTicketInfo)this.get(urlBuilder.toString(), baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                JSONObject rootObj;
                JSONObject neededJson;
                boolean ret = this.parseMetaCode(response);
                CloudTicketInfo ticketInfo = null;
                if (ret && (neededJson = (rootObj = new JSONObject(response)).optJSONObject("data")) != null) {
                    ticketInfo = new CloudTicketInfo();
                    ReflectionUtils.convJSONToObject(neededJson, ticketInfo);
                }
                return ticketInfo;
            }
        });
    }

    private boolean serviceExParseCode(String response) {
        boolean isSuc = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject metaObject = jsonObject.optJSONObject("meta");
            if (metaObject != null) {
                String resultCode = metaObject.optString("code");
                isSuc = String.valueOf(200).equals(resultCode);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return isSuc;
    }

    public boolean isSupportCloundService() throws BaseException {
        LogUtil.i(TAG, "Enter isSupportCloundService");
        if (!this.isUsingGlobalSDK()) {
            return false;
        }
        String urlString = "/api/v3/open/cloud/v3/clouds/cloudServiceSwitch";
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("accessToken=").append(LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken());
        Object result = this.get(urlBuilder.toString(), new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    int switchStatus = rootObj.optJSONObject("data").optInt("switchStatus");
                    return switchStatus == 0;
                }
                return false;
            }
        });
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "isSupportCloundService return " + ret);
        return ret;
    }

    public EZDeviceCloudServiceInfo getCloundDevicePackageInfo(String deviceSerial, int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter getCloundDevicePackageInfo");
        if (!this.isUsingGlobalSDK()) {
            return null;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/v3/open/cloud/v3/clouds/deviceInfo";
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("deviceSerial=").append(deviceSerial).append("&").append("channelNo=").append(cameraNo).append("&").append("accessToken=").append(LocalInfo.getInstance().getEZAccesstoken().getDeviceVideoToken(deviceSerial, cameraNo));
        Object result = this.get(urlBuilder.toString(), new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONObject dataObj = rootObj.getJSONObject("data");
                    EZDeviceCloudServiceInfo serviceInfo = new EZDeviceCloudServiceInfo();
                    ReflectionUtils.convJSONToObject(dataObj, serviceInfo);
                    JSONArray serviceJSONArray = dataObj.getJSONArray("serviceList");
                    if (serviceJSONArray != null) {
                        ArrayList<EZCloudServicePackageInfo> serviceList = new ArrayList<EZCloudServicePackageInfo>();
                        int len = serviceJSONArray.length();
                        for (int i = 0; i < len; ++i) {
                            EZCloudServicePackageInfo info = new EZCloudServicePackageInfo();
                            JSONObject jsonObj = serviceJSONArray.getJSONObject(i);
                            ReflectionUtils.convJSONToObject(jsonObj, info);
                            serviceList.add(info);
                        }
                        serviceInfo.setServiceList(serviceList);
                    }
                    return serviceInfo;
                }
                return null;
            }
        });
        if (result != null) {
            EZDeviceCloudServiceInfo ret = (EZDeviceCloudServiceInfo)result;
            return ret;
        }
        return null;
    }

    public boolean setCloundServiceActive(final String deviceSerial, final int cameraNo, final boolean enable) throws BaseException {
        LogUtil.i(TAG, "Enter setCloundServiceActive");
        if (!this.isUsingGlobalSDK()) {
            return false;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Object result = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="channelNo")
            private int channelNo;
            @HttpParam(name="enable")
            private int mEnable;
            {
                this.mDeviceSerial = deviceSerial;
                this.channelNo = cameraNo;
                this.mEnable = enable ? 1 : 0;
            }
        }, "/api/v3/open/cloud/v3/clouds/device/active", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                return PlayAPI.this.serviceExParseCode(response);
            }
        });
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "setCloundServiceActive return " + ret);
        return ret;
    }

    public List<String> getCloudVideoDays(String deviceSerial, int cameraNo, String month) throws BaseException {
        LogUtil.i(TAG, "Enter getCloudVideoDays");
        if (!this.isUsingGlobalSDK()) {
            return null;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Date date = DateTimeUtil.parseStringToDate(month, "yyyyMM");
        if (month.length() != 6 || date == null) {
            LogUtil.i(TAG, "month format must be yyyyMM");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/v3/open/cloud/v3/clouds/videoDays";
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("deviceSerial=").append(deviceSerial).append("&").append("channelNo=").append(cameraNo).append("&").append("month=").append(month).append("&").append("accessToken=").append(LocalInfo.getInstance().getEZAccesstoken().getDeviceVideoToken(deviceSerial, cameraNo));
        Object result = this.get(urlBuilder.toString(), new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONArray dataJSONArray = rootObj.getJSONArray("data");
                    ArrayList<String> dateList = new ArrayList<String>();
                    if (dataJSONArray != null) {
                        int len = dataJSONArray.length();
                        for (int i = 0; i < len; ++i) {
                            dateList.add(dataJSONArray.getString(i));
                        }
                    }
                    return dateList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            return ret;
        }
        return null;
    }

    public boolean deleteAllCloudVideo(final String deviceSerial, final int cameraNo) throws BaseException {
        LogUtil.i(TAG, "Enter deleteAllCloudVideo");
        if (!this.isUsingGlobalSDK()) {
            return false;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Object result = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="channelNo")
            private int channelNo;
            {
                this.mDeviceSerial = deviceSerial;
                this.channelNo = cameraNo;
            }
        }, "/api/v3/open/cloud/v3/clouds/videos/deleteAll", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean isSuc = false;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject metaObject = jsonObject.optJSONObject("meta");
                    if (metaObject != null) {
                        String resultCode = metaObject.optString("code");
                        isSuc = String.valueOf(200).equals(resultCode);
                        if (!isSuc) {
                            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(1, metaObject.optInt("code"));
                            throw new BaseException(metaObject.optString("message"), errorInfo);
                        }
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return isSuc;
            }
        });
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "deleteAllCloudVideo return " + ret);
        return ret;
    }

    public List<String> getIncrCloudVideos(final String deviceSerial, final int cameraNo, final EZConstants.EZCloudVideoType videoType, final String searchDate, final String maxCreateTime) throws BaseException {
        LogUtil.i(TAG, "Enter getIncrCloudVideos");
        if (!this.isUsingGlobalSDK()) {
            return null;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Date date = DateTimeUtil.parseYMDStringToDate(searchDate);
        if (searchDate.length() != 10 || date == null) {
            LogUtil.i(TAG, "searchDate format must be yyyy-MM-dd");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Object result = this.mRestfulUtils.post(new BaseInfo(){
            @HttpParam(name="deviceSerial")
            private String mDeviceSerial;
            @HttpParam(name="channelNo")
            private int channelNo;
            @HttpParam(name="videoType")
            private int mVideoType;
            @HttpParam(name="searchDate")
            private String mSearchDate;
            @HttpParam(name="maxCreateTime")
            private String mMaxCreateTime;
            {
                this.mDeviceSerial = deviceSerial;
                this.channelNo = cameraNo;
                this.mVideoType = videoType.videoType;
                this.mSearchDate = searchDate;
                this.mMaxCreateTime = maxCreateTime;
            }
        }, "/api/v3/open/cloud/v3/clouds/videosIncrPerDay", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONArray dataJSONArray = rootObj.getJSONArray("data");
                    ArrayList<String> videoList = new ArrayList<String>();
                    if (dataJSONArray != null) {
                        int len = dataJSONArray.length();
                        for (int i = 0; i < len; ++i) {
                            videoList.add(dataJSONArray.getString(i));
                        }
                    }
                    return videoList;
                }
                return null;
            }
        });
        if (result != null) {
            List ret = (List)result;
            return ret;
        }
        return null;
    }

    public List<EZCloudRecordFile> getCloudVideoDetails(String deviceSerial, int cameraNo, List<String> videos) throws BaseException {
        LogUtil.i(TAG, "Enter getCloudVideoDetails");
        if (!this.isUsingGlobalSDK()) {
            return null;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (videos == null || videos.size() == 0 || videos.size() > 30) {
            LogUtil.i(TAG, "video size must be greater than 0 but less than 30 ");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String paramsString = "";
        try {
            JSONArray videoJSONArray = new JSONArray();
            for (String videoString : videos) {
                videoJSONArray.put((Object)new JSONObject(videoString));
            }
            BaseInfo info = new BaseInfo();
            String infoString = new Gson().toJson((Object)info);
            JSONObject infoObj = new JSONObject(infoString);
            infoObj.put("deviceSerial", (Object)deviceSerial);
            infoObj.put("channelNo", cameraNo);
            infoObj.put("videos", (Object)videoJSONArray);
            paramsString = infoObj.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Object result = this.mRestfulUtils.post(paramsString, "/api/v3/open/cloud/v3/clouds/videoDetails", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = PlayAPI.this.serviceExParseCode(response);
                if (ret) {
                    JSONObject rootObj = new JSONObject(response);
                    JSONArray dataJSONArray = rootObj.getJSONArray("data");
                    ArrayList<EZCloudRecordFile> videoList = new ArrayList<EZCloudRecordFile>();
                    if (dataJSONArray != null) {
                        int len = dataJSONArray.length();
                        for (int i = 0; i < len; ++i) {
                            JSONObject obj = dataJSONArray.getJSONObject(i);
                            EZCloudRecordFile cloudRecordFile = new EZCloudRecordFile();
                            ReflectionUtils.convJSONToObject(obj, cloudRecordFile);
                            cloudRecordFile.setDeviceSerial(obj.getString("devSerial"));
                            cloudRecordFile.setStartTime(DateTimeUtil.parseYMDHMSStringToCalendar(obj.getString("startTime")));
                            cloudRecordFile.setStopTime(DateTimeUtil.parseYMDHMSStringToCalendar(obj.getString("stopTime")));
                            cloudRecordFile.setFileId(obj.getString("fileIndex"));
                            cloudRecordFile.setEncryption(obj.getString("keyChecksum"));
                            cloudRecordFile.setCameraNo(obj.getInt("cameraId"));
                            cloudRecordFile.setCloudType(obj.getInt("cloudType"));
                            cloudRecordFile.setiStorageVersion(obj.getInt("storageVersion"));
                            cloudRecordFile.setDownloadPath(obj.getString("streamUrl"));
                            videoList.add(cloudRecordFile);
                        }
                    }
                    return videoList;
                }
                return null;
            }
        }, true, false, true);
        if (result != null) {
            List ret = (List)result;
            return ret;
        }
        return null;
    }

    public boolean deleteCloudVideoFragment(String deviceSerial, int cameraNo, List<String> videos) throws BaseException {
        LogUtil.i(TAG, "Enter deleteCloudVideoFragment");
        if (!this.isUsingGlobalSDK()) {
            return false;
        }
        int nRet = this.mLocalValidate.localValidatDeviceSerial(deviceSerial);
        if (nRet != 0) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String paramsString = "";
        try {
            JSONArray videoJSONArray = new JSONArray();
            for (String videoString : videos) {
                videoJSONArray.put((Object)new JSONObject(videoString));
            }
            BaseInfo info = new BaseInfo();
            String infoString = new Gson().toJson((Object)info);
            JSONObject infoObj = new JSONObject(infoString);
            infoObj.put("deviceSerial", (Object)deviceSerial);
            infoObj.put("channelNo", cameraNo);
            infoObj.put("videos", (Object)videoJSONArray);
            paramsString = infoObj.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, nRet);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        Object result = this.mRestfulUtils.post(paramsString, "/api/v3/open/cloud/v3/clouds/videos/delete", new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                return PlayAPI.this.serviceExParseCode(response);
            }
        }, true, false, true);
        boolean ret = this.resultForAPICall(result);
        LogUtil.i(TAG, "deleteCloudVideoFragment return " + ret);
        return ret;
    }

    public SDKCloudFileEx getSDKCloudRecordUploadUrl(String fileName) throws BaseException {
        String urlString = "/api/service/cloud/upload/url";
        String accessToken = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
        StringBuilder urlBuilder = new StringBuilder(urlString);
        urlBuilder.append("?").append("fileName=").append(fileName).append("&").append("fileType=").append(0).append("&").append("expireDays=").append(-1).append("&").append("urlExpireTime=").append(1).append("&").append("bizType=").append("sdk").append("&").append("accessToken=").append(accessToken);
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.fixAppName2Empty();
        return (SDKCloudFileEx)this.get(urlBuilder.toString(), baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                SDKCloudFileEx info;
                block1: {
                    int i;
                    JSONObject rootObj;
                    JSONArray dataArray;
                    boolean ret = this.parseMetaCode(response);
                    info = null;
                    if (!ret || (dataArray = (rootObj = new JSONObject(response)).optJSONArray("data")) == null || (i = 0) >= dataArray.length()) break block1;
                    info = new SDKCloudFileEx();
                    JSONObject data = dataArray.getJSONObject(i);
                    info.setUploadURL(data.getString("url"));
                    info.setStorageId(data.getString("storageId"));
                    JSONObject fieldsObj = data.getJSONObject("fields");
                    HashMap<String, Object> formMap = new HashMap<String, Object>();
                    Iterator sIterator = fieldsObj.keys();
                    while (sIterator.hasNext()) {
                        String key = (String)sIterator.next();
                        String value = fieldsObj.getString(key);
                        formMap.put(key, value);
                    }
                    info.setFormMap(formMap);
                }
                return info;
            }
        });
    }

    public String uploadSDKCloudRecordPicture(Bitmap bitmap) throws BaseException {
        String fileName = DateTimeUtil.formatDateToString(new Date(), "yyyyMMddHHmmss");
        SDKCloudFileEx sdkCloudFileEx = this.getSDKCloudRecordUploadUrl(fileName);
        if (sdkCloudFileEx == null) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)stream);
        byte[] byteArray = stream.toByteArray();
        boolean result = this.mRestfulUtils.post(byteArray, sdkCloudFileEx.getUploadURL(), sdkCloudFileEx.getFormMap(), fileName, false, true, true);
        return result ? sdkCloudFileEx.getStorageId() : null;
    }

    public Map<String, String> getExtendStreamParam() {
        return this.extendStreamParam;
    }

    public void setExtendStreamParam(Map<String, String> extendStreamParam) {
        this.extendStreamParam = extendStreamParam;
    }

    public boolean videoTalkAction(String action, String appId, String ertcToken, String roomId, String deviceSerial, int cameraNo, String account) throws BaseException {
        LogUtil.i(TAG, "Enter videoTalkAction: " + action);
        if (TextUtils.isEmpty((CharSequence)appId) || TextUtils.isEmpty((CharSequence)ertcToken) || TextUtils.isEmpty((CharSequence)roomId) || TextUtils.isEmpty((CharSequence)deviceSerial) || TextUtils.isEmpty((CharSequence)account)) {
            LogUtil.i(TAG, "videoTalkAction: invalid parameters");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (cameraNo < 0) {
            LogUtil.i(TAG, "cameraNo must be greater than zero");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/service/rtc/call/" + action;
        final String fAppId = appId;
        final String fErtcToken = ertcToken;
        final String fRoomId = roomId;
        final String fAccount = account;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="appId")
            private String appId;
            @HttpParam(name="resourceToken")
            private String resourceToken;
            @HttpParam(name="strRoomId")
            private String strRoomId;
            @HttpParam(name="account")
            private String account;
            {
                this.appId = fAppId;
                this.resourceToken = fErtcToken;
                this.strRoomId = fRoomId;
                this.account = fAccount;
            }
        };
        baseInfo.fixDeviceGlobalToken(deviceSerial, cameraNo);
        BaseHeaderInfo baseHeaderInfo = new BaseHeaderInfo();
        baseHeaderInfo.setAccessToken(baseInfo.getAccessToken());
        baseHeaderInfo.setDeviceSerial(deviceSerial);
        baseHeaderInfo.setLocalIndex(String.valueOf(cameraNo));
        Object result = this.mRestfulUtils.post(baseHeaderInfo, baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseMetaCode(response);
                return ret;
            }
        });
        return this.resultForAPICall(result);
    }

    public EZBWCheckAddressInfo getBWCheckAddress(int bwCheckType, String deviceSerial, int cameraNo, int checkTime, int expireTime, String bwCheckToken) throws BaseException {
        LogUtil.i(TAG, "Enter getBWCheckAddress");
        if (checkTime < 15 || checkTime > 30) {
            LogUtil.e(TAG, "checkTime must between 15 and 30");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (expireTime < 30 || expireTime > 62208000) {
            LogUtil.e(TAG, "expireTime must between 30 and 62208000");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        if (TextUtils.isEmpty((CharSequence)bwCheckToken)) {
            LogUtil.e(TAG, "bwCheckToken must not be null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
        }
        String urlString = "/api/service/media/bwc/address";
        final String fDeviceSerial = deviceSerial;
        final int fCameraNo = cameraNo;
        int fBwCheckType = bwCheckType;
        int fCheckTime = checkTime;
        int fExpireTime = expireTime;
        String url = urlString + "?type=" + fBwCheckType + "&checkTime=" + fCheckTime + "&expireTime=" + fExpireTime;
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="deviceId")
            private String deviceSerial;
            @HttpParam(name="localIndex")
            private int cameraNo;
            {
                this.deviceSerial = fDeviceSerial;
                this.cameraNo = fCameraNo;
            }
        };
        baseInfo.setAccessToken(bwCheckToken);
        baseInfo.fixAppName2Empty();
        Object result = this.get(url, baseInfo, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean bRet = this.parseMetaCode(response);
                if (!bRet) {
                    return null;
                }
                JSONObject rootObject = new JSONObject(response);
                JSONObject dataObject = rootObject.optJSONObject("data");
                if (dataObject != null) {
                    EZBWCheckAddressInfo bwCheckAddressInfo = new EZBWCheckAddressInfo();
                    ReflectionUtils.convJSONToObject(dataObject, bwCheckAddressInfo);
                    return bwCheckAddressInfo;
                }
                return null;
            }
        });
        if (result != null && result instanceof EZBWCheckAddressInfo) {
            EZBWCheckAddressInfo bwCheckAddressInfo = (EZBWCheckAddressInfo)result;
            LogUtil.i(TAG, "getBWCheckAddress: " + bwCheckAddressInfo);
            return bwCheckAddressInfo;
        }
        LogUtil.i(TAG, "getBWCheckAddress: result is null");
        ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
        throw new BaseException(STR_NATIVE_PARAM_ERROR, errorInfo);
    }

    public boolean startBWidthCheckForDevice(EZBWCheckAddressInfo bwCheckAddressInfo) throws BaseException {
        LogUtil.i(TAG, "Enter startBWidthCheckForDevice");
        if (bwCheckAddressInfo == null) {
            return false;
        }
        String urlString = "/api/service/media/bwc";
        final String fUrl = bwCheckAddressInfo.getUrl();
        final String fPublicKey = bwCheckAddressInfo.getPublicKey();
        final int fVersion = bwCheckAddressInfo.getVersion();
        BaseInfo baseInfo = new BaseInfo(){
            @HttpParam(name="url")
            private String url;
            @HttpParam(name="publicKey")
            private String publicKey;
            @HttpParam(name="version")
            private int version;
            {
                this.url = fUrl;
                this.publicKey = fPublicKey;
                this.version = fVersion;
            }
        };
        baseInfo.fixHttpToken();
        BaseHeaderInfo baseHeaderInfo = new BaseHeaderInfo();
        baseHeaderInfo.setAccessToken(baseInfo.getAccessToken());
        Object result = this.mRestfulUtils.post(baseHeaderInfo, baseInfo, urlString, new ApiResponse(){

            @Override
            public Object parse(String response) throws BaseException, JSONException {
                boolean ret = this.parseMetaCode(response);
                return ret;
            }
        });
        return this.resultForAPICall(result);
    }

    class EZNetworkChangeListener
    extends BroadcastReceiver {
        EZNetworkChangeListener() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                final ConnectivityManager manager = (ConnectivityManager)context.getSystemService("connectivity");
                LogUtil.i(PlayAPI.TAG, "CONNECTIVITY_ACTION");
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        DeviceManager.getInstance().clearPreConnect();
                        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                        if (activeNetwork != null) {
                            if (activeNetwork.isConnected()) {
                                isNetConnect = true;
                                DeviceManager.getInstance().startPreConnect();
                                if (activeNetwork.getType() == 1) {
                                    LogUtil.d(PlayAPI.TAG, "\u5f53\u524dWiFi\u8fde\u63a5\u53ef\u7528 ");
                                } else if (activeNetwork.getType() == 0) {
                                    LogUtil.d(PlayAPI.TAG, "\u5f53\u524d\u79fb\u52a8\u7f51\u7edc\u8fde\u63a5\u53ef\u7528 ");
                                }
                            } else {
                                isNetConnect = false;
                                LogUtil.d(PlayAPI.TAG, "\u5f53\u524d\u6ca1\u6709\u7f51\u7edc\u8fde\u63a5\uff0c\u8bf7\u786e\u4fdd\u4f60\u5df2\u7ecf\u6253\u5f00\u7f51\u7edc ");
                            }
                        } else {
                            isNetConnect = false;
                            Log.d((String)PlayAPI.TAG, (String)"\u5f53\u524d\u6ca1\u6709\u7f51\u7edc\u8fde\u63a5\uff0c\u8bf7\u786e\u4fdd\u4f60\u5df2\u7ecf\u6253\u5f00\u7f51\u7edc ");
                        }
                    }
                }).start();
            }
        }
    }
}

