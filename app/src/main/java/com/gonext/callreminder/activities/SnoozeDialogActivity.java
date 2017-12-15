package com.gonext.callreminder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.service.RemoveNotificationService;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sellnews on 10/11/17.
 */

public class SnoozeDialogActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    CustomTextView tvTitle;
    @BindView(R.id.rb5min)
    RadioButton rb5min;
    @BindView(R.id.rb10min)
    RadioButton rb10min;
    @BindView(R.id.rb15min)
    RadioButton rb15min;
    @BindView(R.id.rb30min)
    RadioButton rb30min;
    @BindView(R.id.rb1hour)
    RadioButton rb1hour;
    @BindView(R.id.rgLanguage)
    RadioGroup rgLanguage;
    @BindView(R.id.llSnoozeDuration)
    LinearLayout llSnoozeDuration;
    @BindView(R.id.tvCancel)
    CustomTextView tvCancel;
    @BindView(R.id.tvOk)
    CustomTextView tvOk;
    int snooze = 15;
    DataBaseClass dataBaseClass;
    ReminderDataModel reminderDataModel = new ReminderDataModel();
    @Override
    protected Integer getLayoutId() {
        return R.layout.dialog_remind_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        dataBaseClass = new DataBaseClass(this);
        reminderDataModel = getIntent().getParcelableExtra(getString(R.string.reminderData));
        tvTitle.setText(R.string.snooze_time);
    }

    @OnClick({R.id.tvCancel, R.id.tvOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                finishAffinity();
                break;
            case R.id.tvOk:

                if (rb5min.isChecked()) {
                    snooze = 5;
                } else if (rb10min.isChecked()) {
                    snooze = 10;
                } else if (rb15min.isChecked()) {
                    snooze = 15;
                } else if (rb30min.isChecked()) {
                    snooze = 30;
                } else if (rb1hour.isChecked()) {
                    snooze = 60;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, snooze);

                dataBaseClass.updateIsComplete(reminderDataModel.getReminderId() + "", calendar.getTimeInMillis(), 0);
                getDismissIntent(SnoozeDialogActivity.this,reminderDataModel.getReminderId());
                finishAffinity();
                break;
        }
    }
    private void getDismissIntent(Context context, int id) {
        Intent intent = new Intent(context, RemoveNotificationService.class);
        intent.putExtra(context.getString(R.string.shared_intent_to_remove), context.getString(R.string.shared_intent_to_remove_value));
        intent.putExtra(context.getString(R.string.notificationId),id);
        startService(intent);
    }
}
