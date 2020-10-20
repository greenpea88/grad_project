package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CalendarEditDialog extends Dialog {

    private String alarmTime;
    private String title;
    private String eventContent;

    private EditText eventEditTitle,eventEditContent;
    private Button eventEditBtn;

    private OnDialogListener listener;

    public CalendarEditDialog(Context context, final int position, Event event){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_calendar_edit);

        title = event.getEventName();
        eventContent=event.getEventContent();

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
                    String newTitle=eventEditTitle.getText().toString();
                    String newContent=eventEditContent.getText().toString();

                    Event newEvent = new Event();
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
