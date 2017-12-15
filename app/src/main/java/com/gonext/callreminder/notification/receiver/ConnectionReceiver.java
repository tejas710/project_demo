package com.gonext.callreminder.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gonext.callreminder.notification.service.NotificationGenerateService;


public class ConnectionReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (isNetworkAvailable()) {
            startService(context);
        }

    }

    /**
     * Method to start notification generate service.
     * @param context Instance on context class.
     */
    private void startService(Context context) {
        Intent service = new Intent(context, NotificationGenerateService.class);
        context.startService(service);


    }

    /**
     * Method to check Current Internet connectivity.
     * @return  return true if connection available else returns false.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


}
