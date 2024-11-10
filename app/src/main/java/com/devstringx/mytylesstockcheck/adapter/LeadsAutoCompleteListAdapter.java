package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import com.devstringx.mytylesstockcheck.datamodal.allLeads.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class LeadsAutoCompleteListAdapter extends ArrayAdapter<String> {

    private ArrayList<RecordsItem> fullList;
    private ArrayList<RecordsItem> allItems;

    public LeadsAutoCompleteListAdapter(Context context, int resource, int textViewResourceId, List<RecordsItem> objects) {
        super(context, resource, textViewResourceId);
        fullList = (ArrayList<RecordsItem>) objects;
        allItems=new ArrayList<>(objects);
    }
    public void refreshList(List<RecordsItem> objects){
        fullList = (ArrayList<RecordsItem>) objects;
        allItems=new ArrayList<>(objects);
        notifyDataSetChanged();
    }

    public ArrayList<RecordsItem> getFullList() {
        return fullList;
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public String getItem(int position) {
        return fullList.get(position).getFullName()+"-"+fullList.get(position).getLeadPrimaryPhone();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return (String) resultValue;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<RecordsItem> departmentsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    if(constraint.length()>=3) {
                        for (RecordsItem department : allItems) {
                            if (department.getFullName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                    department.getLeadPrimaryPhone().contains(constraint.toString().toLowerCase())) {
                                departmentsSuggestion.add(department);
                            }
                        }
                        filterResults.values = departmentsSuggestion;
                        filterResults.count = departmentsSuggestion.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                fullList.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof RecordsItem) {
                            fullList.add((RecordsItem) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    fullList.addAll(new ArrayList<>());
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}