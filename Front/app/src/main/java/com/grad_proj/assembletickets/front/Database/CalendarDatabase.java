package com.grad_proj.assembletickets.front.Database;

import android.provider.BaseColumns;

public class CalendarDatabase {

    public static final class CalendarDB implements BaseColumns {
        public static final String EVENTDATE = "eventdate";
        public static final String EVENTNAME = "eventname";
        public static final String EVENTCONTENT = "eventcontent";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String ALARMSET = "alarmset";
        public static final String _TABLENAME = "calendartable";
        public static final String _CREATE = "create table if not exists "+_TABLENAME+"("
                + _ID + " integer primary key autoincrement, "
                + EVENTDATE + " text not null , "
                + EVENTNAME + " text not null , "
                + EVENTCONTENT + " text, "
                + HOUR + " integer not null , "
                + MINUTE + " integer not null,"
                + ALARMSET + " integer not null);";
    }
}
