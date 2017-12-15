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

public class CallNoteSettingActivity extends BaseActivity {

    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvIncomingCall)
    CustomTextView tvIncomingCall;
    @BindView(R.id.tvInComingCallText)
    CustomTextView tvInComingCallText;
    @BindView(R.id.switchIncomingCalls)
    Switch switchIncomingCalls;
    @BindView(R.id.llIncomingCall)
    CardView llIncomingCall;
    @BindView(R.id.tvOutgoingCall)
    CustomTextView tvOutgoingCall;
    @BindView(R.id.tvOutgoingCallText)
    CustomTextView tvOutgoingCallText;
    @BindView(R.id.switchOutgoingCalls)
    Switch switchOutgoingCalls;
    @BindView(R.id.llOutgoingCalls)
    CardView llOutgoingCalls;
    @BindView(R.id.tvDismissText)
    CustomTextView tvDismissText;
    @BindView(R.id.llAfterCallDismissWindow)
    CardView llAfterCallDismissWindow;

    @BindView(R.id.tvSpeakerPhone)
    CustomTextView tvSpeakerPhone;
    @BindView(R.id.tvSpeakerPhoneText)
    CustomTextView tvSpeakerPhoneText;
    @BindView(R.id.cbSpeakerPhone)
    CheckBox cbSpeakerPhone;
    @BindView(R.id.llSpeakerOn)
    CardView llSpeakerOn;
    String displayLength = "";
    AppPref appPref;
    boolean isIncomingCalls, isOutgingCalls, isEditonLongPress, isSpeakerPhone;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_call_note_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        AdUtils.displayBanner(rlAds,CallNoteSettingActivity.this);
        appPref = AppPref.getInstance(CallNoteSettingActivity.this);
        displayLength = appPref.getValue(getString(R.string.display_length), getString(R.string.duration_of_call));
        isIncomingCalls = appPref.getValue(getString(R.string.incoming_calls), true);
        isOutgingCalls = appPref.getValue(getString(R.string.outgoing_calls), true);
        isSpeakerPhone = appPref.getValue(getString(R.string.speakerphone), true);
        switchIncomingCalls.setChecked(isIncomingCalls);
        switchOutgoingCalls.setChecked(isOutgingCalls);
        cbSpeakerPhone.setChecked(isSpeakerPhone);
        switchIncomingCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.incoming_calls), isChecked);
            }
        });
        switchOutgoingCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.outgoing_calls), isChecked);
            }
        });
        cbSpeakerPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.setValue(getString(R.string.speakerphone), isChecked);
            }
        });
    }

    @OnClick({R.id.llBack, R.id.llAfterCallDismissWindow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.llAfterCallDismissWindow:
                PopUtils.showDisplayLengthCallNote(CallNoteSettingActivity.this, displayLength, new RemindInSelection() {
                    @Override
                    public void onRemindInSelection(String value) {
                        displayLength = value;
                        appPref.setValue(getString(R.string.display_length), value);
                    }
                });
                break;
        }
    }
}
