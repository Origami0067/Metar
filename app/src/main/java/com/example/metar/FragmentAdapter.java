package com.example.metar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.metar.fragments.AirportInfos;
import com.example.metar.fragments.Metar;
import com.example.metar.fragments.Taf;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new Taf();
            case 2: return new AirportInfos();
        }
        return new Metar();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
