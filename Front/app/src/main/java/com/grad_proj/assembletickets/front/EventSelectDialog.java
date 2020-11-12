package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class EventSelectDialog extends Dialog {

    private OnDialogListener listener;

    public EventSelectDialog(Context context, final int position, final Event event){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_calendar_edit);

    }

    public void setDialogListener(OnDialogListener listener){
        this.listener=listener;
    }
}
