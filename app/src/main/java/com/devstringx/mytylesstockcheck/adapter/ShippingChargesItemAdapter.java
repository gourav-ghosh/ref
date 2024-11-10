package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ShippingChargeItemBinding;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.AllShippingChargeResponse;

import java.util.ArrayList;
import java.util.List;

public class ShippingChargesItemAdapter extends RecyclerView.Adapter<ShippingChargesItemAdapter.ViewHolder> {
    OnClick click;
    Context mContext;
    private List<AllShippingChargeResponse.Record> listItem = new ArrayList<>();
    private static final String PENDING = "request_sent";
    private static final String APPROVED = "approved";
    private static final String PAID = "paid";
    private static final String REJECTED = "rejected";


    public ShippingChargesItemAdapter(Context context, OnClick click, List<AllShippingChargeResponse.Record> list) {
        this.click = click;
        this.mContext = context;
        this.listItem = list;
    }

    public void refreshList(List<AllShippingChargeResponse.Record> list) {
        this.listItem = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShippingChargesItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShippingChargeItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shipping_charge_item, parent, false);
        return new ViewHolder(binding);
    }

    public void setOrderAmountDetails(ShippingChargesItemAdapter.ViewHolder holder, AllShippingChargeResponse.Record record) {
        TextView tvAmount = holder.binding.orderAmmountTv;
        TextView tvDate = holder.binding.date;
        TextView tvStatusDate = holder.binding.dateTimeRight;
        String status = record.getStatus();
        tvDate.setText(record.getCreatedAt());
        switch (status) {
            case PENDING:
                tvAmount.setBackgroundResource(R.drawable.status_pending_3bg);
                tvAmount.setTextColor(mContext.getColor(R.color.orange));
                tvStatusDate.setText(record.getRequestRaisedTime());
                break;
            case APPROVED:
                tvAmount.setBackgroundResource(R.drawable.status_pending_3bg);
                tvAmount.setTextColor(mContext.getColor(R.color.orange));
                tvStatusDate.setText(record.getApprovedTime());
                break;
            case PAID:
                tvAmount.setTextColor(mContext.getColor(R.color.green));
                tvAmount.setBackgroundResource(R.drawable.status_approved_3bg);
                break;
            case REJECTED:
                tvAmount.setTextColor(mContext.getColor(R.color.red));
                tvAmount.setBackgroundResource(R.drawable.status_rejected_3bg);
                tvStatusDate.setText(record.getRequestRaisedTime());
                break;
        }
        holder.binding.orderAmmountTv.setText("Rs." + record.getAmount());
        holder.binding.orderNo.setText("Order #" + record.getSaleOrderNo());


    }

    @Override
    public void onBindViewHolder(@NonNull ShippingChargesItemAdapter.ViewHolder holder, int position) {
        AllShippingChargeResponse.Record curr = listItem.get(position);
        setOrderAmountDetails(holder, curr);
        holder.binding.customerNameTV.setText(curr.getCustomerName());
        holder.binding.saleExeTV.setText(curr.getSalesExecutive());
        holder.binding.supExeTV.setText(curr.getDeliveryAgent());
        String[] date = curr.getRequestRaisedTime().split(" ");
        holder.binding.dateTV.setText(date.length > 0 ? date[0] : curr.getRequestRaisedTime());
        holder.binding.locTV.setText(curr.getDistance());
        holder.binding.pointsTV.setText(curr.getLoadingPoints());
        if (curr.isSelected()) {
            holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_selected);
        } else {
            holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_unselected);
        }
        holder.binding.cbIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!curr.isSelected()) {
                    holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_selected);
                    curr.setSelected(true);
                } else {
                    holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_unselected);
                    curr.setSelected(false);
                }
                click.onClickListener();
            }
        });
        holder.binding.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onItemClick("" + curr.getOrderId());
            }
        });
    }

    public void selectedAll(ShippingChargesItemAdapter.ViewHolder holder) {
        if (Common.shippingChargeMasterIV) {
            for (int i = 0; i < getItemCount(); i++) {
                holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_selected);

            }
        } else {
            for (int i = 0; i < getItemCount(); i++) {
                holder.binding.cbIV.setImageResource(R.drawable.orange_checkbox_unselected);
            }
        }
    }


    @Override
    public int getItemCount() {
        return listItem != null ? listItem.size() : 0;
    }

    public List<AllShippingChargeResponse.Record> getList() {
        return listItem;
    }

    public boolean checkIsAllItemSelected() {
        boolean check = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (listItem.get(i).isSelected()) check = true;
            else {
                check = false;
                break;
            }
        }
        return check;
    }

    public boolean isAnyItemSelected() {
        boolean check = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (listItem.get(i).isSelected()) {
                check = true;
                break;
            }

        }
        return check;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> records = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (listItem.get(i).isSelected()) {
                records.add(listItem.get(i).getId());
            }

        }
        return records;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ShippingChargeItemBinding binding;

        public ViewHolder(@NonNull ShippingChargeItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnClick {
        public void onClickListener();

        public void onItemClick(String id);
    }
}
