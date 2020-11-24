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

import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TicketTotalFragment extends Fragment {

    public View view;
    private ShowAdapter totalShowAdapter;

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


        new GetTotalShows().execute("http://10.0.2.2:8080/assemble-ticket/search?keyword=팬텀");
//        setTotalList();
        return view;
    }

    private void setTotalList(){
        List<String> totalShows = Arrays.asList("test1","test2","test3","test4","test5","test6","test7");
        for(int i=0; i<totalShows.size(); i++){
            Show show = new Show();
            show.setsName(totalShows.get(i));

            //data를 adpater에 추가하
            totalShowAdapter.addItem(show);
        }
    }

    private static class GetTotalShows extends AsyncTask<String, Void ,String>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String strUrl = strings[0];

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
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
//                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
