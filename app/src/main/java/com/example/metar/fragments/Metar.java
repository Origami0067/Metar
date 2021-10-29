package com.example.metar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metar.R;
import com.example.metar.Results;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Metar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Metar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String code;
    private String title;
    private TextView resultat;

    Results A = new Results();
    Document doc2 = A.doc;

    //Elements trs = doc2.select("table tr");


    String text="";


    public Metar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Metar.
     */
    // TODO: Rename and change types and number of parameters
    public static Metar newInstance(String param1, String param2) {
        Metar fragment = new Metar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        System.out.println("Metar new fragment");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Metar onCreate");
        if (getArguments() != null) {
            code = getArguments().getString(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_metar, container, false);
        resultat = (TextView)v.findViewById(R.id.textview);
        getSiteWeb(doc2);
        System.out.println("Metar onCreateView");
        return v;

    }

    private void getSiteWeb(Document doc) {
        //In the getSiteWeb() method, we create a new Thread to download the content of the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try{

                    Elements trs = doc.select("table tr");
                    String text="";

                    builder.append(title).append("\n");

                        builder.append("\n");
                        for (Element tr : trs) {
                            Elements tds = tr.getElementsByTag("td");
                            //Element td = tds.first();
                            text += tds.text() + "\n";
                        }
                        builder.append(text);
                        String title = doc.title();


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
}