package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;
import com.grad_proj.assembletickets.front.SubscribeAdapter;
import com.grad_proj.assembletickets.front.SubscribeListDeco;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubscribeFragment extends Fragment {

    View view;
    public RecyclerView subscribeRecyclerView, subscribeShowRecyclerView;
    public TextView subscribeNone;
    public Button totalBtn;
    private SubscribeAdapter subscribeAdapter;
    private SubscribeListDeco subscribeListDeco;
    private ShowAdapter showAdapter;

    private Integer selectPerformerId = null;

    private List<List<String>> totalSubscribeShowList = new ArrayList<>();
    private List<String> subscribeShowList = new ArrayList<>();
    private List<String> subscribeName;
    private List<String> totalShows;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subscribe,container,false);

        subscribeNone = (TextView)view.findViewById(R.id.subscribeNone);

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
                //카테고리 바뀔 때!
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

        showAdapter.setOnItemClickListener(new ShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Log.d("SubscribeFragment","show item clicked");
                //해당 item에 맞는 show에 대한 정보를 서버에 요청해서 받은 뒤 이동하는 page에 정보로 띄울 것
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(ShowDetailFragment.newInstance(showAdapter.getItem(position)));
            }
        });

        totalBtn = (Button)view.findViewById(R.id.totalBtn);
        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
//                ((HomeActivity)getActivity()).replaceFragment(TotalSubscribeFragment.newInstance());
                ((HomeActivity)getActivity()).replaceFragment(EditSubscribeFragment.newInstance());
            }
        });

        new GetSubscribeList().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe/performers");
        new GetPerformerShows().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe/shows");

        return view;
    }

    private void getSubscribeData(List<Performer> subscribePerformer){
        //서버로부터 데이터를 받아오도록 할 것

        for(int i=0; i<subscribePerformer.size(); i++){
            Performer performer = subscribePerformer.get(i);

            //data를 adpater에 추가하
            subscribeAdapter.addItem(performer);
        }

        //adapter값 변경을 알림
        //호출하지 않을 경우 추가된 data가 보여지지 않
        subscribeAdapter.notifyDataSetChanged();
    }

    private void setSubscribeShowList(List<Show> shows){

        if(shows.size()==0){
            subscribeNone.setVisibility(View.VISIBLE);
        }
        else {
            subscribeNone.setVisibility(View.INVISIBLE);
            for(int i=0;i<shows.size();i++){
                Show show = shows.get(i);

                showAdapter.addItem(show);
            }
        }
        showAdapter.notifyDataSetChanged();
    }

    private void changeSubscribeCategory(int position) {
        //선택되는 구독 공연자에 따라서 recycler view의 구성 값을 바꾸도록 함
        Log.d("SubscribeFragment","changeSubscribeCategory()");
        if(!subscribeAdapter.selectedList.get(position,false)){
            Log.d("Subscribe","clicked again");
            selectPerformerId = null;
            new GetPerformerShows().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe/shows");
        }
        else{
            selectPerformerId = subscribeAdapter.getPerformerId(position);
            new GetPerformerShows().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe/shows");
        }
        showAdapter.resetItem();
    }

//    private void setSubscribeShowList(){
//        for(int i=0; i<subscribeShowList.size(); i++){
//            Show show = new Show();
//            show.setTitle(subscribeShowList.get(i));
//            //data를 adpater에 추가하기
//            showAdapter.addItem(show);
//        }
//        showAdapter.notifyDataSetChanged();
//    }

    private class GetSubscribeList extends AsyncTask<String, Void ,List<Performer>> {

        //        List<Show> loadedShows = new ArrayList<>();
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
            getSubscribeData(performers);
        }
    }

    private class GetPerformerShows extends AsyncTask<String, Void ,List<Show>> {

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        //null로 보내면 전체를 받을 수 있음
        @Override
        protected List<Show> doInBackground(String... strings) {
            List<Show> totalShow = new ArrayList<>();

            String strUrl;
            if(selectPerformerId==null){
                strUrl = HttpUrl.parse(strings[0]).newBuilder()
                        .addQueryParameter("email", UserSharedPreference.getUserEmail(getContext()))
                        .addQueryParameter("performerId",null)
                        .build().toString();
            }
            else{
                strUrl = HttpUrl.parse(strings[0]).newBuilder()
                        .addQueryParameter("email", UserSharedPreference.getUserEmail(getContext()))
                        .addQueryParameter("performerId",Integer.toString(selectPerformerId))
                        .build().toString();
            }

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
                totalShow = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return totalShow;
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
            setSubscribeShowList(shows);
        }
    }
}
