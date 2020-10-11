package com.grad_proj.assembletickets.front;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonState{
    GONE,
    RIGHT_VISIBLE,
    LEFT_VISIBLE
}

public class SwipeToDelete extends ItemTouchHelper.Callback {

    private boolean swipeBack = false;
    private ButtonState buttonShowState = ButtonState.GONE;
    private static final float buttonWidth = 200;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentViewHolder = null;
    private SwipeToDeleteAction swipeToDeleteAction = null;
    private Context context;
    private String flag;

    public SwipeToDelete(Context context,String flag,SwipeToDeleteAction swipeToDeleteAction){
        this.context = context;
        this.flag = flag;
        this.swipeToDeleteAction = swipeToDeleteAction;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if ("subscribe".equals(flag)) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT);
        }
        else if("event".equals(flag)){
            return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        }

        return makeMovementFlags(0, ItemTouchHelper.LEFT);
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
                else if(buttonShowState == ButtonState.LEFT_VISIBLE){
                    dX = Math.max(dX,buttonWidth);
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
                    else if (dX > buttonWidth){
                        buttonShowState = ButtonState.LEFT_VISIBLE;
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

                    if (swipeToDeleteAction != null && buttonInstance != null && buttonInstance.contains(motionEvent.getX(), motionEvent.getY())) {
                        if (buttonShowState == ButtonState.RIGHT_VISIBLE) {
                            swipeToDeleteAction.onRightClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowState == ButtonState.LEFT_VISIBLE){
                            swipeToDeleteAction.onLeftClicked(viewHolder.getAdapterPosition());
                        }
                    }

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
        float buttonWidthWithoutPadding = buttonWidth;
        float corner = 10;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF rightBtn = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()+20, itemView.getRight()-20,itemView.getBottom()-25);
        p.setColor(Color.parseColor("#F23E2E"));
        c.drawRoundRect(rightBtn,corner,corner,p);
        drawBitmap(c,rightBtn,p,R.drawable.icon_delete);

        RectF leftBtn = new RectF(itemView.getLeft()+20, itemView.getTop()+20, itemView.getLeft() + buttonWidthWithoutPadding,itemView.getBottom()-25);
        p.setColor(Color.parseColor("#5897A6"));
        c.drawRoundRect(leftBtn,corner,corner,p);
        drawBitmap(c,leftBtn,p,R.drawable.icon_edit);

        buttonInstance = null;
        if(buttonShowState == ButtonState.RIGHT_VISIBLE){
            buttonInstance = rightBtn;
        }
        else if(buttonShowState == ButtonState.LEFT_VISIBLE){
            buttonInstance = leftBtn;
        }
    }

    private void drawBitmap(Canvas c, RectF button, Paint p, int drawableId){

        Drawable drawable = ContextCompat.getDrawable(context,drawableId);
        Bitmap bitmap = drawableToBitmap(drawable);

        c.drawBitmap(bitmap,button.centerX()-((int)(bitmap.getWidth()/2)),button.centerY()-((int)(bitmap.getHeight()/2)),p);
    }

    private Bitmap drawableToBitmap(Drawable drawable){
        Bitmap bitmap;
        if(drawable instanceof BitmapDrawable){
            bitmap = ((BitmapDrawable)drawable).getBitmap();
            return resizeBitmap(bitmap,(int)(buttonWidth-100));
        }
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);

        return resizeBitmap(bitmap,(int)(buttonWidth-100));
    }

    private Bitmap resizeBitmap(Bitmap bitmap,int maxSize){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width>height){
            rate = maxSize/(float)width;
            newHeight = (int)(height*rate);
            newWidth = maxSize;
        }
        else{
            rate = maxSize/(float)height;
            newWidth = (int)(width*rate);
            newHeight = maxSize;
        }

        return Bitmap.createScaledBitmap(bitmap,newWidth,newHeight,true);
    }

    public void onDraw(Canvas c){
        if(currentViewHolder != null){
            drawBtn(c,currentViewHolder);
        }
    }
}
