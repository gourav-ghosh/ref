package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.RazorPayListItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentRazorPayBinding;
import com.devstringx.mytylesstockcheck.datamodal.razorpay.RazorpayResponse;
import com.devstringx.mytylesstockcheck.screens.razorpayFilter.RazorPayFilterActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

public class RazorPayFragment extends Fragment implements ResponseListner , RazorPayListItemAdapter.RazorpayBtn {
    RazorPayListItemAdapter adapter;
    FragmentRazorPayBinding razorPayBinding;
    RelativeLayout generate, successfull;
    private TextView dialog_link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        razorPayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_razor_pay, container, false);
        View view = razorPayBinding.getRoot();
        razorPayBinding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRazorpay("");
            }
        });
        if (!Common.getPermission(getContext(), "RZP", "ARZ")) {
            razorPayBinding.createLink.setVisibility(View.GONE);
        }
        getRazorpay("");
        razorPayBinding.createLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        razorPayBinding.filterRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), RazorPayFilterActivity.class), 2);
            }
        });
        razorPayBinding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("pageNumber", "");
                map.put("pageSize", "");
                map.put("search", razorPayBinding.searchET.getText().toString());
                map.put("paymentStatus", Common.razorpayStatus);
                map.put("saleExecutive", Common.selectedSalesExeName);
                map.put("sort", Common.sortByRazorpay);
                map.put("dateRange",Common.dateTypeRazor);
                map.put("fromDate",Common.startDateRazor);
                map.put("toDate",Common.endDateRazor);
                Common.showLog("RESPONSE===+++++++" + map);
                getRazorpay(new Gson().toJson(map));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.generate_razorpay_link_dialog);
        dialog.setCancelable(false);
        generate = dialog.findViewById(R.id.generatge_link_RL);
        successfull = dialog.findViewById(R.id.success_dialog_RL);
        AppCompatButton create = dialog.findViewById(R.id.create_link_dialog);
        AppCompatButton cancel = dialog.findViewById(R.id.cancel_dialog);
        ImageView copy_link = dialog.findViewById(R.id.copy_link);
        ImageView share_link = dialog.findViewById(R.id.link_share_whatsapp);
        ImageView cross = dialog.findViewById(R.id.cross_on_diaog);
        dialog_link = dialog.findViewById(R.id.link_tv);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText orderNo = dialog.findViewById(R.id.sales_order_numberET);
                EditText orderAmount = dialog.findViewById(R.id.order_amountET);
                HashMap<String, Object> map = new HashMap<>();
                if (!orderNo.getText().toString().isEmpty()) map.put("sale_order_no", orderNo.getText().toString());
                map.put("order_amount", orderAmount.getText().toString());
                callGenerateLinkApi(new Gson().toJson(map));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        copy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) dialog.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(dialog_link.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) dialog.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", dialog_link.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(dialog.getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(dialog.getContext(), dialog_link.getText().toString());
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callGenerateLinkApi(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CREATERAZORPAY, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_CREATERAZORPAY, map, true);
    }

    private void getRazorpay(String receivedHashMap) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETRAZORPAY, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETRAZORPAY, map, false);
    }
    private void deletePaymentId(String payment_id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("payment_id", payment_id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DELETERAZORPAY, new Gson().toJson(map1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_DELETERAZORPAY, map, true);
    }

    private void setupRazorpayListAdapter() {
        RecyclerView recyclerView = razorPayBinding.itemRv;
        adapter = new RazorPayListItemAdapter(this,getContext());
        razorPayBinding.itemRv.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(razorPayBinding.refreshLl.isRefreshing()) razorPayBinding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETRAZORPAY) {
                setupRazorpayListAdapter();
                RazorpayResponse razorpayResponse = new Gson().fromJson(responseDO.getResponse(), RazorpayResponse.class);
                BigDecimal bd=new BigDecimal(razorpayResponse.getData().getPaidCount()+razorpayResponse.getData().getPendingCount());
                Common.showLog("====TOTA===="+bd.intValue());
                razorPayBinding.progressBar.setMax(bd.intValue());
                razorPayBinding.progressBar.setProgress(Integer.parseInt(razorpayResponse.getData().getPendingCount()));
                razorPayBinding.paidAmountTv.setText(razorpayResponse.getData().getPaidAmount()+"/-");
                razorPayBinding.pendingAmountTv.setText(razorpayResponse.getData().getPendingAmount()+"/-");
                razorPayBinding.paidCount.setText(String.valueOf(razorpayResponse.getData().getPaidCount()));
                razorPayBinding.pendingCount.setText(String.valueOf(razorpayResponse.getData().getPendingCount()));
                razorPayBinding.totalAmountTv.setText("Rs."+formatToIndianCurrency(Double.parseDouble(razorpayResponse.getData().getTotalAmount()))+"/-");
                adapter.refreshList(razorpayResponse.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CREATERAZORPAY) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (jsonObject.optString("status", "").equalsIgnoreCase("201")) {
                    generate.setVisibility(View.GONE);
                    successfull.setVisibility(View.VISIBLE);
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                    try {
                        String token = jsonObject.getJSONObject("data").getJSONObject("record").getString("generated_link");
                        dialog_link.setText(token);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    getRazorpay("");
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DELETERAZORPAY){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (jsonObject.optString("status", "").equalsIgnoreCase("200")) {
                    Common.showToast(getActivity(), jsonObject.optString("message", ""));
                    getRazorpay("");
                }
            }
        }else Common.showToast(getActivity(),responseDO.getResponse());
    }
    public static String formatToIndianCurrency(double amount) {
        String formattedAmount;
        if (amount >= 1_00_00_000) { // Crore and above
            formattedAmount = String.format("%.2f Cr.", amount / 1_00_00_000);
        } else if (amount >= 1_00_000) { // Lakh to less than Crore
            formattedAmount = String.format("%.2f Lakh", amount / 1_00_000);
        }  else {
            formattedAmount = String.format("%.2f", amount);
        }
        return formattedAmount; //"₹"
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (data!=null) {
                String receivedHashMap = data.getStringExtra("RazorPayFilterDataMap");
                Common.showLog("===================receivedHashMap===" + receivedHashMap);
                getRazorpay(receivedHashMap);
            }
        }
    }

    @Override
    public void onBtnClickListner(int position, String payment_id) {
        deletePaymentId(payment_id);
    }
}