package app.olly.plant_application.sdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MyDBHelper extends SQLiteOpenHelper {
    public final static String CONST_TIME_NAME = "time";
    public final static String CONST_TIME_SEPARATOP = ":";
    public final static String CONST_TIME_DEFAULT_HOURS = "12";
    public final static String CONST_TIME_DEFAULT_MINUTES = "0";

    public final static String CONST_ENABLE_NOTIFICATIONS_TITLE = "notifications";


    public static final class PlantsTable implements BaseColumns {
        public final static String TABLE_NAME = "plants";
        public final static String COLUMN_PLANT_NAME = "name";
        public final static String COLUMN_PLANT_IMAGE = "image";
        /**
         * now it is plant type name =>
         * if change to Plant type links - > id of choosen planttype, if null = is planttype
         */
        public final static String COLUMN_PLANT_TYPE = "ptype";
        public final static String COLUMN_ID = "id";
        public final static String COlUMN_WATER_TIME = "water_time";
        public final static String COLUMN_PERIOD = "period";
    }
    public static final class ConstsTable implements BaseColumns {
        public final static String TABLE_NAME = "constants";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DATA = "simple_data";
    }

    private static final String DATABASE_NAME = "plants.db";

    private static final int DATABASE_VERSION = 1;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + PlantsTable.TABLE_NAME + " ("
                + PlantsTable.COLUMN_PLANT_NAME + " TEXT NOT NULL, "
                + PlantsTable.COLUMN_PLANT_IMAGE + " TEXT NOT NULL, "
                + PlantsTable.COlUMN_WATER_TIME + " TEXT NOT NULL, "
                + PlantsTable.COLUMN_PLANT_TYPE + " INTEGER, "
                + PlantsTable.COLUMN_PERIOD + " INTEGER, "
                + PlantsTable.COLUMN_ID + " INTEGER NOT NULL );";
        db.execSQL(SQL_CREATE_TABLE);
        String SQL_CREATE_TABLE_DATA = "CREATE TABLE " + ConstsTable.TABLE_NAME + " ("
                + ConstsTable.COLUMN_NAME + " TEXT NOT NULL, "
                + ConstsTable.COLUMN_DATA + " TEXT NOT NULL );";
        db.execSQL(SQL_CREATE_TABLE_DATA);
        createTime(db);
    }

    public void createTime(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        String time = CONST_TIME_DEFAULT_HOURS + CONST_TIME_SEPARATOP + CONST_TIME_DEFAULT_MINUTES;
        values.put(ConstsTable.COLUMN_NAME, CONST_TIME_NAME);
        values.put(ConstsTable.COLUMN_DATA, time);
        long success =   db.insert(ConstsTable.TABLE_NAME, null, values);
        values.clear();
        values.put(ConstsTable.COLUMN_NAME, CONST_ENABLE_NOTIFICATIONS_TITLE);
        values.put(ConstsTable.COLUMN_DATA, "0");
        success =   db.insert(ConstsTable.TABLE_NAME, null, values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
    }
    public static void ItemPlantDelete(SQLiteDatabase db, String id) {
        db.delete(PlantsTable.TABLE_NAME, PlantsTable.COLUMN_ID + " = ?", new String[] {id});
    }
}
