package com.example.metar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metar.R;

public class Taf extends Fragment {

    private static String code;
    private TextView resultat;

    public Taf(String code) {
        this.code=code;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Taf onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            View v = inflater.inflate(R.layout.fragment_taf, container, false);
            resultat = (TextView)v.findViewById(R.id.textview);
            resultat.setText(code);
            System.out.println("Taf onCreateView");
            return v;
    }
}