package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;
import com.grad_proj.assembletickets.front.Subscribe;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import java.util.Arrays;
import java.util.List;

public class SubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeRecyclerView, subscribeShowRecyclerView;
    private SubscribeAdapter subscribeAdapter;
    private SubscribeListDeco subscribeListDeco;
    private ShowAdapter showAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subscribe,container,false);

        //구독하는 배우 리스트
        subscribeRecyclerView = (RecyclerView)view.findViewById(R.id.subscribeList);
        //가로형 recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        subscribeRecyclerView.setLayoutManager(linearLayoutManager);

        subscribeAdapter = new SubscribeAdapter();
        subscribeRecyclerView.setAdapter(subscribeAdapter);

        subscribeListDeco = new SubscribeListDeco();
        subscribeRecyclerView.addItemDecoration(subscribeListDeco);

        //구독하는 배우의 공연 리스트
        subscribeShowRecyclerView = (RecyclerView)view.findViewById(R.id.subscribeShowList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(view.getContext());
        subscribeShowRecyclerView.setLayoutManager(linearLayoutManager1);

        showAdapter = new ShowAdapter();
        subscribeShowRecyclerView.setAdapter(showAdapter);

        getSubscribeData();
//        getSubscribeShowData();

        return view;
    }

    private void getSubscribeData(){
        //서버로부터 데이터를 받아오도록 할 것
        List<String> subscribeName = Arrays.asList("test1","test2","test3","test4","test5","test6","test7","test8","test9","test10","test11");

        for(int i=0; i<subscribeName.size(); i++){
            Subscribe subscribe = new Subscribe();
            subscribe.setSubscribeName(subscribeName.get(i));

            //data를 adpater에 추가하
            subscribeAdapter.addItem(subscribe);
        }

        //adapter값 변경을 알림
        //호출하지 않을 경우 추가된 data가 보여지지 않
        subscribeAdapter.notifyDataSetChanged();
    }

    private void getSubscribeShowData(){
        List<String> totalShows = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        for(int i=0; i<totalShows.size(); i++){
            Show show = new Show();
            show.setsName(totalShows.get(i));

            //data를 adpater에 추가하기
            showAdapter.addItem(show);
        }
        showAdapter.notifyDataSetChanged();
    }
}
