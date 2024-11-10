package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.datamodal.salesperson.OrderSalesPerson;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class SalesPersonAutoCompleteListAdapter extends ArrayAdapter<OrderSalesPerson> {
    private Context mContext;
    private SalesPersonOnClickListener clickBtn;
    private List<OrderSalesPerson> listState;
    private List<OrderSalesPerson> selectedItemsList =  new ArrayList<>();
    private List<OrderSalesPerson> allItemsSelectedList = new ArrayList<>();
    private SalesPersonAutoCompleteListAdapter myAdapter;

    public SalesPersonAutoCompleteListAdapter(Context context, int resource, List<OrderSalesPerson> objects, SalesPersonOnClickListener clickBtn) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = objects;
        this.myAdapter = this;
        this.clickBtn = clickBtn;
    }

    public void refreshList(List<OrderSalesPerson> personList) {
        listState = personList;
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.dropdown_sales_person_item_list, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.select_all_textview);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.select_all_checkbox);
            holder.layout = convertView.findViewById(R.id.sales_person_linear_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());
        holder.mCheckBox.setChecked(listState.get(position).isSelected());

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.GONE);
            holder.layout.setBackgroundColor(mContext.getColor(R.color.light_grey));
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (position == 0) clickBtn.onClickSearchDialog();
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        listState.get(position).setSelected(true);
                        selectedItemsList.add(listState.get(position));
                        clickBtn.onClickListner(selectedItemsList);
                    } else if (!isChecked) {
                        listState.get(position).setSelected(false);
                        selectedItemsList.remove(listState.get(position));
                        clickBtn.onClickListner(selectedItemsList);
                    }
            }
        });

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((position == 1)) {
                    if (((CheckBox) v).isChecked()){
                        for (int i = 1; i < listState.size(); i++) {
                            listState.get(i).setSelected(true);
                            holder.mCheckBox.setChecked(listState.get(i).isSelected());
                            allItemsSelectedList.add(listState.get(i));
                        }
                        notifyDataSetChanged();
                        clickBtn.onClickListner(allItemsSelectedList);
                    }
                    if (!((CheckBox) v).isChecked()){
                        for (int i = 1; i < listState.size(); i++) {
                            listState.get(i).setSelected(false);
                            holder.mCheckBox.setChecked(listState.get(i).isSelected());
                            allItemsSelectedList.remove(listState.get(i));
                        }
                        notifyDataSetChanged();
                        clickBtn.onClickListner(allItemsSelectedList);
                    }
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
        private LinearLayout layout;
    }

    public interface SalesPersonOnClickListener{
        void onClickListner(List<OrderSalesPerson> orderSalesPersonList);
        void onClickSearchDialog();
    }
}