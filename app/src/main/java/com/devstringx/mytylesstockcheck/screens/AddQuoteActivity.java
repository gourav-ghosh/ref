package com.devstringx.mytylesstockcheck.screens;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.adapter.LeadsAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.adapter.ProductACAdapter;
import com.devstringx.mytylesstockcheck.adapter.ProductListAdapter;
import com.devstringx.mytylesstockcheck.adapter.ShippingAddAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.CustomerPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.CustomerPickerDeialog;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.ProductPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.ProductPickerDeialog;
import com.devstringx.mytylesstockcheck.databinding.ActivityAddQuoteBinding;
import com.devstringx.mytylesstockcheck.databinding.AddProductBottomsheetBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.AllLeadsResponse;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.BillingAddressItem;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.AllProductsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.ProductImagesItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.LeadDetailsData;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.datamodal.quotationDetails.Records;
import com.devstringx.mytylesstockcheck.screens.fragments.AddressFragment;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddQuoteActivity extends AppCompatActivity implements ResponseListner, ProductListAdapter.onEditClick {
    private static final int REQUESTCODE = 2000;
    private static final int SCAN_RESULT = 1111;
    ActivityAddQuoteBinding binding;
    LeadsAutoCompleteListAdapter leadsAdapter;
    private CustomAutoCompleteListAdapter assignOwnerAdapter;
    private ShippingAddAutoCompleteListAdapter shippingAdapter;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    private List<ProductItem> allProductsList = new ArrayList<>();
    private ProductListAdapter productListAdapter;

//    private ProductACAdapter adapter;
    private com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem selectedRecord;

    private ArrayList<com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem> fullList = new ArrayList<>();

    public static int getRequestcode() {
        return REQUESTCODE;
    }

    public boolean isTransCharge, isDiscountCharge, isUnloadingCharge;
    public BigDecimal taxableAmount = new BigDecimal("0");
    public BigDecimal finalTaxableAmount = new BigDecimal("0");
    public BigDecimal CGSTAmount = new BigDecimal("0");
    public BigDecimal IGSTAmount = new BigDecimal("0");

    public BigDecimal tranAmount = new BigDecimal("0");
    public BigDecimal unloAmount = new BigDecimal("0");
    public BigDecimal disAmount = new BigDecimal("0");

    private int SELECTEDCUSTOMERID = 0;
    private int SELECTEDSALESID = 0;
    private String SELECTEDSTATE = "";
    private Record record = null;
    private ShippingAddresses mShippingAddresses = null;
    private BillingAddressItem mBillingAddressItem = null;

    private Records mRecord;
    private AddProductBottomsheetBinding bottomsheetBinding;
    private String fromScreen="";
    private NetworkRequest.DProgressbar loaderUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_quote);

        binding.addProductLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(null);
            }
        });
        binding.addCustomerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromScreen="";
                Intent intent = new Intent(AddQuoteActivity.this, AddLeadActivity.class);
                intent.putExtra("title", "Add Customer");
                startActivity(intent);
            }
        });
        binding.editCustomerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromScreen="";
                if (SELECTEDCUSTOMERID == 0) return;
                Intent intent = new Intent(AddQuoteActivity.this, AddLeadActivity.class);
                intent.putExtra("data", (Serializable) record);
                intent.putExtra("title", "Edit Customer");
                startActivity(intent);
            }
        });
        leadsAdapter = new LeadsAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, new ArrayList<>());
//        binding.selectCustomerACTV.setAdapter(leadsAdapter);
//        binding.selectCustomerACTV.setThreshold(1);
//        binding.selectCustomerACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Common.showLog("======" + leadsAdapter.getFullList().get(position).getLeadId());
//                SELECTEDCUSTOMERID = leadsAdapter.getFullList().get(position).getLeadId();
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("id", SELECTEDCUSTOMERID);
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//                loaderUtils.show();
//                getLeadDetails(jsonObject.toString());
//
//            }
//        });

        binding.selectCustomerACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerPickerDeialog customerPickerDeialog=new CustomerPickerDeialog(AddQuoteActivity.this, new CustomerPickerCallbacks() {
                    @Override
                    public void onCustomerSelected(com.devstringx.mytylesstockcheck.datamodal.allLeads.RecordsItem recordsItem) {
                        SELECTEDCUSTOMERID = recordsItem.getLeadId();
                        binding.selectCustomerACTV.setText(recordsItem.getFullName()+"-"+recordsItem.getLeadPrimaryPhone());
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", SELECTEDCUSTOMERID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        loaderUtils.show();
                        getLeadDetails(jsonObject.toString());
                    }
                });

                customerPickerDeialog.show();
            }
        });
        assignOwnerAdapter = new CustomAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, allOwnersList, true);
        binding.selectSaleExACTV.setAdapter(assignOwnerAdapter);
        binding.selectSaleExACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SELECTEDSALESID = Integer.parseInt(allOwnersList.get(position).getId());
            }
        });
        binding.quoteDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openCalenderForUpcomingDates(AddQuoteActivity.this, binding.quoteDateET);
            }
        });
//        shippingAdapter = new ShippingAddAutoCompleteListAdapter(this, R.layout.dropdown_item_list, R.id.title_tv, new ArrayList<>());
//        binding.shippingAddressACTV.setAdapter(shippingAdapter);


        productListAdapter = new ProductListAdapter(this, allProductsList, this);
        binding.productRV.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        binding.productRV.setLayoutManager(layoutManager);
        binding.productRV.setAdapter(productListAdapter);
        binding.bankRl.setVisibility(View.VISIBLE);
        binding.termsRl.setVisibility(View.VISIBLE);
        binding.titleTv.setText("Add New Quote");
        binding.quoteDateET.setText(Common.getCurrentDate());
        setOnClickListner();
        if(getIntent().getSerializableExtra("data")!=null){
            binding.qdTil.setEnabled(false);
            mRecord= (Records) getIntent().getSerializableExtra("data");
            setData();
            binding.saveTv.setVisibility(View.GONE);
            binding.saveEditQuoteTv.setVisibility(View.VISIBLE);
        }

    }

    private void setData() {
        allProductsList=mRecord.getProductsItems();
        productListAdapter.refreshList(allProductsList);
        binding.selectSaleExACTV.setText(mRecord.getSalesExecutiveName());
        binding.quoteDateET.setText(mRecord.getQuoteDate());
        binding.poNumberET.setText(mRecord.getPoNumber());
        binding.anyotherRefET.setText(mRecord.getAnyOtherReference());
        isTransCharge=mRecord.isTransportation();
        isUnloadingCharge=mRecord.isUnloadingCharges();
        isDiscountCharge=mRecord.isDiscountCharges();
        binding.titleTv.setText("Edit Quote");
        binding.termsET.setText(mRecord.getTermsConditions());
        binding.termsTT.setText(mRecord.getTermsConditions());
        binding.bankDetails.setText(mRecord.getBankDetails());
        binding.selectCustomerACTV.setText(mRecord.getFullName()+"-"+mRecord.getPrimaryPhone());
        SELECTEDCUSTOMERID = mRecord.getCustomerId();
        if (isTransCharge){
            binding.transChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
            binding.transRl.setVisibility(View.VISIBLE);
            binding.transEt.setText(mRecord.getTransportationCharges());
            binding.transLl.setVisibility(View.VISIBLE);
        }
        if (isUnloadingCharge){
            binding.unloadingChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
            binding.unloadingRl.setVisibility(View.VISIBLE);
            binding.unloadingEt.setText(mRecord.getUnloadingCharges());
            binding.unloadingLl.setVisibility(View.VISIBLE);
        }
        if (isDiscountCharge){
            binding.discChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
            binding.discRl.setVisibility(View.VISIBLE);
            binding.discEt.setText(mRecord.getTotalDiscount());
            binding.discLl.setVisibility(View.VISIBLE);
        }

    }

    private void setOnClickListner() {
        loaderUtils=new NetworkRequest.DProgressbar(this);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.transChargeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTransCharge = !isTransCharge;
                if (isTransCharge) {
                    binding.transChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    binding.transRl.setVisibility(View.VISIBLE);
                    binding.transLl.setVisibility(View.VISIBLE);
                } else {
                    binding.transChargeIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                    binding.transRl.setVisibility(View.GONE);
                    binding.transLl.setVisibility(View.GONE);
                }
                updateTaxValue();
            }
        });
        binding.discChargeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDiscountCharge = !isDiscountCharge;
                if (isDiscountCharge) {
                    binding.discChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    binding.discRl.setVisibility(View.VISIBLE);
                    binding.discLl.setVisibility(View.VISIBLE);
                } else {
                    binding.discChargeIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                    binding.discRl.setVisibility(View.GONE);
                    binding.discLl.setVisibility(View.GONE);
                }
                updateTaxValue();
            }
        });
        binding.unloadingChargeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnloadingCharge = !isUnloadingCharge;
                if (isUnloadingCharge) {
                    binding.unloadingChargeIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    binding.unloadingRl.setVisibility(View.VISIBLE);
                    binding.unloadingLl.setVisibility(View.VISIBLE);
                } else {
                    binding.unloadingChargeIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                    binding.unloadingRl.setVisibility(View.GONE);
                    binding.unloadingLl.setVisibility(View.GONE);
                }
                updateTaxValue();
            }
        });


        binding.transChargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.transChargeIv.performClick();
            }
        });
        binding.discChargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.discChargeIv.performClick();
            }
        });
        binding.unloadingChargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.unloadingChargeIv.performClick();
            }
        });

        binding.transEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTaxValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.unloadingEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTaxValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.discEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().startsWith(".")){
                    binding.discEt.setText("0.");
                    binding.discEt.setSelection(2);
                }else if(!s.toString().isEmpty()) {
                    if (new BigDecimal(s.toString()).compareTo(new BigDecimal("100")) > 0) {
                        binding.discEt.setText("100");
                        binding.discEt.setSelection(3);
                    }
                }
                updateTaxValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.saveEditQuoteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuote(false);
            }
        });
        binding.saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuote(false);
            }
        });
        binding.stockCheckTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuote(true);
            }
        });

        binding.seemoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seemoreTv.setVisibility(View.GONE);
                binding.tilTt.setVisibility(View.GONE);
                binding.seelessTv.setVisibility(View.VISIBLE);
                binding.tilEt.setVisibility(View.VISIBLE);
            }
        });
        binding.seelessTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seemoreTv.setVisibility(View.VISIBLE);
                binding.tilTt.setVisibility(View.VISIBLE);
                binding.termsTT.setText(binding.termsET.getText().toString());
                binding.seelessTv.setVisibility(View.GONE);
                binding.tilEt.setVisibility(View.GONE);
            }
        });
    }

    private void saveQuote(boolean b) {
        if (SELECTEDCUSTOMERID == 0) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.select_customer_error));
            return;
        }
        if (SELECTEDSALESID == 0) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.select_sales_error));
            return;
        }
        if (SELECTEDSALESID == 0) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.select_sales_error));
            return;
        }
        if (binding.quoteDateET.getText().toString().equalsIgnoreCase("")) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.select_date_error));
            return;
        }
        if(record!=null) {
            if (record.getBillingAddress() != null) {
                mBillingAddressItem = record.getBillingAddress();
            }
            if (record.getShippingAddresses().size() > 0) {
                for (int i = 0; i < record.getShippingAddresses().size(); i++) {
                    if (record.getShippingAddresses().get(i).isAsDefault()) {
                        mShippingAddresses = record.getShippingAddresses().get(i);
                    }
                }
            }
        }
        if (mBillingAddressItem == null) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.billing_address_error));
            return;
        }
        if (mShippingAddresses == null) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.shipping_address_error));
            return;
        }
        if (allProductsList.size() == 0) {
            Common.showToast(AddQuoteActivity.this, getString(R.string.product_error));
            return;
        }

        if (SELECTEDSTATE.equalsIgnoreCase("KARNATAKA")) {
            IGSTAmount=new BigDecimal(0);
        } else {
            CGSTAmount=new BigDecimal(0);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", SELECTEDCUSTOMERID);
        map.put("stock_check_type", b ? "Stock Check" : "Draft");//[Draft, Stock Check]
        map.put("any_other_reference", binding.anyotherRefET.getText().toString());
        map.put("billing_address_line_1", mBillingAddressItem.address_line_1);
        map.put("billing_address_line_2", mBillingAddressItem.address_line_2);
        map.put("bank_details", binding.bankDetails.getText().toString());
        map.put("billing_address_pincode", mBillingAddressItem.billing_pincode);
        map.put("billing_city_name", mBillingAddressItem.billing_city);
        map.put("billing_gst_number", mBillingAddressItem.gst_number);
        map.put("billing_address_phone", record.getPrimaryPhone());
        map.put("billing_state_name", mBillingAddressItem.billing_state);
        map.put("shipping_address_line_1", mShippingAddresses.getAddressLine1());
        map.put("shipping_address_line_2", mShippingAddresses.getAddressLine2());
        map.put("shipping_address_pincode", mShippingAddresses.getShippingPincode());
        map.put("shipping_gst_number", mShippingAddresses.getGstNumber());
        map.put("shipping_state_name", mShippingAddresses.getShippingState());
        map.put("shipping_city_name", mShippingAddresses.getShippingCity());
        map.put("shipping_address_phone", mShippingAddresses.getSiteInChargeMobileNumber()==null?"":mShippingAddresses.getSiteInChargeMobileNumber());
        map.put("po_number", binding.poNumberET.getText().toString().trim());
        map.put("quote_date", binding.quoteDateET.getText().toString().trim());
        map.put("sales_executive_user", SELECTEDSALESID);
        map.put("transportation_charges", isTransCharge?binding.transEt.getText().toString().equalsIgnoreCase("")?"0":binding.transEt.getText().toString():binding.transEt.getText().toString());
        map.put("total_discount", isDiscountCharge?binding.discEt.getText().toString().equalsIgnoreCase("")?"0":binding.discEt.getText().toString():binding.discEt.getText().toString());
        map.put("unloading_charges", isUnloadingCharge?binding.unloadingEt.getText().toString().equalsIgnoreCase("")?"0":binding.unloadingEt.getText().toString():binding.unloadingEt.getText().toString());

        map.put("is_transportation", isTransCharge);
        map.put("is_discount_charges", isDiscountCharge);
        map.put("is_unloading_charges", isUnloadingCharge);

        map.put("transportation_charges_val", isTransCharge?binding.transportationsTv.getText().toString().equalsIgnoreCase("")?"0.00":binding.transportationsTv.getText().toString().trim().substring(1).replace(",",""):"0.00");
        map.put("total_discount_val",isDiscountCharge?binding.discountTv.getText().toString().equalsIgnoreCase("")?"0.00":binding.discountTv.getText().toString().trim().substring(2).replace(",",""):"0.00");
        map.put("unloading_charges_val", isUnloadingCharge?binding.unloadingTv.getText().toString().equalsIgnoreCase("")?"0.00":binding.unloadingTv.getText().toString().trim().substring(1).replace(",",""):"0.00");


        map.put("final_total", binding.finalTotalTv.getText().toString().substring(1).replace(",",""));
        map.put("total_taxable", String .format("%.2f",taxableAmount));
        map.put("final_taxable", isDiscountCharge? String .format("%.2f",finalTaxableAmount):"0.00");
        map.put("cgst", String .format("%.2f",CGSTAmount));
        map.put("sgst", String.format("%.2f",CGSTAmount));
        map.put("igst", String .format("%.2f",IGSTAmount));
        map.put("terms_conditions", binding.termsET.getText().toString());
        map.put("products_items", allProductsList);
        Common.showLog("+++++++++" + new Gson().toJson(map));
        HashMap<String, Object> map1 = new HashMap<>();

        if(mRecord!=null) {
            map.put("id", mRecord.getId());
            map.put(NKeys.QUOTATIONDATA, new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_EDITQUOTE, map, true);
        }else {
            map.put(NKeys.QUOTATIONDATA, new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_CREATEQUOTATION, map, true);
        }
    }



    public void updateTaxValue() {
        if(record!=null)
            if (record.getBillingAddress()!= null) {
                SELECTEDSTATE = record.getBillingAddress().getBilling_state();
                Common.showLog("SELECTED STATE=====" + SELECTEDSTATE);
            }
//        if(allProductsList.size()==0)return;
        productListAdapter.refreshList(allProductsList);
        if (allProductsList.size() == 0) {
            binding.productRV.setVisibility(View.GONE);
            binding.chargeLl.setVisibility(View.GONE);
            binding.taxCv.setVisibility(View.GONE);
        } else {
            binding.productRV.setVisibility(View.VISIBLE);
            binding.chargeLl.setVisibility(View.VISIBLE);
            binding.taxCv.setVisibility(View.VISIBLE);
        }
        taxableAmount = new BigDecimal("0");
        CGSTAmount = new BigDecimal("0");
        IGSTAmount = new BigDecimal("0");
        disAmount=new BigDecimal("0");
        for (int i = 0; i < allProductsList.size(); i++) {
            BigDecimal t1 = getProductTaxableAmount(allProductsList.get(i));
            allProductsList.get(i).setTotalAmount(String.valueOf(t1));
            BigDecimal discountP=new BigDecimal("0");
            if (isDiscountCharge) {
                binding.ftRl.setVisibility(View.VISIBLE);
                if(!binding.discEt.getText().toString().equalsIgnoreCase("")) {
                    if (new BigDecimal(binding.discEt.getText().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        discountP = t1.multiply(new BigDecimal(binding.discEt.getText().toString().equalsIgnoreCase("") ? "0" : binding.discEt.getText().toString())).divide(new BigDecimal("100"));
                        BigDecimal t2 = discountP.add(disAmount);
                        disAmount=t2;
                        binding.discountTv.setText("-"+Common.getCurrencyAmount(String.format("%.2f", disAmount)));
                    }else{
                        binding.discountTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                    }
                }else{
                    binding.discountTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                }
            }else{
                binding.ftRl.setVisibility(View.GONE);
            }

            BigDecimal t3 = t1.add(taxableAmount);
            taxableAmount = t3;
            BigDecimal t4=t1.subtract(discountP);
            BigDecimal igst = t4.multiply(new BigDecimal(allProductsList.get(i).getGstRate().equalsIgnoreCase("") ? "0" : allProductsList.get(i).getGstRate())).divide(new BigDecimal("100"));

            BigDecimal gst = igst.divide(new BigDecimal(2));
            igst = igst.add(IGSTAmount);
            IGSTAmount = igst;


            gst = gst.add(CGSTAmount);
            CGSTAmount = gst;

            Common.showLog("====IGSTAmount======" + String.valueOf(IGSTAmount));
        }
        binding.totalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f",taxableAmount)));
        Common.showLog("====totalTaxableTv======" + String.valueOf(taxableAmount));


        if (isTransCharge) {
            if(!binding.transEt.getText().toString().equalsIgnoreCase("")) {
                if (new BigDecimal(binding.transEt.getText().toString()).compareTo(BigDecimal.ZERO)>0) {
                    BigDecimal t1 = new BigDecimal(binding.transEt.getText().toString().equalsIgnoreCase("") ? "0" : binding.transEt.getText().toString()).multiply(new BigDecimal(Common.TRANSPOTATIONDISC)).divide(new BigDecimal("100"));
                    t1 = new BigDecimal(binding.transEt.getText().toString().equalsIgnoreCase("") ? "0" : binding.transEt.getText().toString()).subtract(t1);
                    binding.transportationsTv.setText(Common.getCurrencyAmount(String.format("%.2f", t1)));
                    tranAmount = t1;

                    BigDecimal igst  = t1.multiply(new BigDecimal(Common.TRANSPOTATIONGST)).divide(new BigDecimal("100"));
                    t1 = igst.add(IGSTAmount);
                    IGSTAmount = t1;

                    t1 = igst.divide(new BigDecimal(2));
                    t1 = t1.add(CGSTAmount);
                    CGSTAmount = t1;
                    Common.showLog("====IGSTAmount==TRANS====" + String.valueOf(IGSTAmount));
                }else{
                    binding.transportationsTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                }
            }else{
                binding.transportationsTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
            }
        }

        if (isUnloadingCharge) {
            if(!binding.unloadingEt.getText().toString().equalsIgnoreCase("")) {
                if (new BigDecimal(binding.unloadingEt.getText().toString()).compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal t1 = new BigDecimal(binding.unloadingEt.getText().toString().equalsIgnoreCase("") ? "0" : binding.unloadingEt.getText().toString()).multiply(new BigDecimal(Common.UNLOADINGDISC)).divide(new BigDecimal("100"));
                    t1 = new BigDecimal(binding.unloadingEt.getText().toString().equalsIgnoreCase("") ? "0" : binding.unloadingEt.getText().toString()).subtract(t1);
                    unloAmount = t1;
                    binding.unloadingTv.setText(Common.getCurrencyAmount(String.format("%.2f", t1)));

                    BigDecimal igst = t1.multiply(new BigDecimal(Common.UNLOADINGGST)).divide(new BigDecimal("100"));
                    t1 = igst.add(IGSTAmount);
                    IGSTAmount = t1;

                    t1 = igst.divide(new BigDecimal(2));
                    t1 = t1.add(CGSTAmount);
                    CGSTAmount = t1;
                }else{
                    binding.unloadingTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
                }
            }else{
                binding.unloadingTv.setText(Common.getCurrencyAmount(String.format("%.2f", 0.0)));
            }
        }

        if (SELECTEDSTATE.equalsIgnoreCase("")) {
            BigDecimal FA = taxableAmount.add(tranAmount);
            binding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f",FA)));
            binding.igstLl.setVisibility(View.GONE);
            binding.cgstLl.setVisibility(View.GONE);
            binding.sgstLl.setVisibility(View.GONE);
        } else if (SELECTEDSTATE.equalsIgnoreCase("KARNATAKA")) {

            binding.cgstTv.setText(Common.getCurrencyAmount(String.format("%.2f",CGSTAmount)));
            binding.sgstTv.setText(Common.getCurrencyAmount(String.format("%.2f",CGSTAmount)));
            binding.cgstLl.setVisibility(View.VISIBLE);
            binding.sgstLl.setVisibility(View.VISIBLE);
            binding.igstLl.setVisibility(View.GONE);
            binding.igstTv.setText("0");
            finalTaxableAmount=taxableAmount.subtract(disAmount);
            binding.finalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f",finalTaxableAmount)));
            BigDecimal FA = taxableAmount.add(tranAmount).add(unloAmount).add(CGSTAmount).add(CGSTAmount);
            FA = FA.subtract(disAmount);
            binding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f",FA)));
        } else {
            binding.igstTv.setText(Common.getCurrencyAmount(String.format("%.2f",IGSTAmount)));
            binding.igstLl.setVisibility(View.VISIBLE);
            binding.cgstLl.setVisibility(View.GONE);
            binding.sgstLl.setVisibility(View.GONE);
            binding.sgstTv.setText("0");
            binding.cgstTv.setText("0");
            finalTaxableAmount=taxableAmount.subtract(disAmount);
            binding.finalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f",finalTaxableAmount)));
            BigDecimal FA = taxableAmount.add(tranAmount).add(unloAmount).add(IGSTAmount);
            FA = FA.subtract(disAmount);
            binding.finalTotalTv.setText(Common.getCurrencyAmount(String.format("%.2f",FA)));
        }


    }

    public BigDecimal getProductTaxableAmount(ProductItem productItem) {
        BigDecimal t1 = new BigDecimal(productItem.getPrice()).multiply(new BigDecimal(productItem.getDiscount().equalsIgnoreCase("") ? "0" : productItem.getDiscount())).divide(new BigDecimal("100"));
        t1 = new BigDecimal(productItem.getPrice()).subtract(t1);
        t1 = new BigDecimal(productItem.getQuantity()).multiply(t1);
        return t1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fromScreen.isEmpty()) {
            loaderUtils.show();
            getBankDetails();
        }
    }



    private void getAllProduct(String searchText) {

        ArrayList<String> status=new ArrayList<>();
        status.add("active");
        Map<String,Object> mapData=new HashMap<>();
        mapData.put("product_status",status);
        mapData.put("search",searchText);
        Common.showLog("===="+new Gson().toJson(mapData));
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLPRODUCTINVENTRYDATA, new Gson().toJson(mapData));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ALLPRODUCTINVENTRY, map, true);
    }

    private void getLeadDetails(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.LEADDETAILSDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_LEADDETAILS, map, false);
    }

    private void getAllOwners() {
        String data = "{" +
                "    \"pageSize\":\"\"," +
                "    \"role\":\"\"," +
                "    \"user_status\":\"active\"," +
                "    \"search\":\"\"," +
                "    \"screen\":\"Quotation\"," +
                "    \"sort\":\"\"" +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERFORSCREEN, map, false);
    }
    private void getBankDetails() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GET_BANK_DETAILS, null, false);
    }
    private void loadAddress(Record record) {
        this.record = record;
        DashboardViewPager2Adapter adapter = new DashboardViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFrag(new AddressFragment(record, true,true),"address");
        adapter.addFrag(new AddressFragment(record, false,true),"address");
        binding.addressVp2.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.addressVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();
        updateTaxValue();
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_BANK_DETAILS) {
                try {
                    JSONObject jsonObject=new JSONObject(responseDO.getResponse());
                    jsonObject=jsonObject.getJSONObject("data");
                    JSONArray jsonArray=jsonObject.getJSONArray("records");
                    if(jsonArray.length()>0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject=jsonArray.getJSONObject(i);
                            if(jsonObject.getString("detail_type").equalsIgnoreCase("bank_detail")){
                                binding.bankDetails.setText(jsonObject.getString("details"));
                                break;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getAllOwners();
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERFORSCREEN) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    if (mRecord != null) {
                        if (mRecord.getSalesExecutiveUser().equalsIgnoreCase(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()))) {
                            recordsItem.setSelected(true);
                            SELECTEDSALESID=allOwnersData.getData().getRecords().get(i).getId();
                            Common.showLog("SELECTED ASSIGNID===" + allOwnersData.getData().getRecords().get(i).getId());
                        }
                    }
                    allOwnersList.add(recordsItem);
                }
                if(allOwnersList.size()==1){
                    allOwnersList.get(0).setSelected(true);
                    SELECTEDSALESID= Integer.parseInt(allOwnersList.get(0).getId());
                    binding.selectSaleExACTV.setText(allOwnersList.get(0).getCityName());
                }
                assignOwnerAdapter.refreshList(allOwnersList);
//                getAllProduct();
                if(SELECTEDCUSTOMERID!=0){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", SELECTEDCUSTOMERID);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    getLeadDetails(jsonObject.toString());
                }else{
                    loaderUtils.dismiss();
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_LEADDETAILS) {
                LeadDetailsData response = new Gson().fromJson(responseDO.getResponse(), LeadDetailsData.class);
//                loadLeadDetails(response.getData().getRecord());
                binding.addressCV.setVisibility(View.VISIBLE);
                loadAddress(response.getData().getRecord());
                loaderUtils.dismiss();
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ALLPRODUCTINVENTRY) {
                loaderUtils.dismiss();
                AllProductsResponse allLeadsRespons = new Gson().fromJson(responseDO.getResponse(), AllProductsResponse.class);
                fullList = (ArrayList<com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem>) allLeadsRespons.getData().getRecords();
                if(fullList.size()>0){
                    loadProductonBottomSheet(fullList.get(0));
                }

            }else if(responseDO.getServiceMethods() == ServiceMethods.WS_CREATEQUOTATION||
                    responseDO.getServiceMethods() == ServiceMethods.WS_EDITQUOTE){
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(AddQuoteActivity.this,jsonObject.optString("message"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                setResult(RESULT_OK);
                finish();
            }
        }else{
            Common.showToast(AddQuoteActivity.this,responseDO.getResponse());
        }
    }

    private void showBottomSheet(ProductItem recordsItem) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomsheetBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.add_product_bottomsheet, null, false);
        bottomSheetDialog.setContentView(bottomsheetBinding.getRoot());
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        bottomsheetBinding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromScreen="";
                bottomSheetDialog.dismiss();
            }
        });
        if (recordsItem != null) {
            bottomsheetBinding.title.setText("Edit Product");
            bottomsheetBinding.addProdTxt.setText("Edit Product");
        }

        bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(0)));
        bottomsheetBinding.quantityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Common.showLog("======" + new Gson().toJson(selectedRecord));
                if (selectedRecord!=null && !selectedRecord.getFinalPrice().toString().isEmpty())
                    if (!bottomsheetBinding.quantityET.getText().toString().isEmpty()) {
                        if (!new BigInteger(bottomsheetBinding.quantityET.getText().toString()).equals(new BigInteger("0"))) {
                            BigDecimal total = new BigDecimal(bottomsheetBinding.quantityET.getText().toString()).multiply(new BigDecimal(selectedRecord.getFinalPrice().equalsIgnoreCase("") ? "0" : selectedRecord.getFinalPrice()));
                            bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(total)));
                        } else {
                            bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(0)));
                        }
                    } else {
                        bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(0)));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bottomsheetBinding.prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheetBinding.titleTv.performClick();
            }
        });
        bottomsheetBinding.titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductPickerDeialog productPickerDeialog=new ProductPickerDeialog(AddQuoteActivity.this, new ProductPickerCallbacks() {
                    @Override
                    public void onProductSelected(com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem recordsItem) {
                        loadProductonBottomSheet(recordsItem);
                    }
                });

                productPickerDeialog.show();
            }
        });

        bottomsheetBinding.addNewProductLl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                if (selectedRecord != null) {
                    fromScreen = "";
                    if (bottomsheetBinding.quantityET.getText().toString().isEmpty()) {
                        Common.showToast(AddQuoteActivity.this, getString(R.string.quantity_error_msg));
                        return;
                    }
                    if (new BigInteger(bottomsheetBinding.quantityET.getText().toString()).compareTo(BigInteger.ZERO)>0) {
                        ProductItem productItem = new ProductItem();
                        productItem.setProductName(selectedRecord.getProductName());
                        productItem.setProductId(selectedRecord.getProductId());
                        String url = "";
                        if (selectedRecord.getProductImages().size() > 0) {
                            if (selectedRecord.getProductImages().get(0).getProductAttachment().size() > 0) {
                                url = selectedRecord.getProductImages().get(0).getProductAttachment().get(0);
                            }
                        }
                        productItem.setImageUrl(url);
                        productItem.setGstRate(selectedRecord.getGstRate());
                        productItem.setUnitOfMeasurement(selectedRecord.getUnitOfMeasurement());
                        productItem.setHsnCode(selectedRecord.getProductHsnCode());
                        productItem.setPrice(selectedRecord.getFinalPrice());
                        productItem.setDescription(bottomsheetBinding.descriptionET.getText().toString());
                        BigDecimal total = new BigDecimal(bottomsheetBinding.quantityET.getText().toString()).multiply(new BigDecimal(selectedRecord.getFinalPrice().equalsIgnoreCase("") ? "0" : selectedRecord.getFinalPrice()));
                        productItem.setTotalAmount(String.valueOf(total));
                        productItem.setQuantity(new BigInteger(bottomsheetBinding.quantityET.getText().toString()));
                        productItem.setDiscount(selectedRecord.getDiscount());
                        if (recordsItem != null) {
                            for (int i = 0; i < allProductsList.size(); i++) {
                                if (allProductsList.get(i).getProductId().equalsIgnoreCase(recordsItem.getProductId())) {
                                    allProductsList.set(i, productItem);
                                    break;
                                }
                            }
                        } else
                            allProductsList.add(productItem);
                        bottomSheetDialog.dismiss();
                        selectedRecord = null;
                        Common.showLog("ALLLISTDATA==" + new Gson().toJson(allProductsList));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateTaxValue();
                            }
                        }, 1000);
                    }else{
                        Common.showToast(AddQuoteActivity.this, getString(R.string.quantity_error_msg));
                    }

                }
            }
        });
        bottomsheetBinding.scanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromScreen="qrcode";
                startActivityForResult(new Intent(AddQuoteActivity.this,QrcodeScanActivity.class),SCAN_RESULT);
            }
        });
        if (recordsItem != null) {
            bottomsheetBinding.descriptionET.setText(recordsItem.getDescription()==null?"":recordsItem.getDescription());

            Glide.with(AddQuoteActivity.this)
                    .load(recordsItem.getImageUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(bottomsheetBinding.prodImg);
            bottomsheetBinding.titleTv.setText(recordsItem.getProductName());

            com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem updateRecordsItem=new com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem();
            updateRecordsItem.setProductName(recordsItem.getProductName());
            updateRecordsItem.setProductId(recordsItem.getProductId());
            List<ProductImagesItem> productImages=new ArrayList<>();
            ProductImagesItem productImagesItem=new ProductImagesItem();
            List<String> images=new ArrayList<>();
            images.add(recordsItem.getImageUrl());
            productImagesItem.setProductAttachment(images);
            productImages.add(productImagesItem);
            updateRecordsItem.setProductHsnCode(recordsItem.getHsnCode());
            updateRecordsItem.setProductImages(productImages);
            updateRecordsItem.setGstRate(recordsItem.getGstRate());
            updateRecordsItem.setUnitOfMeasurement(recordsItem.getUnitOfMeasurement());
            updateRecordsItem.setFinalPrice(recordsItem.getPrice());
//            updateRecordsItem.setDescription(bottomsheetBinding.descriptionET.getText().toString());
//            BigDecimal total = new BigDecimal(bottomsheetBinding.quantityET.getText().toString()).multiply(new BigDecimal(selectedRecord.getFinalPrice().equalsIgnoreCase("") ? "0" : selectedRecord.getFinalPrice()));
//            updateRecordsItem.setFinalPrice();TotalAmount(String.valueOf(total));
            updateRecordsItem.setProductQuality(recordsItem.getQuantity().toString());
            updateRecordsItem.setDiscount(recordsItem.getDiscount());
            loadProductonBottomSheet(updateRecordsItem);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomsheetBinding.quantityET.setText(String.valueOf(recordsItem.getQuantity()));
                }
            },1000);


        }
        bottomSheetDialog.show();

    }

    private void loadProductonBottomSheet(com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem recordsItem) {
        selectedRecord = recordsItem;
        String url="";
        if(selectedRecord.getProductImages()!=null)
            if(selectedRecord.getProductImages().size()>0) {
                if(selectedRecord.getProductImages().get(0).getProductAttachment().size()>0) {
                    url=selectedRecord.getProductImages().get(0).getProductAttachment().get(0);
                }
            }
        Glide.with(AddQuoteActivity.this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(bottomsheetBinding.prodImg);
        bottomsheetBinding.titleTv.setText(recordsItem.getProductName());
        bottomsheetBinding.quantityET.requestFocus();
        bottomsheetBinding.quantityET.setCursorVisible(true);
        bottomsheetBinding.quantityET.setNextFocusDownId(bottomsheetBinding.descriptionET.getId());
        setVal(bottomsheetBinding, selectedRecord);
    }

    private void setVal(AddProductBottomsheetBinding bottomsheetBinding, com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem item) {
        bottomsheetBinding.uomET.setText(item.getUnitOfMeasurement());
        bottomsheetBinding.rateET.setText(item.getFinalPrice());

        if (!bottomsheetBinding.quantityET.getText().toString().isEmpty()) {
            if (Integer.parseInt(bottomsheetBinding.quantityET.getText().toString()) > 0) {
                BigDecimal total = new BigDecimal(bottomsheetBinding.quantityET.getText().toString()).multiply(new BigDecimal(item.getFinalPrice().equalsIgnoreCase("") ? "0" : item.getFinalPrice()));
                bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(total)));
            } else {
                bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(0)));
            }
        } else {
            bottomsheetBinding.total.setText(Common.getCurrencyAmount(String.valueOf(0)));
        }
    }


    @Override
    public void onEditClick(int position) {
        showBottomSheet(productListAdapter.getListData().get(position));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SCAN_RESULT){
        if(data!=null){
            Common.showLog("=====PRODUCTID===="+data.getStringExtra("product_data"));
            try {
                JSONObject jsonObject = new JSONObject(data.getStringExtra("product_data"));
                data.putExtra("product_data","");
                String productCode= jsonObject.getString("Code");
                getAllProduct(productCode);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        }else if (RESULT_OK == resultCode) {
            fromScreen="";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", SELECTEDCUSTOMERID);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            getLeadDetails(jsonObject.toString());
        }
    }
}