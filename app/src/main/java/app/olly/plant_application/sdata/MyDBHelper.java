package app.olly.plant_application.sdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MyDBHelper extends SQLiteOpenHelper {

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


    private static final String DATABASE_NAME = "plants.db";

    private static final int DATABASE_VERSION = 2;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
    }
    public static void ItemDelete(SQLiteDatabase db,String id) {
        System.out.println("hello");
        db.delete(PlantsTable.TABLE_NAME, PlantsTable.COLUMN_ID + " = ?", new String[] {id});
        System.out.println("hello2");
    }
}
