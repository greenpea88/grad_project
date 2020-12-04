package com.grad_proj.assembletickets.front;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class LoadDataDialog extends Dialog {

    private TextView loadTextView;

    public LoadDataDialog(@NonNull Context context,String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_loading_data);

        loadTextView = findViewById(R.id.loadTextView);
        loadTextView.setText(message);
    }
}
