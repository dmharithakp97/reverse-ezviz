/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$State
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  com.ez.jna.EZStreamSDKJNA$EZ_DEV_INFO
 *  com.ez.stream.EZEcdhKeyInfo
 *  com.ez.stream.EZStreamClientManager
 *  com.ez.stream.EZStreamClientManager$OnGlobalListener
 *  com.ez.stream.JsonUtils
 *  com.hc.CASClient.CASClient
 *  com.sun.jna.ptr.IntByReference
 *  com.sun.jna.ptr.PointerByReference
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.videogo.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import com.ez.jna.EZStreamSDKJNA;
import com.ez.stream.EZEcdhKeyInfo;
import com.ez.stream.EZStreamClientManager;
import com.ez.stream.JsonUtils;
import com.hc.CASClient.CASClient;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.videogo.constant.RobolectricConfig;
import com.videogo.debug.TestParams;
import com.videogo.ecdh.EcdhKeyInfo;
import com.videogo.exception.BaseException;
import com.videogo.ezdclog.EZDcLogManager;
import com.videogo.ezdclog.params.BaseParams;
import com.videogo.ezdclog.params.EZLogLocalInfoParams;
import com.videogo.ezdclog.params.EZLogStreamDirectDetectParams;
import com.videogo.ezdclog.params.EZLogStreamP2pPreParams;
import com.videogo.main.StreamServerData;
import com.videogo.main.StunClient;
import com.videogo.openapi.PlayAPI;
import com.videogo.openapi.bean.EZSDKConfiguration;
import com.videogo.openapi.bean.EZServerInfo;
import com.videogo.openapi.bean.P2pUserInfo;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.HttpUtils;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.SharedPreferencesUtils;
import com.videogo.util.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class AppManager {
    private static final String TAG = "AppManager";
    public static int ISP_TYPE_DIANXIN = 0;
    public static int ISP_TYPE_LIANTONG = 1;
    public static int ISP_TYPE_YIDONG = 2;
    public static int ISP_TYPE_TIETONG = 3;
    public static int ISP_TYPE_HUASHU = 4;
    private static AppManager mInstance = null;
    private volatile EZServerInfo mServerInfo = null;
    private volatile EZSDKConfiguration mEZSDKConfiguration = null;
    private StreamServerData mStreamerServerInfo = null;
    private CASClient mCASClientSDK = null;
    private StunClient mStunClient = null;
    private int mNatType = 0;
    private String mNetIP = "";
    private int mISPType = -1;
    private String mISP = "";
    private String mISPAddress = null;
    private String mWifiMacAddress = null;
    private EZLogLocalInfoParams localInfoParams;
    private static HashMap<String, String> mInetAddressMap;

    public static synchronized AppManager getInstance() {
        if (null == mInstance) {
            mInstance = new AppManager();
        }
        return mInstance;
    }

    public synchronized EZServerInfo getServerInfo() throws BaseException {
        if (this.mServerInfo == null) {
            this.mServerInfo = PlayAPI.getInstance().getServerInfo();
            if (this.mServerInfo != null) {
                LogUtil.d(TAG, "getServerInfo  = " + JsonUtils.toJson((Object)this.mServerInfo));
                String logUrl = TestParams.isUse() ? "https://test12dclog.ys7.com/statistics.do" : this.mServerInfo.getLogAddr();
                EZDcLogManager.getInstance().setEZLOG_URL(logUrl);
                if (this.localInfoParams != null) {
                    EZDcLogManager.getInstance().submit(this.localInfoParams);
                    this.localInfoParams = null;
                }
            }
            this.refreshNetInfo();
        }
        return this.mServerInfo;
    }

    public void getServerInfoSyn() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    AppManager.this.getServerInfo();
                }
                catch (BaseException e) {
                    LogUtil.printErrStackTrace(AppManager.TAG, e.fillInStackTrace());
                }
            }
        }).start();
    }

    public void refreshNetInfo() throws BaseException {
        if (this.mServerInfo == null) {
            this.mServerInfo = PlayAPI.getInstance().getServerInfo();
        }
        if (this.mServerInfo != null) {
            this.refreshNetTypeSyn();
        }
    }

    public void refreshNetType() {
        try {
            if (this.mServerInfo != null && !TextUtils.isEmpty((CharSequence)this.mServerInfo.getStun1Addr()) && !TextUtils.isEmpty((CharSequence)this.mServerInfo.getStun2Addr()) && this.mStunClient != null) {
                IntByReference natTypePointer = new IntByReference();
                int ret = LocalInfo.getInstance().isPublicServAddr() ? this.mStunClient.Stun_GetNATType_New(this.getLocalIpAddress(), this.mServerInfo.getStun1Addr(), (short)this.mServerInfo.getStun1Port(), this.mServerInfo.getStun2Addr(), (short)this.mServerInfo.getStun2Port(), natTypePointer) : this.mStunClient.Stun_GetNATType(this.getLocalIpAddress(), this.mServerInfo.getStun1Addr(), (short)this.mServerInfo.getStun1Port(), this.mServerInfo.getStun2Addr(), (short)this.mServerInfo.getStun2Port(), natTypePointer);
                LogUtil.i(TAG, "getNATType return : " + ret);
                int natType = natTypePointer.getValue();
                LogUtil.i(TAG, "\u7f51\u7edc\u53d8\u66f4, \u5ba2\u6237\u7aefnat\u7c7b\u578b\u6539\u53d8");
                LogUtil.i(TAG, "\u7f51\u7edc\u53d8\u66f4\u524d\uff1a" + this.mNatType);
                LogUtil.i(TAG, "\u7f51\u7edc\u53d8\u66f4\u540e\uff1a" + natType);
                this.setNatType(natType);
                PointerByReference ipNATPointer = new PointerByReference();
                boolean b = this.mStunClient.Stun_GetNATIP(ipNATPointer);
                String ipNAT = ipNATPointer.getValue().getString(0L);
                LogUtil.i(TAG, "getNATIP return: " + (b ? "true" : "false") + ", ipNAT: " + ipNAT);
                this.setNetIP(ipNAT);
                if (TextUtils.isEmpty((CharSequence)this.mNetIP)) {
                    this.setNetIP(AppManager.getNetworkIp());
                }
            } else {
                LogUtil.e(TAG, "refreshNetInfo fail");
            }
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public void refreshNetTypeSyn() {
        if (this.mServerInfo == null) {
            return;
        }
        new Thread(new Runnable(){

            @Override
            public void run() {
                AppManager.this.refreshNetType();
                try {
                    EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setP2PPublicParam(AppManager.this.getNatType());
                }
                catch (Exception e) {
                    LogUtil.printErrStackTrace(AppManager.TAG, e.fillInStackTrace());
                }
            }
        }).start();
    }

    public synchronized void clearServerInfo() {
        this.mServerInfo = null;
    }

    private StreamServerData getStreamServerData() throws BaseException {
        if (this.mStreamerServerInfo == null) {
            int isp = this.getISPType();
            if (isp == -1) {
                this.getNetworkISPInfo();
                isp = this.getISPType();
            }
            this.mStreamerServerInfo = PlayAPI.getInstance().getStreamServer(isp);
        }
        return this.mStreamerServerInfo;
    }

    public void getAllStreamServer() throws BaseException {
        this.getStreamServerData();
    }

    public void clearAllStreamServer() {
        this.mStreamerServerInfo = null;
    }

    public CASClient getCASClientSDKInstance() {
        return this.mCASClientSDK;
    }

    private AppManager() {
        EZEcdhKeyInfo ezEcdhKeyInfo;
        if (!RobolectricConfig.ROBOLECRITIC_TEST) {
            this.mStunClient = StunClient.INSTANCE;
            this.mCASClientSDK = CASClient.getInstance();
            this.initLibs();
        }
        EZStreamClientManager appManager = EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext());
        appManager.setGlobalListener(new EZStreamClientManager.OnGlobalListener(){

            public void onP2PPreConnectStatistics(String devSerial, String json) {
                EZLogStreamP2pPreParams ezLogStreamP2PPreParams = new EZLogStreamP2pPreParams();
                ezLogStreamP2PPreParams.cnt = AppManager.this.getNatType();
                EZDcLogManager.getInstance().submit((BaseParams)ezLogStreamP2PPreParams, json);
            }

            public void onDirectPreConnectStatistics(String devSerial, String json) {
                EZLogStreamDirectDetectParams ezLogStreamDirectDetectParams = new EZLogStreamDirectDetectParams();
                EZDcLogManager.getInstance().submit((BaseParams)ezLogStreamDirectDetectParams, json);
            }

            public void onEvent(String szDevSerial, int eventType, String data) {
                LogUtil.d(AppManager.TAG, "onEvent devSerial = " + szDevSerial);
            }

            public void onData(int dataLen) {
                LogUtil.d(AppManager.TAG, "onData ");
            }

            public void onPreconnectResult(String szDevSerial, int clientType, boolean isSuccess) {
                LogUtil.d(AppManager.TAG, "onPreconnectResult szDevSerial");
            }

            public void onGlobalEventStatistics(int eventType, String statistics) {
                LogUtil.d(AppManager.TAG, "onGlobalEventStatistics szDevSerial");
            }

            public void onDevInfoUpdated(String szDevSerial, EZStreamSDKJNA.EZ_DEV_INFO devInfo) {
            }

            public void onReverseDirectUpnpStatistics(String json) {
            }
        });
        EcdhKeyInfo ecdhKeyInfo = SharedPreferencesUtils.getEcdhKeyInfo((Context)PlayAPI.mApplication);
        long day90 = 7776000000L;
        if (ecdhKeyInfo == null || ecdhKeyInfo.getTime() + day90 < System.currentTimeMillis()) {
            ezEcdhKeyInfo = new EZEcdhKeyInfo();
            appManager.generateECDHKey(ezEcdhKeyInfo);
            ecdhKeyInfo = EcdhKeyInfo.create(ezEcdhKeyInfo);
            SharedPreferencesUtils.setEcdhKeyInfo((Context)PlayAPI.mApplication, ecdhKeyInfo);
        } else {
            ezEcdhKeyInfo = ecdhKeyInfo.toEZEcdhKeyInfo();
        }
        appManager.setClientECDHKey(ezEcdhKeyInfo);
        this.localInfoParams = new EZLogLocalInfoParams();
        this.localInfoParams.os = String.valueOf(Build.VERSION.RELEASE);
        this.localInfoParams.phoneType = Build.MODEL;
    }

    public void getEZSDKConfigurationSyn() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                if (AppManager.this.mEZSDKConfiguration == null) {
                    EZSDKConfiguration eZSDKConfiguration = AppManager.this.getEZSDKConfiguration(false);
                }
            }
        }).start();
    }

    public synchronized EZSDKConfiguration getEZSDKConfiguration(boolean isReGet) {
        if (this.mEZSDKConfiguration == null || this.mEZSDKConfiguration != null && isReGet) {
            int retryCount = 0;
            int retryTotal = 3;
            int interal = 5;
            while (retryCount <= retryTotal) {
                try {
                    this.mEZSDKConfiguration = PlayAPI.getInstance().getConfiguration();
                    if (this.mEZSDKConfiguration == null) break;
                    EZDcLogManager.getInstance().setNeedUpLoadLog(this.mEZSDKConfiguration.getDataCollect() != 0);
                    break;
                }
                catch (BaseException e) {
                    LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
                    SystemClock.sleep((long)(interal * ++retryCount));
                }
            }
        }
        return this.mEZSDKConfiguration;
    }

    public int getNatType() {
        return this.mNatType;
    }

    public void setNatType(int netType) {
        this.mNatType = netType;
    }

    public String getNetIP() {
        return this.mNetIP;
    }

    public void setNetIP(String netIP) {
        this.mNetIP = netIP;
    }

    public void setISP(String ISP) {
        this.mISP = ISP;
    }

    public int getISPType() {
        return this.mISPType;
    }

    public String getISP() {
        return this.mISP;
    }

    public String getISPAddress() {
        return this.mISPAddress;
    }

    public String getWifiMacAddress() {
        return this.mWifiMacAddress;
    }

    public void setWifiMacAddress(String wifiMacAddress) {
        this.mWifiMacAddress = wifiMacAddress;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getNetworkIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://city.ip138.com/ip2city.asp");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection)connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gbk"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                Pattern pattern = Pattern.compile("(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        }
        catch (MalformedURLException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
            catch (IOException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        }
        return ipLine;
    }

    private String getLocalIpAddress() {
        String ipAddress = null;
        ipAddress = ConnectionDetector.getConnectionType((Context)PlayAPI.mApplication) == 3 ? this.getWifiIpAddress((Context)PlayAPI.mApplication) : this.get3GIpAddress();
        if (ipAddress == null) {
            try {
                Socket socket = new Socket(LocalInfo.getInstance().getServAddr(), 80);
                ipAddress = socket.getLocalAddress().getHostAddress();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ipAddress != null ? ipAddress : "172.0.0.1";
    }

    public String get3GIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress.isLoopbackAddress() || !Utils.isIp(inetAddress.getHostAddress())) continue;
                    return inetAddress.getHostAddress();
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        catch (SocketException ex) {
            LogUtil.e(TAG, "WifiPreference IpAddress " + ex.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getWifiIpAddress(Context context) {
        String ip = null;
        try {
            WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = String.format("%d.%d.%d.%d", ipAddress & 0xFF, ipAddress >> 8 & 0xFF, ipAddress >> 16 & 0xFF, ipAddress >> 24 & 0xFF);
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
        return ip;
    }

    public static boolean checkNetworkState(Context context) {
        boolean flag = false;
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService("connectivity");
        if (manager == null) {
            return false;
        }
        NetworkInfo mNetworkInfo = manager.getNetworkInfo(0);
        NetworkInfo.State mobile = null;
        if (mNetworkInfo == null) {
            return false;
        }
        mobile = manager.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(1).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            flag = true;
        }
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            flag = false;
        }
        return flag;
    }

    public static String getInetAddress(String hostName) {
        if (TextUtils.isEmpty((CharSequence)hostName)) {
            return null;
        }
        String IPAddress = AppManager.getCacheInetAddress(hostName = hostName.replace("http://", ""));
        if (!TextUtils.isEmpty((CharSequence)IPAddress)) {
            return IPAddress;
        }
        InetAddress[] inetAddress = null;
        int retryCount = 0;
        while (retryCount < 2) {
            ++retryCount;
            try {
                inetAddress = InetAddress.getAllByName(hostName);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (inetAddress == null) continue;
            String address = null;
            for (InetAddress inetAddress1 : inetAddress) {
                if (TextUtils.isEmpty((CharSequence)inetAddress1.getHostAddress())) continue;
                if (inetAddress1 instanceof Inet4Address) {
                    IPAddress = inetAddress1.getHostAddress();
                    break;
                }
                if (address != null) continue;
                address = inetAddress1.getHostAddress();
            }
            if (!TextUtils.isEmpty((CharSequence)IPAddress) || address == null) continue;
            IPAddress = address;
            break;
        }
        if (!TextUtils.isEmpty((CharSequence)IPAddress)) {
            AppManager.cacheInetAddress(hostName, IPAddress);
            return IPAddress;
        }
        return null;
    }

    private static void cacheInetAddress(String hostName, String IPAddress) {
        mInetAddressMap.put(hostName, IPAddress + "|" + System.currentTimeMillis());
    }

    private static String getCacheInetAddress(String hostName) {
        String[] caches;
        String IPAddress = mInetAddressMap.get(hostName);
        if (!TextUtils.isEmpty((CharSequence)IPAddress) && (caches = IPAddress.split("\\|")) != null && caches.length == 2) {
            try {
                long cacheTime = Long.parseLong(caches[1]);
                if (System.currentTimeMillis() - cacheTime < 86400000L) {
                    return caches[0];
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        mInetAddressMap.remove(hostName);
        return null;
    }

    public void getNetworkISPInfo() {
        String netIp = this.getNetIP();
        if (TextUtils.isEmpty((CharSequence)netIp)) {
            return;
        }
        String response = HttpUtils.sendPostRequest("http://ip.taobao.com/service/getIpInfo.php?ip=" + netIp);
        LogUtil.d("getNetworkISP", "netIp=" + netIp);
        if (response != null) {
            int code = 1;
            try {
                String data;
                JSONObject jsonObject = new JSONObject(response);
                code = jsonObject.optInt("code");
                if (code == 1) {
                    this.mISPType = ISP_TYPE_DIANXIN;
                }
                if ((data = jsonObject.optString("data")) == null) {
                    this.mISPType = ISP_TYPE_DIANXIN;
                }
                jsonObject = new JSONObject(data);
                this.mISP = jsonObject.getString("isp");
                this.mISPType = this.mISP == null ? ISP_TYPE_DIANXIN : (this.mISP.equals("\u8054\u901a") ? ISP_TYPE_LIANTONG : (this.mISP.equals("\u79fb\u52a8") ? ISP_TYPE_YIDONG : (this.mISP.equals("\u94c1\u901a") ? ISP_TYPE_TIETONG : (this.mISP.equals("\u534e\u6570") ? ISP_TYPE_HUASHU : ISP_TYPE_DIANXIN))));
                this.mISPAddress = jsonObject.getString("region") + jsonObject.getString("city");
            }
            catch (JSONException e) {
                LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
            }
        } else {
            this.mISPType = 0;
        }
    }

    public void initLibs() {
        try {
            StunClient.INSTANCE.Stun_Init();
        }
        catch (UnsatisfiedLinkError e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public void finiLibs() {
        try {
            StunClient.INSTANCE.Stun_Finit();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void refreshP2pUserInfo() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                P2pUserInfo p2pUserInfo = null;
                try {
                    p2pUserInfo = PlayAPI.getInstance().getP2pUserInfo();
                    if (p2pUserInfo == null) {
                        LogUtil.e(AppManager.TAG, "refreshP2pUserInfo: failed!");
                        return;
                    }
                    EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setP2PV3ConfigInfo(p2pUserInfo.getTranslatedP2pLinkKey(), p2pUserInfo.saltIndex, p2pUserInfo.saltVersion);
                    LocalInfo.getInstance().setUserCode(p2pUserInfo.userId);
                    LogUtil.i(AppManager.TAG, "refreshP2pUserInfo: finished");
                }
                catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static {
        try {
            System.loadLibrary("hpr");
        }
        catch (Exception var1) {
            var1.printStackTrace();
        }
        mInetAddressMap = new HashMap();
    }
}

