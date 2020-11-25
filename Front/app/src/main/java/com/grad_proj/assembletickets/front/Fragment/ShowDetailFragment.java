package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.EventSelectDialog;
import com.grad_proj.assembletickets.front.LoadDataDialog;
import com.grad_proj.assembletickets.front.OnSelectDialogListener;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.SubscribeListDeco;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowDetailFragment extends Fragment implements OnSelectDialogListener{

    View view;

    private ImageView detailImgPoster;
    private Button addEventBtn;
    private TextView showDetailTitle, showDetailVenue, showDetailDate, showDetailTicketDate,showDetailPrice;
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
        showDetailDate.setText(show.getStartDate()+"~"+show.getEndDate());

        showDetailTicketDate = (TextView)view.findViewById(R.id.showDetailTicketDate);
        if (TextUtils.isEmpty(show.getTicketOpen())){
            showDetailTicketDate.setText("추후 공지 예정");
        }
        else{
            showDetailTicketDate.setText(show.getTicketOpen());
        }

        showDetailPrice = (TextView)view.findViewById(R.id.showDetailPrice);
        showDetailPrice.setText(show.getPrice());

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

        getPerformerList();

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

        return view;
    }

    private void getPerformerList(){
        performerList = Arrays.asList("test1","test2","test3","test4","test5","test6");

        for(int i=0; i<performerList.size(); i++){
            Performer performer = new Performer();
            performer.setpName(performerList.get(i));

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
        ((HomeActivity)getActivity()).replaceFragment(SelectEventDateFragment.newInstance("티켓팅 날짜",show.getTitle()));
//        ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance("2020-11-12",show.getsName()));
    }

    @Override
    public void onWatchSelect() {
        //공연 시작 날짜 / 공연 마지막 날짜 / 공연 이름 넘겨주기
        Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

        ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
        ((HomeActivity)getActivity()).replaceFragment(SelectEventDateFragment.newInstance("공연 기간",show.getTitle()));
    }

    private class GetSubscribeList extends AsyncTask<String, Void ,List<Performer>>{

        //        List<Show> loadedShows = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<Performer> doInBackground(String... strings) {
            List<Performer> showPerfomer = new ArrayList<>();

            //당겨서 새로고침 test 해보려면 time을 "2020-11-22T15:34:18" 로 넣어서 test해보기
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
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
                showPerfomer = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return showPerfomer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
