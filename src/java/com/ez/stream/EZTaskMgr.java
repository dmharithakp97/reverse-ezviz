/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 */
package com.ez.stream;

import android.os.Looper;
import com.ez.stream.HandleSingleExecutorPool;

public class EZTaskMgr {
    HandleSingleExecutorPool mHandlerThread = new HandleSingleExecutorPool("EZMediaPlayerHandler", 10);

    EZTaskMgr() {
        this.mHandlerThread.start();
    }

    public Looper getLooper() {
        return this.mHandlerThread.getLooper();
    }

    public synchronized void quit() {
        if (this.mHandlerThread != null) {
            this.mHandlerThread.quit();
        }
        this.mHandlerThread = null;
    }
}

