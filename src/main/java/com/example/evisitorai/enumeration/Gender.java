package com.example.evisitorai.enumeration;

public enum Gender {
    MALE("Muški", "muški"),
    FEMALE("Ženski", "ženski");

    private final String label;
    private final String evisitorValue;

    Gender(String label, String evisitorValue) {
        this.label = label;
        this.evisitorValue = evisitorValue;
    }

    public String getLabel() {
        return this.label;
    }

    public String getEvisitorValue() {
        return this.evisitorValue;
    }
}
