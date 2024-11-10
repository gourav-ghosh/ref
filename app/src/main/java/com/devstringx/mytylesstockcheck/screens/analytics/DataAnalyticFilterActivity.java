package com.devstringx.mytylesstockcheck.screens.analytics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityDataAnalyticFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class DataAnalyticFilterActivity extends AppCompatActivity {
    ActivityDataAnalyticFilterBinding analyticFilterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analyticFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_analytic_filter);
        analyticFilterBinding.analyticDate.setChecked(true);
        if (analyticFilterBinding.analyticDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new DataAnalyticDateFragment(), R.id.analytic_filter_frag_container);
        analyticFilterBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (analyticFilterBinding.analyticDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new DataAnalyticDateFragment(), R.id.analytic_filter_frag_container);
                } else if (analyticFilterBinding.role.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new RoleFragment(), R.id.analytic_filter_frag_container);
                }
            }
        });
        analyticFilterBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        analyticFilterBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.analyticDateType = "";
                Common.analyticStartDate = "";
                Common.analyticEndDate = "";
                Common.analyticSortBy = "nameDesc";
                Common.selectedRole = "";
                Common.selectedRole_Original = new ArrayList<>();
                callApi();
            }
        });
        analyticFilterBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
    }


    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("sort", Common.analyticSortBy);
        map.put("dateRange", Common.analyticDateType);
        map.put("fromDate", Common.analyticStartDate);
        map.put("toDate", Common.analyticEndDate);
        map.put("role", TextUtils.isEmpty(Common.selectedRole) ? "sales_person" : Common.selectedRole);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("AnalyticFilterDataMap", new Gson().toJson(map));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.analyticDateType.isEmpty()
                && Common.analyticSortBy.equalsIgnoreCase("nameDesc")
                && Common.selectedRole.isEmpty())
            analyticFilterBinding.clearFilter.setVisibility(View.INVISIBLE);
        else analyticFilterBinding.clearFilter.setVisibility(View.VISIBLE);
    }
}