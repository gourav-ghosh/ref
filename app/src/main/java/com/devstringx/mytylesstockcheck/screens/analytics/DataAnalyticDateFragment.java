package com.devstringx.mytylesstockcheck.screens.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplainDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class DataAnalyticDateFragment extends Fragment {
    FragmentComplainDateBinding analyticDateBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        analyticDateBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_complain_date,container,false);
        View view= analyticDateBinding.getRoot();
        if (Common.analyticDateType!=null) {
            if (Common.analyticDateType.equalsIgnoreCase("all"))
                analyticDateBinding.spinnerView.selectItemByIndex(0);
            else if (Common.analyticDateType.equalsIgnoreCase("today"))
                analyticDateBinding.spinnerView.selectItemByIndex(1);
            else if (Common.analyticDateType.equalsIgnoreCase("yesterday"))
                analyticDateBinding.spinnerView.selectItemByIndex(2);
            else if (Common.analyticDateType.equalsIgnoreCase("this_week"))
                analyticDateBinding.spinnerView.selectItemByIndex(3);
            else if (Common.analyticDateType.equalsIgnoreCase("this_month"))
                analyticDateBinding.spinnerView.selectItemByIndex(4);
            else if (Common.analyticDateType.equalsIgnoreCase("last_month"))
                analyticDateBinding.spinnerView.selectItemByIndex(5);
            else if (Common.analyticDateType.equalsIgnoreCase("this_year"))
                analyticDateBinding.spinnerView.selectItemByIndex(6);
            else if (Common.analyticDateType.equalsIgnoreCase("custom"))
                analyticDateBinding.spinnerView.selectItemByIndex(7);
        }
        if (Common.analyticStartDate!=null) analyticDateBinding.fromDate.setText(Common.analyticStartDate);
        if (Common.analyticEndDate!=null) analyticDateBinding.toDate.setText(Common.analyticEndDate);
        if (analyticDateBinding.spinnerView.getText().equals("Custom")){
            analyticDateBinding.customDate.setVisibility(View.VISIBLE);
        }else analyticDateBinding.customDate.setVisibility(View.GONE);
        Common.isAnalyticFilterSelected(getActivity());
        analyticDateBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i= analyticDateBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    analyticDateBinding.customDate.setVisibility(View.VISIBLE);
                }else analyticDateBinding.customDate.setVisibility(View.GONE);
                if (i==0) Common.analyticDateType="all";
                else if (i==1) Common.analyticDateType="today";
                else if (i==2) Common.analyticDateType="yesterday";
                else if (i==3) Common.analyticDateType="this_week";
                else if (i==4) Common.analyticDateType="this_month";
                else if (i==5) Common.analyticDateType="last_month";
                else if (i==6) Common.analyticDateType="this_year";
                else if (i==7) Common.analyticDateType="custom";
                Common.isAnalyticFilterSelected(getActivity());
            }
        });
        analyticDateBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                analyticDateBinding.spinnerView.dismiss();
            }
        });
        analyticDateBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), analyticDateBinding.fromDate, "analytic_start_date");
            }
        });
        analyticDateBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), analyticDateBinding.toDate, "analytic_end_date");
            }
        });
        return view;
    }
}