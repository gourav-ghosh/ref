package com.devstringx.mytylesstockcheck.screens.orderFilters;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentOrderDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class OrderDateFragment extends Fragment {
    FragmentOrderDateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_date, container, false);
        View view=binding.getRoot();
        if (Common.orderDateType!=null) {
            if (Common.orderDateType!=null) {
                if (Common.orderDateType.equalsIgnoreCase("all")) binding.spinnerView.selectItemByIndex(0);
                else if (Common.orderDateType.equalsIgnoreCase("today")) binding.spinnerView.selectItemByIndex(1);
                else if (Common.orderDateType.equalsIgnoreCase("yesterday")) binding.spinnerView.selectItemByIndex(2);
                else if (Common.orderDateType.equalsIgnoreCase("week")) binding.spinnerView.selectItemByIndex(3);
                else if (Common.orderDateType.equalsIgnoreCase("month")) binding.spinnerView.selectItemByIndex(4);
                else if (Common.orderDateType.equalsIgnoreCase("lastMonth")) binding.spinnerView.selectItemByIndex(5);
                else if (Common.orderDateType.equalsIgnoreCase("year")) binding.spinnerView.selectItemByIndex(6);
                else if (Common.orderDateType.equalsIgnoreCase("custom")) binding.spinnerView.selectItemByIndex(7);
            }
        }
        if (Common.orderStartDate!=null) binding.fromDate.setText(Common.orderStartDate);
        if (Common.orderEndDate!=null) binding.toDate.setText(Common.orderEndDate);
        if (binding.spinnerView.getText().equals("Custom")){
            binding.customDate.setVisibility(View.VISIBLE);
        }else binding.customDate.setVisibility(View.GONE);
        Common.isOrderListingFilterSelected(getActivity());
        binding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=binding.spinnerView.getSelectedIndex();
                if (i==7){
                    binding.customDate.setVisibility(View.VISIBLE);
                }else binding.customDate.setVisibility(View.GONE);
                if (i==0) Common.tempOrderDateType="all";
                else if (i==1) Common.tempOrderDateType="today";
                else if (i==2) Common.tempOrderDateType="yesterday";
                else if (i==3) Common.tempOrderDateType="week";
                else if (i==4) Common.tempOrderDateType="month";
                else if (i==5) Common.tempOrderDateType="lastMonth";
                else if (i==6) Common.tempOrderDateType="year";
                else if (i==7) Common.tempOrderDateType="custom";
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        binding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                binding.spinnerView.dismiss();
            }
        });
        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),binding.fromDate, "order start date");
            }
        });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),binding.toDate, "order end date");
            }
        });
        return view;
    }
}