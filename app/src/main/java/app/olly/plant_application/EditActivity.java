package app.olly.plant_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import app.olly.plant_application.sdata.DefaultPlant;
import app.olly.plant_application.sdata.MyDBHelper;

public class EditActivity extends AppCompatActivity {

    private MyDBHelper dbhelper;
    private Boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbhelper = new MyDBHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        isCreate = DefaultPlant.CONST_CREATE.equals(intent.getStringExtra("data"));

    }
}
