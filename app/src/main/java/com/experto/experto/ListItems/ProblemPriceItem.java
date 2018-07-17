package com.experto.experto.ListItems;

public class ProblemPriceItem {
    private String name;
    private String price;

    public ProblemPriceItem(String name, int price){
        this.name = name;
        this.price = price+" SR.";
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
}
