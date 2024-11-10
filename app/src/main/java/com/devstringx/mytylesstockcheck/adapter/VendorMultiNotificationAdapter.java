package com.devstringx.mytylesstockcheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.MultiNotificationItemBinding;
import com.devstringx.mytylesstockcheck.databinding.NewInquiriesItemBinding;
import com.devstringx.mytylesstockcheck.databinding.QuoteListItemBinding;

public class VendorMultiNotificationAdapter extends RecyclerView.Adapter<VendorMultiNotificationAdapter.ViewHolder> {
    @NonNull
    @Override
    public VendorMultiNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewInquiriesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_inquiries_item, parent, false);
        binding.timeAgoLl.setVisibility(View.VISIBLE);
        binding.inquiryNoLl.setVisibility(View.GONE);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorMultiNotificationAdapter.ViewHolder holder, int position) {
        holder.binding.unavailableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.notAvailableResponseLl.setVisibility(View.VISIBLE);
                holder.binding.availabilityLl.setVisibility(View.GONE);
            }
        });
        holder.binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
                holder.binding.availabilityLl.setVisibility(View.VISIBLE);
            }
        });
        holder.binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
                holder.binding.availabilityLl.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 12;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        NewInquiriesItemBinding binding;
        public ViewHolder(@NonNull NewInquiriesItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
}
