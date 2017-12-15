package com.gonext.callreminder.notification.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;


import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.SplashActivity;
import com.gonext.callreminder.notification.receiver.ConnectionReceiver;
import com.gonext.callreminder.utils.StaticData;

import java.util.Date;
import java.util.Random;


public class NotificationGenerateService extends Service {

    public  int NOTIFICATION_ID = 0;
    public BroadcastReceiver connectionReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isNetworkAvailable()) {
            getUniqueNotificationId();
            Intent pendingIntent = createPendingIntent();
            String title = getResources().getString(R.string.app_name);
            String description = getResources().getString(R.string.notification_description);
            showNotification(title,description,pendingIntent);
            if (connectionReceiver != null) {
                unregisterReceiver(connectionReceiver);
                connectionReceiver = null;
            }
            stopSelf();
        } else {
            if (connectionReceiver == null) {
                connectionReceiver = new ConnectionReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                registerReceiver(connectionReceiver, intentFilter);
            }
        }
        return START_REDELIVER_INTENT;
    }


    /**
     * Method to check Current Internet connectivity.
     * @return  return true if connection available else returns false.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    /**
     * Add all intent flag and extra info here.
     * @return returns intent with flag and extra information.
     */
    private Intent createPendingIntent() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(StaticData.LAUNCHED_FROM_NOTIF,true); // This flag is for app analytics. please do not remove this.
        return  intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectionReceiver != null){
            unregisterReceiver(connectionReceiver);
            connectionReceiver = null;
        }

    }

    /**
     * This method will create unique id by using date time and if any exception is occur, are using random class to
     * create unique id for notification.
     * @return long unique id.
     */
    private  long getUniqueNotificationId(){
        try {
            NOTIFICATION_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            Random random = new Random();
            NOTIFICATION_ID = random.nextInt(9999 - 1000) + 10;
        }
        return NOTIFICATION_ID;
    }


    /**
     * Method to showNotification without Image/Bitmap
     * @param title notification Title
     * @param message notification extra message
     * @param intent pending intent
     */
    private void showNotification(String title, String message, Intent intent){
        showNotification(title, message, intent,"",null);
    }

    /**
     * Method to showNotification with Image/Bitmap. if dont want to show bitmap then it will be null initially
     * @param title notification Title
     * @param message notification extra message
     * @param intent pending intent
     */
    private void showNotification(String title, String message, Intent intent,String imageUrl,Bitmap bitmap){

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = ContextCompat.getColor(NotificationGenerateService.this,R.color.colorPrimary);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        if (TextUtils.isEmpty(imageUrl)) {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }else{
            if (bitmap != null) {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            }
        }
        mBuilder.setSmallIcon(R.drawable.imgnotificationicon);
        mBuilder.setContentTitle(title).setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setColor(color);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


}
