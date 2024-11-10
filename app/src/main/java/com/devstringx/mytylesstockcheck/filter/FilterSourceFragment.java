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
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterSourceBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

import java.util.ArrayList;
import java.util.List;


public class FilterSourceFragment extends Fragment implements ResponseListner, FilterItemAdapter.FilterItemSelected {
    FragmentFilterSourceBinding filterSourceBinding;
    private List<String> leadSourceList = new ArrayList<>();
    private RecordsItem recordsItem=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterSourceBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_source,container,false);
        View view=filterSourceBinding.getRoot();
        if (Common.dateType!=null) {
            if (Common.dateType.equalsIgnoreCase("allTime")) filterSourceBinding.spinnerView.selectItemByIndex(0);
            else if (Common.dateType.equalsIgnoreCase("today")) filterSourceBinding.spinnerView.selectItemByIndex(1);
            else if (Common.dateType.equalsIgnoreCase("yesterday")) filterSourceBinding.spinnerView.selectItemByIndex(2);
            else if (Common.dateType.equalsIgnoreCase("week")) filterSourceBinding.spinnerView.selectItemByIndex(3);
            else if (Common.dateType.equalsIgnoreCase("month")) filterSourceBinding.spinnerView.selectItemByIndex(4);
            else if (Common.dateType.equalsIgnoreCase("lastMonth")) filterSourceBinding.spinnerView.selectItemByIndex(5);
            else if (Common.dateType.equalsIgnoreCase("year")) filterSourceBinding.spinnerView.selectItemByIndex(6);
            else if (Common.dateType.equalsIgnoreCase("custom")) filterSourceBinding.spinnerView.selectItemByIndex(7);
        }
        Common.isFilterSelected(getActivity());
        if (Common.startDate!=null) filterSourceBinding.fromDate.setText(Common.startDate);
        if (Common.endDate!=null) filterSourceBinding.toDate.setText(Common.endDate);
        if (filterSourceBinding.spinnerView.getText().equals("Custom")){
            filterSourceBinding.customDate.setVisibility(View.VISIBLE);
        }else filterSourceBinding.customDate.setVisibility(View.GONE);
        filterSourceBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=filterSourceBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    filterSourceBinding.customDate.setVisibility(View.VISIBLE);
                }else filterSourceBinding.customDate.setVisibility(View.GONE);
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
        filterSourceBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                filterSourceBinding.spinnerView.dismiss();
            }
        });
        filterSourceBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterSourceBinding.fromDate, "start date");
            }
        });
        filterSourceBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterSourceBinding.toDate, "end date");
            }
        });
        allLeadSource();
        setupAdapter();
        return view;
    }
    private void allLeadSource() {
        leadSourceList.add("Website");
        leadSourceList.add("Google Business");
        leadSourceList.add("Interior/Architect Ref");
        leadSourceList.add("Friend Referral");
        leadSourceList.add("Instagram");
        leadSourceList.add("Offline");
        leadSourceList.add("Web Signups");
        leadSourceList.add("Zopim Chat");
    }
    private void setupAdapter() {
        RecyclerView recyclerView = filterSourceBinding.filterItemRv;
        FilterItemAdapter adapter = new FilterItemAdapter(getContext(),leadSourceList,this::onItemSelectedListner,new ArrayList<>(Common.filterLeadSource));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

    }


    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.filterLeadSourceTemp=selected_items;
//        Common.isFilterSelected(getActivity());
    }
}