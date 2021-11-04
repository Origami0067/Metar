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
    String info;

    ArrayList<String> liste;


    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String code, ArrayList<String> liste) {
        super(fragmentManager, lifecycle);
        this.code=code;
        this.metar=liste.get(0);
        this.taf=liste.get(1);
        this.info=liste.get(2);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new Taf(taf);
            case 2: return new AirportInfos(info);
        }
        System.out.println(metar);
        return new Metar(metar);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
