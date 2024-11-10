package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.FragmentUserRoleFilterBinding;
import com.devstringx.mytylesstockcheck.screens.vendor.FilterUsersActivity;

public class UserRoleFilterFragment extends Fragment {
    FragmentUserRoleFilterBinding userRoleFilterBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userRoleFilterBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_user_role_filter,container,false);
        View view=userRoleFilterBinding.getRoot();
        userRoleFilterBinding.userRoleFilterSpinnerView.setItems(((FilterUsersActivity)getActivity()).roleNamesList);
        return view;
    }
}