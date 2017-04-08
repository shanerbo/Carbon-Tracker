package com.example.olive.carbon_tracker.Model;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.provider.MediaStore;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olive.carbon_tracker.R;

import java.util.ArrayList;

/**
 * Helps adapt vehicle icons
 */

public class ImageAdapter extends ArrayAdapter<ImageSpinnerData> {
    int itemID;
    Activity context;
    ArrayList<ImageSpinnerData> list;
    LayoutInflater inflater;
    public ImageAdapter(Activity context, int itemID, int id, ArrayList<ImageSpinnerData> list){
        super(context,id,list);
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemID = itemID;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(itemID,parent,false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.car_img);
        imageView.setImageResource(list.get(position).getImageID());
        TextView textView = (TextView) itemView.findViewById(R.id.car_txt);
        textView.setText(list.get(position).getCarTxt());
        return itemView;

    }
    public View getDropDownView(int position, View convertView,ViewGroup parent){
        return getView(position,convertView,parent);
    }
}

