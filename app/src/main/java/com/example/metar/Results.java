package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;

public class Results extends AppCompatActivity {


    public String codeOACI="no OACI code";
    TabLayout layoutMT;
    ViewPager2 viewSliders;
    FragmentAdapter adapter;
    FloatingActionButton map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        Intent intent = getIntent();
        codeOACI = intent.getStringExtra("code");


        //GET SITE WEB
        /*try {
            Document doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //getSiteWeb(url);
        //GET SITE WEB

        layoutMT=findViewById(R.id.layoutMT);
        viewSliders=findViewById(R.id.viewSliders);
        map=findViewById(R.id.floatingBtn);

        new JsoupListView().execute();

        /*FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle(), this.codeOACI, metartaf);
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
        });*/

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Results.this, MapAirports.class);
                mapIntent.putExtra("oaci",codeOACI);

                startActivity(mapIntent);
            }
        });

    }

    public Results(){
    }

    private class JsoupListView extends AsyncTask<Void, Void, Void> {

        // Create a progressdialog
        ProgressDialog mProgressDialog = new ProgressDialog(Results.this);
        MetarTafInfos mti=new MetarTafInfos();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Set progressdialog message
            mProgressDialog.setMessage(getResources().getString(R.string.loadingResult));
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // Create an array
            String urlMT="https://www.aviationweather.gov/metar/data?ids="+codeOACI+"&format=decoded&hours=0&taf=on&layout=off";
            String urlI="https://ourairports.com/airports/"+codeOACI+"/";
            mti.setCode(codeOACI);
            ArrayList<String> metartaf;

            try{
                Document docMT = Jsoup.connect(urlMT).get();
                Document docInfo = Jsoup.connect(urlI).get();

                metartaf= metartafGet(docMT);
                mti.setMetar(metartaf.get(0));
                mti.setTaf(metartaf.get(1));

                Elements tbody = docInfo.select("aside[id=data]").select("section").select("table").select("tbody");
                String resultat="";
                resultat="\n";
                for (Element trs : tbody.select("tr")){
                    resultat+=trs.select("th").text();
                    resultat+=" : \n";
                    resultat+=trs.select("td").text();
                    resultat+="\n\n";
                }
                mti.setInfos(resultat);

            }
            catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // Close the progressdialog
            FragmentManager fm = getSupportFragmentManager();
            adapter = new FragmentAdapter(fm, getLifecycle(), mti);
            viewSliders.setAdapter(adapter);

            layoutMT.addTab(layoutMT.newTab().setText(getResources().getString(R.string.FragNameMetar)));
            layoutMT.addTab(layoutMT.newTab().setText(getResources().getString(R.string.FragNameTaf)));
            layoutMT.addTab(layoutMT.newTab().setText(getResources().getString(R.string.FragNameInfo)));

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
            mProgressDialog.dismiss();
        }
    }

    public ArrayList<String> metartafGet(Document doc){
        ArrayList<String> result = new ArrayList<String>();

        StringBuilder resultat;
        for (Element div : doc.select("div[id=awc_main_content_wrap]")) {
            for (Element table : div.select("table")) {
                resultat = new StringBuilder();
                for (Element trs : table.select("tr")) {
                    switch (trs.select("td").get(0).text()){

                        case "Text:" :
                            resultat.append("\n").append(trs.select("td").get(1).text()).append("\n");
                            break;
                        case "Temperature:" :
                            resultat.append(getResources().getString(R.string.temperature)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Dewpoint:" :
                            resultat.append(getResources().getString(R.string.Dewpoint)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Pressure (altimeter):" :
                            resultat.append(getResources().getString(R.string.Pressure)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Winds:" :
                            resultat.append(getResources().getString(R.string.Winds)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Visibility:" :
                            resultat.append(getResources().getString(R.string.Visibility)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Ceiling:" :
                            resultat.append(getResources().getString(R.string.Ceiling)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Clouds:" :
                            resultat.append(getResources().getString(R.string.Clouds)).append(trs.select("td").
                                    get(1).text()).append("\n");break;
                        case "Weather:" :
                            resultat.append(getResources().getString(R.string.Weather)).append(trs.select("td").
                                    get(1).text()).append("\n");break;

                        case "Forecast type:" :
                        case "Forecast period:" :
                            resultat.append(trs.select("td").
                                    get(1).text()).append("\n");break;
                    }
                    System.out.println("td0 :"+trs.select("td").get(0).text());
                }
                result.add(resultat.toString());
            }
        }

        return result;
    }
}