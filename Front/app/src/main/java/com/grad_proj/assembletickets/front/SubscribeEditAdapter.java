package com.grad_proj.assembletickets.front;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

    public void removeItem(int position){
        subscribeEditList.remove(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView subscribeEditProfile;
        private TextView subscribeEditName;
//        private ImageView subscribeEditAlarm;

        public ItemViewHolder(View itemView){
            super(itemView);

            subscribeEditProfile = itemView.findViewById(R.id.subscribeEditProfile);
            subscribeEditName = itemView.findViewById(R.id.subscribeEditName);
//            subscribeEditAlarm = itemView.findViewById(R.id.subscribeEditAlarm);
//            subscribeEditAlarm.setColorFilter(Color.parseColor("#F25E3D"), PorterDuff.Mode.SRC_IN);
//
//            subscribeEditAlarm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("Subscribe Edit","alarm clicked");
//                    int position = getAdapterPosition();
//                    if(position!=RecyclerView.NO_POSITION){
//                        Performer performer = subscribeEditList.get(position);
//                        Boolean alarm = performer.getSetAlarm();
//                        if(alarm){
//                            performer.setSetAlarm(false);
//                            subscribeEditAlarm.setImageResource(R.drawable.icon_alarmoff);
//                        }
//                        else{
//                            performer.setSetAlarm(true);
//                            subscribeEditAlarm.setImageResource(R.drawable.icon_alarmon);
//                        }
//                    }
//                }
//            });
        }

        void setData(Performer performer){

            //profile 설정 추가
            subscribeEditName.setText(performer.getName());
            new ImgDownloadTask().execute(performer.getImgSrc());
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
            protected void onPostExecute(Bitmap bitmap) {
                subscribeEditProfile.setImageBitmap(bitmap);
            }
        }
    }
}
