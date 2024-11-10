package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.UserWorklogCardBinding;
import com.devstringx.mytylesstockcheck.datamodal.userWorklog.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class UserWorklogAdapter extends RecyclerView.Adapter<UserWorklogAdapter.ViewHolder> {
    OnWorklogCardClick onItemClick;
    private Context context;
    private List<RecordsItem> listItems = new ArrayList<>();
    private UserWorklogAttachmentAdapter adapter;
    public UserWorklogAdapter(Context context, OnWorklogCardClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
    }
    public void refreshList(List<RecordsItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserWorklogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserWorklogCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_worklog_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserWorklogAdapter.ViewHolder holder, int position) {
        RecordsItem item = listItems.get(position);
        holder.binding.dateTV.setText(item.getWorkingDate());
        if(item.getHoursWorked()==null || item.getHoursWorked().isEmpty()) {
            holder.binding.otTV.setVisibility(View.GONE);
            holder.binding.timeTV.setVisibility(View.VISIBLE);
            holder.binding.timeTV.setText(item.getCreatedTime());
        }
        else {
            holder.binding.otTV.setVisibility(View.VISIBLE);
            holder.binding.timeTV.setVisibility(View.GONE);
            holder.binding.otTV.setText("OT: "+item.getHoursWorked()+" hr(s)");
        }
        if(item.getStatus().equalsIgnoreCase("Present")) {
            holder.binding.statusTV.setTextColor(context.getColor(R.color.green));
            holder.binding.statusTV.setBackgroundResource(R.drawable.present_label_bg);
            holder.binding.statusTV.setText(item.getStatus());
            holder.binding.commentTV.setTextColor(context.getColor(R.color.gray));
        }
        else {
            holder.binding.statusTV.setTextColor(context.getColor(R.color.red));
            holder.binding.statusTV.setBackgroundResource(R.drawable.absent_label_bg);
            holder.binding.statusTV.setText(item.getStatus());
            holder.binding.commentTV.setTextColor(context.getColor(R.color.red));
        }
        if(item.getComment() != null && !item.getComment().isEmpty())
            holder.binding.commentTV.setText(item.getComment());
        else
            holder.binding.commentTV.setText("N/A");
        if(item.getMedias().size()>0) {
            holder.binding.worklogAttachmentRV.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = holder.binding.worklogAttachmentRV;
            adapter = new UserWorklogAttachmentAdapter(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            recyclerView.setAdapter(adapter);
            adapter.refreshList(item.getMedias());
        }
        else {
            holder.binding.worklogAttachmentRV.setVisibility(View.GONE);
        }
        holder.binding.worklogCardCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(String.valueOf(item.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private UserWorklogCardBinding binding;
        public ViewHolder(UserWorklogCardBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface OnWorklogCardClick {
        void onItemClick(String id);
    }
}
