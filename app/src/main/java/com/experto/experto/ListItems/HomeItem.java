package com.experto.experto.ListItems;

import android.graphics.drawable.Drawable;

public class HomeItem {
    private String name;
    private Drawable img;

    public HomeItem(String name){
        this.name = name;
    }

    public HomeItem(String name, Drawable img){
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public Drawable getImg() {
        return img;
    }

    public void setName(String title) {
        this.name = title;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
