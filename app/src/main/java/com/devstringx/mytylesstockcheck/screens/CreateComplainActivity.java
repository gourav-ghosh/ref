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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityCreateComplainBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateComplainActivity extends AppCompatActivity implements UploadFileAdapter.removeImage, ResponseListner {
    ActivityCreateComplainBinding createComplainBinding;
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private List<String> years = new ArrayList<>();
    private List<String> complaintType = new ArrayList<>();
    private List<RecordsItem> allOwnersList;
    private String supportExeId;
    private CustomAutoCompleteListAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createComplainBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_complain);
        addYearsInList();
        addComplaintType();
        getUsersByRoleName();
        createComplainBinding.complainOrderNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSalesOrderApi();
            }
        });
        createComplainBinding.financialYear.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, years));
        createComplainBinding.complaintType.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, complaintType));
        createComplainBinding.financialYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkSalesOrderApi();
            }
        });
        createComplainBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        createComplainBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        createComplainBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        createComplainBinding.dobET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openCalenderForUpcomingDates(CreateComplainActivity.this, createComplainBinding.dobET);
            }
        });
        createComplainBinding.dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(CreateComplainActivity.this, createComplainBinding.dobET);
            }
        });
        createComplainBinding.supportExe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < allOwnersList.size(); j++) {
                    allOwnersList.get(j).setSelected(false);
                }
                allOwnersList.get(i).setSelected(true);
            }
        });
        createComplainBinding.selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(CreateComplainActivity.this)
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
    }

    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        if (createComplainBinding.complainOrderNo.getText().toString().isEmpty()){
            Common.showToast(this,"Order no. is mandatory");
            return;
        }
        else if (createComplainBinding.financialYear.getText().toString().isEmpty()){
            Common.showToast(this,"financial year is mandatory");
            return;
        }
        else if (createComplainBinding.nameET.getText().toString().isEmpty()) {
            Common.showToast(this,"Customer Name is mandatory");
            return;
        }
        else if (createComplainBinding.customerMobileNumET.getText().toString().isEmpty()) {
            Common.showToast(this,"Customer mobile number is mandatory");
            return;
        }
        else if (createComplainBinding.complaintType.getText().toString().isEmpty()) {
            Common.showToast(this,"Complaint type is mandatory");
            return;
        }
        else if (createComplainBinding.supportExe.getText().toString().isEmpty()) {
            Common.showToast(this,"Support Executive is mandatory");
            return;
        }
        else if (createComplainBinding.dobET.getText().toString().isEmpty()) {
            Common.showToast(this,"Exp. Resolution Date is mandatory");
            return;
        }
        else if (createComplainBinding.commentET.getText().toString().isEmpty()) {
            Common.showToast(this,"Comment is mandatory");
            return;
        }

        map.put("sale_order_no", createComplainBinding.complainOrderNo.getText().toString());
        map.put("financial_year", createComplainBinding.financialYear.getText().toString());
        map.put("customer_fullname", createComplainBinding.nameET.getText().toString());
        map.put("customer_mobile_number", createComplainBinding.customerMobileNumET.getText().toString());
        map.put("complaint_type", createComplainBinding.complaintType.getText().toString());
        map.put("other_complaint_type", "");
        map.put("estimate_resolve_date", createComplainBinding.dobET.getText().toString());
        map.put("comment",createComplainBinding.commentET.getText().toString());
        for (int j = 0; j < allOwnersList.size(); j++) {
            if (allOwnersList.get(j).isSelected()) {
                supportExeId = allOwnersList.get(j).getId();
                break;
            }
        }
        map.put("file", SELECTEDFILES);
        Common.showLog("id===="+supportExeId);
        map.put("support_executive", supportExeId);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CREATECOMPLAINT, map, true);
    }

    private void getUsersByRoleName() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Support Executive");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }

    private void addComplaintType() {
        complaintType.add("Damage");
        complaintType.add("Wrong Item Delivery");
        complaintType.add("Batch Variation");
        complaintType.add("Wrong Quantity");
        complaintType.add("Delayed Delivery");
        complaintType.add("Others");
    }

    private void checkSalesOrderApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sale_order_no", createComplainBinding.complainOrderNo.getText().toString());
            jsonObject.put("financial_year", createComplainBinding.financialYear.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CHECKSALESORDERNUMBER, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CHECKSALESORDERNUMBER, map, false);
    }

    private void addYearsInList() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        for (int year = 2000; year <= currentYear; year++) {
            years.add(String.valueOf(year));
            if (year == currentYear)
                createComplainBinding.financialYear.setText(String.valueOf(year));
        }
    }

    private void setupAdapter(ArrayList<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        createComplainBinding.complainUploadImgRv.setHasFixedSize(true);
        createComplainBinding.complainUploadImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        createComplainBinding.complainUploadImgRv.setAdapter(imageAdapter);
    }

    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
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
                                            createComplainBinding.complainUploadImgRv.setVisibility(View.VISIBLE);
                                        } else {
                                            Common.showToast(CreateComplainActivity.this, "Please upload a file smaller than 2 MB.");
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

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_CHECKSALESORDERNUMBER) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        createComplainBinding.errorMsgTv.setVisibility(View.GONE);
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        String customerName = dataObj.getString("customer_name");
                        String mobileNumber = dataObj.getString("customer_phone_number");
                        Common.showLog("data0-----=" + customerName + mobileNumber);
                        createComplainBinding.customerMobileNumET.setText(mobileNumber);
                        createComplainBinding.nameET.setText(customerName);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATECOMPLAINT) {
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
                createComplainBinding.supportExe.setAdapter(cityAdapter);
            }
        } else {
            createComplainBinding.errorMsgTv.setVisibility(View.VISIBLE);
            createComplainBinding.nameET.setText("");
            createComplainBinding.customerMobileNumET.setText("");
        }
    }
}