/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  com.google.gson.Gson
 *  org.MediaPlayer.PlayM4.Player
 */
package com.ez.stream;

import android.content.Context;
import android.util.Log;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.statistics.P2PPreConnectStatistics;
import com.ez.statistics.PingCheckDef;
import com.ez.statistics.PingImp;
import com.ez.stream.EZEcdhKeyInfo;
import com.ez.stream.EZGrayConfig;
import com.ez.stream.EZStreamClient;
import com.ez.stream.EZTaskMgr;
import com.ez.stream.EZTimeoutParam;
import com.ez.stream.EZTokenData;
import com.ez.stream.InitParam;
import com.ez.stream.JsonUtils;
import com.ez.stream.LogUtil;
import com.ez.stream.NativeApi;
import com.ez.transcode.TransManager;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.MediaPlayer.PlayM4.Player;

public class EZStreamClientManager
implements PingImp.PingImpRspListener {
    static final String TAG = "EZStreamClientManager";
    static ReentrantLock sLock = new ReentrantLock();
    static EZStreamClientManager sManager;
    EZTaskMgr mEZTaskMgr = null;
    OnGlobalListener mOnGlobalListener = null;
    GlobalCallback mGlobalCallback = new GlobalCallback();
    OnTokenListener mOnTokenListener = null;

    private EZStreamClientManager(EZTaskMgr mgr) {
        this.mEZTaskMgr = mgr;
        NativeApi.setGlobalCallback(this.mGlobalCallback);
    }

    public synchronized void release() {
        sLock.lock();
        NativeApi.uninitSDK();
        TransManager.uninitSDK();
        if (this.mEZTaskMgr != null) {
            this.mEZTaskMgr.quit();
            this.mEZTaskMgr = null;
            sManager = null;
        }
        sLock.unlock();
    }

    public static EZStreamClientManager create(Context context) {
        return EZStreamClientManager.create(context, null);
    }

    public static EZStreamClientManager create(Context context, String soPath) {
        sLock.lock();
        if (sManager == null) {
            int ret = NativeApi.initSDKEx(soPath);
            TransManager.initSDK();
            if (ret == 0) {
                EZTaskMgr mgr = new EZTaskMgr();
                sManager = new EZStreamClientManager(mgr);
                Log.i((String)TAG, (String)sManager.getVersion());
            }
            PingImp.getInstance().setRspListener(sManager);
            Player.getInstance();
        }
        sLock.unlock();
        return sManager;
    }

    public int setTokens(String[] tokens) {
        int ret = -1;
        if (tokens != null) {
            int maxCount = 50;
            int totalCount = tokens.length;
            int loop = totalCount / maxCount;
            int left = totalCount % maxCount;
            int tokenIndex = 0;
            for (int i = 0; i < loop; ++i) {
                String[] tokensTmp = new String[maxCount];
                for (int j = 0; j < maxCount; ++j) {
                    tokensTmp[j] = tokens[tokenIndex++];
                }
                ret = NativeApi.setTokens(tokensTmp);
            }
            if (left > 0) {
                String[] tokensTmp = new String[left];
                for (int i = 0; i < left; ++i) {
                    tokensTmp[i] = tokens[tokenIndex++];
                }
                ret = NativeApi.setTokens(tokensTmp);
            }
        }
        return ret;
    }

    public int clearTokens() {
        int ret = NativeApi.clearTokens();
        return ret;
    }

    public EZStreamClient createClient(InitParam param) {
        EZStreamClient client = null;
        long nativeClient = NativeApi.createClient(param);
        if (nativeClient != 0L) {
            client = new EZStreamClient(nativeClient);
        }
        return client;
    }

    public EZStreamClient createClientWithUrl(String streamUrl) {
        EZStreamClient client = null;
        long nativeClient = NativeApi.createClientWithUrl(streamUrl);
        if (nativeClient != 0L) {
            client = new EZStreamClient(nativeClient);
        }
        return client;
    }

    public int destroyClient(EZStreamClient client) {
        int ret = -1;
        if (client != null) {
            client.release();
            ret = 0;
        }
        return ret;
    }

    public EZTaskMgr getTaskMgr() {
        return this.mEZTaskMgr;
    }

    public EZStreamClient createCASClient() {
        EZStreamClient client = null;
        long nativeClient = NativeApi.createCASClient();
        if (nativeClient != 0L) {
            client = new EZStreamClient(nativeClient);
        }
        return client;
    }

    public int getLeftTokenCount() {
        return NativeApi.getLeftTokenCount();
    }

    public void startPreconnect(InitParam initParam) {
        NativeApi.startPreconnect(initParam);
    }

    public int clearPreconnectInfo(String szDevSerial) {
        return NativeApi.clearPreconnectInfo(szDevSerial);
    }

    public int setLogPrintEnable(boolean enable) {
        return this.setLogPrintEnable(enable, 2);
    }

    public int setLogPrintEnable(boolean enable, int logLevel) {
        return this.setLogPrintEnable(enable, false, logLevel);
    }

    public int setLogPrintEnable(boolean enable, boolean supportXlog) {
        return this.setLogPrintEnable(enable, supportXlog, 2);
    }

    public int setLogPrintEnable(boolean enable, boolean supportXlog, int logLevel) {
        LogUtil.IS_LOG = enable;
        LogUtil.IS_XLOG = supportXlog;
        return NativeApi.setLogPrintEnable(enable, supportXlog, logLevel);
    }

    public String getVersion() {
        String version = NativeApi.getVersion();
        String sdkVersion = "ezplayersdk:v7.5.16.20260306_2653972";
        String newVersion = version.replaceAll("ezplayersdk:[^|]*", sdkVersion);
        return newVersion;
    }

    public boolean isP2PPreviewing(String szDevSerial, int iChannel) {
        return NativeApi.isP2PPreviewing(szDevSerial, iChannel);
    }

    public boolean isPlayingWithPreconnect(String szDevSerial) {
        return NativeApi.isPlayingWithPreconnect(szDevSerial);
    }

    public boolean isPreConnectionSucceed(String szDevSerial) {
        return NativeApi.isPreConnectionSucceed(szDevSerial);
    }

    public void setGlobalListener(OnGlobalListener listener) {
        this.mOnGlobalListener = listener;
    }

    public OnGlobalListener getOnGlobalListener() {
        return this.mOnGlobalListener;
    }

    public OnTokenListener getOnTokenListener() {
        return this.mOnTokenListener;
    }

    public void setOnTokenListener(OnTokenListener onTokenListener) {
        this.mOnTokenListener = onTokenListener;
        NativeApi.enableTokenCallback(onTokenListener != null);
    }

    public void setLogCallback(LogCallback callback) {
        LogUtil.setLogCallback(callback);
        NativeApi.setLogCallback(callback);
    }

    public int startServerOfReverseDirect(String szStunDomain, int iStunPort, int iCheckPeriod) {
        return NativeApi.startServerOfReverseDirect(szStunDomain, iStunPort, iCheckPeriod);
    }

    public int stopServerOfReverseDirect() {
        return NativeApi.stopServerOfReverseDirect();
    }

    public int clearDeviceListOfReverseDirect(String szDevSerial) {
        return NativeApi.clearDeviceListOfReverseDirect(szDevSerial);
    }

    public int setP2PV3ConfigInfo(short[] szP2PKey, int iSaltIndex, int iSaltVer) {
        return NativeApi.setP2PV3ConfigInfo(szP2PKey, iSaltIndex, iSaltVer);
    }

    public void setPingCheckHost(Context context, String szHost, ArrayList<Integer> arrCapabilist) {
        PingImp.getInstance().setPingCheckHost(context, szHost, arrCapabilist);
    }

    public void setP2PPublicParam(int clnNat) {
        NativeApi.setP2PPublicParam(clnNat);
    }

    public void setLocalNetIp(String netIp) {
        NativeApi.setLocalNetIp(netIp);
    }

    public void setTimeoutParam(EZTimeoutParam timeoutParam) {
        NativeApi.setTimeoutParam(timeoutParam);
        if (timeoutParam != null) {
            EZGrayConfig.setGrayConfig(timeoutParam.ezplayer);
        }
    }

    public void enableStreamClientETP() {
        NativeApi.enableStreamClientETP();
    }

    public void enableStreamClientCMDEcdh() {
        NativeApi.enableStreamClientCMDEcdh();
    }

    public void enableTTSCMDEcdh() {
        NativeApi.enableTTSCMDEcdh();
    }

    public void setMtuConfig(int mtuValue) {
        if (mtuValue < 400) {
            return;
        }
        if (mtuValue > 1600) {
            return;
        }
        NativeApi.setMtuConfig(mtuValue);
    }

    public void setOptimizeTimeout(boolean enable) {
        NativeApi.setTimeoutOptimize(enable);
    }

    public int generateECDHKey(EZEcdhKeyInfo ecdhKeyInfo) {
        return NativeApi.generateECDHKey(ecdhKeyInfo);
    }

    public void setClientECDHKey(EZEcdhKeyInfo ecdhKeyInfo) {
        NativeApi.setClientECDHKey(ecdhKeyInfo.szPBKey, ecdhKeyInfo.iPBKeyLen, ecdhKeyInfo.szPRKey, ecdhKeyInfo.iPRKeyLen);
    }

    @Override
    public void onPingTaskCallback(PingCheckDef.PingCheckRsp rsp) {
        Gson gson = new Gson();
        String statistics = gson.toJson((Object)rsp);
        LogUtil.d("PingImp", statistics);
        if (this.mOnGlobalListener != null) {
            this.mOnGlobalListener.onGlobalEventStatistics(1, statistics);
        }
    }

    public boolean isPreconnecting(String szDevSerial) {
        return NativeApi.isPreconnecting(szDevSerial);
    }

    public void setSSLTryCount(int count) {
        NativeApi.setSSLTryCount(count);
    }

    public String getP2PSelectInfo() {
        return NativeApi.getP2PSelectInfo();
    }

    public int setP2PSelectInfo(String info) {
        return NativeApi.setP2PSelectInfo(info);
    }

    public List<String> selectP2PDevList(ArrayList<String> devList, int netType) {
        if (devList == null || devList.size() == 0) {
            return devList;
        }
        return NativeApi.selectP2PDevices(devList, netType);
    }

    public ArrayList<String> getAllProcessedPreconnectSerials() {
        return NativeApi.getAllProcessedPreconnectSerials();
    }

    public ArrayList<String> getAllToDoPreconnectSerials() {
        return NativeApi.getAllToDoPreconnectSerials();
    }

    @Deprecated
    public int setCASClientType(int type) {
        return NativeApi.setCASClientType(type);
    }

    @Deprecated
    public int setCASClientVersion(String version) {
        return NativeApi.setCASClientVersion(version);
    }

    public static interface OnGlobalListener {
        public void onP2PPreConnectStatistics(String var1, String var2);

        public void onDirectPreConnectStatistics(String var1, String var2);

        public void onEvent(String var1, int var2, String var3);

        public void onData(int var1);

        public void onPreconnectResult(String var1, int var2, boolean var3);

        public void onGlobalEventStatistics(int var1, String var2);

        public void onDevInfoUpdated(String var1, EZStreamSDKJNA.EZ_DEV_INFO var2);

        public void onReverseDirectUpnpStatistics(String var1);
    }

    class GlobalCallback {
        GlobalCallback() {
        }

        void onPreConnectStatistics(int type, String devSerial, String statistics) {
            if (statistics == null || EZStreamClientManager.this.mOnGlobalListener == null) {
                return;
            }
            LogUtil.i(EZStreamClientManager.TAG, "onPreConnectStatistics = " + statistics);
            if (type == 1) {
                P2PPreConnectStatistics obj = JsonUtils.fromJson(statistics, P2PPreConnectStatistics.class);
                EZStreamClientManager.this.mOnGlobalListener.onP2PPreConnectStatistics(devSerial, statistics);
            } else if (type == 2 || type == 3) {
                EZStreamClientManager.this.mOnGlobalListener.onDirectPreConnectStatistics(devSerial, statistics);
            } else if (type == 6) {
                EZStreamClientManager.this.mOnGlobalListener.onReverseDirectUpnpStatistics(statistics);
            }
        }

        void onEvent(String szDevSerial, int eventType, String data) {
            LogUtil.i(EZStreamClientManager.TAG, "onEvent = " + szDevSerial + ",eventType = " + eventType + ",data = " + data);
            if (EZStreamClientManager.this.mOnGlobalListener != null) {
                if (eventType == 100) {
                    EZStreamSDKJNA.EZ_DEV_INFO.ByReference info = new EZStreamSDKJNA.EZ_DEV_INFO.ByReference();
                    boolean ret = EZStreamSDKJNA.sEZStreamSDKJNA.ezstream_getDevInfoFromCache(szDevSerial, info);
                    if (ret) {
                        info.read();
                        EZStreamClientManager.this.mOnGlobalListener.onDevInfoUpdated(szDevSerial, info);
                    }
                } else {
                    EZStreamClientManager.this.mOnGlobalListener.onEvent(szDevSerial, eventType, data);
                }
            }
        }

        void onData(int dataLen) {
            if (EZStreamClientManager.this.mOnGlobalListener != null) {
                EZStreamClientManager.this.mOnGlobalListener.onData(dataLen);
            }
        }

        void onPreconnectResult(String szDevSerial, int clientType, boolean isSuccess) {
            if (EZStreamClientManager.this.mOnGlobalListener != null) {
                EZStreamClientManager.this.mOnGlobalListener.onPreconnectResult(szDevSerial, clientType, isSuccess);
            }
        }

        EZTokenData onFetchToken(String userId, String serial) {
            if (EZStreamClientManager.this.mOnTokenListener != null) {
                LogUtil.d(EZStreamClientManager.TAG, "onFetchToken : userId " + userId + " serial " + serial);
                return EZStreamClientManager.this.mOnTokenListener.onFetchToken(userId, serial);
            }
            return null;
        }
    }

    public static interface OnTokenListener {
        public EZTokenData onFetchToken(String var1, String var2);
    }

    public static interface LogCallback {
        public void onLog(String var1, int var2, String var3);
    }
}

