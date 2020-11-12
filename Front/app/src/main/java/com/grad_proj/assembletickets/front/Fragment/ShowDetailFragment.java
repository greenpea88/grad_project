package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.EventSelectDialog;
import com.grad_proj.assembletickets.front.OnDialogListener;
import com.grad_proj.assembletickets.front.OnSelectDialogListener;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import java.util.Arrays;
import java.util.List;

public class ShowDetailFragment extends Fragment implements OnSelectDialogListener{

    View view;

    private Button addEventBtn;
    private TextView showDetailTitle;
    private RecyclerView showPerformerList;
    private PerformerAdapter performerAdapter;
    private SubscribeListDeco performerDeco;

    private List<String> performerList;
    private Show show;

    private OnSelectDialogListener listener;

    public static ShowDetailFragment newInstance(Show show) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        ShowDetailFragment showDetailFragment = new ShowDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("show",show);
        showDetailFragment.setArguments(bundle);

        return showDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_detail,container,false);
        listener = this;

        if(getArguments()!=null){
            this.show=(Show)getArguments().getSerializable("show");
        }

        showDetailTitle = (TextView)view.findViewById(R.id.showDetailTitle);
        showDetailTitle.setText(show.getsName());

        addEventBtn = (Button)view.findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 보여지고 있는 공연을 캘린더에 등록하도록
                Log.d("ShowDetail","onAddEventBtnClicked()");

                //티켓팅이나 공연 관람을 선택할 dialog 띄우기
                EventSelectDialog  eventSelectDialog = new EventSelectDialog(view.getContext());
                DisplayMetrics displayMetrics = view.getContext().getApplicationContext().getResources().getDisplayMetrics();

                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                WindowManager.LayoutParams windowManger = eventSelectDialog.getWindow().getAttributes();
                windowManger.copyFrom(eventSelectDialog.getWindow().getAttributes());
                windowManger.width=(int)(width*0.8);
                windowManger.height=height/2;

                eventSelectDialog.setDialogListener(listener);

                eventSelectDialog.show();
            }
        });

        showPerformerList = (RecyclerView)view.findViewById(R.id.showPerformerList);
        //가로형 recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        showPerformerList.setLayoutManager(linearLayoutManager);

        performerAdapter = new PerformerAdapter();
        showPerformerList.setAdapter(performerAdapter);

        performerAdapter.setOnItemClickListener(new PerformerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Log.d("ShowDetailFragment","performerItemClicked");
            }
        });

        performerDeco = new SubscribeListDeco();
        showPerformerList.addItemDecoration(performerDeco);

        getPerformerList();

        return view;
    }

    private void getPerformerList(){
        performerList = Arrays.asList("test1","test2","test3","test4","test5","test6");

        for(int i=0; i<performerList.size(); i++){
            Performer performer = new Performer();
            performer.setpName(performerList.get(i));

            //data를 adpater에 추가하
            performerAdapter.addItem(performer);
        }

        //adapter값 변경을 알림
        //호출하지 않을 경우 추가된 data가 보여지지 않
        performerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTicketingSelect() {
        //티켓팅 날짜와 공연 이름 넘겨주기
        Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

        ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
        ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance("2020-11-12",show.getsName()));
    }

    @Override
    public void onWatchSelect() {
        //공연 시작 날짜 / 공연 마지막 날짜 / 공연 이름 넘겨주기
    }
}
