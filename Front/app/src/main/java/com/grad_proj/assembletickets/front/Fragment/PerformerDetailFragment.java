package com.grad_proj.assembletickets.front.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class PerformerDetailFragment extends Fragment {

    View view;

    private ImageView performerImg;
    private TextView performerName;
    private RecyclerView showListView;
    private ShowAdapter showAdapter;

    private List<String> showList;
    private Performer performer;

    public static PerformerDetailFragment newInstance(Performer performer) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        PerformerDetailFragment performerDetailFragment = new PerformerDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("performer", performer);
        performerDetailFragment.setArguments(bundle);

        return performerDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_performer_detail,container,false);

        if(getArguments()!=null){
            this.performer=(Performer)getArguments().getSerializable("performer");
        }

        performerImg = view.findViewById(R.id.performerImg);
        new ImgDownloadTask().execute("http://ticketimage.interpark.com/Play/image/large/20/20008287_p.gif");

        performerName = view.findViewById(R.id.performerName);
        performerName.setText(performer.getpName());

        showListView = view.findViewById(R.id.showList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        showListView.setLayoutManager(linearLayoutManager);

        showAdapter = new ShowAdapter();
        showListView.setAdapter(showAdapter);

        showAdapter.setOnItemClickListener(new ShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                //show detail fragment call
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(ShowDetailFragment.newInstance(showAdapter.getItem(position)));
            }
        });

        getShowlist();

        return view;
    }

    private void getShowlist(){
        showList = Arrays.asList("test1","test2","test3","test4","test5","test6");
        for(int i = 0; i < showList.size(); i++){
            Show show = new Show();
            show.setTitle(showList.get(i));

            //data를 adpater에 추가
            showAdapter.addItem(show);
        }
        showAdapter.notifyDataSetChanged();
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
            performerImg.setImageBitmap(bitmap);
        }
    }
}
