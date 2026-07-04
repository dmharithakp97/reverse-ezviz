/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.opengl.GLES20
 *  android.opengl.Matrix
 *  android.util.Log
 */
package com.ez.transcode;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TextureRender {
    private static final String TAG = "TextureRender";
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private final float[] mTriangleVerticesData = new float[]{-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    private FloatBuffer mTriangleVertices;
    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];
    private int mProgram;
    private int mTextureID = -12345;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int maPositionHandle;
    private int maTextureHandle;

    public TextureRender() {
        this.mTriangleVertices = ByteBuffer.allocateDirect(this.mTriangleVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangleVertices.put(this.mTriangleVerticesData).position(0);
        Matrix.setIdentityM((float[])this.mSTMatrix, (int)0);
    }

    public int getTextureId() {
        return this.mTextureID;
    }

    public void drawFrame(SurfaceTexture st) {
        this.checkGlError("onDrawFrame start");
        st.getTransformMatrix(this.mSTMatrix);
        GLES20.glClearColor((float)0.0f, (float)1.0f, (float)0.0f, (float)1.0f);
        GLES20.glClear((int)16640);
        GLES20.glUseProgram((int)this.mProgram);
        this.checkGlError("glUseProgram");
        GLES20.glActiveTexture((int)33984);
        GLES20.glBindTexture((int)36197, (int)this.mTextureID);
        this.mTriangleVertices.position(0);
        GLES20.glVertexAttribPointer((int)this.maPositionHandle, (int)3, (int)5126, (boolean)false, (int)20, (Buffer)this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray((int)this.maPositionHandle);
        this.checkGlError("glEnableVertexAttribArray maPositionHandle");
        this.mTriangleVertices.position(3);
        GLES20.glVertexAttribPointer((int)this.maTextureHandle, (int)2, (int)5126, (boolean)false, (int)20, (Buffer)this.mTriangleVertices);
        this.checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray((int)this.maTextureHandle);
        this.checkGlError("glEnableVertexAttribArray maTextureHandle");
        Matrix.setIdentityM((float[])this.mMVPMatrix, (int)0);
        GLES20.glUniformMatrix4fv((int)this.muMVPMatrixHandle, (int)1, (boolean)false, (float[])this.mMVPMatrix, (int)0);
        GLES20.glUniformMatrix4fv((int)this.muSTMatrixHandle, (int)1, (boolean)false, (float[])this.mSTMatrix, (int)0);
        GLES20.glDrawArrays((int)5, (int)0, (int)4);
        this.checkGlError("glDrawArrays");
        GLES20.glFinish();
    }

    public void surfaceCreated() {
        this.mProgram = this.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (this.mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
        this.maPositionHandle = GLES20.glGetAttribLocation((int)this.mProgram, (String)"aPosition");
        this.checkGlError("glGetAttribLocation aPosition");
        if (this.maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        this.maTextureHandle = GLES20.glGetAttribLocation((int)this.mProgram, (String)"aTextureCoord");
        this.checkGlError("glGetAttribLocation aTextureCoord");
        if (this.maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
        this.muMVPMatrixHandle = GLES20.glGetUniformLocation((int)this.mProgram, (String)"uMVPMatrix");
        this.checkGlError("glGetUniformLocation uMVPMatrix");
        if (this.muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        this.muSTMatrixHandle = GLES20.glGetUniformLocation((int)this.mProgram, (String)"uSTMatrix");
        this.checkGlError("glGetUniformLocation uSTMatrix");
        if (this.muSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }
        int[] textures = new int[1];
        GLES20.glGenTextures((int)1, (int[])textures, (int)0);
        this.mTextureID = textures[0];
        GLES20.glBindTexture((int)36197, (int)this.mTextureID);
        this.checkGlError("glBindTexture mTextureID");
        GLES20.glTexParameterf((int)36197, (int)10241, (float)9728.0f);
        GLES20.glTexParameterf((int)36197, (int)10240, (float)9729.0f);
        GLES20.glTexParameteri((int)36197, (int)10242, (int)33071);
        GLES20.glTexParameteri((int)36197, (int)10243, (int)33071);
        this.checkGlError("glTexParameter");
    }

    public void changeFragmentShader(String fragmentShader) {
        GLES20.glDeleteProgram((int)this.mProgram);
        this.mProgram = this.createProgram(VERTEX_SHADER, fragmentShader);
        if (this.mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader((int)shaderType);
        this.checkGlError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource((int)shader, (String)source);
        GLES20.glCompileShader((int)shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv((int)shader, (int)35713, (int[])compiled, (int)0);
        if (compiled[0] == 0) {
            Log.e((String)TAG, (String)("Could not compile shader " + shaderType + ":"));
            Log.e((String)TAG, (String)(" " + GLES20.glGetShaderInfoLog((int)shader)));
            GLES20.glDeleteShader((int)shader);
            shader = 0;
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = this.loadShader(35633, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = this.loadShader(35632, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        this.checkGlError("glCreateProgram");
        if (program == 0) {
            Log.e((String)TAG, (String)"Could not create program");
        }
        GLES20.glAttachShader((int)program, (int)vertexShader);
        this.checkGlError("glAttachShader");
        GLES20.glAttachShader((int)program, (int)pixelShader);
        this.checkGlError("glAttachShader");
        GLES20.glLinkProgram((int)program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv((int)program, (int)35714, (int[])linkStatus, (int)0);
        if (linkStatus[0] != 1) {
            Log.e((String)TAG, (String)"Could not link program: ");
            Log.e((String)TAG, (String)GLES20.glGetProgramInfoLog((int)program));
            GLES20.glDeleteProgram((int)program);
            program = 0;
        }
        return program;
    }

    public void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != 0) {
            Log.e((String)TAG, (String)(op + ": glError " + error));
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}

