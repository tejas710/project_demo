package com.gonext.callreminder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.AddReminderActivity;
import com.gonext.callreminder.activities.MainActivity;
import com.gonext.callreminder.adapter.CallNotesAdapter;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.datalayers.storage.AppPref;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.view.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sellnews on 9/28/17.
 */

public class CallNotesFragment extends Fragment implements CallNotesAdapter.Clickable {
    static CallNotesFragment callNotesFragment;
    DataBaseClass dataBaseClass;
    Unbinder unbinder;
    AppPref appPref;
    int page;
    ArrayList<ReminderDataModel> lstCallNotes = new ArrayList<>();
    ArrayList<ReminderDataModel> lstCallNotesTemp = new ArrayList<>();
    CallNotesAdapter callNotesAdapter;
    @BindView(R.id.rvReminder)
    CustomRecyclerView rvReminder;
    @BindView(R.id.llEmptyViewMain)
    LinearLayout llEmptyViewMain;
    MainActivity mainActivity;
    CountDownTimer countDownTimer;

    public static CallNotesFragment newInstance(int page) {
        if (callNotesFragment == null) {
            callNotesFragment = new CallNotesFragment();
            Bundle args = new Bundle();
            args.putInt("page", page);
            callNotesFragment.setArguments(args);
        }

        return callNotesFragment;
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
        getAllCallNote();
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
        callNotesAdapter = new CallNotesAdapter(lstCallNotes, getActivity(), this);
        rvReminder.setItemAnimator(new DefaultItemAnimator());
        rvReminder.setAdapter(callNotesAdapter);
        rvReminder.setEmptyView(llEmptyViewMain);
    }

    public void getAllCallNote() {
        lstCallNotes.clear();
        lstCallNotesTemp.clear();
        if(dataBaseClass==null)
        {
            dataBaseClass=new DataBaseClass(getActivity());
        }
        lstCallNotes = dataBaseClass.getAllReminderData();
        for (ReminderDataModel reminderDataModel : lstCallNotes) {
            if (reminderDataModel.getIsCallNoteShow() == 1) {
                lstCallNotesTemp.add(reminderDataModel);
            }
        }
        if (lstCallNotesTemp.size() > 0) {
            callNotesAdapter.upDateData(lstCallNotesTemp);
        } else {
            rvReminder.setEmptyData("NO CALL NOTES TO SHOW", R.drawable.noreminder);
        }
    }

    public void notifyFrgament() {
        if (callNotesAdapter != null)
            callNotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void cbClick(int position) {
        Intent editIntent = new Intent(getActivity(), AddReminderActivity.class);
        editIntent.putExtra(getString(R.string.request_code_edit), 18);
        editIntent.putExtra(getString(R.string.reminderData), lstCallNotesTemp.get(position));
        getActivity().startActivityForResult(editIntent,mainActivity.ADD_DATA);
    }

    @Override
    public void deleteItem(final int position) {
        PopUtils.showDeleteDialog(getActivity(), getString(R.string.do_you_want_to_delete_Call_note), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lstCallNotesTemp.get(position).getIsCall() == 1 || lstCallNotesTemp.get(position).getIsSms() == 1 ||
                        lstCallNotesTemp.get(position).getIsEmail() == 1 || lstCallNotesTemp.get(position).getIsMisc() == 1) {
                    dataBaseClass.deleteTempCallNote(lstCallNotesTemp.get(position).getReminderId() + "", 0);
                } else {
                    dataBaseClass.deleteCallReminder(lstCallNotesTemp.get(position).getReminderId() + "");
                }
                lstCallNotesTemp.remove(position);
                if (lstCallNotesTemp.size() > 0) {
                    callNotesAdapter.notifyDataSetChanged();
                } else {
                    callNotesAdapter.notifyDataSetChanged();
                    rvReminder.setEmptyData("NO CALL NOTES TO SHOW", R.drawable.noreminder);
                }
            }
        });
    }

    @Override
    public void isActive(int position, int isActive) {

        lstCallNotesTemp.get(position).setIsCallNote(isActive);
        dataBaseClass.updateIsCallNoteEnable(lstCallNotesTemp.get(position).getReminderId() + "", isActive);
        callNotesAdapter.notifyDataSetChanged();

    }
}
