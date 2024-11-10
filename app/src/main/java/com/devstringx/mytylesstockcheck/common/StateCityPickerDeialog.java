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
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateCityPickerDeialog extends AppCompatDialog {

    private List<RecordsItem> mList=new ArrayList<>();
    private StateCityPickerCallbacks pickerCallbacks;
    private ListView listView;
    private CustomAutoCompleteListAdapter adapter;
    private Context mContext;
    private EditText mEditText;
    private TextView norecord_tv;
    private LinearLayout linearLayout;
    private boolean mIsCity=false;

    public StateCityPickerDeialog(Context context,List<RecordsItem> list,boolean isCity, StateCityPickerCallbacks callbacks) {
        super(context);
        mContext=context;
        this.pickerCallbacks = callbacks;
        this.mList=list;
        this.mIsCity=isCity;
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
        ((TextView)findViewById(R.id.title_tv)).setText("Select "+(mIsCity?"City":"State"));
        InputMethodManager inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                linearLayout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        norecord_tv=(TextView)findViewById(R.id.norecord_tv);
        norecord_tv.setText("No "+(mIsCity?"City Found!":"State Found!"));
        adapter = new CustomAutoCompleteListAdapter(this.getContext(), R.layout.dropdown_item_list, R.id.title_tv, new ArrayList<>(), mIsCity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                RecordsItem recordsItem = adapter.getFullList().get(position);
                pickerCallbacks.onStateCitySelected(recordsItem);

            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>=3){
                    getAllLeads(s.toString().trim().toLowerCase());
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

    private void getAllLeads(String trim) {
        List<RecordsItem> tempList=new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if(mIsCity) {
                if (mList.get(i).getCityName().toLowerCase().contains(trim)){
                    tempList.add(mList.get(i));
                }
            }else{
                if (mList.get(i).getStateName().toLowerCase().contains(trim)){
                    tempList.add(mList.get(i));
                }
            }
        }
        if(tempList.size()==0){
            norecord_tv.setVisibility(View.VISIBLE);
            adapter.refreshList(new ArrayList<>());
        }else{
            norecord_tv.setVisibility(View.GONE);
            adapter.refreshList(tempList);
        }

    }
}