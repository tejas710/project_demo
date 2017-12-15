package com.gonext.callreminder.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gonext.callreminder.R;
import com.gonext.callreminder.adapter.CallLogAdapter;
import com.gonext.callreminder.datalayers.model.Calls;
import com.gonext.callreminder.utils.AdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class CallLogActivity extends BaseActivity implements CallLogAdapter.Clickable {
    ArrayList<Calls> lstCallLog = new ArrayList<>();
    int allCount = 0;
    CallLogAdapter callLogAdapter;
    @BindView(R.id.rvAllCall)
    RecyclerView rvAllCall;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_call_log;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        AdUtils.displayBanner(rlAds,CallLogActivity.this);
        lstCallLog.clear();
        getCallDetails();

        callLogAdapter = new CallLogAdapter(lstCallLog, this, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rvAllCall.setLayoutManager(layoutManager);
        rvAllCall.setItemAnimator(new DefaultItemAnimator());
        rvAllCall.setAdapter(callLogAdapter);

    }

    private void getCallDetails() throws SecurityException {

        String strOrder = CallLog.Calls.DATE + " DESC";
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
        int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int img = managedCursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String dir = null;
            int dircode = Integer.parseInt(managedCursor.getString(type));
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = getString(R.string.dialledCall);

                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = getString(R.string.ReceivedCall);

                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = getString(R.string.missedCall);

                    break;
            }
            int dur = Integer.parseInt(managedCursor.getString(duration));
            String hr = "", min = "", sec = "", time = "";
            if (dur / 3600 > 0) {
                hr = String.valueOf(dur / 3600) + "hrs.";
                dur = dur % 3600;
            }
            if (dur / 60 > 0) {
                if (dur / 60 < 10) {
                    min = "0" + String.valueOf(dur / 60) + "mins.";
                    dur = dur % 60;
                } else {
                    min = String.valueOf(dur / 60) + "mins.";
                    dur = dur % 60;
                }
            }
            if (dur > 0) {
                if (dur < 10) {
                    sec = "0" + String.valueOf(dur) + "secs.";
                } else {
                    sec = String.valueOf(dur) + "secs.";
                }
            }

            if (hr != "") {
                time += hr;
            }
            if (min != "") {
                time += min;
            }
            time += sec;

            String callDate = managedCursor.getString(date);
            String callDayTime = formatDate(Long.valueOf(callDate));

            lstCallLog.add(new Calls(managedCursor.getString(id), managedCursor.getString(img), managedCursor.getString(name),
                    managedCursor.getString(number), dir,
                    callDayTime + "", time, false));
        }
        managedCursor.close();


        allCount = lstCallLog.size();
    }

    public static String formatDate(long l) {
        return new SimpleDateFormat("MMM dd").format(new Date(l));
    }

    @Override
    public void click(int position, boolean flag) {
        Intent intent = new Intent(CallLogActivity.this, AddReminderActivity.class);
        intent.putExtra("name", lstCallLog.get(position).getName());
        intent.putExtra("number", lstCallLog.get(position).getNumber());
        intent.putExtra("photo", lstCallLog.get(position).getImg());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @OnClick(R.id.llBack)
    public void onClick() {
        finish();
    }
}
