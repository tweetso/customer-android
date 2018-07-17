package com.experto.experto.ListItems;

import android.graphics.drawable.Drawable;

public class ProblemImageItem {
    private Drawable img;

    public ProblemImageItem(Drawable drawable){
        this.img =drawable;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
