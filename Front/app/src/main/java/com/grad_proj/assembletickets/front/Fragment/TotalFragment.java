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

import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import java.util.Arrays;
import java.util.List;

public class TotalFragment extends Fragment {

    public View view;
    private ShowAdapter totalShowAdapter;

    RecyclerView totalTicketList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total,container, false);

        totalTicketList = (RecyclerView)view.findViewById(R.id.totalTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        totalTicketList.setLayoutManager(linearLayoutManager);

        totalShowAdapter = new ShowAdapter();
        totalTicketList.setAdapter(totalShowAdapter);

//        setTotalList();

        return view;
    }

    private void setTotalList(){
        List<String> totalShows = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        for(int i=0; i<totalShows.size(); i++){
            Show show = new Show();
            show.setsName(totalShows.get(i));

            //data를 adpater에 추가하
            totalShowAdapter.addItem(show);
        }
    }
}
