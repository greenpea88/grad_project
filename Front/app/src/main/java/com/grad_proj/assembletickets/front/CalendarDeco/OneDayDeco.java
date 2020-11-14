package com.grad_proj.assembletickets.front.CalendarDeco;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.grad_proj.assembletickets.front.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class OneDayDeco implements DayViewDecorator {
    //오늘에 해당하는 날짜에 표시하기

    private CalendarDay date;
    private final Drawable drawable;

    public OneDayDeco(Context context){
        date = CalendarDay.today();
        drawable = ResourcesCompat.getDrawable(context.getResources(),R.drawable.today_calendar,null);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
    }
}
