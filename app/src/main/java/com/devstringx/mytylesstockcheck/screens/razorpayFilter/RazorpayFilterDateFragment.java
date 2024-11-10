package com.devstringx.mytylesstockcheck.screens.razorpayFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentRazorpayFilterDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class RazorpayFilterDateFragment extends Fragment {
    FragmentRazorpayFilterDateBinding filterDateBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterDateBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_razorpay_filter_date,container,false);
        View view=filterDateBinding.getRoot();
        if (Common.dateTypeRazor!=null) {
            if (Common.dateTypeRazor!=null) {
                if (Common.dateTypeRazor.equalsIgnoreCase("all")) filterDateBinding.spinnerView.selectItemByIndex(0);
                else if (Common.dateTypeRazor.equalsIgnoreCase("today")) filterDateBinding.spinnerView.selectItemByIndex(1);
                else if (Common.dateTypeRazor.equalsIgnoreCase("yesterday")) filterDateBinding.spinnerView.selectItemByIndex(2);
                else if (Common.dateTypeRazor.equalsIgnoreCase("this_week")) filterDateBinding.spinnerView.selectItemByIndex(3);
                else if (Common.dateTypeRazor.equalsIgnoreCase("this_month")) filterDateBinding.spinnerView.selectItemByIndex(4);
                else if (Common.dateTypeRazor.equalsIgnoreCase("last_month")) filterDateBinding.spinnerView.selectItemByIndex(5);
                else if (Common.dateTypeRazor.equalsIgnoreCase("this_year")) filterDateBinding.spinnerView.selectItemByIndex(6);
                else if (Common.dateTypeRazor.equalsIgnoreCase("custom")) filterDateBinding.spinnerView.selectItemByIndex(7);
            }
        }
        if (Common.startDateRazor!=null) filterDateBinding.fromDate.setText(Common.startDateRazor);
        if (Common.endDateRazor!=null) filterDateBinding.toDate.setText(Common.endDateRazor);
        if (filterDateBinding.spinnerView.getText().equals("Custom")){
            filterDateBinding.customDate.setVisibility(View.VISIBLE);
        }else filterDateBinding.customDate.setVisibility(View.GONE);
        Common.isRazorPayFilterSelected(getActivity());
        filterDateBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=filterDateBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    filterDateBinding.customDate.setVisibility(View.VISIBLE);
                }else filterDateBinding.customDate.setVisibility(View.GONE);
                if (i==0) Common.dateTypeRazor="all";
                else if (i==1) Common.dateTypeRazor="today";
                else if (i==2) Common.dateTypeRazor="yesterday";
                else if (i==3) Common.dateTypeRazor="this_week";
                else if (i==4) Common.dateTypeRazor="this_month";
                else if (i==5) Common.dateTypeRazor="last_month";
                else if (i==6) Common.dateTypeRazor="this_year";
                else if (i==7) Common.dateTypeRazor="custom";
                Common.isRazorPayFilterSelected(getActivity());
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
                Common.openCalenderForUpcomingDates(getActivity(),filterDateBinding.fromDate, "razorpay_start_date");
            }
        });
        filterDateBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),filterDateBinding.toDate, "razorpay_end_date");
            }
        });
        return view;
    }
}