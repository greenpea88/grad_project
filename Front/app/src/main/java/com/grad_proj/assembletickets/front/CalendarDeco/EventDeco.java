package com.grad_proj.assembletickets.front.CalendarDeco;

import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class EventDeco implements DayViewDecorator {

//    private final Drawable drawable;
    private int event;

    public EventDeco(int event){
        this.event = event;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {

    }
}
