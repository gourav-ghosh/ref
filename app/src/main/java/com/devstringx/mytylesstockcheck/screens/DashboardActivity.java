package com.devstringx.mytylesstockcheck.screens;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.devstringx.mytylesstockcheck.BuildConfig;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.adapter.DrawerListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityDashboardBinding;
import com.devstringx.mytylesstockcheck.datamodal.drawerModal.DrawerData;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
import com.devstringx.mytylesstockcheck.screens.analytics.DataAnalyticsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ArchitectInteriorFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ComplaintManagementFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.DailyWorklogFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.HomeFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ManageLeadsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ManageQuotesFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.OrderListingFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.RazorPayFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ShippingChargesFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.TransactionHistoryFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.UserWorklogFragment;
import com.devstringx.mytylesstockcheck.screens.myProfile.MyProfileActivity;
import com.devstringx.mytylesstockcheck.screens.taskreminder.TaskReminderListActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.ManageInquiriesFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.ManageUserFragment;
import com.devstringx.mytylesstockcheck.screens.vendor.VendorNotificationActivity;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DashboardActivity extends AppCompatActivity implements ResponseListner, DrawerListAdapter.DrawerListClick {
    ActivityDashboardBinding binding;
    Response response;
    private PreferenceUtils utils;
    private DashboardViewPager2Adapter adapter;
    private DrawerListAdapter drawerListAdapter;
    private ArrayList<DrawerData> drawerDataArrayList = new ArrayList<>();
    private ArrayList<RadioButton> bottomBarDataArrayList = new ArrayList<>();
    private int askPayment;
    private int eligibleForAdminRights;
    private int eligibleForOvertime;
    private int eligibleForWorklog;
    private int eligibleForCMS;
    private int eligibleForCRM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
        utils=new PreferenceUtils(DashboardActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                EasyPermissions.requestPermissions(
                        this,
                        "Stock Check Push Notification",
                        112,
                        android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
        getModuleByUser();
        getUserProfile(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID, "").toString());
    }
    private void initView(){
        getUserProfile(new PreferenceUtils(getBaseContext()).getStringFromPreference(PreferenceUtils.USERID, "").toString());
        drawerDataArrayList=new ArrayList<>();
        adapter = new DashboardViewPager2Adapter(getSupportFragmentManager(),getLifecycle());
        if(Common.getPermission(this,"DB","")){
            adapter.addFrag(new HomeFragment(),"home");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_manageleads,"Dashboard",true));
        }
//        if(Common.getPermission(this,"ML","") &&
//                (!utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("13") ||
//                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("13") &&
//                                eligibleForCRM == 1))){
        if(Common.getPermission(this,"ML","")) {
            adapter.addFrag(new ManageLeadsFragment(),"lead");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_drawer_lead_listing,"Manage Lead",false));
        }
        if(Common.getPermission(this,"MQ","")){
            adapter.addFrag(new ManageQuotesFragment(),"quote");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_drawer_manage_quote,"Manage Quote",false));
        }
        if(Common.getPermission(this,"MU","")){
            adapter.addFrag(new ManageUserFragment(),"user");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_manageleads,"Manage Users",false));
        }
        if(Common.getPermission(this,"SCE","")){
            adapter.addFrag(new ManageInquiriesFragment(),"inquire");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_manageorders,"Manage Inquiries",false));
        }
        if(Common.getPermission(this,"DAN","")){
            adapter.addFrag(new DataAnalyticsFragment(),"analytics");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_manageorders,"Data Analytics",false));
        }
        if(Common.getPermission(this,"RZP","")){
            adapter.addFrag(new RazorPayFragment(),"razorpay");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_razorpay_link,"Razorpay",false));
        }
        if(Common.getPermission(this,"ODS","")) {
            adapter.addFrag(new OrderListingFragment(), "order");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_truck, "Delivery Status", false));
        }
//        if(Common.getPermission(this,"CMS","") &&
//                (!(utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("13") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) ||
//                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("13") &&
//                                eligibleForCMS == 1) ||
//                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2") &&
//                                eligibleForAdminRights == 1))) {
        if(Common.getPermission(this,"CMS","")) {
            adapter.addFrag(new ComplaintManagementFragment(), "complaint");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_complaint, "Complaints", false));
        }
        if(Common.getPermission(this,"THI","")) {
            adapter.addFrag(new TransactionHistoryFragment(), "transaction");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_money, "Transaction History", false));
        }
//        if((Common.getPermission(this,"DWL","") &&
//                (((utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("8") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("10") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("11") ||
//                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("13"))
//                        && eligibleForWorklog == 1))) ||
//                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("1") ||
//                                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("12"))) {
        if(Common.getPermission(this,"DWL","")) {
            if(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("12") || new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("1"))
                adapter.addFrag(new DailyWorklogFragment(), "worklog");
            else adapter.addFrag(new UserWorklogFragment(), "worklog");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_worklog, "Daily Worklog", false));
        }
//        if(Common.getPermission(this,"SCH","") &&
//                (!utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9") ||
//                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9") &&
//                                askPayment == 1))) {
        if(Common.getPermission(this,"SCH","")) {
            adapter.addFrag(new ShippingChargesFragment(), "shipping");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_shipping_charge, "Shipping Charges", false));
        }
        if(Common.getPermission(this,"AORI","")) {
            adapter.addFrag(new ArchitectInteriorFragment(), "architect_interior");
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_architect, "Architect / Interior", false));
        }
        if(Common.getPermission(this,"TRE","")) {
            drawerDataArrayList.add(new DrawerData(R.drawable.ic_clock,"Task Reminder",false));
        }
        drawerDataArrayList.add(new DrawerData(R.drawable.ic_logout,"Log out",false));
        drawerListAdapter=new DrawerListAdapter(this,this);
        drawerListAdapter.refreshList(drawerDataArrayList);
        binding.menuRv.setHasFixedSize(true);
        binding.menuRv.setLayoutManager(new LinearLayoutManager(this));
        binding.menuRv.setAdapter(drawerListAdapter);
        if (new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID, "").toString().equalsIgnoreCase("6")) {
            binding.bottombarRL.setVisibility(View.VISIBLE);
            binding.bottomBar.inflateMenu(R.menu.admin_bottom_nav_menu);
            bottomBarDataArrayList = new ArrayList<>();
            bottomBarDataArrayList.add(0, binding.rb1);
            bottomBarDataArrayList.add(1, binding.rb2);
            bottomBarDataArrayList.add(2, binding.rb3);
            bottomBarDataArrayList.get(0).setChecked(true);
        } else binding.bottombarRL.setVisibility(View.GONE);
        binding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    onBtnClickListner(0, "");
                    return true;
                } else if (item.getItemId() == R.id.nav_leads) {
                    onBtnClickListner(1, "");
                    return true;
                } else if (item.getItemId() == R.id.nav_orders) {
                    onBtnClickListner(2, "");
                    return true;
                }
                return false;
            }
        });
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("4") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            binding.notiRl.setVisibility(View.VISIBLE);
        } else {
            binding.notiRl.setVisibility(View.GONE);
        }
        binding.navigationView.getHeaderView(0).findViewById(R.id.profile_name_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.myDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(DashboardActivity.this, MyProfileActivity.class);
                Common.showLog("12345678====" + response.getData());
                List<DataItem> profileData = response.getData();
                intent.putExtra("profileData", (Serializable) response.getData());
                startActivityForResult(intent, 123);
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.myDrawerLayout.openDrawer(GravityCompat.START);
                } else binding.myDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        binding.navigationView.getHeaderView(0).findViewById(R.id.cross_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        binding.notiRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2") ||
                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3")||
                        utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("4")){
                    Intent intent=new Intent(DashboardActivity.this, VendorNotificationActivity.class);
                    startActivity(intent);
                }else if(utils.getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("6")){
                    Intent intent=new Intent(DashboardActivity.this, NotificationListActivity.class);
                    intent.putExtra("background",false);
                    startActivity(intent);
                }

            }
        });
        binding.versionNameTv.setText("App Version "+ BuildConfig.VERSION_NAME);
        binding.viewPager.setOffscreenPageLimit(adapter.getItemCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.devstringx.mytylesstockcheck.SOCKET_NOTIFICATION");
        registerReceiver(MyBroadcastReceiver.getInstance(),intentFilter);

        if ((Common.getPermission(this, "DB", "")) &&
                        (Common.getPermission(this, "SCE", ""))) {
            int position = 1;
            binding.viewPager.setCurrentItem(position);
            for (int i = 0; i < drawerDataArrayList.size(); i++) {
                drawerDataArrayList.get(i).setSelected(false);
            }
            drawerDataArrayList.get(position).setSelected(true);
        }

        if ((Common.getPermission(this, "DB", "")) && (Common.getPermission(this, "MU", "")) &&
                (Common.getPermission(this, "SCE", "")) ){
            int position = 0;
            binding.viewPager.setCurrentItem(position);
            for (int i = 0; i < drawerDataArrayList.size(); i++) {
                drawerDataArrayList.get(i).setSelected(false);
            }
            drawerDataArrayList.get(position).setSelected(true);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        getUserProfile(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID, "").toString());
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("4") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            notificationCount(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID, "").toString());
        }

    }
    private void getModuleByUser() {
        HashMap<String,Object> map=new HashMap<>();
        map.put(NKeys.GETMODULEBYUSER,new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETMODULEBYUSER, map, false);
    }

    private void getUserProfile(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(DashboardActivity.this, this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, false);
    }

    private void notificationCount(String id) {

        ArrayList<String> types=new ArrayList<>();
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")) {
            types.add("pending inquiry");
        }else  if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3") ||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("4")) {
            types.add("new inquiry");
        }else if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) {
            types.add("task");
            types.add("response inquiry");
        }

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("notification_tab",types);
        HashMap<String, Object> map = new HashMap<>();
        Common.showLog("DATANOTI======" + new Gson().toJson(map1));
        map.put(NKeys.DATA, new Gson().toJson(map1));
        new NetworkRequest(DashboardActivity.this, this).callWebServices(ServiceMethods.WS_NOTIFICATIONCOUNT, map, false);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE==DASHBOARD=" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                askPayment = response.getData().get(0).getAskPayment();
                eligibleForAdminRights = response.getData().get(0).getEligibleForAdminRights();
                eligibleForOvertime = response.getData().get(0).getEligibleForOvertime();
                eligibleForWorklog = response.getData().get(0).getEligibleForWorklog();
                eligibleForCMS = response.getData().get(0).getEligibleForCMS();
                eligibleForCRM = response.getData().get(0).getEligibleForCRM();
                Common.phoneNumber = response.getData().get(0).getPhoneNumber();
                Common.userProfilEligibleForCRM = eligibleForCRM;
                Common.userProfileRole = response.getData().get(0).getRole();
                TextView profile_name = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_name_tv);
                profile_name.setText(response.getData().get(0).getRole());
                binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv).setVisibility(View.GONE);
                binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv).setVisibility(View.GONE);
                if (response.getData().get(0).getProfileImage()==null) {
                    binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv).setVisibility(View.VISIBLE);
                    TextView profile_img = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv);
                    Common.showLog("=====" + response.getData().get(0).getFirstName().charAt(0) + response.getData().get(0).getLastName().charAt(0));
                    profile_img.setText("" + String.valueOf(response.getData().get(0).getFirstName().charAt(0)).toUpperCase() + String.valueOf(response.getData().get(0).getLastName().charAt(0)).toUpperCase());
                } else {
                    binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv).setVisibility(View.VISIBLE);
                    ImageView profile_img = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv);
                    Glide.with(getBaseContext())
                            .load(response.getData().get(0).getProfileImage())
                            .transform(new CenterCrop(), new CircleCrop())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(profile_img);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETMODULEBYUSER) {
                utils.saveString(PreferenceUtils.ROLEPERMISSIONDATA, responseDO.getResponse());
                initView();
            }else if(responseDO.getServiceMethods() == ServiceMethods.WS_NOTIFICATIONCOUNT){
                try {
                    JSONObject jsonObject=new JSONObject(responseDO.getResponse());
                    jsonObject=jsonObject.getJSONObject("data").getJSONObject("records");
                    if(Integer.parseInt(jsonObject.getString("totalUnreadCount"))==0){
                        binding.notiCountTv.setVisibility(View.GONE);
                    }else{
                        binding.notiCountTv.setVisibility(View.VISIBLE);
                        binding.notiCountTv.setText(jsonObject.getString("totalUnreadCount"));
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onBtnClickListner(int position, String type) {
        if(type.equalsIgnoreCase("log out")){
            Common.showlogoutDialog(DashboardActivity.this);
        } else if (type.equalsIgnoreCase("Task Reminder")) {
            startActivity(new Intent(this,TaskReminderListActivity.class));
        } else {
            binding.viewPager.setCurrentItem(position);
            for (int i = 0; i < drawerDataArrayList.size(); i++) {
                drawerDataArrayList.get(i).setSelected(false);
            }
            drawerDataArrayList.get(position).setSelected(true);
            if (!bottomBarDataArrayList.isEmpty() && position<3) {
                bottomBarDataArrayList.get(position).setChecked(true);
                binding.bottomBar.getMenu().getItem(position).setChecked(true);
            }
            drawerListAdapter.refreshList(drawerDataArrayList);
        }
        binding.myDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("4") || new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID,"").equalsIgnoreCase("3")){
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
