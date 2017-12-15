package com.gonext.callreminder.activities;

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
import com.gonext.callreminder.interfaces.RemindInSelection;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.view.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class AfterCallWindowActivity extends BaseActivity {


    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvAfterCallWindowTitle)
    CustomTextView tvAfterCallWindowTitle;
    @BindView(R.id.tvAfterWindowCallText)
    CustomTextView tvAfterWindowCallText;
    @BindView(R.id.switchAfterCall)
    Switch switchAfterCall;
    @BindView(R.id.llAfterCallWindow)
    CardView llAfterCallWindow;
    @BindView(R.id.tvChooseType)
    CustomTextView tvChooseType;
    @BindView(R.id.llAfterCallType)
    CardView llAfterCallType;
    @BindView(R.id.tvDismissText)
    CustomTextView tvDismissText;
    @BindView(R.id.llAfterCallDismissWindow)
    CardView llAfterCallDismissWindow;
    @BindView(R.id.tvShowOnLockScreen)
    CustomTextView tvShowOnLockScreen;
    @BindView(R.id.tvVibrationText)
    CustomTextView tvVibrationText;
    @BindView(R.id.cbShowOnLockScreen)
    CheckBox cbShowOnLockScreen;
    @BindView(R.id.llShowOverLockScreen)
    CardView llShowOverLockScreen;
    AppPref appPref;
    String dismissAfterCallWindow = "";
    String afterCallWindowType = "";
    boolean isAfterCallWindow, isShowLockScreen;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_after_call_window;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        AdUtils.displayBanner(rlAds,AfterCallWindowActivity.this);
        appPref = AppPref.getInstance(AfterCallWindowActivity.this);
        dismissAfterCallWindow = appPref.getValue(getString(R.string.dismiss_after_call_window), getString(R.string.do_not_dismiss));
        afterCallWindowType = appPref.getValue(getString(R.string.after_call_window_type), getString(R.string.incoming_calls) + " " + getString(R.string.outgoing_calls) + " " + getString(R.string.missed_calls));
        isAfterCallWindow = appPref.getValue(getString(R.string.after_call_window), true);
        isShowLockScreen = appPref.getValue(getString(R.string.show_on_lock_screen), true);
        switchAfterCall.setChecked(isAfterCallWindow);
        cbShowOnLockScreen.setChecked(isShowLockScreen);
        switchAfterCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.after_call_window), isChecked);
            }
        });
        cbShowOnLockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.show_on_lock_screen), isChecked);
            }
        });
    }

    @OnClick({R.id.llBack, R.id.llAfterCallType, R.id.llAfterCallDismissWindow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.llAfterCallType:
                PopUtils.showAfterCallWindowType(AfterCallWindowActivity.this, afterCallWindowType, new RemindInSelection() {
                    @Override
                    public void onRemindInSelection(String value) {
                        afterCallWindowType = value;
                        appPref.setValue(getString(R.string.after_call_window_type), value);
                    }
                });
                break;
            case R.id.llAfterCallDismissWindow:
                PopUtils.showDismissAfterCallWindow(AfterCallWindowActivity.this, dismissAfterCallWindow, new RemindInSelection() {
                    @Override
                    public void onRemindInSelection(String value) {
                        dismissAfterCallWindow = value;
                        appPref.setValue(getString(R.string.dismiss_after_call_window), value);
                    }
                });
                break;
        }
    }
}
