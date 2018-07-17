package com.experto.experto.AppData;

import java.util.ArrayList;
import java.util.List;

public class ServiceA extends Service {

    private List<Company> companies = new ArrayList<>();


    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

}
