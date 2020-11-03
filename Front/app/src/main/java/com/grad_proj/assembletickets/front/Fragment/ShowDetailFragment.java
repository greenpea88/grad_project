package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import java.util.Arrays;
import java.util.List;

public class ShowDetailFragment extends Fragment {

    View view;

    private Button addEventBtn;
    private RecyclerView showPerformerList;
    private SubscribeAdapter subscribeAdapter;
    private SubscribeListDeco subscribeListDeco;

    private List<String> performerList;

    public static ShowDetailFragment newInstance() {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        ShowDetailFragment showDetailFragment = new ShowDetailFragment();

        return showDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_detail,container,false);

        addEventBtn = (Button)view.findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 보여지고 있는 공연을 캘린더에 등록하도록
                Log.d("ShowDetail","onAddEventBtnClicked()");
            }
        });

        showPerformerList = (RecyclerView)view.findViewById(R.id.showPerformerList);
        //가로형 recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        showPerformerList.setLayoutManager(linearLayoutManager);

        subscribeAdapter = new SubscribeAdapter();
        showPerformerList.setAdapter(subscribeAdapter);

        subscribeAdapter.setOnItemClickListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Log.d("ShowDetail","performerItemClicked()");
            }
        });

        subscribeListDeco = new SubscribeListDeco();
        showPerformerList.addItemDecoration(subscribeListDeco);

        getPerformerList();

        return view;
    }

    private void getPerformerList(){
        performerList = Arrays.asList("test1","test2","test3","test4","test5","test6");

        for(int i=0; i<performerList.size(); i++){
            Performer performer = new Performer();
            performer.setpName(performerList.get(i));

            //data를 adpater에 추가하
            subscribeAdapter.addItem(performer);
        }

        //adapter값 변경을 알림
        //호출하지 않을 경우 추가된 data가 보여지지 않
        subscribeAdapter.notifyDataSetChanged();
    }
}
