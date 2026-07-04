/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.sdk.configwifi.common;

import com.ezviz.sdk.configwifi.common.LogUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigWifiTaskManager {
    private static final String TAG = ConfigWifiTaskManager.class.getSimpleName();
    private static ConfigWifiTaskManager mManager = new ConfigWifiTaskManager();
    private static ExecutorService mThreadPool;
    private static boolean isInit;

    public static ConfigWifiTaskManager getInstance() {
        return mManager;
    }

    public static boolean isInit() {
        return isInit;
    }

    public static synchronized void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        mThreadPool = Executors.newCachedThreadPool();
    }

    public static synchronized void unInit() {
        isInit = false;
        if (mThreadPool != null) {
            mThreadPool.shutdown();
        }
        mThreadPool = null;
    }

    public void submit(Runnable task) {
        if (mThreadPool != null && !mThreadPool.isShutdown()) {
            mThreadPool.execute(task);
        } else {
            LogUtil.d(TAG, "please init before submit task");
        }
    }

    static {
        isInit = false;
    }
}

