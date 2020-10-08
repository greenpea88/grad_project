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
import com.grad_proj.assembletickets.front.ShowAdapter;

public class TicketClassicFragment extends Fragment {

    public View view;
    private ShowAdapter classicShowAdapter;
    RecyclerView classicTicketList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_classic,container, false);

        classicTicketList = (RecyclerView)view.findViewById(R.id.classicTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        classicTicketList.setLayoutManager(linearLayoutManager);

        classicShowAdapter = new ShowAdapter();
        classicTicketList.setAdapter(classicShowAdapter);

        return view;
    }
}
