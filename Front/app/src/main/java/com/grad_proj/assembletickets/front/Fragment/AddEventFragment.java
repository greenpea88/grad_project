package com.grad_proj.assembletickets.front.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

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
        eventTimePicker.setIs24HourView(true);
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
                }
                else{
                    alarmSet=0;
                }

                //db에 새로 추가된 정보 넣기
                ((HomeActivity)getActivity()).insertEvent(date,title,eventContent,eventHour,eventMin,alarmSet);

                //다시 원래 페이지로 돌아오기 -> pop 두 번 필요
                ((HomeActivity)getActivity()).submitBtnAction();
            }
        });
        return view;
    }
}
