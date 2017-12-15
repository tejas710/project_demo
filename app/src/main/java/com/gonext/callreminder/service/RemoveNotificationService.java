package com.gonext.callreminder.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gonext.callreminder.R;


/**
 * Created by sellnews on 9/10/17.
 */

public class RemoveNotificationService extends IntentService {

    public RemoveNotificationService() {
        super("RemoveNotificationService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(getString(R.string.shared_intent_to_remove))) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(intent.getIntExtra(getString(R.string.notificationId), 0));
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
