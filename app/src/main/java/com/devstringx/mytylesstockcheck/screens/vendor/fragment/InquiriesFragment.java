package com.devstringx.mytylesstockcheck.screens.vendor.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NewInquiryAdapter;
import com.devstringx.mytylesstockcheck.adapter.OtherInquiryAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentInquiriesBinding;
import com.devstringx.mytylesstockcheck.databinding.FragmentManageInquiriesBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsResponse;
import com.devstringx.mytylesstockcheck.interfaces.OnItemClickListener;
import com.devstringx.mytylesstockcheck.screens.vendor.InquiryDetailActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.ManageInquiriesFragment;
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

public class InquiriesFragment extends Fragment implements OnItemClickListener, ResponseListner , NewInquiryAdapter.SendResponse {
    FragmentInquiriesBinding inquiriesBinding;
    NewInquiryAdapter newInquiryAdapter;
    OtherInquiryAdapter otherInquiryAdapter;
    ManageInquiriesFragment manageInquiriesFragment;
    private String Screen="";
    private String FILTERDATA="";
    private String tag;

    public InquiriesFragment(ManageInquiriesFragment fragment) {
        manageInquiriesFragment=fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inquiriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_inquiries, container, false);
        View view = inquiriesBinding.getRoot();
        FILTERDATA= getArguments().getString("FILTERDATA");
        tag=getTag();
        Common.showLog(tag+"===");
        Common.showLog("tag=="+FILTERDATA);
        getFilterInquiries(FILTERDATA,true);
        inquiriesBinding.enquiryCount.setText("Total Number of Inquiries : 0");
        if (tag.equalsIgnoreCase("new_inquiry") || tag.equalsIgnoreCase("delayed_inquiry")){
            setupNewInwuiryAdapter();
        } else {
            setupOtherInquiryAdapter();
        }
        inquiriesBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(FILTERDATA.equalsIgnoreCase(""))
                    getAllInquiries(tag);
                else getFilterInquiries(FILTERDATA,true);
            }
        });

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(Screen.isEmpty()) {
//            String tag = getTag();
//            getAllInquiries(tag);
//        }
//    }

    @Override
    public void onPause() {
        if(Common.countDownTimer!=null){
            Common.stopTimer();
        }else{
            if(newInquiryAdapter!=null){
                if(newInquiryAdapter.checkIsPopupTrue()){
                    newInquiryAdapter.closePopup();
                }
            }
        }
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }
    private void getAllInquiries(String tab) {
        Screen="";
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("id", "");
        map.put("sort", "createdDateDesc");
        map.put("action", "");
        map.put("quantity", "");
        map.put("enquiry_status", "");
        HashMap<String, Object> date = new HashMap<>();
        date.put("type","");
        date.put("startDate","");
        date.put("endDate","");
        map.put("dateRange",date);
        map.put("enquiry_tab", tab);
        map.put(NKeys.GETVENDORQUOTATIONS,new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETVENDORQUOTATIONS, map, true);
    }
    public void getFilterInquiries(String filter,boolean isShowLoader) {
        Screen="Filter";
        FILTERDATA=filter;
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.GETVENDORQUOTATIONS,filter);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETVENDORQUOTATIONS, data, isShowLoader);
    }
    private void setupNewInwuiryAdapter() {
        newInquiryAdapter = new NewInquiryAdapter(getContext(),this::onItemClick, this::onClickSendResponse,false);
        inquiriesBinding.inquiryRv.setHasFixedSize(true);
        inquiriesBinding.inquiryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        inquiriesBinding.inquiryRv.setAdapter(newInquiryAdapter);
    }
    private void setupOtherInquiryAdapter() {
        otherInquiryAdapter = new OtherInquiryAdapter(getContext(),this::onItemClick);
        inquiriesBinding.inquiryRv.setHasFixedSize(true);
        inquiriesBinding.inquiryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        inquiriesBinding.inquiryRv.setAdapter(otherInquiryAdapter);
    }

    @Override
    public void onItemClick(int position, String id) {
        Intent intent= new Intent(getActivity(), InquiryDetailActivity.class);
        intent.putExtra("inquiry_id",id);
        startActivity(intent);
    }
    private void updateInquiryStatus(HashMap<String, Object> map) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.UPDATEQUOTATIONPRODUCTSTATUS,new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS, map1, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(inquiriesBinding.refreshLayout.isRefreshing())
            inquiriesBinding.refreshLayout.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETVENDORQUOTATIONS) {
                VendorQuotationsResponse response = new Gson().fromJson(responseDO.getResponse(), VendorQuotationsResponse.class);
                Common.showLog("count===+++==="+response.getData().getCount());
                manageInquiriesFragment.setTOTALPRODUCTCOUNT(String.valueOf(response.getData().getCount()));
                inquiriesBinding.enquiryCount.setText("Total Number of Inquiries : "+String.valueOf(response.getData().getCount()));
                if (getTag().equalsIgnoreCase("new_inquiry")||getTag().equalsIgnoreCase("delayed_inquiry")) {
                    if(newInquiryAdapter.checkTimerStartedId().equalsIgnoreCase("")) {
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            if(response.getData().getRecords().get(i).getId().equalsIgnoreCase(newInquiryAdapter.checkTimerStartedId())){
                                response.getData().getRecords().get(i).setTimerRunning(true);
                            }
                        }
                    }
                    newInquiryAdapter.refreshList(response.getData().getRecords());
                }else otherInquiryAdapter.refreshList(response.getData().getRecords());
                if(response.getData().getRecords().size()==0){
                    inquiriesBinding.noInquiriesRecordTv.setVisibility(View.VISIBLE);
                }else{
                    inquiriesBinding.noInquiriesRecordTv.setVisibility(View.GONE);
                }
            }
            else if (responseDO.getServiceMethods()==ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Toast.makeText(getActivity(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if(FILTERDATA.equalsIgnoreCase(""))
                    getAllInquiries(getTag());
                else getFilterInquiries(FILTERDATA,true);
            }
        }
    }

    @Override
    public void onClickSendResponse(HashMap<String, Object> map) {
        updateInquiryStatus(map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.vendorQuotationsRecordsItemList.clear();
    }
}