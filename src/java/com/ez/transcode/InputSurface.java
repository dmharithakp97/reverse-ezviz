/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.EGL14
 *  android.opengl.EGLConfig
 *  android.opengl.EGLContext
 *  android.opengl.EGLDisplay
 *  android.opengl.EGLExt
 *  android.opengl.EGLSurface
 *  android.util.Log
 *  android.view.Surface
 */
package com.ez.transcode;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

public class InputSurface {
    private static final String TAG = "InputSurface";
    private static final boolean VERBOSE = false;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private Surface mSurface;

    public InputSurface(Surface surface) {
        if (surface == null) {
            throw new NullPointerException();
        }
        this.mSurface = surface;
        this.eglSetup();
    }

    private void eglSetup() {
        this.mEGLDisplay = EGL14.eglGetDisplay((int)0);
        if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize((EGLDisplay)this.mEGLDisplay, (int[])version, (int)0, (int[])version, (int)1)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        int[] attribList = new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12344};
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig((EGLDisplay)this.mEGLDisplay, (int[])attribList, (int)0, (EGLConfig[])configs, (int)0, (int)configs.length, (int[])numConfigs, (int)0)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }
        int[] attrib_list = new int[]{12440, 2, 12344};
        this.mEGLContext = EGL14.eglCreateContext((EGLDisplay)this.mEGLDisplay, (EGLConfig)configs[0], (EGLContext)EGL14.EGL_NO_CONTEXT, (int[])attrib_list, (int)0);
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        int[] surfaceAttribs = new int[]{12344};
        this.mEGLSurface = EGL14.eglCreateWindowSurface((EGLDisplay)this.mEGLDisplay, (EGLConfig)configs[0], (Object)this.mSurface, (int[])surfaceAttribs, (int)0);
        this.checkEglError("eglCreateWindowSurface");
        if (this.mEGLSurface == null) {
            throw new RuntimeException("surface was null");
        }
    }

    public void release() {
        if (EGL14.eglGetCurrentContext().equals((Object)this.mEGLContext)) {
            EGL14.eglMakeCurrent((EGLDisplay)this.mEGLDisplay, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLContext)EGL14.EGL_NO_CONTEXT);
        }
        EGL14.eglDestroySurface((EGLDisplay)this.mEGLDisplay, (EGLSurface)this.mEGLSurface);
        EGL14.eglDestroyContext((EGLDisplay)this.mEGLDisplay, (EGLContext)this.mEGLContext);
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mSurface = null;
    }

    public void makeCurrent() {
        if (!EGL14.eglMakeCurrent((EGLDisplay)this.mEGLDisplay, (EGLSurface)this.mEGLSurface, (EGLSurface)this.mEGLSurface, (EGLContext)this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void releaseEGLContext() {
        if (!EGL14.eglMakeCurrent((EGLDisplay)this.mEGLDisplay, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLContext)EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public boolean swapBuffers() {
        return EGL14.eglSwapBuffers((EGLDisplay)this.mEGLDisplay, (EGLSurface)this.mEGLSurface);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void setPresentationTime(long nsecs) {
        EGLExt.eglPresentationTimeANDROID((EGLDisplay)this.mEGLDisplay, (EGLSurface)this.mEGLSurface, (long)nsecs);
    }

    private void checkEglError(String msg) {
        int error;
        boolean failed = false;
        while ((error = EGL14.eglGetError()) != 12288) {
            Log.e((String)TAG, (String)(msg + ": EGL error: 0x" + Integer.toHexString(error)));
            failed = true;
        }
        if (failed) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }
}

