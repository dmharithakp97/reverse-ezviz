/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.text.TextUtils
 */
package com.videogo.alarm;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.videogo.alarm.AlarmLogInfoEx;
import com.videogo.openapi.bean.resp.AlarmInfo;
import com.videogo.openapi.bean.resp.CameraInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlarmLogInfoManager {
    private static final String TAG = "AlarmLogInfoManager";
    private static AlarmLogInfoManager mAlarmLogInfoManager = null;
    private List<AlarmLogInfoEx> mAlarmListFromNotifier = Collections.synchronizedList(new ArrayList());
    private List<AlarmLogInfoEx> mDeviceOfflineAlarmList = null;
    private List<AlarmLogInfoEx> mAllOutsideAlarmList = Collections.synchronizedList(new ArrayList());

    private AlarmLogInfoManager() {
        this.mDeviceOfflineAlarmList = Collections.synchronizedList(new ArrayList());
    }

    public static AlarmLogInfoManager getInstance() {
        if (null == mAlarmLogInfoManager) {
            mAlarmLogInfoManager = new AlarmLogInfoManager();
        }
        return mAlarmLogInfoManager;
    }

    public List<AlarmLogInfoEx> getNotifierDsiplayAlarmInfoList() {
        ArrayList<AlarmLogInfoEx> alarmInfoList = new ArrayList<AlarmLogInfoEx>();
        AlarmLogInfoEx alarmLogInfo = null;
        AlarmLogInfoEx firstAlarmLogInfo = null;
        boolean first = true;
        for (int i = this.mAlarmListFromNotifier.size() - 1; i >= 0; --i) {
            first = true;
            alarmLogInfo = this.mAlarmListFromNotifier.get(i);
            alarmLogInfo.setAlarmNum(1);
            for (int j = 0; j < alarmInfoList.size(); ++j) {
                firstAlarmLogInfo = (AlarmLogInfoEx)alarmInfoList.get(j);
                if (alarmLogInfo.getNotifyType() != firstAlarmLogInfo.getNotifyType() || !alarmLogInfo.getDeviceSerial().equals(firstAlarmLogInfo.getDeviceSerial()) || alarmLogInfo.getChannelNo() != firstAlarmLogInfo.getChannelNo()) continue;
                firstAlarmLogInfo.setAlarmNum(firstAlarmLogInfo.getAlarmNum() + 1);
                first = false;
                break;
            }
            if (!first) continue;
            alarmInfoList.add(alarmLogInfo);
        }
        if (this.mDeviceOfflineAlarmList != null && this.mDeviceOfflineAlarmList.size() > 0) {
            alarmInfoList.addAll(this.mDeviceOfflineAlarmList);
        }
        Collections.reverse(alarmInfoList);
        return alarmInfoList;
    }

    public List<AlarmLogInfoEx> getAlarmLogInfoFromNotifier() {
        return this.mAlarmListFromNotifier;
    }

    public List<AlarmLogInfoEx> getPushListFromNotifierByCamera(Context context, String deviceSerial, int channelNo, int notifyType) {
        ArrayList<AlarmLogInfoEx> pushList = new ArrayList<AlarmLogInfoEx>();
        boolean listChange = false;
        int count = this.mAlarmListFromNotifier.size();
        for (int i = 0; i < count; ++i) {
            AlarmLogInfoEx alarmIO = this.mAlarmListFromNotifier.get(i);
            if (!alarmIO.getDeviceSerial().equalsIgnoreCase(deviceSerial) || alarmIO.getChannelNo() != channelNo || alarmIO.getNotifyType() != notifyType) continue;
            pushList.add(0, alarmIO);
            this.mAlarmListFromNotifier.remove(alarmIO);
            listChange = true;
            --i;
            --count;
        }
        if (context != null && listChange) {
            Intent intent = new Intent();
            intent.setAction("com.vedeogo.action.NOTIFIER_ALARM_LIST_CHANGE_ACTION");
            context.sendBroadcast(intent);
        }
        return pushList;
    }

    public void clearAlarmListFromNotifier() {
        this.mAlarmListFromNotifier.clear();
    }

    public void clearNotifierMessageList(int notifyType) {
        int count = this.mAlarmListFromNotifier.size();
        for (int i = 0; i < count; ++i) {
            AlarmLogInfoEx alarmIO = this.mAlarmListFromNotifier.get(i);
            if (alarmIO.getNotifyType() != notifyType) continue;
            this.mAlarmListFromNotifier.remove(alarmIO);
            --i;
            --count;
        }
    }

    public void insertAlarmLogInfoFromNotifier(Context context, AlarmLogInfoEx alarmLogInfo) {
        this.mAlarmListFromNotifier.add(alarmLogInfo);
    }

    public void clearAllOutsideAlarmList() {
        this.mAllOutsideAlarmList.clear();
    }

    public void insertAllOutsideAlarmList(Context context, AlarmLogInfoEx alarmLogInfo) {
        this.mAllOutsideAlarmList.add(0, alarmLogInfo);
    }

    public List<AlarmLogInfoEx> getAllOutsideAlarmList() {
        return this.mAllOutsideAlarmList;
    }

    public List<AlarmLogInfoEx> getAllOutsideAlarmList(int type) {
        ArrayList<AlarmLogInfoEx> temp = new ArrayList<AlarmLogInfoEx>();
        for (int i = 0; i < this.mAllOutsideAlarmList.size(); ++i) {
            if (this.mAllOutsideAlarmList.get(i).getNotifyType() != type) continue;
            temp.add(this.mAllOutsideAlarmList.get(i));
        }
        return temp;
    }

    public void clearDeviceOfflineAlarmList() {
        this.mDeviceOfflineAlarmList.clear();
    }

    public void insertDeviceOfflineAlarmList(Context context, AlarmLogInfoEx alarmLogInfo) {
        AlarmLogInfoEx firstAlarmLogInfo = null;
        for (int j = 0; j < this.mDeviceOfflineAlarmList.size(); ++j) {
            firstAlarmLogInfo = this.mDeviceOfflineAlarmList.get(j);
            if (!alarmLogInfo.getDeviceSerial().equals(firstAlarmLogInfo.getDeviceSerial()) || alarmLogInfo.getChannelNo() != firstAlarmLogInfo.getChannelNo()) continue;
            this.mDeviceOfflineAlarmList.remove(j);
            break;
        }
        this.mDeviceOfflineAlarmList.add(alarmLogInfo);
    }

    public List<AlarmLogInfoEx> getDeviceOfflineAlarmList() {
        return this.mDeviceOfflineAlarmList;
    }

    public boolean isAlarmLogInfoExist(AlarmLogInfoEx alarmInfo) {
        if (alarmInfo == null) {
            return false;
        }
        int messageType = alarmInfo.getNotifyType();
        switch (messageType) {
            case 1: 
            case 2: {
                for (AlarmLogInfoEx info : this.mAlarmListFromNotifier) {
                    if (!info.getDeviceSerial().equals(alarmInfo.getDeviceSerial()) || !info.getAlarmOccurTime().equals(alarmInfo.getAlarmOccurTime()) || info.getAlarmType() != alarmInfo.getAlarmType()) continue;
                    return true;
                }
                for (AlarmLogInfoEx info : this.mAllOutsideAlarmList) {
                    if (!info.getDeviceSerial().equals(alarmInfo.getDeviceSerial()) || !info.getAlarmOccurTime().equals(alarmInfo.getAlarmOccurTime()) || info.getAlarmType() != alarmInfo.getAlarmType()) continue;
                    return true;
                }
                break;
            }
            case 3: {
                for (AlarmLogInfoEx info : this.mDeviceOfflineAlarmList) {
                    if (!info.getDeviceSerial().equals(alarmInfo.getDeviceSerial()) || !info.getAlarmOccurTime().equals(alarmInfo.getAlarmOccurTime()) || info.getAlarmType() != alarmInfo.getAlarmType()) continue;
                    return true;
                }
                for (AlarmLogInfoEx info : this.mAllOutsideAlarmList) {
                    if (!info.getDeviceSerial().equals(alarmInfo.getDeviceSerial()) || !info.getAlarmOccurTime().equals(alarmInfo.getAlarmOccurTime()) || info.getAlarmType() != alarmInfo.getAlarmType()) continue;
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void insertNewAlarmLogInfo(Context context, AlarmLogInfoEx alarmLogInfo, boolean inside) {
        if (alarmLogInfo == null) {
            return;
        }
        int messageType = alarmLogInfo.getNotifyType();
        switch (messageType) {
            case 1: {
                this.insertAlarmPush(context, alarmLogInfo, inside);
                break;
            }
            case 2: {
                this.insertMessagePush(context, alarmLogInfo, inside);
                break;
            }
            case 3: {
                this.insertDevicePush(context, alarmLogInfo, inside);
                break;
            }
        }
    }

    private void insertAlarmPush(Context context, AlarmLogInfoEx alarmLogInfo, boolean inside) {
        if (inside) {
            this.insertAlarmLogInfoFromNotifier(context, alarmLogInfo);
        } else {
            this.insertAllOutsideAlarmList(context, alarmLogInfo);
        }
    }

    private void insertMessagePush(Context context, AlarmLogInfoEx alarmLogInfo, boolean inside) {
        if (inside) {
            this.insertAlarmLogInfoFromNotifier(context, alarmLogInfo);
        } else {
            this.insertAllOutsideAlarmList(context, alarmLogInfo);
        }
    }

    private void insertDevicePush(Context context, AlarmLogInfoEx alarmLogInfo, boolean inside) {
        if (inside) {
            this.insertDeviceOfflineAlarmList(context, alarmLogInfo);
        } else {
            this.insertAllOutsideAlarmList(context, alarmLogInfo);
        }
    }

    public List<AlarmInfo> getAlarmInfoListFromPush(Context context, String cameraId, boolean inside) {
        ArrayList<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
        AlarmLogInfoEx alarmLogInfo = null;
        CameraInfo cameraInfo = null;
        if (inside) {
            boolean listChange = false;
            int count = this.mAlarmListFromNotifier.size();
            for (int i = 0; i < count; ++i) {
                alarmLogInfo = this.mAlarmListFromNotifier.get(i);
                cameraInfo = alarmLogInfo.getCameraInfo();
                if (cameraInfo == null || !TextUtils.equals((CharSequence)cameraInfo.getCameraId(), (CharSequence)cameraId)) continue;
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setAlarmName(alarmLogInfo.getObjectName());
                alarmInfo.setAlarmStart(alarmLogInfo.getAlarmStartTime());
                alarmInfo.setAlarmType(alarmLogInfo.getAlarmType());
                alarmInfo.setAlarmPicUrl(alarmLogInfo.getAlarmPicUrl());
                alarmInfo.setAlarmIsCloud(alarmLogInfo.getAlarmCloud());
                alarmInfo.setAlarmIsEncyption(alarmLogInfo.getAlarmEncryption());
                alarmInfo.setDeviceSerial(alarmLogInfo.getDeviceSerial());
                alarmInfo.setCheckSum(alarmLogInfo.getCheckSum());
                alarmInfoList.add(alarmInfo);
                this.mAlarmListFromNotifier.remove(alarmLogInfo);
                listChange = true;
                --i;
                --count;
            }
            if (context != null && listChange) {
                Intent intent = new Intent();
                intent.setAction("com.vedeogo.action.NOTIFIER_ALARM_LIST_CHANGE_ACTION");
                context.sendBroadcast(intent);
            }
        } else {
            int count = this.mAllOutsideAlarmList.size();
            for (int i = 0; i < count; ++i) {
                alarmLogInfo = this.mAllOutsideAlarmList.get(i);
                if (alarmLogInfo.getNotifyType() != 1 || (cameraInfo = alarmLogInfo.getCameraInfo()) == null || !TextUtils.equals((CharSequence)cameraInfo.getCameraId(), (CharSequence)cameraId)) continue;
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setAlarmName(alarmLogInfo.getObjectName());
                alarmInfo.setAlarmStart(alarmLogInfo.getAlarmStartTime());
                alarmInfo.setAlarmType(alarmLogInfo.getAlarmType());
                alarmInfo.setAlarmPicUrl(alarmLogInfo.getAlarmPicUrl());
                alarmInfo.setAlarmIsCloud(alarmLogInfo.getAlarmCloud());
                alarmInfo.setAlarmIsEncyption(alarmLogInfo.getAlarmEncryption());
                alarmInfo.setDeviceSerial(alarmLogInfo.getDeviceSerial());
                alarmInfo.setCheckSum(alarmLogInfo.getCheckSum());
                alarmInfoList.add(alarmInfo);
                this.mAllOutsideAlarmList.remove(alarmLogInfo);
                --i;
                --count;
            }
        }
        return alarmInfoList;
    }
}

