/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationManager
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.AssetManager
 *  android.content.res.ColorStateList
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.graphics.NinePatch
 *  android.graphics.Rect
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.NinePatchDrawable
 *  android.graphics.drawable.StateListDrawable
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.provider.Settings$System
 *  android.text.TextUtils
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.Toast
 */
package com.videogo.util;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.videogo.constant.Config;
import com.videogo.util.DateTimeUtil;
import com.videogo.util.LogUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String getNetTypeName(Context context) {
        NetworkInfo curNetwork;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivity != null && (curNetwork = connectivity.getActiveNetworkInfo()) != null) {
            int nType = curNetwork.getType();
            if (nType == 1) {
                return curNetwork.getTypeName();
            }
            if (nType == 0) {
                return curNetwork.getSubtypeName();
            }
            return "UNKNOWN";
        }
        return "UNKNOWN";
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "UNKNOWN";
        }
    }

    public static void showToast(Context context, String text) {
        if (context == null) {
            return;
        }
        if (text != null && !text.equals("")) {
            Toast toast = Toast.makeText((Context)context, (CharSequence)text, (int)1);
            toast.setGravity(17, 0, 0);
            try {
                toast.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showToast(Context context, int id, int errCode) {
        if (context == null) {
            return;
        }
        String text = context.getString(id);
        if (errCode != 0) {
            text = text + " (" + errCode + ")";
        }
        if (text != null && !text.equals("")) {
            Toast toast = Toast.makeText((Context)context, (CharSequence)text, (int)1);
            toast.setGravity(17, 0, 0);
            try {
                toast.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showToast(Context context, int id) {
        if (context == null) {
            return;
        }
        String text = context.getString(id);
        if (text != null && !text.equals("")) {
            Toast toast = Toast.makeText((Context)context, (CharSequence)text, (int)1);
            toast.setGravity(17, 0, 0);
            try {
                toast.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getErrorTip(Context context, int id, int errCode) {
        StringBuffer errorTip = new StringBuffer();
        errorTip.append(context.getString(id));
        if (errCode != 0) {
            errorTip.append(" (").append(errCode).append(")");
        }
        return errorTip.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getCPUSerial() {
        String str = "";
        String strCPU = "";
        String cpuAddress = "0000000000000000";
        Process pp = null;
        InputStreamReader ir = null;
        BufferedReader input = null;
        try {
            pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            ir = new InputStreamReader(pp.getInputStream());
            input = new LineNumberReader(ir);
            for (int i = 1; i < 100 && (str = ((LineNumberReader)input).readLine()) != null; ++i) {
                if (str.indexOf("serial") <= -1) continue;
                strCPU = TextUtils.substring((CharSequence)str, (int)(str.indexOf(":") + 1), (int)str.length());
                cpuAddress = strCPU.trim();
                break;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                input = null;
            }
            if (ir != null) {
                try {
                    ir.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                ir = null;
            }
            if (pp != null) {
                pp.destroy();
                pp = null;
            }
        }
        return cpuAddress;
    }

    public static String getAndroidID(Context context) {
        String androidID = Settings.System.getString((ContentResolver)context.getContentResolver(), (String)"android_id");
        LogUtil.d("androidid", androidID);
        return androidID;
    }

    public static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        notificationManager.cancelAll();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String inputStreamToString(InputStream is) {
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            in = new BufferedReader(new InputStreamReader(is));
            while ((line = in.readLine()) != null) {
                buffer.append(line);
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

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static void showLog(Context context, String content) {
        if (Config.LOGGING) {
            try {
                Toast.makeText((Context)context, (CharSequence)content, (int)0).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Calendar convert14Calender(String stringTime) {
        return DateTimeUtil.parseStringToCalendar(stringTime, "yyyyMMddHHmmss");
    }

    public static Calendar convert16Calender(String stringTime) {
        return DateTimeUtil.parseStringToCalendar(stringTime, "yyyyMMdd'T'HHmmss'Z'");
    }

    public static Calendar convert19Calender(String stringTime) {
        return DateTimeUtil.parseStringToCalendar(stringTime, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static long get19TimeInMillis(String stringTime) {
        Calendar calendar = Utils.convert19Calender(stringTime);
        return calendar != null ? calendar.getTimeInMillis() : 0L;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    private static String trimSpaces(String IP) {
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public static boolean isIp(String IP) {
        String[] s;
        if (IP == null || IP.isEmpty()) {
            return false;
        }
        boolean b = false;
        if ((IP = Utils.trimSpaces(IP)).matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") && Integer.parseInt((s = IP.split("\\."))[0]) < 255 && Integer.parseInt(s[1]) < 255 && Integer.parseInt(s[2]) < 255 && Integer.parseInt(s[3]) < 255) {
            b = true;
        }
        return b;
    }

    public static RelativeLayout.LayoutParams getPlayViewLp(double playRatio, int orientation, int canDisplayWidth, int canDisplayHeight, int displayWidth, int displayHeight) {
        float ratio;
        int width = 0;
        int height = 0;
        double mPhoneRatio = (double)displayWidth / (double)displayHeight;
        float f = ratio = canDisplayWidth == 0 ? 0.0f : (float)canDisplayHeight / (float)canDisplayWidth;
        if (playRatio == 0.0) {
            if (ratio <= 0.5625f) {
                height = canDisplayHeight;
                int deltaX = canDisplayHeight * 4 / 6;
                width = 2 * deltaX;
            } else {
                width = canDisplayWidth;
                int deltaY = (int)((float)canDisplayWidth * 0.5625f) / 2;
                height = deltaY * 2;
            }
        } else if (orientation == 1) {
            width = canDisplayWidth;
            height = (int)((double)width * playRatio);
            height = Math.min(height, width);
        } else if (mPhoneRatio > playRatio) {
            height = displayWidth;
            width = displayHeight;
        } else {
            height = displayWidth;
            width = (int)((double)height / playRatio);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        lp.addRule(13);
        return lp;
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream((InputStream)is);
            is.close();
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace("getImageFromAssetsFile", e.fillInStackTrace());
        }
        return image;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Drawable getDrawableFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream((InputStream)is);
            is.close();
            is = null;
        }
        catch (IOException e) {
            LogUtil.printErrStackTrace("getDrawableFromAssetsFile", e.fillInStackTrace());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
        if (image != null) {
            byte[] chunk = image.getNinePatchChunk();
            if (chunk != null) {
                boolean result = NinePatch.isNinePatchChunk((byte[])chunk);
                NinePatchDrawable patchy = new NinePatchDrawable(image, chunk, new Rect(), null);
                return patchy;
            }
            return new BitmapDrawable(image);
        }
        return null;
    }

    public static StateListDrawable newSelector(Context context, Drawable normal, Drawable pressed, Drawable focused, Drawable unable) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{16842919, 16842910}, pressed);
        bg.addState(new int[]{16842910, 16842908}, focused);
        bg.addState(new int[]{16842910}, normal);
        bg.addState(new int[]{16842908}, focused);
        bg.addState(new int[]{16842909}, unable);
        bg.addState(new int[0], normal);
        return bg;
    }

    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[][]{{16842919, 16842910}, {16842910, 16842908}, {16842910}, {16842908}, {16842909}, new int[0]};
        ColorStateList colorList = new ColorStateList((int[][])states, colors);
        return colorList;
    }

    public static String getUrlValue(String url, String startStr, String endStr) {
        int endIndex;
        if (url == null || startStr == null) {
            return null;
        }
        int startIndex = url.indexOf(startStr);
        if (startIndex < 0) {
            return null;
        }
        int n = endIndex = endStr != null ? url.indexOf(endStr, startIndex) : url.length();
        if (startIndex >= endIndex) {
            endIndex = url.length();
        }
        try {
            return url.substring(startIndex + startStr.length(), endIndex);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void parseTestConfigFile(String filePath, Map<String, String> map) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String lineStr = br.readLine();
            while (lineStr != null) {
                String[] values = lineStr.split("\\$");
                if (values.length == 2) {
                    map.put(values[0], values[1]);
                }
                lineStr = br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isEZOpenProtocol(String url) {
        return url.startsWith("ezopen://");
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

