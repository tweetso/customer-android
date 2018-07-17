package com.experto.experto.AppData;

import java.util.ArrayList;
import java.util.List;

public class ServiceB extends Service {


    private List<Problem> problems = new ArrayList<>();

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
