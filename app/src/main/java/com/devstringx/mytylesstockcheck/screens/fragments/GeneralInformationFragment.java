package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.PdfListingAdapter;
import com.devstringx.mytylesstockcheck.adapter.RescheduleHistoryItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentGeneralInformationBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderRescheduleDatasItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneralInformationFragment extends Fragment implements ResponseListner {
    FragmentGeneralInformationBinding binding;
    public Records records;
    OrderRescheduleDatasItem order_reschedule_dates;
    List<String> expectedDeliveryTimeList = new ArrayList<>();
    RescheduleHistoryItemAdapter rescheduleHistoryItemAdapter;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general_information, container, false);
        View view = binding.getRoot();
        records = ((OrderDetailActivity) getActivity()).records;
        if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6") ||
                new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9") ||
                new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("10") ||
                new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("11")) {
            binding.historyTV.setVisibility(View.GONE);
            binding.rescheduleTV.setVisibility(View.GONE);
            binding.shortageOrderRb.setEnabled(false);
            binding.newOrderRb.setEnabled(false);
            binding.instationRb.setEnabled(false);
            binding.outstationRb.setEnabled(false);
        }
        if (records.getStatus().equalsIgnoreCase("Picked_Up") || records.getStatus().equalsIgnoreCase("Delivered"))
            binding.rescheduleTV.setVisibility(View.GONE);
        expectedDeliveryTimeList.add("First half");
        expectedDeliveryTimeList.add("Second half");
        if (records.getDeliveryLocationType() != null) {
            if (records.getDeliveryLocationType().equalsIgnoreCase("Outstation"))
                binding.outstationRb.setChecked(true);
            else binding.instationRb.setChecked(true);
        }
        if (records.getOrderType() != null) {
            if (records.getOrderType().equalsIgnoreCase("shortage_order"))
                binding.shortageOrderRb.setChecked(true);
            else binding.newOrderRb.setChecked(true);
        }
        setData(records);
        binding.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getActivity().getIntent().getStringExtra("orderId"));
                if (binding.shortageOrderRb.isChecked())
                    map.put("orderRequirement", "shortage_order");
                else map.put("orderRequirement", "new_order");
                callUpdateOrderType(new Gson().toJson(map));
            }
        });
        binding.group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getActivity().getIntent().getStringExtra("orderId"));
                if (binding.outstationRb.isChecked()) map.put("orderType", "Outstation");
                else map.put("orderType", "Instation");
                callUpdateDeliveryLocTypeApi(new Gson().toJson(map));
            }
        });
        binding.historyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHistoryDialog();
            }
        });
        binding.rescheduleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRescheduleDialog();
            }
        });
        return view;
    }

    private void callUpdateOrderType(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.UPDATEORDERTYPE, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATEORDERTYPE, map, true);
    }

    private void callUpdateDeliveryLocTypeApi(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.UPDATEDELIVERYLOCATIONTYPE, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_UPDATEDELIVERYLOCATIONTYPE, map, true);
    }

    private void setData(Records records) {
        binding.custTV.setText(records.getCustomerName());
        binding.custCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getContext(), records.getCustomerPhoneNumber());
            }
        });
        binding.custWhatsappIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(getContext(), records.getCustomerPhoneNumber(), "hii");
            }
        });
        if (records.getDeliveryAgent() != null) {
            binding.deliveryAgentTV.setText(records.getDeliveryAgent().getFirstName() + " " + records.getDeliveryAgent().getLastName());
            binding.daCallIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openDialerPad(getContext(), records.getDeliveryAgent().getPhoneNumber());
                }
            });
        }
        if (records.getSalesPerson() != null) {
            binding.saleExeTV.setText(records.getSalesPerson().getFirstName() + " " + records.getSalesPerson().getLastName());
            binding.saleCallIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openDialerPad(getContext(), records.getSalesPerson().getPhoneNumber());
                }
            });
            binding.saleWhatsappIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openWhatsApp(getContext(), records.getSalesPerson().getPhoneNumber(), "hii");
                }
            });
        } else {
            binding.saleExeTV.setText("N/A");
            binding.saleCallIv.setEnabled(false);
            binding.saleWhatsappIv.setEnabled(false);
        }
        if (records.getDeliveryAgent() != null) {
            binding.deliveryAgentTV.setText(records.getDeliveryAgent().getFirstName() + " " + records.getDeliveryAgent().getLastName());
            binding.daCallIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openDialerPad(getContext(), records.getDeliveryAgent().getPhoneNumber());
                }
            });
        } else {
            binding.deliveryAgentTV.setText("N/A");
            binding.daCallIv.setEnabled(false);
            binding.daUserIv.setEnabled(false);
        }
        if (records.getArchitect() != null) {
            binding.archTV.setText(records.getArchitect().getFirstName() + " " + records.getArchitect().getLastName());
            binding.archCallIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openDialerPad(getContext(), records.getArchitect().getPrimaryPhone());
                }
            });
            binding.archWhatsappIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openWhatsApp(getContext(), records.getArchitect().getPrimaryPhone(), "hii");
                }
            });
        } else {
            binding.archTV.setText("N/A");
            binding.archCallIv.setEnabled(false);
            binding.archWhatsappIv.setEnabled(false);
        }
        if (records.getDispatchManager() != null) {
            binding.disTV.setText(records.getDispatchManager().getFirstName() + " " + records.getDispatchManager().getLastName());
            binding.disCallIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openDialerPad(getContext(), records.getDispatchManager().getPhoneNumber());
                }
            });
            binding.disWhatsappIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.openWhatsApp(getContext(), records.getDispatchManager().getPhoneNumber(), "hii");
                }
            });
        } else {
            binding.disTV.setText("N/A");
            binding.disWhatsappIv.setEnabled(false);
            binding.disCallIv.setEnabled(false);
        }
        if (records.getCustomerAddress() != null && !records.getCustomerAddress().isEmpty()) {
            binding.addressTV.setText(records.getCustomerAddress());
        }
        if (!records.getDeliveryModes().isEmpty() && records.getDeliveryModes() != null) {
            binding.modeTV.setText(records.getDeliveryModes().get(0).getDeliveryMode());
        } else binding.modeTV.setText("N/A");
        if (((OrderDetailActivity) getActivity()).data != null)
            binding.totalTV.setText(((OrderDetailActivity) getActivity()).data.getTotalAmount() + "/-");
        if (records.getOrderRescheduleDatas() != null && !records.getOrderRescheduleDatas().isEmpty()) {
            order_reschedule_dates = records.getOrderRescheduleDatas().get(records.getOrderRescheduleDatas().size() - 1);
            if (order_reschedule_dates.getOldDeliveryTime() != null)
                binding.expectedDDTV1.setText(order_reschedule_dates.getOldDeliveryDate() + ", " + order_reschedule_dates.getOldDeliveryTime());
            else binding.expectedDDTV1.setText(order_reschedule_dates.getOldDeliveryDate());
            if (order_reschedule_dates.getNewDeliveryTime() != null)
                binding.expectedDDTV2.setText(order_reschedule_dates.getNewDeliveryDate() + ", " + order_reschedule_dates.getNewDeliveryTime());
            else binding.expectedDDTV2.setText(order_reschedule_dates.getNewDeliveryDate());
        } else {
            binding.expectedDDTV1.setVisibility(View.GONE);
            binding.expectedDDTV2.setText(records.getDeliveryDate() + "," + records.getDeliveryTime());
            binding.historyTV.setVisibility(View.GONE);
        }
    }

    private void showHistoryDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reschedule_history_dialog);
        ImageView cross = dialog.findViewById(R.id.cross_history_iv);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        RecyclerView rescheduleHistoryRV = dialog.findViewById(R.id.historyRV);
        rescheduleHistoryItemAdapter = new RescheduleHistoryItemAdapter(getContext(), records.getOrderRescheduleDatas());
        rescheduleHistoryRV.setHasFixedSize(true);
        rescheduleHistoryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        rescheduleHistoryRV.setAdapter(rescheduleHistoryItemAdapter);
        dialog.show();
    }

    private void showRescheduleDialog() {
        dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reschedule_delivery_dialog);
        ImageView cross = dialog.findViewById(R.id.cross_reschedule_delivery_dialog);
        AutoCompleteTextView time = dialog.findViewById(R.id.expected_delivery_time);
        AppCompatEditText date = dialog.findViewById(R.id.expected_delivery_date_reschedule_dialog);
        AppCompatEditText reason = dialog.findViewById(R.id.reason_updationTV);
        TextView save = dialog.findViewById(R.id.save);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), date);
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) Common.openCalenderForUpcomingDates(getActivity(), date);
            }
        });
        time.setAdapter(new AutoCompleteAdapter(getActivity(), R.layout.dropdown_item_list, R.id.title_tv, expectedDeliveryTimeList));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Expected Delivery Date cannot be empty");
                    return;
                }
                if (time.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Expected Delivery Time cannot be empty");
                    return;
                }
                if (reason.getText().toString().isEmpty()) {
                    Common.showToast(getContext(), "Reason of Updation cannot be empty");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getActivity().getIntent().getStringExtra("orderId"));
                map.put("expDeliveryDate", date.getText().toString());
                if (time.getText().toString().equalsIgnoreCase("First half"))
                    map.put("expDeliveryTime", "first_half");
                else map.put("expDeliveryTime", "second_half");
                map.put("reasonOfUpdation", reason.getText().toString());
                callRescheduleApi(new Gson().toJson(map));

            }
        });
        dialog.show();
    }

    private void callRescheduleApi(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ORDERRESCHEDULE, data);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_ORDERRESCHEDULE, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEORDERTYPE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(getActivity(), jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEDELIVERYLOCATIONTYPE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(getActivity(), jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ORDERRESCHEDULE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(getActivity(), jsonObject.optString("message"));
                        ((OrderDetailActivity) getActivity()).getOrderDetails();
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}