package com.gonext.callreminder.datalayers.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sell-news on 2/9/17.
 */

public class SqLightDbHelper extends SQLiteOpenHelper {


    final static String DATA_BASE_NAME = "CallReminderData";
    final static int VERSION = 1;
    final static String REMINDERTABLE = "reminder_table";
    final static String REMINDERID = "id";
    final static String REMINDERNAME = "reminder_name";
    final static String REMINDERNUMBER = "reminder_number";
    final static String REMINDEREMAIL = "reminder_email";
    final static String REMINDERNOTE = "reminder_note";
    final static String REMINDERICON = "reminder_icon";
    final static String REMINDERTONE = "reminder_tone";
    final static String REMINDERDATE = "reminder_date";
    final static String REMINDERIN = "reminder_in";
    final static String UPDATE_DATE = "update_date";
    final static String REPEAT_INTERVAL = "repeat_interval";
    final static String REPEAT_FOREVER = "repeat_forever";
    final static String ISCOMPLATE = "isComplate";
    final static String ISCALL = "isCall";
    final static String ISSMS = "isSms";
    final static String ISEMAIL = "isEmail";
    final static String ISMISC = "isMisc";
    final static String ISCALLNOTE = "isCallnote";
    final static String ISCALLNOTESHOW = "isCallnoteShow";
    final static String ISPERSISTANTTONE= "persistant_tone";
    final static String ISCONFIRMMSG= "confirm_msg";
    final static String ISCALENDARSYNC= "calendar_sync";
    final static String CALENDEREVENTID= "calendar_id";
    String CREATE_QYERY_ALARM = new String();
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


    public SqLightDbHelper(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_QYERY_ALARM = "CREATE TABLE " + REMINDERTABLE + "(" + REMINDERID + " integer primary key  autoincrement,"
                + REMINDERNAME + " text," + REMINDERNUMBER + " text,"
                + REMINDERNOTE + " text," + REMINDERICON + " integer," + REMINDERDATE + " text,"
                + REMINDEREMAIL+ " text," + REMINDERTONE + " text," + REMINDERIN + " text,"
                + UPDATE_DATE + " text,"
                + REPEAT_INTERVAL + " text," + REPEAT_FOREVER + " text," + CALENDEREVENTID + " text,"
                + ISCOMPLATE + " integer,"
                + ISCALL + " integer," + ISSMS + " integer," + ISEMAIL + " integer,"
                + ISMISC + " integer," + ISCALLNOTE + " integer," + ISCALLNOTESHOW + " integer,"
                + ISPERSISTANTTONE + " integer,"+ ISCALENDARSYNC + " integer," + ISCONFIRMMSG + " integer );";

        db.execSQL(CREATE_QYERY_ALARM);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ REMINDERTABLE);
        onCreate(db);
    }
}
