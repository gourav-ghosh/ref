package com.devstringx.mytylesstockcheck.screens.taskreminder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityTaskDetailBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity implements TaskRemindersListAdapter.OnTaskReminderItemClick, ResponseListner, UploadFileAdapter.ViewFile {
    ActivityTaskDetailBinding binding;
    private UploadFileAdapter imageAdapter;
    int taskId;
    List<ResponseMediasItem> responseMedias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        taskId = getIntent().getIntExtra("taskId", 0);
        handleClickListners();
        setupAdapter();
        fetchTaskDetails();
    }


    private void setupAdapter() {

        imageAdapter = new UploadFileAdapter(this, "response", responseMedias, this);
        binding.uploadImgRv.setHasFixedSize(true);
        binding.uploadImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.uploadImgRv.setAdapter(imageAdapter);
    }


    private void handleClickListners() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTask();
            }
        });

    }

    private HashMap<String, Object> getReqMap() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("taskId", taskId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, jsonObject.toString());
        return map;
    }

    private void fetchTaskDetails() {


        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GET_TASK_DETAILS, getReqMap(), false);
    }

    private void completeTask() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_COMPLETE_TASK, getReqMap(), true);
    }

    private void setupData(TaskDetailsResponseDO.Records records) {
        binding.soNumber.setText(records.getSaleOrderNo());
        binding.tvDate.setText(records.getScheduleDate());
        binding.tvTime.setText(records.getScheduleTime());
        binding.reminderTime.setText(records.getReminderTiming());
        String commnet = records.getComment();
        String[] split = commnet.split(".\\n");
        String assignedTo = split[0];
        binding.assignTo.setText(assignedTo.split(":")[1]);
        binding.etComment.setText(split[1]);
        //Media
        responseMedias = records.getMedias();
        imageAdapter.refreshMedia(responseMedias);

        if (records.getStatus().equalsIgnoreCase("not_completed")) {
            binding.btnSubmit.setText("Complete Task");
            binding.btnSubmit.setClickable(true);
        } else {
            binding.btnSubmit.setText("Completed");
            binding.btnSubmit.setClickable(false);
        }
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_TASK_DETAILS) {
                TaskDetailsResponseDO taskDetailsResponseDO = new Gson().fromJson(responseDO.getResponse(), TaskDetailsResponseDO.class);
                TaskDetailsResponseDO.Data data = taskDetailsResponseDO.getData();
                if (data != null && data.getRecords() != null) {
                    setupData(data.getRecords());
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_COMPLETE_TASK) {
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
            }
        }


    }

    @Override
    public void onItemClick(TaskReminderItem item) {

    }

    @Override
    public void viewFileListener(List<ComplaintMediasItem> complaintMediasItemList, List<ResponseMediasItem> responseMediasItemList, String type) {
        resolveFileViewDialog(responseMediasItemList);
    }

    private void resolveFileViewDialog(List<ResponseMediasItem> list) {
        Dialog dialog = new Dialog(this);
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
            if (list.get(i).isSelected())
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