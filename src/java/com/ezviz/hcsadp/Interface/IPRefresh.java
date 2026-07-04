/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.ezviz.hcsadp.Interface;

import android.util.Log;
import com.ezviz.hcsadp.Interface.JavaInterface;
import com.ezviz.hcsadp.jna.HCSadpSDKByJNA;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPRefresh
extends Thread {
    private boolean m_bExit = false;
    private HCSadpSDKByJNA.ADAPTER_INFO_LIST m_struAdapterInfoList = new HCSadpSDKByJNA.ADAPTER_INFO_LIST();

    @Override
    public void run() {
        super.run();
        Log.i((String)"IPRefresh", (String)"IPRefresh run begin!");
        while (!this.m_bExit) {
            boolean iRet;
            int i;
            HCSadpSDKByJNA.ADAPTER_INFO_LIST struAdapterInfoList = new HCSadpSDKByJNA.ADAPTER_INFO_LIST();
            this.getNetworkInfoJava(struAdapterInfoList);
            int j = 0;
            boolean bIPv4Changed = false;
            boolean bIPv6Changed = false;
            for (i = 0; i < struAdapterInfoList.dwCount; ++i) {
                while (j < this.m_struAdapterInfoList.dwCount && struAdapterInfoList.struAdapterInfo[i].szIPAddrStr != this.m_struAdapterInfoList.struAdapterInfo[j].szIPAddrStr) {
                    ++j;
                }
                if (j != this.m_struAdapterInfoList.dwCount) continue;
                bIPv4Changed = true;
                break;
            }
            for (i = 0; i < struAdapterInfoList.dwCount; ++i) {
                for (j = 0; j < this.m_struAdapterInfoList.dwCount && struAdapterInfoList.struAdapterInfo[i].szIPv6 != this.m_struAdapterInfoList.struAdapterInfo[j].szIPv6; ++j) {
                }
                if (j != this.m_struAdapterInfoList.dwCount) continue;
                bIPv6Changed = true;
                break;
            }
            if ((bIPv4Changed || bIPv6Changed) && (iRet = JavaInterface.getInstance().SADP_Set_AdapterInfo(struAdapterInfoList))) {
                this.m_struAdapterInfoList = struAdapterInfoList;
            }
            try {
                IPRefresh.sleep(5000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i((String)"IPRefresh", (String)"IPRefresh run end!");
    }

    public void SetExit() {
        this.m_bExit = true;
    }

    public void SetAdapterInfo() {
        HCSadpSDKByJNA.ADAPTER_INFO_LIST struAdapterInfoList = new HCSadpSDKByJNA.ADAPTER_INFO_LIST();
        this.getNetworkInfoJava(struAdapterInfoList);
        JavaInterface.getInstance().SADP_Set_AdapterInfo(struAdapterInfoList);
    }

    private void getNetworkInfoJava(HCSadpSDKByJNA.ADAPTER_INFO_LIST struAdapterInfoList) {
        int iCount = 0;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                byte[] mac = ni.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                if (mac != null) {
                    for (byte b : mac) {
                        sb.append(String.format("%02X:", b));
                    }
                } else {
                    sb.append("00:00:00:00:00:00");
                }
                boolean bFind = false;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    String sIpv6Address;
                    InetAddress addr = addresses.nextElement();
                    if (addr.isLoopbackAddress()) continue;
                    if (addr instanceof Inet4Address) {
                        bFind = true;
                        Log.i((String)"NetInfo", (String)("IPv4: " + addr.getHostAddress()));
                        System.arraycopy(addr.getHostAddress().getBytes(), 0, struAdapterInfoList.struAdapterInfo[iCount].szIPAddrStr, 0, addr.getHostAddress().length());
                        continue;
                    }
                    if (!(addr instanceof Inet6Address) || (sIpv6Address = addr.getHostAddress().split("%")[0]) == null || sIpv6Address.length() == 0) continue;
                    bFind = true;
                    System.arraycopy(sIpv6Address.getBytes(), 0, struAdapterInfoList.struAdapterInfo[iCount].szIPv6, 0, sIpv6Address.length());
                    Log.i((String)"NetInfo", (String)("IPv6: " + addr.getHostAddress()));
                }
                if (!bFind) continue;
                Log.i((String)"NetInfo", (String)("MAC: " + sb.substring(0, sb.length() - 1)));
                System.arraycopy(ni.getDisplayName().getBytes(), 0, struAdapterInfoList.struAdapterInfo[iCount].szDeviceName, 0, ni.getDisplayName().length());
                System.arraycopy(sb.substring(0, sb.length() - 1).getBytes(), 0, struAdapterInfoList.struAdapterInfo[iCount].szHWAddrStr, 0, sb.substring(0, sb.length() - 1).length());
                ++iCount;
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        struAdapterInfoList.dwCount = iCount;
    }
}

