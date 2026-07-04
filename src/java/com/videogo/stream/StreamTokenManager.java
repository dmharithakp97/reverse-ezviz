/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.ez.stream.EZStreamClientManager
 */
package com.videogo.stream;

import android.content.Context;
import com.ez.stream.EZStreamClientManager;
import com.videogo.device.DeviceManager;
import com.videogo.exception.BaseException;
import com.videogo.openapi.PlayAPI;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class StreamTokenManager {
    private static final String TAG = StreamTokenManager.class.getSimpleName();
    private static StreamTokenManager mInstance = new StreamTokenManager();
    private List<String> mTokenContainer = new ArrayList<String>();
    private boolean isTryGetitng = false;

    private StreamTokenManager() {
    }

    public static StreamTokenManager getInstance() {
        return mInstance;
    }

    public void refreshTokenInEZStreamClient() throws BaseException {
        if (EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).getLeftTokenCount() < 10) {
            LogUtil.i(TAG, "no left token, need to set tokens");
            List<String> tokenList = DeviceManager.getInstance().getStreamToken(true);
            if (tokenList != null && tokenList.size() > 0) {
                String[] tokenArrays = new String[tokenList.size()];
                for (int index = 0; index < tokenList.size(); ++index) {
                    tokenArrays[index] = tokenList.get(index);
                }
                EZStreamClientManager.create((Context)PlayAPI.mApplication.getApplicationContext()).setTokens(tokenArrays);
            }
        }
    }

    public String getToken() throws Exception {
        if (this.mTokenContainer.size() == 0) {
            if (!this.tryGettingMoreToken()) {
                throw new Exception("fail to get token from server!");
            }
        } else if (this.mTokenContainer.size() < 10 && !this.isTryGetitng) {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    StreamTokenManager.this.isTryGetitng = true;
                    StreamTokenManager.this.tryGettingMoreToken();
                    StreamTokenManager.this.isTryGetitng = false;
                }
            });
        }
        return this.getLastTokenFromContainer();
    }

    private String getLastTokenFromContainer() {
        return this.mTokenContainer.get(this.mTokenContainer.size() - 1);
    }

    private boolean tryGettingMoreToken() {
        boolean isSuc = false;
        try {
            List<String> tokenList = DeviceManager.getInstance().getStreamToken(true);
            if (tokenList != null && tokenList.size() > 0) {
                isSuc = true;
            }
            if (isSuc) {
                this.mTokenContainer.addAll(tokenList);
            }
        }
        catch (BaseException e) {
            e.printStackTrace();
        }
        return isSuc;
    }
}

