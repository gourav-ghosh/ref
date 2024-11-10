package com.devstringx.mytylesstockcheck.screens.razorpayFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityRazorPayFilterBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.requirenmentData.RequirenmentData;
import com.devstringx.mytylesstockcheck.filter.FilterDateFragment;
import com.devstringx.mytylesstockcheck.filter.FilterLeadActivityFragment;
import com.devstringx.mytylesstockcheck.filter.FilterNoLeadTaskFragment;
import com.devstringx.mytylesstockcheck.filter.FilterOwnerFragment;
import com.devstringx.mytylesstockcheck.filter.FilterSourceFragment;
import com.devstringx.mytylesstockcheck.filter.FilterStageFragment;
import com.devstringx.mytylesstockcheck.filter.FilterStarredFragment;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RazorPayFilterActivity extends AppCompatActivity implements ResponseListner {
    ActivityRazorPayFilterBinding razorPayFilterBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        razorPayFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_razor_pay_filter);
        razorPayFilterBinding.orderDate.setChecked(true);
        if (razorPayFilterBinding.orderDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new RazorpayFilterDateFragment(), R.id.razorpay_filter_frag_container);
        razorPayFilterBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        razorPayFilterBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (razorPayFilterBinding.orderDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new RazorpayFilterDateFragment(), R.id.razorpay_filter_frag_container);
                } else if (razorPayFilterBinding.orderStatus.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new RazorpayFilterStatusFragment(), R.id.razorpay_filter_frag_container);
                } else if (razorPayFilterBinding.salesPerson.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new RazorpayFilterSalePersonFragment(), R.id.razorpay_filter_frag_container);
                } else if (razorPayFilterBinding.sortBy.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new RazorpayFilterSortByFragment(), R.id.razorpay_filter_frag_container);
                }
            }
        });
        razorPayFilterBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.dateTypeRazor="";
                Common.startDateRazor="";
                Common.endDateRazor="";
                Common.razorpayStatus=new ArrayList<>();
                Common.sortByRazorpay="createdAtDesc";
                Common.selectedSalesExeName=new ArrayList<>();
                Intent intent = new Intent();
                HashMap<String, Object> map = new HashMap<>();
                intent.putExtra("RazorPayFilterDataMap",new Gson().toJson(map));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        razorPayFilterBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.dateTypeRazor.equalsIgnoreCase("Custom")) {
                    if (Common.startDateRazor.isEmpty()) {
                        Common.showToast(getBaseContext(), "From Date should not be empty");
                        return;
                    } else if (Common.endDateRazor.isEmpty()) {
                        Common.showToast(getBaseContext(), "To Date should not be empty");
                        return;
                    } else if (!Common.dateValidator(Common.startDateRazor,Common.endDateRazor)) {
                        Common.showToast(getBaseContext(), "from date should not be greater than to date");
                        return;
                    }
                }
                callApi();
            }
        });
    }

    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("paymentStatus", Common.razorpayStatus);
        map.put("saleExecutive", Common.selectedSalesExeName);
        map.put("sort", Common.sortByRazorpay);
        map.put("dateRange",Common.dateTypeRazor);
        map.put("fromDate",Common.startDateRazor);
        map.put("toDate",Common.endDateRazor);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("RazorPayFilterDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.dateTypeRazor.isEmpty() && Common.selectedSalesExeName.isEmpty() && Common.razorpayStatus.isEmpty() && Common.sortByRazorpay=="createdAtDesc")
            razorPayFilterBinding.clearFilter.setVisibility(View.INVISIBLE);
        else razorPayFilterBinding.clearFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

    }
}