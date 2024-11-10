package com.devstringx.mytylesstockcheck.common;

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

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.SalesPersonSelectAdapter;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.AllProductsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.salesperson.OrderSalesPerson;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatDialog;

public class SalesPersonSelectDeialog extends AppCompatDialog {
    private List<OrderSalesPerson> mList = new ArrayList<>();
    private List<OrderSalesPerson> mListRemovedItems = new ArrayList<>();
    private SalesPersonSelectCallbacks salesPersonSelectCallbacks;
    private ListView listView;
    private SalesPersonSelectAdapter adapter;
    private Context mContext;
    private EditText mEditText;
    private TextView norecord_tv;
    private LinearLayout linearLayout;
    private  SalesPersonSelectAdapter.SalesPersonDialogOnClickListener salesPersonDialogOnClickListener;

    public SalesPersonSelectDeialog(Context context, ArrayList<OrderSalesPerson> recordsItems,
                                    SalesPersonSelectAdapter.SalesPersonDialogOnClickListener salesPersonDialogOnClickListener,SalesPersonSelectCallbacks callbacks) {
        super(context);
        mContext=context;
        this.mList = recordsItems;
        this.mListRemovedItems = recordsItems;
        this.salesPersonDialogOnClickListener = salesPersonDialogOnClickListener;
        this.salesPersonSelectCallbacks = callbacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sales_person_select_picker);
        linearLayout=(LinearLayout)findViewById(R.id.product_ll);
        listView = (ListView) findViewById(R.id.listView);
        mEditText=(EditText)findViewById(R.id.editText);
        InputMethodManager inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                linearLayout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        norecord_tv=(TextView)findViewById(R.id.norecord_tv);
        for(int i = 0; i < mListRemovedItems.size(); i++) {
            if(mListRemovedItems.get(i).getTitle().equalsIgnoreCase("search"))
                mListRemovedItems.remove(i);
            if(mListRemovedItems.get(i).getTitle().equalsIgnoreCase("select all"))
                mListRemovedItems.remove(i);
        }
        Common.orderSalesPersonList = mListRemovedItems;
        adapter = new SalesPersonSelectAdapter(this.getContext(), R.layout.dropdown_sales_person_item_list,Common.orderSalesPersonList,salesPersonDialogOnClickListener);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();

//                OrderSalesPerson recordsItem = adapter.getFullList().get(position);
//                salesPersonSelectCallbacks.onProductSelected();

            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>=3){
                    adapter.getFilter().filter(s);
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

}