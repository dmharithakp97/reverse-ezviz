/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaFormat
 *  android.view.Surface
 */
package com.ez.transcode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import com.ez.stream.LogUtil;
import com.ez.transcode.ConvertCallback;
import com.ez.transcode.ConvertParam;
import com.ez.transcode.DataBuffer;
import com.ez.transcode.InputSurface;
import com.ez.transcode.MediaContainer;
import com.ez.transcode.OutputSurface;
import com.ez.transcode.TransCodeListener;
import com.ez.transcode.VideoDecoder;
import com.ez.transcode.VideoEncoder;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class MP4Converter {
    public static String TAG = "MP4Converter";
    ConvertParam convertParam;
    private String srcMimeType;
    private String dstMimeType;
    private MediaFormat inputFormat;
    private VideoEncoder mVideoEncoder;
    private VideoDecoder mVideoDecoder;
    private InputSurface mInputSurface;
    private OutputSurface mOutputSurface;
    private TransCodeListener convertListener;
    private boolean isCancel;
    private DataBuffer mDataBuffer = null;
    private DataBuffer mSPSDataBuffer = null;
    private final Object objLock = new Object();
    private final Object finishLock = new Object();
    private boolean isFinish;
    private final Object eosLock = new Object();
    private boolean eosFinish;
    private int inputEndFlag = 0;

    public void setConvertListener(TransCodeListener convertListener) {
        this.convertListener = convertListener;
    }

    public boolean init(ConvertParam convertParam) {
        this.convertParam = convertParam;
        if (convertParam.dataCB == 0L || convertParam.srcWidth <= 0 || convertParam.srcHeight <= 0 || convertParam.dstWidth <= 0 || convertParam.dstHeight <= 0 || convertParam.bitrate <= 0 || convertParam.pFCUser == 0L || convertParam.gopLen <= 0) {
            return false;
        }
        switch (convertParam.srcCodecType) {
            case 256: {
                this.srcMimeType = "video/avc";
                break;
            }
            case 5: {
                this.srcMimeType = "video/hevc";
                break;
            }
            default: {
                return false;
            }
        }
        switch (convertParam.dstCodecType) {
            case 256: {
                this.dstMimeType = "video/avc";
                break;
            }
            case 5: {
                this.dstMimeType = "video/hevc";
                break;
            }
            default: {
                return false;
            }
        }
        this.inputFormat = MediaFormat.createVideoFormat((String)this.srcMimeType, (int)convertParam.srcWidth, (int)convertParam.srcHeight);
        if (this.inputFormat == null) {
            return false;
        }
        try {
            this.mVideoEncoder = new VideoEncoder(convertParam, this.dstMimeType);
            this.mVideoEncoder.setConvertCallback(new CodecCallbackImpl(CodecType.ENCODE));
            boolean support = this.mVideoEncoder.getMediaCodec().getCodecInfo().getCapabilitiesForType(this.dstMimeType).getVideoCapabilities().isSizeSupported(convertParam.dstWidth, convertParam.dstHeight);
            if (!support) {
                throw new Exception("not support encode resolution :" + convertParam.dstWidth + "_" + convertParam.dstHeight);
            }
            this.mVideoDecoder = new VideoDecoder(convertParam, this.srcMimeType);
            this.mVideoDecoder.setConvertCallback(new CodecCallbackImpl(CodecType.DECODE));
            support = this.mVideoDecoder.getMediaCodec().getCodecInfo().getCapabilitiesForType(this.srcMimeType).getVideoCapabilities().isSizeSupported(convertParam.srcWidth, convertParam.srcHeight);
            if (!support) {
                throw new Exception("not support decode resolution :" + convertParam.srcWidth + "_" + convertParam.srcHeight);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean start() {
        return this.processVideo();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int inputData(byte[] data, int len, long timeStamp, long frameType) {
        Object object = this.objLock;
        synchronized (object) {
            while (!this.isCancel) {
                if (this.mDataBuffer == null) {
                    this.mDataBuffer = new DataBuffer(data, len, timeStamp, frameType);
                    LogUtil.d(TAG, "input data len = " + len + " timestamp = " + timeStamp);
                    if (len == 0 && this.inputEndFlag == 0) {
                        this.inputEndFlag = 2;
                    }
                    this.objLock.notifyAll();
                    return 0;
                }
                try {
                    this.objLock.wait();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release() {
        if (this.inputEndFlag == 0) {
            this.inputEndFlag = 1;
            this.inputData(null, 0, 0L, 0L);
        }
        Object object = this.finishLock;
        synchronized (object) {
            if (!this.isFinish) {
                try {
                    this.finishLock.wait(10000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.releaseMediaConverter(this.mVideoDecoder);
        this.releaseMediaConverter(this.mVideoEncoder);
        this.mVideoDecoder = null;
        this.mVideoEncoder = null;
        if (this.mOutputSurface != null) {
            this.mOutputSurface.release();
            this.mOutputSurface = null;
        }
        if (this.mInputSurface != null) {
            this.mInputSurface.release();
            this.mInputSurface = null;
        }
    }

    private void releaseMediaConverter(MediaContainer mediaContainer) {
        if (mediaContainer != null) {
            mediaContainer.release();
        }
    }

    private boolean processVideo() {
        boolean ret = this.mVideoEncoder.config(this.dstMimeType);
        if (!ret) {
            return false;
        }
        AtomicReference<Surface> inputSurfaceReference = new AtomicReference<Surface>();
        inputSurfaceReference.set(this.mVideoEncoder.mediaCodec.createInputSurface());
        this.mInputSurface = new InputSurface((Surface)inputSurfaceReference.get());
        try {
            this.mVideoEncoder.start();
            this.mInputSurface.makeCurrent();
            this.mOutputSurface = new OutputSurface();
            ret = this.mVideoDecoder.start(this.inputFormat, this.mOutputSurface.getSurface());
            this.mInputSurface.releaseEGLContext();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return ret;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void cancel() {
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.cancel();
        }
        if (this.mVideoDecoder != null) {
            this.mVideoDecoder.cancel();
        }
        MP4Converter mP4Converter = this;
        synchronized (mP4Converter) {
            this.isCancel = true;
        }
    }

    private void onError(String error) {
        LogUtil.d(TAG, "onError : " + error);
        if (this.convertListener != null) {
            this.convertListener.onError(100, error);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void finishTask() {
        Object object = this.eosLock;
        synchronized (object) {
            if (!this.eosFinish) {
                this.eosFinish = true;
                this.eosLock.notify();
            }
        }
        object = this.finishLock;
        synchronized (object) {
            if (!this.isFinish) {
                this.isFinish = true;
                this.finishLock.notify();
                if (this.inputEndFlag == 2) {
                    this.convertListener.onDataOutput(null, 0, 0, 0L);
                }
            }
        }
    }

    private class CodecCallbackImpl
    implements ConvertCallback {
        private CodecType codeType;

        CodecCallbackImpl(CodecType codeType) {
            this.codeType = codeType;
        }

        @Override
        public void onError(String error) {
            MP4Converter.this.onError(error);
            MP4Converter.this.cancel();
        }

        @Override
        public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onInputBufferAvailable(MediaCodec codec, int index) {
            ByteBuffer decoderInputBuffer;
            if (this.codeType == CodecType.DECODE && (decoderInputBuffer = codec.getInputBuffer(index)) != null) {
                Object object = MP4Converter.this.objLock;
                synchronized (object) {
                    while (!MP4Converter.this.isCancel) {
                        if (MP4Converter.this.mDataBuffer != null) {
                            LogUtil.d(TAG, "onInputBufferAvailable len = " + ((MP4Converter)MP4Converter.this).mDataBuffer.len);
                            if (((MP4Converter)MP4Converter.this).mDataBuffer.len > 0) {
                                decoderInputBuffer.put(((MP4Converter)MP4Converter.this).mDataBuffer.data, 0, ((MP4Converter)MP4Converter.this).mDataBuffer.len);
                                int flags = ((MP4Converter)MP4Converter.this).mDataBuffer.frameType == 1L ? 1 : 0;
                                codec.queueInputBuffer(index, 0, ((MP4Converter)MP4Converter.this).mDataBuffer.len, ((MP4Converter)MP4Converter.this).mDataBuffer.timeStamp * 1000L, flags);
                            } else {
                                codec.queueInputBuffer(index, 0, 0, 0L, 4);
                                MP4Converter.this.isCancel = true;
                            }
                            MP4Converter.this.mDataBuffer = null;
                            MP4Converter.this.objLock.notifyAll();
                            break;
                        }
                        try {
                            MP4Converter.this.objLock.wait();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
            if (this.codeType == CodecType.ENCODE) {
                if (MP4Converter.this.convertListener != null) {
                    this.writeData(codec, index, info);
                } else {
                    codec.releaseOutputBuffer(index, false);
                }
            } else {
                if ((info.flags & 2) != 0) {
                    LogUtil.d(TAG, "codec config buffer codeType = " + (Object)((Object)this.codeType));
                    codec.releaseOutputBuffer(index, false);
                    return;
                }
                this.tryEncodeVideo(codec, index, info);
            }
        }

        private void writeData(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
            if ((info.flags & 4) != 0) {
                LogUtil.d(TAG, "transCode: EOS");
                codec.releaseOutputBuffer(index, false);
                MP4Converter.this.finishTask();
            } else {
                ByteBuffer encoderOutputBuffer;
                if (info.size != 0 && (encoderOutputBuffer = codec.getOutputBuffer(index)) != null) {
                    byte[] bytes = new byte[info.size];
                    encoderOutputBuffer.get(bytes, 0, info.size);
                    int type = (info.flags & 1) == 1 ? 1 : 2;
                    LogUtil.d(TAG, "onOutputBufferAvailable size = " + info.size + " offset = " + info.offset + " type = " + type);
                    if ((info.flags & 2) != 0) {
                        LogUtil.d(TAG, "codec config buffer codeType = " + (Object)((Object)this.codeType));
                        MP4Converter.this.mSPSDataBuffer = new DataBuffer(bytes, info.size, 0L, 0L);
                    } else if (MP4Converter.this.mSPSDataBuffer != null) {
                        byte[] bytesFirst = new byte[((MP4Converter)MP4Converter.this).mSPSDataBuffer.len + info.size];
                        if (((MP4Converter)MP4Converter.this).mSPSDataBuffer.len >= 0) {
                            System.arraycopy(((MP4Converter)MP4Converter.this).mSPSDataBuffer.data, 0, bytesFirst, 0, ((MP4Converter)MP4Converter.this).mSPSDataBuffer.len);
                        }
                        if (info.size >= 0) {
                            System.arraycopy(bytes, 0, bytesFirst, ((MP4Converter)MP4Converter.this).mSPSDataBuffer.len, info.size);
                        }
                        MP4Converter.this.convertListener.onDataOutput(bytesFirst, info.size + ((MP4Converter)MP4Converter.this).mSPSDataBuffer.len, type, info.presentationTimeUs / 1000L);
                        MP4Converter.this.mSPSDataBuffer = null;
                    } else {
                        MP4Converter.this.convertListener.onDataOutput(bytes, info.size, type, info.presentationTimeUs / 1000L);
                    }
                }
                codec.releaseOutputBuffer(index, false);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void tryEncodeVideo(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
            block9: {
                try {
                    boolean render = info.size != 0;
                    codec.releaseOutputBuffer(index, render);
                    if (render) {
                        MP4Converter.this.mInputSurface.makeCurrent();
                        LogUtil.d(TAG, "output surface: await new image");
                        MP4Converter.this.mOutputSurface.awaitNewImage();
                        LogUtil.d(TAG, "output surface: draw image");
                        MP4Converter.this.mOutputSurface.drawImage();
                        MP4Converter.this.mInputSurface.setPresentationTime(info.presentationTimeUs * 1000L);
                        LogUtil.d(TAG, "input surface: swap buffers info.presentationTimeUs = " + info.presentationTimeUs);
                        MP4Converter.this.mInputSurface.swapBuffers();
                        LogUtil.d(TAG, "video encoder: notified of new frame");
                        MP4Converter.this.mInputSurface.releaseEGLContext();
                    }
                    if ((info.flags & 4) == 0) break block9;
                    LogUtil.d(TAG, "video decoder: EOS");
                    MP4Converter.this.mVideoEncoder.signalEndOfInputStream();
                    Object object = MP4Converter.this.eosLock;
                    synchronized (object) {
                        if (!MP4Converter.this.eosFinish) {
                            try {
                                MP4Converter.this.eosLock.wait(5000L);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    MP4Converter.this.finishTask();
                }
                catch (Exception e) {
                    MP4Converter.this.onError(e.getMessage());
                }
            }
        }
    }

    private static enum CodecType {
        DECODE,
        ENCODE;

    }
}

