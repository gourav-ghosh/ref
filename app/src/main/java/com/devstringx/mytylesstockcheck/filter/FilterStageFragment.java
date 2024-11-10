package com.devstringx.mytylesstockcheck.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.FilterItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterStageBinding;
import com.devstringx.mytylesstockcheck.datamodal.leadstage.Response;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

import java.util.ArrayList;
import java.util.List;

public class FilterStageFragment extends Fragment implements ResponseListner, FilterItemAdapter.FilterItemSelected {
    FragmentFilterStageBinding filterStageBinding;
    private FilterItemAdapter adapter;
    private List<String> selecteditemsList = new ArrayList<>();
    private List<String> leadStageList = new ArrayList<>();
    FilterActivity filterActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterStageBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_stage,container,false);
        View view=filterStageBinding.getRoot();
        if (Common.dateType!=null) {
            if (Common.dateType!=null) {
                if (Common.dateType.equalsIgnoreCase("allTime")) filterStageBinding.spinnerView.selectItemByIndex(0);
                else if (Common.dateType.equalsIgnoreCase("today")) filterStageBinding.spinnerView.selectItemByIndex(1);
                else if (Common.dateType.equalsIgnoreCase("yesterday")) filterStageBinding.spinnerView.selectItemByIndex(2);
                else if (Common.dateType.equalsIgnoreCase("week")) filterStageBinding.spinnerView.selectItemByIndex(3);
                else if (Common.dateType.equalsIgnoreCase("month")) filterStageBinding.spinnerView.selectItemByIndex(4);
                else if (Common.dateType.equalsIgnoreCase("lastMonth")) filterStageBinding.spinnerView.selectItemByIndex(5);
                else if (Common.dateType.equalsIgnoreCase("year")) filterStageBinding.spinnerView.selectItemByIndex(6);
                else if (Common.dateType.equalsIgnoreCase("custom")) filterStageBinding.spinnerView.selectItemByIndex(7);
            }
        }
        Common.isFilterSelected(getActivity());
        if (Common.startDate!=null) filterStageBinding.fromDate.setText(Common.startDate);
        if (Common.endDate!=null) filterStageBinding.toDate.setText(Common.endDate);
        if (filterStageBinding.spinnerView.getText().equals("Custom")){
            filterStageBinding.customDate.setVisibility(View.VISIBLE);
        }else filterStageBinding.customDate.setVisibility(View.GONE);
        filterStageBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=filterStageBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    filterStageBinding.customDate.setVisibility(View.VISIBLE);
                }else filterStageBinding.customDate.setVisibility(View.GONE);
                if (i==0) Common.dateTypeTemp="allTime";
                else if (i==1) Common.dateTypeTemp="today";
                else if (i==2) Common.dateTypeTemp="yesterday";
                else if (i==3) Common.dateTypeTemp="week";
                else if (i==4) Common.dateTypeTemp="month";
                else if (i==5) Common.dateTypeTemp="lastMonth";
                else if (i==6) Common.dateTypeTemp="year";
                else if (i==7) Common.dateTypeTemp="custom";
//                Common.isFilterSelected(getActivity());
            }
        });
        filterStageBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                filterStageBinding.spinnerView.dismiss();
            }
        });
        filterStageBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterStageBinding.fromDate,"start date");

            }
        });
        filterStageBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterStageBinding.toDate, "end date");

            }
        });
        getLeadStage();
        Common.showLog("ownerList===="+Common.filterLeadStage);
        return view;
    }
    private void getLeadStage() {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETLEADSTAGE, null, true);
    }

    private void setupAdapter() {
        RecyclerView recyclerView = filterStageBinding.filterItemRv;
        adapter = new FilterItemAdapter(getContext(),leadStageList,this::onItemSelectedListner,new ArrayList<>(Common.filterLeadStage));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETLEADSTAGE) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                leadStageList = response.getData().getRecord();
                setupAdapter();
//                Common.showLog("LeadStage" + leadStageList);
            }
        }
    }
    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.filterLeadStageTemp=selected_items;
    }
}