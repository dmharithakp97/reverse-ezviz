/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

import com.hikvision.sadp.SADP_DEVICE_INFO;

public class SADP_DEVICE_INFO_V40 {
    public SADP_DEVICE_INFO struSadpDeviceInfo = new SADP_DEVICE_INFO();
    public byte byLicensed;
    public byte bySystemMode;
    public byte byControllerType;
    public byte[] szEhmoeVersion = new byte[16];
    public byte bySpecificDeviceType;
    public int dwSDKOverTLSPort;
    public byte bySecurityMode;
    public byte bySDKServerStatus;
    public byte bySDKOverTLSServerStatus;
    public byte[] szUserName = new byte[33];
    public byte[] byRes = new byte[452];
}

