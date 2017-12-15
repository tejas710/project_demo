package com.gonext.callreminder.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.SnoozeDialogActivity;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.receiver.AlaramReceiver;
import com.gonext.callreminder.utils.StaticData;
import com.gonext.callreminder.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by sell-news on 6/13/17.
 */

public class NotificationReminderService extends Service {

    public BroadcastReceiver connectionReceiver;
    DataBaseClass dataBaseClass;
    RemoteViews bigView;
    AppPref appPref;
    private ArrayList<ReminderDataModel> lstReminder = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appPref = AppPref.getInstance(getApplicationContext());
        dataBaseClass = new DataBaseClass(getApplicationContext());
        bigView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.layout_notification_pannel);
        lstReminder.addAll(AlaramReceiver.lstMatchReminder);
        AlaramReceiver.lstMatchReminder.clear();
        changeUpdateDate();
        for (ReminderDataModel reminderDataModel : lstReminder) {
            setDataAndClickListener(reminderDataModel);

        }

    }

    private void setDataAndClickListener(ReminderDataModel reminderDataModel) {
        Intent snoozeIntent = new Intent(NotificationReminderService.this, SnoozeDialogActivity.class);
        snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        snoozeIntent.putExtra(getString(R.string.reminderData), reminderDataModel);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(this, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + reminderDataModel.getReminderNumber()));
        PendingIntent callPendingIntent = PendingIntent.getActivity(this, 0, callIntent, PendingIntent.FLAG_ONE_SHOT);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + reminderDataModel.getReminderNumber()));
        smsIntent.putExtra("sms_body", reminderDataModel.getReminderNote());
        PendingIntent smsPendingIntent = PendingIntent.getActivity(this, 0, smsIntent, PendingIntent.FLAG_ONE_SHOT);
        String[] reciever = new String[]{reminderDataModel.getReminderEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setPackage("com.google.android.gm");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, reciever);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_TEXT, reminderDataModel.getReminderNote());
        PendingIntent emailPendingIntent = PendingIntent.getActivity(this, 0, emailIntent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent dismissIntent = getDismissIntent(this,reminderDataModel.getReminderId());
        bigView.setOnClickPendingIntent(R.id.llSms, smsPendingIntent);
        bigView.setOnClickPendingIntent(R.id.llEmail, emailPendingIntent);
        bigView.setOnClickPendingIntent(R.id.llCall, callPendingIntent);
        bigView.setOnClickPendingIntent(R.id.llSnooze, snoozePendingIntent);
        bigView.setOnClickPendingIntent(R.id.llDone, dismissIntent);
        if (reminderDataModel.getIsCall() == 0) {
            bigView.setViewVisibility(R.id.llCall, View.GONE);
        }
        if (reminderDataModel.getIsEmail() == 0) {
            bigView.setViewVisibility(R.id.llEmail, View.GONE);
        }
        if (reminderDataModel.getIsSms() == 0) {
            bigView.setViewVisibility(R.id.llSms, View.GONE);
        }
        bigView.setTextViewText(R.id.tvName, reminderDataModel.getReminderName());
        bigView.setTextViewText(R.id.tvNote, reminderDataModel.getReminderNote());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.imgnotificationicon)
                .setCustomBigContentView(bigView);
        // now show notification..
        if (appPref.getValue(getString(R.string.reminder_tone), true)) {
            if (reminderDataModel.getIsPersistantTone() == 0)
                builder.setSound(Uri.parse(reminderDataModel.getReminderTone().substring(0, reminderDataModel.getReminderTone().indexOf(" ")))).build();
            else
                builder.setSound(Uri.parse(reminderDataModel.getReminderTone().substring(0, reminderDataModel.getReminderTone().indexOf(" "))), 1).build();

        }
        if (appPref.getValue(getString(R.string.reminder_vibration), true)) {
            builder.setVibrate(new long[]{1000, 1000}).build();
        }
        if (appPref.getValue(getString(R.string.persistent_notification), true)) {
            builder.setOngoing(true).build();
        }
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setAutoCancel(true);
        mNotifyManager.notify(reminderDataModel.getReminderId(), builder.build());
        StaticData.isNotification = true;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }


        return START_NOT_STICKY;
    }

    private PendingIntent getDismissIntent(Context context,int id) {
        Intent intent = new Intent(context, RemoveNotificationService.class);
        intent.putExtra(context.getString(R.string.shared_intent_to_remove), context.getString(R.string.shared_intent_to_remove_value));
        intent.putExtra(context.getString(R.string.notificationId),id);
        return PendingIntent.getService(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private void changeUpdateDate() {
        for (ReminderDataModel reminderDataModel : lstReminder) {
            String interval = reminderDataModel.getRepeatInterval();
            if (!interval.equals("")) {
                if (interval.equalsIgnoreCase(getString(R.string.daily))) {
                    Calendar updateDate = Calendar.getInstance();
                    updateDate.add(Calendar.DATE, 1);
                    long newReminderDate = updateDate.getTimeInMillis();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getReminderDate());
                    String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                    String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);
                    } else {
                        if (Long.parseLong(reminderDataModel.getRepeatForever()) < newReminderDate) {
                            newReminderDate = TimeUtils.getDateinlongValue(newDate);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                        }
                    }
                } else if (interval.equalsIgnoreCase(getString(R.string.weekly))) {
                    Calendar updateDate = Calendar.getInstance();
                    updateDate.add(Calendar.DATE, 7);
                    long newReminderDate = updateDate.getTimeInMillis();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getReminderDate());
                    String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                    String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);

                    } else {
                        if (Long.parseLong(reminderDataModel.getRepeatForever()) < newReminderDate) {
                            newReminderDate = TimeUtils.getDateinlongValue(newDate);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                        }
                    }
                } else if (interval.equalsIgnoreCase(getString(R.string.monthly))) {
                    Calendar updateDate = Calendar.getInstance();
                    updateDate.add(Calendar.MONTH, 1);
                    long newReminderDate = updateDate.getTimeInMillis();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getReminderDate());
                    String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                    String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);

                    } else {
                        if (Long.parseLong(reminderDataModel.getRepeatForever()) < newReminderDate) {
                            newReminderDate = TimeUtils.getDateinlongValue(newDate);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                        }
                    }
                } else if (interval.equalsIgnoreCase(getString(R.string.yearly))) {
                    Calendar updateDate = Calendar.getInstance();
                    updateDate.add(Calendar.YEAR, 1);
                    long newReminderDate = updateDate.getTimeInMillis();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getReminderDate());
                    String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                    String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);

                    } else {
                        if (Long.parseLong(reminderDataModel.getRepeatForever()) < newReminderDate) {
                            newReminderDate = TimeUtils.getDateinlongValue(newDate);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                        }
                    }
                } else {
                    Calendar updateDate = Calendar.getInstance();
                    long newReminderDate = updateDate.getTimeInMillis();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getReminderDate());
                    String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                    String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);

                    } else {
                        if (Long.parseLong(reminderDataModel.getRepeatForever()) < newReminderDate) {
                            newReminderDate = TimeUtils.getDateinlongValue(newDate);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                        }
                    }
                }

            } else {
                dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", reminderDataModel.getUpdateDate(), 1);
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
