package com.example.metar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metar.R;


public class AirportInfos extends Fragment {

    private static String code;
    private TextView resultat;

    public AirportInfos(String code) {
        this.code=code;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Info onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_airport_infos, container, false);
        resultat = v.findViewById(R.id.textview);
        resultat.setText(code);
        System.out.println("Info onCreateView");
        return v;

    }
}