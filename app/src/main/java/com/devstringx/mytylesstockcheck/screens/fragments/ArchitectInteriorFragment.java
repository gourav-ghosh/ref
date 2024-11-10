package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ArchitectCardItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentArchitectInteriorBinding;
import com.devstringx.mytylesstockcheck.datamodal.architectListingData.Response;
import com.devstringx.mytylesstockcheck.screens.CreateArchitectActivity;
import com.devstringx.mytylesstockcheck.screens.architectFilter.ArchitectFilterActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ArchitectInteriorFragment extends Fragment implements ResponseListner , ArchitectCardItemAdapter.OnArchitectItemClickListener {
    FragmentArchitectInteriorBinding binding;
    ArchitectCardItemAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_architect_interior, container, false);
        View view = binding.getRoot();
        setupAdapter();
        binding.refreshLl.setRefreshing(false);
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setHashmapData(false);
            }
        });
        setHashmapData(false);
        if(Common.getPermission(getContext(),"AORI","AEA")) {
            binding.createArchitectIV.setVisibility(View.VISIBLE);
        } else binding.createArchitectIV.setVisibility(View.GONE);
        binding.createArchitectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateArchitectActivity.class);
                intent.putExtra("title","Create Architect / Interior");
                intent.putExtra("type","create");
                startActivity(intent);
            }
        });
        binding.importArchitectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHashmapData(true);
            }
        });
        binding.filterArchitectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ArchitectFilterActivity.class) , 200);
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setHashmapData(false);
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
            binding.importArchitectIV.setVisibility(View.INVISIBLE);
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
            binding.importArchitectIV.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHashmapData(false);
    }

    private void setHashmapData(boolean b) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        if (binding.searchET.getText().toString()!=null)
        map.put("search", binding.searchET.getText().toString());
        else map.put("search", "");
        map.put("sort",Common.architectSortBy);
        map.put("dateRange", Common.architectDateType);
        map.put("fromDate", Common.architectStartDate);
        map.put("toDate", Common.architectEndDate);
        map.put("salesExecutive", Common.architectSelectedSalepersonId);
        map.put("exportArchitect",b);
        getFilterAllArchitect(new Gson().toJson(map),false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (data!=null) {
                String receivedHashMap = data.getStringExtra("ArchitectFilterDataMap");
                getFilterAllArchitect(receivedHashMap, true);
            }
        }
    }

    private void getFilterAllArchitect(String receivedHashMap , boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLARCHITECT, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLARCHITECT, map, isLoader);
    }

    private void setupAdapter() {
        RecyclerView recyclerView =binding.architectCardRv;
        adapter = new ArchitectCardItemAdapter(getContext(), (id, mobileNum, type) -> onClickRedirection(id, mobileNum, type));
        binding.architectCardRv.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(binding.refreshLl.isRefreshing())binding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLARCHITECT) {
                Response architectResponse = new Gson().fromJson(responseDO.getResponse(), Response.class);
                adapter.refreshList(architectResponse.getData().getRecords());
                if (!architectResponse.getData().getLink().isEmpty()){
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(architectResponse.getData().getLink()));
                        startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onClickRedirection(String id, String mobileNum, String type) {
        if (type.equalsIgnoreCase("whatsapp")){
            Common.openWhatsApp(getContext(),mobileNum,"");
        }else if (type.equalsIgnoreCase("call")){
            Common.openDialerPad(getContext(),mobileNum);
        }else {
            Intent intent = new Intent(getActivity(), CreateArchitectActivity.class);
            intent.putExtra("title","Profile Details");
            intent.putExtra("type","details");
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}