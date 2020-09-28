package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.R;

public class AddEventFragment extends Fragment {

    public View view;

    Button submitBtn;

    public static AddEventFragment newInstance() {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        AddEventFragment addEventFragment = new AddEventFragment();

        return addEventFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_addevent,container, false);

        submitBtn = (Button)view.findViewById(R.id.submitBtn);
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return view;
    }
}