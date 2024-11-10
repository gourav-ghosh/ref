package com.devstringx.mytylesstockcheck.adapter;

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
import com.devstringx.mytylesstockcheck.databinding.UploadedItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.MediasItem;

import java.util.ArrayList;
import java.util.List;

public class ImagePDFCardSetup extends RecyclerView.Adapter<ImagePDFCardSetup.ViewHolder> {
    private Context mContext;
    private ViewFile viewFile;
    List<MediasItem> list = new ArrayList<>();
    public boolean editable;

    public ImagePDFCardSetup(Context context, List<MediasItem> list, boolean editable,ViewFile viewFile) {
        this.mContext=context;
        this.list=list;
        this.editable=editable;
        this.viewFile=viewFile;
    }
    public void refreshAdapter(Context context, boolean editable) {
        this.mContext=context;
        this.editable=editable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImagePDFCardSetup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UploadedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.uploaded_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePDFCardSetup.ViewHolder holder, int position) {
        MediasItem currentItem = list.get(position);
        holder.binding.itemTitleTV.setText(currentItem.getName());
        holder.binding.itemSizeDateTV.setText(currentItem.getCreatedAt());
        if (editable) holder.binding.deleteItem.setVisibility(View.GONE);
        else holder.binding.deleteItem.setVisibility(View.VISIBLE);
        if (currentItem.getMediaType().equalsIgnoreCase("pdf")){
            Glide.with(mContext)
                    .load(R.drawable.ic_pdf_svg)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.logo);
        }else {
            Glide.with(mContext)
                    .load(currentItem.getMinPath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.logo);
        }
        holder.binding.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> deleteMediaList = new ArrayList<>();
                if (!holder.binding.cardView.isSelected()){
                    deleteMediaList.add(String.valueOf(currentItem.getId()));
                    holder.binding.cardView.setSelected(true);
                }
                else{
                    deleteMediaList.remove(String.valueOf(currentItem.getId()));
                    holder.binding.cardView.setSelected(false);
                }
                viewFile.deleteFileListener(deleteMediaList,String.valueOf(currentItem.getArchitectId()));
            }
        });
        holder.binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                currentItem.setSelected(true);
                viewFile.viewFileListener(list);
            }
        });
//        if(deleteFileListner==null){
//            holder.binding.deleteItem.setVisibility(View.GONE);
//        }else{
//            holder.binding.deleteItem.setVisibility(View.VISIBLE);
//        }
//        holder.binding.deleteItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(deleteFileListner!=null){
//                    deleteFileListner.removeImageListner(position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UploadedItemBinding binding;
        public ViewHolder(@NonNull UploadedItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface deleteFile{
        public void deleteFileListner(int position);
    }
    public interface ViewFile{
        public void viewFileListener(List<MediasItem> list);
        public void deleteFileListener(List<String> list , String id);

    }
}
