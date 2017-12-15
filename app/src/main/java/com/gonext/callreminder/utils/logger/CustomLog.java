package com.gonext.callreminder.utils.logger;

import com.gonext.callreminder.BuildConfig;
import android.util.Log;

/**
 * Created by sellnews on 13/9/17.
 */

public class CustomLog {


    /**
     * Method to print verbose information
     * @param tag key
     * @param message value to be printed
     */
    public static void verbose(final String tag, final Object message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message.toString());
    }

    /**
     * Method to print warning information
     * @param tag key
     * @param message value to be printed
     */
    public static void warning(final String tag, final String message) {
        if (BuildConfig.DEBUG)
            Log.w(tag, message);
    }

    /**
     * Method to print debug information
     * @param tag key
     * @param message value to be printed
     */
    public static void debug(final String tag, final Object message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message.toString());
    }

    /**
     * Method to print info information
     * @param tag key
     * @param message value to be printed
     */

    public static void info(final String tag, final String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    /**
     * Method to print error information
     * @param tag key
     * @param message value to be printed
     */
    public static void error(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);

        }
    }

    /**
     * Method to print verbose information
     * @param tag key
     * @param message value to be printed
     */

    /**
     * Method to print error  information with throwable
     * @param tag key
     * @param message value to be printed
     * @param throwable exception throwable
     */
    public static void error(final String tag, final String message, final Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message, throwable);
    }

}
