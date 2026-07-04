/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

import com.hikvision.sadp.SADP_CONDITION;

public class SADP_HCPLATFORM_STATUS_INFO
extends SADP_CONDITION {
    public int dwSize;
    public byte byEnableHCPlatform;
    public byte[] byRes = new byte[3];
    public byte[] szPassword = new byte[16];
    public byte[] byRes2 = new byte[128];
}

