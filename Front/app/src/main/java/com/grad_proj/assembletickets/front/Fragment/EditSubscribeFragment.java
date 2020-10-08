package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeEditAdapter;
import com.grad_proj.assembletickets.front.SwipeToDelete;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class EditSubscribeFragment extends Fragment {

    Button submitEditBtn;

    View view;
    public RecyclerView editDetailList;
    private SubscribeEditAdapter subscribeEditAdapter;

//    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            subscribeEditAdapter.removeItem(position);
//            subscribeEditAdapter.notifyItemRemoved(position);
//        }
//    };

    private SwipeToDelete swipeToDelete = new SwipeToDelete();

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

       ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDelete);
       itemTouchHelper.attachToRecyclerView(editDetailList);

       editDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
           @Override
           public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
               swipeToDelete.onDraw(c);
           }
       });

        getEditData();

        submitEditBtn = (Button)view.findViewById(R.id.submitEditBtn);
        submitEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 setting 값 서버로 보내고
                ((HomeActivity)getActivity()).replaceFragment(TotalSubscribeFragment.newInstance());
            }
        });
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