package app.olly.plant_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Date;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.Plant;

public class EditActivity extends AppCompatActivity {

    private MyDBHelper dbhelper;
    private Boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbhelper = new MyDBHelper(this);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        isCreate = DefaultPlant.CONST_CREATE.equals(intent.getStringExtra("data"));
        final TextView name = findViewById(R.id.name_edit);
        final TextView planttype = findViewById(R.id.type_edit);
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView time = findViewById(R.id.time);
        final Plant editPlant;

        if (!isCreate) {

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
                Date date = new Date(calendarView.getDate());
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
}
