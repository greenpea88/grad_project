package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.grad_proj.assembletickets.front.EventSelectDialog;
import com.grad_proj.assembletickets.front.OnSelectDialogListener;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.PerformerAdapter;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowDetailFragment extends Fragment implements OnSelectDialogListener{

    View view;

    private ImageView detailImgPoster;
    private Button addEventBtn;
    private TextView showDetailTitle, showDetailVenue, showDetailDate, showDetailTicketDate,showDetailPrice, showDetailTicketing;
    private TextView nonePerformer;
    private RecyclerView showPerformerList;
    private PerformerAdapter performerAdapter;
    private SubscribeListDeco performerDeco;

//    private List<String> performers;
    private Show show;

    private OnSelectDialogListener listener;

    public static ShowDetailFragment newInstance(Show show) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        ShowDetailFragment showDetailFragment = new ShowDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("show", show);
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

        detailImgPoster = (ImageView)view.findViewById(R.id.detailImgPoster);
        new ImgDownloadTask().execute(show.getPosterSrc());

        showDetailTitle = (TextView)view.findViewById(R.id.showDetailTitle);
        showDetailTitle.setText(show.getTitle());

        showDetailVenue = (TextView)view.findViewById(R.id.showDetailVenue);
        showDetailVenue.setText(show.getVenue());

        showDetailDate = (TextView)view.findViewById(R.id.showDetailDate);
        if(show.getStartDate().equals(show.getEndDate())){
            showDetailDate.setText(show.getStartDate());
        }
        else{
            showDetailDate.setText(show.getStartDate()+" ~ "+show.getEndDate());
        }

        showDetailTicketDate = (TextView)view.findViewById(R.id.showDetailTicketDate);
        if (TextUtils.isEmpty(show.getTicketOpen())){
            showDetailTicketDate.setText("추후 공지 예정");
        }
        else{
            showDetailTicketDate.setText(show.getTicketOpen());
        }

        showDetailPrice = (TextView)view.findViewById(R.id.showDetailPrice);
        showDetailPrice.setText(show.getPrice());

        showDetailTicketing = (TextView)view.findViewById(R.id.showDetailTicketing);
        showDetailTicketing.setText(show.getBuyTicket());

        nonePerformer = (TextView)view.findViewById(R.id.performerNone);

        addEventBtn = (Button)view.findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 보여지고 있는 공연을 캘린더에 등록하도록
                Log.d("ShowDetail","onAddEventBtnClicked()");

                //티켓팅이나 공연 관람을 선택할 dialog 띄우기
                EventSelectDialog  eventSelectDialog = new EventSelectDialog(view.getContext(),show.getTicketOpen());
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

//        getPerformerList();

        performerAdapter.setOnItemClickListener(new PerformerAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Log.d("ShowDetailFragment","performerItemClicked");
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(PerformerDetailFragment.newInstance(performerAdapter.getItem(position)));
            }
        });

        performerDeco = new SubscribeListDeco();
        showPerformerList.addItemDecoration(performerDeco);

        new GetSubscribeList().execute("http://10.0.2.2:8080/assemble-ticket/show");

        return view;
    }

    private void getPerformerList(List<Performer> performers){

        for(int i = 0; i< performers.size(); i++){
            Performer performer = performers.get(i);

            //data를 adpater에 추가
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
        ((HomeActivity)getActivity()).replaceFragment(SelectEventDateFragment.newInstance("티켓팅 날짜 : ",show.getTicketOpen(),show.getTitle()));
//        ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance("2020-11-12",show.getsName()));
    }

    @Override
    public void onWatchSelect() {
        //공연 시작 날짜 / 공연 마지막 날짜 / 공연 이름 넘겨주기
        Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

        ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);

        if(show.getStartDate().equals(show.getEndDate())){
            ((HomeActivity)getActivity()).replaceFragment(SelectEventDateFragment.newInstance("공연 기간 : ",show.getStartDate(),show.getTitle()));
        }
        else{
            ((HomeActivity)getActivity()).replaceFragment(SelectEventDateFragment.newInstance("공연 기간 : ",show.getStartDate()+" ~ "+show.getEndDate(),show.getTitle()));
        }
    }

    private class GetSubscribeList extends AsyncTask<String, Void ,List<Performer>>{

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Performer> doInBackground(String... strings) {
            List<Performer> showPerformer = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("id",Integer.toString(show.getId()))
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("ShowDetailFragment","doInBackground : "+response.body().string());
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("performers");


                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Performer>>() {}.getType();
                showPerformer = gson.fromJson(jsonArray.toString(), listType);

                Log.d("ShowDetailFragment","doInBackground : "+jsonArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return showPerformer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Performer> performers) {
            if(performers.size()==0){
                nonePerformer.setVisibility(View.VISIBLE);
            }
            else{
                getPerformerList(performers);
            }
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
            detailImgPoster.setImageBitmap(bitmap);
        }
    }
}
