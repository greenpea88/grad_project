package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

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

    private String eventTitle;

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
        eventTitleEditText = (EditText)view.findViewById(R.id.eventTitleEditText);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((HomeActivity)getActivity()).submitBtnAction();
            }
        });

        return view;
    }
}
