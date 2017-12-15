package com.gonext.callreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;


import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.AfterCallWindowDialogActivity;
import com.gonext.callreminder.activities.CallNoteDialogActivity;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.StaticData;
import com.gonext.callreminder.utils.logger.CustomLog;

import java.util.ArrayList;

/**
 * Created by snweb on 8/19/17.
 */

public class ReceiverIncommingCall extends BroadcastReceiver {

    Context ctx;
    Intent i, intentValue;

    private SensorManager sensorManager;
    private Sensor sensor;
    public ArrayList<ReminderDataModel> lstCallNote = new ArrayList<>();
    public ArrayList<ReminderDataModel> lstCallNoteTemp = new ArrayList<>();
    public String fullScreenValue = "", incommingPhotoUri = null, incommingNumber = "", incommingName = "";
    DataBaseClass dataBaseClass;
    AppPref appPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            appPref = AppPref.getInstance(context);
            dataBaseClass = new DataBaseClass(context);

            String afterCallWindowflag = appPref.getValue(context.getString(R.string.after_call_window_type), context.getString(R.string.incoming_calls)
                    + " " + context.getString(R.string.outgoing_calls) + " " + context.getString(R.string.missed_calls));
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                incommingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                getContactName(incommingNumber, context);
                appPref.setValue(context.getString(R.string.ring), true);
                appPref.setValue(context.getString(R.string.ringcallnote), true);

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                appPref.setValue(context.getString(R.string.offhook), true);
                appPref.setValue(context.getString(R.string.offhookcallnote), true);
                if (appPref.getValue(context.getString(R.string.ringcallnote), false) && appPref.getValue(context.getString(R.string.offhookcallnote), false)) {
                    if(appPref.getValue(context.getString(R.string.incoming_calls),true))
                    {
                        lstCallNote.clear();
                        lstCallNoteTemp.clear();
                        incommingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        lstCallNote = dataBaseClass.getAllReminderData(incommingNumber);
                        for (ReminderDataModel reminderDataModel : lstCallNote) {
                            if (reminderDataModel.getIsCallNote() == 1) {
                                lstCallNoteTemp.add(reminderDataModel);
                            }
                        }
                        if (lstCallNoteTemp.size() > 0) {
                            Intent callNoteDialogIntent = new Intent(context, CallNoteDialogActivity.class);
                            callNoteDialogIntent.putExtra(context.getString(R.string.incomingnumber), incommingNumber);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(callNoteDialogIntent);
                        }
                    }

                } else if (appPref.getValue(context.getString(R.string.offhookcallnote), false)) {
                    if(appPref.getValue(context.getString(R.string.outgoing_calls),true))
                    {
                        lstCallNote.clear();
                        lstCallNoteTemp.clear();
                        incommingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        lstCallNote = dataBaseClass.getAllReminderData(incommingNumber);
                        for (ReminderDataModel reminderDataModel : lstCallNote) {
                            if (reminderDataModel.getIsCallNote() == 1) {
                                lstCallNoteTemp.add(reminderDataModel);
                            }
                        }
                        if (lstCallNoteTemp.size() > 0) {
                            Intent callNoteDialogIntent = new Intent(context, CallNoteDialogActivity.class);
                            callNoteDialogIntent.putExtra(context.getString(R.string.incomingnumber), incommingNumber);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            callNoteDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(callNoteDialogIntent);
                        }
                    }
                }
                appPref.setValue(context.getString(R.string.ringcallnote), false);
                appPref.setValue(context.getString(R.string.offhookcallnote), false);

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                sendBroadcastForImage(context);
                appPref.setValue(context.getString(R.string.idle), true);
                incommingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                getContactName(incommingNumber, context);
                if (appPref.getValue(context.getString(R.string.after_call_window), true)) {
                    if (appPref.getValue(context.getString(R.string.ring), false) && appPref.getValue(context.getString(R.string.offhook), false) && appPref.getValue(context.getString(R.string.idle), false)) {
                        if (afterCallWindowflag.contains(context.getString(R.string.incoming_calls))) {
                            navigateToAfterWindow(context);
                        }
                    } else if (appPref.getValue(context.getString(R.string.offhook), false) && appPref.getValue(context.getString(R.string.idle), false)) {
                        if (afterCallWindowflag.contains(context.getString(R.string.outgoing_calls))) {
                            navigateToAfterWindow(context);
                        }
                    } else if (appPref.getValue(context.getString(R.string.ring), false) && appPref.getValue(context.getString(R.string.idle), false)) {
                        if (afterCallWindowflag.contains(context.getString(R.string.missed_calls))) {
                            navigateToAfterWindow(context);
                        }
                    }
                }
                appPref.setValue(context.getString(R.string.ring), false);
                appPref.setValue(context.getString(R.string.offhook), false);
                appPref.setValue(context.getString(R.string.idle), false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendBroadcastForImage(Context context) {

        Intent broadcast = new Intent();
        broadcast.setAction(StaticData.COMPLETECALL);
        context.sendBroadcast(broadcast);
    }
    public void navigateToAfterWindow(Context context) {
        Intent afterWindowDialogIntent = new Intent(context, AfterCallWindowDialogActivity.class);
        afterWindowDialogIntent.putExtra(context.getString(R.string.incomingnumber), incommingNumber);
        afterWindowDialogIntent.putExtra(context.getString(R.string.incomingname), incommingName);
        afterWindowDialogIntent.putExtra(context.getString(R.string.incomingphoturi), incommingPhotoUri);
        afterWindowDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        afterWindowDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        afterWindowDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        afterWindowDialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(afterWindowDialogIntent);
    }

    public void getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_URI};

        incommingName = "unknown";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                incommingName = cursor.getString(0);
                incommingPhotoUri = cursor.getString(1);

            }
            cursor.close();
        }
    }
/*
    private void picUpCallSensor() {
        if (autoPicup.equalsIgnoreCase("true")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                for (MediaController mediaController : ((MediaSessionManager) ctx.getApplicationContext().getSystemService("media_session")).getActiveSessions(new ComponentName(ctx.getApplicationContext(), Notification.class))) {
                    if ("com.android.server.telecom".equals(mediaController.getPackageName())) {
                        mediaController.dispatchMediaButtonEvent(new KeyEvent(1, 79));
                        return;
                    }
                }
            } else {
                Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
                buttonUp.putExtra(Intent.EXTRA_KEY_EVENT,
                        new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                ctx.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
            }
        }
    }*/
}
