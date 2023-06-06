package com.george.memoshareapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FriendListPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> friendFragmentList;
    public FriendListPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.friendFragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return friendFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return friendFragmentList.size();
    }
}
