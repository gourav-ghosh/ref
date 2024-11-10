package com.devstringx.mytylesstockcheck.common;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ProductACAdapter;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.AllProductsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
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
import java.util.Map;

public class ProductPickerDeialog extends AppCompatDialog implements ResponseListner {

    private List<RecordsItem> mList=new ArrayList<>();
    private ProductPickerCallbacks productCallbacks;
    private ListView listView;
    private ProductACAdapter adapter;
    private Context mContext;
    private EditText mEditText;
    private TextView norecord_tv;
    private LinearLayout linearLayout;

    public ProductPickerDeialog(Context context, ProductPickerCallbacks callbacks) {
        super(context);
        mContext=context;
        this.productCallbacks = callbacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_picker);
        linearLayout=(LinearLayout)findViewById(R.id.product_ll);
        listView = (ListView) findViewById(R.id.listView);
        mEditText=(EditText)findViewById(R.id.editText);
        InputMethodManager inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                linearLayout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        norecord_tv=(TextView)findViewById(R.id.norecord_tv);
        adapter = new ProductACAdapter(this.getContext(), R.layout.product_drop_item,mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();

                RecordsItem recordsItem = adapter.getFullList().get(position);
                productCallbacks.onProductSelected(recordsItem);

            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>=3){
                    getAllProduct(s.toString().trim());
                }else{
                    norecord_tv.setVisibility(View.GONE);
                    adapter.refreshList(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        new NetworkRequest(mContext, this).callWebServices(ServiceMethods.WS_ALLPRODUCTINVENTRY, map, false);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        if (responseDO.getServiceMethods() == ServiceMethods.WS_ALLPRODUCTINVENTRY) {
            AllProductsResponse allLeadsRespons = new Gson().fromJson(responseDO.getResponse(), AllProductsResponse.class);
            ArrayList<com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem>  fullList = (ArrayList<com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem>) allLeadsRespons.getData().getRecords();
            if(fullList.size()==0){
                norecord_tv.setVisibility(View.VISIBLE);
            }else{
                norecord_tv.setVisibility(View.GONE);
            }
            adapter.refreshList(fullList);
        }
    }
}