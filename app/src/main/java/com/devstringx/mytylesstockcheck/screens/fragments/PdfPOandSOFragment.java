package com.devstringx.mytylesstockcheck.screens.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.adapter.DetailProductListAdapter;
import com.devstringx.mytylesstockcheck.adapter.PdfListingAdapter;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentPdfPoAndSoBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.BillingAddressItem;
import com.devstringx.mytylesstockcheck.datamodal.architectDetailsData.MediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.ProductsItemsItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.QuotationDetails;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jsibbold.zoomage.ZoomageView;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PdfPOandSOFragment extends Fragment implements PdfListingAdapter.OnClick , UploadFileAdapter.removeImage, ResponseListner, DetailProductListAdapter.onRecheckClick {
    FragmentPdfPoAndSoBinding binding;
    private OrderDetailActivity orderDetailActivity;
    private RecyclerView dialogRecyclerView;
    private Dialog imageUploadDialog;
    private Dialog poStatusChangeDialog;
    private Records records;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private ArrayList<Images> removedImagesArrayList = new ArrayList<>();
    private ArrayList<String> SELECTEDFILES = new ArrayList<>();
    private ProgressBar progressBar;
    private UploadFileAdapter imageAdapter;
    PdfListingAdapter adapter;
    private boolean isShowRecheckAllOpt=true;
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
    private int poId;
    private String poStatus;
    private int schDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pdf_po_and_so, container, false);
        View view = binding.getRoot();
        records = ((OrderDetailActivity) getActivity()).records;
        binding.paymentDetailsRb.setChecked(true);
        showQuotationDetails(records.getQuotationDetails());
        loadData();
        binding.uploadPoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageUploadDialog("Upload PO","Pdf of PO");
            }
        });
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                loadData();
            }
        });
        return view;
    }

    private void showImageUploadDialog(String title, String fieldTitle) {
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
        fieldHeader.setText(fieldTitle);
        formatTV.setText(R.string.upload_pdf_format);
        selectImg.setText(R.string.select_pdf_to_upload);
        setupDialogPdfAdapter();
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
                callUploadImageApi();
            }
        });

        imageUploadDialog.show();
    }

    private void SelectImageLauncher() {
        ArrayList<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("application/pdf");
        captureImageResultLauncher.launch(
                new FilePicker.Builder(getContext())
                        .pickDocumentFileBuild(
                                new DocumentFilePickerConfig(
                                        null,
                                        null,
                                        false,
                                        1,
                                        mMimeTypesList,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                        )
        );
    }

    private void callUploadImageApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", getActivity().getIntent().getStringExtra("orderId").toString());
        map.put("file", SELECTEDFILES);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_UPLOADPOFILE, map, true);
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

    private void setupDialogPdfAdapter() {
        imageAdapter = new UploadFileAdapter(getContext(), imagesArrayList, this);
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRecyclerView.setAdapter(imageAdapter);
    }

    private void loadData() {
        if (binding.paymentDetailsRb.isChecked()) {
            binding.soPdfRL.setVisibility(View.VISIBLE);
            binding.poPdfRL.setVisibility(View.GONE);
            binding.uploadPoTV.setVisibility(View.GONE);
            if (!records.getDocuments().getSO().isEmpty() && records.getDocuments() != null) {
                binding.web.setVisibility(View.VISIBLE);
                binding.quotationDetails.setVisibility(View.GONE);
                binding.noPdfTv.setVisibility(View.GONE);
                binding.web.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        // Show loader when page starts loading
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        binding.web.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

                binding.web.getSettings().setSupportZoom(true);
                binding.web.getSettings().setJavaScriptEnabled(true);
                binding.web.getSettings().setDatabaseEnabled(true);
                String pdfUrl = records.getDocuments().getSO().get(0).toString();
                Common.showLog("pdfUrl" + pdfUrl);
                String googleDocsUrl = null;
                try {
                    googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(pdfUrl, "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                binding.web.loadUrl(googleDocsUrl);
            } else if(records.getQuotationDetails() != null){
                binding.web.setVisibility(View.GONE);
                binding.quotationDetails.setVisibility(View.VISIBLE);
                binding.noPdfTv.setVisibility(View.GONE);
            }
            else {
                binding.web.setVisibility(View.GONE);
                binding.quotationDetails.setVisibility(View.GONE);
                binding.noPdfTv.setVisibility(View.VISIBLE);
            }
        } else {
            binding.soPdfRL.setVisibility(View.GONE);
            binding.poPdfRL.setVisibility(View.VISIBLE);
            if (new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("8"))
                binding.uploadPoTV.setVisibility(View.VISIBLE);
            else binding.uploadPoTV.setVisibility(View.GONE);
            if (!records.getDocuments().getPO().isEmpty()) {
                binding.pdfRV.setVisibility(View.VISIBLE);
                binding.noPOpdfTv.setVisibility(View.GONE);
                setupAdapter();
            } else {
                binding.noPOpdfTv.setVisibility(View.VISIBLE);
                binding.pdfRV.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void showQuotationDetails(QuotationDetails records) {
        binding.quoteNumTv.setText(String.valueOf(records.getQuoteNumber()));
        binding.customerNameTv.setText(records.getFullName());
        binding.customerMobileTv.setText(records.getPrimaryPhone());
        binding.salesExecutiveTv.setText(records.getSalesExecutiveName());
        binding.statusTv.setText(records.getQuotationStatus());
        binding.issueDateTv.setText(records.getQuoteDate());
        binding.poNumberTv.setText(records.getPoNumber());
        binding.otherReferencesTv.setText(records.getAnyOtherReference());

        Record record=new Record();
        record.setPrimaryPhone(records.getPrimaryPhone());
        BillingAddressItem billingAddressItem=new BillingAddressItem();
        billingAddressItem.setAddress_line_1(records.getBillingAddressLine1());
        billingAddressItem.setAddress_line_2(Common.getData(records.getBillingAddressLine2()));
        billingAddressItem.setBilling_city(records.getBillingCityName());
        billingAddressItem.setBilling_pincode(records.getBillingAddressPincode());
        billingAddressItem.setGst_number(Common.getData(records.getBillingGstNumber()));
        billingAddressItem.setBilling_state(records.getBillingStateName());
        record.setBilling_address(billingAddressItem);
        List<ShippingAddresses> shippingAddress=new ArrayList<>();
        ShippingAddresses addresses=new ShippingAddresses();
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
        DashboardViewPager2Adapter adapter = new DashboardViewPager2Adapter(getChildFragmentManager(), getLifecycle());
        adapter.addFrag(new AddressFragment(record, true,false),"address");
        adapter.addFrag(new AddressFragment(record, false,false),"address");
        binding.addressVp2.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.addressVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();

        if(records.getQuotationStatus().equalsIgnoreCase("open")){
            binding.statusIv.setImageResource(R.drawable.open_status_dot);
            binding.statusTv.setTextColor(R.color.openstatus_color);
        }else if(records.getQuotationStatus().equalsIgnoreCase("draft")){
            binding.statusIv.setImageResource(R.drawable.draft_status_dot);
            binding.statusTv.setTextColor(R.color.draftstatus_color);
        }else if(records.getQuotationStatus().equalsIgnoreCase("Converted")){
            binding.statusIv.setImageResource(R.drawable.converted_status_dot);
            binding.statusTv.setTextColor(R.color.convertedstatus_color);
        }
        boolean isAllCharge=true;
        if(!records.isTransportation()){
            binding.transChargeLl.setVisibility(View.GONE);
            binding.transChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        }else{
            isAllCharge=false;
            binding.transChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if(!records.isUnloadingCharges()){
            binding.uploadChargeLl.setVisibility(View.GONE);
            binding.unloadingChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        }else{
            isAllCharge=false;
            binding.unloadingChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if(!records.isDiscountCharges()){
            binding.discChargeLl.setVisibility(View.GONE);
            binding.discChargeIv.setImageResource(R.drawable.orange_checkbox_unselected);
        }else{
            isAllCharge=false;
            binding.discChargeIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if(isAllCharge){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,0);
            params.setMargins(0,0,0,0);
            binding.allChargeLl.setLayoutParams(params);
            binding.allChargeLl.setVisibility(View.GONE);
        }
        binding.transEt.setText(String.valueOf(records.getTransportationCharges()));
        binding.unloadingEt.setText(String.valueOf(records.getUnloadingCharges()));
        binding.discEt.setText(String.valueOf(records.getTotalDiscount()));

        binding.totalTaxableTv.setText(Common.getCurrencyAmount(String.valueOf(records.getTotalTaxable())));
        binding.finalTotalTv.setText(Common.getCurrencyAmount(String.valueOf(records.getFinalTotal())));
        updateList(records);
    }

    private void updateList(QuotationDetails records) {
        isShowRecheckAllOpt=true;
        allProductsList=records.getProductsItems();
        for (int i = 0; i < allProductsList.size(); i++) {
            if(allProductsList.get(i).isCanRecheck()){
                isShowRecheckAllOpt=false;
                break;
            }
        }
        productListAdapter = new DetailProductListAdapter(getContext(), allProductsList, this);
        binding.productRV.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        binding.productRV.setLayoutManager(layoutManager);
        binding.productRV.setAdapter(productListAdapter);
        if (allProductsList.size() == 0) {
            binding.productRV.setVisibility(View.GONE);
            binding.chargeLl.setVisibility(View.GONE);
            binding.taxCv.setVisibility(View.GONE);
        } else {
            binding.productRV.setVisibility(View.VISIBLE);
            binding.chargeLl.setVisibility(View.VISIBLE);
            binding.taxCv.setVisibility(View.VISIBLE);
            updateTaxValue(records);
        }
    }

    private void updateTaxValue(QuotationDetails records) {
        SELECTEDSTATE = records.getBillingStateName();
        Common.showLog("SELECTED STATE=====" + SELECTEDSTATE);
//        if(allProductsList.size()==0)return;
        taxableAmount = new BigDecimal("0");
        CGSTAmount = new BigDecimal("0");
        IGSTAmount = new BigDecimal("0");
        disAmount=new BigDecimal("0");
        for (int i = 0; i < allProductsList.size(); i++) {
            BigDecimal t1 = getProductTaxableAmount(allProductsList.get(i));
            BigDecimal discountP=new BigDecimal("0");
            if (records.isDiscountCharges()) {
                binding.discLl.setVisibility(View.VISIBLE);
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
        }
        binding.totalTaxableTv.setText(Common.getCurrencyAmount(String.format("%.2f",taxableAmount)));
        Common.showLog("====totalTaxableTv======" + String.valueOf(taxableAmount));


        if (records.isTransportation()) {
            binding.transLl.setVisibility(View.VISIBLE);
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
        }

        if (records.isUnloadingCharges()) {
            binding.unloadingLl.setVisibility(View.VISIBLE);
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

    @Override
    public BigDecimal getProductTaxableAmount(ProductItem productItem) {
        BigDecimal t1 = new BigDecimal(productItem.getPrice()).multiply(new BigDecimal(productItem.getDiscount().equalsIgnoreCase("") ? "0" : productItem.getDiscount())).divide(new BigDecimal("100"));
        t1 = new BigDecimal(productItem.getPrice()).subtract(t1);
        t1 = new BigDecimal(productItem.getQuantity()).multiply(t1);
        return t1;
    }

    private void setupAdapter() {
        adapter = new PdfListingAdapter(getContext(), records.getDocuments().getPO(), this);
        binding.pdfRV.setHasFixedSize(true);
        binding.pdfRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.pdfRV.setAdapter(adapter);
    }

    @Override
    public void onClickListener(String link) {
        fileViewDialog(link);
    }
    @Override
    public void onStatusClickListener(int id, String name) {
        openPOStatusChangeDialog(id, name);
    }

    private void openPOStatusChangeDialog(int id, String name) {
        poId = id;
        poStatusChangeDialog = new Dialog(getContext());
        poStatusChangeDialog.setContentView(R.layout.po_status_change_dialog);
        poStatusChangeDialog.setCancelable(true);
        ImageView close = poStatusChangeDialog.findViewById(R.id.poStatusClose);
        AppCompatEditText poNameET = poStatusChangeDialog.findViewById(R.id.poNameET);
        LinearLayout statusReady = poStatusChangeDialog.findViewById(R.id.statusReady);
        LinearLayout statusAgainstOrder = poStatusChangeDialog.findViewById(R.id.statusAgainstOrder);
        ImageView statusReadyRB = poStatusChangeDialog.findViewById(R.id.statusReadyRB);
        ImageView statusAgainstOrderRB = poStatusChangeDialog.findViewById(R.id.statusAgainstOrderRB);
        TextInputLayout expDate = poStatusChangeDialog.findViewById(R.id.expDate);
        AppCompatButton poStatusUpdate = poStatusChangeDialog.findViewById(R.id.poStatusUpdate);
        poNameET.setText(name);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poStatusChangeDialog.dismiss();
            }
        });
        statusReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusAgainstOrderRB.setImageResource(R.drawable.ic_unchecked_green_radio_button);
                statusReadyRB.setImageResource(R.drawable.ic_checked_green_radio_button);
                expDate.setVisibility(View.GONE);
                poStatus = "ready";
            }
        });
        statusAgainstOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusAgainstOrderRB.setImageResource(R.drawable.ic_checked_green_radio_button);
                statusReadyRB.setImageResource(R.drawable.ic_unchecked_green_radio_button);
                expDate.setVisibility(View.VISIBLE);
                poStatus = "against_order";
            }
        });
        poStatusUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPOStatusChangeAPI();
            }
        });
        poStatusChangeDialog.show();
    }

    private void callPOStatusChangeAPI() {
        AppCompatEditText scheduleDate = poStatusChangeDialog.findViewById(R.id.scheduleDate);
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map.put("order_id", getActivity().getIntent().getStringExtra("orderId").toString());
        map.put("poID", poId);
        map.put("poStatus", poStatus);
        if(poStatus.equalsIgnoreCase("against_order"))
            map.put("scheduleDate", Integer.parseInt(scheduleDate.getText().toString()));
        map1.put(NKeys.POSTATUSCHANGE, new Gson().toJson(map));
        new NetworkRequest(getContext(), this::onResponseReceived).callWebServices(ServiceMethods.WS_POSTATUSCHANGE, map1, true);
    }

    @Override
    public void onDownloadListener(String link) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
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

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADPOFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        imageUploadDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((OrderDetailActivity) getActivity()).getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_POSTATUSCHANGE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        poStatusChangeDialog.dismiss();
                        Common.showToast(getContext(), jsonObject.optString("message").toString());
                        ((OrderDetailActivity) getActivity()).getOrderDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void getReCheckQuotes(String id, String quID){
        String data = "{" +
                "    \"id\":" +id+","+
                "    \"quotation_id\":" +quID+
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.QUOTATIONDATA, data);
        new NetworkRequest(getContext(),this).callWebServices(ServiceMethods.WS_RECHECKQUOTE,map,true);
    }
    @Override
    public void onReCheckClick(int position) {

        JsonArray jsonArray=new JsonArray();
        jsonArray.add(allProductsList.get(position).getId());
        getReCheckQuotes(jsonArray.toString(),String.valueOf(allProductsList.get(position).getQuotation_id()));
    }
    @Override
    public void onMarkArrangeClickListener(int id, String name) {
    }
}