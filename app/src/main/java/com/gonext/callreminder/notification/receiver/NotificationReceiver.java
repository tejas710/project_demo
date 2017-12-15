package com.gonext.callreminder.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gonext.callreminder.notification.service.NotificationGenerateService;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context);
    }

    /**
     * Method to start notification generate service.
     * @param context Instance on context class.
     */
    private void startService(Context context) {
        Intent service = new Intent(context, NotificationGenerateService.class);
        context.startService(service);
    }
}