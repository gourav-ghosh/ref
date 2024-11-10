package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.WorklogAttacthmentCardBinding;
import com.devstringx.mytylesstockcheck.datamodal.userWorklog.MediaItems;

import java.util.ArrayList;
import java.util.List;

public class UserWorklogAttachmentAdapter extends RecyclerView.Adapter<UserWorklogAttachmentAdapter.ViewHolder> {
    private Context context;
    private List<MediaItems> mediaItemsList = new ArrayList<>();
    public UserWorklogAttachmentAdapter(Context context) {
        this.context = context;
    }
    public void refreshList(List<MediaItems> mediaItemsList) {
        this.mediaItemsList = mediaItemsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserWorklogAttachmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorklogAttacthmentCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.worklog_attacthment_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserWorklogAttachmentAdapter.ViewHolder holder, int position) {
        MediaItems item = mediaItemsList.get(position);
        if(item.getMediaType().equalsIgnoreCase("image")) {
            holder.binding.doctypeIV.setImageResource(R.drawable.ic_img);
        } else if (item.getMediaType().equalsIgnoreCase("document")) {
            holder.binding.doctypeIV.setImageResource(R.drawable.ic_pdf);
        }
        else {
            holder.binding.doctypeIV.setImageResource(com.nareshchocha.filepickerlibrary.R.drawable.ic_video);
        }
        holder.binding.docName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mediaItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private WorklogAttacthmentCardBinding binding;
        public ViewHolder(WorklogAttacthmentCardBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}
