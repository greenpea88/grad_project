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

public class MusicalFragment extends Fragment {

    public View view;
    private ShowAdapter musicalShowAdapter;
    RecyclerView musicalTicketList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_musical,container, false);

        musicalTicketList = (RecyclerView)view.findViewById(R.id.musicalTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        musicalTicketList.setLayoutManager(linearLayoutManager);

        musicalShowAdapter = new ShowAdapter();
        musicalTicketList.setAdapter(musicalShowAdapter);

        return view;
    }
}
