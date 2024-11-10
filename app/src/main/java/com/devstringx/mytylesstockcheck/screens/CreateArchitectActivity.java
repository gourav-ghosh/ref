package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.ImagePDFCardSetup;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityCreateArchitectBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.MediasItem;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.Records;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.Response;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.textfield.TextInputLayout;
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

public class CreateArchitectActivity extends AppCompatActivity implements UploadFileAdapter.removeImage, ResponseListner, ImagePDFCardSetup.ViewFile {
    ActivityCreateArchitectBinding binding;
    private UploadFileAdapter imageAdapter;
    private ImagePDFCardSetup fileAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private List<String> deleteArchitectMediaList = new ArrayList<>();
    private boolean editable = false;
    private Records item;
    private List<RecordsItem> allOwnersList;
    private AutoCompleteTextView salesExecutive;
    private TextView dialogLintTV;
    private LinearLayout linkViewLL;
    private TextView dialogMsg;
    private CustomAutoCompleteListAdapter cityAdapter;
    private Dialog dialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_architect);
        setupAdapter(imagesArrayList);
        binding.titleTv.setText(getIntent().getStringExtra("title"));
        binding.editArchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccessibiltyToggle();
                binding.editArchIv.setVisibility(View.GONE);
                binding.deleteArchIv.setVisibility(View.GONE);
            }
        });
        if (getIntent().getStringExtra("type").equalsIgnoreCase("create")) {
            binding.profileDetailsRL.setVisibility(View.GONE);
            binding.architectOrdersRL.setVisibility(View.GONE);
            binding.personalDetailsRL.setVisibility(View.VISIBLE);
            binding.save.setText("Register");
            binding.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callApi(getIntent().getStringExtra("id"));
                }
            });
        } else {
            binding.profileDetailsRL.setVisibility(View.VISIBLE);
            binding.architectOrdersRL.setVisibility(View.VISIBLE);
            binding.personalDetailsRL.setVisibility(View.GONE);
            binding.save.setText("Update");
            getArchitectDetail(getIntent().getStringExtra("id"));
            if(Common.getPermission(this,"AORI","AEA")) {
                binding.editArchIv.setVisibility(View.VISIBLE);
            } else binding.editArchIv.setVisibility(View.GONE);
            if(Common.getPermission(this,"AORI","DEA")) {
                binding.deleteArchIv.setVisibility(View.VISIBLE);
            } else binding.deleteArchIv.setVisibility(View.GONE);
            binding.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callApi(getIntent().getStringExtra("id"));
                }
            });
        }
        binding.showOrdersByArchitect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateArchitectActivity.this, ArchitectOrdersActivity.class);
                intent.putExtra("architectId", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
        binding.shareArchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
        binding.deleteArchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog("Confirm", "want to delete this architect  permanently?", "Confirm Delete", getIntent().getStringExtra("id"));
            }
        });
        binding.selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("application/pdf");
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(CreateArchitectActivity.this)
                                .pickDocumentFileBuild(
                                        new DocumentFilePickerConfig(
                                                null,
                                                null,
                                                false,
                                                1,
                                                mMimeTypesList,
                                                null,
                                                null,
                                                null,
                                                null
                                        )
                                )
                );
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fileViewDialog(List<MediasItem> list) {
        Dialog dialog = new Dialog(this);
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
        showFileInFullView(list,web,fullSizeViewIV,dialog.getContext(),count);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i <list.size() ; i++) {
                    if (list.get(i).isSelected()){
                        list.get(i).setSelected(false);
                        if (i+1<list.size())
                        list.get(i+1).setSelected(true);
                        else list.get(0).setSelected(true);
                        break;
                    }
                }
                web.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                showFileInFullView(list,web,fullSizeViewIV,dialog.getContext(),count);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i <list.size() ; i++) {
                    if (list.get(i).isSelected()){
                        list.get(i).setSelected(false);
                        if (i-1!=-1)
                            list.get(i-1).setSelected(true);
                        else list.get(list.size()-1).setSelected(true);
                        break;
                    }
                }
                web.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                showFileInFullView(list,web,fullSizeViewIV,dialog.getContext(),count);
            }
        });
        dialog.show();
    }
    private void showFileInFullView(List<MediasItem> list, WebView web, ZoomageView fullSizeViewIV, Context context, TextView count){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count.setText(i+1+"/"+list.size());
                if (list.get(i).getMediaType().equalsIgnoreCase("pdf")) {
                    fullSizeViewIV.setVisibility(View.GONE);
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
                } else {
                    web.setVisibility(View.GONE);
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
    private void showShareDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.share_architect_dialog);
        dialog.setCancelable(false);
        TextInputLayout saleExeField = dialog.findViewById(R.id.saleExeInput);
        linkViewLL = dialog.findViewById(R.id.dialog_link_viewLL);
        dialogMsg = dialog.findViewById(R.id.dialog_message);
        dialogLintTV = dialog.findViewById(R.id.dialog_link_tv);
        salesExecutive = dialog.findViewById(R.id.dialog_sale_exe);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        ImageView whatsapp = dialog.findViewById(R.id.dialog_link_share_whatsapp);
        ImageView copy = dialog.findViewById(R.id.dialog_copy_link);
        HashMap<String, Object> map = new HashMap<>();
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(dialog.getContext(), dialogLintTV.getText().toString());
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(dialogLintTV.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", dialogLintTV.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(dialog.getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            saleExeField.setVisibility(View.GONE);
            map.put("sales_executive", new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID, "").toString());
            generateLink(new Gson().toJson(map));
        } else {
            getUsersByRoleName();
            saleExeField.setVisibility(View.VISIBLE);
            salesExecutive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    for (int j = 0; j < allOwnersList.size(); j++) {
                        allOwnersList.get(j).setSelected(false);
                    }
                    allOwnersList.get(i).setSelected(true);
                    for (int j = 0; j < allOwnersList.size(); j++) {
                        if (allOwnersList.get(j).isSelected()) {
                            map.put("sales_executive", allOwnersList.get(j).getId());
                            break;
                        }
                    }
                    generateLink(new Gson().toJson(map));
                }
            });
        }

        dialog.show();
    }

    private void generateLink(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GENERATEARCHITECTLINK, data);
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_GENERATEARCHITECTLINK, map, true);
    }

    private void getUsersByRoleName() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Sales Executive");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }

    private void showDeleteDialog(String button_msg, String msg, String title_msg, String id) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.order_delete_dialog);
        dialog.setCancelable(false);
        ImageView cross = dialog.findViewById(R.id.order_dialog_crossIV);
        TextView title = dialog.findViewById(R.id.order_dialog_titleTV);
        TextView text = dialog.findViewById(R.id.order_dialog_text);
        TextView yes = dialog.findViewById(R.id.order_dialog_yes);
        TextView no = dialog.findViewById(R.id.order_dialog_no);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!title_msg.equalsIgnoreCase("Delete Architect Media")) callDeleteArchitectApi(id);
            }
        });
        title.setText(title_msg);
        text.setText(msg);
        yes.setText(button_msg);
        dialog.show();

    }

    private void callDeleteMediaApi(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("architectId", id);
        map.put("mediaId", deleteArchitectMediaList);
        map.put(NKeys.DELETEARCHITECTMEDIA, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_DELETEARCHITECTMEDIA, map, true);
    }

    private void callDeleteArchitectApi(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("architectId", id);
        map.put(NKeys.DELETEARCHITECT, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_DELETEARCHITECT, map, true);
    }

    private void getArchitectDetail(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("architectId", id);
        map.put(NKeys.GETARCHITECTDETAILS, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_GETARCHITECTDETAILS, map, true);

    }

    private void setArchitectData(Records item) {
        setAccessibiltyToggle();
        binding.nameTV.setText(item.getFirstName() + " " + item.getLastName());
        binding.orderCountTV.setText(String.valueOf(item.getTotalOrderCount()));
        binding.firstNameET.setText(item.getFirstName());
        binding.lastNameET.setText(item.getLastName());
        binding.customerMobileNumET.setText(item.getPrimaryPhone());
        binding.whatsappIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(getBaseContext(),item.getPrimaryPhone());
            }
        });
        binding.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getBaseContext(),item.getPrimaryPhone());
            }
        });
        if (item.getSecondaryPhone()!=null) binding.alternateMobileNumET.setText(item.getSecondaryPhone());
        if (item.getEmail()!=null) binding.emailET.setText(item.getEmail());
        Common.showLog("++city++=="+item.getCity());
        binding.cityET.setText(item.getCity());
        binding.stateET.setText(item.getState());
        binding.companyNameET.setText(item.getCompanyName());
        fileAdapter = new ImagePDFCardSetup(this, item.getMedias(), editable,this);
        binding.uploadArchitectLetterheadImgRv.setHasFixedSize(true);
        binding.uploadArchitectLetterheadImgRv.setLayoutManager(new LinearLayoutManager(this));
        binding.uploadArchitectLetterheadImgRv.setAdapter(fileAdapter);
        if (item.getBrandName() != null)
            binding.brandNameET.setText(item.getBrandName().toString());
        binding.addressET.setText(item.getAddress());
        if (item.getPincode() != null) binding.pincodeET.setText(item.getPincode());
        if (item.getGstNumber() != null) binding.enterGst.setText(item.getGstNumber().toString());
        if (item.getEstablishedYear() != null)
            binding.establishedYearET.setText(item.getEstablishedYear().toString());
        if (item.getMedias().size() > 0) {
            binding.uploadArchitectLetterheadImgRv.setVisibility(View.VISIBLE);
//            imagesArrayList = (ArrayList<MediasItem>) item.getMedias();
//            imageAdapter.refreshList(imagesArrayList,"");
        } else {
            binding.uploadArchitectLetterheadImgRv.setVisibility(View.GONE);
        }
    }

    private void setAccessibiltyToggle() {
        binding.firstNameET.setEnabled(editable);
        binding.lastNameET.setEnabled(editable);
        binding.customerMobileNumET.setEnabled(editable);
        binding.alternateMobileNumET.setEnabled(editable);
        binding.emailET.setEnabled(editable);
        binding.companyNameET.setEnabled(editable);
        binding.brandNameET.setEnabled(editable);
        binding.addressET.setEnabled(editable);
        binding.cityET.setEnabled(editable);
        binding.stateET.setEnabled(editable);
        binding.pincodeET.setEnabled(editable);
        binding.enterGst.setEnabled(editable);
        binding.establishedYearET.setEnabled(editable);
        binding.selectImg.setClickable(editable);
        binding.save.setEnabled(editable);
        if (editable) {
            binding.uploadImageRL.setVisibility(View.VISIBLE);
        } else {
            binding.uploadImageRL.setVisibility(View.GONE);
        }
        if (fileAdapter != null) {
            fileAdapter.editable = !editable;
            fileAdapter.notifyDataSetChanged();
        }
        editable = !editable;
    }

    private void callApi(String id) {
        callDeleteMediaApi(id);
        HashMap<String, Object> map = new HashMap<>();
        if (getIntent().getStringExtra("type").equalsIgnoreCase("details")) {
            map.put("type", "edit");
            map.put("architectId", id);
            setAccessibiltyToggle();
            binding.deleteArchIv.setVisibility(View.VISIBLE);
            binding.editArchIv.setVisibility(View.VISIBLE);
        } else {
            map.put("type", "create");
        }
        map.put("first_name", binding.firstNameET.getText().toString());
        map.put("last_name", binding.lastNameET.getText().toString());
        map.put("primary_phone", binding.customerMobileNumET.getText().toString());
        map.put("company_name", binding.companyNameET.getText().toString());
        map.put("address", binding.addressET.getText().toString());
        map.put("brand_name", binding.brandNameET.getText().toString());
        map.put("gst_number", binding.enterGst.getText().toString());
        map.put("established_year", binding.establishedYearET.getText().toString());
        map.put("city", binding.cityET.getText().toString());
        map.put("state", binding.stateET.getText().toString());
        map.put("country", binding.countryET.getText().toString());
        map.put("pincode", binding.pincodeET.getText().toString());
        map.put("email", binding.emailET.getText().toString());
        map.put("secondary_phone", binding.alternateMobileNumET.getText().toString());

        map.put("file", SELECTEDFILES);
        Common.showLog("map==--==" + map);
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_CREATEARCHITECT, map, true);
    }

    private void setupAdapter(ArrayList<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        binding.architectLetterheadImgRv.setHasFixedSize(true);
        binding.architectLetterheadImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.architectLetterheadImgRv.setAdapter(imageAdapter);
    }

    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
        if (imagesArrayList.isEmpty()) binding.architectLetterheadImgRv.setVisibility(View.GONE);
        else binding.architectLetterheadImgRv.setVisibility(View.VISIBLE);
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
                                        // Extract file extension

                                        ArrayList<String> imgs = new ArrayList<>();
                                        imgs.add(listData);
                                        Images images = new Images();
                                        images.setLeadAttachment(imgs);
                                        imagesArrayList.add(images);
                                        if (imagesArrayList.isEmpty()) binding.architectLetterheadImgRv.setVisibility(View.GONE);
                                        else binding.architectLetterheadImgRv.setVisibility(View.VISIBLE);
                                        imageAdapter.refreshList(imagesArrayList);
//                                        } else {
//                                            Common.showToast(CreateArchitectActivity.this, "Please upload a file smaller than 2 MB.");
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

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATEARCHITECT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    allOwnersList.add(recordsItem);
                }
                cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, allOwnersList, true);
                salesExecutive.setAdapter(cityAdapter);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETARCHITECTDETAILS) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                item = response.getData().getRecords();
                if (item.getTotalOrderCount()==0) binding.showOrdersByArchitect.setVisibility(View.GONE);
                setArchitectData(item);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEARCHITECT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEARCHITECTMEDIA) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GENERATEARCHITECTLINK) {
                try {
                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        String token = jsonObject.getJSONObject("data").getString("token");
                        linkViewLL.setVisibility(View.VISIBLE);
                        dialogLintTV.setText("https://mytyles.digital/home/guest-architect-signup/" + token);
                        dialogMsg.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseDO.getResponse());
                Common.showToast(this, jsonObject.optString("message", ""));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void viewFileListener(List<MediasItem> list) {
        fileViewDialog(list);
    }

    @Override
    public void deleteFileListener(List<String> list , String id) {
        deleteArchitectMediaList=list;
    }
}