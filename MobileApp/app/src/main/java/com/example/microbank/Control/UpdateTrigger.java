package com.example.microbank.Control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateTrigger {
    private Context context;

    public UpdateTrigger(Context context){
        this.context = context;
    }

    public void setAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 02);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            long firstMillis = calendar.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,AlarmManager.INTERVAL_DAY, pIntent);
            Log.d("TRIGGER","Trigger is Set");
        }
    }
    public void cancelAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.cancel(pIntent);
        }
    }

}
