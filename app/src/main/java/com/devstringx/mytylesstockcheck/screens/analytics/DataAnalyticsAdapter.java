package com.devstringx.mytylesstockcheck.screens.analytics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.DataAnalyticItemViewBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.RecordsItem;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyticsAdapter extends RecyclerView.Adapter<DataAnalyticsAdapter.ViewHolder> {
    private List<DataAnalyticsResponse.Record> recordsItems = new ArrayList<>();
    private String statusTab;
    private Context context;

    public void refreshList(List<DataAnalyticsResponse.Record> records) {
        this.recordsItems = records;
        notifyDataSetChanged();
    }

    public DataAnalyticsAdapter(Context context, List<DataAnalyticsResponse.Record> recordsItems) {
        this.context = context;
        this.recordsItems = recordsItems;
    }

    @NonNull
    @Override
    public DataAnalyticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DataAnalyticItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.data_analytic_item_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAnalyticsAdapter.ViewHolder holder, int position) {
        DataAnalyticsResponse.Record item = recordsItems.get(position);
        holder.binding.tvSerialNo.setText("" + (position + 1));
        holder.binding.saleExecutive.setText(item.getUserName());
        String orderCount = item.getOrderCount() > 9 ? "" + item.getOrderCount() : "0" + item.getOrderCount();
        holder.binding.orders.setText(orderCount);
        holder.binding.totalAmount.setText("" + item.getTotalOrderAmount());


    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(recordsItems) ? 0 : recordsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DataAnalyticItemViewBinding binding;

        public ViewHolder(@NonNull DataAnalyticItemViewBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

}
