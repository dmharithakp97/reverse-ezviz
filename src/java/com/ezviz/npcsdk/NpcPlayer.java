/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.media.AudioManager
 *  android.os.Handler
 *  android.os.Message
 *  android.util.Log
 *  android.view.SurfaceHolder
 *  com.mediaplayer.audio.AudioCodecParam
 *  com.mediaplayer.audio.AudioEngine
 *  org.MediaPlayer.PlayM4.Player
 *  org.MediaPlayer.PlayM4.PlayerCallBack$PlayerDisplayCB
 */
package com.ezviz.npcsdk;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import com.ezviz.npcsdk.NativeApi;
import com.ezviz.npcsdk.OnNpcListener;
import com.mediaplayer.audio.AudioCodecParam;
import com.mediaplayer.audio.AudioEngine;
import com.videogo.constant.Config;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.ezdclog.EZDcLogManager;
import com.videogo.ezdclog.params.EZLogStreamClientParams;
import com.videogo.openapi.PlayAPI;
import com.videogo.util.LogUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.PlayerCallBack;

public class NpcPlayer
implements OnNpcListener {
    Player mPlayer = Player.getInstance();
    int mPlayPort = -1;
    Object mDisplay = null;
    Object mDisplay2 = null;
    int mVideoWidth = 0;
    int mVideoHeight = 0;
    long mNPC = 0L;
    private Handler mHandler;
    private String mPlayerUrl;
    AudioCodecParam mAudioCodecParam = null;
    AudioEngine mAudioEngine = null;
    AudioManager mAudioManager = null;
    private boolean isAudioOnly;
    private volatile boolean isTimeOut;
    private volatile boolean retry;
    public EZLogStreamClientParams mEZLogStreamClientParams;
    PlayerCallBack.PlayerDisplayCB mPlayerDisplayCB = new PlayerCallBack.PlayerDisplayCB(){

        public void onDisplay(int nPort, byte[] data, int nDataLen, int nWidth, int nHeight, int nFrameTime, int nDataType, int Reserved) {
            if ((NpcPlayer.this.mVideoWidth != nWidth || NpcPlayer.this.mVideoHeight != nHeight) && nWidth > 0 && nHeight > 0) {
                boolean needResetDisplay = NpcPlayer.this.mVideoWidth != 0;
                NpcPlayer.this.mVideoWidth = nWidth;
                NpcPlayer.this.mVideoHeight = nHeight;
                NpcPlayer.this.sendMessage(102, 0, null);
                NpcPlayer.this.sendMessage(134, 0, new StringBuffer().append(NpcPlayer.this.mVideoWidth).append(":").append(NpcPlayer.this.mVideoHeight).toString());
                if (NpcPlayer.this.mEZLogStreamClientParams != null) {
                    NpcPlayer.this.getEZLogStreamClientParams().cost = (int)(System.currentTimeMillis() - NpcPlayer.this.getEZLogStreamClientParams().timebyLong);
                    NpcPlayer.this.getEZLogStreamClientParams().via = 911;
                    NpcPlayer.this.getEZLogStreamClientParams().errCd = 0;
                }
            }
        }
    };
    Runnable runnable = new Runnable(){

        @Override
        public void run() {
            NpcPlayer.this.isTimeOut = true;
            NpcPlayer.this.retry = false;
        }
    };
    Runnable startrunnable = new Runnable(){

        @Override
        public void run() {
            NpcPlayer.this.reStart();
        }
    };

    public EZLogStreamClientParams getEZLogStreamClientParams() {
        return this.mEZLogStreamClientParams;
    }

    public void initEZLogStreamClientParams() {
        this.mEZLogStreamClientParams = new EZLogStreamClientParams();
        this.mEZLogStreamClientParams.plTp = 1;
        this.mEZLogStreamClientParams.errCd = -2;
        this.mEZLogStreamClientParams.opId = UUID.randomUUID().toString();
    }

    public void setEZLogStreamClientParams(EZLogStreamClientParams mEZLogStreamClientParams) {
        this.mEZLogStreamClientParams = mEZLogStreamClientParams;
    }

    public void setAudioOnly(boolean audioOnly) {
        this.isAudioOnly = audioOnly;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private NpcPlayer() {
        this.mPlayPort = this.mPlayer.getPort();
        this.mPlayer.setStreamOpenMode(this.mPlayPort, 0);
        this.mPlayer.setDisplayCB(this.mPlayPort, this.mPlayerDisplayCB);
        this.mAudioCodecParam = new AudioCodecParam();
        this.mAudioCodecParam.nCodecType = 2;
        this.mAudioCodecParam.nBitWidth = 2;
        this.mAudioCodecParam.nSampleRate = 8000;
        this.mAudioCodecParam.nChannel = 1;
        this.mAudioCodecParam.nBitRate = 16000;
        this.mAudioCodecParam.nVolume = 100;
        this.mAudioEngine = new AudioEngine(1);
    }

    private NpcPlayer(String url) {
        this();
        this.mPlayerUrl = url;
    }

    public static NpcPlayer create(String url) {
        NpcPlayer player = null;
        if (url == null) {
            return player;
        }
        player = new NpcPlayer(url);
        return player;
    }

    private void reStart() {
        if (this.mNPC >= 0L) {
            NativeApi.destroyNPC(this.mNPC);
            this.mNPC = 0L;
        }
        this.mNPC = NativeApi.createNPC(this.mPlayerUrl, this.mPlayPort, this.isAudioOnly, this);
        LogUtil.d("NPC reStart", "reStart ret = " + this.mNPC);
    }

    public void start() {
        if (this.mEZLogStreamClientParams != null) {
            this.getEZLogStreamClientParams().timebyLong = System.currentTimeMillis();
        }
        this.retry = true;
        this.mNPC = NativeApi.createNPC(this.mPlayerUrl, this.mPlayPort, this.isAudioOnly, this);
        LogUtil.d("NPC Start", "Start ret = " + this.mNPC);
        this.isTimeOut = false;
        if (this.mHandler != null) {
            this.mHandler.postDelayed(this.runnable, 5000L);
        }
    }

    public void stop() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.runnable);
        }
        this.release();
        if (this.mEZLogStreamClientParams != null) {
            EZDcLogManager.getInstance().submit(this.getEZLogStreamClientParams());
            this.setEZLogStreamClientParams(null);
        }
    }

    public void setDisplay(Object display) {
        this.setDisplay(display, 0);
    }

    public void setDisplay(Object display, int streamId) {
        if (streamId == 0) {
            this.mDisplay = display;
            if (this.mPlayPort != -1 && this.mPlayer != null) {
                if (this.mDisplay instanceof SurfaceTexture) {
                    this.mPlayer.setVideoWindowEx(this.mPlayPort, 0, (SurfaceTexture)display);
                } else {
                    this.mPlayer.setVideoWindow(this.mPlayPort, 0, (SurfaceHolder)display);
                }
            }
        } else if (streamId == 1) {
            this.mDisplay2 = display;
            if (this.mPlayPort != -1 && this.mPlayer != null) {
                if (this.mDisplay2 instanceof SurfaceTexture) {
                    this.mPlayer.setVideoWindowExMultiTrack(this.mPlayPort, 0, (SurfaceTexture)display, streamId);
                } else {
                    this.mPlayer.setVideoWindowMultiTrack(this.mPlayPort, 0, (SurfaceHolder)display, streamId);
                }
            }
        }
    }

    private void play() {
        if (this.mPlayPort != -1 && this.mPlayer != null) {
            if (this.mDisplay instanceof SurfaceTexture) {
                this.mPlayer.playEx(this.mPlayPort, (SurfaceTexture)this.mDisplay);
            } else {
                this.mPlayer.play(this.mPlayPort, (SurfaceHolder)this.mDisplay);
            }
        }
        this.mPlayer.playSound(this.mPlayPort);
    }

    @Override
    public void onDataBack(int type, byte[] audioData, int len) {
        this.isTimeOut = false;
        this.retry = false;
        if (type == 1) {
            this.play();
            return;
        }
        if (type == 4) {
            this.mAudioEngine.inputData(audioData, len);
        }
    }

    @Override
    public void onAudioParamsDataBack(int codeType, int nBitWidth, int nSampleRate, int nChannel, int nBitRate, int nVolume) {
        Log.d((String)"onAudioParamsDataBack", (String)("  codeType = " + codeType + "  nBitWidth = " + nBitWidth + "  nSampleRate = " + nSampleRate + "  nChannel = " + nChannel + "  nBitRate = " + nBitRate + "  nVolume = " + nVolume));
        this.mAudioCodecParam.nCodecType = 6;
        this.mAudioCodecParam.nBitWidth = 2;
        this.mAudioCodecParam.nSampleRate = 16000;
        this.mAudioCodecParam.nChannel = 1;
        this.mAudioCodecParam.nSampleRate = nSampleRate;
        int ret = -1;
        ret = this.mAudioEngine.open();
        ret = this.mAudioEngine.setAudioParam(this.mAudioCodecParam, 2);
        if (ret == 0) {
            ret = this.mAudioEngine.startPlay();
        }
        if (ret != 0) {
            // empty if block
        }
        this.sendMessage(102, 0, null);
    }

    @Override
    public void onError(int errorCode) {
        Log.d((String)" NPC onError", (String)("  errorCode = " + errorCode));
        if (!this.isTimeOut && this.retry) {
            if (this.mHandler != null) {
                this.mHandler.postDelayed(this.startrunnable, 1000L);
            }
        } else {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(50, errorCode);
            this.sendMessage(103, errorCode, errorInfo);
            if (this.mEZLogStreamClientParams != null) {
                this.getEZLogStreamClientParams().errCd = errorInfo.errorCode;
                this.getEZLogStreamClientParams().cost = (int)(System.currentTimeMillis() - this.getEZLogStreamClientParams().timebyLong);
            }
            this.stop();
        }
    }

    protected void sendMessage(int msg, int arg1, Object obj) {
        if (this.mHandler != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.arg1 = arg1;
            message.obj = obj;
            this.mHandler.sendMessage(message);
        }
    }

    private void release() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.runnable);
        }
        this.freePlayer();
        if (this.mNPC >= 0L) {
            NativeApi.destroyNPC(this.mNPC);
            this.mNPC = 0L;
        }
    }

    void freePlayer() {
        if (this.mPlayPort != -1 && this.mPlayer != null) {
            this.mPlayer.stop(this.mPlayPort);
            this.mPlayer.setHardDecode(this.mPlayPort, 0);
            this.mPlayer.closeStream(this.mPlayPort);
            this.mPlayer.freePort(this.mPlayPort);
        }
        this.mPlayPort = -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void onDataCallBack(int datatype, byte[] data, int len) {
        if (data == null && data.length <= 0) {
            return;
        }
        if (Config.STREAMDEBUGGING) {
            File file = new File(PlayAPI.mApplication.getExternalFilesDir(null), "stream");
            File parent = file.getParentFile();
            if (parent == null || !parent.exists() || parent.isFile()) {
                parent.mkdirs();
            }
            if (!file.exists() || !file.isFile()) {
                try {
                    file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file, true);
                if (fileOutputStream != null) {
                    byte[] src = new byte[]{(byte)(len >> 24 & 0xFF), (byte)(len >> 16 & 0xFF), (byte)(len >> 8 & 0xFF), (byte)(len & 0xFF)};
                    fileOutputStream.write(src);
                    fileOutputStream.write(data);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

