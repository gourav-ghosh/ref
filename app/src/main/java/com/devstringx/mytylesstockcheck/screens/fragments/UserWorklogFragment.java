package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.adapter.UserWorklogAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentUserWorklogBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.userWorklog.Data;
import com.devstringx.mytylesstockcheck.datamodal.userWorklog.Response;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.WorklogDetailsActivity;
import com.devstringx.mytylesstockcheck.screens.userWorklogFilter.UserWorklogFilterActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserWorklogFragment extends Fragment implements UploadFileAdapter.removeImage, ResponseListner {
    FragmentUserWorklogBinding binding;
    UserWorklogAdapter adapter;
    Dialog employeeAbsentDialog;
    Dialog employeePresentDialog;
    com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response response;
    private String comment;
    private String hoursWorked;
    private RecyclerView dialogRecyclerView;
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private boolean overtime = false;
    private int eligibleForOvertime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_worklog, container, false);
        View view = binding.getRoot();
        setupAdapter();
        getUserProfile(new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.USERID, "").toString());
        binding.refreshLl.setRefreshing(false);
//        TODO have to take care of back button
        binding.backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setHashmapData(binding.searchET.getText().toString());
            }
        });
        binding.userWorklogFilterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), UserWorklogFilterActivity.class), 200);
            }
        });
        binding.employeeWorklogFilterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), UserWorklogFilterActivity.class), 200);
            }
        });
        binding.exportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportWorklog();
            }
        });
        binding.employeeExportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportWorklog();
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setHashmapData(binding.searchET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.absentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAbsentDialog();
            }
        });
        binding.presentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPresentDialog();
            }
        });
        checkUserRoles();

        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.exportIV.setVisibility(View.INVISIBLE);
        }
        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "SCE", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.exportIV.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void getUserProfile(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, false);
    }

    private void openAbsentDialog() {
        employeeAbsentDialog = new Dialog(getContext());
        employeeAbsentDialog.setContentView(R.layout.employee_absent_dialog);
        employeeAbsentDialog.setCancelable(true);
        ImageView close = employeeAbsentDialog.findViewById(R.id.absent_dialog_crossIV);
        TextView cancel = employeeAbsentDialog.findViewById(R.id.absent_dialog_no);
        TextView confirm = employeeAbsentDialog.findViewById(R.id.absent_dialog_yes);
        AppCompatEditText reason = employeeAbsentDialog.findViewById(R.id.reasonET);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeAbsentDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeAbsentDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!reason.getText().toString().isEmpty()) {
                    comment = reason.getText().toString();
                    callCreateWorklogAPI("absent");
                }
                else {
                    Common.showToast(getContext(), "Please mention your reason");
                }
            }
        });
        employeeAbsentDialog.show();
    }
    private void callCreateWorklogAPI(String status) {
        Date today=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate= sdf.format(today);
        HashMap<String, Object> map = new HashMap<>();
        map.put("worklogStatus", status);
        map.put("comment", comment);
        map.put("workingDate", todayDate);
        if(status.equalsIgnoreCase("present")) {
            map.put("overTime", overtime);
            if(overtime)
                map.put("hoursWorked", hoursWorked);
            map.put("file", SELECTEDFILES);
        }
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_CREATEWORKLOG, map, true);
    }

    private void openPresentDialog() {
        employeePresentDialog = new Dialog(getContext());
        employeePresentDialog.setContentView(R.layout.employee_present_dialog);
        employeePresentDialog.setCancelable(true);
        ImageView close = employeePresentDialog.findViewById(R.id.present_dialog_crossIV);
        AppCompatEditText workDetails = employeePresentDialog.findViewById(R.id.workDetailsET);
        RelativeLayout overtimeRL = employeePresentDialog.findViewById(R.id.overtimeRL);
        ImageView overtimeCB = employeePresentDialog.findViewById(R.id.overtimeCB);
        EditText hoursWorkedET = employeePresentDialog.findViewById(R.id.hoursWorkedET);
        TextView upload = employeePresentDialog.findViewById(R.id.present_dialog_upload_btn);
        TextView selectImg = employeePresentDialog.findViewById(R.id.present_select_img);
        TextView formatTV = employeePresentDialog.findViewById(R.id.presentFormatTV);
        dialogRecyclerView = employeePresentDialog.findViewById(R.id.present_upload_img_rv);
        setupUploadedImageAdapter();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeePresentDialog.dismiss();
            }
        });
        if(eligibleForOvertime != 1) {
            overtimeRL.setVisibility(View.GONE);
            overtime = false;
        }
        else overtimeRL.setVisibility(View.VISIBLE);
        overtimeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overtime) {
                    overtime = false;
                    overtimeCB.setImageResource(R.drawable.orange_checkbox_unselected);
                    hoursWorkedET.setVisibility(View.GONE);
                }
                else {
                    overtime = true;
                    overtimeCB.setImageResource(R.drawable.orange_checkbox_selected);
                    hoursWorkedET.setVisibility(View.VISIBLE);
                }
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImageLauncher();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workDetails.getText().toString() == null || workDetails.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Please mention work details");
                    return;
                }
                else comment = workDetails.getText().toString();
                if(overtime && (hoursWorkedET.getText().toString().isEmpty() || hoursWorkedET.getText().toString() == null)) {
                    Common.showToast(getContext(), "Please enter extra working hours");
                    return;
                } else if (overtime) {
                    hoursWorked = hoursWorkedET.getText().toString();
                }
                callCreateWorklogAPI("present");
            }
        });
        employeePresentDialog.show();
    }
    private void setupUploadedImageAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }
    private void SelectImageLauncher() {
        ArrayList<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("image/*");
        mMimeTypesList.add("application/pdf");
        mMimeTypesList.add("video/mp4");
        captureImageResultLauncher.launch(
                new FilePicker.Builder(getContext())
                        .pickDocumentFileBuild(
                                new DocumentFilePickerConfig(
                                        null,
                                        null,
                                        true,
                                        10,
                                        mMimeTypesList,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                        )
        );
    }
    private final ActivityResultLauncher<Intent> captureImageResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            try {
                                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData().getData() != null) {
                                        String listData = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                        File testFile = new File(listData);
//                                        float size = testFile.length() / (1024 * 1024);
//                                        if (size <= 2) {
                                        SELECTEDFILES.add(listData);
                                        ArrayList<String> imgs = new ArrayList<>();
                                        imgs.add(listData);
                                        Images images = new Images();
                                        images.setLeadAttachment(imgs);
                                        imagesArrayList.add(images);
                                        imageAdapter.refreshList(imagesArrayList);
                                        Common.showLog("qwert==" + imagesArrayList);
                                        if (!imagesArrayList.isEmpty())
                                            dialogRecyclerView.setVisibility(View.VISIBLE);
                                        else dialogRecyclerView.setVisibility(View.GONE);
//                                        } else {
//                                            Common.showToast(getContext(), "Please upload a file smaller than 2 MB.");
//                                        }
                                    } else {
//                                        ArrayList<Uri> listData = getClipDataUris(result.getData());
//                                        // ArrayList<String> listData = result.getData().getStringArrayListExtra(Const.BundleExtras.FILE_PATH_LIST);
//                                        uriList.addAll(listData);
                                    }
                                }
                            } catch (Exception e) {
                                Common.showLog(e.getMessage());
                            }
                        }
                    });


    private void checkUserRoles() {
        if(new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("12") || new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("1")) {
            binding.hrTopOptions.setVisibility(View.VISIBLE);
            binding.employeeTopOptions.setVisibility(View.GONE);
            binding.employeeWorklogFilterIV.setVisibility(View.GONE);
            binding.employeeExportIV.setVisibility(View.GONE);
            binding.userDetailsRL.setVisibility(View.VISIBLE);
//            binding.backIV.setVisibility(View.VISIBLE);
        }
        else {
            binding.hrTopOptions.setVisibility(View.GONE);
            binding.employeeTopOptions.setVisibility(View.VISIBLE);
            binding.employeeWorklogFilterIV.setVisibility(View.VISIBLE);
            binding.employeeExportIV.setVisibility(View.VISIBLE);
            binding.userDetailsRL.setVisibility(View.GONE);
//            binding.backIV.setVisibility(View.GONE);
        }
    }

    private void exportWorklog() {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("search", binding.searchET.getText().toString());
        map.put("dateRange", Common.userWorklogDateType);
        map.put("fromDate", Common.userWorklogStartDate);
        map.put("toDate", Common.userWorklogEndDate);
        map.put("userId", Common.userWorklogUserId);
        map1.put(NKeys.EXPORTWORKLOG, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_EXPORTWORKLOG, map1, true);
    }

    private void setupAdapter() {
        RecyclerView recyclerView = binding.userWorklogCardRv;
        adapter = new UserWorklogAdapter(getContext(), this::onItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
    public void onItemClick(String id) {
        Intent intent=new Intent(getActivity(), WorklogDetailsActivity.class);
        intent.putExtra("worklogId", id);
        Common.showLog("worklogId==="+id);
        startActivityForResult(intent, 002);
    }
    private void setHashmapData(String searchText) {
        String id;
        if(new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("12") || new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("1"))
            id = getArguments().getString("userId");
        else id = new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.USERID,"");
        Common.userWorklogUserId = Integer.parseInt(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", searchText);
        map.put("sort", Common.userWorklogSortBy);
        map.put("dateRange", Common.userWorklogDateType);
        map.put("fromDate", Common.userWorklogStartDate);
        map.put("toDate", Common.userWorklogEndDate);
        map.put("userId", Common.userWorklogUserId);
        getFilterUserWorklogs(new Gson().toJson(map), false);
    }
    private void getFilterUserWorklogs(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETSINGLEUSERALLWORKLOGS, receivedHashMap);
        new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_GETSINGLEUSERALLWORKLOGS, map, isLoader);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            if (data!=null) {
                String receivedHashMap = data.getStringExtra("UserWorklogFilterDataMap");
                getFilterUserWorklogs(receivedHashMap, true);
            }
        }
    }
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE==="+ responseDO.getServiceMethods());
        if(binding.refreshLl.isRefreshing()) binding.refreshLl.setRefreshing(false);
        if(!responseDO.isError()) {
            if(responseDO.getServiceMethods() == ServiceMethods.WS_GETSINGLEUSERALLWORKLOGS) {
                Response userWorklogResponse = new Gson().fromJson(responseDO.getResponse(), Response.class);
                setData(userWorklogResponse.getData());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTWORKLOG) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optJSONObject("records").optString("url")));
                    startActivity(intent);
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATEWORKLOG) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
//                    if(jsonObject.optString("status").equalsIgnoreCase("200")) {
                        if(employeePresentDialog.isShowing()) employeePresentDialog.dismiss();
                        if(employeeAbsentDialog.isShowing()) employeeAbsentDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        setHashmapData("");
//                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException();
                }
            } else if(responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                response = new Gson().fromJson(responseDO.getResponse(), com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response.class);
                eligibleForOvertime = response.getData().get(0).getEligibleForOvertime();
            }
        }
        else{
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }
    private void setData(Data data) {
        if(data.getUserDetails().getProfileImage()!=null) {
            ImageView profileImage = binding.profileImgIV;
            Glide.with(getContext()).load(data.getUserDetails().getProfileImage()).transform(new CenterCrop(), new CircleCrop()).into(profileImage);
        }
        else {
            binding.profileImgIV.setImageResource(R.drawable.ic_worklog_profile);
        }
        binding.userNameTV.setText(data.getUserDetails().getUserFirstName()+ " " + data.getUserDetails().getUserLastName());
        binding.roleTV.setText(data.getUserDetails().getUserRoleName());
        binding.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getContext(), data.getUserDetails().getUserPhone());
            }
        });
        if(data.getRecords().size()>0) {
            binding.noWorklogTV.setVisibility(View.GONE);
            binding.refreshLl.setVisibility(View.VISIBLE);
            adapter.refreshList(data.getRecords());
        }
        else {
            binding.refreshLl.setVisibility(View.GONE);
            binding.noWorklogTV.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        setHashmapData("");
    }

    @Override
    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
        if (imagesArrayList.isEmpty()) dialogRecyclerView.setVisibility(View.GONE);
        else dialogRecyclerView.setVisibility(View.VISIBLE);
    }
}