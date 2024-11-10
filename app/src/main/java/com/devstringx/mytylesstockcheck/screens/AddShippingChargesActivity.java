package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityAddShippingChargesBinding;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddShippingChargesActivity extends AppCompatActivity implements ResponseListner, UploadFileAdapter.removeImage {
    ActivityAddShippingChargesBinding binding;
    List<String> orderNumbers;
    List<String> shippingId;
    private String selectedShippingId = "";
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_shipping_charges);
        getShippingChargesToAdd();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        binding.selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(AddShippingChargesActivity.this)
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
        });
        setupAdapter(imagesArrayList);
        binding.shipOrderNumTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedShippingId = shippingId.get(i);
            }
        });
    }
    private void setupAdapter(ArrayList<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        binding.shippingUploadImgRv.setHasFixedSize(true);
        binding.shippingUploadImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.shippingUploadImgRv.setAdapter(imageAdapter);
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
                                        float size = testFile.length() / (1024 * 1024);
                                        if (size <= 2) {
                                            SELECTEDFILES.add(listData);
                                            ArrayList<String> imgs = new ArrayList<>();
                                            imgs.add(listData);
                                            Images images = new Images();
                                            images.setLeadAttachment(imgs);
                                            imagesArrayList.add(images);
                                            imageAdapter.refreshList(imagesArrayList);
                                            binding.shippingUploadImgRv.setVisibility(View.VISIBLE);
                                        } else {
                                            Common.showToast(AddShippingChargesActivity.this, "Please upload a file smaller than 2 MB.");
                                        }
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


    private void callApi() {
        if (binding.shipOrderNumTV.getText().toString().isEmpty()) {
            Common.showToast(this,"Please select an order number.");
            return;
        }
        if (binding.distanceCoveredET.getText().toString().isEmpty()) {
            Common.showToast(this,"Distance covered field is mandatory.");
            return;
        }
        if (binding.loadingPoints.getText().toString().isEmpty()) {
            Common.showToast(this,"No. Of Loading Points field is mandatory.");
            return;
        }
        if (binding.amountET.getText().toString().isEmpty()) {
            Common.showToast(this,"Amount field is mandatory.");
            return;
        }
        if (binding.commentET.getText().toString().isEmpty()) {
            Common.showToast(this,"Comment field is mandatory.");
            return;
        }
        if (SELECTEDFILES.isEmpty()) {
            Common.showToast(this,"Please select an image.");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("shipping_id", selectedShippingId);
        map.put("distance", binding.distanceCoveredET.getText().toString());
        map.put("loading_points", binding.loadingPoints.getText().toString());
        map.put("amount", binding.amountET.getText().toString());
        map.put("comment", binding.commentET.getText().toString());
        map.put("file", SELECTEDFILES);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ADDSHIPPINGCHARGE, map, true);
    }

    private void getShippingChargesToAdd() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETSHIPPINGCHARGESTOADD, null, false);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETSHIPPINGCHARGESTOADD) {
                orderNumbers = new ArrayList<>();
                shippingId = new ArrayList<>();

                try {
                    JSONObject response = new JSONObject(responseDO.getResponse());
                    JSONObject data = response.getJSONObject("data");
                    JSONArray shippingCharges = data.getJSONArray("shipping_charge");

                    for (int i = 0; i < shippingCharges.length(); i++) {
                        JSONObject shippingCharge = shippingCharges.getJSONObject(i);
                        String orderNo = shippingCharge.getString("sale_order_no");
                        String shipping_id = shippingCharge.getString("shipping_id");
                        orderNumbers.add(orderNo);
                        shippingId.add(shipping_id);
                    }
                    binding.shipOrderNumTV.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, orderNumbers));
                    Common.showLog(orderNumbers.get(0)+"   "+orderNumbers.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_ADDSHIPPINGCHARGE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            Common.showToast(this, responseDO.getResponse());
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
    }
}