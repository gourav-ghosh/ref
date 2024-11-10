package com.devstringx.mytylesstockcheck.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.adapter.DetailProductListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityQuoteDetailBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.BillingAddressItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.datamodal.quotationDetails.QuotationDetailsResponse;
import com.devstringx.mytylesstockcheck.datamodal.quotationDetails.Records;
import com.devstringx.mytylesstockcheck.screens.fragments.AddressFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.PdfPOandSOFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.PickUpOrHomeDeliveryFragment;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuoteDetailActivity extends AppCompatActivity implements ResponseListner, DetailProductListAdapter.onRecheckClick {
    ActivityQuoteDetailBinding quoteDetailBinding;

    private List<ProductItem> allProductsList = new ArrayList<>();
    private DetailProductListAdapter productListAdapter;

    public BigDecimal taxableAmount = new BigDecimal("0");
    public BigDecimal finalTaxableAmount = new BigDecimal("0");
    public BigDecimal CGSTAmount = new BigDecimal("0");
    public BigDecimal IGSTAmount = new BigDecimal("0");

    public BigDecimal tranAmount = new BigDecimal("0");
    public BigDecimal unloAmount = new BigDecimal("0");
    public BigDecimal disAmount = new BigDecimal("0");
    private String SELECTEDSTATE = "";
    private Records mRecord;
    private SocketManager manager;
    private boolean isShowRecheckAllOpt = true;
    private MyBroadcastReceiver myReceiver = new MyBroadcastReceiver();
    private boolean isShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_quote_detail);
        manager = SocketManager.getInstance();
        if (!Common.getPermission(QuoteDetailActivity.this, "MQ", "AEFPDQ")) {
            quoteDetailBinding.otherMenu.setVisibility(View.GONE);
        }
        quoteDetailBinding.otherMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.showLog("======isShowRecheckAllOpt====" + isShowRecheckAllOpt);
                Context context = new ContextThemeWrapper(QuoteDetailActivity.this, R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.quote_detail_popup_menu, popupMenu.getMenu());
                if (!Common.getPermission(QuoteDetailActivity.this, "MQ", "AEFPDQ")) {
                    popupMenu.getMenu().removeItem(R.id.edit_quote);
                    popupMenu.getMenu().removeItem(R.id.dispatch);
                    popupMenu.getMenu().removeItem(R.id.print);

                }
                if (quoteDetailBinding.statusTv.getText().toString().equalsIgnoreCase("converted")) {
                    popupMenu.getMenu().removeItem(R.id.edit_quote);
                    popupMenu.getMenu().removeItem(R.id.dispatch);
                    popupMenu.getMenu().removeItem(R.id.recheck_all);
                } else if (quoteDetailBinding.statusTv.getText().toString().equalsIgnoreCase("draft")) {
                    popupMenu.getMenu().removeItem(R.id.dispatch);
                }
                if (isShowRecheckAllOpt) {
                    popupMenu.getMenu().removeItem(R.id.recheck_all);
                }
                if (Common.getPermission(context, "DB", "") &&
                        Common.getPermission(context, "ML", "") &&
                        Common.getPermission(context, "MQ", "") &&
                        Common.getPermission(context, "RZP", "") &&
                        Common.getPermission(context, "ODS", "") &&
                        Common.getPermission(context, "CMS", "") &&
                        Common.getPermission(context, "DWL", "") &&
                        Common.getPermission(context, "AORI", "")) {
                    popupMenu.getMenu().removeItem(R.id.print);
                }
                if (Common.getPermission(context, "DB", "") &&
                        Common.getPermission(context, "ML", "") &&
                        Common.getPermission(context, "MQ", "") &&
                        Common.getPermission(context, "RZP", "") &&
                        Common.getPermission(context, "ODS", "") &&
                                Common.getPermission(context, "CMS", "") &&
                                Common.getPermission(context, "SCE", "") &&
                                Common.getPermission(context, "DWL", "") &&
                                Common.getPermission(context, "AORI", "")) {
                    popupMenu.getMenu().add(1, R.id.print, 3, "Print/Download");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Handle item click
                        switch (menuItem.getItemId()) {
                            case R.id.edit_quote:
                                startActivity();
                                return true;
                            case R.id.dispatch:
                                quoteDetailBinding.titleTv.setText("");
                                quoteDetailBinding.otherMenu.setVisibility(View.GONE);
                                PickUpOrHomeDeliveryFragment fragment = new PickUpOrHomeDeliveryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", mRecord);
                                fragment.setArguments(bundle);
                                Common.addtoBackStackFragment(getSupportFragmentManager(), fragment, R.id.order_view_pager, "pickup or homedelivery fragment");
                                return true;
                            case R.id.print:
                                isShare = false;
                                downloadQuotes(String.valueOf(mRecord.getId()));
                                return true;
                            case R.id.recheck_all:
                                JsonArray jsonArray = new JsonArray();
                                for (int i = 0; i < allProductsList.size(); i++) {
                                    if (allProductsList.get(i).isCanRecheck())
                                        jsonArray.add(allProductsList.get(i).getId());
                                }
                                getReCheckQuotes(jsonArray.toString(), String.valueOf(mRecord.getId()));
                                return true;
                            case R.id.share:
                                isShare = true;
                                downloadQuotes(String.valueOf(mRecord.getId()));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                Menu menu = popupMenu.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    Common.applyFontToMenuItem(QuoteDetailActivity.this, mi);
                }
                // Showing the popup menu
                popupMenu.show();
            }
        });


        quoteDetailBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                    return;
                }

                if (getIntent().getStringExtra("from").equalsIgnoreCase("notification")) {
                    startActivity(new Intent(QuoteDetailActivity.this, DashboardActivity.class));
                }
                finish();
            }
        });

        quoteDetailBinding.termsRl.setVisibility(View.VISIBLE);
        quoteDetailBinding.seemoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quoteDetailBinding.seemoreTv.setVisibility(View.GONE);
                quoteDetailBinding.tilTt.setVisibility(View.GONE);
                quoteDetailBinding.seelessTv.setVisibility(View.VISIBLE);
                quoteDetailBinding.tilEt.setVisibility(View.VISIBLE);
            }
        });

        quoteDetailBinding.seelessTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quoteDetailBinding.seemoreTv.setVisibility(View.VISIBLE);
                quoteDetailBinding.tilTt.setVisibility(View.VISIBLE);
                quoteDetailBinding.termsTT.setText(quoteDetailBinding.termsET.getText().toString());
                quoteDetailBinding.seelessTv.setVisibility(View.GONE);
                quoteDetailBinding.tilEt.setVisibility(View.GONE);
            }
        });

    }

    private void checkPermission() {
        quoteDetailBinding.titleTv.setText("Quote Details");
        if (!Common.getPermission(QuoteDetailActivity.this, "MQ", "AEFPDQ")) {
            quoteDetailBinding.otherMenu.setVisibility(View.GONE);
        } else
            quoteDetailBinding.otherMenu.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        checkPermission();
    }

    @Override
    public void onBackPressed() {

        checkPermission();

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }

        if (getIntent().getStringExtra("from").equalsIgnoreCase("notification")) {
            startActivity(new Intent(QuoteDetailActivity.this, DashboardActivity.class));
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        getQuotes(getIntent().getIntExtra("id", 0));
        manager.onMessageReceived(new SocketManager.MessageReceived() {
            @Override
            public void onMessageReceivedListner(String response) {
                Common.showLog("SOCKET MSG===" + response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject = jsonObject.getJSONObject("quotationProductSocketData");
                            for (int i = 0; i < allProductsList.size(); i++) {
                                if (jsonObject.optString("quotation_product_id").equalsIgnoreCase(allProductsList.get(i).getId())) {
                                    allProductsList.get(i).setStockCheckStatus(jsonObject.optString("stock_check_status"));
                                    allProductsList.get(i).setCanRecheck(jsonObject.optBoolean("can_recheck"));
                                    break;
                                }
                            }
                            productListAdapter.refreshList(allProductsList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    private void startActivity() {
        Intent intent = new Intent(QuoteDetailActivity.this, AddQuoteActivity.class);
        intent.putExtra("data", mRecord);
        startActivity(intent);
    }

    private void getQuotes(int id) {
        String data = "{" +
                "    \"id\":" + id +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, data);
        new NetworkRequest(QuoteDetailActivity.this, this).callWebServices(ServiceMethods.WS_GETALLQUOTES, map, true);
    }

    private void getReCheckQuotes(String id, String quID) {
        String data = "{" +
                "    \"id\":" + id + "," +
                "    \"quotation_id\":" + quID +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, data);
        new NetworkRequest(QuoteDetailActivity.this, this).callWebServices(ServiceMethods.WS_RECHECKQUOTE, map, true);
    }

    private void downloadQuotes(String quID) {
        String data = "{" +
                "    \"quotation_id\":" + quID +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, data);
        Common.showLog(data);
        new NetworkRequest(QuoteDetailActivity.this, this).callWebServices(ServiceMethods.WS_DOWNLOADQUOTE, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLQUOTES) {
                QuotationDetailsResponse detailsResponse = new Gson().fromJson(responseDO.getResponse(), QuotationDetailsResponse.class);
                mRecord = detailsResponse.getData().getRecords();
                showDetails(mRecord);
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_RECHECKQUOTE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(QuoteDetailActivity.this, jsonObject.optString("message"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getQuotes(getIntent().getIntExtra("id", 0));
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_DOWNLOADQUOTE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    String url = jsonObject.optJSONObject("data").optJSONObject("records").optString("url");
                    if (isShare) {
                        File file = downloadFile(url);
                        shareFile(file);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void shareFile(File file) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        Uri outputPdfUri = FileProvider.getUriForFile(this, "com.devstringx.mytylesstockcheck.fileprovider", file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, outputPdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share PDF using.."));
    }

    private File downloadFile(String url) {
        DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(url));
        String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        dmr.setTitle(fileName);
        dmr.setDescription(" ");
        dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName); // Set Your File Name
        dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (!file.exists()) {
            manager.enqueue(dmr);
            return file;
        } else {
            return file;
        }
    }


    private void showDetails(Records records) {
        quoteDetailBinding.quoteNumTv.setText(String.valueOf(records.getQuoteNumber()));
        quoteDetailBinding.customerNameTv.setText(records.getFullName());
        quoteDetailBinding.customerMobileTv.setText(records.getPrimaryPhone());
        quoteDetailBinding.salesExecutiveTv.setText(records.getSalesExecutiveName());
        quoteDetailBinding.statusTv.setText(records.getQuotationStatus());
        quoteDetailBinding.issueDateTv.setText(records.getQuoteDate());
        quoteDetailBinding.poNumberTv.setText(records.getPoNumber());
        quoteDetailBinding.otherReferencesTv.setText(records.getAnyOtherReference());

        Record record = new Record();
        record.setPrimaryPhone(records.getPrimaryPhone());
        BillingAddressItem billingAddressItem = new BillingAddressItem();
        billingAddressItem.setAddress_line_1(records.getBillingAddressLine1());
        billingAddressItem.setAddress_line_2(Common.getData(records.getBillingAddressLine2()));
        billingAddressItem.setBilling_city(records.getBillingCityName());
        billingAddressItem.setBilling_pincode(records.getBillingAddressPincode());
        billingAddressItem.setGst_number(Common.getData(records.getBillingGstNumber()));
        billingAddressItem.setBilling_state(records.getBillingStateName());
        record.setBilling_address(billingAddressItem);
        List<ShippingAddresses> shippingAddress = new ArrayList<>();
        ShippingAddresses addresses = new ShippingAddresses();
        addresses.setAddressLine1(records.getShippingAddressLine1());
        addresses.setAddressLine2(Common.getData(records.getShippingAddressLine2()));
        addresses.setCity(records.getShippingCityName());
        addresses.setPincode(records.getShippingAddressPincode());
        addresses.setShippingState(records.getShippingStateName());
        addresses.setGstNumber(Common.getData(records.getShippingGstNumber()));
        addresses.setLandmark("");
        addresses.setAsDefault(true);
        addresses.setSiteInChargeMobileNumber("");
        shippingAddress.add(addresses);
        record.setShippingAddresses(shippingAddress);
        DashboardViewPager2Adapter adapter = new DashboardViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFrag(new AddressFragment(record, true, false), "address");
        adapter.addFrag(new AddressFragment(record, false, false), "address");
        quoteDetailBinding.addressVp2.setAdapter(adapter);
        new TabLayoutMediator(quoteDetailBinding.tabLayout, quoteDetailBinding.addressVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();

        if (records.getQuotationStatus().equalsIgnoreCase("open")) {
            quoteDetailBinding.statusIv.setImageResource(R.drawable.open_status_dot);
            quoteDetailBinding.statusTv.setTextColor(getColor(R.color.openstatus_color));
        } else if (records.getQuotationStatus().equalsIgnoreCase("draft")) {
            quoteDetailBinding.statusIv.setImageResource(R.drawable.draft_status_dot);
            quoteDetailBinding.statusTv.setTextColor(getColor(R.color.draftstatus_color));
        } else if (records.getQuotationStatus().equalsIgnoreCase("Converted")) {
            quoteDetailBinding.statusIv.setImageResource(R.drawable.converted_status_dot);
            quoteDetailBinding.statusTv.setTextColor(getColor(R.color.convertedstatus_color));
        }
        boolean isAllCharge = true;
        if (!records.isTransportation()) {
            quoteDetailBinding.transChargeLl.setVisibility(View.GONE);
            quoteDetailBinding.transChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        } else {
            isAllCharge = false;
            quoteDetailBinding.transChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if (!records.isUnloadingCharges()) {
            quoteDetailBinding.uploadChargeLl.setVisibility(View.GONE);
            quoteDetailBinding.unloadingChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        } else {
            isAllCharge = false;
            quoteDetailBinding.unloadingChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if (!records.isDiscountCharges()) {
            quoteDetailBinding.discChargeLl.setVisibility(View.GONE);
            quoteDetailBinding.discChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        } else {
            isAllCharge = false;
            quoteDetailBinding.discChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if (isAllCharge) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            params.setMargins(0, 0, 0, 0);
            quoteDetailBinding.allChargeLl.setLayoutParams(params);
            quoteDetailBinding.allChargeLl.setVisibility(View.GONE);
        }
        quoteDetailBinding.transEt.setText(String.valueOf(records.getTransportationCharges()));
        quoteDetailBinding.unloadingEt.setText(String.valueOf(records.getUnloadingCharges()));
        quoteDetailBinding.discEt.setText(String.valueOf(records.getTotalDiscount()));

        quoteDetailBinding.totalTaxableTv.setText(Common.getCurrencyAmount(String.valueOf(records.getTotalTaxable())));
        quoteDetailBinding.finalTotalTv.setText(Common.getCurrencyAmount(String.valueOf(records.getFinalTotal())));
        quoteDetailBinding.termsET.setText(records.getTermsConditions());
        quoteDetailBinding.bankDetails.setText(records.getBankDetails());
        updateList(records);
    }

    public void updateList(Records records) {
        isShowRecheckAllOpt = true;
        allProductsList = records.getProductsItems();
        for (int i = 0; i < allProductsList.size(); i++) {
            if (allProductsList.get(i).isCanRecheck()) {
                isShowRecheckAllOpt = false;
                break;
            }
        }
        productListAdapter = new DetailProductListAdapter(this, allProductsList, this);
        quoteDetailBinding.productRV.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        quoteDetailBinding.productRV.setLayoutManager(layoutManager);
        quoteDetailBinding.productRV.setAdapter(productListAdapter);
        if (allProductsList.size() == 0) {
            quoteDetailBinding.productRV.setVisibility(View.GONE);
            quoteDetailBinding.chargeLl.setVisibility(View.GONE);
            quoteDetailBinding.taxCv.setVisibility(View.GONE);
        } else {
            quoteDetailBinding.productRV.setVisibility(View.VISIBLE);
            quoteDetailBinding.chargeLl.setVisibility(View.VISIBLE);
            quoteDetailBinding.taxCv.setVisibility(View.VISIBLE);
            updateTaxValue(records);
        }

    }

    private void updateTaxValue(Records records) {
        SELECTEDSTATE = records.getBillingStateName();
        Common.showLog("SELECTED STATE=====" + SELECTEDSTATE);
//        if(allProductsList.size()==0)return;
        taxableAmount = new BigDecimal("0");
        CGSTAmount = new BigDecimal("0");
        IGSTAmount = new BigDecimal("0");
        disAmount = new BigDecimal("0");
        for (int i = 0; i < allProductsList.size(); i++) {
            BigDecimal t1 = getProductTaxableAmount(allProductsList.get(i));
            BigDecimal discountP = new BigDecimal("0");
            if (records.isDiscountCharges()) {
                quoteDetailBinding.discLl.setVisibility(View.VISIBLE);
                quoteDetailBinding.ftRl.setVisibility(View.VISIBLE);
                if (!quoteDetailBinding.discEt.getText().toString().equalsIgnoreCase("")) {
                    if (new BigDecimal(quoteDetailBinding.discEt.getText().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        discountP = t1.multiply(new BigDecimal(quoteDetailBinding.discEt.getText().toString().equalsIgnoreCase("") ? "0" : quoteDetailBinding.discEt.getText().toString())).divide(new BigDecimal("100"));
                        BigDecimal t2 = discountP.add(disAmount);
                        disAmount = t2;
                        quoteDetailBinding.discountTv.setText("-" + Common.getCurrencyAmount(String.format("%.2f", disAmount)));
                    } else {
                        quoteDetailBinding.discountTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                    }
                } else {
                    quoteDetailBinding.discountTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                }
            } else {
                quoteDetailBinding.ftRl.setVisibility(View.GONE);
            }
            BigDecimal t3 = t1.add(taxableAmount);
            taxableAmount = t3;

            BigDecimal t4 = t1.subtract(discountP);
            BigDecimal igst = t4.multiply(new BigDecimal(allProductsList.get(i).getGstRate().equalsIgnoreCase("") ? "0" : allProductsList.get(i).getGstRate())).divide(new BigDecimal("100"));

            BigDecimal gst = igst.divide(new BigDecimal(2));
            igst = igst.add(IGSTAmount);
            IGSTAmount = igst;

            gst = gst.add(CGSTAmount);
            CGSTAmount = gst;
        }
        quoteDetailBinding.totalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f", taxableAmount)));
        Common.showLog("====totalTaxableTv======" + String.valueOf(taxableAmount));


        if (records.isTransportation()) {
            quoteDetailBinding.transLl.setVisibility(View.VISIBLE);
            BigDecimal t1 = new BigDecimal(quoteDetailBinding.transEt.getText().toString().equalsIgnoreCase("") ? "0" : quoteDetailBinding.transEt.getText().toString()).multiply(new BigDecimal(Common.TRANSPOTATIONDISC)).divide(new BigDecimal("100"));
            t1 = new BigDecimal(quoteDetailBinding.transEt.getText().toString().equalsIgnoreCase("") ? "0" : quoteDetailBinding.transEt.getText().toString()).subtract(t1);
            quoteDetailBinding.transportationsTv.setText(Common.getCurrencyAmount(String.format("%.2f", t1)));
            tranAmount = t1;
            BigDecimal igst = t1.multiply(new BigDecimal(Common.TRANSPOTATIONGST)).divide(new BigDecimal("100"));
            t1 = igst.add(IGSTAmount);
            IGSTAmount = t1;

            t1 = igst.divide(new BigDecimal(2));
            t1 = t1.add(CGSTAmount);
            CGSTAmount = t1;
        }

        if (records.isUnloadingCharges()) {
            quoteDetailBinding.unloadingLl.setVisibility(View.VISIBLE);
            BigDecimal t1 = new BigDecimal(quoteDetailBinding.unloadingEt.getText().toString().equalsIgnoreCase("") ? "0" : quoteDetailBinding.unloadingEt.getText().toString()).multiply(new BigDecimal(Common.UNLOADINGDISC)).divide(new BigDecimal("100"));
            t1 = new BigDecimal(quoteDetailBinding.unloadingEt.getText().toString().equalsIgnoreCase("") ? "0" : quoteDetailBinding.unloadingEt.getText().toString()).subtract(t1);
            unloAmount = t1;
            quoteDetailBinding.unloadingTv.setText(Common.getCurrencyAmount(String.format("%.2f", t1)));

            BigDecimal igst = t1.multiply(new BigDecimal(Common.UNLOADINGGST)).divide(new BigDecimal("100"));
            t1 = igst.add(IGSTAmount);
            IGSTAmount = t1;

            t1 = igst.divide(new BigDecimal(2));
            t1 = t1.add(CGSTAmount);
            CGSTAmount = t1;
        }

        if (SELECTEDSTATE.equalsIgnoreCase("")) {
            BigDecimal FA = taxableAmount.add(tranAmount);
            quoteDetailBinding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f", FA)));
            quoteDetailBinding.igstLl.setVisibility(View.GONE);
            quoteDetailBinding.cgstLl.setVisibility(View.GONE);
            quoteDetailBinding.sgstLl.setVisibility(View.GONE);
        } else if (SELECTEDSTATE.equalsIgnoreCase("KARNATAKA")) {

            quoteDetailBinding.cgstTv.setText(Common.getCurrencyAmount(String.format("%.2f", CGSTAmount)));
            quoteDetailBinding.sgstTv.setText(Common.getCurrencyAmount(String.format("%.2f", CGSTAmount)));
            quoteDetailBinding.cgstLl.setVisibility(View.VISIBLE);
            quoteDetailBinding.sgstLl.setVisibility(View.VISIBLE);
            quoteDetailBinding.igstLl.setVisibility(View.GONE);
            quoteDetailBinding.igstTv.setText("0");
            finalTaxableAmount = taxableAmount.subtract(disAmount);
            quoteDetailBinding.finalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f", finalTaxableAmount)));
            BigDecimal FA = taxableAmount.add(tranAmount).add(unloAmount).add(CGSTAmount).add(CGSTAmount);
            FA = FA.subtract(disAmount);
            quoteDetailBinding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f", FA)));
        } else {
            quoteDetailBinding.igstTv.setText(Common.getCurrencyAmount(String.format("%.2f", IGSTAmount)));
            quoteDetailBinding.igstLl.setVisibility(View.VISIBLE);
            quoteDetailBinding.cgstLl.setVisibility(View.GONE);
            quoteDetailBinding.sgstLl.setVisibility(View.GONE);
            quoteDetailBinding.sgstTv.setText("0");
            quoteDetailBinding.cgstTv.setText("0");
            finalTaxableAmount = taxableAmount.subtract(disAmount);
            quoteDetailBinding.finalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f", finalTaxableAmount)));
            BigDecimal FA = taxableAmount.add(tranAmount).add(unloAmount).add(IGSTAmount);
            FA = FA.subtract(disAmount);
            quoteDetailBinding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f", FA)));
        }
    }

    @Override
    public BigDecimal getProductTaxableAmount(ProductItem productItem) {
        BigDecimal t1 = new BigDecimal(productItem.getPrice()).multiply(new BigDecimal(productItem.getDiscount().equalsIgnoreCase("") ? "0" : productItem.getDiscount())).divide(new BigDecimal("100"));
        t1 = new BigDecimal(productItem.getPrice()).subtract(t1);
        t1 = new BigDecimal(productItem.getQuantity()).multiply(t1);
        return t1;
    }

    @Override
    public void onReCheckClick(int position) {

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(allProductsList.get(position).getId());
        getReCheckQuotes(jsonArray.toString(), String.valueOf(allProductsList.get(position).getQuotation_id()));
    }
}