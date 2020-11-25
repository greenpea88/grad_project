package com.grad_proj.assembletickets.front;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder> {

    ArrayList<Notification> items = new ArrayList<>();

    @NonNull
    @Override
    public NotificationAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);

        return new NotificationAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        //recyclerView item의 총 갯수
        return items.size();
    }

    public void addItem(Notification item){
        //외부에서 item을 추가할 경우
        items.add(item);
    }

    public int removeItem(int id){
        int position = getPosition(id);
        if (position != -1)
            items.remove(position);
        notifyDataSetChanged();

        return id;
    }

    public void removeAll(){
        items.clear();
        notifyDataSetChanged();
    }

    public int getPosition(int id){
        int i = 0;
        while (i < getItemCount()){
            if (items.get(i).getId() == id)
                return i;
        }
        return -1;
    }

    public Notification getItem(int position){
        return items.get(position);
    }

    public void changeItem(int id, Notification item){
        int position = getPosition(id);
        if (position != -1)
            items.set(position, item);
    }

//subView(item setting)
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView notiText;
        private TextView dateText;

        public ItemViewHolder(View itemView){
            super(itemView);

            notiText = itemView.findViewById(R.id.notiText);
            dateText = itemView.findViewById(R.id.notiDateText);
        }

        void setData(Notification item){
            notiText.setText(item.getContext());
            dateText.setText(item.getDate());
        }
    }

}
