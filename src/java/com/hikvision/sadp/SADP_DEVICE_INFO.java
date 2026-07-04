/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

public class SADP_DEVICE_INFO {
    public byte[] szSeries = new byte[12];
    public byte[] szSerialNO = new byte[48];
    public byte[] szMAC = new byte[20];
    public byte[] szIPv4Address = new byte[16];
    public byte[] szIPv4SubnetMask = new byte[16];
    public int dwDeviceType;
    public int dwPort;
    public int dwNumberOfEncoders;
    public int dwNumberOfHardDisk;
    public byte[] szDeviceSoftwareVersion = new byte[48];
    public byte[] szDSPVersion = new byte[48];
    public byte[] szBootTime = new byte[48];
    public int iResult;
    public byte[] szDevDesc = new byte[24];
    public byte[] szOEMinfo = new byte[24];
    public byte[] szIPv4Gateway = new byte[16];
    public byte[] szIPv6Address = new byte[46];
    public byte[] szIPv6Gateway = new byte[46];
    public byte byIPv6MaskLen;
    public byte bySupport;
    public byte byDhcpEnabled;
    public byte byDeviceAbility;
    public short wHttpPort;
    public short wDigitalChannelNum;
    public byte[] szCmsIPv4 = new byte[16];
    public short wCmsPort;
    public byte byOEMCode;
    public byte byActivated;
    public byte[] szBaseDesc = new byte[24];
    public byte bySupport1;
    public byte byHCPlatform;
    public byte byEnableHCPlatform;
    public byte byEZVIZCode;
    public int dwDetailOEMCode;
    public byte byModifyVerificationCode;
    public byte[] byRes2 = new byte[7];
}

