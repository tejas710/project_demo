package com.gonext.callreminder.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sellnews on 9/28/17.
 */

public class ReminderDataModel implements Parcelable {
    String  reminderName, reminderNumber,reminderIn, reminderTone,reminderIcon , reminderEmail, reminderNote, repeatInterval,repeatForever;
    int isComplete,isPersistantTone,reminderId,isCall,isSms,isEmail,isMisc,isCallNote,isCallNoteShow,isCalenderSync,isConfirmMsg;
    long  reminderDate;
    long updateDate;
    long calendarId;
    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }



    public ReminderDataModel() {
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderNumber() {
        return reminderNumber;
    }

    public void setReminderNumber(String reminderNumber) {
        this.reminderNumber = reminderNumber;
    }

    public String getReminderIn() {
        return reminderIn;
    }

    public void setReminderIn(String reminderIn) {
        this.reminderIn = reminderIn;
    }

    public String getReminderTone() {
        return reminderTone;
    }

    public void setReminderTone(String reminderTone) {
        this.reminderTone = reminderTone;
    }

    public String getReminderIcon() {
        return reminderIcon;
    }

    public void setReminderIcon(String reminderIcon) {
        this.reminderIcon = reminderIcon;
    }

    public String getReminderEmail() {
        return reminderEmail;
    }

    public void setReminderEmail(String reminderEmail) {
        this.reminderEmail = reminderEmail;
    }

    public String getReminderNote() {
        return reminderNote;
    }

    public void setReminderNote(String reminderNote) {
        this.reminderNote = reminderNote;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getRepeatForever() {
        return repeatForever;
    }

    public void setRepeatForever(String repeatForever) {
        this.repeatForever = repeatForever;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public int getIsPersistantTone() {
        return isPersistantTone;
    }

    public void setIsPersistantTone(int isPersistantTone) {
        this.isPersistantTone = isPersistantTone;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getIsCall() {
        return isCall;
    }

    public void setIsCall(int isCall) {
        this.isCall = isCall;
    }

    public int getIsSms() {
        return isSms;
    }

    public void setIsSms(int isSms) {
        this.isSms = isSms;
    }

    public int getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(int isEmail) {
        this.isEmail = isEmail;
    }

    public int getIsMisc() {
        return isMisc;
    }

    public void setIsMisc(int isMisc) {
        this.isMisc = isMisc;
    }

    public int getIsCallNote() {
        return isCallNote;
    }

    public void setIsCallNote(int isCallNote) {
        this.isCallNote = isCallNote;
    }

    public int getIsCallNoteShow() {
        return isCallNoteShow;
    }

    public void setIsCallNoteShow(int isCallNoteShow) {
        this.isCallNoteShow = isCallNoteShow;
    }

    public int getIsCalenderSync() {
        return isCalenderSync;
    }

    public void setIsCalenderSync(int isCalenderSync) {
        this.isCalenderSync = isCalenderSync;
    }

    public int getIsConfirmMsg() {
        return isConfirmMsg;
    }

    public void setIsConfirmMsg(int isConfirmMsg) {
        this.isConfirmMsg = isConfirmMsg;
    }

    public long getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reminderName);
        dest.writeString(this.reminderNumber);
        dest.writeString(this.reminderIn);
        dest.writeString(this.reminderTone);
        dest.writeString(this.reminderIcon);
        dest.writeString(this.reminderEmail);
        dest.writeString(this.reminderNote);
        dest.writeString(this.repeatInterval);
        dest.writeString(this.repeatForever);
        dest.writeInt(this.isComplete);
        dest.writeInt(this.isPersistantTone);
        dest.writeInt(this.reminderId);
        dest.writeInt(this.isCall);
        dest.writeInt(this.isSms);
        dest.writeInt(this.isEmail);
        dest.writeInt(this.isMisc);
        dest.writeInt(this.isCallNote);
        dest.writeInt(this.isCallNoteShow);
        dest.writeInt(this.isCalenderSync);
        dest.writeInt(this.isConfirmMsg);
        dest.writeLong(this.reminderDate);
        dest.writeLong(this.updateDate);
        dest.writeLong(this.calendarId);
    }

    protected ReminderDataModel(Parcel in) {
        this.reminderName = in.readString();
        this.reminderNumber = in.readString();
        this.reminderIn = in.readString();
        this.reminderTone = in.readString();
        this.reminderIcon = in.readString();
        this.reminderEmail = in.readString();
        this.reminderNote = in.readString();
        this.repeatInterval = in.readString();
        this.repeatForever = in.readString();
        this.isComplete = in.readInt();
        this.isPersistantTone = in.readInt();
        this.reminderId = in.readInt();
        this.isCall = in.readInt();
        this.isSms = in.readInt();
        this.isEmail = in.readInt();
        this.isMisc = in.readInt();
        this.isCallNote = in.readInt();
        this.isCallNoteShow = in.readInt();
        this.isCalenderSync = in.readInt();
        this.isConfirmMsg = in.readInt();
        this.reminderDate = in.readLong();
        this.updateDate = in.readLong();
        this.calendarId = in.readLong();
    }

    public static final Parcelable.Creator<ReminderDataModel> CREATOR = new Parcelable.Creator<ReminderDataModel>() {
        @Override
        public ReminderDataModel createFromParcel(Parcel source) {
            return new ReminderDataModel(source);
        }

        @Override
        public ReminderDataModel[] newArray(int size) {
            return new ReminderDataModel[size];
        }
    };
}