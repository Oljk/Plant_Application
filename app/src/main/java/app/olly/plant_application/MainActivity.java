package app.olly.plant_application;

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
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.Plant;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener, MyAdapter.ItemLongClickListener{
    private MyDBHelper dbhelper;
    RecyclerView recyclerView;
    private MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ;

    ArrayList<Plant> plants;


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
    public void onResume() {
        super.onResume();
        updateList();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemCLick(View view, int pos) {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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
                MyDBHelper.ItemDelete(db, String.valueOf(id));
                MainActivity.this.updateList();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}
