package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class CalendarEditDialog extends Dialog {

    private int alarmHour;
    private int alarmMin;
    private String title;
    private String eventContent;
    private int alarmSet;

    private TimePicker eventEditTime;
    private EditText eventEditTitle,eventEditContent;
    private Button eventEditBtn;
    private SwitchMaterial alarmEditSwitch;

    private OnDialogListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CalendarEditDialog(Context context, final int position, final Event event){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_calendar_edit);

        alarmHour=event.getTimeHour();
        alarmMin=event.getTimeMin();
        title = event.getEventName();
        eventContent=event.getEventContent();
        if(eventContent==null){
            System.out.println("empty");
            eventContent="";
        }
        alarmSet=event.getAlarmSet();

        eventEditTime =findViewById(R.id.eventEditTimePicker);
        eventEditTime.setHour(alarmHour);
        eventEditTime.setMinute(alarmMin);

        eventEditTitle = findViewById(R.id.eventEditTitle);
        eventEditTitle.setText(title);

        eventEditContent=findViewById(R.id.eventEditContent);
        eventEditContent.setText(eventContent);

        alarmEditSwitch = findViewById(R.id.alarmEditSwitch);
        if(alarmSet==0){
            alarmEditSwitch.setChecked(false);
        }
        else{
            alarmEditSwitch.setChecked(true);
        }

        eventEditBtn=findViewById(R.id.eventEditBtn);
        eventEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    //수정된 값 가져오기
                    Log.d("Dialog","eventEditBtnClicked");
                    int newHour=eventEditTime.getHour();
                    int newMin=eventEditTime.getMinute();
                    String newTitle=eventEditTitle.getText().toString();
                    String newContent=eventEditContent.getText().toString();
                    int editAlarmSet;
                    if(alarmEditSwitch.isChecked()){
                        editAlarmSet=1;
                    }
                    else{
                        editAlarmSet=0;
                    }
                    Log.d("Dialog",newContent);

                    if(newHour==alarmHour && newMin==alarmMin && title.equals(newTitle) && eventContent.equals(newContent) && alarmSet==editAlarmSet){
                        //값의 변화가 없을 경우
                        Toast toast=Toast.makeText(view.getContext(),"수정 사항이 없습니다",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else{
                        if(newTitle.isEmpty()){
                            Toast toast=Toast.makeText(view.getContext(),"이벤트 제목은 비워둘 수 없습니다.",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        else{
                            Event newEvent = new Event();
                            newEvent.setId(event.getId());
                            newEvent.setTimeHour(newHour);
                            newEvent.setTimeMin(newMin);
                            newEvent.setEventName(newTitle);
                            newEvent.setEventContent(newContent);
                            newEvent.setAlarmSet(editAlarmSet);

                            listener.onFinish(position,newEvent);

                            dismiss();
                        }
                    }
                }
            }
        });
    }

    public void setDialogListener(OnDialogListener listener){
        this.listener=listener;
    }
}
