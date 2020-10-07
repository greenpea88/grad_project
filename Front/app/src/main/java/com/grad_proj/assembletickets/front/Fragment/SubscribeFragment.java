package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;
import com.grad_proj.assembletickets.front.Subscribe;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeRecyclerView, subscribeShowRecyclerView;
    public Button totalBtn;
    private SubscribeAdapter subscribeAdapter;
    private SubscribeListDeco subscribeListDeco;
    private ShowAdapter showAdapter;

    private List<List<String>> totalSubscribeShowList = new ArrayList<>();
    private List<String> subscribeShowList = new ArrayList<>();
    private List<String> subscribeName;

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

        subscribeAdapter.setOnItemClickListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Log.d("SubscribeFragment","performer item clicked");
                changeSubscribeCategory(position);
            }
        });

        subscribeListDeco = new SubscribeListDeco();
        subscribeRecyclerView.addItemDecoration(subscribeListDeco);

        //구독하는 배우의 공연 리스트
        subscribeShowRecyclerView = (RecyclerView)view.findViewById(R.id.subscribeShowList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(view.getContext());
        subscribeShowRecyclerView.setLayoutManager(linearLayoutManager1);

        showAdapter = new ShowAdapter();
        subscribeShowRecyclerView.setAdapter(showAdapter);

        totalBtn = (Button)view.findViewById(R.id.totalBtn);
        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(TotalSubscribeFragment.newInstance());
            }
        });

        getSubscribeData();
        getSubscribeShowData();

        return view;
    }

    private void getSubscribeData(){
        //서버로부터 데이터를 받아오도록 할 것
        subscribeName = Arrays.asList("test1","test2","test3","test4","test5","test6","test7","test8","test9","test10","test11");

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
        //일단 서버로부터 데이터를 받아옴 -> 이중 리스트의 형태로 저장
        //처음에는 total을 보여줌


        List<String> totalShows = Arrays.asList("total1","total2","total3","total4","total5","total6","total7");
        subscribeShowList = totalShows;

        for(int i=0; i<subscribeName.size(); i++){
            List<String> temp = Arrays.asList("test1","test2","test3","test4");

            totalSubscribeShowList.add(temp);
        }
        setSubscribeShowList();
    }

    private void changeSubscribeCategory(int position) {
        //선택되는 구독 공연자에 따라서 recycler view의 구성 값을 바꾸도록 함
        Log.d("SubscribeFragment","changeSubscribeCategory()");
        subscribeShowList = totalSubscribeShowList.get(position);
        showAdapter.resetItem();
        setSubscribeShowList();
    }

    private void setSubscribeShowList(){
        for(int i=0; i<subscribeShowList.size(); i++){
            Show show = new Show();
            show.setsName(subscribeShowList.get(i));

            //data를 adpater에 추가하기
            showAdapter.addItem(show);
        }
        showAdapter.notifyDataSetChanged();
    }
}
