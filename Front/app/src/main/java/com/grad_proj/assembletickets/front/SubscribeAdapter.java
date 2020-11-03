package com.grad_proj.assembletickets.front;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ItemViewHolder> {

    public interface OnItemClickListener{
        void onItemClicked(View v, int position);
    }

    private OnItemClickListener onItemClickListener = null;

    ArrayList<Performer> subscribeList = new ArrayList<>();
    public SparseBooleanArray selectedList = new SparseBooleanArray(0);
    private int currentPosition = -1;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setData(subscribeList.get(position));
        if(selectedList.get(position,false)){
            holder.itemView.setBackgroundColor(Color.parseColor("#B3F28379"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#00FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }

    public void addItem(Performer performer){
        //외부에서 item을 추가할 경우
        subscribeList.add(performer);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    //subView(item setting)
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView subscribeProfile;
        private TextView subscribeName;

        public ItemViewHolder(View itemView){
            super(itemView);

            subscribeProfile = itemView.findViewById(R.id.subscribeProfile);
            subscribeName = itemView.findViewById(R.id.subscribeName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(position!=currentPosition){
                            selectedList.clear();
                        }
                        Performer performer = subscribeList.get(position);
                        Log.d("SubscribeAdapter","itemClicked");
                        if(!selectedList.get(position,false)){
                            selectedList.put(position,true);
                            currentPosition=position;
                            view.setBackgroundColor(Color.parseColor("#B3F28379"));
                        }
                        else{
                            Log.d("SubscribeAdapter","clickedAgain");
                            selectedList.put(position,false);
                            view.setBackgroundColor(Color.parseColor("#00FF0000"));
                        }
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClicked(view,position);
                        }
                        notifyDataSetChanged();
                    }
                }
            });


        }

        void setData(Performer performer){
            subscribeName.setText(performer.getpName());
            //사진 설정도 추가하기
        }
    }
}
