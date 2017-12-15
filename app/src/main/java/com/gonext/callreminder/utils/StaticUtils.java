package com.gonext.callreminder.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.BuildConfig;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gonext.callreminder.datalayers.model.ContactDetailModel;
import com.gonext.callreminder.utils.logger.CustomLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Lenovo.
 */

public class StaticUtils {


    public static ArrayList<ContactDetailModel> getAllDeviceContacts(Context context) {
        ArrayList<ContactDetailModel> lstContact = new ArrayList<>();


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.PHOTO_URI};

        Cursor people = context.getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int indexPhoto = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        HashSet<String> numberMap = new HashSet<>();
        if (people.moveToFirst()) {
            do {
                String name = people.getString(indexName);
                String number = people.getString(indexNumber);
                String photo = people.getString(indexPhoto);
                if (numberMap.add(number.replace(" ", ""))) {
                    ContactDetailModel contactDetailModel = new ContactDetailModel();
                    contactDetailModel.setContactName(name);
                    contactDetailModel.setContactNumber(number.replace(" ", ""));
                    contactDetailModel.setContactImage(photo);
                    lstContact.add(contactDetailModel);
                }
            } while (people.moveToNext());
        }
        return lstContact;
    }
    /**
     * Method to check internet connection.
     *
     * @param context context
     * @return true/false
     */
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = null;
            try {
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectivityManager.getAllNetworks();
                    NetworkInfo networkInfo;
                    for (Network mNetwork : networks) {
                        networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                        if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            return true;
                        }
                    }
                } else {

                    //noinspection deprecation
                    NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                CustomLog.info("Utils", "isConnectingToInternet: NETWORKNAME=" + anInfo.getTypeName());
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }
    /**
     * Method to navigate playstore with user entered package name
     *
     * @param context     context of activity as well as frgament
     * @param packageName package name what will navigate in playstore based on this package name
     */
    public static void navigateToPlayStoreWithSpecificPackage(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName + "&hl=en");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            } else {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check app is currently running or not
     *
     * @param context context
     * @return  returns true if app is running in background else false
     */
    public static boolean isAppRunning(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    /**
     * This method is used set window dimensions
     *
     * @param context context of current screen.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void setWindowDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        StaticData.SCREEN_WIDTH = size.x;
        StaticData.SCREEN_HEIGHT = size.y;
    }

    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     */
    public static void hideKeyboard(FragmentActivity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    public static void showKeyboard(FragmentActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    public static void showKeyboard(FragmentActivity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     * @param view     instance of view
     */
    public static void hideKeyboard(FragmentActivity activity, View view) {
        try {
            if (activity != null && view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            CustomLog.info("KeyBoardUtil", e.toString());
        }
    }

    /**
     * method to get device mac address
     *
     * @return string of device mac address
     */
    public static String getDveviceMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Method to code text in clip board
     *
     * @param context context
     * @param text    text what wan to copy in clipboard
     * @param label   label what want to copied
     */
    public static void copyCodeInClipBoard(Context context, String text, String label) {
        if (context != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);

        }
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                Locale loc = new Locale("", simCountry.toLowerCase(Locale.US));

                return loc.getDisplayCountry();
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    Locale loc = new Locale("", networkCountry.toLowerCase(Locale.US));
                    return loc.getDisplayCountry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get user's current conuntry. if user has not simcard detected then will return country from device.
     *
     * @param context context
     * @return device's current country name
     */
    public static String getUserDeviceCountry(Context context) {

        String userCountry = getUserCountry(context);

        if (userCountry == null) {
            userCountry = context.getResources().getConfiguration().locale.getDisplayCountry();
        }
        return userCountry;
    }

    /**
     * Method to check third party application is installed in device or not.
     *
     * @param activity    Instance on the activity
     * @param packageName Third party application package name to chaek is it available in device.
     * @return true if requested package name is installed in device alfe false
     */
    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static HashMap<String, Object> getDeviceInfo() {
        HashMap<String, Object> mDeviceInfoParams = new HashMap<>();
        try {
            mDeviceInfoParams.put("ManuFacturer", Build.MANUFACTURER);
            mDeviceInfoParams.put("Serial", Build.SERIAL);
            mDeviceInfoParams.put("Model", Build.MODEL);
            mDeviceInfoParams.put("Id", Build.ID);
            mDeviceInfoParams.put("Brand", Build.BRAND);
            mDeviceInfoParams.put("Type", Build.TYPE);
            mDeviceInfoParams.put("Incremental", Build.VERSION.INCREMENTAL);
            mDeviceInfoParams.put("Board", Build.BOARD);
            mDeviceInfoParams.put("Host", Build.HOST);
            mDeviceInfoParams.put("VersionCode", Build.VERSION.RELEASE);
            mDeviceInfoParams.put("AppVersion", BuildConfig.VERSION_NAME);
            mDeviceInfoParams.put("Display", Build.DISPLAY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mDeviceInfoParams;
    }

    public static JSONObject getDeviceInfoInJSON() {
        JSONObject deviceInfoJObject = new JSONObject();
        try {
            deviceInfoJObject.put("ManuFacturer", Build.MANUFACTURER);
            deviceInfoJObject.put("Serial", Build.SERIAL);
            deviceInfoJObject.put("Model", Build.MODEL);
            deviceInfoJObject.put("Id", Build.ID);
            deviceInfoJObject.put("Brand", Build.BRAND);
            deviceInfoJObject.put("Type", Build.TYPE);
            deviceInfoJObject.put("Incremental", Build.VERSION.INCREMENTAL);
            deviceInfoJObject.put("Board", Build.BOARD);
            deviceInfoJObject.put("Host", Build.HOST);
            deviceInfoJObject.put("VersionCode", Build.VERSION.RELEASE);
            deviceInfoJObject.put("AppVersion", BuildConfig.VERSION_NAME);
            deviceInfoJObject.put("Display", Build.DISPLAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfoJObject;
    }

    /**
     * Method to check runnable device is virtual device ot not.
     * @return true if it is virtual device else false.
     */
    public static boolean isRunningAppInEmulator() {
        try {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.MANUFACTURER.contains("unknown")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || Build.PRODUCT.equals("google_sdk")
                    || Build.PRODUCT.equals("sdk_google")
                    || Build.HARDWARE.contains("golfdish");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to rate application
     * @param context context of activity
     */
    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }else{
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    /**
     * Method to share application
     * @param context  context of activity
     * @param shareText text to be shared with app link
     */
    public static void shareApp(Context context,String shareText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText+ "\n\n" +
                "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share"));
    }

    public static long addRemindersToCalender(Context context,String title,
                                              String desc, int status, long startDate,
                                              boolean needReminder) {
        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title);
            eventValues.put("description", desc);


            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);

            // values.put("allDay", 1); //If it is bithday alarm or such
            // kind (which should remind me for whole day) 0 for false, 1
            // for true
            eventValues.put("eventStatus", status); // This information is
            // sufficient for most
            // entries tentative (0),
            // confirmed (1) or canceled
            // (2):
            eventValues.put("eventTimezone", "UTC/GMT +5:30");
            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true
            Uri eventUri = context.getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());

            if (needReminder) {
                /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                reminderValues.put("minutes", 2); // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                Uri reminderUri = context.getContentResolver()
                        .insert(Uri.parse(reminderUriString), reminderValues);
            }

        } catch (Exception ex) {
            System.out.print("Error in adding event on calendar" + ex.getMessage());
        }

        return eventID;

    }
   /* public static int upDateRemindersToCalender(String calenderId,Context context,String title,
                                              String desc, int status, long startDate,
                                              boolean needReminder) {
        int event = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title);
            eventValues.put("description", desc);


            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);

            // values.put("allDay", 1); //If it is bithday alarm or such
            // kind (which should remind me for whole day) 0 for false, 1
            // for true
            eventValues.put("eventStatus", status); // This information is
            // sufficient for most
            // entries tentative (0),
            // confirmed (1) or canceled
            // (2):
            eventValues.put("eventTimezone", "UTC/GMT +5:30");
            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

            Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, Long.parseLong(calenderId));
            int rows = context.getContentResolver().update(updateUri, eventValues, null, null);


            // eventID = Long.parseLong(eventUri.getLastPathSegment());

            if (needReminder) {
                *//***************** Event: Reminder(with alert) Adding reminder to event ***********        ********//*

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", Long.parseLong(calenderId));
                reminderValues.put("minutes", 2); // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                Uri reminderUri = context.getContentResolver()
                        .insert(Uri.parse(reminderUriString), reminderValues);
            }

        } catch (Exception ex) {
            System.out.print("Error in adding event on calendar" + ex.getMessage());
        }

        return event;

    }*/

    public static void deleteRemindersToCalendar(Context context,String eventId)
    {
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://com.android.calendar/events"), Integer.parseInt(eventId));
        context.getContentResolver().delete(uri, null, null);
    }
}
