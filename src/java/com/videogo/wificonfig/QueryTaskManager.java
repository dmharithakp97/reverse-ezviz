/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ezviz.sdk.configwifi.common.LogUtil
 */
package com.videogo.wificonfig;

import com.ezviz.sdk.configwifi.common.LogUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueryTaskManager {
    private static final String TAG = QueryTaskManager.class.getSimpleName();
    private static QueryTaskManager mManager = new QueryTaskManager();
    private static ExecutorService mThreadPool;
    private static boolean isInit;

    public static QueryTaskManager getInstance() {
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
            LogUtil.d((String)TAG, (String)"please init before submit task");
        }
    }

    static {
        isInit = false;
    }
}

