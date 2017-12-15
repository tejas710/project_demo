package com.gonext.callreminder.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.adapter.ContactSuggestionAdapter;
import com.gonext.callreminder.datalayers.model.ContactDetailModel;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.interfaces.RemindInSelection;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.PermissionUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.StaticData;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.TimeUtils;
import com.gonext.callreminder.utils.view.CustomEditText;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gonext.callreminder.utils.PermissionUtils.hasPermissions;
import static com.gonext.callreminder.utils.PermissionUtils.requestPermission;

public class AddReminderActivity extends BaseActivity implements ContactSuggestionAdapter.Clickable {


    @BindView(R.id.cbCallReminder)
    CheckBox cbCallReminder;
    @BindView(R.id.cbSmsReminder)
    CheckBox cbSmsReminder;
    @BindView(R.id.cbEmailReminder)
    CheckBox cbEmailReminder;
    @BindView(R.id.cbMiscReminder)
    CheckBox cbMiscReminder;
    @BindView(R.id.cbCallNote)
    CheckBox cbCallNote;
    @BindView(R.id.llReminderType)
    LinearLayout llReminderType;
    @BindView(R.id.ivProfilePhoto)
    ImageView ivProfilePhoto;
    @BindView(R.id.edtContactName)
    CustomEditText edtContactName;
    @BindView(R.id.llName)
    LinearLayout llName;
    @BindView(R.id.edtNumber)
    CustomEditText edtNumber;
    @BindView(R.id.llNumber)
    LinearLayout llNumber;
    @BindView(R.id.edtEmail)
    CustomEditText edtEmail;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.edtNote)
    CustomEditText edtNote;
    @BindView(R.id.llNotes)
    LinearLayout llNotes;
    @BindView(R.id.llDateAndTime)
    LinearLayout llDateAndTime;
    @BindView(R.id.tvDate)
    CustomTextView tvDate;
    @BindView(R.id.tvTime)
    CustomTextView tvTime;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.tvRepeatInterval)
    CustomTextView tvRepeatInterval;
    @BindView(R.id.llRepeatInterval)
    LinearLayout llRepeatInterval;
    @BindView(R.id.tvTone)
    CustomTextView tvTone;
    @BindView(R.id.llTone)
    LinearLayout llTone;
    @BindView(R.id.cbPersistantTone)
    CheckBox cbPersistantTone;

    @BindView(R.id.cbAddCalendar)
    CheckBox cbAddCalendar;
    @BindView(R.id.llAddCalendar)
    LinearLayout llAddCalendar;
    @BindView(R.id.llMainData)
    LinearLayout llMainData;
    @BindView(R.id.tvMon)
    CustomTextView tvMon;
    @BindView(R.id.tvTue)
    CustomTextView tvTue;
    @BindView(R.id.tvWen)
    CustomTextView tvWen;
    @BindView(R.id.tvThu)
    CustomTextView tvThu;
    @BindView(R.id.tvFri)
    CustomTextView tvFri;
    @BindView(R.id.tvSat)
    CustomTextView tvSat;
    @BindView(R.id.tvSun)
    CustomTextView tvSun;
    @BindView(R.id.llWeekDaysDisplay)
    LinearLayout llWeekDaysDisplay;

    @BindView(R.id.tvRepeat_forever)
    CustomTextView tvRepeatForever;
    @BindView(R.id.llRepeat_forever)
    LinearLayout llRepeatForever;
    private static final int PICK_CONTACT = 23;
    private static final int REQUEST_CODE_RINGTONE = 24;
    private static final int PICK_NUMBER = 25;
    private static final int SPEECH_REQUEST_CODE = 1010;
    boolean isDaily = true, isWeekly = true, isMonthly = true, isYearly = true;
    boolean isMon = true, isTue = true, isWen = true, isThu = true, isFri = true, isSat = true, isSun = true;
    Calendar calendar, foreverDateCalender;
    String alarmDate = "";
    String foreverDate = "";
    String repeat_interval = "", ringPath = "";
    SimpleDateFormat dateFormat;
    int mYear, mMonth, mDay, mHour, mMinute;
    String photo = null;
    boolean isDateTag;
    AppPref appPref;
    DataBaseClass dataBaseClass;
    @BindView(R.id.ivContactList)
    ImageView ivContactList;
    @BindView(R.id.ivCallLog)
    ImageView ivCallLog;
    @BindView(R.id.rbDateTime)
    RadioButton rbDateTime;
    @BindView(R.id.rbRemindIn)
    RadioButton rbRemindIn;
    @BindView(R.id.rgDateAndTime)
    RadioGroup rgDateAndTime;
    @BindView(R.id.tvRemindTime)
    CustomTextView tvRemindTime;
    @BindView(R.id.llRemindIn)
    LinearLayout llRemindIn;
    @BindView(R.id.rlName)
    RelativeLayout rlName;
    @BindView(R.id.rvContactSuggestion)
    RecyclerView rvContactSuggestion;
    ArrayList<ContactDetailModel> lstContactSuggestion = new ArrayList<>();
    ContactSuggestionAdapter contactSuggestionAdapter;
    @BindView(R.id.llPersistantAndCalender)
    LinearLayout llPersistantAndCalender;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    ReminderDataModel reminderDataModel = new ReminderDataModel();

    @BindView(R.id.tvCallReminder)
    CustomTextView tvCallReminder;
    @BindView(R.id.tvCallNote)
    CustomTextView tvCallNote;
    @BindView(R.id.tvSmsReminder)
    CustomTextView tvSmsReminder;
    @BindView(R.id.tvEmailReminder)
    CustomTextView tvEmailReminder;
    @BindView(R.id.tvOther)
    CustomTextView tvOther;
    @BindView(R.id.cbComfirmMsg)
    CheckBox cbComfirmMsg;
    @BindView(R.id.llConfirmMsg)
    LinearLayout llConfirmMsg;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.ivNote)
    ImageView ivNote;
    @BindView(R.id.ivEmail)
    ImageView ivEmail;
    @BindView(R.id.ivRepeat)
    ImageView ivRepeat;
    @BindView(R.id.ivRingtone)
    ImageView ivRingtone;
    @BindView(R.id.llForeverView)
    LinearLayout llForeverView;
    @BindView(R.id.llBottomLayout)
    LinearLayout llBottomLayout;
    long reminderDate, forever;
    int REQUEST_CODE, REQUEST_CODE_AFTER_WINDOW;
    @BindView(R.id.ivCalendar)
    ImageView ivCalendar;
    @BindView(R.id.ivTime)
    ImageView ivTime;
    @BindView(R.id.ivNoEndDate)
    ImageView ivNoEndDate;
    @BindView(R.id.tvEdit)
    CustomTextView tvEdit;
    private static final int MY_CALENDAR_PERMISSION = 115;
    String[] calendar_permissions = new String[]{
            Manifest.permission.READ_CALENDAR};
    private static final int MY_PHONE_PERMISSION = 116;
    private static final int CALL_LOG = 117;
    String[] phone_permissions = new String[]{
            Manifest.permission.READ_CALL_LOG};
    String[] contact_permissions = new String[]{
            Manifest.permission.READ_CONTACTS};
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;
    @BindView(R.id.cvContactSuggestion)
    CardView cvContactSuggestion;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_add_reminder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }


    private void init() {
        AdUtils.displayBanner(rlAds, AddReminderActivity.this);
        AdUtils.loadIntestial(AddReminderActivity.this);
        appPref = AppPref.getInstance(AddReminderActivity.this);
        dataBaseClass = new DataBaseClass(AddReminderActivity.this);
        REQUEST_CODE = getIntent().getIntExtra(getString(R.string.request_code_edit), 0);
        REQUEST_CODE_AFTER_WINDOW = getIntent().getIntExtra(getString(R.string.reqcodeafterwindow), 0);
        if (hasPermissions(AddReminderActivity.this, calendar_permissions)) {

            boolean isCalendar = appPref.getValue(getString(R.string.calendar_sync), false);
            cbAddCalendar.setChecked(isCalendar);
        } else {
            requestPermission(AddReminderActivity.this, calendar_permissions, MY_CALENDAR_PERMISSION);
        }
        if (hasPermissions(AddReminderActivity.this, contact_permissions)) {
            new GetDataContact().execute();
        }

        foreverDateCalender = Calendar.getInstance();
        calendar = Calendar.getInstance();
        if (REQUEST_CODE == 18) {
            tvEdit.setVisibility(View.VISIBLE);
            allViewClickable(false);
            upDateReminderData();

        } else {

            if (REQUEST_CODE_AFTER_WINDOW == 21) {
                switch (getIntent().getIntExtra(getString(R.string.remindertype), 101)) {
                    case 101:
                        cbCallReminder.setChecked(true);
                        cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));
                        edtNote.requestFocus();
                        edtNote.setFocusable(true);
                        edtNote.setFocusableInTouchMode(true);
                        break;
                    case 102:
                        cbCallNote.setChecked(true);
                        cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote));
                        edtNote.requestFocus();
                        edtNote.setFocusable(true);
                        edtNote.setFocusableInTouchMode(true);
                        break;
                    case 103:
                        cbSmsReminder.setChecked(true);
                        cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder));
                        edtNote.requestFocus();
                        edtNote.setFocusable(true);
                        edtNote.setFocusableInTouchMode(true);
                        break;
                    case 104:
                        cbEmailReminder.setChecked(true);
                        cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder));
                        edtEmail.requestFocus();
                        edtEmail.setFocusable(true);
                        edtEmail.setFocusableInTouchMode(true);
                        break;
                    case 105:
                        cbMiscReminder.setChecked(true);
                        cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgotherreminder));
                        edtNote.requestFocus();
                        edtNote.setFocusable(true);
                        edtNote.setFocusableInTouchMode(true);
                        break;
                }
                visibleCallNote();
                edtNumber.setText(getIntent().getStringExtra(getString(R.string.incomingnumber)));
                edtContactName.setText(getIntent().getStringExtra(getString(R.string.incomingname)));
                photo = getIntent().getStringExtra(getString(R.string.incomingphoturi));
            } else {
                cbCallReminder.setChecked(true);
                cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));

            }
            String defaultTone = appPref.getValue("defaultRingTone", " ");
            ringPath = defaultTone.substring(0, defaultTone.indexOf(" "));
            tvTone.setText(defaultTone.substring(defaultTone.indexOf(" ") + 1));
            tvEdit.setVisibility(View.GONE);


            String duration = appPref.getValue(getString(R.string.default_reminder_time), getString(R.string._15_minutes));
            if (duration.equalsIgnoreCase(getString(R.string._5_minutes))) {
                calendar.add(Calendar.MINUTE, 5);
            } else if (duration.equalsIgnoreCase(getString(R.string._10_minutes))) {
                calendar.add(Calendar.MINUTE, 10);
            } else if (duration.equalsIgnoreCase(getString(R.string._15_minutes))) {
                calendar.add(Calendar.MINUTE, 15);
            } else if (duration.equalsIgnoreCase(getString(R.string._30_minutes))) {
                calendar.add(Calendar.MINUTE, 30);
            } else if (duration.equalsIgnoreCase(getString(R.string._1_hour))) {
                calendar.add(Calendar.MINUTE, 60);
            }
            /*boolean isCalendar = appPref.getValue(getString(R.string.calendar_sync), true);
            cbAddCalendar.setChecked(isCalendar);*/
            reminderDate = calendar.getTimeInMillis();
            String repeatDate = TimeUtils.getDateIn24HrsFormat(reminderDate);
            tvDate.setText(repeatDate.substring(0, repeatDate.indexOf(" ")));
            tvTime.setText(repeatDate.substring(repeatDate.indexOf(" ") + 1));
        }

        edtContactName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    cvContactSuggestion.setVisibility(View.GONE);
                    rvContactSuggestion.setVisibility(View.GONE);
                }
                return false;
            }
        });
        cbCallReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                visibleCallNote();
                if (isChecked) {
                    tvCallReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                    cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));
                } else {
                    if (!isAllReminderType()) {
                        cbCallReminder.setChecked(true);
                        tvCallReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));
                        visibleCallNote();
                    } else {
                        tvCallReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder2));
                    }

                }


            }
        });

        cbSmsReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                visibleCallNote();
                if (isChecked) {
                    tvSmsReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                    cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder));
                } else {
                    if (!isAllReminderType()) {
                        cbSmsReminder.setChecked(true);
                        tvSmsReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder));
                        visibleCallNote();
                    } else {
                        tvSmsReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder2));
                    }

                }
            }
        });
       /* cbComfirmMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (appPref.getValue("firstTimeUnChecked", true)) {
                    PopUtils.showConfirmSms(AddReminderActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cbComfirmMsg.setChecked(false);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cbComfirmMsg.setChecked(true);
                        }
                    });
                    appPref.setValue("firstTimeUnChecked", false);
                }

            }
        });*/
        cbEmailReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                visibleCallNote();
                if (isChecked) {
                    tvEmailReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                    cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder));
                } else {
                    if (!isAllReminderType()) {
                        cbEmailReminder.setChecked(true);
                        tvEmailReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder));
                        visibleCallNote();
                    } else {
                        tvEmailReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder2));
                    }

                }
            }
        });

        cbMiscReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                visibleCallNote();
                if (isChecked) {
                    tvOther.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                    cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgotherreminder));
                } else {
                    if (!isAllReminderType()) {
                        cbMiscReminder.setChecked(true);
                        tvOther.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));
                        visibleCallNote();
                    } else {
                        tvOther.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgotherreminder2));
                    }

                }
            }
        });

        cbCallNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                visibleCallNote();
                if (isChecked) {
                    tvCallNote.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                    cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote));
                } else {
                    if (!isAllReminderType()) {
                        cbCallNote.setChecked(true);
                        tvCallNote.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote));
                        visibleCallNote();
                    } else {
                        tvCallNote.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
                        cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote2));
                    }

                }
            }
        });
        rbDateTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbDateTime.isChecked()) {
                    llRemindIn.setVisibility(View.GONE);
                    llDate.setVisibility(View.VISIBLE);
                }
            }
        });
        rbRemindIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbRemindIn.isChecked()) {
                    llRemindIn.setVisibility(View.VISIBLE);
                    llDate.setVisibility(View.GONE);
                }
            }
        });

        edtContactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lstContactSuggestion.clear();

                if (charSequence.length() > 0) {
                    cvContactSuggestion.setVisibility(View.VISIBLE);
                    rvContactSuggestion.setVisibility(View.VISIBLE);

                } else {
                    cvContactSuggestion.setVisibility(View.GONE);
                    rvContactSuggestion.setVisibility(View.GONE);
                }

                for (ContactDetailModel contactDetailModel : StaticData.lstPhoneContact) {
                    if (contactDetailModel.getContactName().length() >= charSequence.length()) {
                        if (contactDetailModel.getContactName().substring(0, charSequence.length()).equalsIgnoreCase(charSequence.toString())) {
                            ContactDetailModel contactDetailModel1 = new ContactDetailModel();
                            contactDetailModel1.setContactName(contactDetailModel.getContactName());
                            contactDetailModel1.setContactImage(contactDetailModel.getContactImage());
                            contactDetailModel1.setContactNumber(contactDetailModel.getContactNumber());
                            contactDetailModel1.setId(contactDetailModel.getId());
                            lstContactSuggestion.add(contactDetailModel1);
                        }
                    }
                }

                contactSuggestionAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddReminderActivity.this);
        rvContactSuggestion.setLayoutManager(layoutManager);
        contactSuggestionAdapter = new ContactSuggestionAdapter(lstContactSuggestion, this, this);


        rvContactSuggestion.setAdapter(contactSuggestionAdapter);

    }

    //request permisiion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CALENDAR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                boolean isCalendar = appPref.getValue(getString(R.string.calendar_sync), false);
                cbAddCalendar.setChecked(isCalendar);
            } else {
                if (PermissionUtils.shouldShowRequestPermissions(AddReminderActivity.this, permissions)) {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "Calendar Permission is required for reminder add to google calendar.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.requestPermission(AddReminderActivity.this, calendar_permissions, MY_CALENDAR_PERMISSION);
                        }
                    });
                } else {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "Calendar Permission is required for reminder add to google calendar.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSettingScreen(MY_CALENDAR_PERMISSION);
                        }
                    });
                }

            }
        }
        if (requestCode == MY_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                if (saveData()) {
                    StaticData.isSavedReminder = true;
                    if (REQUEST_CODE_AFTER_WINDOW == 21) {
                        Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
                        AdUtils.displayIntestial();
                        navigateToDifferentScreen(intent, true);


                    } else {
                        AdUtils.displayIntestial();
                        finish();
                    }
                }
            } else {
                if (PermissionUtils.shouldShowRequestPermissions(AddReminderActivity.this, permissions)) {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "Phone Permission is required for Manage and Display CallNotes.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.requestPermission(AddReminderActivity.this, phone_permissions, MY_PHONE_PERMISSION);
                        }
                    });
                } else {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "Phone Permission is required for Manage and Display CallNotes.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSettingScreen(MY_PHONE_PERMISSION);
                        }
                    });
                }

            }
        }
        if (requestCode == CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                navigateToCallLogActivity();
            } else {
                if (PermissionUtils.shouldShowRequestPermissions(AddReminderActivity.this, permissions)) {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "CALL LOG Permission is required for get your device Call log.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.requestPermission(AddReminderActivity.this, phone_permissions, CALL_LOG);
                        }
                    });
                } else {
                    PopUtils.showDialogforPermission(AddReminderActivity.this, "CALL LOG Permission is required for get your device Call log.", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSettingScreen(CALL_LOG);
                        }
                    });
                }

            }
        }

    }

    public void openSettingScreen(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestCode);
    }

    public void allViewClickable(boolean enable) {

        llConfirmMsg.setClickable(enable);
        edtContactName.setClickable(enable);
        edtNote.setClickable(enable);
        edtNumber.setClickable(enable);
        edtEmail.setClickable(enable);
        edtContactName.setFocusable(enable);
        edtNote.setFocusable(enable);
        edtNumber.setFocusable(enable);
        edtEmail.setFocusable(enable);
        edtContactName.setFocusableInTouchMode(enable);
        edtEmail.setFocusableInTouchMode(enable);
        edtNote.setFocusableInTouchMode(enable);
        edtNumber.setFocusableInTouchMode(enable);
        cbComfirmMsg.setClickable(enable);
        cbCallReminder.setClickable(enable);
        cbPersistantTone.setClickable(enable);
        cbAddCalendar.setClickable(enable);
        cbCallNote.setClickable(enable);
        cbEmailReminder.setClickable(enable);
        cbMiscReminder.setClickable(enable);
        cbSmsReminder.setClickable(enable);
        rbRemindIn.setClickable(enable);
        rbDateTime.setClickable(enable);
        tvTone.setClickable(enable);
        tvRemindTime.setClickable(enable);
        tvRepeatInterval.setClickable(enable);
        tvRepeatForever.setClickable(enable);
        tvFri.setClickable(enable);
        tvWen.setClickable(enable);
        tvThu.setClickable(enable);
        tvTue.setClickable(enable);
        tvSat.setClickable(enable);
        tvSun.setClickable(enable);
        tvMon.setClickable(enable);
        tvTime.setClickable(enable);
        tvDate.setClickable(enable);
        ivCallLog.setClickable(enable);
        ivContactList.setClickable(enable);
        ivNote.setClickable(enable);
        ivRingtone.setClickable(enable);
        ivRepeat.setClickable(enable);
        llName.setClickable(enable);
        llDateAndTime.setClickable(enable);
        llDate.setClickable(enable);
        llAddCalendar.setClickable(enable);
        llRepeatForever.setClickable(enable);
        llEmail.setClickable(enable);
        llTone.setClickable(enable);
        llRepeatInterval.setClickable(enable);
        llRemindIn.setClickable(enable);
        llWeekDaysDisplay.setClickable(enable);


    }

    private void upDateReminderData() {
        reminderDataModel = getIntent().getParcelableExtra(getString(R.string.reminderData));
        if (reminderDataModel.getIsCall() == 1) {
            tvCallReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder));
            cbCallReminder.setChecked(true);
        } else {
            tvCallReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbCallReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallreminder2));
            cbCallReminder.setChecked(false);
        }
        if (reminderDataModel.getIsSms() == 1) {
            tvSmsReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder));
            cbSmsReminder.setChecked(true);
        } else {
            tvSmsReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbSmsReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgsmsreminder2));
            cbSmsReminder.setChecked(false);
        }
        if (reminderDataModel.getIsEmail() == 1) {
            tvEmailReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder));
            cbEmailReminder.setChecked(true);
        } else {
            tvEmailReminder.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbEmailReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgemailreminder2));
            cbEmailReminder.setChecked(false);
        }
        if (reminderDataModel.getIsMisc() == 1) {
            tvOther.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgotherreminder));
            cbMiscReminder.setChecked(true);
        } else {
            tvOther.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbMiscReminder.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgotherreminder2));

            cbMiscReminder.setChecked(false);
        }
        if (reminderDataModel.getIsCallNote() == 1) {
            tvCallNote.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote));
            cbCallNote.setChecked(true);
        } else {
            tvCallNote.setTextColor(ContextCompat.getColor(AddReminderActivity.this, R.color.colorPrimary));
            cbCallNote.setBackground(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.imgcallnote2));
            cbCallNote.setChecked(false);
        }
        visibleCallNote();
        if (!reminderDataModel.getReminderName().equals("")) {
            edtContactName.setText(reminderDataModel.getReminderName());
        }
        if (!reminderDataModel.getReminderNumber().equals("")) {
            edtNumber.setText(reminderDataModel.getReminderNumber());
        }
        if (!reminderDataModel.getReminderEmail().equals("")) {
            edtEmail.setText(reminderDataModel.getReminderEmail());
        }
        if (!reminderDataModel.getReminderNote().equals("")) {
            edtNote.setText(reminderDataModel.getReminderNote());
        }
        if (reminderDataModel.getReminderIcon() != null) {
            photo = reminderDataModel.getReminderIcon();
        } else {
            photo = null;
        }
        if (reminderDataModel.getReminderIn().equals("") || reminderDataModel.getReminderIn() != null) {
            rbDateTime.setChecked(true);
            llRemindIn.setVisibility(View.GONE);
            llDate.setVisibility(View.VISIBLE);

        } else {
            llRemindIn.setVisibility(View.VISIBLE);
            llDate.setVisibility(View.GONE);
            rbRemindIn.setChecked(true);
            tvRemindTime.setText(reminderDataModel.getReminderIn());
        }
        reminderDate = reminderDataModel.getUpdateDate();
        String repeatDate1 = TimeUtils.getDateIn24HrsFormat(reminderDate);
        tvDate.setText(repeatDate1.substring(0, repeatDate1.indexOf(" ")));
        tvTime.setText(repeatDate1.substring(repeatDate1.indexOf(" ") + 1));
        if (!reminderDataModel.getReminderTone().equals("")) {
            String tone = reminderDataModel.getReminderTone();
            ringPath = tone.substring(0, tone.indexOf(" "));
            tvTone.setText(tone.substring(tone.indexOf(" ") + 1));
        } else {
            String defaultTone = appPref.getValue("defaultRingTone", "");
            ringPath = defaultTone.substring(0, defaultTone.indexOf(" "));
            tvTone.setText(defaultTone.substring(defaultTone.indexOf(" ") + 1));

        }
        if (!reminderDataModel.getRepeatInterval().equals("")) {

            if (reminderDataModel.getRepeatInterval().equalsIgnoreCase(getString(R.string.daily))) {
                isDaily = false;
                tvRepeatInterval.setText(getString(R.string.daily));
            } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase(getString(R.string.weekly))) {
                isWeekly = false;
                tvRepeatInterval.setText(getString(R.string.weekly));
            } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase(getString(R.string.monthly))) {
                isMonthly = false;
                tvRepeatInterval.setText(getString(R.string.monthly));
            } else if (reminderDataModel.getRepeatInterval().equalsIgnoreCase(getString(R.string.yearly))) {
                isYearly = false;
                tvRepeatInterval.setText(getString(R.string.yearly));
            } else {

                llWeekDaysDisplay.setVisibility(View.VISIBLE);
                tvRepeatInterval.setText(getString(R.string.onDaysofWeek));
                for (String str : reminderDataModel.getRepeatInterval().split(",")) {
                    switch (str) {
                        case "SU":
                            setChangeBackground(tvSun, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isSun = false;
                            break;
                        case "M":
                            setChangeBackground(tvMon, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isMon = false;
                            break;
                        case "T":
                            setChangeBackground(tvTue, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isTue = false;
                            break;
                        case "W":
                            setChangeBackground(tvWen, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isWen = false;
                            break;
                        case "TH":
                            setChangeBackground(tvThu, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isThu = false;
                            break;
                        case "F":
                            setChangeBackground(tvFri, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isFri = false;
                            break;
                        case "S":
                            setChangeBackground(tvSat, R.drawable.drawable_weekroundbuttongray, R.color.white);
                            isSat = false;
                            break;
                        case "":

                            break;

                    }
                }
                getDisplayWeekDays();
            }
        } else {
            tvRepeatInterval.setText(getString(R.string.doNotrepeat));
        }
        if (!reminderDataModel.getRepeatForever().equals("")) {

            if (reminderDataModel.getRepeatForever().equalsIgnoreCase(getString(R.string.forever))) {
                tvRepeatForever.setText(getString(R.string.noEndDate));
            } else {
                forever = Long.parseLong(reminderDataModel.getRepeatForever());
                String repeatDate = TimeUtils.getDateIn24HrsFormat(forever);
                tvRepeatForever.setText(getString(R.string.expiresOn) + repeatDate.substring(0, repeatDate.indexOf(" ")));
            }
        }
        if (reminderDataModel.getIsCalenderSync() == 1) {
            cbAddCalendar.setChecked(true);
        } else {
            cbAddCalendar.setChecked(false);
        }
        if (reminderDataModel.getIsPersistantTone() == 1) {
            cbPersistantTone.setChecked(true);
        } else {
            cbPersistantTone.setChecked(false);
        }
        /*if (reminderDataModel.getIsConfirmMsg() == 1) {
            cbComfirmMsg.setChecked(true);
        } else {
            cbComfirmMsg.setChecked(false);
        }*/

    }

    public boolean isAllReminderType() {
        if (cbCallNote.isChecked() || cbCallReminder.isChecked() || cbSmsReminder.isChecked() ||
                cbEmailReminder.isChecked() || cbMiscReminder.isChecked()) {
            return true;
        } else {
            return false;
        }
    }


    private void visibleCallNote() {
        llMainData.setVisibility(View.VISIBLE);

        if (cbCallReminder.isChecked() || cbSmsReminder.isChecked() || cbEmailReminder.isChecked() || cbMiscReminder.isChecked()) {

            llBottomLayout.setVisibility(View.VISIBLE);
        } else {
            llBottomLayout.setVisibility(View.GONE);
        }
        if (cbEmailReminder.isChecked()) {
            llEmail.setVisibility(View.VISIBLE);
        } else {
            llEmail.setVisibility(View.GONE);
        }
        if (cbCallReminder.isChecked() || cbSmsReminder.isChecked() || cbCallNote.isChecked()) {
            llNumber.setVisibility(View.VISIBLE);
        } else {
            llNumber.setVisibility(View.GONE);
        }
        if (cbMiscReminder.isChecked()) {
            if (cbCallReminder.isChecked() || cbSmsReminder.isChecked() || cbCallNote.isChecked() || cbEmailReminder.isChecked()) {
                llName.setVisibility(View.VISIBLE);
            } else {
                llName.setVisibility(View.GONE);
            }
        } else {
            llName.setVisibility(View.VISIBLE);
        }
        if (cbSmsReminder.isChecked()) {
            llConfirmMsg.setVisibility(View.GONE);
        } else {
            llConfirmMsg.setVisibility(View.GONE);
        }
    }

    public void openTimePicker(int hour, int minute) {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        reminderDate = calendar.getTimeInMillis();
                        String formattedDate = TimeUtils.getDateIn24HrsFormat(reminderDate);
                        tvTime.setText(formattedDate.substring(formattedDate.indexOf(" ") + 1));
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public void openDatePicker(int day, int month, int year) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if (isDateTag) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            reminderDate = calendar.getTimeInMillis();
                            String formattedDate = TimeUtils.getDateIn24HrsFormat(reminderDate);
                            alarmDate = formattedDate.substring(0, formattedDate.indexOf(" "));
                            tvDate.setText(formattedDate.substring(0, formattedDate.indexOf(" ")));
                        } else {
                            foreverDateCalender.set(Calendar.YEAR, year);
                            foreverDateCalender.set(Calendar.MONTH, monthOfYear);
                            foreverDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            forever = foreverDateCalender.getTimeInMillis();
                            String formattedDate = TimeUtils.getDateIn24HrsFormat(forever);
                            foreverDate = formattedDate.substring(0, formattedDate.indexOf(" "));
                            tvRepeatForever.setText(getString(R.string.expiresOn) + formattedDate.substring(0, formattedDate.indexOf(" ")));
                            foreverDate = String.valueOf(forever);
                        }
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void popmenuRepeatForever() {
        PopupMenu popupMenu = new PopupMenu(this, tvRepeatForever);
        {
            popupMenu.getMenuInflater().inflate(R.menu.menu_repeatforever, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_noEndDate) {
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                    } else if (item.getItemId() == R.id.menu_ToDate) {


                        forever = foreverDateCalender.getTimeInMillis();
                        String repeatDate = TimeUtils.getDateIn24HrsFormat(forever);
                        mDay = Integer.parseInt(repeatDate.substring(0, 2));
                        mMonth = (Integer.parseInt(repeatDate.substring(3, 5))) - 1;
                        mYear = Integer.parseInt(repeatDate.substring(6, 10));
                        isDateTag = false;
                        openDatePicker(mDay, mMonth, mYear);

                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }

    private void popmenuRepeatInterVal() {
        PopupMenu popupMenu = new PopupMenu(this, tvRepeatInterval);
        {
            popupMenu.getMenuInflater().inflate(R.menu.menu_repeatinterval, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_DoNotRepeat) {
                        llWeekDaysDisplay.setVisibility(View.GONE);
                        tvRepeatInterval.setText(R.string.doNotrepeat);
                        llRepeatForever.setVisibility(View.GONE);
                        llForeverView.setVisibility(View.GONE);
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = "";
                        onDaysOfWeektrue();
                    } else if (item.getItemId() == R.id.menu_Daily) {
                        tvRepeatInterval.setText(getString(R.string.daily));
                        llWeekDaysDisplay.setVisibility(View.GONE);
                        llRepeatForever.setVisibility(View.VISIBLE);
                        llForeverView.setVisibility(View.VISIBLE);
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                        onDaysOfWeektrue();
                    } else if (item.getItemId() == R.id.menu_DayOfWeek) {
                        llWeekDaysDisplay.setVisibility(View.VISIBLE);
                        tvRepeatInterval.setText(R.string.onDaysofWeek);
                        llRepeatForever.setVisibility(View.VISIBLE);
                        llForeverView.setVisibility(View.VISIBLE);
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                        onDaysOfWeekfalse();
                        getDisplayWeekDays();
                    } else if (item.getItemId() == R.id.menu_Weekly) {
                        tvRepeatInterval.setText(getString(R.string.weekly));
                        llWeekDaysDisplay.setVisibility(View.GONE);
                        llRepeatForever.setVisibility(View.VISIBLE);
                        llForeverView.setVisibility(View.VISIBLE);
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                        onDaysOfWeektrue();
                    } else if (item.getItemId() == R.id.menu_Monthly) {
                        llWeekDaysDisplay.setVisibility(View.GONE);
                        llRepeatForever.setVisibility(View.VISIBLE);
                        llForeverView.setVisibility(View.VISIBLE);
                        tvRepeatInterval.setText(getString(R.string.monthly));
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                        onDaysOfWeektrue();
                    } else if (item.getItemId() == R.id.menu_Yearly) {
                        llWeekDaysDisplay.setVisibility(View.GONE);
                        llRepeatForever.setVisibility(View.VISIBLE);
                        llForeverView.setVisibility(View.VISIBLE);
                        tvRepeatInterval.setText(getString(R.string.yearly));
                        tvRepeatForever.setText(getString(R.string.noEndDate));
                        foreverDate = getString(R.string.forever);
                        onDaysOfWeektrue();
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }

    private void getDisplayWeekDays() {
        llWeekDaysDisplay.setVisibility(View.VISIBLE);

        if (!isMon) {
            setChangeBackground(tvMon, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvMon, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isTue) {
            setChangeBackground(tvTue, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvTue, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isWen) {
            setChangeBackground(tvWen, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvWen, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isThu) {
            setChangeBackground(tvThu, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvThu, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isFri) {
            setChangeBackground(tvFri, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvFri, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isSat) {
            setChangeBackground(tvSat, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvSat, R.drawable.drawable_weekroundbutton, R.color.gray);
        }
        if (!isSun) {
            setChangeBackground(tvSun, R.drawable.drawable_weekroundbuttongray, R.color.white);
        } else {
            setChangeBackground(tvSun, R.drawable.drawable_weekroundbutton, R.color.gray);
        }

    }

    public void setChangeBackground(CustomTextView customTextView, int drawable, int color) {
        customTextView.setBackground(ContextCompat.getDrawable(this, drawable));
        customTextView.setTextColor(ContextCompat.getColor(this, color));
    }

    public void onDaysOfWeekfalse() {
        isMon = false;
        isTue = false;
        isWen = false;
        isThu = false;
        isFri = false;
        isSat = false;
        isSun = false;

    }

    public void onDaysOfWeektrue() {
        isMon = true;
        isTue = true;
        isWen = true;
        isThu = true;
        isFri = true;
        isSat = true;
        isSun = true;

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

    public void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private void navigateToCallLogActivity() {
        Intent intent = new Intent(AddReminderActivity.this, CallLogActivity.class);
        startActivityForResult(intent, PICK_NUMBER);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void showTapToSpeakInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @OnClick({R.id.ivNote, R.id.ivHome, R.id.fab, R.id.tvEdit, R.id.llRemindIn, R.id.ivContactList, R.id.ivCallLog, R.id.llRepeat_forever, R.id.tvDate, R.id.tvTime, R.id.llRepeatInterval, R.id.tvMon, R.id.tvTue, R.id.tvWen, R.id.tvThu, R.id.tvFri, R.id.tvSat, R.id.tvSun, R.id.llTone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHome:

                onBackPressed();
                break;
            case R.id.ivNote:
                StaticUtils.hideKeyboard(AddReminderActivity.this);
                if (StaticUtils.isNetworkAvailable(AddReminderActivity.this)) {

                    showTapToSpeakInput();
                } else {
                    dialogNoInternet();
                }

                break;
            case R.id.ivContactList:
                getContact();
                break;
            case R.id.tvEdit:
                tvEdit.setVisibility(View.GONE);
                allViewClickable(true);
                edtContactName.requestFocus();
                edtContactName.setFocusable(true);
                edtContactName.setCursorVisible(true);
                StaticUtils.showKeyboard(AddReminderActivity.this, edtContactName);

                break;
            case R.id.ivCallLog:
                if (hasPermissions(AddReminderActivity.this, phone_permissions)) {
                    navigateToCallLogActivity();
                } else {
                    requestPermission(AddReminderActivity.this, phone_permissions, CALL_LOG);
                }
                break;
            case R.id.llRemindIn:
                PopUtils.showRemindInDialog(AddReminderActivity.this, tvRemindTime.getText().toString(), new RemindInSelection() {
                    @Override
                    public void onRemindInSelection(String value) {
                        tvRemindTime.setText(value);
                    }
                });
                break;

            case R.id.tvDate:
                if (REQUEST_CODE != 18)
                    reminderDate = calendar.getTimeInMillis();
                String repeatDate = TimeUtils.getDateIn24HrsFormat(reminderDate);
                mDay = Integer.parseInt(repeatDate.substring(0, 2));
                mMonth = (Integer.parseInt(repeatDate.substring(3, 5))) - 1;
                mYear = Integer.parseInt(repeatDate.substring(6, 10));
                isDateTag = true;
                openDatePicker(mDay, mMonth, mYear);
                break;
            case R.id.tvTime:
                if (REQUEST_CODE != 18)
                    reminderDate = calendar.getTimeInMillis();
                String repeatTime = TimeUtils.getDateIn24HrsFormat(reminderDate);
                mHour = Integer.parseInt(repeatTime.substring(repeatTime.indexOf(" ") + 1, repeatTime.indexOf(":")));
                mMinute = Integer.parseInt(repeatTime.substring(repeatTime.indexOf(":") + 1));
                openTimePicker(mHour, mMinute);
                break;
            case R.id.llRepeatInterval:
                popmenuRepeatInterVal();
                break;
            case R.id.llRepeat_forever:
                popmenuRepeatForever();
                break;
            case R.id.tvMon:
                if (isMon) {
                    setChangeBackground(tvMon, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isMon = false;
                } else {
                    setChangeBackground(tvMon, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isMon = true;
                }
                break;
            case R.id.tvTue:
                if (isTue) {
                    setChangeBackground(tvTue, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isTue = false;
                } else {
                    setChangeBackground(tvTue, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isTue = true;
                }
                break;
            case R.id.tvWen:
                if (isWen) {
                    setChangeBackground(tvWen, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isWen = false;
                } else {
                    setChangeBackground(tvWen, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isWen = true;
                }
                break;
            case R.id.tvThu:
                if (isThu) {
                    setChangeBackground(tvThu, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isThu = false;
                } else {
                    setChangeBackground(tvThu, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isThu = true;
                }
                break;
            case R.id.tvFri:
                if (isFri) {
                    setChangeBackground(tvFri, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isFri = false;
                } else {
                    setChangeBackground(tvFri, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isFri = true;
                }
                break;
            case R.id.tvSat:
                if (isSat) {
                    setChangeBackground(tvSat, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isSat = false;
                } else {
                    setChangeBackground(tvSat, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isSat = true;
                }
                break;
            case R.id.tvSun:
                if (isSun) {
                    setChangeBackground(tvSun, R.drawable.drawable_weekroundbuttongray, R.color.white);
                    isSun = false;
                } else {
                    setChangeBackground(tvSun, R.drawable.drawable_weekroundbutton, R.color.gray);
                    isSun = true;
                }
                break;
            case R.id.llTone:
                getRingTone();
                break;
            case R.id.fab:
                if (hasPermissions(AddReminderActivity.this, phone_permissions)) {
                    if (saveData()) {
                        StaticData.isSavedReminder = true;
                        if (REQUEST_CODE_AFTER_WINDOW == 21) {
                            Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
                            AdUtils.displayIntestial();
                            navigateToDifferentScreen(intent, true);


                        } else {
                            AdUtils.displayIntestial();
                            finish();
                        }
                    }
                } else {
                    requestPermission(AddReminderActivity.this, phone_permissions, MY_PHONE_PERMISSION);
                }


                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (REQUEST_CODE_AFTER_WINDOW == 21) {
            Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
            navigateToDifferentScreen(intent, true);
            AdUtils.displayIntestial();

        } else {
            AdUtils.displayIntestial();
            finish();
        }
    }

    public void dialogNoInternet() {
        final Dialog dialog = new Dialog(AddReminderActivity.this);
        dialog.setContentView(R.layout.dialog_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvbtnOK = (TextView) dialog.findViewById(R.id.tvbtnOK);


        tvbtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean saveData() {

        if (tvRepeatInterval.getText().toString().equalsIgnoreCase(getString(R.string.daily))) {
            repeat_interval = getString(R.string.daily);
        }
        if (tvRepeatInterval.getText().toString().equalsIgnoreCase(getString(R.string.monthly))) {
            repeat_interval = getString(R.string.monthly);
        }
        if (tvRepeatInterval.getText().toString().equalsIgnoreCase(getString(R.string.weekly))) {
            repeat_interval = getString(R.string.weekly);
        }
        if (tvRepeatInterval.getText().toString().equalsIgnoreCase(getString(R.string.yearly))) {
            repeat_interval = getString(R.string.yearly);
        }
        if (!isMon) {
            repeat_interval = repeat_interval + "M,";
        }
        if (!isTue) {
            repeat_interval = repeat_interval + "T,";
        }
        if (!isWen) {
            repeat_interval = repeat_interval + "W,";
        }
        if (!isThu) {
            repeat_interval = repeat_interval + "TH,";
        }
        if (!isFri) {
            repeat_interval = repeat_interval + "F,";
        }
        if (!isSat) {
            repeat_interval = repeat_interval + "S,";
        }
        if (!isSun) {
            repeat_interval = repeat_interval + "SU,";
        }
        reminderDataModel.setRepeatInterval(repeat_interval);
        reminderDataModel.setRepeatForever(foreverDate);
        reminderDataModel.setIsComplete(0);

        if (cbCallReminder.isChecked() || cbSmsReminder.isChecked() ||
                cbEmailReminder.isChecked() || cbMiscReminder.isChecked()) {
            if (rbDateTime.isChecked()) {
                Calendar calendar = Calendar.getInstance();
                String reminder = TimeUtils.getDateIn24HrsFormat(reminderDate);
                String currentDat = TimeUtils.getDateIn24HrsFormat(calendar.getTimeInMillis());
                Date date1 = null, date2 = null;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    date1 = dateFormat.parse(reminder);
                    date2 = dateFormat.parse(currentDat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1.compareTo(date2) < 0) {
                    PopUtils.showErrorDialog(AddReminderActivity.this, "Please Enter upcomming time.");
                    return false;
                } else {
                    reminderDataModel.setReminderDate(reminderDate);
                    reminderDataModel.setUpdateDate(reminderDate);
                    reminderDataModel.setReminderIn("");
                }
            } else {
                calendar = Calendar.getInstance();
                String duration = tvRemindTime.getText().toString();
                if (duration.equals(getString(R.string._5_minutes))) {
                    calendar.add(Calendar.MINUTE, 5);
                } else if (duration.equals(getString(R.string._10_minutes))) {
                    calendar.add(Calendar.MINUTE, 10);
                } else if (duration.equals(getString(R.string._15_minutes))) {
                    calendar.add(Calendar.MINUTE, 15);
                } else if (duration.equals(getString(R.string._30_minutes))) {
                    calendar.add(Calendar.MINUTE, 30);
                } else if (duration.equals(getString(R.string._1_hour))) {
                    calendar.add(Calendar.MINUTE, 60);
                }
                long date = calendar.getTimeInMillis();
                reminderDataModel.setReminderDate(date);
                reminderDataModel.setUpdateDate(date);
                reminderDataModel.setReminderIn(duration);
            }


        } else {
            reminderDataModel.setReminderDate(reminderDate);
            reminderDataModel.setUpdateDate(reminderDate);
            reminderDataModel.setReminderIn("");
        }
        reminderDataModel.setReminderTone(ringPath + " " + tvTone.getText().toString());
        if (cbCallReminder.isChecked()) {
            if (edtContactName.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().length() < 8) {
                PopUtils.showErrorDialog(AddReminderActivity.this, getString(R.string.errorMsgCallReminder));
                return false;
            } else {
                setReminderModelData();
                reminderDataModel.setIsCall(1);
            }

        } else {
            reminderDataModel.setIsCall(0);
        }
        if (cbSmsReminder.isChecked()) {
            if (edtContactName.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().length() < 8) {
                PopUtils.showErrorDialog(AddReminderActivity.this, getString(R.string.errorMsgSMS));
                return false;
            } else {
                setReminderModelData();
                reminderDataModel.setIsSms(1);
            }
        } else {
            reminderDataModel.setIsSms(0);
        }
        if (cbMiscReminder.isChecked()) {
            if (edtNote.getText().toString().trim().equals("")) {
                PopUtils.showErrorDialog(AddReminderActivity.this, getString(R.string.errorMsgMisc));
                return false;
            } else {
                setReminderModelData();
                reminderDataModel.setIsMisc(1);
            }
        } else {
            reminderDataModel.setIsMisc(0);
        }
        if (cbCallNote.isChecked()) {
            if (edtContactName.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().equals("") || edtNumber.getText().toString().trim().length() < 8 || edtNote.getText().toString().trim().equals("")) {
                PopUtils.showErrorDialog(AddReminderActivity.this, getString(R.string.errorMsgCallNote));
                return false;
            } else {
                setReminderModelData();
                reminderDataModel.setIsCallNote(1);
                reminderDataModel.setIsCallNoteShow(1);
            }
        } else {
            reminderDataModel.setIsCallNote(0);
        }
        if (cbEmailReminder.isChecked()) {
            if (edtEmail.getText().toString().trim().equals("") || edtContactName.getText().toString().trim().equals("")) {
                PopUtils.showErrorDialog(AddReminderActivity.this, getString(R.string.errorMsgEmail));
                return false;
            } else {
                setReminderModelData();
                reminderDataModel.setIsEmail(1);
            }

        } else {
            reminderDataModel.setIsEmail(0);
        }
        if (cbAddCalendar.isChecked()) {
            if (cbCallReminder.isChecked() || cbSmsReminder.isChecked() ||
                    cbEmailReminder.isChecked() || cbMiscReminder.isChecked()) {
                long calendarID = 0;
                if (hasPermissions(AddReminderActivity.this, calendar_permissions)) {
                    calendarID = StaticUtils.addRemindersToCalender(AddReminderActivity.this, reminderDataModel.getReminderName(), reminderDataModel.getReminderNote()
                            , 1, reminderDataModel.getUpdateDate(), true);
                }
                reminderDataModel.setIsCalenderSync(1);
                reminderDataModel.setCalendarId(calendarID);
            } else {
                reminderDataModel.setIsCalenderSync(0);
            }

        } else {
            reminderDataModel.setIsCalenderSync(0);
        }
        if (cbPersistantTone.isChecked()) {
            reminderDataModel.setIsPersistantTone(1);
        } else {
            reminderDataModel.setIsPersistantTone(0);
        }
       /* if (cbComfirmMsg.isChecked()) {
            reminderDataModel.setIsConfirmMsg(1);
        } else {
            reminderDataModel.setIsConfirmMsg(0);
        }*/
        reminderDataModel.setIsConfirmMsg(0);
        if (REQUEST_CODE == 18) {
            dataBaseClass.updateCallReminder(reminderDataModel);
        } else {
            dataBaseClass.insertCallReminder(reminderDataModel);
        }

        return true;
    }

    public void setReminderModelData() {
        reminderDataModel.setReminderName(edtContactName.getText().toString());
        reminderDataModel.setReminderNumber(edtNumber.getText().toString());
        reminderDataModel.setReminderIcon(photo);
        reminderDataModel.setReminderEmail(edtEmail.getText().toString());
        reminderDataModel.setReminderNote(edtNote.getText().toString());
    }

    private class GetDataContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            StaticData.lstPhoneContact.clear();
            StaticData.lstPhoneContact = StaticUtils.getAllDeviceContacts(AddReminderActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
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
                        ringPath = String.valueOf(data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
                        tvTone.setText(ringtone.getTitle(getApplicationContext()));
                    } catch (Exception e) {
                    }
                    break;
                case (PICK_CONTACT):

                    Uri contactData = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(contactData,
                            null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String name = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        photo = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        edtContactName.setText(name);
                        edtNumber.setText(number);
                        if (photo != null) {
                            Glide.with(context)
                                    .load(Uri.parse(photo)) // Uri of the picture
                                    .into(ivProfilePhoto);
                        } else {
                            ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.ic_profile));
                        }
                    }
                    cvContactSuggestion.setVisibility(View.GONE);
                    rvContactSuggestion.setVisibility(View.GONE);
                    StaticUtils.hideKeyboard(AddReminderActivity.this);
                    break;
                case (PICK_NUMBER):
                    String name = data.getStringExtra("name");
                    String number = data.getStringExtra("number");
                    photo = data.getStringExtra("photo");
                    edtContactName.setText(name);
                    edtNumber.setText(number);
                    if (photo != null) {
                        Glide.with(context)
                                .load(Uri.parse(photo)) // Uri of the picture
                                .into(ivProfilePhoto);
                    } else {
                        ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.ic_profile));
                    }
                    cvContactSuggestion.setVisibility(View.GONE);
                    rvContactSuggestion.setVisibility(View.GONE);
                    StaticUtils.hideKeyboard(AddReminderActivity.this);
                    break;

                case MY_PHONE_PERMISSION:
                    if (hasPermissions(AddReminderActivity.this, phone_permissions)) {
                        if (saveData()) {
                            StaticData.isSavedReminder = true;
                            if (REQUEST_CODE_AFTER_WINDOW == 21) {
                                Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
                                AdUtils.displayIntestial();
                                navigateToDifferentScreen(intent, true);


                            } else {
                                AdUtils.displayIntestial();
                                finish();
                            }
                        }
                    }
                    break;
                case MY_CALENDAR_PERMISSION:
                    if (hasPermissions(AddReminderActivity.this, calendar_permissions)) {
                        boolean isCalendar = appPref.getValue(getString(R.string.calendar_sync), false);
                        cbAddCalendar.setChecked(isCalendar);
                    }
                    break;
                case CALL_LOG:
                    if (hasPermissions(AddReminderActivity.this, phone_permissions)) {
                        navigateToCallLogActivity();
                    }
                    break;
                case SPEECH_REQUEST_CODE:

                    if (resultCode == RESULT_OK) {
                        List<String> results = data.getStringArrayListExtra(
                                RecognizerIntent.EXTRA_RESULTS);
                        String spokenText = results.get(0);
                        edtNote.setText(edtNote.getText().append(" " + spokenText));
                    } else {
                    }
                    break;
            }
        }
    }


    @Override
    public void cbClick(int position) {
        ContactDetailModel contactDetailModel = lstContactSuggestion.get(position);
        String name = contactDetailModel.getContactName();
        String number = contactDetailModel.getContactNumber();
        photo = contactDetailModel.getContactImage();
        edtContactName.setText(name);
        edtNumber.setText(number);
        if (photo != null) {
            Glide.with(context)
                    .load(Uri.parse(photo)) // Uri of the picture
                    .into(ivProfilePhoto);
        } else {
            ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(AddReminderActivity.this, R.drawable.ic_profile));
        }
       /* spContactSuggestion.setVisibility(View.GONE);*/
        cvContactSuggestion.setVisibility(View.GONE);
        rvContactSuggestion.setVisibility(View.GONE);
        StaticUtils.hideKeyboard(AddReminderActivity.this);
    }
}
