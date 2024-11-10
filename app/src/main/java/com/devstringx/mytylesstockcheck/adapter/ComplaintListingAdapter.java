package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.NewComplaintItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class ComplaintListingAdapter extends RecyclerView.Adapter<ComplaintListingAdapter.ViewHolder> {
    private OnBtnClickListener onBtnClickListener;
    private List<RecordsItem> recordsItems = new ArrayList<>();
    private String statusTab;
    private Context context;
    public void refreshList(List<RecordsItem> records, String statusTab) {
        this.statusTab=statusTab;
        this.recordsItems=records;
        notifyDataSetChanged();
    }

    public ComplaintListingAdapter(Context context,OnBtnClickListener onBtnClickListener) {
        this.context=context;
        this.onBtnClickListener = onBtnClickListener;
    }

    @NonNull
    @Override
    public ComplaintListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewComplaintItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_complaint_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintListingAdapter.ViewHolder holder, int position) {
        RecordsItem item=recordsItems.get(position);
        if (item.getComplaintStatus().equalsIgnoreCase("Created")) {
            holder.binding.CreatedTag.setBackgroundResource(R.drawable.black_round_bg);
            holder.binding.CreatedTag.setText("Created");
            holder.binding.cautionTV.setVisibility(View.GONE);
        } else if (item.getComplaintStatus().equalsIgnoreCase("Inprogress")) {
            holder.binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
            holder.binding.CreatedTag.setText("inprogress");
        } else if (item.getComplaintStatus().equalsIgnoreCase("Resolved")) {
            holder.binding.CreatedTag.setBackgroundResource(R.drawable.green_round_bg);
            holder.binding.CreatedTag.setText("resolved");
        }
        if (item.getDeletedAt()!=null && statusTab.equalsIgnoreCase("Cancelled")) {
            holder.binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
            holder.binding.CreatedTag.setText("cancelled");
        }
        holder.binding.ticketNo.setText(item.getTicketNumber().toString());
        holder.binding.orderNoTV.setText(item.getSaleOrderNo().toString());
        holder.binding.customerNameTV.setText(item.getOrderCustomerName().toString());
        holder.binding.dateTV.setText(item.getEstimateResolveDate().toString());
        holder.binding.supExeTV.setText(item.getSupportExecutiveFirstName()+" "+item.getSupportExecutiveLastName());
        holder.binding.saleExeTV.setText(item.getOrderSaleExecutive().toString());
        holder.binding.complaintCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClickListener.onClick(position,String.valueOf(item.getId()));
            }
        });
        holder.binding.complaintCommentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClickListener.getComplaintId(String.valueOf(item.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NewComplaintItemBinding binding;

        public ViewHolder(@NonNull NewComplaintItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnBtnClickListener {
        public void onClick(int position, String s);
        public void getComplaintId(String id);
    }
}
