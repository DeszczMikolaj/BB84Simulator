package com.company;

public enum Strategy {

    ALL ("all qubits"),
    SERIES ("series");

    private String name;

    Strategy(String s) {
        this.name = s ;
    }

    public String getName() {
        return name;
    }
}
