package com.devstringx.mytylesstockcheck.screens.vendor;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityInquiryDetailBinding;
import com.devstringx.mytylesstockcheck.databinding.NotAvailableResponseDialogBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.VendorQuoteDetailRecords;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.VendorQuoteDetailResponse;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.NotificationListActivity;
import com.devstringx.mytylesstockcheck.steperView.Step1Test;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class InquiryDetailActivity extends AppCompatActivity implements ResponseListner {
    ActivityInquiryDetailBinding inquiryDetailBinding;

    int myProgress = 0;
    ProgressBar progressBarView;
    TextView tv_time;
    int progress;
    CountDownTimer countDownTimer;
    int endTime = 8;
    private boolean historyLayoutOpen = false;
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inquiryDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_inquiry_detail);
        if (getIntent().getStringExtra("inquiry_id") != null)
            getAllInquiries(getIntent().getStringExtra("inquiry_id"));
        inquiryDetailBinding.historyDropdownIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!historyLayoutOpen) {
                    inquiryDetailBinding.inquiryHistoryLl.setVisibility(View.VISIBLE);
                    inquiryDetailBinding.historyDropdownIv.setImageResource(R.drawable.ic_up_arrow);
                    historyLayoutOpen = true;

                } else {
                    inquiryDetailBinding.inquiryHistoryLl.setVisibility(View.GONE);
                    inquiryDetailBinding.historyDropdownIv.setImageResource(R.drawable.ic_dropdown);
                    historyLayoutOpen = false;
                }
            }
        });
        inquiryDetailBinding.notAvailableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotAvailableResponseDialog(InquiryDetailActivity.this);
            }
        });
        inquiryDetailBinding.availableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", getIntent().getStringExtra("inquiry_id"));
                map.put("stock_check_status", "Available");
                showCountDownDialog(map);
            }
        });
        inquiryDetailBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra("background",false)) {
                    Intent intent=new Intent(InquiryDetailActivity.this, DashboardActivity.class);
                    intent.putExtra("background",true);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra("background",false)) {
            Intent intent=new Intent(InquiryDetailActivity.this, DashboardActivity.class);
            intent.putExtra("background",true);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }


    }

    private void getAllInquiries(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("id", id);
        map.put("sort", "");
        map.put("action", "");
        map.put("quantity", "");
        map.put("enquiry_status", "");
        HashMap<String, Object> date = new HashMap<>();
        date.put("type","");
        date.put("startDate","");
        date.put("endDate","");
        map.put("dateRange",date);
        map.put("enquiry_tab", "");
        map.put(NKeys.GETVENDORQUOTATIONS,new Gson().toJson(map));
        new NetworkRequest(this,this).callWebServices(ServiceMethods.WS_GETVENDORQUOTATIONS, map, true);
    }
    private void updateInquiryStatus(HashMap<String, Object> map) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.UPDATEQUOTATIONPRODUCTSTATUS,new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS, map1, true);
    }
    private void setData(VendorQuoteDetailRecords recordsItem) {
        inquiryDetailBinding.inquiryNumberTv.setText(recordsItem.getEnquiryNo());
        inquiryDetailBinding.tileNameTv.setText(recordsItem.getProductName());
        inquiryDetailBinding.tileNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(InquiryDetailActivity.this)
                        .anchorView(inquiryDetailBinding.tileNameTv)
                        .text(recordsItem.getProductName())
                        .gravity(Gravity.TOP)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });

        inquiryDetailBinding.quantityRequiredTv.setText(recordsItem.getQuantity()+" ("+recordsItem.getUnitOfMeasurement()+")");
        inquiryDetailBinding.requestReceivedAtTv.setText(recordsItem.getRequestReceivedTime());
        inquiryDetailBinding.inquiryTypeTv.setText(recordsItem.getEnquiryStatus());
        if (recordsItem.getEnquiryStatus().equalsIgnoreCase("New inquiry")) {
            inquiryDetailBinding.inquiryTypeTv.setBackgroundResource(R.drawable.new_inquiry_left_cut_label);
        }else {
            inquiryDetailBinding.inquiryTypeTv.setBackgroundResource(R.drawable.old_inquiry_left_cut_label);
        }
        if (recordsItem.getStockCheckStatus().equalsIgnoreCase("Request Sent")) {
            inquiryDetailBinding.availabilityLl.setVisibility(View.VISIBLE);
            inquiryDetailBinding.statusLl.setVisibility(View.GONE);
            inquiryDetailBinding.respondedLl.setVisibility(View.GONE);
        }else {
            inquiryDetailBinding.availabilityLl.setVisibility(View.GONE);
            inquiryDetailBinding.inquiryTypeTv.setVisibility(View.GONE);
            inquiryDetailBinding.statusLl.setVisibility(View.VISIBLE);
            inquiryDetailBinding.respondedLl.setVisibility(View.VISIBLE);
            inquiryDetailBinding.statusTv.setText(recordsItem.getStockCheckStatus());
            if(recordsItem.getStockCheckStatus().equalsIgnoreCase("Available")){
                inquiryDetailBinding.statusIv.setImageResource(R.drawable.green_dot);
            }else{
                inquiryDetailBinding.statusIv.setImageResource(R.drawable.red_dot);
            }
            inquiryDetailBinding.respondedBy.setText(recordsItem.getApprovedByName());
            inquiryDetailBinding.respondedIn.setText(recordsItem.getRespondedIn());
        }


//        inquiryDetailBinding.commentTv.setText(recordsItem.get);
        String url=(!recordsItem.getProductAttachment().isEmpty())? recordsItem.getProductAttachment().get(0):"";
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(inquiryDetailBinding.tileIv);

        if(recordsItem.getHistory().size()==0){
            inquiryDetailBinding.historyLl.setVisibility(View.GONE);
        }else{
            inquiryDetailBinding.historyLl.setVisibility(View.VISIBLE);

            for (int i = 0; i < recordsItem.getHistory().size(); i++) {
                Step1Test step = new Step1Test(this);
                inquiryDetailBinding.stepLayout.addStepView(step);
            }
            inquiryDetailBinding.stepLayout.load(recordsItem.getHistory());
        }
    }

    private void showCountDownDialog(HashMap<String, Object> map) {
        final Dialog dialog = new Dialog(this, R.style.TransparentDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.circular_countdown_dialog, null);
        dialog.setContentView(dialogView);
        progressBarView = (ProgressBar) dialog.findViewById(R.id.view_progress_bar);
        tv_time = (TextView) dialog.findViewById(R.id.tv_timer);
        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(0);
        progressBarView.setProgress(0);
        TextView edit_response= dialog.findViewById(R.id.edit_response_tv);
        edit_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                countDownTimer.cancel();
//                showNotAvailableResponseDialog(InquiryDetailActivity.this);
            }
        });
        fn_countdown(dialog,map);
        dialog.show();
    }

    private void fn_countdown(Dialog dialog, HashMap<String, Object> map) {
        myProgress = 0;
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
        }
        progress = 1; // up to finish time

        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress + 1;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                String newtime = minutes + ":" + seconds;

                if (newtime.equals("0:0:0")) {
                    tv_time.setText("00:00:00");
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    tv_time.setText("0" + minutes + ":0" + seconds);
                } else if (String.valueOf(minutes).length() == 1) {
                    tv_time.setText("0" + minutes + ":" + seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    tv_time.setText(minutes + ":0" + seconds);
                } else {
                    tv_time.setText(minutes + ":" + seconds);
                }
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                setProgress(progress, endTime);
                countDownTimer.cancel();
                updateInquiryStatus(map);
            }
        };
        countDownTimer.start();
    }


    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    private void showNotAvailableResponseDialog(Context mContext) {
        final Dialog dialog = new Dialog(this, R.style.TransparentDialogTheme);
        NotAvailableResponseDialogBinding dialogBinding=DataBindingUtil.inflate(getLayoutInflater(), R.layout.not_available_response_dialog, null, false);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.rb1DaysIv.setSelected(true);
        dialogBinding.rb1DaysIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogBinding.rb1DaysIv.isSelected()) {
                    dialogBinding.rb1DaysIv.setImageResource(R.drawable.ic_checked_radio_button);
                    dialogBinding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb1DaysIv.setSelected(true);
                    dialogBinding.rb2BoxesIv.setSelected(false);
                    dialogBinding.rb3MultipleBatchesIv.setSelected(false);
                }

            }
        });
        dialogBinding.rb2BoxesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogBinding.rb2BoxesIv.isSelected()) {
                    dialogBinding.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb2BoxesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    dialogBinding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb1DaysIv.setSelected(false);
                    dialogBinding.rb2BoxesIv.setSelected(true);
                    dialogBinding.rb3MultipleBatchesIv.setSelected(false);
                }
            }
        });
        dialogBinding.rb3MultipleBatchesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogBinding.rb3MultipleBatchesIv.isSelected()) {
                    dialogBinding.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    dialogBinding.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    dialogBinding.rb1DaysIv.setSelected(false);
                    dialogBinding.rb2BoxesIv.setSelected(false);
                    dialogBinding.rb3MultipleBatchesIv.setSelected(true);
                }
            }
        });
        dialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", getIntent().getStringExtra("inquiry_id"));
                if (dialogBinding.rb1DaysIv.isSelected()) {
                    if (!dialogBinding.daysNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(dialogBinding.daysNumEt.getText().toString()) > 0) {
                            Common.showLog("days" + dialogBinding.daysNumEt.getText());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_in_days", dialogBinding.daysNumEt.getText().toString());
                            dialog.dismiss();
                            showCountDownDialog(map);
                        } else
                            Toast.makeText(mContext, "entered value must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "please enter the value of selected item", Toast.LENGTH_SHORT).show();
                } else if (dialogBinding.rb2BoxesIv.isSelected()) {
                    if (!dialogBinding.boxesNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(String.valueOf(dialogBinding.boxesNumEt.getText())) > 0) {
                            Common.showLog("box" + dialogBinding.boxesNumEt.getText());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_quantity", dialogBinding.boxesNumEt.getText().toString());
                            dialog.dismiss();
                            showCountDownDialog(map);
                        } else
                            Toast.makeText(mContext, "entered value must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "please enter the value of selected item", Toast.LENGTH_SHORT).show();
                } else if (dialogBinding.rb3MultipleBatchesIv.isSelected()) {
                    Common.showLog("multipleBatches");
                    map.put("stock_check_status", "Available Multiple Batches");
                    dialog.dismiss();
                    showCountDownDialog(map);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETVENDORQUOTATIONS) {
                VendorQuoteDetailResponse response = new Gson().fromJson(responseDO.getResponse(), VendorQuoteDetailResponse.class);
                setData(response.getData().getRecords());
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Toast.makeText(InquiryDetailActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getAllInquiries(getIntent().getStringExtra("inquiry_id"));
            }
        }

    }
}