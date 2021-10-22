package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private TextView resultat;
    private EditText codeOACI;
    private String code;


    private String uriBuilder(String code) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.aviationweather.gov")
                .appendPath("metar")
                .appendPath("data")
                .appendQueryParameter("ids",code)
                .appendQueryParameter("format", "decoded")
                .appendQueryParameter("date","")
                .appendQueryParameter("hours", "0")
                .appendQueryParameter("taf", "false");
        String myUrl = builder.build().toString();
        return myUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get instances of the Button and the TextView from our layout
        resultat = (TextView)findViewById(R.id.resultat);
        codeOACI = (EditText)findViewById(R.id.codeOACI);
        getBtn = (Button)findViewById(R.id.getBtn);



        //set a click listener on the Button to start the download of the website when the user will click it
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSiteWeb();
            }
        });
    }

    private void getSiteWeb(){
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                code = String.valueOf(codeOACI.getText());

                try{
                    String url = uriBuilder(code);
                    System.out.println(url);
                    Document doc = Jsoup.connect(url).get();//url
                    String title = doc.title();

                    Elements trs = doc.select("table tr");

                    String text="";

                    for (Element tr : trs) {
                        Elements tds = tr.getElementsByTag("td");
                        //Element td = tds.first();
                        text+=tds.text()+"\n";
                    }
                    builder.append(text);

                    /*builder.append(title).append("\n");

                    for (Element link : links) {
                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());
                    }*/
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultat.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}