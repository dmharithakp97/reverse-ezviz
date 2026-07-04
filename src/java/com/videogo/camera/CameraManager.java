/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.videogo.camera;

import android.text.TextUtils;
import com.ezviz.opensdk.data.FileCacheDeviceInfoManager;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.ShareCameraItem;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraManager {
    private static final String TAG = "CameraManager";
    private static final int CAPTURE_MAX_LENTH = 102400;
    private List<CameraInfoEx> mAddedCameraList = new ArrayList<CameraInfoEx>();
    private static CameraManager mCameraManager = null;
    private String mHardwareCode = null;
    private String mCameraSnapshotPath = LocalInfo.getInstance().getFilePath() + "/CameraSnapshot";

    private CameraManager() {
        this.mHardwareCode = LocalInfo.getInstance().getHardwareCode();
    }

    public static synchronized CameraManager getInstance() {
        if (null == mCameraManager) {
            mCameraManager = new CameraManager();
        }
        return mCameraManager;
    }

    public List<CameraInfoEx> getAddedCameraList() {
        return this.mAddedCameraList;
    }

    public void decAddedCameraUnreadMessageCount(String deviceSerial, int channelNo) {
        CameraInfoEx cameraInfoEx = this.getAddedCamera(deviceSerial, channelNo);
        if (cameraInfoEx == null) {
            return;
        }
        int count = cameraInfoEx.getAlarmCount();
        if (count > 0) {
            cameraInfoEx.setAlarmCount(--count);
        }
    }

    public void incAddedCameraUnreadMessageCount(String deviceSerial, int channelNo) {
        CameraInfoEx cameraInfoEx = this.getAddedCamera(deviceSerial, channelNo);
        if (cameraInfoEx == null) {
            return;
        }
        int count = cameraInfoEx.getAlarmCount();
        cameraInfoEx.setAlarmCount(++count);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<CameraInfoEx> getAddedCameraList(String deviceSerial) {
        ArrayList<CameraInfoEx> cameraList = new ArrayList<CameraInfoEx>();
        if (null == deviceSerial) {
            LogUtil.e(TAG, "getAddedCameraList, deviceSerial is null");
            return cameraList;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            int count = this.mAddedCameraList.size();
            CameraInfoEx cameraInfoEx = null;
            for (int i = 0; i < count; ++i) {
                cameraInfoEx = this.mAddedCameraList.get(i);
                if (!cameraInfoEx.getDeviceID().equalsIgnoreCase(deviceSerial)) continue;
                cameraList.add(cameraInfoEx);
            }
        }
        return cameraList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearAlarmCount() {
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            int count = this.mAddedCameraList.size();
            CameraInfoEx cameraInfoEx = null;
            for (int i = 0; i < count; ++i) {
                cameraInfoEx = this.mAddedCameraList.get(i);
                if (cameraInfoEx.getAlarmCount() == 0) continue;
                cameraInfoEx.setAlarmCount(0);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setAddedCameras(List<CameraInfoEx> cameraList) {
        if (cameraList == null) {
            LogUtil.e(TAG, "setAddedCameras, cameraList is null");
            return;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            this.mAddedCameraList.clear();
            this.mAddedCameraList.addAll(cameraList);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addAddedCamera(CameraInfoEx camInfoEx, boolean insertInHead) {
        if (camInfoEx == null) {
            LogUtil.e(TAG, "addAddedCamera, camInfoEx is null");
            return;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            boolean isExist = false;
            int cameraCount = this.mAddedCameraList.size();
            for (int i = 0; i < cameraCount; ++i) {
                CameraInfoEx info = this.mAddedCameraList.get(i);
                if (info.getChannelNo() != camInfoEx.getChannelNo() || !info.getDeviceID().equalsIgnoreCase(camInfoEx.getDeviceID())) continue;
                isExist = true;
                info.copy(camInfoEx);
                break;
            }
            if (!isExist) {
                if (insertInHead) {
                    this.mAddedCameraList.add(0, camInfoEx);
                } else {
                    this.mAddedCameraList.add(camInfoEx);
                }
            }
            FileCacheDeviceInfoManager.addCameraEx(camInfoEx);
        }
    }

    public void addAddedCamera(CameraInfoEx camInfoEx) {
        this.addAddedCamera(camInfoEx, true);
    }

    public void updateAddCamraList(List<CameraInfoEx> cameraList) {
        if (cameraList == null) {
            return;
        }
        for (CameraInfoEx cameraInfoEx : cameraList) {
            this.addAddedCamera(cameraInfoEx, true);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setAlarmCount(CameraInfoEx mSelCamInfo) {
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            for (int i = 0; i < this.mAddedCameraList.size(); ++i) {
                CameraInfoEx camInfo = this.mAddedCameraList.get(i);
                if (!camInfo.getCameraID().equalsIgnoreCase(mSelCamInfo.getCameraID())) continue;
                camInfo.setAlarmCount(camInfo.getAlarmCount() - 1);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void deleteAddedCamera(String deviceSerial, int cameraNo) {
        if (TextUtils.isEmpty((CharSequence)deviceSerial)) {
            LogUtil.e(TAG, "deleteAddedCamera, deviceSerial is null");
            return;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            int size = this.mAddedCameraList.size();
            for (int i = 0; i < size; ++i) {
                CameraInfoEx camInfo = this.mAddedCameraList.get(i);
                if (!TextUtils.equals((CharSequence)camInfo.getDeviceID(), (CharSequence)deviceSerial) || camInfo.getChannelNo() != cameraNo) continue;
                this.mAddedCameraList.remove(i);
                break;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void deleteCameraByDeviceId(String deviceID) {
        if (deviceID == null) {
            LogUtil.e(TAG, "deleteAddedCamera, cameraID is null");
            return;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            int size = this.mAddedCameraList.size();
            for (int i = 0; i < size; ++i) {
                CameraInfoEx camInfo = this.mAddedCameraList.get(i);
                if (!camInfo.getDeviceID().equalsIgnoreCase(deviceID)) continue;
                this.deleteCaptureFile(camInfo.getDeviceID(), camInfo.mChannelNo);
                this.mAddedCameraList.remove(i);
                --size;
                --i;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearCamera() {
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            this.mAddedCameraList.clear();
        }
        FileCacheDeviceInfoManager.clearCamera();
    }

    public CameraInfoEx getAddedCamera(String deviceSerial, int channelNo) {
        return this.getAddedCamera(deviceSerial, channelNo, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CameraInfoEx getAddedCamera(String deviceSerial, int channelNo, String channelNoStr) {
        if (null == deviceSerial) {
            LogUtil.e(TAG, "deviceSerial is null");
            return null;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            CameraInfoEx camera = null;
            for (int i = 0; i < this.mAddedCameraList.size(); ++i) {
                camera = this.mAddedCameraList.get(i);
                if (channelNoStr == null) {
                    if (camera == null || !camera.getDeviceID().equalsIgnoreCase(deviceSerial) || camera.getChannelNo() != channelNo) continue;
                    return camera;
                }
                if (camera == null || !camera.getDeviceID().equalsIgnoreCase(deviceSerial) || !channelNoStr.equals(camera.getSzChnlIndex())) continue;
                return camera;
            }
            if (camera == null) {
                LogUtil.e(TAG, "not find, deviceSerial:" + deviceSerial + ", channelNo:" + channelNo);
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CameraInfoEx getAddedCameraByThirdDevId(String thirdDevId) {
        if (null == thirdDevId) {
            LogUtil.e(TAG, "thirdDevId is null");
            return null;
        }
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            CameraInfoEx camera = null;
            for (int i = 0; i < this.mAddedCameraList.size(); ++i) {
                camera = this.mAddedCameraList.get(i);
                if (!camera.getThirdDevId().equalsIgnoreCase(thirdDevId)) continue;
                return camera;
            }
            if (camera == null) {
                LogUtil.e(TAG, "not find, deviceSerial:" + thirdDevId);
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setShareCameraItemList(List<ShareCameraItem> shareCameraItemList) {
        if (shareCameraItemList == null || shareCameraItemList.size() == 0) {
            return;
        }
        CameraInfoEx cameraInfoEx = null;
        ShareCameraItem shareCameraItem = null;
        List<CameraInfoEx> list = this.mAddedCameraList;
        synchronized (list) {
            for (int i = 0; i < this.mAddedCameraList.size(); ++i) {
                cameraInfoEx = this.mAddedCameraList.get(i);
                for (int j = 0; j < shareCameraItemList.size(); ++j) {
                    shareCameraItem = shareCameraItemList.get(j);
                    if (!cameraInfoEx.getDeviceID().equalsIgnoreCase(shareCameraItem.getDeviceSN()) || cameraInfoEx.getChannelNo() != shareCameraItem.getChannelNo()) continue;
                    cameraInfoEx.setShareCameraItem(shareCameraItem);
                }
            }
        }
    }

    private String getCapturePath(String deviceSerial, int cameraNo) {
        return this.mCameraSnapshotPath + "/" + deviceSerial + "_" + cameraNo;
    }

    public boolean deleteCaptureFile(String deviceSerial, int cameraNo) {
        String capturePath = this.getCapturePath(deviceSerial, cameraNo);
        File oldFile = new File(capturePath);
        if (oldFile.exists() && oldFile.delete()) {
            LogUtil.d(TAG, "\u5220\u9664\u65e7\u56fe\u6587\u4ef6\u6210\u529f.path = " + capturePath);
        }
        return true;
    }
}

