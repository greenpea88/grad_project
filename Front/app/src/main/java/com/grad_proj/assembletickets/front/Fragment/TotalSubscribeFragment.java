package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.R;

public class TotalSubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeDetailList;

    public static TotalSubscribeFragment newInstance(){
        return new TotalSubscribeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_totalsubscribe,container,false);

        subscribeDetailList = (RecyclerView)view.findViewById(R.id.subscribeDetailList);

        return view;
    }
}
