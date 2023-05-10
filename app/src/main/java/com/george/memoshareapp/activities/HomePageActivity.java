package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.george.memoshareapp.Fragment.CalendarTripFragment;
import com.george.memoshareapp.Fragment.HomepageFragment;
import com.george.memoshareapp.Fragment.MessageFragment;
import com.george.memoshareapp.Fragment.PersonalPageFragment;
import com.george.memoshareapp.R;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private HomepageFragment homepageFragment;

    private CalendarTripFragment calendarTripFragment;

    private MessageFragment messageFragment;


    private PersonalPageFragment personalPageFragment;

    private ImageView icon_one;

    private ImageView icon_two;

    private ImageView icon_three;

    private ImageView icon_four;


    private FragmentManager fragmentManager;
    private ImageView capsuleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initViews();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);

    }

    private void initViews() {
        icon_one = (ImageView) findViewById(R.id.bottom_icon_one);
        icon_two = (ImageView) findViewById(R.id.bottom_icon_two);
        icon_three = (ImageView) findViewById(R.id.bottom_icon_three);
        icon_four = (ImageView) findViewById(R.id.bottom_icon_four);
        capsuleButton = (ImageView) findViewById(R.id.home_capsule);
        icon_one.setOnClickListener(this);
        icon_two.setOnClickListener(this);
        icon_three.setOnClickListener(this);
        icon_four.setOnClickListener(this);
        capsuleButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_icon_one:
                setTabSelection(0);
                break;
            case R.id.bottom_icon_two:
                setTabSelection(1);
                break;
            case R.id.bottom_icon_three:
                setTabSelection(2);
                break;
            case R.id.bottom_icon_four:
                setTabSelection(3);
                break;
            case R.id.home_capsule:
                startActivity(new Intent(HomePageActivity.this, ReleaseActivity.class));
            default:
                break;
        }

    }
    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
//                icon_one.setImageResource(R.drawable.message_selected);
                if (homepageFragment == null) {
                    homepageFragment = new HomepageFragment();
                    transaction.add(R.id.content, homepageFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(homepageFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
//                contactsImage.setImageResource(R.drawable.contacts_selected);
                if (calendarTripFragment == null) {
                    calendarTripFragment = new CalendarTripFragment();
                    transaction.add(R.id.content, calendarTripFragment);
                } else {
                    transaction.show(calendarTripFragment);
                }
                break;
            case 2:
//                icon_three.setImageResource(R.drawable.news_selected);
                if (messageFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 3:
            default:
//                icon_four.setImageResource(R.drawable.setting_selected);
                if (personalPageFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    personalPageFragment = new PersonalPageFragment();
                    transaction.add(R.id.content, personalPageFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(personalPageFragment);
                }
                break;
        }
        transaction.commit();

    }

    private void clearSelection() {
//        icon_one.setImageResource(R.drawable.message_unselected);
//        contactsImage.setImageResource(R.drawable.contacts_unselected);
//        icon_three.setImageResource(R.drawable.news_unselected);
//        icon_four.setImageResource(R.drawable.setting_unselected);
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (homepageFragment != null) {
            transaction.hide(homepageFragment);
        }
        if (calendarTripFragment != null) {
            transaction.hide(calendarTripFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (personalPageFragment != null) {
            transaction.hide(personalPageFragment);
        }
    }
}