package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityInquiryFilterBinding;
import com.devstringx.mytylesstockcheck.filter.FilterDateFragment;
import com.devstringx.mytylesstockcheck.filter.FilterLeadActivityFragment;
import com.devstringx.mytylesstockcheck.filter.FilterNoLeadTaskFragment;
import com.devstringx.mytylesstockcheck.filter.FilterOwnerFragment;
import com.devstringx.mytylesstockcheck.filter.FilterSourceFragment;
import com.devstringx.mytylesstockcheck.filter.FilterStageFragment;
import com.devstringx.mytylesstockcheck.filter.FilterStarredFragment;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.InquiriesFilterActionFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.InquiriesFilterInquiryFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.InquiriesFilterQuantityFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.InquiriesFilterTimeFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class InquiryFilterActivity extends AppCompatActivity {
    ActivityInquiryFilterBinding inquiryFilterBinding;
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inquiryFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_inquiry_filter);
        inquiryFilterBinding.timeRb.setChecked(true);
        if (getIntent().getStringExtra("code").equalsIgnoreCase("new_inquiry")) {
            inquiryFilterBinding.thirdFilter.setText("Inquiry Type");
        } else inquiryFilterBinding.thirdFilter.setText("Action");
        if (inquiryFilterBinding.timeRb.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new InquiriesFilterTimeFragment(), R.id.filter_inquiry_frag_container);

        inquiryFilterBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (inquiryFilterBinding.timeRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new InquiriesFilterTimeFragment(), R.id.filter_inquiry_frag_container);
                } else if (inquiryFilterBinding.quantityRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new InquiriesFilterQuantityFragment(), R.id.filter_inquiry_frag_container);
                } else if (inquiryFilterBinding.thirdFilter.isChecked()) {
                    if (inquiryFilterBinding.thirdFilter.getText().equals("Action"))
                        Common.loadFragment(getSupportFragmentManager(), new InquiriesFilterActionFragment(), R.id.filter_inquiry_frag_container);
                    else
                        Common.loadFragment(getSupportFragmentManager(), new InquiriesFilterInquiryFragment(), R.id.filter_inquiry_frag_container);
                }
            }
        });
        inquiryFilterBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.inquiryDateType.equalsIgnoreCase("Custom")) {
                    if (Common.inquiryStartDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "From Date should not be empty");
                        return;
                    } else if (Common.inquiryEndDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "To Date should not be empty");
                        return;
                    } else if (!Common.dateValidator(Common.inquiryStartDate, Common.inquiryEndDate)) {
                        Common.showToast(getBaseContext(), "from date should not be greater than to date");
                        return;
                    }
                }
                callApi();
            }
        });
        inquiryFilterBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.clearInquiryFilterData();
                Intent intent = new Intent();
                HashMap<String, Object> map = new HashMap<>();
                map.put("enquiry_tab", getIntent().getStringExtra("code"));
                intent.putExtra("FilterInquiryDataMap", new Gson().toJson(map));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        inquiryFilterBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callApi() {
        Common.showLog("ownerList===="+Common.inquiryDateType);
        Common.showLog("ownerList===="+Common.inquiryStartDate);
        Common.showLog("ownerList===="+Common.inquiryEndDate);
        Common.showLog("ownerList===="+Common.inquiryFilterQuantity);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("id", "");
        map.put("sort", "createdDateDesc");
        if(!Common.inquiryFilterActionAvailable.isEmpty() && !Common.inquiryFilterActionUnAvailable.isEmpty() || Common.inquiryFilterActionUnAvailable.isEmpty()
        && Common.inquiryFilterActionAvailable.isEmpty()) {
            map.put("action", "");
        }else if (!Common.inquiryFilterActionAvailable.isEmpty()) { map.put("action", Common.inquiryFilterActionAvailable);
        }else if(!Common.inquiryFilterActionUnAvailable.isEmpty()){ map.put("action", Common.inquiryFilterActionUnAvailable);}
        map.put("quantity", Common.inquiryFilterQuantity);
        map.put("enquiry_status", Common.filterInquiryType);
        HashMap<String, Object> date = new HashMap<>();
        date.put("type",Common.inquiryDateType);
        date.put("startDate",Common.inquiryStartDate);
        date.put("endDate",Common.inquiryEndDate);
        map.put("dateRange",date);
        map.put("enquiry_tab", getIntent().getStringExtra("code"));
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("FilterInquiryDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.inquiryFilterQuantity.isEmpty() && Common.inquiryDateType.isEmpty() && Common.inquiryFilterActionAvailable.isEmpty()
                && Common.inquiryFilterActionUnAvailable.isEmpty() && Common.filterInquiryType.isEmpty())
            inquiryFilterBinding.clearFilter.setVisibility(View.INVISIBLE);
        else inquiryFilterBinding.clearFilter.setVisibility(View.VISIBLE);
    }
}