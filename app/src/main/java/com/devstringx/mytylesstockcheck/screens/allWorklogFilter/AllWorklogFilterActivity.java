package com.devstringx.mytylesstockcheck.screens.allWorklogFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityAllWorklogFilterBinding;
import com.google.gson.Gson;

import java.util.HashMap;

public class AllWorklogFilterActivity extends AppCompatActivity {
    ActivityAllWorklogFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_worklog_filter);
        binding.allWorklogDate.setChecked(true);
        if(binding.allWorklogDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new AllWorklogDateFragment(), R.id.all_worklog_filter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(binding.allWorklogDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new AllWorklogDateFragment(), R.id.all_worklog_filter_frag_container);
                }
                else if(binding.allWorklogSortBy.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new AllWorklogSortByFragment(), R.id.all_worklog_filter_frag_container);
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
                Common.allWorklogSortBy="createdAtDesc";
                Common.allWorklogDateType="";
                Common.allWorklogStartDate="";
                Common.allWorklogEndDate="";
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
        HashMap<String, Object> map=new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("sort", Common.allWorklogSortBy.toString());
        map.put("dateRange", Common.allWorklogDateType.toString());
        map.put("fromDate", Common.allWorklogStartDate.toString());
        map.put("toDate", Common.allWorklogEndDate.toString());
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("AllWorklogFilterDataMap", new Gson().toJson(map));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if(Common.allWorklogDateType.isEmpty() && Common.allWorklogSortBy.equalsIgnoreCase("createdAtDesc")) binding.clearFilter.setVisibility(View.INVISIBLE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }
}