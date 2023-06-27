package com.george.memoshareapp.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.HomePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.Fragment
 * @className: HomepageFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/8 20:35
 * @version: 1.0
 */
public class HomeFragment extends Fragment {//外部

    private TabLayout mTabLayout;
    public ViewPager2 mViewPager2;
    private List<String> mData = new ArrayList<>();
    private HomePagerAdapter homePagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initData();
        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        mTabLayout = rootView.findViewById(R.id.home_indicator);
        mViewPager2 = rootView.findViewById(R.id.home_pager);
        homePagerAdapter = new HomePagerAdapter(getActivity(), mData);
        mViewPager2.setAdapter(homePagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mData.get(position));

            }
        }).attach();


    }

    private void initData() {
        mData.add("好友");
        mData.add("推荐");
        mData.add("活动");
    }

}
