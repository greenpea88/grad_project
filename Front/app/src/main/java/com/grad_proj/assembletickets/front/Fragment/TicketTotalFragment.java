package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TicketTotalFragment extends Fragment {

    public View view;
    private ShowAdapter totalShowAdapter;
    private String now;
    int page = 0;

    RecyclerView totalTicketList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket_total,container, false);

        totalTicketList = (RecyclerView)view.findViewById(R.id.totalTicketList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        totalTicketList.setLayoutManager(linearLayoutManager);

        totalShowAdapter = new ShowAdapter();
        totalTicketList.setAdapter(totalShowAdapter);

        //무한 스크롤 관련
        totalTicketList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        now = simpleDateFormat.format(date);

        new GetTotalShows().execute("http://10.0.2.2:8080/assemble-ticket/shows/all");
        return view;
    }

    private void setTotalList(List<Show> loadedShows){
//        List<String> totalShows = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        for(int i=0; i<loadedShows.size(); i++){
            Show show = loadedShows.get(i);
//            Log.d("TicketTotalFragment","getData : "+show.getTitle());

            //data를 adpater에 추가하
            totalShowAdapter.addItem(show);
        }
        totalShowAdapter.notifyDataSetChanged();
    }

    private class GetTotalShows extends AsyncTask<String, Void ,List<Show>>{

//        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Show> doInBackground(String... strings) {
//            String result = null;
            List<Show> loadedShows = new ArrayList<>();
//            String strUrl = strings[0];
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("page",Integer.toString(page))
                    .addQueryParameter("time",now)
                    .build().toString();


//            HttpUrl httpUrl = new HttpUrl.Builder()
//                    .scheme("http")
//                    .host(strUrl)
//                    .addPathSegment("/assemble-ticket/search")
//                    .addQueryParameter("keyword","팬텀")
//                    .build();

//            System.out.println(httpUrl);

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

//                JSONArray jsonArray = new JSONArray(response.body().string());
//                for(int i=0; i<jsonArray.length(); i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    Show show = new Show();
//                    show.setId(jsonObject.getInt("id"));
//                    show.setTitle(jsonObject.getString("title"));
//                    show.setType(jsonObject.getInt("type"));
//                    show.setStartDate(jsonObject.getString("startDate"));
//                    show.setEndDate(jsonObject.getString("endDate"));
//                    show.setTicketOpen(jsonObject.getString("ticketOpen"));
//                    show.setTime(jsonObject.getString("time"));
//                    show.setRunningTime(jsonObject.getInt("runningTime"));
//                    show.setPrice(jsonObject.getString("price"));
//                    show.setBuyTicket(jsonObject.getString("buyTicket"));
//                    show.setPosterSrc(jsonObject.getString("posterSrc"));
//                    show.setVenue(jsonObject.getString("venue"));
//                    show.setRegisteredTime(jsonObject.getString("registeredTime"));
//
//                    loadedShows.add(show);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return loadedShows;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
//            super.onPostExecute(shows);
            page++;
            setTotalList(shows);
        }
    }
}
