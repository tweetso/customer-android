package com.experto.experto.ListItems;

import android.graphics.drawable.Drawable;

public class ProblemItem {

    private String name;
    private String price;
    private Drawable image;

    public ProblemItem(String name, int price,Drawable image){
        this.name = name;
        this.price = price+" SR.";
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
