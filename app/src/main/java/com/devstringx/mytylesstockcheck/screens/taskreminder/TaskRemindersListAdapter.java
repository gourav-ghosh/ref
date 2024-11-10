package com.devstringx.mytylesstockcheck.screens.taskreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.AllTaskRemindersRvListBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskRemindersListAdapter extends RecyclerView.Adapter<TaskRemindersListAdapter.ViewHolder> {
    OnTaskReminderItemClick onItemCLick;
    List<TaskReminderItem> records = new ArrayList<>();

    Context context;

    public TaskRemindersListAdapter(Context context, OnTaskReminderItemClick onItemCLick) {
        this.context = context;
        this.onItemCLick = onItemCLick;
    }

    public void refreshList(List<TaskReminderItem> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskRemindersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllTaskRemindersRvListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.all_task_reminders_rv_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRemindersListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaskReminderItem item = records.get(position);
        holder.binding.orderId.setText("#" + item.getSaleOrderNo());
        holder.binding.dateTimeTV.setText(item.getCreatedAt());
        String commnet = item.getComment();
        String[] split = commnet.split(".\\n");
        String assignedTo = split[0];
        holder.binding.assignedToTV.setText(assignedTo.split(":")[1]);
        holder.binding.commentTV.setText(split[1]);
        holder.binding.deadlineTv.setText(item.getScheduleDate()+" "+item.getScheduleTime());

        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLick.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (records.isEmpty()) return 0;
        else return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AllTaskRemindersRvListBinding binding;

        public ViewHolder(@NonNull AllTaskRemindersRvListBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnTaskReminderItemClick {
        void onItemClick(TaskReminderItem item);
    }
}
