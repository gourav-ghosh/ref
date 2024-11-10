package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.InstructionFileInfoBinding;
import com.devstringx.mytylesstockcheck.databinding.UploadFileInfoBinding;
import com.devstringx.mytylesstockcheck.datamodal.FilePickerData;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstructionFileAdapter extends RecyclerView.Adapter<InstructionFileAdapter.ViewHolder> {
    private List<FilePickerData> mImages=new ArrayList<>();
    private Context mContext;
    private InstructionListner removeImageListner;
    public InstructionFileAdapter(Context context, List<FilePickerData> images, InstructionListner removeImageltr) {
        this.mImages=images;
        this.mContext=context;
        removeImageListner=removeImageltr;
    }

    public void refreshList(List<FilePickerData> images){
        this.mImages=images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InstructionFileInfoBinding uploadFileInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.instruction_file_info, parent, false);
        return new ViewHolder(uploadFileInfoBinding);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mImages.get(position).getFileType().equalsIgnoreCase("png")) {
            Glide.with(mContext)
                    .load(mImages.get(position).getFilePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.fileInfoIv);
        }else
        holder.binding.fileInfoIv.setImageResource(mImages.get(position).getFileType().equalsIgnoreCase("link")?R.drawable.ic_link_attachment:R.drawable.ic_pdf);
        holder.binding.fileTv.setText(mImages.get(position).getFileName());
        holder.binding.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeImageListner!=null){
                    removeImageListner.removeInstructionFileListner(position);
                }
            }
        });
        holder.binding.rowLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeImageListner!=null){
                    removeImageListner.openFileListner(mImages.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InstructionFileInfoBinding binding;

        public ViewHolder(@NonNull InstructionFileInfoBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface InstructionListner{
        public void removeInstructionFileListner(int position);
        public void openFileListner(FilePickerData filepath);
    }

}
