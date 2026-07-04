/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteOpenHelper
 */
package com.ezviz.opensdk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EZDatabaseHelper
extends SQLiteOpenHelper {
    public static final String name = "ezviz_sdk_db";
    private static final int version = 1;

    public EZDatabaseHelper(Context context) {
        super(context, name, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS open_error_code (       id integer primary key autoincrement, module_code    varchar(64),detail_code        varchar(64),description         varchar(128),solution        varchar(128),update_time        integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS open_version (       id integer primary key autoincrement, name   varchar(64),version   varchar(128))");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "error_code_version");
        contentValues.put("version", "");
        db.insert("open_version", null, contentValues);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

