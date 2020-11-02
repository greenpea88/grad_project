package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.R;

public class ShowDetailFragment extends Fragment {

    View view;

    public static ShowDetailFragment newInstance() {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        ShowDetailFragment showDetailFragment = new ShowDetailFragment();

        return showDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_detail,container,false);

        return view;
    }
}
