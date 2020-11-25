package com.grad_proj.assembletickets.front;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClicked(View v, int position);
    }

    List<Show> items = new ArrayList<Show>();

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

    public void addNewItems(List<Show> newLoadedShows){
        newLoadedShows.addAll(items);
        items=newLoadedShows;
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
        TextView venueText;
        TextView dateText;
        TextView priceText;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.imageView);
            titleText = itemView.findViewById(R.id.titleText);
            venueText = itemView.findViewById(R.id.venueText);
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
            venueText.setText(item.getVenue());
            titleText.setText(item.title);
            dateText.setText(item.getStartDate()+"~"+item.getEndDate());
            priceText.setText(item.getPrice());
            new ImgDownloadTask().execute(item.getPosterSrc());
        }

        private class ImgDownloadTask extends AsyncTask<String,Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;

                try {
                    String imgUrl = strings[0];
                    URL url = new URL(imgUrl);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                poster.setImageBitmap(bitmap);
            }
        }
    }
}
