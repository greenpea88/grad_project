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

public class TicketMusicalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public View view;
    private ShowAdapter musicalShowAdapter;
    private SwipeRefreshLayout.OnRefreshListener listener;
    private SwipeRefreshLayout musicalSwipeToRefresh;

    private String now;
    private int page = 0;

    RecyclerView musicalTicketList;

    LoadDataDialog loadDataDialog = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_musical,container, false);
        listener=this;

        musicalTicketList = (RecyclerView)view.findViewById(R.id.musicalTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        musicalTicketList.setLayoutManager(linearLayoutManager);

        musicalShowAdapter = new ShowAdapter();
        musicalTicketList.setAdapter(musicalShowAdapter);

        //무한 스크롤 관련
        musicalTicketList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //마지막 체크
                if(!musicalTicketList.canScrollVertically(1)){
                    Log.d("TicketTotalFragment","end of Scroll");
                    loadMoreShow();
                }
            }
        });

        musicalSwipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.musicalSwipeToRefresh);
        musicalSwipeToRefresh.setOnRefreshListener(listener);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        now = simpleDateFormat.format(date);

        new GetMusicalShows().execute("http://10.0.2.2:8080/assemble-ticket//shows/type");

        return view;
    }

    private void setMusicalList(List<Show> loadedShows){
//        List<String> totalShows = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        for(int i=0; i<loadedShows.size(); i++){
            Show show = loadedShows.get(i);
//            Log.d("TicketTotalFragment","getData : "+show.getTitle());

            //data를 adpater에 추가하기
            musicalShowAdapter.addItem(show);
        }
        musicalShowAdapter.notifyDataSetChanged();
    }

    private void loadMoreShow(){
        new GetMusicalShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/type");
    }

    private void loadNewShows(){
        new GetNewShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/type/new");
    }

    @Override
    public void onRefresh() {

        loadNewShows();
        musicalSwipeToRefresh.setRefreshing(false);
    }

    private class GetMusicalShows extends AsyncTask<String, Void ,List<Show>> {

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Show> doInBackground(String... strings) {
            List<Show> loadedShows = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("page",Integer.toString(page))
                    .addQueryParameter("time",now)
                    .addQueryParameter("type","1")
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
                loadedShows = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return loadedShows;
        }

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            if(musicalShowAdapter.getItemCount()!=0){
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
                setMusicalList(shows);
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
            String refresh = musicalShowAdapter.getItem(0).getRegisteredTime();

            System.out.println(refresh);

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("time",refresh)
                    .addQueryParameter("type","1")
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
                musicalShowAdapter.addNewItems(newLoadedShows);
                musicalShowAdapter.notifyDataSetChanged();
            }
            else{
                Toast toast = Toast.makeText(view.getContext(),"더 이상 불러올 공연이 없습니다.",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
    }
}
