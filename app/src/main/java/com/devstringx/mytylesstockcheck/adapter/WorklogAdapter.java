package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.WorklogCardItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.allWorklogs.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class WorklogAdapter extends RecyclerView.Adapter<WorklogAdapter.ViewHolder> {
    OnUserWorklogClick onItemClick;
    private Context context;
    private List<RecordsItem> listItems=new ArrayList<>();
    public WorklogAdapter(Context context, OnUserWorklogClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
    }

    public void refreshList(List<RecordsItem> recordsItems) {
        this.listItems = recordsItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WorklogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorklogCardItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.worklog_card_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordsItem item = listItems.get(position);
        if(position % 2 != 0) holder.binding.worklogCV.setBackgroundResource(R.drawable.gray_border_orange_fill_bg);
        else holder.binding.worklogCV.setBackgroundResource(R.drawable.gray_border_white_fill_bg);
        if(item.getProfileImage()!=null) {
            ImageView profImage = holder.binding.profileImage;
            Glide.with(context).load(item.getProfileImage()).transform(new CenterCrop(), new CircleCrop()).into(profImage);
        }
        else {
            holder.binding.profileImage.setImageResource(R.drawable.ic_worklog_profile);
        }
        holder.binding.employeeNameTV.setText(item.getFirstName()+ " " +item.getLastName());
        holder.binding.employeeRoleTV.setText(item.getRoleName());
        if(item.getStatus().equalsIgnoreCase("Absent")) holder.binding.worklogCommentTV.setTextColor(context.getColor(R.color.red));
        else holder.binding.worklogCommentTV.setTextColor(context.getColor(R.color.black));
        holder.binding.worklogCommentTV.setText(item.getComment());
        holder.binding.worklogCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(String.valueOf(item.getUserId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private WorklogCardItemBinding binding;
        public ViewHolder(WorklogCardItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface OnUserWorklogClick{
        void onItemClick(String id);
    }
}
