package com.devstringx.mytylesstockcheck.screens.myProfile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentMyProfileOptionsBinding;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;

import java.util.ArrayList;
import java.util.List;

public class MyProfileOptionsFragment extends Fragment {
    FragmentMyProfileOptionsBinding myProfileOptionsBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myProfileOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile_options, container, false);
        View view = myProfileOptionsBinding.getRoot();
        ((MyProfileActivity)(getActivity())).myProfileBinding.backLl.setVisibility(View.VISIBLE);
        myProfileOptionsBinding.myProfileCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new EditProfileFragment(),R.id.profile_frag_container);
            }
        });
        myProfileOptionsBinding.changePasswordCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new ChangePasswordFragment(),R.id.profile_frag_container);
            }
        });
        return view;
    }
}