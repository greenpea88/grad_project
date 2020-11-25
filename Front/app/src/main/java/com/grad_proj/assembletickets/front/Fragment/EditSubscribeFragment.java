package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SubscribeEditAdapter;
import com.grad_proj.assembletickets.front.SwipeToDelete;
import com.grad_proj.assembletickets.front.SwipeToDeleteAction;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditSubscribeFragment extends Fragment {

    Button submitEditBtn;

    View view;
    public RecyclerView editDetailList;
    private SubscribeEditAdapter subscribeEditAdapter;

    private int deleteId;

    private SwipeToDelete swipeToDelete = null;

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

        swipeToDelete = new SwipeToDelete(view.getContext(),"subscribe",new SwipeToDeleteAction() {
            @Override
            public void onRightClicked(int position){
                deleteId = subscribeEditAdapter.getPerformerId(position);
                new DeleteSubscribe().execute("http://10.0.2.2:8080/assemble-ticket/subscribe");
                subscribeEditAdapter.removeItem(position);
                subscribeEditAdapter.notifyItemRemoved(position);
                subscribeEditAdapter.notifyItemRangeChanged(position,subscribeEditAdapter.getItemCount());
                //구독 해제 후 서버에도 구독 해제했음을 알리기
            }
        });

        //recycler view에 swipeToDelete 붙이기
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDelete);
        itemTouchHelper.attachToRecyclerView(editDetailList);

        new GetSubscribeList().execute("http://10.0.2.2:8080/assemble-ticket/subscribe/performers");

        //화면에 삭제 버튼 그리기
       editDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
           @Override
           public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
               swipeToDelete.onDraw(c);
           }
       });

        submitEditBtn = (Button)view.findViewById(R.id.submitEditBtn);
        submitEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 setting 값 서버로 보내고
//                ((HomeActivity)getActivity()).replaceFragment(TotalSubscribeFragment.newInstance());
                ((HomeActivity)getActivity()).editSubmitBtnAction();
            }
        });
        return view;
    }

    public void getEditData(List<Performer> performers){

        for(int i=0;i<performers.size();i++){
            Performer performer = performers.get(i);
//            performer.setSetAlarm(setAlarm.get(i));

            subscribeEditAdapter.addItem(performer);
        }

        subscribeEditAdapter.notifyDataSetChanged();
    }

    private class GetSubscribeList extends AsyncTask<String, Void ,List<Performer>> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Performer> doInBackground(String... strings) {
            List<Performer> userSubscribe = new ArrayList<>();

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("email", UserSharedPreference.getUserEmail(getContext()))
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Performer>>() {}.getType();
                userSubscribe = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return userSubscribe;
        }

        @Override
        protected void onPostExecute(List<Performer> performers) {
            getEditData(performers);
        }
    }

    private class DeleteSubscribe extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add("email",UserSharedPreference.getUserEmail(getContext()))
                    .add("performerId",Integer.toString(deleteId))
                    .build();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .delete(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("EditSubscribeFragment","doInBackground : "+response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
