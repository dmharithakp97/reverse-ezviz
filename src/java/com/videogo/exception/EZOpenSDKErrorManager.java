/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.ez.stream.JsonUtils
 */
package com.videogo.exception;

import android.text.TextUtils;
import com.ez.stream.JsonUtils;
import com.ezviz.opensdk.data.EZDatabaseManager;
import com.videogo.exception.BaseException;
import com.videogo.exception.EZOpenSDKErrorInfo;
import com.videogo.openapi.BaseAPI;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EZOpenSDKErrorManager {
    private ExecutorService cachedThreadPool = Executors.newSingleThreadExecutor();
    private static EZOpenSDKErrorManager mamager;

    public static EZOpenSDKErrorManager getMamager() {
        if (mamager == null) {
            mamager = new EZOpenSDKErrorManager();
        }
        return mamager;
    }

    private EZOpenSDKErrorManager() {
    }

    public void getEZOpenSDKErrorInfoList() {
        ArrayList<EZOpenSDKErrorInfo> errorInfos = EZDatabaseManager.getInstance().searchAllData();
        if (errorInfos != null && errorInfos.size() > 0) {
            for (int i = 0; i < errorInfos.size(); ++i) {
                LogUtil.d("searchAllData", JsonUtils.toJson((Object)errorInfos.get(i)));
            }
        }
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    List<EZOpenSDKErrorInfo> list = BaseAPI.getInstance().getErrorList(EZDatabaseManager.getInstance().getErrorTableVersion());
                    if (list != null) {
                        for (int i = 0; i < list.size(); ++i) {
                            EZDatabaseManager.getInstance().updateData(list.get(i));
                        }
                        EZDatabaseManager.getInstance().setErrorTableVersion(String.valueOf(list.get((int)0).updateTime));
                    }
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getEZOpenSDKErrorInfoList(final String time) {
        this.cachedThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    List<EZOpenSDKErrorInfo> list = BaseAPI.getInstance().getErrorList(time);
                    long version = 0L;
                    String versionStr = EZDatabaseManager.getInstance().getErrorTableVersion();
                    if (!TextUtils.isEmpty((CharSequence)versionStr)) {
                        version = Long.parseLong(versionStr);
                    }
                    if (list != null && list.size() > 0) {
                        if (list.get((int)0).updateTime == version) {
                            return;
                        }
                        for (int i = 0; i < list.size(); ++i) {
                            EZDatabaseManager.getInstance().updateData(list.get(i));
                        }
                        if (version < list.get((int)0).updateTime) {
                            EZDatabaseManager.getInstance().setErrorTableVersion(String.valueOf(list.get((int)0).updateTime));
                        }
                    }
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EZOpenSDKErrorInfo getEZOpenSDKErrorInfo(final String detailedCode, boolean report) {
        EZOpenSDKErrorInfo errorInfo = null;
        errorInfo = EZDatabaseManager.getInstance().searchData(detailedCode);
        if (errorInfo == null && report) {
            this.cachedThreadPool.submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        EZOpenSDKErrorInfo errorInfo1 = BaseAPI.getInstance().getErrorInfo(String.valueOf(detailedCode));
                        EZDatabaseManager.getInstance().updateData(errorInfo1);
                    }
                    catch (BaseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return errorInfo;
    }
}

