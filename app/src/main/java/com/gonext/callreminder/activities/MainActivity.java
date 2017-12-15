package com.gonext.callreminder.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gonext.callreminder.R;
import com.gonext.callreminder.adapter.CallReminderViewPagerAdapter;
import com.gonext.callreminder.checkupdate.UpdateChecker;
import com.gonext.callreminder.checkupdate.VersionCallListener;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.fragment.CallNotesFragment;
import com.gonext.callreminder.fragment.ReminderFragment;
import com.gonext.callreminder.notification.service.NotificationService;
import com.gonext.callreminder.service.TimerService;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.PermissionUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.StaticData;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.logger.CustomLog;
import com.gonext.callreminder.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gonext.callreminder.utils.PermissionUtils.hasPermissions;
import static com.gonext.callreminder.utils.PermissionUtils.requestPermission;


public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {


    private static final int MY_READEXTERNAL_PERMISSION = 112;
    private static final int MY_CONTACT_PERMISSION = 113;
    public static final int ADD_DATA= 114;
    @BindView(R.id.tlCallReminder)
    TabLayout tlCallReminder;
    @BindView(R.id.rlCallTab)
    RelativeLayout rlCallTab;
    @BindView(R.id.vpCallReminder)
    ViewPager vpCallReminder;
    @BindView(R.id.rlCallPager)
    RelativeLayout rlCallPager;
    int selectedFragment = 0;
    Fragment fragment;
    CallReminderViewPagerAdapter callReminderViewPagerAdapter;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;

    AppPref appPref;
    boolean isDontShow = false;
    DataBaseClass dataBaseClass;
    @BindView(R.id.ivRateApp)
    ImageView ivRateApp;
    @BindView(R.id.ivHistory)
    ImageView ivHistory;
    @BindView(R.id.fab)
    public FloatingActionButton fab;
    String[] read_external_storage_permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE};
    Uri urie = null;
    String[] contact_permissions = new String[]{
            Manifest.permission.READ_CONTACTS};
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        requestForServerAdvertisement();
    }


    private void init() {
        AdUtils.displayBanner(rlAds, MainActivity.this);
        appPref = AppPref.getInstance(MainActivity.this);
        UpdateChecker updateChecker = new UpdateChecker(MainActivity.this);

        updateChecker.start(getPackageName(), new VersionCallListener() {
            @Override
            public void versionCall(String playstoreVersion, String playStoreDate, boolean isPublish) {
                CustomLog.info("playstoreVersion", playstoreVersion);
                CustomLog.info("playStoreDate", playStoreDate);
                CustomLog.info("isPublish", isPublish + "");
            }

        });
        Intent service = new Intent(MainActivity.this, TimerService.class);
        startService(service);

        Intent notificationService = new Intent(MainActivity.this, NotificationService.class);
        startService(notificationService);

        if (hasPermissions(MainActivity.this, read_external_storage_permissions)) {
            if (appPref.getValue("firstTimeGetTone", true)) {
                urie = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
                appPref.setValue("defaultRingTone", urie + " " + getString(R.string.defaultrington));
            }
            appPref.setValue("firstTimeGetTone", false);
        } else {
            appPref.setValue("firstTimeGetTone", true);
            requestPermission(MainActivity.this, read_external_storage_permissions, MY_READEXTERNAL_PERMISSION);
        }


        if (appPref.getValue("firstTime", true)) {


            appPref.setValue(getString(R.string.default_reminder_time), getString(R.string._15_minutes));
            appPref.setValue(getString(R.string.reminder_tone), true);
            appPref.setValue(getString(R.string.persistent_notification), true);
            appPref.setValue(getString(R.string.reminder_vibration), true);
            appPref.setValue(getString(R.string.calendar_sync), false);
            appPref.setValue(getString(R.string.display_length), getString(R.string.duration_of_call));
            appPref.setValue(getString(R.string.incoming_calls), true);
            appPref.setValue(getString(R.string.outgoing_calls), true);
            appPref.setValue(getString(R.string.speakerphone), true);
            appPref.setValue(getString(R.string.after_call_window), true);
            appPref.setValue(getString(R.string.show_on_lock_screen), true);
            appPref.setValue(getString(R.string.dismiss_after_call_window), getString(R.string.do_not_dismiss));
            appPref.setValue(getString(R.string.after_call_window_type), getString(R.string.incoming_calls) + " " + getString(R.string.outgoing_calls) + " " + getString(R.string.missed_calls));
            appPref.setValue("firstTime", false);
        }
        isDontShow = appPref.getValue(getString(R.string.donotshow), false);
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        if (getIntent().getIntExtra(getString(R.string.splash), 0) == 7) {
            if (deviceMan.equalsIgnoreCase("samsung") || deviceName.equalsIgnoreCase("samsung")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (!isDontShow) {
                        dialogForSamsundDevice(getString(R.string.infoSamsungNaugat));
                    }

                } else {
                    if (!isDontShow) {
                        dialogForSamsundDevice(getString(R.string.infoSamsungMarshmallw));
                    }
                }
            }
        }
        dataBaseClass = new DataBaseClass(this);

        FragmentManager fm = getSupportFragmentManager();
        vpCallReminder.setOffscreenPageLimit(2);
        vpCallReminder.setCurrentItem(0);

        callReminderViewPagerAdapter = new CallReminderViewPagerAdapter(fm, this);
        // Set the View Pager Adapter into ViewPager
        callReminderViewPagerAdapter.getItem(tlCallReminder.getSelectedTabPosition());
        vpCallReminder.setAdapter(callReminderViewPagerAdapter);
        tlCallReminder.setupWithViewPager(vpCallReminder);

        tlCallReminder.addOnTabSelectedListener(this);

        if (selectedFragment == 0) {
            fragment = callReminderViewPagerAdapter.getItem(selectedFragment);

        }
        // new GetDataContact().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_READEXTERNAL_PERMISSION:
                if (hasPermissions(this, read_external_storage_permissions)) {
                    urie = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
                    appPref.setValue("defaultRingTone", urie + " " + getString(R.string.defaultrington));
                }
                break;
            case MY_CONTACT_PERMISSION:
                if (hasPermissions(this, contact_permissions)) {
                    navigateTOAddReminderActivity();
                }
                break;
            case ADD_DATA:
                if (StaticData.isSavedReminder) {
                    StaticData.isSavedReminder = false;
                    selectFragment();
                }
                break;
        }
    }

    //request permisiion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READEXTERNAL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                urie = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
                appPref.setValue("defaultRingTone", urie + " " + getString(R.string.defaultrington));
            } else {
                if (PermissionUtils.shouldShowRequestPermissions(MainActivity.this, permissions)) {
                    PopUtils.showDialogforPermission(MainActivity.this, getString(R.string.storage_permission_is_require_to_use_this_application), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.requestPermission(MainActivity.this, read_external_storage_permissions, MY_READEXTERNAL_PERMISSION);
                        }
                    });
                } else {
                    PopUtils.showDialogforPermission(MainActivity.this, getString(R.string.storage_permission_is_require_to_use_this_application), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSettingScreen(MY_READEXTERNAL_PERMISSION);
                        }
                    });

                }

            }
        }
        if (requestCode == MY_CONTACT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                navigateTOAddReminderActivity();
            } else {
                if (PermissionUtils.shouldShowRequestPermissions(MainActivity.this, permissions)) {
                    PopUtils.showDialogforPermission(MainActivity.this, "Contact Permission is required for Contact Suggestion.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.requestPermission(MainActivity.this, contact_permissions, MY_CONTACT_PERMISSION);
                        }
                    });
                } else {
                    PopUtils.showDialogforPermission(MainActivity.this, "Contact Permission is required for Contact Suggestion.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSettingScreen(MY_CONTACT_PERMISSION);
                        }
                    });

                }

            }
        }

    }

    public void openSettingScreen(int requestcode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestcode);
    }

    @Override
    protected void onResume() {

        if(StaticData.isNotification)
        {
            StaticData.isNotification = false;
            recreate();
        }
        super.onResume();
    }


    private void requestForServerAdvertisement() {
        requestForServerAd(null);
    }


    public void dialogForSamsundDevice(String text) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_powersaving_samsung);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustomTextView tvMsg = (CustomTextView) dialog.findViewById(R.id.tvMsg);
        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        final SwitchCompat switchDontshow = (SwitchCompat) dialog.findViewById(R.id.switchDonotShow);
        tvMsg.setText(text);
        switchDontshow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchDontshow.setChecked(true);
                } else {

                    switchDontshow.setChecked(false);

                }
                appPref.setValue(getString(R.string.donotshow), switchDontshow.isChecked());
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, ExitActivity.class);
        navigateToDifferentScreen(intent);

    }

    public void selectFragment() {
        fragment = callReminderViewPagerAdapter.getItem(selectedFragment);
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                fragment = callReminderViewPagerAdapter.getItem(i);
                ((CallNotesFragment) fragment).getAllCallNote();
            } else if (i == 0) {
                fragment = callReminderViewPagerAdapter.getItem(i);
                ((ReminderFragment) fragment).getAllReminder();
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpCallReminder.setCurrentItem(tab.getPosition());
        selectedFragment = tab.getPosition();
        fragment = callReminderViewPagerAdapter.getItem(selectedFragment);
        if (selectedFragment == 1) {
            ((CallNotesFragment) fragment).notifyFrgament();
        } else {
            ((ReminderFragment) fragment).notifyFrgament();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        vpCallReminder.setCurrentItem(position);
        selectedFragment = position;
        fragment = callReminderViewPagerAdapter.getItem(selectedFragment);
        if (selectedFragment == 1) {
            ((CallNotesFragment) fragment).notifyFrgament();
        } else {
            ((ReminderFragment) fragment).notifyFrgament();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @OnClick({R.id.ivSetting, R.id.ivRateApp, R.id.ivHistory, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSetting:
                navigateTOSettingActivity();
                break;

            case R.id.ivRateApp:
                    PopUtils.showRateAppDialog(MainActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StaticUtils.rateApp(MainActivity.this);

                        }
                    });
                break;
            case R.id.fab:
                if (hasPermissions(MainActivity.this, contact_permissions)) {
                    navigateTOAddReminderActivity();
                } else {
                    requestPermission(MainActivity.this, contact_permissions, MY_CONTACT_PERMISSION);
                }

                break;
            case R.id.ivHistory:
                navigateTOHistoryActivity();
                break;
        }
    }

    public void navigateTOSettingActivity() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        navigateToDifferentScreen(intent);
    }

    public void navigateTOHistoryActivity() {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        navigateToDifferentScreen(intent);

    }

    public void navigateTOAddReminderActivity() {

        Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
        startActivityForResult(intent,ADD_DATA);
    }
}
