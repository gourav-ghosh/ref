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

import androidx.appcompat.app.AppCompatDialog;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.LeadsAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.AllLeadsResponse;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.RecordsItem;
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

public class CustomerPickerDeialog extends AppCompatDialog implements ResponseListner {

    private List<RecordsItem> mList=new ArrayList<>();
    private CustomerPickerCallbacks customerCallbacks;
    private ListView listView;
    private LeadsAutoCompleteListAdapter adapter;
    private Context mContext;
    private EditText mEditText;
    private TextView norecord_tv;
    private LinearLayout linearLayout;

    public CustomerPickerDeialog(Context context, CustomerPickerCallbacks callbacks) {
        super(context);
        mContext=context;
        this.customerCallbacks = callbacks;
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
        ((TextView)findViewById(R.id.title_tv)).setText("Select Customer");
        InputMethodManager inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                linearLayout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        norecord_tv=(TextView)findViewById(R.id.norecord_tv);
        norecord_tv.setText("No Customer Found!");
        adapter = new LeadsAutoCompleteListAdapter(this.getContext(), R.layout.dropdown_item_list, R.id.title_tv,mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();

                RecordsItem recordsItem = adapter.getFullList().get(position);
                customerCallbacks.onCustomerSelected(recordsItem);

            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>=3){
                    getAllLeads(s.toString().trim());
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

    private void getAllLeads(String searchText) {
        Map<String,Object> mapData=new HashMap<>();
        mapData.put("search",searchText);
        Common.showLog("===="+new Gson().toJson(mapData));
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETALLLEADS, new Gson().toJson(mapData));
        new NetworkRequest(mContext, this).callWebServices(ServiceMethods.WS_GETALLLEADS, map, false);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        if (responseDO.getServiceMethods() == ServiceMethods.WS_GETALLLEADS) {
            AllLeadsResponse allLeadsRespons = new Gson().fromJson(responseDO.getResponse(), AllLeadsResponse.class);
            ArrayList<RecordsItem>  fullList = (ArrayList<RecordsItem>) allLeadsRespons.getData().getRecords();
            if(fullList.size()==0){
                norecord_tv.setVisibility(View.VISIBLE);
            }else{
                norecord_tv.setVisibility(View.GONE);
            }
            adapter.refreshList(fullList);
        }
    }
}