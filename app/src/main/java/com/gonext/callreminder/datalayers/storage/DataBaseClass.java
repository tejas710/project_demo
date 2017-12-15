package com.gonext.callreminder.datalayers.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gonext.callreminder.datalayers.model.ReminderDataModel;

import java.util.ArrayList;
public class DataBaseClass extends SqLightDbHelper {
    public DataBaseClass(Context context) {
        super(context);
    }
    public void insertCallReminder(ReminderDataModel reminderDataModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDERNAME, reminderDataModel.getReminderName());
        contentValues.put(REMINDERNUMBER, reminderDataModel.getReminderNumber());
        contentValues.put(REMINDEREMAIL, reminderDataModel.getReminderEmail());
        contentValues.put(REMINDERTONE, reminderDataModel.getReminderTone());
        contentValues.put(REMINDERICON, reminderDataModel.getReminderIcon());
        contentValues.put(REMINDERNOTE, reminderDataModel.getReminderNote());
        contentValues.put(REMINDERDATE, String.valueOf(reminderDataModel.getReminderDate()));
        contentValues.put(UPDATE_DATE, String.valueOf(reminderDataModel.getUpdateDate()));
        contentValues.put(CALENDEREVENTID, String.valueOf(reminderDataModel.getCalendarId()));
        contentValues.put(REMINDERIN, reminderDataModel.getReminderIn());
        contentValues.put(REPEAT_INTERVAL, reminderDataModel.getRepeatInterval());
        contentValues.put(REPEAT_FOREVER, reminderDataModel.getRepeatForever());
        contentValues.put(ISCOMPLATE, reminderDataModel.getIsComplete());
        contentValues.put(ISCALL, reminderDataModel.getIsCall());
        contentValues.put(ISSMS, reminderDataModel.getIsSms());
        contentValues.put(ISEMAIL, reminderDataModel.getIsEmail());
        contentValues.put(ISCALLNOTE, reminderDataModel.getIsCallNote());
        contentValues.put(ISCALLNOTESHOW, reminderDataModel.getIsCallNoteShow());
        contentValues.put(ISMISC, reminderDataModel.getIsMisc());
        contentValues.put(ISCONFIRMMSG, reminderDataModel.getIsConfirmMsg());
        contentValues.put(ISCALENDARSYNC, reminderDataModel.getIsCalenderSync());
        contentValues.put(ISPERSISTANTTONE, reminderDataModel.getIsPersistantTone());

        sqLiteDatabase.insert(REMINDERTABLE, null, contentValues);
    }



    public ArrayList<ReminderDataModel> getAllReminderData()

    {
        ArrayList<ReminderDataModel> lstReminder = new ArrayList<ReminderDataModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + REMINDERTABLE, null);
        if (cursor.moveToLast())

        {
            do {
                ReminderDataModel reminderDataModel = new ReminderDataModel();
                reminderDataModel.setReminderId(cursor.getInt(cursor.getColumnIndex(REMINDERID)));
                reminderDataModel.setReminderName(cursor.getString(cursor.getColumnIndex(REMINDERNAME)));
                reminderDataModel.setReminderNumber(cursor.getString(cursor.getColumnIndex(REMINDERNUMBER)));
                reminderDataModel.setReminderEmail(cursor.getString(cursor.getColumnIndex(REMINDEREMAIL)));
                reminderDataModel.setReminderDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(REMINDERDATE))));
                reminderDataModel.setUpdateDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(UPDATE_DATE))));
                reminderDataModel.setCalendarId(Long.parseLong(cursor.getString(cursor.getColumnIndex(CALENDEREVENTID))));
                reminderDataModel.setRepeatForever(cursor.getString(cursor.getColumnIndex(REPEAT_FOREVER)));
                reminderDataModel.setRepeatInterval(cursor.getString(cursor.getColumnIndex(REPEAT_INTERVAL)));
                reminderDataModel.setReminderTone(cursor.getString(cursor.getColumnIndex(REMINDERTONE)));
                reminderDataModel.setIsCall(cursor.getInt(cursor.getColumnIndex(ISCALL)));
                reminderDataModel.setIsCallNote(cursor.getInt(cursor.getColumnIndex(ISCALLNOTE)));
                reminderDataModel.setIsMisc(cursor.getInt(cursor.getColumnIndex(ISMISC)));
                reminderDataModel.setIsSms(cursor.getInt(cursor.getColumnIndex(ISSMS)));
                reminderDataModel.setIsCallNoteShow(cursor.getInt(cursor.getColumnIndex(ISCALLNOTESHOW)));
                reminderDataModel.setIsEmail(cursor.getInt(cursor.getColumnIndex(ISEMAIL)));
                reminderDataModel.setIsComplete(cursor.getInt(cursor.getColumnIndex(ISCOMPLATE)));
                reminderDataModel.setReminderIn(cursor.getString(cursor.getColumnIndex(REMINDERIN)));
                reminderDataModel.setIsPersistantTone(cursor.getInt(cursor.getColumnIndex(ISPERSISTANTTONE)));
                reminderDataModel.setReminderIcon(cursor.getString(cursor.getColumnIndex(REMINDERICON)));
                reminderDataModel.setReminderNote(cursor.getString(cursor.getColumnIndex(REMINDERNOTE)));
                reminderDataModel.setIsCalenderSync(cursor.getInt(cursor.getColumnIndex(ISCALENDARSYNC)));
                reminderDataModel.setIsConfirmMsg(cursor.getInt(cursor.getColumnIndex(ISCONFIRMMSG)));
                lstReminder.add(reminderDataModel);
            }
            while (cursor.moveToPrevious());

        }
        return lstReminder;

    }


    public ArrayList<ReminderDataModel> getAllReminderData(String number)

    {
        ArrayList<ReminderDataModel> lstReminder = new ArrayList<ReminderDataModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + REMINDERTABLE + " where " + REMINDERNUMBER + "='" + number + "'", null);
        if (cursor.moveToLast())

        {
            do {
                ReminderDataModel reminderDataModel = new ReminderDataModel();
                reminderDataModel.setReminderId(cursor.getInt(cursor.getColumnIndex(REMINDERID)));
                reminderDataModel.setReminderName(cursor.getString(cursor.getColumnIndex(REMINDERNAME)));
                reminderDataModel.setReminderNumber(cursor.getString(cursor.getColumnIndex(REMINDERNUMBER)));
                reminderDataModel.setReminderEmail(cursor.getString(cursor.getColumnIndex(REMINDEREMAIL)));
                reminderDataModel.setReminderDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(REMINDERDATE))));
                reminderDataModel.setUpdateDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(UPDATE_DATE))));
                reminderDataModel.setCalendarId(Long.parseLong(cursor.getString(cursor.getColumnIndex(CALENDEREVENTID))));
                reminderDataModel.setRepeatForever(cursor.getString(cursor.getColumnIndex(REPEAT_FOREVER)));
                reminderDataModel.setRepeatInterval(cursor.getString(cursor.getColumnIndex(REPEAT_INTERVAL)));
                reminderDataModel.setReminderTone(cursor.getString(cursor.getColumnIndex(REMINDERTONE)));
                reminderDataModel.setIsCall(cursor.getInt(cursor.getColumnIndex(ISCALL)));
                reminderDataModel.setIsCallNote(cursor.getInt(cursor.getColumnIndex(ISCALLNOTE)));
                reminderDataModel.setIsMisc(cursor.getInt(cursor.getColumnIndex(ISMISC)));
                reminderDataModel.setIsSms(cursor.getInt(cursor.getColumnIndex(ISSMS)));
                reminderDataModel.setIsCallNoteShow(cursor.getInt(cursor.getColumnIndex(ISCALLNOTESHOW)));
                reminderDataModel.setIsEmail(cursor.getInt(cursor.getColumnIndex(ISEMAIL)));
                reminderDataModel.setIsComplete(cursor.getInt(cursor.getColumnIndex(ISCOMPLATE)));
                reminderDataModel.setReminderIn(cursor.getString(cursor.getColumnIndex(REMINDERIN)));
                reminderDataModel.setIsPersistantTone(cursor.getInt(cursor.getColumnIndex(ISPERSISTANTTONE)));
                reminderDataModel.setReminderIcon(cursor.getString(cursor.getColumnIndex(REMINDERICON)));
                reminderDataModel.setReminderNote(cursor.getString(cursor.getColumnIndex(REMINDERNOTE)));
                reminderDataModel.setIsCalenderSync(cursor.getInt(cursor.getColumnIndex(ISCALENDARSYNC)));
                reminderDataModel.setIsConfirmMsg(cursor.getInt(cursor.getColumnIndex(ISCONFIRMMSG)));
                lstReminder.add(reminderDataModel);
            }
            while (cursor.moveToPrevious());

        }
        return lstReminder;

    }
    public boolean updateCallReminder(ReminderDataModel reminderDataModel) {
        ContentValues contentValues = new ContentValues();
        String ids[] = new String[]{String.valueOf(reminderDataModel.getReminderId())};
        contentValues.put(REMINDERNAME, reminderDataModel.getReminderName());
        contentValues.put(REMINDERNUMBER, reminderDataModel.getReminderNumber());
        contentValues.put(REMINDEREMAIL, reminderDataModel.getReminderEmail());
        contentValues.put(REMINDERTONE, reminderDataModel.getReminderTone());
        contentValues.put(REMINDERICON, reminderDataModel.getReminderIcon());
        contentValues.put(REMINDERNOTE, reminderDataModel.getReminderNote());
        contentValues.put(REMINDERDATE, String.valueOf(reminderDataModel.getReminderDate()));
        contentValues.put(UPDATE_DATE, String.valueOf(reminderDataModel.getUpdateDate()));
        contentValues.put(CALENDEREVENTID, String.valueOf(reminderDataModel.getCalendarId()));
        contentValues.put(REMINDERIN, reminderDataModel.getReminderIn());
        contentValues.put(REPEAT_INTERVAL, reminderDataModel.getRepeatInterval());
        contentValues.put(REPEAT_FOREVER, reminderDataModel.getRepeatForever());
        contentValues.put(ISCOMPLATE, reminderDataModel.getIsComplete());
        contentValues.put(ISCALL, reminderDataModel.getIsCall());
        contentValues.put(ISSMS, reminderDataModel.getIsSms());
        contentValues.put(ISEMAIL, reminderDataModel.getIsEmail());
        contentValues.put(ISCALLNOTE, reminderDataModel.getIsCallNote());
        contentValues.put(ISCALLNOTESHOW, reminderDataModel.getIsCallNoteShow());
        contentValues.put(ISMISC, reminderDataModel.getIsMisc());
        contentValues.put(ISCONFIRMMSG, reminderDataModel.getIsConfirmMsg());
        contentValues.put(ISCALENDARSYNC, reminderDataModel.getIsCalenderSync());
        contentValues.put(ISPERSISTANTTONE, reminderDataModel.getIsPersistantTone());
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }



    public void deleteCallReminder(String id) {
        String ids[] = new String[]{id};
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.delete(REMINDERTABLE, wherecluses, ids);
    }




    public boolean updateIsActive(String id, int isActive) {

        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(ISCOMPLATE, isActive);
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }

    public boolean deleteTempReminder(String id, int isActive) {

        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
       /* contentValues.put(ISCOMPLATE, isActive);*/
        contentValues.put(ISCALL, isActive);
        contentValues.put(ISMISC, isActive);
        contentValues.put(ISSMS, isActive);
        contentValues.put(ISEMAIL, isActive);
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }
    public boolean deleteTempCallNote(String id, int isActive) {

        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(ISCALLNOTE, isActive);
        contentValues.put(ISCALLNOTESHOW, isActive);
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }
    public boolean updateIsCallNoteEnable(String id, int isActive) {

        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(ISCALLNOTE, isActive);
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }

    public boolean updateIsComplete(String id, long UpdateDate, int isComplete) {

        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATE_DATE, String.valueOf(UpdateDate));
        contentValues.put(ISCOMPLATE, isComplete);
        String wherecluses = REMINDERID + "=?";
        sqLiteDatabase.update(REMINDERTABLE, contentValues, wherecluses, ids);
        return true;
    }



}
