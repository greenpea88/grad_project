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

import java.util.Calendar;

public class SelectEventDateFragment extends Fragment {

    View view;

    private TextView eventTypeText, eventSampleDate;
    private DatePicker eventDatePicker;
    private Button nextBtn;

    private String sampleDate;
    private String title;

    private Calendar minDate = Calendar.getInstance();
    private Calendar maxDate = Calendar.getInstance();

    public static SelectEventDateFragment newInstance(String sampleDate, String title) {
        //이전 fragment로부터 데이터 넘겨받기
        SelectEventDateFragment selectEventDateFragment = new SelectEventDateFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("eventType",eventType);
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
//            eventType = getArguments().getString("eventType");
            sampleDate = getArguments().getString("sampleDate");
            title = getArguments().getString("title");
        }

//        eventTypeText = view.findViewById(R.id.eventTypeText);
//        eventTypeText.setText(eventType);
//
//        eventSampleDate = view.findViewById(R.id.eventSampleDate);
//        eventSampleDate.setText(sampleDate);

        eventDatePicker = view.findViewById(R.id.eventDatePicker);
        String[] dates = sampleDate.split(" ~ ");
        if(dates.length!=2){
            String[] date = dates[0].split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1])-1;
            int day = Integer.parseInt(date[2]);
            minDate.set(year,month,day);
            eventDatePicker.setMinDate(minDate.getTime().getTime());

            maxDate.set(year,month,day);
            eventDatePicker.setMaxDate(maxDate.getTime().getTime());
        }
        else{
            String[] startDate = dates[0].split("-");
            int startYear = Integer.parseInt(startDate[0]);
            int startMonth = Integer.parseInt(startDate[1])-1;
            int startDay = Integer.parseInt(startDate[2]);
            minDate.set(startYear,startMonth,startDay);
            eventDatePicker.setMinDate(minDate.getTime().getTime());

            String[] endDate = dates[1].split("-");
            int endYear = Integer.parseInt(endDate[0]);
            int endMonth = Integer.parseInt(endDate[1])-1;
            int endDay = Integer.parseInt(endDate[2]);
            maxDate.set(endYear,endMonth,endDay);
            eventDatePicker.setMaxDate(maxDate.getTime().getTime());
        }

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
