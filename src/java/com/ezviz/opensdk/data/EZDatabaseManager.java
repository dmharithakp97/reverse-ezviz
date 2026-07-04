/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.text.TextUtils
 */
package com.ezviz.opensdk.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.ezviz.opensdk.data.EZDatabaseHelper;
import com.videogo.exception.EZOpenSDKErrorInfo;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EZDatabaseManager {
    private static EZDatabaseManager mDatabaseManager;
    private static EZDatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private EZDatabaseManager() {
    }

    public static EZDatabaseManager getInstance() {
        return mDatabaseManager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized void insertData(EZOpenSDKErrorInfo ezOpenSDKErrorInfo) {
        if (ezOpenSDKErrorInfo == null || TextUtils.isEmpty((CharSequence)ezOpenSDKErrorInfo.detailCode)) {
            return;
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("module_code", ezOpenSDKErrorInfo.moduleCode);
            cv.put("detail_code", ezOpenSDKErrorInfo.detailCode);
            cv.put("description", ezOpenSDKErrorInfo.description);
            cv.put("solution", ezOpenSDKErrorInfo.solution);
            cv.put("update_time", Long.valueOf(ezOpenSDKErrorInfo.updateTime));
            db.insert("open_error_code", null, cv);
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "getErrorTableVersion: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(null);
        }
    }

    public synchronized void deleteData(EZOpenSDKErrorInfo ezOpenSDKErrorInfo) {
        if (ezOpenSDKErrorInfo == null || TextUtils.isEmpty((CharSequence)ezOpenSDKErrorInfo.detailCode)) {
            return;
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("open_error_code", "detail_code=?", new String[]{ezOpenSDKErrorInfo.detailCode});
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "getErrorTableVersion: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized ArrayList<EZOpenSDKErrorInfo> searchAllData() {
        ArrayList<EZOpenSDKErrorInfo> errorInfos = new ArrayList<EZOpenSDKErrorInfo>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(true, "open_error_code", null, null, null, null, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                EZOpenSDKErrorInfo errorInfo = new EZOpenSDKErrorInfo();
                errorInfo.moduleCode = cursor.getString(cursor.getColumnIndex("module_code"));
                errorInfo.detailCode = cursor.getString(cursor.getColumnIndex("detail_code"));
                errorInfo.description = cursor.getString(cursor.getColumnIndex("description"));
                errorInfo.solution = cursor.getString(cursor.getColumnIndex("solution"));
                errorInfo.updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
                errorInfos.add(errorInfo);
            }
            this.closeDatabase(cursor);
        }
        catch (Exception e) {
            try {
                LogUtil.e("EZDatabaseManager", "getErrorTableVersion: " + e.getMessage(), e);
                this.closeDatabase(cursor);
            }
            catch (Throwable throwable) {
                this.closeDatabase(cursor);
                throw throwable;
            }
        }
        return errorInfos;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized EZOpenSDKErrorInfo searchData(String detailCode) {
        if (TextUtils.isEmpty((CharSequence)detailCode)) {
            return null;
        }
        EZOpenSDKErrorInfo errorInfo = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.query(true, "open_error_code", null, "detail_code=?", new String[]{detailCode}, null, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                errorInfo = new EZOpenSDKErrorInfo();
                errorInfo.moduleCode = cursor.getString(cursor.getColumnIndex("module_code"));
                errorInfo.detailCode = cursor.getString(cursor.getColumnIndex("detail_code"));
                errorInfo.description = cursor.getString(cursor.getColumnIndex("description"));
                errorInfo.solution = cursor.getString(cursor.getColumnIndex("solution"));
                errorInfo.updateTime = cursor.getLong(cursor.getColumnIndex("update_time"));
            }
            this.closeDatabase(cursor);
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "searchData: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(cursor);
        }
        return errorInfo;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized void updateData(EZOpenSDKErrorInfo ezOpenSDKErrorInfo) {
        if (ezOpenSDKErrorInfo == null || TextUtils.isEmpty((CharSequence)ezOpenSDKErrorInfo.detailCode)) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("module_code", ezOpenSDKErrorInfo.moduleCode);
        cv.put("detail_code", ezOpenSDKErrorInfo.detailCode);
        cv.put("description", ezOpenSDKErrorInfo.description);
        cv.put("solution", ezOpenSDKErrorInfo.solution);
        cv.put("update_time", Long.valueOf(ezOpenSDKErrorInfo.updateTime));
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(true, "open_error_code", null, "detail_code=?", new String[]{ezOpenSDKErrorInfo.detailCode}, null, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                db.update("open_error_code", cv, "detail_code=?", new String[]{ezOpenSDKErrorInfo.detailCode});
            } else {
                db.insert("open_error_code", null, cv);
            }
            this.closeDatabase(cursor);
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "updateData: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(cursor);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized String getErrorTableVersion() {
        String errorTableVersion = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.query(true, "open_version", null, "name=?", new String[]{"error_code_version"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                errorTableVersion = cursor.getString(cursor.getColumnIndex("version"));
            }
            this.closeDatabase(cursor);
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "getErrorTableVersion: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(cursor);
        }
        return errorTableVersion;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized void setErrorTableVersion(String time) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "error_code_version");
            contentValues.put("version", String.valueOf(time));
            db.update("open_version", contentValues, "name=?", new String[]{"error_code_version"});
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "getErrorTableVersion: " + e.getMessage(), e);
        }
        finally {
            this.closeDatabase(null);
        }
    }

    public static synchronized void initializeInstance(EZDatabaseHelper helper) {
        if (mDatabaseManager == null) {
            mDatabaseManager = new EZDatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized EZDatabaseManager getInstance(EZDatabaseHelper helper) {
        if (mDatabaseManager == null) {
            EZDatabaseManager.initializeInstance(helper);
        }
        return mDatabaseManager;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (this.mOpenCounter.incrementAndGet() == 1) {
            this.mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return this.mDatabase;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (this.mOpenCounter.incrementAndGet() == 1) {
            this.mDatabase = mDatabaseHelper.getReadableDatabase();
        }
        return this.mDatabase;
    }

    private synchronized void closeDatabase(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "closeDatabase cursor:" + e.getMessage(), e);
        }
        try {
            if (this.mOpenCounter.decrementAndGet() == 0) {
                this.mDatabase.close();
            }
        }
        catch (Exception e) {
            LogUtil.e("EZDatabaseManager", "closeDatabase mDatabase:" + e.getMessage(), e);
        }
    }
}

