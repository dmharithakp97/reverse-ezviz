/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.openapi.bean;

import com.videogo.openapi.annotation.Serializable;

public class EZUserInfo {
    @Serializable(name="userName")
    private String username;
    @Serializable(name="nickname")
    private String nickname;
    @Serializable(name="avatarUrl")
    private String avatarUrl;
    @Serializable(name="areaDomain")
    private String areaDomain;

    public String getUsername() {
        return this.username;
    }

    private void setUsername(String userName) {
        this.username = userName;
    }

    public String getNickname() {
        return this.nickname;
    }

    private void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    private void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    private void setAreaDomain(String areaDomain) {
        this.areaDomain = areaDomain;
    }

    public String getAreaDomain() {
        return this.areaDomain;
    }
}

