package com.gonext.callreminder.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gonext.callreminder.R;
import com.gonext.callreminder.adapter.HistoryReminderAdapter;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.view.CustomButton;
import com.gonext.callreminder.utils.view.CustomRecyclerView;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryActivity extends BaseActivity implements HistoryReminderAdapter.Clickable {
    DataBaseClass dataBaseClass;
    @BindView(R.id.rvHistoryReminder)
    CustomRecyclerView rvHistoryReminder;
    @BindView(R.id.llEmptyViewMain)
    LinearLayout llEmptyViewMain;
    HistoryReminderAdapter historyReminderAdapter;
    ArrayList<ReminderDataModel> lstHistoryReminder = new ArrayList<>();
    ArrayList<ReminderDataModel> lstHistoryReminderTemp = new ArrayList<>();
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvEmptyDescription)
    CustomTextView tvEmptyDescription;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.ivEmptyImage)
    ImageView ivEmptyImage;
    @BindView(R.id.tvEmptyTitle)
    CustomTextView tvEmptyTitle;
    @BindView(R.id.btnEmpty)
    CustomButton btnEmpty;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        AdUtils.loadIntestial(HistoryActivity.this);
        AdUtils.displayBanner(rlAds,HistoryActivity.this);
        initilaizeRvReminder();
        getAllHistoryData();
    }

    public void initilaizeRvReminder() {
        dataBaseClass = new DataBaseClass(HistoryActivity.this);
        historyReminderAdapter = new HistoryReminderAdapter(lstHistoryReminder, this, this);
        rvHistoryReminder.setItemAnimator(new DefaultItemAnimator());
        rvHistoryReminder.setAdapter(historyReminderAdapter);
        rvHistoryReminder.setEmptyView(llEmptyViewMain);

    }

    public void getAllHistoryData() {

        lstHistoryReminder.clear();
        lstHistoryReminderTemp.clear();
        lstHistoryReminder = dataBaseClass.getAllReminderData();
        for (ReminderDataModel reminderDataModel : lstHistoryReminder) {
            if (reminderDataModel.getIsComplete() == 1) {

                lstHistoryReminderTemp.add(reminderDataModel);

            }

        }
        if (lstHistoryReminderTemp.size() > 0) {
            historyReminderAdapter.upDateData(lstHistoryReminderTemp);
        } else {
            rvHistoryReminder.setEmptyData("NO HISTORY TO SHOW", R.drawable.noreminder);
        }

    }

    @Override
    public void cbClick(int position, Boolean f, boolean click) {

    }

    @Override
    public void deleteItem(final int position) {
        PopUtils.showDeleteDialog(HistoryActivity.this, getString(R.string.do_you_want_to_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lstHistoryReminderTemp.get(position).getIsCallNoteShow() == 1) {
                    dataBaseClass.deleteTempReminder(lstHistoryReminderTemp.get(position).getReminderId() + "", 0);
                } else {
                    dataBaseClass.deleteCallReminder(lstHistoryReminderTemp.get(position).getReminderId() + "");
                }
                lstHistoryReminderTemp.remove(position);
                if (lstHistoryReminderTemp.size() > 0) {
                    historyReminderAdapter.notifyDataSetChanged();
                } else {
                    historyReminderAdapter.notifyDataSetChanged();
                    rvHistoryReminder.setEmptyData("NO HISTORY TO SHOW", R.drawable.noreminder);
                }

            }
        });
    }

    @Override
    public void isActive(int position, int isActive) {

    }

    @Override
    public void onBackPressed() {
        AdUtils.displayIntestial();
        finish();
    }

    @OnClick({R.id.llBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                AdUtils.displayIntestial();
                finish();
                break;
        }
    }
}
