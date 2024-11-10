package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentInquiriesFilterQuantityBinding;

public class InquiriesFilterQuantityFragment extends Fragment {
    FragmentInquiriesFilterQuantityBinding inquiriesFilterQuantityBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inquiriesFilterQuantityBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_inquiries_filter_quantity,container,false);
        View view=inquiriesFilterQuantityBinding.getRoot();
        if (!Common.inquiryFilterQuantity.isEmpty()) inquiriesFilterQuantityBinding.inquiryFilterQtyEt.setText(Common.inquiryFilterQuantity);
        Common.isInquiryFilterSelected(getActivity());
        Common.showLog("=="+Common.inquiryFilterAction+"    "+Common.inquiryDateType+"    "+Common.inquiryStartDate+"   "+Common.inquiryEndDate+"    "+Common.inquiryFilterQuantity+"       "+Common.filterInquiryType);
        inquiriesFilterQuantityBinding.inquiryFilterQtyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Common.inquiryFilterQuantity= String.valueOf(charSequence);
                Common.showLog("QTY+=+=+==="+Common.inquiryFilterQuantity);
                Common.isInquiryFilterSelected(getActivity());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }
}