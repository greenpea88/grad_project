package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeEditAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class EditSubscribeFragment extends Fragment {

    View view;
    public RecyclerView editDetailList;
    private SubscribeEditAdapter subscribeEditAdapter;

    public static EditSubscribeFragment newInstance() {
        return new EditSubscribeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editsubscribe,container,false);

        editDetailList = view.findViewById(R.id.editDetailList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        editDetailList.setLayoutManager(linearLayoutManager);

        subscribeEditAdapter = new SubscribeEditAdapter();
        editDetailList.setAdapter(subscribeEditAdapter);

        getEditData();

        return view;
    }

    public void getEditData(){
        List<String> performerName = Arrays.asList("test1","test2","test3","test4");
        List<Boolean> setAlarm = Arrays.asList(true,false,true,false);

        for(int i=0;i<performerName.size();i++){
            Performer performer = new Performer();

            performer.setpName(performerName.get(i));
            performer.setSetAlarm(setAlarm.get(i));

            subscribeEditAdapter.addItem(performer);
        }

        subscribeEditAdapter.notifyDataSetChanged();
    }
}
