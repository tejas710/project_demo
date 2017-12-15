package com.gonext.callreminder.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationSettingActivity extends BaseActivity {

    private static final int REQUEST_CODE_RINGTONE = 10;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvToneTitle)
    CustomTextView tvToneTitle;
    @BindView(R.id.tvToneTitletext)
    CustomTextView tvToneTitletext;
    @BindView(R.id.switchReminderTone)
    Switch switchReminderTone;
    @BindView(R.id.llReminderTone)
    CardView llReminderTone;
    @BindView(R.id.tvDefaultReminderToneValue)
    CustomTextView tvDefaultReminderToneValue;
    @BindView(R.id.llDefaultReminderTone)
    CardView llDefaultReminderTone;
    @BindView(R.id.cbPersistantNotification)
    CheckBox cbPersistantNotification;
    @BindView(R.id.llPersistantNotification)
    CardView llPersistantNotification;
    @BindView(R.id.tvVibrationTitle)
    CustomTextView tvVibrationTitle;
    @BindView(R.id.tvVibrationText)
    CustomTextView tvVibrationText;
    @BindView(R.id.switchVibration)
    Switch switchVibration;
    @BindView(R.id.llVibration)
    CardView llVibration;
    AppPref appPref;
    String ringPath = "";
    boolean isReminderTone, isReminderVibration, isPersistantNotification;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_notification_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AdUtils.displayBanner(rlAds,NotificationSettingActivity.this);
        appPref = AppPref.getInstance(NotificationSettingActivity.this);
        isReminderTone = appPref.getValue(getString(R.string.reminder_tone), true);
        isReminderVibration = appPref.getValue(getString(R.string.reminder_vibration), true);
        isPersistantNotification = appPref.getValue(getString(R.string.persistent_notification), true);
        if (isReminderTone) {
            switchReminderTone.setChecked(true);
        } else {
            switchReminderTone.setChecked(false);
        }
        if (isReminderVibration) {
            switchVibration.setChecked(true);
        } else {
            switchVibration.setChecked(false);
        }
        if (isPersistantNotification) {
            cbPersistantNotification.setChecked(true);
        } else {
            cbPersistantNotification.setChecked(false);
        }
        String defaultTone = appPref.getValue("defaultRingTone", "");
        ringPath = defaultTone.substring(0, defaultTone.indexOf(" "));
        tvDefaultReminderToneValue.setText(defaultTone.substring(defaultTone.indexOf(" ") + 1));
        switchReminderTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchReminderTone.setChecked(true);
                } else {
                    switchReminderTone.setChecked(false);

                }
                appPref.setValue(getString(R.string.reminder_tone), isChecked);
            }
        });
        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchVibration.setChecked(true);
                } else {
                    switchVibration.setChecked(false);

                }
                appPref.setValue(getString(R.string.reminder_vibration), isChecked);
            }
        });
        cbPersistantNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbPersistantNotification.setChecked(true);
                } else {
                    cbPersistantNotification.setChecked(false);

                }
                appPref.setValue(getString(R.string.persistent_notification), isChecked);
            }
        });
    }

    @OnClick({R.id.llBack, R.id.llDefaultReminderTone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.llDefaultReminderTone:
                getRingTone();
                break;
        }
    }


    public void getRingTone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.select_rington));
        String selectedUri = appPref.getValue("defaultRingTone", "");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(selectedUri));
        startActivityForResult(intent, REQUEST_CODE_RINGTONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_RINGTONE:
                    try {
                        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                        /*Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));*/

                        ringPath = String.valueOf(data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
                        tvDefaultReminderToneValue.setText(ringtone.getTitle(getApplicationContext()));
                        appPref.setValue("defaultRingTone", ringPath + " " + ringtone.getTitle(getApplicationContext()));
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    }
}
