/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 *  android.media.MediaCodec$Callback
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 */
package com.ez.transcode;

import android.media.MediaCodec;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.IOException;

public class CallbackHandler
extends Handler {
    private MediaCodec mCodec;
    private boolean mEncoder;
    private MediaCodec.Callback mCallback;
    private String mMime;
    private boolean mSetDone;

    CallbackHandler(Looper l) {
        super(l);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void handleMessage(Message msg) {
        try {
            this.mCodec = this.mEncoder ? MediaCodec.createEncoderByType((String)this.mMime) : MediaCodec.createDecoderByType((String)this.mMime);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.mCodec.setCallback(this.mCallback);
        CallbackHandler callbackHandler = this;
        synchronized (callbackHandler) {
            this.mSetDone = true;
            ((Object)((Object)this)).notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void create(boolean encoder, String mime, MediaCodec.Callback callback) {
        this.mEncoder = encoder;
        this.mMime = mime;
        this.mCallback = callback;
        this.mSetDone = false;
        this.sendEmptyMessage(0);
        CallbackHandler callbackHandler = this;
        synchronized (callbackHandler) {
            while (!this.mSetDone) {
                try {
                    ((Object)((Object)this)).wait();
                }
                catch (InterruptedException interruptedException) {}
            }
        }
    }

    MediaCodec getCodec() {
        return this.mCodec;
    }
}

