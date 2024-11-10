package com.devstringx.mytylesstockcheck.screens.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
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
import com.devstringx.mytylesstockcheck.adapter.ShippingChargesItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentShippingChargesBinding;
import com.devstringx.mytylesstockcheck.screens.AddShippingChargesActivity;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.AllShippingChargeResponse;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.AllShippingTabResponse;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.ShippingChargeFilterActivity;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.ShippingChargesStatusAdapter;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.ShippingStatus;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingChargesFragment extends Fragment implements ResponseListner, ShippingChargesItemAdapter.OnClick, ShippingChargesStatusAdapter.onStatusSelectedListner {
    FragmentShippingChargesBinding binding;
    ShippingChargesItemAdapter adapter;
    ShippingChargesStatusAdapter shippingChargesStatusAdapter;
    boolean isSelectAllSelected = false;
    RecyclerView recyclerView;
    RecyclerView shippingTabRv;
    private List<AllShippingChargeResponse.Record> allRecords = new ArrayList<>();
    private List<ShippingStatus> statusList = new ArrayList<>();
    private String SELECTED_TAB = "All";
    private static final String ALL = "All";
    private static final String PENDING = "Pending";
    private static final String APPROVED = "Approved";
    private static final String PAID = "Paid";
    private static final String REJECTED = "Rejected";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipping_charges, container, false);
        View view = binding.getRoot();
        setupAdapter();
        setupTabsAdapter();
        if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9")) {
            binding.shippingAddIV.setVisibility(View.VISIBLE);
        }else binding.shippingAddIV.setVisibility(View.GONE);
        binding.shippingFilterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ShippingChargeFilterActivity.class), 343);
            }
        });
        binding.shippingExportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportShippingCharges();
            }
        });
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllShippingTabs();
                getAllShippingCharges();
            }
        });
        binding.allScTab.setSelected(true);
        binding.allScTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.allScTab.isSelected()) {
                    binding.allScTab.setSelected(true);
                    binding.pendingScTab.setSelected(false);
                    binding.approveScTab.setSelected(false);
                    binding.paidScTab.setSelected(false);
                    binding.rejectedScTab.setSelected(false);
                    onTabSwitchClearFilter();
                    filterByStatus(ALL);
                }
            }
        });
        binding.pendingScTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.pendingScTab.isSelected()) {
                    binding.allScTab.setSelected(false);
                    binding.pendingScTab.setSelected(true);
                    binding.approveScTab.setSelected(false);
                    binding.paidScTab.setSelected(false);
                    binding.rejectedScTab.setSelected(false);
                    onTabSwitchClearFilter();
                    filterByStatus(PENDING);
                }
            }
        });
        binding.approveScTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.approveScTab.isSelected()) {
                    binding.allScTab.setSelected(false);
                    binding.pendingScTab.setSelected(false);
                    binding.approveScTab.setSelected(true);
                    binding.paidScTab.setSelected(false);
                    binding.rejectedScTab.setSelected(false);
                    onTabSwitchClearFilter();
                    filterByStatus(APPROVED);
                }
            }
        });
        binding.paidScTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.paidScTab.isSelected()) {
                    binding.allScTab.setSelected(false);
                    binding.pendingScTab.setSelected(false);
                    binding.approveScTab.setSelected(false);
                    binding.paidScTab.setSelected(true);
                    binding.rejectedScTab.setSelected(false);
                    onTabSwitchClearFilter();
                    filterByStatus(PAID);
                }
            }
        });
        binding.rejectedScTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.rejectedScTab.isSelected()) {
                    binding.allScTab.setSelected(false);
                    binding.pendingScTab.setSelected(false);
                    binding.approveScTab.setSelected(false);
                    binding.paidScTab.setSelected(false);
                    binding.rejectedScTab.setSelected(true);
                    onTabSwitchClearFilter();
                    filterByStatus(REJECTED);
                }
            }
        });
        binding.masterCbIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSelectAllSelected) {
                    binding.masterCbIV.setImageResource(R.drawable.orange_checkbox_selected);
                    isSelectAllSelected = true;

                } else {
                    binding.masterCbIV.setImageResource(R.drawable.orange_checkbox_unselected);
                    isSelectAllSelected = false;
                }
                updateRecyclerViewSelection(isSelectAllSelected);
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
                searchShippingCharges(binding.searchET.getText().toString(), false);
            }
        });
        binding.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveShippingCharges();
            }
        });
        binding.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectShippingCharges();
            }
        });
        binding.shippingAddIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddShippingChargesActivity.class);
                startActivityForResult(intent,200);
            }
        });
        getAllShippingTabs();
        getAllShippingCharges();
        return view;
    }

    private void exportShippingCharges() {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("search", binding.searchET.getText().toString());
        map.put("sort", Common.shippingFilterSortBy);
        map.put("dateRange", Common.shippingDateType);
        map.put("fromDate", Common.shippingStartDate);
        map.put("toDate", Common.shippingEndDate);
        map.put("statusTab",SELECTED_TAB);
        map.put("shippingChargeStatus", Common.selectedShippingStatustName);
        map.put("deliveryAgent", Common.selectedShippingDeliveryAgentName);
        map1.put(NKeys.EXPORTSHIPPINGCHARGE, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTSHIPPINGCHARGE, map1, true);
    }

    private void onTabSwitchClearFilter() {
        Common.shippingDateType = "";
        Common.shippingStartDate = "";
        Common.shippingEndDate = "";
        Common.shippingFilterSortBy = "createdAtDesc";
        Common.selectedShippingStatustName=new ArrayList<>();
        Common.selectedShippingDeliveryAgentName=new ArrayList<>();
    }


    private void rejectShippingCharges() {
        JsonObject jsonObject = new JsonObject();

        try {
            // Convert the list of selected item IDs to a Gson JsonArray
            JsonArray jsonArray = new JsonArray();
            for (Integer id : adapter.getSelectedItemIds()) {
                jsonArray.add(id);
            }
            jsonObject.add("shipping_id", jsonArray);
            jsonObject.addProperty("reason","ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Common.showLog("size====++++ " + adapter.getSelectedItemIds().size());
        HashMap<String, Object> map = new HashMap<>();
        // Convert the jsonObject to string and put it in the request parameters map
        map.put(NKeys.REQUEST_PARAMS, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_REJECT_SHIPPING_CHARGE, map, true);
    }

    private void approveShippingCharges() {
        JsonObject jsonObject = new JsonObject();

        try {
            // Convert the list of selected item IDs to a Gson JsonArray
            JsonArray jsonArray = new JsonArray();
            for (Integer id : adapter.getSelectedItemIds()) {
                jsonArray.add(id);
            }
            jsonObject.add("shipping_id", jsonArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.REQUEST_PARAMS, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_APPROVE_SHIPPING_CHARGE, map, true);

    }

    private void searchShippingCharges(String search, boolean isShowLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("search", search);
        getFilterAllShippingCharges(new Gson().toJson(map), false);
    }

    private void updateRecyclerViewSelection(boolean isSelectAllSelected) {
        showActionButtons(SELECTED_TAB);
        if (isSelectAllSelected) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                allRecords.get(i).setSelected(true);
            }
        } else {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                allRecords.get(i).setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupAdapter() {
        recyclerView = binding.shippingChargesRV;
        adapter = new ShippingChargesItemAdapter(getContext(), this, allRecords);
        binding.shippingChargesRV.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setupTabsAdapter() {
        shippingTabRv = binding.shippingTabRv;
        shippingChargesStatusAdapter = new ShippingChargesStatusAdapter(getContext(), statusList, this);
        binding.shippingTabRv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shippingTabRv.setLayoutManager(linearLayoutManager);
        shippingTabRv.setAdapter(shippingChargesStatusAdapter);
    }

    private void setupTabsData(List<ShippingStatus> statusTab) {
        statusList = statusTab;
        shippingChargesStatusAdapter.refreshList(statusList);
    }

    @Override
    public void onClickListener() {
        if (adapter.checkIsAllItemSelected()) {
            binding.masterCbIV.setImageResource(R.drawable.orange_checkbox_selected);
            isSelectAllSelected = true;
        } else {
            binding.masterCbIV.setImageResource(R.drawable.orange_checkbox_unselected);
            isSelectAllSelected = false;
        }
        if (adapter.isAnyItemSelected())
            showActionButtons(SELECTED_TAB);
    }

    @Override
    public void onItemClick(String id) {
        moveToOrderDetail(id);
    }

    private void moveToOrderDetail(String order_id) {
//        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//        intent.putExtra("orderId", id);
//        Common.showLog("orderId====" + id);
//        startActivityForResult(intent, 002);
        Intent intent = new Intent(this.getActivity(),OrderDetailActivity.class);
        intent.putExtra("source_page","shipping_charges");
        intent.putExtra("orderId",order_id);
        Common.showLog("ShippingChargesFragment======"+intent.getStringExtra("source_page")+"      "+intent.getStringExtra("orderId"));
        startActivityForResult(intent,100);
    }

    private void getAllShippingTabs() {
        HashMap<String, Object> map = new HashMap<>();
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GET_ALL_SHIPPING_TAB, map, true);
    }

    private void getAllShippingCharges() {
        getFilterAllShippingCharges(new Gson().toJson(getAllShippingChargesMap()), true);
    }

    private HashMap<String, Object> getAllShippingChargesMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("search", "");
        map.put("sort", Common.shippingFilterSortBy);
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("dateRange", Common.shippingDateType);
        map.put("fromDate", Common.shippingStartDate);
        map.put("toDate", Common.shippingEndDate);
        map.put("statusTab",SELECTED_TAB);
        map.put("shippingChargeStatus", Common.selectedShippingStatustName);
        map.put("deliveryAgent", Common.selectedShippingDeliveryAgentName);
        return map;
    }
    private void hideActionButtons() {
        binding.rejectBtn.setVisibility(View.GONE);
        binding.approveBtn.setVisibility(View.GONE);
    }

    private void filterByStatus(String status) {
        //Hide action buttons
        hideActionButtons();
        SELECTED_TAB = status;
        binding.masterCbIV.setImageResource(R.drawable.orange_checkbox_unselected);
        isSelectAllSelected = false;

        HashMap<String, Object> map = new HashMap<>();
        List<String> selectedShippingStatustName = new ArrayList<>();
        selectedShippingStatustName.add(status);
        map.put("statusTab",status);
        map.put("shippingChargeStatus", selectedShippingStatustName);
        getFilterAllShippingCharges(new Gson().toJson(map), true);
    }

    private void showActionButtons(String status) {
        hideActionButtons();
        switch (status) {
            case PENDING:
                binding.approveBtn.setVisibility(View.VISIBLE);
                binding.rejectBtn.setVisibility(View.VISIBLE);
                break;
            case APPROVED:
                binding.rejectBtn.setVisibility(View.VISIBLE);
                break;
            case PAID:
                break;
            case REJECTED:
                binding.approveBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 343 && resultCode == RESULT_OK) {
            getAllShippingTabs();
            if (data != null) {
                String receivedHashMap = data.getStringExtra("ShippingFilterDataMap");
                getFilterAllShippingCharges(receivedHashMap, true);
            } else getAllShippingCharges();
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
                getAllShippingTabs();
                getAllShippingCharges();
        }
    }

    private void getFilterAllShippingCharges(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GET_ALL_SHIPPING_CHARGE, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GET_ALL_SHIPPING_CHARGE, map, isLoader);
    }


    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (binding.refreshLl.isRefreshing())
            binding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_ALL_SHIPPING_CHARGE) {
                AllShippingChargeResponse allShippingChargeResponse = new Gson().fromJson(responseDO.getResponse(), AllShippingChargeResponse.class);
                allRecords = allShippingChargeResponse.getData().getRecords();
                if (allShippingChargeResponse.getData() != null)
                    allRecords = allShippingChargeResponse.getData().getRecords();
                else allRecords = null;
                adapter.refreshList(allRecords);

                if (allRecords != null && allRecords.size() > 0) {
                    binding.shippingChargesRV.setVisibility(View.VISIBLE);
                    binding.llCheckAll.setVisibility(View.VISIBLE);
                    binding.noShippingChargesTv.setVisibility(View.GONE);
                } else {
                    binding.shippingChargesRV.setVisibility(View.GONE);
                    binding.llCheckAll.setVisibility(View.GONE);
                    binding.noShippingChargesTv.setVisibility(View.VISIBLE);
                }
                if (shippingChargesStatusAdapter != null && allShippingChargeResponse.getData().getStatusTab() != null) {
                    shippingChargesStatusAdapter.setSelTab(allShippingChargeResponse.getData().getStatusTab());
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_ALL_SHIPPING_TAB) {
                AllShippingTabResponse allShippingTabResponse = new Gson().fromJson(responseDO.getResponse(), AllShippingTabResponse.class);
                if (allShippingTabResponse.getData() != null && allShippingTabResponse.getData().getStatusTab() != null) {
                    AllShippingTabResponse.StatusTab statusTab = allShippingTabResponse.getData().getStatusTab();
                    JsonObject jsonObject = new Gson().fromJson(new Gson().toJson(statusTab).toString(), JsonObject.class);
                    List<ShippingStatus> listTabs = new ArrayList<>();

                    for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
                        ShippingStatus shippingStatus = new ShippingStatus();
                        shippingStatus.setStatusName(e.getKey());
                        int value = 0;
                        try {
                            value = Integer.parseInt(e.getValue().toString().replace("\"", ""));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        shippingStatus.setValue(value);
                        listTabs.add(shippingStatus);
                    }
                    setupTabsData(listTabs);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_APPROVE_SHIPPING_CHARGE) {
                getAllShippingTabs();
                filterByStatus(SELECTED_TAB);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_REJECT_SHIPPING_CHARGE) {
                getAllShippingTabs();
                filterByStatus(SELECTED_TAB);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTSHIPPINGCHARGE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optString("link")));
                    startActivity(intent);
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_ALL_SHIPPING_CHARGE) {
                allRecords = null;
                binding.shippingChargesRV.setVisibility(View.GONE);
                binding.llCheckAll.setVisibility(View.GONE);
                binding.noShippingChargesTv.setVisibility(View.VISIBLE);
            }
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }

    @Override
    public void onStatusSelected(String status) {
        onTabSwitchClearFilter();
        filterByStatus("Requested".equalsIgnoreCase(status)?"Pending":status);
    }
}