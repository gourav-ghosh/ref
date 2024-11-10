package com.devstringx.mytylesstockcheck.screens.vendor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NewInquiryAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentManageInquiriesBinding;
import com.devstringx.mytylesstockcheck.filter.FilterActivity;
import com.devstringx.mytylesstockcheck.screens.fragments.AllLeadsFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.fragment.InquiriesFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.interfaces.UpdateInquiriesCount;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ManageInquiriesFragment extends Fragment implements  ResponseListner {
    public FragmentManageInquiriesBinding manageInquiriesBinding;
    InquiriesFragment inquiriesFragment;
    public String currTab = "new_inquiry";
    public String code = "new_inquiry";
    private String FILTERDATA="",TOTALPRODUCTCOUNT="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manageInquiriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_inquiries, container, false);
        View view = manageInquiriesBinding.getRoot();
        inquiriesFragment=new InquiriesFragment(ManageInquiriesFragment.this);
        if(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("2")) {
            manageInquiriesBinding.delayedInquiryRb.setVisibility(View.VISIBLE);
            currTab = "delayed_inquiry";
            code = "delayed_inquiry";
        }
        if(manageInquiriesBinding.delayedInquiryRb.getVisibility() == View.VISIBLE)
            manageInquiriesBinding.delayedInquiryRb.setChecked(true);
        else
            manageInquiriesBinding.newInquiryRb.setChecked(true);
        try {
            JSONObject object=new JSONObject();
            object.put("enquiry_tab",code);
            object.put("sort", "createdDateDesc");
            FILTERDATA=object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadFragment(inquiriesFragment,code);

        manageInquiriesBinding.inquiryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),InquiryFilterActivity.class);
                intent.putExtra("code",code);
                startActivityForResult(intent,222);
            }
        });
        if(!Common.getPermission(getActivity(),"SCE","VEAIU")){
            manageInquiriesBinding.exportIv.setVisibility(View.GONE);
        }
        if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) {
            if (!Common.getPermission(getActivity(), "SCE", "VEAIA")) {
                manageInquiriesBinding.exportIv.setVisibility(View.GONE);
            } else {
                manageInquiriesBinding.exportIv.setVisibility(View.VISIBLE);
            }
        }
        manageInquiriesBinding.exportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TOTALPRODUCTCOUNT.equalsIgnoreCase("")||TOTALPRODUCTCOUNT.equalsIgnoreCase("0")){
                    Common.showToast(getActivity(),getString(R.string.no_record_found));
                    return;
                }
                if(FILTERDATA.equalsIgnoreCase("")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("enquiry_tab", code);
                    map.put("sort", "createdDateDesc");
                    FILTERDATA=new Gson().toJson(map);
                }
                exportInquiryData();
            }
        });
        manageInquiriesBinding.supportCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openDialerPad(getActivity(),"7785882840");
            }
        });
        manageInquiriesBinding.inquiryRbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (manageInquiriesBinding.delayedInquiryRb.isChecked()){
                    inquiriesFragment=new InquiriesFragment(ManageInquiriesFragment.this);
                    code="delayed_inquiry";
                    currTab = "delayed_inquiry";
                } else if (manageInquiriesBinding.newInquiryRb.isChecked()){
                    inquiriesFragment=new InquiriesFragment(ManageInquiriesFragment.this);
                    code="new_inquiry";
                    currTab = "new_inquiry";
                } else if (manageInquiriesBinding.respondedInquiryRb.isChecked()) {
                    inquiriesFragment=new InquiriesFragment(ManageInquiriesFragment.this);
                    code="responded_inquiry";
                    currTab="responded_inquiry";
                } else {
                    inquiriesFragment=new InquiriesFragment(ManageInquiriesFragment.this);
                    code="order_placed_inquiry";
                    currTab = "order_placed_inquiry";
                }
                if (Common.countDownTimer != null) {
                    Common.countDownTimer.onFinish();
                }
                if(FILTERDATA.equalsIgnoreCase("")){
                    try {
                        JSONObject object=new JSONObject();
                        object.put("enquiry_tab",code);
                        object.put("sort", "createdDateDesc");
                        FILTERDATA=object.toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        JSONObject object=new JSONObject(FILTERDATA);
                        object.put("enquiry_tab",code);
                        object.put("sort", "createdDateDesc");
                        FILTERDATA=object.toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadFragment(inquiriesFragment,code);
            }
        });
        manageInquiriesBinding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filterData="";
                if(FILTERDATA.equalsIgnoreCase("")){
                    try {
                        JSONObject object=new JSONObject();
                        object.put("search",manageInquiriesBinding.searchET.getText().toString());
                        object.put("enquiry_tab",code);
                        object.put("sort", "createdDateDesc");
                        filterData=object.toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        JSONObject object=new JSONObject(FILTERDATA);
                        object.put("search",manageInquiriesBinding.searchET.getText().toString());
                        object.put("sort", "createdDateDesc");
                        filterData=object.toString();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Common.showLog("FILTERDATA========="+filterData);
                FILTERDATA=filterData.toString();
                inquiriesFragment.getFilterInquiries(filterData,false);
            }
        });
        return view;
    }

    private void loadFragment(Fragment fragment, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("FILTERDATA", FILTERDATA);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.inquiry_frag_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
//    @Override
//    public void updateInquiriesCountListner(String count) {
//        TOTALPRODUCTCOUNT=count;
//        manageInquiriesBinding.enquiryCount.setText("Total Number of Inquiries : "+count);
//    }

    public void setTOTALPRODUCTCOUNT(String TOTALPRODUCTCOUNT) {
        this.TOTALPRODUCTCOUNT = TOTALPRODUCTCOUNT;
    }

    private void exportInquiryData() {
        Common.showLog("EXPORT===="+FILTERDATA);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.QUOTATIONDATA,FILTERDATA);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTQUOTATIONDATA, map1, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==222){
            if(data!=null){
                String receivedHashMap =  data.getStringExtra("FilterInquiryDataMap");
                Common.showLog("===================receivedHashMap==="+receivedHashMap);
                FILTERDATA=receivedHashMap;
                if(getActivity()==null || inquiriesFragment==null)return;
                inquiriesFragment.getFilterInquiries(receivedHashMap,true);
            }
        }
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTQUOTATIONDATA) {
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
}