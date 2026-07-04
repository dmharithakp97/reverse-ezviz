/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.opensdk.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveInfo(String path, String value) {
        File file = new File(path);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getInfo(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        BufferedReader in = null;
        try {
            String currentLine;
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            while ((currentLine = in.readLine()) != null) {
                readString = readString + currentLine;
            }
            String string = readString;
            return string;
        }
        catch (IOException e) {
            e.printStackTrace();
            String string = "";
            return string;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

