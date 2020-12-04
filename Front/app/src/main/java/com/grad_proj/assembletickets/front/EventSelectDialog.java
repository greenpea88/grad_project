package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class EventSelectDialog extends Dialog {

    private Button ticketingBtn, watchBtn;

    private OnSelectDialogListener listener;

    public EventSelectDialog(final Context context, final String ticketing){
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
                    if (TextUtils.isEmpty(ticketing)) {
                        Toast toast = Toast.makeText(context,"티켓팅 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else{
                        listener.onTicketingSelect();
                        dismiss();
                    }
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
