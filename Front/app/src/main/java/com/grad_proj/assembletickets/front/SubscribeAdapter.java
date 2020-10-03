package com.grad_proj.assembletickets.front;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ItemViewHolder> {

    ArrayList<Subscribe> subscribeList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setData(subscribeList.get(position));
    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }

    public void addItem(Subscribe subscribe){
        //외부에서 item을 추가할 경우
        subscribeList.add(subscribe);
    }

    //subView(item setting)
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView subscribeProfile;
        private TextView subscribeName;

        public ItemViewHolder(View itemView){
            super(itemView);

            subscribeProfile = itemView.findViewById(R.id.subscribeProfile);
            subscribeName = itemView.findViewById(R.id.subscribeName);
        }

        void setData(Subscribe subscribe){
            subscribeName.setText(subscribe.getSubscribeName());
            //사진 설정도 추가하기
        }
    }
}
