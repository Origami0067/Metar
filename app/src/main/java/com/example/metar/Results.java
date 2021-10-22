package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

public class Results extends AppCompatActivity {

    TabLayout layoutMT;
    ViewPager2 viewSliders;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        layoutMT=findViewById(R.id.layoutMT);
        viewSliders=findViewById(R.id.viewSliders);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewSliders.setAdapter(adapter);

        layoutMT.addTab(layoutMT.newTab().setText("Metar"));
        layoutMT.addTab(layoutMT.newTab().setText("Taf"));
        layoutMT.addTab(layoutMT.newTab().setText("AirportInfo"));

        layoutMT.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewSliders.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewSliders.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                layoutMT.selectTab(layoutMT.getTabAt(position));
            }
        });

    }
}