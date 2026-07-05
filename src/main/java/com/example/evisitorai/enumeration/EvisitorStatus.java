package com.example.evisitorai.enumeration;

public enum EvisitorStatus {
    NOT_SENT("Nije poslano"),
    SENT("Poslano"),
    FAILED("Greška");

    private final String label;

    EvisitorStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
