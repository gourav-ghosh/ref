package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.ComplaintCommentAdapter;
import com.devstringx.mytylesstockcheck.adapter.OrderListingAdapter;
import com.devstringx.mytylesstockcheck.adapter.OrderSubTabAdapter;
import com.devstringx.mytylesstockcheck.adapter.OrdersTabAdapter;
import com.devstringx.mytylesstockcheck.adapter.PoAoAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentOrderListingBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllOrders.OrderListingResponse;
import com.devstringx.mytylesstockcheck.datamodal.getOrderComment.OrderCommentResponse;
import com.devstringx.mytylesstockcheck.datamodal.getOrderComment.OrderRecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.ordersTabData.CustomEntry;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.screens.orderFilters.OrderFilterActivity;
import com.devstringx.mytylesstockcheck.screens.taskreminder.TaskReminderListActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jsibbold.zoomage.ZoomageView;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListingFragment extends Fragment implements UploadFileAdapter.removeImage, OrderListingAdapter.OnOrderItemCLick, ResponseListner, OrdersTabAdapter.OnTabClick, OrderSubTabAdapter.OnSubTabClick, PoAoAdapter.OnClick {
    FragmentOrderListingBinding binding;
    OrderListingAdapter adapter;
    private ComplaintCommentAdapter complaintCommentAdapter;
    private RecyclerView commentsRV;
    private RelativeLayout noCommentTV;
    private TextView commentET;
    PoAoAdapter po_ao_adapter;
    OrdersTabAdapter ordersTabAdapter;
    String orderId;
    String selected_mode;
    List<String> payment_modes = new ArrayList<>();
    List<String> payment_modes_value = new ArrayList<>();
    Dialog updateTransportDialog;
    private ProgressBar progressBar;
    Dialog imageUploadDialog;
    private RecyclerView dialogRecyclerView;
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_listing, container, false);
        View view = binding.getRoot();
        binding.refreshLl.setRefreshing(false);
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderTabs();
            }
        });
        binding.filterOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), OrderFilterActivity.class), 001);
            }
        });
        binding.addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.intent(getActivity(), TaskReminderListActivity.class);
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Common.orderSearch = binding.searchET.getText().toString();
                getOrderList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportData();
            }
        });
        getOrderTabs();
        setupAdapter();
        setupPOAOAdapter();

        Bundle bundle = this.getArguments();
        String from = "";
        if (bundle != null) {
            from = bundle.getString("from","");
        }
        if(from.equalsIgnoreCase("order_conversion")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.orderListingRV.findViewHolderForAdapterPosition(0).itemView.performClick();
                }
            },1000);
        }

        if (Common.getPermission(getActivity(), "DB", "") &&
                Common.getPermission(getActivity(), "ML", "") &&
                Common.getPermission(getActivity(), "MQ", "") &&
                Common.getPermission(getActivity(), "RZP", "") &&
                Common.getPermission(getActivity(), "ODS", "") &&
                Common.getPermission(getActivity(), "CMS", "") &&
                Common.getPermission(getActivity(), "DWL", "") &&
                Common.getPermission(getActivity(), "AORI", "")) {
            binding.createOrder.setVisibility(View.INVISIBLE);
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
            binding.createOrder.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void exportData() {
        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", Common.orderSearch.toString());
        map.put("sort", Common.orderFilterSortby.toString());
        map.put("dateRange", Common.orderDateType.toString());
        map.put("fromDate", Common.orderStartDate.toString());
        map.put("toDate", Common.orderEndDate.toString());
        map.put("orderStatus", Common.selectedOrderStatus);
        map.put("paymentMode", Common.selectedOrderPaymentMode);
        map.put("poCodes", Common.selectedPOCodes);
        map.put("orderType", Common.selectedOrderType);
        map.put("deliveryType", Common.selectedDeliveryType);
        map.put("deliveryAgent", Common.selectedOrderDeliveryAgentName);
        map.put("saleExecutive", Common.selectedOrderSupExeName);
        map.put("manager", Common.selectedOrderDisManagerName);
        map.put("statusTab", Common.OrderSelectedTabName);
        if (Common.OrderSelectedTabName.equalsIgnoreCase("Outstation")) {
            map.put("statusTab2", Common.OutstationSelectedSubtab);
        }
        map1.put(NKeys.EXPORTORDER, new Gson().toJson(map));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTORDER, map1, true);
    }

    private void getOrderTabs() {
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETORDERTABS, null, true);
    }

    private void setupAdapter() {
        adapter = new OrderListingAdapter(getContext(), this);
        binding.orderListingRV.setHasFixedSize(true);
        binding.orderListingRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.orderListingRV.setAdapter(adapter);
    }

    private void callCreateOrderCommentApi(HashMap<String, Object> map) {
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_SENDORDERCOMMENT, map, true);
    }

    private void showOrderCommentDialog(String id) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.complaint_chat_dialog);
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        TextView title = dialog.findViewById(R.id.titleTV);
        title.setText("Comment on Order");
        commentsRV = dialog.findViewById(R.id.comments_rv);
        ImageView send = dialog.findViewById(R.id.sendIV);
        commentET = dialog.findViewById(R.id.commentET);
        noCommentTV = dialog.findViewById(R.id.no_comment_tv);
        getOrderCommentApi(id);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentET.getText().toString().isEmpty()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("order_id", id);
                    map.put("comment", commentET.getText().toString());
                    map.put(NKeys.SENDORDERCOMMENT, new Gson().toJson(map));
                    callCreateOrderCommentApi(map);
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

    private void setupCommentAdapter(Context context, List<OrderRecordsItem> records) {
        complaintCommentAdapter = new ComplaintCommentAdapter(context, records, "order");
        commentsRV.setHasFixedSize(true);
        commentsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRV.setAdapter(complaintCommentAdapter);
    }

    private void getOrderCommentApi(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", id);
        map.put(NKeys.GETORDERCOMMENT, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETORDERCOMMENT, map, true);
    }

    @Override
    public void onItemClick(String id, boolean b) {
        if (b) {
            orderId = id;
            showOrderCommentDialog(id);
        } else {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderId", id);
            Common.showLog("orderId====" + id);
            startActivityForResult(intent, 002);
        }
    }

    @Override
    public void onLocationItemClick(String orderId,String deliveryAddress) {
        showDialog(orderId,deliveryAddress);
    }

    private void processStatusObject(JsonObject statusObject) {
        List<CustomEntry<String, JsonElement>> statusList = new ArrayList<>();

        // Iterate over the entries of the statusObject
        for (Map.Entry<String, JsonElement> entry : statusObject.entrySet()) {
            // Create a new CustomEntry object based on the current Map.Entry object
            CustomEntry<String, JsonElement> customEntry = new CustomEntry<>(entry.getKey(), entry.getValue());
            statusList.add(customEntry);
        }

        Common.showLog("====count====" + statusList.size());
        if (Common.OrderSelectedTabName.equalsIgnoreCase("Outstation"))
            setOutstationTabsAdapter(statusList);
        else setOrderTabsAdapter(statusList);
        getOrderList();
    }

    private void setOutstationTabsAdapter(List<CustomEntry<String, JsonElement>> statusList) {
        OrderSubTabAdapter ordersSubTabAdapter = new OrderSubTabAdapter(getContext(), statusList, this);
        binding.outstationSubtabRv.setHasFixedSize(true);
        binding.outstationSubtabRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.outstationSubtabRv.setAdapter(ordersSubTabAdapter);
    }

    private void setOrderTabsAdapter(List<CustomEntry<String, JsonElement>> statusList) {
        ordersTabAdapter = new OrdersTabAdapter(getContext(), statusList, this);
        binding.orderTabRv.setHasFixedSize(true);
        binding.orderTabRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.orderTabRv.setAdapter(ordersTabAdapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERTABS) {
                JsonObject jsonObject = new Gson().fromJson(responseDO.getResponse(), JsonObject.class);
                JsonObject data = jsonObject.getAsJsonObject("data");
                int i = 1;
                if (Common.OrderSelectedTabName.equalsIgnoreCase("Outstation")) i = 2;
                JsonElement statusElement = data.get("status");
                if (statusElement != null) {
                    if (statusElement.isJsonObject()) {
                        // If "status" is a JsonObject
                        JsonObject statusObject = statusElement.getAsJsonObject();
                        processStatusObject(statusObject);
                    } else if (statusElement.isJsonArray()) {
                        // If "status" is a JsonArray
                        JsonArray statusArray = statusElement.getAsJsonArray();
                        if (statusArray.size() > i && statusArray.get(i).isJsonObject()) {
                            JsonObject statusObject = statusArray.get(i).getAsJsonObject();
                            processStatusObject(statusObject);
                        } else {
                            // Handle unexpected format of statusArray
                            Common.showLog("====Unexpected format of status array====");
                            // Handle error condition appropriately
                        }
                    } else {
                        // Handle the case where "status" is neither a JsonObject nor a JsonArray
                        Common.showLog("====Unexpected format of status====");
                        // Handle error condition appropriately
                    }
                } else {
                    // Handle the case where "status" doesn't exist
                    Common.showLog("====Status object not found====");
                    // Handle error condition appropriately
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLORDERS) {
                OrderListingResponse response = new Gson().fromJson(responseDO.getResponse(), OrderListingResponse.class);

                if (response.getData().getRecords().isEmpty()) {
                    binding.noLeadRecordTv.setVisibility(View.VISIBLE);
                    binding.orderListingRV.setVisibility(View.GONE);
                } else {
                    binding.noLeadRecordTv.setVisibility(View.GONE);
                    binding.orderListingRV.setVisibility(View.VISIBLE);
                }
                if (binding.refreshLl.isRefreshing()) {
                    for (int i = 0; i < ordersTabAdapter.getItemCount(); i++) {
                        if (response.getData().getStatusTab().equalsIgnoreCase(ordersTabAdapter.statusList.get(i).getKey())) {
                            ordersTabAdapter.statusList.get(i).setSelected(true);
                            Common.OrderSelectedTabName = response.getData().getStatusTab().toString();
                            ordersTabAdapter.UpdateSelectedTab(binding.orderTabRv, i);
                        } else ordersTabAdapter.statusList.get(i).setSelected(false);
                    }
                    binding.refreshLl.setRefreshing(false);
                }
                if (response.getData().getStatusTab().equalsIgnoreCase("All")) {
                    if (!Common.OrderSelectedTabName.equalsIgnoreCase(response.getData().getStatusTab().toString())) {
                        for (int i = 0; i < ordersTabAdapter.getItemCount(); i++) {
                            if (response.getData().getStatusTab().equalsIgnoreCase(ordersTabAdapter.statusList.get(i).getKey())) {
                                ordersTabAdapter.statusList.get(i).setSelected(true);
                                Common.OrderSelectedTabName = response.getData().getStatusTab().toString();
                                ordersTabAdapter.UpdateSelectedTab(binding.orderTabRv, i);
                            } else ordersTabAdapter.statusList.get(i).setSelected(false);
                        }
                        ordersTabAdapter.notifyDataSetChanged();
                    }
                }
                if (Common.OrderSelectedTabName.equalsIgnoreCase("PO AO")) {
                    binding.aoPoListingRV.setVisibility(View.VISIBLE);
                    binding.orderListingRV.setVisibility(View.GONE);
                    po_ao_adapter.refreshAdapter(getContext(), response.getData().getRecords());
                } else {
                    binding.aoPoListingRV.setVisibility(View.GONE);
                    binding.orderListingRV.setVisibility(View.VISIBLE);
                    adapter.refreshList(response.getData().getRecords());
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTORDER) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optString("link")));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        OrderCommentResponse response = new Gson().fromJson(responseDO.getResponse(), OrderCommentResponse.class);
                        if (response.getData().getCount() == 0) {
                            noCommentTV.setVisibility(View.VISIBLE);
                            commentsRV.setVisibility(View.GONE);
                        } else {
                            noCommentTV.setVisibility(View.GONE);
                            commentsRV.setVisibility(View.VISIBLE);
                        }
                        setupCommentAdapter(getActivity(), response.getData().getRecords());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_SENDORDERCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getContext(), jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        commentET.setText("");
                        getOrderCommentApi(orderId);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERFILTERVALUE) {       //For payment_mode
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray records = data.getJSONArray("records");

                        for (int i = 0; i < records.length(); i++) {
                            JSONObject record = records.getJSONObject(i);
                            String label = record.getString("label");
                            String value = record.getString("value");
                            payment_modes.add(label);
                            payment_modes_value.add(value);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEPOAOTRANSPORTATION) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(getContext(), jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        updateTransportDialog.dismiss();
                        getOrderList();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_POARRANGE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        imageUploadDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        po_ao_adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onTabClickListener() {
        Common.showLog("3====" + Common.OrderSelectedTabName);
        if (Common.OrderSelectedTabName.equalsIgnoreCase("Outstation") && Common.OutstationSelectedSubtab.isEmpty()) {
            binding.outstationSubtabRv.setVisibility(View.VISIBLE);
            getOrderTabs();
        } else {
            binding.outstationSubtabRv.setVisibility(View.GONE);
            getOrderList();
        }
    }

    private void setupPOAOAdapter() {
        po_ao_adapter = new PoAoAdapter(getContext(), this);
        binding.aoPoListingRV.setHasFixedSize(true);
        binding.aoPoListingRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.aoPoListingRV.setAdapter(po_ao_adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001) {
            if (data != null) {
                String receivedHashMap = data.getStringExtra("OrderFilterDataMap");
                Common.showLog("===================receivedHashMap===" + receivedHashMap);
                getAllOrders(receivedHashMap);
            }
        } else if (requestCode == 002) {
            binding.refreshLl.setRefreshing(true);
            getOrderTabs();
        }
    }

    public void getOrderList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", Common.orderSearch.toString());
        map.put("sort", Common.orderFilterSortby.toString());
        map.put("dateRange", Common.orderDateType.toString());
        map.put("fromDate", Common.orderStartDate.toString());
        map.put("toDate", Common.orderEndDate.toString());
        map.put("orderStatus", Common.selectedOrderStatus);
        map.put("paymentMode", Common.selectedOrderPaymentMode);
        map.put("poCodes", Common.selectedPOCodes);
        map.put("orderType", Common.selectedOrderType);
        map.put("deliveryType", Common.selectedDeliveryType);
        map.put("deliveryAgent", Common.selectedOrderDeliveryAgentName);
        map.put("saleExecutive", Common.selectedOrderSupExeName);
        map.put("manager", Common.selectedOrderDisManagerName);
        map.put("statusTab", Common.OrderSelectedTabName);
        if (Common.OrderSelectedTabName.equalsIgnoreCase("Outstation")) {
            map.put("statusTab2", Common.OutstationSelectedSubtab);
        }
        getAllOrders(new Gson().toJson(map));
    }

    private void getAllOrders(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLORDERS, data);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETALLORDERS, map, false);
    }

    @Override
    public void onSubTabClickListener() {
        getOrderList();
    }

    @Override
    public void onClickListener(String id) {
        UpdateTransportDialog(id);
    }

    @Override
    public void onMarkArrangeClickListener(int id, String name) {
        showImageUploadDialog("Mark PO Arranged", name, id);
    }

    @Override
    public void onViewClickListener(String link) {
        fileViewDialog(link);
    }
    private void showImageUploadDialog(String title, String name, int id) {
        imageUploadDialog = new Dialog(getContext());
        imageUploadDialog.setContentView(R.layout.upload_image_dialog);
        imageUploadDialog.setCancelable(true);
        TextView dialogTitle = imageUploadDialog.findViewById(R.id.dialog_title_tv);
        ImageView close = imageUploadDialog.findViewById(R.id.image_dialog_crossIV);
        TextView fieldHeader = imageUploadDialog.findViewById(R.id.field_titleTV);
        TextView uploadBtn = imageUploadDialog.findViewById(R.id.dialog_upload_btn);
        TextView selectImg = imageUploadDialog.findViewById(R.id.select_img);
        TextView formatTV = imageUploadDialog.findViewById(R.id.formatTV);
        dialogRecyclerView = imageUploadDialog.findViewById(R.id.upload_img_rv);
        dialogTitle.setText(title);
        fieldHeader.setText(name);
        formatTV.setText(R.string.upload_media_format);
        selectImg.setText(R.string.select_media_to_upload);
        setupUploadedImageAdapter();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUploadDialog.dismiss();
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImageLauncher();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUploadImageApi(id);
            }
        });
        imageUploadDialog.show();
    }
    private void callUploadImageApi(int id) {
//        TODO add optional conditions for Admin and Dispt. Manager
        if(SELECTEDFILES.size()>0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("task_id", id);
            map.put("file", SELECTEDFILES);
            new NetworkRequest(getActivity(), this::onResponseReceived).callWebServices(ServiceMethods.WS_POARRANGE, map, true);
        } else {
            Common.showToast(getContext(), "Select atleast 1 proof to mark PO as arranged");
        }
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

    private void setupUploadedImageAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }
    private void fileViewDialog(String path) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.full_size_image_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.horizontalMargin = 20;
            layoutParams.verticalMargin = 20;
            window.setAttributes(layoutParams);
        }
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_IV);
        ZoomageView fullSizeViewIV = dialog.findViewById(R.id.image_viewer);
        WebView web = dialog.findViewById(R.id.dialog_web_view);
        ImageView next = dialog.findViewById(R.id.nextIV);
        ImageView prev = dialog.findViewById(R.id.prevIV);
        TextView count = dialog.findViewById(R.id.countTV);
        progressBar = dialog.findViewById(R.id.progress_bar);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fullSizeViewIV.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        count.setVisibility(View.GONE);
        web.setVisibility(View.VISIBLE);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Show loader when page starts loading
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                web.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        web.getSettings().setSupportZoom(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        Common.showLog("pdfUrl"+path);
        String googleDocsUrl = null;
        try {
            googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(path, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        web.loadUrl(googleDocsUrl);
        dialog.show();
    }
    private void UpdateTransportDialog(String id) {
        updateTransportDialog = new Dialog(getContext());
        updateTransportDialog.setContentView(R.layout.update_vehicle_details_dialog);
        updateTransportDialog.setCancelable(true);
        getPaymentModes("deliveryMode");
        ImageView close = updateTransportDialog.findViewById(R.id.close_vehicle_dialogIV);
        AutoCompleteTextView delivery_mode = updateTransportDialog.findViewById(R.id.delivery_mode);
        AppCompatEditText other_mode = updateTransportDialog.findViewById(R.id.specific_mode_name);
        AppCompatEditText l_r_number = updateTransportDialog.findViewById(R.id.LR_num);
        AppCompatEditText booking_date = updateTransportDialog.findViewById(R.id.booking_date);
        TextView update = updateTransportDialog.findViewById(R.id.updateTV);
        TextInputLayout other_modeIL = updateTransportDialog.findViewById(R.id.specific_mode_nameIL);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransportDialog.dismiss();
            }
        });
        delivery_mode.setAdapter(new AutoCompleteAdapter(getContext(), R.layout.dropdown_item_list, R.id.title_tv, payment_modes));
        booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), booking_date);
            }
        });
        booking_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) Common.openCalenderForUpcomingDates(getActivity(), booking_date);
            }
        });
        delivery_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_mode = payment_modes_value.get(i).toString();
                if (selected_mode.equalsIgnoreCase("Others")) {
                    other_modeIL.setVisibility(View.VISIBLE);
                } else other_modeIL.setVisibility(View.GONE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_mode == null) {
                    Common.showToast(getContext(), "Please select delivery mode");
                    return;
                }
                if (l_r_number.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Please select the valid L.R. Number.");
                    return;
                }
                if (booking_date.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Please select the valid expected arrival date.");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("documentId", id);
                map.put("transportMode", selected_mode);
                if (selected_mode.equalsIgnoreCase("Others")) {
                    if (other_mode.getText().toString().isEmpty()) {
                        Common.showToast(getContext(), "Please provide the specific delivery mode.");
                        return;
                    }
                    other_modeIL.setVisibility(View.VISIBLE);
                    map.put("specificTransportMode", other_mode.getText().toString());
                }
                map.put("lrNumber", l_r_number.getText().toString());
                map.put("expArrivalDate", booking_date.getText().toString());
                map.put(NKeys.UPDATEPOAOTRANSPORTATION, new Gson().toJson(map));
                new NetworkRequest(getContext(), OrderListingFragment.this).callWebServices(ServiceMethods.WS_UPDATEPOAOTRANSPORTATION, map, true);
            }
        });

        updateTransportDialog.show();
    }

    private void getPaymentModes(String mode) {
        HashMap<String, Object> map = new HashMap<>();
        payment_modes.clear();
        payment_modes_value.clear();
        map.put("filter_value", mode);
        map.put(NKeys.GETORDERFILTERVALUE, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, false);
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

    private void showDialog(String orderId,String deliveryAddress) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.order_delivery_location_dialog);
        dialog.setCancelable(false);
        ImageView cross = dialog.findViewById(R.id.cross_on_diaog);
        TextView titleTv = dialog.findViewById(R.id.dialog_title_tv);
        TextView deliveryAddressTv = dialog.findViewById(R.id.delivery_address_tv);
        titleTv.setText("Order" + "(#"+ orderId +") Delivery Location");

        deliveryAddressTv.setText(deliveryAddress);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}