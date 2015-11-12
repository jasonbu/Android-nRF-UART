package com.hetangsmart.testv1;

/**
 * Created by jasonbu on 2015/11/13.
 * DBhelper to locate db to sd
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends  SQLiteOpenHelper{
    private static final String TAG = "sqlite_helper";
    private static final int DATABASE_VERSION = 2;
//    private static final String CREATE_TBL = "CREATE TABLE IF NOT EXISTS STEP_DATA" +
//            "(id INTEGER PRIMARY KEY NOT NULL, value INTEGER)";
    private static final String CREATE_TBL = "CREATE TABLE IF NOT EXISTS STEP_DATA" +
            "(id INTEGER PRIMARY KEY NOT NULL, TIME INTEGER, X INTEGER, Y INTEGER, Z INTEGER)";

    private static String DATABASE_NAME = "current.db";


    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DBHelper(Context context, String dbName) {
        //CursorFactory设置为null,使用默认值
        super(context, dbName, null, DATABASE_VERSION);
        this.DATABASE_NAME = dbName;
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL);
        db.execSQL("INSERT INTO STEP_DATA(TIME,X,Y,Z) VALUES(0,1,2,3)");
        Log.v(TAG, "on_create");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ramdon_data");
        db.execSQL("DROP TABLE IF EXISTS STEP_DATA");
        Log.v(TAG, "on_upgrade");
    }
}
