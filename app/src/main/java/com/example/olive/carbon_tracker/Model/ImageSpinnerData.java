package com.example.olive.carbon_tracker.Model;

import android.media.Image;

/**
 * Stores vehicle icons spinner data
 */

public class ImageSpinnerData {
    String carTxt;
    int imageID;
    public ImageSpinnerData(String txt,Integer imageid){
        this.carTxt = txt;
        this.imageID = imageid;
    }
    public String getCarTxt(){
        return carTxt;
    }
    public int getImageID(){
        return imageID;
    }

}
