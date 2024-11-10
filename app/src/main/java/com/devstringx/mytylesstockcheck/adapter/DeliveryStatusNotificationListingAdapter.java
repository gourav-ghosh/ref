package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.DeliveryStatusNotificationItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersNotification.OrderNotificationRecordsItem;

import java.util.ArrayList;
import java.util.List;

public class DeliveryStatusNotificationListingAdapter extends RecyclerView.Adapter<DeliveryStatusNotificationListingAdapter.ViewHolder> {
    List<OrderNotificationRecordsItem> records = new ArrayList<>();
    Context context;
    OnClick onClick;

    public DeliveryStatusNotificationListingAdapter(Context context,OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public DeliveryStatusNotificationListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeliveryStatusNotificationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.delivery_status_notification_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryStatusNotificationListingAdapter.ViewHolder holder, int position) {
        OrderNotificationRecordsItem item = records.get(position);
        if (item.getIsRead().equalsIgnoreCase("0")) holder.binding.orderCardView.setCardBackgroundColor(context.getResources().getColor(R.color.very_light_bluish));
        else holder.binding.orderCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        holder.binding.titleTV.setText(item.getTitle());
        holder.binding.bodyTV.setText(item.getBody());
        holder.binding.timeTV.setText(item.getRequestReceivedTimeNotification());
        holder.binding.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onCLickListener(item.getId(),item.getOrderId(),item.getNotificationType());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (records.isEmpty()) return 0;
        return records.size();
    }

    public void refreshList(List<OrderNotificationRecordsItem> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DeliveryStatusNotificationItemBinding binding;
        public ViewHolder(@NonNull DeliveryStatusNotificationItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface OnClick{
        public void onCLickListener(String user_id, String id, String notificationType);
    }
}
