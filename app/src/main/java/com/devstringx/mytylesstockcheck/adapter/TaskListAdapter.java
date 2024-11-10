package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.AllTaskRvListBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeadTask.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private List<RecordsItem> listItems=new ArrayList<>();
    private TaskItemsBtn clickBtn;
    private boolean fromBottomSheet=false;
    public void refreshList(List<RecordsItem> recordsItems, TaskItemsBtn clickBtn){
        this.clickBtn=clickBtn;
        listItems=recordsItems;
        notifyDataSetChanged();
    }
//    public TaskListAdapter(Context context, LeadListAdapter.LeadListBtn clickBtn, String type) {
//        this.clickBtn=clickBtn;
//        this.mContext=context;
//    }
    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllTaskRvListBinding taskListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.all_task_rv_list, parent, false);
        return new ViewHolder(taskListBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordsItem item=listItems.get(position);
        holder.binding.followup.setText(item.getSubject());

        if (item.getStatus().equalsIgnoreCase("Completed")) {
            holder.binding.taskStatus.setText(Common.getProperCase(item.getStatus()));
            holder.binding.taskStatus.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            holder.binding.editTask.setVisibility(View.GONE);
            holder.binding.taskStatus.setBackgroundResource(R.drawable.green_round_bg);
            holder.binding.colorStrip.setBackgroundResource(R.drawable.green_strip_bg);
        }else{
            holder.binding.taskStatus.setText(Common.getProperCase(item.getStatus()));
            holder.binding.taskStatus.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_pencil,0);
            holder.binding.editTask.setVisibility(View.VISIBLE);
            holder.binding.taskStatus.setBackgroundResource(R.drawable.black_round_bg);
            holder.binding.colorStrip.setBackgroundResource(R.drawable.red_strip_bg);
        }
        holder.binding.dateTv.setText(Common.convertYYYYMMMdd(item.getFollowUpDate()));
        holder.binding.taskTv.setText("Task : "+item.getTaskDetails());
        holder.binding.timeTv.setText(item.getFollowUpTime());
        holder.binding.createdDateTv.setText("Created Date: "+Common.taskDateConvert(item.getCreatedAt()));
        if(item.getFullName()!=null)
        if(!item.getFullName().equalsIgnoreCase(""))
            holder.binding.leadTv.setText("Lead : "+item.getFullName()+" ");
        holder.binding.taskStatus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                if (!item.getStatus().equalsIgnoreCase("completed"))
                clickBtn.onBtnClickListner(position,item.getId(),"taskStatus",item);
            }
        });
        holder.binding.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onBtnClickListner(position, item.getId(),"rescheduleTask",item);
            }
        });
        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn.onBtnClickListner(position, item.getId(),fromBottomSheet?"bottomSheet":"details",item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AllTaskRvListBinding binding;
        public ViewHolder(@NonNull AllTaskRvListBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface TaskItemsBtn {
        void onBtnClickListner(int position , int id , String type,RecordsItem recordsItem);
    }

    public void setFromBottomSheet(boolean fromBottomSheet) {
        this.fromBottomSheet = fromBottomSheet;
    }
}
