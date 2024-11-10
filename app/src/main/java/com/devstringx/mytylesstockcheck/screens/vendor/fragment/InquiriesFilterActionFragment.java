package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentInquiriesFilterActionBinding;

public class InquiriesFilterActionFragment extends Fragment {
    FragmentInquiriesFilterActionBinding inquiriesFilterActionBinding;
    private boolean isAvailableLinearLayout = false, isUnavailableLinearLayout = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inquiriesFilterActionBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_inquiries_filter_action,container,false);
        View view=inquiriesFilterActionBinding.getRoot();
        if (Common.inquiryFilterActionAvailable.equalsIgnoreCase("Available")) {
            isAvailableLinearLayout = true;
            inquiriesFilterActionBinding.filterAvailableIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if (Common.inquiryFilterActionUnAvailable.equalsIgnoreCase("Not Available")) {
            isUnavailableLinearLayout = true;
            inquiriesFilterActionBinding.filterNotAvailableIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        Common.isInquiryFilterSelected(getActivity());
        inquiriesFilterActionBinding.filterAvailableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAvailableLinearLayout) {
                    isAvailableLinearLayout = false;
                    if (Common.inquiryFilterActionAvailable.equalsIgnoreCase("Available")) {
                        inquiriesFilterActionBinding.filterAvailableIv.setImageResource(R.drawable.orange_checkbox_unselected);
                        Common.inquiryFilterActionAvailable="";
                    }
                }else if(!isAvailableLinearLayout){
                    isAvailableLinearLayout = true;
                    if (Common.inquiryFilterActionAvailable.equalsIgnoreCase("")) {
                        inquiriesFilterActionBinding.filterAvailableIv.setImageResource(R.drawable.orange_checkbox_selected);
                        Common.inquiryFilterActionAvailable="Available";
                    }
                }
                Common.isInquiryFilterSelected(getActivity());
            }
        });
        inquiriesFilterActionBinding.filterNotAvailableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUnavailableLinearLayout){
                    isUnavailableLinearLayout = false;
                    if (Common.inquiryFilterActionUnAvailable.equalsIgnoreCase("Not Available")) {
                        inquiriesFilterActionBinding.filterNotAvailableIv.setImageResource(R.drawable.orange_checkbox_unselected);
                        Common.inquiryFilterActionUnAvailable="";
                    }
                }else if(!isUnavailableLinearLayout){
                    isUnavailableLinearLayout = true;
                    if (Common.inquiryFilterActionUnAvailable.equalsIgnoreCase("")) {
                        inquiriesFilterActionBinding.filterNotAvailableIv.setImageResource(R.drawable.orange_checkbox_selected);
                        Common.inquiryFilterActionUnAvailable="Not Available";
                    }
                }

                Common.isInquiryFilterSelected(getActivity());
            }
        });
        return view;
    }
}