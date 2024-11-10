package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.PaymentHistoryCardBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.PaymentInfoItem;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
    Context context;
    List<PaymentInfoItem> paymentInfoItemList;
    public PaymentHistoryAdapter(Context context, List<PaymentInfoItem> paymentInfoItemList) {
        this.context = context;
        this.paymentInfoItemList = paymentInfoItemList;
    }
    public void refreshList(List<PaymentInfoItem> paymentInfoItemList) {
        this.paymentInfoItemList = paymentInfoItemList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentHistoryCardBinding paymentHistoryCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.payment_history_card, parent, false);
        return new ViewHolder(paymentHistoryCardBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.amountTV.setText(Common.getCurrencyAmount(paymentInfoItemList.get(position).getPartialAmount()) + " /-");
        holder.binding.modeTV.setText(paymentInfoItemList.get(position).getPaymentMode());
        holder.binding.transactionDateTV.setText(paymentInfoItemList.get(position).getCreatedAt().replace('-', '/'));
        if(paymentInfoItemList.get(position).getRefNo() != null && !paymentInfoItemList.get(position).getRefNo().equalsIgnoreCase("")) {
            holder.binding.refRL.setVisibility(View.VISIBLE);
            holder.binding.refTV.setText(paymentInfoItemList.get(position).getRefNo());
        }
        else {
            holder.binding.refRL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return paymentInfoItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PaymentHistoryCardBinding binding;
        public ViewHolder(@NonNull PaymentHistoryCardBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}
