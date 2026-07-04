/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Environment
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 */
package com.ezviz.opensdk.data;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.ezviz.opensdk.data.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videogo.camera.CameraInfoEx;
import com.videogo.camera.CameraManager;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.openapi.PlayAPI;
import com.videogo.util.AESCBCCipher;
import com.videogo.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileCacheDeviceInfoManager {
    private static final int MAX_SAVE_DEVICE_COUNT = 16;
    private static List<DeviceInfoEx> mDeviceCaches = new ArrayList<DeviceInfoEx>();
    private static List<CameraInfoEx> mCamerasChces = new ArrayList<CameraInfoEx>();
    public static ExecutorService mSingleThreadThreadPool = Executors.newSingleThreadExecutor();
    private static final Object mSaveDeviceCachesLock = new Object();

    public static void getDeviceCacheDetailInfo() {
        mSingleThreadThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                FileCacheDeviceInfoManager.getDeviceEx();
                FileCacheDeviceInfoManager.getCameraInfoEx();
            }
        });
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        File file;
        String cachePath = "mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        File parentFile = new File(cachePath, "ezopensdk");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!(file = new File(parentFile, uniqueName)).exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static String getDeviceCachesPath() {
        return FileCacheDeviceInfoManager.getDiskCacheDir((Context)PlayAPI.mApplication, "DEVICECACHES").getAbsolutePath();
    }

    private static String getCamerasCachesPath() {
        return FileCacheDeviceInfoManager.getDiskCacheDir((Context)PlayAPI.mApplication, "CAMERACACHES").getAbsolutePath();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void removeDeviceEx(DeviceInfoEx deviceInfoEx) {
        if (deviceInfoEx == null) {
            return;
        }
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            Iterator<DeviceInfoEx> it = mDeviceCaches.iterator();
            while (it.hasNext()) {
                DeviceInfoEx deviceInfoex = it.next();
                if (!deviceInfoex.getDeviceID().equalsIgnoreCase(deviceInfoEx.getDeviceID())) continue;
                it.remove();
                break;
            }
        }
        mSingleThreadThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    FileUtils.saveInfo(FileCacheDeviceInfoManager.getDeviceCachesPath(), AESCBCCipher.encrypt(PlayAPI.getInstance().getAppKey(), new Gson().toJson((Object)mDeviceCaches)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void addDeviceEx(DeviceInfoEx deviceInfoEx) {
        if (deviceInfoEx == null) {
            return;
        }
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            Iterator<DeviceInfoEx> it = mDeviceCaches.iterator();
            while (it.hasNext()) {
                DeviceInfoEx deviceInfoex = it.next();
                if (!deviceInfoex.getDeviceID().equalsIgnoreCase(deviceInfoEx.getDeviceID())) continue;
                it.remove();
                break;
            }
            if (mDeviceCaches.size() > 16) {
                mDeviceCaches.remove(0);
            }
            mDeviceCaches.add(deviceInfoEx);
        }
        FileCacheDeviceInfoManager.saveDeviceCachesToFile(mDeviceCaches);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void addCameraEx(CameraInfoEx cameraInfoEx) {
        if (cameraInfoEx == null) {
            return;
        }
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            Iterator<CameraInfoEx> it = mCamerasChces.iterator();
            while (it.hasNext()) {
                CameraInfoEx cameraInfoEx1 = it.next();
                if (!cameraInfoEx1.getDeviceID().equalsIgnoreCase(cameraInfoEx.getDeviceID()) || cameraInfoEx1.getChannelNo() != cameraInfoEx.getChannelNo()) continue;
                it.remove();
                break;
            }
            if (mCamerasChces.size() > 16) {
                mCamerasChces.remove(0);
            }
            mCamerasChces.add(cameraInfoEx);
        }
        mSingleThreadThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    FileUtils.saveInfo(FileCacheDeviceInfoManager.getCamerasCachesPath(), AESCBCCipher.encrypt(PlayAPI.getInstance().getAppKey(), new Gson().toJson((Object)mCamerasChces)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clearDevice() {
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            if (mDeviceCaches != null) {
                mDeviceCaches.clear();
            }
        }
        mSingleThreadThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    FileUtils.saveInfo(FileCacheDeviceInfoManager.getDeviceCachesPath(), AESCBCCipher.encrypt(PlayAPI.getInstance().getAppKey(), new Gson().toJson((Object)mDeviceCaches)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clearCamera() {
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            if (mCamerasChces != null) {
                mCamerasChces.clear();
            }
        }
        mSingleThreadThreadPool.submit(new Runnable(){

            @Override
            public void run() {
                try {
                    FileUtils.saveInfo(FileCacheDeviceInfoManager.getCamerasCachesPath(), AESCBCCipher.encrypt(PlayAPI.getInstance().getAppKey(), new Gson().toJson((Object)mCamerasChces)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void getDeviceEx() {
        String deviceSerialCache = null;
        try {
            long b = System.currentTimeMillis();
            String encryptionKey = PlayAPI.getInstance().getAppKey();
            String encryptedContent = FileUtils.getInfo(FileCacheDeviceInfoManager.getDeviceCachesPath());
            if (TextUtils.isEmpty((CharSequence)encryptionKey) || TextUtils.isEmpty((CharSequence)encryptedContent)) {
                LogUtil.w("getDeviceEx", "empty encryptionKey or encryptedContent");
                return;
            }
            deviceSerialCache = AESCBCCipher.decrypt(encryptionKey, encryptedContent);
            LogUtil.d("getDeviceEx", "cost time = " + (System.currentTimeMillis() - b));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<ArrayList<DeviceInfoEx>>(){}.getType();
        List deviceInfoExes = (List)new Gson().fromJson(deviceSerialCache, type);
        List<DeviceInfoEx> list = mDeviceCaches;
        synchronized (list) {
            mDeviceCaches.clear();
            if (deviceInfoExes == null || deviceInfoExes.size() <= 0) {
                return;
            }
            mDeviceCaches.addAll(deviceInfoExes);
        }
        DeviceManager.getInstance().setAddedDevices(deviceInfoExes);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void getCameraInfoEx() {
        String appKey = PlayAPI.getInstance().getAppKey();
        String encryptedCache = FileUtils.getInfo(FileCacheDeviceInfoManager.getCamerasCachesPath());
        if (!TextUtils.isEmpty((CharSequence)appKey) && !TextUtils.isEmpty((CharSequence)encryptedCache)) {
            String cameraCaches = null;
            try {
                cameraCaches = AESCBCCipher.decrypt(appKey, encryptedCache);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Type type = new TypeToken<ArrayList<CameraInfoEx>>(){}.getType();
            Gson gson = new Gson();
            List cameraInfoExes = (List)gson.fromJson(cameraCaches, type);
            List<DeviceInfoEx> list = mDeviceCaches;
            synchronized (list) {
                mCamerasChces.clear();
                if (cameraInfoExes == null || cameraInfoExes.size() <= 0) {
                    return;
                }
                mCamerasChces.addAll(cameraInfoExes);
            }
            CameraManager.getInstance().setAddedCameras(cameraInfoExes);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void saveDeviceCachesToFile(List<DeviceInfoEx> deviceCachesList) {
        Object object = mSaveDeviceCachesLock;
        synchronized (object) {
            final ArrayList<DeviceInfoEx> tmpDeviceList = new ArrayList<DeviceInfoEx>(deviceCachesList);
            mSingleThreadThreadPool.submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        FileUtils.saveInfo(FileCacheDeviceInfoManager.getDeviceCachesPath(), AESCBCCipher.encrypt(PlayAPI.getInstance().getAppKey(), new Gson().toJson((Object)tmpDeviceList)));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

