package com.devstringx.mytylesstockcheck.screens.architectFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityArchitectFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ArchitectFilterActivity extends AppCompatActivity {
    ActivityArchitectFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_architect_filter);
        binding.architectDate.setChecked(true);
        if (binding.architectDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new ArchitectFilterDateFragment(), R.id.architect_filter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.architectDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ArchitectFilterDateFragment(), R.id.architect_filter_frag_container);
                } else if (binding.architectSalePerson.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ArchitectFilterSalesPersonFragment(), R.id.architect_filter_frag_container);
                } else if (binding.architectSortBy.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ArchitectFilterSortByFragment(), R.id.architect_filter_frag_container);
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
                Common.architectDateType="";
                Common.startDate="";
                Common.endDate="";
                Common.architectSelectedSaleperson=new ArrayList<>();
                Common.architectSelectedSalepersonId=new ArrayList<>();
                Common.architectSortBy="createdAtDesc";
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
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("sort",Common.architectSortBy.toString());
        map.put("dateRange", Common.architectDateType.toString());
        map.put("fromDate", Common.architectStartDate.toString());
        map.put("toDate", Common.architectEndDate.toString());
        map.put("salesExecutive", Common.architectSelectedSalepersonId);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("ArchitectFilterDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.architectDateType.isEmpty() && Common.architectSortBy.equalsIgnoreCase("createdAtDesc") && Common.architectSelectedSaleperson.isEmpty())
            binding.clearFilter.setVisibility(View.INVISIBLE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }

}