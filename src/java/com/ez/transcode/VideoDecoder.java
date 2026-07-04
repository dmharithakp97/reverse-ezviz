/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaCodec$Callback
 *  android.media.MediaCodec$CodecException
 *  android.media.MediaFormat
 *  android.util.Log
 *  android.view.Surface
 */
package com.ez.transcode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.ez.stream.HandleSingleExecutorPool;
import com.ez.transcode.CallbackHandler;
import com.ez.transcode.ConvertParam;
import com.ez.transcode.MediaContainer;

class VideoDecoder
extends MediaContainer {
    private static final String TAG = "VideoDecoder";
    private HandleSingleExecutorPool mVideoDecoderHandlerThread;
    private CallbackHandler mVideoDecoderHandler;

    VideoDecoder(ConvertParam convertParam, String mimeType) {
        this.mConvertParam = convertParam;
        this.mediaCodec = this.createVideoDecoder(mimeType);
    }

    boolean start(MediaFormat inputFormat, Surface surface) {
        this.mediaCodec.configure(inputFormat, surface, null, 0);
        this.mediaCodec.start();
        return true;
    }

    @Override
    void release() {
        super.release();
        if (this.mVideoDecoderHandlerThread != null) {
            this.mVideoDecoderHandlerThread.quitSafely();
            this.mVideoDecoderHandlerThread = null;
            this.mVideoDecoderHandler = null;
        }
    }

    private MediaCodec createVideoDecoder(String mime) {
        this.mVideoDecoderHandlerThread = new HandleSingleExecutorPool("DecoderThread");
        this.mVideoDecoderHandlerThread.start();
        this.mVideoDecoderHandler = new CallbackHandler(this.mVideoDecoderHandlerThread.getLooper());
        MediaCodec.Callback callback = new MediaCodec.Callback(){

            public void onError(MediaCodec codec, MediaCodec.CodecException exception) {
                if (VideoDecoder.this.convertCallback != null) {
                    VideoDecoder.this.convertCallback.onError(exception.getMessage());
                }
            }

            public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
                MediaFormat decoderOutputVideoFormat = codec.getOutputFormat();
                Log.d((String)VideoDecoder.TAG, (String)("video decoder: onOutputFormatChanged(): " + decoderOutputVideoFormat));
            }

            public void onInputBufferAvailable(MediaCodec codec, int index) {
                if (VideoDecoder.this.isCancel) {
                    return;
                }
                if (VideoDecoder.this.convertCallback != null) {
                    try {
                        VideoDecoder.this.convertCallback.onInputBufferAvailable(codec, index);
                    }
                    catch (Exception e) {
                        VideoDecoder.this.convertCallback.onError(e.getMessage());
                    }
                }
            }

            public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
                if (VideoDecoder.this.isCancel) {
                    return;
                }
                if (VideoDecoder.this.convertCallback != null) {
                    VideoDecoder.this.convertCallback.onOutputBufferAvailable(codec, index, info);
                }
            }
        };
        this.mVideoDecoderHandler.create(false, mime, callback);
        return this.mVideoDecoderHandler.getCodec();
    }
}

