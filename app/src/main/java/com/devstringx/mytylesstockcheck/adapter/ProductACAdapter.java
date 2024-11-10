package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class ProductACAdapter extends ArrayAdapter<RecordsItem> {

    private ArrayList<RecordsItem> fullList=new ArrayList<>();
    private LayoutInflater mInflater = null;
    private int viewResourceID;
    private Context mContext;
    public ProductACAdapter(Context context, int resource, List<RecordsItem> objects) {
        super(context,resource,objects);
        fullList = (ArrayList<RecordsItem>) objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewResourceID=resource;
        mContext=context;
    }

    public void refreshList(List<RecordsItem> objects) {
        fullList = (ArrayList<RecordsItem>) objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    public ArrayList<RecordsItem> getFullList() {
        return fullList;
    }

//    @Override
//    public RecordsItem getItem(int i) {
//        return fullList.get(i);
//    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(viewResourceID,parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.prod_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(fullList.get(position).getProductName());
        String url="";
        if(fullList.get(position).getProductImages()!=null)
            if(fullList.get(position).getProductImages().size()>0) {
                if(fullList.get(position).getProductImages().get(0).getProductAttachment().size()>0) {
                    url=fullList.get(position).getProductImages().get(0).getProductAttachment().get(0);
                }
            }
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(viewResourceID,parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.prod_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(fullList.get(position).getProductName());
        String url="";
        if(fullList.get(position).getProductImages().size()>0) {
            if(fullList.get(position).getProductImages().get(0).getProductAttachment().size()>0) {
                url=fullList.get(position).getProductImages().get(0).getProductAttachment().get(0);
            }
        }
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
        return convertView;
    }

    public static class ViewHolder {
        public TextView title;
        public ImageView imageView;
    }

//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return fruitFilter;
//    }
//    private Filter fruitFilter = new Filter() {
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            RecordsItem record = (RecordsItem) resultValue;
//            return record.getProductName();
//        }
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            if (charSequence != null) {
//                suggestions.clear();
//                for (RecordsItem recordsItem: tempItems) {
//                    if (recordsItem.getProductName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
//                        suggestions.add(recordsItem);
//                    }else if (recordsItem.getProductCode().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
//                        suggestions.add(recordsItem);
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = suggestions;
//                filterResults.count = suggestions.size();
//                return filterResults;
//            } else {
//                return new FilterResults();
//            }
//        }
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            ArrayList<RecordsItem> tempValues = (ArrayList<RecordsItem>) filterResults.values;
//            if (filterResults != null && filterResults.count > 0) {
//                clear();
//                for (RecordsItem itemObj : tempValues) {
//                    add(itemObj);
//                }
//                notifyDataSetChanged();
//            } else {
//                clear();
//                notifyDataSetChanged();
//            }
//        }
//    };
}