package com.devstringx.mytylesstockcheck.screens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.CustomerPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.CustomerPickerDeialog;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.StateCityPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.StateCityPickerDeialog;
import com.devstringx.mytylesstockcheck.databinding.ActivityAddLeadBinding;
import com.devstringx.mytylesstockcheck.datamodal.FilePickerData;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.DataResponse;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.requirenmentData.RecordItem;
import com.devstringx.mytylesstockcheck.datamodal.requirenmentData.RequirenmentData;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddLeadActivity extends AppCompatActivity implements ResponseListner, UploadFileAdapter.removeImage {
    ActivityAddLeadBinding addLeadBinding;
    private List<String> selectleadTicketSizeList = new ArrayList<>();
    private List<String> leadTypeList = new ArrayList<>();
    private List<String> leadSourceList = new ArrayList<>();
    List<String> selectedItems = new ArrayList<>();
    private List<RecordItem> requirenmentList = new ArrayList<>();
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    private Record record = null;
    private List<RecordsItem> cityList = new ArrayList<>();
    private List<RecordsItem> stateList = new ArrayList<>();
    private String assignOwnerId, cityid, stateId;
    private CustomAutoCompleteListAdapter  assignOwnerAdapter;
    private static final int PICK_FILE_REQUEST_CODE = 1;

    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };
    private ArrayList<String> SELECTEDFILES=new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList=new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList=new ArrayList<>();
    private NetworkRequest.DProgressbar loaderUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLeadBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lead);
        addLeadBinding.titleTv.setText(getIntent().getStringExtra("title"));
        assignOwnerId=""; cityid=""; stateId="";
        loaderUtils=new NetworkRequest.DProgressbar(this);
        if (getIntent().getStringExtra("title").equalsIgnoreCase("Edit Customer")) {
            record = (Record) getIntent().getSerializableExtra("data");
            Common.showLog(record.getFullName());
            setData(record);
        } else if (getIntent().getStringExtra("title").equalsIgnoreCase("Edit Lead")) {
            record = (Record) getIntent().getSerializableExtra("data");
            Common.showLog(record.getFullName());
            setData(record);
        }
        addLeadBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addLeadBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addAllLeadTypes();
        addAllLeadSource();
        addAllSelectLeadTicketSize();
        getAllOwners();
        getRequirenments();
//        cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, cityList, true);
//        stateAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, stateList, false);
        assignOwnerAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, allOwnersList, true);
        addLeadBinding.assignOwner.setAdapter(assignOwnerAdapter);
        addLeadBinding.leadSource.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, leadSourceList));
        addLeadBinding.leadType.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, leadTypeList));
        addLeadBinding.selectLeadTicketSize.setAdapter(new AutoCompleteAdapter(this,R.layout.dropdown_item_list,R.id.title_tv,selectleadTicketSizeList));
        addLeadBinding.requirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequirenmentsDialog();
            }
        });
//        addLeadBinding.selectCity.setAdapter(cityAdapter);
//        addLeadBinding.selectState.setAdapter(stateAdapter);
        addLeadBinding.assignOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < allOwnersList.size(); j++) {
                    allOwnersList.get(j).setSelected(false);
                }
                allOwnersList.get(i).setSelected(true);
            }
        });
        addLeadBinding.selectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateCityPickerDeialog pickerDeialog=new StateCityPickerDeialog(AddLeadActivity.this, stateList, false, new StateCityPickerCallbacks() {
                    @Override
                    public void onStateCitySelected(RecordsItem recordsItem) {
                        for (int i = 0; i < stateList.size(); i++) {
                            stateList.get(i).setSelected(false);
                            if(recordsItem.getId().equalsIgnoreCase(stateList.get(i).getId())){
                                stateList.get(i).setSelected(true);
                            }
                        }
                        stateId=recordsItem.getId();
                        addLeadBinding.selectState.setText(recordsItem.getStateName());
                        addLeadBinding.selectCity.setText("");
                        cityid="";
                        getCities(recordsItem.getId());
                        Common.showLog("stateid===="+recordsItem.getId());
                    }
                });
                pickerDeialog.show();
            }
        });
        addLeadBinding.selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateCityPickerDeialog pickerDeialog=new StateCityPickerDeialog(AddLeadActivity.this, cityList, true, new StateCityPickerCallbacks() {
                    @Override
                    public void onStateCitySelected(RecordsItem recordsItem) {
                        for (int j = 0; j < cityList.size(); j++) {
                            cityList.get(j).setSelected(false);
                            if(recordsItem.getId().equalsIgnoreCase(cityList.get(j).getId())){
                                cityList.get(j).setSelected(true);
                            }
                        }
                        cityid=recordsItem.getId();
                        addLeadBinding.selectCity.setText(recordsItem.getCityName());
                        Common.showLog("cityid===="+recordsItem.getId());
                    }
                });
                pickerDeialog.show();
            }
        });

        addLeadBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
            }
        });
        addLeadBinding.chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("application/pdf");
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(AddLeadActivity.this)
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

        if(Common.userProfileRole.equalsIgnoreCase("Admin") ||
                Common.userProfileRole.equalsIgnoreCase("Employee") &&
                        Common.userProfilEligibleForCRM != 0)
            addLeadBinding.selectLeadTicketSizeTIL.setVisibility(View.VISIBLE);
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
                                                addLeadBinding.uploadFileRv.setVisibility(View.VISIBLE);
                                        } else {
                                            Common.showToast(AddLeadActivity.this, "Please upload a file smaller than 2 MB.");
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

    private void setData(Record record) {
        addLeadBinding.fullNameET.setText(record.getFullName());
        addLeadBinding.mobilenoET.setText(record.getPrimaryPhone());
        addLeadBinding.alternateMobileET.setText(record.getSecondaryPhone());
        addLeadBinding.emailET.setText(record.getEmail() == null ? "" : record.getEmail().isEmpty() ? "" : record.getEmail());
        addLeadBinding.leadType.setText(record.getLeadType());
        addLeadBinding.leadSource.setText(record.getLeadSource());
        addLeadBinding.assignOwner.setText(record.getLeadOwnerName());
        addLeadBinding.selectLeadTicketSize.setText(record.getTicketSize());
        assignOwnerId= String.valueOf(record.getLeadOwnerId());
        if (record.getBillingAddress()!=null) {
            stateId= String.valueOf(record.getBillingAddress().billing_state_id);
            cityid= String.valueOf(record.getBillingAddress().billing_city_id);
            addLeadBinding.address1ET.setText(record.getBillingAddress().getAddress_line_1());
            addLeadBinding.address2ET.setText(record.getBillingAddress().getAddress_line_2());
            addLeadBinding.selectState.setText(record.getBillingAddress().getBilling_state());
            addLeadBinding.selectCity.setText(record.getBillingAddress().getBilling_city());
            addLeadBinding.enterGst.setText(record.getBillingAddress().getGst_number());
            addLeadBinding.zipcodeET.setText(String.valueOf(record.getBillingAddress().getBilling_pincode()));
            getCities(stateId);
        }
        addLeadBinding.note.setText(record.getNotes());
        String string = "";
        for (int j = 0; j < record.getRequirements().size(); j++) {
            string += record.getRequirements().get(j).getRequirement() + ", ";
        }
        if (string.length() > 2) {
            addLeadBinding.requirement.setText(string.substring(0, string.length() - 2));
        } else {
            addLeadBinding.requirement.setText("");
        }

        if(record.getImages().size()>0){
            addLeadBinding.uploadFileRv.setVisibility(View.VISIBLE);
            imagesArrayList= (ArrayList<Images>) record.getImages();

        }else{
            addLeadBinding.uploadFileRv.setVisibility(View.GONE);
        }

    }

    private void getAllOwners() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role", "");
            jsonObject.put("user_status", "active");
            jsonObject.put("screen", "Lead");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERFORSCREEN, map, false);
    }

    private void getRequirenments() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETREQUIRENMENTS, null, false);
    }

    private void getCities(String stateId) {
        String data = "{" +
                " \"state_id\":"+stateId +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CITIESDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETCITIES, map, false);
    }
    private void uploadAttachment(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, SELECTEDFILES);
        map.put("id", id);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPLOADATTACHMENT, map, true);
    }

    private void deleteAttachment() {
            String[] ids=new String[removedImagesArrayList.size()];
        for (int i = 0; i < removedImagesArrayList.size(); i++) {
            ids[i]= String.valueOf(removedImagesArrayList.get(i).getId());
        }
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", ids);
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put(NKeys.QUOTATIONDATA, new Gson().toJson(map));
            Common.showLog("=====IDS===="+new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_DELETEATTACHMENT, map1, true);


    }

    private void getStates() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETSTATES, null, false);
    }

    private void callAPI() {
        if (addLeadBinding.fullNameET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.fullname_error_msg));
            return;
        } else if (addLeadBinding.mobilenoET.getText().toString().trim().isEmpty() || addLeadBinding.mobilenoET.getText().toString().trim().length() <= 9) {
            Common.showToast(this, getString(R.string.mobileno_error_msg));
            return;
        } else if (addLeadBinding.requirement.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.requirenment_error_msg));
            return;
        } else if (addLeadBinding.leadType.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.leadtype_error_msg));
            return;
        } else if (addLeadBinding.leadSource.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.leadSource_error_msg));
            return;
        } else if (addLeadBinding.assignOwner.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.leadowner_error_msg));
            return;
        } else  if(Common.userProfileRole.equalsIgnoreCase("Admin") ||
                Common.userProfileRole.equalsIgnoreCase("Employee") &&
                        Common.userProfilEligibleForCRM != 0) {
            if (addLeadBinding.selectLeadTicketSize.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.select_lead_ticket_size_error_msg));
                return;
            }
        }
        else if (!addLeadBinding.address1ET.getText().toString().trim().isEmpty() || !addLeadBinding.address2ET.getText().toString().trim().isEmpty()
                || !addLeadBinding.selectCity.getText().toString().trim().isEmpty() || !addLeadBinding.selectState.getText().toString().trim().isEmpty() ||
                !addLeadBinding.zipcodeET.getText().toString().trim().isEmpty() || !addLeadBinding.enterGst.getText().toString().trim().isEmpty()) {
            if (addLeadBinding.address1ET.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.addressline1_error_msg));
                return;
            } else if (addLeadBinding.address2ET.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.addressline2_error_msg));
                return;
            } else if (addLeadBinding.selectCity.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.city_error_msg));
                return;
            } else if (addLeadBinding.selectState.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.state_error_msg));
                return;
            } else if (addLeadBinding.zipcodeET.getText().toString().trim().isEmpty()) {
                Common.showToast(this, getString(R.string.zipcode_error_msg));
                return;
            }
        }
        else if (addLeadBinding.note.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.note_error_msg));
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("full_name", addLeadBinding.fullNameET.getText().toString().trim());
        map.put("primary_phone", addLeadBinding.mobilenoET.getText().toString().trim());
        map.put("secondary_phone", addLeadBinding.alternateMobileET.getText().toString().trim());
        map.put("email", addLeadBinding.emailET.getText().toString().trim());
        map.put("lead_type", addLeadBinding.leadType.getText().toString().trim());
        map.put("lead_source", addLeadBinding.leadSource.getText().toString().trim());
        map.put("notes", addLeadBinding.note.getText().toString().trim());
        ArrayList<Integer> seleRequ = new ArrayList<>();
        for (int j = 0; j < requirenmentList.size(); j++) {
            if (requirenmentList.get(j).isSelected()) {
                seleRequ.add(requirenmentList.get(j).getId());
            }
        }
        map.put("requirements", seleRequ);
        cityid="";stateId="";
        for (int j = 0; j < cityList.size(); j++) {
            if (cityList.get(j).isSelected()) {
                cityid = cityList.get(j).getId();
                break;
            }
        }

        for (int j = 0; j < stateList.size(); j++) {
            if (stateList.get(j).isSelected()) {
                stateId = stateList.get(j).getId();
                break;
            }
        }
        for (int j = 0; j < allOwnersList.size(); j++) {
            if (allOwnersList.get(j).isSelected()) {
                assignOwnerId = allOwnersList.get(j).getId();
                break;
            }
        }
        Common.showLog("CITYID==" + cityid + "==" + stateId + "===" + assignOwnerId);
        if (assignOwnerId.isEmpty() || assignOwnerId==null) return;
        if (cityid.isEmpty() && stateId.isEmpty() && addLeadBinding.address1ET.getText().toString().trim().isEmpty()
                && addLeadBinding.address2ET.getText().toString().trim().isEmpty()
                && addLeadBinding.zipcodeET.getText().toString().trim().isEmpty()
                && addLeadBinding.enterGst.getText().toString().trim().isEmpty())
        { map.put("billing_address",map1); }
        else {
            if (stateId==null || cityid==null) {
                return;
            }
            if (record!=null){
                map1.put("city_id", cityid);
                map1.put("state_id", stateId);
            }else{
                map1.put("city", cityid);
                map1.put("state", stateId);
            }
            map1.put("pincode", addLeadBinding.zipcodeET.getText().toString().trim());
            map1.put("gst_number", addLeadBinding.enterGst.getText().toString().trim());
            map1.put("address_line_1", addLeadBinding.address1ET.getText().toString().trim());
            map1.put("address_line_2", addLeadBinding.address2ET.getText().toString().trim());
            map.put("billing_address",map1);
        }
        map.put("lead_owner_id", Integer.parseInt(assignOwnerId));
        map.put("lead_owner", Integer.parseInt(assignOwnerId));
        map.put("ticket_size",addLeadBinding.selectLeadTicketSize.getText().toString().trim());

        if (record != null) {
            map.put("id", record.getLeadId());
            Common.showLog("RECORDDATA===" + new Gson().toJson(map));
            HashMap<String, Object> data = new HashMap<>();
            data.put(NKeys.LEADSDATA, new Gson().toJson(map));
            Common.showLog("map========"+data+"");
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATELEAD, data, true);
        } else {
            Common.showLog("RECORDDATA===" + new Gson().toJson(map));
            HashMap<String, Object> data = new HashMap<>();
            data.put(NKeys.LEADSDATA, new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CREATELEAD, data, true);
        }

    }

    private void addAllLeadTypes() {
        leadTypeList.add("Call");
        leadTypeList.add("Walkin");
        leadTypeList.add("Website Signups");
    }

    private void addAllLeadSource() {
        leadSourceList.add("Website");
        leadSourceList.add("Google Business");
        leadSourceList.add("Interior/Architect Ref");
        leadSourceList.add("Friend Referral");
        leadSourceList.add("Instagram");
        leadSourceList.add("Offline");
        leadSourceList.add("Web Signups");
        leadSourceList.add("Zopim Chat");
    }

    private void addAllSelectLeadTicketSize() {
        selectleadTicketSizeList.add("Low");
        selectleadTicketSizeList.add("Medium");
        selectleadTicketSizeList.add("High");
        selectleadTicketSizeList.add("Unknown");
    }

    private void setupAdapter(List<Images> list) {
         imageAdapter = new UploadFileAdapter(this,list,this);
        addLeadBinding.uploadFileRv.setHasFixedSize(true);
        addLeadBinding.uploadFileRv.setLayoutManager(new GridLayoutManager(this, 3));
        addLeadBinding.uploadFileRv.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void showRequirenmentsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddLeadActivity.this);
        builder.setTitle("Select Requirements");
        builder.setCancelable(false);

        String[] reqArray = new String[requirenmentList.size()];
        boolean[] selecteditem = new boolean[requirenmentList.size()];
        for (int i = 0; i < requirenmentList.size(); i++) {
            reqArray[i] = requirenmentList.get(i).getRequirement();
            selecteditem[i] = requirenmentList.get(i).isSelected();
        }
        builder.setMultiChoiceItems(reqArray, selecteditem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    requirenmentList.get(i).setSelected(true);
                } else {
                    requirenmentList.get(i).setSelected(false);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                String string = "";
                for (int j = 0; j < requirenmentList.size(); j++) {
                    // concat array value
                    if (requirenmentList.get(j).isSelected()) {
                        string += requirenmentList.get(j).getRequirement() + ", ";
                    }
                }
                if (string.length() > 2) {
                    addLeadBinding.requirement.setText(string.substring(0, string.length() - 2));
                } else {
                    addLeadBinding.requirement.setText("");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        // show dialog
        builder.show();
    }


    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETREQUIRENMENTS) {
                RequirenmentData requirenmentData = new Gson().fromJson(responseDO.getResponse(), RequirenmentData.class);
                requirenmentList = requirenmentData.getData().getRecord();
                if (record != null) {
                    for (int i = 0; i < record.getRequirements().size(); i++) {
                        for (int j = 0; j < requirenmentList.size(); j++) {
                            if (record.getRequirements().get(i).getMasterRequirementId() == requirenmentList.get(j).getId()) {
                                requirenmentList.get(j).setSelected(true);
                                break;
                            }
                        }
                    }
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERFORSCREEN) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    if (record != null) {
                        if (record.getLeadOwnerId() == allOwnersData.getData().getRecords().get(i).getId()) {
                            recordsItem.setSelected(true);
                            Common.showLog("SELECTED ASSIGNID===" + allOwnersData.getData().getRecords().get(i).getId());
                        }
                    }
                    allOwnersList.add(recordsItem);
                }
                if(allOwnersList.size()==1){
                    allOwnersList.get(0).setSelected(true);
                    addLeadBinding.assignOwner.setText(allOwnersList.get(0).getCityName());
                }
                assignOwnerAdapter.refreshList(allOwnersList);
                getStates();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCITIES) {
                DataResponse cityDataResponse = new Gson().fromJson(responseDO.getResponse(), DataResponse.class);
                cityList = cityDataResponse.getData().getRecords();
                if (record != null && record.getBillingAddress()!=null) {
                    for (int i = 0; i < cityList.size(); i++) {
                        if (String.valueOf(record.getBillingAddress().getBilling_city_id()).equalsIgnoreCase(cityList.get(i).getId())) {
                            cityList.get(i).setSelected(true);
                            Common.showLog("SELECTED CITYID===" + cityList.get(i).getId());
                        }
                    }
                }
                loaderUtils.dismiss();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETSTATES) {
                DataResponse stateDataResponse = new Gson().fromJson(responseDO.getResponse(), DataResponse.class);
                stateList = stateDataResponse.getData().getRecords();
                if (record != null && record.getBillingAddress()!=null) {
                    for (int i = 0; i < stateList.size(); i++) {
                        if (String.valueOf(record.getBillingAddress().billing_state_id).equalsIgnoreCase(stateList.get(i).getId())) {
                            stateList.get(i).setSelected(true);
                            Common.showLog("SELECTED STATEID===" + stateList.get(i).getId());
                        }
                    }
                }
//                getCities(stateId);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATELEAD ||
                    responseDO.getServiceMethods() == ServiceMethods.WS_UPDATELEAD) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    String leadid="";
                    if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATELEAD) {
                        leadid=jsonObject.optJSONObject("data").getString("lead_id");
                    }else{
                        leadid= String.valueOf(record.getLeadId());
                    }
//                    Common.showToast(AddLeadActivity.this,jsonObject.optString("message"));
                    if(SELECTEDFILES.size()>0) {
                        uploadAttachment(leadid);
                    }else{
                        if(removedImagesArrayList.size()==0) {
                            finish();
                        }else{
                            deleteAttachment();
                        }
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADATTACHMENT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        if(removedImagesArrayList.size()==0) {
                            Common.showToast(AddLeadActivity.this,jsonObject.optString("message"));
                            finish();
                        }else{
                            deleteAttachment();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEATTACHMENT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(AddLeadActivity.this,jsonObject.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            loaderUtils.dismiss();
            Common.showToast(this,responseDO.getResponse());
        }
    }
    @Override
    public void removeImageListner(int position) {
        if(imagesArrayList.get(position).getId()!=0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        }else{
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
    }
}