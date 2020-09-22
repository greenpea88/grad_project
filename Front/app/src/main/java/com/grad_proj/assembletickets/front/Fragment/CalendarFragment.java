package com.grad_proj.assembletickets.front.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.grad_proj.assembletickets.front.CalendarDeco.OneDayDeco;
import com.grad_proj.assembletickets.front.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarFragment extends Fragment {

    MaterialCalendarView materialCalendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(new OneDayDeco(rootView));
        return rootView;
    }
}
