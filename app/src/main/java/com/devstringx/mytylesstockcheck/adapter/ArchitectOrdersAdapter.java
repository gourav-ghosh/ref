package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.ArchitectOrdersItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect.OrdersItem;

import java.util.ArrayList;
import java.util.List;

public class ArchitectOrdersAdapter extends RecyclerView.Adapter<ArchitectOrdersAdapter.ViewHolder> {
    private List<OrdersItem> listItems = new ArrayList<>();
    private Context context;
    private OnClick onClick;
//    private ArchitectOrdersBtn clickBtn;
//      , ArchitectOrdersBtn clickBtn
    public ArchitectOrdersAdapter(Context context, List<OrdersItem> listItems,OnClick onClick) {
        this.onClick = onClick;
        this.listItems = listItems;
        this.context = context;
    }
    public void refreshList(List<OrdersItem> ordersItems){
        listItems= ordersItems;
        notifyDataSetChanged();
    }

    public List<OrdersItem> getListItems() {
        return listItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArchitectOrdersItemBinding viewOrdersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.architect_orders_item, parent, false);
        return new ViewHolder(viewOrdersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchitectOrdersAdapter.ViewHolder holder, int position) {
        OrdersItem item=listItems.get(position);
        holder.binding.orderNo.setText(item.getOrderNo());
        holder.binding.orderDate.setText((item.getOrderDate()).replace("-", " "));
        holder.binding.orderAmount.setText(item.getOrderAmount() + " /-");
        holder.binding.architectOrderCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickListener(item.getOrderId().toString());
            }
        });
//      TODO ADD ONCLICKLISTENER TO EACH ORDERS WHICH WILL START ORDER DETAILS ACTIVITY FOR THAT ORDER
//      TODO customise the Status backgrounds according to different status


        holder.binding.orderStatus.setText((item.getOrderStatus()).replace("_", " "));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//
        private ArchitectOrdersItemBinding binding;
        public ViewHolder(ArchitectOrdersItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface OnClick{
        public void onClickListener(String id);
    }
}
