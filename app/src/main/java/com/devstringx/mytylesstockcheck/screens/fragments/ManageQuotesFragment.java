package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.QuoteListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentManageQuotesBinding;
import com.devstringx.mytylesstockcheck.databinding.QuoteDetailInfoBinding;
import com.devstringx.mytylesstockcheck.datamodal.manageQuotes.ManageQuotesData;
import com.devstringx.mytylesstockcheck.datamodal.manageQuotes.RecordsItem;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.AddQuoteActivity;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageQuotesFragment extends Fragment implements ResponseListner, QuoteListAdapter.InfoClick {

    private String statusString = "Open";
    FragmentManageQuotesBinding binding;
    QuoteListAdapter adapter;
    List<RecordsItem> allQuotesList=new ArrayList<>();
    int DELETEDPOS=0;
    private SocketManager manager;
    private String FILTERDATA="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_manage_quotes,container,false);
        View view=binding.getRoot();
        binding.quotationRb.setChecked(true);

        binding.refreshLl.setRefreshing(false);
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllQuotes(binding.searchET.getText().toString(),true);
            }
        });

        adapter=new QuoteListAdapter(getActivity(),allQuotesList,this);
        binding.mqRV.setHasFixedSize(true);
        binding.mqRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.mqRV.setAdapter(adapter);
        manager=SocketManager.getInstance();
        manager.connect();
        binding.addQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddQuoteActivity.class);
                startActivityForResult(intent,3000);
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getAllQuotes(binding.searchET.getText().toString(),false);
            }
        });
        if(!Common.getPermission(getActivity(),"MQ","VPDEAQAU")){
            binding.exportIv.setVisibility(View.GONE);
        }
        if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) {
            if (!Common.getPermission(getActivity(), "MQ", "VPDEQA")) {
                binding.exportIv.setVisibility(View.GONE);
            } else {
                binding.exportIv.setVisibility(View.VISIBLE);
            }
        }
        binding.exportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getListData().size() == 0) {
                    Common.showToast(getActivity(), getString(R.string.no_record_found));
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("search", binding.searchET.getText().toString());
                FILTERDATA = new Gson().toJson(map);
                exportData();
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getAllQuotes("",true);
//            }
//        },1000);

        binding.inquiryRbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                refreshList(allQuotesList);
            }
        });

        binding.quotationRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusString = "Open";
                getAllQuotes("",true);
            }
        });

        binding.orderRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusString = "Converted";
                getAllQuotes("",true);
            }
        });

        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.exportIv.setVisibility(View.INVISIBLE);
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
            binding.exportIv.setVisibility(View.VISIBLE);
        }

        return view;
    }
    private void exportData() {
        Common.showLog("EXPORT====" + FILTERDATA);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.QUOTATIONDATA, FILTERDATA);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTUSERQUOTATIONDATA, map1, true);
    }
    @Override
    public void onResume() {
        super.onResume();
        binding.quotationRb.setChecked(true);
        statusString = "Open";
        getAllQuotes("",true);
        manager.onMessageReceived(new SocketManager.MessageReceived() {
            @Override
            public void onMessageReceivedListner(String response) {
                Common.showLog("SOCKET MSG==="+response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String qu_id=jsonObject.optString("quotationId");
                            jsonObject=jsonObject.getJSONObject("quotationProductSocketData");
                            for (int i = 0; i < allQuotesList.size(); i++) {
                                if(qu_id.equalsIgnoreCase(String.valueOf(allQuotesList.get(i).getId()))){
                                    allQuotesList.get(i).setTotalQuotationProducts(jsonObject.optInt("total_quotation_products",0));
                                    allQuotesList.get(i).setTotalResponse(jsonObject.optInt("total_response",0));
                                    break;
                                }
                            }
                            refreshList(allQuotesList);
                        } catch (JSONException e) {
                            Common.showLog("ERROR====="+e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.disconnect();
    }

    private void getAllQuotes(String searchText,boolean isLoader){
        try {
            JSONObject object=new JSONObject();
            object.put("search",searchText);
            object.put("status",statusString);
            HashMap<String, Object> map = new HashMap<>();
            map.put(NKeys.QUOTATIONDATA, object.toString());
            new NetworkRequest(getActivity(),this).callWebServices(ServiceMethods.WS_GETALLQUOTES,map,isLoader);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteQuotation(String id) {
        String data = "{" +
                " \"id\":"+id +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_DELETEQUOTATION, map, true);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        allQuotesList.clear();
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(binding.refreshLl.isRefreshing())binding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLQUOTES) {
                ManageQuotesData manageQuotesData = new Gson().fromJson(responseDO.getResponse(), ManageQuotesData.class);
                allQuotesList=manageQuotesData.getData().getRecords();
                refreshList(allQuotesList);
                if(allQuotesList.size()==0){
                    binding.noQuoteRecordTv.setVisibility(View.VISIBLE);
                }else{
                    binding.noQuoteRecordTv.setVisibility(View.GONE);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETEQUOTATION) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                    adapter.updateList(DELETEDPOS);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTUSERQUOTATIONDATA) {
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
        }else{
            Common.showToast(getActivity(), responseDO.getResponse());
        }

    }

    private void refreshList(List<RecordsItem> allList) {
            adapter.refreshList(allList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAllQuotes("",true);
    }

    @Override
    public void infoClickListner(int totalResponse, int totalQuotationProducts,String type) {
        if(type.equalsIgnoreCase("info")) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            QuoteDetailInfoBinding quoteDetailInfoBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.quote_detail_info, null, false);

            bottomSheetDialog.setContentView(quoteDetailInfoBinding.getRoot());
            bottomSheetDialog.show();
            quoteDetailInfoBinding.statusTv.setText(Html.fromHtml("Response received for " + "<font color=#000000><b>" + totalResponse + "</b> </font> out of " +
                    "<font color=#000000> <b>" + totalQuotationProducts + "</b> </font>" + " products"));

            quoteDetailInfoBinding.closeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
        }else{
            DELETEDPOS=totalResponse;
            deleteQuotation(String.valueOf(adapter.getListData().get(totalResponse).getId()));
        }
    }



}