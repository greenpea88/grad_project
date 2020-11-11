package com.grad_proj.assembletickets.front.Database;

import android.provider.BaseColumns;

public class SearchDatabase {
    public static final class SearchHistoryDB implements BaseColumns {
        public static final String CONTEXT = "eventdate";
        public static final String _TABLENAME = "searchtable";
        public static final String _CREATE = "create table if not exists "+_TABLENAME+"("
                + _ID + " integer primary key autoincrement, "
                + CONTEXT + " text not null);";
    }
}
