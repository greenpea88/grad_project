package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeAdapter;

public class TotalSubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeDetailList;
    Button editBtn;

    public static TotalSubscribeFragment newInstance(){
        return new TotalSubscribeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_totalsubscribe,container,false);

        subscribeDetailList = (RecyclerView)view.findViewById(R.id.subscribeDetailList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        subscribeDetailList.setLayoutManager(linearLayoutManager);



        editBtn = (Button)view.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(EditSubscribeFragment.newInstance());
            }
        });

        return view;
    }
}
