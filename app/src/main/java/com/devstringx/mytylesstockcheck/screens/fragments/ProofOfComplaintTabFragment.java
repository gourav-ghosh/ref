package com.devstringx.mytylesstockcheck.screens.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentProofOfComplaintTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.Data;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.ComplaintDetailsActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.jsibbold.zoomage.ZoomageView;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProofOfComplaintTabFragment extends Fragment implements UploadFileAdapter.removeImage, ResponseListner, UploadFileAdapter.ViewFile,UploadFileAdapter.DeleteMediaItems {
    FragmentProofOfComplaintTabBinding binding;
    private UploadFileAdapter imageAdapter;
    private RecyclerView dialogRecyclerView;
    private Data complaintDetailResponse;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private List<String> selected_delete_media_list = new ArrayList<>();
    private Dialog imageUploadDialog;
    private Dialog deleteMediaDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_proof_of_complaint_tab, container, false);
        View view = binding.getRoot();
        binding.uploadComplaintIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload Complaint Proof", "Complaint Proof", "complaint");
            }
        });
        binding.uploadResolutionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload Resolution Proof", "Resolution Proof", "complaint_response");
            }
        });
        complaintDetailResponse = ((ComplaintDetailsActivity) getActivity()).complaintDetailResponse;
        if (complaintDetailResponse.getComplaintMedias().isEmpty()) binding.complaintSelectTV.setVisibility(View.GONE);
        if (complaintDetailResponse.getResponseMedias().isEmpty()) binding.responseSelectTV.setVisibility(View.GONE);
        if (complaintDetailResponse.getComplaintStatus().equalsIgnoreCase("Created"))
            binding.resLL.setVisibility(View.GONE);
        else binding.resLL.setVisibility(View.VISIBLE);
        if (complaintDetailResponse.getComplaintMedias().size() > 3) {
            binding.complaintSeeMore.setVisibility(View.VISIBLE);
        } else binding.complaintSeeMore.setVisibility(View.GONE);
        if (complaintDetailResponse.getResponseMedias().size() > 3) {
            binding.resolveSeeMore.setVisibility(View.VISIBLE);
        } else binding.resolveSeeMore.setVisibility(View.GONE);
        setData(complaintDetailResponse);
        return view;
    }

    private void setData(Data data) {
        if (!data.getComplaintMedias().isEmpty()) {
            UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), data.getComplaintMedias(), "complaint", this);
            binding.uploadComplaintFileRv.setHasFixedSize(true);
            binding.uploadComplaintFileRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.noComplaintProofTV.setVisibility(View.GONE);
            binding.uploadComplaintFileRv.setVisibility(View.VISIBLE);
            binding.uploadComplaintFileRv.setAdapter(imageAdapter);
            binding.complaintSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.complaintSeeMore.getText().toString().equalsIgnoreCase("Show more")) {
                        ViewGroup.LayoutParams layoutParams = binding.uploadComplaintFileRv.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        binding.uploadComplaintFileRv.setLayoutParams(layoutParams);
                        imageAdapter.complaintShowMore = true;
                        binding.complaintSeeMore.setText("Show less");
                    } else {
                        float density = binding.uploadComplaintFileRv.getContext().getResources().getDisplayMetrics().density;
                        int heightInPixels = (int) (120 * density + 0.5f);
                        ViewGroup.LayoutParams layoutParams = binding.uploadComplaintFileRv.getLayoutParams();
                        layoutParams.height = heightInPixels;
                        binding.uploadComplaintFileRv.setLayoutParams(layoutParams);
                        imageAdapter.complaintShowMore = false;
                        binding.complaintSeeMore.setText("Show more");
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            });
            binding.complaintSelectTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDeleteFilesDialog(complaintDetailResponse, "complaint", "Proof of Complaint");
                }
            });
        } else {
            binding.complaintSeeMore.setVisibility(View.GONE);
            binding.noComplaintProofTV.setVisibility(View.VISIBLE);
            binding.uploadComplaintFileRv.setVisibility(View.GONE);
        }
        if (!data.getResponseMedias().isEmpty()) {
            UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), "response", data.getResponseMedias(), this);
            binding.uploadResolutionFileRv.setHasFixedSize(true);
            binding.uploadResolutionFileRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.noResolutionProofTV.setVisibility(View.GONE);
            binding.uploadResolutionFileRv.setVisibility(View.VISIBLE);
            binding.uploadResolutionFileRv.setAdapter(imageAdapter);
            binding.resolveSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.resolveSeeMore.getText().toString().equalsIgnoreCase("Show more")) {
                        ViewGroup.LayoutParams layoutParams = binding.uploadResolutionFileRv.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        binding.uploadResolutionFileRv.setLayoutParams(layoutParams);
                        imageAdapter.resolveShowMore = true;
                        binding.resolveSeeMore.setText("Show less");
                    } else {
                        float density = binding.uploadResolutionFileRv.getContext().getResources().getDisplayMetrics().density;
                        int heightInPixels = (int) (120 * density + 0.5f);
                        ViewGroup.LayoutParams layoutParams = binding.uploadResolutionFileRv.getLayoutParams();
                        layoutParams.height = heightInPixels;
                        binding.uploadResolutionFileRv.setLayoutParams(layoutParams);
                        imageAdapter.resolveShowMore = false;
                        binding.resolveSeeMore.setText("Show more");
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            });
            binding.responseSelectTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDeleteFilesDialog(complaintDetailResponse, "response", "Proof of Resolution");
                }
            });
        } else {
            binding.complaintSeeMore.setVisibility(View.GONE);
            binding.noResolutionProofTV.setVisibility(View.VISIBLE);
            binding.uploadResolutionFileRv.setVisibility(View.GONE);
        }
    }

    private void openDeleteFilesDialog(Data data, String type, String title) {
        deleteMediaDialog = new Dialog(getContext());
        deleteMediaDialog.setContentView(R.layout.delete_media_dialog);
        deleteMediaDialog.setCancelable(true);
        RecyclerView mediaRV = deleteMediaDialog.findViewById(R.id.mediaRV);
        TextView titleTV = deleteMediaDialog.findViewById(R.id.title_TV);
        TextView cancel = deleteMediaDialog.findViewById(R.id.dialog_cancel_btn);
        TextView delete = deleteMediaDialog.findViewById(R.id.dialog_delete_btn);
        titleTV.setText(title);
        UploadFileAdapter imageAdapter;
        if (type.equalsIgnoreCase("complaint"))
            imageAdapter = new UploadFileAdapter(getContext(), data.getComplaintMedias(), type, "delete", this);
        else
            imageAdapter = new UploadFileAdapter(getContext(), type, data.getResponseMedias(), "delete", this);
        mediaRV.setHasFixedSize(true);
        mediaRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
        String data = "{" +
                " \"medias_id\":"+selected_delete_media_list +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DELETECOMPLAINTMEDIA, data);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_DELETECOMPLAINTMEDIA, map, true);
    }

    private void showImageUploadDialog(String title, String fieldTitle, String mediaType) {
        imageUploadDialog = new Dialog(getContext());
        imageUploadDialog.setContentView(R.layout.upload_image_dialog);
        imageUploadDialog.setCancelable(true);
        TextView dialogTitle = imageUploadDialog.findViewById(R.id.dialog_title_tv);
        ImageView close = imageUploadDialog.findViewById(R.id.image_dialog_crossIV);
        TextView fieldHeader = imageUploadDialog.findViewById(R.id.field_titleTV);
        TextView uploadBtn = imageUploadDialog.findViewById(R.id.dialog_upload_btn);
        TextView selectImg = imageUploadDialog.findViewById(R.id.select_img);
        dialogRecyclerView = imageUploadDialog.findViewById(R.id.upload_img_rv);
        dialogTitle.setText(title);
        fieldHeader.setText(fieldTitle);
        setupAdapter();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUploadDialog.dismiss();
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImageLauncher();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUploadImageApi(mediaType);
            }
        });

        imageUploadDialog.show();
    }

    private void callUploadImageApi(String mediaType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", getActivity().getIntent().getStringExtra("id").toString());
        map.put("media_type", mediaType);
        map.put("file", SELECTEDFILES);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPLOADCOMPLAINTMEDIA, map, true);
    }

    private void setupAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }

    private void SelectImageLauncher() {
        ArrayList<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("image/*");
        captureImageResultLauncher.launch(
                new FilePicker.Builder(getContext())
                        .pickDocumentFileBuild(
                                new DocumentFilePickerConfig(
                                        null,
                                        null,
                                        true,
                                        10,
                                        mMimeTypesList,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                        )
        );

    }

    private final ActivityResultLauncher<Intent> captureImageResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            try {
                                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData().getData() != null) {
                                        String listData = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                        File testFile = new File(listData);
//                                        float size = testFile.length() / (1024 * 1024);
//                                        if (size <= 2) {
                                        SELECTEDFILES.add(listData);
                                        ArrayList<String> imgs = new ArrayList<>();
                                        imgs.add(listData);
                                        Images images = new Images();
                                        images.setLeadAttachment(imgs);
                                        imagesArrayList.add(images);
                                        imageAdapter.refreshList(imagesArrayList);
                                        Common.showLog("qwert==" + imagesArrayList);
                                        if (!imagesArrayList.isEmpty())
                                            dialogRecyclerView.setVisibility(View.VISIBLE);
                                        else dialogRecyclerView.setVisibility(View.GONE);
//                                        } else {
//                                            Common.showToast(getContext(), "Please upload a file smaller than 2 MB.");
//                                        }
                                    } else {
//                                        ArrayList<Uri> listData = getClipDataUris(result.getData());
//                                        // ArrayList<String> listData = result.getData().getStringArrayListExtra(Const.BundleExtras.FILE_PATH_LIST);
//                                        uriList.addAll(listData);
                                    }
                                }
                            } catch (Exception e) {
                                Common.showLog(e.getMessage());
                            }

                        }
                    });

    private void complaintFileViewDialog(List<ComplaintMediasItem> list) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.full_size_image_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_IV);
        ZoomageView fullSizeViewIV = dialog.findViewById(R.id.image_viewer);
        ImageView next = dialog.findViewById(R.id.nextIV);
        ImageView prev = dialog.findViewById(R.id.prevIV);
        TextView count = dialog.findViewById(R.id.countTV);
        if (list.size() < 2) {
            next.setVisibility(View.GONE);
            prev.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        complaintFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if (i + 1 < list.size())
                            list.get(i + 1).setSelected(true);
                        else list.get(0).setSelected(true);
                        break;
                    }
                }
                complaintFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if (i - 1 != -1)
                            list.get(i - 1).setSelected(true);
                        else list.get(list.size() - 1).setSelected(true);
                        break;
                    }
                }
                complaintFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
            }
        });
        dialog.show();
    }

    private void complaintFileInFullView(List<ComplaintMediasItem> list, ZoomageView fullSizeViewIV, Context context, TextView count) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count.setText(i + 1 + "/" + list.size());
                fullSizeViewIV.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(list.get(i).getPath())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(fullSizeViewIV);

            }
        }
    }

    private void resolveFileViewDialog(List<ResponseMediasItem> list) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.full_size_image_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_IV);
        ZoomageView fullSizeViewIV = dialog.findViewById(R.id.image_viewer);
        ImageView next = dialog.findViewById(R.id.nextIV);
        ImageView prev = dialog.findViewById(R.id.prevIV);
        TextView count = dialog.findViewById(R.id.countTV);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        resolveFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if (i + 1 < list.size())
                            list.get(i + 1).setSelected(true);
                        else list.get(0).setSelected(true);
                        break;
                    }
                }
                resolveFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        list.get(i).setSelected(false);
                        if (i - 1 != -1)
                            list.get(i - 1).setSelected(true);
                        else list.get(list.size() - 1).setSelected(true);
                        break;
                    }
                }
                resolveFileInFullView(list, fullSizeViewIV, dialog.getContext(), count);
            }
        });
        dialog.show();
    }

    @SuppressLint("SuspiciousIndentation")
    private void resolveFileInFullView(List<ResponseMediasItem> list, ZoomageView fullSizeViewIV, Context context, TextView count) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count.setText(i + 1 + "/" + list.size());
                fullSizeViewIV.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(list.get(i).getPath())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(fullSizeViewIV);
            }
        }
    }

    @Override
    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
        if (imagesArrayList.isEmpty()) dialogRecyclerView.setVisibility(View.GONE);
        else dialogRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADCOMPLAINTMEDIA) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        imageUploadDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((ComplaintDetailsActivity) getActivity()).getComplaintDetails();
                        setData(complaintDetailResponse);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETECOMPLAINTMEDIA) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        deleteMediaDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((ComplaintDetailsActivity) getActivity()).getComplaintDetails();
                        setData(complaintDetailResponse);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void viewFileListener(List<ComplaintMediasItem> complaintMediasItemList, List<ResponseMediasItem> responseMediasItemList, String type) {
        if (type.contains("complaint"))
            complaintFileViewDialog(complaintMediasItemList);
        else
            resolveFileViewDialog(responseMediasItemList);
    }

    @Override
    public void deleteMediaItemsListener(List<String> selectedMediaId, List<String> selectedFileId) {
        selected_delete_media_list = selectedMediaId;
    }
}