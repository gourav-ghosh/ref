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
import com.devstringx.mytylesstockcheck.databinding.FragmentInquiriesFilterInquiryTypeBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class InquiriesFilterInquiryFragment extends Fragment {
    FragmentInquiriesFilterInquiryTypeBinding inquiriesFilterInquiryTypeBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inquiriesFilterInquiryTypeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_inquiries_filter_inquiry_type,container,false);
        View view=inquiriesFilterInquiryTypeBinding.getRoot();
        if (Common.filterInquiryType!=null) {
            if (Common.filterInquiryType.equalsIgnoreCase("Old inquiry")) inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.selectItemByIndex(0);
            else if (Common.filterInquiryType.equalsIgnoreCase("New inquiry")) inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.selectItemByIndex(1);
        }
        Common.isInquiryFilterSelected(getActivity());
        inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.getSelectedIndex();
                if (i==0) Common.filterInquiryType="Old inquiry";
                else if (i==1) Common.filterInquiryType="New inquiry";
                Common.isInquiryFilterSelected(getActivity());
            }
        });
        inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                inquiriesFilterInquiryTypeBinding.inquiryTypeFilterSpinnerView.dismiss();
            }
        });
        return view;
    }
}