package com.grad_proj.assembletickets.front;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grad_proj.assembletickets.front.Fragment.TicketClassicFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketConcertFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketMusicalFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketPlayFragment;
import com.grad_proj.assembletickets.front.Fragment.TicketTotalFragment;

public class TopMenuBarAdapter extends FragmentStatePagerAdapter {

    private int topMenuCount;

    public TopMenuBarAdapter(FragmentManager fragmentManager,int topMenuCount){
        super(fragmentManager);
        this.topMenuCount=topMenuCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TicketTotalFragment totalFragment = new TicketTotalFragment();
                return totalFragment;
            case 1:
                TicketMusicalFragment musicalFragment = new TicketMusicalFragment();
                return musicalFragment;
            case 2:
                TicketConcertFragment concertFragment = new TicketConcertFragment();
                return concertFragment;
            case 3:
                TicketPlayFragment playFragment = new TicketPlayFragment();
                return playFragment;
            case 4:
                TicketClassicFragment classicFragment = new TicketClassicFragment();
                return classicFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return topMenuCount;
    }
}
