package com.devstringx.mytylesstockcheck.screens.taskreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NewInquiryAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityTaskReminderBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderDetailsResponse;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.fragments.PdfPOandSOFragment;
import com.devstringx.mytylesstockcheck.screens.myProfile.MyProfileActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskReminderListActivity extends AppCompatActivity implements TaskRemindersListAdapter.OnTaskReminderItemClick, ResponseListner, UploadFileAdapter.removeImage {
    ActivityTaskReminderBinding binding;
    private TaskRemindersListAdapter adapter;
    private List<TaskReminderItem> lsItems;
    private static final int CREATE_TASK = 2342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_reminder);
        handleClickListners();
        setupTaskRemindersAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllTask();
    }

    private void handleClickListners() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTaskReminder();
            }
        });
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTask();
            }
        });
    }

    private void createNewTaskReminder() {
        Intent intent = new Intent(this, CreateNewTaskReminderActivity.class);
        startActivityForResult(intent, CREATE_TASK);
    }

    private void setupTaskRemindersAdapter() {
        adapter = new TaskRemindersListAdapter(this, this);
        binding.taskRemindersListRv.setHasFixedSize(true);
        binding.taskRemindersListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.taskRemindersListRv.setAdapter(adapter);
    }


    @Override
    public void removeImageListner(int position) {

    }

    private void getAllTask() {
        HashMap<String, Object> map = new HashMap<>();
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GET_ALL_TASK, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (binding.refreshLl.isRefreshing())
            binding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_ALL_TASK) {
                AllTaskResponseDO response = new Gson().fromJson(responseDO.getResponse(), AllTaskResponseDO.class);
                if (response.getData() != null)
                    lsItems = response.getData().getRecords();
                else lsItems = null;
                if (lsItems != null && lsItems.size() > 0) {
                    binding.taskRemindersListRv.setVisibility(View.VISIBLE);
                    binding.noTaskRecordTv.setVisibility(View.GONE);
                    adapter.refreshList(lsItems);
                } else {
                    binding.taskRemindersListRv.setVisibility(View.GONE);
                    binding.noTaskRecordTv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemClick(TaskReminderItem item) {
        Intent intent = new Intent(TaskReminderListActivity.this, TaskDetailActivity.class);
        intent.putExtra("taskId", item.getId());
        startActivityForResult(intent, CREATE_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_TASK && RESULT_OK == resultCode) {
            getAllTask();
        }
    }
}
