/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.ez.statistics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import com.ez.statistics.PingCheckDef;
import com.ez.stream.EZGrayConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingImp {
    public static final String TAG = "PING_CHECK";
    public static final int PING_DEFAULT_SIZE = 64;
    public static final int PING_DEFAULT_TASKCOUNT = 10;
    static PingImp instance = null;
    private String mPingHost;
    private ArrayList<Integer> mArrCapabilist = null;
    private Context mContext = null;
    List<PingCheckDef.PingCheckReq> mPingTaskList = new ArrayList<PingCheckDef.PingCheckReq>();
    ThreadPoolExecutor mPingTaskThread = null;
    PingCheckDef.PingCheckReq mCurrentTask = null;
    ReentrantLock taskLock = new ReentrantLock();
    PingImpRspListener mPingImpRspListener = null;

    private PingImp() {
        this.mArrCapabilist = new ArrayList();
    }

    public static PingImp getInstance() {
        if (instance == null) {
            instance = new PingImp();
        }
        return instance;
    }

    public void setPingCheckHost(Context context, String szHost, ArrayList<Integer> arrCapabilist) {
        this.mContext = context;
        this.mPingHost = szHost;
        this.mArrCapabilist.clear();
        if (arrCapabilist != null) {
            this.mArrCapabilist.addAll(arrCapabilist);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized int addPingTask(PingCheckDef.PingCheckReq task) {
        if (this.mPingHost == null || this.mPingHost.length() == 0) {
            Log.d((String)TAG, (String)"no set ping host");
            return -1;
        }
        if (TextUtils.isEmpty((CharSequence)task.netHost)) {
            task.netHost = this.mPingHost;
        }
        if (this.mArrCapabilist == null || this.mArrCapabilist.size() == 0) {
            Log.d((String)TAG, (String)"no set capabilist");
            return -1;
        }
        if (!this.mArrCapabilist.contains(task.type * 10000000 + task.errCode)) {
            Log.d((String)TAG, (String)("no contains " + (task.type * 10000000 + task.errCode)));
            return -1;
        }
        try {
            this.taskLock.lock();
            if (this.mPingTaskList.size() >= 10) {
                int n = -1;
                return n;
            }
            if (this.mCurrentTask != null && this.mCurrentTask.uuid == task.uuid) {
                int n = -1;
                return n;
            }
            for (PingCheckDef.PingCheckReq req : this.mPingTaskList) {
                if (!req.uuid.equals(task.uuid)) continue;
                int n = 0;
                return n;
            }
            this.mPingTaskList.add(task);
        }
        finally {
            this.taskLock.unlock();
        }
        if (this.mPingTaskThread == null || this.mPingTaskThread.isTerminated()) {
            this.mPingTaskThread = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
            this.mPingTaskThread.execute(() -> {
                while (true) {
                    this.taskLock.lock();
                    if (this.mPingTaskList.size() > 0) {
                        this.mCurrentTask = this.mPingTaskList.get(0);
                        this.mPingTaskList.remove(0);
                    }
                    this.taskLock.unlock();
                    if (this.mCurrentTask == null) break;
                    PingCheckDef.PingCheckRsp rsp = this.startPingInner(this.mCurrentTask);
                    this.taskLock.lock();
                    this.mCurrentTask = null;
                    this.taskLock.unlock();
                    if (this.mPingImpRspListener == null) continue;
                    this.mPingImpRspListener.onPingTaskCallback(rsp);
                }
            });
            this.mPingTaskThread.shutdown();
        }
        return 0;
    }

    public PingCheckDef.PingCheckRsp startPingTask(PingCheckDef.PingCheckReq task) {
        if (this.mPingHost == null || this.mPingHost.length() == 0) {
            Log.d((String)TAG, (String)"no set ping host");
            return null;
        }
        if (TextUtils.isEmpty((CharSequence)task.netHost)) {
            task.netHost = this.mPingHost;
        }
        if (this.mArrCapabilist == null || this.mArrCapabilist.size() == 0) {
            Log.d((String)TAG, (String)"no set capabilist");
            return null;
        }
        if (!this.mArrCapabilist.contains(task.type * 10000000 + task.errCode)) {
            Log.d((String)TAG, (String)("no contains " + (task.type * 10000000 + task.errCode)));
            return null;
        }
        return this.startPingInner(task);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PingCheckDef.PingCheckRsp startPingInner(PingCheckDef.PingCheckReq task) {
        PingCheckDef.PingCheckRsp rsp = new PingCheckDef.PingCheckRsp();
        rsp.uuid = task.uuid;
        rsp.eventTime = task.eventTime;
        rsp.errorCode = task.errCode;
        rsp.type = task.type;
        rsp.vtmhost = task.vtmHost;
        rsp.netHost = task.netHost;
        if (rsp.errorCode == 20001 && this.mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            rsp.isNetAvailable = networkInfo != null && networkInfo.isAvailable() ? 1 : 0;
        }
        boolean[] pingOver = new boolean[]{false};
        if (rsp.isNetAvailable != 0 && !TextUtils.isEmpty((CharSequence)rsp.vtmhost)) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
            executor.execute(() -> {
                PingCheckDef.PingCheckSingleRsp vtmRsp = new PingCheckDef.PingCheckSingleRsp();
                int ret = this.start(rsp.vtmhost, vtmRsp);
                rsp.iVtmSend = vtmRsp.iSend;
                rsp.iVtmSuccess = vtmRsp.iSuccess;
                rsp.vtmLost = (float)(vtmRsp.iSend - vtmRsp.iSuccess) * 1.0f / (float)vtmRsp.iSend;
                rsp.iVtmAvg = vtmRsp.iAvg;
                rsp.iVtmMax = vtmRsp.iMax;
                rsp.iVtmMin = vtmRsp.iMin;
                rsp.iVtmStatus = ret;
                PingCheckDef.PingCheckRsp pingCheckRsp = rsp;
                synchronized (pingCheckRsp) {
                    pingOver[0] = true;
                    rsp.notifyAll();
                }
            });
            executor.shutdown();
        } else {
            pingOver[0] = true;
        }
        if (rsp.isNetAvailable != 0 && !TextUtils.isEmpty((CharSequence)rsp.netHost)) {
            PingCheckDef.PingCheckSingleRsp netRsp = new PingCheckDef.PingCheckSingleRsp();
            int ret = this.start(rsp.netHost, netRsp);
            rsp.iNetSend = netRsp.iSend;
            rsp.iNetSuccess = netRsp.iSuccess;
            rsp.netLost = (float)(netRsp.iSend - netRsp.iSuccess) * 1.0f / (float)netRsp.iSend;
            rsp.iNetAvg = netRsp.iAvg;
            rsp.iNetMax = netRsp.iMax;
            rsp.iNetMin = netRsp.iMin;
            rsp.iNetStatus = ret;
        }
        PingCheckDef.PingCheckRsp pingCheckRsp = rsp;
        synchronized (pingCheckRsp) {
            if (!pingOver[0]) {
                try {
                    rsp.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return rsp;
    }

    private int start(String host, PingCheckDef.PingCheckSingleRsp rsp) {
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
        String command = "ping -i " + EZGrayConfig.pingInterval + " -c " + EZGrayConfig.pingCount + " -w " + EZGrayConfig.pingTimeout + " " + host;
        String regEx = "[^\\d\\.]";
        Pattern p = Pattern.compile(regEx);
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                return -1;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                List<String> value0;
                Log.d((String)TAG, (String)line);
                line = line.toLowerCase();
                if (line.contains("packets transmitted")) {
                    value0 = this.getValues(line);
                    if (value0 == null || value0.size() < 4) {
                        return -1;
                    }
                    rsp.iSend = Integer.parseInt(value0.get(0));
                    rsp.iSuccess = Integer.parseInt(value0.get(1));
                }
                if (!line.contains("rtt min/avg/max/mdev")) continue;
                value0 = this.getValues(line);
                if (value0 == null || value0.size() < 4) {
                    return -1;
                }
                rsp.iMin = Float.valueOf(Float.parseFloat(value0.get(0))).intValue();
                rsp.iAvg = Float.valueOf(Float.parseFloat(value0.get(1))).intValue();
                rsp.iMax = Float.valueOf(Float.parseFloat(value0.get(2))).intValue();
            }
            int status = process.waitFor();
            Log.d((String)TAG, (String)("status " + status));
            return status;
        }
        catch (IOException | InterruptedException | NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<String> getValues(String ss) {
        ArrayList<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public void setRspListener(PingImpRspListener listener) {
        this.mPingImpRspListener = listener;
    }

    public static interface PingImpRspListener {
        public void onPingTaskCallback(PingCheckDef.PingCheckRsp var1);
    }
}

