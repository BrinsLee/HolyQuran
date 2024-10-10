package com.ihyas.soharamkarubar;
//this adapter to listview of sours of Quran

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CustomList extends ArrayAdapter<String> {

    //=====================================


    //===================================


    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;


    public CustomList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

//    Anees Code Starts

//    You need to implement the getCount method within your custom ArrayAdapter.
//    The ListView invokes that method on its adapter to find out how many items it will display.
//    You need something like this:

    public int getCount() {
        return imageId.length;
    }

//    Anees Code Ends

    @Override
    public View getView(int position, View view, ViewGroup parent) {


//====================================


        //============================
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, false);
        //  TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        //  txtTitle.setText(web[position]);


        imageView.setImageResource(imageId[position]);


        return rowView;

    }

}