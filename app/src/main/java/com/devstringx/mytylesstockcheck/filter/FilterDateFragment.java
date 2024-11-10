package com.devstringx.mytylesstockcheck.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class FilterDateFragment extends Fragment {
    FragmentFilterDateBinding filterDateBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterDateBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_date,container,false);
        View view=filterDateBinding.getRoot();
        if (Common.dateType!=null) {
            if (Common.dateType!=null) {
                if (Common.dateType.equalsIgnoreCase("allTime")) filterDateBinding.spinnerView.selectItemByIndex(0);
                else if (Common.dateType.equalsIgnoreCase("today")) filterDateBinding.spinnerView.selectItemByIndex(1);
                else if (Common.dateType.equalsIgnoreCase("yesterday")) filterDateBinding.spinnerView.selectItemByIndex(2);
                else if (Common.dateType.equalsIgnoreCase("week")) filterDateBinding.spinnerView.selectItemByIndex(3);
                else if (Common.dateType.equalsIgnoreCase("month")) filterDateBinding.spinnerView.selectItemByIndex(4);
                else if (Common.dateType.equalsIgnoreCase("lastMonth")) filterDateBinding.spinnerView.selectItemByIndex(5);
                else if (Common.dateType.equalsIgnoreCase("year")) filterDateBinding.spinnerView.selectItemByIndex(6);
                else if (Common.dateType.equalsIgnoreCase("custom")) filterDateBinding.spinnerView.selectItemByIndex(7);
            }
        }
        if (Common.startDate!=null) filterDateBinding.fromDate.setText(Common.startDate);
        if (Common.endDate!=null) filterDateBinding.toDate.setText(Common.endDate);
        if (filterDateBinding.spinnerView.getText().equals("Custom")){
            filterDateBinding.customDate.setVisibility(View.VISIBLE);
        }else filterDateBinding.customDate.setVisibility(View.GONE);
        Common.isFilterSelected(getActivity());
        filterDateBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=filterDateBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    filterDateBinding.customDate.setVisibility(View.VISIBLE);
                }else filterDateBinding.customDate.setVisibility(View.GONE);
                if (i==0) Common.dateTypeTemp="allTime";
                else if (i==1) Common.dateTypeTemp="today";
                else if (i==2) Common.dateTypeTemp="yesterday";
                else if (i==3) Common.dateTypeTemp="week";
                else if (i==4) Common.dateTypeTemp="month";
                else if (i==5) Common.dateTypeTemp="lastMonth";
                else if (i==6) Common.dateTypeTemp="year";
                else if (i==7) Common.dateTypeTemp="custom";
                Common.isFilterSelected(getActivity());
            }
        });
        filterDateBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                filterDateBinding.spinnerView.dismiss();
            }
        });
        filterDateBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterDateBinding.fromDate, "start date");
            }
        });
        filterDateBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterDateBinding.toDate, "end date");
            }
        });
        return view;
    }
}