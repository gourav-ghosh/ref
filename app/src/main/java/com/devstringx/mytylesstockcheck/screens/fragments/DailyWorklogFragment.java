package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.WorklogAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentDailyWorklogBinding;
import com.devstringx.mytylesstockcheck.datamodal.allWorklogs.Response;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.allWorklogFilter.AllWorklogFilterActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DailyWorklogFragment extends Fragment {
    FragmentDailyWorklogBinding binding;
    WorklogAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily_worklog, container, false);
        View view = binding.getRoot();
        setupAdapter();
        binding.refreshLl.setRefreshing(false);
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setHasmapData("");
            }
        });
        binding.worklogFilterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AllWorklogFilterActivity.class), 200);
            }
        });
        binding.worklogExportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportWorklog();
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setHasmapData(binding.searchET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.worklogExportIV.setVisibility(View.INVISIBLE);
        }
        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "SCE", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.worklogExportIV.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void exportWorklog() {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("search", binding.searchET.getText().toString());
        map.put("dateRange", Common.allWorklogDateType);
        map.put("fromDate", Common.allWorklogStartDate);
        map.put("toDate", Common.allWorklogEndDate);
        map.put("sort", Common.allWorklogSortBy);
        map1.put(NKeys.EXPORTALLWORKLOGS, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_EXPORTALLWORKLOGS, map1, true);
    }

    private void setupAdapter() {
        RecyclerView recyclerView = binding.worklogCardRv;
        adapter = new WorklogAdapter(getContext(), this::onItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
    public void onItemClick(String id) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("userId", id);
        UserWorklogFragment userWorklogFragment = new UserWorklogFragment();
        userWorklogFragment.setArguments(args);
        transaction.replace(((ViewGroup) getView().getParent()).getId(), userWorklogFragment);
        transaction.addToBackStack("UserWorklogFragment");
        transaction.commit();

    }
    public void setHasmapData(String searchText) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", searchText);
        map.put("dateRange", Common.allWorklogDateType);
        map.put("fromDate", Common.allWorklogStartDate);
        map.put("toDate", Common.allWorklogEndDate);
        map.put("sort", Common.allWorklogSortBy);
        getFilterAllWorklogs(new Gson().toJson(map), false);
    }

    private void getFilterAllWorklogs(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLWORKLOGS, receivedHashMap);
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_GETALLWORKLOGS, map, isLoader);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            if(data!=null) {
                String receivedHashMap = data.getStringExtra("AllWorklogFilterDataMap");
                getFilterAllWorklogs(receivedHashMap, true);
            }
        }
    }
    private void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(binding.refreshLl.isRefreshing())binding.refreshLl.setRefreshing(false);
        if(!responseDO.isError()) {
            if(responseDO.getServiceMethods() == ServiceMethods.WS_GETALLWORKLOGS) {
                Response worklogResponse = new Gson().fromJson(responseDO.getResponse(), Response.class);
                binding.absentEmployeeTV.setText(String.valueOf(worklogResponse.getData().getTabCounts().getAbsentEmployee()));
                binding.notUpdatedEmployeeTV.setText(String.valueOf(worklogResponse.getData().getTabCounts().getNotUpdatedEmployee()));
                binding.presentEmployeeTV.setText(String.valueOf(worklogResponse.getData().getTabCounts().getPresentEmployee()));
                binding.totalEmployeeTV.setText(String.valueOf(worklogResponse.getData().getTabCounts().getTotalEmployee()));
//                if(worklogResponse.getData().getSearch()!=null) binding.searchET.setText(String.valueOf(worklogResponse.getData().getSearch()));
                Date today=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                String todayDate= sdf.format(today);
                binding.todayTV.setText(todayDate);
                if(worklogResponse.getData().getRecords().size() > 0) {
                    binding.noWorklogTV.setVisibility(View.GONE);
                    binding.refreshLl.setVisibility(View.VISIBLE);
                    adapter.refreshList(worklogResponse.getData().getRecords());
                }
                else {
                    binding.refreshLl.setVisibility(View.GONE);
                    binding.noWorklogTV.setVisibility(View.VISIBLE);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTALLWORKLOGS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optString("link")));
                    startActivity(intent);
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasmapData("");
    }
}