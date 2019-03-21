package app.olly.plant_application.sdata;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.widget.CalendarView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.olly.plant_application.EditNotificationTime;
import app.olly.plant_application.MainActivity;
import app.olly.plant_application.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentToRepeat = new Intent(context, MainActivity.class);
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification repeatedNotification = buildLocalNotification(context, pendingIntent).build();

        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {
        MyDBHelper dbhelper = new MyDBHelper(context);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        ArrayList<Plant> plants = new ArrayList<>();
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
        Calendar curtime = Calendar.getInstance();
        curtime.setTime(new Date(System.currentTimeMillis()));
        Calendar c = Calendar.getInstance();
        ArrayList<Plant> notifyPlant = new ArrayList<>();
        for (Plant plant: plants ) {
            if (plant.checkNeedWater()) notifyPlant.add(plant);
        }
        String mess;
        if (notifyPlant.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Water plants: ");
            for (Plant plant : notifyPlant) {
                stringBuilder.append(plant.getName() + ", ");
            }
            mess = stringBuilder.substring(0, stringBuilder.length() - 2);
        } else  {
            mess = "No plants need water";
        }
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setContentTitle("Water time")
                        .setContentText(mess)
                        .setAutoCancel(true);

        return builder;
    }
}
