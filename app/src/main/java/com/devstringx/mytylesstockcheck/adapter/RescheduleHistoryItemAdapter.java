package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.RescheduleHistoryListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderRescheduleDatasItem;

import java.util.List;

public class RescheduleHistoryItemAdapter extends RecyclerView.Adapter<RescheduleHistoryItemAdapter.ViewHolder> {
    private Context context;
    List<OrderRescheduleDatasItem> orderRescheduleDatas;
    public RescheduleHistoryItemAdapter(Context context, List<OrderRescheduleDatasItem> order_reschedule_dates) {
        this.orderRescheduleDatas = order_reschedule_dates;
        this.context=context;
    }

    @NonNull
    @Override
    public RescheduleHistoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RescheduleHistoryListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.reschedule_history_list_item, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RescheduleHistoryItemAdapter.ViewHolder holder, int position) {
        if (orderRescheduleDatas.size()==1){
            if (position==1) {
                holder.binding.rescheduleHistoryColor.setBackgroundResource(R.color.green);
                holder.binding.inputLayout.setVisibility(View.GONE);
                holder.binding.rescheduleDateTV.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.binding.rescheduleTypeTV.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.binding.updateOnTV.setVisibility(View.GONE);
                if (orderRescheduleDatas.get(0).getNewDeliveryTime() != null)
                    holder.binding.rescheduleDateTV.setText(orderRescheduleDatas.get(0).getNewDeliveryDate() + ", " + orderRescheduleDatas.get(0).getNewDeliveryTime());
                else holder.binding.rescheduleDateTV.setText(orderRescheduleDatas.get(0).getNewDeliveryDate());
                holder.binding.oldRL.setBackgroundResource(R.drawable.green_border_round4_bg);
                holder.binding.rescheduleTypeTV.setText("New");
            } else {
                holder.binding.rescheduleHistoryColor.setBackgroundResource(R.color.red);
                holder.binding.inputLayout.setVisibility(View.VISIBLE);
                holder.binding.rescheduleDateTV.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.binding.rescheduleTypeTV.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (orderRescheduleDatas.get(0).getOldDeliveryTime() != null)
                    holder.binding.rescheduleDateTV.setText(orderRescheduleDatas.get(0).getOldDeliveryDate() + ", " + orderRescheduleDatas.get(0).getOldDeliveryTime());
                else holder.binding.rescheduleDateTV.setText(orderRescheduleDatas.get(0).getOldDeliveryDate());
                holder.binding.updateOnTV.setVisibility(View.VISIBLE);
                holder.binding.reasonET.setText(orderRescheduleDatas.get(0).getReasonForReschedule());
                holder.binding.updateOnTV.setText(orderRescheduleDatas.get(0).getUpdatedAt());
                holder.binding.oldRL.setBackgroundResource(R.drawable.red_border_round_bg);
                holder.binding.rescheduleTypeTV.setText("Old");
            }
        }else {
            OrderRescheduleDatasItem item = orderRescheduleDatas.get(position);
            if (item.getId() == orderRescheduleDatas.get(orderRescheduleDatas.size() - 1).getId()) {
                holder.binding.rescheduleHistoryColor.setBackgroundResource(R.color.green);
                holder.binding.inputLayout.setVisibility(View.GONE);
                holder.binding.rescheduleDateTV.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.binding.rescheduleTypeTV.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.binding.updateOnTV.setVisibility(View.GONE);
                if (item.getNewDeliveryTime() != null)
                    holder.binding.rescheduleDateTV.setText(item.getNewDeliveryDate() + ", " + item.getNewDeliveryTime());
                else holder.binding.rescheduleDateTV.setText(item.getNewDeliveryDate());
                holder.binding.oldRL.setBackgroundResource(R.drawable.green_border_round4_bg);
                holder.binding.rescheduleTypeTV.setText("New");
            } else {
                holder.binding.rescheduleHistoryColor.setBackgroundResource(R.color.red);
                holder.binding.inputLayout.setVisibility(View.VISIBLE);
                holder.binding.rescheduleDateTV.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.binding.rescheduleTypeTV.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (item.getOldDeliveryTime() != null)
                    holder.binding.rescheduleDateTV.setText(item.getNewDeliveryDate() + ", " + item.getNewDeliveryTime());
                else holder.binding.rescheduleDateTV.setText(item.getOldDeliveryDate());
                holder.binding.updateOnTV.setVisibility(View.VISIBLE);
                holder.binding.reasonET.setText(item.getReasonForReschedule());
                holder.binding.updateOnTV.setText(item.getUpdatedAt());
                holder.binding.oldRL.setBackgroundResource(R.drawable.red_border_round_bg);
                holder.binding.rescheduleTypeTV.setText("Old");
            }
        }
    }

    @Override
    public int getItemCount() {
        return (orderRescheduleDatas.size() == 1) ? 2 : orderRescheduleDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RescheduleHistoryListItemBinding binding;
        public ViewHolder(@NonNull RescheduleHistoryListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
}
