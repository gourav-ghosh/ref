package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteListAdapter extends ArrayAdapter<String> {

    private ArrayList<RecordsItem> fullList=new ArrayList<>();
    private boolean isCity=false;

    public CustomAutoCompleteListAdapter(Context context, int resource, int textViewResourceId, List<RecordsItem> objects,boolean isCity) {
        super(context, resource, textViewResourceId);
        fullList = (ArrayList<RecordsItem>) objects;
        this.isCity=isCity;
    }
    public void refreshList(List<RecordsItem> objects){
        fullList = (ArrayList<RecordsItem>) objects;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public String getItem(int position) {
        return isCity?fullList.get(position).getCityName():fullList.get(position).getStateName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public ArrayList<RecordsItem> getFullList() {
        return fullList;
    }
}