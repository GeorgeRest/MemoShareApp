package com.george.memoshareapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.george.memoshareapp.Fragment.CalendarTripFragment;
import com.george.memoshareapp.Fragment.HomeFragment;
import com.george.memoshareapp.Fragment.MessageFragment;
import com.george.memoshareapp.Fragment.NewPersonPageFragment;
import com.george.memoshareapp.R;
import com.george.memoshareapp.manager.UserManager;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private HomeFragment homeFragment;

    private CalendarTripFragment calendarTripFragment;

    private MessageFragment messageFragment;


    private NewPersonPageFragment newPersonPageFragment;

    private ImageView icon_one;

    private ImageView icon_two;

    private ImageView icon_three;

    private ImageView icon_four;


    private FragmentManager fragmentManager;
    private ImageView capsuleButton;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initViews();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String phoneNumber = sp.getString("phoneNumber", "");
        new UserManager(this).saveUserToLocal(phoneNumber);
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
        sp = getSharedPreferences("User", MODE_PRIVATE);
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
                String phoneNumber = sp.getString("phoneNumber", "");
                Intent intent = new Intent(HomePageActivity.this, ReleaseActivity.class).putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
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
                icon_one.setImageResource(R.drawable.bottom_icon_one_click);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment,"HomeFragment");
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:

                icon_two.setImageResource(R.drawable.bottom_icon_two_click);
                if (calendarTripFragment == null) {
                    calendarTripFragment = new CalendarTripFragment();
                    transaction.add(R.id.content, calendarTripFragment);
                } else {
                    transaction.show(calendarTripFragment);
                }
                break;
            case 2:
                icon_three.setImageResource(R.drawable.bottom_icon_three_click);
                if (messageFragment == null) {

                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {

                    transaction.show(messageFragment);
                }
                break;
            case 3:
            default:
                icon_four.setImageResource(R.drawable.bottom_icon_four_click);
                if (newPersonPageFragment == null) {

                    newPersonPageFragment = new NewPersonPageFragment();
                    transaction.add(R.id.content, newPersonPageFragment);
                } else {

                    transaction.show(newPersonPageFragment);
                }
                break;
        }
        transaction.commit();

    }

    private void clearSelection() {
        icon_one.setImageResource(R.drawable.bottom_icon_one);
        icon_two.setImageResource(R.drawable.bottom_icon_two);
        icon_three.setImageResource(R.drawable.bottom_icon_three);
        icon_four.setImageResource(R.drawable.bottom_icon_four);
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (calendarTripFragment != null) {
            transaction.hide(calendarTripFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (newPersonPageFragment != null) {
            transaction.hide(newPersonPageFragment);
        }
    }


}