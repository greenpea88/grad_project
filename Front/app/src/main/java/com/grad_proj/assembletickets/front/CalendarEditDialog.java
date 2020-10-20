package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

public class CalendarEditDialog extends Dialog {

    private int alarmHour;
    private int alarmMin;
    private String title;
    private String eventContent;

    private TimePicker eventEditTime;
    private EditText eventEditTitle,eventEditContent;
    private Button eventEditBtn;

    private OnDialogListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CalendarEditDialog(Context context, final int position, Event event){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_calendar_edit);

        alarmHour=event.getTimeHour();
        alarmMin=event.getTimeMin();
        title = event.getEventName();
        eventContent=event.getEventContent();

        eventEditTime =findViewById(R.id.eventEditTimePicker);
        eventEditTime.setHour(alarmHour);
        eventEditTime.setMinute(alarmMin);

        eventEditTitle = findViewById(R.id.eventEditTitle);
        eventEditTitle.setText(title);

        eventEditContent=findViewById(R.id.eventEditContent);
        eventEditContent.setText(eventContent);

        eventEditBtn=findViewById(R.id.eventEditBtn);
        eventEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    //수정된 값 가져오기
                    int newHour=eventEditTime.getHour();
                    int newMin=eventEditTime.getMinute();
                    String newTitle=eventEditTitle.getText().toString();
                    String newContent=eventEditContent.getText().toString();

                    Event newEvent = new Event();
                    newEvent.setTimeHour(newHour);
                    newEvent.setTimeMin(newMin);
                    newEvent.setEventName(newTitle);
                    newEvent.setEventContent(newContent);

                    listener.onFinish(position,newEvent);

                    dismiss();
                }
            }
        });
    }

    public void setDialogListener(OnDialogListener listener){
        this.listener=listener;
    }
}
