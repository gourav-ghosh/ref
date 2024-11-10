package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.GraphColorListAdapter;
import com.devstringx.mytylesstockcheck.adapter.SalesPersonAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.SalesPersonSelectAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.CustomBarChartRender;
import com.devstringx.mytylesstockcheck.common.CustomMarkerView;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.common.ProductPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.ProductPickerDeialog;
import com.devstringx.mytylesstockcheck.common.SalesPersonSelectCallbacks;
import com.devstringx.mytylesstockcheck.common.SalesPersonSelectDeialog;
import com.devstringx.mytylesstockcheck.databinding.FragmentHomeBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.leadAnalyticData.LeadAnalyticDataResponse;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.leadAnalyticData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.leadConversationData.LeadConversationDataResponse;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.quotationAnalyticData.QuotationAnalyticDataResponse;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.scAnalyticData.Records;
import com.devstringx.mytylesstockcheck.datamodal.dashboard.scAnalyticData.SCAnalyticDataResponse;
import com.devstringx.mytylesstockcheck.datamodal.salesperson.OrderSalesPerson;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements ResponseListner, SalesPersonAutoCompleteListAdapter.SalesPersonOnClickListener,
        SalesPersonSelectAdapter.SalesPersonDialogOnClickListener {
    private String FILTERDATA = "Today";
    private int QATOTAL = 0, LGSTOTAL = 0, LCRTOTAL = 0;
    FragmentHomeBinding homeBinding;
    String SELECTEDYEAR = "2023", SELECTEDORDERMONTH = "all" ,SELECTEDMONTH = "0", SELECTEDLEADYEAR = "2023", SELECTEDLEADDMONTH = "0";
    private PreferenceUtils utils;
    private String[] months;
    ArrayAdapter<CharSequence> yearAdapter, monthAdapter, leadYearAdapter, leadMonthAdapter, scAdapter, OrderAnanlyticsAdapter;
    private SalesPersonAutoCompleteListAdapter  salesPersonAdapter;
    private List<String> orderSalesPersonStringList = new ArrayList<>();
    private List<String> spinnerListSalesPerson = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isAnyPermission = false;
    private NetworkRequest.DProgressbar loaderUtils;
    private SalesPersonSelectAdapter.SalesPersonDialogOnClickListener salesPersonDialogOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = homeBinding.getRoot();
        salesPersonDialogOnClickListener = this;
        Common.userRoleId = new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID, "").toString();
        getAllOwners();
        setupClickListner();
        homeBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                orderSalesPersonStringList.clear();
                orderSalesPersonResponseList.clear();
                getAllOwners();
                if(Common.userRoleId.equalsIgnoreCase("1") || Common.userRoleId.equalsIgnoreCase("6") || Common.userRoleId.equalsIgnoreCase("13") && Common.userProfilEligibleForCRM == 1) {
                    homeBinding.orderCv.setVisibility(View.VISIBLE);
                    orderSalesPersonStringList.clear();
                    getOrderAnalyticsData(orderSalesPersonStringList);
                } else homeBinding.orderCv.setVisibility(View.GONE);
                if (!Common.getPermission(getActivity(), "DB", "QA")) {
                    homeBinding.quotationCv.setVisibility(View.GONE);
                } else {
                    getQuotationAnalyticData();
                }
                if (!Common.getPermission(getActivity(), "DB", "LA")) {
                    homeBinding.leadCv.setVisibility(View.GONE);
                } else {
                    getLeadAnalyticData();
                }
                if (!Common.getPermission(getActivity(), "DB", "SCIA")) {
                    homeBinding.scLl.setVisibility(View.GONE);
                } else {
                    getStockCheckAnalyticData(FILTERDATA);
                }
            }
        });
        if(Common.userRoleId.equalsIgnoreCase("1") || Common.userRoleId.equalsIgnoreCase("6") || Common.userRoleId.equalsIgnoreCase("13") && Common.userProfilEligibleForCRM == 1) {
            homeBinding.orderCv.setVisibility(View.VISIBLE);
            orderSalesPersonStringList.clear();
            getOrderAnalyticsData(orderSalesPersonStringList);
        } else homeBinding.orderCv.setVisibility(View.GONE);

        if (!Common.getPermission(getActivity(), "DB", "QA")) {
            homeBinding.quotationCv.setVisibility(View.GONE);
        } else {
            isAnyPermission = true;
            getQuotationAnalyticData();
        }
        if (!Common.getPermission(getActivity(), "DB", "LA")) {
            homeBinding.leadCv.setVisibility(View.GONE);
        } else {
            isAnyPermission = true;
            getLeadAnalyticData();
        }
        if (!Common.getPermission(getActivity(), "DB", "SCIA")) {
            homeBinding.scLl.setVisibility(View.GONE);
        } else {
            isAnyPermission = true;
            getStockCheckAnalyticData(FILTERDATA);
        }
        if (isAnyPermission) {
            homeBinding.reportNs.setVisibility(View.VISIBLE);
            homeBinding.noReportTv.setVisibility(View.GONE);
        } else {
            homeBinding.reportNs.setVisibility(View.GONE);
            homeBinding.noReportTv.setVisibility(View.VISIBLE);
        }

        if ((Common.getPermission(getActivity(), "DB", "")) &&
                (Common.getPermission(getActivity(), "MU", "")) &&
                (Common.getPermission(getActivity(), "SCE", "")) ||
                (Common.getPermission(getActivity(), "DB", "")) &&
                        (Common.getPermission(getActivity(), "SCE", ""))) {
            homeBinding.stockCheckInquiryLinearLayout.setColumnCount(2);
            homeBinding.stockCheckInquiryLinearLayout.setOrientation(GridLayout.HORIZONTAL);
        }

        if ((Common.getPermission(getActivity(), "DB", "")) &&
                (Common.getPermission(getActivity(), "MU", "")) &&
                (Common.getPermission(getActivity(), "SCE", "")) &&
                (Common.getPermission(getActivity(),"ML",""))) {
            homeBinding.stockCheckInquiryLinearLayout.setRowCount(1);
            homeBinding.stockCheckInquiryLinearLayout.setOrientation(GridLayout.VERTICAL);
        }

        String[] allmonthListSpinner = getResources().getStringArray(R.array.CustomList);
        homeBinding.allMonthSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(allmonthListSpinner[position].equalsIgnoreCase("custom")) {
                    homeBinding.customDate.setVisibility(View.VISIBLE);
                    homeBinding.fromDate.setText("");
                    homeBinding.toDate.setText("");
                } else {
                    homeBinding.customDate.setVisibility(View.GONE);
                    SELECTEDORDERMONTH = allmonthListSpinner[position].toLowerCase().replace(" ", "_");
                    orderSalesPersonStringList.clear();
                    getOrderAnalyticsData(orderSalesPersonStringList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        homeBinding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openCalenderForUpcomingDates(getActivity(), homeBinding.fromDate, "");
            }
        });

        homeBinding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openCalenderForUpcomingDates(getActivity(), homeBinding.toDate, "");
            }
        });

        homeBinding.applyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSalesPersonStringList.clear();
                getOrderAnalyticsData(orderSalesPersonStringList);
            }
        });

        return view;
    }

    private void setupClickListner() {
        utils = new PreferenceUtils(getActivity());
        loaderUtils = new NetworkRequest.DProgressbar(getActivity());
        Calendar calendar = Calendar.getInstance();
        String[] years = getResources().getStringArray(R.array.YearList);
        String[] customList = getResources().getStringArray(R.array.Dashboard_CustomList);
        months = getResources().getStringArray(R.array.MonthList);
        OrderAnanlyticsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.CustomList, R.layout.spinner_item);
        yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.YearList, R.layout.spinner_item);
        monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.MonthList, R.layout.spinner_item);
        leadYearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.YearList, R.layout.spinner_item);
        leadMonthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.MonthList, R.layout.spinner_item);
        scAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Dashboard_CustomList, R.layout.spinner_item);
        homeBinding.allMonthSpinnerView.setAdapter(OrderAnanlyticsAdapter);
        homeBinding.yearSp.setAdapter(yearAdapter);
        homeBinding.monthSpinnerView.setAdapter(monthAdapter);
        homeBinding.leadYearSpinnerView.setAdapter(leadYearAdapter);
        homeBinding.leadMonthSpinnerView.setAdapter(leadMonthAdapter);
        homeBinding.scSp.setAdapter(scAdapter);

        for (int i = 0; i < years.length; i++) {
            if (years[i].equalsIgnoreCase(String.valueOf(calendar.get(Calendar.YEAR)))) {
                homeBinding.yearSp.setSelection(i);
                homeBinding.leadYearSpinnerView.setSelection(i);
                SELECTEDYEAR = String.valueOf(calendar.get(Calendar.YEAR));
                SELECTEDLEADYEAR = String.valueOf(calendar.get(Calendar.YEAR));
                break;
            }
        }
        homeBinding.yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        for (int i = 0; i < months.length; i++) {
            if (i == calendar.get(Calendar.MONTH)) {
                homeBinding.monthSpinnerView.setSelection(i);
                homeBinding.leadMonthSpinnerView.setSelection(i);
                SELECTEDMONTH = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                SELECTEDLEADDMONTH = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                break;
            }
        }
        homeBinding.yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDYEAR = years[position];
                getQuotationAnalyticData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        homeBinding.monthSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDMONTH = String.valueOf(position + 1);
                getQuotationAnalyticData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        homeBinding.leadYearSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDLEADYEAR = years[position];
                getLeadAnalyticData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        homeBinding.leadMonthSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDLEADDMONTH = String.valueOf(position + 1);
                getLeadConversationData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        homeBinding.scSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FILTERDATA = customList[position];
                if (!customList[position].equalsIgnoreCase("today")) {
                    getStockCheckAnalyticData("This " + customList[position]);
                } else {
                    getStockCheckAnalyticData(customList[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        homeBinding.quotationExportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QATOTAL == 0) {
                    Common.showToast(getActivity(), getString(R.string.no_record_found));
                    return;
                }
                getExportQuotationAnalyticData();
            }
        });
        homeBinding.exportLeadGenIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LGSTOTAL == 0) {
                    Common.showToast(getActivity(), getString(R.string.no_record_found));
                    return;
                }
                getExportLeadAnalyticData();
            }
        });
        homeBinding.exportLeadConvIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LCRTOTAL == 0) {
                    Common.showToast(getActivity(), getString(R.string.no_record_found));
                    return;
                }
                getExportLeadConversationData();
            }
        });

        homeBinding.allMonthSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getQuotationAnalyticData() {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDYEAR));
        data1.put("month", Integer.parseInt(SELECTEDMONTH));
        data1.put("user_id", utils.getStringFromPreference(PreferenceUtils.USERID, ""));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_DASHBOARANALYTICSDDATA, data, false);
    }

    private void getLeadAnalyticData() {
        loaderUtils.show();
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDLEADYEAR));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_LEADANALYTICSDATA, data, false);
    }

    private void getLeadConversationData() {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDLEADYEAR));
        data1.put("month", Integer.parseInt(SELECTEDLEADDMONTH));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_LEADCONVERSATIONDATA, data, false);
    }

    private void getStockCheckAnalyticData(String filterdata) {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("filter", filterdata);
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_SCANALYTICSDATA, data, false);
    }

    private void getExportQuotationAnalyticData() {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDYEAR));
        data1.put("month", Integer.parseInt(SELECTEDMONTH));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTQUOTATIONANALYTICSDATA, data, true);
    }

    private void getExportLeadAnalyticData() {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDLEADYEAR));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTLEADGENDATA, data, true);
    }

    private void getExportLeadConversationData() {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("year", Integer.parseInt(SELECTEDLEADYEAR));
        data1.put("month", Integer.parseInt(SELECTEDLEADDMONTH));
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.DATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_EXPORTLEADCONVDATA, data, true);
    }

    private void getOrderAnalyticsData(List<String> orderSalesPersonName) {
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("dateRange", SELECTEDORDERMONTH);
        data1.put("fromDate", homeBinding.fromDate.getText().toString());
        data1.put("toDate", homeBinding.toDate.getText().toString());
        data1.put("saleExecutive", orderSalesPersonName);
        HashMap<String, Object> data = new HashMap<>();
        data.put(NKeys.ORDERANALYTICSDDATA, new Gson().toJson(data1));
        Common.showLog("DATA====" + new Gson().toJson(data1));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_ORDERANALYTICSDDATA, data, false);
    }

    private void getAllOwners() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNumber","");
            jsonObject.put("pageSize","");
            jsonObject.put("role", "6");
            jsonObject.put("user_status", "active");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, jsonObject.toString());
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GETALLOWNERS, map, false);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        homeBinding.refreshLayout.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_ORDERANALYTICSDDATA) {
                try {
                    JSONObject obj = new JSONObject(responseDO.getResponse());
                    setupOrderAnalytics(obj.getJSONObject("data").getJSONObject("records"));
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \""  + "\"");
                }
//                OrderAnalyticsDataResponse orderAnalyticsDataResponse = new Gson().fromJson(responseDO.getResponse(), OrderAnalyticsDataResponse.class);
//                setupdataOA(orderAnalyticsDataResponse.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DASHBOARANALYTICSDDATA) {
                QuotationAnalyticDataResponse topDashboardResponse = new Gson().fromJson(responseDO.getResponse(), QuotationAnalyticDataResponse.class);
                setupdataQA(topDashboardResponse.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_LEADANALYTICSDATA) {
                LeadAnalyticDataResponse analyticDataResponse = new Gson().fromJson(responseDO.getResponse(), LeadAnalyticDataResponse.class);
                setLineChartData(analyticDataResponse.getData().getRecords());
                getLeadConversationData();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_LEADCONVERSATIONDATA) {
                LeadConversationDataResponse conversationDataResponse = new Gson().fromJson(responseDO.getResponse(), LeadConversationDataResponse.class);
                setupConverSationData(conversationDataResponse.getData().getRecords());
                loaderUtils.dismiss();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_SCANALYTICSDATA) {
                SCAnalyticDataResponse analyticDataResponse = new Gson().fromJson(responseDO.getResponse(), SCAnalyticDataResponse.class);

                setupSCData(analyticDataResponse.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLOWNERS) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                setupOrderSalesPersonList(allOwnersData.getData().getRecords());
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTQUOTATIONANALYTICSDATA ||
                    responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTLEADCONVDATA ||
                    responseDO.getServiceMethods() == ServiceMethods.WS_EXPORTLEADGENDATA) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(jsonObject.optJSONObject("data").optString("records")));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    ArrayList<OrderSalesPerson> orderSalesPersonResponseList = new ArrayList<>();
    private void setupOrderSalesPersonList(List<com.devstringx.mytylesstockcheck.datamodal.allOwners.RecordsItem> recordsItems) {
        orderSalesPersonResponseList.clear();
        OrderSalesPerson person1 = new OrderSalesPerson();person1.setTitle("Search");person1.setSelected(false);
        OrderSalesPerson person2 = new OrderSalesPerson();person2.setTitle("Select All");person2.setSelected(false);
        orderSalesPersonResponseList.add(person1);
        orderSalesPersonResponseList.add(person2);
        for (int i = 0; i < recordsItems.size(); i++) {
            OrderSalesPerson stateVO = new OrderSalesPerson();
            stateVO.setTitle(recordsItems.get(i).getFirstName()+" "+ recordsItems.get(i).getLastName());
            stateVO.setSelected(false);
            orderSalesPersonResponseList.add(stateVO);
        }
        Common.orderSalesPersonResponseList = orderSalesPersonResponseList;
        salesPersonAdapter = new SalesPersonAutoCompleteListAdapter(getActivity(),0, orderSalesPersonResponseList,this);
        homeBinding.salePersonSp.setAdapter(salesPersonAdapter);
    }

    private void setupOrderAnalytics(JSONObject jsonObject) {
        List<String> xList = new ArrayList<>();
        List<String> yList = new ArrayList<>();
        List<String> zList = new ArrayList<>();
        if(Common.userRoleId.equalsIgnoreCase("6")) homeBinding.allSalesCv.setVisibility(View.GONE);
        else homeBinding.allSalesCv.setVisibility(View.VISIBLE);
        if(!Common.userRoleId.equalsIgnoreCase("13")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONObject("SaleAnalysis").getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(String.valueOf(jsonArray.get(i)));
                }
                JSONArray jsonArray1 = jsonObject.getJSONObject("SaleAnalysis").getJSONArray("values");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    yList.add(String.valueOf(jsonArray1.get(i)));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            showBarChart(homeBinding.BarChart1, xList, yList);
            yList.clear();
            xList.clear();

            try {
                JSONArray jsonArray = jsonObject.getJSONObject("OrderAnalysis").getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(String.valueOf(jsonArray.get(i)));
                }
                JSONArray jsonArray1 = jsonObject.getJSONObject("OrderAnalysis").getJSONArray("values");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    yList.add(String.valueOf(jsonArray1.get(i)));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            showBarChart(homeBinding.barChart2, xList, yList);
            yList.clear();
            xList.clear();

            try {
                JSONArray jsonArray = jsonObject.getJSONObject("ShowroomVisit").getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(String.valueOf(jsonArray.get(i)));
                }
                JSONArray jsonArray1 = jsonObject.getJSONObject("ShowroomVisit").getJSONArray("values");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    yList.add(String.valueOf(jsonArray1.get(i)));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            showBarChart(homeBinding.barChart3, xList, yList);
            yList.clear();
            xList.clear();

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("SaleAmountReport");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(jsonArray.getJSONObject(i).getString("amount"));
                    yList.add(jsonArray.getJSONObject(i).getString("count"));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            showSaleAmountreport(xList, yList);
            yList.clear();
            xList.clear();

            try {
                JSONArray jsonArray = jsonObject.getJSONObject("SaleAnalysisMonth").getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(String.valueOf(jsonArray.get(i)));
                }
                JSONArray jsonArray1 = jsonObject.getJSONObject("SaleAnalysisMonth").getJSONArray("values");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    yList.add(String.valueOf(jsonArray1.get(i)));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            showBarChart(homeBinding.barChart4, xList, yList);
            yList.clear();
            xList.clear();

            try {
                JSONArray jsonArray = jsonObject.getJSONObject("LeadTicketSizeAnalysis").getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    xList.add(String.valueOf(jsonArray.get(i)));
                }
                JSONArray jsonArray1 = jsonObject.getJSONObject("LeadTicketSizeAnalysis").getJSONArray("percentage");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    yList.add(String.valueOf(jsonArray1.get(i)));
                }
                JSONArray jsonArray2 = jsonObject.getJSONObject("LeadTicketSizeAnalysis").getJSONArray("number");
                for (int i = 0; i < jsonArray2.length(); i++) {
                    zList.add(String.valueOf(jsonArray2.get(i)));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        if(Common.userRoleId.equalsIgnoreCase("1") || Common.userRoleId.equalsIgnoreCase("13") && Common.userProfilEligibleForCRM == 1) {
            homeBinding.leadTicketPieChart.setVisibility(View.VISIBLE);
            showPieChart(homeBinding.orderPieChart,xList,yList,zList);
        } else homeBinding.leadTicketPieChart.setVisibility(View.GONE);
        yList.clear();
        xList.clear();
        zList.clear();

    }

    private void showPieChart(PieChart pieChart,List<String> xList, List<String> yList, List<String> zList) {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#5BA944"));
        colors.add(Color.parseColor("#F8B360"));
        colors.add(Color.parseColor("#A8AAB0"));
        colors.add(Color.parseColor("#FA564A"));

        homeBinding.childLayout.removeAllViews();
        Map<String, Integer> typeMap = new HashMap<>();
        for(int i = 0; i < xList.size(); i++ ){
            pieEntries.add(new PieEntry(Float.parseFloat(yList.get(i)), xList.get(i)));

            LayoutInflater inflater = getLayoutInflater();
            TableRow tr = (TableRow)  inflater.inflate(R.layout.table_row_legend,
                    homeBinding.childLayout, false);
            homeBinding.childLayout.addView(tr);
            LinearLayout linearLayoutColorContainer=(LinearLayout) tr.getChildAt(0);
            LinearLayout linearLayoutColor= (LinearLayout) linearLayoutColorContainer.getChildAt(0);
            TextView tvLabel = (TextView) tr.getChildAt(1);
            TextView tvAmt = (TextView) tr.getChildAt(2);
            linearLayoutColor.setBackgroundColor(colors.get(i));
            String text = "";
            if(xList.get(i).equalsIgnoreCase("high")) {
                text = "<font color=#FF000000> High - " + zList.get(i) + "</font> <font color=#5BA944>" + "(" + yList.get(i) + "%)" + "</font>";
            }else if(xList.get(i).equalsIgnoreCase("medium")) {
                text = "<font color=#FF000000> Medium - " + zList.get(i) + "</font> <font color=#F8B360>" + "(" + yList.get(i) + "%)" + "</font>";
            }else if(xList.get(i).equalsIgnoreCase("low")) {
                text = "<font color=#FF000000> Low - " + zList.get(i) + "</font> <font color=#A8AAB0>" + "(" + yList.get(i) + "%)" + "</font>";
            }else if(xList.get(i).equalsIgnoreCase("unknown")) {
                text = "<font color=#FF000000> Unknown - " + zList.get(i) + "</font> <font color=#FA564A>" + "(" + yList.get(i) + "%)" + "</font>";
            }
            tvLabel.setText(Html.fromHtml(text));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(0f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(0);
        pieChart.setHoleColor(getResources().getColor(R.color.transparent));
        pieChart.setDrawHoleEnabled(false);

        //legend attributes
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(false);
        legend.setDrawInside(false);
        legend.setEnabled(false);
        legend.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.outfit_regular));
        legend.setTextSize(12);
        legend.setFormSize(20);
        legend.setFormToTextSpace(2);

    }


    private void showSaleAmountreport(List<String> saleAnalysisxlist, List<String> saleAnalysisylist) {
        homeBinding.saleAmountReportLinearLayout.removeAllViews();

        for (int i = 0; i < saleAnalysisxlist.size(); i++) {
            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.outfit_medium);
            RelativeLayout mRelLayout00 = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams mRelLayoutParams = new
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mRelLayout00.setBackgroundColor(getResources().getColor(R.color.light_grey));
            mRelLayout00.setLayoutParams(mRelLayoutParams);
            mRelLayout00.getLayoutParams().height = 80;
            mRelLayoutParams.setMargins(30, 0, 30, 30);
            TextView textView = new TextView(getActivity());
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            textParams.addRule(RelativeLayout.CENTER_VERTICAL);
            textParams.setMargins(40, 0, 0, 0);
            textView.setTypeface(typeface);
            textView.setLayoutParams(textParams);
            textView.setTextSize(13);
            textView.setShadowLayer(10, 0, 0, android.R.color.white);
            textView.setText(saleAnalysisxlist.get(i));
            textView.setTextColor(getResources().getColor(R.color.gray));
            mRelLayout00.addView(textView);

            TextView textView2 = new TextView(getActivity());
            RelativeLayout.LayoutParams textParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textParams2.addRule(RelativeLayout.ALIGN_PARENT_END);
            textParams2.addRule(RelativeLayout.CENTER_VERTICAL);
            textParams2.setMargins(0, 0, 40, 0);
            textView2.setLayoutParams(textParams2);
            textView2.setTextSize(13);
            textView2.setTypeface(typeface);
            textView2.setShadowLayer(10, 0, 0, android.R.color.white);
            textView2.setText(saleAnalysisylist.get(i));
            textView2.setTextColor(getResources().getColor(R.color.orange));
            mRelLayout00.addView(textView2);
            homeBinding.saleAmountReportLinearLayout.addView(mRelLayout00);
        }

    }

    private void showBarChart(BarChart BarChart1, List<String> xlist, List<String> ylist){

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < ylist.size(); i++){
            entries.add(new BarEntry(i,Float.parseFloat(ylist.get(i))));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "months");
        barDataSet.setDrawValues(false);
        barDataSet.setHighLightColor(getResources().getColor(R.color.orange));
        barDataSet.setHighLightAlpha(0);
        if(BarChart1.getId() == R.id.barChart4)
            barDataSet.setColor(getResources().getColor(R.color.orange));
        else
            barDataSet.setColor(getResources().getColor(R.color.light_grey));
        barDataSet.setBarBorderWidth(1f);
        barDataSet.setBarBorderColor(getResources().getColor(R.color.orange));

        BarData data = new BarData(barDataSet);
        BarChart1.setData(data);
        BarChart1.invalidate();

        BarChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xlist));

        BarChart1.getAxisLeft().setEnabled(true);
        BarChart1.getAxisRight().setEnabled(false);

        BarChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        BarChart1.getXAxis().setGranularity(1f);
        BarChart1.getXAxis().setGranularityEnabled(true);
        BarChart1.getDescription().setEnabled(false);
        BarChart1.getAxisLeft().setAxisMinimum(0f);
        BarChart1.getAxisRight().setAxisMaximum(0f);
        BarChart1.getXAxis().setDrawGridLines(false);
        BarChart1.getLegend().setEnabled(false);
        BarChart1.getAxisRight().setDrawLabels(false);
        BarChart1.getAxisLeft().setDrawAxisLine(false);
        BarChart1.getAxisLeft().setTextColor(getResources().getColor(R.color.light_orange));
        BarChart1.getXAxis().setTextColor(getResources().getColor(R.color.light_orange));
        BarChart1.getAxisLeft().setGridColor(getResources().getColor(R.color.light_grey));
        BarChart1.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        BarChart1.setScaleEnabled(false);
        CustomBarChartRender barChartRender = new CustomBarChartRender(BarChart1,BarChart1.getAnimator(), BarChart1.getViewPortHandler());
        barChartRender.setRadius(20);
        barChartRender.initBuffers();
        BarChart1.setRenderer(barChartRender);
        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.custom_marker_view_layout);
        BarChart1.setMarker(mv);
        BarChart1.setDrawMarkers(true);

    }

    private void showBarChart(com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics.Records records){
        List<String> xList = new ArrayList<>();
        ArrayList<Double> valueList = new ArrayList<Double>();
        String title = "Title";

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,45f));
        entries.add(new BarEntry(1,850f));
        entries.add(new BarEntry(2,55f));
        entries.add(new BarEntry(3,60f));
        entries.add(new BarEntry(4,5f));
        entries.add(new BarEntry(5,75f));

        BarDataSet barDataSet = new BarDataSet(entries, "months");

        BarData data = new BarData(barDataSet);
        homeBinding.BarChart1.setData(data);
        homeBinding.BarChart1.invalidate();
        homeBinding.BarChart1.getDescription().setEnabled(false);

        List<String> list = Arrays.asList(records.getSaleAnalysis().getCategories().toString());
        XAxis xAxis = homeBinding.BarChart1.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));

        homeBinding.BarChart1.getXAxis().setEnabled(false);
        homeBinding.BarChart1.getAxisLeft().setEnabled(true);
        homeBinding.BarChart1.getAxisRight().setEnabled(false);
    }

    private void setupdataOA(com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics.Records records) {
        Common.showLog("WWWWWWWWWWWWWWWWW" + records.getSaleAnalysis().getCategories().size());
        showBarChart(records);
    }

    private void setupdataQA(com.devstringx.mytylesstockcheck.datamodal.dashboard.quotationAnalyticData.Records records) {

        homeBinding.count.setText(getVal(records.getQuotationAnalytics().getOpenQuotes()));
        homeBinding.amountTV.setText(getVal(records.getQuotationAnalytics().getTotalNotConverted()));
        homeBinding.timeTV.setText(months[Integer.parseInt(SELECTEDMONTH) - 1] + " " + SELECTEDYEAR);

        homeBinding.convertedCount.setText(getVal(records.getQuotationAnalytics().getConvertedQuotes()));
        homeBinding.convertedAmountTV.setText(getVal(records.getQuotationAnalytics().getTotalConverted()));
        homeBinding.convertedTimeTV.setText(months[Integer.parseInt(SELECTEDMONTH) - 1] + " " + SELECTEDYEAR);
        BigDecimal bd = new BigDecimal((Integer.parseInt(getVal(records.getQuotationAnalytics().getOpenQuotes())) + Integer.parseInt(getVal(records.getQuotationAnalytics().getConvertedQuotes()))));
        Common.showLog("====TOTA====" + bd.intValue());
        homeBinding.progressBar.setMax(bd.intValue());
        homeBinding.progressBar.setProgress(Integer.parseInt(getVal(records.getQuotationAnalytics().getOpenQuotes())));
        homeBinding.totalTv.setText(String.valueOf(bd.intValue()));
        QATOTAL = bd.intValue();
    }

    private void setupSCData(Records records) {
        homeBinding.totalInquiriesCountTv2.setText(getVal(records.getResult().get(0).getTotalEnquiries()));
        String val = getVal(records.getPercentageChange().getTotalEnquiries());
        if (val.startsWith("-")) {
            homeBinding.totalInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val.substring(1))) + "%");
            homeBinding.totalInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.totalInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.totalInquiryPercentageTextTV.setText("Less" + getSuffixData());
            homeBinding.totalInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_down, 0);
        } else {
            homeBinding.totalInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val)) + "%");
            homeBinding.totalInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.totalInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.totalInquiryPercentageTextTV.setText("More" + getSuffixData());
            homeBinding.totalInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_up, 0);
        }


        homeBinding.availableInquiriesCountTv.setText(getVal(records.getResult().get(0).getAvailableEnquiries()));
        val = getVal(records.getPercentageChange().getAvailableEnquiries());
        if (val.startsWith("-")) {
            homeBinding.availableInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val.substring(1))) + "%");
            homeBinding.availableInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.availableInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.availableInquiryPercentageTextTV.setText("Less" + getSuffixData());
            homeBinding.availableInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_down, 0);
        } else {
            homeBinding.availableInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val)) + "%");
            homeBinding.availableInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.availableInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.availableInquiryPercentageTextTV.setText("More" + getSuffixData());
            homeBinding.availableInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_up, 0);
        }

        homeBinding.notAvailableInquiriesCountTv.setText(getVal(records.getResult().get(0).getNotAvailableEnquiries()));
        homeBinding.notAvailableInquiryPercentageTextTV.setText(getSuffixData());
        val = getVal(records.getPercentageChange().getNotAvailableEnquiries());
        if (val.startsWith("-")) {
            homeBinding.notAvailableInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val.substring(1))) + "%");
            homeBinding.notAvailableInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailableInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailableInquiryPercentageTextTV.setText("Less" + getSuffixData());
            homeBinding.notAvailableInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_down, 0);
        } else {
            homeBinding.notAvailableInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val)) + "%");
            homeBinding.notAvailableInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailableInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailableInquiryPercentageTextTV.setText("More" + getSuffixData());
            homeBinding.notAvailableInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_up, 0);
        }


        homeBinding.notAvailOrderPlaceInquiriesCountTv.setText(getVal(records.getResult().get(0).getNotAvailableEnquiriesAndOrderPlaced()));
        homeBinding.notAvailOrderPlaceInquiryPercentageTextTV.setText(getSuffixData());
        val = getVal(records.getPercentageChange().getNotAvailableEnquiriesAndOrderPlaced());
        if (val.startsWith("-")) {
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val.substring(1))) + "%");
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.notAvailOrderPlaceInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.notAvailOrderPlaceInquiryPercentageTextTV.setText("Less" + getSuffixData());
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_down, 0);
        } else {
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setText(String.format("%.0f", Double.parseDouble(val)) + "%");
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailOrderPlaceInquiryPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.notAvailOrderPlaceInquiryPercentageTextTV.setText("More" + getSuffixData());
            homeBinding.notAvailOrderPlaceInquiryPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_up, 0);
        }
        homeBinding.inquiriesHandledCountTv.setText(getVal(records.getResult().get(0).getEnquiriesHandledStockCheck()));
        val = getVal(records.getPercentageChange().getEnquiriesHandledStockCheck());
        if (val.startsWith("-")) {
            homeBinding.inquiriesHandledPercentageTV.setText(String.format("%.0f", Double.parseDouble(val.substring(1))) + "%");
            homeBinding.inquiriesHandledPercentageTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.inquiriesHandledPercentageTextTV.setTextColor(getActivity().getColor(R.color.red));
            homeBinding.inquiriesHandledPercentageTextTV.setText("Less" + getSuffixData());
            homeBinding.inquiriesHandledPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_down, 0);
        } else {
            homeBinding.inquiriesHandledPercentageTV.setText(String.format("%.0f", Double.parseDouble(val)) + "%");
            homeBinding.inquiriesHandledPercentageTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.inquiriesHandledPercentageTextTV.setTextColor(getActivity().getColor(R.color.green));
            homeBinding.inquiriesHandledPercentageTextTV.setText("More" + getSuffixData());
            homeBinding.inquiriesHandledPercentageTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_up, 0);
        }
    }

    private String getVal(String val) {
        if (val == null) return "0";
        if (val.equalsIgnoreCase("")) return "0";
        return val;
    }

    private String getSuffixData() {
        if (FILTERDATA.equalsIgnoreCase("today")) {
            return " than yesterday";
        } else {
            return " than previous " + FILTERDATA;
        }
    }

    private void setupConverSationData(List<com.devstringx.mytylesstockcheck.datamodal.dashboard.leadConversationData.RecordsItem> records) {
        ArrayList<PieEntry> orderEntries = new ArrayList<>();
        boolean isAllZero = false;
        LCRTOTAL = 0;
        for (int i = 0; i < records.size(); i++) {

            LCRTOTAL += records.get(i).getTotalLeads() + records.get(i).getOrderPlaced() + records.get(i).getShowroomVisit();
        }
        ArrayList<String> statusTotalCount = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            statusTotalCount.add(String.valueOf(records.get(i).getTotalLeads()));
            statusTotalCount.add(String.valueOf(records.get(i).getShowroomVisit()));
            statusTotalCount.add(String.valueOf(records.get(i).getOrderPlaced()));
            if (records.get(i).getTotalLeads() > 0) {
                float leadPer = (records.get(i).getTotalLeads() * 100) / LCRTOTAL;
                orderEntries.add(new PieEntry(leadPer, "Leads Created " + String.valueOf(records.get(i).getTotalLeads())));
            }
            if (records.get(i).getOrderPlaced() > 0) {
                float leadPer = (records.get(i).getOrderPlaced() * 100) / LCRTOTAL;
                orderEntries.add(new PieEntry(leadPer, "Confirm Orders " + String.valueOf(records.get(i).getOrderPlaced())));
            }
            if (records.get(i).getShowroomVisit() > 0) {
                float leadPer = (records.get(i).getShowroomVisit() * 100) / LCRTOTAL;
                orderEntries.add(new PieEntry(leadPer, "Visited Showroom " + String.valueOf(records.get(i).getShowroomVisit())));
            }
            if (records.get(i).getTotalLeads() == 0 && records.get(i).getShowroomVisit() == 0 && records.get(i).getOrderPlaced() == 0) {
                isAllZero = true;
            }
        }
        if (isAllZero) {
            LCRTOTAL = 0;
            homeBinding.pieChart.clear();
            homeBinding.piechartRv.setVisibility(View.GONE);
            return;
        }
        homeBinding.piechartRv.setVisibility(View.VISIBLE);
        PieDataSet orderDataSet = new PieDataSet(orderEntries, "");


        ArrayList colors = new ArrayList();
        colors.add(Color.parseColor("#97FF78"));
        colors.add(Color.parseColor("#FF7F76"));
        colors.add(Color.parseColor("#6B9CF9"));
        orderDataSet.setColors(colors);

        PieData orderData = new PieData(orderDataSet);
        homeBinding.pieChart.setData(orderData);

        homeBinding.pieChart.setCenterTextRadiusPercent(100);
        homeBinding.pieChart.setDrawEntryLabels(false);
        Legend l = homeBinding.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(false);
        l.setEnabled(false);
        l.setDrawInside(false);
        homeBinding.pieChart.getData().setValueFormatter(new PercentFormatter());
        homeBinding.pieChart.setUsePercentValues(true);
        homeBinding.pieChart.setTouchEnabled(false);
        homeBinding.pieChart.setOnDragListener(null);
        homeBinding.pieChart.getDescription().setEnabled(false);

        homeBinding.pieChart.invalidate();
        ArrayList<String> statusNames = new ArrayList<>();
        statusNames.add("Leads Created");
        statusNames.add("Visited Showroom");
        statusNames.add("Confirm Orders");
        ArrayList colorsNames = new ArrayList();
        colorsNames.add("#97FF78");
        colorsNames.add("#6B9CF9");
        colorsNames.add("#FF7F76");
        GraphColorListAdapter imageAdapter = new GraphColorListAdapter(getActivity(), statusNames, colorsNames, statusTotalCount);
        homeBinding.piechartRv.setHasFixedSize(true);
        homeBinding.piechartRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        homeBinding.piechartRv.setAdapter(imageAdapter);
    }

    private void setLineChartData(List<RecordsItem> records) {
        LGSTOTAL = records.size();
        if (records.size() == 0) {
            homeBinding.lineChart.clear();
            homeBinding.colorStatusRv.setVisibility(View.GONE);
            return;
        } else {
            homeBinding.colorStatusRv.setVisibility(View.VISIBLE);
        }

        List<Entry> webSite = new ArrayList();
        List<Entry> friendRef = new ArrayList();
        List<Entry> instagram = new ArrayList();
        List<Entry> offline = new ArrayList();
        List<Entry> webSignup = new ArrayList();
        List<Entry> googleBusiness = new ArrayList();
        List<Entry> architect = new ArrayList();
        List<Entry> zopimChat = new ArrayList();
        ArrayList<String> statusTotalCount = new ArrayList<>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        int webT = 0, frdT = 0, insT = 0, offT = 0, webST = 0, googleT = 0, arcT = 0, zopT = 0;
        for (int i = 0; i < records.size(); i++) {
            webT += records.get(i).getWebsite();
            frdT += records.get(i).getFriendReferral();
            insT += records.get(i).getInstagram();
            offT += records.get(i).getOffline();
            webST += records.get(i).getWebSignups();
            googleT += records.get(i).getGoogleBusiness();
            arcT += records.get(i).getInteriorArchitectRef();
            zopT += records.get(i).getZopimChat();
            webSite.add(new Entry(i, records.get(i).getWebsite()));
            friendRef.add(new Entry(i, records.get(i).getFriendReferral()));
            instagram.add(new Entry(i, records.get(i).getInstagram()));
            offline.add(new Entry(i, records.get(i).getOffline()));
            webSignup.add(new Entry(i, records.get(i).getWebSignups()));
            googleBusiness.add(new Entry(i, records.get(i).getGoogleBusiness()));
            architect.add(new Entry(i, records.get(i).getInteriorArchitectRef()));
            zopimChat.add(new Entry(i, records.get(i).getZopimChat()));
        }
        statusTotalCount.add(String.valueOf(webT));
        statusTotalCount.add(String.valueOf(frdT));
        statusTotalCount.add(String.valueOf(insT));
        statusTotalCount.add(String.valueOf(offT));
        statusTotalCount.add(String.valueOf(webST));
        statusTotalCount.add(String.valueOf(googleT));
        statusTotalCount.add(String.valueOf(arcT));
        statusTotalCount.add(String.valueOf(zopT));
        for (int i = records.size(); i < 12; i++) {
            webSite.add(new Entry(i, 0));
            friendRef.add(new Entry(i, 0));
            instagram.add(new Entry(i, 0));
            offline.add(new Entry(i, 0));
            webSignup.add(new Entry(i, 0));
            googleBusiness.add(new Entry(i, 0));
            architect.add(new Entry(i, 0));
            zopimChat.add(new Entry(i, 0));
        }

        LineDataSet setWebSite = new LineDataSet(webSite, "Website");
        setWebSite.setColor(Color.parseColor("#BE26A6"));
        setWebSite.setCircleColor(Color.parseColor("#BE26A6"));
        setWebSite.setLineWidth(1f);
        setWebSite.setCircleRadius(3f);
        setWebSite.setDrawCircleHole(false);
        setWebSite.setValueTextSize(0f);
        setWebSite.setDrawFilled(false);
        dataSets.add(setWebSite);
        LineDataSet setFriendRef = new LineDataSet(friendRef, "Friend referral");
        setFriendRef.setColor(Color.parseColor("#178C04"));
        setFriendRef.setCircleColor(Color.parseColor("#178C04"));
        setFriendRef.setLineWidth(1f);
        setFriendRef.setCircleRadius(3f);
        setFriendRef.setDrawCircleHole(false);
        setFriendRef.setValueTextSize(0f);
        setFriendRef.setDrawFilled(false);
        dataSets.add(setFriendRef);
        LineDataSet setInstagram = new LineDataSet(instagram, "Instagram");
        setInstagram.setColor(Color.parseColor("#091D3E"));
        setInstagram.setCircleColor(Color.parseColor("#091D3E"));
        setInstagram.setLineWidth(1f);
        setInstagram.setCircleRadius(3f);
        setInstagram.setDrawCircleHole(false);
        setInstagram.setValueTextSize(0f);
        setInstagram.setDrawFilled(false);
        dataSets.add(setInstagram);
        LineDataSet setOffline = new LineDataSet(offline, "Offline");
        setOffline.setColor(Color.parseColor("#F7941D"));
        setOffline.setCircleColor(Color.parseColor("#F7941D"));
        setOffline.setLineWidth(1f);
        setOffline.setCircleRadius(3f);
        setOffline.setDrawCircleHole(false);
        setOffline.setValueTextSize(0f);
        setOffline.setDrawFilled(false);
        dataSets.add(setOffline);
        LineDataSet setWebSignup = new LineDataSet(webSignup, "Web signup");
        setWebSignup.setColor(Color.parseColor("#C7A175"));
        setWebSignup.setCircleColor(Color.parseColor("#C7A175"));
        setWebSignup.setLineWidth(1f);
        setWebSignup.setCircleRadius(3f);
        setWebSignup.setDrawCircleHole(false);
        setWebSignup.setValueTextSize(0f);
        setWebSignup.setDrawFilled(false);
        dataSets.add(setWebSignup);
        LineDataSet setGoogleBusiness = new LineDataSet(googleBusiness, "Google Business");
        setGoogleBusiness.setColor(Color.parseColor("#66728A"));
        setGoogleBusiness.setCircleColor(Color.parseColor("#66728A"));
        setGoogleBusiness.setLineWidth(1f);
        setGoogleBusiness.setCircleRadius(3f);
        setGoogleBusiness.setDrawCircleHole(false);
        setGoogleBusiness.setValueTextSize(0f);
        setGoogleBusiness.setDrawFilled(false);
        dataSets.add(setGoogleBusiness);
        LineDataSet setArchitect = new LineDataSet(architect, "Interior architect ref");
        setArchitect.setColor(Color.parseColor("#33B9CB"));
        setArchitect.setCircleColor(Color.parseColor("#33B9CB"));
        setArchitect.setLineWidth(1f);
        setArchitect.setCircleRadius(3f);
        setArchitect.setDrawCircleHole(false);
        setArchitect.setValueTextSize(0f);
        setArchitect.setDrawFilled(false);
        dataSets.add(setArchitect);
        LineDataSet setZopimChat = new LineDataSet(zopimChat, "Zopim chat");
        setZopimChat.setColor(Color.parseColor("#A358DF"));
        setZopimChat.setCircleColor(Color.parseColor("#A358DF"));
        setZopimChat.setLineWidth(1f);
        setZopimChat.setCircleRadius(3f);
        setZopimChat.setDrawCircleHole(false);
        setZopimChat.setValueTextSize(0f);
        setZopimChat.setDrawFilled(false);
        dataSets.add(setZopimChat);
        ArrayList<String> statusNames = new ArrayList<>();
        statusNames.add("Website");
        statusNames.add("Friend referral");
        statusNames.add("Instagram");
        statusNames.add("Offline");
        statusNames.add("Web signup");
        statusNames.add("Google Business");
        statusNames.add("Interior architect ref");
        statusNames.add("Zopim chat");
        ArrayList<String> colorsNames = new ArrayList<>();
        colorsNames.add("#BE26A6");
        colorsNames.add("#178C04");
        colorsNames.add("#091D3E");
        colorsNames.add("#F7941D");
        colorsNames.add("#C7A175");
        colorsNames.add("#66728A");
        colorsNames.add("#33B9CB");
        colorsNames.add("#A358DF");
        ValueFormatter formatterX = new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
//                if (value == 0) {
//                    return "";
//                } else {
                return getResources().getStringArray(R.array.TripMonthList)[(int) value];
//                }
            }
        };
        homeBinding.lineChart.getXAxis().setValueFormatter((ValueFormatter) formatterX);
        LineData data = new LineData(dataSets);

//        // set data

        homeBinding.lineChart.getDescription().setEnabled(false);
        homeBinding.lineChart.setData(data);
        homeBinding.lineChart.setPinchZoom(false);
        homeBinding.lineChart.setScaleEnabled(false);
//        homeBinding.lineChart.getXAxis().mAxisRange=10;
        homeBinding.lineChart.getXAxis().setLabelCount(11);
        Legend l = homeBinding.lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setStackSpace(80);
        l.setTextSize(8);
        l.setDrawInside(false);
        l.setEnabled(false);
        homeBinding.lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        homeBinding.lineChart.invalidate();


        GraphColorListAdapter imageAdapter = new GraphColorListAdapter(getActivity(), statusNames, colorsNames, statusTotalCount);
        homeBinding.colorStatusRv.setHasFixedSize(true);
        homeBinding.colorStatusRv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        homeBinding.colorStatusRv.setAdapter(imageAdapter);

    }


    @Override
    public void onClickListner(List<OrderSalesPerson> orderSalesPersonList) {
        for(int i =0 ; i< orderSalesPersonList.size(); i++){
            orderSalesPersonStringList.add(orderSalesPersonList.get(i).getTitle());
        }
        getOrderAnalyticsData(orderSalesPersonStringList);
    }

    SalesPersonSelectDeialog salesPersonSelectDeialog ;
    @Override
    public void onClickSearchDialog() {
        getAllOwners();
        hideSpinnerDropDown(homeBinding.salePersonSp);
        salesPersonSelectDeialog = new SalesPersonSelectDeialog(getActivity(),orderSalesPersonResponseList, salesPersonDialogOnClickListener , new SalesPersonSelectCallbacks() {
            @Override
            public void onProductSelected() {

            }
        });

        salesPersonSelectDeialog.show();
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickDialogListner(List<OrderSalesPerson> orderSalesPersonList) {
//        getAllOwners();
//        salesPersonSelectDeialog.dismiss();
        orderSalesPersonStringList.clear();
        for(int i  = 0; i < orderSalesPersonList.size(); i++){
            OrderSalesPerson person = orderSalesPersonList.get(i);
            orderSalesPersonStringList.add(person.getTitle());
        }
        getOrderAnalyticsData(orderSalesPersonStringList);
    }
}