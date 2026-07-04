/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

public class SADP_DEV_NET_PARAM {
    public byte[] szIPv4Address = new byte[16];
    public byte[] szIPv4SubnetMask = new byte[16];
    public byte[] szIPv4Gateway = new byte[16];
    public byte[] szIPv6Address = new byte[128];
    public byte[] szIPv6Gateway = new byte[128];
    public int wPort;
    public byte byIPv6MaskLen;
    public byte byDhcpEnabled;
    public int wHttpPort;
    public byte[] byRes = new byte[126];
}

