package com.devstringx.mytylesstockcheck.screens.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.LeadHistoryAdapter;
import com.devstringx.mytylesstockcheck.adapter.NotificationMainDashboardAdapter;
import com.devstringx.mytylesstockcheck.adapter.TaskListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ChangeTaskStatusDialogBinding;
import com.devstringx.mytylesstockcheck.databinding.CreateTaskDialogBinding;
import com.devstringx.mytylesstockcheck.databinding.FragmentUpcomingTasksBinding;
import com.devstringx.mytylesstockcheck.databinding.LeadDetailPageBinding;
import com.devstringx.mytylesstockcheck.datamodal.LeadHistory.ResponseLeadHistory;
import com.devstringx.mytylesstockcheck.datamodal.allLeadTask.ResponseTask;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.LeadDetailsData;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Requirenments;
import com.devstringx.mytylesstockcheck.datamodal.leadstage.Response;
import com.devstringx.mytylesstockcheck.datamodal.taskNotification.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.taskNotification.TaskNotificationResponse;
import com.devstringx.mytylesstockcheck.interfaces.CreateLeadActivity;
import com.devstringx.mytylesstockcheck.interfaces.LeadTask;
import com.devstringx.mytylesstockcheck.interfaces.UpdateLeadStage;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpcomingTasksFragment extends Fragment implements ResponseListner, NotificationMainDashboardAdapter.NotificationUpdate, UpdateLeadStage, CreateLeadActivity, LeadTask ,TaskListAdapter.TaskItemsBtn{
    private static final int REQUESTCODE = 2000;
    private FragmentUpcomingTasksBinding binding;
    private NotificationMainDashboardAdapter adapter;
    LeadDetailPageBinding leadDetailPageBinding;
    private int id;
    private List<String> leadStageList = new ArrayList<>();
    private List<String> leadActivityList = new ArrayList<>();
    TaskListAdapter taskAdapter;
    LeadHistoryAdapter leadHistoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_upcoming_tasks, container, false);
        View view=binding.getRoot();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
            }
        });
        getLeadStage();
        getActivityType();
        return view;
    }

    private void notificationMarkAsRead(ArrayList<String> id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id",id);
        map1.put("action","unread");
        map1.put("notification_type","task");
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DATA, new Gson().toJson(map1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATENOTIFICATION, map, false);
    }
    private void getLeadTaskByLeadId(int id) {
        this.id=id;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lead_id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETLEADTASKBYLEADID, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETLEADTASKBYLEADID, map, true);
    }
    private void getLeadHistoryByLeadId(int id) {
        this.id=id;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lead_id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETLEADHISTORYBYLEADID, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETLEADHISTORYBYLEADID, map, true);
    }

    private void getLeadStage() {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETLEADSTAGE, null, true);
    }
    private void getActivityType() {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETACTIVITYTYPE, null, true);
    }
    @Override
    public void onResume() {
        super.onResume();
        getNotification(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
    }

    private void getNotification(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("user_id",id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DATA, new Gson().toJson(map1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETNOTIFICATIONLISTINGFORTASK, map, true);
    }
    private void getLeadDetails(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.LEADDETAILSDATA, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_LEADDETAILS, map, true);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        binding.refreshLayout.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETNOTIFICATIONLISTINGFORTASK) {
                TaskNotificationResponse taskNotificationResponse = new Gson().fromJson(responseDO.getResponse(), TaskNotificationResponse.class);
                adapter = new NotificationMainDashboardAdapter(getActivity(), (ArrayList<RecordsItem>) taskNotificationResponse.getData().getRecords(),UpcomingTasksFragment.this);
                binding.multiNotificationRv.setHasFixedSize(true);
                binding.multiNotificationRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.multiNotificationRv.setAdapter(adapter);
                if(taskNotificationResponse.getData().getRecords().size()==0){
                    binding.noTaskRecordTv.setText(getString(R.string.no_task_record));
                    binding.noTaskRecordTv.setVisibility(View.VISIBLE);
                }else{
                    binding.noTaskRecordTv.setVisibility(View.GONE);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETLEADSTAGE) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                leadStageList = response.getData().getRecord();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATELEADSTAGE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETLEADTASK) {
                ResponseTask response = new Gson().fromJson(responseDO.getResponse(), ResponseTask.class);
                taskAdapter.refreshList(response.getData().getRecords(),this::onBtnClickListner);
            }
            else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATELEADTASK) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    getLeadTaskByLeadId(id);
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATELEADACTIVITY) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_LEADDETAILS) {
                LeadDetailsData response = new Gson().fromJson(responseDO.getResponse(), LeadDetailsData.class);
                loadLeadDetails(response.getData().getRecord());
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETLEADTASKBYLEADID) {
                ResponseTask response = new Gson().fromJson(responseDO.getResponse(), ResponseTask.class);
                taskAdapter.refreshList(response.getData().getRecords(),this::onBtnClickListner);
                if (response.getData().getCount()==0) {
                    leadDetailPageBinding.taskByLeadId.noTaskRecordTv.setVisibility(View.VISIBLE);
                }else{
                    leadDetailPageBinding.taskByLeadId.noTaskRecordTv.setVisibility(View.GONE);
                }
            }
            else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETLEADHISTORYBYLEADID) {
                ResponseLeadHistory response = new Gson().fromJson(responseDO.getResponse(), ResponseLeadHistory.class);
                leadHistoryAdapter.refreshList(response.getData().getRecord());
                if (response.getData().getRecord().size()==0) {
                    leadDetailPageBinding.leadHistoryLl.noRecordTv.setVisibility(View.VISIBLE);
                }else{
                    leadDetailPageBinding.leadHistoryLl.noRecordTv.setVisibility(View.GONE);
                }
            }
            else  if (responseDO.getServiceMethods() == ServiceMethods.WS_GETACTIVITYTYPE) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                leadActivityList = response.getData().getRecord();
                Common.showLog("leadActivity" +leadActivityList);
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETMARKTASKASCOMPLETED) {
                try {
                    JSONObject jsonObject=new JSONObject(responseDO.getResponse());
                    getLeadTaskByLeadId(id);
                    Common.showToast(getActivity(),jsonObject.optString("message",""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETRESCHEDULETASK) {
                try {
                    JSONObject jsonObject=new JSONObject(responseDO.getResponse());
                    Common.showToast(getActivity(),jsonObject.optString("message",""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getLeadTaskByLeadId(id);
            }else{
                getNotification(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
            }
        }
    }

    @Override
    public void updateNotifationListner(int position) {
        if(adapter.getmRecordsItems().get(position).getIsRead().equalsIgnoreCase("0")){
            ArrayList<String> ids=new ArrayList<>();
            ids.add(String.valueOf(adapter.getmRecordsItems().get(position).getId()));
            notificationMarkAsRead(ids);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", adapter.getmRecordsItems().get(position).getLeadId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        getLeadDetails(jsonObject.toString());
    }


    @SuppressLint("SuspiciousIndentation")
    private void loadLeadDetails(Record record) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        leadDetailPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.lead_detail_page, null, false);

        bottomSheetDialog.setContentView(leadDetailPageBinding.getRoot());
        bottomSheetDialog.show();
        leadDetailPageBinding.detailsTv.setSelected(true);
        setSelectedTab(2);
        leadDetailPageBinding.historyTv.setSelected(false);
        leadDetailPageBinding.taskTv.setSelected(false);

        UploadFileAdapter imageAdapter = new UploadFileAdapter(getActivity(),record.getImages(),null);
        leadDetailPageBinding.leadDetails.uploadFileRv.setHasFixedSize(true);
        leadDetailPageBinding.leadDetails.uploadFileRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        leadDetailPageBinding.leadDetails.uploadFileRv.setAdapter(imageAdapter);
        if (leadDetailPageBinding.detailsTv.isSelected())
            leadDetailPageBinding.leadDetailsSs.setVisibility(View.VISIBLE);
        else leadDetailPageBinding.leadDetailsSs.setVisibility(View.GONE);

        leadDetailPageBinding.leadName.setText(record.getFullName());
        if (record.getActivity()!=null) leadDetailPageBinding.activityType.setText(record.getActivity().getActivityType());
        else leadDetailPageBinding.activityType.setText("No lead activity available");
        if (!record.getLeadCreatedDate().isEmpty())
            leadDetailPageBinding.dateTv.setText(Common.convertYYYYMMMdd(record.getLeadCreatedDate()));
        leadDetailPageBinding.ownerName.setText(record.getLeadOwnerName());
        leadDetailPageBinding.sourceName.setText("Via " + record.getLeadSource());
        leadDetailPageBinding.leadStage.setText(record.getLeadStage());
        if (record.getBillingAddress()!=null) {
            leadDetailPageBinding.addressTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location,0,0,0);
            leadDetailPageBinding.addressTv.setText(record.getBillingAddress().getBilling_city() + " , " + record.getBillingAddress().getBilling_state());
        }
        else leadDetailPageBinding.addressTv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        leadDetailPageBinding.leadDetails.nameTV.setText(record.getFullName());
        leadDetailPageBinding.leadDetails.mobileTV.setText(String.valueOf(record.getPrimaryPhone()));
        leadDetailPageBinding.leadDetails.alternateMobileTV.setText(String.valueOf(record.getSecondaryPhone()));
        leadDetailPageBinding.leadDetails.leadTypeTV.setText(record.getLeadType());
        leadDetailPageBinding.leadDetails.leadSourceTv.setText(record.getLeadSource());
        leadDetailPageBinding.leadDetails.leadOwnerTv.setText(record.getLeadOwnerName());
        leadDetailPageBinding.leadDetails.emailTv.setText(record.getEmail());
        if (record.getBillingAddress()!=null) {
            leadDetailPageBinding.leadDetails.addressLine1Tv.setText(record.getBillingAddress().getAddress_line_1());
            leadDetailPageBinding.leadDetails.addressLine2Tv.setText(record.getBillingAddress().getAddress_line_2());
            leadDetailPageBinding.leadDetails.enterZipcodeTv.setText(String.valueOf(record.getBillingAddress().getBilling_pincode()));
            leadDetailPageBinding.leadDetails.gstET.setText(record.getBillingAddress().getGst_number());
            leadDetailPageBinding.leadDetails.cityTv.setText(record.getBillingAddress().getBilling_city());
            leadDetailPageBinding.leadDetails.stateTv.setText(record.getBillingAddress().getBilling_state());
        }
        leadDetailPageBinding.leadDetails.notesTv.setText(record.getNotes());
        leadDetailPageBinding.leadDetails.requirementTV.setText(getAllRequirenments(record.getRequirements()));
        leadDetailPageBinding.addActivityLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddActivityDialog(String.valueOf(record.getLeadId()), bottomSheetDialog);
            }
        });
        leadDetailPageBinding.addTaskLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog(record);
            }
        });
        leadDetailPageBinding.leadStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.updateLeadStageDialog(getContext(), String.valueOf(record.getLeadId()), record.getLeadStage(), leadStageList, UpcomingTasksFragment.this, bottomSheetDialog);
            }
        });
        leadDetailPageBinding.detailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedTab(2);
                leadDetailPageBinding.detailsTv.setSelected(true);
                leadDetailPageBinding.historyTv.setSelected(false);
                leadDetailPageBinding.taskTv.setSelected(false);
                leadDetailPageBinding.taskByLeadId.taskByLeadId.setVisibility(View.GONE);
                leadDetailPageBinding.leadHistoryLl.leadHistoryLl.setVisibility(View.GONE);
                if (leadDetailPageBinding.detailsTv.isSelected()){
                    leadDetailPageBinding.leadDetailsSs.setVisibility(View.VISIBLE);
                }
                else{
                    leadDetailPageBinding.leadDetailsSs.setVisibility(View.GONE);
                }

            }
        });
        if(!Common.getPermission(getActivity(),"ML","EL")){
            leadDetailPageBinding.leadDetails.leadEdit.setVisibility(View.GONE);
        }
        leadDetailPageBinding.leadDetails.leadEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddLeadActivity.class);
                intent.putExtra("title", "Edit Lead");
                intent.putExtra("data",record);
                startActivityForResult(intent, REQUESTCODE);
                bottomSheetDialog.dismiss();
            }
        });
        leadDetailPageBinding.historyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leadDetailPageBinding.historyTv.isSelected())return;
                // Toggle the selected state
                setSelectedTab(1);
                leadDetailPageBinding.historyTv.setSelected(true);
                leadDetailPageBinding.leadDetailsSs.setVisibility(View.GONE);
                leadDetailPageBinding.taskByLeadId.taskByLeadId.setVisibility(View.GONE);
                leadDetailPageBinding.detailsTv.setSelected(false);
                leadDetailPageBinding.taskTv.setSelected(false);
                if (leadDetailPageBinding.historyTv.isSelected()){
                    getLeadHistoryByLeadId(record.getLeadId());
                    setupLeadHistoryAdapter(v);
                    leadDetailPageBinding.leadHistoryLl.leadHistoryLl.setVisibility(View.VISIBLE);

                }else {
                    leadDetailPageBinding.leadHistoryLl.leadHistoryLl.setVisibility(View.GONE);
                }
            }
        });
        leadDetailPageBinding.taskTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leadDetailPageBinding.historyTv.isSelected())return;
                setSelectedTab(3);
                leadDetailPageBinding.taskTv.setSelected(true);
                leadDetailPageBinding.leadDetailsSs.setVisibility(View.GONE);
                leadDetailPageBinding.leadHistoryLl.leadHistoryLl.setVisibility(View.GONE);
                leadDetailPageBinding.historyTv.setSelected(false);
                leadDetailPageBinding.detailsTv.setSelected(false);
                if (leadDetailPageBinding.taskTv.isSelected()){
                    getLeadTaskByLeadId(record.getLeadId());
                    setupTaskByLeadIdAdapter(v);
                    leadDetailPageBinding.taskByLeadId.taskByLeadId.setVisibility(View.VISIBLE);

                }else{
                    leadDetailPageBinding.taskByLeadId.taskByLeadId.setVisibility(View.GONE);
                }
            }
        });
        leadDetailPageBinding.icCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        leadDetailPageBinding.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), leadDetailPageBinding.threeDots);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.three_dots_pop_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.edit_lead) {
                            Intent intent = new Intent(getActivity(), AddLeadActivity.class);
                            intent.putExtra("title", "Edit Lead");
                            intent.putExtra("data", record);
                            startActivityForResult(intent, REQUESTCODE);
                            bottomSheetDialog.dismiss();
                        } else if (menuItem.getItemId() == R.id.add_task) {
                            showAddTaskDialog(record);
                        } else {
                            Common.updateLeadStageDialog(getActivity(), String.valueOf(record.getLeadId()), record.getLeadStage(), leadStageList, UpcomingTasksFragment.this, bottomSheetDialog);
                        }
                        return true;
                    }
                });
                Menu menu = popupMenu.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    Common.applyFontToMenuItem(getActivity(),mi);
                }
                // Showing the popup menu
                popupMenu.show();
            }
        });

        leadDetailPageBinding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openWhatsApp(getActivity(),record.getPrimaryPhone(),"Hi");
            }
        });
        leadDetailPageBinding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openDialerPad(getActivity(),record.getPrimaryPhone());
            }
        });
    }

    private void setSelectedTab(int i) {
        if(i==1){
            leadDetailPageBinding.v1.setBackgroundResource(R.drawable.orange_round_bg50);
            leadDetailPageBinding.v2.setBackgroundResource(R.color.transparent);
            leadDetailPageBinding.v3.setBackgroundResource(R.color.transparent);
        }else if(i==2){
            leadDetailPageBinding.v1.setBackgroundResource(R.color.transparent);
            leadDetailPageBinding.v2.setBackgroundResource(R.drawable.orange_round_bg50);
            leadDetailPageBinding.v3.setBackgroundResource(R.color.transparent);
        }else if(i==3){
            leadDetailPageBinding.v1.setBackgroundResource(R.color.transparent);
            leadDetailPageBinding.v2.setBackgroundResource(R.color.transparent);
            leadDetailPageBinding.v3.setBackgroundResource(R.drawable.orange_round_bg50);
        }
    }
    private void showAddTaskDialog(Record record){
        Dialog dialog = new Dialog(getContext());
        CreateTaskDialogBinding createTaskDialogBinding=DataBindingUtil.inflate(getLayoutInflater(),R.layout.create_task_dialog,null , false);
        dialog.setContentView(createTaskDialogBinding.getRoot());
        dialog.setCancelable(false);
        createTaskDialogBinding.reminderIv.setSelected(true);
        createTaskDialogBinding.subject.setText("Follow up with "+record.getFullName());
        if (createTaskDialogBinding.reminderIv.isSelected()) createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_selected);
        else createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_unselected);
        createTaskDialogBinding.reminderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!createTaskDialogBinding.reminderIv.isSelected()){
                    createTaskDialogBinding.reminderIv.setSelected(true);
                    createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_selected);
                }
                else {
                    createTaskDialogBinding.reminderIv.setSelected(false);
                    createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_unselected);

                }
            }
        });
        createTaskDialogBinding.scheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),createTaskDialogBinding.scheduleDate);
            }
        });
        createTaskDialogBinding.scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openTimePicker(getActivity(),createTaskDialogBinding.scheduleTime);
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        Button add = dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!createTaskDialogBinding.subject.getText().toString().isEmpty() && !createTaskDialogBinding.taskDetails.getText().toString().isEmpty() && !createTaskDialogBinding.scheduleDate.getText().toString().isEmpty() && !createTaskDialogBinding.scheduleTime.getText().toString().isEmpty()) {
                    createTaskListner(String.valueOf(record.getLeadId()), createTaskDialogBinding.scheduleDate.getText().toString().trim(), createTaskDialogBinding.scheduleTime.getText().toString().trim(), createTaskDialogBinding.subject.getText().toString().trim(), createTaskDialogBinding.taskDetails.getText().toString().trim(), createTaskDialogBinding.reminderIv.isSelected());
                    Common.showLog(createTaskDialogBinding.reminderIv.isSelected() + "");
                    dialog.dismiss();
                }
                else Toast.makeText(getActivity(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
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
    private void showRescheduleTaskDialog(com.devstringx.mytylesstockcheck.datamodal.allLeadTask.RecordsItem record){
        Dialog dialog = new Dialog(getContext());
        CreateTaskDialogBinding createTaskDialogBinding=DataBindingUtil.inflate(getLayoutInflater(),R.layout.create_task_dialog,null , false);
        dialog.setContentView(createTaskDialogBinding.getRoot());
        dialog.setCancelable(false);
        createTaskDialogBinding.titleTv.setText("Reschedule Task");
        createTaskDialogBinding.subject.setText(record.getSubject());
        createTaskDialogBinding.reminderIv.setSelected(true);
        if (createTaskDialogBinding.reminderIv.isSelected()) createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_selected);
        else createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_unselected);
        createTaskDialogBinding.reminderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!createTaskDialogBinding.reminderIv.isSelected()){
                    createTaskDialogBinding.reminderIv.setSelected(true);
                    createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_selected);
                }
                else {
                    createTaskDialogBinding.reminderIv.setSelected(false);
                    createTaskDialogBinding.reminderIv.setImageResource(R.drawable.orange_checkbox_unselected);

                }
            }
        });
        createTaskDialogBinding.scheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),createTaskDialogBinding.scheduleDate);
            }
        });
        createTaskDialogBinding.scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openTimePicker(getActivity(),createTaskDialogBinding.scheduleTime);
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        Button add = dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!createTaskDialogBinding.subject.getText().toString().isEmpty() && !createTaskDialogBinding.taskDetails.getText().toString().isEmpty() && !createTaskDialogBinding.scheduleDate.getText().toString().isEmpty() && !createTaskDialogBinding.scheduleTime.getText().toString().isEmpty()) {
                    rescheduleTaskListner(record.getId(), createTaskDialogBinding.scheduleDate.getText().toString().trim(), createTaskDialogBinding.scheduleTime.getText().toString().trim(), createTaskDialogBinding.subject.getText().toString().trim(), createTaskDialogBinding.taskDetails.getText().toString().trim(), createTaskDialogBinding.reminderIv.isSelected());
                    Common.showLog(createTaskDialogBinding.reminderIv.isSelected() + "");
                    dialog.dismiss();
                }
                else Toast.makeText(getActivity(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
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

    private void showAddActivityDialog(String leadId, BottomSheetDialog bottomSheetDialog) {
        AutoCompleteAdapter adapter;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_task_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        AutoCompleteTextView autoCompleteTxt = dialog.findViewById(R.id.activity_type_auto_complete_txt);
        EditText notes = dialog.findViewById(R.id.noteET);
        adapter = new AutoCompleteAdapter(getContext(), R.layout.dropdown_item_list, R.id.title_tv, leadActivityList);
        autoCompleteTxt.setAdapter(adapter);
        autoCompleteTxt.setThreshold(1);
        autoCompleteTxt.setTextColor(Color.BLACK);
        Button add = dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoCompleteTxt.getText().toString().isEmpty()||notes.getText().toString().trim().isEmpty()){
                    Common.showToast(getActivity(),"Please select Activity Type and Add Note");
                    return;
                }
                dialog.dismiss();
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
                addActivityTaskListner(leadId, autoCompleteTxt.getText().toString(), notes.getText().toString().trim());
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

    private String getAllRequirenments(List<Requirenments> data) {
        String requirenmentsData = "";
        if (data.size()!=0) {
            for (int i = 0; i < data.size(); i++) {
                requirenmentsData += data.get(i).getRequirement() + " , ";
            }
            return requirenmentsData.substring(0, requirenmentsData.length() - 2);
        }
        else return "";
    }

    private void setupTaskByLeadIdAdapter(View view) {
        taskAdapter = new TaskListAdapter();
        this.leadDetailPageBinding.taskByLeadId.taskByLeadIdRv.setHasFixedSize(true);
        this.leadDetailPageBinding.taskByLeadId.taskByLeadIdRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.leadDetailPageBinding.taskByLeadId.taskByLeadIdRv.setAdapter(taskAdapter);
    }
    private void setupLeadHistoryAdapter(View view) {
        leadHistoryAdapter = new LeadHistoryAdapter();
        this.leadDetailPageBinding.leadHistoryLl.leadHistoryRv.setHasFixedSize(true);
        this.leadDetailPageBinding.leadHistoryLl.leadHistoryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.leadDetailPageBinding.leadHistoryLl.leadHistoryRv.setAdapter(leadHistoryAdapter);
    }
    @Override
    public void addActivityTaskListner(String id, String activityType, String notes) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lead_id", id);
            jsonObject.put("activity_type", activityType);
            jsonObject.put("notes", notes);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Common.showLog("RESPONSE===" + jsonObject.toString());
        getCreateLeadActivity(jsonObject.toString());
    }

    @Override
    public void createTaskListner(String id, String follow_up_date, String follow_up_time, String subject, String task_details, boolean reminder) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lead_id",id);
            jsonObject.put("follow_up_date", follow_up_date);
            jsonObject.put("follow_up_time", follow_up_time);
            jsonObject.put("subject", subject);
            jsonObject.put("task_details", task_details);
            jsonObject.put("reminder", reminder);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Common.showLog("RESPONSE===" + jsonObject.toString());
        getCreateTask(jsonObject.toString());
    }

    @Override
    public void rescheduleTaskListner(int id, String follow_up_date, String follow_up_time, String subject, String task_details, boolean reminder) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("task_id",id);
            jsonObject.put("follow_up_date", follow_up_date);
            jsonObject.put("follow_up_time", follow_up_time);
            jsonObject.put("subject", subject);
            jsonObject.put("task_details", task_details);
            jsonObject.put("reminder", reminder);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Common.showLog("RESPONSE===" + jsonObject.toString());
        getRescheduleTask(jsonObject.toString());
    }
    private void getCreateTask(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CREATELEADTASK, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_CREATELEADTASK, map, true);
    }
    private void getCreateLeadActivity(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CREATELEADACTIVITY, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_CREATELEADACTIVITY, map, true);
    }
    private void getRescheduleTask(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETRESCHEDULETASK, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETRESCHEDULETASK, map, true);
    }
    private void getUpdateLeadStage(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.UPDATELEADSTAGEDATA, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATELEADSTAGE, map, true);
    }

    @Override
    public void upldateLeadStageListner(String id, String StageName, String comment) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("lead_stage", StageName);
            jsonObject.put("comment", comment);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Common.showLog("RESPONSE===" + jsonObject.toString());
        getUpdateLeadStage(jsonObject.toString());
    }

    @Override
    public void upldateLeadStageListnerLost(String id, String StageName, String comment, List<String> stringList) {

    }

    private void showChangeStatusDialog(int id) {
        Dialog dialog = new Dialog(getContext());
        ChangeTaskStatusDialogBinding changeTaskStatusDialogBinding=DataBindingUtil.inflate(getLayoutInflater(),R.layout.change_task_status_dialog,null , false);
        dialog.setContentView(changeTaskStatusDialogBinding.getRoot());
        dialog.setCancelable(false);
        changeTaskStatusDialogBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        changeTaskStatusDialogBinding.yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTaskMarkAsCompleted(id);
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();

    }
    private void getTaskMarkAsCompleted(int id) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("task_id",id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETMARKTASKASCOMPLETED, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETMARKTASKASCOMPLETED, map, true);    }


    @Override
    public void onBtnClickListner(int position, int id, String type, com.devstringx.mytylesstockcheck.datamodal.allLeadTask.RecordsItem recordsItem) {
        if (type.equalsIgnoreCase("taskStatus")) showChangeStatusDialog(id);
        else if(!recordsItem.getStatus().equalsIgnoreCase("Completed"))showRescheduleTaskDialog(recordsItem);
    }
}