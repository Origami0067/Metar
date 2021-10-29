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
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();


    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);

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
        ImageView flag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.rank);
        // Locate the ImageView in listview_item.xml
        flag = (ImageView) itemView.findViewById(R.id.flag);

        // Capture position and set results to the TextViews
        rank.setText(resultp.get(MainActivity.RANK));
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(resultp.get(MainActivity.FLAG), flag);
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                System.out.println("resultp : \n"+resultp+"\n");

                String oaci = resultp.toString().substring(6,10);
                Toast toast = Toast.makeText(context, oaci, Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(context, Results.class);
                // Pass all data rank
                intent.putExtra("code", oaci);
                intent.putExtra("flag", resultp.get(MainActivity.FLAG));
                // Pass all data country
                /*intent.putExtra("country", resultp.get(MainActivity.COUNTRY));
                // Pass all data population
                intent.putExtra("population",resultp.get(MainActivity.POPULATION));
                // Pass all data flag
                intent.putExtra("flag", resultp.get(MainActivity.FLAG));*/
                //Start SingleItemView Class
                context.startActivity(intent);

            }
        });
        return itemView;


    }
}
