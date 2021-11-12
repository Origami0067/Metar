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


    MetarTafInfos mti=new MetarTafInfos();
    TabLayout layoutMT;
    ViewPager2 viewSliders;
    FragmentAdapter adapter;
    FloatingActionButton map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        mti.setCode(intent.getStringExtra("code"));

        layoutMT=findViewById(R.id.layoutMT);
        viewSliders=findViewById(R.id.viewSliders);
        map=findViewById(R.id.floatingBtn);

        new JsoupGetInfos().execute();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Results.this, MapAirports.class);
                mapIntent.putExtra("oaci",mti.getCode());

                startActivity(mapIntent);
            }
        });

    }

    public Results(){
    }

    private class JsoupGetInfos extends AsyncTask<Void, Void, Void> {

        // Create a progressdialog
        ProgressDialog mProgressDialog = new ProgressDialog(Results.this);


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
            String urlMT="https://www.aviationweather.gov/metar/data?ids="+mti.getCode()+"&format=decoded&hours=0&taf=on&layout=off";
            String urlI="https://ourairports.com/airports/"+mti.getCode()+"/";
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
            // Fragments initializing
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
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    // Récupère et formate les metar et les tafs
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
                }
                result.add(resultat.toString());
            }
        }

        return result;
    }
}