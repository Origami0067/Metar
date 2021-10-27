package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

public class Results extends AppCompatActivity {

    String codeOACI;

    TabLayout layoutMT;
    ViewPager2 viewSliders;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        codeOACI = intent.getStringExtra("code");

        //GET SITE WEB
        String url="https://www.aviationweather.gov/metar/data?ids="+codeOACI+"&format=decoded&hours=0&taf=on&layout=off";
        getSiteWeb(url);
        //GET SITE WEB

        layoutMT=findViewById(R.id.layoutMT);
        viewSliders=findViewById(R.id.viewSliders);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewSliders.setAdapter(adapter);

        layoutMT.addTab(layoutMT.newTab().setText("Metar"));
        layoutMT.addTab(layoutMT.newTab().setText("Taf"));
        layoutMT.addTab(layoutMT.newTab().setText("Infos"));

        layoutMT.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewSliders.setCurrentItem(tab.getPosition());
                System.out.println("getPosition() "+tab.getPosition());
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

    private void getSiteWeb(String url) {
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    globale.result = Jsoup.connect(url).get();//url
                    String title = globale.result .title();
                    System.out.println("title : "+title);

                    //Elements trs = doc.select("table tr");

                    /*String text="";

                    for (Element tr : trs) {
                        Elements tds = tr.getElementsByTag("td");
                        //Element td = tds.first();
                        text+=tds.text()+"\n";
                    }
                    builder.append(text);*/
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}