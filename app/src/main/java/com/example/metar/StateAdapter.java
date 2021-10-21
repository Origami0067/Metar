package com.example.metar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/*

Tutorial for ViewPager :
https://www.codingdemos.com/android-tablayout-example-viewpager/

Tutorial to migrate ViewPager2 from ViewPager :
https://developer.android.com/training/animation/vp2-migration


YT Tutorial for ViewPager2:
https://www.youtube.com/watch?v=5-RWOvJ9oq8

 */

public class StateAdapter extends FragmentStateAdapter {

    private int numOfTabs;

    public StateAdapter(@NonNull FragmentActivity fragmentActivity, int numOfTabs) {
        super(fragmentActivity);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
