package com.grad_proj.assembletickets.front;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DateEventAdapter extends RecyclerView.Adapter<DateEventAdapter.ItemViewHolder> {

    ArrayList<Event> eventItems = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item을 inflate
        // return 인자는 ViewHolder

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dateevent_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //item을 보여주는 함수
        holder.setData(eventItems.get(position));
    }

    @Override
    public int getItemCount() {
        //recyclerView item의 총 갯수
        return eventItems.size();
    }

    public void addItem(Event event){
        //외부에서 item을 추가할 경우
         eventItems.add(event);
    }

    //subView(item setting)
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName, eventTime;

        public ItemViewHolder(View itemView){
            super(itemView);

            eventName = itemView.findViewById(R.id.eventNameTextView);
            eventTime = itemView.findViewById(R.id.eventTimeTextView);
        }

        void setData(Event event){
            eventName.setText(event.getEventName());
            eventTime.setText(event.getTime());
        }
    }
}
