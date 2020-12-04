package com.grad_proj.assembletickets.front.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grad_proj.assembletickets.front.Activity.HomeActivity;
import com.grad_proj.assembletickets.front.Database.SearchDatabase;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.SearchHistory;
import com.grad_proj.assembletickets.front.SearchHistoryAdapter;

public class SearchFragment extends Fragment {

    SearchHistoryAdapter searchHistoryAdapter;

    View view;
    RecyclerView historyRecyclerView;
    EditText searchText;
    TextView searchClear;
    TextView removeAll;
    ImageButton search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container,false);

        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        historyRecyclerView = (RecyclerView)view.findViewById(R.id.historyRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        historyRecyclerView.setLayoutManager(linearLayoutManager);

        search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchText.length() > 1) {
                    ((HomeActivity)getActivity()).deleteDupAndInsertHistory(searchText.getText().toString());

                    Fragment currentFragment = ((HomeActivity)getActivity()).fragmentManager.findFragmentById(R.id.frameLayout);
                    ((HomeActivity)getActivity()).fragmentStack.push(currentFragment);
                    ((HomeActivity)getActivity()).replaceFragment(SearchResultFragment.newInstance(searchText.getText().toString()));

                    // 검색 후 키보드 숨김
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                }
                else {
                    Toast.makeText(getContext(), "두 자 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        searchText = view.findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                search.callOnClick();
                return true;
            }
        });

        searchHistoryAdapter = new SearchHistoryAdapter(
        new SearchHistoryAdapter.OnItemClickListenerSelect() {
            @Override
            public void onItemClickListenerSelect(View view, int position) {
                SearchHistory history = searchHistoryAdapter.getItem(position);
                searchText.setText(history.getContext());
            }
        }, new SearchHistoryAdapter.OnItemClickListenerDelete() {
            @Override
            public void onItemClickListenerDelete(View view, int position) {
                SearchHistory history = searchHistoryAdapter.getItem(position);
                int id = history.getId();
                ((HomeActivity)getActivity()).deleteHistory(id);
            }
        });

        searchClear = view.findViewById(R.id.searchClear);
        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });

        removeAll = view.findViewById(R.id.removeBtn);
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).deleteHistoryAll();
                searchHistoryAdapter.removeAll();
            }
        });

        historyRecyclerView.setAdapter(searchHistoryAdapter);

        getHistories();

        return view;
    }

    @Override
    public void onDestroyView() {
        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        super.onDestroyView();
    }

    public void getHistories() {
        //내부 디비에서 데이터 가져오기
        Cursor cursor=((HomeActivity)getActivity()).getHistoryAll();

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SearchDatabase.SearchHistoryDB._ID));
            String context = cursor.getString(cursor.getColumnIndex(SearchDatabase.SearchHistoryDB.CONTEXT));

            SearchHistory history = new SearchHistory();
            history.setId(id);
            history.setContext(context);

            searchHistoryAdapter.addItem(history);
        }
        ((HomeActivity)getActivity()).closeHistoryDB();
        searchHistoryAdapter.notifyDataSetChanged();
    }

}
