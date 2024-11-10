package com.devstringx.mytylesstockcheck.screens.complaintFIlter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityComplaintFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ComplaintFilterActivity extends AppCompatActivity {
    ActivityComplaintFilterBinding complaintFilterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        complaintFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_complaint_filter);
        complaintFilterBinding.complainDate.setChecked(true);
        if (complaintFilterBinding.complainDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new ComplainDateFragment(), R.id.complain_filter_frag_container);
        complaintFilterBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (complaintFilterBinding.complainDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainDateFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.complainType.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainTypeFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.issueFrom.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new IssueFromFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.disManager.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainDispatchManagerFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.complainSalePerson.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainSalePersonFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.supExe.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainSupportExecutiveFragment(), R.id.complain_filter_frag_container);
                } else if (complaintFilterBinding.complainSortBy.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplainSortByFragment(), R.id.complain_filter_frag_container);
                }
            }
        });
        complaintFilterBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        complaintFilterBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.complaintDateType = "";
                Common.complaintStartDate = "";
                Common.complaintEndDate = "";
                Common.complaintSortBy = "createdAtDesc";
                Common.selectedComplaintDisName=new ArrayList<>();
                Common.selectedComplaintSupExeName=new ArrayList<>();
                Common.selectedComplaintSalesExeName=new ArrayList<>();
                Common.selectedIssueFrom=new ArrayList<>();
                Common.selectedComplaintType=new ArrayList<>();
                callApi();
            }
        });
        complaintFilterBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
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
        map.put("sort",Common.complaintSortBy);
        map.put("dateRange", Common.complaintDateType);
        map.put("fromDate", Common.complaintStartDate);
        map.put("toDate", Common.complaintEndDate);
        map.put("complaintType", Common.selectedComplaintType);
        map.put("supportExecutive", Common.selectedComplaintSupExeName);
        map.put("saleExecutive",Common.selectedComplaintSalesExeName);
        map.put("manager", Common.selectedComplaintDisName);
        map.put("issueForm", Common.selectedIssueFrom);
        map.put("statusTab", getIntent().getStringExtra("selectedTab").toString());
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("ComplaintFilterDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.complaintDateType.isEmpty() && Common.complaintSortBy.equalsIgnoreCase("createdAtDesc") && Common.selectedComplaintSupExeName.isEmpty()
                && Common.selectedComplaintSalesExeName.isEmpty() && Common.selectedComplaintDisName.isEmpty()
                && Common.selectedComplaintType.isEmpty() && Common.selectedIssueFrom.isEmpty())
            complaintFilterBinding.clearFilter.setVisibility(View.INVISIBLE);
        else complaintFilterBinding.clearFilter.setVisibility(View.VISIBLE);
    }
}