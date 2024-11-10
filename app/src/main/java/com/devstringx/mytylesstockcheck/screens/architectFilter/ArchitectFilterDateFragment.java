package com.devstringx.mytylesstockcheck.screens.architectFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentArchitectFilterDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class ArchitectFilterDateFragment extends Fragment {
    FragmentArchitectFilterDateBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_architect_filter_date, container, false);
        View view = binding.getRoot();
        if (Common.architectDateType!=null) {
            if (Common.architectDateType.equalsIgnoreCase("all")) binding.spinnerView.selectItemByIndex(0);
            else if (Common.architectDateType.equalsIgnoreCase("today")) binding.spinnerView.selectItemByIndex(1);
            else if (Common.architectDateType.equalsIgnoreCase("yesterday")) binding.spinnerView.selectItemByIndex(2);
            else if (Common.architectDateType.equalsIgnoreCase("this_week")) binding.spinnerView.selectItemByIndex(3);
            else if (Common.architectDateType.equalsIgnoreCase("this_month")) binding.spinnerView.selectItemByIndex(4);
            else if (Common.architectDateType.equalsIgnoreCase("last_month")) binding.spinnerView.selectItemByIndex(5);
            else if (Common.architectDateType.equalsIgnoreCase("this_year")) binding.spinnerView.selectItemByIndex(6);
            else if (Common.architectDateType.equalsIgnoreCase("custom")) binding.spinnerView.selectItemByIndex(7);
        }
        if (Common.architectStartDate!=null) binding.fromDate.setText(Common.architectStartDate);
        if (Common.architectEndDate!=null) binding.toDate.setText(Common.architectEndDate);
        if (binding.spinnerView.getText().equals("Custom")){
            binding.customDate.setVisibility(View.VISIBLE);
        }else binding.customDate.setVisibility(View.GONE);
        Common.isArchitectFilterSelected(getActivity());
        binding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=binding.spinnerView.getSelectedIndex();
                if (i==7){
                    binding.customDate.setVisibility(View.VISIBLE);
                }else binding.customDate.setVisibility(View.GONE);
                if (i==0) Common.architectDateType="all";
                else if (i==1) Common.architectDateType="today";
                else if (i==2) Common.architectDateType="yesterday";
                else if (i==3) Common.architectDateType="this_week";
                else if (i==4) Common.architectDateType="this_month";
                else if (i==5) Common.architectDateType="last_month";
                else if (i==6) Common.architectDateType="this_year";
                else if (i==7) Common.architectDateType="custom";
                Common.isArchitectFilterSelected(getActivity());
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
                Common.openCalenderForUpcomingDates(getActivity(),binding.fromDate, "architect_start_date");
            }
        });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),binding.toDate, "architect_end_date");
            }
        });
        return view;
    }
}