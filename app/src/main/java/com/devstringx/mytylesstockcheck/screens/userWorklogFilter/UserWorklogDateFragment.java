package com.devstringx.mytylesstockcheck.screens.userWorklogFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentUserWorklogDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class UserWorklogDateFragment extends Fragment {
    FragmentUserWorklogDateBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_worklog_date, container, false);
        View view=binding.getRoot();
        if(Common.userWorklogDateType!=null) {
            if(Common.userWorklogDateType.equalsIgnoreCase("all")) binding.spinnerView.selectItemByIndex(0);
            else if(Common.userWorklogDateType.equalsIgnoreCase("today")) binding.spinnerView.selectItemByIndex(1);
            else if(Common.userWorklogDateType.equalsIgnoreCase("yesterday")) binding.spinnerView.selectItemByIndex(2);
            else if(Common.userWorklogDateType.equalsIgnoreCase("this_week")) binding.spinnerView.selectItemByIndex(3);
            else if(Common.userWorklogDateType.equalsIgnoreCase("this_month")) binding.spinnerView.selectItemByIndex(4);
            else if(Common.userWorklogDateType.equalsIgnoreCase("last_month")) binding.spinnerView.selectItemByIndex(5);
            else if(Common.userWorklogDateType.equalsIgnoreCase("this_year")) binding.spinnerView.selectItemByIndex(6);
            else if(Common.userWorklogDateType.equalsIgnoreCase("custom")) binding.spinnerView.selectItemByIndex(7);
        }
        if(Common.allWorklogStartDate!=null) binding.fromDate.setText(Common.allWorklogStartDate);
        if(Common.allWorklogEndDate!=null) binding.toDate.setText(Common.allWorklogEndDate);
        if(binding.spinnerView.getText().equals("Custom")) {
            binding.customDate.setVisibility(View.VISIBLE);
        }
        else binding.customDate.setVisibility(View.GONE);
        Common.isUserWorklogFilterSelected(getActivity());
        binding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {

            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=binding.spinnerView.getSelectedIndex();
                if(i==7) binding.customDate.setVisibility(View.VISIBLE);
                else binding.customDate.setVisibility(View.GONE);
                if(i==0) Common.userWorklogDateType="all";
                else if(i==1) Common.userWorklogDateType="today";
                else if (i==2) Common.userWorklogDateType="yesterday";
                else if (i==3) Common.userWorklogDateType="this_week";
                else if (i==4) Common.userWorklogDateType="this_month";
                else if (i==5) Common.userWorklogDateType="last_month";
                else if (i==6) Common.userWorklogDateType="this_year";
                else if (i==7) Common.userWorklogDateType="custom";
                Common.isUserWorklogFilterSelected(getActivity());
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
                Common.openCalenderForUpcomingDates(getActivity(), binding.fromDate, "user_worklog_start_date");
            }
        });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), binding.toDate, "user_worklog_end_date");
            }
        });
        return view;
    }
}