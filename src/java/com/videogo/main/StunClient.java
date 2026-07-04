/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.ptr.IntByReference
 *  com.sun.jna.ptr.PointerByReference
 */
package com.videogo.main;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface StunClient
extends Library {
    public static final int FULL_CONE_NAT = 1;
    public static final int RESTRICTED_CONE_NAT = 2;
    public static final int RESTRICTED_PORT_NAT = 3;
    public static final int SYMMETRIC_NAT = 4;
    public static final int OPEN_INTERNET = 5;
    public static final int SYMMETRIC_FIREWALL = 6;
    public static final int UDP_BLOCKED = 7;
    public static final int UNKNOWN_NAT = 8;
    public static final StunClient INSTANCE = (StunClient)Native.load((String)"stunClient", StunClient.class);

    public boolean Stun_Init();

    public boolean Stun_Finit();

    public int Stun_GetNATType(String var1, String var2, short var3, String var4, short var5, IntByReference var6);

    public int Stun_GetNATType_New(String var1, String var2, short var3, String var4, short var5, IntByReference var6);

    public boolean Stun_GetNATIP(PointerByReference var1);
}

