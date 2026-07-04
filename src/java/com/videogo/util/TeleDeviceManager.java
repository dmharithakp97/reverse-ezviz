/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$MemoryInfo
 *  android.content.Context
 *  android.os.Debug
 *  android.os.Debug$MemoryInfo
 *  android.os.Process
 *  android.util.Log
 */
package com.videogo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TeleDeviceManager {
    private static int coreCount;
    private static int idlePos;
    private static int cpuPos;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static CPUUsage getCPU() {
        CPUUsage cpuUsage = new CPUUsage();
        Process process = null;
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec("top -n 1");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            boolean cpuFind = false;
            boolean tempAppFind = false;
            block11: while (!cpuFind || !tempAppFind || cpuPos == -1) {
                int i;
                String[] info;
                String line = bufferedReader.readLine().trim();
                Log.d((String)"TEST1234", (String)line);
                if (!cpuFind && line.contains("cpu") && line.contains("idle")) {
                    cpuFind = true;
                    info = line.split(" +");
                    if (coreCount == 0) {
                        coreCount = Integer.parseInt(info[0].replace("%cpu", "")) / 100;
                    }
                    if (idlePos == -1) {
                        for (i = 0; i < info.length; ++i) {
                            if (!info[i].contains("idle")) continue;
                            idlePos = i;
                            break;
                        }
                    }
                    info[TeleDeviceManager.idlePos] = info[idlePos].replace("%idle", "");
                    cpuUsage.idlePercent = Integer.parseInt(info[idlePos]) / coreCount;
                    continue;
                }
                if (!tempAppFind && line.contains(String.valueOf(android.os.Process.myPid()))) {
                    tempAppFind = true;
                    info = line.split(" +");
                    if (info[cpuPos].matches("[A-Za-z]+")) {
                        ++cpuPos;
                    }
                    cpuUsage.processUsedPercent = (int)(Float.parseFloat(info[cpuPos]) / (float)coreCount);
                    continue;
                }
                if (!line.contains("%CPU")) continue;
                info = line.split(" +");
                for (i = 0; i < info.length; ++i) {
                    if (!info[i].contains("%CPU")) continue;
                    cpuPos = i;
                    continue block11;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return cpuUsage;
    }

    public static long getIdleMemory(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / 1024L;
    }

    public static long getTempMemory() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo((Debug.MemoryInfo)memoryInfo);
        return memoryInfo.getTotalPss();
    }

    static {
        idlePos = -1;
        cpuPos = -1;
    }

    public static class CPUUsage {
        public int idlePercent;
        public int processUsedPercent;
    }
}

