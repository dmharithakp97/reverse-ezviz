/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 */
package com.videogo.camera;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.videogo.camera.CameraInfo;
import com.videogo.camera.ShareCameraItem;
import com.videogo.openapi.bean.EZVideoQualityInfo;
import com.videogo.util.Utils;
import java.util.ArrayList;

public class CameraInfoEx
extends CameraInfo {
    public static final int CAMERA_STATUS_CLOSE = 0;
    public static final int CAMERA_STATUS_OPEN = 1;
    public static final int TYPE_ERROR = -1;
    public static final int TYPE_C1 = 1;
    public static final int TYPE_C2 = 2;
    public static final int TYPE_C3 = 3;
    public static final int TYPE_C4 = 4;
    public static final int TYPE_8464 = 5;
    public static final int TYPE_8464_CV1 = 6;
    public static final int TYPE_8133 = 7;
    public static final int TYPE_D1 = 8;
    public static final int TYPE_N1 = 9;
    public static final int TYPE_R1 = 10;
    public static final String MODEL_C1 = "C1";
    public static final String MODEL_C2 = "C2";
    public static final String MODEL_C3 = "C3";
    public static final String MODEL_C4 = "C4";
    public static final String MODEL_8464 = "8464";
    public static final String MODEL_8133 = "8133";
    public static final String MODEL_D1 = "D1";
    public static final String MODEL_N1 = "N1";
    public static final String MODEL_R1 = "R1";
    public static final int STREAM_TYPE_MAIN = 0;
    public static final int STREAM_TYPE_SUB = 1;
    public static final String MODEL_8464_FORMER_VERSION = "CV1";
    public static final int SHARE_CAMERA_NO = 0;
    public static final int SHARE_CAMERA_START = 1;
    public static final int SHARE_CAMERA_PROMPT = 2;
    public static final int SHARE_CAMERA_END = 3;
    public static final int FORCE_STREAM_TYPE_NORMAL = 0;
    public static final int FORCE_STREAM_TYPE_CAS = 1;
    public static final int FORCE_STREAM_TYPE_P2P = 2;
    public static final int FORCE_STREAM_TYPE_VTDU = 3;
    public static final int FORCE_STREAM_TYPE_ALL_VTDU = 1004;
    public static final int FORCE_STREAM_TYPE_REAL_VTDU = 1005;
    private ShareCameraItem shareCameraItem = null;
    private String permissionBinary = null;
    public static final Parcelable.Creator<CameraInfoEx> CREATOR = new Parcelable.Creator<CameraInfoEx>(){

        public CameraInfoEx createFromParcel(Parcel in) {
            return new CameraInfoEx(in);
        }

        public CameraInfoEx[] newArray(int size) {
            return new CameraInfoEx[size];
        }
    };

    public boolean isSameCameraDetailInfo(CameraInfoEx cameraInfoEx) {
        if (cameraInfoEx == null || TextUtils.isEmpty((CharSequence)this.getDeviceID()) || TextUtils.isEmpty((CharSequence)cameraInfoEx.getDeviceID())) {
            return false;
        }
        return this.getDeviceSerialAndCameraNo().equalsIgnoreCase(cameraInfoEx.getDeviceSerialAndCameraNo());
    }

    public boolean isSameCameraDetailInfo(String deviceSerial, int cameraNo) {
        if (TextUtils.isEmpty((CharSequence)this.getDeviceID()) || TextUtils.isEmpty((CharSequence)deviceSerial)) {
            return false;
        }
        StringBuffer stringBuffer = new StringBuffer("");
        if (!TextUtils.isEmpty((CharSequence)deviceSerial)) {
            stringBuffer.append(deviceSerial);
        }
        stringBuffer.append("_").append(cameraNo);
        return this.getDeviceSerialAndCameraNo().equalsIgnoreCase(stringBuffer.toString().trim());
    }

    public String getDeviceSerialAndCameraNo() {
        StringBuffer stringBuffer = new StringBuffer("");
        if (!TextUtils.isEmpty((CharSequence)this.getDeviceID())) {
            stringBuffer.append(this.getDeviceID());
        }
        stringBuffer.append("_").append(this.getChannelNo());
        return stringBuffer.toString();
    }

    public CameraInfoEx() {
    }

    public int getMinimumStreamType() {
        int streamCount = this.supportStreamTypeCount();
        return streamCount == 1 ? 0 : 1;
    }

    public int getStreamType() {
        int streamType = 1;
        int mode = this.getVideoLevel();
        if (this.videoQualityInfos != null && this.videoQualityInfos.size() > 0) {
            for (int i = 0; i < this.videoQualityInfos.size(); ++i) {
                if (((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getVideoLevel() != mode) continue;
                streamType = ((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getStreamType() - 1;
                return streamType;
            }
        }
        if (mode == 3) {
            streamType = 0;
            return streamType;
        }
        String[] capabilitys = this.getCapability().split("-");
        if (capabilitys.length >= 2 && mode <= 2 && (Integer.parseInt(capabilitys[this.videoLevel]) == 1 || Integer.parseInt(capabilitys[this.videoLevel]) == 0)) {
            streamType = 0;
        }
        return streamType;
    }

    public int getStreamType(int videoLevel) {
        int streamType = 1;
        if (this.videoQualityInfos != null && this.videoQualityInfos.size() > 0) {
            for (int i = 0; i < this.videoQualityInfos.size(); ++i) {
                if (((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getVideoLevel() != videoLevel) continue;
                streamType = ((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getStreamType() - 1;
                return streamType;
            }
        }
        if (videoLevel == 3) {
            streamType = 0;
            return streamType;
        }
        String[] capabilitys = this.getCapability().split("-");
        if (capabilitys.length >= 2 && videoLevel <= 2 && (Integer.parseInt(capabilitys[videoLevel]) == 1 || Integer.parseInt(capabilitys[videoLevel]) == 0)) {
            streamType = 0;
        }
        return streamType;
    }

    public int supportStreamTypeCount() {
        String[] capabilitys;
        int streamCount = 1;
        if (this.videoQualityInfos != null && this.videoQualityInfos.size() > 0) {
            ArrayList<Integer> countArr = new ArrayList<Integer>();
            for (int i = 0; i < this.videoQualityInfos.size(); ++i) {
                boolean needAdd = true;
                for (Integer type : countArr) {
                    if (type.intValue() != ((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getStreamType()) continue;
                    needAdd = false;
                    break;
                }
                if (!needAdd) continue;
                countArr.add(new Integer(((EZVideoQualityInfo)this.videoQualityInfos.get(i)).getStreamType()));
            }
            if (countArr.size() == 0) {
                return 1;
            }
            return countArr.size();
        }
        for (String typeStr : capabilitys = this.getCapability().split("-")) {
            if (Integer.parseInt(typeStr) != 2) continue;
            streamCount = 2;
        }
        return streamCount;
    }

    @Override
    public void setPermission(int permission) {
        super.setPermission(permission);
        this.permissionBinary = Integer.toBinaryString(permission);
    }

    private int getPermission(int index) {
        if (this.permissionBinary != null && index < this.permissionBinary.length()) {
            return this.permissionBinary.charAt(this.permissionBinary.length() - 1 - index) == '1' ? 1 : 0;
        }
        return 0;
    }

    public int getRealPlayPermission() {
        return this.getPermission(0);
    }

    public int getRemotePlayPermission() {
        return this.getPermission(1);
    }

    public int getMessagePermission() {
        return this.getPermission(2);
    }

    public int getTalkPermission() {
        return this.getPermission(3);
    }

    public int getQualityPermission() {
        return this.getPermission(4);
    }

    public int getCapturePermission() {
        return this.getPermission(5);
    }

    public int getRecordPermission() {
        return this.getPermission(6);
    }

    public int getSharePermission() {
        return this.getPermission(7);
    }

    public int getPtzPermission() {
        return this.getPermission(8);
    }

    public int getSpeechPermission() {
        return this.getPermission(9);
    }

    public int getPrivacyPermission() {
        return this.getPermission(10);
    }

    public boolean isOnline() {
        return this.status == 1;
    }

    public int getModelType() {
        if (this.fullModel == null) {
            return -1;
        }
        if (this.fullModel.contains(MODEL_C1)) {
            return 1;
        }
        if (this.fullModel.contains(MODEL_C2)) {
            return 2;
        }
        if (this.fullModel.contains(MODEL_C3)) {
            return 3;
        }
        if (this.fullModel.contains(MODEL_C4)) {
            return 4;
        }
        if (this.fullModel.contains(MODEL_8464)) {
            if (this.fullModel.contains(MODEL_8464_FORMER_VERSION)) {
                return 6;
            }
            return 5;
        }
        if (this.fullModel.contains(MODEL_8133)) {
            return 7;
        }
        if (this.fullModel.contains(MODEL_D1)) {
            return 8;
        }
        if (this.fullModel.contains(MODEL_N1)) {
            return 9;
        }
        if (this.fullModel.contains(MODEL_R1)) {
            return 10;
        }
        return -1;
    }

    public void setShareCameraItem(ShareCameraItem shareCameraItem) {
        this.shareCameraItem = shareCameraItem;
    }

    public ShareCameraItem getShareCameraItem() {
        return this.shareCameraItem;
    }

    public int getShareCameraStatus() {
        if (this.shareCameraItem == null) {
            return 0;
        }
        long serverTime = 0L;
        long endTime = Utils.get19TimeInMillis(this.shareCameraItem.getEndTime());
        if (endTime > serverTime) {
            if (endTime - serverTime < 1800000L) {
                return 2;
            }
            return 1;
        }
        return 3;
    }

    public int getShareCameraRemainMins() {
        if (this.shareCameraItem == null) {
            return 0;
        }
        long serverTime = 0L;
        long endTime = Utils.get19TimeInMillis(this.shareCameraItem.getEndTime());
        if (endTime > serverTime) {
            return (int)(endTime - serverTime) / 60000;
        }
        return 0;
    }

    public void copy(CameraInfoEx cameraInfoEx) {
        this.copy((CameraInfo)cameraInfoEx);
    }

    public boolean isSharedCamera() {
        int isShared = this.getIsShared();
        return isShared == 2 || isShared == 4 || isShared == 5;
    }

    protected CameraInfoEx(Parcel in) {
        super(in);
        this.shareCameraItem = (ShareCameraItem)in.readValue(ShareCameraItem.class.getClassLoader());
        this.permissionBinary = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue((Object)this.shareCameraItem);
        dest.writeString(this.permissionBinary);
    }
}

