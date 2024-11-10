package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityAddUserBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.branchNames.BranchResponse;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
import com.devstringx.mytylesstockcheck.datamodal.vendorComapnyData.CompanyResponse;
import com.devstringx.mytylesstockcheck.datamodal.vendorComapnyData.Record;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
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

public class AddUserActivity extends AppCompatActivity implements ResponseListner, UploadFileAdapter.removeImage {
    ActivityAddUserBinding addUserBinding;
    private Response response;
    private String imageLink="";
    private RecordsItem record = null;
    private DataItem latestRecord = null;
    private UploadFileAdapter imageAdapter;
    private List<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem> branchList,roleList, companyList;
    private CustomAutoCompleteListAdapter adapter,roleAdapter, companyAdapter;
    private String Profile_image="";
    private PreferenceUtils utils;
    private String SELECTEDROLE="";
    private boolean loggedinUser = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_user);
        addUserBinding.titleTv.setText(getIntent().getStringExtra("title"));
        utils=new PreferenceUtils(this);
        addUserBinding.roleTil.setEnabled(false);
        if (getIntent().getStringExtra("title").equalsIgnoreCase("Edit User")) {
            addUserBinding.save.setText("Update");
            if(Common.userRoleId.equalsIgnoreCase("1")) {
                addUserBinding.permissionsLl.setVisibility(View.VISIBLE);
            } else addUserBinding.permissionsLl.setVisibility(View.GONE);
            addUserBinding.vendorCompanyETTitle.setVisibility(View.GONE);
            addUserBinding.vendorCompanyTitle.setEnabled(false);
            addUserBinding.vendorCodeTitle.setEnabled(false);
            record = (RecordsItem) getIntent().getSerializableExtra("data");
            if(Common.userRoleId.equalsIgnoreCase("1")) setPermissionsVisibilty();
            callGetUserProfileApi(String.valueOf(record.getId()));
//            setData(record);
        } else {
            addUserBinding.save.setText("Add");
            addUserBinding.permissionsLl.setVisibility(View.GONE);
            if(Common.userRoleId.equalsIgnoreCase("1")) {
                addUserBinding.vendorCompanyETTitle.setVisibility(View.VISIBLE);
                addUserBinding.vendorCompanyTitle.setVisibility(View.GONE);
                addUserBinding.roleTil.setEnabled(true);
                getRoles();
            }
            if(utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3")){
                addUserBinding.selectRole.setText("Vendor User");
                SELECTEDROLE="4";
            }
            checkVendorRole();
            getUserProfile(utils.getStringFromPreference(PreferenceUtils.USERID, "").toString());
        }
        getVendorCompanyList();
        addUserBinding.chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(AddUserActivity.this)
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
        addUserBinding.dobET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openCalenderForUptoPresentDate(AddUserActivity.this, addUserBinding.dobET);
            }
        });
        addUserBinding.dojET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) Common.openCalender(AddUserActivity.this, addUserBinding.dojET);
            }
        });
        addUserBinding.dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUptoPresentDate(AddUserActivity.this, addUserBinding.dobET);
            }
        });
        addUserBinding.dojET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalender(AddUserActivity.this, addUserBinding.dojET);
            }
        });
        addUserBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addUserBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        addUserBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addUserBinding.selectBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < branchList.size(); i++) {
                    branchList.get(i).setSelected(false);
                }
                branchList.get(position).setSelected(true);
            }
        });
        addUserBinding.selectVendorCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < companyList.size(); i++) {
                    companyList.get(i).setSelected(false);
                }
                companyList.get(position).setSelected(true);
                addUserBinding.vendorCodeET.setText(companyList.get(position).getStateName());
            }
        });
        addUserBinding.selectRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < roleList.size(); i++) {
                    roleList.get(i).setSelected(false);
                }
                roleList.get(position).setSelected(true);
                SELECTEDROLE=roleList.get(position).getId();
                checkVendorRole();
                enableVendorCodeEditable();
            }
        });
        addUserBinding.complaintToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_cms", b ? "true" : "false");
            }
        });
        addUserBinding.complaintToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_cms", b ? "true" : "false");
            }
        });
        addUserBinding.crmToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_crm", b ? "true" : "false");
            }
        });
        addUserBinding.worklogToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_work_log", b ? "true" : "false");
            }
        });
        addUserBinding.overtimeToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_overtime", b ? "true" : "false");
            }
        });
        addUserBinding.orderToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("eligible_for_order_listing", b ? "true" : "false");
            }
        });
        addUserBinding.externalAgentToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUserPermissions("ask_payment", b ? "true" : "false");
            }
        });
//        addUserBinding.orderToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                updateUserPermissions("eligible_for_admin_rights", b ? "true" : "false");
//            }
//        });
        setupAdapter(imagesArrayList);
        getBranchName();
    }

    private void setPermissionsVisibilty() {
        if(record.getRole() == 9) addUserBinding.externalAgentEligibilityRl.setVisibility(View.VISIBLE);
        else addUserBinding.externalAgentEligibilityRl.setVisibility(View.GONE);
        switch(record.getRole()) {
            case 6:
            case 8:
            case 10:
            case 11:
                addUserBinding.worklogEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.overtimeEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.orderEligibilityRl.setVisibility(View.GONE);
                addUserBinding.externalAgentEligibilityRl.setVisibility(View.GONE);
                addUserBinding.complaintEligibilityRl.setVisibility(View.GONE);
                addUserBinding.crmEligibilityRl.setVisibility(View.GONE);
                break;
            case 9:
                addUserBinding.worklogEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.overtimeEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.orderEligibilityRl.setVisibility(View.GONE);
                addUserBinding.externalAgentEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.complaintEligibilityRl.setVisibility(View.GONE);
                addUserBinding.crmEligibilityRl.setVisibility(View.GONE);
                break;
            case 13:
                addUserBinding.worklogEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.overtimeEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.orderEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.externalAgentEligibilityRl.setVisibility(View.GONE);
                addUserBinding.complaintEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.crmEligibilityRl.setVisibility(View.VISIBLE);
                break;
            case 2:
                addUserBinding.worklogEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.overtimeEligibilityRl.setVisibility(View.VISIBLE);
                addUserBinding.orderEligibilityRl.setVisibility(View.GONE);
                addUserBinding.externalAgentEligibilityRl.setVisibility(View.GONE);
                addUserBinding.complaintEligibilityRl.setVisibility(View.GONE);
                addUserBinding.crmEligibilityRl.setVisibility(View.GONE);
//                TODO add full access to cms as admin_rights
                break;
            default:
                addUserBinding.worklogEligibilityRl.setVisibility(View.GONE);
                addUserBinding.overtimeEligibilityRl.setVisibility(View.GONE);
                addUserBinding.orderEligibilityRl.setVisibility(View.GONE);
                addUserBinding.externalAgentEligibilityRl.setVisibility(View.GONE);
                addUserBinding.complaintEligibilityRl.setVisibility(View.GONE);
                addUserBinding.crmEligibilityRl.setVisibility(View.GONE);
                break;

        }
    }

    private void updateUserPermissions(String permission, String isEligible) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("user_id", String.valueOf(record.getId()));
        map1.put("eligible_for", permission);
        map1.put("eligible", isEligible);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.SETELIGIBLEUSER, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_SETELIGIBLEUSER, map, false);
    }
    private void callGetUserProfileApi(String id) {
        loggedinUser = false;
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, false);
    }
    private void checkVendorRole() {
        if(SELECTEDROLE.equalsIgnoreCase("3") || SELECTEDROLE.equalsIgnoreCase("4")) {
            if(SELECTEDROLE.equalsIgnoreCase("4")) {
                addUserBinding.vendorCompanyTitle.setVisibility(View.VISIBLE);
                addUserBinding.vendorCompanyETTitle.setVisibility(View.GONE);
            } else {
                addUserBinding.vendorCompanyTitle.setVisibility(View.GONE);
                addUserBinding.vendorCompanyETTitle.setVisibility(View.VISIBLE);
            }
            addUserBinding.vendorCodeTitle.setVisibility(View.VISIBLE);
        } else {
            addUserBinding.vendorCompanyTitle.setVisibility(View.GONE);
            addUserBinding.vendorCompanyETTitle.setVisibility(View.GONE);
            addUserBinding.vendorCodeTitle.setVisibility(View.GONE);
        }
    }
    private void enableVendorCodeEditable() {
        if(SELECTEDROLE.equalsIgnoreCase("3")) {
            addUserBinding.vendorCodeTitle.setEnabled(true);
        } else addUserBinding.vendorCodeTitle.setEnabled(false);
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
                                            imageLink = listData;
                                            ArrayList<String> imgs = new ArrayList<>();
                                            imgs.add(listData);
                                            Images images = new Images();
                                            images.setLeadAttachment(imgs);
                                            imagesArrayList.add(images);
                                            imageAdapter.refreshList(imagesArrayList);
                                            addUserBinding.profileLl.setVisibility(View.GONE);
                                            addUserBinding.uploadFileRv.setVisibility(View.VISIBLE);
                                        } else {
                                            Common.showToast(AddUserActivity.this, "Please upload a file smaller than 2 MB.");
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

    private void setupAdapter(List<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        addUserBinding.uploadFileRv.setHasFixedSize(true);
        addUserBinding.uploadFileRv.setLayoutManager(new GridLayoutManager(this, 3));
        addUserBinding.uploadFileRv.setAdapter(imageAdapter);
        adapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, branchList, true);
        addUserBinding.selectBranch.setAdapter(adapter);

        roleAdapter=new CustomAutoCompleteListAdapter(this,R.layout.dropdown_item_list, R.id.title_tv, roleList, true);
        addUserBinding.selectRole.setAdapter(roleAdapter);

        companyAdapter=new CustomAutoCompleteListAdapter(this,R.layout.dropdown_item_list, R.id.title_tv, companyList, true);
        addUserBinding.selectVendorCompany.setAdapter(companyAdapter);
    }

    private void callApi() {
        if (addUserBinding.firstNameET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, "First name should not be empty.");
            return;
        }else if (addUserBinding.lastNameET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, "Last name should not be empty.");
            return;
        }else if (addUserBinding.mobilenoET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, "Please enter the valid phone number.");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("first_name", addUserBinding.firstNameET.getText().toString().trim());
        map.put("last_name", addUserBinding.lastNameET.getText().toString().trim());
        map.put("user_email", addUserBinding.emailET.getText().toString().trim());
        map.put("phone_number", addUserBinding.mobilenoET.getText().toString().trim());
        map.put("date_of_birth", addUserBinding.dobET.getText().toString().trim());
        map.put("date_of_joining", addUserBinding.dojET.getText().toString().trim());
        map.put("profile_image", Profile_image);
        map.put("role", SELECTEDROLE);

        String branchId="";
        for (int i = 0; i < branchList.size(); i++) {
            if(branchList.get(i).isSelected()){
                branchId=branchList.get(i).getId();
                break;
            }
        }
        map.put("branch", branchId);
        if(!getIntent().getStringExtra("title").equalsIgnoreCase("Edit User")) {
            if (SELECTEDROLE.equalsIgnoreCase("4")) {
                if(Common.userRoleId.equalsIgnoreCase("3")) {
                    map.put("vendor_company_admin_id", utils.getStringFromPreference(PreferenceUtils.USERID, "").toString());
                } else {
                    int companySelected = -1;
                    for (int i = 0; i < companyList.size(); i++) {
                        if (companyList.get(i).isSelected()) {
                            companySelected = i;
                            break;
                        }
                    }
                    if (companySelected < 0) {
                        Common.showToast(this, "Please select vendor company");
                        return;
                    }
                    map.put("vendor_company_admin_id", companyList.get(companySelected).getId());
                }
            } if(SELECTEDROLE.equalsIgnoreCase("3")) {
                if(addUserBinding.vendorCompanyET.getText() == null || addUserBinding.vendorCompanyET.getText().toString().equalsIgnoreCase("")) {
                    Common.showToast(this, "Please enter vendor company");
                    return;
                }
                if(addUserBinding.vendorCodeET.getText() == null || addUserBinding.vendorCodeET.getText().toString().equalsIgnoreCase("")) {
                    Common.showToast(this, "Please enter vendor code");
                    return;
                }
                map.put("vendor_company_name", addUserBinding.vendorCompanyET.getText().toString());
                map.put("vendor_code", addUserBinding.vendorCodeET.getText().toString());
            }
        }
        map.put("allowed_devices", addUserBinding.allowedDevicesET.getText().toString());
        if (record != null) {
            map.put("id", String.valueOf(record.getId()));
            updateUserDetails(new Gson().toJson(map));
        } else createUser(new Gson().toJson(map));
    }

    private void createUser(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CREATEUSER, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CREATEUSER, map, true);
    }

    private void updateUserDetails(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.UPDATEUSER, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEUSER, map, true);
    }

    private void uploadAttachment(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("file", imageLink);
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_UPLOADPROFILEIMAGE, map, true);
    }

    private void setData(RecordsItem record) {
        addUserBinding.dojET.setText(record.getDateOfJoining());
        addUserBinding.dobET.setText(record.getDateOfBirth());
        addUserBinding.selectRole.setText(record.getRoleName());
        addUserBinding.selectVendorCompany.setText(record.getVendorCompanyName());
        addUserBinding.emailET.setText(record.getUserEmail());
        addUserBinding.mobilenoET.setText(record.getPhoneNumber());
        addUserBinding.firstNameET.setText(record.getFirstName());
        addUserBinding.lastNameET.setText(record.getLastName());
        addUserBinding.statusLl.setVisibility(View.VISIBLE);
        addUserBinding.statusTv.setText(Common.getProperCase(record.getUserStatus()));
        if(record.getUserStatus().equalsIgnoreCase("active")){
            addUserBinding.statusIv.setImageResource(R.drawable.green_dot);
        }else if(record.getUserStatus().equalsIgnoreCase("added")){
            addUserBinding.statusIv.setImageResource(R.drawable.grey_dot);
        }else{
            addUserBinding.statusIv.setImageResource(R.drawable.red_dot);
        }
        SELECTEDROLE=String.valueOf(record.getRole());
        checkVendorRole();
        if(SELECTEDROLE.equalsIgnoreCase("3") && record.getVendorCode().equalsIgnoreCase("")) {
            enableVendorCodeEditable();
        }
        addUserBinding.vendorCodeET.setText(record.getVendorCode());
        addUserBinding.allowedDevicesET.setText(record.getAllowedDevices());
        Common.showLog("PROFILE IMAGE======"+record.getProfileImage());
        if (record.getProfileImage() != null && !record.getProfileImage().isEmpty()) {
            Profile_image=record.getProfileImage();
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(record.getProfileImage());
            Images images = new Images();
            images.setId(record.getId());
            images.setLeadAttachment(imgs);
            imagesArrayList.add(images);
            addUserBinding.profileLl.setVisibility(View.GONE);
            addUserBinding.uploadFileRv.setVisibility(View.VISIBLE);
        }
        if(!record.getRoleName().equalsIgnoreCase("Vendor User")){
            getRoles();
        }
    }
    private void setPermissionToggles(DataItem latestRecord) {
        addUserBinding.complaintToggleBtn.setChecked(latestRecord.getEligibleForCMS() == 1);
        addUserBinding.crmToggleBtn.setChecked(latestRecord.getEligibleForCRM() == 1);
        addUserBinding.orderToggleBtn.setChecked(latestRecord.getEligibleForOrderListing() == 1);
        addUserBinding.worklogToggleBtn.setChecked(latestRecord.getEligibleForWorklog() == 1);
        addUserBinding.overtimeToggleBtn.setChecked(latestRecord.getEligibleForOvertime() == 1);
        addUserBinding.externalAgentToggleBtn.setChecked(latestRecord.getAskPayment() == 1);
//        TODO add condition of admin rights key
//        addUserBinding.complaintToggleBtn.setChecked(latestRecord.getEligibleForCMS() == 1);
        setData(record);
    }
    private void getUserProfile(String id) {
        loggedinUser = true;
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, false);
    }
    private void getVendorCompanyList() {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("dropdown_data", "vendor_code");
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETPRODUCTDETAILS, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETPRODUCTDETAILS, map, false);
    }
    private void getRoles() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETROLES, null, false);
    }
    private void getBranchName() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETBRANCHNAME, null, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                if(!loggedinUser) {
                    response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                    latestRecord = response.getData().get(0);
                    setPermissionToggles(latestRecord);
//                    AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
//                    record = allOwnersData.getData().getRecords().get(0);
//                    setData(record);
                } else {
                    response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                    if (response.getData().get(0).getVendorCompanyName() != null) {
                        addUserBinding.selectVendorCompany.setText(response.getData().get(0).getVendorCompanyName().toString());
                        addUserBinding.vendorCodeET.setText(response.getData().get(0).getVendorCode().toString());
                    }
                    if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) {
                        getRoles();
                    }
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETROLES) {
                JSONObject jsonObject = null;
                roleList=new ArrayList<>();
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
                    for (int i = 0; i < records.length(); i++) {
                        JSONObject record1 = records.getJSONObject(i);
                        String roleName = record1.getString("role_name");
                        String id = record1.getString("roleId");
                        com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem recordsItem = new com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem();
                        recordsItem.setCityName(roleName);
                        recordsItem.setId(id);
                        if (record != null) {
                            if (String.valueOf(record.getRole()).equalsIgnoreCase(id)) {
                                recordsItem.setSelected(true);
                                addUserBinding.selectRole.setText(record.getRoleName());
                            }
                        }
                        roleList.add(recordsItem);
                    }
                    addUserBinding.roleTil.setEnabled(true);
                    roleAdapter.refreshList(roleList);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETBRANCHNAME) {
                BranchResponse branchResponse=new Gson().fromJson(responseDO.getResponse(), BranchResponse.class);
                branchList = new ArrayList<>();
                for (int i = 0; i < branchResponse.getData().size(); i++) {
                    com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem recordsItem = new com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem();
                    recordsItem.setCityName(branchResponse.getData().get(i).getBranchName());
                    recordsItem.setId(String.valueOf(branchResponse.getData().get(i).getId()));
                    if (record != null) {
                        if (record.getBranch().equalsIgnoreCase(String.valueOf(branchResponse.getData().get(i).getId()))) {
                            recordsItem.setSelected(true);
                            addUserBinding.selectBranch.setText(branchResponse.getData().get(i).getBranchName());
                        }
                    }
                    branchList.add(recordsItem);
                }
                adapter.refreshList(branchList);
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETPRODUCTDETAILS) {
                JSONObject jsonObject = null;
                companyList=new ArrayList<>();
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
                    for (int i = 0; i < records.length(); i++) {
                        JSONObject record1 = records.getJSONObject(i);
                        String vendorCompanyName = record1.getString("vendor_company_name");
                        String vendorCode = record1.getString("vendor_code");
                        String id = record1.getString("id");
                        com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem recordsItem = new com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem();
                        recordsItem.setCityName(vendorCompanyName);
                        recordsItem.setStateName(vendorCode);
                        recordsItem.setId(id);
                        if (record != null) {
                            if (String.valueOf(record.getVendorCompanyName()).equalsIgnoreCase(vendorCompanyName)) {
                                recordsItem.setSelected(true);
                                addUserBinding.selectVendorCompany.setText(record.getVendorCompanyName());
                                addUserBinding.vendorCodeET.setText(record.getVendorCode());
                            }
                        }
                        companyList.add(recordsItem);
                    }
                    addUserBinding.vendorCompanyTitle.setEnabled(true);
                    companyAdapter.refreshList(companyList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATEUSER) {
                if (responseDO.getCode() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseDO.getResponse());
                        Common.showToast(this, jsonObject.optString("message"));
                        if (!imageLink.equalsIgnoreCase("")) {
                            uploadAttachment(jsonObject.getJSONObject("data").getString("user_id"));
                        } else {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else Common.showToast(this, responseDO.getResponse().toString());

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEUSER) {
                if (responseDO.getCode() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseDO.getResponse());
                        Common.showToast(this, jsonObject.optString("message"));
                        if (!imageLink.equalsIgnoreCase("")) {
                            uploadAttachment(String.valueOf(record.getId()));
                        } else {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else Common.showToast(this, responseDO.getResponse().toString());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_SETELIGIBLEUSER) {
                if (responseDO.getCode() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseDO.getResponse());
                        Common.showToast(this, jsonObject.optString("message"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else Common.showToast(this, responseDO.getResponse().toString());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADPROFILEIMAGE) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (responseDO.getCode() == 403)
                Common.showToast(this, responseDO.getResponse());
        } else Common.showToast(this, responseDO.getResponse());
    }

    @Override
    public void removeImageListner(int position) {
        if(Profile_image.equalsIgnoreCase(imagesArrayList.get(position).getLeadAttachment().get(0))){
            Profile_image="";
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
        addUserBinding.profileLl.setVisibility(View.VISIBLE);
        addUserBinding.uploadFileRv.setVisibility(View.GONE);

    }
}