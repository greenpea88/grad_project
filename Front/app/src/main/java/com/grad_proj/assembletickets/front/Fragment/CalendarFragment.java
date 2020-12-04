package com.grad_proj.assembletickets.front.Fragment;

import android.database.Cursor;
import android.os.AsyncTask;
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
import com.grad_proj.assembletickets.front.CalendarDeco.EventDeco;
import com.grad_proj.assembletickets.front.CalendarDeco.OneDayDeco;
import com.grad_proj.assembletickets.front.Database.CalendarDatabase;
import com.grad_proj.assembletickets.front.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment {

    MaterialCalendarView materialCalendarView;
    View view;

//    private DateFragment dateFragment = new DateFragment();
//    private FragmentManager fragmentManager = getFragmentManager();

    private List<String> eventDates;

    public static CalendarFragment newInstance(){
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(new OneDayDeco(view.getContext()));

        //날짜를 선택했을 때
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth();
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

//                Toast.makeText(getActivity(), shot_Day, Toast.LENGTH_SHORT).show();

                //누른 날짜의 화면으로 전환
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);

                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(DateFragment.newInstance(shot_Day));
            }
        });

        getDates();
        new DotEvent(eventDates).executeOnExecutor(Executors.newSingleThreadExecutor());

        return view;
    }

    private void getDates(){
        Cursor cursor = ((HomeActivity)getActivity()).getEventDates();

        eventDates = new ArrayList<>();

        while(cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex(CalendarDatabase.CalendarDB.EVENTDATE));

            eventDates.add(date);
            System.out.println(date);
        }
        ((HomeActivity)getActivity()).closeCalendarDB();
    }

    private class DotEvent extends AsyncTask<Void, Void, List<CalendarDay>>{

        List<String> eventExistDates;

        DotEvent(List<String> eventExistDates){
            this.eventExistDates=eventExistDates;
        }

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

//            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> days = new ArrayList<>();

            //달력의 특정 날짜에 점 표시하기
            for(int i=0; i<eventExistDates.size();i++){
                String[] date = eventExistDates.get(i).split("-");
                int year=Integer.parseInt(date[0]);
                int month=Integer.parseInt(date[1]);
                int day=Integer.parseInt(date[2]);
//                calendar.set(year,month-1,day);

                CalendarDay calendarDay = CalendarDay.from(year,month,day);
                days.add(calendarDay);
            }

            return days;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

//            if(isFinishing()){
//                return;
//            }
            materialCalendarView.addDecorator(new EventDeco(calendarDays,view.getContext()));
        }
    }
}
