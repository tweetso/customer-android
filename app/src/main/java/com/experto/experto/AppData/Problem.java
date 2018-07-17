package com.experto.experto.AppData;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Problem implements Serializable {
    private String name;
    private String sku;
    private int partFee;
    private int serviceFee;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPartFee() {
        return partFee;
    }

    public void setPartFee(int partFee) {
        this.partFee = partFee;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

}
