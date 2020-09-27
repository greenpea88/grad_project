package com.grad_proj.assembletickets.front;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grad_proj.assembletickets.front.Fragment.ClassicFragment;
import com.grad_proj.assembletickets.front.Fragment.ConcertFragment;
import com.grad_proj.assembletickets.front.Fragment.MusicalFragment;
import com.grad_proj.assembletickets.front.Fragment.PlayFragment;
import com.grad_proj.assembletickets.front.Fragment.TotalFragment;

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
                TotalFragment totalFragment = new TotalFragment();
                return totalFragment;
            case 1:
                MusicalFragment musicalFragment = new MusicalFragment();
                return musicalFragment;
            case 2:
                ConcertFragment concertFragment = new ConcertFragment();
                return concertFragment;
            case 3:
                PlayFragment playFragment = new PlayFragment();
                return playFragment;
            case 4:
                ClassicFragment classicFragment = new ClassicFragment();
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
