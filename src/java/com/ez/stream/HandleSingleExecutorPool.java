/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Looper
 *  android.os.Process
 */
package com.ez.stream;

import android.os.Looper;
import android.os.Process;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HandleSingleExecutorPool
implements Runnable {
    private ThreadPoolExecutor mTaskExecutor;
    private Looper mLooper;
    private final int mPriority;

    public HandleSingleExecutorPool(String name) {
        this(name, 0);
    }

    public HandleSingleExecutorPool(String name, int priority) {
        this.mPriority = priority;
        DefaultThreadFactory threadFactory = new DefaultThreadFactory(name);
        this.mTaskExecutor = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20), threadFactory);
        Executors.newSingleThreadExecutor();
    }

    public void start() {
        if (!this.mTaskExecutor.isShutdown()) {
            this.mTaskExecutor.execute(this);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Looper getLooper() {
        HandleSingleExecutorPool handleSingleExecutorPool = this;
        synchronized (handleSingleExecutorPool) {
            if (this.mLooper == null) {
                try {
                    this.wait();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return this.mLooper;
    }

    public void quit() {
        if (this.mLooper != null) {
            this.mLooper.quit();
        }
        if (this.mTaskExecutor != null && !this.mTaskExecutor.isShutdown()) {
            this.mTaskExecutor.shutdownNow();
            this.mTaskExecutor = null;
        }
    }

    public void quitSafely() {
        if (this.mLooper != null) {
            this.mLooper.quitSafely();
        }
        if (this.mTaskExecutor != null && !this.mTaskExecutor.isShutdown()) {
            this.mTaskExecutor.shutdown();
            this.mTaskExecutor = null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        Looper.prepare();
        HandleSingleExecutorPool handleSingleExecutorPool = this;
        synchronized (handleSingleExecutorPool) {
            this.mLooper = Looper.myLooper();
            this.notifyAll();
        }
        Process.setThreadPriority((int)this.mPriority);
        Looper.loop();
    }

    private static class DefaultThreadFactory
    implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.group, r, this.namePrefix, 0L);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            return t;
        }
    }
}

