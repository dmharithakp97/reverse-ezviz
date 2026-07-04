/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Message
 *  android.text.TextUtils
 *  com.ez.jna.EZP2PRecordCoverJNA$EZRecordReq
 *  com.ez.jna.EZP2PRecordCoverJNA$EZRecordResp
 *  com.ez.p2ptrans.EZP2PBaseFetcher$EZP2PTransParam
 *  com.ez.p2ptrans.RecordCoverCallback
 *  com.ez.p2ptrans.RecordCoverFetcher
 */
package com.videogo.remoteplayback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.ez.jna.EZP2PRecordCoverJNA;
import com.ez.p2ptrans.EZP2PBaseFetcher;
import com.ez.p2ptrans.RecordCoverCallback;
import com.ez.p2ptrans.RecordCoverFetcher;
import com.videogo.device.DeviceInfoEx;
import com.videogo.device.DeviceManager;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.main.AppManager;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.openapi.bean.EZRelayServeInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class RecordCoverFetcherManager {
    private static final String TAG = RecordCoverFetcherManager.class.getSimpleName();
    private static RecordCoverFetcherManager manager;
    private RecordCoverFetcher recordCoverFetcher;
    private RecordCoverFetcherInitCallBack fetcherInitCallBack;
    private RecordCoverFetcherCallBack fetcherCallBack;
    private Context context;
    private String deviceSerial;
    private int cameraNo;
    private Map<String, EZRelayServeInfo> serveMap = new HashMap<String, EZRelayServeInfo>();
    private Map<String, String> ticketMap = new HashMap<String, String>();
    private static final int INIT_FAILED = 0;
    private static final int INIT_SUCCESS = 1;
    private static final int FETCHER_FAILED = 2;
    private static final int FETCHER_SUCCESS = 3;
    private RecordCoverCallback recordCoverCallback = new RecordCoverCallback(){

        public void onMsg(int i) {
        }

        public void onError(int i) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(31, i);
            LogUtil.e(TAG, "onError : " + errorInfo.errorCode);
            if (RecordCoverFetcherManager.this.fetcherCallBack != null) {
                Message message = Message.obtain();
                message.what = 2;
                message.arg1 = errorInfo.errorCode;
                RecordCoverFetcherManager.this.handler.sendMessage(message);
            } else {
                RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
            }
        }

        public void onRespData(EZP2PRecordCoverJNA.EZRecordResp ezRecordResp, byte[] bytes) {
            LogUtil.d(TAG, "onRespData length : " + bytes.length);
            if (RecordCoverFetcherManager.this.fetcherCallBack == null) {
                return;
            }
            Message message = Message.obtain();
            message.what = 3;
            message.arg1 = ezRecordResp.seq;
            message.obj = bytes;
            RecordCoverFetcherManager.this.handler.sendMessage(message);
        }
    };
    @SuppressLint(value={"HandlerLeak"})
    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    if (RecordCoverFetcherManager.this.fetcherInitCallBack == null) break;
                    RecordCoverFetcherManager.this.fetcherInitCallBack.onFetcherInitFailed();
                    break;
                }
                case 1: {
                    if (RecordCoverFetcherManager.this.fetcherInitCallBack == null) break;
                    RecordCoverFetcherManager.this.fetcherInitCallBack.onFetcherInitSuccess();
                    break;
                }
                case 2: {
                    if (RecordCoverFetcherManager.this.fetcherCallBack == null) break;
                    RecordCoverFetcherManager.this.fetcherCallBack.onGetCoverFailed(msg.arg1);
                    break;
                }
                case 3: {
                    if (RecordCoverFetcherManager.this.fetcherCallBack == null) break;
                    RecordCoverFetcherManager.this.fetcherCallBack.onGetCoverSuccess(msg.arg1, (byte[])msg.obj);
                    break;
                }
            }
        }
    };
    private EZRelayServeInfo serveInfo;
    private String ticket;

    public static RecordCoverFetcherManager getInstance() {
        if (manager == null) {
            manager = new RecordCoverFetcherManager();
        }
        return manager;
    }

    private RecordCoverFetcherManager() {
    }

    public void initFetcher(Context context, String deviceSerial, int cameraNo, RecordCoverFetcherInitCallBack fetcherInitCallBack) {
        LogUtil.d(TAG, "Enter initFetcher. deviceSerial = " + deviceSerial + ", cameraNo = " + cameraNo);
        this.context = context;
        this.deviceSerial = deviceSerial;
        this.cameraNo = cameraNo;
        this.fetcherInitCallBack = fetcherInitCallBack;
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        EZRelayServeInfo serveInfo = this.serveMap.get(deviceKey);
        String ticket = this.ticketMap.get(deviceKey);
        if (serveInfo != null && ticket != null) {
            LogUtil.e(TAG, "device(" + deviceKey + ") serveInfo & ticket are not null");
            this.startFetcher(deviceSerial, cameraNo, this.recordCoverCallback);
            return;
        }
        this.initParams(deviceSerial, cameraNo);
    }

    private void initParams(final String deviceSerial, final int cameraNo) {
        new Thread(new Runnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                try {
                    DeviceInfoEx deviceInfo = DeviceManager.getInstance().getDeviceInfoExById(deviceSerial);
                    if (deviceInfo == null) {
                        DeviceManager.getInstance().getDeviceInfoExFromOnlineToLocal(deviceSerial, cameraNo);
                        deviceInfo = DeviceManager.getInstance().getDeviceInfoExById(deviceSerial);
                    }
                    if (deviceInfo == null || !deviceInfo.getSupportSdCover()) {
                        LogUtil.e(TAG, "device(" + deviceSerial + ") is not support sdCover");
                        RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                        return;
                    }
                    final CountDownLatch latch = new CountDownLatch(2);
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                RecordCoverFetcherManager.this.serveInfo = PlayAPI.getInstance().getSdCoverRelayServeInfo(deviceSerial, cameraNo);
                            }
                            catch (BaseException e) {
                                e.printStackTrace();
                                LogUtil.e(TAG, "getSdCoverRelayServeInfo failed");
                                RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                            }
                            finally {
                                latch.countDown();
                            }
                        }
                    }).start();
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                RecordCoverFetcherManager.this.ticket = PlayAPI.getInstance().getSdCoverRelayTicket(deviceSerial, cameraNo);
                            }
                            catch (BaseException e) {
                                e.printStackTrace();
                                LogUtil.e(TAG, "getSdCoverRelayTicket failed");
                                RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                            }
                            finally {
                                latch.countDown();
                            }
                        }
                    }).start();
                    latch.await();
                    if (RecordCoverFetcherManager.this.serveInfo == null || TextUtils.isEmpty((CharSequence)RecordCoverFetcherManager.this.ticket)) {
                        RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                        return;
                    }
                    String deviceKey = RecordCoverFetcherManager.this.deviceKey(deviceSerial, cameraNo);
                    RecordCoverFetcherManager.this.serveMap.put(deviceKey, RecordCoverFetcherManager.this.serveInfo);
                    RecordCoverFetcherManager.this.ticketMap.put(deviceKey, RecordCoverFetcherManager.this.ticket);
                    RecordCoverFetcherManager.this.startFetcher(deviceSerial, cameraNo, RecordCoverFetcherManager.this.recordCoverCallback);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "InterruptedException error");
                    RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                }
                catch (BaseException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "getDeviceDetail failed");
                    RecordCoverFetcherManager.this.handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void startFetcher(String deviceSerial, int cameraNo, RecordCoverCallback recordCoverCallback) {
        LogUtil.d(TAG, "Enter startFetcher. deviceSerial = " + deviceSerial + ", cameraNo = " + cameraNo);
        String deviceKey = this.deviceKey(deviceSerial, cameraNo);
        EZRelayServeInfo serveInfo = this.serveMap.get(deviceKey);
        String ticket = this.ticketMap.get(deviceKey);
        EZP2PBaseFetcher.EZP2PTransParam params = new EZP2PBaseFetcher.EZP2PTransParam();
        params.serial_ = deviceSerial;
        params.channel_ = cameraNo;
        params.autoType_ = 1;
        params.token_ = ticket;
        params.relayAddr_ = AppManager.getInetAddress(serveInfo.getDomain());
        params.relayPort_ = serveInfo.getPort();
        params.relayPublicKey_ = serveInfo.getKey().getBytes();
        params.relayPublicKeyVer_ = serveInfo.getVersion();
        params.timeout_ = 10000;
        this.recordCoverFetcher = new RecordCoverFetcher(params);
        this.recordCoverFetcher.setCallback(recordCoverCallback);
        this.recordCoverFetcher.start();
        this.handler.sendEmptyMessage(1);
    }

    public void requestRecordCover(List<EZDeviceRecordFile> recordFiles, RecordCoverFetcherCallBack callBack) {
        LogUtil.d(TAG, "Enter requestRecordCover");
        if (callBack == null) {
            LogUtil.e(TAG, "requestRecordCover RecordCoverFetcherCallBack is null");
            return;
        }
        if (this.recordCoverFetcher == null) {
            LogUtil.e(TAG, "recordCoverFetcher is null, please init RecordCoverFetcher first");
            return;
        }
        this.fetcherCallBack = callBack;
        for (EZDeviceRecordFile recordFile : recordFiles) {
            EZP2PRecordCoverJNA.EZRecordReq ezRecordReq = new EZP2PRecordCoverJNA.EZRecordReq();
            ezRecordReq.type = 0;
            ezRecordReq.cmd = 1;
            ezRecordReq.seq = recordFile.getSeq();
            ezRecordReq.serial = this.deviceSerial;
            ezRecordReq.channel = this.cameraNo;
            ezRecordReq.startTime = recordFile.getBegin();
            ezRecordReq.stopTime = recordFile.getEnd();
            this.recordCoverFetcher.requestRecordCover(ezRecordReq);
        }
    }

    public void stopFetcher() {
        LogUtil.d(TAG, "Enter stopFetcher");
        if (this.recordCoverFetcher != null) {
            this.recordCoverFetcher.stop();
        }
    }

    public static byte[] getBytesFromAssets(Context context, String fileName) {
        try {
            InputStream input = context.getResources().getAssets().open(fileName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.close();
            input.close();
            return output.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private String deviceKey(String deviceSerial, int cameraNo) {
        String token = LocalInfo.getInstance().getEZAccesstoken().getAccessTokenOrHttpToken();
        return token + "_" + deviceSerial + "_" + cameraNo;
    }

    public static interface RecordCoverFetcherInitCallBack {
        public void onFetcherInitSuccess();

        public void onFetcherInitFailed();
    }

    public static interface RecordCoverFetcherCallBack {
        public void onGetCoverSuccess(int var1, byte[] var2);

        public void onGetCoverFailed(int var1);
    }
}

