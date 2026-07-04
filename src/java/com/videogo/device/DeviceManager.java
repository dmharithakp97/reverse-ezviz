/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.text.TextUtils
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.InitParam
 */
package com.videogo.device;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.InitParam;
import com.ezviz.opensdk.data.FileCacheDeviceInfoManager;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.device.DeviceInfoEx;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.P2pDeviceInfo;
import com.videogo.openapi.bean.resp.EZDevicePlayInfo;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceManager {
    private static final String TAG = "DeviceManager";
    public static final String DEVICE_LIST_CHANGE_ACTION = "com.videogo.action.DEVICE_LIST_CHANGE_ACTION";
    private List<DeviceInfoEx> mDeviceList = new ArrayList<DeviceInfoEx>();
    private Map<String, P2pDeviceInfo> mP2pDeviceInfoMap = new HashMap<String, P2pDeviceInfo>();
    private List<String> mDeviceTokens = null;
    private long preGetDeviceTokenTime = 0L;
    private static DeviceManager mDeviceManager = null;
    private int mDeviceID = -1;
    private Context mContext = PlayAPI.mApplication;
    private static ExecutorService mExecutorService;

    private DeviceManager() {
        this.resetExecutorService();
    }

    private void resetExecutorService() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static synchronized DeviceManager getInstance() {
        if (mDeviceManager == null) {
            mDeviceManager = new DeviceManager();
        }
        return mDeviceManager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DeviceInfoEx getDeviceInfoExById(String deviceID) throws InnerException {
        if (deviceID == null) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new InnerException("deviceID eques null", errorInfo);
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            DeviceInfoEx devInfo = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                if (!this.mDeviceList.get(i).getDeviceID().trim().equalsIgnoreCase(deviceID.trim()) || System.currentTimeMillis() - this.mDeviceList.get(i).getTimeStamp() >= 3600000L) continue;
                devInfo = this.mDeviceList.get(i);
                break;
            }
            return devInfo;
        }
    }

    public DeviceInfoEx getDeviceInfoExFromEZPlayInfo(EZDevicePlayInfo info) {
        DeviceInfoEx deviceInfoEx = new DeviceInfoEx();
        deviceInfoEx.setDeviceID(info.getDeviceSerial());
        deviceInfoEx.setDeviceIP(info.getDeviceIP());
        deviceInfoEx.setDevicePort(info.getDevicePort());
        deviceInfoEx.setCmdPort(info.getCmdPort());
        deviceInfoEx.setHttpPort(info.getHttpPort());
        deviceInfoEx.setStreamPort(info.getStreamPort());
        deviceInfoEx.setLocalDeviceIp(info.getLocalIp());
        deviceInfoEx.setLocalDevicePort(info.getLocalDevicePort());
        deviceInfoEx.setLocalCmdPort(info.getLocalCmdPort());
        deviceInfoEx.setLocalHttpPort(info.getLocalHttpPort());
        deviceInfoEx.setLocalStreamPort(info.getLocalStreamPort());
        deviceInfoEx.setNetType(info.getNetType());
        deviceInfoEx.setPpvsAddr(info.getPpvsAddr());
        deviceInfoEx.setPpvsPort((short)info.getPpvsPort());
        deviceInfoEx.setCasIp(info.getCasIp());
        deviceInfoEx.setCasPort(info.getCasPort());
        deviceInfoEx.setMaskIp(info.getMaskIp());
        deviceInfoEx.setUpnp(info.getUpnp());
        deviceInfoEx.setCloudServiceStatus(info.getCloudServiceStatus());
        deviceInfoEx.setReleaseVersion(info.getReleaseVersion());
        deviceInfoEx.setIsEncrypt(info.getIsEncrypt());
        deviceInfoEx.setEncryptPwd(info.getEncryptPwd());
        deviceInfoEx.setBelongState(info.getBelongState());
        deviceInfoEx.setVtmIp(info.getVtmIp());
        deviceInfoEx.setVtmPort(info.getVtmPort());
        deviceInfoEx.setTtsIp(info.getTtsIp());
        deviceInfoEx.setTtsPort(info.getTtsPort());
        deviceInfoEx.setDeviceStatus(info.getDeviceStatus());
        deviceInfoEx.setSupportExtShort(info.getSupportExtShort());
        deviceInfoEx.setSupportExt(info.getSupportExt());
        deviceInfoEx.setPublicKey(info.getPublicKey());
        deviceInfoEx.setPublicKeyVersion(info.getPublicKeyVersion());
        return deviceInfoEx;
    }

    public void getDeviceInfoExFromOnlineToLocal(String deviceSerial, int cameraNo) throws BaseException {
        this.getDeviceInfoExFromOnlineToLocal(null, null, deviceSerial, cameraNo, null);
    }

    public void getDeviceInfoExFromOnlineToLocal(String bizType, String platformId, final String deviceSerial, int cameraNo, String cameraNoStr) throws BaseException {
        final DeviceInfoEx deviceInfoEx = PlayAPI.getInstance().getDeviceInfoEx(bizType, platformId, deviceSerial, cameraNo, cameraNoStr);
        final long b = System.currentTimeMillis();
        LogUtil.d(TAG, "getDeviceInfoExFromOnlineToLocal " + b);
        boolean supportP2pV3 = deviceInfoEx != null && deviceInfoEx.isSupportP2pV3();
        this.addDeviceInfo(deviceInfoEx, deviceSerial, b, !supportP2pV3);
        if (supportP2pV3) {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        P2pDeviceInfo p2pDeviceInfo = PlayAPI.getInstance().getP2pDeviceInfo(deviceSerial);
                        deviceInfoEx.setP2pDeviceInfo(p2pDeviceInfo);
                    }
                    catch (BaseException e) {
                        LogUtil.printErrStackTrace(DeviceManager.TAG, e.fillInStackTrace());
                    }
                    try {
                        DeviceManager.this.addDeviceInfo(deviceInfoEx, deviceSerial, b, true);
                    }
                    catch (BaseException e) {
                        LogUtil.printErrStackTrace(DeviceManager.TAG, e.fillInStackTrace());
                    }
                }
            }).start();
        }
    }

    public void addDeviceInfo(DeviceInfoEx deviceInfoEx, String deviceSerial, long b, boolean fileCacheUpdate) throws BaseException {
        long c = System.currentTimeMillis();
        LogUtil.d(TAG, "getDeviceInfoExFromOnlineToLocal " + c);
        if (deviceInfoEx != null) {
            if (deviceInfoEx.getCameraInfoEx() != null) {
                CameraInfoEx cameraInfoEx = new CameraInfoEx();
                cameraInfoEx.copy(deviceInfoEx.getCameraInfoEx());
                CameraManager.getInstance().addAddedCamera(cameraInfoEx);
                deviceInfoEx.setCameraInfoEx(null);
            }
            if (deviceInfoEx.getBelongDevice() != null) {
                DeviceInfoEx belongDeviceInfoEx = new DeviceInfoEx();
                belongDeviceInfoEx.copy(deviceInfoEx.getBelongDevice());
                belongDeviceInfoEx.setDeviceID(deviceInfoEx.getBelongSerial());
                belongDeviceInfoEx.setEncryptPwd(deviceInfoEx.getEncryptPwd());
                DeviceManager.getInstance().addDevice(belongDeviceInfoEx, fileCacheUpdate);
            }
            deviceInfoEx.setUserName("admin");
            deviceInfoEx.setTimeStamp(System.currentTimeMillis());
            deviceInfoEx.setAccessToken(LocalInfo.getInstance().getEZAccesstoken().getDeviceToken(deviceSerial));
            DeviceManager.getInstance().addDevice(deviceInfoEx, fileCacheUpdate);
            LogUtil.d(TAG, "getDeviceInfoExFromOnlineToLocal " + (System.currentTimeMillis() - b));
        }
    }

    public DeviceInfoEx getDeviceInfoExBelongDevice(DeviceInfoEx info) throws InnerException {
        if (info == null) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new InnerException("deviceID eques null", errorInfo);
        }
        if (info.getBelongDevice() != null) {
            DeviceInfoEx belongDeviceInfoEx = new DeviceInfoEx();
            belongDeviceInfoEx.copy(info.getBelongDevice());
            belongDeviceInfoEx.setDeviceID(info.getBelongSerial());
            belongDeviceInfoEx.setIsEncrypt(info.getIsEncrypt());
            belongDeviceInfoEx.setEncryptPwd(info.getEncryptPwd());
            belongDeviceInfoEx.setBelongNo(info.getBelongNo());
            return belongDeviceInfoEx;
        }
        return info;
    }

    private void handleDeviceListChange() {
        Intent intent = new Intent();
        intent.setAction(DEVICE_LIST_CHANGE_ACTION);
        this.mContext.sendBroadcast(intent);
    }

    public List<DeviceInfoEx> getDeviceList() {
        return this.mDeviceList;
    }

    public List<DeviceInfoEx> getSubDeviceList(int pageStart, int pageSize) {
        int start = pageStart * pageSize;
        int end = start + pageSize;
        if (this.mDeviceList.size() > start) {
            end = Math.min(end, this.mDeviceList.size());
            return this.mDeviceList.subList(start, end);
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setAddedDevices(List<DeviceInfoEx> devices) {
        if (devices == null) {
            LogUtil.e(TAG, "setAddedDevices, devices is null");
            return;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            this.mDeviceList.clear();
            this.mDeviceList.addAll(devices);
            if (PlayAPI.isNetConnect) {
                this.clearPreConnect();
                this.startPreConnect();
            }
        }
    }

    public void addDevice(DeviceInfoEx devInfoEx, boolean fileCacheUpdate) {
        if (this.addDevice(devInfoEx, false, fileCacheUpdate)) {
            this.handleDeviceListChange();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean addDevice(DeviceInfoEx devInfoEx, boolean insertInHead, boolean fileCacheUpdate) {
        if (devInfoEx == null) {
            LogUtil.e(TAG, "addDevice, devInfoEx is null");
            return false;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            boolean listChanged = false;
            boolean isExist = false;
            int deviceCount = this.mDeviceList.size();
            for (int i = 0; i < deviceCount; ++i) {
                DeviceInfoEx info = this.mDeviceList.get(i);
                if (!info.getDeviceID().equalsIgnoreCase(devInfoEx.getDeviceID())) continue;
                isExist = true;
                info.copy(devInfoEx);
                break;
            }
            if (!isExist) {
                if (insertInHead) {
                    this.mDeviceList.add(0, devInfoEx);
                } else {
                    this.mDeviceList.add(devInfoEx);
                }
                listChanged = true;
            }
            if (fileCacheUpdate) {
                FileCacheDeviceInfoManager.addDeviceEx(devInfoEx);
            }
            return listChanged;
        }
    }

    public List<String> getStreamToken(boolean isRemote) throws BaseException {
        if (isRemote || this.mDeviceTokens == null || this.mDeviceTokens.size() <= 0 || System.currentTimeMillis() - this.preGetDeviceTokenTime > 300000L) {
            this.mDeviceTokens = PlayAPI.getInstance().getStreamTokenList();
            this.preGetDeviceTokenTime = System.currentTimeMillis();
        }
        return this.mDeviceTokens;
    }

    public void clearDeviceToken() {
        if (this.mDeviceTokens != null) {
            this.mDeviceTokens.clear();
        }
    }

    public List<String> getStreamToken() throws BaseException {
        return this.getStreamToken(false);
    }

    public P2pDeviceInfo getP2pDeviceInfo(String deviceSerial) throws BaseException {
        if (this.mP2pDeviceInfoMap.containsKey(deviceSerial)) {
            return this.mP2pDeviceInfoMap.get(deviceSerial);
        }
        P2pDeviceInfo p2pDeviceInfo = PlayAPI.getInstance().getP2pDeviceInfo(deviceSerial);
        if (p2pDeviceInfo != null) {
            this.mP2pDeviceInfoMap.put(deviceSerial, p2pDeviceInfo);
        }
        return p2pDeviceInfo;
    }

    public void removeP2pDeviceInfo(String deviceSerial) {
        this.mP2pDeviceInfoMap.remove(deviceSerial);
    }

    public void clearP2pDeviceInfo() {
        this.mP2pDeviceInfoMap.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void deleteDevice(String deviceSN) {
        if (deviceSN == null) {
            LogUtil.e(TAG, "deleteDevice, deviceSN is null");
            return;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            int deviceCount = this.mDeviceList.size();
            for (int i = 0; i < deviceCount; ++i) {
                DeviceInfoEx info = this.mDeviceList.get(i);
                if (!info.getDeviceID().equalsIgnoreCase(deviceSN)) continue;
                this.mDeviceList.remove(i);
                FileCacheDeviceInfoManager.removeDeviceEx(info);
                LogUtil.d(TAG, " order \u5220\u9664\u8bbe\u5907 id  =" + deviceSN);
                break;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearDevice() {
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            this.mDeviceList.clear();
        }
        FileCacheDeviceInfoManager.clearDevice();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearPreConnect() {
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            int deviceCount = this.mDeviceList.size();
            this.resetExecutorService();
            for (int i = 0; i < deviceCount; ++i) {
                DeviceInfoEx info = this.mDeviceList.get(i);
                final String szDevSerial = info.getDeviceID();
                mExecutorService.submit(new Runnable(){

                    @Override
                    public void run() {
                        EZStreamClientManager.create((Context)PlayAPI.mApplication).clearPreconnectInfo(szDevSerial);
                    }
                });
            }
            this.clearP2pDeviceInfo();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startPreConnect() {
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            int deviceCount = this.mDeviceList.size();
            this.resetExecutorService();
            for (int i = 0; i < deviceCount; ++i) {
                DeviceInfoEx info = this.mDeviceList.get(i);
                this.startPreConnect(info);
            }
        }
    }

    public void startPreConnect(final DeviceInfoEx info) {
        final String szDevSerial = info.getDeviceID();
        mExecutorService.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    if (EZStreamClientManager.create((Context)PlayAPI.mApplication).isPreconnecting(szDevSerial)) {
                        EZStreamClientManager.create((Context)PlayAPI.mApplication).clearPreconnectInfo(szDevSerial);
                    }
                    EZStreamParamHelp ezStreamParamHelp = new EZStreamParamHelp(szDevSerial, 1);
                    ezStreamParamHelp.readyParamInfo();
                    InitParam initParam = ezStreamParamHelp.getInitParam(0);
                    if (TextUtils.isEmpty((CharSequence)initParam.szClientSession)) {
                        initParam.szClientSession = info.accessToken;
                    }
                    EZStreamClientManager.create((Context)PlayAPI.mApplication).startPreconnect(initParam);
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startPreConnect(final String deviceSerial) {
        mExecutorService.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    EZStreamParamHelp ezStreamParamHelp = new EZStreamParamHelp(deviceSerial, 1);
                    ezStreamParamHelp.readyParamInfo();
                    EZStreamClientManager.create((Context)PlayAPI.mApplication).startPreconnect(ezStreamParamHelp.getInitParam(0));
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isDisplayNotice() {
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                DeviceInfoEx deviceInfo = this.mDeviceList.get(i);
                boolean isStorageDisPlay = false;
                boolean isNeedUpdate = false;
                boolean isAlarmUnormalDisPlay = false;
                if ("1".indexOf(deviceInfo.getDiskStatus()) >= 0 || "2".indexOf(deviceInfo.getDiskStatus()) >= 0 || "3".indexOf(deviceInfo.getDiskStatus()) >= 0) {
                    isStorageDisPlay = true;
                }
                if (deviceInfo.hasUpgrade()) {
                    isNeedUpdate = true;
                }
                if (deviceInfo.getUnnormalStatus() == 1) {
                    isAlarmUnormalDisPlay = true;
                }
                if (!isNeedUpdate && !isStorageDisPlay && !isAlarmUnormalDisPlay) continue;
                return true;
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<DeviceInfoEx> getDownloadUpgradeDeviceList() {
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            ArrayList<DeviceInfoEx> deviceList = new ArrayList<DeviceInfoEx>();
            DeviceInfoEx deviceInfoEx = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                deviceInfoEx = this.mDeviceList.get(i);
                if (deviceInfoEx.getSupportUpgrade() != 1 || deviceInfoEx.getNeedUpgrade() <= 0) continue;
                deviceList.add(deviceInfoEx);
            }
            return deviceList;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean haveAlarmDevice() {
        if (this.mDeviceList == null) {
            return false;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                if (this.mDeviceList.get(i).getSupportDefence() == 0) continue;
                return true;
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean haveLeaveMessageDevice() {
        if (this.mDeviceList == null) {
            return false;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                if (this.mDeviceList.get(i).getModelType() != 12) continue;
                return true;
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearPreConncetInfo() {
        if (this.mDeviceList == null) {
            return;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            try {
                for (int i = 0; i < this.mDeviceList.size(); ++i) {
                    if (EZStreamClientManager.create((Context)PlayAPI.mApplication).isPreconnecting(this.mDeviceList.get(i).getDeviceID())) continue;
                    EZStreamClientManager.create((Context)PlayAPI.mApplication).clearPreconnectInfo(this.mDeviceList.get(i).getDeviceID());
                }
                this.clearP2pDeviceInfo();
            }
            catch (Error e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearDevicePlayType() {
        if (this.mDeviceList == null) {
            return;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            DeviceInfoEx deiviceInfo = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                deiviceInfo = this.mDeviceList.get(i);
                if (deiviceInfo == null) continue;
                deiviceInfo.setRealPlayType(0);
                deiviceInfo.setPlayBackType(0);
                deiviceInfo.setInLan(-1);
                deiviceInfo.setInUpnp(-1);
                deiviceInfo.clearIpAddress();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getDeviceNeedUpdateCount() {
        if (this.mDeviceList == null) {
            return 0;
        }
        int count = 0;
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            DeviceInfoEx deiviceInfo = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                deiviceInfo = this.mDeviceList.get(i);
                if (deiviceInfo == null || !deiviceInfo.isOnline() || deiviceInfo.getSupportUpgrade() != 1 || deiviceInfo.getNeedUpgrade() <= 0) continue;
                ++count;
            }
            LogUtil.i(TAG, "count: " + count);
            return count;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean getDeviceIsOneKeyUpdating() {
        if (this.mDeviceList == null) {
            return false;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            DeviceInfoEx deiviceInfo = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                deiviceInfo = this.mDeviceList.get(i);
                if (deiviceInfo == null || !deiviceInfo.isOnline() || deiviceInfo.getUpgradeStatus() == -1 || deiviceInfo.getUpgradeStatus() == 2 || deiviceInfo.getUpgradeStatus() == 3) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean getDeviceOneKeyUpdateFinished() {
        if (this.mDeviceList == null) {
            return false;
        }
        List<DeviceInfoEx> list = this.mDeviceList;
        synchronized (list) {
            DeviceInfoEx deiviceInfo = null;
            for (int i = 0; i < this.mDeviceList.size(); ++i) {
                deiviceInfo = this.mDeviceList.get(i);
                if (deiviceInfo == null || !deiviceInfo.isOnline() || deiviceInfo.getUpgradeStatus() == 2 || deiviceInfo.getUpgradeStatus() == -1 || deiviceInfo.getUpgradeStatus() == 3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isLan(DeviceInfoEx deviceInfoEx) {
        return false;
    }
}

