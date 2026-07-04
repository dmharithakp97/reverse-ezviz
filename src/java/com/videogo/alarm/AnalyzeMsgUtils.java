/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.Uri
 *  android.text.TextUtils
 */
package com.videogo.alarm;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.videogo.alarm.AlarmLogInfoEx;
import com.videogo.alarm.BaseMessageInfo;
import com.videogo.alarm.NoticeInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AnalyzeMsgUtils {
    private static final String TAG = "AnalyzePushMessageManager";
    public static final String NOTIFICATION_EXT = "NOTIFICATION_EXT";
    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
    private static final String URL_ENCODE_PREFIX = "hik$shipin7#1#USK#";

    public static BaseMessageInfo getAlarmLogInfoFromPushMsg(Intent intent) {
        String notificationExt = intent.getStringExtra(NOTIFICATION_EXT);
        String notificationMessage = intent.getStringExtra(NOTIFICATION_MESSAGE);
        if (TextUtils.isEmpty((CharSequence)notificationExt) || TextUtils.isEmpty((CharSequence)notificationMessage)) {
            return null;
        }
        LogUtil.i(TAG, "notificationMessage=" + notificationMessage);
        LogUtil.i(TAG, "notificationExt=" + notificationExt);
        String[] fields = notificationExt.split(",");
        if (fields.length == 0) {
            return null;
        }
        int messageType = -1;
        try {
            messageType = Integer.parseInt(fields[0]);
        }
        catch (NumberFormatException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        switch (messageType) {
            case 1: {
                AlarmLogInfoEx alarmLogInfoEx = AnalyzeMsgUtils.createAlarmLogInfo(notificationMessage, fields);
                return alarmLogInfoEx;
            }
            case 2: {
                return AnalyzeMsgUtils.analyzLeaveMsg(notificationMessage, fields);
            }
            case 3: {
                return AnalyzeMsgUtils.analyzDeviceMsg(notificationMessage, fields);
            }
            case 4: {
                return AnalyzeMsgUtils.analyzSystemMsg(notificationMessage, fields);
            }
        }
        return null;
    }

    private static NoticeInfo analyzSystemMsg(String notificationMessage, String[] fields) {
        if (fields.length < 4) {
            LogUtil.e("NotificationReceiver", "messageType is not 4 or fields.length < 4");
            return null;
        }
        if (fields[0] == null || fields[1] == null || fields[3] == null) {
            LogUtil.e(TAG, "messageExt is not right");
            return null;
        }
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setNotifyType(4);
        noticeInfo.setInfoType(Integer.parseInt(fields[1]));
        noticeInfo.setInfoContant(notificationMessage);
        if (fields[3] != null) {
            noticeInfo.setUrl1(fields[3]);
        }
        if (fields.length >= 5 && fields[4] != null) {
            noticeInfo.setUrl2(fields[4]);
        }
        return noticeInfo;
    }

    private static AlarmLogInfoEx analyzDeviceMsg(String notificationMessage, String[] fields) {
        if (fields.length < 4) {
            LogUtil.e("NotificationReceiver", "messageType is not 3 or fields.length < 4");
            return null;
        }
        String msgOccurTime = fields[2];
        String deviceSerial = null;
        if (fields[3] != null) {
            deviceSerial = fields[3];
        }
        int channelNo = -1;
        if (fields[4] != null && !fields[4].isEmpty()) {
            try {
                channelNo = Integer.parseInt(fields[3]);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        AlarmLogInfoEx alarmLogInfo = new AlarmLogInfoEx();
        alarmLogInfo.setNotifyType(3);
        alarmLogInfo.setObjectName(notificationMessage);
        alarmLogInfo.setAlarmOccurTime(msgOccurTime);
        alarmLogInfo.setAlarmStartTime(msgOccurTime);
        alarmLogInfo.setDeviceSerial(deviceSerial);
        alarmLogInfo.setChannelNo(channelNo);
        return alarmLogInfo;
    }

    private static AlarmLogInfoEx analyzLeaveMsg(String notificationMessage, String[] fields) {
        if (fields.length < 5) {
            LogUtil.e("NotificationReceiver", "messageType is not 2 or fields.length < 5");
            return null;
        }
        if (fields[0] == null || fields[1] == null || fields[2] == null) {
            LogUtil.e(TAG, "messageExt is not right");
            return null;
        }
        String msgOccurTime = fields[1];
        String deviceSerial = fields[2];
        int channelNo = -1;
        if (fields[3] != null && !fields[3].isEmpty()) {
            try {
                channelNo = Integer.parseInt(fields[3]);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        int msgType = -1;
        try {
            msgType = Integer.parseInt(fields[4]);
        }
        catch (NumberFormatException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        if (msgType == -1) {
            LogUtil.e(TAG, "msgType is not OK");
            return null;
        }
        String uuid = fields[5];
        String leaveLen = fields.length > 6 ? fields[6] : null;
        AlarmLogInfoEx alarmLogInfo = new AlarmLogInfoEx();
        alarmLogInfo.setNotifyType(2);
        alarmLogInfo.setAlarmLogId(uuid);
        alarmLogInfo.setObjectName(notificationMessage);
        alarmLogInfo.setAlarmOccurTime(msgOccurTime);
        alarmLogInfo.setAlarmStartTime(msgOccurTime);
        alarmLogInfo.setDeviceSerial(deviceSerial);
        alarmLogInfo.setChannelNo(channelNo);
        alarmLogInfo.setAlarmType(msgType);
        if (leaveLen != null && !"".equals(leaveLen)) {
            try {
                alarmLogInfo.setLeaveLen(Integer.parseInt(leaveLen));
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return alarmLogInfo;
    }

    private static AlarmLogInfoEx createAlarmLogInfo(String notificationMessage, String[] fields) {
        if (fields.length < 5) {
            LogUtil.e("NotificationReceiver", "messageType is not 1 or fields.length < 5");
            return null;
        }
        String alarmOccurTime = fields[1];
        String deviceSerial = fields[2];
        int channelNo = -1;
        if (fields[3] != null && !fields[3].isEmpty()) {
            try {
                channelNo = Integer.parseInt(fields[3]);
            }
            catch (NumberFormatException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        int alarmType = -1;
        try {
            alarmType = Integer.parseInt(fields[4]);
        }
        catch (NumberFormatException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        if (alarmType == -1) {
            LogUtil.e(TAG, "alarmType is not OK");
            return null;
        }
        String alarmPicUrl = fields.length > 5 ? fields[5] : null;
        String alarmRecUrl = fields.length > 6 ? fields[6] : null;
        AlarmLogInfoEx alarmLogInfo = new AlarmLogInfoEx();
        alarmLogInfo.setNotifyType(1);
        alarmLogInfo.setObjectName(notificationMessage);
        alarmLogInfo.setAlarmOccurTime(alarmOccurTime);
        alarmLogInfo.setAlarmStartTime(alarmOccurTime);
        alarmLogInfo.setDeviceSerial(deviceSerial);
        alarmLogInfo.setChannelNo(channelNo);
        alarmLogInfo.setAlarmType(alarmType);
        alarmLogInfo.setCheckState(0);
        if (alarmPicUrl != null) {
            AnalyzeMsgUtils.getAlarmPicUrlFromPush(deviceSerial, alarmPicUrl, alarmLogInfo);
        }
        if (alarmRecUrl != null) {
            alarmLogInfo.setAlarmRecUrl(alarmRecUrl);
        }
        return alarmLogInfo;
    }

    public static void getAlarmPicUrlFromPush(String deviceSerial, String infoPicUrl, AlarmLogInfoEx alarmLogInfo) {
        if (TextUtils.isEmpty((CharSequence)infoPicUrl)) {
            return;
        }
        LogUtil.i(TAG, infoPicUrl);
        String session = LocalInfo.getInstance().getEZAccesstoken().getDeviceToken(deviceSerial);
        String alarmPicUrl = null;
        String alarmOriginalPicUrl = null;
        boolean isEncrypted = false;
        if (infoPicUrl.length() > 2 && infoPicUrl.indexOf("_e") == infoPicUrl.length() - 2) {
            isEncrypted = true;
            infoPicUrl = TextUtils.substring((CharSequence)infoPicUrl, (int)0, (int)(infoPicUrl.length() - 2));
        }
        if (infoPicUrl.contains("/p/")) {
            alarmOriginalPicUrl = infoPicUrl.contains("://") ? infoPicUrl + "_" + deviceSerial + "_" + session : "http://" + infoPicUrl + "_" + deviceSerial + "_" + session;
            alarmPicUrl = alarmOriginalPicUrl;
        } else {
            int start = TextUtils.indexOf((CharSequence)infoPicUrl, (CharSequence)"/c/");
            String shipinhost = null;
            String shortPicUrl = null;
            if (start > 1) {
                shipinhost = TextUtils.substring((CharSequence)infoPicUrl, (int)0, (int)start);
                shortPicUrl = TextUtils.substring((CharSequence)infoPicUrl, (int)(start + 3), (int)infoPicUrl.length());
            }
            if (shipinhost != null && shortPicUrl != null) {
                String urlEncode = null;
                try {
                    urlEncode = URLEncoder.encode(URL_ENCODE_PREFIX, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
                infoPicUrl = shipinhost + "/api/cloud?method=susdownload&sus=" + shortPicUrl + "_" + deviceSerial + "_" + session + "&session=" + urlEncode + session;
                alarmOriginalPicUrl = infoPicUrl.contains("://") ? infoPicUrl : "https://" + infoPicUrl;
                alarmPicUrl = alarmOriginalPicUrl;
            }
        }
        alarmLogInfo.setAlarmPicUrl(alarmPicUrl);
        alarmLogInfo.setAlarmIsEncyption(isEncrypted);
        LogUtil.i(TAG, "AlarmPicUrl=" + alarmLogInfo.getAlarmPicUrl());
    }

    public static String getAlarmPicUrlFromPush(String deviceSerial, String infoPicUrl) {
        if (TextUtils.isEmpty((CharSequence)infoPicUrl)) {
            return null;
        }
        LogUtil.i(TAG, infoPicUrl);
        String session = LocalInfo.getInstance().getEZAccesstoken().getDeviceToken(deviceSerial);
        String alarmPicUrl = null;
        String alarmOriginalPicUrl = null;
        boolean isEncrypted = false;
        if (infoPicUrl.length() > 2 && infoPicUrl.indexOf("_e") == infoPicUrl.length() - 2) {
            isEncrypted = true;
            infoPicUrl = TextUtils.substring((CharSequence)infoPicUrl, (int)0, (int)(infoPicUrl.length() - 2));
        }
        if (infoPicUrl.contains("/p/")) {
            alarmOriginalPicUrl = infoPicUrl.contains("://") ? infoPicUrl + "_" + deviceSerial + "_" + session : "http://" + infoPicUrl + "_" + deviceSerial + "_" + session;
            alarmPicUrl = alarmOriginalPicUrl;
        } else {
            int start = TextUtils.indexOf((CharSequence)infoPicUrl, (CharSequence)"/c/");
            String shipinhost = null;
            String shortPicUrl = null;
            if (start > 1) {
                shipinhost = TextUtils.substring((CharSequence)infoPicUrl, (int)0, (int)start);
                shortPicUrl = TextUtils.substring((CharSequence)infoPicUrl, (int)(start + 3), (int)infoPicUrl.length());
            }
            if (shipinhost != null && shortPicUrl != null) {
                String urlEncode = null;
                try {
                    urlEncode = URLEncoder.encode(URL_ENCODE_PREFIX, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                }
                infoPicUrl = shipinhost + "/api/cloud?method=susdownload&sus=" + shortPicUrl + "_" + deviceSerial + "_" + session + "&session=" + urlEncode + session;
                alarmOriginalPicUrl = infoPicUrl.contains("://") ? infoPicUrl : "https://" + infoPicUrl;
                alarmPicUrl = alarmOriginalPicUrl;
            }
        }
        LogUtil.i(TAG, "AlarmPicUrl=" + alarmPicUrl);
        return alarmPicUrl;
    }

    public static boolean getAlarmPicIsEncrypted(String infoPicUrl) {
        boolean isEncrypted = false;
        if (TextUtils.isEmpty((CharSequence)infoPicUrl)) {
            return isEncrypted;
        }
        if (infoPicUrl.length() > 2 && infoPicUrl.indexOf("_e") == infoPicUrl.length() - 2) {
            isEncrypted = true;
        } else {
            int ret = 0;
            try {
                Uri uri = Uri.parse((String)infoPicUrl);
                ret = Integer.parseInt(uri.getQueryParameter("isEncrypted"));
            }
            catch (Exception e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
            if (ret == 1) {
                isEncrypted = true;
            }
        }
        return isEncrypted;
    }
}

