package com.gonext.callreminder.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.ContactDetailModel;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;

/**
 * Created by sellnews on 10/3/17.
 */

public class ContactSuggestionAdapter extends RecyclerView.Adapter<ContactSuggestionAdapter.ContactSuggestionHolder> {
    ArrayList<ContactDetailModel> lstContact;
    Context context;
    Clickable clickable;

    public ContactSuggestionAdapter(ArrayList<ContactDetailModel> lstContact, Context context, Clickable clickable) {
        this.lstContact = lstContact;
        this.context = context;
        this.clickable = clickable;
    }

    public void upDateData(ArrayList<ContactDetailModel> lstReminder) {
        this.lstContact = lstReminder;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (lstContact != null && lstContact.size() > 0) {
            lstContact.clear();
            notifyDataSetChanged();
        }
    }

    public interface Clickable {
        void cbClick(int position);


    }


    @Override
    public ContactSuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_suggestion, parent, false);
        return new ContactSuggestionHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ContactSuggestionHolder holder, final int position) {
        ContactDetailModel contactDetailModel = lstContact.get(position);
        String name = contactDetailModel.getContactName();
        String number = contactDetailModel.getContactNumber();
        String photo = contactDetailModel.getContactImage();
        holder.tvContactName.setText(name);
        holder.tvContactNumber.setText(number);
        if (photo != null) {
            Glide.with(context)
                    .load(Uri.parse(photo)) // Uri of the picture
                    .into(holder.ivContactImage);
        } else {
            holder.ivContactImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }
        holder.cvContactSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.cbClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstContact.size();
    }

    public class ContactSuggestionHolder extends RecyclerView.ViewHolder {
        CustomTextView tvContactName,tvContactNumber;
        ImageView ivContactImage;
        CardView cvContactSuggestion;
        public ContactSuggestionHolder(View itemView) {
            super(itemView);
            tvContactName = (CustomTextView)itemView.findViewById(R.id.tvContactName);
            tvContactNumber = (CustomTextView)itemView.findViewById(R.id.tvContactNumber);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            cvContactSuggestion = (CardView)itemView.findViewById(R.id.cvContactSuggestion);
        }
    }
}