package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.InstructionFileAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityProcessForDispatchBinding;
import com.devstringx.mytylesstockcheck.datamodal.FilePickerData;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.quotationDetails.Records;
import com.devstringx.mytylesstockcheck.screens.fragments.OrderListingFragment;
import com.devstringx.mytylesstockcheck.screens.myProfile.MyProfileOptionsFragment;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class ProcessForDispatchActivity extends AppCompatActivity implements ResponseListner, UploadFileAdapter.removeImage, InstructionFileAdapter.InstructionListner {
    ActivityProcessForDispatchBinding processForDispatchBinding;
    private Records mRecord;
    private List<String> orderTypeList = new ArrayList<>();
    private List<String> expectedDeliveryTimeList = new ArrayList<>();
    private List<String> deliveryLocationList = new ArrayList<>();
    private List<RecordsItem> architectList;
    private List<RecordsItem> dispatch_managerList;
    private String dispatch_managerId;
    private String architectId;
    private CustomAutoCompleteListAdapter cityAdapter;
    String deliveryType = "";

    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private InstructionFileAdapter instructionFileAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();

    private List<FilePickerData> filePickerData = new ArrayList<>();
    private boolean isInstructionFile = true;
    private MyBroadcastReceiver myReceiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processForDispatchBinding = DataBindingUtil.setContentView(this, R.layout.activity_process_for_dispatch);
        getDispatchManagerList();
        getArchitectNameList();
        if (getIntent().getSerializableExtra("data") != null) {
            mRecord = (Records) getIntent().getSerializableExtra("data");
            setData();
        }

        String delivery = getIntent().getStringExtra("deliveryType");
        if(delivery.equalsIgnoreCase("home"))
            processForDispatchBinding.homeDeliveryIv.setSelected(true);

        if (delivery.equalsIgnoreCase("pick-up"))
            processForDispatchBinding.pickUpIv.setSelected(true);

        processForDispatchBinding.deliveryLinearlayout.setVisibility(View.GONE);

        orderTypeList.add("New Order");
        orderTypeList.add("Shortage Order");
        expectedDeliveryTimeList.add("First half");
        expectedDeliveryTimeList.add("Second half");
        deliveryLocationList.add("Instation");
        deliveryLocationList.add("Outstation");
//        processForDispatchBinding.pickUpIv.setSelected(true);
        processForDispatchBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(delivery.equalsIgnoreCase("home")) {
            processForDispatchBinding.homeDeliveryIv.setImageResource(R.drawable.orange_checkbox_selected);
            processForDispatchBinding.pickUpIv.setImageResource(R.drawable.orange_checkbox_unselected);
            processForDispatchBinding.homeDeliveryIv.setSelected(true);
            processForDispatchBinding.isPickupReadyLl.setVisibility(View.GONE);
            processForDispatchBinding.deliveryLoc.setVisibility(View.VISIBLE);
            processForDispatchBinding.deliveryTimeLL.setVisibility(View.VISIBLE);
            processForDispatchBinding.orderRequirement.setVisibility(View.VISIBLE);
            processForDispatchBinding.arrangeBeforeDate.setVisibility(View.GONE);
            processForDispatchBinding.pickUpIv.setSelected(false);
            processForDispatchBinding.deliveryTv.setText("Home Delivery");
            deliveryType = "Home Delivery";
        }

        if (delivery.equalsIgnoreCase("pick-up")){
            processForDispatchBinding.pickUpIv.setImageResource(R.drawable.orange_checkbox_selected);
            processForDispatchBinding.isPickupReadyLl.setVisibility(View.VISIBLE);
            processForDispatchBinding.deliveryLoc.setVisibility(View.GONE);
            processForDispatchBinding.deliveryTimeLL.setVisibility(View.GONE);
            processForDispatchBinding.orderRequirement.setVisibility(View.GONE);
            processForDispatchBinding.arrangeBeforeDate.setVisibility(View.VISIBLE);
            processForDispatchBinding.homeDeliveryIv.setImageResource(R.drawable.orange_checkbox_unselected);
            processForDispatchBinding.deliveryTv.setText("Pick - Up Delivery");
            deliveryType = "Pick Up";
        }

        processForDispatchBinding.homeDeliveryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!processForDispatchBinding.homeDeliveryIv.isSelected()) {
                    processForDispatchBinding.homeDeliveryIv.setImageResource(R.drawable.orange_checkbox_selected);
                    processForDispatchBinding.pickUpIv.setImageResource(R.drawable.orange_checkbox_unselected);
                    processForDispatchBinding.homeDeliveryIv.setSelected(true);
                    processForDispatchBinding.isPickupReadyLl.setVisibility(View.GONE);
                    processForDispatchBinding.deliveryLoc.setVisibility(View.VISIBLE);
                    processForDispatchBinding.deliveryTimeLL.setVisibility(View.VISIBLE);
                    processForDispatchBinding.orderRequirement.setVisibility(View.VISIBLE);
                    processForDispatchBinding.arrangeBeforeDate.setVisibility(View.GONE);
                    processForDispatchBinding.pickUpIv.setSelected(false);
                    deliveryType = "Home Delivery";
                }
            }
        });
        processForDispatchBinding.pickUpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!processForDispatchBinding.pickUpIv.isSelected()) {
                    processForDispatchBinding.pickUpIv.setImageResource(R.drawable.orange_checkbox_selected);
                    processForDispatchBinding.homeDeliveryIv.setImageResource(R.drawable.orange_checkbox_unselected);
                    processForDispatchBinding.pickUpIv.setSelected(true);
                    processForDispatchBinding.isPickupReadyLl.setVisibility(View.VISIBLE);
                    processForDispatchBinding.deliveryLoc.setVisibility(View.GONE);
                    processForDispatchBinding.deliveryTimeLL.setVisibility(View.GONE);
                    processForDispatchBinding.orderRequirement.setVisibility(View.GONE);
                    processForDispatchBinding.arrangeBeforeDate.setVisibility(View.VISIBLE);
                    processForDispatchBinding.homeDeliveryIv.setSelected(false);
                    deliveryType = "Pick Up";
                }
            }
        });
        processForDispatchBinding.orderType.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, orderTypeList));
        processForDispatchBinding.expectedDeliveryTime.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, expectedDeliveryTimeList));
        processForDispatchBinding.deliveryLocET.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, deliveryLocationList));
        processForDispatchBinding.arrangeBeforeDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(ProcessForDispatchActivity.this, processForDispatchBinding.arrangeBeforeDateET);
            }
        });
        processForDispatchBinding.arrangeBeforeDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openCalenderForUpcomingDates(ProcessForDispatchActivity.this, processForDispatchBinding.arrangeBeforeDateET);
            }
        });
        processForDispatchBinding.expectedDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(ProcessForDispatchActivity.this, processForDispatchBinding.expectedDeliveryDate);
            }
        });
        processForDispatchBinding.expectedDeliveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openCalenderForUpcomingDates(ProcessForDispatchActivity.this, processForDispatchBinding.expectedDeliveryDate);
            }
        });

        processForDispatchBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        processForDispatchBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        processForDispatchBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        processForDispatchBinding.addMediaTv.setOnClickListener(v -> {
            isInstructionFile = true;
            ArrayList<String> mMimeTypesList = new ArrayList<>();
            mMimeTypesList.add("image/*");
            captureImageResultLauncher.launch(
                    new FilePicker.Builder(this)
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
        });
        processForDispatchBinding.addLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddActivityDialog();
            }
        });
        processForDispatchBinding.chooseFileLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInstructionFile = false;
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(ProcessForDispatchActivity.this)
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
        setupAdapter(imagesArrayList);
        processForDispatchBinding.architectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                processForDispatchBinding.architectNameCantBeEmpty.setVisibility(View.GONE);
                for (int j = 0; j < architectList.size(); j++) {
                    architectList.get(j).setSelected(false);
                }
                architectList.get(i).setSelected(true);
            }
        });
        processForDispatchBinding.dispatchManagerNameET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                processForDispatchBinding.dispatchManagerNameCantBeEmpty.setVisibility(View.GONE);
                for (int j = 0; j < dispatch_managerList.size(); j++) {
                    dispatch_managerList.get(j).setSelected(false);
                }
                dispatch_managerList.get(i).setSelected(true);
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.devstringx.mytylesstockcheck.SOCKET_NOTIFICATION");
        registerReceiver(myReceiver, intentFilter);

        processForDispatchBinding.salesOrderNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.salesOrderNoCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.arrangeBeforeDateET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.arrangeBeforeDateCantBeEmpty.setVisibility(View.GONE);
            }
        });

        processForDispatchBinding.customerNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.customerNameCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.customerMobileNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.customerMobileNoCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.salesPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.salesPersonNameCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.expectedDeliveryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.expDeliveryDateCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.orderAmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processForDispatchBinding.orderAmountCantBeEmpty.setVisibility(View.GONE);

            }
        });

        processForDispatchBinding.orderType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processForDispatchBinding.orderTypeCantBeEmpty.setVisibility(View.GONE);
            }
        });

        processForDispatchBinding.orderRequirementACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processForDispatchBinding.orderRequirementCantBeEmpty.setVisibility(View.GONE);
            }
        });

        processForDispatchBinding.expectedDeliveryTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processForDispatchBinding.expDeliveryTimeCantBeEmpty.setVisibility(View.GONE);
            }
        });

        processForDispatchBinding.deliveryLocET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processForDispatchBinding.deliveryLocationCantBeEmpty.setVisibility(View.GONE);
            }
        });
    }


    private void getArchitectNameList() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETARCHITECTNAMELIST, null, true);
    }

    private void getDispatchManagerList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Dispatch Manager");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }

    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();

        if (processForDispatchBinding.salesOrderNumber.getText().toString().isEmpty()) {
            processForDispatchBinding.salesOrderNoCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.orderType.getText().toString().isEmpty()) {
            processForDispatchBinding.orderTypeCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.customerNameET.getText().toString().isEmpty()) {
            processForDispatchBinding.customerNameCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.customerMobileNumET.getText().toString().isEmpty()) {
            processForDispatchBinding.customerMobileNoCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.orderAmountET.getText().toString().isEmpty()) {
            processForDispatchBinding.orderAmountCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.salesPersonName.getText().toString().isEmpty()) {
            processForDispatchBinding.salesPersonNameCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.architectName.getText().toString().isEmpty()) {
            processForDispatchBinding.architectNameCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (processForDispatchBinding.dispatchManagerNameET.getText().toString().isEmpty()) {
            processForDispatchBinding.dispatchManagerNameCantBeEmpty.setVisibility(View.VISIBLE);
        }

        if (SELECTEDFILES.size() == 0) {
            processForDispatchBinding.pleaseAttachProof.setVisibility(View.VISIBLE);
        }

        if (deliveryType.equalsIgnoreCase("Pick Up")){
            if (processForDispatchBinding.arrangeBeforeDateET.getText().toString().isEmpty()) {
                processForDispatchBinding.arrangeBeforeDateCantBeEmpty.setVisibility(View.VISIBLE);
            }
            map.put("delivery_date", processForDispatchBinding.arrangeBeforeDateET.getText().toString());
        } else {
            if (processForDispatchBinding.expectedDeliveryDate.getText().toString().isEmpty()) {
                processForDispatchBinding.expDeliveryDateCantBeEmpty.setVisibility(View.VISIBLE);
            }
            if (processForDispatchBinding.expectedDeliveryTime.getText().toString().isEmpty()) {
                processForDispatchBinding.expDeliveryTimeCantBeEmpty.setVisibility(View.VISIBLE);
            }
            map.put("delivery_date", processForDispatchBinding.expectedDeliveryDate.getText().toString());
            map.put("delivery_time", processForDispatchBinding.expectedDeliveryTime.getText().toString());

            if (processForDispatchBinding.orderRequirementACTV.getText().toString().isEmpty()) {
                processForDispatchBinding.orderRequirementCantBeEmpty.setVisibility(View.VISIBLE);
            }

            if (processForDispatchBinding.deliveryLocET.getText().toString().isEmpty()) {
                processForDispatchBinding.deliveryLocationCantBeEmpty.setVisibility(View.VISIBLE);
            }

        }

        if(deliveryType.equalsIgnoreCase("Pick Up")){

            if(processForDispatchBinding.salesOrderNumber.getText().toString().isEmpty() ||
                    processForDispatchBinding.orderType.getText().toString().isEmpty() ||
                    processForDispatchBinding.arrangeBeforeDateET.getText().toString().isEmpty() ||
                    processForDispatchBinding.customerNameET.getText().toString().isEmpty() ||
                    processForDispatchBinding.customerMobileNumET.getText().toString().isEmpty() ||
                    processForDispatchBinding.orderAmountET.getText().toString().isEmpty() ||
                    processForDispatchBinding.architectName.getText().toString().isEmpty() ||
                    processForDispatchBinding.salesPersonName.getText().toString().isEmpty() ||
                    processForDispatchBinding.dispatchManagerNameET.getText().toString().isEmpty() ||
                    SELECTEDFILES.size() == 0) {

                processForDispatchBinding.nestedScrollView.scrollTo(0,0);
                return;
            }

        } else {

            if(processForDispatchBinding.salesOrderNumber.getText().toString().isEmpty() ||
                    processForDispatchBinding.orderType.getText().toString().isEmpty() ||
                    processForDispatchBinding.orderRequirementACTV.getText().toString().isEmpty() ||
                    processForDispatchBinding.expectedDeliveryDate.getText().toString().isEmpty() ||
                    processForDispatchBinding.expectedDeliveryTime.getText().toString().isEmpty() ||
                    processForDispatchBinding.customerNameET.getText().toString().isEmpty() ||
                    processForDispatchBinding.customerMobileNumET.getText().toString().isEmpty() ||
                    processForDispatchBinding.orderAmountET.getText().toString().isEmpty() ||
                    processForDispatchBinding.architectName.getText().toString().isEmpty() ||
                    processForDispatchBinding.salesPersonName.getText().toString().isEmpty() ||
                    processForDispatchBinding.dispatchManagerNameET.getText().toString().isEmpty() ||
                    processForDispatchBinding.deliveryLocET.getText().toString().isEmpty() ||
                    SELECTEDFILES.size() == 0) {

                processForDispatchBinding.nestedScrollView.scrollTo(0,0);

                return;
            }

        }


        String links = "";
        String instru = "";
        for (int i = 0; i < filePickerData.size(); i++) {
            if (filePickerData.get(i).getFileType().equalsIgnoreCase("link")) {
                links += filePickerData.get(i).getFileName() + ",";
            } else {
                instru += filePickerData.get(i).getFilePath() + ",";
            }
        }
        if (!links.isEmpty())
            links = links.substring(0, links.length() - 1);
        if (!instru.isEmpty())
            instru = instru.substring(0, instru.length() - 1);


        map.put("id", String.valueOf(mRecord.getId()));
        map.put("comment", processForDispatchBinding.commentET.getText().toString());
        map.put("delivery_instructions", processForDispatchBinding.instructionET.getText().toString());
        map.put("delivery_type", deliveryType);
        map.put("delivery_location_type", processForDispatchBinding.deliveryLocET.getText().toString());

        if (processForDispatchBinding.orderType.getText().toString().equalsIgnoreCase("New Order"))
            map.put("order_type", "new_order");
        else map.put("order_type", "shortage_order");

        map.put("sale_order_no", mRecord.getQuoteNumber());
        map.put("customer_phone_number", mRecord.getPrimaryPhone());
        map.put("quote_order_amount", mRecord.getFinalTotal());
        map.put("direct_ready_for_pickup", String.valueOf(processForDispatchBinding.pickupReadyToggleBtn.isChecked()));
        map.put("sales_person", mRecord.getSalesExecutiveUser());
        map.put("links", links);
        map.put("instructions", instru);

        for (int j = 0; j < dispatch_managerList.size(); j++) {
            if (dispatch_managerList.get(j).isSelected()) {
                dispatch_managerId = dispatch_managerList.get(j).getId();
                break;
            }
        }

        for (int j = 0; j < architectList.size(); j++) {
            if (architectList.get(j).isSelected()) {
                architectId = architectList.get(j).getId();
                break;
            }
        }
        Common.showLog("architectId"+architectId+"   "+"dispatch_managerId"+dispatch_managerId);
        map.put("dispatch_manager",dispatch_managerId);
        map.put("architect",architectId);
        map.put("proof", SELECTEDFILES);
        Common.showLog("map==--==" + map);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ORDERDISPATCHED, map, true);
    }

    private void setData() {
        processForDispatchBinding.salesOrderNumber.setText(mRecord.getQuoteNumber());
        processForDispatchBinding.customerNameET.setText(mRecord.getFullName());
        processForDispatchBinding.orderAmountET.setText(mRecord.getFinalTotal());
        processForDispatchBinding.salesPersonName.setText(mRecord.getSalesExecutiveName());
        processForDispatchBinding.customerMobileNumET.setText(mRecord.getPrimaryPhone());
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_ORDERDISPATCHED) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        OrderListingFragment orderListingFragment = new OrderListingFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("from","order_conversion");
                        orderListingFragment.setArguments(bundle);
                        Common.loadFragment(getSupportFragmentManager(), orderListingFragment, R.id.order_view_pager);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                dispatch_managerList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    dispatch_managerList.add(recordsItem);
                }
                cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, dispatch_managerList, true);
                processForDispatchBinding.dispatchManagerNameET.setAdapter(cityAdapter);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETARCHITECTNAMELIST) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                architectList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    architectList.add(recordsItem);
                }
//                JSONArray records = null;
//                try {
//                    JSONObject obj = new JSONObject(responseDO.getResponse());
//                    records = obj.getJSONObject("data").getJSONArray("records");
//                    for (int i = 0; i < records.length(); i++) {
//                        JSONObject record = records.getJSONObject(i);
//                        String firstName = record.getString("first_name");
//                        String lastName = record.getString("last_name");
//                        String fullName = firstName + " " + lastName;
//                        architectNameList.add(fullName);
//                    }
                cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, architectList, true);
                processForDispatchBinding.architectName.setAdapter(cityAdapter);
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//
//                }
            }
        } else {
            Common.showToast(this, responseDO.getResponse().toString());

        }
    }

    private void setupAdapter(List<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        processForDispatchBinding.uploadFileRv.setHasFixedSize(true);
        processForDispatchBinding.uploadFileRv.setLayoutManager(new GridLayoutManager(this, 3));
        processForDispatchBinding.uploadFileRv.setAdapter(imageAdapter);
        instructionFileAdapter = new InstructionFileAdapter(this, filePickerData, this);
        processForDispatchBinding.instructionFileRv.setHasFixedSize(true);
        processForDispatchBinding.instructionFileRv.setLayoutManager(new GridLayoutManager(this, 2));
        processForDispatchBinding.instructionFileRv.setAdapter(instructionFileAdapter);
    }

    private void showAddActivityDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_link_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        AutoCompleteTextView autoCompleteTxt = dialog.findViewById(R.id.activity_type_auto_complete_txt);
        EditText linkET = dialog.findViewById(R.id.linkET);
        linkET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String url = editable.toString();
                if (!Patterns.WEB_URL.matcher(url).matches()) {
                    linkET.setError("Invalid URL");
                } else {
                    linkET.setError(null);
                }
            }
        });
        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (linkET.getText().toString().trim().equalsIgnoreCase("")) {
                    Common.showToast(ProcessForDispatchActivity.this, "Please enter link.");
                    return;
                }
                FilePickerData data = new FilePickerData();
                data.setFileType("link");
                data.setFilePath(linkET.getText().toString().toString());
                data.setFileName(linkET.getText().toString().toString());
                filePickerData.add(data);
                instructionFileAdapter.refreshList(filePickerData);
                dialog.dismiss();
            }
        });
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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

    private final ActivityResultLauncher<Intent> captureImageResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            try {
                                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData().getData() != null) {
                                        processForDispatchBinding.pleaseAttachProof.setVisibility(View.GONE);
                                        String listData = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                        File testFile = new File(listData);
                                        float size = testFile.length() / (1024 * 1024);
                                        if (size <= 2) {
                                            if (isInstructionFile) {
                                                FilePickerData data = new FilePickerData();
                                                data.setFileName(testFile.getName());
                                                data.setFilePath(testFile.getPath());
                                                data.setFileType(testFile.getPath().toLowerCase().endsWith(".pdf") ? "PDF" : "png");
                                                filePickerData.add(data);
                                                instructionFileAdapter.refreshList(filePickerData);
                                            } else {
                                                SELECTEDFILES.add(listData);
                                                ArrayList<String> imgs = new ArrayList<>();
                                                imgs.add(listData);
                                                Images images = new Images();
                                                images.setLeadAttachment(imgs);
                                                imagesArrayList.add(images);
                                                imageAdapter.refreshList(imagesArrayList);
                                                processForDispatchBinding.uploadFileRv.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            Common.showToast(ProcessForDispatchActivity.this, "Please upload a file smaller than 2 MB.");
                                        }
//                                        filePickerData.add(result.getData().getData());


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
    public void removeInstructionFileListner(int position) {
        filePickerData.remove(position);
        instructionFileAdapter.refreshList(filePickerData);
    }

    @Override
    public void openFileListner(FilePickerData filepath) {
        if (filepath.getFileType().equalsIgnoreCase("PDF")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.fromFile(new File(filepath.getFilePath())));
                startActivity(Intent.createChooser(intent, "choseFile"));
            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(ProcessForDispatchActivity.this, "File not supported.");
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(new File(filepath.getFilePath())));
            startActivity(Intent.createChooser(intent, "choseFile"));
        }
    }
}