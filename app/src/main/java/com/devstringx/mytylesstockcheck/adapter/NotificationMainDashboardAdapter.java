package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.NotificationListMainDashboardBinding;
import com.devstringx.mytylesstockcheck.datamodal.taskNotification.RecordsItem;

import java.util.ArrayList;

public class NotificationMainDashboardAdapter extends RecyclerView.Adapter<NotificationMainDashboardAdapter.ViewHolder> {
    private ArrayList<RecordsItem> mRecordsItems = new ArrayList<>();
    private Context mContext;
    private NotificationUpdate mNotificationUpdate;

    public NotificationMainDashboardAdapter(Context context, ArrayList<RecordsItem> recordsItems,NotificationUpdate notificationUpdate) {
        mRecordsItems = recordsItems;
        mContext = context;
        mNotificationUpdate=notificationUpdate;
    }

    public ArrayList<RecordsItem> getmRecordsItems() {
        return mRecordsItems;
    }

    @NonNull
    @Override
    public NotificationMainDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationListMainDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_list_main_dashboard, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationMainDashboardAdapter.ViewHolder holder, int position) {
        holder.binding.itemTitle.setText(mRecordsItems.get(position).getSubject());
        holder.binding.taskDetails.setText("Task : " + mRecordsItems.get(position).getTaskDetails());
        holder.binding.timeAgoTv.setText(mRecordsItems.get(position).getRequestReceivedTimeNotification());
        holder.binding.createdDateTv.setText(mRecordsItems.get(position).getCreatedDate());
        holder.binding.dueDateTv.setText(mRecordsItems.get(position).getDueDate());
        if(mRecordsItems.get(position).getIsRead().equalsIgnoreCase("0")){
            holder.binding.rowCv.setCardBackgroundColor(mContext.getColor(R.color.light_grey));
        }else{
            holder.binding.rowCv.setCardBackgroundColor(Color.WHITE);
        }
        holder.binding.rowCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNotificationUpdate!=null)mNotificationUpdate.updateNotifationListner(position);
            }
        });
        holder.binding.whatsappIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openWhatsApp(mContext, mRecordsItems.get(position).getPrimaryPhone(), "Hi");
            }
        });
        holder.binding.phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openDialerPad(mContext, mRecordsItems.get(position).getPrimaryPhone());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecordsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NotificationListMainDashboardBinding binding;

        public ViewHolder(@NonNull NotificationListMainDashboardBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface NotificationUpdate{
        public void updateNotifationListner(int position);
    }
}
