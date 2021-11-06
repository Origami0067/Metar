package com.example.metar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ListView listview;
    ListAdapter adapter;
    int drapeau[] = {R.drawable.fr};
    ArrayList<String> items;
    ArrayList<HashMap<String, String>> tdList;
    static String RANK = "rank";
    static String FLAG = "flag";


    boolean noData=false;
    private TextView resultat;
    private EditText codeOACI;
    private String code;
    private String countryCode;

    public String url;
    Context context = this;
    int duration = Toast.LENGTH_SHORT;

    private String uriBuilder(String code) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.aviationweather.gov")
                .appendPath("metar")
                .appendPath("data")
                .appendQueryParameter("ids", code)
                .appendQueryParameter("format", "decoded")
                .appendQueryParameter("date", "")
                .appendQueryParameter("hours", "0")
                .appendQueryParameter("taf", "false");
        return builder.build().toString();
    }

    private String srcBuilder(String countryCode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("purecatamphetamine.github.io")
                .appendPath("country-flag-icons")
                .appendPath("3x2")
                .appendPath(countryCode + ".svg");
        return builder.build().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get instances of the Button and the TextView from our layout
        resultat = findViewById(R.id.resultat);
        codeOACI = findViewById(R.id.codeOACI);
        Button getBtn = findViewById(R.id.getBtn);


        //set a click listener on the Button to start the download of the website when the user will click it
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!codeOACI.getText().toString().equals("")) { //to be sure there's at least one code
                    getSiteWeb();
                    new JsoupListView().execute();
                } else {
                    CharSequence text = getResources().getString(R.string.emptyField);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }


    private void getSiteWeb() {
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                code = String.valueOf(codeOACI.getText());
                try {
                    url = uriBuilder(code);
                } catch (Exception e) {
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


    private class JsoupListView extends AsyncTask<Void, Void, Void> {

        // Create a progressdialog
        ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Set progressdialog title
            // Set progressdialog message
            mProgressDialog.setMessage(getResources().getString(R.string.loadingMain));
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // Create an array
            tdList = new ArrayList<HashMap<String, String>>();


            try {

                // Connect to the Website URL
                Document doc = Jsoup.connect(url).get();
                // Identify Div id=awc_main_content_wrap

                for (Element div : doc.select("div[id=awc_main_content_wrap]")) {

                    for (Element table : div.select("table")) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Elements td = table.select("tr").select("td");
                        countryCode = td.get(1).text();
                        StringBuffer sb = new StringBuffer(countryCode);
                        sb.delete(countryCode.length() - 1, countryCode.length());
                        while (sb.length() != 2) {
                            sb.delete(0, 1);
                        }

                        System.out.println(sb);
                        String cCode = sb.toString();

                        String imgUrl = srcBuilder(cCode);
                        System.out.println(imgUrl);

                        map.put("rank", td.get(1).text());
                        map.put("flag", imgUrl);

                        if (!td.get(3).text().equals("No data found")) {
                            tdList.add(map);
                        }else{
                            noData=true;
                        }

                        // Identify all the table row's(tr)
/*
                    for (Element row : table.select("tr")) {
                        //HashMap<String, String> map = new HashMap<String, String>();

                        // Identify all the table cell's(td)
                        Elements tds = row.select("td");

                        // Identify all img src's
                        //Elements imgSrc = row.select("img[src]");
                        // Get only src from img src
                        //String imgSrcStr = imgSrc.attr("src");

                        // Retrive Jsoup Elements
                        // Get the first td
                        map.put("rank", tds.get(0).text());
                        // Get the second td
                        //map.put("country", tds.get(1).text());
                        // Get the third td
                        //map.put("population", tds.get(2).text());
                        // Get the image src links
                        //map.put("flag", imgSrcStr);
                        // Set all extracted Jsoup Elements into the array
                        arraylist.add(map);
                    }
*/
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if(noData) {
                CharSequence text = getResources().getString(R.string.no_data_found);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
            }
            noData=false;
            // Locate the listview in listview_main.xml
            listview = findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this, tdList);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();

        }


    }


}