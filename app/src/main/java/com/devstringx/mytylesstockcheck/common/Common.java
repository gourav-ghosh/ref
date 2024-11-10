package com.devstringx.mytylesstockcheck.common;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.databinding.AllStatusDialogBinding;
import com.devstringx.mytylesstockcheck.databinding.ChangeTaskStatusDialogBinding;
import com.devstringx.mytylesstockcheck.datamodal.salesperson.OrderSalesPerson;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsRecordsItem;
import com.devstringx.mytylesstockcheck.filter.FilterActivity;
import com.devstringx.mytylesstockcheck.interfaces.UpdateLeadStage;
import com.devstringx.mytylesstockcheck.interfaces.UpdateUserStatus;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.analytics.DataAnalyticFilterActivity;
import com.devstringx.mytylesstockcheck.screens.allWorklogFilter.AllWorklogFilterActivity;
import com.devstringx.mytylesstockcheck.screens.architectFilter.ArchitectFilterActivity;
import com.devstringx.mytylesstockcheck.screens.complaintFIlter.ComplaintFilterActivity;
import com.devstringx.mytylesstockcheck.screens.orderFilters.OrderFilterActivity;
import com.devstringx.mytylesstockcheck.screens.razorpayFilter.RazorPayFilterActivity;
import com.devstringx.mytylesstockcheck.screens.shippingChargeFilter.ShippingChargeFilterActivity;
import com.devstringx.mytylesstockcheck.screens.transactionFilters.TransactionHistoryFilter;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.userWorklogFilter.UserWorklogFilterActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.FilterUsersActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.InquiryFilterActivity;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.emitter.Emitter;

public class Common {
    public static long TIMER_TIME = 60000;
    public static long TIMER_TIME30 = 30000;
    public static long TIMER_TIME08 = 8000;
    public static float TRANSPOTATIONDISC = 15.254F;
    public static float TRANSPOTATIONGST = 18F;

    public static float UNLOADINGDISC = 15.254F;
    public static float UNLOADINGGST = 18F;
    public static CountDownTimer countDownTimer;
    public static String orderFilterSortby = "createdAtDesc";
    public static boolean shippingChargeMasterIV = false;
    public static String OrderSelectedTabName = "";
    public static String OutstationSelectedSubtab = "";
    public static List<OrderSalesPerson> orderSalesPersonList = new ArrayList<>();
    public static ArrayList<OrderSalesPerson> orderSalesPersonResponseList = new ArrayList<>();
    private static boolean showlog = true;
    private static String TAG = "myTyles==";
    public static List<String> filterOwnerList = new ArrayList<>();
    public static List<String> filterLeadStage = new ArrayList<>();
    public static List<String> filterLeadSource = new ArrayList<>();
    public static List<String> filterLeadActivity = new ArrayList<>();
    public static boolean filterStarMarked = false;
    public static boolean filterNoLeadTask = false;
    public static String dateType = "";
    public static String startDate = "";
    public static String endDate = "";
    public static String analyticDateType = "";
    public static String analyticStartDate = "";
    public static String analyticEndDate = "";
    public static String selectedRole = "";
    public static String userRoleId = "";
    public static List<String> selectedRole_Original = new ArrayList<>();

    public static String complaintDateType = "";
    public static String complaintStartDate = "";
    public static String complaintEndDate = "";
    public static String complaintSortBy = "createdAtDesc";
    public static String analyticSortBy = "nameDesc";
    public static List<String> razorpayStatus = new ArrayList<>();
    public static String sortByRazorpay = "createdAtDesc";
    public static long phoneNumber = 0;
    public static List<String> selectedSalesExeName = new ArrayList<>();
    public static List<String> selectedComplaintSalesExeName = new ArrayList<>();
    public static List<String> selectedComplaintSalesExeId = new ArrayList<>();
    public static List<String> selectedComplaintDisName = new ArrayList<>();
    public static List<String> selectedComplaintSupExeName = new ArrayList<>();
    public static List<String> selectedComplaintType = new ArrayList<>();
    public static List<String> selectedIssueFrom = new ArrayList<>();
    public static List<String> selectedOrderDisManagerName = new ArrayList<>();
    public static List<String> selectedOrderSupExeName = new ArrayList<>();
    public static List<String> selectedOrderStatus = new ArrayList<>();
    public static List<String> selectedDeliveryType = new ArrayList<>();
    public static List<String> selectedOrderType = new ArrayList<>();
    public static List<String> selectedOrderPaymentMode = new ArrayList<>();
    public static List<String> selectedPOCodes = new ArrayList<>();
    public static List<String> selectedOrderDeliveryAgentName = new ArrayList<>();
    public static String tempOrderDateType = "";
    public static List<String> tempSelectedOrderDisManagerName = new ArrayList<>();
    public static List<String> tempSelectedOrderSupExeName = new ArrayList<>();
    public static List<String> tempSelectedOrderStatus = new ArrayList<>();
    public static List<String> tempSelectedDeliveryType = new ArrayList<>();
    public static List<String> tempSelectedOrderType = new ArrayList<>();
    public static List<String> tempSelectedOrderPaymentMode = new ArrayList<>();
    public static List<String> tempSelectedPOCodes = new ArrayList<>();
    public static List<String> tempSelectedOrderDeliveryAgentName = new ArrayList<>();
    public static String tempOrderSortby = "createdAtDesc";
    public static String orderSearch = "";
    public static List<String> transactionAccountant = new ArrayList<>();
    public static List<String> transactionSalesExe = new ArrayList<>();
    public static List<String> tempTransactionAccountant = new ArrayList<>();
    public static List<String> tempTransactionSalesExe = new ArrayList<>();
    public static List<String> transactionPaymentMode = new ArrayList<>();
    public static List<String> transactionPaymentModeOri = new ArrayList<>();
    public static String sortBy = "";

    public static String dateTypeRazor = "";
    public static String startDateRazor = "";
    public static String endDateRazor = "";
    public static String inquiryDateType = "";
    public static String inquiryStartDate = "";
    public static String inquiryEndDate = "";
    public static String inquiryFilterQuantity = "";
    public static String filterInquiryType = "";
    public static String inquiryFilterAction = "";
    public static String inquiryFilterActionAvailable = "";
    public static String inquiryFilterActionUnAvailable = "";
    public static String userStatusFilter = "";
    public static String orderDateType = "";
    public static String orderStartDate = "";
    public static String orderEndDate = "";
    public static String transactionDateType = "";
    public static String transactionStartDate = "";
    public static String transactionEndDate = "";
    public static List<String> filterOwnerListTemp = new ArrayList<>();
    public static List<String> filterLeadStageTemp = new ArrayList<>();
    public static List<String> filterLeadSourceTemp = new ArrayList<>();
    public static List<String> filterLeadActivityTemp = new ArrayList<>();
    public static boolean filterStarMarkedTemp = false;
    public static boolean filterNoLeadTaskTemp = false;
    public static String dateTypeTemp = "";
    public static String startDateTemp = "";
    public static String endDateTemp = "";
    private static Handler mHandler;
    public static String shippingDateType = "";
    public static String shippingStartDate = "";
    public static String shippingEndDate = "";
    public static String shippingFilterSortBy = "createdAtDesc";
    public static List<String> selectedShippingDeliveryAgentName = new ArrayList<>();
    public static List<String> selectedShippingStatustName = new ArrayList<>();
    public static String architectDateType = "";
    public static String architectStartDate = "";
    public static String architectEndDate = "";
    public static String architectSortBy = "createdAtDesc";
    public static String allWorklogStartDate="";
    public static String allWorklogDateType="";
    public static String allWorklogEndDate="";
    public static String allWorklogSortBy="createdAtDesc";
    public static String userWorklogStartDate="";
    public static String userWorklogDateType="";
    public static String userWorklogEndDate="";
    public static int userWorklogUserId;
    public static String userWorklogSortBy="createdAtDesc";
    public static List<String> architectSelectedSaleperson = new ArrayList<>();
    public static List<String> architectSelectedSalepersonId = new ArrayList<>();
    public static List<VendorQuotationsRecordsItem> vendorQuotationsRecordsItemList = new ArrayList<>();
    public static int eligibleForAdminRights;
    public static int askPayment;
    public static int eligibleForOvertime;
    public static int eligibleForWorklog;
    public static int eligibleForCMS;
    public static int eligibleForCRM;
    public static String userProfileRole;
    public static int userProfilEligibleForCRM;

    public static void intent(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent, null);
    }

    public static void clearFilter() {
        shippingChargeMasterIV = false;
        OrderSelectedTabName = "";
        OutstationSelectedSubtab = "";
        filterOwnerList = new ArrayList<>();
        filterLeadStage = new ArrayList<>();
        filterLeadSource = new ArrayList<>();
        filterLeadActivity = new ArrayList<>();
        filterStarMarked = false;
        filterNoLeadTask = false;
        dateType = "";
        startDate = "";
        endDate = "";
        analyticDateType = "";
        analyticStartDate = "";
        analyticEndDate = "";
        selectedRole = "sales_person";
        complaintDateType = "";
        complaintStartDate = "";
        complaintEndDate = "";
        complaintSortBy = "createdAtDesc";
        analyticSortBy = "nameDesc";
        razorpayStatus = new ArrayList<>();
        sortByRazorpay = "createdAtDesc";
        phoneNumber = 0;
        selectedSalesExeName = new ArrayList<>();
        selectedComplaintSalesExeName = new ArrayList<>();
        selectedComplaintSalesExeId = new ArrayList<>();
        selectedComplaintDisName = new ArrayList<>();
        selectedComplaintSupExeName = new ArrayList<>();
        selectedComplaintType = new ArrayList<>();
        selectedIssueFrom = new ArrayList<>();
        selectedOrderDisManagerName = new ArrayList<>();
        selectedOrderSupExeName = new ArrayList<>();
        selectedOrderStatus = new ArrayList<>();
        selectedDeliveryType = new ArrayList<>();
        selectedOrderType = new ArrayList<>();
        selectedOrderPaymentMode = new ArrayList<>();
        selectedPOCodes = new ArrayList<>();
        selectedOrderDeliveryAgentName = new ArrayList<>();
        tempOrderDateType = "";
        tempSelectedOrderDisManagerName = new ArrayList<>();
        tempSelectedOrderSupExeName = new ArrayList<>();
        tempSelectedOrderStatus = new ArrayList<>();
        tempSelectedDeliveryType = new ArrayList<>();
        tempSelectedOrderType = new ArrayList<>();
        tempSelectedOrderPaymentMode = new ArrayList<>();
        tempSelectedPOCodes = new ArrayList<>();
        tempSelectedOrderDeliveryAgentName = new ArrayList<>();
        tempOrderSortby = "createdAtDesc";
        orderSearch = "";
        transactionAccountant = new ArrayList<>();
        transactionSalesExe = new ArrayList<>();
        transactionPaymentMode = new ArrayList<>();
        sortBy = "";

        dateTypeRazor = "";
        startDateRazor = "";
        endDateRazor = "";
        inquiryDateType = "";
        inquiryStartDate = "";
        inquiryEndDate = "";
        inquiryFilterQuantity = "";
        filterInquiryType = "";
        inquiryFilterAction = "";
        userStatusFilter = "";
        orderDateType = "";
        orderStartDate = "";
        orderEndDate = "";
        transactionDateType = "";
        transactionStartDate = "";
        transactionEndDate = "";
        filterOwnerListTemp = new ArrayList<>();
        filterLeadStageTemp = new ArrayList<>();
        filterLeadSourceTemp = new ArrayList<>();
        filterLeadActivityTemp = new ArrayList<>();
        filterStarMarkedTemp = false;
        filterNoLeadTaskTemp = false;
        dateTypeTemp = "";
        startDateTemp = "";
        endDateTemp = "";
        shippingDateType = "";
        shippingStartDate = "";
        shippingEndDate = "";
        shippingFilterSortBy = "";
        selectedShippingDeliveryAgentName = new ArrayList<>();
        selectedShippingStatustName = new ArrayList<>();
        architectDateType = "";
        architectStartDate = "";
        architectEndDate = "";
        architectSortBy = "createdAtDesc";
        allWorklogStartDate = "";
        allWorklogDateType = "";
        allWorklogEndDate = "";
        allWorklogSortBy = "createdAtDesc";
        architectSelectedSaleperson = new ArrayList<>();
        architectSelectedSalepersonId = new ArrayList<>();

    }

    public static void isFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((FilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isComplaintFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((ComplaintFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isAnalyticFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((DataAnalyticFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isArchitectFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((ArchitectFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isAllWorklogFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((AllWorklogFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isUserWorklogFilterSelected(Activity getActivity){
        if (getActivity != null) {
            ((UserWorklogFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isTransactionFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((TransactionHistoryFilter) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isOrderListingFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((OrderFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isRazorPayFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((RazorPayFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isUserFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((FilterUsersActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isInquiryFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((InquiryFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void isShippingChargeFilterSelected(Activity getActivity) {
        if (getActivity != null) {
            ((ShippingChargeFilterActivity) getActivity).checkIsAnyFIlterSelected();
        }
    }

    public static void showLog(String msg) {
        if (showlog)
            Log.e(TAG, msg);
    }

    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void startTimer(TextView tv, TextView resend) {
        countDownTimer = new CountDownTimer(TIMER_TIME, 1000) {
            @Override
            public void onTick(long l) {
                NumberFormat f = new DecimalFormat("00");

                long min = (l / 60000) % 60;

                long sec = (l / 1000) % 60;

                tv.setText(f.format(min) + ":" + f.format(sec));

            }

            @Override
            public void onFinish() {
                resend.setEnabled(true);
            }
        };
        countDownTimer.start();

    }

    public static String convertDateTimeFormat(String inputDate) {
        try {
            // Parse the input date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);

            // Format the output date
            SimpleDateFormat outputFormat = new SimpleDateFormat("d' 'MMM yyyy h:mm a", Locale.getDefault());
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception according to your requirements
            return null;
        }
    }

    public static void start08SecTimer(TextView tv, LinearLayout timerLl, TimerCallback callback) {
        if (countDownTimer != null) countDownTimer.cancel();
        TIMER_TIME30 = 8000;
        countDownTimer = new CountDownTimer(TIMER_TIME30, 1000) {
            @Override
            public void onTick(long l) {
                long sec = (l / 1000) % 60;
                tv.setText(String.valueOf(sec) + " Sec");
            }

            @Override
            public void onFinish() {
                TIMER_TIME30 = 0;
                timerLl.setVisibility(View.GONE);
                if (callback != null) {
                    callback.onTimerFinish();
                }
            }
        };
        countDownTimer.start();
    }

    public static void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public static void clearInquiryFilterData() {
        inquiryDateType = "";
        inquiryStartDate = "";
        inquiryEndDate = "";
        inquiryFilterAction = "";
        inquiryFilterQuantity = "";
        filterInquiryType = "";
        Common.showLog("==" + Common.inquiryFilterAction + "    " + Common.inquiryDateType + "    " + Common.inquiryStartDate + "   " + Common.inquiryEndDate + "    " + Common.inquiryFilterQuantity + "       " + Common.filterInquiryType);
    }

    public interface TimerCallback {
        void onTimerFinish();
    }

    @SuppressLint("InvalidAnalyticsName")
    public static void trackEvent(Context mContext, String key, String val) {
        Bundle bundle = new Bundle();
        bundle.putString(key, val);
        FirebaseAnalytics.getInstance(mContext).logEvent("Stock_Check", bundle);
    }

    public static boolean pwdVsalid(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!containsUpperCase(password)) {
            return false;
        }
        if (!containsLowerCase(password)) {
            return false;
        }
        if (!containsSpecialCharacter(password)) {
            return false;
        }
        if (!containsNumericValue(password)) {
            return false;
        }
        if (!containsAlphabet(password)) {
            return false;
        }
        if (password.trim().length() != password.length()) {
            return false;
        }
        return true;
    }

    private static boolean containsUpperCase(String password) {
        return !password.equals(password.toLowerCase());
    }

    private static boolean containsLowerCase(String password) {
        return !password.equals(password.toUpperCase());
    }

    private static boolean containsSpecialCharacter(String password) {
        Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = specialCharacterPattern.matcher(password);
        return matcher.find();
    }

    private static boolean containsNumericValue(String password) {
        return password.matches(".*\\d.*");
    }

    private static boolean containsAlphabet(String password) {
        return password.matches(".*[a-zA-Z].*");
    }

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        fragmentManager
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    public static void addtoBackStackFragment(FragmentManager fragmentManager, Fragment fragment, int containerId,String backstackstring) {
        fragmentManager
                .beginTransaction()
                .addToBackStack(backstackstring)
                .add(containerId,fragment)
                .commit();
    }

    public static void showMultiSelection(AddLeadActivity context, AutoCompleteTextView requirement, List<String> item) {
        AutoCompleteAdapter adapter;
        adapter = new AutoCompleteAdapter(context, R.layout.dropdown_item_list, R.id.title_tv, item);
        requirement.setAdapter(adapter);
        requirement.setThreshold(1);
        requirement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Common.showToast(context, item);
            }
        });
    }

    public static void showlogoutDialog(Context context) {
        Dialog dialog = new Dialog(context);
        ChangeTaskStatusDialogBinding changeTaskStatusDialogBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.change_task_status_dialog, null, false);
        dialog.setContentView(changeTaskStatusDialogBinding.getRoot());
        changeTaskStatusDialogBinding.headerTv.setText("Log Out");
        changeTaskStatusDialogBinding.dialogTitleTv.setText("Are you sure you want to logout.");
        dialog.setCancelable(false);
        changeTaskStatusDialogBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        changeTaskStatusDialogBinding.yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                logout(context, dialog, new Gson().toJson(map));

            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();

    }

    public static void showStatusDialog(Context context, String id, String title, String desc, String statusName, UpdateUserStatus status) {
        Dialog dialog = new Dialog(context);
        AllStatusDialogBinding allStatusDialogBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.all_status_dialog, null, false);
        dialog.setContentView(allStatusDialogBinding.getRoot());
        allStatusDialogBinding.headerTv.setText(title);
        allStatusDialogBinding.dialogTitleTv.setText(desc);
        dialog.setCancelable(false);
        allStatusDialogBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        allStatusDialogBinding.yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (status != null) status.updateUserStatusListner(id, statusName, "");

            }
        });
        allStatusDialogBinding.crossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();

    }

    public static void applyFontToMenuItem(Context context, MenuItem mi) {
        Typeface font = ResourcesCompat.getFont(context, R.font.outfit_medium);//Typeface.createFromAsset(getActivity().getResources().getAssets(), "fonts/outfit_medium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mNewTitle.setSpan(new TypefaceSpan(font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {

        }
        mi.setTitle(mNewTitle);
    }

    public static void logout(Context context, Dialog dialog, String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.DATA, data);
        new NetworkRequest(context, new ResponseListner() {
            @Override
            public void onResponseReceived(ResponseDO responseDO) {
                if (dialog != null)
                    dialog.dismiss();
                new PreferenceUtils(context).clearAll();
                clearFilter();
                ((Activity) context).finishAffinity();
                Common.clearFilter();
                context.startActivity(new Intent(context, LoginWithPasswordActivity.class));
            }
        }).callWebServices(ServiceMethods.WS_LOGOUT, map, false);
    }

    public static void updateLeadStageDialog(Context context, String leadId, String leadType, List<String> leadStageList, UpdateLeadStage updateLeadStage, BottomSheetDialog bottomSheetDialog) {
        AutoCompleteAdapter adapter;
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_lead_stage_popup);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        AutoCompleteTextView autoCompleteTxt = dialog.findViewById(R.id.auto_complete_txt);
        EditText commentET = dialog.findViewById(R.id.commentET);
        adapter = new AutoCompleteAdapter(context, R.layout.dropdown_item_list, R.id.title_tv, leadStageList);
        autoCompleteTxt.setAdapter(adapter);
        autoCompleteTxt.setThreshold(1);
        autoCompleteTxt.setText(getSelectedItem(leadType, leadStageList));
        autoCompleteTxt.setTextColor(Color.BLACK);
        Button update = dialog.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoCompleteTxt.getText().toString().isEmpty()) return;
                dialog.dismiss();
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
                updateLeadStage.upldateLeadStageListner(leadId, autoCompleteTxt.getText().toString(), commentET.getText().toString().trim());
            }
        });
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static void updateLeadStageDialogAllLeads(Context context, String leadId, String leadType, List<String> leadStageList, UpdateLeadStage updateLeadStage, BottomSheetDialog bottomSheetDialog,List<String> leadStageLostList) {
        CheckBox checkBoxPriceIssue,checkBoxDesignIssue,checkBoxStockIssue,checkBoxDeliveryTimeIssue,
                checkBoxBrandIssue,checkBoxPaymenetPolicyIssue,checkBoxNeededFreeDelivery,checkBoxDamagePolicyIssue,
                checkBoxDistanceIssue,checkBoxWebsiteRegNotInterested,checkBoxNotResponding,checkBoxCurrentlyNoReq;
        LinearLayout priceIssueLinearLayout,designIssueLinearLayout,stockIssueLinearLayout,deliveryTimeLinearLayout,
                brandIssueLinearLayout,paymentPolicyLinearLayout,neededFreeDeliveryLinearLayout,damamgePolicyIssueLinearLayout,
                distanceIssueLinearLayout,websiteRegNotInterestedLinearLayout,notResponidngLinearLayout,currentlyNoReqLinearLayout;
        AutoCompleteAdapter adapter;
        List<String> stringList = new ArrayList<>();
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_lead_stage_popup);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
        dialog.show();
        checkBoxPriceIssue = dialog.findViewById(R.id.checkbox_price_issue);
        checkBoxDesignIssue = dialog.findViewById(R.id.checkbox_design_issue);
        checkBoxStockIssue = dialog.findViewById(R.id.checkbox_stock_issue);
        checkBoxDeliveryTimeIssue = dialog.findViewById(R.id.checkbox_delivery_time_issue);
        checkBoxBrandIssue = dialog.findViewById(R.id.checkbox_brand_issue);
        checkBoxPaymenetPolicyIssue = dialog.findViewById(R.id.checkbox_payment_policy_issue);
        checkBoxNeededFreeDelivery = dialog.findViewById(R.id.checkbox_needed_free_delivery);
        checkBoxDamagePolicyIssue = dialog.findViewById(R.id.checkbox_damage_policy_issue);
        checkBoxDistanceIssue = dialog.findViewById(R.id.checkbox_distance_issue);
        checkBoxWebsiteRegNotInterested = dialog.findViewById(R.id.checkbox_website_reg_not_interested);
        checkBoxNotResponding = dialog.findViewById(R.id.checkbox_not_responding);
        checkBoxCurrentlyNoReq = dialog.findViewById(R.id.checkbox_currently_no_req);
        priceIssueLinearLayout = dialog.findViewById(R.id.price_issue_linear_layout);
        designIssueLinearLayout = dialog.findViewById(R.id.design_issue_linear_layout);
        stockIssueLinearLayout = dialog.findViewById(R.id.stock_issue_linear_layout);
        deliveryTimeLinearLayout = dialog.findViewById(R.id.delivery_time_issue_linear_layout);
        brandIssueLinearLayout = dialog.findViewById(R.id.brand_issue_linear_layout);
        paymentPolicyLinearLayout = dialog.findViewById(R.id.payment_policy_issue_linear_layout);
        neededFreeDeliveryLinearLayout = dialog.findViewById(R.id.needed_free_delivery_linear_layout);
        damamgePolicyIssueLinearLayout = dialog.findViewById(R.id.damage_policy_issue_linear_layout);
        distanceIssueLinearLayout = dialog.findViewById(R.id.distance_issue_linear_layout);
        websiteRegNotInterestedLinearLayout = dialog.findViewById(R.id.website_reg_not_interested_linear_layout);
        notResponidngLinearLayout = dialog.findViewById(R.id.not_responding_linear_layout);
        currentlyNoReqLinearLayout = dialog.findViewById(R.id.currently_no_req_linear_layout);
        LinearLayout  lostStageLinearLayout = dialog.findViewById(R.id.lost_stage_linear_layout);
        TextInputLayout reasonTextInputLayout = dialog.findViewById(R.id.reason_text_input_layout);
        AutoCompleteTextView autoCompleteTxt = dialog.findViewById(R.id.auto_complete_txt);
        EditText commentET = dialog.findViewById(R.id.commentET);
        adapter = new AutoCompleteAdapter(context, R.layout.dropdown_item_list, R.id.title_tv, leadStageList);
        autoCompleteTxt.setAdapter(adapter);
        autoCompleteTxt.setThreshold(1);
        autoCompleteTxt.setText(getSelectedItem(leadType, leadStageList));
        autoCompleteTxt.setTextColor(Color.BLACK);
        Button update = dialog.findViewById(R.id.update);
        if(autoCompleteTxt.getText().toString().equalsIgnoreCase("lost")) lostStageLinearLayout.setVisibility(View.VISIBLE);
        checkBoxPriceIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Price Issue");
                if(!isChecked) stringList.remove("Price Issue");
            }
        });
        checkBoxDesignIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Design Issue");
                if(!isChecked) stringList.remove("Design Issue");
            }
        });
        checkBoxStockIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Stock Issue");
                if(!isChecked) stringList.remove("Stock Issue");
            }
        });
        checkBoxDeliveryTimeIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Delivery Time Issue");
                if(!isChecked) stringList.remove("Delivery Time Issue");
            }
        });
        checkBoxBrandIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Brand Issue");
                if(!isChecked) stringList.remove("Brand Issue");
            }
        });
        checkBoxPaymenetPolicyIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Payment Policy Issue");
                if(!isChecked) stringList.remove("Payment Policy Issue");
            }
        });
        checkBoxNeededFreeDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Needed Free Delivery");
                if(!isChecked) stringList.remove("Needed Free Delivery");
            }
        });
        checkBoxDamagePolicyIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Damage Policy Issue");
                if(!isChecked) stringList.remove("Damage Policy Issue");
            }
        });
        checkBoxDistanceIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Distance Issue");
                if(!isChecked) stringList.remove("Distance Issue");
            }
        });
        checkBoxWebsiteRegNotInterested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Website Reg Not Interested");
                if(!isChecked) stringList.remove("Website Reg Not Interested");
            }
        });

        checkBoxNotResponding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Not Reseponding");
                if(!isChecked) stringList.remove("Not Reseponding");
            }
        });

        checkBoxCurrentlyNoReq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) stringList.add("Currently No Req");
                if(!isChecked) stringList.remove("Currently No Req");
            }
        });

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(autoCompleteTxt.getText().toString().equalsIgnoreCase("lost")) {
                    lostStageLinearLayout.setVisibility(View.VISIBLE);
                    reasonTextInputLayout.setHint("Comments");

                    if(leadStageLostList.size() == 0) {
                        lostStageLinearLayout.setVisibility(View.GONE);
                    } else {
                        if (leadStageLostList.contains("Price Issue"))
                            priceIssueLinearLayout.setVisibility(View.VISIBLE);
                        else priceIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Stock Issue"))
                            stockIssueLinearLayout.setVisibility(View.VISIBLE);
                        else stockIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Design Issue"))
                            designIssueLinearLayout.setVisibility(View.VISIBLE);
                        else designIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Delivery Time Issue"))
                            deliveryTimeLinearLayout.setVisibility(View.VISIBLE);
                        else deliveryTimeLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Brand Issue"))
                            brandIssueLinearLayout.setVisibility(View.VISIBLE);
                        else brandIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Payment Policy Issue"))
                            paymentPolicyLinearLayout.setVisibility(View.VISIBLE);
                        else paymentPolicyLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Needed Free Delivery"))
                            neededFreeDeliveryLinearLayout.setVisibility(View.VISIBLE);
                        else neededFreeDeliveryLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Damage Policy Issue"))
                            damamgePolicyIssueLinearLayout.setVisibility(View.VISIBLE);
                        else damamgePolicyIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Distance Issue"))
                            distanceIssueLinearLayout.setVisibility(View.VISIBLE);
                        else distanceIssueLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Website Reg Not Interested"))
                            websiteRegNotInterestedLinearLayout.setVisibility(View.VISIBLE);
                        else websiteRegNotInterestedLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Not Responding"))
                            notResponidngLinearLayout.setVisibility(View.VISIBLE);
                        else notResponidngLinearLayout.setVisibility(View.GONE);
                        if (leadStageLostList.contains("Currently No Req."))
                            currentlyNoReqLinearLayout.setVisibility(View.VISIBLE);
                        else currentlyNoReqLinearLayout.setVisibility(View.GONE);
                    }

                }else {
                    lostStageLinearLayout.setVisibility(View.GONE);
                    reasonTextInputLayout.setHint("Reason");
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoCompleteTxt.getText().toString().isEmpty()) return;
                dialog.dismiss();
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
                if(autoCompleteTxt.getText().toString().equalsIgnoreCase("lost"))
                updateLeadStage.upldateLeadStageListnerLost(leadId, autoCompleteTxt.getText().toString(), commentET.getText().toString().trim(),stringList);
                else updateLeadStage.upldateLeadStageListner(leadId, autoCompleteTxt.getText().toString(), commentET.getText().toString().trim());

            }
        });
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static boolean getPermission(Context context, String moduleName, String submoduleName) {
        boolean permission = false;
        String rolesPermission = new PreferenceUtils(context).getStringFromPreference(PreferenceUtils.ROLEPERMISSIONDATA, "");
        if (!rolesPermission.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(rolesPermission);
                jsonObject = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("records").getJSONObject(0).getJSONArray("permissions");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.optString("module_code").equalsIgnoreCase(moduleName)) {
                        boolean modulePermission = false;
                        JSONArray array = object.getJSONArray("modulePermission");
                        for (int j = 0; j < array.length(); j++) {
                            if (object.optString("permission").equalsIgnoreCase(array.getJSONObject(j).getString("name"))) {
                                modulePermission = array.getJSONObject(j).optBoolean("isSelected", false);
                                break;
                            }
                        }
                        if (submoduleName.equalsIgnoreCase("")) {
                            return modulePermission;
                        }
                        //// sub module start /////

                        jsonArray = object.getJSONArray("submodules");

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject object1 = jsonArray.getJSONObject(k);
                            if (object1.optString("module_code").equalsIgnoreCase(submoduleName)) {
                                modulePermission = false;
                                array = object1.getJSONArray("modulePermission");
                                for (int j = 0; j < array.length(); j++) {
                                    if (object1.optString("permission").equalsIgnoreCase(array.getJSONObject(j).getString("name"))) {
                                        modulePermission = array.getJSONObject(j).optBoolean("isSelected", false);
                                        break;
                                    }
                                }
                                return modulePermission;
                            }
                        }
                        ////Sub module end //////
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.showLog("=======" + e.getMessage());
            }
        }
        return permission;
    }

    private static String getSelectedItem(String leadType, List<String> leadStageList) {
        for (int i = 0; i < leadStageList.size(); i++) {
            if (leadStageList.get(i).equalsIgnoreCase(leadType)) {
                return leadType;
            }
        }
        return leadType;
    }

    public static String convertYYYYMMMdd(String date) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String date2 = new SimpleDateFormat("yyyy MMM dd").format(date1);
            return date2.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String taskDateConvert(String date) {
        String outputDateString = null;
        try {
            Date inputDate = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.US).parse(date);
            outputDateString = new SimpleDateFormat("d'th' MMM yy hh:mm a", Locale.US).format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static String historyDateConvert(String date) {
        String outputDateString = null;
        try {
            Date inputDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            outputDateString = new SimpleDateFormat("MMM d, yyyy").format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static void openCalenderForUpcomingDates(Activity activity, TextView text_view, String field) {
        final Calendar calendar = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        try {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            String date2 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                            text_view.setText(date2);
                            if (field.equalsIgnoreCase("start date")) startDateTemp = date2;
                            else if (field.equalsIgnoreCase("end date")) endDateTemp = date2;
                            else if (field.equalsIgnoreCase("inquiry_start_date"))
                                inquiryStartDate = date2;
                            else if (field.equalsIgnoreCase("inquiry_end_date"))
                                inquiryEndDate = date2;
                            else if (field.equalsIgnoreCase("razorpay_start_date"))
                                startDateRazor = date2;
                            else if (field.equalsIgnoreCase("razorpay_end_date"))
                                endDateRazor = date2;
                            else if (field.equalsIgnoreCase("complaint_start_date"))
                                complaintStartDate = date2;
                            else if (field.equalsIgnoreCase("complaint_end_date"))
                                complaintEndDate = date2;
                            else if (field.equalsIgnoreCase("order start date"))
                                orderStartDate = date2;
                            else if (field.equalsIgnoreCase("order end date")) orderEndDate = date2;
                            else if (field.equalsIgnoreCase("transaction_start_date"))
                                transactionStartDate = date2;
                            else if (field.equalsIgnoreCase("transaction_end_date"))
                                transactionEndDate = date2;
                            else if (field.equalsIgnoreCase("architect_start_date"))
                                architectStartDate = date2;
                            else if (field.equalsIgnoreCase("architect_end_date"))
                                architectEndDate = date2;
                            else if (field.equalsIgnoreCase("analytic_start_date"))
                                analyticStartDate = date2;
                            else if (field.equalsIgnoreCase("analytic_end_date"))
                                analyticEndDate = date2;
                            else if (field.equalsIgnoreCase("transaction_start_date"))
                                transactionStartDate = date2;
                            else if (field.equalsIgnoreCase("transaction_end_date"))
                                transactionEndDate = date2;
                            else if (field.equalsIgnoreCase("architect_start_date"))
                                architectStartDate = date2;
                            else if (field.equalsIgnoreCase("architect_end_date"))
                                architectEndDate = date2;
                            else if (field.equalsIgnoreCase("all_worklog_start_date"))
                                allWorklogStartDate = date2;
                            else if (field.equalsIgnoreCase("all_worklog_end_date"))
                                allWorklogEndDate = date2;
                            else if (field.equalsIgnoreCase("shipping_start_date"))
                                shippingStartDate = date2;
                            else if (field.equalsIgnoreCase("shipping_end_date"))
                                shippingEndDate = date2;

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    public static boolean dateValidator(String initialDateString, String finalDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date initialDate = dateFormat.parse(initialDateString);
            Date finalDate = dateFormat.parse(finalDateString);

            // Compare the dates
            return !initialDate.after(finalDate);
        } catch (ParseException e) {
            // Handle parsing exception if needed
            e.printStackTrace();
            return false;
        }
    }

    public static void openCalenderForUpcomingDates(Activity activity, EditText text_view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        text_view.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();


    }

    public static String getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day);
    }

    public static void openCalender(Activity activity, EditText text_view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        text_view.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                },
                year, month, day);
        datePickerDialog.show();


    }

    public static void openCalenderForUptoPresentDate(Activity activity, EditText text_view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        text_view.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                    }
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    public static void openTimePicker(Activity activity, EditText text_view) {
        final Calendar calendar = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // on below line we are creating a variable for date picker dialog.
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // on below line we are setting selected time
                        // in our text view.
                        NumberFormat f = new DecimalFormat("00");

                        text_view.setText(f.format(hourOfDay) + ":" + f.format(minute));
                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();

    }

    public static String getCurrencyAmount(String amount) {
        if (amount == null || amount.equalsIgnoreCase("")) return "";
        @SuppressLint({"NewApi", "LocalSuppress"})
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }

    public static String getCurrencyAmountWithoutDecimal(String amount) {
        if (amount == null || amount.equalsIgnoreCase("")) return "";
        @SuppressLint({"NewApi", "LocalSuppress"})
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        format.setMaximumFractionDigits(2);
        return format.format(new BigDecimal(amount));
    }

    public static String getData(String val) {
        if (val == null) return "";
        if (val.isEmpty()) return "";
        return val;
    }

    public static String getProperCase(String val) {
        if (val == null) return "";
        if (val.isEmpty()) return "";
        String s = val.substring(0, 1).toUpperCase();
        val = s + val.substring(1);
        return val;
    }

//    public static void showDialog(Context context) {
//        Dialog dialog = new Dialog(context);
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        dialog.setContentView(R.layout.dropdown_menu_dialog);
//
//        DropdownMenuAdapter adapter = new DropdownMenuAdapter();
//        RecyclerView recyclerView = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            recyclerView = dialog.requireViewById(R.id.dropdown_menu_rv);
//        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(adapter);
//
//        dialog.show();
//    }

    public static void showSingleNotificationDialog(Context mContext) {
        final Dialog dialog = new Dialog(mContext, R.style.TransparentDialogTheme);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.single_notification_dialog, null);
        dialog.setContentView(dialogView);
        dialog.show();
    }

    public static void openWhatsApp(Context context, String mobno, String msg) {
        context.startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+91" + mobno, msg)
                        )
                )
        );
    }

    public static void openWhatsApp(Context context, String msg) {
        context.startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=&text=%s", msg)
                        )
                )
        );
    }

    public static void openDialerPad(Context context, String phoneno) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneno));
        context.startActivity(intent);
    }

    public static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf(".");
        if (lastIndex != -1 && lastIndex < filePath.length() - 1) {
            return filePath.substring(lastIndex + 1);
        }
        return ""; // Return empty string if no extension found or filePath is empty
    }

    // Method to determine content type based on file extension
    public static String getContentType(String fileExtension) {
        // Implement your logic to map file extensions to content types
        // Example: you can use a switch statement or if-else conditions
        // This is just a sample logic, you need to extend it based on your requirements
        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
                return "image/png"; // Assuming it's an image/jpeg content type
            case "pdf":
                return "application/pdf";
            case "mp4":
            case "avi":
            case "mov":
                return "video/mp4";
            case "txt":
                return "text/plain";
            // Add more cases as needed for other file extensions
            default:
                return "application/octet-stream"; // Default content type for unknown file types
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showStatusDialog(Context context, String message, boolean isLogout) {
        Common.showToast(context, message);
        if (isLogout) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
            logout(context, null, new Gson().toJson(map));
        } else {
            ((Activity) context).finishAffinity();
            context.startActivity(new Intent(context, DashboardActivity.class));
        }
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View myView = layoutInflater.inflate(R.layout.socket_status_dialog, null);
//        Dialog dialog=new Dialog(context);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        builder.setView(myView);
//        AlertDialog dialog = builder.create();
//        dialog.setContentView(R.layout.socket_status_dialog);
//        dialog.setCancelable(false);
//        Window window = dialog.getWindow();
//        window.setBackgroundDrawableResource(R.drawable.white_round_10bg);
//        dialog.show();
//        Common.showToast(context,message);
//        ((TextView) dialog.findViewById(R.id.dialog_title_tv)).setText(message);
//
//        ((TextView) dialog.findViewById(R.id.ok_tv)).setText(isLogout ? "Logout" : "Ok");
//        ((TextView) dialog.findViewById(R.id.ok_tv)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "profile_pic", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void receivedSocketMessage(Context mActivity) {
        if (SocketManager.socket == null) return;
        Common.showLog("SOCKET isConnected===" + SocketManager.isConnected());
        mHandler = new Handler(Looper.getMainLooper());
        SocketManager.socket.on("Permission_Check", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String response = args[0].toString();
                Common.showLog("===App===" + response);
                Intent intent = new Intent();
                intent.setAction("com.devstringx.mytylesstockcheck.SOCKET_NOTIFICATION");
                intent.putExtra("data", response);
                mActivity.sendBroadcast(intent);

            }
        });
    }
}
