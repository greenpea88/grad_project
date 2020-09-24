package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.CalendarDeco.OneDayDeco;
import com.grad_proj.assembletickets.front.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class CalendarFragment extends Fragment {

    MaterialCalendarView materialCalendarView;

    private DateFragment dateFragment = new DateFragment();
    private FragmentManager fragmentManager = getFragmentManager();

    public static CalendarFragment newInstance(){
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(new OneDayDeco(rootView));

        //날짜를 선택했을 때
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "," + Month + "," + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

//                Toast.makeText(getActivity(), shot_Day, Toast.LENGTH_SHORT).show();

                //누른 날짜의 화면으로 전환
                ((HomeActivity)getActivity()).replaceFragment(DateFragment.newInstance(shot_Day));
            }
        });
        return rootView;
    }
}
