package com.grad_proj.assembletickets.front.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grad_proj.assembletickets.front.Event;

public class SDatabaseOpen {

    public static String DATABASE_NAME = "searchManager.db";
    public static int VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context context;

    public class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME,null,VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //event 정보 저장하는 db 생성하기
            sqLiteDatabase.execSQL(SearchDatabase.SearchHistoryDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
            if (newVer > 1) {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS searchtable");
            }
        }
    }

    public SDatabaseOpen(Context context){
        this.context = context;
    }

    public SDatabaseOpen open() throws SQLException{
        mDBHelper = new DatabaseHelper(context);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDBHelper.close();
    }

    public long insertColumn(String context){
        //insert DB - with content
        ContentValues values = new ContentValues();
        values.put(SearchDatabase.SearchHistoryDB.CONTEXT, context);

        return mDB.insert(SearchDatabase.SearchHistoryDB._TABLENAME,null, values);
    }

    public Cursor selectHistory(String context){
        String sql = "SELECT * FROM " + SearchDatabase.SearchHistoryDB._TABLENAME
                + " WHERE " + SearchDatabase.SearchHistoryDB.CONTEXT + "='" + context + "'"
                + " ORDER BY " + SearchDatabase.SearchHistoryDB._ID + " DESC";

        return mDB.rawQuery(sql,null);
    }

    public Cursor selectHistoryAll(){
        String sql = "SELECT * FROM " + SearchDatabase.SearchHistoryDB._TABLENAME
                + " ORDER BY " + SearchDatabase.SearchHistoryDB._ID + " DESC";

        return mDB.rawQuery(sql,null);
    }

    public void deleteColumn(int id){
        String sql = "DELETE FROM " + SearchDatabase.SearchHistoryDB._TABLENAME
                + " WHERE "+ SearchDatabase.SearchHistoryDB._ID + "=" + id;

        mDB.execSQL(sql);
    }

    public void deleteDuplicateColumn(String context){
        String sql = "DELETE FROM "+ SearchDatabase.SearchHistoryDB._TABLENAME
                +" WHERE "+ SearchDatabase.SearchHistoryDB.CONTEXT + "='" + context + "'";

        mDB.execSQL(sql);
    }

    public void deleteAll(){
        String sql = "DELETE FROM " + SearchDatabase.SearchHistoryDB._TABLENAME;

        mDB.execSQL(sql);
    }

}
