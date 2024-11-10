package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.HelpingAgentAdapter;
import com.devstringx.mytylesstockcheck.adapter.PdfListingAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentPurchaseDetailsSubTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.MainAgent;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
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

public class PurchaseDetailsSubTabFragment extends Fragment implements UploadFileAdapter.removeImage, PdfListingAdapter.OnClick , HelpingAgentAdapter.FileData {
    FragmentPurchaseDetailsSubTabBinding binding;
    PdfListingAdapter adapter;
    HelpingAgentAdapter helpingAgentAdapter;
    private Records records;
    private ProgressBar progressBar;
    Dialog imageUploadDialog;
    private RecyclerView dialogRecyclerView;
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_purchase_details_sub_tab, container, false);
        View view = binding.getRoot();
        records = ((OrderDetailActivity) getActivity()).records;

        if (records.getOrderTasks().getMainAgent().isEmpty()){
            binding.noProofTV.setVisibility(View.VISIBLE);
            binding.detailsLL.setVisibility(View.GONE);
        }else {
            binding.detailsLL.setVisibility(View.VISIBLE);
            binding.noProofTV.setVisibility(View.GONE);
            setupAdapter();
            setData();
        }
        if (!records.getOrderTasks().getHelpingAgent().isEmpty()){
            setupHelpingAgentAdapter();
        }
        return view;
    }

    private void setupHelpingAgentAdapter() {
        helpingAgentAdapter = new HelpingAgentAdapter(getContext(),records.getOrderTasks(),this);
        binding.helpingAgentRV.setHasFixedSize(true);
        binding.helpingAgentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.helpingAgentRV.setAdapter(helpingAgentAdapter);
    }

    private void setData() {
        binding.saleExeTV.setText(records.getOrderTasks().getMainAgent().get(0).getAssignedTo().toString());
        binding.instructionTV.setText(records.getOrderTasks().getMainAgent().get(0).getComment());
        binding.saleCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getContext(),records.getOrderTasks().getMainAgent().get(0).getAssignedPhone());
            }
        });
        binding.saleWhatsappIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(getContext(),records.getOrderTasks().getMainAgent().get(0).getAssignedPhone());
            }
        });
    }

    private void setupAdapter() {
        adapter = new PdfListingAdapter(getContext(),"delivery_details_tab",records.getOrderTasks(),this);
        binding.pdfRV.setHasFixedSize(true);
        binding.pdfRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.pdfRV.setAdapter(adapter);
    }

    @Override
    public void onClickListener(String path) {
        fileViewDialog(path);
    }

    private void fileViewDialog(String path) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.full_size_image_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.horizontalMargin = 20;
            layoutParams.verticalMargin = 20;
            window.setAttributes(layoutParams);
        }
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_IV);
        ZoomageView fullSizeViewIV = dialog.findViewById(R.id.image_viewer);
        WebView web = dialog.findViewById(R.id.dialog_web_view);
        ImageView next = dialog.findViewById(R.id.nextIV);
        ImageView prev = dialog.findViewById(R.id.prevIV);
        TextView count = dialog.findViewById(R.id.countTV);
        progressBar = dialog.findViewById(R.id.progress_bar);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fullSizeViewIV.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        count.setVisibility(View.GONE);
        web.setVisibility(View.VISIBLE);
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
        Common.showLog("pdfUrl"+path);
        String googleDocsUrl = null;
        try {
            googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(path, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        web.loadUrl(googleDocsUrl);
        dialog.show();
    }

    @Override
    public void onDownloadListener(String link) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }
    @Override
    public void onStatusClickListener(int id, String name) {

    }
    @Override
    public void onMarkArrangeClickListener(int id, String name) {
        showImageUploadDialog("Mark PO Arranged", name, id);
    }

    private void showImageUploadDialog(String title, String name, int id) {
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
        fieldHeader.setText(name);
        formatTV.setText(R.string.upload_media_format);
        selectImg.setText(R.string.select_media_to_upload);
        setupUploadedImageAdapter();
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
                callUploadImageApi(id);
            }
        });
        imageUploadDialog.show();
    }

    private void callUploadImageApi(int id) {
//        TODO add optional conditions for Admin and Dispt. Manager
        if(SELECTEDFILES.size()>0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("task_id", id);
            map.put("file", SELECTEDFILES);
            new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_POARRANGE, map, true);
        } else {
            Common.showToast(getContext(), "Select atleast 1 proof to mark PO as arranged");
        }

    }

    private void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_POARRANGE) {
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
            }
        }
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

    private void setupUploadedImageAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void sendFileData(List<MainAgent> files, RecyclerView rv) {
        PdfListingAdapter adapter = new PdfListingAdapter(getContext(), "delivery_details_tab", files,this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
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
}