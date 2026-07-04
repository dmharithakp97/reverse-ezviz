/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.ezviz.sdk.configwifi.mixedconfig;

import android.text.TextUtils;
import com.ezviz.utils.NativeApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil {
    String mIp;
    int mPort;
    int mMaxSpeed = 262144;
    int mPackgeSize = 16384;
    int mInterval = this.mPackgeSize * 1000 / this.mMaxSpeed;
    static Pattern mNumPattern = Pattern.compile("\\d+(\\.\\d+)?");
    volatile boolean mCancelSpeedTest = false;
    Process mProcess = null;
    Object mPingLock = new Object();

    private NetUtil() {
    }

    public NetUtil(String ip, int port) {
        this.mIp = ip;
        this.mPort = port;
    }

    public static String getInetAddress(String hostName) {
        if (TextUtils.isEmpty((CharSequence)hostName)) {
            return null;
        }
        hostName = hostName.replace("http://", "");
        String IPAddress = null;
        InetAddress[] inetAddress = null;
        try {
            inetAddress = InetAddress.getAllByName(hostName);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (inetAddress != null) {
            for (InetAddress inetAddress1 : inetAddress) {
                if (!(inetAddress1 instanceof Inet4Address)) continue;
                IPAddress = inetAddress1.getHostAddress();
                break;
            }
        }
        return IPAddress;
    }

    public void startSeedTest(int time, onSpeedListener onSpeedListener2) {
        InputStream in = null;
        OutputStream out = null;
        if (onSpeedListener2 == null) {
            return;
        }
        if (time > 30) {
            time = 30;
        }
        byte[] bTime = new byte[2];
        bTime[1] = (byte)(time >> 8 & 0xFF);
        bTime[0] = (byte)(time & 0xFF);
        int current = 0;
        int average = 0;
        Socket socket = new Socket();
        int timeOut = 5000;
        long t = System.currentTimeMillis();
        int maxTime = time * 1000;
        long flow = 0L;
        long stepFlow = 0L;
        long stepT = t;
        try {
            InetSocketAddress localInetSocketAddress = new InetSocketAddress(this.mIp, this.mPort);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(20);
            socket.setKeepAlive(true);
            socket.setPerformancePreferences(0, 1, 1);
            socket.setReuseAddress(true);
            socket.connect(localInetSocketAddress, timeOut);
            socket.setSoTimeout(timeOut);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
            onSpeedListener2.onConnectFail();
            return;
        }
        try {
            byte[] req = new byte[]{36, 26, bTime[1], bTime[0]};
            byte[] res = new byte[4];
            out.write(req);
            int r = in.read(res);
            byte[] buf = new byte[this.mPackgeSize];
            if (res[0] == 36 && res[1] == 0) {
                while (!this.mCancelSpeedTest) {
                    long t1 = System.currentTimeMillis();
                    out.write(buf);
                    out.flush();
                    long t2 = System.currentTimeMillis() - t1;
                    if (t2 < (long)this.mInterval) {
                        try {
                            Thread.sleep((long)this.mInterval - t2);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ++stepFlow;
                    flow += (long)this.mPackgeSize;
                    stepFlow += (long)this.mPackgeSize;
                    long t3 = System.currentTimeMillis();
                    long t4 = t3 - stepT;
                    long t5 = t3 - t;
                    if (t4 > 500L) {
                        average = (int)(flow * 1000L / t5) >> 10;
                        current = (int)(stepFlow * 1000L / t4) >> 10;
                        onSpeedListener2.onSpeed(current, average);
                        stepT = t3;
                        stepFlow = 0L;
                    }
                    if (t5 <= (long)maxTime) continue;
                    break;
                }
            }
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.mCancelSpeedTest = false;
        long t6 = System.currentTimeMillis() - t;
        if (t6 > 0L) {
            average = (int)(flow * 1000L / t6) >> 10;
        }
        onSpeedListener2.onFinish(average);
    }

    public void cancelSeedTest() {
        this.mCancelSpeedTest = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public NetStatus ping(int times) {
        NetStatus result = new NetStatus();
        int status = -1;
        Object object = this.mPingLock;
        synchronized (object) {
            if (this.mProcess != null) {
                this.mProcess.destroy();
            }
        }
        ArrayList<String> list = new ArrayList<String>();
        try {
            this.mProcess = Runtime.getRuntime().exec("ping -c   10 " + this.mIp);
            status = this.mProcess.waitFor();
            InputStream input = this.mProcess.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line).append("\r\n");
                list.add(line);
            }
            result.result = buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        int size = list.size();
        if (size > 2) {
            String ret1 = (String)list.get(size - 2);
            String ret2 = (String)list.get(size - 1);
            ArrayList<String> values = new ArrayList<String>();
            Matcher matcher1 = mNumPattern.matcher(ret1);
            values.clear();
            while (matcher1.find()) {
                values.add(matcher1.group());
            }
            if (values.size() >= 2) {
                result.transmitted = Integer.parseInt((String)values.get(0));
                result.received = Integer.parseInt((String)values.get(1));
            }
            values.clear();
            Matcher matcher2 = mNumPattern.matcher(ret2);
            while (matcher2.find()) {
                values.add(matcher2.group());
            }
            if (values.size() >= 4) {
                result.min = Float.parseFloat((String)values.get(0));
                result.avg = Float.parseFloat((String)values.get(1));
                result.max = Float.parseFloat((String)values.get(2));
                result.mdev = Float.parseFloat((String)values.get(3));
            }
        }
        Object object2 = this.mPingLock;
        synchronized (object2) {
            this.mProcess = null;
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void cancelPing() {
        Object object = this.mPingLock;
        synchronized (object) {
            if (this.mProcess != null) {
                this.mProcess.destroy();
            }
            this.mProcess = null;
        }
    }

    public synchronized void startSeedTestEx(int time, onSpeedListener onSpeedListener2) {
        if (time > 30) {
            time = 30;
        }
        NativeApi.startSeedTest(this.mIp, this.mPort, time, onSpeedListener2);
    }

    public synchronized void cancelSeedTestEx() {
        NativeApi.cancelSeedTest();
    }

    public static interface onSpeedListener {
        public void onSpeed(int var1, int var2);

        public void onFinish(int var1);

        public void onConnectFail();
    }

    public static class NetStatus {
        public float min = -1.0f;
        public float avg = -1.0f;
        public float max = -1.0f;
        public float mdev = -1.0f;
        public int transmitted = -1;
        public int received = -1;
        public String result;
    }
}

