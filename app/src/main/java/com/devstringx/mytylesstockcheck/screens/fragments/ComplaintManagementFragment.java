package com.devstringx.mytylesstockcheck.screens.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ComplaintCommentAdapter;
import com.devstringx.mytylesstockcheck.adapter.ComplaintListingAdapter;
import com.devstringx.mytylesstockcheck.adapter.ShippingChargesItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplaintManagementBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.ComplaintCommentResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.AllComplaintResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.StatusCounts;
import com.devstringx.mytylesstockcheck.screens.ComplaintDetailsActivity;
import com.devstringx.mytylesstockcheck.screens.CreateComplainActivity;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.screens.complaintFIlter.ComplaintFilterActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplaintManagementFragment extends Fragment implements ResponseListner, ComplaintListingAdapter.OnBtnClickListener {
    FragmentComplaintManagementBinding complaintBinding;
    ComplaintListingAdapter adapter;
    View view1;
    private String currentStatus = "Created";
    private RecyclerView commentsRV;
    private RelativeLayout noCommentTV;
    private TextView commentET;
    private String complaintID;
    private ComplaintCommentAdapter complaintCommentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        complaintBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_complaint_management, container, false);
        view1 = complaintBinding.getRoot();
        if (!Common.getPermission(getContext(), "CMS", "ACMS")) {
            complaintBinding.createComplaint.setVisibility(View.GONE);
        }
        complaintBinding.newTab.setSelected(true);
        if (complaintBinding.newTab.isSelected()) setHashmapData();
        complaintBinding.createComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CreateComplainActivity.class), 100);
            }
        });
        complaintBinding.filterComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ComplaintFilterActivity.class);
                intent.putExtra("selectedTab", currentStatus);
                startActivityForResult(intent, 200);
            }
        });
        complaintBinding.exportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportComplaints();
            }
        });
        complaintBinding.newTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.newTab.isSelected()) {
                    complaintBinding.newTab.setSelected(true);
                    complaintBinding.allTab.setSelected(false);
                    complaintBinding.cancelledTab.setSelected(false);
                    complaintBinding.delayedTab.setSelected(false);
                    complaintBinding.resolvedTab.setSelected(false);
                    complaintBinding.inprogressTab.setSelected(false);
                    currentStatus = "Created";
                    setHashmapData();
                }
            }
        });
        complaintBinding.inprogressTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.inprogressTab.isSelected()) {
                    complaintBinding.newTab.setSelected(false);
                    complaintBinding.allTab.setSelected(false);
                    complaintBinding.cancelledTab.setSelected(false);
                    complaintBinding.delayedTab.setSelected(false);
                    complaintBinding.resolvedTab.setSelected(false);
                    complaintBinding.inprogressTab.setSelected(true);
                    currentStatus = "Inprogress";
                    setHashmapData();
                }
            }
        });
        complaintBinding.delayedTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.delayedTab.isSelected()) {
                    complaintBinding.newTab.setSelected(false);
                    complaintBinding.allTab.setSelected(false);
                    complaintBinding.cancelledTab.setSelected(false);
                    complaintBinding.delayedTab.setSelected(true);
                    complaintBinding.resolvedTab.setSelected(false);
                    complaintBinding.inprogressTab.setSelected(false);
                    currentStatus = "Delayed";
                    setHashmapData();
                }
            }
        });
        complaintBinding.allTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.allTab.isSelected()) {
                    complaintBinding.newTab.setSelected(false);
                    complaintBinding.allTab.setSelected(true);
                    complaintBinding.cancelledTab.setSelected(false);
                    complaintBinding.delayedTab.setSelected(false);
                    complaintBinding.resolvedTab.setSelected(false);
                    complaintBinding.inprogressTab.setSelected(false);
                    currentStatus = "All";
                    setHashmapData();
                }
            }
        });
        complaintBinding.resolvedTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.resolvedTab.isSelected()) {
                    complaintBinding.newTab.setSelected(false);
                    complaintBinding.allTab.setSelected(false);
                    complaintBinding.cancelledTab.setSelected(false);
                    complaintBinding.delayedTab.setSelected(false);
                    complaintBinding.resolvedTab.setSelected(true);
                    complaintBinding.inprogressTab.setSelected(false);
                    currentStatus = "Resolved";
                    setHashmapData();
                }
            }
        });
        complaintBinding.cancelledTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!complaintBinding.cancelledTab.isSelected()) {
                    complaintBinding.newTab.setSelected(false);
                    complaintBinding.allTab.setSelected(false);
                    complaintBinding.cancelledTab.setSelected(true);
                    complaintBinding.delayedTab.setSelected(false);
                    complaintBinding.resolvedTab.setSelected(false);
                    complaintBinding.inprogressTab.setSelected(false);
                    currentStatus = "Cancelled";
                    setHashmapData();
                }
            }
        });
        complaintBinding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setHashmapData();
            }
        });

        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            complaintBinding.exportComplaint.setVisibility(View.INVISIBLE);
        }
        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "SCE", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            complaintBinding.exportComplaint.setVisibility(View.VISIBLE);
        }

        return view1;
    }

    private void exportComplaints() {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        if (complaintBinding.searchET.getText().toString()!=null) map.put("search", complaintBinding.searchET.getText().toString());
        map.put("sort",Common.complaintSortBy);
        map.put("dateRange", Common.complaintDateType);
        map.put("fromDate", Common.complaintStartDate);
        map.put("toDate", Common.complaintEndDate);
        map.put("complaintType", Common.selectedComplaintType);
        map.put("supportExecutive", Common.selectedComplaintSupExeName);
        map.put("saleExecutive",Common.selectedComplaintSalesExeName);
        map.put("manager", Common.selectedComplaintDisName);
        map.put("issueForm", Common.selectedIssueFrom);
        map.put("statusTab", currentStatus);
        map1.put(NKeys.EXPORTCOMPLAINTS, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_EXPORTCOMPLAINTS, map1, true);
    }

    private void setHashmapData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        if (complaintBinding.searchET.getText().toString() != null)
            map.put("search", complaintBinding.searchET.getText().toString());
        else map.put("search", "");
        map.put("sort", Common.complaintSortBy);
        map.put("dateRange", Common.complaintDateType);
        map.put("fromDate", Common.complaintStartDate);
        map.put("toDate", Common.complaintEndDate);
        map.put("complaintType", Common.selectedComplaintType);
        map.put("supportExecutive", Common.selectedComplaintSupExeName);
        map.put("saleExecutive", Common.selectedComplaintSalesExeName);
        map.put("manager", Common.selectedComplaintDisName);
        map.put("issueForm", Common.selectedIssueFrom);
        map.put("statusTab", currentStatus);
        getFilterAllComplaints(new Gson().toJson(map), false);
    }

    private void getAllComplaints() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLCOMPLAINTS, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLCOMPLAINTS, map, true);
    }

    private void setupComplaintListAdapter() {
        RecyclerView recyclerView = complaintBinding.complaintItemRV;
        adapter = new ComplaintListingAdapter(getContext(), this);
        complaintBinding.complaintItemRV.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            if (data != null) {
                String receivedHashMap = data.getStringExtra("ComplaintFilterDataMap");
                getFilterAllComplaints(receivedHashMap, true);
            }
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            clearComplaintFilter();
            getAllComplaints();
        }
    }

    private void clearComplaintFilter() {
        Common.complaintDateType = "";
        Common.complaintStartDate = "";
        Common.complaintEndDate = "";
        Common.complaintSortBy = "createdAtDesc";
        Common.selectedComplaintDisName=new ArrayList<>();
        Common.selectedComplaintSupExeName=new ArrayList<>();
        Common.selectedComplaintSalesExeName=new ArrayList<>();
        Common.selectedIssueFrom=new ArrayList<>();
        Common.selectedComplaintType=new ArrayList<>();
    }

    private void getFilterAllComplaints(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLCOMPLAINTS, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLCOMPLAINTS, map, isLoader);
    }

    private void showComplaintCommentDialog(String id) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.complaint_chat_dialog);
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        commentsRV = dialog.findViewById(R.id.comments_rv);
        ImageView send = dialog.findViewById(R.id.sendIV);
        commentET = dialog.findViewById(R.id.commentET);
        noCommentTV = dialog.findViewById(R.id.no_comment_tv);
        getComplaintCommentApi(id);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentET.getText().toString().isEmpty()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("complaint_id", id);
                    map.put("comment", commentET.getText().toString());
                    map.put(NKeys.CREATECOMPLAINTCOMMENT, new Gson().toJson(map));
                    callCreateComplaintCommentApi(map);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callCreateComplaintCommentApi(HashMap<String, Object> map) {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_CREATECOMPLAINTCOMMENT, map, true);
    }

    private void getComplaintCommentApi(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("complaint_id", id);
        map.put(NKeys.GETCOMPLAINTCOMMENT, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETCOMPLAINTCOMMENT, map, true);
    }

    private void setupCommentAdapter(Context context, List<com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.RecordsItem> records) {
        complaintCommentAdapter = new ComplaintCommentAdapter(context, records);
        commentsRV.setHasFixedSize(true);
        commentsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRV.setAdapter(complaintCommentAdapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLCOMPLAINTS) {
                AllComplaintResponse response = new Gson().fromJson(responseDO.getResponse(), AllComplaintResponse.class);
                setTabCount(response.getData().getStatusCounts());
                setupComplaintListAdapter();
                adapter.refreshList(response.getData().getRecords(), response.getData().getStatusTab());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCOMPLAINTCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getContext(), jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        ComplaintCommentResponse response = new Gson().fromJson(responseDO.getResponse(), ComplaintCommentResponse.class);
                        if (response.getData().getCount() == 0) {
                            noCommentTV.setVisibility(View.VISIBLE);
                            commentsRV.setVisibility(View.GONE);
                        } else {
                            noCommentTV.setVisibility(View.GONE);
                            commentsRV.setVisibility(View.VISIBLE);
                        }
                        setupCommentAdapter(getContext(), response.getData().getRecords());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATECOMPLAINTCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getContext(), jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        commentET.setText("");
                        getComplaintCommentApi(complaintID);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTCOMPLAINTS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optString("link")));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void setTabCount(StatusCounts statusCounts) {
        complaintBinding.createdCountTV.setText(String.valueOf(statusCounts.getCreated()));
        complaintBinding.inprogressCountTV.setText(String.valueOf(statusCounts.getInprogress()));
        complaintBinding.resolvedCountTV.setText(String.valueOf(statusCounts.getResolved()));
        complaintBinding.delayedCountTV.setText(String.valueOf(statusCounts.getDelayed()));
        complaintBinding.cancelledCountTV.setText(String.valueOf(statusCounts.getCancelled()));
        complaintBinding.allCountTV.setText(String.valueOf(statusCounts.getAll()));
    }

    @Override
    public void onClick(int position, String id) {
        Intent intent = new Intent(getActivity(), ComplaintDetailsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void getComplaintId(String id) {
        this.complaintID = id;
        showComplaintCommentDialog(id);
    }
}