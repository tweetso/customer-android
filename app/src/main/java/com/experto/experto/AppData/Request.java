package com.experto.experto.AppData;

import android.support.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request{
    private int state;
    private String rid;
    private String cid;
    private String tid;
    private double cost;
    private Timestamp picked;
    private List<String> sku = new ArrayList<>();
    private Timestamp created;
    private GeoPoint cLocation;
    private HashMap<String,Object> cRating =new HashMap<>();
    private HashMap<String,Object> tRating =new HashMap<>();
    private Boolean canceledByTechnician;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Timestamp getPicked() {
        return picked;
    }

    public void setPicked(Timestamp picked) {
        this.picked = picked;
    }

    public List<String> getSku() {
        return sku;
    }

    public void setSku(List<String> sku) {
        this.sku = sku;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public GeoPoint getcLocation() {
        return cLocation;
    }

    public void setcLocation(GeoPoint cLocation) {
        this.cLocation = cLocation;
    }

    public HashMap<String, Object> getcRating() {
        return cRating;
    }

    public void setcRating(HashMap<String, Object> cRating) {
        this.cRating = cRating;
    }

    public HashMap<String, Object> gettRating() {
        return tRating;
    }

    public void settRating(HashMap<String, Object> tRating) {
        this.tRating = tRating;
    }

    public Boolean getCanceledByTechnician() {
        return canceledByTechnician;
    }

    public void setCanceledByTechnician(Boolean canceledByTechnician) {
        this.canceledByTechnician = canceledByTechnician;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }


}
