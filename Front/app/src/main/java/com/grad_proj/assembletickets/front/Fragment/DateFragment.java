package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;


public class DateFragment extends Fragment {

    private static final String DATE = "";
    private String date;

    TextView dateTextView;

    public static DateFragment newInstance(String date) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        DateFragment dateFragment = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        dateFragment.setArguments(bundle);

        return dateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dateTextView = (TextView)getActivity().findViewById(R.id.dateTextView);


        if(getArguments() != null){
            date = getArguments().getString(DATE);
//            dateTextView.setText(date);
        }

        return inflater.inflate(R.layout.fragment_date,container, false);
//        return view;
    }

    public void setTextViewValue(){

        dateTextView.setText(date); //전달 받은 문자열로 TextView의 글씨를 변경

    }
}
