package com.devstringx.mytylesstockcheck.screens.complaintFIlter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentComplainDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class ComplainDateFragment extends Fragment {
    FragmentComplainDateBinding complainDateBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        complainDateBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_complain_date,container,false);
        View view=complainDateBinding.getRoot();
        if (Common.complaintDateType!=null) {
            if (Common.complaintDateType!=null) {
                if (Common.complaintDateType.equalsIgnoreCase("all")) complainDateBinding.spinnerView.selectItemByIndex(0);
                else if (Common.complaintDateType.equalsIgnoreCase("today")) complainDateBinding.spinnerView.selectItemByIndex(1);
                else if (Common.complaintDateType.equalsIgnoreCase("yesterday")) complainDateBinding.spinnerView.selectItemByIndex(2);
                else if (Common.complaintDateType.equalsIgnoreCase("this_week")) complainDateBinding.spinnerView.selectItemByIndex(3);
                else if (Common.complaintDateType.equalsIgnoreCase("this_month")) complainDateBinding.spinnerView.selectItemByIndex(4);
                else if (Common.complaintDateType.equalsIgnoreCase("last_month")) complainDateBinding.spinnerView.selectItemByIndex(5);
                else if (Common.complaintDateType.equalsIgnoreCase("this_year")) complainDateBinding.spinnerView.selectItemByIndex(6);
                else if (Common.complaintDateType.equalsIgnoreCase("custom")) complainDateBinding.spinnerView.selectItemByIndex(7);
            }
        }
        if (Common.complaintStartDate!=null) complainDateBinding.fromDate.setText(Common.complaintStartDate);
        if (Common.complaintEndDate!=null) complainDateBinding.toDate.setText(Common.complaintEndDate);
        if (complainDateBinding.spinnerView.getText().equals("Custom")){
            complainDateBinding.customDate.setVisibility(View.VISIBLE);
        }else complainDateBinding.customDate.setVisibility(View.GONE);
        Common.isComplaintFilterSelected(getActivity());
        complainDateBinding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=complainDateBinding.spinnerView.getSelectedIndex();
                if (i==7){
                    complainDateBinding.customDate.setVisibility(View.VISIBLE);
                }else complainDateBinding.customDate.setVisibility(View.GONE);
                if (i==0) Common.complaintDateType="all";
                else if (i==1) Common.complaintDateType="today";
                else if (i==2) Common.complaintDateType="yesterday";
                else if (i==3) Common.complaintDateType="this_week";
                else if (i==4) Common.complaintDateType="this_month";
                else if (i==5) Common.complaintDateType="last_month";
                else if (i==6) Common.complaintDateType="this_year";
                else if (i==7) Common.complaintDateType="custom";
                Common.isComplaintFilterSelected(getActivity());
            }
        });
        complainDateBinding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                complainDateBinding.spinnerView.dismiss();
            }
        });
        complainDateBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),complainDateBinding.fromDate, "complaint_start_date");
            }
        });
        complainDateBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),complainDateBinding.toDate, "complaint_end_date");
            }
        });
        return view;
    }
}