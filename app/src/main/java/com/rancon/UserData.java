package com.rancon;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaishu on 5/2/18.
 */

public class UserData {

    String cardId;
    List<String> nameList = new ArrayList<>();
    List<String> locationList = new ArrayList<>();
    List<Bitmap> pictureList = new ArrayList<>();
    public  UserData(String cardId) {
        this.cardId = cardId;
    }
    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    public List<Bitmap> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Bitmap> pictureList) {
        this.pictureList = pictureList;
    }
}
