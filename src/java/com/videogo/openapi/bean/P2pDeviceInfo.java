/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ez.stream.EZP2PServerInfo
 */
package com.videogo.openapi.bean;

import com.ez.stream.EZP2PServerInfo;
import com.videogo.openapi.bean.P2pSerInfo;

public class P2pDeviceInfo {
    public P2pSerInfo[] serverInfos;
    public String defaultKey;
    public int defaultKeyVer;

    public EZP2PServerInfo[] getTranslatedP2pServerInfoArray() {
        if (this.serverInfos == null) {
            return null;
        }
        EZP2PServerInfo[] ezp2PServerInfos = new EZP2PServerInfo[this.serverInfos.length];
        int index = 0;
        for (P2pSerInfo server : this.serverInfos) {
            EZP2PServerInfo ezp2PServerInfo = new EZP2PServerInfo();
            ezp2PServerInfo.szP2PServerIp = server.ip;
            ezp2PServerInfo.iP2PServerPort = server.port;
            ezp2PServerInfos[index] = ezp2PServerInfo;
            ++index;
        }
        return ezp2PServerInfos;
    }

    public String getDeviceP2pServerString() {
        StringBuffer sb = new StringBuffer();
        if (this.serverInfos.length > 0) {
            for (int i = 0; i < this.serverInfos.length; ++i) {
                P2pSerInfo serInfo = this.serverInfos[i];
                sb.append(serInfo.ip);
                sb.append(":");
                sb.append(serInfo.port);
                if (i == this.serverInfos.length - 1) continue;
                sb.append(";");
            }
        }
        return sb.toString();
    }
}

