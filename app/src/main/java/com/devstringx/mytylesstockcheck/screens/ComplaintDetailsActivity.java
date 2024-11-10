package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.ComplaintCommentAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.customSwipeButton.CustomSwipeButton;
import com.devstringx.mytylesstockcheck.databinding.ActivityComplaintDetailsBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.ComplaintCommentResponse;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintDetailsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.Data;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.fragments.ComplaintDetailsTabFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ComplaintStatusTabFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ProofOfComplaintTabFragment;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplaintDetailsActivity extends AppCompatActivity implements ResponseListner, UploadFileAdapter.removeImage {
    ActivityComplaintDetailsBinding binding;
    public Data complaintDetailResponse;
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    public List<RecordsItem> allOwnersList = new ArrayList<>();
    public List<String> agentNameList = new ArrayList<>();
    private CustomAutoCompleteListAdapter deliveryAgentAdapter;
    private RecyclerView commentsRV;
    private RelativeLayout noCommentTV;
    private TextView commentET;
    List<String> solutionList;
    private ComplaintCommentAdapter complaintCommentAdapter;
    private String agent_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complaint_details);
        if(!Common.getPermission(this,"CMS","DCMS")) {
            binding.deleteIv.setVisibility(View.GONE);
        }
        getComplaintDetails();
        getUsersByRoleName();
        binding.msgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComplaintCommentDialog();
            }
        });
        binding.complaintDetailBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showComplaintCommentDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.complaint_chat_dialog);
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        TextView title = dialog.findViewById(R.id.titleTV);
        title.setText("Comment on complaint");
        commentsRV = dialog.findViewById(R.id.comments_rv);
        ImageView send = dialog.findViewById(R.id.sendIV);
        commentET = dialog.findViewById(R.id.commentET);
        noCommentTV = dialog.findViewById(R.id.no_comment_tv);
        getComplaintCommentApi();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentET.getText().toString().isEmpty()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("complaint_id", getIntent().getStringExtra("id"));
                    map.put("comment", commentET.getText().toString());
                    map.put(NKeys.CREATECOMPLAINTCOMMENT, new Gson().toJson(map));
                    callCreateComplaintCommentApi(map);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callCreateComplaintCommentApi(HashMap<String, Object> map) {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CREATECOMPLAINTCOMMENT, map, true);
    }

    private void getComplaintCommentApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("complaint_id", getIntent().getStringExtra("id"));
        map.put(NKeys.GETCOMPLAINTCOMMENT, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETCOMPLAINTCOMMENT, map, true);
    }

    private void setupCommentAdapter(Context context , List<com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments.RecordsItem> records) {
        complaintCommentAdapter = new ComplaintCommentAdapter(context,records);
        commentsRV.setHasFixedSize(true);
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        commentsRV.setAdapter(complaintCommentAdapter);
    }

    public void getComplaintDetails() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("complaint_id", getIntent().getStringExtra("id"));
        map.put(NKeys.GETCOMPLAINTDETAILS, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETCOMPLAINTDETAILS, map, true);
    }

    private void deleteComplaint(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("complaint_id", id);
        map.put(NKeys.DELETECOMPLAINT, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_DELETECOMPLAINT, map, true);
    }

    private void showProcessComplaintDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.process_complaint_dialog);
        RadioButton mytyles = dialog.findViewById(R.id.mytyles_rb);
        RadioButton vendor = dialog.findViewById(R.id.vendor_rb);
        RadioGroup radioGroup = dialog.findViewById(R.id.rg);
        TextInputLayout vendor_name_layout = dialog.findViewById(R.id.vendor_name_Layout);
        AppCompatEditText vendor_name = dialog.findViewById(R.id.vendor_name);
        TextInputLayout agent = dialog.findViewById(R.id.agentInput);
        AutoCompleteTextView agentAssigned = dialog.findViewById(R.id.agent_name);
        TextView confirmBtn = dialog.findViewById(R.id.processBtn);
        ImageView close = dialog.findViewById(R.id.process_crossIV);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.swipeBtn.collapseButton();
                dialog.dismiss();
            }
        });
        agentAssigned.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, agentNameList));
        mytyles.setChecked(true);
        if (mytyles.isChecked()) {
            agent.setVisibility(View.VISIBLE);
            vendor_name_layout.setVisibility(View.GONE);
        } else {
            vendor_name_layout.setVisibility(View.VISIBLE);
            agent.setVisibility(View.GONE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mytyles.isChecked()) {
                    agent.setVisibility(View.VISIBLE);
                    vendor_name_layout.setVisibility(View.GONE);
                } else {
                    vendor_name_layout.setVisibility(View.VISIBLE);
                    agent.setVisibility(View.GONE);
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("complaint_id", getIntent().getStringExtra("id"));
                if (vendor.isChecked()) {
                    if (vendor_name.getText().toString().trim().isEmpty()) {
                        Common.showToast(ComplaintDetailsActivity.this, "Please Enter The Vendor Name");
                        return;
                    }
                    map.put("issue_from", "Vendor");
                    map.put("vendor_name", vendor_name.getText().toString());
                }
                if (mytyles.isChecked()) {
                    if (agentAssigned.getText().toString().trim().isEmpty()) {
                        Common.showToast(ComplaintDetailsActivity.this, "Please select an agent");
                        return;
                    }
                    for (int i = 0; i < allOwnersList.size(); i++) {
                        if (agentAssigned.getText().toString().equalsIgnoreCase(allOwnersList.get(i).getCityName().toString())) {
                            agent_id = allOwnersList.get(i).getId();
                        }
                    }
                    map.put("issue_from", "Mytyles");
                    map.put("agent_id", agent_id);
                }
                callComplaintProcessApi(new Gson().toJson(map));
            }
        });
        dialog.show();
    }

    private void callComplaintProcessApi(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.COMPLAINTPROCESS, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_COMPLAINTPROCESS, map, true);
    }

    private void showCustomDialog(String button_msg, String msg, String title_msg) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.order_delete_dialog);
        dialog.setCancelable(false);
        ImageView cross = dialog.findViewById(R.id.order_dialog_crossIV);
        TextView title = dialog.findViewById(R.id.order_dialog_titleTV);
        TextView text = dialog.findViewById(R.id.order_dialog_text);
        TextView yes = dialog.findViewById(R.id.order_dialog_yes);
        TextView no = dialog.findViewById(R.id.order_dialog_no);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComplaint(getIntent().getStringExtra("id"));
            }
        });
        title.setText(title_msg);
        text.setText(msg);
        yes.setText(button_msg);
        dialog.show();
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCOMPLAINTDETAILS) {
                ComplaintDetailsResponse response = new Gson().fromJson(responseDO.getResponse(), ComplaintDetailsResponse.class);
                complaintDetailResponse = response.getData();
                setData(response.getData());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETECOMPLAINT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_COMPLAINTPROCESS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCOMPLAINTFILTERVALUE) {
                String jsonResponse = responseDO.getResponse().toString();
                JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray recordsArray = dataObject.getAsJsonArray("records");

                solutionList = new ArrayList<>();
                for (int i = 0; i < recordsArray.size(); i++) {
                    solutionList.add(recordsArray.get(i).getAsString());
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_RESOLVECOMPLAINT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCOMPLAINTCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        ComplaintCommentResponse response = new Gson().fromJson(responseDO.getResponse(), ComplaintCommentResponse.class);
                        if (response.getData().getCount()==0){
                            noCommentTV.setVisibility(View.VISIBLE);
                            commentsRV.setVisibility(View.GONE);
                        }else {
                            noCommentTV.setVisibility(View.GONE);
                            commentsRV.setVisibility(View.VISIBLE);
                        }
                        setupCommentAdapter(this,response.getData().getRecords());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATECOMPLAINTCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        commentET.setText("");
                        getComplaintCommentApi();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    allOwnersList.add(recordsItem);
                }
                for (int i = 0; i < allOwnersList.size(); i++) {
                    agentNameList.add(allOwnersList.get(i).getCityName().toString());
                }
                getSolutionsList();
            }
        } else {
                Common.showToast(this, responseDO.getResponse());
        }
    }

    private void getSolutionsList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("filter_value", "resolve_soluction");
        map.put(NKeys.GETCOMPLAINTFILTERVALUE, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETCOMPLAINTFILTERVALUE, map, true);
    }

    private void getUsersByRoleName() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Delivery Agent");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }

    private void setData(Data data) {
        if (data.getComplaintStatus().equalsIgnoreCase("Resolved")) {
            binding.tagCV.setCardBackgroundColor(getResources().getColor(R.color.green));
            binding.tagTV.setText("Resolved");
            binding.deleteIv.setVisibility(View.GONE);
            binding.swipeBtn.setVisibility(View.GONE);
            binding.tagTV.setTextColor(getResources().getColor(R.color.white));
        } else if (data.getComplaintStatus().equalsIgnoreCase("Inprogress")) {
            binding.tagCV.setCardBackgroundColor(getResources().getColor(R.color.faded_orange));
            binding.tagTV.setText("Inprogress");
            binding.swipeBtn.setText("Swipe to Resolve");
            binding.tagTV.setTextColor(getResources().getColor(R.color.orange));
        } else {
            binding.tagCV.setCardBackgroundColor(getResources().getColor(R.color.darker_Blue));
            binding.tagTV.setText("Created");
            binding.swipeBtn.setText("Swipe to Inprogress");
            binding.tagTV.setTextColor(getResources().getColor(R.color.white));
        }
        binding.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Delete", "want to delete this order  permanently?", "Confirm Delete");

            }
        });
        binding.swipeBtn.setOnSwipeListener(new CustomSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipe() {
                if (data.getComplaintStatus().equalsIgnoreCase("Created"))
                    showProcessComplaintDialog(ComplaintDetailsActivity.this);
                else
                    showResolveComplaintDialog(ComplaintDetailsActivity.this);
            }
        });
        if (data.getComplaintActivities().getCancelled() != null) {
            binding.tagCV.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.tagTV.setText("Cancelled");
            binding.progressRL.setVisibility(View.GONE);
            binding.tagTV.setTextColor(getResources().getColor(R.color.white));
        } else {
            binding.progressRL.setVisibility(View.VISIBLE);
        }
        binding.ticketNo.setText("Ticket No." + "\"" + data.getTicketNumber() + "\"");
        binding.erdTv.setText("ERD " + data.getEstimateResolveDate());
        if (data.getComplaintActivities().getCreated() != null) {
            binding.createdIv.setBackgroundResource(R.drawable.green_fill_circle);
            if (data.getComplaintActivities().getInprogress() != null) {
                binding.inprogressView.setBackgroundResource(R.color.green);
                binding.inprogressIv.setBackgroundResource(R.drawable.green_fill_circle);
                if (data.getComplaintActivities().getResolved() != null) {
                    binding.resolvedView.setBackgroundResource(R.color.green);
                    binding.resolvedIv.setBackgroundResource(R.drawable.green_fill_circle);
                }
            }
        }
        binding.complaintDetailRb.setChecked(true);
        Common.loadFragment(getSupportFragmentManager(), new ComplaintDetailsTabFragment(), R.id.complaint_detail_view_pager);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.complaintDetailRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ComplaintDetailsTabFragment(), R.id.complaint_detail_view_pager);
                } else if (binding.proofOfComplaintRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new ProofOfComplaintTabFragment(), R.id.complaint_detail_view_pager);
                } else {
                    Common.loadFragment(getSupportFragmentManager(), new ComplaintStatusTabFragment(), R.id.complaint_detail_view_pager);
                }
            }
        });
    }

    private void showResolveComplaintDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.resolve_complaint_dialog);
        dialog.setCancelable(false);
        AutoCompleteTextView solutionDropdown = dialog.findViewById(R.id.solution);
        AppCompatEditText other_solution = dialog.findViewById(R.id.other_solutionET);
        TextInputLayout otherSolutionLayout = dialog.findViewById(R.id.other_solutionLayout);
        TextView selectImg = dialog.findViewById(R.id.resolve_dialog_select_img);
        AppCompatEditText cost = dialog.findViewById(R.id.cost_to_mytylesET);
        AppCompatEditText comment = dialog.findViewById(R.id.commentET);
        TextView confirm = dialog.findViewById(R.id.resolveBtn);
        RecyclerView recyclerView = dialog.findViewById(R.id.resolve_upload_img_rv);
        ImageView cross = dialog.findViewById(R.id.resolve_dialog_crossIV);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.swipeBtn.collapseButton();
                dialog.dismiss();
            }
        });
        solutionDropdown.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, solutionList));
        solutionDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (solutionDropdown.getText().toString().equalsIgnoreCase("Others")) {
                    otherSolutionLayout.setVisibility(View.VISIBLE);
                } else {
                    otherSolutionLayout.setVisibility(View.GONE);
                }
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                mMimeTypesList.add("video/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(ComplaintDetailsActivity.this)
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
        });
        setupAdapter(imagesArrayList, recyclerView);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("complaint_id", getIntent().getStringExtra("id"));
                if (!solutionDropdown.getText().toString().isEmpty()) {
                    map.put("solution", solutionDropdown.getText().toString());
                    if (solutionDropdown.getText().toString().equalsIgnoreCase("Others")) {
                        map.put("other_solution", other_solution.getText().toString());
                    }
                    if (!comment.getText().toString().isEmpty()) {
                        map.put("comment", comment.getText().toString());
                    } else Common.showToast(dialog.getContext(), "Please enter the comment.");
                    map.put("cost_to_mytyles", cost.getText().toString());
                    map.put("file", SELECTEDFILES);
                    new NetworkRequest(ComplaintDetailsActivity.this, ComplaintDetailsActivity.this).callWebServices(ServiceMethods.WS_RESOLVECOMPLAINT, map, true);
                } else {
                    Common.showToast(dialog.getContext(), "Please select a solution");
                }
            }
        });
        dialog.show();
    }

    private void setupAdapter(ArrayList<Images> imagesArrayList, RecyclerView recyclerView) {
        imageAdapter = new UploadFileAdapter(this, imagesArrayList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(imageAdapter);
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
                                        float size = testFile.length() / (1024 * 1024);
                                        if (size <= 2) {
                                            SELECTEDFILES.add(listData);
                                            ArrayList<String> imgs = new ArrayList<>();
                                            imgs.add(listData);
                                            Images images = new Images();
                                            images.setLeadAttachment(imgs);
                                            imagesArrayList.add(images);
                                            imageAdapter.refreshList(imagesArrayList);
                                        } else {
                                            Common.showToast(ComplaintDetailsActivity.this, "Please upload a file smaller than 2 MB.");
                                        }
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

    @Override
    public void removeImageListner(int position) {
        if (imagesArrayList.get(position).getId() != 0) {
            removedImagesArrayList.add(imagesArrayList.get(position));
        } else {
            SELECTEDFILES.remove(position);
        }
        imagesArrayList.remove(position);
        imageAdapter.refreshList(imagesArrayList);
    }
}