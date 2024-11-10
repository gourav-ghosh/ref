package com.devstringx.mytylesstockcheck.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.RazorpayListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.razorpay.RazorpayRecordsItem;

import java.util.ArrayList;
import java.util.List;

public class RazorPayListItemAdapter extends RecyclerView.Adapter<RazorPayListItemAdapter.ViewHolder> {
    private List<RazorpayRecordsItem> listItems = new ArrayList<>();
    private RazorpayBtn clickBtn;
    private Context context;

    public RazorPayListItemAdapter(RazorpayBtn clickBtn, Context context) {
        this.context = context;
        this.clickBtn = clickBtn;
    }

    public void refreshList(List<RazorpayRecordsItem> recordsItems) {
        listItems = recordsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RazorPayListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RazorpayListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.razorpay_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RazorPayListItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RazorpayRecordsItem item = listItems.get(position);
        holder.binding.link.setText(item.getGeneratedLink());
        holder.binding.nameET.setText(item.getCreatedByFirstName() + " " + item.getCreatedByLastName());
        holder.binding.dateTimeET.setText(item.getCreatedDate());
        holder.binding.itemAmountET.setText("₹ "+item.getOrderAmount() + "/-");
        if (!Common.getPermission(context, "RZP", "DRZ")) {
            holder.binding.delete.setVisibility(View.GONE);
        }
        if (item.getPayment().equalsIgnoreCase("true")) {
            holder.binding.itemTag.setText("Paid");
            holder.binding.itemTag.setBackgroundResource(R.drawable.green_round_bg);
        } else {
            holder.binding.itemTag.setText("Pending");
            holder.binding.itemTag.setBackgroundResource(R.drawable.orange_round_bg);
        }
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onBtnClickListner(position, item.getPaymentId());
            }
        });
        holder.binding.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(item.getGeneratedLink());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", item.getGeneratedLink());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        holder.binding.grayWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(context, "Dear Customer,\n" +
                        "\n" +
                        "MYTYLES is requesting Rs." + item.getOrderAmount() + "/- towards your purchase. To make the payment please use this link: " + item.getGeneratedLink() + "\n" +
                        "\n" +
                        "Thank you");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RazorpayListItemBinding binding;

        public ViewHolder(@NonNull RazorpayListItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface RazorpayBtn {
        void onBtnClickListner(int position, String payment_id);
    }
}
