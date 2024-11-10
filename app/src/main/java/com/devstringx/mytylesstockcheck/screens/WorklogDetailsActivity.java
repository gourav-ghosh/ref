package com.devstringx.mytylesstockcheck.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityWorklogDetailsBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.worklogDetails.Data;
import com.devstringx.mytylesstockcheck.datamodal.worklogDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.worklogDetails.Response;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorklogDetailsActivity extends AppCompatActivity implements UploadFileAdapter.ViewFile, UploadFileAdapter.removeImage, ResponseListner, UploadFileAdapter.DeleteMediaItems {
    ActivityWorklogDetailsBinding binding;
    private UploadFileAdapter imageAdapter;
    private Records item;
    private Dialog deleteMediaDialog;
    private Dialog deleteWorklogDialog;
    private List<String> selected_delete_media_list = new ArrayList<>();
    public List<String> selectedFileId = new ArrayList<>();
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();

    private ProgressBar progressBar;
    private String path;
    private PlayerView playerView;
    SimpleExoPlayer player;
    private RecyclerView dialogRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worklog_details);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_worklog_details);
        getWorklogDetails(getIntent().getStringExtra("worklogId"));

        binding.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                deleteWorklog();
                openDeleteWorklogDialog();
            }
        });
        binding.responseSelectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteFilesDialog(item, "response", "Attachments");
            }
        });
        if(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("12") || new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("1")) {
            binding.callIV.setVisibility(View.VISIBLE);
            binding.deleteIV.setVisibility(View.VISIBLE);
        }
        else {
            binding.callIV.setVisibility(View.GONE);
            binding.deleteIV.setVisibility(View.GONE);
        }
    }

    private void openDeleteWorklogDialog() {
        deleteWorklogDialog = new Dialog(this);
        deleteWorklogDialog.setContentView(R.layout.worklog_delete_dialog);
        deleteWorklogDialog.setCancelable(true);
        TextView text = deleteWorklogDialog.findViewById(R.id.worklog_dialog_text);
        TextView cancel = deleteWorklogDialog.findViewById(R.id.worklog_dialog_no);
        TextView confirm = deleteWorklogDialog.findViewById(R.id.worklog_dialog_yes);
        ImageView close = deleteWorklogDialog.findViewById(R.id.worklog_dialog_crossIV);
        text.setText("Are you sure, you want to delete "+ item.getUserFirstName() + " " + item.getUserLastName() +"'s worklog?");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWorklogDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWorklogDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWorklog();
            }
        });
        deleteWorklogDialog.show();
    }

    private void deleteWorklog() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("worklogId", item.getId());
        map.put(NKeys.DELETEWORKLOG, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_DELETEWORKLOG, map, true);
    }

    private void openDeleteFilesDialog(Records item, String type, String title) {
        deleteMediaDialog = new Dialog(this);
        deleteMediaDialog.setContentView(R.layout.delete_media_dialog);
        deleteMediaDialog.setCancelable(true);
        RecyclerView mediaRV = deleteMediaDialog.findViewById(R.id.mediaRV);
        TextView titleTV = deleteMediaDialog.findViewById(R.id.title_TV);
        TextView cancel = deleteMediaDialog.findViewById(R.id.dialog_cancel_btn);
        TextView delete = deleteMediaDialog.findViewById(R.id.dialog_delete_btn);
        titleTV.setText(title);
        UploadFileAdapter imageAdapter = new UploadFileAdapter((Context) this, type, item.getMediaItems(), "delete", (UploadFileAdapter.DeleteMediaItems) this);
        mediaRV.setHasFixedSize(true);
        mediaRV.setLayoutManager(new GridLayoutManager(this, 3));
        mediaRV.setAdapter(imageAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMediaDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteMediaApi();
            }
        });
        deleteMediaDialog.show();
    }

    private void callDeleteMediaApi() {
        if(selected_delete_media_list!=null && !selected_delete_media_list.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("fileId", selected_delete_media_list);
            map.put("fileType", "media");
            map.put(NKeys.DELETEWORKLOGFILE , new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_DELETEWORKLOGFILE, map, true);
        }
        if(selectedFileId!=null && !selectedFileId.isEmpty()) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("fileId", selectedFileId);
            map1.put("fileType", "document");
            map1.put(NKeys.DELETEWORKLOGFILE , new Gson().toJson(map1));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_DELETEWORKLOGFILE, map1, true);
        }
        getWorklogDetails(getIntent().getStringExtra("worklogId"));
    }

    private void getWorklogDetails(String worklogId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("worklogId", Integer.parseInt(worklogId));
        map.put(NKeys.GETWORKLOGDETAILS, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_GETWORKLOGDETAILS, map, true);
    }
    private void resolveFileViewDialog(List<ResponseMediasItem> list) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.full_size_image_dialog);
        Window window = dialog.getWindow();
        if(window!=null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        dialog.setCancelable(true);
        ImageView close = dialog.findViewById(R.id.close_IV);
        ZoomageView fullSizeViewIV = dialog.findViewById(R.id.image_viewer);
        ImageView next = dialog.findViewById(R.id.nextIV);
        ImageView prev = dialog.findViewById(R.id.prevIV);
        TextView count = dialog.findViewById(R.id.countTV);
        progressBar = dialog.findViewById(R.id.progress_bar);
        WebView webView = dialog.findViewById(R.id.dialog_web_view);
        playerView = dialog.findViewById(R.id.player_view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        resolveFileInFullView(list, webView, fullSizeViewIV, playerView, dialog.getContext(), count);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<list.size(); i++) {
                    if(list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if ((i + 1) < list.size())
                            list.get(i + 1).setSelected(true);
                        else list.get(0).setSelected(true);
                        break;
                    }
                }
                resolveFileInFullView(list, webView, fullSizeViewIV, playerView, dialog.getContext(), count);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<list.size(); i++) {
                    if(list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if (i != 0)
                            list.get(i - 1).setSelected(true);
                        else list.get(list.size() - 1).setSelected(true);
                        break;
                    }
                }
                resolveFileInFullView(list, webView, fullSizeViewIV, playerView, dialog.getContext(), count);
            }
        });
        dialog.show();
    }

    private void resolveFileInFullView(List<ResponseMediasItem> list, WebView web, ZoomageView fullSizeViewIV, PlayerView playerView, Context context, TextView count) {
        for (int i=0; i<list.size(); i++) {
            if(list.get(i).isSelected()) {
                count.setText((i+1) + "/" + list.size());
                if(list.get(i).getMediaType().equalsIgnoreCase("document")) {
                    if(player!=null) player.stop();
                    fullSizeViewIV.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    web.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            // Show loader when page starts loading
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            web.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    web.getSettings().setSupportZoom(true);
                    web.getSettings().setJavaScriptEnabled(true);
                    web.getSettings().setDatabaseEnabled(true);
                    String pdfUrl = list.get(i).getPath().toString();
                    Common.showLog("pdfUrl"+pdfUrl);
                    String googleDocsUrl = null;
                    try {
                        googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(pdfUrl, "ISO-8859-1");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    web.loadUrl(googleDocsUrl);
                }
                if (list.get(i).getMediaType().equalsIgnoreCase("video")) {
                    web.setVisibility(View.GONE);
                    fullSizeViewIV.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);

                    // Initialize the player and set the video URL
                    player = new SimpleExoPlayer.Builder(context).build();
                    playerView.setPlayer(player);
                    MediaItem mediaItem = MediaItem.fromUri(list.get(i).getPath());
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();

                } else {
                    if (player!=null) player.stop();
                    web.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    fullSizeViewIV.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(list.get(i).getPath())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
//                            .error(R.drawable.spinner_load)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // Hide loader if image loading fails
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Image loaded successfully
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(fullSizeViewIV);
                }
            }
        }
    }
    @Override
    public void viewFileListener(List<ComplaintMediasItem> complaintMediasItemList, List<ResponseMediasItem> responseMediasItemList, String type) {
        resolveFileViewDialog(responseMediasItemList);
    }

    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETWORKLOGDETAILS) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                item = response.getData().getRecords().get(0);
                setWorklogDetails();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEWORKLOGFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
//                    if(jsonObject.optString("status").equalsIgnoreCase("200")) {
                        deleteMediaDialog.dismiss();
                        Common.showToast(this, jsonObject.optString("message").toString());
//                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException();
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEWORKLOG) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if(jsonObject.optString("status").equalsIgnoreCase("200")) {
                        deleteWorklogDialog.dismiss();
                        Common.showToast(this, jsonObject.optString("message").toString());
                        onBackPressed();
                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException();
                }
            }
        }
        else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseDO.getResponse());
                Common.showToast(this, String.valueOf(jsonObject.optString("message", "")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeImageListner(int position) {
        if(imagesArrayList.get(position).getId() != 0) removedImagesArrayList.add(imagesArrayList.get(position));
        else SELECTEDFILES.remove(position);
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
        if(imagesArrayList.isEmpty()) dialogRecyclerView.setVisibility(View.GONE);
        else dialogRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteMediaItemsListener(List<String> selectedMediaId, List<String> selectedFileId) {
        selected_delete_media_list = selectedMediaId;
        this.selectedFileId = selectedFileId;
    }

    private void setWorklogDetails() {
        binding.dateTV.setText(item.getWorkingDate());
        if(item.getUserProfileImage()!=null) {
            ImageView profileImage = binding.profileImgIV;
            Glide.with(WorklogDetailsActivity.this).load(item.getUserProfileImage()).transform(new CenterCrop(), new CircleCrop()).into(profileImage);
        }
        else {
            binding.profileImgIV.setImageResource(R.drawable.ic_worklog_profile);
        }
        binding.userNameTV.setText(item.getUserFirstName()+" "+item.getUserLastName());
        binding.roleTV.setText(item.getUserRoleName());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(WorklogDetailsActivity.this, item.getUserPhone());
            }
        });
        if(item.getComment()!=null && !item.getComment().isEmpty()) binding.commentTV.setText(item.getComment());
        else binding.commentTV.setText("N/A");
        if(item.getMediaItems().isEmpty()) {
            binding.responseSelectTV.setVisibility(View.GONE);
            binding.noAttachmentTV.setVisibility(View.VISIBLE);
            binding.uploadFileRv.setVisibility(View.GONE);
        }
        else {
            if(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("12") || new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("1")) {
                binding.responseSelectTV.setVisibility(View.VISIBLE);
            }
            binding.noAttachmentTV.setVisibility(View.GONE);
            binding.uploadFileRv.setVisibility(View.VISIBLE);

            UploadFileAdapter imageAdapter = new UploadFileAdapter((Context) this, "response", item.getMediaItems(), (UploadFileAdapter.ViewFile) this);
            binding.uploadFileRv.setHasFixedSize(true);
            binding.uploadFileRv.setLayoutManager(new GridLayoutManager(this, 3));
            binding.uploadFileRv.setAdapter(imageAdapter);
            binding.resolveSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.resolveSeeMore.getText().toString().equalsIgnoreCase("Show more")) {
                        ViewGroup.LayoutParams layoutParams = binding.uploadFileRv.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        binding.uploadFileRv.setLayoutParams(layoutParams);
                        imageAdapter.resolveShowMore = true;
                        binding.resolveSeeMore.setText("Show less");
                    } else {
                        float density = binding.uploadFileRv.getContext().getResources().getDisplayMetrics().density;
                        int heightInPixels = (int) (120 * density + 0.5f);
                        ViewGroup.LayoutParams layoutParams = binding.uploadFileRv.getLayoutParams();
                        layoutParams.height = heightInPixels;
                        binding.uploadFileRv.setLayoutParams(layoutParams);
                        imageAdapter.resolveShowMore = false;
                        binding.resolveSeeMore.setText("Show more");
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            });
        }
        if(item.getMediaItems().size()>3) binding.resolveSeeMore.setVisibility(View.VISIBLE);
        else binding.resolveSeeMore.setVisibility(View.GONE);
    }
}