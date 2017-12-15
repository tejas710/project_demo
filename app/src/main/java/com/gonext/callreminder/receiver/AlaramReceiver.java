package com.gonext.callreminder.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.service.NotificationReminderService;
import com.gonext.callreminder.service.TimerService;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AlaramReceiver extends BroadcastReceiver {
    DataBaseClass dataBaseClass;
    private ArrayList<ReminderDataModel> lstReminder;

    public static ArrayList<ReminderDataModel> lstMatchReminder;
    long currentTime;
    AlarmManager alarmMgr;
    AppPref appPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendnotification(context);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent intent1 = new Intent(context, TimerService.class);
            context.startService(intent1);

        }
    }


    private void sendnotification(Context context) {
        lstReminder = new ArrayList<>();

        lstMatchReminder = new ArrayList<>();
        dataBaseClass = new DataBaseClass(context);
        lstReminder = dataBaseClass.getAllReminderData();
        String val = "";
        appPref = AppPref.getInstance(context);

        Calendar current = Calendar.getInstance();
        currentTime = System.currentTimeMillis();
        String currentDay = String.valueOf(current.getTime().getDay());
        long currentDate = current.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (ReminderDataModel reminderDataModel : lstReminder) {
            if (reminderDataModel.getIsComplete() == 0) {
                if (reminderDataModel.getIsCall() == 1 || reminderDataModel.getIsSms() == 1 || reminderDataModel.getIsEmail() == 1 || reminderDataModel.getIsMisc() == 1) {

                    val = "";
                    if (!reminderDataModel.getRepeatInterval().equals("")) {
                        if (reminderDataModel.getRepeatInterval().equalsIgnoreCase("Daily")) {
                            val = "D";
                        } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase("Weekly")) {
                            val = "W";
                        } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase("Monthly")) {
                            val = "M";
                        } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase("Yearly")) {
                            val = "Y";
                        } else {
                            for (String str : reminderDataModel.getRepeatInterval().split(",")) {
                                switch (str) {
                                    case "SU":
                                        if (val.equals("")) {
                                            val = "0";
                                        } else {
                                            val = val + ",0";
                                        }
                                        break;
                                    case "M":
                                        if (val.equals("")) {
                                            val = "1";
                                        } else {
                                            val = val + ",1";
                                        }
                                        break;
                                    case "T":
                                        if (val.equals("")) {
                                            val = "2";
                                        } else {
                                            val = val + ",2";
                                        }
                                        break;
                                    case "W":
                                        if (val.equals("")) {
                                            val = "3";
                                        } else {
                                            val = val + ",3";
                                        }
                                        break;
                                    case "TH":
                                        if (val.equals("")) {
                                            val = "4";
                                        } else {
                                            val = val + ",4";
                                        }
                                        break;
                                    case "F":
                                        if (val.equals("")) {
                                            val = "5";
                                        } else {
                                            val = val + ",5";
                                        }
                                        break;
                                    case "S":
                                        if (val.equals("")) {
                                            val = "6";
                                        } else {
                                            val = val + ",6";
                                        }
                                        break;
                                    case "":
                                        val = "";
                                        break;

                                }
                            }
                        }
                    } else {
                        val = "D";
                    }
                }
                if (val.equals("D")) {
                    long reminderDate = reminderDataModel.getUpdateDate();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDate);
                    String currentDat = TimeUtils.getDateIn24HrsFormat(currentDate);
                    Date date1 = null, date2 = null;
                    try {
                        date1 = dateFormat.parse(reminder);
                        date2 = dateFormat.parse(currentDat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        String newDate = currentDat.substring(0, currentDat.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                        long newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        reminderDataModel.setUpdateDate(newReminderDate);
                        dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);

                    }

                    if (TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate()).equals(TimeUtils.getDateIn24HrsFormat(currentDate))) {
                        //CheckSetTime();
                        lstMatchReminder.add(reminderDataModel);
                    }
                }
                if (val.equals("W")) {
                    if (TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate()).equals(TimeUtils.getDateIn24HrsFormat(currentDate))) {
                        //CheckSetTime();
                        lstMatchReminder.add(reminderDataModel);
                    }
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(dateFormat.parse(TimeUtils.getDateIn24HrsFormat(calendar.getTimeInMillis())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long reminderDate = reminderDataModel.getUpdateDate();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDate);
                    String currentDat = TimeUtils.getDateIn24HrsFormat(currentDate);
                    Date date1 = null, date2 = null;
                    try {
                        date1 = dateFormat.parse(reminder);
                        date2 = dateFormat.parse(currentDat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        calendar.add(Calendar.DATE, 7);
                        long newReminderDate = calendar.getTimeInMillis();
                        String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                        String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        reminderDataModel.setUpdateDate(newReminderDate);

                        if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever"))
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);
                        else {
                            if (Long.parseLong(reminderDataModel.getRepeatForever()) < currentDate) {
                                dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                            }
                        }
                    }
                }
                if (val.equals("M")) {
                    if (TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate()).equals(TimeUtils.getDateIn24HrsFormat(currentDate))) {
                        //CheckSetTime();
                        lstMatchReminder.add(reminderDataModel);
                    }
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(dateFormat.parse(TimeUtils.getDateIn24HrsFormat(calendar.getTimeInMillis())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long reminderDate = reminderDataModel.getUpdateDate();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDate);
                    String currentDat = TimeUtils.getDateIn24HrsFormat(currentDate);
                    Date date1 = null, date2 = null;
                    try {
                        date1 = dateFormat.parse(reminder);
                        date2 = dateFormat.parse(currentDat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        calendar.add(Calendar.MONTH, 1);
                        long newReminderDate = calendar.getTimeInMillis();
                        String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                        String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        reminderDataModel.setUpdateDate(newReminderDate);

                        if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever"))
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);
                        else {
                            if (Long.parseLong(reminderDataModel.getRepeatForever()) < currentDate) {
                                dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                            }
                        }
                    }
                }
                if (val.equals("Y")) {
                    if (TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate()).equals(TimeUtils.getDateIn24HrsFormat(currentDate))) {
                        //CheckSetTime();
                        lstMatchReminder.add(reminderDataModel);
                    }
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(dateFormat.parse(TimeUtils.getDateIn24HrsFormat(calendar.getTimeInMillis())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long reminderDate = reminderDataModel.getUpdateDate();
                    String reminder = TimeUtils.getDateIn24HrsFormat(reminderDate);
                    String currentDat = TimeUtils.getDateIn24HrsFormat(currentDate);
                    Date date1 = null, date2 = null;
                    try {
                        date1 = dateFormat.parse(reminder);
                        date2 = dateFormat.parse(currentDat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1.compareTo(date2) < 0) {
                        calendar.add(Calendar.YEAR, 1);
                        long newReminderDate = calendar.getTimeInMillis();
                        String newReminderDateValue = TimeUtils.getDateIn24HrsFormat(newReminderDate);
                        String newDate = newReminderDateValue.substring(0, newReminderDateValue.indexOf(" ")) + " " + reminder.substring(reminder.indexOf(" ") + 1);
                        newReminderDate = TimeUtils.getDateinlongValue(newDate);
                        reminderDataModel.setUpdateDate(newReminderDate);

                        if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever"))
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 0);
                        else {
                            if (Long.parseLong(reminderDataModel.getRepeatForever()) < currentDate) {
                                dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", newReminderDate, 1);
                            }
                        }
                    }
                }

                if (val.contains(currentDay)) {
                    String cureentDat = TimeUtils.getDateIn24HrsFormat(currentDate);
                    String reminderDate = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate());
                    if (reminderDataModel.getRepeatForever().equalsIgnoreCase("forever")) {
                        if ((reminderDate.substring(reminderDate.indexOf(" ") + 1)).equals(cureentDat.substring(cureentDat.indexOf(" ") + 1))) {
                            lstMatchReminder.add(reminderDataModel);
                        }
                    } else {

                        if (currentDate < Long.parseLong(reminderDataModel.getRepeatForever())) {
                            if ((reminderDate.substring(reminderDate.indexOf(" ") + 1)).equals(cureentDat.substring(cureentDat.indexOf(" ") + 1))) {
                                lstMatchReminder.add(reminderDataModel);
                            }
                        } else {
                            String newDate = cureentDat.substring(0, cureentDat.indexOf(" ")) + " " + reminderDate.substring(reminderDate.indexOf(" ") + 1);
                            dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", TimeUtils.getDateinlongValue(newDate), 1);

                        }
                    }

                }


                if (lstMatchReminder.size() > 0) {
                    initaiallizedata(context);
                }
            }
        }

    }

    private void initaiallizedata(Context context) {
        Intent service = new Intent(context, NotificationReminderService.class);
        context.startService(service);


    }
}
