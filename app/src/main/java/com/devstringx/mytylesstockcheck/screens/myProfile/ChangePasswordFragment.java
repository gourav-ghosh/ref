package com.devstringx.mytylesstockcheck.screens.myProfile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentChangePasswordBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.startup.BaseActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChangePasswordFragment extends Fragment implements ResponseListner {
    FragmentChangePasswordBinding changePasswordBinding;
    private List<DataItem> profileData= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        changePasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        View view = changePasswordBinding.getRoot();
        ((MyProfileActivity)(getActivity())).myProfileBinding.backLl.setVisibility(View.GONE);
        changePasswordBinding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(), new MyProfileOptionsFragment(), R.id.profile_frag_container);
            }
        });
        changePasswordBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(), new MyProfileOptionsFragment(), R.id.profile_frag_container);
            }
        });
        changePasswordBinding.yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        return view;
    }

    private void callApi() {
        String new_password=changePasswordBinding.newPasswordET.getText().toString();
        String confirm_password=changePasswordBinding.confirmPasswordET.getText().toString();
        if (new_password.isEmpty()) {
            Common.showToast(getActivity(), "Password cannot be blank !");
            return;
        } else if (!Common.pwdVsalid(new_password)) {
            Common.showToast(getActivity(), "Your password must include 8 characters,at least 1 lower case, 1 upper case, 1 special character, 1 number and no spaces.");
            return;
        }else if (confirm_password.isEmpty()) {
            Common.showToast(getActivity(), "Password cannot be blank !");
            return;
        } else if (!(confirm_password.equals(new_password))) {
            Common.showToast(getActivity(), "Passwords must match.");
            return;
        }

        profileData=((MyProfileActivity)(getActivity())).profileData;
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("id", new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.USERID, ""));
        map.put("first_name", profileData.get(0).getFirstName().toString());
        map.put("last_name", profileData.get(0).getLastName().toString());
        map.put("user_email", profileData.get(0).getUserEmail());
        map.put("date_of_birth", "");
        map.put("date_of_joining", "");
        map1.put("new_password", changePasswordBinding.newPasswordET.getText().toString());
        map1.put("confirm_password", changePasswordBinding.confirmPasswordET.getText().toString());
        map.put("change_password", map1);
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.UPDATEUSERPROFILE, new Gson().toJson(map));
        Common.showLog("map========"+data+"");
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPDATEUSERPROFILE, data, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEUSERPROFILE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Common.showToast(getActivity(), jsonObject.optString("message", ""));
                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new MyProfileOptionsFragment(),R.id.profile_frag_container);
            }
        }else {
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }
}