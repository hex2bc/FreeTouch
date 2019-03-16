package lmkj.freetouch.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DiskDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "disk.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DiskEntry.TABLE_NAME + " (" +
                        DiskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                        DiskEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        DiskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        DiskEntry.COLUMN_NAME_DRAWABLE_ID + INTEGER_TYPE + COMMA_SEP +
                        DiskEntry.COLUMN_NAME_FRONT + BOOLEAN_TYPE + COMMA_SEP +
                        DiskEntry.COLUMN_NAME_CAN_DELETE + BOOLEAN_TYPE + COMMA_SEP +
                        DiskEntry.COLUMN_NAME_REMOVE + BOOLEAN_TYPE +
                        " )";


    public DiskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public static abstract class DiskEntry implements BaseColumns {
        public static final String TABLE_NAME = "disk";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "text";
        public static final String COLUMN_NAME_DRAWABLE_ID = "drawable";
        public static final String COLUMN_NAME_FRONT = "front";
        public static final String COLUMN_NAME_REMOVE = "remove";
        public static final String COLUMN_NAME_CAN_DELETE = "candelete";
    }
}
