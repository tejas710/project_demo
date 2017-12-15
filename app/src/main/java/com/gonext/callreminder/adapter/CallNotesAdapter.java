package com.gonext.callreminder.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;

/**
 * Created by sellnews on 9/28/17.
 */

public class CallNotesAdapter extends RecyclerView.Adapter<CallNotesAdapter.CallNotesViewHolder> {
    ArrayList<ReminderDataModel> lstNotes;
    Context context;
    Clickable clickable;

    public CallNotesAdapter(ArrayList<ReminderDataModel> lstNotes, Context context, Clickable clickable) {
        this.lstNotes = lstNotes;
        this.context = context;
        this.clickable = clickable;
    }

    public void upDateData(ArrayList<ReminderDataModel> lstReminder) {
        this.lstNotes = lstReminder;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (lstNotes != null && lstNotes.size() > 0) {
            lstNotes.clear();
            notifyDataSetChanged();
        }
    }

    public interface Clickable {
        void cbClick(int position);
        void deleteItem(int position);
        void isActive(int position, int isActive);
    }


    @Override
    public CallNotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_callnote_list, parent, false);
        return new CallNotesViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CallNotesViewHolder holder, final int position) {
        holder.swEnable.setOnCheckedChangeListener(null);
        final ReminderDataModel reminderDataModel = lstNotes.get(position);
        holder.tvName.setText(reminderDataModel.getReminderName());
        int enable = reminderDataModel.getIsCallNote();
        if (enable == 1) {
            holder.swEnable.setChecked(true);
        } else {
            holder.swEnable.setChecked(false);
        }
        if (!reminderDataModel.getReminderNote().toString().equals("")) {
            holder.tvNote.setText(reminderDataModel.getReminderNote());
        }
        if (reminderDataModel.getReminderIcon() != null) {
            Glide.with(context)
                    .load(Uri.parse(reminderDataModel.getReminderIcon().toString())) // Uri of the picture
                    .into(holder.ivProfilePhoto);
        } else {
            holder.ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }
        holder.swEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    clickable.isActive(position, 1);
                else
                    clickable.isActive(position, 0);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.deleteItem(position);
            }
        });
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.cbClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lstNotes.size();
    }

    public class CallNotesViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvName, tvNote;
        ImageView ivProfilePhoto, ivDelete;
        RelativeLayout rlMain;
        Switch swEnable;

        public CallNotesViewHolder(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvNote = (CustomTextView) itemView.findViewById(R.id.tvNote);
            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            swEnable = (Switch) itemView.findViewById(R.id.swEnable);
            rlMain = (RelativeLayout) itemView.findViewById(R.id.rlMain);

        }
    }
}
