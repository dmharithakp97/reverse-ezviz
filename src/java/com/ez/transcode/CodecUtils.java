/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodecList
 *  android.media.MediaExtractor
 *  android.media.MediaFormat
 */
package com.ez.transcode;

import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;

public class CodecUtils {
    public static String findEncodeFormat(String mimeType) {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", mimeType);
        MediaCodecList mediaCodecList = new MediaCodecList(0);
        return mediaCodecList.findEncoderForFormat(mediaFormat);
    }

    public static int getAndSelectVideoTrackIndex(MediaExtractor extractor) {
        for (int index = 0; index < extractor.getTrackCount(); ++index) {
            if (!CodecUtils.isVideoFormat(extractor.getTrackFormat(index))) continue;
            extractor.selectTrack(index);
            return index;
        }
        return -1;
    }

    public static int getAndSelectAudioTrackIndex(MediaExtractor extractor) {
        for (int index = 0; index < extractor.getTrackCount(); ++index) {
            if (!CodecUtils.isAudioFormat(extractor.getTrackFormat(index))) continue;
            extractor.selectTrack(index);
            return index;
        }
        return -1;
    }

    private static boolean isVideoFormat(MediaFormat format) {
        return CodecUtils.getMimeTypeFor(format).startsWith("video/");
    }

    private static boolean isAudioFormat(MediaFormat format) {
        return CodecUtils.getMimeTypeFor(format).startsWith("audio/");
    }

    public static String getMimeTypeFor(MediaFormat format) {
        return format.getString("mime");
    }
}

