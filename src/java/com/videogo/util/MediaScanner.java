/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.MediaScannerConnection
 *  android.media.MediaScannerConnection$MediaScannerConnectionClient
 *  android.net.Uri
 */
package com.videogo.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

public class MediaScanner {
    private MediaScannerConnection mediaScanConn = null;
    private MusicSannerClient client = new MusicSannerClient();
    private String filePath = null;
    private String fileType = null;
    private String[] filePaths = null;

    public MediaScanner(Context context) {
        this.mediaScanConn = new MediaScannerConnection(context, (MediaScannerConnection.MediaScannerConnectionClient)this.client);
    }

    public void scanFile(String filepath, String fileType) {
        this.filePath = filepath;
        this.fileType = fileType;
        this.mediaScanConn.connect();
    }

    public void scanFile(String[] filePaths, String fileType) {
        this.filePaths = filePaths;
        this.fileType = fileType;
        this.mediaScanConn.connect();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    static /* synthetic */ String[] access$302(MediaScanner x0, String[] x1) {
        x0.filePaths = x1;
        return x1;
    }

    class MusicSannerClient
    implements MediaScannerConnection.MediaScannerConnectionClient {
        MusicSannerClient() {
        }

        public void onMediaScannerConnected() {
            if (MediaScanner.this.filePath != null) {
                MediaScanner.this.mediaScanConn.scanFile(MediaScanner.this.filePath, MediaScanner.this.fileType);
            }
            if (MediaScanner.this.filePaths != null) {
                for (String file : MediaScanner.this.filePaths) {
                    MediaScanner.this.mediaScanConn.scanFile(file, MediaScanner.this.fileType);
                }
            }
            MediaScanner.this.filePath = null;
            MediaScanner.this.fileType = null;
            MediaScanner.access$302(MediaScanner.this, null);
        }

        public void onScanCompleted(String path, Uri uri) {
            MediaScanner.this.mediaScanConn.disconnect();
        }
    }
}

