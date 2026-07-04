/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.graphics.SurfaceTexture$OnFrameAvailableListener
 *  android.util.Log
 *  android.view.Surface
 *  javax.microedition.khronos.egl.EGL10
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.egl.EGLContext
 *  javax.microedition.khronos.egl.EGLDisplay
 *  javax.microedition.khronos.egl.EGLSurface
 */
package com.ez.transcode;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import com.ez.transcode.TextureRender;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class OutputSurface
implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "OutputSurface";
    private static final boolean VERBOSE = false;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private EGL10 mEGL;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private Object mFrameSyncObject = new Object();
    private boolean mFrameAvailable;
    private TextureRender mTextureRender;

    public OutputSurface(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        this.eglSetup(width, height);
        this.makeCurrent();
        this.setup();
    }

    public OutputSurface() {
        this.setup();
    }

    private void setup() {
        this.mTextureRender = new TextureRender();
        this.mTextureRender.surfaceCreated();
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    private void eglSetup(int width, int height) {
        this.mEGL = (EGL10)EGLContext.getEGL();
        this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (!this.mEGL.eglInitialize(this.mEGLDisplay, null)) {
            throw new RuntimeException("unable to initialize EGL10");
        }
        int[] attribList = new int[]{12324, 8, 12323, 8, 12322, 8, 12339, 1, 12352, 4, 12344};
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!this.mEGL.eglChooseConfig(this.mEGLDisplay, attribList, configs, 1, numConfigs)) {
            throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
        }
        int[] attrib_list = new int[]{12440, 2, 12344};
        this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, configs[0], EGL10.EGL_NO_CONTEXT, attrib_list);
        this.checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        int[] surfaceAttribs = new int[]{12375, width, 12374, height, 12344};
        this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, configs[0], surfaceAttribs);
        this.checkEglError("eglCreatePbufferSurface");
        if (this.mEGLSurface == null) {
            throw new RuntimeException("surface was null");
        }
    }

    public void release() {
        if (this.mEGL != null) {
            if (this.mEGL.eglGetCurrentContext().equals(this.mEGLContext)) {
                this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        }
        if (this.mSurfaceTexture != null) {
            this.mSurfaceTexture.setOnFrameAvailableListener(null);
        }
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mEGL = null;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public void makeCurrent() {
        if (this.mEGL == null) {
            throw new RuntimeException("not configured for makeCurrent");
        }
        this.checkEglError("before makeCurrent");
        if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void changeFragmentShader(String fragmentShader) {
        this.mTextureRender.changeFragmentShader(fragmentShader);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void awaitNewImage() throws Exception {
        int TIMEOUT_MS = 500;
        Object object = this.mFrameSyncObject;
        synchronized (object) {
            while (!this.mFrameAvailable) {
                this.mFrameSyncObject.wait(500L);
                if (this.mFrameAvailable) continue;
                throw new RuntimeException("Surface frame wait timed out");
            }
            this.mFrameAvailable = false;
        }
        this.mTextureRender.checkGlError("before updateTexImage");
        this.mSurfaceTexture.updateTexImage();
    }

    public void drawImage() {
        this.mTextureRender.drawFrame(this.mSurfaceTexture);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void onFrameAvailable(SurfaceTexture st) {
        Object object = this.mFrameSyncObject;
        synchronized (object) {
            if (this.mFrameAvailable) {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
            this.mFrameAvailable = true;
            this.mFrameSyncObject.notifyAll();
        }
    }

    private void checkEglError(String msg) {
        int error;
        boolean failed = false;
        while ((error = this.mEGL.eglGetError()) != 12288) {
            Log.e((String)TAG, (String)(msg + ": EGL error: 0x" + Integer.toHexString(error)));
            failed = true;
        }
        if (failed) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }
}

