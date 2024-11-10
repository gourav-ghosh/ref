package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.TransactionHistoryItemBinding;
import com.devstringx.mytylesstockcheck.screens.transactionFilters.TransactionHistoryResponse;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.List;

public class TransactionHistoryListingAdapter extends RecyclerView.Adapter<TransactionHistoryListingAdapter.ViewHolder> {
    private Context context;
    private List<TransactionHistoryResponse.Record> recordsList;

    public TransactionHistoryListingAdapter(Context context, List<TransactionHistoryResponse.Record> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }
    public void refreshList(List<TransactionHistoryResponse.Record> recordsList) {
        this.recordsList = recordsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionHistoryListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransactionHistoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.transaction_history_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryListingAdapter.ViewHolder holder, int position) {
        TransactionHistoryResponse.Record record = recordsList.get(position);
        if (position % 2 == 0)
            holder.binding.itemLL.setBackgroundResource(R.drawable.gray_border_grayish_fill_bg);
        else holder.binding.itemLL.setBackgroundResource(R.drawable.gray_border_white_fill_bg);
        holder.binding.orderNumTV.setText(record.getSaleOrderNo());
        holder.binding.saleExeTV.setText(record.getSalesExecutive());
        holder.binding.clientNameTV.setText(record.getClientName());
        holder.binding.accountTV.setText(record.getAccountManager());
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(recordsList) ? 0 : recordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TransactionHistoryItemBinding binding;

        public ViewHolder(@NonNull TransactionHistoryItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}
