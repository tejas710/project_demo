package com.gonext.callreminder.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.ReminderDataModel;
import com.gonext.callreminder.utils.TimeUtils;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;

/**
 * Created by sellnews on 10/9/17.
 */

public class HistoryReminderAdapter extends RecyclerView.Adapter<HistoryReminderAdapter.HistoryReminderViewHolder> {
    ArrayList<ReminderDataModel> lstHistoryReminder;
    Context context;
    Clickable clickable;

    public HistoryReminderAdapter(ArrayList<ReminderDataModel> lstHistoryReminder, Context context, Clickable clickable) {
        this.lstHistoryReminder = lstHistoryReminder;
        this.context = context;
        this.clickable = clickable;
    }

    public void upDateData(ArrayList<ReminderDataModel> lstReminder) {
        this.lstHistoryReminder = lstReminder;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (lstHistoryReminder != null && lstHistoryReminder.size() > 0) {
            lstHistoryReminder.clear();
            notifyDataSetChanged();
        }
    }

    public interface Clickable {
        void cbClick(int position, Boolean f, boolean click);
        void deleteItem(int position);
        void isActive(int position, int isActive);
    }


    @Override
    public HistoryReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list, parent, false);
        return new HistoryReminderViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final HistoryReminderViewHolder holder, final int position) {
        final ReminderDataModel reminderDataModel = lstHistoryReminder.get(position);
        holder.tvName.setText(reminderDataModel.getReminderName());
        if (!reminderDataModel.getReminderNote().toString().equals("")) {
            holder.viewMiddle.setVisibility(View.VISIBLE);
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(reminderDataModel.getReminderNote());
        }else{
            holder.viewMiddle.setVisibility(View.GONE);
            holder.tvNote.setVisibility(View.GONE);
        }
        final String time = TimeUtils.getDateIn24HrsFormat(reminderDataModel.getUpdateDate());
        holder.tvReminderTime.setText(time.substring(time.indexOf(" ") + 1));

        if (reminderDataModel.getReminderIcon() != null) {
            Glide.with(context)
                    .load(Uri.parse(reminderDataModel.getReminderIcon().toString())) // Uri of the picture
                    .into(holder.ivProfilePhoto);
        } else {
            holder.ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.deleteItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstHistoryReminder.size();
    }

    public class HistoryReminderViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvReminderTime, tvName, tvNote;
        ImageView ivProfilePhoto, ivDelete;
        RelativeLayout rlMain;
        View viewMiddle;

        public HistoryReminderViewHolder(View itemView) {
            super(itemView);
            tvReminderTime = (CustomTextView) itemView.findViewById(R.id.tvReminderTime);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvNote = (CustomTextView) itemView.findViewById(R.id.tvNote);
            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            rlMain = (RelativeLayout) itemView.findViewById(R.id.rlMain);
            viewMiddle = itemView.findViewById(R.id.viewMiddle);

        }
    }
}
