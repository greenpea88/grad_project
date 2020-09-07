package com.grad_proj.assembletickets.front.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grad_proj.assembletickets.front.R;

import com.grad_proj.assembletickets.front.Fragment.CalendarFragment;
import com.grad_proj.assembletickets.front.Fragment.SubscribeFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketFragment;
import com.grad_proj.assembletickets.front.Fragment.UserFragment;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private CalendarFragment calendarFragment = new CalendarFragment();
    private TicketFragment ticketFragment = new TicketFragment();
    private SubscribeFragment subscribeFragment = new SubscribeFragment();
    private UserFragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //첫화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,calendarFragment).commitAllowingStateLoss();

        //bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch(menuItem.getItemId()){
                    case R.id.navigation_calendar:{
                        fragmentTransaction.replace(R.id.frameLayout,calendarFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_ticket:{
                        fragmentTransaction.replace(R.id.frameLayout,ticketFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_sub:{
                        fragmentTransaction.replace(R.id.frameLayout,subscribeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_profile:{
                        fragmentTransaction.replace(R.id.frameLayout,userFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }
}
