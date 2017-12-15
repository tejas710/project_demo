package com.gonext.callreminder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sellnews on 10/11/17.
 */

public class AfterCallWindowDialogActivity extends BaseActivity {
    @BindView(R.id.tvName)
    CustomTextView tvName;
    @BindView(R.id.tvNumber)
    CustomTextView tvNumber;
    @BindView(R.id.ivProfilePhoto)
    ImageView ivProfilePhoto;

    AppPref appPref;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    int seconds = 0;
    boolean isDismiss = false;
    @BindView(R.id.llCall)
    LinearLayout llCall;
    @BindView(R.id.llCallNote)
    LinearLayout llCallNote;
    @BindView(R.id.llSms)
    LinearLayout llSms;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.llOther)
    LinearLayout llOther;
    String photo = null;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.dialog_after_call_window;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AdUtils.displayBanner(rlAds,AfterCallWindowDialogActivity.this);
        appPref = AppPref.getInstance(this);
        if (appPref.getValue(getString(R.string.show_on_lock_screen), true)) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        }
        String dissmissWindow = appPref.getValue(getString(R.string.dismiss_after_call_window), getString(R.string.do_not_dismiss));
        if (dissmissWindow.equalsIgnoreCase(getString(R.string.dismiss_after_5_seconds))) {
            seconds = 5;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.dismiss_after_10_seconds))) {
            seconds = 10;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.dismiss_after_20_seconds))) {
            seconds = 20;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.dismiss_after_30_seconds))) {
            seconds = 30;
        } else if (dissmissWindow.equalsIgnoreCase(getString(R.string.do_not_dismiss))) {
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
        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra(getString(R.string.incomingname)));
        tvNumber.setText(intent.getStringExtra(getString(R.string.incomingnumber)));
        photo = intent.getStringExtra(getString(R.string.incomingphoturi));
        if (photo != null) {
            Glide.with(this)
                    .load(Uri.parse(photo)) // Uri of the picture
                    .into(ivProfilePhoto);
        } else {
            ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(AfterCallWindowDialogActivity.this, R.drawable.ic_profile));
        }

    }


    @OnClick({R.id.ivClose, R.id.llCall, R.id.llCallNote, R.id.llSms, R.id.llEmail, R.id.llOther})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                finishAffinity();
                break;
            case R.id.llCall:
                navigateToAddReminderActivity(101);
                break;
            case R.id.llCallNote:
                navigateToAddReminderActivity(102);
                break;
            case R.id.llSms:
                navigateToAddReminderActivity(103);
                break;
            case R.id.llEmail:
                navigateToAddReminderActivity(104);
                break;
            case R.id.llOther:
                navigateToAddReminderActivity(105);
                break;
        }
    }

    public void navigateToAddReminderActivity(int reqCaode) {
        Intent intent = new Intent(AfterCallWindowDialogActivity.this, AddReminderActivity.class);
        intent.putExtra(getString(R.string.reqcodeafterwindow), 21);
        intent.putExtra(getString(R.string.remindertype), reqCaode);
        intent.putExtra(getString(R.string.incomingnumber), tvNumber.getText());
        intent.putExtra(getString(R.string.incomingname), tvName.getText());
        intent.putExtra(getString(R.string.incomingphoturi), photo);
        navigateToDifferentScreen(intent);
        finish();
    }
}
