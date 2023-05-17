package com.george.memoshareapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.george.memoshareapp.Fragment.HomeFragment;
import com.george.memoshareapp.Fragment.HomePageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.adapters
 * @className: HomePagerAdapter
 * @author: George
 * @description: TODO
 * @date: 2023/5/14 17:19
 * @version: 1.0
 */
public class HomePagerAdapter extends FragmentStateAdapter {

    private List<String> mData = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> data) {
        super(fragmentActivity);
        mData = data;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return HomePageFragment.newInstance(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}

