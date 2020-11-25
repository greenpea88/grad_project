package com.grad_proj.assembletickets.front;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClicked(View v, int position);
    }

    ArrayList<Show> items = new ArrayList<Show>();

    private ShowAdapter.OnItemClickListener onItemClickListener = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.show_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Show item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Show item) {
        items.add(item);
    }

    public void resetItem(){
        items.clear();
    }

    public void setItems(ArrayList<Show> items) {
        this.items = items;
    }

    public Show getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(ShowAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView titleText;
        TextView performersText;
        TextView dateText;
        TextView priceText;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.imageView);
            titleText = itemView.findViewById(R.id.titleText);
            performersText = itemView.findViewById(R.id.performersText);
            dateText = itemView.findViewById(R.id.dateText);
            priceText = itemView.findViewById(R.id.priceText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();

                    if(onItemClickListener!= null){
                        onItemClickListener.onItemClicked(view,position);
                    }
                }
            });
        }

        public void setItem(Show item) {
            // poster.setImageDrawable(); URL로부터 이미지를 로드해오는 함수 필요
            titleText.setText(item.title);
//            performersText.setText(item.performerList.get(0).pName); // 추후 수정해야함 리스트의 이름을 나열해 문자열로 정렬하는 함수 필요
//            dateText.setText(item.startDate + " ~ " + item.endDate); // 추후 수정해야함 경우의 수에 따라 조건함수 필요
//            priceText.setText(item.price);
        }
    }
}
