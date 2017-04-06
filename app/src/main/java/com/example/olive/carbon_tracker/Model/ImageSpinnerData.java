package com.example.olive.carbon_tracker.Model;

import android.media.Image;

/**
 * Created by Unchained_Erbo on 2017-04-03.
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
