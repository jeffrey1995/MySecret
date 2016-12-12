package com.mrtian.isecret.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrtian.isecret.entity.Secret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tianxiying on 16/7/14.
 */
public class MyDBManager {
    private MyDBHelper helper;
    private SQLiteDatabase db;

    public MyDBManager(Context context) {
        helper = new MyDBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add secret
     *
     * @param secrets
     */
    public void add(List<Secret> secrets) {
        db.beginTransaction();  //开始事务
        try {
            for (Secret secret : secrets) {
                ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
                cv.put("title", secret.getTitle());
                cv.put("image_id", secret.getImageId());
                cv.put("text", secret.getText());
                cv.put("date", secret.getDate());
                db.insert(MyDBHelper.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void add(Secret secret) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
            cv.put("title", secret.getTitle());
            cv.put("image_id", secret.getImageId());
            cv.put("text", secret.getText());
            cv.put("date", secret.getDate());
            db.insert(MyDBHelper.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    /**
     * query all forwardHistory, return list
     *
     * @return List<ForwardHistory>
     */
    public List<Secret> query() {
        ArrayList<Secret> history_list = new ArrayList<Secret>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Secret secret = new Secret();
            secret.setId(c.getString(c.getColumnIndex("_id")));
            secret.setImageId(c.getString(c.getColumnIndex("image_id")));
            secret.setTitle(c.getString(c.getColumnIndex("title")));
            secret.setText(c.getString(c.getColumnIndex("text")));
            secret.setDate(c.getString(c.getColumnIndex("date")));
            history_list.add(secret);
        }
        c.close();
        return history_list;
    }

    /**
     * 查询数据库中是否包含相同ID的数据
     *
     * @param news_id
     * @return
     */
    public boolean isExist(String news_id) {
        boolean result = false;
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex("_id")).equals(news_id)) {
                result = true;
                break;
            }
        }
        c.close();
        return result;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + MyDBHelper.TABLE_NAME, null);
        return c;
    }

    /**
     * 更新数据
     *
     * @param map
     * @return
     */
    public int update(Map<String, Object> map) {
        int result = -1;
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
            cv.put("title", map.get("title").toString());
            cv.put("text", map.get("text").toString());
            cv.put("date", map.get("date").toString());
            String[] where = {map.get("_id").toString()};
            result = db.update(MyDBHelper.TABLE_NAME, cv, "_id=?", where);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (result > 0) return 0;
        else return -1;
    }

    /**
     * 从数据库中移除某条数据
     *
     * @param _id 数据ID
     */
    public void remove(String _id) {
        db.execSQL("DELETE FROM " + MyDBHelper.TABLE_NAME + " where _id=" + _id);
    }

    /**
     * 清空
     */
    public void clear() {
        db.execSQL("DELETE FROM " + MyDBHelper.TABLE_NAME);
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
