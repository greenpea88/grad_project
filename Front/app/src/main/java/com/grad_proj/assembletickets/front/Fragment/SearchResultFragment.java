package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.R;

public class SearchResultFragment extends Fragment {

    private static final String TEXT = "";

    View view;
    TextView textToSearch;

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

        textToSearch = view.findViewById(R.id.toSearch);
        if(getArguments() != null){
            textToSearch.setText("\"" + getArguments().getString(TEXT) + "\"에 대한 검색 결과");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        super.onDestroyView();
    }
}