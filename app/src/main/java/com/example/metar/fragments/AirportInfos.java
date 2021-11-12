package com.example.metar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metar.R;


public class AirportInfos extends Fragment {

    private final String resultat;

    public AirportInfos(String code) {
        this.resultat=code;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_airport_infos, container, false);
        TextView textView = v.findViewById(R.id.textview);
        textView.setText(resultat);
        return v;

    }
}