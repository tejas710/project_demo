package com.gonext.callreminder.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.CursorJoiner;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.AddReminderActivity;
import com.gonext.callreminder.activities.MainActivity;
import com.gonext.callreminder.adapter.CallReminderAdapter;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.view.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.gonext.callreminder.utils.PermissionUtils.hasPermissions;

/**
 * Created by sellnews on 9/28/17.
 */

public class ReminderFragment extends Fragment implements CallReminderAdapter.Clickable {
    static ReminderFragment reminderFragment;
    DataBaseClass dataBaseClass;
    Unbinder unbinder;
    AppPref appPref;
    int page;
    ArrayList<ReminderDataModel> lstReminder = new ArrayList<>();
    ArrayList<ReminderDataModel> lstReminderTemp = new ArrayList<>();
    CallReminderAdapter callReminderAdapter;
    MainActivity mainActivity;
    CountDownTimer countDownTimer;
    @BindView(R.id.rvReminder)
    CustomRecyclerView rvReminder;
    @BindView(R.id.llEmptyViewMain)
    LinearLayout llEmptyViewMain;
    String[] calendar_permissions = new String[]{
            Manifest.permission.READ_CALENDAR};

    public static ReminderFragment newInstance(int page) {
        if (reminderFragment == null) {
            reminderFragment = new ReminderFragment();
            Bundle args = new Bundle();
            args.putInt("page", page);
            reminderFragment.setArguments(args);
        }

        return reminderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = getArguments().getInt("page", 0);
        appPref = AppPref.getInstance(getActivity());
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        unbinder = ButterKnife.bind(this, view);
        initilaizeRvReminder();
        mainActivity = (MainActivity) getActivity();
        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(getActivity().TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        getAllReminder();
        rvReminder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (dy > 0) {
                    // scroll down
                    if (mainActivity.fab != null) {
                        mainActivity.fab.setVisibility(View.INVISIBLE);
                    }

                } else if (dy < 0) {
                    // scroll up
                    if (mainActivity.fab != null) {
                        mainActivity.fab.setVisibility(View.VISIBLE);
                    }

                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            // fabAddNewPlaylist.show();
                            if (mainActivity.fab != null) {
                                mainActivity.fab.setVisibility(View.VISIBLE);
                            }

                        }
                    }.start();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    public void initilaizeRvReminder() {
        dataBaseClass = new DataBaseClass(getActivity());
        callReminderAdapter = new CallReminderAdapter(lstReminder, getActivity(), this);
        rvReminder.setItemAnimator(new DefaultItemAnimator());
        rvReminder.setAdapter(callReminderAdapter);
        rvReminder.setEmptyView(llEmptyViewMain);

    }

    public void getAllReminder() {
        lstReminder.clear();
        lstReminderTemp.clear();
        if(dataBaseClass==null)
        {
            dataBaseClass=new DataBaseClass(getActivity());
        }
        lstReminder = dataBaseClass.getAllReminderData();
        for (ReminderDataModel reminderDataModel : lstReminder) {
            if (reminderDataModel.getIsComplete() == 0) {
                if (reminderDataModel.getIsCall() == 1 || reminderDataModel.getIsSms() == 1 || reminderDataModel.getIsEmail() == 1 || reminderDataModel.getIsMisc() == 1) {
                    lstReminderTemp.add(reminderDataModel);
                }
            }

        }
        if (lstReminderTemp.size() > 0) {
            callReminderAdapter.upDateData(lstReminderTemp);
        } else {
            rvReminder.setEmptyData("NO REMINDERS TO SHOW", R.drawable.noreminder);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void rlClick(int position) {
        Intent editIntent = new Intent(getActivity(), AddReminderActivity.class);
        editIntent.putExtra(getString(R.string.request_code_edit), 18);
        editIntent.putExtra(getString(R.string.reminderData), lstReminderTemp.get(position));
        getActivity().startActivityForResult(editIntent,mainActivity.ADD_DATA);
    }

    public void notifyFrgament() {
        if (callReminderAdapter != null)
            callReminderAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(final int position) {
        PopUtils.showDeleteDialog(getActivity(), getString(R.string.do_you_want_to_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lstReminderTemp.get(position).getIsCallNoteShow() == 1) {
                    dataBaseClass.deleteTempReminder(lstReminderTemp.get(position).getReminderId() + "", 0);
                } else {
                    dataBaseClass.deleteCallReminder(lstReminderTemp.get(position).getReminderId() + "");
                }
                if (hasPermissions(getActivity(), calendar_permissions)) {
                    StaticUtils.deleteRemindersToCalendar(getActivity(), String.valueOf(lstReminderTemp.get(position).getCalendarId()));
                }
                lstReminderTemp.remove(position);
                if (lstReminderTemp.size() > 0) {
                    callReminderAdapter.notifyDataSetChanged();
                } else {
                    callReminderAdapter.notifyDataSetChanged();
                    rvReminder.setEmptyData("NO REMINDERS TO SHOW", R.drawable.noreminder);
                }
            }
        });

    }

    @Override
    public void isActive(int position, int isActive) {
        ReminderDataModel reminderDataModel = lstReminderTemp.get(position);
        switch (isActive) {
            case 1:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + reminderDataModel.getReminderNumber()));
                startActivity(callIntent);
                break;
            case 2:
                Intent smsMsgAppVar = new Intent(Intent.ACTION_VIEW);
                smsMsgAppVar.setData(Uri.parse("sms:" + reminderDataModel.getReminderNumber()));
                smsMsgAppVar.putExtra("sms_body", reminderDataModel.getReminderNote());
                startActivity(smsMsgAppVar);
                break;
            case 3:
                String[] reciever = new String[]{reminderDataModel.getReminderEmail()};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setPackage("com.google.android.gm");
                intent.putExtra(Intent.EXTRA_EMAIL, reciever);
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("plain/text");
                //  intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                intent.putExtra(Intent.EXTRA_TEXT, reminderDataModel.getReminderNote());

                startActivity(intent);

                break;
            case 4:
                PopUtils.showErrorDialog(getActivity(), reminderDataModel.getReminderNote());
                break;
        }

    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {

                if (isPhoneCalling) {

                    isPhoneCalling = false;
                }

            }
        }
    }

}
