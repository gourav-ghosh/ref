package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentInquiriesFilterTimeBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class InquiriesFilterTimeFragment extends Fragment {
    FragmentInquiriesFilterTimeBinding inquiriesFilterTimeBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inquiriesFilterTimeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_inquiries_filter_time,container,false);
        View view=inquiriesFilterTimeBinding.getRoot();
            if (Common.inquiryDateType!=null) {
                if (Common.inquiryDateType.equalsIgnoreCase("all")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(0);
                else if (Common.inquiryDateType.equalsIgnoreCase("today")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(1);
                else if (Common.inquiryDateType.equalsIgnoreCase("yesterday")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(2);
                else if (Common.inquiryDateType.equalsIgnoreCase("this_week")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(3);
//                else if (Common.inquiryDateType.equalsIgnoreCase("last_week")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(4);
                else if (Common.inquiryDateType.equalsIgnoreCase("this_month")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(4);
                else if (Common.inquiryDateType.equalsIgnoreCase("last_month")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(5);
                else if (Common.inquiryDateType.equalsIgnoreCase("this_year")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(6);
                else if (Common.inquiryDateType.equalsIgnoreCase("custom")) inquiriesFilterTimeBinding.inquiryFilterSpinnerView.selectItemByIndex(7);
            }

        if (Common.inquiryStartDate!=null) inquiriesFilterTimeBinding.inquiryFilterfromDate.setText(Common.inquiryStartDate);
        if (Common.inquiryEndDate!=null) inquiriesFilterTimeBinding.inquiryFilterToDate.setText(Common.inquiryEndDate);
        if (inquiriesFilterTimeBinding.inquiryFilterSpinnerView.getText().equals("Custom")){
            inquiriesFilterTimeBinding.inquiryFilterCustomDate.setVisibility(View.VISIBLE);
        }else inquiriesFilterTimeBinding.inquiryFilterCustomDate.setVisibility(View.GONE);
        Common.isInquiryFilterSelected(getActivity());
        inquiriesFilterTimeBinding.inquiryFilterSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=inquiriesFilterTimeBinding.inquiryFilterSpinnerView.getSelectedIndex();
                if (i==7){
                    inquiriesFilterTimeBinding.inquiryFilterCustomDate.setVisibility(View.VISIBLE);
                }else inquiriesFilterTimeBinding.inquiryFilterCustomDate.setVisibility(View.GONE);
                if (i==0) Common.inquiryDateType="all";
                else if (i==1) Common.inquiryDateType="today";
                else if (i==2) Common.inquiryDateType="yesterday";
                else if (i==3) Common.inquiryDateType="this_week";
                else if (i==4) Common.inquiryDateType="this_month";
                else if (i==5) Common.inquiryDateType="last_Month";
                else if (i==6) Common.inquiryDateType="this_year";
                else if (i==7) Common.inquiryDateType="custom";
                Common.isInquiryFilterSelected(getActivity());
            }
        });
        inquiriesFilterTimeBinding.inquiryFilterSpinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                inquiriesFilterTimeBinding.inquiryFilterSpinnerView.dismiss();
            }
        });
        inquiriesFilterTimeBinding.inquiryFilterfromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),inquiriesFilterTimeBinding.inquiryFilterfromDate, "inquiry_start_date");
            }
        });
        inquiriesFilterTimeBinding.inquiryFilterToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),inquiriesFilterTimeBinding.inquiryFilterToDate, "inquiry_end_date");
            }
        });
        return view;
    }
}