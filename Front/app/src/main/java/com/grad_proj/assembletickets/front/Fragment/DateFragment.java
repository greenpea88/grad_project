package com.grad_proj.assembletickets.front.Fragment;

import android.database.Cursor;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.CalendarEditDialog;
import com.grad_proj.assembletickets.front.Database.CalendarDatabase;
import com.grad_proj.assembletickets.front.DateEventAdapter;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.OnDialogListener;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.ShowAdapter;
import com.grad_proj.assembletickets.front.SwipeToDelete;
import com.grad_proj.assembletickets.front.SwipeToDeleteAction;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DateFragment extends Fragment implements OnDialogListener {

    private static final String DATE = "";
    private String date;

    public View view;
    public RecyclerView eventRecyclerView;
    private DateEventAdapter dateEventAdapter;

    private SwipeToDelete swipeToDelete = null;
    private OnDialogListener listener;

    private Event updateEvent;
    private int deleteEvent;


    TextView dateTextView,emptyEventTextView;
//    Button eventAddBtn;

    public static DateFragment newInstance(String date) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        DateFragment dateFragment = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        dateFragment.setArguments(bundle);

        return dateFragment;
    }

//    public static DateFragment getInstance(){
//        return new DateFragment();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("DataFragment","onCreateView()");
        view = inflater.inflate(R.layout.fragment_date,container, false);
        listener = this;

        //getActivity()를 통해 불러올 경우 null pointer error가 발생함
        dateTextView = (TextView)view.findViewById(R.id.dateTextView);
        emptyEventTextView=(TextView)view.findViewById(R.id.emptyEventTextView);
        eventRecyclerView = (RecyclerView)view.findViewById(R.id.dateEventList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        eventRecyclerView.setLayoutManager(linearLayoutManager);

        dateEventAdapter = new DateEventAdapter();
        eventRecyclerView.setAdapter(dateEventAdapter);

        swipeToDelete = new SwipeToDelete(view.getContext(), "event",new SwipeToDeleteAction() {
            @Override
            public void onRightClicked(int position) {
                deleteEvent = dateEventAdapter.getEventId(position);
                int removeId = dateEventAdapter.removeItem(position);
                dateEventAdapter.notifyItemRemoved(position);
                dateEventAdapter.notifyItemRangeChanged(position,dateEventAdapter.getItemCount());
                new DeleteEvent().execute("https://ticketdate.azurewebsites.net/assemble-ticket/calendar");
                //자체 db에 알릴 것
                ((HomeActivity)getActivity()).deleteEvent(removeId);
                if(dateEventAdapter.getItemCount()==0){
                    emptyEventTextView.setVisibility(View.VISIBLE);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                //수정할 dialog 띄우기

                CalendarEditDialog calendarEditDialog = new CalendarEditDialog(view.getContext(),position,dateEventAdapter.getItem(position));

                DisplayMetrics displayMetrics = view.getContext().getApplicationContext().getResources().getDisplayMetrics();

                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                WindowManager.LayoutParams windowManger = calendarEditDialog.getWindow().getAttributes();
                windowManger.copyFrom(calendarEditDialog.getWindow().getAttributes());
                windowManger.width=(int)(width*0.7);
                windowManger.height=(height/4)*3;

                calendarEditDialog.setDialogListener(listener);

                calendarEditDialog.show();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDelete);
        itemTouchHelper.attachToRecyclerView(eventRecyclerView);

        eventRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeToDelete.onDraw(c);
            }
        });

        if(getArguments() != null){
            date = getArguments().getString(DATE);
            dateTextView.setText(date);
        }

        getData();
        if(dateEventAdapter.getItemCount()==0){
            emptyEventTextView.setVisibility(View.VISIBLE);
        }

//        eventAddBtn = (Button)view.findViewById(R.id.eventAddBtn);
//        eventAddBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                //fragment 전환
//                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
//
//                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
//                ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance(date));
//            }
//        });

        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.d("DataFragment","onCreate()");
//        super.onCreate(savedInstanceState);
//    }

    //    public void addEvent(Event event){
//        //List에 새로운 값 추가하기
//        eventName.add(event.getEventName());
//        eventTime.add(event.getTime());
//    }

    private void getData(){
        //내부 디비에서 데이터 가져오기
        Log.d("DateFragement",date);
        Cursor cursor=((HomeActivity)getActivity()).getDateEvents(date);

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(CalendarDatabase.CalendarDB._ID));
            String eventName = cursor.getString(cursor.getColumnIndex(CalendarDatabase.CalendarDB.EVENTNAME));
            String eventContent = cursor.getString(cursor.getColumnIndex(CalendarDatabase.CalendarDB.EVENTCONTENT));
            int hour = cursor.getInt(cursor.getColumnIndex(CalendarDatabase.CalendarDB.HOUR));
            int min = cursor.getInt(cursor.getColumnIndex(CalendarDatabase.CalendarDB.MINUTE));
            int alarmSet = cursor.getInt(cursor.getColumnIndex(CalendarDatabase.CalendarDB.ALARMSET));
            int showId = cursor.getInt(cursor.getColumnIndex(CalendarDatabase.CalendarDB.SHOWID));

            Event event = new Event();
            event.setId(id);
            event.setEventName(eventName);
            event.setEventContent(eventContent);
            event.setTimeHour(hour);
            event.setTimeMin(min);
            event.setAlarmSet(alarmSet);
            event.setShowId(showId);

            dateEventAdapter.addItem(event);
        }
        ((HomeActivity)getActivity()).closeCalendarDB();
        dateEventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinish(int position, Event event) {
        Log.d("DateFragment","Dialog finish");

        updateEvent=event;
        updateEvent.setDate(date);
        new UpdateEvent().execute("https://ticketdate.azurewebsites.net/assemble-ticket/calendar");

        dateEventAdapter.changeItem(position,event);
        dateEventAdapter.notifyDataSetChanged();

//        dateEventAdapter.setOnItemClickListener(new DateEventAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClicked(View v, int position) {
//                Log.d("SubscribeFragment","show item clicked");
//                //해당 item에 맞는 show에 대한 정보를 서버에 요청해서 받은 뒤 이동하는 page에 정보로 띄울 것
//                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
//                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
//                ((HomeActivity)getActivity()).replaceFragment(ShowDetailFragment.newInstance(dateEventAdapter.getItem(position)));
//            }
//        });
    }

    private class UpdateEvent extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            String hour;
            if(Integer.toString(updateEvent.getTimeHour()).length()<2){
                hour = "0"+updateEvent.getTimeHour();
            }
            else{
                hour = Integer.toString(updateEvent.getTimeHour());
            }

            String minute;
            if(Integer.toString(updateEvent.getTimeMin()).length()<2){
                minute = "0"+updateEvent.getTimeMin();
            }
            else{
                minute = Integer.toString(updateEvent.getTimeMin());
            }

            String content;
            if(TextUtils.isEmpty(updateEvent.getEventContent())){
                content="";
            }
            else{
                content=updateEvent.getEventContent();
            }

            String postJson = "{\n" +
                    "  \"calId\" : "+updateEvent.getId()+",\n" +
                    "  \"calDate\" : \""+updateEvent.getDate()+"\",\n" +
                    "  \"calTime\" : \""+hour+":"+minute+":00"+"\",\n" +
                    "  \"calTitle\" : \""+updateEvent.getEventName()+"\",\n" +
                    "  \"calMemo\" : \""+content+"\",\n" +
                    "  \"alarmSet\" : "+updateEvent.getAlarmSet()+"\n"+
                    "}";

            System.out.println(postJson);

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    postJson
            );

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .put(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("TicketTotalFragment","doInBackground : "+response.body().string());
//                Gson gson = new Gson();
//
//                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
//                newLoadedShows = gson.fromJson(response.body().string(), listType);
//                System.out.println(newLoadedShows.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((HomeActivity)getActivity()).updateEvent(updateEvent);
        }
    }

    private class DeleteEvent extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add("calId",Integer.toString(deleteEvent))
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
