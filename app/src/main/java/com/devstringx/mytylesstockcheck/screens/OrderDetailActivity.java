package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.BuildConfig;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AddMultiplePaymentReceiptAdapter;
import com.devstringx.mytylesstockcheck.adapter.AssignMultipleHelpingAgentsAdapter;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.ComplaintCommentAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.POSelectionAdapter;
import com.devstringx.mytylesstockcheck.adapter.PoCodesAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityOrderDetailBinding;
import com.devstringx.mytylesstockcheck.datamodal.HelpingAgentData;
import com.devstringx.mytylesstockcheck.datamodal.PaymentReceiptData;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.deliveryModes.AllDeliveryModesData;
import com.devstringx.mytylesstockcheck.datamodal.drawerModal.DrawerData;
import com.devstringx.mytylesstockcheck.datamodal.getOrderComment.OrderCommentResponse;
import com.devstringx.mytylesstockcheck.datamodal.getOrderComment.OrderRecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Data;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderDetailsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.screens.fragments.DeliveryDetailsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.GeneralInformationFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.OrderListingFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.PaymentDetailsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.PdfPOandSOFragment;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class OrderDetailActivity extends AppCompatActivity implements ResponseListner, AddMultiplePaymentReceiptAdapter.RemoveReceipt, AssignMultipleHelpingAgentsAdapter.RemoveAgent, UploadFileAdapter.removeImage, PoCodesAdapter.OnClick {
    ActivityOrderDetailBinding binding;
    private Dialog cancelDeleteDialog;
    AppCompatEditText orderType;
    public Records records;
    public Data data;
    Dialog dialog;
    Dialog vehicleDetailDialog;
    Dialog paymentVerifyDialog;
    Dialog agentAssignDialog;
    Dialog imageUploadDialog;
    Dialog orderTrackingDialog;
    Dialog arrangePOCodesDialog;
    private ComplaintCommentAdapter complaintCommentAdapter;
    AddMultiplePaymentReceiptAdapter paymentReceiptAdapter;
    AssignMultipleHelpingAgentsAdapter helpingAgentsAdapter;
    POSelectionAdapter mainPOSelectionAdapter;
    List<String> payment_modes = new ArrayList<>();
    List<String> payment_modes_value = new ArrayList<>();
    private List<String> po_codes = new ArrayList<>();
    List<String> selectedMainAgentPOList = new ArrayList<>();
    List<String> allCheckedPOList = new ArrayList<>();
    List<String> expectedDeliveryTimeList = new ArrayList<>();
    private List<RecordsItem> agentNameList;
    private List<RecordsItem> deliveryModes;
    private List<RecordsItem> payer;
    private List<POItem> poDocs;
    private CustomAutoCompleteListAdapter cityAdapter;
    private CustomAutoCompleteListAdapter deliveryModesAdapter;
    private CustomAutoCompleteListAdapter payerAdapter;
    private RecyclerView commentsRV;
    private RelativeLayout noCommentTV;
    private TextView commentET;
    private String shortURL;
    String selected_mode;
    private String filterValue;
    private RecyclerView dialogRecyclerView;
    private RecyclerView dialogPOCodesRecyclerView;
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private UploadFileAdapter imageAdapter;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private HashSet<String> uniqueCheckedPODocs = new HashSet<>();
//    private DashboardViewPager2Adapter adapter;
    private String orderStatus = "";
    private String deliveryType = "";
    private Boolean isHelpingAgent = false;
    private Boolean isDeliveryAgent = false;
    private String userRoleId = "";
    private HashSet<String> selectedPOCodes = new HashSet<>();
    private PoCodesAdapter poCodesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        if (!Common.getPermission(this, "ODS", "EODS")){
            binding.editIv.setVisibility(View.GONE);
            binding.changeIcon.setVisibility(View.GONE);
            binding.changeDeliveryTypeIV.setEnabled(false);
        }
        if (!Common.getPermission(this, "ODS", "DODS")) binding.deleteIv.setVisibility(View.GONE);
        if (!Common.getPermission(this, "ODS", "CODS")) binding.blockIv.setVisibility(View.GONE);
        userRoleId = new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"");
        customizeDetailsTabs();
        getOrderDetails();
        binding.changeDeliveryTypeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.deliveryTypeTV.getText().toString().equalsIgnoreCase("Pick Up")) {
                    showDeliveryTypeDialog("Pick Up");
                } else {
                    showDeliveryTypeDialog("Home Delivery");
                }
            }
        });
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.pdfSoPoRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new PdfPOandSOFragment(), R.id.order_view_pager);
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("pdf"));
                } else if (binding.generalInfoRb.isChecked()) {
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("info"));
                    Common.loadFragment(getSupportFragmentManager(), new GeneralInformationFragment(), R.id.order_view_pager);
                } else if (binding.paymentDetailsRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new PaymentDetailsFragment(), R.id.order_view_pager);
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("payment"));
                } else if (binding.deliveryDetailsRb.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new DeliveryDetailsFragment(), R.id.order_view_pager);
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("delivery"));
                }
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        TODO add roles condition for giving tracking link option
        binding.shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrderTrackingDialog();
            }
        });
        binding.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Delete", "want to delete this order  permanently?", "Confirm Delete");
            }
        });
        binding.mailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderCommentDialog();
            }
        });
        binding.blockIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Confirm", "Are you sure to cancel this order?", "Confirm Cancel");
            }
        });
//        binding.viewPager.setCurrentItem(adapter.getFragPosition("pdf"));
//        binding.viewPager.setOffscreenPageLimit(adapter.getItemCount());
//        binding.viewPager.setAdapter(adapter);
//        binding.viewPager.setUserInputEnabled(false);
    }

    private void customizeDetailsTabs() {
        if(userRoleId.equalsIgnoreCase("8") || userRoleId.equalsIgnoreCase("9")) {
            binding.paymentDetailsRb.setText("Shipping Charge Details");
        }
    }

    private void openOrderTrackingDialog() {
        int colonIndex = BuildConfig.BASE_URL.indexOf(':', 10);
        String hostURL = BuildConfig.BASE_URL.substring(0, colonIndex);
        shortURL = hostURL+"/track-order-status/"+records.getShortUrl();
        orderTrackingDialog = new Dialog(this);
        orderTrackingDialog.setContentView(R.layout.order_tracking_dialog);
        orderTrackingDialog.setCancelable(true);
        ImageView close = orderTrackingDialog.findViewById(R.id.orderTrackingClose);
        TextView urlTV = orderTrackingDialog.findViewById(R.id.urlTV);
        ImageView copyIV = orderTrackingDialog.findViewById(R.id.copyIV);
        ImageView whatsappIV = orderTrackingDialog.findViewById(R.id.whatsappIV);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderTrackingDialog.dismiss();
            }
        });
        urlTV.setText(shortURL);
        copyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyShortURL(shortURL);
            }
        });
        whatsappIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTrackingLink(shortURL);
            }
        });
        orderTrackingDialog.show();
    }

    private void shareTrackingLink(String shortURL) {
        Common.openWhatsApp(this, records.getCustomerPhoneNumber(), shortURL);
    }

    private void copyShortURL(String shortURL) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Order Tracking Link: ", shortURL);
        clipboard.setPrimaryClip(clip);
        Common.showToast(this, "Order Tracking Link copied to clipboard");
    }

    public void getOrderDetails() {
        HashMap<String, Object> map = new HashMap<>();
        Common.showLog("orderID====" + getIntent().getStringExtra("orderId"));
        map.put("order_id", getIntent().getStringExtra("orderId"));
        map.put(NKeys.GETORDERDETAILS, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETORDERDETAILS, map, true);
    }

    private void showOrderCommentDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.complaint_chat_dialog);
        dialog.setCancelable(false);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        TextView title = dialog.findViewById(R.id.titleTV);
        title.setText("Comment on Order");
        commentsRV = dialog.findViewById(R.id.comments_rv);
        ImageView send = dialog.findViewById(R.id.sendIV);
        commentET = dialog.findViewById(R.id.commentET);
        noCommentTV = dialog.findViewById(R.id.no_comment_tv);
        getOrderCommentApi();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentET.getText().toString().isEmpty()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("order_id", getIntent().getStringExtra("orderId"));
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

    private void callCreateOrderCommentApi(HashMap<String, Object> map) {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_SENDORDERCOMMENT, map, true);
    }

    private void getOrderCommentApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("orderId"));
        map.put(NKeys.GETORDERCOMMENT, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETORDERCOMMENT, map, true);
    }

    private void showVehicleDetailsDialog() {
        vehicleDetailDialog = new Dialog(this);
        vehicleDetailDialog.setContentView(R.layout.loading_vehicle_detail_dialog);
        vehicleDetailDialog.setCancelable(true);
        payment_modes.clear();
        payment_modes_value.clear();
        getPaymentModes("deliveryMode");
        AppCompatEditText driver_name = vehicleDetailDialog.findViewById(R.id.driver_name);
        ImageView close = vehicleDetailDialog.findViewById(R.id.close_vehicle_dialogIV);
        AutoCompleteTextView delivery_mode = vehicleDetailDialog.findViewById(R.id.delivery_mode);
        AppCompatEditText vehicle_num = vehicleDetailDialog.findViewById(R.id.vehicle_num);
        AppCompatEditText mobile_num = vehicleDetailDialog.findViewById(R.id.mobile_numET);
        AppCompatEditText other_mode = vehicleDetailDialog.findViewById(R.id.specific_mode_name);
        AppCompatEditText l_r_number = vehicleDetailDialog.findViewById(R.id.LR_num);
        TextInputLayout driver_nameIL = vehicleDetailDialog.findViewById(R.id.driver_nameIL);
        TextInputLayout vehicle_numIL = vehicleDetailDialog.findViewById(R.id.vehicle_numIL);
        TextInputLayout mobile_numIL = vehicleDetailDialog.findViewById(R.id.mobile_numIL);
        TextInputLayout other_modeIL = vehicleDetailDialog.findViewById(R.id.specific_mode_nameIL);
        TextInputLayout l_r_numberIL = vehicleDetailDialog.findViewById(R.id.LR_numIL);
        AppCompatEditText booking_date = vehicleDetailDialog.findViewById(R.id.booking_date);
        AppCompatEditText comment = vehicleDetailDialog.findViewById(R.id.commentET);
        ImageView save = vehicleDetailDialog.findViewById(R.id.save_details_cb);
        TextView update = vehicleDetailDialog.findViewById(R.id.updateTV);
        delivery_mode.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, payment_modes));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleDetailDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!save.isSelected()) {
                    save.setSelected(true);
                    save.setBackgroundResource(R.drawable.orange_checkbox_selected);
                } else {
                    save.setSelected(false);
                    save.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                }
            }
        });
        booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(OrderDetailActivity.this, booking_date);
            }
        });
        booking_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) Common.openCalenderForUpcomingDates(OrderDetailActivity.this, booking_date);
            }
        });
        delivery_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_mode = payment_modes_value.get(i).toString();
                if (selected_mode.equalsIgnoreCase("Others")) {
                    other_modeIL.setVisibility(View.VISIBLE);
                    l_r_numberIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.VISIBLE);
                    mobile_numIL.setVisibility(View.VISIBLE);
                    vehicle_numIL.setVisibility(View.VISIBLE);
                } else if (selected_mode.equalsIgnoreCase("Spoton") || selected_mode.equalsIgnoreCase("Jabbar") || selected_mode.equalsIgnoreCase("SVR") || selected_mode.equalsIgnoreCase("DTDC")) {
                    l_r_numberIL.setVisibility(View.VISIBLE);
                    other_modeIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.GONE);
                    mobile_numIL.setVisibility(View.GONE);
                    vehicle_numIL.setVisibility(View.GONE);
                } else {
                    other_modeIL.setVisibility(View.GONE);
                    l_r_numberIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.VISIBLE);
                    mobile_numIL.setVisibility(View.VISIBLE);
                    vehicle_numIL.setVisibility(View.VISIBLE);
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_mode == null) {
                    Common.showToast(OrderDetailActivity.this, "Please select delivery mode");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getIntent().getStringExtra("orderId"));
                map.put("deliveryMode", selected_mode);
                if (selected_mode.equalsIgnoreCase("Others")) {
                    if (other_mode.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please provide the specific delivery mode.");
                        return;
                    } else if (driver_name.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please provide the driver name.");
                        return;
                    } else if (mobile_num.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please enter the valid mobile number.");
                        return;
                    } else if (booking_date.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please select the valid booking date.");
                        return;
                    }
                    other_modeIL.setVisibility(View.VISIBLE);
                    l_r_numberIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.VISIBLE);
                    mobile_numIL.setVisibility(View.VISIBLE);
                    vehicle_numIL.setVisibility(View.VISIBLE);
                    map.put("specificDeliveryMode", other_mode.getText().toString());
                    map.put("driverName", driver_name.getText().toString());
                    map.put("phoneNumber", mobile_num.getText().toString());
                    map.put("vehicleNumber", vehicle_num.getText().toString());
                    map.put("bookingDate", booking_date.getText().toString());
                    map.put("comment", comment.getText().toString());

                } else if (selected_mode.equalsIgnoreCase("Spoton") || selected_mode.equalsIgnoreCase("Jabbar") || selected_mode.equalsIgnoreCase("SVR") || selected_mode.equalsIgnoreCase("DTDC")) {
                    if (l_r_number.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please enter the valid L.R. Number.");
                        return;
                    } else if (booking_date.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please select the valid booking date.");
                        return;
                    }
                    l_r_numberIL.setVisibility(View.VISIBLE);
                    other_modeIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.GONE);
                    mobile_numIL.setVisibility(View.GONE);
                    vehicle_numIL.setVisibility(View.GONE);
                    map.put("lrNumber", l_r_number.getText().toString());
                    map.put("bookingDate", booking_date.getText().toString());
                    map.put("comment", comment.getText().toString());
                } else {
                    if (driver_name.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please provide the driver name.");
                        return;
                    } else if (mobile_num.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please enter the valid mobile number.");
                        return;
                    } else if (booking_date.getText().toString().isEmpty()) {
                        Common.showToast(OrderDetailActivity.this, "Please select the valid booking date.");
                        return;
                    }
                    other_modeIL.setVisibility(View.GONE);
                    l_r_numberIL.setVisibility(View.GONE);
                    driver_nameIL.setVisibility(View.VISIBLE);
                    mobile_numIL.setVisibility(View.VISIBLE);
                    vehicle_numIL.setVisibility(View.VISIBLE);
                    map.put("driverName", driver_name.getText().toString());
                    map.put("phoneNumber", mobile_num.getText().toString());
                    map.put("vehicleNumber", vehicle_num.getText().toString());
                    map.put("bookingDate", booking_date.getText().toString());
                    map.put("comment", comment.getText().toString());
                }
                map.put(NKeys.STARTLOADING, new Gson().toJson(map));
                new NetworkRequest(OrderDetailActivity.this, OrderDetailActivity.this).callWebServices(ServiceMethods.WS_STARTLOADING, map, true);
            }
        });
        vehicleDetailDialog.show();
    }

    private void showDeliveryTypeDialog(String curr_type) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_delivery_type_dialog);
        dialog.setCancelable(false);
        List<String> expectedDeliveryTimeList = new ArrayList<>();
        expectedDeliveryTimeList.add("First half");
        expectedDeliveryTimeList.add("Second half");
        TextInputLayout exp_time = dialog.findViewById(R.id.time);
        LinearLayout deleteAddressLL = dialog.findViewById(R.id.delete_delivery_locLL);
        AppCompatEditText date = dialog.findViewById(R.id.arrange_before_dateET);
        AppCompatEditText addressET = dialog.findViewById(R.id.addressET);
        AppCompatEditText deliveryAgent = dialog.findViewById(R.id.delivery_agent_name);
        orderType = dialog.findViewById(R.id.delivery_order_typeET);
        ImageView cross = dialog.findViewById(R.id.cross_OTD_IV);
        ImageView check = dialog.findViewById(R.id.pick_up_iv);
        AppCompatButton yes = dialog.findViewById(R.id.order_delivery_saveBtn);
        AppCompatButton no = dialog.findViewById(R.id._order_delivery_cancelBtn);
        AutoCompleteTextView time = dialog.findViewById(R.id.expected_delivery_time);
        if (!curr_type.equalsIgnoreCase("Pick Up")) {
            orderType.setText("Pick Up");

        } else {
            orderType.setText("Home Delivery");
        }
        if (orderType.getText().toString().equalsIgnoreCase("Pick Up")) {
            deleteAddressLL.setVisibility(View.VISIBLE);
            addressET.setEnabled(false);
            addressET.setFocusable(false);
        } else {
            exp_time.setVisibility(View.VISIBLE);
        }
        if (records.getDeliveryAddress() != null && !records.getDeliveryAddress().isEmpty()) {
            addressET.setText(records.getDeliveryAddress());
        }
        if (records.getDeliveryAgent() != null) {
            deliveryAgent.setText(records.getDeliveryAgent().getFirstName() + " " + records.getDeliveryAgent().getLastName());
        } else deliveryAgent.setText("N/A");
        check.setSelected(true);
        if (check.isSelected()) check.setBackgroundResource(R.drawable.orange_checkbox_selected);
        else check.setBackgroundResource(R.drawable.orange_checkbox_unselected);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check.isSelected()) {
                    check.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    check.setSelected(true);
                } else {
                    check.setSelected(false);
                    check.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(OrderDetailActivity.this, date);
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) Common.openCalenderForUpcomingDates(OrderDetailActivity.this, date);
            }
        });
        time.setAdapter(new AutoCompleteAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, expectedDeliveryTimeList));

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
                if (date.getText().toString().isEmpty()) {
                    Common.showToast(dialog.getContext(), "Delivery Date is not allowed to be empty.");
                    return;
                }
                if (orderType.getText().toString().equalsIgnoreCase("Home Delivery") && time.getText().toString().isEmpty()) {
                    Common.showToast(dialog.getContext(), "Delivery time is not allowed to be empty.");
                    return;
                }
                if (orderType.getText().toString().equalsIgnoreCase("Home Delivery") && addressET.getText().toString().isEmpty()) {
                    Common.showToast(dialog.getContext(), "Delivery address is not allowed to be empty.");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getIntent().getStringExtra("orderId"));
                map.put("deliveryDate", date.getText().toString());
                map.put("deliveryType", orderType.getText().toString());
                if (orderType.getText().toString().equalsIgnoreCase("Home Delivery")) {
                    map.put("deliveryAddress", addressET.getText().toString());
                    if (time.getText().toString().equalsIgnoreCase("Second half"))
                        map.put("deliveryTime", "second_half");
                    else map.put("deliveryTime", "first_half");
                } else {
                    if (check.isSelected()) map.put("deliveryAddressDelete", "yes");
                    else map.put("deliveryAddressDelete", "no");
                }
                callChangeStatusApi(new Gson().toJson(map));
            }
        });
        dialog.show();
    }

    private void callChangeStatusApi(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.UPDATEDELIVERYTYPE, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEDELIVERYTYPE, map, true);
    }

    public void showCustomDialog(String button_msg, String msg, String title_msg) {
        cancelDeleteDialog = new Dialog(this);
        cancelDeleteDialog.setContentView(R.layout.order_delete_dialog);
        cancelDeleteDialog.setCancelable(false);
        ImageView cross = cancelDeleteDialog.findViewById(R.id.order_dialog_crossIV);
        TextView title = cancelDeleteDialog.findViewById(R.id.order_dialog_titleTV);
        TextView text = cancelDeleteDialog.findViewById(R.id.order_dialog_text);
        TextView yes = cancelDeleteDialog.findViewById(R.id.order_dialog_yes);
        TextView no = cancelDeleteDialog.findViewById(R.id.order_dialog_no);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDeleteDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDeleteDialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button_msg.equalsIgnoreCase("Delete")) {
                    callOrderDeleteApi();
                } else if (button_msg.equalsIgnoreCase("Payment Reject")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("order_id", getIntent().getStringExtra("orderId"));
                    map.put("paymentStatus", "accept");
                    map.put(NKeys.UPDATEPAYMENT, new Gson().toJson(map));
                    new NetworkRequest(OrderDetailActivity.this, OrderDetailActivity.this).callWebServices(ServiceMethods.WS_UPDATEPAYMENT, map, true);
                } else {
                    callOrderCancelApi();
                }
            }
        });
        title.setText(title_msg);
        text.setText(msg);
        yes.setText(button_msg);
        cancelDeleteDialog.show();
    }

    private void callOrderCancelApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("orderId"));
        map.put(NKeys.ORDERCANCEL, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ORDERCANCEL, map, true);
    }

    private void callOrderDeleteApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("orderId"));
        map.put(NKeys.ORDERDELETE, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ORDERDELETE, map, true);
    }
    public static boolean isWithinFiveMinutes(String responseDate) {
        // Parse the given date string
        LocalDateTime wfCreatedAt = LocalDateTime.parse(responseDate, DateTimeFormatter.ISO_DATE_TIME);

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Calculate the difference between the two times in minutes
        long diffInMinutes = Duration.between(wfCreatedAt, now).toMinutes();

        // Check if the difference is less than or equal to 5 minutes
        return diffInMinutes <= 5;
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERDETAILS) {
                OrderDetailsResponse response = new Gson().fromJson(responseDO.getResponse(), OrderDetailsResponse.class);
                records = response.getData().getRecords();
                orderStatus = records.getStatus();
                deliveryType = records.getDeliveryType();
                if(new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("9")) {
                    isDeliveryAgent = true;
                    isHelpingAgent = !new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.USERID, "").equalsIgnoreCase(records.getDeliveryAgent().getId());
                } else isDeliveryAgent = false;
                redirectionsHandler();
                setPermissions();
                setData();
                if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("6")) {
                    if (isWithinFiveMinutes(records.getWfCreatedAt())){
                        binding.editIv.setVisibility(View.VISIBLE);
                    }else binding.editIv.setVisibility(View.GONE);
                } else if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("10")) {
                    if (records.getStatus().equalsIgnoreCase("To_Be_Approved_From_Account")) binding.updateProgressTV.setVisibility(View.VISIBLE);
                    else binding.updateProgressTV.setVisibility(View.GONE);
                }
                data = response.getData();
                poDocs = new ArrayList<>();
                poDocs.addAll(records.getDocuments().getPO());
                payment_modes.clear();
                payment_modes_value.clear();
                getPaymentModes("paymentMode");
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ORDERCANCEL) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        cancelDeleteDialog.dismiss();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ORDERDELETE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETPOCODES) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray records = data.getJSONArray("poCodes");
                        po_codes.clear();
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject record = records.getJSONObject(i);
                            String value = record.getString("value");
                            po_codes.add(value);
                        }
                        markPOCodesarranged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_POARRANGE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        arrangePOCodesDialog.dismiss();
                        Common.showToast(this, jsonObject.optString("message"));
                        if(isDeliveryAgent && !isHelpingAgent)
                            updateOrderStatus("Reaching_Store");
                        else getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_CHANGEORDERSTATUS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERFILTERVALUE) {       //For payment_mode
                if(filterValue.equalsIgnoreCase("paymentMode")) {
                    filterValue="";
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
                } else if(filterValue.equalsIgnoreCase("deliveryMode")) {
                    filterValue="";
                    AllDeliveryModesData allDeliveryModesData = new Gson().fromJson(responseDO.getResponse(), AllDeliveryModesData.class);
                    deliveryModes = new ArrayList<>();
                    for (int i = 0; i < allDeliveryModesData.getData().getRecords().size(); i++) {
                        RecordsItem recordsItem = new RecordsItem();
                        recordsItem.setCityName(allDeliveryModesData.getData().getRecords().get(i).getLabel());
                        recordsItem.setId(String.valueOf(allDeliveryModesData.getData().getRecords().get(i).getValue()));
                        deliveryModes.add(recordsItem);
                    }
                    getPayer();
                } else if(filterValue.equalsIgnoreCase("payer")) {
                    AllDeliveryModesData payerData = new Gson().fromJson(responseDO.getResponse(), AllDeliveryModesData.class);
                    payer = new ArrayList<>();
                    for (int i = 0; i < payerData.getData().getRecords().size(); i++) {
                        RecordsItem recordsItem = new RecordsItem();
                        recordsItem.setCityName(payerData.getData().getRecords().get(i).getLabel());
                        recordsItem.setId(String.valueOf(payerData.getData().getRecords().get(i).getValue()));
                        payer.add(recordsItem);
                    }
                    showAgentAssignDialog();
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEDELIVERYTYPE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        binding.deliveryTypeTV.setText(orderType.getText().toString());
                        dialog.dismiss();
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEPAYMENT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        paymentVerifyDialog.dismiss();
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                agentNameList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    recordsItem.setStateName(String.valueOf(allOwnersData.getData().getRecords().get(i).getAskPayment()));
                    agentNameList.add(recordsItem);
                }
                getDeliveryModes();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ASSIGNDELIVERYAGENT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        agentAssignDialog.dismiss();
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_STARTLOADING) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(this, jsonObject.optString("message"));
                        vehicleDetailDialog.dismiss();
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        OrderCommentResponse response = new Gson().fromJson(responseDO.getResponse(), OrderCommentResponse.class);
                        if (response.getData().getCount() == 0) {
                            noCommentTV.setVisibility(View.VISIBLE);
                            commentsRV.setVisibility(View.GONE);
                        } else {
                            noCommentTV.setVisibility(View.GONE);
                            commentsRV.setVisibility(View.VISIBLE);
                        }
                        setupCommentAdapter(this, response.getData().getRecords());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_SENDORDERCOMMENT) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(this, jsonObject.optString("message", ""));
                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                        commentET.setText("");
                        getOrderCommentApi();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADMULTIPLEORDERFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        imageUploadDialog.dismiss();
                        Common.showToast(this, jsonObject.optString("message").toString());
                        getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        } else Common.showToast(this,responseDO.getResponse());
    }

    private void redirectionsHandler() {
//        Common.showLog("order_details======"+getIntent().getStringExtra("source_page") );
        if (getIntent().getStringExtra("source_page")!=null){
            if (getIntent().getStringExtra("source_page").equalsIgnoreCase("shipping_charges")){
                binding.paymentDetailsRb.setChecked(true);
                Common.loadFragment(getSupportFragmentManager(), new PaymentDetailsFragment(), R.id.order_view_pager);
            }else if (getIntent().getStringExtra("source_page").equalsIgnoreCase("CMS") ||
                    getIntent().getStringExtra("source_page").equalsIgnoreCase("architect_order")){
                binding.generalInfoRb.setChecked(true);
                Common.loadFragment(getSupportFragmentManager(), new GeneralInformationFragment(), R.id.order_view_pager);
            }
        }else {
            binding.pdfSoPoRb.setChecked(true);
            Common.loadFragment(getSupportFragmentManager(), new PdfPOandSOFragment(), R.id.order_view_pager);
        }
    }

    private void setPermissions() {
        if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            binding.poView.setVisibility(View.GONE);
            binding.poIv.setVisibility(View.GONE);
            binding.poTV.setVisibility(View.GONE);
            binding.updateProgressTV.setVisibility(View.GONE);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    0,
                    getResources().getDimensionPixelSize(R.dimen.dimen5),
                    11
            );
            int width = getResources().getDimensionPixelSize(R.dimen.dimen45); // Replace with your dimension resource

            LinearLayout.LayoutParams imageVParams = new LinearLayout.LayoutParams(
                    0,
                    width,
                    11
            );

            binding.accIv.setLayoutParams(imageVParams);
            binding.progressIv.setLayoutParams(imageVParams);
            binding.deliveredIv.setLayoutParams(imageVParams);
            binding.loadingIv.setLayoutParams(imageVParams);
            binding.dispatchIv.setLayoutParams(imageVParams);
            viewParams.gravity = Gravity.CENTER_VERTICAL;
            viewParams.setMarginStart(-10);
            viewParams.setMarginEnd(-10);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    20
            );
            binding.accTV.setLayoutParams(textParams);
            binding.processTV.setLayoutParams(textParams);
            binding.loadingTV.setLayoutParams(textParams);
            binding.dispatchTV.setLayoutParams(textParams);
            binding.deliveredTV.setLayoutParams(textParams);
            binding.progressView.setLayoutParams(viewParams);
            binding.loadingView.setLayoutParams(viewParams);
            binding.dispatchView.setLayoutParams(viewParams);
            binding.deliveredView.setLayoutParams(viewParams);
        } else if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("10")) {
            binding.changeIcon.setVisibility(View.GONE);
            binding.changeDeliveryTypeIV.setEnabled(false);
        } else if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("11")) {
            binding.changeIcon.setVisibility(View.GONE);
            binding.changeDeliveryTypeIV.setEnabled(false);
            binding.mailIv.setVisibility(View.GONE);
            binding.accIv.setVisibility(View.GONE);
            binding.poIv.setVisibility(View.GONE);
            binding.poView.setVisibility(View.GONE);
            binding.progressView.setVisibility(View.GONE);
            binding.progressIv.setVisibility(View.GONE);
            binding.loadingView.setVisibility(View.GONE);
            binding.accTV.setVisibility(View.GONE);
            binding.poTV.setVisibility(View.GONE);
            binding.processTV.setVisibility(View.GONE);

            binding.updateProgressTV.setVisibility(View.GONE);

            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.dimen30),
                    getResources().getDimensionPixelSize(R.dimen.dimen5)
            );
            int width = getResources().getDimensionPixelSize(R.dimen.dimen45); // Replace with your dimension resource

            LinearLayout.LayoutParams imageVParams = new LinearLayout.LayoutParams(
                    width,
                    width
            );


            binding.deliveredIv.setLayoutParams(imageVParams);
            binding.dispatchIv.setLayoutParams(imageVParams);
            viewParams.gravity = Gravity.CENTER_VERTICAL;
            viewParams.setMarginStart(-10);
            viewParams.setMarginEnd(-10);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.BELOW, R.id.progress_statusLL);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            binding.statusTextLL.setLayoutParams(params);
            textParams.setMargins(12,0,12,0);
            if (records.getDeliveryType().equalsIgnoreCase("Pick Up")){
                binding.dispatchView.setLayoutParams(viewParams);
                binding.loadingIv.setLayoutParams(imageVParams);
                binding.loadingTV.setLayoutParams(textParams);
            }else {
                binding.dispatchView.setVisibility(View.GONE);
                binding.loadingIv.setVisibility(View.GONE);
                binding.loadingTV.setVisibility(View.GONE);
            }
            binding.dispatchTV.setLayoutParams(textParams);
            binding.deliveredTV.setLayoutParams(textParams);
            binding.dispatchView.setLayoutParams(viewParams);
            binding.deliveredView.setLayoutParams(viewParams);
        }else if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("9")) {
            binding.changeIcon.setVisibility(View.GONE);
            binding.changeDeliveryTypeIV.setEnabled(false);
            binding.accIv.setVisibility(View.GONE);
            binding.poIv.setVisibility(View.GONE);
            binding.poView.setVisibility(View.GONE);
            binding.progressView.setVisibility(View.GONE);
            binding.accTV.setVisibility(View.GONE);
            binding.poTV.setVisibility(View.GONE);

            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.dimen30),
                    getResources().getDimensionPixelSize(R.dimen.dimen5)
            );
            int width = getResources().getDimensionPixelSize(R.dimen.dimen45); // Replace with your dimension resource

            LinearLayout.LayoutParams imageVParams = new LinearLayout.LayoutParams(
                    width,
                    width
            );
            viewParams.gravity = Gravity.CENTER_VERTICAL;
            viewParams.setMarginStart(-10);
            viewParams.setMarginEnd(-10);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.BELOW, R.id.progress_statusLL);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            binding.statusTextLL.setLayoutParams(params);
            textParams.setMargins(12,0,12,0);

            binding.progressIv.setLayoutParams(imageVParams);
            binding.processTV.setLayoutParams(textParams);
            binding.loadingView.setLayoutParams(viewParams);
            binding.loadingTV.setLayoutParams(textParams);
            binding.loadingIv.setLayoutParams(imageVParams);
            binding.dispatchTV.setLayoutParams(textParams);
            binding.dispatchView.setLayoutParams(viewParams);
            binding.dispatchIv.setLayoutParams(imageVParams);
            binding.deliveredTV.setLayoutParams(textParams);
            binding.deliveredView.setLayoutParams(viewParams);
            binding.deliveredIv.setLayoutParams(imageVParams);
        }else if (new PreferenceUtils(OrderDetailActivity.this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("8")) {
            if (records.getShippingChargeDetails()==null || records.getShippingChargeDetails().isEmpty()){
                binding.updateProgressTV.setVisibility(View.VISIBLE);
            }else binding.updateProgressTV.setVisibility(View.GONE);
        }

    }

    private void setupCommentAdapter(Context context, List<OrderRecordsItem> records) {
        complaintCommentAdapter = new ComplaintCommentAdapter(context, records, "order");
        commentsRV.setHasFixedSize(true);
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        commentsRV.setAdapter(complaintCommentAdapter);
    }
    private void handleUpdateProgressTvVisibility() {
        if(!userRoleId.equalsIgnoreCase("")) {
            switch (records.getStatus()) {
                case "To_Be_Approved_From_Account":
                    if(userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("10")) {
                        binding.updateProgressTV.setVisibility(View.VISIBLE);
                    } else binding.updateProgressTV.setVisibility(View.GONE);
                    break;
                case "Pending_At_Dispatch_Manager":
                case "Po_Sent":
                case "Reaching_Store":
                    if(userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("8")) {
                        binding.updateProgressTV.setVisibility(View.VISIBLE);
                    } else binding.updateProgressTV.setVisibility(View.GONE);
                    break;
                case "Order_Processed":
                case "Loading":
                case "Dispatched":
                    if(userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("8") || userRoleId.equalsIgnoreCase("9")) {
                        binding.updateProgressTV.setVisibility(View.VISIBLE);
                    } else binding.updateProgressTV.setVisibility(View.GONE);
                    break;
                case "Ready_For_Pick_Up":
                    if(userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("11")) {
                        binding.updateProgressTV.setVisibility(View.VISIBLE);
                    } else binding.updateProgressTV.setVisibility(View.GONE);
                    break;
                default:
                    binding.updateProgressTV.setVisibility(View.GONE);
            }
        }
    }
    private void setData() {
        switch (records.getStatus()) {
            case "To_Be_Approved_From_Account":
                binding.CreatedTag.setText("Acc. Pending");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.gray_bg10);
                break;
            case "Pending_At_Dispatch_Manager":
                binding.CreatedTag.setText("Pending");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            case "Po_Sent":
                binding.CreatedTag.setText("Pending");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            case "Order_Processed":
                binding.CreatedTag.setText("Assign");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.black_round_bg);
                break;
            case "Loading":
                binding.CreatedTag.setText("loading");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Reaching_Store":
                binding.CreatedTag.setText("Reached");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Ready_For_Pick_Up":
                binding.CreatedTag.setText("Ready");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Dispatched":
                binding.CreatedTag.setText("Dispatched");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Delivered":
            case "Picked_Up":
                binding.CreatedTag.setText("Completed");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.green_round_bg);
                break;
            case "Cancelled":
                binding.CreatedTag.setText("Cancelled");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.red_round_bg);
                break;
            default:
                binding.CreatedTag.setText("Acc. Pending");
                binding.CreatedTag.setTextColor(getColor(R.color.white));
                binding.CreatedTag.setBackgroundResource(R.drawable.gray_bg10);
        }
        switch (records.getStatus().toString()) {
            case "To_Be_Approved_From_Account":
                binding.updateProgressTV.setText("Acc. Verify");
                binding.updateProgressTV.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Pending_At_Dispatch_Manager":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.updateProgressTV.setText("Agent assign");
                binding.updateProgressTV.setBackgroundResource(R.drawable.black_round_bg);
                break;
            case "Po_Sent":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText("Agent assign");
                binding.updateProgressTV.setBackgroundResource(R.drawable.black_round_bg);
                break;
            case "Order_Processed":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText(records.getDeliveryType().toString().equalsIgnoreCase("Pick Up") ? "Material Arranged" : "Start Loading");
                binding.updateProgressTV.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Loading":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.loadingIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.loadingView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText("Dispatched");
                binding.updateProgressTV.setBackgroundResource(R.drawable.orange_round_bg);
                break;
            case "Reaching_Store":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.loadingIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.loadingView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText("Ready For Pick Up");
                binding.updateProgressTV.setBackgroundResource(R.drawable.orange_round_bg);
//                setNextStatus('Ready For Pickup');
//                setNextStatusBGColor('bg-[#F7941D]');
                break;
            case "Ready_For_Pick_Up":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.loadingIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.loadingView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.dispatchIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.dispatchView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText("Picked Up");
                binding.updateProgressTV.setBackgroundResource(R.drawable.green_round_bg);
                break;
            case "Dispatched":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.loadingIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.loadingView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.dispatchIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.dispatchView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setText("Complete");
                binding.updateProgressTV.setBackgroundResource(R.drawable.green_round_bg);
                break;
            case "Delivered":
            case "Picked_Up":
                binding.accIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.poView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.progressIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.progressView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.loadingIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.loadingView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.dispatchIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.dispatchView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.deliveredIv.setBackgroundResource(R.drawable.green_fill_circle);
                binding.deliveredView.setBackgroundColor(getResources().getColor(R.color.green));
                binding.updateProgressTV.setVisibility(View.GONE);
                binding.blockIv.setVisibility(View.GONE);
                binding.changeIcon.setVisibility(View.GONE);
                binding.changeDeliveryTypeIV.setEnabled(false);
                break;
        }
        String[] restrictedStatus = {"To_Be_Approved_From_Account", "Picked_Up", "Delivered", "Cancelled"};
        if((userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("8")) && !Arrays.asList(restrictedStatus).contains(orderStatus)) {
            binding.markOrderCompleteTV.setVisibility(View.VISIBLE);
        } else binding.markOrderCompleteTV.setVisibility(View.GONE);
        binding.markOrderCompleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deliveryType.equalsIgnoreCase("Home Delivery")) {
                    updateOrderStatus("Delivered");
                } else if (deliveryType.equalsIgnoreCase("Pick Up")) {
                    updateOrderStatus("Picked_Up");
                }
            }
        });
        binding.updateProgressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> allowedUsers = new ArrayList<>();
                if(isHelpingAgent || ((isDeliveryAgent && !isHelpingAgent) && (
                        orderStatus.equalsIgnoreCase("Dispatched") || (
                                orderStatus.equalsIgnoreCase("Order_Processed") &&
                                        (deliveryType.equalsIgnoreCase("Pick Up"))
                                )
                        ))) {
//                                markPOCodesarranged();
                                callGetPoCodesAPI();
                } else {
                    switch (orderStatus) {
                        case "To_Be_Approved_From_Account":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("10");
                            if (allowedUsers.contains(userRoleId)) {
                                showPaymentVerificationDialog();
                            }
                            break;
                        case "Pending_At_Dispatch_Manager":
                        case "Po_Sent":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("8");
                            if (allowedUsers.contains(userRoleId)) {
                                getAgentNameList();
                            }
                            break;
                        case "Order_Processed":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("8");
                            allowedUsers.add("9");
                            if (allowedUsers.contains(userRoleId)) {
                                if (deliveryType.equalsIgnoreCase("Home Delivery")) {
                                    showVehicleDetailsDialog();
                                } else if (deliveryType.equalsIgnoreCase("Pick Up")) {
                                    uploadStatusChangeProof("Mark Order Arranged", "Proof of Arrangement", "Reaching_Store");
                                }
                            }
                            break;
                        case "Dispatched":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("8");
                            allowedUsers.add("9");
                            if (allowedUsers.contains(userRoleId)) {
                                if (deliveryType.equalsIgnoreCase("Home Delivery")) {
                                    uploadStatusChangeProof("Mark Order Delivered", "Proof of Delivery", "Delivered");
                                } else if (deliveryType.equalsIgnoreCase("Pick Up")) {
                                    uploadStatusChangeProof("Mark Order Arranged", "Proof of Arrangement", "Reaching_Store");
                                }
                            }
                            break;
                        case "Loading":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("8");
                            allowedUsers.add("9");
                            if (allowedUsers.contains(userRoleId)) {
                                uploadStatusChangeProof("Mark Order Dispatched", "Proof of Loading", "Dispatched");
                            }
                            break;
                        case "Reaching_Store":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("8");
                            if (allowedUsers.contains(userRoleId)) {
                                updateOrderStatus("Ready_For_Pick_Up");
                            }
                            break;
                        case "Ready_For_Pick_Up":
                            allowedUsers.clear();
                            allowedUsers.add("1");
                            allowedUsers.add("11");
                            if (allowedUsers.contains(userRoleId)) {
                                uploadStatusChangeProof("Mark Order Picked Up", "Proof of Pick Up", "Picked_Up");
                            }
                            break;
                        default:
                            Common.showToast(OrderDetailActivity.this, "Cannot change the order status");
                    }
                }
//                if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Acc. Verify")) {
//                    showPaymentVerificationDialog();
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Agent assign")) {
//                    getAgentNameList();
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Start Loading")) {
//                    showVehicleDetailsDialog();
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Material Arranged")) {
//                    uploadStatusChangeProof("Mark Order Arranged", "Proof of Arrangement", "Reaching_Store");
////                    updateOrderStatus("Reaching_Store");
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Dispatched")) {
//                    uploadStatusChangeProof("Mark Order Dispatched", "Proof of Loading", "Dispatched");
////                    updateOrderStatus("Dispatched");
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Ready For Pick Up")) {
//                    uploadStatusChangeProof("Mark Order Ready For Pick Up", "Proof of Order Ready For Pick Up", "Ready_For_Pick_Up");
////                    updateOrderStatus("Ready_For_Pick_Up");
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Picked Up")) {
//                    uploadStatusChangeProof("Mark Order Picked Up", "Proof of Pick Up", "Picked_Up");
////                    updateOrderStatus("Picked_Up");
//                } else if (binding.updateProgressTV.getText().toString().equalsIgnoreCase("Complete")) {
//                    uploadStatusChangeProof("Mark Order Delivered", "Proof of Delivery", "Delivered");
////                    updateOrderStatus("Delivered");
//                }
            }
        });
        if (records.getStatus().equalsIgnoreCase("Cancelled")) {
            binding.shareIv.setVisibility(View.GONE);
            binding.editIv.setVisibility(View.GONE);
            binding.blockIv.setVisibility(View.GONE);
            binding.mailIv.setVisibility(View.GONE);
            binding.changeIcon.setVisibility(View.GONE);
            binding.changeDeliveryTypeIV.setEnabled(false);
            binding.updateProgressTV.setVisibility(View.GONE);
        }
        binding.createdDateTV.setText("Order Created On " + records.getCreatedAt());
        binding.num.setText("#" + records.getSaleOrderNo());
        binding.dateHalf.setText(records.getDeliveryDate() + ", " + records.getDeliveryTime());
        binding.deliveryTypeTV.setText(records.getDeliveryType());
        setupDeliveryStatus();
        if(getIntent().getStringExtra("type")!=null && getIntent().getStringExtra("type").equalsIgnoreCase("comment")){
            showOrderCommentDialog();
        }
        handleUpdateProgressTvVisibility();
    }

    private void markPOCodesarranged() {
//        TODO use mark mark po arrange dialog template for whole thing only add PO codes options and modified API
        arrangePOCodesDialog = new Dialog(this);
        arrangePOCodesDialog.setContentView(R.layout.upload_image_dialog);
        arrangePOCodesDialog.setCancelable(true);
//        SELECTEDFILES.clear();
        TextView dialogTitle = arrangePOCodesDialog.findViewById(R.id.dialog_title_tv);
        ImageView close = arrangePOCodesDialog.findViewById(R.id.image_dialog_crossIV);
        TextView fieldHeader = arrangePOCodesDialog.findViewById(R.id.field_titleTV);
        TextView uploadBtn = arrangePOCodesDialog.findViewById(R.id.dialog_upload_btn);
        TextView selectImg = arrangePOCodesDialog.findViewById(R.id.select_img);
        TextView formatTV = arrangePOCodesDialog.findViewById(R.id.formatTV);
        dialogRecyclerView = arrangePOCodesDialog.findViewById(R.id.upload_img_rv);
        dialogPOCodesRecyclerView = arrangePOCodesDialog.findViewById(R.id.po_codesRV);
        if(po_codes.size() > 0) {
            dialogPOCodesRecyclerView.setVisibility(View.VISIBLE);
            selectedPOCodes.addAll(po_codes);
            setupPOCodesAdapter();
        }
        else dialogPOCodesRecyclerView.setVisibility(View.GONE);
        String title = "Mark PO Code(s) Arranged";
        String fieldTitle = "Proof of Arrangement";
        String mediaType = "po_arranged";
        dialogTitle.setText(title);
        fieldHeader.setText(fieldTitle);
        formatTV.setText(R.string.upload_media_format);
        selectImg.setText(R.string.select_media_to_upload);
        setupAdapter();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrangePOCodesDialog.dismiss();
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
                Common.showLog("selectedPoCodes====" + selectedPOCodes);
                Common.showLog("selectedPoCodes Size ====" + selectedPOCodes.size());
                if(selectedPOCodes.size() > 0) {
                    Common.showLog("Inside if condition");
                    callMarkPOArrangeAPI();
                }
                else callUploadImageApi(mediaType);
            }
        });
        arrangePOCodesDialog.show();
    }

    private void callMarkPOArrangeAPI() {
        if(SELECTEDFILES.size()>0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("po_codes", convertSetToString(selectedPOCodes));
            map.put("order_id", getIntent().getStringExtra("orderId").toString());
            map.put("file", SELECTEDFILES);
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_POARRANGE, map, true);
        } else {
            Common.showToast(this, "Select atleast 1 proof to mark PO(s) as arranged");
        }
    }
    private String convertSetToString(HashSet<String> set) {
        StringJoiner joiner = new StringJoiner("\", \"", "[\"", "\"]");
        for (String s : set) {
            joiner.add(s);
        }
        return joiner.toString();
    }
    private void callGetPoCodesAPI() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("orderId").toString());
        map.put(NKeys.GETPOCODES, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETPOCODES, map, false);
    }

    private void setupPOCodesAdapter() {
        poCodesAdapter = new PoCodesAdapter(this, po_codes, selectedPOCodes, this);
        dialogPOCodesRecyclerView.setHasFixedSize(true);
        dialogPOCodesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        dialogPOCodesRecyclerView.setAdapter(poCodesAdapter);
    }

    private void uploadStatusChangeProof(String title, String fieldTitle,String mediaType) {
        imageUploadDialog = new Dialog(this);
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
        fieldHeader.setText(fieldTitle);
        formatTV.setText(R.string.upload_media_format);
        selectImg.setText(R.string.select_media_to_upload);
        setupAdapter();
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
                callUploadImageApi(mediaType);
            }
        });
        imageUploadDialog.show();
    }

    private void callUploadImageApi(String mediaType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("orderId").toString());
        map.put("for_status", mediaType);
        map.put("file", SELECTEDFILES);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPLOADMULTIPLEORDERFILE, map, true);
        updateOrderStatus(mediaType);
    }

    private void SelectImageLauncher() {
        ArrayList<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("image/*");
        mMimeTypesList.add("application/pdf");
        mMimeTypesList.add("video/mp4");
        captureImageResultLauncher.launch(
                new FilePicker.Builder(this)
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
    private void setupAdapter() {
        imageAdapter = new UploadFileAdapter(this, imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        dialogRecyclerView.setAdapter(imageAdapter);
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
    private void updateOrderStatus(String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id",getIntent().getStringExtra("orderId"));
        map.put("orderStatus",status);
        map.put(NKeys.CHANGEORDERSTATUS, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CHANGEORDERSTATUS, map, true);
    }

    private void getAgentNameList() {
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

    private void getDeliveryModes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_value", "deliveryMode");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        filterValue = "deliveryMode";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETORDERFILTERVALUE, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, true);
    }

    private void getPayer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_value", "payer");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        filterValue = "payer";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETORDERFILTERVALUE, jsonObject.toString());
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, true);

    }

    private void showAgentAssignDialog() {
        agentAssignDialog = new Dialog(this);
        agentAssignDialog.setContentView(R.layout.agent_assign_dialog);
        agentAssignDialog.setCancelable(true);
        AutoCompleteTextView agent_assign = agentAssignDialog.findViewById(R.id.agent_assign);
        AutoCompleteTextView delivery_mode = agentAssignDialog.findViewById(R.id.delivery_mode);
        AutoCompleteTextView shippingChargePayer = agentAssignDialog.findViewById(R.id.payer);
        ImageView close = agentAssignDialog.findViewById(R.id.close_assign_agent_dialog);
        ImageView add_more = agentAssignDialog.findViewById(R.id.add_more_helping_agentsIV);
        AppCompatEditText comment = agentAssignDialog.findViewById(R.id.instructionET);
        TextView agent_assign_btn = agentAssignDialog.findViewById(R.id.update_agentTV);
        RecyclerView helpingAgentsRV = agentAssignDialog.findViewById(R.id.helpingAgentsRV);
        cityAdapter = new CustomAutoCompleteListAdapter(agentAssignDialog.getContext(), R.layout.dropdown_item_list, R.id.title_tv, agentNameList, true);
        Common.showLog("agent==++==" + agentNameList.size());
        agent_assign.setAdapter(cityAdapter);
        deliveryModesAdapter = new CustomAutoCompleteListAdapter(agentAssignDialog.getContext(), R.layout.dropdown_item_list, R.id.title_tv, deliveryModes, true);
        Common.showLog("delivery modes==++=="+deliveryModes.size());
        delivery_mode.setAdapter(deliveryModesAdapter);
        payerAdapter = new CustomAutoCompleteListAdapter(agentAssignDialog.getContext(), R.layout.dropdown_item_list, R.id.title_tv, payer, true);
        Common.showLog("payer==++=="+payer.size());
        shippingChargePayer.setAdapter(payerAdapter);
        if(records.getDocuments().getPO().size()>0) {
            agentAssignDialog.findViewById(R.id.poOptionsRV).setVisibility(View.VISIBLE);
            agentAssignDialog.findViewById(R.id.add_more_helping_agentsIV).setVisibility(View.VISIBLE);
            mainPOSelectionAdapter = new POSelectionAdapter(this, this, helpingAgentsAdapter, -1, poDocs, selectedMainAgentPOList, true, 0, 0);
            RecyclerView poOptionsRV = agentAssignDialog.findViewById(R.id.poOptionsRV);
            poOptionsRV.setHasFixedSize(true);
            poOptionsRV.setLayoutManager(new LinearLayoutManager(this));
            poOptionsRV.setAdapter(mainPOSelectionAdapter);
            expectedDeliveryTimeList.add("First Half");
            expectedDeliveryTimeList.add("Second Half");
            helpingAgentsAdapter = new AssignMultipleHelpingAgentsAdapter(this, this, this, agentNameList, poDocs, selectedMainAgentPOList, allCheckedPOList, expectedDeliveryTimeList);
        }
        else {
            agentAssignDialog.findViewById(R.id.poOptionsRV).setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMainAgentPOList.clear();
                allCheckedPOList.clear();
                expectedDeliveryTimeList.clear();
                agentAssignDialog.dismiss();
            }
        });
        agent_assign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < agentNameList.size(); j++) {
                    agentNameList.get(j).setSelected(false);
                }
                agentNameList.get(i).setSelected(true);
                if(agentNameList.get(i).getStateName().equalsIgnoreCase("1")) {
                    agentAssignDialog.findViewById(R.id.select_payer_dropdown).setVisibility(View.VISIBLE);
                }
                else {
                    agentAssignDialog.findViewById(R.id.select_payer_dropdown).setVisibility(View.GONE);
                }
            }
        });
        delivery_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0; j<deliveryModes.size(); j++) {
                    deliveryModes.get(j).setSelected(false);
                }
                deliveryModes.get(i).setSelected(true);
            }
        });
        shippingChargePayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0; j<payer.size(); j++) {
                    payer.get(j).setSelected(false);
                }
                payer.get(i).setSelected(true);
            }
        });

        helpingAgentsRV.setHasFixedSize(true);
        helpingAgentsRV.setLayoutManager(new LinearLayoutManager(agentAssignDialog.getContext()));
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 HelpingAgentData data1 = new HelpingAgentData();
                 data1.setSr_no(helpingAgentsAdapter.helping_agent_count);
                 helpingAgentsAdapter.helpingAgentList.add(data1);
                 if(helpingAgentsAdapter.isFirstLoad)
                     helpingAgentsRV.setAdapter(helpingAgentsAdapter);
                 else
                     helpingAgentsAdapter.notifyDataSetChanged();
                helpingAgentsAdapter.helping_agent_count += 1;
                mainPOSelectionAdapter.helpingAgentCount +=1 ;
            }
        });
        agent_assign_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                if (agent_assign.getText().toString().isEmpty()) {
                    Common.showToast(agentAssignDialog.getContext(), "Please assign an agent.");
                    return;
                }
                if(delivery_mode.getText().toString().isEmpty()){
                    Common.showToast(agentAssignDialog.getContext(), "Please select delivery mode");
                    return;
                }
                String agent_id="";
                String selected_delivery_mode="";
                String ask_payment="";
                String selected_payer="";
                for (int j = 0; j < agentNameList.size(); j++) {
                    if (agentNameList.get(j).isSelected()) {
                        agent_id = agentNameList.get(j).getId();
                        ask_payment = String.valueOf(agentNameList.get(j).getStateName());
                        break;
                    }
                }
                for(int j=0; j<deliveryModes.size(); j++) {
                    if(deliveryModes.get(j).isSelected()) {
                        selected_delivery_mode = deliveryModes.get(j).getCityName();
                    }
                }
                if(ask_payment.equalsIgnoreCase("1") && shippingChargePayer.getText().toString().isEmpty()) {
                    Common.showToast(agentAssignDialog.getContext(), "Please select shipping charge payer");
                    return;
                }
                for(int j=0; j<payer.size(); j++) {
                    if(payer.get(j).isSelected()) {
                        selected_payer = payer.get(j).getId();
                    }
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("order_id", getIntent().getStringExtra("orderId"));
                HashMap<String, Object> main_agent_map = new HashMap<>();
                List<Map<String, Object>> helping_agent = new ArrayList<>();
                main_agent_map.put("agentId",agent_id);
                if(ask_payment.equalsIgnoreCase("1")) main_agent_map.put("payer", selected_payer);
                main_agent_map.put("deliveryMode", selected_delivery_mode);
                if(records.getDocuments().getPO().size()>0 && selectedMainAgentPOList.size()>0) {
                    main_agent_map.put("assignPOs", selectedMainAgentPOList);
                }
                main_agent_map.put("comment",comment.getText().toString());
                for(int i=0; i<helpingAgentsAdapter.helpingAgentList.size(); i++) {
                    HashMap<String, Object> helping_agent_map = new HashMap<>();
                    if(helpingAgentsAdapter.helpingAgentList.get(i).getAgentId() ==null || helpingAgentsAdapter.helpingAgentList.get(i).getAgentId().isEmpty()) {
                        Common.showToast(agentAssignDialog.getContext(), "Please select delivery agent for Helping agent "+(i+1));
                        return;
                    }
                    if(helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryDate() == null || helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryDate().isEmpty()) {
                        Common.showToast(agentAssignDialog.getContext(), "Please select expected delivery date for Helping agent "+(i+1));
                        return;
                    }
                    if(helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryTime() == null || helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryTime().isEmpty()) {
                        Common.showToast(agentAssignDialog.getContext(), "Please select expected delivery time for Helping agent " + (i + 1));
                        return;
                    }
                    if(helpingAgentsAdapter.helpingAgentList.get(i).getSelectedHelpingAgentPOList().size() == 0) {
                        Common.showToast(agentAssignDialog.getContext(), "Please select expected delivery time for Helping agent "+(i+1));
                        return;
                    }
                    helping_agent_map.put("agentId", Integer.parseInt(helpingAgentsAdapter.helpingAgentList.get(i).getAgentId()));
                    helping_agent_map.put("expDeliveryDate", helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryDate().toString());
                    if(helpingAgentsAdapter.helpingAgentList.get(i).getExpDeliveryTime().equalsIgnoreCase("First Half"))
                        helping_agent_map.put("expDeliveryTime", "first_half");
                    else
                        helping_agent_map.put("expDeliveryTime", "second_half");
                    if (helpingAgentsAdapter.helpingAgentList.get(i).getComment()!=null)
                    helping_agent_map.put("comment", helpingAgentsAdapter.helpingAgentList.get(i).getComment().toString());
                    helping_agent_map.put("assignPOs", helpingAgentsAdapter.helpingAgentList.get(i).getSelectedHelpingAgentPOList());
                    helping_agent.add(helping_agent_map);
                }
                map.put("mainAgent",main_agent_map);
                map.put("helpingAgents", helping_agent);
                map.put(NKeys.ASSIGNDELIVERYAGENT, new Gson().toJson(map));
                new NetworkRequest(agentAssignDialog.getContext(), OrderDetailActivity.this).callWebServices(ServiceMethods.WS_ASSIGNDELIVERYAGENT, map, false);
            }
        });
        agentAssignDialog.show();
    }
    public void checkPoRemaining() {
        if(poDocs.size() > uniqueCheckedPODocs.size()) {
            agentAssignDialog.findViewById(R.id.add_more_helping_agentsIV).setVisibility(View.VISIBLE);
        }
        else {
            agentAssignDialog.findViewById(R.id.add_more_helping_agentsIV).setVisibility(View.GONE);
        }
    }
    public void updateAllCheckedPOList(List<String> checkedPOList) {
        for(int i=0; i<checkedPOList.size(); i++) {
            allCheckedPOList.remove(checkedPOList.get(i));
        }
    }
    public void showPaymentVerificationDialog() {
        paymentVerifyDialog = new Dialog(this);
        paymentVerifyDialog.setContentView(R.layout.acc_verify_dialog);
        paymentVerifyDialog.setCancelable(false);

        RecyclerView paymentReceiptRv = paymentVerifyDialog.findViewById(R.id.paymentRecieptRv);
        ImageView add = paymentVerifyDialog.findViewById(R.id.add_more_payment_receiptIV);
        TextView update = paymentVerifyDialog.findViewById(R.id.update_TV);
        AppCompatEditText total = paymentVerifyDialog.findViewById(R.id.total_amount_tv);
        AppCompatEditText paid = paymentVerifyDialog.findViewById(R.id.amountPaidTV);
        AppCompatEditText due = paymentVerifyDialog.findViewById(R.id.amount_dueET);
        ImageView close = paymentVerifyDialog.findViewById(R.id.close_payment_update_dialog);

        total.setText(data.getTotalAmount().toString());
        paid.setText(data.getAmountPaid().toString());
        due.setText(data.getDueAmount().toString());

        paymentReceiptAdapter = new AddMultiplePaymentReceiptAdapter(this, new AddMultiplePaymentReceiptAdapter.RemoveReceipt() {
            @Override
            public void removeReceiptListener(int srNo) {
                // Handle the receipt removal event if needed
            }

            @Override
            public void inputDataListener(PaymentReceiptData receiptData, String type, AppCompatEditText amountPaidTV, AutoCompleteTextView paymentMode) {
                // Handle input data change if needed
            }
        }, payment_modes,payment_modes_value);

        paymentReceiptRv.setLayoutManager(new LinearLayoutManager(this));
        paymentReceiptRv.setAdapter(paymentReceiptAdapter);

        close.setOnClickListener(view -> paymentVerifyDialog.dismiss());

        paymentReceiptRv.setOnTouchListener((v, event) -> {
            hideKeyboard(paymentReceiptRv);
            clearFocus(paymentReceiptRv);
            return false;
        });

        update.setOnClickListener(view -> checkAllFields());

        add.setOnClickListener(view -> {
            paymentReceiptAdapter.updateReceiptCount("add");
            paymentReceiptAdapter.notifyItemInserted(paymentReceiptAdapter.getItemCount() - 1);
        });

        paymentVerifyDialog.show();
    }

    private void getPaymentModes(String mode) {
        HashMap<String, Object> map = new HashMap<>();
        filterValue = "paymentMode";
        map.put("filter_value", mode);
        map.put(NKeys.GETORDERFILTERVALUE, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, false);
    }

    private void checkAllFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("orderId"));
        map.put("paymentStatus", "accept");
        List<Map<String, Object>> paymentDetailsList = new ArrayList<>();

        for (int i = 0; i < paymentReceiptAdapter.paymentReceiptList.size(); i++) {
            PaymentReceiptData receiptData = paymentReceiptAdapter.paymentReceiptList.get(i);
            int j = i + 1;
            if (receiptData.getPayment_amount() == null) {
                Common.showToast(this, "Payment Receipt " + j + " has empty amount field.");
                return;
            }
            if (receiptData.getPayment_mode() == null) {
                Common.showToast(this, "Payment Receipt " + j + " has empty payment mode field.");
                return;
            }

            HashMap<String, Object> paymentDetails = new HashMap<>();
            paymentDetails.put("paymentAmount", receiptData.getPayment_amount().toString());
            paymentDetails.put("paymentMode", receiptData.getPayment_mode().toString());
            if (receiptData.getPayment_ref() != null)
                paymentDetails.put("paymentRef", receiptData.getPayment_ref().toString());
            paymentDetailsList.add(paymentDetails);
        }

        map.put("paymentDetails", paymentDetailsList);
        map.put(NKeys.UPDATEPAYMENT, new Gson().toJson(map));
        Common.showLog(new Gson().toJson(map) + "  data====++++");
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEPAYMENT, map, true);
    }

    // Method to hide keyboard
    private void hideKeyboard(RecyclerView recyclerView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
        }
    }

    // Method to clear focus
    private void clearFocus(RecyclerView recyclerView) {
        recyclerView.clearFocus();
    }

    private void setupDeliveryStatus() {
        binding.accTV.setText("Acc.\nVerified");
        binding.poTV.setText("PO\nReceived");
        binding.processTV.setText("Processed");
        if (records.getDeliveryType().equalsIgnoreCase("Home Delivery")) {
            binding.dispatchTV.setMaxLines(1);
            binding.dispatchTV.setEllipsize(TextUtils.TruncateAt.END);
            binding.loadingTV.setMaxLines(1);
            binding.loadingTV.setEllipsize(TextUtils.TruncateAt.END);
            binding.loadingTV.setText("Loading");
            binding.dispatchTV.setText("Dispatched");
            binding.deliveredTV.setText("Delivered");
        } else {
            binding.loadingTV.setText("Material\nArranged");
            binding.dispatchTV.setText("Ready for\nPick-Up");
            binding.deliveredTV.setText("Picked\nUp");
        }
    }

    @Override
    public void removeReceiptListener(int srNo) {
        paymentReceiptAdapter.receipt_count -= 1;
        paymentReceiptAdapter.paymentReceiptList.remove(srNo - 1);
        for (int i = srNo - 1; i < paymentReceiptAdapter.receipt_count; i++) {
            paymentReceiptAdapter.paymentReceiptList.get(i).setSr_no(i + 1);
        }
        paymentReceiptAdapter.notifyDataSetChanged();
    }

    @Override
    public void inputDataListener(PaymentReceiptData receiptData, String type, AppCompatEditText editText, AutoCompleteTextView paymentMode) {
        if (type.equalsIgnoreCase("paid")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    receiptData.setPayment_amount(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else if (type.equalsIgnoreCase("ref")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    receiptData.setPayment_ref(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else {
            paymentMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    receiptData.setPayment_mode(payment_modes_value.get(i).toString());
                }
            });
        }
    }

    @Override
    public void removeAgentListener(int srNo) {
        mainPOSelectionAdapter.helpingAgentCount -=1 ;
        helpingAgentsAdapter.helping_agent_count -= 1;
        helpingAgentsAdapter.helpingAgentList.remove(srNo);
        for(int i = srNo; i<helpingAgentsAdapter.helping_agent_count; i++) {
            helpingAgentsAdapter.helpingAgentList.get(i).setSr_no(i);
        }
        helpingAgentsAdapter.notifyDataSetChanged();
    }

    public List<String> getAllCheckedPOList() {
        return allCheckedPOList;
    }
    public void updateAllCheckedPOList(String poId, String action) {
        if(action.equalsIgnoreCase("add")) {
            allCheckedPOList.add(poId);
            uniqueCheckedPODocs.add(poId);
        }
        else if(action.equalsIgnoreCase("remove")) {
            allCheckedPOList.remove(poId);
            if(uniqueCheckedPODocs.contains(poId)) uniqueCheckedPODocs.remove(poId);
        }
    }
    @Override
    public void inputHelpingAgentDataListener(HelpingAgentData helpingAgentData, String type, AppCompatEditText appCompatEditText, AutoCompleteTextView autoCompleteTextView, int position, String agentId) {
        if(type.equalsIgnoreCase("expectedDate")) {
            Common.openCalenderForUpcomingDates(this, appCompatEditText);
            appCompatEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    helpingAgentData.setExpDeliveryDate(appCompatEditText.getText().toString());
                    helpingAgentsAdapter.helpingAgentList.get(position).setExpDeliveryDate(appCompatEditText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
        else if(type.equalsIgnoreCase("comment")) {
            appCompatEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    helpingAgentData.setComment(appCompatEditText.getText().toString());
                    helpingAgentsAdapter.helpingAgentList.get(position).setComment(appCompatEditText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
        else if(type.equalsIgnoreCase("agent")) {
//            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                    helpingAgentData.setAgentId(agentNameList.get(i).getId().toString());
//                    helpingAgentsAdapter.helpingAgentList.get(position).setAgentId(agentNameList.get(i).getId().toString());
//                }
//            });
            helpingAgentsAdapter.helpingAgentList.get(position).setAgentId(agentId);
        }
        else if(type.equalsIgnoreCase("expectedTime")) {
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    helpingAgentData.setExpDeliveryTime(expectedDeliveryTimeList.get(i).toString());
                    helpingAgentsAdapter.helpingAgentList.get(position).setExpDeliveryTime(expectedDeliveryTimeList.get(i).toString());
                }
            });
        }
    }

    @Override
    public void onPoCodeClickListener(String poCode) {
        if(selectedPOCodes.contains(poCode)) selectedPOCodes.remove(poCode);
        else selectedPOCodes.add(poCode);
    }
}