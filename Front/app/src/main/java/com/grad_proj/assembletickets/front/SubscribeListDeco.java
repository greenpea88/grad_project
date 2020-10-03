package com.grad_proj.assembletickets.front;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubscribeListDeco extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()-1){
            //단위 pixel
            outRect.right = 30;
        }

        if(parent.getChildAdapterPosition(view) == 0){
            outRect.left = 30;
        }
    }
}
