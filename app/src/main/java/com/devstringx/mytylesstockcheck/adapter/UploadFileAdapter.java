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
import com.devstringx.mytylesstockcheck.databinding.UploadFileInfoBinding;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.MediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadFileAdapter extends RecyclerView.Adapter<UploadFileAdapter.ViewHolder> {
    private List<Images> mImages = new ArrayList<>();
    private List<MediasItem> mediasItems = new ArrayList<>();
    private Context mContext;
    private removeImage removeImageListner;
    private DeleteMediaItems  deleteMediaItemsListener;
    private String type;
    private String delete;
    private List<ComplaintMediasItem> complaintMediaList = new ArrayList<>();
    private List<ResponseMediasItem> responseMediaList = new ArrayList<>();
    public boolean complaintShowMore = false;
    public boolean resolveShowMore = false;
    public List<String> selectedMediaId = new ArrayList<>();
    public List<String> selectedFileId = new ArrayList<>();
    private ViewFile viewFile;

    public UploadFileAdapter(Context context, List<Images> images, removeImage removeImageltr) {
        this.mImages = images;
        this.mContext = context;
        removeImageListner = removeImageltr;
    }

    public UploadFileAdapter(Context context, List<ComplaintMediasItem> complaintMedias, String complaint, ViewFile viewFile) {
        this.mContext = context;
        this.complaintMediaList = complaintMedias;
        this.type = complaint;
        this.viewFile = viewFile;
    }

    public UploadFileAdapter(Context context, String response, List<ResponseMediasItem> responseMedias, ViewFile viewFile) {
        this.mContext = context;
        this.responseMediaList = responseMedias;
        this.type = response;
        this.viewFile = viewFile;
    }

    public UploadFileAdapter(Context context, List<ComplaintMediasItem> complaintMedias, String type, String delete, DeleteMediaItems deleteMediaItemsListener) {
        this.mContext = context;
        this.complaintMediaList = complaintMedias;
        this.type = type;
        this.delete = delete;
        this.deleteMediaItemsListener=deleteMediaItemsListener;
    }

    public UploadFileAdapter(Context context, String type, List<ResponseMediasItem> responseMedias, String delete, DeleteMediaItems deleteMediaItemsListener) {
        this.mContext = context;
        this.responseMediaList = responseMedias;
        this.type = type;
        this.delete = delete;
        this.deleteMediaItemsListener=deleteMediaItemsListener;
    }
//    public void refreshList(List<Images> images, String contentType) {
//        this.contentType=contentType;
//        this.mImages=images;
//        notifyDataSetChanged();
//    }

    public void refreshList(List<Images> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }
    public void refreshMedia(List<ResponseMediasItem> responseMediasItemList){
        this.responseMediaList = responseMediasItemList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UploadFileInfoBinding uploadFileInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.upload_file_info, parent, false);
        return new ViewHolder(uploadFileInfoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (type != null) {
            if (type.equalsIgnoreCase("complaint")) {
                ComplaintMediasItem currentMedia = complaintMediaList.get(position);
                Common.showLog("complaintMedia===== " + currentMedia.getThumbnailPath());
                if (currentMedia.getMediaType().equalsIgnoreCase("video")){
                        Glide.with(mContext)
                                .load(R.drawable.ic_video)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(holder.binding.fileIv);
                }else if(currentMedia.getMediaType().equalsIgnoreCase("document")){
                    Glide.with(mContext)
                            .load(R.drawable.ic_pdf_svg)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(holder.binding.fileIv);
                }else {
                    Glide.with(mContext)
                            .load(currentMedia.getThumbnailPath())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(holder.binding.fileIv);
                }
                if (complaintMediaList.size() > 3 && position == 2 && !complaintShowMore && delete == null) {
                    holder.binding.fileCountLl.setVisibility(View.VISIBLE);
                    holder.binding.fileCountTv.setText(String.valueOf(complaintMediaList.size() - 2) + "+");
                } else {
                    holder.binding.fileCountLl.setVisibility(View.GONE);
                }
            } else if (type.equalsIgnoreCase("response")) {
                ResponseMediasItem currentMedia = responseMediaList.get(position);
                Common.showLog("resolveMedia===== " + currentMedia.getThumbnailPath());
                if(currentMedia.getDeletedAt() == null) {
                    if (currentMedia.getMediaType().equalsIgnoreCase("video")){
                        Glide.with(mContext)
                                .load(R.drawable.ic_video)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(holder.binding.fileIv);
                    }else if(currentMedia.getMediaType().equalsIgnoreCase("document")){
                        Glide.with(mContext)
                                .load(R.drawable.ic_pdf_svg)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(holder.binding.fileIv);
                    }else {
                        Glide.with(mContext)
                                .load(currentMedia.getThumbnailPath())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(holder.binding.fileIv);
                    }
                }
                if (responseMediaList.size() > 3 && position == 2 && !resolveShowMore && delete==null) {
                    holder.binding.fileCountLl.setVisibility(View.VISIBLE);
                    holder.binding.fileCountTv.setText(String.valueOf(responseMediaList.size() - 2) + "+");
                } else {
                    holder.binding.fileCountLl.setVisibility(View.GONE);
                }
            }
            if (delete != null) {
                holder.binding.checkIv.setVisibility(View.VISIBLE);
                holder.binding.fileIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!complaintMediaList.isEmpty()) {
                            ComplaintMediasItem currentMediasItem = complaintMediaList.get(position);
                            if (!currentMediasItem.isSelected()){
                                currentMediasItem.setSelected(true);
                                holder.binding.checkIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                                if (currentMediasItem.getMediaType().equalsIgnoreCase("document"))
                                    selectedFileId.add(String.valueOf(currentMediasItem.getId()));
                                else selectedMediaId.add(String.valueOf(currentMediasItem.getId()));

                            }else {
                                currentMediasItem.setSelected(false);
                                if (currentMediasItem.getMediaType().equalsIgnoreCase("document"))
                                    selectedFileId.remove(String.valueOf(currentMediasItem.getId()));
                                else selectedMediaId.remove(String.valueOf(currentMediasItem.getId()));
                                holder.binding.checkIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                            }
                            deleteMediaItemsListener.deleteMediaItemsListener(selectedMediaId, selectedFileId);
                        }else {
                            ResponseMediasItem currentMediasItem = responseMediaList.get(position);
                            if (!currentMediasItem.isSelected()){
                                currentMediasItem.setSelected(true);
                                holder.binding.checkIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                                if (currentMediasItem.getMediaType().equalsIgnoreCase("document"))
                                    selectedFileId.add(String.valueOf(currentMediasItem.getId()));
                                else selectedMediaId.add(String.valueOf(currentMediasItem.getId()));
                            }else {
                                currentMediasItem.setSelected(false);
                                if (currentMediasItem.getMediaType().equalsIgnoreCase("document"))
                                 selectedFileId.remove(String.valueOf(currentMediasItem.getId()));
                                else selectedMediaId.remove(String.valueOf(currentMediasItem.getId()));
                                holder.binding.checkIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                            }

                            deleteMediaItemsListener.deleteMediaItemsListener(selectedMediaId,selectedFileId);
                        }
                    }
                });
            } else {
                holder.binding.checkIv.setVisibility(View.GONE);
                holder.binding.fileIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!complaintMediaList.isEmpty()) {
                            for (int i = 0; i < complaintMediaList.size(); i++) {
                                complaintMediaList.get(i).setSelected(false);
                            }
                            complaintMediaList.get(position).setSelected(true);
                            viewFile.viewFileListener(complaintMediaList, null, "complaint");
                        } else {
                            for (int i = 0; i < responseMediaList.size(); i++) {
                                responseMediaList.get(i).setSelected(false);
                            }
                            responseMediaList.get(position).setSelected(true);
                            viewFile.viewFileListener(null, responseMediaList, "response");
                        }
                        Common.showLog("mediaItemEmpty");
                        Common.showLog("position > array" + " pos=" + position + " complaint array" + complaintMediaList.size() + " response array=" + responseMediaList.size());
                    }
                });
            }
        } else {
            Common.showLog("=====IMG==ADAPTER=" + mImages.get(position).getLeadAttachment().get(0));
            String fileExtension = Common.getFileExtension(mImages.get(position).getLeadAttachment().get(0));
            // Determine content type based on file extension
            String contentType = Common.getContentType(fileExtension);
            Common.showLog(contentType);
            if (contentType.equalsIgnoreCase("application/pdf")) {
                Glide.with(mContext)
                        .load(R.drawable.ic_pdf_svg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.binding.fileIv);
            }else {
                Glide.with(mContext)
                        .load(mImages.get(position).getId() == 0 ? new File(mImages.get(position).getLeadAttachment().get(0)) : mImages.get(position).getLeadAttachment().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.binding.fileIv);
            }
        }
        if (removeImageListner == null) {
            holder.binding.deleteIv.setVisibility(View.GONE);
        } else {
            holder.binding.deleteIv.setVisibility(View.VISIBLE);
        }
        holder.binding.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeImageListner != null) {
                    removeImageListner.removeImageListner(position);
                }
            }
        });
    }
//    private static Bitmap extractFrameFromVideo(String videoPath) {
//        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
//        Bitmap bitmap = null;
//        try {
//            mmr.setDataSource(videoPath);
//            bitmap = mmr.getFrameAtTime(1000000); // Get frame at 1 second
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            mmr.release();
//        }
//        return bitmap;
//    }

    @Override
    public int getItemCount() {
        if (type != null) {
            if (type.equalsIgnoreCase("complaint"))
                return complaintMediaList.size();
            else return responseMediaList.size();
        } else return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UploadFileInfoBinding binding;

        public ViewHolder(@NonNull UploadFileInfoBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface removeImage {
        public void removeImageListner(int position);
    }

    public interface ViewFile {
        public void viewFileListener(List<ComplaintMediasItem> complaintMediasItemList, List<ResponseMediasItem> responseMediasItemList, String type);
    }
    public interface DeleteMediaItems{
        public void deleteMediaItemsListener(List<String> selectedMediaId, List<String> selectedFileId);
    }
}
