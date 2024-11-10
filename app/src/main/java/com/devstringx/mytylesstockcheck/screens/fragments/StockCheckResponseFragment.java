package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ResponseNotificationAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentUpcomingTasksBinding;
import com.devstringx.mytylesstockcheck.datamodal.responseNotification.ResponseNotificationData;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class StockCheckResponseFragment extends Fragment implements ResponseListner, ResponseNotificationAdapter.NotificationUpdate {
    private FragmentUpcomingTasksBinding binding;
    private ResponseNotificationAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_upcoming_tasks, container, false);
        View view=binding.getRoot();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotification(new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
    }

    private void getNotification(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("user_id",id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DATA, new Gson().toJson(map1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETNOTIFICATIONLISTINGFORRESPONSE, map, true);
    }
    private void notificationMarkAsRead(ArrayList<String> id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id",id);
        map1.put("action","unread");
        map1.put("notification_type","response inquiry");
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DATA, new Gson().toJson(map1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATENOTIFICATION, map, false);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE==DASHBOARD=" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETNOTIFICATIONLISTINGFORRESPONSE) {
                ResponseNotificationData responseNotificationResponse = new Gson().fromJson(responseDO.getResponse(), ResponseNotificationData.class);
                adapter = new ResponseNotificationAdapter(getActivity(), responseNotificationResponse.getData().getRecords(),StockCheckResponseFragment.this);
                binding.multiNotificationRv.setHasFixedSize(true);
                binding.multiNotificationRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.multiNotificationRv.setAdapter(adapter);
                if(responseNotificationResponse.getData().getRecords().size()==0){
                    binding.noTaskRecordTv.setVisibility(View.VISIBLE);
                }else{
                    binding.noTaskRecordTv.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void updateNotifationListner(int position) {
        ArrayList<String> ids=new ArrayList<>();
        ids.add(String.valueOf(adapter.getListItems().get(position).getId()));
        notificationMarkAsRead(ids);
        Intent intent=new Intent(getActivity(), QuoteDetailActivity.class);
        intent.putExtra("from", "stock_check_resp");
        intent.putExtra("id",Integer.parseInt(adapter.getListItems().get(position).getQuotation_id()));
        getActivity().startActivity(intent);
    }
}