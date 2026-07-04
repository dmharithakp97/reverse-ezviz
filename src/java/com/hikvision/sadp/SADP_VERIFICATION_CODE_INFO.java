/*
 * Decompiled with CFR 0.152.
 */
package com.hikvision.sadp;

import com.hikvision.sadp.SADP_CONDITION;

public class SADP_VERIFICATION_CODE_INFO
extends SADP_CONDITION {
    public int dwSize;
    public byte[] szVerificationCode = new byte[12];
    public byte[] szPassword = new byte[16];
    public byte[] byRes = new byte[128];
}

