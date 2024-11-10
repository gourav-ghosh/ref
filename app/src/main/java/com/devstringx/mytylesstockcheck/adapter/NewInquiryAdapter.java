package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.NewInquiriesItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsRecordsItem;
import com.devstringx.mytylesstockcheck.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class NewInquiryAdapter extends RecyclerView.Adapter<NewInquiryAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private SendResponse sendResponse;
    Context mContext;
    private List<VendorQuotationsRecordsItem> listItems = new ArrayList<>();
    private boolean mIsNotification;

    public NewInquiryAdapter(Context context, OnItemClickListener onItemClickListener, SendResponse sendResponse, boolean isNotifition) {
        this.mContext = context;
        this.sendResponse = sendResponse;
        this.onItemClickListener = onItemClickListener;
        mIsNotification = isNotifition;
    }

    public void refreshList(List<VendorQuotationsRecordsItem> recordsItems) {
        listItems = recordsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewInquiryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewInquiriesItemBinding newInquiriesItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_inquiries_item, parent, false);
        return new ViewHolder(newInquiriesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewInquiryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VendorQuotationsRecordsItem item = listItems.get(position);
        if (mIsNotification) {
//            holder.binding.inquiryNoLl.setVisibility(View.GONE);
//            holder.binding.dateTimeTv.setText("Inquiry No. " + item.getInquiryNo());
            holder.binding.inquiryNumberTv.setText(item.getInquiryNo());
            holder.binding.dateTimeTv.setText(item.getRequestReceivedTime());
            holder.binding.dateTimeTv2.setVisibility(View.GONE);
        } else {
            holder.binding.inquiryNoLl.setVisibility(View.VISIBLE);
            holder.binding.dateTimeTv2.setVisibility(View.GONE);
            holder.binding.dateTimeTv.setText(item.getRequestReceivedTime());
        }
        holder.binding.inquiryNumberTv.setText(item.getInquiryNo());
        holder.binding.tileUom.setText("Qty (" + item.getUnitOfMeasurement() + ")");
        holder.binding.tilesQtyTv.setText(item.getQuantity());
        holder.binding.tileDetail.setText(item.getProductName());
        String code = "<font color=#66728A> Code : " + "</font> <font color=#FF000000> " + item.getProductCode() + "</font>";
        holder.binding.tileCode.setText(Html.fromHtml(code));
        holder.binding.uomTv.setText(item.getUnitOfMeasurement());
        String url = (!item.getProductAttachment().isEmpty()) ? item.getProductAttachment().get(0) : "";
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.tilesIv);
        holder.binding.tileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(mContext)
                        .anchorView(holder.binding.tileDetail)
                        .text(item.getProductName())
                        .gravity(Gravity.TOP)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });
        if (item.getEnquiryStatus().equalsIgnoreCase("New inquiry")) {
            holder.binding.inquiryTypeTag.setBackgroundResource(R.drawable.new_inquiry_label);
            holder.binding.inquiryTypeTag.setText(item.getEnquiryStatus());
        } else {
            holder.binding.inquiryTypeTag.setBackgroundResource(R.drawable.old_inquiry_label);
            holder.binding.inquiryTypeTag.setText(item.getEnquiryStatus());
        }
        if (!Common.getPermission(mContext, "SCE", "RI")) {
            holder.binding.availabilityIv.setVisibility(View.GONE);
            holder.binding.unavailableIv.setVisibility(View.GONE);
        }
        holder.binding.newInquiryCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIsPopupTrue()) {
                    closePopup();
                    notifyDataSetChanged();
                } else if (checkIsTimerTrue()) {

                } else {
                    onItemClickListener.onItemClick(position, item.getId());
                }
            }
        });
        holder.binding.rb1DaysIv.setSelected(true);
        holder.binding.availabilityIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.availabilityLl.setVisibility(View.GONE);
                holder.binding.timerLl.setVisibility(View.VISIBLE);
                item.setTimerRunning(true);
                HashMap<String, Object> map = new HashMap();
                map.put("id", item.getId());
                map.put("stock_check_status", "Available");
                item.setMap(map);
                holder.downTimer = new CountDownTimer(Common.TIMER_TIME08,
                        1000) {
                    public void onTick(long l) {
                        long sec = (l / 1000) % 60;
                        item.setTimeInSecs(sec);
                        holder.binding.timerTv.setText(String.valueOf(sec) + " Sec");
                    }

                    public void onFinish() {
                        if (map != null && !map.isEmpty()) {
                            sendResponse.onClickSendResponse(map);
                            item.setTimerRunning(false);
                        }
                    }
                }.start();
                Common.vendorQuotationsRecordsItemList.add(item);
            }
        });
        holder.binding.rb1DaysIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard((Activity) mContext);
                if (!holder.binding.rb1DaysIv.isSelected()) {
                    holder.binding.rb1DaysIv.setImageResource(R.drawable.ic_checked_radio_button);
                    holder.binding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb1DaysIv.setSelected(true);
                    holder.binding.rb2BoxesIv.setSelected(false);
                    holder.binding.rb3MultipleBatchesIv.setSelected(false);
                }

            }
        });
        holder.binding.rb2BoxesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard((Activity) mContext);
                if (!holder.binding.rb2BoxesIv.isSelected()) {
                    holder.binding.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb2BoxesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    holder.binding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb1DaysIv.setSelected(false);
                    holder.binding.rb2BoxesIv.setSelected(true);
                    holder.binding.rb3MultipleBatchesIv.setSelected(false);
                }
            }
        });
        holder.binding.rb3MultipleBatchesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard((Activity) mContext);
                if (!holder.binding.rb3MultipleBatchesIv.isSelected()) {
                    holder.binding.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    holder.binding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    holder.binding.rb1DaysIv.setSelected(false);
                    holder.binding.rb2BoxesIv.setSelected(false);
                    holder.binding.rb3MultipleBatchesIv.setSelected(true);
                }
            }
        });
        holder.binding.unavailableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.downTimer != null)
                    holder.downTimer.cancel();
                holder.binding.notAvailableResponseLl.setVisibility(View.VISIBLE);
                holder.binding.availabilityLl.setVisibility(View.GONE);
                item.setPopupOpen(true);
                holder.binding.daysNumEt.setText("");
                holder.binding.boxesNumEt.setText("");
                holder.binding.avalabilityLlDividerView.setVisibility(View.INVISIBLE);
            }
        });
        holder.binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard((Activity) mContext);
                if (holder.binding.rb1DaysIv.isSelected()) {
                    if (!holder.binding.daysNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(holder.binding.daysNumEt.getText().toString()) > 0) {
                            Common.showLog("days" + holder.binding.daysNumEt.getText());
                            HashMap<String, Object> map = new HashMap();
                            map.put("id", item.getId());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_in_days", holder.binding.daysNumEt.getText().toString());
                            callTimerFunction(holder, map, item);
                        } else {
                            Common.showToast(mContext, "entered value must be greater than 0");
                            return;
                        }
                    } else {
                        Common.showToast(mContext, "please enter the value of selected item");
                        return;
                    }
                } else if (holder.binding.rb2BoxesIv.isSelected()) {
                    if (!holder.binding.boxesNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(String.valueOf(holder.binding.boxesNumEt.getText())) > 0) {
                            Common.showLog("box" + holder.binding.boxesNumEt.getText());
                            HashMap<String, Object> map = new HashMap();
                            map.put("id", item.getId());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_quantity", holder.binding.boxesNumEt.getText().toString());
                            callTimerFunction(holder, map, item);
                        } else {
                            Common.showToast(mContext, "entered value must be greater than 0");
                            return;
                        }
                    } else {
                        Common.showToast(mContext, "please enter the value of selected item");
                        return;
                    }
                } else if (holder.binding.rb3MultipleBatchesIv.isSelected()) {
                    Common.showLog("multipleBatches");
                    HashMap<String, Object> map = new HashMap();
                    map.put("id", item.getId());
                    map.put("stock_check_status", "Available Multiple Batches");
                    callTimerFunction(holder, map, item);
                }
                holder.binding.rb1DaysIv.setImageResource(R.drawable.ic_checked_radio_button);
                holder.binding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                holder.binding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                holder.binding.rb1DaysIv.setSelected(true);
                holder.binding.rb2BoxesIv.setSelected(false);
                holder.binding.rb3MultipleBatchesIv.setSelected(false);
            }
        });
        holder.binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard((Activity) mContext);
                holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
                holder.binding.availabilityLl.setVisibility(View.VISIBLE);
                item.setPopupOpen(false);
                holder.binding.rb1DaysIv.setImageResource(R.drawable.ic_checked_radio_button);
                holder.binding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                holder.binding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                holder.binding.rb1DaysIv.setSelected(true);
                holder.binding.rb2BoxesIv.setSelected(false);
                holder.binding.rb3MultipleBatchesIv.setSelected(false);
                holder.binding.avalabilityLlDividerView.setVisibility(View.VISIBLE);
            }
        });

        if (item.isTimerRunning()) {
            holder.binding.timerLl.setVisibility(View.VISIBLE);
            holder.binding.availabilityLl.setVisibility(View.GONE);
        } else {
            holder.binding.timerLl.setVisibility(View.GONE);
            holder.binding.availabilityLl.setVisibility(View.VISIBLE);
        }
        if (item.isPopupOpen()) {
            holder.binding.notAvailableResponseLl.setVisibility(View.VISIBLE);
            holder.binding.availabilityLl.setVisibility(View.GONE);
        } else {
            holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
            holder.binding.availabilityLl.setVisibility(View.VISIBLE);
        }
        holder.binding.editResponseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.downTimer != null)
                    holder.downTimer.cancel();
                item.setTimerRunning(false);
                for (int i = 0; i < Common.vendorQuotationsRecordsItemList.size(); i++) {
                    if (item.getInquiryNo().equalsIgnoreCase(Common.vendorQuotationsRecordsItemList.get(i).getInquiryNo()))
                        Common.vendorQuotationsRecordsItemList.get(i).setTimerRunning(false);
                }
                item.setPopupOpen(false);
                holder.binding.timerLl.setVisibility(View.GONE);
                holder.binding.availabilityLl.setVisibility(View.VISIBLE);
                holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
            }
        });

        if(Common.getPermission(mContext,"DB","") && Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"MU","") ||
                Common.getPermission(mContext,"DB","") && Common.getPermission(mContext,"SCE","")) {
            holder.binding.copy.setVisibility(View.INVISIBLE);
            holder.binding.tileCode.setVisibility(View.GONE);
        }
        if(Common.getPermission(mContext,"DB","") &&
                Common.getPermission(mContext,"ML","") &&
                Common.getPermission(mContext,"MU","") &&
                Common.getPermission(mContext,"MQ","") &&
                Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"CMS","") &&
                Common.getPermission(mContext,"DWL","")) {
            holder.binding.copy.setVisibility(View.VISIBLE);
            holder.binding.tileCode.setVisibility(View.VISIBLE);
        }
        if(Common.getPermission(mContext,"DB","") &&
                Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"MU","") &&
                Common.getPermission(mContext,"MQ","") &&
                Common.getPermission(mContext,"RZP","")) {
            holder.binding.copy.setVisibility(View.INVISIBLE);
            holder.binding.tileCode.setVisibility(View.VISIBLE);
        }


        holder.binding.copy.setClickable(true);
        holder.binding.copy.setFocusableInTouchMode(true);
        holder.binding.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Product Name : " + item.getProductName() + " & Quantity : " + item.getQuantity()  + " " +item.getUnitOfMeasurement();
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied name & code", text);
                if (clipboard == null || clip == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext,"Copied Product Name & Code",Toast.LENGTH_SHORT).show();
            }
        });

        if (Common.vendorQuotationsRecordsItemList.size() == 0) return;
        for (int i = 0; i < Common.vendorQuotationsRecordsItemList.size(); i++) {
            if (Common.vendorQuotationsRecordsItemList.get(i).getInquiryNo().equalsIgnoreCase(item.getInquiryNo())) {
                HashMap<String, Object> mapRefresh = Common.vendorQuotationsRecordsItemList.get(i).getMap();
                if (!Common.vendorQuotationsRecordsItemList.get(i).isTimerRunning()) {
                    if (holder.downTimer != null) holder.downTimer.cancel();
                    holder.binding.timerLl.setVisibility(View.GONE);
                    holder.binding.availabilityLl.setVisibility(View.VISIBLE);
                } else {
                    if (holder.downTimer != null) holder.downTimer.cancel();
                    holder.binding.timerLl.setVisibility(View.VISIBLE);
                    holder.binding.availabilityLl.setVisibility(View.GONE);
                    item.setTimerRunning(true);
                    int finalI = i;
                    holder.downTimer = new CountDownTimer(
                            Common.vendorQuotationsRecordsItemList.get(finalI).getTimeInSecs() * 1000,
                            1000) {
                        public void onTick(long l) {
                            long sec = (l / 1000) % 60;
                            Common.vendorQuotationsRecordsItemList.get(finalI).setTimeInSecs(sec);
                            holder.binding.timerTv.setText(String.valueOf(sec) + " Sec");
                        }

                        public void onFinish() {
                            if (mapRefresh != null && !mapRefresh.isEmpty()) {
                                sendResponse.onClickSendResponse(mapRefresh);
                                item.setTimerRunning(false);
                            }
                        }
                    }.start();
                }
            }
        }

    }

    private void setClipboard(Context context, String text) {
        try {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                ClipData data = ClipData.newPlainText("data", text);
                manager.setPrimaryClip(data);
            } else {
                manager.setText(text);
            }
        } catch (Exception e) {
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void closePopup() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isPopupOpen()) {
                listItems.get(i).setPopupOpen(false);
                break;
            }
        }
        Common.hideKeyboard((Activity) mContext);
    }

    private void callTimerFunction(ViewHolder holder, HashMap<String, Object> map, VendorQuotationsRecordsItem item) {
        holder.binding.notAvailableResponseLl.setVisibility(View.GONE);
        holder.binding.availabilityLl.setVisibility(View.GONE);
        holder.binding.timerLl.setVisibility(View.VISIBLE);
        holder.downTimer = new CountDownTimer(Common.TIMER_TIME08,
                1000) {
            public void onTick(long l) {
                long sec = (l / 1000) % 60;
                item.setTimeInSecs(sec);
                holder.binding.timerTv.setText(String.valueOf(sec) + " Sec");
            }

            public void onFinish() {
                if (map != null && !map.isEmpty()) {
                    item.setTimerRunning(false);
                    Common.showLog("maaap===" + map);
                    sendResponse.onClickSendResponse(map);
                }
            }
        }.start();
        item.setTimerRunning(true);
        item.setPopupOpen(false);
        item.setMap(map);
        Common.vendorQuotationsRecordsItemList.add(item);
    }

    public boolean checkIsTimerTrue() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isTimerRunning()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIsPopupTrue() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isPopupOpen()) {
                return true;
            }
        }
        return false;
    }

    public String checkTimerStartedId() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isPopupOpen() || listItems.get(i).isTimerRunning()) {

                return listItems.get(i).getId();
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NewInquiriesItemBinding binding;

        CountDownTimer downTimer;

        public ViewHolder(@NonNull NewInquiriesItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface SendResponse {
        void onClickSendResponse(HashMap<String, Object> map);
    }
}
