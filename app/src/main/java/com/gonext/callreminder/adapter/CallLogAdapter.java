package com.gonext.callreminder.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;


import com.bumptech.glide.Glide;
import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.Calls;
import com.gonext.callreminder.utils.view.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallViewHolder> {
    private ArrayList<Calls> callArrayList = new ArrayList<>();
    ListView lst;
    Context context;
    Clickable clickable;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void deleteRecyclerData(int position) {
        callArrayList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * @param callArrayList contains object of type Calls received from database
     * @param context       contains reference to current activity
     * @param clickable     interface that is to be implemented on DisplayCallsActivity
     */
    public CallLogAdapter(ArrayList<Calls> callArrayList, Context context, Clickable clickable) {
        this.callArrayList = callArrayList;
        this.context = context;
        this.clickable = clickable;

    }

    // View holder of call that binds all the views of item
    public static class CallViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivContactImage, ivType;
        public CustomTextView tvName, tvNumber, tvDate;
        public CardView cvItemCall;

        public CallViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvNumber = (CustomTextView) itemView.findViewById(R.id.tvNumber);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tvDate);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
            cvItemCall = (CardView) itemView.findViewById(R.id.cvItemCall);
        }
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call, parent, false);

        CallViewHolder appViewHolder = new CallViewHolder(v);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(final CallViewHolder holder, final int position) {
        final Calls calls = callArrayList.get(position);

        holder.tvName.setText(calls.getName());


        holder.tvNumber.setText(calls.getNumber());
        if (calls.getType().equals(context.getString(R.string.missedCall))) {
            holder.ivType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgmissedcall));
        } else if (calls.getType().equals(context.getString(R.string.dialledCall))) {
            holder.ivType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgoutgoing));
        } else if (calls.getType().equals(context.getString(R.string.ReceivedCall))) {
            holder.ivType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.imgincoming));
        }

        holder.tvDate.setText(calls.getDate());
        String photo = calls.getImg();
        if (photo != null) {
            Glide.with(context)
                    .load(Uri.parse(photo)) // Uri of the picture
                    .into(holder.ivContactImage);
        } else {
            holder.ivContactImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }
        holder.cvItemCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.click(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return callArrayList.size();
    }

    /**
     * interface that contains click method of checkbox
     */
    public interface Clickable {
        void click(int position, boolean flag);
    }
}
