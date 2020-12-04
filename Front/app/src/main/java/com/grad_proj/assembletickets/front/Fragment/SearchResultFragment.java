package com.grad_proj.assembletickets.front.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Performer;
import com.grad_proj.assembletickets.front.PerformerAdapter;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.Show;
import com.grad_proj.assembletickets.front.ShowAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResultFragment extends Fragment {

    private static final String TEXT = "";

    View view;
    TextView textToSearch;
    TextView noResult;
    RecyclerView searchRecyclerView;

    private ShowAdapter showAdapter;
    private PerformerAdapter performerAdapter;

    public static SearchResultFragment newInstance(String search) {
        //fragment 전환 시 이전 fragment로부터 데이터 넘겨받기
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEXT, search);
        searchResultFragment.setArguments(bundle);

        return searchResultFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);

        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        noResult = view.findViewById(R.id.noResultTxt);

        textToSearch = view.findViewById(R.id.toSearch);
        if(getArguments() != null){
            textToSearch.setText("\"" + getArguments().getString(TEXT) + "\"에 대한 검색 결과");
        }

        searchRecyclerView = (RecyclerView)view.findViewById(R.id.searchRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);

        showAdapter = new ShowAdapter();
        searchRecyclerView.setAdapter(showAdapter);
        showAdapter.setOnItemClickListener(new ShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                ((HomeActivity)getActivity()).replaceFragment(ShowDetailFragment.newInstance(showAdapter.getItem(position)));
            }
        });
        new GetSearchResult().execute("http://10.0.2.2:8080/assemble-ticket/search");
        return view;
    }

    @Override
    public void onDestroyView() {
        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        super.onDestroyView();
    }

    private class SearchResult {
        List<Show> shows;
        List<Performer> performers;


        public List<Show> getShows() {
            return shows;
        }

        public void setShows(List<Show> shows) {
            this.shows = shows;
        }

        public List<Performer> getPerformers() {
            return performers;
        }

        public void setPerformers(List<Performer> performers) {
            this.performers = performers;
        }
    }

    private class GetSearchResult extends AsyncTask<String, Void, SearchResult>{

        OkHttpClient client = new OkHttpClient();
        SearchResult searchResult;

        @Override
        protected SearchResult doInBackground(String... strings) {
            searchResult = new SearchResult();

            String strUrl = HttpUrl.parse(strings[0]).newBuilder()
                    .addQueryParameter("keyword", getArguments().getString(TEXT))
                    .build().toString();

            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();

                Gson gson = new Gson();

                Type listType = new TypeToken<SearchResult>(){}.getType();
                searchResult = gson.fromJson(response.body().string(), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResult;
        }

        @Override
        protected void onPostExecute(SearchResult searchResult) {
            super.onPostExecute(searchResult);
            if(searchResult.shows.size() != 0) {
                showAdapter.addNewItems(searchResult.shows);
                showAdapter.notifyDataSetChanged();
            } else {
                noResult.setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.GONE);
            }
        }
    }
}