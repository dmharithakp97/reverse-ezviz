/*
 * Decompiled with CFR 0.152.
 */
package com.ez.p2ptrans;

import com.ez.jna.EZP2PDevFileJNA;
import com.ez.p2ptrans.EZP2PBaseFetcher;

public interface EZDevFileCallback
extends EZP2PBaseFetcher.BaseCallback {
    public void onRespData(EZP2PDevFileJNA.EZP2PDevFileResp var1, byte[] var2);
}

