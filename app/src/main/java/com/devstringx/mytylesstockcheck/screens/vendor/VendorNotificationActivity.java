package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NewInquiryAdapter;
import com.devstringx.mytylesstockcheck.adapter.VendorMultiNotificationAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityNotificationBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsResponse;
import com.devstringx.mytylesstockcheck.interfaces.OnItemClickListener;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.NotificationListActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VendorNotificationActivity extends AppCompatActivity implements OnItemClickListener, NewInquiryAdapter.SendResponse, ResponseListner {
    ActivityNotificationBinding notificationBinding;
    NewInquiryAdapter vendorMultiNotificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        setupNewInwuiryAdapter();
        notificationBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        notificationBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllInquiries("new_inquiry");
            }
        });
        getAllInquiries("new_inquiry");
    }
        private void setupNewInwuiryAdapter() {
        vendorMultiNotificationAdapter = new NewInquiryAdapter(this,this, this,true);
        notificationBinding.multiNotificationRv.setHasFixedSize(true);
        notificationBinding.multiNotificationRv.setLayoutManager(new LinearLayoutManager(this));
        notificationBinding.multiNotificationRv.setAdapter(vendorMultiNotificationAdapter);
    }
    @Override
    public void onItemClick(int position, String id) {
        Intent intent= new Intent(this, InquiryDetailActivity.class);
        intent.putExtra("background", false);
        intent.putExtra("inquiry_id",id);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra("background",false)) {
            Intent intent=new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }
    private void updateInquiryStatus(HashMap<String, Object> map) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.UPDATEQUOTATIONPRODUCTSTATUS,new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS, map1, true);
    }

    private void getAllInquiries(String tab) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("id", "");
        map.put("sort", "");
        map.put("action", "");
        map.put("quantity", "");
        map.put("enquiry_status", "");
        HashMap<String, Object> date = new HashMap<>();
        date.put("type","");
        date.put("startDate","");
        date.put("endDate","");
        map.put("dateRange",date);
        map.put("enquiry_tab", tab);
        map.put(NKeys.GETVENDORQUOTATIONS,new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETVENDORQUOTATIONS, map, true);
    }
    @Override
    public void onClickSendResponse(HashMap<String, Object> map) {
        updateInquiryStatus(map);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(notificationBinding.refreshLayout.isRefreshing())
            notificationBinding.refreshLayout.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETVENDORQUOTATIONS) {
                VendorQuotationsResponse response = new Gson().fromJson(responseDO.getResponse(), VendorQuotationsResponse.class);
                Common.showLog("count===+++===" + response.getData().getCount());
                    vendorMultiNotificationAdapter.refreshList(response.getData().getRecords());
                if(response.getData().getRecords().size()==0){
                    notificationBinding.noInquiriesRecordTv.setVisibility(View.VISIBLE);
                }else{
                    notificationBinding.noInquiriesRecordTv.setVisibility(View.GONE);
                }
            }else if (responseDO.getServiceMethods()==ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getAllInquiries("new_inquiry");
            }
        }
    }
}