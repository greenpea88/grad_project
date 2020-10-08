package com.grad_proj.assembletickets.front;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String NAME = "userdata.db";
    public static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists userdata("
                + " _id integer PRIMARY KEY, "
                + " email text, "
                + " display_name text, "
                + " birthday text, "
                + " gender integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        if (newVer > 1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS userdata");
        }
    }
}
