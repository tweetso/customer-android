package com.experto.experto.AppData;

import java.util.ArrayList;
import java.util.List;

public class Device {

    private String name;
    private List<Problem> problems = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
