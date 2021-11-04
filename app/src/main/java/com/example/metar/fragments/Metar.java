package com.example.metar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metar.R;

public class Metar extends Fragment {

    private static String resultat;
    private TextView textView;

    public Metar(String resultat) {
        this.resultat = resultat;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Metar onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_metar, container, false);
        textView = (TextView)v.findViewById(R.id.textview);
        textView.setText(resultat);
        System.out.println("Metar onCreateView");
        return v;

    }


}