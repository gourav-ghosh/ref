package com.devstringx.mytylesstockcheck.screens.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentDeliveryDetailsSubTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.DeliveryModesItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryDetailsSubTabFragment extends Fragment implements UploadFileAdapter.ViewFile, UploadFileAdapter.removeImage, ResponseListner, UploadFileAdapter.DeleteMediaItems {
    FragmentDeliveryDetailsSubTabBinding binding;
    public Records records;
    private Dialog imageUploadDialog;
    private RecyclerView dialogRecyclerView;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private List<String> selected_delete_media_list = new ArrayList<>();
    public List<String> selectedFileId = new ArrayList<>();
    private Dialog deleteMediaDialog;
    private ProgressBar progressBar;
    private String path;
    private PlayerView playerView;
    SimpleExoPlayer player;
    private UploadFileAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_details_sub_tab, container, false);
        View view = binding.getRoot();
        records = ((OrderDetailActivity) getActivity()).records;
        if (records.getDeliveryType().equalsIgnoreCase("Pick Up")){
            binding.proofOfDelivery.setText("Proof of Pick Up");
            binding.proofOfLoading.setText("Proof of Arrange");
        }else {
            binding.proofOfDelivery.setText("Proof of Delivery");
            binding.proofOfLoading.setText("Proof of Loading");
        }
        if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            binding.poRL.setVisibility(View.GONE);
            if (records.getStatus().equalsIgnoreCase("Loading") || records.getStatus().equalsIgnoreCase("Reaching_Store") ||
                    records.getStatus().equalsIgnoreCase("Ready_For_Pick_Up") || records.getStatus().equalsIgnoreCase("Dispatched") ||
                    records.getStatus().equalsIgnoreCase("Delivered") || records.getStatus().equalsIgnoreCase("Picked_Up")) {
                binding.proofOfLoadingRL.setVisibility(View.VISIBLE);
                binding.uploadLoadingProofIV.setVisibility(View.GONE);
                binding.loadingSelectTV.setVisibility(View.GONE);
            } else binding.proofOfLoadingRL.setVisibility(View.GONE);

            if (records.getStatus().equalsIgnoreCase("Delivered") || records.getStatus().equalsIgnoreCase("Picked_Up")) {
                binding.proofOfDeliveryRL.setVisibility(View.VISIBLE);
                binding.deliverySelectTV.setVisibility(View.GONE);
                binding.uploadDeliveryProofIV.setVisibility(View.GONE);
            } else binding.proofOfDeliveryRL.setVisibility(View.GONE);
        } else if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("10")) {
            if (records.getStatus().equalsIgnoreCase("Loading") || records.getStatus().equalsIgnoreCase("Reaching_Store") ||
                    records.getStatus().equalsIgnoreCase("Ready_For_Pick_Up") || records.getStatus().equalsIgnoreCase("Dispatched") ||
                    records.getStatus().equalsIgnoreCase("Delivered") || records.getStatus().equalsIgnoreCase("Picked_Up")) {
                binding.proofOfLoadingRL.setVisibility(View.VISIBLE);
                binding.uploadLoadingProofIV.setVisibility(View.GONE);
                binding.loadingSelectTV.setVisibility(View.GONE);
            } else binding.proofOfLoadingRL.setVisibility(View.GONE);

            if (records.getStatus().equalsIgnoreCase("Delivered") || records.getStatus().equalsIgnoreCase("Picked_Up")) {
                binding.proofOfDeliveryRL.setVisibility(View.VISIBLE);
                binding.deliverySelectTV.setVisibility(View.GONE);
                binding.uploadDeliveryProofIV.setVisibility(View.GONE);
            } else binding.proofOfDeliveryRL.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("11")) {
            binding.uploadDeliveryProofIV.setVisibility(View.GONE);
            binding.uploadLoadingProofIV.setVisibility(View.GONE);
            binding.loadingSelectTV.setVisibility(View.GONE);
            binding.deliverySelectTV.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("9")) {
            binding.loadingSelectTV.setVisibility(View.GONE);
            binding.deliverySelectTV.setVisibility(View.GONE);
            binding.accRL.setVisibility(View.GONE);
            binding.poRL.setVisibility(View.GONE);
        }
        setData();
        binding.deliveryDetailsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.arrowIV.isSelected()) {
                    binding.arrowIV.setImageResource(R.drawable.ic_dropdown);
                    binding.orderStatusLL.setVisibility(View.VISIBLE);
                    binding.arrowIV.setSelected(true);
                } else {
                    binding.arrowIV.setImageResource(R.drawable.ic_right_pointed_arrow);
                    binding.orderStatusLL.setVisibility(View.GONE);
                    binding.arrowIV.setSelected(false);
                }
            }
        });
        binding.uploadLoadingProofIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload Loading Proof", "Proof of Loading", "Dispatched");
            }
        });
        binding.uploadDeliveryProofIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload Delivery Proof", "Proof of Delivered", "Delivered");
            }
        });
        return view;
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
        TextView formatTV = imageUploadDialog.findViewById(R.id.formatTV);
        dialogRecyclerView = imageUploadDialog.findViewById(R.id.upload_img_rv);
        dialogTitle.setText(title);
        fieldHeader.setText(fieldTitle);
        formatTV.setText(R.string.upload_media_format);
        selectImg.setText(R.string.select_media_to_upload);
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
        map.put("id", getActivity().getIntent().getStringExtra("orderId").toString());
        map.put("for_status", mediaType);
        map.put("file", SELECTEDFILES);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPLOADMULTIPLEORDERFILE, map, true);
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
        mMimeTypesList.add("application/pdf");
        mMimeTypesList.add("video/mp4");
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

    private void setData() {
        if (records != null) {
            if (records.getDeliveryModes() != null && !records.getDeliveryModes().isEmpty()) {
                DeliveryModesItem item = records.getDeliveryModes().get(0);
                if (item.getDriverName()==null)binding.driverNameTV.setText("N/A");
                else binding.driverNameTV.setText(item.getDriverName());
                if (item.getVehicleNumber()==null)binding.vehicleNumberTV.setText("N/A");
                else binding.vehicleNumberTV.setText(item.getVehicleNumber());
                if (item.getDeliveryMode()==null)binding.modeTV.setText("N/A");
                else binding.modeTV.setText(item.getDeliveryMode());
                if (item.getComments()==null)binding.instructionTV.setText("N/A");
                else binding.instructionTV.setText(item.getComments());
                if(item.getLRNo()==null) binding.lrNoRL.setVisibility(View.GONE);
                else {
                    binding.lrNoRL.setVisibility(View.VISIBLE);
                    binding.lrNumberTV.setText(item.getLRNo().toString());
                }
                binding.driverCallIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.openDialerPad(getContext(), item.getDriverContact());
                    }
                });
                binding.driverWhatsappIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.openWhatsApp(getContext(), item.getDriverContact(), "hii");
                    }
                });
            } else {
                binding.driverNameTV.setText("N/A");
                binding.vehicleNumberTV.setText("N/A");
                binding.modeTV.setText("N/A");
                binding.instructionTV.setText("N/A");
                binding.driverCallIv.setVisibility(View.GONE);
                binding.driverWhatsappIv.setVisibility(View.GONE);
                binding.driverLinkIv.setVisibility(View.GONE);
            }
            if (records.getMediaProofs().getLoadingProof().isEmpty()) {
                binding.uploadLoadingFileRv.setVisibility(View.GONE);
                binding.noProofTV.setVisibility(View.VISIBLE);
                binding.loadingSelectTV.setVisibility(View.GONE);
                binding.complaintSeeMore.setVisibility(View.GONE);
            } else {
                if (records.getMediaProofs().getLoadingProof().size() > 3) {
                    binding.complaintSeeMore.setVisibility(View.VISIBLE);
                } else binding.complaintSeeMore.setVisibility(View.GONE);
                binding.uploadLoadingFileRv.setVisibility(View.VISIBLE);
                binding.noProofTV.setVisibility(View.GONE);
                UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), records.getMediaProofs().getLoadingProof(), "complaint", this);
                binding.uploadLoadingFileRv.setHasFixedSize(true);
                binding.uploadLoadingFileRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
                binding.uploadLoadingFileRv.setAdapter(imageAdapter);
                binding.loadingSelectTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDeleteFilesDialog(records, "complaint", "Proof of Loading");
                    }
                });
                binding.complaintSeeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.complaintSeeMore.getText().toString().equalsIgnoreCase("Show more")) {
                            ViewGroup.LayoutParams layoutParams = binding.uploadLoadingFileRv.getLayoutParams();
                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            binding.uploadLoadingFileRv.setLayoutParams(layoutParams);
                            imageAdapter.complaintShowMore = true;
                            binding.complaintSeeMore.setText("Show less");
                        } else {
                            float density = binding.uploadLoadingFileRv.getContext().getResources().getDisplayMetrics().density;
                            int heightInPixels = (int) (120 * density + 0.5f);
                            ViewGroup.LayoutParams layoutParams = binding.uploadLoadingFileRv.getLayoutParams();
                            layoutParams.height = heightInPixels;
                            binding.uploadLoadingFileRv.setLayoutParams(layoutParams);
                            imageAdapter.complaintShowMore = false;
                            binding.complaintSeeMore.setText("Show more");
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                });
            }
            if (records.getMediaProofs().getDeliveryProof().isEmpty()) {
                binding.noProofOfDeliveryTV.setVisibility(View.VISIBLE);
                binding.uploadDeliveryProofRv.setVisibility(View.GONE);
                binding.deliverySelectTV.setVisibility(View.GONE);
                binding.resolveSeeMore.setVisibility(View.GONE);
            } else {
                if (records.getMediaProofs().getDeliveryProof().size() > 3) {
                    binding.resolveSeeMore.setVisibility(View.VISIBLE);
                } else binding.resolveSeeMore.setVisibility(View.GONE);
                binding.uploadDeliveryProofRv.setVisibility(View.VISIBLE);
                binding.noProofOfDeliveryTV.setVisibility(View.GONE);
                binding.deliverySelectTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDeleteFilesDialog(records, "response", "Proof of Delivery");
                    }
                });
                UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), "response", records.getMediaProofs().getDeliveryProof(), this);
                binding.uploadDeliveryProofRv.setHasFixedSize(true);
                binding.uploadDeliveryProofRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
                binding.uploadDeliveryProofRv.setAdapter(imageAdapter);
                binding.resolveSeeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.resolveSeeMore.getText().toString().equalsIgnoreCase("Show more")) {
                            ViewGroup.LayoutParams layoutParams = binding.uploadDeliveryProofRv.getLayoutParams();
                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            binding.uploadDeliveryProofRv.setLayoutParams(layoutParams);
                            imageAdapter.resolveShowMore = true;
                            binding.resolveSeeMore.setText("Show less");
                        } else {
                            float density = binding.uploadDeliveryProofRv.getContext().getResources().getDisplayMetrics().density;
                            int heightInPixels = (int) (120 * density + 0.5f);
                            ViewGroup.LayoutParams layoutParams = binding.uploadDeliveryProofRv.getLayoutParams();
                            layoutParams.height = heightInPixels;
                            binding.uploadDeliveryProofRv.setLayoutParams(layoutParams);
                            imageAdapter.resolveShowMore = false;
                            binding.resolveSeeMore.setText("Show more");
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                });

            }
            switch (records.getStatus().toString()) {
                case "To_Be_Approved_From_Account":

                    break;
                case "Pending_At_Dispatch_Manager":
                    binding.accVerifiedLL.setSelected(true);
                    break;
                case "Po_Sent":
                    binding.accVerifiedLL.setSelected(true);
                    binding.poReceivedIV.setSelected(true);
                    break;
                case "Order_Processed":
                    binding.accVerifiedLL.setSelected(true);
                    binding.agentAssignedIV.setSelected(true);
                    binding.poReceivedIV.setSelected(true);
                    break;
                case "Loading":
                case "Reaching_Store":
                    binding.accVerifiedLL.setSelected(true);
                    binding.agentAssignedIV.setSelected(true);
                    binding.poReceivedIV.setSelected(true);
                    binding.loadingIV.setSelected(true);
                    break;
                case "Ready_For_Pick_Up":
                case "Dispatched":
                    binding.accVerifiedLL.setSelected(true);
                    binding.agentAssignedIV.setSelected(true);
                    binding.poReceivedIV.setSelected(true);
                    binding.loadingIV.setSelected(true);
                    binding.dispatchIV.setSelected(true);
                    break;
                case "Delivered":
                case "Picked_Up":
                    binding.accVerifiedLL.setSelected(true);
                    binding.agentAssignedIV.setSelected(true);
                    binding.poReceivedIV.setSelected(true);
                    binding.loadingIV.setSelected(true);
                    binding.dispatchIV.setSelected(true);
                    binding.deliveredIV.setSelected(true);
                    break;
            }

            if (records.getActivityLogs().getAccountVerified() != null) {
                binding.tvTitle.setText("Acc. Verified");
                binding.verifiedByTV.setText("Verified by : " + records.getActivityLogs().getAccountVerified().getActivityBy());
                binding.dateTimeTV.setText(records.getActivityLogs().getAccountVerified().getUpdatedAt());
            }
            if (records.getActivityLogs().getPoReceived() != null || records.getStatus().equalsIgnoreCase("Po_Sent")) {
                binding.poTitle.setText("PO Receieved");
            }
            if (records.getActivityLogs().getOrderProcessed() != null) {
                binding.agentTitleTV.setText("Agent Assigned");
                binding.agentDetailsTV.setText("Assigned by : " + records.getActivityLogs().getOrderProcessed().getActivityBy());
                binding.agentDateTimeTV.setText(records.getActivityLogs().getOrderProcessed().getUpdatedAt());
            }
            if (records.getDeliveryType().equalsIgnoreCase("Pick Up")) {
                if (records.getActivityLogs().getMaterialArranged() != null) {
                    binding.loadingTitleTV.setText("Material Arranged");
                    binding.loadByTV.setText("Material Arranged by : " + records.getActivityLogs().getMaterialArranged().getActivityBy());
                    binding.loadDateTV.setText(records.getActivityLogs().getMaterialArranged().getUpdatedAt());
                } else {
                    binding.loadingTitleTV.setText("Material Not Arranged");
                }
                if (records.getActivityLogs().getReadyForPickup() != null) {
                    binding.dispatchTitleTV.setText("Ready for Pick Up");
                    binding.dispatchByTV.setText("Ready for Pick-Up by : " + records.getActivityLogs().getReadyForPickup().getActivityBy());
                    binding.dispatchTimeTV.setText(records.getActivityLogs().getReadyForPickup().getUpdatedAt());
                } else {
                    binding.dispatchTitleTV.setText("Not Ready for Picked Up");
                }
                if (records.getActivityLogs().getPickedUp() != null) {
                    binding.deliveredTitleTV.setText("Picked Up");
                    binding.deliveredByTV.setText("Picked Up by : " + records.getActivityLogs().getPickedUp().getActivityBy());
                    binding.deliveredDateTV.setText(records.getActivityLogs().getPickedUp().getUpdatedAt());
                } else {
                    binding.deliveredTitleTV.setText("Not Picked Up");
                }
            } else {
                if (records.getActivityLogs().getLoading() != null) {
                    binding.loadingTitleTV.setText("Loading");
                    binding.loadByTV.setText("Load by : " + records.getActivityLogs().getLoading().getActivityBy());
                    binding.loadDateTV.setText(records.getActivityLogs().getLoading().getUpdatedAt());
                } else {
                    binding.loadingTitleTV.setText("Not Loading");
                }
                if (records.getActivityLogs().getDispatched() != null) {
                    binding.dispatchTitleTV.setText("Dispatched");
                    binding.dispatchByTV.setText("Dispatched by : " + records.getActivityLogs().getDispatched().getActivityBy());
                    binding.dispatchTimeTV.setText(records.getActivityLogs().getDispatched().getUpdatedAt());
                } else {
                    binding.dispatchTitleTV.setText("Not Dispatched");
                }
                if (records.getActivityLogs().getDelivered() != null) {
                    binding.deliveredTitleTV.setText("Delivered");
                    binding.deliveredByTV.setText("Delivered by : " + records.getActivityLogs().getDelivered().getActivityBy());
                    binding.deliveredDateTV.setText(records.getActivityLogs().getDelivered().getUpdatedAt());
                } else {
                    binding.deliveredTitleTV.setText("Not Delivered");
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
        progressBar = dialog.findViewById(R.id.progress_bar);
        WebView webView = dialog.findViewById(R.id.dialog_web_view);
        playerView = dialog.findViewById(R.id.player_view);
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
        complaintFileInFullView(list,webView, fullSizeViewIV,playerView, dialog.getContext(), count);
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
                complaintFileInFullView(list,webView, fullSizeViewIV,playerView, dialog.getContext(), count);
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
                complaintFileInFullView(list,webView, fullSizeViewIV,playerView, dialog.getContext(), count);
            }
        });
        dialog.show();
    }

    private void complaintFileInFullView(List<ComplaintMediasItem> list, WebView web, ZoomageView fullSizeViewIV, PlayerView playerView, Context context, TextView count){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count.setText(i+1+"/"+list.size());
                if (list.get(i).getMediaType().equalsIgnoreCase("document")) {
                    if (player!=null) player.stop();
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
                }if (list.get(i).getMediaType().equalsIgnoreCase("video")) {
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

    private void openDeleteFilesDialog(Records records, String type, String title) {
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
            imageAdapter = new UploadFileAdapter(getContext(), records.getMediaProofs().getLoadingProof(), type, "delete", this);
        else
            imageAdapter = new UploadFileAdapter(getContext(), type, records.getMediaProofs().getDeliveryProof(), "delete", this);
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
        if (selected_delete_media_list!=null && !selected_delete_media_list.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("fileId", selected_delete_media_list);
            map.put("fileType", "media");
            map.put(NKeys.DELETEORDERFILE, new Gson().toJson(map));
            new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_DELETEORDERFILE, map, true);
        }
        if (selectedFileId!=null && !selectedFileId.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("fileId", selectedFileId);
            map.put("fileType", "document");
            map.put(NKeys.DELETEORDERFILE, new Gson().toJson(map));
            new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_DELETEORDERFILE, map, true);
        }
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADMULTIPLEORDERFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        imageUploadDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((OrderDetailActivity) getActivity()).getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEORDERFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        deleteMediaDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((OrderDetailActivity) getActivity()).getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void deleteMediaItemsListener(List<String> selectedMediaId, List<String> selectedFileId) {
        selected_delete_media_list = selectedMediaId;
        this.selectedFileId = selectedFileId;
    }
}