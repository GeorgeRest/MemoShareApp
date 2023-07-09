package com.george.memoshareapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.george.memoshareapp.Fragment.NewPersonFragment;

import java.util.ArrayList;
import java.util.List;

public class PersonPageAdapter extends FragmentStateAdapter {
    private List<String> mData = new ArrayList<>();
    private String phoneNumber;
    public PersonPageAdapter(@NonNull FragmentActivity fragmentActivity, List<String> data,String phoneNumber) {
        super(fragmentActivity);
        mData = data;
        this.phoneNumber=phoneNumber;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NewPersonFragment.newInstance(mData.get(position),phoneNumber);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}