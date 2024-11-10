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
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterLeadActivityBinding;
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

public class FilterLeadActivityFragment extends Fragment implements ResponseListner , FilterItemAdapter.FilterItemSelected {
    FragmentFilterLeadActivityBinding filterLeadActivityBinding;
    private List<String> leadActivityList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterLeadActivityBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_lead_activity,container,false);
        View view=filterLeadActivityBinding.getRoot();
        if (Common.dateType!=null) {
            if (Common.dateType!=null) {
                if (Common.dateType.equalsIgnoreCase("allTime")) filterLeadActivityBinding.spinnerView.selectItemByIndex(0);
                else if (Common.dateType.equalsIgnoreCase("today")) filterLeadActivityBinding.spinnerView.selectItemByIndex(1);
                else if (Common.dateType.equalsIgnoreCase("yesterday")) filterLeadActivityBinding.spinnerView.selectItemByIndex(2);
                else if (Common.dateType.equalsIgnoreCase("week")) filterLeadActivityBinding.spinnerView.selectItemByIndex(3);
                else if (Common.dateType.equalsIgnoreCase("month")) filterLeadActivityBinding.spinnerView.selectItemByIndex(4);
                else if (Common.dateType.equalsIgnoreCase("lastMonth")) filterLeadActivityBinding.spinnerView.selectItemByIndex(5);
                else if (Common.dateType.equalsIgnoreCase("year")) filterLeadActivityBinding.spinnerView.selectItemByIndex(6);
                else if (Common.dateType.equalsIgnoreCase("custom")) filterLeadActivityBinding.spinnerView.selectItemByIndex(7);
            }
        }
        Common.isFilterSelected(getActivity());
        if (Common.startDate!=null) filterLeadActivityBinding.fromDate.setText(Common.startDate);
        if (Common.endDate!=null) filterLeadActivityBinding.toDate.setText(Common.endDate);
        if (filterLeadActivityBinding.spinnerView.getText().equals("Custom")){
            filterLeadActivityBinding.customDate.setVisibility(View.VISIBLE);
        }else filterLeadActivityBinding.customDate.setVisibility(View.GONE);
        filterLeadActivityBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=filterLeadActivityBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    filterLeadActivityBinding.customDate.setVisibility(View.VISIBLE);
                }else filterLeadActivityBinding.customDate.setVisibility(View.GONE);
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
        filterLeadActivityBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                filterLeadActivityBinding.spinnerView.dismiss();
            }
        });
        filterLeadActivityBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterLeadActivityBinding.fromDate, "start date");
            }
        });
        filterLeadActivityBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterLeadActivityBinding.toDate, "end date");
            }
        });
        getActivityType();
        return view;
    }
    private void setupAdapter() {
        RecyclerView recyclerView = filterLeadActivityBinding.filterItemRv;
        FilterItemAdapter adapter = new FilterItemAdapter(getContext(),leadActivityList,this::onItemSelectedListner,new ArrayList<>(Common.filterLeadActivity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    private void getActivityType() {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETACTIVITYTYPE, null, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETACTIVITYTYPE) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                leadActivityList = response.getData().getRecord();
                Common.showLog("Activity" +leadActivityList);
                setupAdapter();
            }
        }
    }


    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.filterLeadActivityTemp=selected_items;
        Common.isFilterSelected(getActivity());
    }
}