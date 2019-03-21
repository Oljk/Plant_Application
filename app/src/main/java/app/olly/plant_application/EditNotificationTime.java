package app.olly.plant_application;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import app.olly.plant_application.sdata.MyDBHelper;
import app.olly.plant_application.sdata.NotificationHelper;


public class EditNotificationTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification_time);
        MyDBHelper dbhelper = new MyDBHelper(EditNotificationTime.this);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        final EditText hours = findViewById(R.id.hours);
        final EditText minutes = findViewById(R.id.minutes);

        String cur_time = MyDBHelper.CONST_TIME_DEFAULT_HOURS + MyDBHelper.CONST_TIME_SEPARATOP + MyDBHelper.CONST_TIME_DEFAULT_MINUTES;
        String query = "SELECT " + MyDBHelper.ConstsTable.COLUMN_DATA +
                " FROM " + MyDBHelper.ConstsTable.TABLE_NAME +
                " WHERE " + MyDBHelper.ConstsTable.COLUMN_NAME+ " ='" + MyDBHelper.CONST_TIME_NAME + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            cur_time = cursor.getString(cursor.getColumnIndex(MyDBHelper.ConstsTable.COLUMN_DATA));
        } else {
            dbhelper.createTime(db);
        }
        cursor.close();
        final String[] times = cur_time.split(MyDBHelper.CONST_TIME_SEPARATOP);
        if (times[0].length() < 2) times[0] = "0" + times[0];
        hours.setText(times[0]);
        if (times[1].length() < 2) times[1] = "0" + times[1];
        minutes.setText(times[1] );
        Button buttonEdit = findViewById(R.id.confirm_time);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean good = true;
                int hoursInt = 0;
                int minInt = 0;
                String alertText = "";
                if (hours.getText() != null) {
                    hoursInt = Integer.parseInt(hours.getText().toString());
                } else {
                    good = false;
                    alertText = "Wrong hours";
                }
                if (minutes.getText() != null) {
                    minInt = Integer.parseInt(minutes.getText().toString());
                } else {
                    good = false;
                    alertText += " Wrong minutes";
                }
                if (hoursInt >= 24 || hoursInt < 0) {
                    good = false;
                    alertText = "Edit hours";
                }
                if (minInt >= 60 || minInt < 0) {
                    good = false;
                    alertText += " Edit minutes";
                }
                if (good) {

                    String time = hoursInt + MyDBHelper.CONST_TIME_SEPARATOP + minInt;
                    ContentValues values = new ContentValues();
                    values.put(MyDBHelper.ConstsTable.COLUMN_DATA, time);
                   long status = db.update(MyDBHelper.ConstsTable.TABLE_NAME, values, MyDBHelper.ConstsTable.COLUMN_NAME + " = ?", new String[]{MyDBHelper.CONST_TIME_NAME});
                    boolean isEnable = false;
                   String query = "SELECT " + MyDBHelper.ConstsTable.COLUMN_DATA +
                            " FROM " + MyDBHelper.ConstsTable.TABLE_NAME +
                            " WHERE " + MyDBHelper.ConstsTable.COLUMN_NAME+ " ='" + MyDBHelper.CONST_ENABLE_NOTIFICATIONS_TITLE + "'" ;
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor.moveToNext()) {
                        isEnable = "1".equals(cursor.getString(cursor.getColumnIndex(MyDBHelper.ConstsTable.COLUMN_DATA)));
                    }
                    cursor.close();
                   if (isEnable) {
                       NotificationHelper.scheduleRepeatingRTCNotification(getApplicationContext(), hoursInt, minInt);
                      // NotificationHelper.enableBootReceiver(getApplicationContext());
                   }
                    finish();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditNotificationTime.this);
                    builder.setTitle("Alert");
                    builder.setMessage(alertText);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });
    }

}
