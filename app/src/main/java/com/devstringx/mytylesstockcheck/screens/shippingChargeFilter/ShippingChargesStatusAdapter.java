package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.ShippingStatusItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ShippingChargesStatusAdapter extends RecyclerView.Adapter<ShippingChargesStatusAdapter.ViewHolder> {
    onStatusSelectedListner click;
    Context mContext;
    private List<ShippingStatus> listItem = new ArrayList<>();
    private static final String ALL = "All";
    private static final String PENDING = "Pending";
    private static final String APPROVED = "Approved";
    private static final String PAID = "Paid";
    private static final String REJECTED = "Rejected";

    public ShippingChargesStatusAdapter(Context context, List<ShippingStatus> list, onStatusSelectedListner click) {
        this.click = click;
        this.mContext = context;
        this.listItem = list;
    }

    public void refreshList(List<ShippingStatus> list) {
        this.listItem = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShippingChargesStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShippingStatusItemBinding statusItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shipping_status_item, parent, false);
        return new ViewHolder(statusItemBinding);
    }

    private void setupStatusValBackground(String status, TextView tvAmount) {
        switch (status) {
            case ALL:
                tvAmount.setBackgroundResource(R.drawable.black_round_bg);
                break;
            case PENDING:
                tvAmount.setBackgroundResource(R.drawable.dark_grey_round_bg);
                break;
            case APPROVED:
                tvAmount.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case PAID:
                tvAmount.setBackgroundResource(R.drawable.green_round_bg);
                break;
            case REJECTED:
                tvAmount.setBackgroundResource(R.drawable.red_round_bg);
                break;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingChargesStatusAdapter.ViewHolder holder, int position) {
        ShippingStatus curr = listItem.get(position);
        setupStatusValBackground(curr.getStatusName(),holder.binding.value);
        holder.binding.tittle.setText(curr.getStatusName());
        holder.binding.tittle.setSelected(curr.isSelected());
        holder.binding.value.setText("" + curr.getValue());
        holder.binding.tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!curr.isSelected()) {
                    resetAll();
                    curr.setSelected(true);
                    notifyDataSetChanged();
                    click.onStatusSelected(curr.getStatusName());
                }
            }
        });

    }

    private void resetAll() {
        for (ShippingStatus item : listItem) {
            item.setSelected(false);
        }
    }


    @Override
    public int getItemCount() {
        return listItem != null ? listItem.size() : 0;
    }

    public List<ShippingStatus> getList() {
        return listItem;
    }

    public void setSelTab(String statusTab) {
        for (ShippingStatus item : listItem) {
            if(statusTab.equalsIgnoreCase(item.getStatusName())){
                item.setSelected(true);
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ShippingStatusItemBinding binding;

        public ViewHolder(@NonNull ShippingStatusItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface onStatusSelectedListner {

        public void onStatusSelected(String status);
    }
}
