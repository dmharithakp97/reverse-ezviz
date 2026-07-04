/*
 * Decompiled with CFR 0.152.
 */
package com.ez.stream;

import com.ez.stream.EZP2PServerInfo;
import com.ez.stream.P2PServerKey;
import java.util.Arrays;

public class InitParam {
    public static final int EZ_STREAM_DISABLE_NONE = 0;
    public static final int EZ_STREAM_DISABLE_DIRECT_INNER = 1;
    public static final int EZ_STREAM_DISABLE_DIRECT_OUTER = 2;
    public static final int EZ_STREAM_DISABLE_P2P = 4;
    public static final int EZ_STREAM_DISABLE_DIRECT_REVERSE = 8;
    public static final int EZ_STREAM_DISABLE_PRIVATE_STREAM = 16;
    public static final int EZ_STREAM_SOURCE_LIVE_MINE = 0;
    public static final int EZ_STREAM_SOURCE_LIVE_SQUERE = 1;
    public static final int EZ_STREAM_SOURCE_PLAYBACK_LOCAL = 2;
    public static final int EZ_STREAM_SOURCE_PLAYBACK_CLOUD = 3;
    public static final int EZ_STREAM_SOURCE_RECORDING_CLOUD = 4;
    public static final int EZ_STREAM_SOURCE_LOCAL_DOWNLOAD = 5;
    public static final int EZ_STREAM_SOURCE_TALKBACK = 6;
    public static final int EZ_STREAM_SOURCE_PLAYBACK_LOCAL_EX = 8;
    public static final int EZ_STREAM_SOURCE_PLAYBACK_CLOUD_EX = 9;
    public int iStreamSource;
    public int iStreamInhibit;
    public int iPreOpWhileStream;
    public String szDevIP;
    public String szDevLocalIP;
    public int iDevCmdPort;
    public int iDevCmdLocalPort;
    public int iDevStreamPort;
    public int iDevStreamLocalPort;
    public int iStreamType;
    public int iVideoLevel;
    public int iChannelNumber;
    public String szChnlIndex;
    public String szDevSerial;
    public String szSuperDeviceSerial;
    public String szChnlSerial;
    public int iVoiceChannelNumber;
    public String szHardwareCode;
    public String szTtsIP;
    public String szTtsBackupIP;
    public int iTtsPort;
    public int iTalkType;
    public String szCallingId;
    public int iMicType;
    public String szClientSession;
    public String szStreamToken;
    public String szPermanetkey;
    public String szCasServerIP;
    public int iCasServerPort;
    public String szStunIP;
    public int iStunPort;
    public int iClnType;
    public int iVtmPort;
    public String szVtmIP;
    public String szVtmBackIP;
    public int iVtmV6Port;
    public String szVtmV6IP;
    public int iVtduTransferType = 0;
    public int iStreamTimeOut;
    public String szCloudServerIP;
    public int iCloudServerPort;
    public String szCloudServerBackupIP;
    public String szTicketToken;
    public String szExtensionParas;
    public int iIPV6 = 0;
    public int iNeedProxy;
    public int iSupportNAT34;
    public int iChannelCount;
    public boolean support_new_talk;
    public int iInternetType;
    public int iClnIspType;
    public int iCheckInterval;
    public int iP2PVersion;
    public int iP2PSPS;
    public String szUserID;
    public int iPlaybackSpeed = 0;
    public int iNetSDKUserId = -1;
    public int iNetSDKChannelNumber;
    public EZP2PServerInfo[] p2pServerList;
    public P2PServerKey stP2PServerKey;
    public int iStorageVersion = 1;
    public int iCloudVideoType = -1;
    public int iSDCardVideoType;
    public int iBusType;
    public int iInterlaceFlag;
    public int iFrameInterval;
    @Deprecated
    public String szVtduIpCache = "";
    @Deprecated
    public int iVtduPortCache = 0;
    public String szLid = "";
    public int usP2PKeyVer = 0;
    public byte[] szP2PLinkKey = new byte[32];
    public int iShared = 0;
    public int iSmallStream = 0;
    public int isSmallMtu = 0;
    public int iDevSupportAsyn = 1;
    public int iSupportPlayBackEndFlag = 0;
    public String szStartTime = null;
    public String szStopTime = null;
    public String szFileID = null;
    public byte[] vtduServerPublicKey = new byte[91];
    public int vtduServerKeyVersion = 0;
    public int iQosTalkVersion = 0;
    public String szQosTaklIP;
    public int iQosTakPort;
    public int iLinkEncryptV2;
    public String szExtInfo;
    public int udpEcdh;

    public String toString() {
        return "InitParam{iStreamSource=" + this.iStreamSource + ", iStreamInhibit=" + this.iStreamInhibit + ", szDevIP='" + this.szDevIP + '\'' + ", szDevLocalIP='" + this.szDevLocalIP + '\'' + ", iDevCmdPort=" + this.iDevCmdPort + ", iDevCmdLocalPort=" + this.iDevCmdLocalPort + ", iDevStreamPort=" + this.iDevStreamPort + ", iDevStreamLocalPort=" + this.iDevStreamLocalPort + ", iStreamType=" + this.iStreamType + ", iVideoLevel=" + this.iVideoLevel + ", iChannelNumber=" + this.iChannelNumber + ", szSuperDeviceSerial=" + this.szSuperDeviceSerial + ", szDevSerial='" + this.szDevSerial + '\'' + ", szChnlSerial='" + this.szChnlSerial + '\'' + ", iVoiceChannelNumber=" + this.iVoiceChannelNumber + ", szHardwareCode='" + this.szHardwareCode + '\'' + ", szTtsIP='" + this.szTtsIP + '\'' + ", szTtsBackupIP='" + this.szTtsBackupIP + '\'' + ", iTtsPort=" + this.iTtsPort + ", szClientSession='" + this.szClientSession + '\'' + ", szPermanetkey='" + this.szPermanetkey + '\'' + ", szCasServerIP='" + this.szCasServerIP + '\'' + ", iCasServerPort=" + this.iCasServerPort + ", szStunIP='" + this.szStunIP + '\'' + ", iStunPort=" + this.iStunPort + ", iClnType=" + this.iClnType + ", iVtmPort=" + this.iVtmPort + ", szVtmIP='" + this.szVtmIP + '\'' + ", iVtmV6Port=" + this.iVtmV6Port + ", szVtmV6IP='" + this.szVtmV6IP + '\'' + ", iStreamTimeOut=" + this.iStreamTimeOut + ", szCloudServerIP='" + this.szCloudServerIP + '\'' + ", szCloudServerBackupIP='" + this.szCloudServerBackupIP + '\'' + ", iCloudServerPort=" + this.iCloudServerPort + ", szTicketToken='" + this.szTicketToken + '\'' + ", szExtensionParas='" + this.szExtensionParas + '\'' + ", iIPV6=" + this.iIPV6 + ", iNeedProxy=" + this.iNeedProxy + ", iSupportNAT34=" + this.iSupportNAT34 + ", iChannelCount=" + this.iChannelCount + ", support_new_talk=" + this.support_new_talk + ", iInternetType=" + this.iInternetType + ", iCheckInterval=" + this.iCheckInterval + ", iP2PVersion=" + this.iP2PVersion + ", szUserID='" + this.szUserID + '\'' + ", iPlaybackSpeed=" + this.iPlaybackSpeed + ", iNetSDKUserId=" + this.iNetSDKUserId + ", iNetSDKChannelNumber=" + this.iNetSDKChannelNumber + ", p2pServerList=" + Arrays.toString(this.p2pServerList) + ", iStorageVersion=" + this.iStorageVersion + ", iCloudVideoType=" + this.iCloudVideoType + ", iSDCardVideoType=" + this.iSDCardVideoType + ", iFrameInterval=" + this.iFrameInterval + ", szLid='" + this.szLid + '\'' + ", usP2PKeyVer=" + this.usP2PKeyVer + ", szP2PLinkKey=" + Arrays.toString(this.szP2PLinkKey) + ", iShared=" + this.iShared + ", iSmallStream=" + this.iSmallStream + ", isSmallMtu=" + this.isSmallMtu + ", iDevSupportAsyn=" + this.iDevSupportAsyn + ", iSupportPlayBackEndFlag=" + this.iSupportPlayBackEndFlag + ", iLinkEncryptV2=" + this.iLinkEncryptV2 + ", szStartTime='" + this.szStartTime + '\'' + ", szStopTime='" + this.szStopTime + '\'' + ", szFileID='" + this.szFileID + '\'' + ", vtduServerPublicKey=" + Arrays.toString(this.vtduServerPublicKey) + ", vtduServerKeyVersion=" + this.vtduServerKeyVersion + ", iQosTalkVersion=" + this.iQosTalkVersion + ", szQosTaklIP='" + this.szQosTaklIP + '\'' + ", iQosTakPort=" + this.iQosTakPort + ", szExtInfo='" + this.szExtInfo + '\'' + '}';
    }
}

