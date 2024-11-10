package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.OrderListingItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllOrders.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class OrderListingAdapter extends RecyclerView.Adapter<OrderListingAdapter.ViewHolder> {
    OnOrderItemCLick onItemCLick;
    List<RecordsItem> records = new ArrayList<>();

    Context context;
    public OrderListingAdapter(Context context, OnOrderItemCLick onItemCLick) {
        this.context=context;
        this.onItemCLick=onItemCLick;
    }
    public void refreshList(List<RecordsItem> records) {
        this.records=records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_listing_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordsItem item = records.get(position);
        holder.binding.orderId.setText("#"+item.getSaleOrderNo().toString());
        holder.binding.dateTimeTV.setText(item.getCreatedAt());
        holder.binding.type.setText(item.getDeliveryLocationType());
        holder.binding.customerNameTV.setText(item.getCustomerName());
        holder.binding.deliveryTypeTV.setText(item.getDeliveryType());
        holder.binding.completeBeforeTV.setText(item.getDeliveryDate()+" "+item.getDeliveryTime());
        holder.binding.salesExeTV.setText(item.getSalesExecutiveName());
        holder.binding.paymentStatusTV.setText(item.getPaymentStatus());
        if (item.getIsDelayed().equalsIgnoreCase("yes")) holder.binding.delayedRL.setText("Delay");
        else holder.binding.delayedRL.setText("");
        holder.binding.amountTv.setText("₹ "+item.getOrderAmount()+"/-");
        switch (item.getStatus()) {
            case "To_Be_Approved_From_Account":
                holder.binding.CreatedTag.setText("Acc. Pending");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.gray_bg10);
                break;
            case "Pending_At_Dispatch_Manager":
                holder.binding.CreatedTag.setText("Pending");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            case "Po_Sent":
                holder.binding.CreatedTag.setText("Pending");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            case "Order_Processed":
                holder.binding.CreatedTag.setText("Assign");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.black_round_bg);
                break;
            case "Loading":
                holder.binding.CreatedTag.setText("loading");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Reaching_Store":
                holder.binding.CreatedTag.setText("Reached");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Ready_For_Pick_Up":
                holder.binding.CreatedTag.setText("Ready");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Dispatched":
                holder.binding.CreatedTag.setText("Dispatched");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Delivered":
            case "Picked_Up":
                holder.binding.CreatedTag.setText("Completed");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.green_round_bg);
                break;
            case "Cancelled":
                holder.binding.CreatedTag.setText("Cancelled");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            default:
                holder.binding.CreatedTag.setText("Acc. Pending");
                holder.binding.CreatedTag.setTextColor(context.getColor(R.color.white));
                holder.binding.CreatedTag.setBackgroundResource(R.drawable.gray_bg10);
        }        holder.binding.orderCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLick.onItemClick(String.valueOf(item.getId()), false);
            }
        });
        holder.binding.commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLick.onItemClick(String.valueOf(item.getId()),true);
            }
        });

        holder.binding.locIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLick.onLocationItemClick(String.valueOf(item.getSaleOrderNo()),item.getDeliveryAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (records.isEmpty())return 0;
        else return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderListingItemBinding binding;
        public ViewHolder(@NonNull OrderListingItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface OnOrderItemCLick{
        void onItemClick(String position, boolean b);
        void onLocationItemClick(String position,String deliveryAddress);
    }
}
