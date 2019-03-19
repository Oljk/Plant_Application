package app.olly.plant_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.Plant;

public class EditActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private MyDBHelper dbhelper;
    private Boolean isCreate;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbhelper = new MyDBHelper(this);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String intentData = intent.getStringExtra("data");
        isCreate = DefaultPlant.CONST_CREATE.equals(intentData);
        final TextView name = findViewById(R.id.name_edit);
        final TextView planttype = findViewById(R.id.type_edit);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView time = findViewById(R.id.edit_time);
        calendarView.setOnDateChangeListener(this);
        if (!isCreate) {

            String query = "SELECT " + MyDBHelper.PlantsTable.COLUMN_ID  + ", " +
                    MyDBHelper.PlantsTable.COLUMN_PLANT_NAME + " , " +
                    MyDBHelper.PlantsTable.COLUMN_PLANT_TYPE + " , " +
                    MyDBHelper.PlantsTable.COLUMN_PERIOD + " , " +
                    MyDBHelper.PlantsTable.COlUMN_WATER_TIME +
                    " FROM " + MyDBHelper.PlantsTable.TABLE_NAME +
                    " WHERE " + MyDBHelper.PlantsTable.COLUMN_ID + " = " + intentData ;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToNext()) {
                String Pname = cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PLANT_NAME));
                String Pplanttype = cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PLANT_TYPE));
                int Pperiod =  cursor.getInt(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PERIOD));
                Date Pwater_date;
                try {
                    Pwater_date = Plant.DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COlUMN_WATER_TIME)));
                } catch (ParseException e) {
                    Pwater_date = new Date();
                }
                name.setText(Pname);
                planttype.setText(Pplanttype);
                time.setText(String.valueOf(Pperiod));
                calendarView.setDate(Pwater_date.getTime());
            }
            cursor.close();
            dbhelper.ItemDelete(db, intentData);
        }


        Button buttonEdit = findViewById(R.id.button);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long numberOfRows = DatabaseUtils.queryNumEntries(db, MyDBHelper.PlantsTable.TABLE_NAME);
                int id = DefaultPlant.generateNewId((int) numberOfRows);
                ContentValues values = new ContentValues();
                values.put(MyDBHelper.PlantsTable.COLUMN_PLANT_NAME, name.getText().toString());
                values.put(MyDBHelper.PlantsTable.COLUMN_ID, id);
                values.put(MyDBHelper.PlantsTable.COLUMN_PLANT_TYPE, planttype.getText().toString());
                values.put(MyDBHelper.PlantsTable.COLUMN_PERIOD, time.getText().toString());
                Date date = (calendar != null)?calendar.getTime():new Date(calendarView.getDate());
                String sdate = Plant.DATE_FORMAT.format(date);
                values.put(MyDBHelper.PlantsTable.COlUMN_WATER_TIME, sdate);
                /* for maybe future image */
                values.put(MyDBHelper.PlantsTable.COLUMN_PLANT_IMAGE, 0);
               long success =   db.insert(MyDBHelper.PlantsTable.TABLE_NAME, null, values);
                Intent intent = new Intent();
                // setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        calendar = new GregorianCalendar( year, month, dayOfMonth );
    }
}
