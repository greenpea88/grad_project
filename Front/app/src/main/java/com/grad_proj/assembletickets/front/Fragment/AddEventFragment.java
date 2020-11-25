package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Event;
import com.grad_proj.assembletickets.front.LoadDataDialog;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    Button submitBtn;
    TimePicker eventTimePicker;
    TextView eventTitleText;
    EditText eventContentEditText;
    SwitchMaterial alarmSwitch;

    private String postJson;
//    private String eventTitle="";

    public static AddEventFragment newInstance(String date,String title) {
        //이전 fragment로부터 데이터 넘겨받기
        AddEventFragment addEventFragment = new AddEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        bundle.putString("title",title);
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

                //db에 새로 추가된 정보 넣기
                ((HomeActivity)getActivity()).insertEvent(date,title,eventContent,eventHour,eventMin,alarmSet);

                //새로 추가된 정보 서버에도 넣기
                Event event = new Event();
                event.setDate(date);
                event.setTimeHour(eventHour);
                event.setTimeMin(eventMin);
                event.setEventName(title);
                event.setEventContent(eventContent);
                event.setAlarmSet(alarmSet);

                Gson gson = new Gson();
                postJson = gson.toJson(event);

                new UpdateEvent().execute("http://10.0.2.2:8080/assemble-ticket/calendar");

                //다시 원래 페이지로 돌아오기 -> pop 두 번 필요
                ((HomeActivity)getActivity()).submitBtnAction();
            }
        });
        return view;
    }

    private class UpdateEvent extends AsyncTask<String, Void ,Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {
            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .build().toString();

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
    }
}
