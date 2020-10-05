package com.grad_proj.assembletickets.front;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscribeEditAdapter extends RecyclerView.Adapter<SubscribeEditAdapter.ItemViewHolder> {


    ArrayList<Performer> subscribeEditList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribeedit_item,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setData(subscribeEditList.get(position));
    }

    @Override
    public int getItemCount() {
        return subscribeEditList.size();
    }

    public void addItem(Performer performer){
        subscribeEditList.add(performer);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView subscribeEditProfile;
        private TextView subscribeEditName;
        private ImageView subscribeEditAlarm;

        public ItemViewHolder(View itemView){
            super(itemView);

            subscribeEditProfile = itemView.findViewById(R.id.subscribeDetailProfile);
            subscribeEditName = itemView.findViewById(R.id.subscribeEditName);
            subscribeEditAlarm = itemView.findViewById(R.id.subscribeEditAlarm);
            subscribeEditAlarm.setColorFilter(Color.parseColor("#F25E3D"), PorterDuff.Mode.SRC_IN);

            subscribeEditAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Subscribe Edit","alarm clicked");
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        Performer performer = subscribeEditList.get(position);
                        Boolean alarm = performer.getSetAlarm();
                        if(alarm){
                            performer.setSetAlarm(false);
                            subscribeEditAlarm.setImageResource(R.drawable.icon_alarmoff);
                        }
                        else{
                            performer.setSetAlarm(true);
                            subscribeEditAlarm.setImageResource(R.drawable.icon_alarmon);
                        }
                    }
                }
            });
        }

        void setData(Performer performer){

            //profile 설정 추가
            subscribeEditName.setText(performer.getpName());
            if(performer.getSetAlarm()){
                subscribeEditAlarm.setImageResource(R.drawable.icon_alarmon);
            }
            else{
                subscribeEditAlarm.setImageResource(R.drawable.icon_alarmoff);
            }
        }
    }
}
