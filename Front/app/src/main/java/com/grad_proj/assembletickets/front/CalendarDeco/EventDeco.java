package com.grad_proj.assembletickets.front.CalendarDeco;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.grad_proj.assembletickets.front.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDeco implements DayViewDecorator {

    private final Drawable drawable;
    private HashSet<CalendarDay> days;

    public EventDeco(Collection<CalendarDay> days, Context context){
        drawable = context.getResources().getDrawable(R.drawable.event_deco);
        this.days = new HashSet<>(days);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return days.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
