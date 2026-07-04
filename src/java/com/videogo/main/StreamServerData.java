/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.main;

import com.videogo.main.AppManager;
import com.videogo.main.IspInfo;
import com.videogo.main.StreamServer;
import com.videogo.util.Utils;
import java.util.List;

public class StreamServerData {
    private StreamServer s1;
    private StreamServer s2;

    public StreamServer getS1() {
        return this.s1;
    }

    public void setS1(StreamServer s1) {
        this.s1 = s1;
    }

    public StreamServer getS2() {
        return this.s2;
    }

    public void setS2(StreamServer s2) {
        this.s2 = s2;
    }

    public String getS2ExternalIp(int ispcode) {
        if (this.s2 == null || this.s2.getIspInfos() == null) {
            return null;
        }
        String s2ExternalIp = null;
        IspInfo ispInfo = null;
        List<IspInfo> ispInfoList = this.s2.getIspInfos();
        for (int i = 0; i < ispInfoList.size(); ++i) {
            ispInfo = ispInfoList.get(i);
            if (ispInfo.getIspcode() == (long)ispcode) {
                s2ExternalIp = ispInfo.getExternalIp();
                break;
            }
            if (ispInfo.getIspcode() != 0L) continue;
            s2ExternalIp = ispInfo.getExternalIp();
        }
        String IPAddress = null;
        if (!Utils.isIp(s2ExternalIp)) {
            IPAddress = AppManager.getInetAddress(s2ExternalIp);
        }
        return IPAddress != null ? IPAddress : s2ExternalIp;
    }

    public int getS2ExternalDataPort() {
        if (this.s2 == null) {
            return 0;
        }
        return this.s2.getExternalDataPort();
    }

    public String getS1ExternalIp(int ispcode) {
        if (this.s1 == null || this.s1.getIspInfos() == null) {
            return null;
        }
        String s1ExternalIp = null;
        IspInfo ispInfo = null;
        List<IspInfo> ispInfoList = this.s1.getIspInfos();
        for (int i = 0; i < ispInfoList.size(); ++i) {
            ispInfo = ispInfoList.get(i);
            if (ispInfo.getIspcode() == (long)ispcode) {
                s1ExternalIp = ispInfo.getExternalIp();
                break;
            }
            if (ispInfo.getIspcode() != 0L) continue;
            s1ExternalIp = ispInfo.getExternalIp();
        }
        String IPAddress = null;
        if (!Utils.isIp(s1ExternalIp)) {
            IPAddress = AppManager.getInetAddress(s1ExternalIp);
        }
        return IPAddress != null ? IPAddress : s1ExternalIp;
    }

    public int getS1ExternalDataPort() {
        if (this.s1 == null) {
            return 0;
        }
        return this.s1.getExternalDataPort();
    }
}

