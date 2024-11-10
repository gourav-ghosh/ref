package com.devstringx.mytylesstockcheck.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityNotificationListBinding;
import com.devstringx.mytylesstockcheck.datamodal.taskNotification.RecordsItem;

import java.util.ArrayList;

import com.devstringx.mytylesstockcheck.adapter.NotificationMainDashboardAdapter;
import com.devstringx.mytylesstockcheck.screens.fragments.DeliveryStatusNotificationFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.StockCheckResponseFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.UpcomingTasksFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NotificationListActivity extends AppCompatActivity {
    ActivityNotificationListBinding activityNotificationListBinding;
    NotificationMainDashboardAdapter adapter;
    private ArrayList<RecordsItem> recordsItems=new ArrayList<>();
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationListBinding= DataBindingUtil.setContentView(this , R.layout.activity_notification_list);
        setListAdapter();
    }
    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra("background",false)) {
            Intent intent=new Intent(NotificationListActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    private void setListAdapter() {
        DashboardViewPager2Adapter adapter = new DashboardViewPager2Adapter(getSupportFragmentManager(),getLifecycle());
        adapter.addFrag(new UpcomingTasksFragment(),"UpcomingTasksFragment");
        adapter.addFrag(new StockCheckResponseFragment(),"StockCheckResponseFragment");
        adapter.addFrag(new DeliveryStatusNotificationFragment(),"DeliveryStatusFragment");
        activityNotificationListBinding.pager.setAdapter(adapter);
        activityNotificationListBinding.pager.setUserInputEnabled(false);
        new TabLayoutMediator(activityNotificationListBinding.tabLayout, activityNotificationListBinding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==0){
                    tab.setText("Upcoming Tasks");
                }else if(position==1){
                    tab.setText("Stock Check Response");
                }else tab.setText("Delivery Status");
            }
        }).attach();
        activityNotificationListBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra("background",false)) {
                    Intent intent=new Intent(NotificationListActivity.this, DashboardActivity.class);
                    intent.putExtra("background",true);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
}