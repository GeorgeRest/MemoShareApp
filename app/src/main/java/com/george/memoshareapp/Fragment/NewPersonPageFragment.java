package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.george.memoshareapp.R;
import com.george.memoshareapp.adapters.PersonPageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class NewPersonPageFragment extends Fragment {
    //外部
    private TabLayout mTabLayout;
    public ViewPager2 mViewPager2;
    private List<String> mData = new ArrayList<>();
    private PersonPageAdapter personpageadapter;
    private AppBarLayout appBarLayout;
    private TextView toolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_page, container, false);
        initData();
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager2 = rootView.findViewById(R.id.person_pager);
        personpageadapter = new PersonPageAdapter(getActivity(), mData);
        mViewPager2.setAdapter(personpageadapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mData.get(position));
            }
        }).attach();

        appBarLayout = rootView.findViewById(R.id.appBar);
        toolbarTitle = rootView.findViewById(R.id.toolbar_title);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void initData() {
        mData.add("发布");
        mData.add("点赞");
    }


}
