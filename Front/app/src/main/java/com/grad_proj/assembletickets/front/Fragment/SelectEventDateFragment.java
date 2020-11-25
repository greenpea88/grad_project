package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

public class SelectEventDateFragment extends Fragment {

    View view;

    private TextView eventTypeText, eventSampleDate;
    private DatePicker eventDatePicker;
    private Button nextBtn;

    private String eventType;
    private String sampleDate;
    private String title;

    public static SelectEventDateFragment newInstance(String eventType, String sampleDate,String title) {
        //이전 fragment로부터 데이터 넘겨받기
        SelectEventDateFragment selectEventDateFragment = new SelectEventDateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventType",eventType);
        bundle.putString("sampleDate", sampleDate);
        bundle.putString("title",title);
        selectEventDateFragment.setArguments(bundle);

        return selectEventDateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_eventdate,container, false);

        if(getArguments() != null){
            eventType = getArguments().getString("eventType");
            sampleDate = getArguments().getString("sampleDate");
            title = getArguments().getString("title");
        }

        eventTypeText = view.findViewById(R.id.eventTypeText);
        eventTypeText.setText(eventType);

        eventSampleDate = view.findViewById(R.id.eventSampleDate);
        eventSampleDate.setText(sampleDate);

        eventDatePicker = view.findViewById(R.id.eventDatePicker);
        nextBtn = view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SelectEventDateFragment","onNextBtnClicked");
                int year = eventDatePicker.getYear();
                int month = eventDatePicker.getMonth()+1;
                int date = eventDatePicker.getDayOfMonth();
                String selectDate = year+"-"+month+"-"+date;
                System.out.println(selectDate);

                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(AddEventFragment.newInstance(selectDate,title));
            }
        });

        return view;
    }
}
