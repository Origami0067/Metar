package com.example.metar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        // Declare Variables
        TextView rank;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        resultp = data.get(position);
        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.rank);

        // Capture position and set results to the TextViews
        rank.setText(resultp.get(MainActivity.RANK));
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                String oaci=resultp.get("rank").toString().substring(0,4);

                Intent resultIntent = new Intent(context, Results.class);
                // Pass all data rank
                resultIntent.putExtra("code", oaci);
                //Start SingleItemView Class
                context.startActivity(resultIntent);

            }
        });
        return itemView;


    }
}
