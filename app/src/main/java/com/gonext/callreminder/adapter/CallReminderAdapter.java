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
 * Created by sellnews on 9/28/17.
 */

public class CallReminderAdapter extends RecyclerView.Adapter<CallReminderAdapter.CallReminderViewHolder> {
    ArrayList<ReminderDataModel> lstReminder;
    Context context;
    Clickable clickable;

    public CallReminderAdapter(ArrayList<ReminderDataModel> lstReminder, Context context, Clickable clickable) {
        this.lstReminder = lstReminder;
        this.context = context;
        this.clickable = clickable;
    }

    public void upDateData(ArrayList<ReminderDataModel> lstReminder) {
        this.lstReminder = lstReminder;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (lstReminder != null && lstReminder.size() > 0) {
            lstReminder.clear();
            notifyDataSetChanged();
        }
    }

    public interface Clickable {
        void rlClick(int position);
        void deleteItem(int position);
        void isActive(int position, int isActive);
    }


    @Override
    public CallReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_list, parent, false);
        return new CallReminderViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CallReminderViewHolder holder, final int position) {
        final ReminderDataModel reminderDataModel = lstReminder.get(position);
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
        String diff = TimeUtils.getDateTimeDifference(reminderDataModel.getUpdateDate());
        if (!diff.equals("0"))
            holder.tvTime.setText(diff);
        else
            holder.tvTime.setText("");
        if (reminderDataModel.getReminderIcon() != null) {
            Glide.with(context)
                    .load(Uri.parse(reminderDataModel.getReminderIcon().toString())) // Uri of the picture
                    .into(holder.ivProfilePhoto);
        } else {
            holder.ivProfilePhoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }
        if (reminderDataModel.getIsCall() == 1) {
            holder.ivCall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgcallhome));
        } else {
            holder.ivCall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgcallhome2));
        }
        if (reminderDataModel.getIsSms() == 1) {
            holder.ivSms.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgsmshome));
        } else {
            holder.ivSms.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgsmshome2));
        }
        if (reminderDataModel.getIsEmail() == 1) {
            holder.ivEmail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgemailhome));
        } else {
            holder.ivEmail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgemailhome2));
        }
        if (reminderDataModel.getIsMisc() == 1) {
            holder.ivOther.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgotherhome));
        } else {
            holder.ivOther.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgotherhome2));
        }
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDataModel.getIsCall() == 1) {
                    clickable.isActive(position, 1);
                }
            }
        });
        holder.ivSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDataModel.getIsSms() == 1) {
                    clickable.isActive(position, 2);
                }
            }
        });
        holder.ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDataModel.getIsEmail() == 1) {
                    clickable.isActive(position, 3);
                }
            }
        });
        holder.ivOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderDataModel.getIsMisc() == 1) {
                    clickable.isActive(position, 4);
                }
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
                clickable.rlClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstReminder.size();
    }

    public class CallReminderViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvReminderTime, tvTime, tvName, tvNote;
        ImageView ivProfilePhoto, ivDelete, ivEmail, ivSms, ivCall, ivOther;
        RelativeLayout rlMain;
        View viewMiddle;

        public CallReminderViewHolder(View itemView) {
            super(itemView);
            tvReminderTime = (CustomTextView) itemView.findViewById(R.id.tvReminderTime);
            tvTime = (CustomTextView) itemView.findViewById(R.id.tvTime);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvNote = (CustomTextView) itemView.findViewById(R.id.tvNote);
            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivEmail = (ImageView) itemView.findViewById(R.id.ivEmail);
            ivSms = (ImageView) itemView.findViewById(R.id.ivSms);
            ivCall = (ImageView) itemView.findViewById(R.id.ivCall);
            ivOther = (ImageView) itemView.findViewById(R.id.ivOtherNote);
            rlMain = (RelativeLayout) itemView.findViewById(R.id.rlMain);
            viewMiddle = itemView.findViewById(R.id.viewMiddle);

        }
    }
}
