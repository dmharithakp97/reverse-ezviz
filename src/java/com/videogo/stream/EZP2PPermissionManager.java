/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.stream;

import com.videogo.exception.BaseException;
import com.videogo.openapi.PlayAPI;
import com.videogo.util.LogUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EZP2PPermissionManager {
    private static final String TAG = EZP2PPermissionManager.class.getSimpleName();
    private static EZP2PPermissionManager manager;
    private ExecutorService permissionThreadPool;
    private Timer timer;
    private TimerTask timerTask;
    private Map<String, PermissionVerifyCallBack> callbackMap = new ConcurrentHashMap<String, PermissionVerifyCallBack>();
    private Map<String, String> playTypeMap = new ConcurrentHashMap<String, String>();

    public static synchronized EZP2PPermissionManager getInstance() {
        if (manager == null) {
            manager = new EZP2PPermissionManager();
        }
        return manager;
    }

    private EZP2PPermissionManager() {
        this.permissionThreadPool = Executors.newSingleThreadExecutor();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void addVerification(String deviceSerial, int cameraNo, String playType, PermissionVerifyCallBack callBack) {
        LogUtil.d(TAG, "Enter addVerification");
        if (callBack == null) {
            return;
        }
        if (this.timer == null) {
            Class<EZP2PPermissionManager> clazz = EZP2PPermissionManager.class;
            // MONITORENTER : com.videogo.stream.EZP2PPermissionManager.class
            if (this.timer == null) {
                this.timer = new Timer();
                this.timerTask = new TimerTask(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     * Converted monitor instructions to comments
                     * Lifted jumps to return sites
                     */
                    @Override
                    public void run() {
                        if (EZP2PPermissionManager.this.callbackMap.isEmpty()) {
                            Class<EZP2PPermissionManager> clazz = EZP2PPermissionManager.class;
                            // MONITORENTER : com.videogo.stream.EZP2PPermissionManager.class
                            if (EZP2PPermissionManager.this.callbackMap.isEmpty()) {
                                LogUtil.d(TAG, "timer stop");
                                EZP2PPermissionManager.this.timer.cancel();
                                EZP2PPermissionManager.this.timer = null;
                                // MONITOREXIT : clazz
                                return;
                            }
                            // MONITOREXIT : clazz
                        }
                        Iterator iterator = EZP2PPermissionManager.this.callbackMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            final Map.Entry entry = iterator.next();
                            String[] array = ((String)entry.getKey()).split("_");
                            final String deviceSerial = array[0];
                            final int cameraNo = Integer.parseInt(array[1]);
                            final PermissionVerifyCallBack callback = (PermissionVerifyCallBack)entry.getValue();
                            EZP2PPermissionManager.this.permissionThreadPool.submit(new Runnable(){

                                @Override
                                public void run() {
                                    boolean ret = true;
                                    try {
                                        ret = PlayAPI.getInstance().getP2pPermission(deviceSerial, cameraNo, (String)EZP2PPermissionManager.this.playTypeMap.get(entry.getKey()));
                                    }
                                    catch (BaseException e) {
                                        e.printStackTrace();
                                    }
                                    if (!ret) {
                                        callback.onVerifyResult(false);
                                    }
                                }
                            });
                        }
                    }
                };
                this.timer.schedule(this.timerTask, 300000L, 300000L);
            }
            // MONITOREXIT : clazz
        }
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        LogUtil.d(TAG, "deviceKey : " + deviceKey);
        this.callbackMap.put(deviceKey, callBack);
        this.playTypeMap.put(deviceKey, playType);
    }

    public void removeVerification(String deviceSerial, int cameraNo) {
        LogUtil.d(TAG, "Enter removeVerification");
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        LogUtil.d(TAG, "deviceKey : " + deviceKey);
        this.callbackMap.remove(deviceKey);
        this.playTypeMap.remove(deviceKey);
    }

    private String deviceKey(String deviceSerial, int cameraNo) {
        return deviceSerial + "_" + cameraNo;
    }

    public static interface PermissionVerifyCallBack {
        public void onVerifyResult(boolean var1);
    }
}

