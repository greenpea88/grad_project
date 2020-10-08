package com.grad_proj.assembletickets.front;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonState{
    GONE,
    RIGHT_VISIBLE
}

public class SwipeToDelete extends ItemTouchHelper.Callback {

    private boolean swipeBack = false;
    private ButtonState buttonShowState = ButtonState.GONE;
    private static final float buttonWidth = 200;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentViewHolder = null;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        if(swipeBack){
            swipeBack=false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //아이텡을 터치 or 스와이프 or 뷰의 변화 발생 시 불려짐
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //아이템이 스와이프 됐을 경우
            if (buttonShowState != ButtonState.GONE) {
                if (buttonShowState == ButtonState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -buttonWidth);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if(buttonShowState == ButtonState.GONE){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                swipeBack = motionEvent.getAction() == MotionEvent.ACTION_CANCEL || motionEvent.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) {
                        buttonShowState = ButtonState.RIGHT_VISIBLE;
                    }
                    if (buttonShowState != ButtonState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){
        Log.d("SwipeToDelete","setTouchDownListener()");
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //처음 눌렷을 때
                    Log.d("SwipeToDelete","setTouchDownListener() -> up");
                    setTouchUpListener(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
                }

                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){
        Log.d("SwipeToDelete","setTouchUpListener()");
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    //누른걸 땟을 때
                    SwipeToDelete.super.onChildDraw(c,recyclerView,viewHolder,0F,dY,actionState,isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return false;
                        }
                    });
                    setItemClickable(recyclerView,true);
                    swipeBack = false;

//                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
//                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
//                        }
//                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
//                        }
//                    }

                    buttonShowState = ButtonState.GONE;
                    currentViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemClickable(RecyclerView recyclerView,boolean isClickable){
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawBtn(Canvas c, RecyclerView.ViewHolder viewHolder){
        float buttonWidthWithoutPadding = buttonWidth - 10;
        float corner = 10;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF rightBtn = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()-4, itemView.getRight(),itemView.getBottom()-4);
        p.setColor(Color.parseColor("#F21905"));
        c.drawRoundRect(rightBtn,corner,corner,p);
        drawText("DELETE",c,rightBtn,p);

        buttonInstance = null;
        if(buttonShowState == ButtonState.RIGHT_VISIBLE){
            buttonInstance = rightBtn;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p){
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text,button.centerX()-(textWidth/2),button.centerY()+(textWidth/2),p);
    }

    public void onDraw(Canvas c){
        if(currentViewHolder != null){
            drawBtn(c,currentViewHolder);
        }
    }
}
