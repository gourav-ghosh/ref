package com.devstringx.mytylesstockcheck.screens.userWorklogFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityUserWorklogFilterBinding;
import com.google.gson.Gson;

import java.util.HashMap;

public class UserWorklogFilterActivity extends AppCompatActivity {
    ActivityUserWorklogFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_worklog_filter);
        binding.userWorklogDate.setChecked(true);
        if(binding.userWorklogDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new UserWorklogDateFragment(), R.id.user_worklog_filter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(binding.userWorklogDate.isChecked())
                    Common.loadFragment(getSupportFragmentManager(), new UserWorklogDateFragment(), R.id.user_worklog_filter_frag_container);
                else if (binding.userWorklogSortBy.isChecked())
                    Common.loadFragment(getSupportFragmentManager(), new UserWorklogSortByFragment(), R.id.user_worklog_filter_frag_container);
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
                Common.userWorklogSortBy = "createdAtDesc";
                Common.userWorklogDateType = "";
                Common.userWorklogStartDate = "";
                Common.userWorklogEndDate = "";
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
        map.put("sort", Common.userWorklogSortBy.toString());
        map.put("dateRange", Common.userWorklogDateType.toString());
        map.put("fromDate", Common.userWorklogStartDate.toString());
        map.put("toDate", Common.userWorklogEndDate.toString());
        map.put("userId", Common.userWorklogUserId);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("UserWorklogFilterDataMap", new Gson().toJson(map));
        setResult(RESULT_OK, intent);
        finish();

    }

    public void checkIsAnyFIlterSelected() {
        if(Common.userWorklogDateType.isEmpty() && Common.userWorklogSortBy.equalsIgnoreCase("createdAtDesc"))
            binding.clearFilter.setVisibility(View.GONE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }
}