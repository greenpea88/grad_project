package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.LoadDataDialog;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TicketConcertFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public View view;
    private ShowAdapter concertShowAdapter;
    private SwipeRefreshLayout concertSwipeToRefresh;
    RecyclerView concertTicketList;

    LoadDataDialog loadDataDialog;

    private int page = 0;
    private String now;
    private String type = "2";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_concert,container, false);

        concertTicketList = (RecyclerView)view.findViewById(R.id.concertTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        concertTicketList.setLayoutManager(linearLayoutManager);

        concertShowAdapter = new ShowAdapter();
        concertTicketList.setAdapter(concertShowAdapter);

        //무한 스크롤 관련
        concertTicketList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //마지막 체크
                if(!concertTicketList.canScrollVertically(1)){
                    loadMoreShow();
                }
            }
        });

        concertSwipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.concertSwipeToRefresh);
        concertSwipeToRefresh.setOnRefreshListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        now = simpleDateFormat.format(date);

        new GetConcertShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/type");

        return view;
    }

    private void setConcertList(List<Show> loadedShows){
        for(int i=0; i<loadedShows.size(); i++){
            Show show = loadedShows.get(i);

            //data를 adpater에 추가하기
            concertShowAdapter.addItem(show);
        }
        concertShowAdapter.notifyDataSetChanged();
    }

    private void loadMoreShow(){
        new GetConcertShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/type");
    }

    private void loadNewShows(){
        new GetNewShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/type/new");
    }

    @Override
    public void onRefresh() {

        loadNewShows();
        concertSwipeToRefresh.setRefreshing(false);
    }

    private class GetConcertShows extends AsyncTask<String, Void ,List<Show>> {

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Show> doInBackground(String... strings) {
            List<Show> loadedShows = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("page",Integer.toString(page))
                    .addQueryParameter("time",now)
                    .addQueryParameter("type",type)
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
                loadedShows = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return loadedShows;
        }

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            if(concertShowAdapter.getItemCount()!=0){
                loadDataDialog = new LoadDataDialog(view.getContext(),"더 불러오는 중...");
            }
            else{
                loadDataDialog = new LoadDataDialog(view.getContext(),"공연 정보 불러오는 중...");
            }

            DisplayMetrics displayMetrics = view.getContext().getApplicationContext().getResources().getDisplayMetrics();

            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            WindowManager.LayoutParams windowManger = loadDataDialog.getWindow().getAttributes();
            windowManger.copyFrom(loadDataDialog.getWindow().getAttributes());
            windowManger.width=(int)(width*0.8);
            windowManger.height=(int)(height*0.2);

            loadDataDialog.show();
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
//            super.onPostExecute(shows);
            page++;
            loadDataDialog.dismiss();
            if(shows.size()!=0){
                setConcertList(shows);
            }
            else{
                Toast toast = Toast.makeText(view.getContext(),"더 이상 불러올 공연이 없습니다.",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
    }

    private class GetNewShows extends AsyncTask<String, Void ,List<Show>>{

        OkHttpClient client = new OkHttpClient();
        List<Show> newLoadedShows;

        @Override
        protected List<Show> doInBackground(String... strings) {
            newLoadedShows = new ArrayList<>();

            //recyclerView 가장 상단 Item의 시간 받아오기
            String refresh = concertShowAdapter.getItem(0).getRegisteredTime();

            System.out.println(refresh);

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("time",refresh)
                    .addQueryParameter("type",type)
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
                newLoadedShows = gson.fromJson(response.body().string(), listType);
//                System.out.println(newLoadedShows.size());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(newLoadedShows.size());
            return newLoadedShows;
        }

        @Override
        protected void onPreExecute() {
            loadDataDialog = new LoadDataDialog(view.getContext(),"더 불러오는 중...");

            DisplayMetrics displayMetrics = view.getContext().getApplicationContext().getResources().getDisplayMetrics();

            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            WindowManager.LayoutParams windowManger = loadDataDialog.getWindow().getAttributes();
            windowManger.copyFrom(loadDataDialog.getWindow().getAttributes());
            windowManger.width=(int)(width*0.8);
            windowManger.height=(int)(height*0.2);

            loadDataDialog.show();
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
            super.onPostExecute(shows);
            loadDataDialog.dismiss();
            System.out.println(shows.size());
            if(newLoadedShows.size()!=0){
                //현재 리스트 앞쪽으로 붙여야함
                concertShowAdapter.addNewItems(newLoadedShows);
                concertShowAdapter.notifyDataSetChanged();
            }
            else{
                Toast toast = Toast.makeText(view.getContext(),"더 이상 불러올 공연이 없습니다.",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
    }
}
