package com.gonext.callreminder.utils;

import com.gonext.callreminder.datalayers.model.ContactDetailModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 26-04-2017.
 */

public class StaticData {

//Add all static data here
public static int SCREEN_HEIGHT, SCREEN_WIDTH;

    public  static String AD_FILE_NAME = "adDataFile";
    public static ArrayList<ContactDetailModel> lstPhoneContact = new ArrayList<>();
    public static boolean isSavedReminder = false;
    public static boolean isNotification = false;
    public static String COMPLETECALL = "complete_call";
    public static String LAUNCHED_FROM_NOTIF = "launchedFromNotif";
    public  static  String DEVICE_UUID= "deviceUuid";

}

