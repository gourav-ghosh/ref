package com.devstringx.mytylesstockcheck.filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.FilterItemAdapter;
import com.devstringx.mytylesstockcheck.adapter.LeadListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityFilterBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.AllLeadsResponse;
import com.devstringx.mytylesstockcheck.interfaces.UpdateTabTitle;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.fragments.AllLeadsFragment;
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

public class FilterActivity extends AppCompatActivity {
    ActivityFilterBinding filterBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        filterBinding.stage.setChecked(true);
        if (filterBinding.stage.isChecked()) Common.loadFragment(getSupportFragmentManager(),new FilterStageFragment(),R.id.filter_frag_container);

        filterBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterBinding.stage.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterStageFragment(),R.id.filter_frag_container);
                }else if (filterBinding.source.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterSourceFragment(),R.id.filter_frag_container);
                }else if (filterBinding.starred.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterStarredFragment(),R.id.filter_frag_container);
                }else if (filterBinding.date.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterDateFragment(),R.id.filter_frag_container);
                }else if (filterBinding.owner.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterOwnerFragment(),R.id.filter_frag_container);
                }else if (filterBinding.activity.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterLeadActivityFragment(),R.id.filter_frag_container);
                }
                else if (filterBinding.noLeadTask.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new FilterNoLeadTaskFragment(),R.id.filter_frag_container);
                }
            }
        });
        checkIsAnyFIlterSelected();
        filterBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        filterBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.dateType="";
                Common.startDate="";
                Common.endDate="";
                Common.filterStarMarked=false;
                Common.filterNoLeadTask=false;
                Common.filterOwnerList= new ArrayList<>();
                Common.filterLeadActivity= new ArrayList<>();
                Common.filterLeadSource= new ArrayList<>();
                Common.filterLeadStage= new ArrayList<>();
                Intent intent = new Intent();
                intent.putExtra("FilterDataMap","");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        filterBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.filterLeadStage=Common.filterLeadStageTemp;
                Common.startDate=Common.startDateTemp;
                Common.endDate=Common.endDateTemp;
                Common.dateType=Common.dateTypeTemp;
                Common.filterLeadSource=Common.filterLeadSourceTemp;
                Common.filterOwnerList=Common.filterOwnerListTemp;
                Common.filterStarMarked=Common.filterStarMarkedTemp;
                Common.filterLeadActivity=Common.filterLeadActivityTemp;
                Common.filterNoLeadTask=Common.filterNoLeadTaskTemp;

                if (Common.dateType.equalsIgnoreCase("Custom")) {
                    if (Common.startDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "From Date should not be empty");
                        return;
                    } else if (Common.endDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "To Date should not be empty");
                        return;
                    } else if (!Common.dateValidator(Common.startDate,Common.endDate)) {
                        Common.showToast(getBaseContext(), "from date should not be greater than to date");
                        return;
                    }
                }


                callApi();
            }
        });

        if ((Common.getPermission(this, "DB", "")) && (Common.getPermission(this, "ML", ""))
                && (Common.getPermission(this, "MQ", "")) && (Common.getPermission(this, "RZP", ""))
                && (Common.getPermission(this, "ODS", "")) && (Common.getPermission(this, "CMS", ""))
                && (Common.getPermission(this, "DWL", "")) && (Common.getPermission(this, "AORI", "")))
        {
            filterBinding.owner.setVisibility(View.GONE);
        }

        if ((Common.getPermission(this, "DB", "")) && (Common.getPermission(this, "ML", ""))
                && (Common.getPermission(this, "MQ", "")) && (Common.getPermission(this, "RZP", ""))
                && (Common.getPermission(this, "ODS", "")) && (Common.getPermission(this, "CMS", ""))
                && (Common.getPermission(this, "DWL", "")) && (Common.getPermission(this, "AORI", ""))
                && (Common.getPermission(this, "SCH", "")) && (Common.getPermission(this, "TRE", "")))
        {
            filterBinding.owner.setVisibility(View.VISIBLE);
        }
    }
    public void checkIsAnyFIlterSelected(){
        if (Common.filterNoLeadTask==false && Common.filterStarMarked==false &&
                Common.filterLeadActivity.isEmpty() && Common.filterLeadStage.isEmpty() &&
                Common.filterOwnerList.isEmpty() && Common.filterLeadSource.isEmpty() && Common.dateType.isEmpty()) filterBinding.clearFilter.setVisibility(View.INVISIBLE);
        else filterBinding.clearFilter.setVisibility(View.VISIBLE);
    }

    private void callApi() {
        Common.showLog("ownerList===="+Common.filterLeadStage);
        Common.showLog("ownerList===="+Common.filterLeadSource);
        Common.showLog("ownerList===="+Common.filterOwnerList);
        Common.showLog("ownerList===="+Common.filterLeadActivity);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("lead_stage", Common.filterLeadStage);
        map.put("lead_source", Common.filterLeadSource);
        map.put("lead_owner_id", Common.filterOwnerList);
        map.put("lead_activity", Common.filterLeadActivity);
        map.put("is_star_marked", Common.filterStarMarked);
        HashMap<String, Object> date = new HashMap<>();
        date.put("type",Common.dateType);
        date.put("startDate",Common.startDate);
        date.put("endDate",Common.endDate);
        map.put("dateFilter",date);
        map.put("no_task_lead", Common.filterNoLeadTask);
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("FilterDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }
}