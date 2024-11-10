package com.devstringx.mytylesstockcheck.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ProductACAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityAddProductBinding;
import com.devstringx.mytylesstockcheck.databinding.ProductListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.AllProductsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements ResponseListner {
    ActivityAddProductBinding binding;
    private ProductACAdapter adapter;
    private List<ProductItem> list = new ArrayList<>();
    private ArrayList<RecordsItem> fullList = new ArrayList<>();
    private RecordsItem selectedRecord;
    private int CURRENTPOSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);

        getAllProduct();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backScreen();
            }
        });
        adapter = new ProductACAdapter(this, R.layout.product_drop_item, fullList);
        binding.selectProductACTV.setAdapter(adapter);
        binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
        binding.selectProductACTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRecord = adapter.getFullList().get(position);
                setVal(selectedRecord);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.quantityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!binding.rateET.getText().toString().isEmpty())
                    if (!binding.quantityET.getText().toString().isEmpty()) {
                        if (Integer.parseInt(binding.quantityET.getText().toString()) > 0) {
                            double total = Integer.parseInt(binding.quantityET.getText().toString()) * Long.parseLong(binding.rateET.getText().toString());
                            binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(total)));
                        } else {
                            binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
                        }
                    } else {
                        binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.quantityET.getText().toString().isEmpty()) {
                    Common.showToast(AddProductActivity.this, getString(R.string.quantity_error_msg));
                    return;
                }
                saveData();
                backScreen();
            }
        });
        binding.saveTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.quantityET.getText().toString().isEmpty()) {
                    Common.showToast(AddProductActivity.this, getString(R.string.quantity_error_msg));
                    return;
                }
                saveData();
                backScreen();
            }
        });
        binding.nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.quantityET.getText().toString().isEmpty()) {
                    Common.showToast(AddProductActivity.this, getString(R.string.quantity_error_msg));
                    return;
                }
                saveData();
                resetVal();
                binding.nextTv.setVisibility(View.GONE);
                binding.saveTv.setVisibility(View.GONE);
                binding.saveTv2.setVisibility(View.VISIBLE);
            }
        });
        if(getIntent().getStringExtra("action").equalsIgnoreCase("edit")) {
            binding.nextTv.setVisibility(View.GONE);
            binding.saveTv.setVisibility(View.GONE);
            binding.saveTv2.setVisibility(View.VISIBLE);
        }

    }
    private void backScreen(){
        Intent intent=new Intent();
        intent.putExtra("data", (Serializable) list);
        intent.putExtra("action",getIntent().getStringExtra("action"));
        intent.putExtra("pos",getIntent().getIntExtra("pos",0));
        setResult(RESULT_OK,intent);
        finish();
    }

    private void setData() {
        ProductItem item= (ProductItem) getIntent().getSerializableExtra("item");
        for (int i = 0; i < fullList.size(); i++) {
            if (fullList.get(i).getProductId().equalsIgnoreCase(item.getProductId())) {
                binding.selectProductACTV.setSelection(i);
                break;
            }
        }

        binding.descriptionET.setText(item.getDescription());
        binding.quantityET.setText(String.valueOf(item.getQuantity()));
        binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(item.getTotalAmount())));
    }

    private void saveData() {
        ProductItem item = new ProductItem();
        item.setDiscount(selectedRecord.getDiscount());
        item.setDescription(binding.descriptionET.getText().toString());
        item.setPrice(selectedRecord.getFinalPrice());
        item.setProductId(selectedRecord.getProductId());
        item.setHsnCode(selectedRecord.getProductHsnCode());
        item.setGstRate(selectedRecord.getGstRate());
        item.setProductName(selectedRecord.getProductName());
        if(selectedRecord.getProductImages().size()>0){
            if(selectedRecord.getProductImages().get(0).getProductAttachment().size()>0){
                item.setImageUrl(selectedRecord.getProductImages().get(0).getProductAttachment().get(0));
            }
        }
        item.setUnitOfMeasurement(selectedRecord.getUnitOfMeasurement());
        if (!binding.rateET.getText().toString().isEmpty())
            if (!binding.quantityET.getText().toString().isEmpty()) {
                if (Integer.parseInt(binding.quantityET.getText().toString()) > 0) {
                    item.setQuantity(new BigInteger(binding.quantityET.getText().toString()));
                    BigDecimal total = new BigDecimal(binding.quantityET.getText().toString()).multiply(new BigDecimal(binding.rateET.getText().toString()));
                    binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(total)));
                    item.setTotalAmount(String.valueOf(total));
                } else {
                    item.setTotalAmount(String.valueOf(0));
                }
            } else {
                item.setTotalAmount(String.valueOf(0));
            }
        list.add(item);
    }

    private void resetVal() {
        binding.quantityET.setText("");
        binding.descriptionET.setText("");
        binding.selectProductACTV.setSelection(0);
        binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
    }

    private void getAllProduct() {
        String data = "{" +
                "\"pageNumber\": \"1\"," +
                "\"pageSize\": \"2000\"," +
                "\"search\": \"\"," +
                "\"sort\": \"\"," +////'productNameDesc', 'productNameAsc', 'productStatusAsc', 'productStatusDesc', 'productCodeAsc', 'productCodeDesc', 'priceAsc', 'priceDesc', "inventoryAsc", "inventoryDesc"
                "\"brand\": []," +
                "\"delivery_time\": []," +
                "\"product_material_type\": []," +
                "\"product_category\": []," +
                "\"product_uses\": []," +
                "\"product_size\": \"\"," +//"// {" +
//                "//     \"min_product_size\":\"1\"," +
//                "//     \"max_product_size\":\"50\"" +
//                "// }," +
                "\"final_price\": \"\"," +
//                "// {" +
//                "//     \"min_final_price\": 1," +
//                "//     \"max_final_price\": 10000" +
//                "//     }," +
                "\"inventory\": \"\"," +
//                "//     \"min_inventory\": 3," +
//                "//     \"max_inventory\":5" +
//                "// }," +
                "\"mrp\": \"\"," +
//                "// {" +
//                "//     \"min_mrp\": 100," +
//                "//     \"max_mrp\": 10000" +
//                "// }," +
                "\"vendor_company_name\": []," +
                "\"coverage\": \"\"," +
                "\"purchase_rate\": \"\"," +
                "\"gst_rate\": []," +
                "\"product_status\":[]," +
                "\"product_finish\": []," +
                "\"product_quality\": []," +
                "\"country_of_manufacture\": []," +
                "\"unit_of_measurement\": []" +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLPRODUCTINVENTRYDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ALLPRODUCTINVENTRY, map, true);
    }

    private void setVal(RecordsItem item) {
        binding.hsnET.setText(item.getProductHsnCode());
        binding.uomET.setText(item.getUnitOfMeasurement());
        binding.rateET.setText(item.getFinalPrice());
        binding.discountET.setText(item.getDiscount());
        binding.gstET.setText(item.getGstRate() + "%");

        if (!binding.rateET.getText().toString().isEmpty())
            if (!binding.quantityET.getText().toString().isEmpty()) {
                if (Integer.parseInt(binding.quantityET.getText().toString()) > 0) {
                    double total = Integer.parseInt(binding.quantityET.getText().toString()) * Long.parseLong(binding.rateET.getText().toString());
                    binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(total)));
                } else {
                    binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
                }
            } else {
                binding.totalATV.setText(Common.getCurrencyAmount(String.valueOf(0)));
            }

    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_ALLPRODUCTINVENTRY) {
                AllProductsResponse allLeadsRespons = new Gson().fromJson(responseDO.getResponse(), AllProductsResponse.class);
                fullList = (ArrayList<RecordsItem>) allLeadsRespons.getData().getRecords();
                adapter.refreshList(fullList);
                if(getIntent().getStringExtra("action").equalsIgnoreCase("edit")) {
                    setData();
                }
            }
        }
    }

}