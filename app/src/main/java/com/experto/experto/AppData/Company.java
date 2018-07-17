package com.experto.experto.AppData;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private List<Device> devices = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
