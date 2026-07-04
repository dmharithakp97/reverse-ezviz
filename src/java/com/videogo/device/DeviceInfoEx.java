/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  org.json.JSONObject
 */
package com.videogo.device;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.videogo.camera.CameraInfoEx;
import com.videogo.device.DeviceInfo;
import com.videogo.device.DeviceManager;
import com.videogo.device.DeviceSwitchInfo;
import com.videogo.openapi.PlayAPI;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

public class DeviceInfoEx
extends DeviceInfo {
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
    public static final int TYPE_A1 = 11;
    public static final int TYPE_F1 = 12;
    public static final int TYPE_C2_2 = 13;
    public static final int TYPE_C2_S = 14;
    public static final int TYPE_R2 = 15;
    public static final int TYPE_C2_MINI = 16;
    public static final int TYPE_X1 = 17;
    public static final int TYPE_C2PT = 18;
    public static final int TYPE_C6 = 19;
    public static final int TYPE_C2_W = 20;
    public static final int TYPE_X3 = 21;
    public static final int TYPE_A1C = 22;
    public static final int TYPE_C2C = 23;
    public static final int TYPE_H2C = 24;
    public static final int TYPE_CO2 = 25;
    public static final int TYPE_H2S = 26;
    public static final int TYPE_X2 = 27;
    public static final int TYPE_W1 = 28;
    public static final String MODEL_C1 = "C1";
    public static final String MODEL_C2 = "C2";
    public static final String MODEL_C2_2 = "C2-2";
    public static final String MODEL_C2_S = "C2S";
    public static final String MODEL_C2_MINI = "C2mini";
    public static final String MODEL_C2_W = "CS-C2W";
    public static final String MODEL_C3 = "C3";
    public static final String MODEL_C4 = "C4";
    public static final String MODEL_8464 = "8464";
    public static final String MODEL_8133 = "8133";
    public static final String MODEL_D1 = "D1";
    public static final String MODEL_N1 = "N1";
    public static final String MODEL_R1 = "R1";
    public static final String MODEL_R2 = "R2";
    public static final String MODEL_F1 = "F1";
    public static final String MODEL_A1 = "A1";
    public static final String MODEL_X1 = "X1";
    public static final String MODEL_C6 = "CS-C6";
    public static final String MODEL_C2PT = "CS-C2PT";
    public static final String MODEL_X3 = "CS-X3";
    public static final String MODEL_A1C = "CS-A1C";
    public static final String MODEL_C2C = "CS-C2C";
    public static final String MODEL_H2C = "CS-H2C";
    public static final String MODEL_CO2 = "CS-CO2";
    public static final String MODEL_H2S = "CS-H2S";
    public static final String MODEL_X2 = "CS-X2";
    public static final String MODEL_W1 = "CS-W1";
    public static final String MODEL_8464_FORMER_VERSION = "CV1";
    public static final int DEFENCE_OPEN = 1;
    public static final int DEFENCE_CLOSE = 0;
    public static final int DEFENCE_AT_HOME = 8;
    public static final int DEFENCE_OUT_DOOR = 16;
    public static final int DEFENCE_FORCE = 3;
    public static final int DEFENCE_BABY_CRY = 32;
    public static final int DEFENCE_MOTION_DETECT = 64;
    public static final int DEFENCE_ALL = 96;
    public static final int TALK_HALF_DUPLEX = 3;
    public static final int TALK_FULL_DUPLEX = 1;
    private String mUserName = null;
    private String mPassword = null;
    private String mSafeModePassword = null;
    private String mCloudSafeModePasswd = null;
    private int mRealPlayType = 0;
    private int mPlayBackType = 0;
    private String operationCode;
    private String encryptKey;
    private int encryptType;
    public static final int UPGRADE_STATUS_NO = -1;
    public static final int UPGRADE_STATUS_UPGRADING = 0;
    public static final int UPGRADE_STATUS_REBOOTING = 1;
    public static final int UPGRADE_STATUS_SUCCEED = 2;
    public static final int UPGRADE_STATUS_FAILED = 3;
    public static final int UPGRADE_STATUS_WAITING = 4;
    public static final int UPGRADE_STATUS_DOWNLOADING = 5;
    public static final int UPGRADE_STATUS_ERROR = 6;
    public static final String DISK_NORMAL = "0";
    public static final String DISK_STORAGE_ERROR = "1";
    public static final String DISK_FORMATTING = "3";
    public static final String DISK_UNFORMATTED = "2";
    public static final String DISK_NO_SDCARD = "9";
    private int notifyStatus = -1;
    private int notifyOnline = -1;
    private int upgradeProgress = 0;
    private int upgradeErrorCode = 0;
    private String upgradeInputPwd = "";
    private long upgradeRebootingTime = 0L;
    private int mInLan = -1;
    protected int mInUpnp = -1;
    private int defencePlanEnable = -1;
    private String[] supportExtValues;
    private int streamAdaptiveStatus = 0;
    private int privacyStatus = 0;
    private int soundLocalizationStatus = 0;
    private int cruiseSwitchStatus = 0;
    private int wifiMarketingStatus = 0;
    private int infraredLightStatus = 0;
    private int wifiLightStatus = 1;
    private int wifiStatus = 0;
    private long mTimeStamp;
    private CameraInfoEx mCameraInfoEx;
    public static final Parcelable.Creator<DeviceInfoEx> CREATOR = new Parcelable.Creator<DeviceInfoEx>(){

        public DeviceInfoEx createFromParcel(Parcel in) {
            return new DeviceInfoEx(in);
        }

        public DeviceInfoEx[] newArray(int size) {
            return new DeviceInfoEx[size];
        }
    };

    public DeviceInfoEx() {
    }

    public CameraInfoEx getCameraInfoEx() {
        return this.mCameraInfoEx;
    }

    public void setCameraInfoEx(CameraInfoEx cameraInfoEx) {
        this.mCameraInfoEx = cameraInfoEx;
    }

    public int getPrivacyStatus() {
        return this.privacyStatus;
    }

    public void setPrivacyStatus(int privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public int getSoundLocalizationStatus() {
        return this.soundLocalizationStatus;
    }

    public void setSoundLocalizationStatus(int soundLocalizationStatus) {
        this.soundLocalizationStatus = soundLocalizationStatus;
    }

    public int getCruiseSwitchStatus() {
        return this.cruiseSwitchStatus;
    }

    public void setCruiseSwitchStatus(int cruiseSwitchStatus) {
        this.cruiseSwitchStatus = cruiseSwitchStatus;
    }

    public int getWifiMarketingStatus() {
        return this.wifiMarketingStatus;
    }

    public void setWifiMarketingStatus(int wifiMarketingStatus) {
        this.wifiMarketingStatus = wifiMarketingStatus;
    }

    public int getWifiLightStatus() {
        return this.wifiLightStatus;
    }

    public void setWifiLightStatus(int wifiLightStatus) {
        this.wifiLightStatus = wifiLightStatus;
    }

    public int getWifiStatus() {
        return this.wifiStatus;
    }

    public void setWifiStatus(int wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public int getStreamAdaptiveStatus() {
        return this.streamAdaptiveStatus;
    }

    public void setStreamAdaptiveStatus(int streamAdaptiveStatus) {
        this.streamAdaptiveStatus = streamAdaptiveStatus;
    }

    public int getInfraredLightStatus() {
        return this.infraredLightStatus;
    }

    public void setInfraredLightStatus(int infraredLightStatus) {
        this.infraredLightStatus = infraredLightStatus;
    }

    public int getDefencePlanEnable() {
        return this.defencePlanEnable;
    }

    public void setDefencePlanEnable(int defencePlanEnable) {
        this.defencePlanEnable = defencePlanEnable;
    }

    public void setDeviceSwitchInfoList(List<DeviceSwitchInfo> deviceSwitchInfoList) {
        if (deviceSwitchInfoList != null) {
            DeviceSwitchInfo deviceSwitchInfo = null;
            block13: for (int i = 0; i < deviceSwitchInfoList.size(); ++i) {
                deviceSwitchInfo = deviceSwitchInfoList.get(i);
                switch (deviceSwitchInfo.getType()) {
                    case 1: {
                        this.setAlarmStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 2: {
                        this.setStreamAdaptiveStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 3: {
                        this.setLightStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 6: {
                        this.setDefencePlanEnable(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 7: {
                        this.setPrivacyStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 8: {
                        this.setSoundLocalizationStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 9: {
                        this.setCruiseSwitchStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 10: {
                        this.setInfraredLightStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 11: {
                        this.setWifiStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 12: {
                        this.setWifiMarketingStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                    case 13: {
                        this.setWifiLightStatus(deviceSwitchInfo.isEnable() ? 1 : 0);
                        continue block13;
                    }
                }
            }
        }
    }

    @Override
    public void setSwitches(List<DeviceSwitchInfo> switches) {
        super.setSwitches(switches);
        this.setDeviceSwitchInfoList(switches);
    }

    public boolean isOnline() {
        return this.mDeviceStatus == 1;
    }

    public String getUserName() {
        return this.mUserName != null ? this.mUserName : "admin";
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getPassword() {
        if (this.getSupportChangeSafePasswd() == 1) {
            if (TextUtils.isEmpty((CharSequence)this.mSafeModePassword)) {
                return this.getVerifyCodePassword();
            }
            return this.mSafeModePassword;
        }
        return this.getVerifyCodePassword();
    }

    public void setPassword(String password) {
        if (this.getSupportChangeSafePasswd() == 1) {
            this.mSafeModePassword = password;
        } else {
            this.setVerifyCodePassword(password);
        }
    }

    public String getVerifyCodePassword() {
        return this.mPassword != null ? this.mPassword : "ABCDEF";
    }

    public void setVerifyCodePassword(String password) {
        this.mPassword = password;
    }

    public String getCloudSafeModePasswd() {
        return this.mCloudSafeModePasswd;
    }

    public void setCloudSafeModePasswd(String mCloundSafeModePasswd) {
        this.mCloudSafeModePasswd = mCloundSafeModePasswd;
    }

    public int getRealPlayType() {
        return this.mRealPlayType;
    }

    public void setRealPlayType(int realPlayType) {
        this.mRealPlayType = realPlayType;
    }

    public int getPlayBackType() {
        return this.mPlayBackType;
    }

    public void setPlayBackType(int playBackType) {
        this.mPlayBackType = playBackType;
    }

    public int getInLan() {
        if (ConnectionDetector.getConnectionType(LocalInfo.getInstance().getContext()) != 3) {
            this.mInLan = 0;
        } else if (this.mInLan == -1) {
            this.mInLan = DeviceManager.getInstance().isLan(this) ? 1 : 0;
        }
        return this.mInLan;
    }

    public void setInLan(int inLan) {
        this.mInLan = inLan;
    }

    public void setInUpnp(int inUpnp) {
        this.mInUpnp = inUpnp;
    }

    public void setDiskStatus(int index, char status) {
        char[] statuschar = this.diskStatus.toCharArray();
        if (index <= 0 || index > statuschar.length) {
            return;
        }
        statuschar[index - 1] = status;
        this.diskStatus = new String(statuschar);
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationCode() {
        return this.operationCode;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getEncryptKey() {
        return this.encryptKey;
    }

    public void setEncryptType(int encryptType) {
        this.encryptType = encryptType;
    }

    public int getEncryptType() {
        return this.encryptType;
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
        if (this.fullModel.contains(MODEL_C2_2)) {
            return 13;
        }
        if (this.fullModel.contains(MODEL_C2_MINI)) {
            return 16;
        }
        if (this.fullModel.contains(MODEL_C3)) {
            return 3;
        }
        if (this.fullModel.contains(MODEL_C4)) {
            return 4;
        }
        if (this.fullModel.contains(MODEL_C6) || this.fullModel.contains(MODEL_C2PT)) {
            return 19;
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
        if (this.fullModel.contains(MODEL_R2)) {
            return 15;
        }
        if (this.fullModel.contains(MODEL_A1C)) {
            return 22;
        }
        if (this.fullModel.contains(MODEL_A1)) {
            return 11;
        }
        if (this.fullModel.contains(MODEL_F1)) {
            return 12;
        }
        if (this.fullModel.contains(MODEL_X1)) {
            return 17;
        }
        if (this.fullModel.contains(MODEL_X3)) {
            return 21;
        }
        if (this.fullModel.contains(MODEL_C2C)) {
            return 23;
        }
        if (this.fullModel.contains(MODEL_H2C)) {
            return 24;
        }
        if (this.fullModel.contains(MODEL_CO2)) {
            return 25;
        }
        if (this.fullModel.contains(MODEL_H2S)) {
            return 26;
        }
        if (this.fullModel.contains(MODEL_C2_W)) {
            return 20;
        }
        if (this.fullModel.contains(MODEL_X2)) {
            return 27;
        }
        if (this.fullModel.contains(MODEL_W1)) {
            return 28;
        }
        return -1;
    }

    @Override
    public void setSupportExtShort(String supportExtShort) {
        super.setSupportExtShort(supportExtShort);
        if (this.supportExtShort != null) {
            this.supportExtValues = this.supportExtShort.split("\\|");
        }
    }

    private int getSupportInt(int index) {
        String value = this.getSupportValue(index);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            try {
                int number = Integer.parseInt(value);
                if (index == 50 && number == -1) {
                    number = 2;
                }
                return Math.max(number, 0);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace("DeviceInfo", e.fillInStackTrace());
            }
        }
        return 0;
    }

    private String getSupportString(int index) {
        String strValue = this.getSupportValue(index);
        if (TextUtils.isEmpty((CharSequence)strValue) || strValue.equalsIgnoreCase("-1")) {
            return "";
        }
        return strValue;
    }

    private String getSupportValue(int index) {
        if (this.supportExtValues == null && !TextUtils.isEmpty((CharSequence)this.getSupportExtShort())) {
            this.setSupportExtShort(this.getSupportExtShort());
        }
        if (this.supportExtValues != null && index > 0 && index <= this.supportExtValues.length) {
            return this.supportExtValues[index - 1];
        }
        return "";
    }

    public int getSupportMore() {
        return this.getSupportInt(54);
    }

    public int getSupportFlowStatistics() {
        return this.getSupportInt(53);
    }

    public int getSupportChanType() {
        return this.getSupportInt(52);
    }

    public int getSupportTalkType() {
        return this.getSupportInt(51);
    }

    public int getSupportModifyChanName() {
        return this.getSupportInt(49);
    }

    public int getSupportAutoAdjust() {
        return this.getSupportInt(45);
    }

    public int getSupportUnbind() {
        return this.getSupportInt(44);
    }

    public int getSupportWifiPortal() {
        return this.getSupportInt(43);
    }

    public int getSupportWifi5G() {
        return this.getSupportInt(42);
    }

    public int getSupportWifi24G() {
        return this.getSupportInt(41);
    }

    public int getSupportPtzPrivacy() {
        return this.getSupportInt(40);
    }

    public int getSupportPtzTopBottomMirror() {
        return this.getSupportInt(39);
    }

    public int getSupportPtzLeftRightMirror() {
        return this.getSupportInt(37);
    }

    public int getSupportPtzCenterMirror() {
        return this.getSupportInt(37);
    }

    public int getSupportPtzFigureCruise() {
        return this.getSupportInt(36);
    }

    public int getSupportPtzCommonCruise() {
        return this.getSupportInt(35);
    }

    public int getSupportPtzZoom() {
        return this.getSupportInt(33);
    }

    public int getSupportPtz45Degree() {
        return this.getSupportInt(32);
    }

    public int getSupportPtzLeftRight() {
        return this.getSupportInt(31);
    }

    public int getSupportPtzTopBottom() {
        return this.getSupportInt(30);
    }

    @Override
    public int getSupportWifi() {
        return this.getSupportInt(13);
    }

    public int getSupportSdkTransport() {
        return this.getSupportInt(29);
    }

    public int getSupportRemoteAuthRandcode() {
        return this.getSupportInt(28);
    }

    public int getSupportRelatedStorage() {
        return this.getSupportInt(27);
    }

    public int getSupportPreset() {
        return this.getSupportInt(34);
    }

    public int getSupportSsl() {
        return this.getSupportInt(25);
    }

    public int getSupportPtzModel() {
        return this.getSupportInt(50);
    }

    public int getSupportIpcLink() {
        return this.getSupportInt(20);
    }

    public int getSupportAddDelDetector() {
        return this.getSupportInt(19);
    }

    public int getSupportMultiScreen() {
        return this.getSupportInt(17);
    }

    public String getSupportResolution() {
        return this.getSupportString(16);
    }

    public int getSupportUploadCloudFile() {
        return this.getSupportInt(18);
    }

    public int getSupportChangeSafePasswd() {
        return this.getSupportInt(15);
    }

    public int getSupportCapture() {
        return this.getSupportInt(14);
    }

    public int getSupportAlarmVoice() {
        return this.getSupportInt(7);
    }

    public int getSupportAutoOffline() {
        return this.getSupportInt(8);
    }

    public int getSupportTalk() {
        return this.getSupportInt(2);
    }

    public int getSupportDefence() {
        return this.getSupportInt(1);
    }

    public int getSupportDefencePlan() {
        return this.getSupportInt(3);
    }

    public int getSupportDisk() {
        return this.getSupportInt(4);
    }

    public int getSupportMessage() {
        return this.getSupportInt(6);
    }

    public int getSupportPrivacy() {
        return this.getSupportInt(5);
    }

    public int getSupportEncrypt() {
        return this.getSupportInt(9);
    }

    public int getSupportCloud() {
        return this.getSupportInt(11);
    }

    public int getSupportCloudVersion() {
        return this.getSupportInt(12);
    }

    public int getSupportUpgrade() {
        return this.getSupportInt(10);
    }

    public int getSupportRelatedDevice() {
        return this.getSupportInt(26);
    }

    public int getSupportModifyDetectorname() {
        return this.getSupportInt(21);
    }

    public int getSupportSafeModePlan() {
        return this.getSupportInt(22);
    }

    public String getSupportModifyDetectorguard() {
        return this.getSupportString(23);
    }

    public int getSupportTimezone() {
        return this.getSupportInt(46);
    }

    public String getSupportLanguage() {
        return this.getSupportString(47);
    }

    public int getSupportCloseInfraredLight() {
        return this.getSupportInt(48);
    }

    public int getSupportWeixin() {
        return this.getSupportInt(24);
    }

    public int getSupportPtz() {
        return 0;
    }

    public boolean getSupporP2P() {
        return this.getSupportInt(59) == 1;
    }

    public int getSupportNatPass() {
        return this.getSupportInt(84);
    }

    public boolean getSupportNewTTS() {
        return this.getSupportInt(87) == 1;
    }

    public int getNotifyStatus() {
        return this.notifyStatus;
    }

    public void setNotifyStatus(int notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public int getNotifyOnline() {
        return this.notifyOnline;
    }

    public void setNotifyOnline(int notifyOnline) {
        this.notifyOnline = notifyOnline;
    }

    public void setUpgradeProgress(int upgradeProgress) {
        this.upgradeProgress = upgradeProgress;
    }

    public int getUpgradeProgress() {
        return this.upgradeProgress;
    }

    public int getUpgradeErrorCode() {
        return this.upgradeErrorCode;
    }

    public void setUpgradeErrorCode(int upgradeErrorCode) {
        this.upgradeErrorCode = upgradeErrorCode;
        if (upgradeErrorCode == 380121) {
            this.setDeviceStatus(0);
        }
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    public String getUpgradeInputPwd() {
        return this.upgradeInputPwd;
    }

    public void setUpgradeInputPwd(String upgradeInputPwd) {
        this.upgradeInputPwd = upgradeInputPwd;
    }

    public boolean hasUpgrade() {
        return this.isOnline() && this.getSupportUpgrade() == 1 && (this.isNeedUpgrade > 0 || this.upgradeStatus == 0 || this.upgradeStatus == 1);
    }

    @Override
    public void setUpgradeStatus(int upgradeStatus) {
        if (this.upgradeStatus != upgradeStatus && upgradeStatus == 1) {
            Calendar calendar = Calendar.getInstance();
            this.upgradeRebootingTime = calendar.getTimeInMillis();
        } else if (upgradeStatus != 1) {
            this.upgradeRebootingTime = 0L;
        }
        super.setUpgradeStatus(upgradeStatus);
    }

    @Override
    public int getUpgradeStatus() {
        if (this.upgradeStatus == 1) {
            Calendar calendar = Calendar.getInstance();
            if (this.upgradeRebootingTime > 0L && Math.abs(calendar.getTimeInMillis() - this.upgradeRebootingTime) > 120000L) {
                return 6;
            }
        }
        return super.getUpgradeStatus();
    }

    public void copy(DeviceInfoEx deviceInfoEx) {
        this.copy((DeviceInfo)deviceInfoEx);
        this.supportExtValues = deviceInfoEx.supportExtValues;
        this.mTimeStamp = deviceInfoEx.mTimeStamp;
    }

    protected DeviceInfoEx(Parcel in) {
        super(in);
        this.mUserName = in.readString();
        this.mPassword = in.readString();
        this.mSafeModePassword = in.readString();
        this.mCloudSafeModePasswd = in.readString();
        this.mRealPlayType = in.readInt();
        this.mPlayBackType = in.readInt();
        this.operationCode = in.readString();
        this.encryptKey = in.readString();
        this.encryptType = in.readInt();
        this.notifyStatus = in.readInt();
        this.notifyOnline = in.readInt();
        this.upgradeProgress = in.readInt();
        this.upgradeErrorCode = in.readInt();
        this.upgradeInputPwd = in.readString();
        this.upgradeRebootingTime = in.readLong();
        this.mInLan = in.readInt();
        this.mInUpnp = in.readInt();
        this.mTimeStamp = in.readLong();
        int length = in.readInt();
        if (length > 0) {
            this.supportExtValues = new String[length];
            in.readStringArray(this.supportExtValues);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mUserName);
        dest.writeString(this.mPassword);
        dest.writeString(this.mSafeModePassword);
        dest.writeString(this.mCloudSafeModePasswd);
        dest.writeInt(this.mRealPlayType);
        dest.writeInt(this.mPlayBackType);
        dest.writeString(this.operationCode);
        dest.writeString(this.encryptKey);
        dest.writeInt(this.encryptType);
        dest.writeInt(this.notifyStatus);
        dest.writeInt(this.notifyOnline);
        dest.writeInt(this.upgradeProgress);
        dest.writeInt(this.upgradeErrorCode);
        dest.writeString(this.upgradeInputPwd);
        dest.writeLong(this.upgradeRebootingTime);
        dest.writeInt(this.mInLan);
        dest.writeInt(this.mInUpnp);
        dest.writeLong(this.mTimeStamp);
        if (this.supportExtValues == null || this.supportExtValues.length == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(this.supportExtValues.length);
            dest.writeStringArray(this.supportExtValues);
        }
    }

    public boolean isSdcardError() {
        boolean isSdcardError = false;
        if (this.getSupportDisk() != 1 || this.getDiskStatus().length() == 0) {
            isSdcardError = false;
        } else if (DISK_STORAGE_ERROR.indexOf(this.getDiskStatus()) >= 0) {
            isSdcardError = true;
        } else if (DISK_UNFORMATTED.indexOf(this.getDiskStatus()) >= 0) {
            isSdcardError = true;
        } else if (DISK_FORMATTING.indexOf(this.getDiskStatus()) >= 0) {
            isSdcardError = true;
        } else if (DISK_NO_SDCARD.indexOf(this.getDiskStatus()) >= 0) {
            isSdcardError = false;
        } else if (DISK_NORMAL.indexOf(this.getDiskStatus()) >= 0) {
            isSdcardError = false;
        }
        return isSdcardError;
    }

    public int getDirectPlayback_EndFlag() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("DirectPlayback_EndFlag");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public boolean isSupportP2pV3() {
        return this.getV3() == 1 || this.getV3Sec() == 1;
    }

    public int getV3Sec() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("V3Sec");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public int getV3() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("V3");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    private String getEzSupportExtByKey(String key) {
        String ezSupportExt = this.getEzDeviceSupportExt();
        if (ezSupportExt == null) {
            return null;
        }
        String value = null;
        try {
            JSONObject jsonObj = new JSONObject(ezSupportExt);
            value = jsonObj.getString(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getV3Playback() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("V3Playback");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public int getV3Talk() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("V3Talk");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public int getV3Download() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("V3Download");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public boolean getSupportSeekPlayback() {
        int value = 0;
        String valueString = this.getSupportExtByKey("support_seek_playback");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value == 1;
    }

    public boolean getSupportSeekV2() {
        int value = 0;
        String valueString = this.getSupportExtByKey("support_seek_v2");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value == 1;
    }

    public boolean getSupportPtzViaP2pv3() {
        int value = 0;
        String valueString = this.getSupportExtByKey("support_ptzcmd_via_p2pv3");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value == 1;
    }

    public int getSupportChannelTalk() {
        if (PlayAPI.getInstance().isUsingGlobalSDK()) {
            int value = 0;
            String valueString = this.getSupportExtByKey("support_channel_talkback");
            if (!TextUtils.isEmpty((CharSequence)valueString)) {
                try {
                    value = Integer.parseInt(valueString);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return value;
        }
        return this.getSupportInt(192);
    }

    public int getSupportDeviceAutoVideolevel() {
        int value = 0;
        String valueString = this.getSupportExtByKey("support_device_auto_video_level");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public boolean getSupportVoiceChange() {
        return this.getSupportInt(481) == 1;
    }

    public boolean getSupportSdCover() {
        return this.getSupportInt(483) == 1;
    }

    public boolean isSupportMultiChannel() {
        String supportMultiChannelTypeValue = this.getSupportValue(719);
        boolean isSupportMultiChannel = !"-1".equals(supportMultiChannelTypeValue) && supportMultiChannelTypeValue.split("-").length >= 2;
        boolean isSupportMultiPlay = this.getSupportInt(665) > 0;
        return isSupportMultiChannel || isSupportMultiPlay;
    }

    public int needCycleVerifyPermission() {
        int value = 0;
        String valueString = this.getEzSupportExtByKey("ShareVerify");
        if (!TextUtils.isEmpty((CharSequence)valueString)) {
            try {
                value = Integer.parseInt(valueString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}

