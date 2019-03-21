package app.olly.plant_application;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.NotificationHelper;
import app.olly.plant_application.sdata.Plant;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener, MyAdapter.ItemLongClickListener{
    private MyDBHelper dbhelper;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Plant> plants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("data", DefaultPlant.CONST_CREATE);
                startActivity(intent);
            }
        });
        dbhelper = new MyDBHelper(this);
        recyclerView = findViewById(R.id.mylist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        updateList();



    }
    private void updateList() {
        plants = new ArrayList<>();
        final SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT " + MyDBHelper.PlantsTable.COLUMN_ID  + ", " +
                                   MyDBHelper.PlantsTable.COLUMN_PLANT_NAME + " , " +
                                  MyDBHelper.PlantsTable.COLUMN_PLANT_TYPE + " , " +
                                   MyDBHelper.PlantsTable.COLUMN_PERIOD + " , " +
                                   MyDBHelper.PlantsTable.COlUMN_WATER_TIME +
                       " FROM " + MyDBHelper.PlantsTable.TABLE_NAME +
                       " ORDER BY " + MyDBHelper.PlantsTable.COLUMN_ID;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PLANT_NAME));
            int id = cursor.getInt(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_ID));
            String planttype = cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PLANT_TYPE));
            int period =  cursor.getInt(cursor.getColumnIndex(MyDBHelper.PlantsTable.COLUMN_PERIOD));
            Date water_date;
            try {
                water_date = Plant.DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(MyDBHelper.PlantsTable.COlUMN_WATER_TIME)));
            } catch (ParseException e) {
                water_date = new Date();
            }
            Plant plant = new Plant(R.drawable.cactus, period, name, id, water_date, planttype);
            plants.add(plant);
        }
        cursor.close();

        adapter = new MyAdapter(this, plants);
        adapter.setOnClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "SELECT " + MyDBHelper.ConstsTable.COLUMN_DATA +
                " FROM " + MyDBHelper.ConstsTable.TABLE_NAME +
                " WHERE " + MyDBHelper.ConstsTable.COLUMN_NAME+ " ='" + MyDBHelper.CONST_ENABLE_NOTIFICATIONS_TITLE + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            if(!"1".equals(cursor.getString(cursor.getColumnIndex(MyDBHelper.ConstsTable.COLUMN_DATA)))) {
                menu.findItem(R.id.action_notifications).setTitle("Disable notifications");
            } else  {
                menu.findItem(R.id.action_notifications).setTitle("Enable notifications");
            }
        }
        cursor.close();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        int id = item.getItemId();
        if (id == R.id.edit_time_action) {
            Intent intent = new Intent(MainActivity.this, EditNotificationTime.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_notifications) {
            boolean isEnable = false;
            final SQLiteDatabase db = dbhelper.getWritableDatabase();
            String query = "SELECT " + MyDBHelper.ConstsTable.COLUMN_DATA +
                    " FROM " + MyDBHelper.ConstsTable.TABLE_NAME +
                    " WHERE " + MyDBHelper.ConstsTable.COLUMN_NAME+ " ='" + MyDBHelper.CONST_ENABLE_NOTIFICATIONS_TITLE + "'" ;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToNext()) {
                isEnable = "1".equals(cursor.getString(cursor.getColumnIndex(MyDBHelper.ConstsTable.COLUMN_DATA)));
            }
            cursor.close();

            String enabled;
            if (isEnable) {
                // when notifications was enable, so we should it disable
                NotificationHelper.cancelAlarmRTC();
               // NotificationHelper.disableBootReceiver(context);
                item.setTitle("Enable notifications");
                enabled = "0";
            } else {
                query = "SELECT " + MyDBHelper.ConstsTable.COLUMN_DATA +
                        " FROM " + MyDBHelper.ConstsTable.TABLE_NAME +
                        " WHERE " + MyDBHelper.ConstsTable.COLUMN_NAME+ " ='" + MyDBHelper.CONST_TIME_NAME + "'" ;
                 cursor = db.rawQuery(query, null);
                 String cur_time = "0:0";
                if (cursor.moveToNext()) {
                    cur_time = cursor.getString(cursor.getColumnIndex(MyDBHelper.ConstsTable.COLUMN_DATA));
                }
                cursor.close();
                String[] times = cur_time.split(MyDBHelper.CONST_TIME_SEPARATOP);
                int hoursInt = Integer.parseInt(times[0]);
                int minInt = Integer.parseInt(times[1]);
                NotificationHelper.scheduleRepeatingRTCNotification(context, hoursInt, minInt);
               // NotificationHelper.enableBootReceiver(context);
                item.setTitle("Disable notifications");
                enabled = "1";
            }
            //update notifications status in database
            ContentValues values = new ContentValues();
            values.put(MyDBHelper.ConstsTable.COLUMN_DATA, enabled);
            long status = db.update(MyDBHelper.ConstsTable.TABLE_NAME, values, MyDBHelper.ConstsTable.COLUMN_NAME + " = ?", new String[]{MyDBHelper.CONST_ENABLE_NOTIFICATIONS_TITLE});
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemCLick(View view, String name, final int id) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Watering");
        builder.setMessage("Have you watered your plant?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                values.put(MyDBHelper.PlantsTable.COlUMN_WATER_TIME, Plant.DATE_FORMAT.format(new Date(System.currentTimeMillis())));
                db.update(MyDBHelper.PlantsTable.TABLE_NAME, values, MyDBHelper.PlantsTable.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
                updateList();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onLongItemCLick(View view, int position, final int id) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Editing");
        builder.setMessage("Edit or delete?");

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("data", String.valueOf(id));
                dialog.dismiss();
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDBHelper.ItemPlantDelete(db, String.valueOf(id));
                MainActivity.this.updateList();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
