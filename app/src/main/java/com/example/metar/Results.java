package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;


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
    public Document doc;
    TabLayout layoutMT;
    ViewPager2 viewSliders;
    FragmentAdapter adapter;
    FloatingActionButton map;

    Context context;
    ArrayList<String> metartaf = new ArrayList<String>();


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

                System.out.println("click oaci : " + codeOACI);
                startActivity(mapIntent);
            }
        });

    }

    public Results(){
    }

    private void getSiteWeb(String url) {
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    doc = Jsoup.connect(url).get();//url
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class JsoupListView extends AsyncTask<Void, Void, Void> {

        // Create a progressdialog
        ProgressDialog mProgressDialog = new ProgressDialog(Results.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // Create an array
            String url="https://www.aviationweather.gov/metar/data?ids="+codeOACI+"&format=decoded&hours=0&taf=on&layout=off";

            try{
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();//url
                Document docInfo = Jsoup.connect("https://ourairports.com/airports/"+codeOACI+"/").get();
                String title = doc.title();
                System.out.println(title);
                for (Element div : doc.select("div[id=awc_main_content_wrap]")) {

                    for (Element table : div.select("table")) {

                        metartaf.add(table.text());
                    }
                }

                for(Element tables : docInfo.select("table[class=small table table-stripped]")){
                    metartaf.add(tables.text());
                }
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
            adapter = new FragmentAdapter(fm, getLifecycle(), codeOACI, metartaf);
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
            mProgressDialog.dismiss();

        }


    }
}