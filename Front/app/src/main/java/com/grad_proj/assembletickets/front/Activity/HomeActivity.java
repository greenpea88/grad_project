package com.grad_proj.assembletickets.front.Activity;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.grad_proj.assembletickets.front.Database.DatabaseOpen;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.Fragment.DateFragment;
import com.grad_proj.assembletickets.front.Fragment.SearchFragment;
import com.grad_proj.assembletickets.front.R;

import com.grad_proj.assembletickets.front.Fragment.CalendarFragment;
import com.grad_proj.assembletickets.front.Fragment.SubscribeFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketFragment;
import com.grad_proj.assembletickets.front.Fragment.UserFragment;

import java.util.Stack;

public class HomeActivity extends AppCompatActivity {

    public Stack<Fragment> fragmentStack = new Stack<>();
    private static final String TAG_PARENT = "TAG_PARENT";

    public DatabaseOpen databaseOpen;

    public FragmentManager fragmentManager = getSupportFragmentManager();
    private CalendarFragment calendarFragment = new CalendarFragment();
//    private TicketFragment ticketFragment = new TicketFragment();
    private Fragment ticketFragment;
    private SubscribeFragment subscribeFragment = new SubscribeFragment();
    private UserFragment userFragment = new UserFragment();
    private SearchFragment searchFragment = new SearchFragment();

    private TextView titleText;
    private ImageButton searchBtn;
    private ImageButton notificationBtn;
    private LinearLayout notiPage;

    private boolean isPageOpen = false;

    private Animation translateDown;
    private Animation translateUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        databaseOpen = new DatabaseOpen(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleText = findViewById(R.id.titleText);
        titleText.setText("캘린더");
        notiPage = findViewById(R.id.notiPage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //첫화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, calendarFragment).commitAllowingStateLoss();

        //bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switchFragment(menuItem);
                return true;
            }
        });

        translateDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        translateUp = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        SlidingPageAnimationListner animListner = new SlidingPageAnimationListner();
        translateDown.setAnimationListener(animListner);
        translateUp.setAnimationListener(animListner);

        // 검색 사이드바
        searchBtn = findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentStack.push(fragmentManager.findFragmentById(R.id.frameLayout));
                replaceFragment(searchFragment);
                ActionBar actionBar = getSupportActionBar();
                actionBar.hide();
            }
        });

        // 알림 사이드바
        notificationBtn = findViewById(R.id.notiButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPageOpen) {
                    notiPage.startAnimation(translateUp);
                } else {
                    notiPage.setVisibility(View.VISIBLE);
                    notiPage.startAnimation(translateDown);
                }
            }
        });

    }

    private void switchFragment(@NonNull MenuItem menuItem) {

        //tab이 바뀌는 경우 backstack에 쌓인 fragment들 전부 비우기
//        fragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //tab 바뀔 때마다 stack 초기화
        fragmentStack = new Stack<>();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch(menuItem.getItemId()){
            case R.id.navigation_calendar:{
                fragmentStack.push(calendarFragment);
                fragmentTransaction.replace(R.id.frameLayout,calendarFragment).commitAllowingStateLoss();
                titleText.setText("캘린더");
                break;
            }
            case R.id.navigation_ticket:{
                //중첩 프래그먼트 사용 시 프래그먼트 재생성으로 인한 오류를 해결하기 위함
                ticketFragment = fragmentManager.findFragmentByTag(TAG_PARENT);
                if(ticketFragment == null){
                    ticketFragment = TicketFragment.getInstance();
                }
                fragmentTransaction.replace(R.id.frameLayout,ticketFragment,TAG_PARENT).commitAllowingStateLoss();
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
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Fragment로 사용할 MainActivity내의 layout공간을 선택
        // 뒤로가기 버튼 누르면 이전 fragment로 되돌아옴
        fragmentTransaction.replace(R.id.frameLayout, fragment).commitAllowingStateLoss();

    }

    public void submitBtnAction(){
        Log.i("submit button","pressed");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Event event = new Event();
        if(!fragmentStack.isEmpty()){
            DateFragment lastFragment = (DateFragment)fragmentStack.pop();
            fragmentTransaction.replace(R.id.frameLayout, lastFragment).commitAllowingStateLoss();
        }
    }

    private class SlidingPageAnimationListner implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                notiPage.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            } else {
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }

    @Override
    public void onBackPressed() {
        if (isPageOpen) {
            notiPage.setVisibility(View.INVISIBLE);
            isPageOpen = false;
        } else {
            Log.i("Back button","pressed");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(!fragmentStack.isEmpty()){
                Fragment lastFragment = fragmentStack.pop();
                fragmentTransaction.replace(R.id.frameLayout,lastFragment).commit();
            }
            else{
                super.onBackPressed();
            }
        }
    }

    public void insertEvent(String date,String eventName,int hour, int minute){
        databaseOpen.open();

        databaseOpen.insertColumn(date,eventName,hour,minute);
        databaseOpen.close();
    }

    public void insertEvent(String date,String eventName, String eventContent,int hour, int minute){
        databaseOpen.open();

        databaseOpen.insertColumn(date,eventName,eventContent,hour,minute);
        databaseOpen.close();
    }

    public Cursor getDateEvents(String date){
        databaseOpen.open();

        return databaseOpen.selectDataEvent(date);
    }

    public void closeDB(){
        databaseOpen.close();
    }

}
