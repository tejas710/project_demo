package com.gonext.callreminder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gonext.callreminder.R;
import com.gonext.callreminder.activities.CallNoteDialogActivity;
import com.gonext.callreminder.datalayers.storage.DataBaseClass;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.view.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sellnews on 10/12/17.
 */

public class CallnotedialogFragment extends Fragment {
    @BindView(R.id.edtNotes)
    CustomEditText edtNotes;
    DataBaseClass dataBaseClass;
    Unbinder unbinder;
    int page;

    public static CallnotedialogFragment newInstance(int page) {

        CallnotedialogFragment callnotedialogFragment = new CallnotedialogFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        callnotedialogFragment.setArguments(args);
        return callnotedialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.item_pager_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        dataBaseClass = new DataBaseClass(getActivity());
        edtNotes.setText(CallNoteDialogActivity.lstCallNoteTemp.get(page).getReminderNote());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    public void editCallNote() {
        edtNotes.setClickable(true);
        edtNotes.setFocusableInTouchMode(true);

    }

    public void saveCallNote() {
        edtNotes.setClickable(false);
        edtNotes.setFocusable(false);
        edtNotes.setFocusableInTouchMode(false);
        CallNoteDialogActivity.lstCallNoteTemp.get(page).setReminderNote(edtNotes.getText().toString());
        dataBaseClass.updateCallReminder(CallNoteDialogActivity.lstCallNoteTemp.get(page));
        StaticUtils.hideKeyboard(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
