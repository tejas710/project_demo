package com.gonext.callreminder.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {


    /**
     * dd MM yyyy
     *
     * @param updateDate date in string format
     * @return date format as a string
     */
    public static long getDateInLong(String updateDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(updateDate);
            return date.getTime();
        }catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * dd MM yyyy
     *
     * @param updateDate date in string format
     * @return date format as a string
     */
    public static long getDateinlongValue(String updateDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(updateDate);
            return date.getTime();
        }catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }
    public static String getDateInDayFormat(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Method to get date in dd/MM/yyyy HH:mm format
     *
     * @param time timestamp in long datatype
     * @return date format as a string
     */
    public static String getDateIn24HrsFormatInUTC(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Method to get date in dd/MM/yyyy HH:mm format
     *
     * @param time timestamp in long datatype
     * @return date format as a string
     */
    public static String getDateIn24HrsFormat(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * Method to get time from current time.
     * This method will check Months, Weeks, days, Weeks, Days,Hours, Minutes, Sec different
     * @param timeStamp end date
     * @return time different in string
     */
    public static String getDateTimeDifference(long timeStamp) {


        Calendar currentDate = Calendar.getInstance();
        long different = timeStamp - currentDate.getTimeInMillis();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = weeksInMilli * 4;

        long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        long elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String time = null;

        if (elapsedMonths>0){
            time = formatTime(elapsedMonths,elapsedWeeks," month"," week");
        }
        else {
            if (elapsedWeeks > 0) {
                time = formatTime(elapsedWeeks,elapsedDays," week"," day");
            }else {
                if (elapsedDays > 0) {
                    time = formatTime(elapsedDays, elapsedHours, " day", " hour");
                } else {
                    if (elapsedHours > 0) {
                        time = formatTime(elapsedHours, elapsedMinutes, " hour", " minute");
                    } else {
                        if (elapsedMinutes > 0) {
                            if (elapsedMinutes == 1) {
                                time = "In " + elapsedMinutes + " minute";
                            } else {
                                time = "In " + elapsedMinutes + " minutes";
                            }
                        } else {
                            if (elapsedSeconds > 0) {
                                time = "In " + elapsedSeconds + " seconds";
                            }
                        }
                    }
                }
            }

        }
        if (time == null) {
            time = "0";
        }
        return time;
    }

    /**
     * Common methos for checking single or multiple values in string and concating string
     * @param checkVar Main params
     * @param CompareVar sub params
     * @param mainString Main string
     * @param subString Sub String
     * @return String with whole text which will be displayed.
     */
    private static String formatTime(long checkVar, long CompareVar,String mainString, String subString){

        String time = "";

        if (checkVar == 1) {
            if (CompareVar>0) {
                time = "In " + checkVar + mainString + CompareVar + subString;
            }else{
                time = "In " + checkVar + subString+ " s ";
            }
        } else {
            if (CompareVar>0) {
                time = "In " + checkVar + mainString+"s " + CompareVar + subString+ "s ";
            }else{
                time = "In " + checkVar + subString+ " s ";
            }
        }
        return time;
    }



}
