package com.grad_proj.assembletickets.front.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        titleText = findViewById(R.id.titleText);
        titleText.setText("캘린더");

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
                        titleText.setText("캘린더");
                        break;
                    }
                    case R.id.navigation_ticket:{
                        fragmentTransaction.replace(R.id.frameLayout,ticketFragment).commitAllowingStateLoss();
                        titleText.setText("둘러보기");
                        break;
                    }
                    case R.id.navigation_sub:{
                        fragmentTransaction.replace(R.id.frameLayout,subscribeFragment).commitAllowingStateLoss();
                        titleText.setText("구독");
                        break;
                    }
                    case R.id.navigation_profile:{
                        fragmentTransaction.replace(R.id.frameLayout,userFragment).commitAllowingStateLoss();
                        titleText.setText("설정");
                        break;
                    }
                }

                return true;
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

}
