package com.example.metar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.metar.fragments.AirportInfos;
import com.example.metar.fragments.Metar;
import com.example.metar.fragments.Taf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentAdapter extends FragmentStateAdapter {
    String code;
    String metar;
    String taf;

    ArrayList<String> liste;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String code, ArrayList<String> liste) {
        super(fragmentManager, lifecycle);
        this.code=code;
        this.metar=liste.get(0);
        this.taf=liste.get(1);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new Taf(taf);
            case 2: return new AirportInfos();
        }
        System.out.println(metar);
        return new Metar(metar);
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    private void getSiteWeb() {
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                ArrayList<String> tables = new ArrayList<String>();

                try{
                    String url = "https://www.aviationweather.gov/metar/data?ids="+code+"&format=decoded&hours=0&taf=on&layout=off";
                    System.out.println(url);
                    Document doc = Jsoup.connect(url).get();//url
                    String title = doc.title();
                    System.out.println(title);
                    for (Element div : doc.select("div[id=awc_main_content_wrap]")) {

                        for (Element table : div.select("table")) {
                            tables.add(table.text());
                        }
                    }
                    metar=tables.get(0);
                    taf=tables.get(1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
