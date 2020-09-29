package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.DateEventAdapter;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.R;

import java.util.Arrays;
import java.util.List;


public class DateFragment extends Fragment {

    private static final String DATE = "";
    private String date;

    public View view;
    public RecyclerView eventRecyclerView;
    private DateEventAdapter dateEventAdapter;


    TextView dateTextView;
    Button eventAddBtn;

    public static DateFragment newInstance(String date) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        DateFragment dateFragment = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        dateFragment.setArguments(bundle);

        return dateFragment;
    }

//    public static DateFragment getInstance(){
//        return new DateFragment();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_date,container, false);

        //getActivity()를 통해 불러올 경우 null pointer error가 발생함
        dateTextView = (TextView)view.findViewById(R.id.dateTextView);
        eventAddBtn = (Button)view.findViewById(R.id.eventAddBtn);
        eventRecyclerView = (RecyclerView)view.findViewById(R.id.dateEventList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        eventRecyclerView.setLayoutManager(linearLayoutManager);

        dateEventAdapter = new DateEventAdapter();
        eventRecyclerView.setAdapter(dateEventAdapter);

        if(getArguments() != null){
            date = getArguments().getString(DATE);
            dateTextView.setText(date);
        }

        getData();

        eventAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //fragment 전환
                ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance());
            }
        });

        return view;
    }

    private void getData(){
        //서버로부터 데이터를 받아오도록 할 것
        List<String> eventName = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        List<String> eventTime = Arrays.asList("time1","time2","time2","time4","time5","time6","time7");

        for(int i=0; i<eventName.size(); i++){
            Event event = new Event();
            event.setEventName(eventName.get(i));
            event.setTime(eventTime.get(i));

            //data를 adpater에 추가하
            dateEventAdapter.addItem(event);
        }

        //adapter값 변경을 알림
        //호출하지 않을 경우 추가된 data가 보여지지 않
        dateEventAdapter.notifyDataSetChanged();
    }

}
