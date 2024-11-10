package com.devstringx.mytylesstockcheck.screens.vendor;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UserListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentManageUserBinding;
import com.devstringx.mytylesstockcheck.databinding.UserMenuBottomsheetBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.RecordsItem;
import com.devstringx.mytylesstockcheck.interfaces.UpdateUserStatus;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ManageUserFragment extends Fragment implements ResponseListner, UserListAdapter.UserListBtn, UpdateUserStatus {
    private FragmentManageUserBinding userBinding;
    private UserListAdapter adapter;
    private UserMenuBottomsheetBinding menuBottomsheetBinding;
    private String FILTERSTATUS = "";
    private String FILTERDATA = "";
    private PreferenceUtils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_user, container, false);
        View view = userBinding.getRoot();
        utils=new PreferenceUtils(getActivity());
        adapter = new UserListAdapter(getContext(), this);
        userBinding.userListRv.setHasFixedSize(true);
        userBinding.userListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userBinding.userListRv.setAdapter(adapter);

        if(!Common.getPermission(getActivity(),"MU","AEU")){
            userBinding.addLead.setVisibility(View.GONE);
        }
        if(!Common.getPermission(getActivity(),"MU","VEVUV")){
            userBinding.exportIv.setVisibility(View.GONE);
        }
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) {
            if (!Common.getPermission(getActivity(), "MU", "VEAUAA")) {
                userBinding.exportIv.setVisibility(View.GONE);
            } else {
                userBinding.exportIv.setVisibility(View.VISIBLE);
            }
        }

        userBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), true);
            }
        });
        userBinding.addLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                intent.putExtra("title", "Add User");
                startActivityForResult(intent, 3000);
            }
        });
        userBinding.filterIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), FilterUsersActivity.class), 2000);
            }
        });

        userBinding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), false);
            }
        });
        userBinding.exportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getListItems().size() == 0) {
                    Common.showToast(getActivity(), getString(R.string.no_record_found));
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("search", userBinding.searchET.getText().toString());
//                map.put("role", utils.getStringFromPreference(PreferenceUtils.ROLEID, "4"));
                map.put("user_status", FILTERSTATUS);
                FILTERDATA = new Gson().toJson(map);
                exportData();
            }
        });
        getAllOwners("", "", true);
        return view;
    }

    private void exportData() {
        Common.showLog("EXPORT====" + FILTERDATA);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.QUOTATIONDATA, FILTERDATA);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTUSERDATA, map1, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if(data==null)return;
            String receivedHashMap = data.getStringExtra("FilterUserDataMap");
            Common.showLog("===================receivedHashMap===" + receivedHashMap);
            FILTERSTATUS = receivedHashMap;
            getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), true);
        }else{
            getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), true);
        }
    }


//    private void getFilterOwners(String receivedHashMap) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put(NKeys.ALLOWNERSDATA, receivedHashMap);
//        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLOWNERS, map, true);
//    }

    private void getAllOwners(String status, String search, boolean isShowLoader) {
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("role", utils.getStringFromPreference(PreferenceUtils.ROLEID, "4"));
            jsonObject.put("search", search);
            jsonObject.put("user_status", status);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLOWNERS, map, isShowLoader);
    }

    private void openUserBottomSheet(RecordsItem item, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        menuBottomsheetBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.user_menu_bottomsheet, null, false);
        bottomSheetDialog.setContentView(menuBottomsheetBinding.getRoot());
        bottomSheetDialog.show();
        if (item.getUserStatus().toString().equalsIgnoreCase("added")) {
            menuBottomsheetBinding.resendPasswordTv.setVisibility(View.VISIBLE);
            menuBottomsheetBinding.userStatusTv.setText("Activate");
            menuBottomsheetBinding.userStatusTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activate, 0, 0, 0);
        } else {
            menuBottomsheetBinding.resendPasswordTv.setVisibility(View.GONE);
            if (item.getUserStatus().toString().equalsIgnoreCase("active")) {
                menuBottomsheetBinding.userStatusTv.setText("Deactivate");
                menuBottomsheetBinding.userStatusTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_deactivate, 0, 0, 0);
            } else {
                menuBottomsheetBinding.userStatusTv.setText("Activate");
                menuBottomsheetBinding.userStatusTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_activate, 0, 0, 0);
            }
        }
        if(!Common.getPermission(getActivity(),"MU","AEU")){
            menuBottomsheetBinding.editTv.setVisibility(View.GONE);
        }
        if(!Common.getPermission(getActivity(),"MU","AIUPRPU")){
            menuBottomsheetBinding.changePasswordTv.setVisibility(View.GONE);
            menuBottomsheetBinding.deleteUserTv.setVisibility(View.GONE);
            menuBottomsheetBinding.resendPasswordTv.setVisibility(View.GONE);
            menuBottomsheetBinding.userStatusTv.setVisibility(View.GONE);
        }
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3")||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")){
            menuBottomsheetBinding.deleteUserTv.setVisibility(View.GONE);
        }
        menuBottomsheetBinding.editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                intent.putExtra("title", "Edit User");
                intent.putExtra("data", item);
                startActivityForResult(intent,3000);
            }
        });
        menuBottomsheetBinding.changePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangePasswordDialog(item.getId());
                bottomSheetDialog.dismiss();
            }
        });
        menuBottomsheetBinding.deleteUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc="Are you sure you want to delete \n"+Common.getProperCase(item.getFirstName())+" "+Common.getProperCase(item.getLastName())+" ?";
                Common.showStatusDialog(getActivity(),String.valueOf(item.getId()),"Delete User",desc,"DELETE",ManageUserFragment.this);
                bottomSheetDialog.dismiss();
            }
        });
        menuBottomsheetBinding.resendPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc="Are you sure you want to resend password for \n"+Common.getProperCase(item.getFirstName())+" "+Common.getProperCase(item.getLastName())+" ?";
                Common.showStatusDialog(getActivity(),String.valueOf(item.getId()),"Resend Password",desc,"resend",ManageUserFragment.this);
                bottomSheetDialog.dismiss();
            }
        });
        menuBottomsheetBinding.userStatusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getUserStatus().equalsIgnoreCase("active")) {
                    String desc="Are you sure you want to deactivate \n"+Common.getProperCase(item.getFirstName())+" "+Common.getProperCase(item.getLastName())+" ?";
                    Common.showStatusDialog(getActivity(),String.valueOf(item.getId()),"Deactivate User",desc,"inactive",ManageUserFragment.this);
                } else {
                    String desc="Are you sure you want to active \n"+Common.getProperCase(item.getFirstName())+" "+Common.getProperCase(item.getLastName())+" ?";
                    Common.showStatusDialog(getActivity(),String.valueOf(item.getId()),"Activate User",desc,"active",ManageUserFragment.this);
                }
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void callResendPasswordApi(String id) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        data.put(NKeys.RESENDPASSWORD, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_RESENDPASSWORD, data, true);
    }

    private void updateUserStatus(String map) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.UPDATESTATUS, map);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPDATESTATUS, data, true);
    }

    private void openChangePasswordDialog(int id) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.change_password_dialog);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        dialog.setCancelable(false);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel_dialog);
        AppCompatButton save = dialog.findViewById(R.id.save_pwd);
        ImageView cross_iv=dialog.findViewById(R.id.cross_iv);
        cross_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        AppCompatEditText newPassword = dialog.findViewById(R.id.new_password_ET);
        AppCompatEditText confirm_new_password = dialog.findViewById(R.id.confirmNewPasswordET);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_password=newPassword.getText().toString();
                String confirm_password=confirm_new_password.getText().toString();
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
                dialog.dismiss();
                callChangePasswordApi(new_password, confirm_password, id);

            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(com.nareshchocha.filepickerlibrary.R.drawable.transparent);
        dialog.show();
    }

    private void callChangePasswordApi(String newPassword, String confirmNewPassword, int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("new_password", newPassword);
        map.put("confirm_password", confirmNewPassword);
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.CHANGEPASSWORDFORUSER, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_CHANGEPASSWORDFORUSER, data, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (userBinding.refreshLayout.isRefreshing())
            userBinding.refreshLayout.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLOWNERS) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                if(allOwnersData.getData().getRecords().size()==0){
                    userBinding.noUserRecordTv.setVisibility(View.VISIBLE);
                }else{
                    userBinding.noUserRecordTv.setVisibility(View.GONE);
                }
                adapter.refreshList(allOwnersData.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CHANGEPASSWORDFORUSER) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Common.showToast(getActivity(), jsonObject.optString("message", ""));
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATESTATUS) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), true);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Common.showToast(getActivity(), jsonObject.optString("message", ""));
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_RESENDPASSWORD) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    getAllOwners(FILTERSTATUS, userBinding.searchET.getText().toString(), true);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Common.showToast(getActivity(), jsonObject.optString("message", ""));
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTUSERDATA) {
                try {
                    JSONObject jsonObject=new JSONObject(responseDO.getResponse());
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optJSONObject("records").optString("url")));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onBtnClickListner(int position, String type, boolean b) {

    }

    @Override
    public void onBtnClickListner(RecordsItem item, int position) {
        openUserBottomSheet(item, position);
    }

    @Override
    public void updateUserStatusListner(String id, String StageName, String comment) {
        if(StageName.equalsIgnoreCase("resend")){
            callResendPasswordApi(id);
        }else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            if (StageName.equalsIgnoreCase("DELETE")) {
                map.put("user_status", "deactivated");
            } else if (StageName.equalsIgnoreCase("inactive") || StageName.equalsIgnoreCase("active")) {
                map.put("user_status", StageName);
            }
            updateUserStatus(new Gson().toJson(map));
        }

    }
}