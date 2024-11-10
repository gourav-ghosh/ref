package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityFilterUsersBinding;
import com.devstringx.mytylesstockcheck.databinding.FragmentUserRoleFilterBinding;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.UserRoleFilterFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.UserStatusFilterFragment;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterUsersActivity extends AppCompatActivity implements ResponseListner {
    ActivityFilterUsersBinding filterUsersBinding;
    public List<String> roleNamesList = new ArrayList<>();
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();
    private UserStatusFilterFragment userStatusFilterFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterUsersBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter_users);
//        getRoles();
        filterUsersBinding.statusRb.setChecked(true);
        if (filterUsersBinding.statusRb.isChecked()) {
            userStatusFilterFragment=new UserStatusFilterFragment();
            Common.loadFragment(getSupportFragmentManager(), userStatusFilterFragment, R.id.filter_inquiry_frag_container);
        }
        filterUsersBinding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (filterUsersBinding.statusRb.isChecked()) {
                    userStatusFilterFragment=new UserStatusFilterFragment();
                    Common.loadFragment(getSupportFragmentManager(), userStatusFilterFragment, R.id.filter_inquiry_frag_container);
                } else {
                    Common.loadFragment(getSupportFragmentManager(), new UserRoleFilterFragment(), R.id.filter_inquiry_frag_container);
                }
            }
        });
        filterUsersBinding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.userStatusFilter=userStatusFilterFragment.getStatus();
                Common.isUserFilterSelected(FilterUsersActivity.this);
                Intent intent = new Intent();
                intent.putExtra("FilterUserDataMap", Common.userStatusFilter);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        filterUsersBinding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.userStatusFilter="";
                Common.isUserFilterSelected(FilterUsersActivity.this);
                Intent intent = new Intent();
                HashMap<String, Object> map = new HashMap<>();
                intent.putExtra("FilterUserDataMap", Common.userStatusFilter);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        filterUsersBinding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                HashMap<String, Object> map = new HashMap<>();
                intent.putExtra("FilterUserDataMap", Common.userStatusFilter);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }



    private void getRoles() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETROLES, null, true);
    }
    public void checkIsAnyFIlterSelected(){
        if (Common.userStatusFilter.isEmpty()) filterUsersBinding.clearFilter.setVisibility(View.INVISIBLE);
        else filterUsersBinding.clearFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
//            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETROLES) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(responseDO.getResponse());
//                    JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
//                    for (int i = 0; i < records.length(); i++) {
//                        JSONObject record = records.getJSONObject(i);
//                        String roleName = record.getString("role_name");
//                        roleNamesList.add(roleName);
//                    }
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        }
    }
}