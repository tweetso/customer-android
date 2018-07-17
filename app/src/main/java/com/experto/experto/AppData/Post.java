package com.experto.experto.AppData;


import com.google.firebase.Timestamp;

public class Post {
    private String content;
    private String imageURL;
    private Timestamp timeStamp;

    public Post(){

    }

    public Post(String content, String imageURL, Timestamp timeStamp) {
        this.content = content;
        this.imageURL = imageURL;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
