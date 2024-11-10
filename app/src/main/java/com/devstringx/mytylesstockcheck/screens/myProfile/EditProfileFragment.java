package com.devstringx.mytylesstockcheck.screens.myProfile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentEditProfileBinding;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
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

public class EditProfileFragment extends Fragment implements ResponseListner {
    FragmentEditProfileBinding editProfileBinding;
    public List<DataItem> profileData= new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        if (((MyProfileActivity)(getActivity())).profileData!=null){
            profileData=((MyProfileActivity)(getActivity())).profileData;
            setData(profileData);
        }
        ((MyProfileActivity)(getActivity())).myProfileBinding.backLl.setVisibility(View.GONE);

        editProfileBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MyProfileActivity)(getActivity())).isEditable) {
                    ((MyProfileActivity)(getActivity())).isEditable=false;
                    ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new EditProfileFragment(),R.id.profile_frag_container);
                }else ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new MyProfileOptionsFragment(),R.id.profile_frag_container);
            }
        });
        editProfileBinding.editProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).isEditable = true;
                setToggle();
//                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new EditProfileFragment(),R.id.profile_frag_container);
            }
        });
        editProfileBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyProfileActivity)(getActivity())).isEditable = false;
                setToggle();
//                ((MyProfileActivity)(getActivity())).loadFragment(getParentFragmentManager(),new EditProfileFragment(),R.id.profile_frag_container);
            }
        });
        editProfileBinding.yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        View view = editProfileBinding.getRoot();
        setToggle();
        return view;
    }

    private void setToggle(){
            editProfileBinding.firstNameTL.setEnabled(((MyProfileActivity)(getActivity())).isEditable);
        editProfileBinding.lastNameTL.setEnabled(((MyProfileActivity)(getActivity())).isEditable);
        editProfileBinding.emailTL.setEnabled(((MyProfileActivity)(getActivity())).isEditable);
        editProfileBinding.dobTL.setEnabled(((MyProfileActivity)(getActivity())).isEditable);
        if(((MyProfileActivity)(getActivity())).isEditable) {
            editProfileBinding.editProfileDetails.setVisibility(View.GONE);
            editProfileBinding.profilePageTitle.setText("Edit Profile");
            editProfileBinding.responseLl.setVisibility(View.VISIBLE);
            editProfileBinding.mobilenoET.setFocusable(false);
            editProfileBinding.mobilenoET.setTextColor(R.color.gray);
            editProfileBinding.dobET.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_UP)
                        Common.openCalenderForUptoPresentDate(getActivity(),editProfileBinding.dobET);
                    return false;
                }
            });
        }else{
            editProfileBinding.editProfileDetails.setVisibility(View.VISIBLE);
            editProfileBinding.profilePageTitle.setText("My Profile");
            editProfileBinding.responseLl.setVisibility(View.GONE);
            editProfileBinding.mobilenoET.setTextColor(R.color.gray);
        }
    }

    private void callApi() {
        if (editProfileBinding.firstNameET.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Enter the valid First Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (editProfileBinding.lastNameET.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Enter the valid Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.USERID,""));
        map.put("first_name", editProfileBinding.firstNameET.getText().toString());
        map.put("last_name", editProfileBinding.lastNameET.getText().toString());
        map.put("user_email", editProfileBinding.emailET.getText().toString());
        map.put("date_of_birth", editProfileBinding.dobET.getText().toString());
        map.put("date_of_joining", editProfileBinding.dojET.getText().toString());
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.UPDATEUSERPROFILE, new Gson().toJson(map));
        Common.showLog("map========"+data+"");
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPDATEUSERPROFILE, data, true);
    }

    private void setData(List<DataItem> profileData) {
        editProfileBinding.dojET.setText(profileData.get(0).getDateOfJoining());
        editProfileBinding.emailET.setText(profileData.get(0).getUserEmail());
        editProfileBinding.mobilenoET.setText(String.valueOf(profileData.get(0).getPhoneNumber()));
        editProfileBinding.firstNameET.setText(profileData.get(0).getFirstName());
        editProfileBinding.lastNameET.setText(profileData.get(0).getLastName());
        editProfileBinding.dobET.setText(profileData.get(0).getDateOfBirth());
    }
    private void getUserProfile(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id",id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEUSERPROFILE) {
                if (responseDO.getCode()==200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseDO.getResponse());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    ((MyProfileActivity) (getActivity())).isEditable = false;
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                    getUserProfile(new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.USERID, ""));
                }else Common.showToast(getContext(),responseDO.getResponse());
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                ((MyProfileActivity)(getActivity())).profileData=response.getData();
                ((MyProfileActivity)(getActivity())).setData();
                setToggle();
            }
        }else if (responseDO.getCode()==403) Common.showToast(getContext(),responseDO.getResponse());
    }
}