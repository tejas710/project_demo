package com.gonext.callreminder.datalayers.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.Set;


public final class AppPref {

    public static final String PREF_NAME = "user_data";
    public static final String PREF_HINT_NAME = "app_hint_data";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences sharedPreferencesHint;

    private static AppPref instance;
    private SharedPreferences.Editor edit;
    private SharedPreferences.Editor editHint;

    private AppPref(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        sharedPreferencesHint = mContext.getSharedPreferences(PREF_HINT_NAME, Activity.MODE_PRIVATE);
        editHint = sharedPreferencesHint.edit();
    }

    public static synchronized AppPref getInstance(Context context) {
        if (instance == null) {
            instance = new AppPref(context);
        }
        return instance;
    }


    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long getValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getHintValue(String key, boolean defaultValue) {
        return sharedPreferencesHint.getBoolean(key, defaultValue);
    }

    public float getValue(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> stringSet) {
        return sharedPreferences.getStringSet(key, stringSet);
    }

    //############Set values################

    public void setValue(String key, String defaultValue) {
        edit.putString(key, defaultValue).commit();
    }

    public void setValue(String key, int defaultValue) {
        edit.putInt(key, defaultValue).apply();
    }

    public void setValue(String key, long defaultValue) {
        edit.putLong(key, defaultValue).commit();

    }

    public void setValue(String key, boolean defaultValue) {
        edit.putBoolean(key, defaultValue).commit();
    }

    public void setHintValue(String key, boolean defaultValue) {
        editHint.putBoolean(key, defaultValue).commit();
    }

    public void setValue(String key, float defaultValue) {
        edit.putFloat(key, defaultValue).commit();
    }

    public void setStringSet(String key, Set<String> stringSet) {
        edit.putStringSet(key, stringSet).commit();
    }

    public void clear() {
        edit.clear().commit();
    }

}
