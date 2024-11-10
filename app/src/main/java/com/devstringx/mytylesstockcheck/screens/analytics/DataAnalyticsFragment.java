package com.devstringx.mytylesstockcheck.screens.analytics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ComplaintCommentAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentDataAnalyticsBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.ComplaintCommentResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.AllComplaintResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.RecordsItem;
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

public class DataAnalyticsFragment extends Fragment implements ResponseListner {
    FragmentDataAnalyticsBinding dataAnalyticsBinding;
    DataAnalyticsAdapter adapter;
    List<DataAnalyticsResponse.Record> recordsItems = new ArrayList<>();
    View view1;
    private String currentStatus = "Created";
    private RecyclerView commentsRV;
    private RelativeLayout noCommentTV;
    private TextView commentET;
    private String complaintID;
    private ComplaintCommentAdapter complaintCommentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataAnalyticsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_analytics, container, false);
        view1 = dataAnalyticsBinding.getRoot();

        dataAnalyticsBinding.filterAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DataAnalyticFilterActivity.class);
                startActivityForResult(intent, 200);
            }
        });
        setupComplaintListAdapter();

        return view1;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAnalytics();
    }

    private void getAnalytics() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("sort", Common.analyticSortBy);
        map.put("dateRange", Common.analyticDateType);
        map.put("fromDate", Common.analyticStartDate);
        map.put("toDate", Common.analyticEndDate);
        map.put("role", TextUtils.isEmpty(Common.selectedRole)?"sales_person":Common.selectedRole);
        getFilterAllAnalytics(new Gson().toJson(map), true);
    }


    private void setupComplaintListAdapter() {
        RecyclerView recyclerView = dataAnalyticsBinding.analyticsRv;
        adapter = new DataAnalyticsAdapter(getContext(), recordsItems);
        dataAnalyticsBinding.analyticsRv.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (data!=null) {
                String receivedHashMap = data.getStringExtra("AnalyticFilterDataMap");
                getFilterAllAnalytics(receivedHashMap, true);
            }
        }
    }

    private void getFilterAllAnalytics(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GET_ANALYTICS, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GET_ANALYTICS, map, isLoader);
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
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_ANALYTICS) {
                DataAnalyticsResponse response = new Gson().fromJson(responseDO.getResponse(), DataAnalyticsResponse.class);
                if(response.getData()!=null && response.getData().getRecords()!=null) {
                    adapter.refreshList(response.getData().getRecords());
                }
            }
        }
    }


}