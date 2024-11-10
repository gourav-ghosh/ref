package com.devstringx.mytylesstockcheck.screens.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.devstringx.mytylesstockcheck.adapter.PaymentHistoryAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentPaymentDetailSubTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Data;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.PaymentInfoItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
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

public class PaymentDetailSubTabFragment extends Fragment implements UploadFileAdapter.ViewFile, UploadFileAdapter.removeImage, ResponseListner , UploadFileAdapter.DeleteMediaItems {
    FragmentPaymentDetailSubTabBinding binding;
    private Dialog imageUploadDialog;
    private UploadFileAdapter imageAdapter;
    private PaymentHistoryAdapter paymentHistoryAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private List<String> selected_delete_media_list = new ArrayList<>();
    private Dialog deleteMediaDialog;
    private RecyclerView dialogRecyclerView;
    private RecyclerView paymentHistoryRecyclerView;
    public Records records;
    public Data data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_payment_detail_sub_tab, container, false);
        View view=binding.getRoot();
        records = ((OrderDetailActivity) getActivity()).records;
        data = ((OrderDetailActivity) getActivity()).data;
        if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("6")){
            binding.paymentResponseBtnRL.setVisibility(View.GONE);
            binding.responseSelectTV.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("10")) {
            if (records.getStatus().equalsIgnoreCase("To_Be_Approved_From_Account")) {
                binding.paymentResponseBtnRL.setVisibility(View.VISIBLE);
            }
            else binding.paymentResponseBtnRL.setVisibility(View.GONE);
            binding.uploadPaymentProofIV.setVisibility(View.GONE);
            binding.responseSelectTV.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("11")) {
            binding.uploadPaymentProofIV.setVisibility(View.GONE);
            binding.responseSelectTV.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("9")) {
            binding.uploadPaymentProofIV.setVisibility(View.GONE);
            binding.responseSelectTV.setVisibility(View.GONE);
            binding.paymentResponseBtnRL.setVisibility(View.GONE);
        }else if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("9")) {
            binding.uploadPaymentProofIV.setVisibility(View.GONE);
            binding.responseSelectTV.setVisibility(View.GONE);
            binding.paymentResponseBtnRL.setVisibility(View.GONE);
        }
        if (data!=null) {
            if (data.getTotalAmount() != null)
                binding.totalTV.setText("₹ "+data.getTotalAmount().toString());
            if (data.getAmountPaid() != null)
                binding.paidTV.setText("₹ "+data.getAmountPaid().toString());
            if (data.getDueAmount() != null)
                binding.dueAmountTV.setText("₹ "+data.getDueAmount().toString());
            if (records.getActivityLogs().getAccountVerified() == null) {
                binding.approveTV.setVisibility(View.VISIBLE);
                binding.rejectTV.setVisibility(View.VISIBLE);
            } else {
                binding.approveTV.setVisibility(View.GONE);
                binding.rejectTV.setVisibility(View.GONE);
            }
        }
        if (records.getMediaProofs().getPaymentProof().isEmpty()) binding.responseSelectTV.setVisibility(View.GONE);
        binding.approveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OrderDetailActivity)getActivity()).showPaymentVerificationDialog();
            }
        });
//        binding.paymentInstructionTV.setText(records.getPaymentInfo().get(0).);
        binding.rejectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OrderDetailActivity)getActivity()).showCustomDialog("Confirm","Are you sure you want to reject this payment order","Payment Reject");
            }
        });
        if (records.getMediaProofs().getPaymentProof().isEmpty()){
            binding.noProofTV.setVisibility(View.VISIBLE);
            binding.uploadFileRv.setVisibility(View.GONE);
        }else {
            binding.noProofTV.setVisibility(View.GONE);
            binding.uploadFileRv.setVisibility(View.VISIBLE);

            UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), "response", records.getMediaProofs().getPaymentProof(), this);
            binding.uploadFileRv.setHasFixedSize(true);
            binding.uploadFileRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
        binding.responseSelectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteFilesDialog(records, "response", "Payment Proofs");
            }
        });
        binding.uploadPaymentProofIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload Payment Proof", "Payment Proof", "Payment_Received");
            }
        });
        if (records.getMediaProofs().getPaymentProof().size() > 3) {
            binding.resolveSeeMore.setVisibility(View.VISIBLE);
        } else binding.resolveSeeMore.setVisibility(View.GONE);
        if(records.getPaymentInfo().size() > 1) {
            binding.transactionInfoLL.setVisibility(View.VISIBLE);
            paymentHistoryRecyclerView = binding.paymentInfoRV;
            setupPaymentInfoAdapter();
        } else binding.transactionInfoLL.setVisibility(View.GONE);
        return view;
    }
    private void openDeleteFilesDialog(Records data, String type, String title) {
        deleteMediaDialog = new Dialog(getContext());
        deleteMediaDialog.setContentView(R.layout.delete_media_dialog);
        deleteMediaDialog.setCancelable(true);
        RecyclerView mediaRV = deleteMediaDialog.findViewById(R.id.mediaRV);
        TextView titleTV = deleteMediaDialog.findViewById(R.id.title_TV);
        TextView cancel = deleteMediaDialog.findViewById(R.id.dialog_cancel_btn);
        TextView delete = deleteMediaDialog.findViewById(R.id.dialog_delete_btn);
        titleTV.setText(title);
        UploadFileAdapter imageAdapter = new UploadFileAdapter(getContext(), type, data.getMediaProofs().getPaymentProof(), "delete", this);
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
        HashMap<String, Object> map = new HashMap<>();
        map.put("fileId", selected_delete_media_list);
        map.put("fileType", "media");
        map.put(NKeys.DELETEORDERFILE , new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_DELETEORDERFILE, map, true);
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
        map.put("id", getActivity().getIntent().getStringExtra("orderId").toString());
        map.put("for_status", mediaType);
        map.put("file", SELECTEDFILES);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPLOADMULTIPLEORDERFILE, map, true);
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

    private void setupAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }
    private void setupPaymentInfoAdapter() {
        List<PaymentInfoItem> newPaymentInfoList = new ArrayList<>(records.getPaymentInfo().subList(0, records.getPaymentInfo().size()-1));
        paymentHistoryAdapter = new PaymentHistoryAdapter(getContext(), newPaymentInfoList);
        paymentHistoryRecyclerView.setHasFixedSize(true);
        paymentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentHistoryRecyclerView.setAdapter(paymentHistoryAdapter);
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
        dialog.setCancelable(true);
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
    public void viewFileListener(List<ComplaintMediasItem> complaintMediasItemList, List<ResponseMediasItem> responseMediasItemList, String type) {
        resolveFileViewDialog(responseMediasItemList);
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
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEORDERFILE) {
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
        for (int i = 0; i < selectedMediaId.size(); i++) {
            Common.showLog(i+"selected"+selectedMediaId.get(i));
        }
        selected_delete_media_list = selectedMediaId;
    }
}