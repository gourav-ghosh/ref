package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FilterTaskBinding;
import com.devstringx.mytylesstockcheck.databinding.FragmentManageLeadsBinding;
import com.devstringx.mytylesstockcheck.databinding.LeadDetailPageBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.AllLeadsResponse;
import com.devstringx.mytylesstockcheck.filter.FilterActivity;
import com.devstringx.mytylesstockcheck.interfaces.UpdateTabTitle;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageLeadsFragment extends Fragment implements UpdateTabTitle {
    FragmentManageLeadsBinding manageLeadsBinding;
    AllLeadsFragment allLeadsFragment;
    TaskFragment taskFragment;
    ViewPagerAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manageLeadsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_manage_leads,container,false);
        View view=manageLeadsBinding.getRoot();
        allLeadsFragment=new AllLeadsFragment(this);
        taskFragment=new TaskFragment(this);
        createViewPager(manageLeadsBinding.viewPager);
        manageLeadsBinding.tabLayout.setupWithViewPager(manageLeadsBinding.viewPager);
        manageLeadsBinding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manageLeadsBinding.viewPager.getCurrentItem()==0) {
                    Intent intent = new Intent(getActivity(), FilterActivity.class);
                    startActivityForResult(intent,2001);
                }else{
                    taskFragment.showFilterBottomSheetDialog(manageLeadsBinding);
                }
            }
        });
        createTabIcons("0","0");
        allLeadsFragment.setTaskFragment(taskFragment);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2001){
            if(data!=null){
               String receivedHashMap =  data.getStringExtra("FilterDataMap");
                Common.showLog("===================receivedHashMap==="+receivedHashMap);
                allLeadsFragment.getFilterLeads(receivedHashMap);
            }
        }
    }

    private void createTabIcons(String tab1Count, String tab2Count) {
        manageLeadsBinding.tabLayout.getTabAt(0).setText("All Leads ("+tab1Count+")");
        manageLeadsBinding.tabLayout.getTabAt(1).setText(" My Tasks ("+tab2Count+")");
    }

    private void createViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(allLeadsFragment, "");
        adapter.addFrag(taskFragment, "");
        viewPager.setAdapter(adapter);
    }
    @Override
    public void updateTabListner(int tabIndex, String count) {
        if(tabIndex==0){
            manageLeadsBinding.tabLayout.getTabAt(0).setText("All Leads ("+count+")");
        }else if(tabIndex==1){
            manageLeadsBinding.tabLayout.getTabAt(1).setText("My Tasks ("+count+")");
        }
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}