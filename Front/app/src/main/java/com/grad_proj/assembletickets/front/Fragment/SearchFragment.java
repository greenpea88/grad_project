package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

public class SearchFragment extends Fragment {


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container,false);
        return view;
    }

    @Override
    public void onDestroyView() {
        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        super.onDestroyView();
    }


}
