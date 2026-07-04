/*
 * Decompiled with CFR 0.152.
 */
package com.ez.p2ptrans;

import com.ez.jna.EZP2PRecordCoverJNA;
import com.ez.p2ptrans.EZP2PBaseFetcher;

public interface RecordCoverCallback
extends EZP2PBaseFetcher.BaseCallback {
    public void onRespData(EZP2PRecordCoverJNA.EZRecordResp var1, byte[] var2);
}

