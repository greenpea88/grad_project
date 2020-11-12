package com.grad_proj.assembletickets.front;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ItemViewHolder> {

    ArrayList<SearchHistory> histories = new ArrayList<>();
    private OnItemClickListenerSelect onItemClickListenerSelect;
    private OnItemClickListenerDelete onItemClickListenerDelete;

    public SearchHistoryAdapter(OnItemClickListenerSelect onItemClickListenerSelect, OnItemClickListenerDelete onItemClickListenerDelete) {
        this.onItemClickListenerSelect = onItemClickListenerSelect;
        this.onItemClickListenerDelete = onItemClickListenerDelete;
    }

    @NonNull
    @Override
    public SearchHistoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);

        return new SearchHistoryAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.setData(histories.get(position));
        holder.context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOnItemClicked(view, position);
                }
        });
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOnItemClicked(view, position);
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        //recyclerView item의 총 갯수
        return histories.size();
    }

    public void addItem(SearchHistory history){
        //외부에서 item을 추가할 경우
        histories.add(history);
    }

    public int removeItem(int position){
        int removeId = histories.get(position).getId();
        histories.remove(position);
        notifyDataSetChanged();

        return removeId;
    }

    public void removeAll(){
        histories.clear();
        notifyDataSetChanged();
    }

    public SearchHistory getItem(int position){
        return histories.get(position);
    }

    public void changeItem(int position, SearchHistory history){
        histories.set(position, history);
    }

//subView(item setting)
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView context;
        private Button delBtn;

        public ItemViewHolder(View itemView){
            super(itemView);

            context = itemView.findViewById(R.id.historyText);
            delBtn = itemView.findViewById(R.id.delBtn);
        }

        void setData(SearchHistory history){
            context.setText(history.getContext());
        }
    }

    public interface OnItemClickListenerSelect {
        void onItemClickListenerSelect(View view, int position);
    }

    private void selectOnItemClicked(View v, int p) {
        onItemClickListenerSelect.onItemClickListenerSelect(v, p);
    }

    public interface OnItemClickListenerDelete {
        void onItemClickListenerDelete(View view, int position);
    }

    private void deleteOnItemClicked(View v, int p) {
        onItemClickListenerDelete.onItemClickListenerDelete(v, p);
    }

}
