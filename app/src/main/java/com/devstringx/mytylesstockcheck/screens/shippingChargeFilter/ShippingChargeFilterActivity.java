package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityShippingChargeFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ShippingChargeFilterActivity extends AppCompatActivity {
    ActivityShippingChargeFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_shipping_charge_filter);
        binding.dateRB.setChecked(true);
        if (binding.dateRB.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new ShippingChargeFilterByDateFragment(), R.id.shipping_filter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.dateRB.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ShippingChargeFilterByDateFragment(), R.id.shipping_filter_frag_container);
                } else if (binding.statusRB.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ShippingChargeFilterByStatusFragment(), R.id.shipping_filter_frag_container);
                } else if (binding.deliveryAgentRB.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ShippingChargeFilterByDeliveryAgentFragment(), R.id.shipping_filter_frag_container);
                } else if (binding.sortByRB.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ShippingChargeSortByFragment(), R.id.shipping_filter_frag_container);
                }
            }
        });
        binding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shippingDateType = "";
                Common.shippingStartDate = "";
                Common.shippingEndDate = "";
                Common.shippingFilterSortBy = "createdAtDesc";
                Common.selectedShippingStatustName=new ArrayList<>();
                Common.selectedShippingDeliveryAgentName=new ArrayList<>();
                callApi();
            }
        });
        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
    }
    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("search", "");
        map.put("sorting", Common.shippingFilterSortBy);
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("dateRange", Common.shippingDateType);
        map.put("fromDate", Common.shippingStartDate);
        map.put("toDate", Common.shippingEndDate);
        map.put("shippingChargeStatus", Common.selectedShippingStatustName);
        map.put("deliveryAgent", Common.selectedShippingDeliveryAgentName);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("ShippingFilterDataMap", new Gson().toJson(map));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.shippingDateType.isEmpty() && Common.selectedShippingStatustName.isEmpty() && Common.selectedShippingDeliveryAgentName.isEmpty() && Common.shippingFilterSortBy.equalsIgnoreCase("createdAtDesc"))
            binding.clearFilter.setVisibility(View.GONE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }
}