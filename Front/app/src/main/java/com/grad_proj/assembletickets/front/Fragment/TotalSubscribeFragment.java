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
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeTotalAdapter;

import java.util.Arrays;
import java.util.List;

public class TotalSubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeDetailList;
    private SubscribeTotalAdapter subscribeTotalAdapter;
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

        subscribeTotalAdapter = new SubscribeTotalAdapter();
        subscribeDetailList.setAdapter(subscribeTotalAdapter);

        getTotalSubData();

        editBtn = (Button)view.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).replaceFragment(EditSubscribeFragment.newInstance());
            }
        });

        return view;
    }

    public void getTotalSubData(){
        List<String> totalSub = Arrays.asList("test1","test2","test3","test4","test5");

        for(int i=0;i<totalSub.size();i++){
            Performer performer = new Performer();

            performer.setpName(totalSub.get(i));
            subscribeTotalAdapter.addItem(performer);
        }
        subscribeTotalAdapter.notifyDataSetChanged();
    }
}
