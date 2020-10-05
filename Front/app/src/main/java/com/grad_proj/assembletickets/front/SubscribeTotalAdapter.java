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

public class SubscribeTotalAdapter extends RecyclerView.Adapter<SubscribeTotalAdapter.ItemViewHolder>{

    ArrayList<Performer> subscribeTotalList = new ArrayList<>();


    @NonNull
    @Override
    public SubscribeTotalAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribedetail_item,parent,false);

        return new SubscribeTotalAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribeTotalAdapter.ItemViewHolder holder, int position) {
        holder.setData(subscribeTotalList.get(position));
    }

    @Override
    public int getItemCount() {
        return subscribeTotalList.size();
    }

    public void addItem(Performer performer){
        subscribeTotalList.add(performer);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView subscribeDetailProfile;
        private TextView subscribeDetailName;

        public ItemViewHolder(View itemView){
            super(itemView);

            subscribeDetailProfile = itemView.findViewById(R.id.subscribeDetailProfile);
            subscribeDetailName = itemView.findViewById(R.id.subscribeDetailName);
        }

        void setData(Performer performer){
            //profile 설정 추가
            subscribeDetailName.setText(performer.getpName());
        }
    }
}
