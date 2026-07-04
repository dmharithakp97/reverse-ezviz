/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  org.json.JSONObject
 */
package com.videogo.device;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.videogo.device.DeviceOnlineInfo;
import com.videogo.device.DeviceSwitchInfo;
import com.videogo.device.DeviceWifiInfo;
import com.videogo.main.AppManager;
import com.videogo.openapi.annotation.Serializable;
import com.videogo.openapi.bean.EZVtmBackupInfo;
import com.videogo.openapi.bean.P2pDeviceInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class DeviceInfo
implements Parcelable {
    protected static final String TAG = "DeviceInfo";
    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int PRIVATE = 3;
    public static final int SLEEP = 5;
    @Serializable(name="deviceSerial")
    protected String mDeviceID = "";
    @Serializable(name="devlogicId")
    private String devlogicId = "";
    @Serializable(name="deviceIP")
    protected String mDeviceIP = "";
    @Serializable(name="devicePort")
    protected int mDevicePort = 8000;
    @Serializable(name="localIp")
    protected String mLocalDeviceIp = "";
    @Serializable(name="localDevicePort")
    protected int mLocalDevicePort = 8000;
    @Serializable(name="localHttpPort")
    protected int mLocalHttpPort = 80;
    @Serializable(name="httpPort")
    protected int mHttpPort = 80;
    @Serializable(name="deviceStatus")
    protected int mDeviceStatus = 0;
    @Serializable(name="model")
    protected String mModel = "";
    @Serializable(name="maskIp")
    protected String maskIp = "";
    @Serializable(name="netType")
    protected int netType = 1;
    @Serializable(name="ppvsAddr")
    protected String ppvsAddr = "";
    @Serializable(name="ppvsPort")
    protected short ppvsPort = (short)-1;
    @Serializable(name="upnp")
    protected int upnp = -1;
    @Serializable(name="privateStatus")
    protected int privateStatus = -1;
    @Serializable(name="defence")
    protected int defence = -1;
    @Serializable(name="diskNum")
    protected int diskNum = -1;
    @Serializable(name="version")
    protected String version = "";
    @Serializable(name="diskStatus")
    protected String diskStatus = "";
    @Serializable(name="resultCode")
    protected int resultCode = 0;
    @Serializable(name="fullSerial")
    protected String fullSerial;
    @Serializable(name="fullModel")
    protected String fullModel;
    @Serializable(name="isNeedUpgrade")
    protected int isNeedUpgrade;
    @Serializable(name="belongSerial")
    protected String belongSerial;
    @Serializable(name="belongNo")
    protected int belongNo;
    @Serializable(name="belongAdded")
    protected int belongAdded;
    @Serializable(name="belongState")
    protected int belongState;
    @Serializable(name="isEncrypt")
    protected int isEncrypt;
    @Serializable(name="isShared")
    protected String isShared;
    @Serializable(name="isOwner")
    protected int isOwner;
    @Serializable(name="cmdPort")
    protected int cmdPort;
    @Serializable(name="streamPort")
    protected int streamPort;
    @Serializable(name="localCmdPort")
    protected int localCmdPort;
    @Serializable(name="localStreamPort")
    protected int localStreamPort;
    @Serializable(name="casIp")
    protected String casIp = "";
    @Serializable(name="casPort")
    protected int casPort = 0;
    @Serializable(name="encryptPwd")
    protected String encryptPwd = null;
    @Serializable(name="releaseVersion")
    protected String releaseVersion;
    @Serializable(name="name")
    protected String mDeviceName;
    @Serializable(name="supportExt")
    protected String supportExt;
    @Serializable(name="supportExtShort")
    protected String supportExtShort;
    @Serializable(name="ezDeviceSupportExt")
    private String ezDeviceSupportExt = null;
    @Serializable(name="supportWifi")
    protected int supportWifi = 1;
    @Serializable(name="upgradeStatus")
    protected int upgradeStatus = -1;
    @Serializable(name="cloudServiceStatus")
    protected int cloudServiceStatus = 0;
    @Serializable(name="devicePicUrl")
    protected String picUrl = "";
    @Serializable(name="relatedDeviceCount")
    protected int relatedDeviceCount = -1;
    @Serializable(name="unnormalStatus")
    protected int unnormalStatus = -1;
    @Serializable(name="alarmSoundMode")
    protected int alarmSoundMode;
    @Serializable(name="deviceInfoExt")
    protected DeviceOnlineInfo onlineInfo;
    @Serializable(name="offlineNotify")
    protected int offlineNotify;
    @Serializable(name="vtmIp")
    private String vtmIp;
    @Serializable(name="vtmDomain")
    private String vtmDomain;
    @Serializable(name="vtmPort")
    private int vtmPort;
    public ArrayList<EZVtmBackupInfo> vtmBackups;
    @Serializable(name="ttsIp")
    private String ttsIp;
    @Serializable(name="ttsPort")
    private int ttsPort;
    private String casIPAddress = null;
    private String ppvsIPAddress = null;
    private String vtmIPAddress = null;
    private String ttsIPAddress = null;
    @Serializable(name="userDeviceCreateTime")
    private String userDeviceCreateTime = null;
    @Serializable(name="type")
    private String type;
    @Serializable(name="cloudType")
    private int cloudType;
    @Serializable(name="alarmStatus")
    private int alarmStatus = -1;
    @Serializable(name="lightStatus")
    private int lightStatus = -1;
    @Serializable(name="weixinQrcode")
    private String weixinQrcode;
    @Serializable(name="wifiInfoDto")
    private DeviceWifiInfo wifiInfo;
    @Serializable(name="streamStopTimeMs")
    private int streamStopTimeMs = 0;
    @Serializable(name="supportChannelNum")
    private int supportChannelNum;
    @Serializable(name="switches")
    private List<DeviceSwitchInfo> switches;
    @Serializable(name="publicKey")
    private String publicKey;
    @Serializable(name="publicKeyVersion")
    private int publicKeyVersion;
    @Serializable(name="devProtoEnum")
    protected int devProtoEnum;
    private DeviceInfo belongDevice;
    protected P2pDeviceInfo p2pDeviceInfo;
    protected String accessToken;
    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>(){

        public DeviceInfo createFromParcel(Parcel in) {
            return new DeviceInfo(in);
        }

        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };

    public DeviceInfo getBelongDevice() {
        return this.belongDevice;
    }

    public void setBelongDevice(DeviceInfo belongDevice) {
        this.belongDevice = belongDevice;
    }

    public P2pDeviceInfo getP2pDeviceInfo() {
        return this.p2pDeviceInfo;
    }

    public void setP2pDeviceInfo(P2pDeviceInfo p2pDeviceInfo) {
        this.p2pDeviceInfo = p2pDeviceInfo;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public DeviceInfo() {
    }

    public String getDeviceID() {
        return this.mDeviceID;
    }

    public void setDeviceID(String deviceID) {
        this.mDeviceID = deviceID;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    public String getDeviceIP() {
        return this.mDeviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.mDeviceIP = deviceIP;
    }

    public int getDevicePort() {
        return this.mDevicePort;
    }

    public void setDevicePort(int devicePort) {
        this.mDevicePort = devicePort;
    }

    public int getDeviceStatus() {
        return this.mDeviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.mDeviceStatus = deviceStatus;
    }

    public void setHttpPort(int httpPort) {
        this.mHttpPort = httpPort;
    }

    public int getHttpPort() {
        return this.mHttpPort;
    }

    public String getModel() {
        return this.mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getLocalDeviceIp() {
        return this.mLocalDeviceIp;
    }

    public void setLocalDeviceIp(String localDevcieIP) {
        this.mLocalDeviceIp = localDevcieIP;
    }

    public int getLocalDevicePort() {
        return this.mLocalDevicePort;
    }

    public void setLocalDevicePort(int localDevicePort) {
        this.mLocalDevicePort = localDevicePort;
    }

    public void setMaskIp(String maskIp) {
        this.maskIp = maskIp;
    }

    public String getMaskIp() {
        return this.maskIp;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }

    public int getNetType() {
        return this.netType;
    }

    public void setPpvsAddr(String ppvsAddr) {
        this.ppvsAddr = ppvsAddr;
        this.ppvsIPAddress = null;
    }

    public String getPpvsAddr() {
        if (this.ppvsIPAddress != null) {
            return this.ppvsIPAddress;
        }
        if (!Utils.isIp(this.ppvsAddr)) {
            this.ppvsIPAddress = AppManager.getInetAddress(this.ppvsAddr);
        }
        return this.ppvsIPAddress != null ? this.ppvsIPAddress : this.ppvsAddr;
    }

    public void setPpvsPort(short ppvsPort) {
        this.ppvsPort = ppvsPort;
    }

    public short getPpvsPort() {
        return this.ppvsPort;
    }

    public void setUpnp(int upnp) {
        this.upnp = upnp;
    }

    public void setPrivateStatus(int privateStatus) {
        this.privateStatus = privateStatus;
    }

    public int getPrivateStatus() {
        return this.privateStatus;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getDefence() {
        return this.defence;
    }

    public boolean isDefenceOn() {
        return this.defence != 0 && this.defence != 32;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public int getDiskNum() {
        return this.diskNum;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public String getDiskStatus() {
        if ("9".equals(this.diskStatus) && ("V4.1.0 build 130126".equalsIgnoreCase(this.getVersion()) || "0".equalsIgnoreCase(this.getVersion()))) {
            return "0";
        }
        return this.diskStatus;
    }

    public void setDiskStatus(String diskStatus) {
        this.diskStatus = diskStatus;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getFullSerial() {
        return this.fullSerial;
    }

    public void setFullSerial(String fullSerial) {
        this.fullSerial = fullSerial;
    }

    public String getFullModel() {
        return this.fullModel;
    }

    public void setFullModel(String fullModel) {
        this.fullModel = fullModel;
    }

    public void setNeedUpgrade(int isNeedUpgrade) {
        this.isNeedUpgrade = isNeedUpgrade;
    }

    public int getNeedUpgrade() {
        return this.isNeedUpgrade;
    }

    public void setBelongSerial(String belongSerial) {
        this.belongSerial = belongSerial;
    }

    public String getBelongSerial() {
        return this.belongSerial;
    }

    public void setBelongNo(int belongNo) {
        this.belongNo = belongNo;
    }

    public int getBelongNo() {
        return this.belongNo;
    }

    public void setBelongAdded(int belongAdded) {
        this.belongAdded = belongAdded;
    }

    public int getBelongAdded() {
        return this.belongAdded;
    }

    public void setBelongState(int belongState) {
        this.belongState = belongState;
    }

    public int getBelongState() {
        return this.belongState;
    }

    public void setIsEncrypt(int isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public int getIsEncrypt() {
        return this.isEncrypt;
    }

    public String getIsShared() {
        return this.isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public int getIsOwner() {
        return this.isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public void setCmdPort(int cmdPort) {
        this.cmdPort = cmdPort;
    }

    public int getCmdPort() {
        return this.cmdPort;
    }

    public void setStreamPort(int streamPort) {
        this.streamPort = streamPort;
    }

    public int getStreamPort() {
        return this.streamPort;
    }

    public void setLocalCmdPort(int localCmdPort) {
        this.localCmdPort = localCmdPort;
    }

    public int getLocalCmdPort() {
        return this.localCmdPort;
    }

    public void setLocalStreamPort(int localStreamPort) {
        this.localStreamPort = localStreamPort;
    }

    public int getLocalStreamPort() {
        return this.localStreamPort;
    }

    public String getCasIp() {
        if (this.casIPAddress != null) {
            return this.casIPAddress;
        }
        if (!Utils.isIp(this.casIp)) {
            this.casIPAddress = AppManager.getInetAddress(this.casIp);
        }
        return this.casIPAddress != null ? this.casIPAddress : this.casIp;
    }

    public void setCasIp(String casIp) {
        this.casIp = casIp;
        this.casIPAddress = null;
    }

    public int getCasPort() {
        return this.casPort;
    }

    public void setCasPort(int casPort) {
        this.casPort = casPort;
    }

    public String getEncryptPwd() {
        return this.encryptPwd;
    }

    public void setEncryptPwd(String encryptPwd) {
        this.encryptPwd = encryptPwd;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getReleaseVersion() {
        return this.releaseVersion;
    }

    public void setSupportExt(String supportExt) {
        this.supportExt = supportExt;
    }

    public String getSupportExt() {
        return this.supportExt;
    }

    public void setSupportExtShort(String supportExtShort) {
        this.supportExtShort = supportExtShort;
    }

    public String getSupportExtShort() {
        return this.supportExtShort;
    }

    public void setSupportWifi(int supportWifi) {
        this.supportWifi = supportWifi;
    }

    public int getSupportWifi() {
        return this.supportWifi;
    }

    protected void setUpgradeStatus(int upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    protected int getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public void setCloudServiceStatus(int cloudServiceStatus) {
        this.cloudServiceStatus = cloudServiceStatus;
    }

    public int getCloudServiceStatus() {
        return this.cloudServiceStatus;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return LocalInfo.getInstance().getServAddr() + this.picUrl + ".jpeg";
    }

    public int getRelatedDeviceCount() {
        return this.relatedDeviceCount;
    }

    public void setRelatedDeviceCount(int relatedDeviceCount) {
        this.relatedDeviceCount = relatedDeviceCount;
    }

    public int getUnnormalStatus() {
        return this.unnormalStatus;
    }

    public void setUnnormalStatus(int unnormalStatus) {
        this.unnormalStatus = unnormalStatus;
    }

    public int getAlarmSoundMode() {
        return this.alarmSoundMode;
    }

    public void setAlarmSoundMode(int alarmSoundMode) {
        this.alarmSoundMode = alarmSoundMode;
    }

    public DeviceOnlineInfo getOnlineInfo() {
        return this.onlineInfo;
    }

    public void setOnlineInfo(DeviceOnlineInfo onlineInfo) {
        this.onlineInfo = onlineInfo;
    }

    public int getOfflineNotify() {
        return this.offlineNotify;
    }

    public void setOfflineNotify(int offlineNotify) {
        this.offlineNotify = offlineNotify;
    }

    public String getVtmIp() {
        if (!TextUtils.isEmpty((CharSequence)this.vtmIPAddress)) {
            return this.vtmIPAddress;
        }
        if (!Utils.isIp(this.vtmDomain)) {
            this.vtmIPAddress = AppManager.getInetAddress(this.vtmDomain);
        }
        if (TextUtils.isEmpty((CharSequence)this.vtmIPAddress) && !Utils.isIp(this.vtmIp)) {
            this.vtmIPAddress = AppManager.getInetAddress(this.vtmIp);
        }
        return this.vtmIPAddress != null ? this.vtmIPAddress : this.vtmIp;
    }

    public void setVtmIp(String vtmIp) {
        this.vtmIp = vtmIp;
    }

    public void setVtmDomain(String vtmDomain) {
        this.vtmDomain = vtmDomain;
    }

    public int getVtmPort() {
        return this.vtmPort;
    }

    public void setVtmPort(int vtmPort) {
        this.vtmPort = vtmPort;
    }

    public String getTtsIp() {
        if (this.ttsIPAddress != null) {
            return this.ttsIPAddress;
        }
        if (!Utils.isIp(this.ttsIp)) {
            this.ttsIPAddress = AppManager.getInetAddress(this.ttsIp);
        }
        return this.ttsIPAddress != null ? this.ttsIPAddress : this.ttsIp;
    }

    public void setTtsIp(String ttsIp) {
        this.ttsIp = ttsIp;
    }

    public int getTtsPort() {
        return this.ttsPort;
    }

    public void setTtsPort(int ttsPort) {
        this.ttsPort = ttsPort;
    }

    public String getUserDeviceCreateTime() {
        return this.userDeviceCreateTime;
    }

    public void setUserDeviceCreateTime(String userDeviceCreateTime) {
        this.userDeviceCreateTime = userDeviceCreateTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLocalHttpPort() {
        return this.mLocalHttpPort;
    }

    public void setLocalHttpPort(int localHttpPort) {
        this.mLocalHttpPort = localHttpPort;
    }

    public int getCloudType() {
        return this.cloudType;
    }

    public void setCloudType(int cloudType) {
        this.cloudType = cloudType;
    }

    public int getAlarmStatus() {
        return this.alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getLightStatus() {
        return this.lightStatus;
    }

    public void setLightStatus(int lightStatus) {
        this.lightStatus = lightStatus;
    }

    public String getWeixinQrcode() {
        return this.weixinQrcode;
    }

    public void setWeixinQrcode(String weixinQrcode) {
        this.weixinQrcode = weixinQrcode;
    }

    public DeviceWifiInfo getWifiInfo() {
        return this.wifiInfo;
    }

    public void setWifiInfo(DeviceWifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public int getStreamStopTimeMs() {
        return this.streamStopTimeMs;
    }

    public void setStreamStopTimeMs(int streamStopTimeMs) {
        this.streamStopTimeMs = streamStopTimeMs;
    }

    public List<DeviceSwitchInfo> getSwitches() {
        return this.switches;
    }

    public void setSwitches(List<DeviceSwitchInfo> switches) {
        this.switches = switches;
    }

    public void clearIpAddress() {
        this.casIPAddress = null;
        this.ppvsIPAddress = null;
        this.vtmIPAddress = null;
        this.ttsIPAddress = null;
    }

    public void copy(DeviceInfo deviceInfo) {
        this.mLocalDeviceIp = deviceInfo.mLocalDeviceIp;
        this.mLocalDevicePort = deviceInfo.mLocalDevicePort;
        this.mHttpPort = deviceInfo.mHttpPort;
        this.mLocalHttpPort = deviceInfo.mLocalHttpPort;
        this.maskIp = deviceInfo.maskIp;
        this.netType = deviceInfo.netType;
        this.ppvsAddr = deviceInfo.ppvsAddr;
        this.ppvsPort = deviceInfo.ppvsPort;
        this.mModel = deviceInfo.mModel;
        this.upnp = deviceInfo.upnp;
        this.privateStatus = deviceInfo.privateStatus;
        this.defence = deviceInfo.defence;
        this.diskNum = deviceInfo.diskNum;
        this.version = deviceInfo.version;
        this.diskStatus = deviceInfo.diskStatus;
        this.fullSerial = deviceInfo.fullSerial;
        this.fullModel = deviceInfo.fullModel;
        this.isNeedUpgrade = deviceInfo.isNeedUpgrade;
        this.belongSerial = deviceInfo.belongSerial;
        this.belongNo = deviceInfo.belongNo;
        this.belongState = deviceInfo.belongState;
        this.isEncrypt = deviceInfo.isEncrypt;
        this.encryptPwd = deviceInfo.encryptPwd;
        this.isShared = deviceInfo.isShared;
        this.isOwner = deviceInfo.isOwner;
        this.cmdPort = deviceInfo.cmdPort;
        this.streamPort = deviceInfo.streamPort;
        this.localCmdPort = deviceInfo.localCmdPort;
        this.localStreamPort = deviceInfo.localStreamPort;
        this.casIp = deviceInfo.casIp;
        this.casPort = deviceInfo.casPort;
        this.releaseVersion = deviceInfo.releaseVersion;
        this.supportWifi = deviceInfo.supportWifi;
        this.mDeviceStatus = deviceInfo.mDeviceStatus;
        this.cloudServiceStatus = deviceInfo.cloudServiceStatus;
        this.picUrl = deviceInfo.picUrl;
        this.relatedDeviceCount = deviceInfo.relatedDeviceCount;
        this.unnormalStatus = deviceInfo.unnormalStatus;
        this.alarmSoundMode = deviceInfo.alarmSoundMode;
        this.onlineInfo = deviceInfo.onlineInfo;
        this.offlineNotify = deviceInfo.offlineNotify;
        if (this.releaseVersion != null && this.releaseVersion.equalsIgnoreCase("VERSION_17")) {
            this.upgradeStatus = deviceInfo.upgradeStatus;
        }
        this.vtmIp = deviceInfo.vtmIp;
        this.vtmDomain = deviceInfo.vtmDomain;
        this.vtmPort = deviceInfo.vtmPort;
        this.vtmBackups = deviceInfo.vtmBackups;
        this.ttsIp = deviceInfo.ttsIp;
        this.ttsPort = deviceInfo.ttsPort;
        this.mDeviceName = deviceInfo.mDeviceName;
        this.userDeviceCreateTime = deviceInfo.userDeviceCreateTime;
        this.type = deviceInfo.type;
        this.cloudType = deviceInfo.cloudType;
        this.alarmStatus = deviceInfo.alarmStatus;
        this.lightStatus = deviceInfo.lightStatus;
        this.weixinQrcode = deviceInfo.weixinQrcode;
        this.wifiInfo = deviceInfo.wifiInfo;
        this.streamStopTimeMs = deviceInfo.streamStopTimeMs;
        this.supportChannelNum = deviceInfo.supportChannelNum;
        this.supportExtShort = deviceInfo.supportExtShort;
        this.switches = deviceInfo.switches;
        this.publicKey = deviceInfo.publicKey;
        this.p2pDeviceInfo = deviceInfo.p2pDeviceInfo;
        this.belongDevice = deviceInfo.belongDevice;
        this.publicKeyVersion = deviceInfo.publicKeyVersion;
        this.devProtoEnum = deviceInfo.devProtoEnum;
    }

    protected DeviceInfo(Parcel in) {
        this.mDeviceID = in.readString();
        this.mDeviceIP = in.readString();
        this.mDevicePort = in.readInt();
        this.mLocalDeviceIp = in.readString();
        this.mLocalDevicePort = in.readInt();
        this.mLocalHttpPort = in.readInt();
        this.mHttpPort = in.readInt();
        this.mDeviceStatus = in.readInt();
        this.mModel = in.readString();
        this.maskIp = in.readString();
        this.netType = in.readInt();
        this.ppvsAddr = in.readString();
        this.ppvsPort = (Short)in.readValue(Short.TYPE.getClassLoader());
        this.upnp = in.readInt();
        this.privateStatus = in.readInt();
        this.defence = in.readInt();
        this.diskNum = in.readInt();
        this.version = in.readString();
        this.diskStatus = in.readString();
        this.resultCode = in.readInt();
        this.fullSerial = in.readString();
        this.fullModel = in.readString();
        this.isNeedUpgrade = in.readInt();
        this.belongAdded = in.readInt();
        this.belongSerial = in.readString();
        this.belongNo = in.readInt();
        this.belongState = in.readInt();
        this.isEncrypt = in.readInt();
        this.isShared = in.readString();
        this.isOwner = in.readInt();
        this.cmdPort = in.readInt();
        this.streamPort = in.readInt();
        this.localCmdPort = in.readInt();
        this.localStreamPort = in.readInt();
        this.casIp = in.readString();
        this.casPort = in.readInt();
        this.encryptPwd = in.readString();
        this.releaseVersion = in.readString();
        this.mDeviceName = in.readString();
        this.supportExt = in.readString();
        this.supportExtShort = in.readString();
        this.ezDeviceSupportExt = in.readString();
        this.supportWifi = in.readInt();
        this.upgradeStatus = in.readInt();
        this.cloudServiceStatus = in.readInt();
        this.picUrl = in.readString();
        this.relatedDeviceCount = in.readInt();
        this.unnormalStatus = in.readInt();
        this.alarmSoundMode = in.readInt();
        this.onlineInfo = (DeviceOnlineInfo)in.readValue(DeviceOnlineInfo.class.getClassLoader());
        this.offlineNotify = in.readInt();
        this.vtmIp = in.readString();
        this.vtmDomain = in.readString();
        this.vtmPort = in.readInt();
        this.vtmBackups = in.createTypedArrayList(EZVtmBackupInfo.CREATOR);
        this.ttsIp = in.readString();
        this.ttsPort = in.readInt();
        this.casIPAddress = in.readString();
        this.ppvsIPAddress = in.readString();
        this.vtmIPAddress = in.readString();
        this.ttsIPAddress = in.readString();
        this.userDeviceCreateTime = in.readString();
        this.type = in.readString();
        this.cloudType = in.readInt();
        this.alarmStatus = in.readInt();
        this.lightStatus = in.readInt();
        this.weixinQrcode = in.readString();
        this.wifiInfo = (DeviceWifiInfo)in.readValue(DeviceWifiInfo.class.getClassLoader());
        this.streamStopTimeMs = in.readInt();
        this.supportChannelNum = in.readInt();
        this.switches = new ArrayList<DeviceSwitchInfo>();
        in.readTypedList(this.switches, DeviceSwitchInfo.CREATOR);
        this.publicKey = in.readString();
        this.publicKeyVersion = in.readInt();
        this.devProtoEnum = in.readInt();
        this.devlogicId = in.readString();
        this.p2pDeviceInfo = (P2pDeviceInfo)in.readValue(P2pDeviceInfo.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDeviceID);
        dest.writeString(this.mDeviceIP);
        dest.writeInt(this.mDevicePort);
        dest.writeString(this.mLocalDeviceIp);
        dest.writeInt(this.mLocalDevicePort);
        dest.writeInt(this.mLocalHttpPort);
        dest.writeInt(this.mHttpPort);
        dest.writeInt(this.mDeviceStatus);
        dest.writeString(this.mModel);
        dest.writeString(this.maskIp);
        dest.writeInt(this.netType);
        dest.writeString(this.ppvsAddr);
        dest.writeValue((Object)this.ppvsPort);
        dest.writeInt(this.upnp);
        dest.writeInt(this.privateStatus);
        dest.writeInt(this.defence);
        dest.writeInt(this.diskNum);
        dest.writeString(this.version);
        dest.writeString(this.diskStatus);
        dest.writeInt(this.resultCode);
        dest.writeString(this.fullSerial);
        dest.writeString(this.fullModel);
        dest.writeInt(this.isNeedUpgrade);
        dest.writeInt(this.belongAdded);
        dest.writeString(this.belongSerial);
        dest.writeInt(this.belongNo);
        dest.writeInt(this.belongState);
        dest.writeInt(this.isEncrypt);
        dest.writeString(this.isShared);
        dest.writeInt(this.isOwner);
        dest.writeInt(this.cmdPort);
        dest.writeInt(this.streamPort);
        dest.writeInt(this.localCmdPort);
        dest.writeInt(this.localStreamPort);
        dest.writeString(this.casIp);
        dest.writeInt(this.casPort);
        dest.writeString(this.encryptPwd);
        dest.writeString(this.releaseVersion);
        dest.writeString(this.mDeviceName);
        dest.writeString(this.supportExt);
        dest.writeString(this.supportExtShort);
        dest.writeString(this.ezDeviceSupportExt);
        dest.writeInt(this.supportWifi);
        dest.writeInt(this.upgradeStatus);
        dest.writeInt(this.cloudServiceStatus);
        dest.writeString(this.picUrl);
        dest.writeInt(this.relatedDeviceCount);
        dest.writeInt(this.unnormalStatus);
        dest.writeInt(this.alarmSoundMode);
        dest.writeValue((Object)this.onlineInfo);
        dest.writeInt(this.offlineNotify);
        dest.writeString(this.vtmIp);
        dest.writeString(this.vtmDomain);
        dest.writeInt(this.vtmPort);
        dest.writeTypedList(this.vtmBackups);
        dest.writeString(this.ttsIp);
        dest.writeInt(this.ttsPort);
        dest.writeString(this.casIPAddress);
        dest.writeString(this.ppvsIPAddress);
        dest.writeString(this.vtmIPAddress);
        dest.writeString(this.ttsIPAddress);
        dest.writeString(this.userDeviceCreateTime);
        dest.writeString(this.type);
        dest.writeInt(this.cloudType);
        dest.writeInt(this.alarmStatus);
        dest.writeInt(this.lightStatus);
        dest.writeString(this.weixinQrcode);
        dest.writeValue((Object)this.wifiInfo);
        dest.writeInt(this.streamStopTimeMs);
        dest.writeInt(this.supportChannelNum);
        dest.writeTypedList(this.switches);
        dest.writeString(this.publicKey);
        dest.writeInt(this.publicKeyVersion);
        dest.writeInt(this.devProtoEnum);
        dest.writeString(this.devlogicId);
        dest.writeValue((Object)this.p2pDeviceInfo);
    }

    public int getSupportChannelNum() {
        return this.supportChannelNum;
    }

    public void setSupportChannelNum(int supportChannelNum) {
        this.supportChannelNum = supportChannelNum;
    }

    public String getDevlogicId() {
        return this.devlogicId;
    }

    public void setDevlogicId(String devlogicId) {
        this.devlogicId = devlogicId;
    }

    public String getEzDeviceSupportExt() {
        return this.ezDeviceSupportExt;
    }

    public void setEzDeviceSupportExt(String ezDeviceSupportExt) {
        this.ezDeviceSupportExt = ezDeviceSupportExt;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getPublicKeyVersion() {
        return this.publicKeyVersion;
    }

    public void setPublicKeyVersion(int publicKeyVersion) {
        this.publicKeyVersion = publicKeyVersion;
    }

    public int getDevProtoEnum() {
        return this.devProtoEnum;
    }

    public void setDevProtoEnum(int devProtoEnum) {
        this.devProtoEnum = devProtoEnum;
    }

    public String getSupportExtByKey(String key) {
        String supportExt = this.getSupportExt();
        if (supportExt == null) {
            return null;
        }
        String value = null;
        try {
            JSONObject jsonObj = new JSONObject(supportExt);
            value = jsonObj.getString(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getLinkEncryptByStreamSource(String source) {
        String support_ecdh_v2 = this.getSupportExtByKey("support_ecdh_v2");
        if (support_ecdh_v2 != null) {
            String[] abilities;
            for (String each : abilities = support_ecdh_v2.split(",")) {
                if (each.equals("0")) {
                    return 0;
                }
                if (each.equals("1") && source.equals("1")) {
                    return 1;
                }
                if (each.equals("2") && source.equals("2")) {
                    return 1;
                }
                if (!each.equals("3") || !source.equals("3")) continue;
                return 1;
            }
        }
        return 0;
    }
}

