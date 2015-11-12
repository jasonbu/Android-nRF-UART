package com.hetangsmart.testv1;

/**
 * Created by jasonbu on 2015/11/13.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private static final String dbDirName = "tsSQLITE";     //数据库存放的文件夹
    private static final String dbDFileName = "t1.db";     //数据库存放的文件名

    public DBManager(Context context){
        Context c = new DatabaseContext(context,dbDirName);   //将保存路径修改到指定位置
        helper = new DBHelper(c,dbDFileName);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void add(DBAccelerationType data){
        try{
            db.beginTransaction();  //开始事务
            db.execSQL("INSERT INTO STEP_DATA(TIME,X,Y,Z) VALUES(?,?,?,?)",
                    new Object[]{data.stamp, data.x, data.y, data.z});
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();// 结束事务
        }
    }

/*    ArrayList<DBAccelerationType> ds = new ArrayList<DBAccelerationType>();

    DBAccelerationType d1 = new DBAccelerationType(0,1,2,3);
    DBAccelerationType d2 = new DBAccelerationType(0,1,2,4);
    DBAccelerationType d3 = new DBAccelerationType(0,1,2,5);
    DBAccelerationType d4 = new DBAccelerationType(0,1,2,3);
    ds.add(d1);
    ds.add(d2);
    ds.add(d3);
    ds.add(d4);
    ds.add(d4);
    this.add(ds);*/
    public void add(List<DBAccelerationType> datas){
        try{
            db.beginTransaction();  //开始事务
            for(DBAccelerationType data :datas) {
                db.execSQL("INSERT INTO STEP_DATA(TIME,X,Y,Z) VALUES(?,?,?,?)",
                        new Object[]{data.stamp, data.x, data.y, data.z});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();// 结束事务
        }
    }
}
