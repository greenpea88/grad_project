package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.grad_proj.assembletickets.front.UserSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PerformerDetailFragment extends Fragment {

    View view;

    private ImageView performerImg;
    private TextView performerName;
    private ImageView bookmarkSet;
    private RecyclerView showListView;
    private ShowAdapter showAdapter;

    private Performer performer;

    private boolean setBookmark = false;

    public static PerformerDetailFragment newInstance(Performer performer) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        PerformerDetailFragment performerDetailFragment = new PerformerDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("performer", performer);
        performerDetailFragment.setArguments(bundle);

        return performerDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_performer_detail,container,false);

        if(getArguments()!=null){
            this.performer=(Performer)getArguments().getSerializable("performer");
        }

        performerImg = view.findViewById(R.id.performerImg);
        new ImgDownloadTask().execute(performer.getImgSrc());

        performerName = view.findViewById(R.id.performerName);
        performerName.setText(performer.getName());

        bookmarkSet = view.findViewById(R.id.bookmarkSet);
        new CheckSubscribe().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe");
//        if(!setBookmark){
//            bookmarkSet.setImageResource(R.drawable.icon_bookmarkoff);
//        }
//        else{
//            bookmarkSet.setImageResource(R.drawable.icon_bookmarkon);
//        }
        bookmarkSet.setColorFilter(Color.parseColor("#F25E3D"), PorterDuff.Mode.SRC_IN);
        bookmarkSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PerformerDetailFragment","subscribe Clicked");
                if(!setBookmark){
                    setBookmark=true;
                    bookmarkSet.setImageResource(R.drawable.icon_bookmarkon);
                    new SetSubscribe().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe");
                }
                else{
                    setBookmark=false;
                    bookmarkSet.setImageResource(R.drawable.icon_bookmarkoff);
                    new DeleteSubscribe().execute("https://ticketdate.azurewebsites.net/assemble-ticket/subscribe");
                }
            }
        });
        //TODO: set 되어있는 것을 확인하는 api 되면 추가할 것

        showListView = view.findViewById(R.id.showList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        showListView.setLayoutManager(linearLayoutManager);

        showAdapter = new ShowAdapter();
        showListView.setAdapter(showAdapter);

        showAdapter.setOnItemClickListener(new ShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                //show detail fragment call
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(ShowDetailFragment.newInstance(showAdapter.getItem(position)));
            }
        });

        new GetPerformerDetail().execute("https://ticketdate.azurewebsites.net/assemble-ticket/performer");

        return view;
    }

    private void setShowList(List<Show> performShow){
//        showList = Arrays.asList("test1","test2","test3","test4","test5","test6");
        for(int i = 0; i < performShow.size(); i++){
            Show show = performShow.get(i);

            //data를 adpater에 추가
            showAdapter.addItem(show);
        }
        showAdapter.notifyDataSetChanged();
    }

    private class GetPerformerDetail extends AsyncTask<String, Void ,List<Show>>{

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Show> doInBackground(String... strings) {
            List<Show> performShows = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("id",Integer.toString(performer.getId()))
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("shows");
                Gson gson = new Gson();
//
                Type listType = new TypeToken<List<Show>>() {}.getType();
                performShows = gson.fromJson(jsonArray.toString(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return performShows;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Show> shows) {
//            super.onPostExecute(shows);
            setShowList(shows);
        }
    }

    private class CheckSubscribe extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("email",UserSharedPreference.getUserEmail(getContext()))
                    .addQueryParameter("performerId",Integer.toString(performer.getId()))
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("PerformerDetailFragment","doInBackground : "+response.body().string());
                if("false".equals(response.body().string())){
                    setBookmark = false;
                }
                else{
                    setBookmark = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            if(!setBookmark){
                bookmarkSet.setImageResource(R.drawable.icon_bookmarkoff);
            }
            else{
                bookmarkSet.setImageResource(R.drawable.icon_bookmarkon);
            }
        }
    }

    private class SetSubscribe extends AsyncTask<String, Void, Void> {

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {
            List<Show> performShows = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add("email",UserSharedPreference.getUserEmail(getContext()))
                    .add("performerId",Integer.toString(performer.getId()))
                    .build();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("PerformerDetailFragment","doInBackground : "+response.body().string());
//                JSONObject jsonObject = new JSONObject(response.body().string());
//                JSONArray jsonArray = jsonObject.getJSONArray("shows");
//                Gson gson = new Gson();
////
//                Type listType = new TypeToken<List<Show>>() {}.getType();
//                performShows = gson.fromJson(jsonArray.toString(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }private class DeleteSubscribe extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add("email",UserSharedPreference.getUserEmail(getContext()))
                    .add("performerId",Integer.toString(performer.getId()))
                    .build();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .delete(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("PerformerDetailFragment","doInBackground : "+response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



    private class ImgDownloadTask extends AsyncTask<String,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                String imgUrl = strings[0];
                URL url = new URL(imgUrl);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            performerImg.setImageBitmap(bitmap);
        }
    }
}
