package com.grad_proj.assembletickets.front.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grad_proj.assembletickets.front.Alarm.AlarmReceiver;
import com.grad_proj.assembletickets.front.Database.CDatabaseOpen;
import com.grad_proj.assembletickets.front.Database.CalendarDatabase;
import com.grad_proj.assembletickets.front.Database.SDatabaseOpen;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.Fragment.CalendarFragment;
import com.grad_proj.assembletickets.front.Fragment.SearchFragment;
import com.grad_proj.assembletickets.front.Fragment.SubscribeFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketFragment;
import com.grad_proj.assembletickets.front.Fragment.UserFragment;
import com.grad_proj.assembletickets.front.Notification;
import com.grad_proj.assembletickets.front.NotificationAdapter;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class HomeActivity extends AppCompatActivity {

    public Stack<Fragment> fragmentStack = new Stack<>();
    private static final String TAG_PARENT = "TAG_PARENT";

    public CDatabaseOpen cDatabaseOpen;
    public SDatabaseOpen sDatabaseOpen;

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

    GoogleSignInClient mGoogleSignInClient;

    RecyclerView notiRecyclerview;
    NotificationAdapter notificationAdapter;
    AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        if(intent.hasExtra("id")) {
            UserSharedPreference.setIdToken(this, "google" + intent.getStringExtra("id"));
        }
        if(intent.hasExtra("email")) {
//            UserSharedPreference.setUserEmail(this, intent.getStringExtra("email"));
            UserSharedPreference.setUserEmail(this, "user00@gmail.com");
        }
        if(intent.hasExtra("username")) {
            UserSharedPreference.setUserName(this, intent.getStringExtra("username"));
            Toast.makeText(this, UserSharedPreference.getUserName(this) + "님, 안녕하세요!", Toast.LENGTH_LONG).show();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        cDatabaseOpen = new CDatabaseOpen(this);
        sDatabaseOpen = new SDatabaseOpen(this);

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
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayout);
                fragmentStack.push(currentFragment);
                replaceFragment(searchFragment);
            }
        });

        notificationAdapter = new NotificationAdapter();
        notiRecyclerview = findViewById(R.id.notiRecyclerview);
        notiRecyclerview.setAdapter(notificationAdapter);

        // 알림 사이드바
        notificationBtn = findViewById(R.id.notiButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPageOpen) {
                    notiPage.startAnimation(translateUp);
                } else {
                    updateNotification();
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
//                fragmentStack.push(calendarFragment);
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
        if(!fragmentStack.isEmpty()){
            //date 선택 fragment -> 시간 및 세부사항 선택 fragment 총 두가지를 거치므로 두번의 pop을 필요로 함.
            fragmentStack.pop();
            Fragment lastFragment = fragmentStack.pop();
            fragmentTransaction.replace(R.id.frameLayout, lastFragment).commitAllowingStateLoss();
        }
    }

    public void editSubmitBtnAction(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!fragmentStack.isEmpty()){
            //date 선택 fragment -> 시간 및 세부사항 선택 fragment 총 두가지를 거치므로 두번의 pop을 필요로 함.
            Fragment lastFragment = fragmentStack.pop();
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
            //알림 사이드 바
            notiPage.setVisibility(View.INVISIBLE);
            isPageOpen = false;
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(!fragmentStack.isEmpty()){
                Fragment lastFragment = fragmentStack.pop();
                if(lastFragment instanceof TicketFragment){
                    Log.d("HomeActivity","ticket back pressed");
                    //이전 fragment가 ticket탭 일 경우에는 중첩 fragment를 사용하므로 check를 필요로 함
                    ticketFragment = fragmentManager.findFragmentByTag(TAG_PARENT);
                    if(ticketFragment == null){
                        ticketFragment = TicketFragment.getInstance();
                    }
                    fragmentTransaction.replace(R.id.frameLayout,ticketFragment,TAG_PARENT).commit();
                }
                else{
                    fragmentTransaction.replace(R.id.frameLayout,lastFragment).commit();
                }
            }
            else{
                super.onBackPressed();
            }
        }
    }

    public void updateNotification(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH mm");
        Date date = new Date();
        String now = simpleDateFormat.format(date);
        String[] dateSet = now.split(" ");
        String dateString = dateSet[0];
        String hour = dateSet[1];
        String min = dateSet[2];

        notificationAdapter.removeAll();
        Cursor cursor = getDateEvents(dateString);

        while(cursor.moveToNext()){
            Notification notification = new Notification();
            notification.setContext(cursor.getString(cursor.getColumnIndex(CalendarDatabase.CalendarDB.EVENTNAME)));
            notification.setDate(cursor.getString(cursor.getColumnIndex(CalendarDatabase.CalendarDB.EVENTDATE)));

            notificationAdapter.addItem(notification);
        }
        closeCalendarDB();
        notificationAdapter.notifyDataSetChanged();
    }

    public void insertEvent(int id,String date,String eventName, String eventContent,int hour, int minute,int alarmSet,int showId){
        cDatabaseOpen.open();

        cDatabaseOpen.insertColumn(id,date,eventName,eventContent,hour,minute,alarmSet,showId);
        cDatabaseOpen.close();
    }

    public Cursor getDateEvents(String date){
        cDatabaseOpen.open();

        return cDatabaseOpen.selectDataEvent(date);
    }

    public Cursor getEventsAfterDate(String date){
        cDatabaseOpen.open();

        return cDatabaseOpen.selectEventsAfterDate(date);
    }

    public Cursor getEventDates(){
        cDatabaseOpen.open();

        return cDatabaseOpen.selectDate();
    }

    public void deleteEvent(int id){
        cDatabaseOpen.open();

        cDatabaseOpen.deleteColumn(id);
        cDatabaseOpen.close();
    }

    public void updateEvent(Event event){
        cDatabaseOpen.open();

        cDatabaseOpen.updateColumn(event);
        cDatabaseOpen.close();
    }

    public void closeCalendarDB(){
        cDatabaseOpen.close();
    }

    public Cursor getHistoryAll(){
        sDatabaseOpen.open();
        return sDatabaseOpen.selectHistoryAll();
    }

    public void deleteDupAndInsertHistory(String context){
        sDatabaseOpen.open();
        sDatabaseOpen.deleteDuplicateColumn(context);
        sDatabaseOpen.insertColumn(context);
        sDatabaseOpen.close();
    }

    public void deleteHistory(int id){
        sDatabaseOpen.open();
        sDatabaseOpen.deleteColumn(id);
        sDatabaseOpen.close();
    }

    public void deleteHistoryAll(){
        sDatabaseOpen.open();
        sDatabaseOpen.deleteAll();
        sDatabaseOpen.close();
    }

    public void closeHistoryDB(){
        sDatabaseOpen.close();
    }

    //alarm notification
    public void setAlarm(String date, int hour, int min, String title){
        String[] dateSet = date.split("-"); //YYYY-MM-DD

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR,Integer.parseInt(dateSet[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateSet[1])-1); //JAN이 0부터 시작함
        calendar.set(Calendar.DATE,Integer.parseInt(dateSet[2]));
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);

        String dateString = dateSet[0] + dateSet[1] + dateSet[2];

        int id = Integer.parseInt(dateString) * 100;
        id += hour;
        id *= 100;
        id += min;

        alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", title);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if(alarmManager!=null){
            Log.d("alarm", "set");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void unsetAlarm(int requestCode){
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        sendBroadcast(alarmIntent);
    }

    public void googleSignOut(){
        mGoogleSignInClient.signOut();
    }
}
