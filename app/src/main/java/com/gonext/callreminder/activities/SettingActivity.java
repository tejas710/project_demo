package com.gonext.callreminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.interfaces.RemindInSelection;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvDefaultReminderValue)
    CustomTextView tvDefaultReminderValue;
    @BindView(R.id.llDefaultReminderTime)
    CardView llSnooze;
    @BindView(R.id.llNotificationSetting)
    CardView llNotificationSetting;
    @BindView(R.id.llAfterCallWindow)
    CardView llAfterCallWindow;
    @BindView(R.id.llCallNotesSettings)
    CardView llCallNotesSettings;
    @BindView(R.id.llShareApp)
    CardView llShareApp;
    @BindView(R.id.llPrivacy)
    CardView llPrivacy;
    String defaultReminderTime = "";
    AppPref appPref;
    @BindView(R.id.switchCalendarSync)
    Switch switchCalendarSync;
    @BindView(R.id.llCalendarAdd)
    CardView llCalendarAdd;
    boolean isCalendarSync;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AdUtils.displayBanner(rlAds,SettingActivity.this);
        AdUtils.loadIntestial(SettingActivity.this);
        appPref = AppPref.getInstance(SettingActivity.this);
        defaultReminderTime = appPref.getValue(getString(R.string.default_reminder_time), getString(R.string._15_minutes));
        tvDefaultReminderValue.setText(defaultReminderTime);
        isCalendarSync = appPref.getValue(getString(R.string.calendar_sync), true);
        switchCalendarSync.setChecked(isCalendarSync);
        switchCalendarSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.calendar_sync), isChecked);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AdUtils.displayIntestial();
        finish();
    }

    @OnClick({R.id.llBack, R.id.llDefaultReminderTime, R.id.llNotificationSetting, R.id.llAfterCallWindow, R.id.llCallNotesSettings, R.id.llShareApp, R.id.llPrivacy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                AdUtils.displayIntestial();
                finish();
                break;
            case R.id.llDefaultReminderTime:
                PopUtils.showRemindInDialog(SettingActivity.this, defaultReminderTime, new RemindInSelection() {
                    @Override
                    public void onRemindInSelection(String value) {
                        tvDefaultReminderValue.setText(value);
                        defaultReminderTime = value;
                        appPref.setValue(getString(R.string.default_reminder_time), value);

                    }
                });
                break;
            case R.id.llNotificationSetting:
                navigateToNotificationSetting();
                break;
            case R.id.llAfterCallWindow:
                navigateToAfterCallWindow();
                break;
            case R.id.llCallNotesSettings:
                navigateToCallNoteSetting();
                break;
            case R.id.llShareApp:
                StaticUtils.shareApp(SettingActivity.this, getString(R.string.shareAppText));
                break;

            case R.id.llPrivacy:
                navigateToPrivacyPolicy();
                break;
        }
    }

    private void navigateToNotificationSetting() {
        Intent intent = new Intent(SettingActivity.this, NotificationSettingActivity.class);
        navigateToDifferentScreen(intent);
        AdUtils.displayIntestial();
        AdUtils.loadIntestial(SettingActivity.this);
    }

    private void navigateToPrivacyPolicy() {
        Intent intent = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
        navigateToDifferentScreen(intent);
    }

    private void navigateToAfterCallWindow() {
        Intent intent = new Intent(SettingActivity.this, AfterCallWindowActivity.class);
        navigateToDifferentScreen(intent);
        AdUtils.displayIntestial();
        AdUtils.loadIntestial(SettingActivity.this);
    }

    private void navigateToCallNoteSetting() {
        Intent intent = new Intent(SettingActivity.this, CallNoteSettingActivity.class);
        navigateToDifferentScreen(intent);
        AdUtils.displayIntestial();
        AdUtils.loadIntestial(SettingActivity.this);
    }
}
