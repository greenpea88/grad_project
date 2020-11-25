package com.grad_proj.assembletickets.front.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grad_proj.assembletickets.front.Event;

public class CDatabaseOpen {

    public static String DATABASE_NAME = "userdataManager.db";
    public static int VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context context;

    public class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //event 정보 저장하는 db 생성하기
            sqLiteDatabase.execSQL(CalendarDatabase.CalendarDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
//            if (newVer > 1) {
//                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS userdata");
//            }
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CalendarDatabase.CalendarDB._TABLENAME);
            onCreate(sqLiteDatabase);
        }
    }

    public CDatabaseOpen(Context context){
        this.context=context;
    }

    public CDatabaseOpen open() throws SQLException{
        //해당 데이터베이스를 열어서 사용 가능하도록 함
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

    public long insertColumn(String eventDate,String eventName,String eventContent,int hour,int min,int alarmSet){
        //insert DB - with content
        ContentValues values = new ContentValues();
        values.put(CalendarDatabase.CalendarDB.EVENTDATE,eventDate);
        values.put(CalendarDatabase.CalendarDB.EVENTNAME,eventName);
        values.put(CalendarDatabase.CalendarDB.EVENTCONTENT,eventContent);
        values.put(CalendarDatabase.CalendarDB.HOUR,hour);
        values.put(CalendarDatabase.CalendarDB.MINUTE,min);
        values.put(CalendarDatabase.CalendarDB.ALARMSET,alarmSet);

        return mDB.insert(CalendarDatabase.CalendarDB._TABLENAME,null,values);
    }

    public Cursor selectDataEvent(String date){
        String sql = "SELECT * FROM "+ CalendarDatabase.CalendarDB._TABLENAME
                +" WHERE "+ CalendarDatabase.CalendarDB.EVENTDATE+"='"+date+"'"
                +" ORDER BY "+ CalendarDatabase.CalendarDB.HOUR;

        return mDB.rawQuery(sql,null);
    }

    public Cursor selectDate(){
        String sql = "SELECT DISTINCT "+ CalendarDatabase.CalendarDB.EVENTDATE
                +" FROM "+ CalendarDatabase.CalendarDB._TABLENAME;

        return mDB.rawQuery(sql,null);
    }

    public Cursor selectEventsAfterDate(String date){
        String sql = "SELECT * FROM "+ CalendarDatabase.CalendarDB._TABLENAME
                +" WHERE "+ CalendarDatabase.CalendarDB.EVENTDATE+">='"+date+"'"
                +" ORDER BY "+ CalendarDatabase.CalendarDB.HOUR;

        return mDB.rawQuery(sql,null);
    }

    public Cursor selectDateByTitle(String title){
        String sql = "SELECT DISTINCT "+ CalendarDatabase.CalendarDB.EVENTDATE
                + " WHERE " + CalendarDatabase.CalendarDB.EVENTNAME + "='" + title + "'"
                +" FROM "+ CalendarDatabase.CalendarDB._TABLENAME;

        return mDB.rawQuery(sql,null);
    }

    public void deleteColumn(int id){
        String sql = "DELETE FROM " + CalendarDatabase.CalendarDB._TABLENAME
                + " WHERE "+ CalendarDatabase.CalendarDB._ID+"="+id;

        mDB.execSQL(sql);
    }

    public boolean updateColumn(Event event){
        ContentValues values = new ContentValues();
        values.put(CalendarDatabase.CalendarDB.EVENTNAME,event.getEventName());
        values.put(CalendarDatabase.CalendarDB.EVENTCONTENT,event.getEventContent());
        values.put(CalendarDatabase.CalendarDB.HOUR,event.getTimeHour());
        values.put(CalendarDatabase.CalendarDB.MINUTE,event.getTimeMin());
        values.put(CalendarDatabase.CalendarDB.ALARMSET,event.getAlarmSet());

        return mDB.update(CalendarDatabase.CalendarDB._TABLENAME,values, CalendarDatabase.CalendarDB._ID+"="+event.getId(),null) > 0;
    }
}
