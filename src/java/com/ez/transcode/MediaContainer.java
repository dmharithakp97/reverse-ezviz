/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 */
package com.ez.transcode;

import android.media.MediaCodec;
import com.ez.transcode.ConvertCallback;
import com.ez.transcode.ConvertParam;

class MediaContainer {
    ConvertParam mConvertParam;
    boolean isCancel;
    MediaCodec mediaCodec;
    ConvertCallback convertCallback;

    MediaContainer() {
    }

    void cancel() {
        this.isCancel = true;
    }

    void setConvertCallback(ConvertCallback convertCallback) {
        this.convertCallback = convertCallback;
    }

    MediaCodec getMediaCodec() {
        return this.mediaCodec;
    }

    void release() {
        if (this.mediaCodec != null) {
            try {
                this.mediaCodec.stop();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    this.mediaCodec.release();
                }
                catch (Exception exception) {}
            }
        }
    }
}

