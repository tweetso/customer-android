package com.experto.experto.AppData;

public class SKU {
    private String companyName;
    private String deviceName;
    private String deviceType;
    private String problemName;
    private int serviceFee;
    private int partFee;

    public SKU(String companyName, String deviceName, String deviceType, String problemName, int serviceFee, int partFee) {
        this.companyName = companyName;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.problemName = problemName;
        this.serviceFee = serviceFee;
        this.partFee = partFee;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

    public int getPartFee() {
        return partFee;
    }

    public void setPartFee(int partFee) {
        this.partFee = partFee;
    }
}
