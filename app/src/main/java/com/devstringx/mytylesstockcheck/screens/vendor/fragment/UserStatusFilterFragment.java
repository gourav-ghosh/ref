package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentUserStatusFilterBinding;

public class UserStatusFilterFragment extends Fragment {
    FragmentUserStatusFilterBinding userStatusFilterBinding;
    String status="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userStatusFilterBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_user_status_filter,container,false);
        View view=userStatusFilterBinding.getRoot();
        if (Common.userStatusFilter.equalsIgnoreCase("active")){
            userStatusFilterBinding.activeRb.setImageResource(R.drawable.ic_checked_radio_button);
        } else if (Common.userStatusFilter.equalsIgnoreCase("inactive")){
            userStatusFilterBinding.inactiveRb.setImageResource(R.drawable.ic_checked_radio_button);
        } else if (Common.userStatusFilter.equalsIgnoreCase("added")) {
            userStatusFilterBinding.addedRb.setImageResource(R.drawable.ic_checked_radio_button);
        }
        Common.isUserFilterSelected(getActivity());
        userStatusFilterBinding.activeRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userStatusFilterBinding.activeRb.isSelected()) {
                    userStatusFilterBinding.activeRb.setImageResource(R.drawable.ic_checked_radio_button);
                    userStatusFilterBinding.inactiveRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    userStatusFilterBinding.addedRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    status="active";
                }
            }
        });
        userStatusFilterBinding.inactiveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userStatusFilterBinding.activeRb.isSelected()) {
                    userStatusFilterBinding.activeRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    userStatusFilterBinding.inactiveRb.setImageResource(R.drawable.ic_checked_radio_button);
                    userStatusFilterBinding.addedRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    status="inactive";
                }
            }
        });
        userStatusFilterBinding.addedRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userStatusFilterBinding.activeRb.isSelected()) {
                    userStatusFilterBinding.activeRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    userStatusFilterBinding.inactiveRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                    userStatusFilterBinding.addedRb.setImageResource(R.drawable.ic_checked_radio_button);
                    status="added";
                }
            }
        });
        return view;
    }

    public String getStatus() {
        return status;
    }
}