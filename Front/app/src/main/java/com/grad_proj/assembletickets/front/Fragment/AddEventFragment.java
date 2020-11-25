package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.UserSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddEventFragment extends Fragment {

    public View view;

    private static final String DATE="date";
    private String date;
    private String title;
    private int showId;

    Button submitBtn;
    TimePicker eventTimePicker;
    TextView eventTitleText;
    EditText eventContentEditText;
    SwitchMaterial alarmSwitch;

    private Event postEvent = new Event();
    private int calId;
//    private String eventTitle="";

    public static AddEventFragment newInstance(String date,String title,int id) {
        //이전 fragment로부터 데이터 넘겨받기
        AddEventFragment addEventFragment = new AddEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        bundle.putString("title",title);
        bundle.putInt("showId",id);
        addEventFragment.setArguments(bundle);

        return addEventFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_addevent,container, false);

        if(getArguments() != null){
            date = getArguments().getString(DATE);
            title = getArguments().getString("title");
            showId = getArguments().getInt("showId");
        }

        submitBtn = (Button)view.findViewById(R.id.submitBtn);
        eventTimePicker = (TimePicker)view.findViewById(R.id.eventTimePicker);
//        eventTimePicker.setIs24HourView(true);
        eventTitleText = (TextView)view.findViewById(R.id.eventTitleText);
        eventTitleText.setText(title);
//        eventTitleEditText = (EditText)view.findViewById(R.id.eventTitleEditText);
//        eventTitleEditText.setText(title);
        eventContentEditText = (EditText)view.findViewById(R.id.eventContentEditText);
        alarmSwitch = (SwitchMaterial)view.findViewById(R.id.alarmSwitch);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
//                if("".equals(eventTitle)){
//                    Toast toast=Toast.makeText(view.getContext(),"이벤트 제목을 입력해주세요",Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER,0,0);
//                    toast.show();
//                }
//                else{
//                    int eventHour =  eventTimePicker.getHour();
//                    int eventMin = eventTimePicker.getMinute();
//                    String eventContent = eventContentEditText.getText().toString();
//
//                    //db에 새로 추가된 정보 넣기
//                    ((HomeActivity)getActivity()).insertEvent(date,eventTitle,eventContent,eventHour,eventMin);
//                    ((HomeActivity)getActivity()).submitBtnAction();
//                }
                int eventHour =  eventTimePicker.getHour();
                int eventMin = eventTimePicker.getMinute();
                String eventContent = eventContentEditText.getText().toString();
                int alarmSet;
                if(alarmSwitch.isChecked()){
                    //true일 경우
                    alarmSet=1;
                    ((HomeActivity)getActivity()).setAlarm(date, eventHour, eventMin, title);
                }
                else{
                    alarmSet=0;
                }

                //새로 추가된 정보 서버에도 넣기
                postEvent.setDate(date);
                postEvent.setTimeHour(eventHour);
                postEvent.setTimeMin(eventMin);
                postEvent.setEventName(title);
                postEvent.setEventContent(eventContent);
                postEvent.setAlarmSet(alarmSet);

                new PostEvent().execute("http://10.0.2.2:8080/assemble-ticket/calendar");
            }
        });
        return view;
    }

    private void postDB(){
        ((HomeActivity)getActivity())
                .insertEvent(calId,date,title,postEvent.getEventContent(),postEvent.getTimeHour(),postEvent.getTimeMin(),postEvent.getAlarmSet(),showId);
        //다시 원래 페이지로 돌아오기 -> pop 두 번 필요
        ((HomeActivity)getActivity()).submitBtnAction();
    }

    private class PostEvent extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();
        @Override
        protected Void doInBackground(String... strings) {
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

            String hour;
            if(Integer.toString(postEvent.getTimeHour()).length()<2){
                hour = "0"+postEvent.getTimeHour();
            }
            else{
                hour = Integer.toString(postEvent.getTimeHour());
            }

            String minute;
            if(Integer.toString(postEvent.getTimeMin()).length()<2){
                minute = "0"+postEvent.getTimeMin();
            }
            else{
                minute = Integer.toString(postEvent.getTimeMin());
            }

            String content;
            if(TextUtils.isEmpty(postEvent.getEventContent())){
                content="";
            }
            else{
                content=postEvent.getEventContent();
            }

            String postJson = "{\n" +
                    "  \"email\" : \""+UserSharedPreference.getUserEmail(getContext())+"\",\n" +
                    "  \"showId\" : "+showId+",\n" +
                    "  \"calDate\" : \""+postEvent.getDate()+"\",\n" +
                    "  \"calTime\" : \""+hour+":"+minute+":00"+"\",\n" +
                    "  \"calTitle\" : \""+postEvent.getEventName()+"\",\n" +
                    "  \"calMemo\" : \""+content+"\",\n" +
                    "  \"alarmSet\" : "+postEvent.getAlarmSet()+"\n" +
                    "  \n" +
                    "}";

            System.out.println(postJson);
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    postJson
            );

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
//                Log.d("AddEventFragment","doInBackground : "+response.body().string());
                JSONObject jsonObject = new JSONObject(response.body().string());
                calId = jsonObject.getInt("id");

//                Gson gson = new Gson();
//
//                Type listType = new TypeToken<ArrayList<Show>>() {}.getType();
//                newLoadedShows = gson.fromJson(response.body().string(), listType);
//                System.out.println(newLoadedShows.size());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            //db에 새로 추가된 정보 넣기
            postDB();
        }
    }
}
