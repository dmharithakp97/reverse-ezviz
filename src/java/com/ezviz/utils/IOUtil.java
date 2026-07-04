/*
 * Decompiled with CFR 0.152.
 */
package com.ezviz.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class IOUtil {
    private static final int BUFFER_SIZE = 8192;

    public static long write(InputStream is, OutputStream os) throws IOException {
        return IOUtil.write(is, os, 8192);
    }

    public static long write(InputStream is, OutputStream os, int bufferSize) throws IOException {
        long total = 0L;
        byte[] buff = new byte[bufferSize];
        while (is.available() > 0) {
            int read = is.read(buff, 0, buff.length);
            if (read <= 0) continue;
            os.write(buff, 0, read);
            total += (long)read;
        }
        return total;
    }

    public static String read(Reader reader) throws IOException {
        try (StringWriter writer = new StringWriter();){
            IOUtil.write(reader, writer);
            String string = writer.getBuffer().toString();
            return string;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static long write(Writer writer, String string) throws IOException {
        try (StringReader reader = new StringReader(string);){
            long l = IOUtil.write(reader, writer);
            return l;
        }
    }

    public static long write(Reader reader, Writer writer) throws IOException {
        return IOUtil.write(reader, writer, 8192);
    }

    public static long write(Reader reader, Writer writer, int bufferSize) throws IOException {
        int read;
        long total = 0L;
        char[] buf = new char[8192];
        while ((read = reader.read(buf)) != -1) {
            writer.write(buf, 0, read);
            total += (long)read;
        }
        return total;
    }

    public static String[] readLines(File file) throws IOException {
        if (file == null || !file.exists() || !file.canRead()) {
            return new String[0];
        }
        return IOUtil.readLines(new FileInputStream(file));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String[] readLines(InputStream is) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));){
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            String[] stringArray = lines.toArray(new String[lines.size()]);
            return stringArray;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeLines(OutputStream os, String[] lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));){
            for (String line : lines) {
                writer.println(line);
            }
            writer.flush();
        }
    }

    public static void writeLines(File file, String[] lines) throws IOException {
        if (file == null) {
            throw new IOException("File is null.");
        }
        IOUtil.writeLines(new FileOutputStream(file), lines);
    }

    public static void appendLines(File file, String[] lines) throws IOException {
        if (file == null) {
            throw new IOException("File is null.");
        }
        IOUtil.writeLines(new FileOutputStream(file, true), lines);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtil.copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = IOUtil.copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int)count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += (long)n;
        }
        return count;
    }

    public static void closeQuietly(Closeable x) {
        if (x != null) {
            try {
                x.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String inputStreamToChars(InputStream is) {
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        char[] chars = new char[1024];
        try {
            in = new BufferedReader(new InputStreamReader(is));
            int r = -1;
            while ((r = in.read(chars, 0, 1024)) != -1) {
                buffer.append(chars, 0, r);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return buffer.toString();
    }

    public static InputStream stringToInputStream(String src) {
        ByteArrayInputStream stream = new ByteArrayInputStream(src.getBytes());
        return stream;
    }

    public static String getFileNameByUrl(String url) {
        String name = null;
        if (url != null) {
            int start = url.lastIndexOf("/");
            int end = url.lastIndexOf("?");
            name = url.substring(start == -1 ? 0 : start + 1, end == -1 ? url.length() : end);
        }
        return name;
    }
}

