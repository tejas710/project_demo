package com.gonext.callreminder.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.adapter.ViewPagerAdapter;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.fragment.CallnotedialogFragment;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.StaticData;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sellnews on 10/11/17.
 */

public class CallNoteDialogActivity extends BaseActivity {
    @BindView(R.id.ivProfilePhoto)
    ImageView ivProfilePhoto;
    @BindView(R.id.llProfilePhoto)
    LinearLayout llProfilePhoto;
    @BindView(R.id.tvName)
    CustomTextView tvName;
    @BindView(R.id.tvNumber)
    CustomTextView tvNumber;
    @BindView(R.id.rlTop)
    RelativeLayout rlTop;
    @BindView(R.id.indicator_container)
    LinearLayout indicatorContainer;
    @BindView(R.id.llReminderType)
    LinearLayout llReminderType;
    @BindView(R.id.vpCallNote)
    ViewPager vpCallNote;
    @BindView(R.id.tvAddNew)
    CustomTextView tvAddNew;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;
    private List<ImageView> mDots;
    Fragment fragment;
    public int COUNT = 0;
    DataBaseClass dataBaseClass;
    public ArrayList<ReminderDataModel> lstCallNote = new ArrayList<>();
    public static ArrayList<ReminderDataModel> lstCallNoteTemp = new ArrayList<>();
    int selectedFragment = 0;
    ViewPagerAdapter viewpageradapter;
    AudioManager audioManager;
    AppPref appPref;
    int seconds;
    boolean isDismiss = false;

    @Override
    protected Integer getLayoutId() {
        return R.layout.dialog_call_note;
    }

    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AdUtils.displayBanner(rlAds,CallNoteDialogActivity.this);
        dataBaseClass = new DataBaseClass(this);
        appPref = AppPref.getInstance(this);

        String dissmissWindow = appPref.getValue(getString(R.string.display_length), getString(R.string.duration_of_call));
        if (dissmissWindow.equalsIgnoreCase(getString(R.string.display_for_10_seconds))) {
            seconds = 10;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.display_for_20_seconds))) {
            seconds = 20;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.display_for_30_seconds))) {
            seconds = 30;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.duration_of_call))) {
            isDismiss = true;
        }
        if (!isDismiss) {
            new CountDownTimer(seconds * 1000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    finishAffinity();
                }
            }.start();
        }
        String number = getIntent().getStringExtra(getString(R.string.incomingnumber));
        lstCallNote.clear();
        lstCallNoteTemp.clear();
        lstCallNote = dataBaseClass.getAllReminderData(number);
        for (ReminderDataModel reminderDataModel : lstCallNote) {
            if (reminderDataModel.getIsCallNoteShow() == 1) {
                lstCallNoteTemp.add(reminderDataModel);
            }
        }
        if (lstCallNoteTemp.size() > 0) {
            COUNT = lstCallNoteTemp.size();
            tvName.setText(lstCallNoteTemp.get(0).getReminderName());
            tvNumber.setText(lstCallNoteTemp.get(0).getReminderNumber());
            String photo = lstCallNoteTemp.get(0).getReminderIcon();
            if (photo != null) {
                Glide.with(this)
                        .load(Uri.parse(photo)) // Uri of the picture
                        .into(ivProfilePhoto);
            } else {
                ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(CallNoteDialogActivity.this, R.drawable.ic_profile));
            }
        } else {

        }
        FragmentManager fm = getSupportFragmentManager();
        viewpageradapter = new ViewPagerAdapter(fm, COUNT);
        vpCallNote.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedFragment = position;
                selectPosition(position);
                //setAnylytics();
                if (isEdit) {
                    fragment = viewpageradapter.getItem(selectedFragment);
                    ((CallnotedialogFragment) fragment).editCallNote();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupIndicators();
        vpCallNote.setAdapter(viewpageradapter);
        if (selectedFragment == 0) {
            fragment = viewpageradapter.getItem(selectedFragment);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticData.COMPLETECALL);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @OnClick({R.id.ivEdit, R.id.ivClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                finishAffinity();
                break;

            case R.id.ivEdit:
                audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                if (isEdit) {
                    audioManager.setSpeakerphoneOn(false);
                    ivEdit.setImageDrawable(ContextCompat.getDrawable(CallNoteDialogActivity.this, R.drawable.ic_edit));
                    for (int i = 0; i < lstCallNoteTemp.size(); i++) {
                        fragment = viewpageradapter.getItem(i);
                        ((CallnotedialogFragment) fragment).saveCallNote();
                    }

                    isEdit = false;
                } else {
                    if (appPref.getValue(getString(R.string.speakerphone), true)) {
                        audioManager.setSpeakerphoneOn(true);
                    }
                    ivEdit.setImageDrawable(ContextCompat.getDrawable(CallNoteDialogActivity.this, R.drawable.ic_save));
                    fragment = viewpageradapter.getItem(selectedFragment);
                    ((CallnotedialogFragment) fragment).editCallNote();
                    isEdit = true;
                }

                audioManager.setMode(AudioManager.MODE_NORMAL);

                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    //indicator
    private void setupIndicators() {

        mDots = new ArrayList<>(COUNT);

        float scale = getResources().getDisplayMetrics().density;

        for (int i = 0; i < COUNT; i++) {

            ImageView dot = new ImageView(this);

            dot.setImageResource(i == 0 ? R.drawable.intro_selected_dot : R.drawable.intro_unselected_dot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,

                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins((int) (8 * scale), 0, 0, 0);

            indicatorContainer.addView(dot, params);

            mDots.add(dot);

        }
    }

    public void selectPosition(final int index) {

        for (int i = 0; i < COUNT; i++) {

            mDots.get(i).setImageResource((i == index) ? R.drawable.intro_selected_dot

                    : R.drawable.intro_unselected_dot);

        }

    }
}
