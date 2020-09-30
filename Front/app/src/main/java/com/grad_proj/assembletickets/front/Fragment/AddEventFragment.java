package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

public class AddEventFragment extends Fragment {

    public View view;

    Button submitBtn;
    TimePicker eventTimePicker;
    EditText eventTitleEditText;

    private String eventTitle="";

    public static AddEventFragment newInstance() {
        AddEventFragment addEventFragment = new AddEventFragment();
        return addEventFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_addevent,container, false);

        submitBtn = (Button)view.findViewById(R.id.submitBtn);
        eventTimePicker = (TimePicker)view.findViewById(R.id.eventTimePicker);
        eventTimePicker.setIs24HourView(true);
        eventTitleEditText = (EditText)view.findViewById(R.id.eventTitleEditText);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(eventTitle)){
                    Toast toast=Toast.makeText(view.getContext(),"이벤트 제목을 입력해주세요",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else{
                    String eventHour =  eventTimePicker.getCurrentHour().toString();
                    String eventMin = eventTimePicker.getCurrentMinute().toString();
                    ((HomeActivity)getActivity()).submitBtnAction(eventTitle,eventHour+"/"+eventMin);
                }
            }
        });

        eventTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!=null){
                    eventTitle=charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }
}
