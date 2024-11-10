package com.devstringx.mytylesstockcheck.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewPager2Adapter extends FragmentStateAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> tags = new ArrayList<>();
    public DashboardViewPager2Adapter(FragmentManager manager, Lifecycle lifecycle) {
        super(manager,lifecycle);
    }
    public void addFrag(Fragment fragment,String tag) {
        mFragmentList.add(fragment);
        tags.add(tag);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }
    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

    public int getFragPosition(String tag) {
        for (int i = 0; i < tags.size(); i++) {
            if(tags.get(i).equalsIgnoreCase(tag)){
                return i;
            }
        }
        return 0;
    }
}