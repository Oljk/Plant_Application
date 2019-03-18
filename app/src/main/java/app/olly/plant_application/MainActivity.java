package app.olly.plant_application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.Plant;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
    private MyDBHelper dbhelper;
    RecyclerView recyclerView;
    private MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


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
        Plant plant = new Plant(R.drawable.first, 1000, "bodka", 1,new Date(), "ficus");
        Plant plant2 =  new Plant(R.drawable.second, 1000, "alenka", 10,new Date(), "kaktus");
        ArrayList<Plant> plants = new ArrayList<>();
        plants.add(plant);
        plants.add(plant2);
        adapter = new MyAdapter(this, plants);
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);

        TextView testTextView = findViewById(R.id.textView);
        testTextView.setText(String.valueOf(recyclerView.getAdapter().getItemCount()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemCLick(View view, int pos) {
        Toast.makeText(this, "your: " + adapter.getItem(pos) + " pos: " + pos, Toast.LENGTH_SHORT).show();
    }
}
