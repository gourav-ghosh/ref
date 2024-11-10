package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.datamodal.salesperson.OrderSalesPerson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SalesPersonSelectAdapter extends ArrayAdapter<OrderSalesPerson> implements Filterable {

    private List<OrderSalesPerson> fullList = new ArrayList<>();
    private List<OrderSalesPerson> itemsModelListFiltered;
    private LayoutInflater mInflater = null;
    private int viewResourceID;
    private Context mContext;
    private SalesPersonDialogOnClickListener clickBtn;

    private List<OrderSalesPerson> selectedItemsList =  new ArrayList<>();
    private List<OrderSalesPerson> allItemsSelectedList = new ArrayList<>();
    public SalesPersonSelectAdapter(Context context, int resource, List<OrderSalesPerson> fullList, SalesPersonDialogOnClickListener clickBtn) {
        super(context,resource,fullList);
        this.fullList =  fullList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewResourceID = resource;
        this.mContext = context;
        this.clickBtn = clickBtn;
    }

    public void refreshList(List<OrderSalesPerson> fullList) {
        this.fullList = fullList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    public List<OrderSalesPerson> getFullList() {
        return fullList;
    }

//    @Override
//    public RecordsItem getItem(int i) {
//        return fullList.get(i);
//    }


    @Nullable
    @Override
    public OrderSalesPerson getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(viewResourceID,parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.select_all_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(fullList.get(position).getTitle());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(viewResourceID,parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.select_all_textview);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.select_all_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(fullList.get(position).getTitle());

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((position == 0) && holder.title.getText().toString().equalsIgnoreCase("select all")) {
                    if (((CheckBox) v).isChecked()){
                        for (int i = 1; i < fullList.size(); i++) {
                            OrderSalesPerson person = fullList.get(i);
                            person.setSelected(true);
                            holder.mCheckBox.setChecked(true);
                            allItemsSelectedList.add(person);
                        }
                        notifyDataSetChanged();
                        clickBtn.onClickDialogListner(allItemsSelectedList);
                    }
                    if (!((CheckBox) v).isChecked()){
                        for (int i = 1; i < fullList.size(); i++) {
                            OrderSalesPerson person = fullList.get(i);
                            person.setSelected(true);
                            holder.mCheckBox.setChecked(true);
                            allItemsSelectedList.remove(person);
                        }
                        notifyDataSetChanged();
                        clickBtn.onClickDialogListner(allItemsSelectedList);
                    }
                }
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fullList.get(position).setSelected(true);
                    selectedItemsList.add(fullList.get(position));
                    clickBtn.onClickDialogListner(selectedItemsList);
                }else if(!isChecked){
                    fullList.get(position).setSelected(false);
                    selectedItemsList.remove(fullList.get(position));
                    clickBtn.onClickDialogListner(selectedItemsList);
                }

            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public TextView title;
        private CheckBox mCheckBox;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = Common.orderSalesPersonList.size();
                    filterResults.values = Common.orderSalesPersonList;
                }else{
                    List<OrderSalesPerson> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(OrderSalesPerson orderSalesPerson : Common.orderSalesPersonList) {
                        if(orderSalesPerson.getTitle().toLowerCase().contains(searchStr)) {
                            resultsModel.add(orderSalesPerson);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                fullList = (List<OrderSalesPerson>) results.values;
                if(results.count == 2) {
                    OrderSalesPerson orderSalesPerson = new OrderSalesPerson();
                    orderSalesPerson.setTitle("Select All");
                    fullList.add(0, orderSalesPerson);
                }
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public interface SalesPersonDialogOnClickListener{
        void onClickDialogListner(List<OrderSalesPerson> orderSalesPersonList);
    }

}