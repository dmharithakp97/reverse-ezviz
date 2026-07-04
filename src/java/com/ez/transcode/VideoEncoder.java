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
 */
package com.ez.transcode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import com.ez.stream.LogUtil;
import com.ez.transcode.CodecUtils;
import com.ez.transcode.ConvertParam;
import com.ez.transcode.MediaContainer;
import java.io.IOException;

public class VideoEncoder
extends MediaContainer {
    private static final String TAG = "VideoEncoder";
    private MediaFormat mEncoderOutputVideoFormat;
    private int mWidth;
    private int mHeight;
    private int mBitrate;
    private static int mFrameRate = 25;
    private static int miFrameInterval = 25;

    VideoEncoder(ConvertParam convertParam, String mimeType) throws Exception {
        this.mConvertParam = convertParam;
        this.initParam();
        String videoCodecInfoName = CodecUtils.findEncodeFormat(mimeType);
        if (videoCodecInfoName == null) {
            throw new Exception("can not find appropriate encode mediaCodec for " + mimeType);
        }
        this.mediaCodec = this.createVideoEncoder(videoCodecInfoName);
    }

    private void initParam() {
        this.setSize(this.mConvertParam.dstWidth, this.mConvertParam.dstHeight);
    }

    boolean config(String mimeType) {
        MediaFormat outputVideoFormat = this.createOutputFormat(mimeType);
        try {
            this.mediaCodec.configure(outputVideoFormat, null, null, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void start() {
        this.mediaCodec.start();
    }

    @Override
    public void cancel() {
        if (!this.isCancel) {
            this.isCancel = true;
        }
    }

    void signalEndOfInputStream() {
        if (this.mediaCodec != null) {
            this.mediaCodec.signalEndOfInputStream();
        }
    }

    private MediaFormat createOutputFormat(String mimeType) {
        MediaFormat outputVideoFormat = MediaFormat.createVideoFormat((String)mimeType, (int)this.mConvertParam.dstWidth, (int)this.mConvertParam.dstHeight);
        outputVideoFormat.setInteger("color-format", 2130708361);
        outputVideoFormat.setInteger("bitrate", this.mConvertParam.bitrate * 1024);
        int frameRate = Float.valueOf(this.mConvertParam.frameRate).intValue();
        outputVideoFormat.setInteger("frame-rate", frameRate == 0 ? 10 : frameRate);
        outputVideoFormat.setInteger("i-frame-interval", this.mConvertParam.gopLen);
        Log.d((String)TAG, (String)("video format: " + outputVideoFormat));
        return outputVideoFormat;
    }

    private MediaCodec createVideoEncoder(String codecInfoName) throws IOException {
        MediaCodec encoder = MediaCodec.createByCodecName((String)codecInfoName);
        encoder.setCallback(new MediaCodec.Callback(){

            public void onError(MediaCodec codec, MediaCodec.CodecException exception) {
                if (VideoEncoder.this.convertCallback != null) {
                    VideoEncoder.this.convertCallback.onError("video encoder error() ");
                }
            }

            public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
                LogUtil.d(VideoEncoder.TAG, "video encoder: onOutputFormatChanged()");
                if (VideoEncoder.this.mEncoderOutputVideoFormat != null) {
                    LogUtil.e(VideoEncoder.TAG, "video encoder changed its output format again?");
                }
                VideoEncoder.this.mEncoderOutputVideoFormat = codec.getOutputFormat();
                if (VideoEncoder.this.convertCallback != null) {
                    VideoEncoder.this.convertCallback.onOutputFormatChanged(codec, format);
                }
            }

            public void onInputBufferAvailable(MediaCodec codec, int index) {
            }

            public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
                if (VideoEncoder.this.isCancel) {
                    return;
                }
                if (VideoEncoder.this.convertCallback != null) {
                    VideoEncoder.this.convertCallback.onOutputBufferAvailable(codec, index, info);
                }
            }
        });
        return encoder;
    }

    private void setSize(int originalWidth, int originalHeight) {
        this.mWidth = originalWidth;
        this.mHeight = originalHeight;
        this.mBitrate = this.mWidth / 2 * (this.mHeight / 2) * 10;
    }
}

