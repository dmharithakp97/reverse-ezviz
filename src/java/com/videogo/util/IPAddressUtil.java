/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import java.net.InetAddress;

public class IPAddressUtil {
    private static final int INADDR4SZ = 4;
    private static final int INADDR16SZ = 16;
    private static final int INT16SZ = 2;

    public static boolean isReservedAddr(InetAddress inetAddr) {
        return inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress();
    }

    public static byte[] textToNumericFormatV4(String src) {
        if (src.length() == 0) {
            return null;
        }
        byte[] res = new byte[4];
        String[] s = src.split("\\.", -1);
        try {
            switch (s.length) {
                case 1: {
                    long val = Long.parseLong(s[0]);
                    if (val < 0L || val > 0xFFFFFFFFL) {
                        return null;
                    }
                    res[0] = (byte)(val >> 24 & 0xFFL);
                    res[1] = (byte)((val & 0xFFFFFFL) >> 16 & 0xFFL);
                    res[2] = (byte)((val & 0xFFFFL) >> 8 & 0xFFL);
                    res[3] = (byte)(val & 0xFFL);
                    break;
                }
                case 2: {
                    long val = Integer.parseInt(s[0]);
                    if (val >= 0L && val <= 255L) {
                        res[0] = (byte)(val & 0xFFL);
                        val = Integer.parseInt(s[1]);
                        if (val < 0L || val > 0xFFFFFFL) {
                            return null;
                        }
                        res[1] = (byte)(val >> 16 & 0xFFL);
                        res[2] = (byte)((val & 0xFFFFL) >> 8 & 0xFFL);
                        res[3] = (byte)(val & 0xFFL);
                        break;
                    }
                    return null;
                }
                case 3: {
                    long val;
                    for (int i = 0; i < 2; ++i) {
                        val = Integer.parseInt(s[i]);
                        if (val < 0L || val > 255L) {
                            return null;
                        }
                        res[i] = (byte)(val & 0xFFL);
                    }
                    val = Integer.parseInt(s[2]);
                    if (val < 0L || val > 65535L) {
                        return null;
                    }
                    res[2] = (byte)(val >> 8 & 0xFFL);
                    res[3] = (byte)(val & 0xFFL);
                    break;
                }
                case 4: {
                    for (int i = 0; i < 4; ++i) {
                        long val = Integer.parseInt(s[i]);
                        if (val < 0L || val > 255L) {
                            return null;
                        }
                        res[i] = (byte)(val & 0xFFL);
                    }
                    return res;
                }
                default: {
                    return null;
                }
            }
            return res;
        }
        catch (NumberFormatException var6) {
            return null;
        }
    }

    public static byte[] textToNumericFormatV6(String src) {
        int n;
        if (src.length() < 2) {
            return null;
        }
        char[] srcb = src.toCharArray();
        byte[] dst = new byte[16];
        int srcb_length = srcb.length;
        int pc = src.indexOf("%");
        if (pc == srcb_length - 1) {
            return null;
        }
        if (pc != -1) {
            srcb_length = pc;
        }
        int colonp = -1;
        int i = 0;
        int j = 0;
        if (srcb[i] == ':' && srcb[++i] != ':') {
            return null;
        }
        int curtok = i;
        boolean saw_xdigit = false;
        int val = 0;
        while (i < srcb_length) {
            char ch;
            if ((n = Character.digit(ch = srcb[i++], 16)) != -1) {
                val <<= 4;
                if ((val |= n) > 65535) {
                    return null;
                }
                saw_xdigit = true;
                continue;
            }
            if (ch != ':') {
                if (ch == '.' && j + 4 <= 16) {
                    String ia4 = src.substring(curtok, srcb_length);
                    int dot_count = 0;
                    int index = 0;
                    while ((index = ia4.indexOf(46, index)) != -1) {
                        ++dot_count;
                        ++index;
                    }
                    if (dot_count != 3) {
                        return null;
                    }
                    byte[] v4addr = IPAddressUtil.textToNumericFormatV4(ia4);
                    if (v4addr == null) {
                        return null;
                    }
                    for (int k = 0; k < 4; ++k) {
                        dst[j++] = v4addr[k];
                    }
                    saw_xdigit = false;
                    break;
                }
                return null;
            }
            curtok = i;
            if (!saw_xdigit) {
                if (colonp != -1) {
                    return null;
                }
                colonp = j;
                continue;
            }
            if (i == srcb_length) {
                return null;
            }
            if (j + 2 > 16) {
                return null;
            }
            dst[j++] = (byte)(val >> 8 & 0xFF);
            dst[j++] = (byte)(val & 0xFF);
            saw_xdigit = false;
            val = 0;
        }
        if (saw_xdigit) {
            if (j + 2 > 16) {
                return null;
            }
            dst[j++] = (byte)(val >> 8 & 0xFF);
            dst[j++] = (byte)(val & 0xFF);
        }
        if (colonp != -1) {
            n = j - colonp;
            if (j == 16) {
                return null;
            }
            for (i = 1; i <= n; ++i) {
                dst[16 - i] = dst[colonp + n - i];
                dst[colonp + n - i] = 0;
            }
            j = 16;
        }
        if (j != 16) {
            return null;
        }
        byte[] newdst = IPAddressUtil.convertFromIPv4MappedAddress(dst);
        if (newdst != null) {
            return newdst;
        }
        return dst;
    }

    public static boolean isIPv4LiteralAddress(String src) {
        return IPAddressUtil.textToNumericFormatV4(src) != null;
    }

    public static boolean isIPv6LiteralAddress(String src) {
        return IPAddressUtil.textToNumericFormatV6(src) != null;
    }

    public static byte[] convertFromIPv4MappedAddress(byte[] addr) {
        if (IPAddressUtil.isIPv4MappedAddress(addr)) {
            byte[] newAddr = new byte[4];
            System.arraycopy(addr, 12, newAddr, 0, 4);
            return newAddr;
        }
        return null;
    }

    private static boolean isIPv4MappedAddress(byte[] addr) {
        if (addr.length < 16) {
            return false;
        }
        return addr[0] == 0 && addr[1] == 0 && addr[2] == 0 && addr[3] == 0 && addr[4] == 0 && addr[5] == 0 && addr[6] == 0 && addr[7] == 0 && addr[8] == 0 && addr[9] == 0 && addr[10] == -1 && addr[11] == -1;
    }

    public static String getUrlAddress(String ip, int port) {
        return IPAddressUtil.isIPv6LiteralAddress(ip) ? "[" + ip + "]:" + port : ip + ":" + port;
    }
}

