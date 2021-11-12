package com.example.metar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();


    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView code;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        resultp = data.get(position);
        code = (TextView) itemView.findViewById(R.id.code);

        code.setText(resultp.get("code"));
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resultp = data.get(position);

                String oaci=resultp.get("code").toString().substring(0,4);

                Intent resultIntent = new Intent(context, Results.class);
                resultIntent.putExtra("code", oaci);
                context.startActivity(resultIntent);

            }
        });
        return itemView;


    }
}
