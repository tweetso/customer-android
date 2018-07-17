package com.experto.experto.ListItems;

import android.support.annotation.NonNull;
import android.widget.Button;

import com.experto.experto.Adapters.ProblemImageItemsAdapter;

public class RequestItem  implements Comparable {
    private String device;
    private String id;
    private String price;
    private String state;
    private String date;
    private ProblemImageItemsAdapter adapter;
    public RequestItem(){

    }
    public RequestItem(String device, String id, String price, String state, String date, ProblemImageItemsAdapter requestsList) {
        this.device = device;
        this.id = id;
        this.price = price;
        this.state = state;
        this.date = date;
        this.adapter = requestsList;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ProblemImageItemsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ProblemImageItemsAdapter requestsList) {
        this.adapter = requestsList;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(!(o instanceof RequestItem)){
            return -1;
        }
        else {
            RequestItem requestItem = (RequestItem) o;
            return requestItem.getId().compareTo(this.getId());
        }
    }
}
