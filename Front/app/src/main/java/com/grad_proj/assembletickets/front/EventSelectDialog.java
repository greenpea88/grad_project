package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class EventSelectDialog extends Dialog {

    private Button ticketingBtn, watchBtn;

    private OnSelectDialogListener listener;

    public EventSelectDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_event_select);

        ticketingBtn = findViewById(R.id.ticketingBtn);
        ticketingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //티켓팅 날짜가 null인 경우 접근 불가 or toast 띄우기
                if(listener!=null){
                    listener.onTicketingSelect();
                    dismiss();
                }
            }
        });

        watchBtn = findViewById(R.id.watchBtn);
        watchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(listener!=null){
                    listener.onWatchSelect();
                    dismiss();
                }
            }
        });
    }

    public void setDialogListener(OnSelectDialogListener listener){
        this.listener=listener;
    }
}
