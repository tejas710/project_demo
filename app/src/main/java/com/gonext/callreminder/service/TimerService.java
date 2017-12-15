package com.gonext.callreminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


import com.gonext.callreminder.receiver.AlaramReceiver;

import java.util.Calendar;

public class TimerService extends Service {
    private BroadcastReceiver mScreenReceiver;
    AlarmManager alarmManager;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();


        mScreenReceiver = new AlaramReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(mScreenReceiver, intentFilter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent alaramintent = new Intent(this, AlaramReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramintent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        calender.add(Calendar.SECOND, 60);
        long frequency = 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), frequency, pendingIntent);
        return Service.START_STICKY;
    }
}
