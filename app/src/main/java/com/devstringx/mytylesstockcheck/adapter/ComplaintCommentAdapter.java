package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.ComplaintCommentsBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderComment.OrderRecordsItem;

import java.util.ArrayList;
import java.util.List;

public class ComplaintCommentAdapter extends RecyclerView.Adapter<ComplaintCommentAdapter.ViewHolder> {
    private Context mContext;
    private List<RecordsItem> recordsItems = new ArrayList<>();
    private List<OrderRecordsItem> orderRecordsItems = new ArrayList<>();
    private String type;

    public ComplaintCommentAdapter(Context context, List<RecordsItem> records) {
        this.mContext = context;
        this.recordsItems = records;
    }

    public ComplaintCommentAdapter(Context context, List<OrderRecordsItem> records, String type) {
        this.mContext = context;
        this.orderRecordsItems = records;
        this.type = type;
    }

    @NonNull
    @Override
    public ComplaintCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComplaintCommentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.complaint_comments, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintCommentAdapter.ViewHolder holder, int position) {
        if (type != null) {
            OrderRecordsItem currentItem = orderRecordsItems.get(position);
            holder.binding.senderRoleTV.setText(currentItem.getCommentedByName().toString()+" "+currentItem.getCommentedByLastName().toString());
            holder.binding.senderTextTV.setText(currentItem.getComment().toString());
            holder.binding.senderTimeTV.setText(currentItem.getCreatedDate().toString());
        } else {
            RecordsItem currentItem = recordsItems.get(position);
            holder.binding.senderRoleTV.setText(currentItem.getCreatedByName().toString());
            holder.binding.senderTextTV.setText(currentItem.getComment().toString());
            holder.binding.senderTimeTV.setText(currentItem.getCreatedDate().toString());
        }
    }

    @Override
    public int getItemCount() {
        return type==null ? recordsItems.size() : orderRecordsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ComplaintCommentsBinding binding;

        public ViewHolder(@NonNull ComplaintCommentsBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}
