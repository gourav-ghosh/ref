package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.devstringx.mytylesstockcheck.BuildConfig;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityDashboardBinding;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.fragments.AllLeadsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.HomeFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ManageLeadsFragment;
import com.devstringx.mytylesstockcheck.screens.fragments.ManageQuotesFragment;
import com.devstringx.mytylesstockcheck.screens.myProfile.MyProfileActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class VendorDashboardActivity extends AppCompatActivity implements ResponseListner {
    ActivityDashboardBinding binding;
    ManageUserFragment manageUserFragment;
    HomeFragment homeFragment;
    ManageInquiriesFragment manageInquiriesFragment;
    public Response response;
    private DashboardViewPager2Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
        manageUserFragment=new ManageUserFragment();
        manageInquiriesFragment=new ManageInquiriesFragment();
        homeFragment=new HomeFragment();
//        createViewPager(binding.viewPager);
        binding.navigationView.inflateMenu(R.menu.vender_admin_nav_drawer_menu);
//        binding.viewPager.setCurrentItem(0,true);
        binding.navigationView.getMenu().findItem(R.id.dashboard).setChecked(true);
        if(!Common.getPermission(this,"MU","")){
            binding.navigationView.getMenu().findItem(R.id.drawer_manage_users).setVisible(false);
        }
        if(!Common.getPermission(this,"SCE","")){
            binding.navigationView.getMenu().findItem(R.id.drawer_manage_inquiries).setVisible(false);
        }
        binding.navigationView.getHeaderView(0).findViewById(R.id.profile_name_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VendorDashboardActivity.this, MyProfileActivity.class);
                Common.showLog("12345678===="+response.getData());
                List<DataItem> profileData=response.getData();
                intent.putExtra("profileData", (Serializable) response.getData());
                startActivityForResult(intent,123);
            }
        });
        binding.navigationView.getHeaderView(0).findViewById(R.id.cross_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.dashboard:
//                        binding.viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.drawer_manage_users:
//                        binding.viewPager.setCurrentItem(2);
//                        break;
//                    case R.id.drawer_manage_inquiries:
//                        binding.viewPager.setCurrentItem(1);
//                        break;
                    case R.id.logout:
                        Common.showlogoutDialog(VendorDashboardActivity.this);
                        break;
                }

                // Close the drawer when an item is selected
                binding.myDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });
        binding.bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorDashboardActivity.this, VendorNotificationActivity.class);
                startActivityForResult(intent,200);
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    binding.myDrawerLayout.openDrawer(GravityCompat.START);
                }else binding.myDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });
        binding.versionNameTv.setText("App Version "+ BuildConfig.VERSION_NAME);

    }
    private void createViewPager(ViewPager2 viewPager) {
        adapter = new DashboardViewPager2Adapter(getSupportFragmentManager(),getLifecycle());
//        adapter.addFrag(homeFragment);
//        adapter.addFrag(manageInquiriesFragment);
//        adapter.addFrag(manageUserFragment);

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
    }
    private void getUserProfile(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id",id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(VendorDashboardActivity.this, this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfile(new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID,"").toString());
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE==DASHBOARD=" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                TextView profile_name=binding.navigationView.getHeaderView(0).findViewById(R.id.profile_name_tv);
                profile_name.setText(response.getData().get(0).getRole());
                binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv).setVisibility(View.GONE);
                binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv).setVisibility(View.GONE);
                if (response.getData().get(0).getProfileImage()==null) {
                    binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv).setVisibility(View.VISIBLE);
                    TextView profile_img=binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_tv);
                    Common.showLog("===DRAWER=="+response.getData().get(0).getFirstName().charAt(0)+response.getData().get(0).getLastName().charAt(0));
                    profile_img.setText(""+String.valueOf(response.getData().get(0).getFirstName().charAt(0)).toUpperCase()+String.valueOf(response.getData().get(0).getLastName().charAt(0)).toUpperCase());
                }else {
                    binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv).setVisibility(View.VISIBLE);
                    ImageView profile_img=binding.navigationView.getHeaderView(0).findViewById(R.id.profile_img_iv);
                    Glide.with(getBaseContext())
                            .load(response.getData().get(0).getProfileImage())
                            .transform(new CenterCrop(),new CircleCrop())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(profile_img);
                }
            }
        }
    }
}