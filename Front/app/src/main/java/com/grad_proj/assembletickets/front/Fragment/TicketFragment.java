package com.grad_proj.assembletickets.front.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.grad_proj.assembletickets.front.R;
import com.grad_proj.assembletickets.front.TopMenuBarAdapter;

public class TicketFragment extends Fragment {

    private TabLayout topMenuTab;
    private ViewPager viewPager;
    private TopMenuBarAdapter topMenuBarAdapter;
//    private FragmentActivity mContext;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket, container, false);

        topMenuTab = (TabLayout)view.findViewById(R.id.topMenuTab);
        setMenu();

        viewPager = (ViewPager)view.findViewById(R.id.pagerView);
        topMenuBarAdapter = new TopMenuBarAdapter(
                getChildFragmentManager(),topMenuTab.getTabCount()
        );
        viewPager.setAdapter(topMenuBarAdapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(topMenuTab)
        );
        topMenuTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void setMenu(){
        topMenuTab.addTab(topMenuTab.newTab().setText("전체보기"));
        topMenuTab.addTab(topMenuTab.newTab().setText("뮤지컬"));
        topMenuTab.addTab(topMenuTab.newTab().setText("콘서트"));
        topMenuTab.addTab(topMenuTab.newTab().setText("연극"));
        topMenuTab.addTab(topMenuTab.newTab().setText("클래식"));
    }
}
