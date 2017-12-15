package com.gonext.callreminder.notification.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


import com.gonext.callreminder.notification.receiver.NotificationReceiver;

import java.util.Calendar;


public class NotificationService extends Service {
    private BroadcastReceiver alarmReceiver;
    AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        registerNotificationReceiver();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setNotificationTime();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alarmReceiver);
    }

    /**
     * Method to set notification time from calender. this method set notification time with alarm manager to repeat it.
     */
    private void setNotificationTime(){
        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calNotificationTime = setTimeInCalendarAndReturnTimeInMilliSeconds();
        Calendar calender = Calendar.getInstance();

        long nextNotificationTime  = calNotificationTime.getTimeInMillis();
        long currentTime = calender.getTimeInMillis();

        if (nextNotificationTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, nextNotificationTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            calNotificationTime.add(Calendar.DAY_OF_MONTH, 1);
            nextNotificationTime = calNotificationTime.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC, nextNotificationTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private Calendar setTimeInCalendarAndReturnTimeInMilliSeconds(){

        Calendar calNotificationTime = Calendar.getInstance();
        calNotificationTime.set(Calendar.HOUR, 9); // At the hour you wanna fire
        calNotificationTime.set(Calendar.MINUTE, 0); // Particular minute
        calNotificationTime.set(Calendar.SECOND, 0); // particular second

        return  calNotificationTime;
    }
    /**
     * Method to register notification receiver.
     */
    private void registerNotificationReceiver(){
        alarmReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        registerReceiver(alarmReceiver, intentFilter);
    }

}
