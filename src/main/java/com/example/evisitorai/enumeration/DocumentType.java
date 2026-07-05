package com.example.evisitorai.enumeration;

public enum DocumentType {
    IDENTITY_CARD_FOREIGN("Osobna iskaznica (strana)", "027"),
    PASSPORT_FOREIGN("Osobna putovnica (strana)", "002");

    private final String label;
    private final String evisitorCode;

    DocumentType(String label, String evisitorCode) {
        this.label = label;
        this.evisitorCode = evisitorCode;
    }

    public String getLabel() {
        return this.label;
    }

    public String getEvisitorCode() {
        return this.evisitorCode;
    }
}
