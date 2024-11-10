package com.devstringx.mytylesstockcheck.screens.taskreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityCreateNewTaskReminderBinding;
import com.devstringx.mytylesstockcheck.databinding.ActivityTaskReminderBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.DataResponse;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.requirenmentData.RequirenmentData;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.AddQuoteActivity;
import com.devstringx.mytylesstockcheck.screens.ProcessForDispatchActivity;
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

public class CreateNewTaskReminderActivity extends AppCompatActivity implements TaskRemindersListAdapter.OnTaskReminderItemClick, ResponseListner, UploadFileAdapter.removeImage {
    ActivityCreateNewTaskReminderBinding binding;
    private TaskRemindersListAdapter adapter;
    private List<TaskReminderItem> lsItems;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    private CustomAutoCompleteListAdapter cityAdapter;
    private List<String> timeSlotList = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private List<RecordsItem> delivery_agent_name_list;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_task_reminder);
        getDeliveryAgentList();
        handleClickListners();
        setupData();

    }

    private void setupData() {
        //With API
//        getAllOwners();
//        cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, delivery_agent_name_list, true);
//        binding.assignTo.setAdapter(cityAdapter);

        //Time Slots
        addAllTimeSlots();
        binding.reminderTime.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, timeSlotList));

        setupAdapter(imagesArrayList);
    }

    private void addAllTimeSlots() {
        timeSlotList.add("10 Min Earlier");
        timeSlotList.add("15 Min Earlier");
        timeSlotList.add("30 Min Earlier");
        timeSlotList.add("45 Min Earlier");
        timeSlotList.add("1 HR. Earlier");
    }
    private void getDeliveryAgentList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Delivery Agent");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }

    private void handleClickListners() {
        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openCalenderForUpcomingDates(CreateNewTaskReminderActivity.this, binding.tvDate);
            }
        });
        binding.tvDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openCalenderForUpcomingDates(CreateNewTaskReminderActivity.this, binding.tvDate);
            }
        });
        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openTimePicker(CreateNewTaskReminderActivity.this, binding.tvTime);
            }
        });
        binding.tvTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    Common.openTimePicker(CreateNewTaskReminderActivity.this, binding.tvTime);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
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
                        new FilePicker.Builder(CreateNewTaskReminderActivity.this)
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

    }

    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        if (binding.soNumber.getText().toString().trim().isEmpty()) {
            Common.showToast(this,"SO Number should not be empty");
            return;
        }
        else if (binding.assignTo.getText().toString().isEmpty()) {
            Common.showToast(this,"Assign to should not be empty");
            return;
        }
        else if (binding.tvDate.getText().toString().isEmpty()) {
            Common.showToast(this," Date should not be empty");
            return;
        }
        else if (binding.tvTime.getText().toString().isEmpty()) {
            Common.showToast(this,"Time should not be empty");
            return;
        }
        else if (binding.reminderTime.getText().toString().isEmpty()) {
            Common.showToast(this,"Reminder time should not be empty");
            return;
        }
        else if (binding.etComment.getText().toString().isEmpty()) {
            Common.showToast(this, "Comment should not be empty");
            return;
        }else if (SELECTEDFILES.isEmpty()) {
            Common.showToast(this, "Please at least add 1 attachment.");
            return;
        }

        map.put("so_number", binding.soNumber.getText().toString());
        map.put("assign_to", binding.assignTo.getText().toString());
        map.put("date", binding.tvDate.getText().toString());
        map.put("time", binding.tvTime.getText().toString());
        String remTime = binding.reminderTime.getText().toString();
        int reminderTime = 10;
        if (remTime.contains("HR")) {
            reminderTime = 60;
        } else {
            String[] split = remTime.split(" ");
            reminderTime = Integer.parseInt(split[0].trim().toString());
        }
        map.put("reminder_time", reminderTime);
        map.put("comment", binding.etComment.getText().toString());
        map.put("file", SELECTEDFILES);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ADD_TASK, map, true);
    }

    private void setupAdapter(ArrayList<Images> list) {
        imageAdapter = new UploadFileAdapter(this, list, this);
        binding.uploadImgRv.setHasFixedSize(true);
        binding.uploadImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.uploadImgRv.setAdapter(imageAdapter);
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
                                            binding.uploadImgRv.setVisibility(View.VISIBLE);
                                        } else {
                                            Common.showToast(CreateNewTaskReminderActivity.this, "Please upload a file smaller than 2 MB.");
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
    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if(responseDO.getServiceMethods() == ServiceMethods.WS_ADD_TASK){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                delivery_agent_name_list = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    delivery_agent_name_list.add(recordsItem);
                }
                cityAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, delivery_agent_name_list, true);
                binding.assignTo.setAdapter(cityAdapter);
            }
        }


    }

    @Override
    public void onItemClick(TaskReminderItem item) {

    }
}