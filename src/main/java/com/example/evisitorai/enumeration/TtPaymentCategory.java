package com.example.evisitorai.enumeration;

import java.time.LocalDate;
import java.time.Period;

public enum TtPaymentCategory {
    A("A – Djeca do 12 godina", "1"),
    J("J – Djeca 12–18 godina", "2"),
    N("N – Turist u ugostiteljskom objektu", "14");

    private final String label;
    private final String evisitorCode;

    TtPaymentCategory(String label, String evisitorCode) {
        this.label = label;
        this.evisitorCode = evisitorCode;
    }

    public String getLabel() {
        return this.label;
    }

    public String getEvisitorCode() {
        return this.evisitorCode;
    }

    public static TtPaymentCategory forAgeOn(LocalDate dateOfBirth, LocalDate reference) {
        if (dateOfBirth == null || reference == null) {
            return null;
        }
        int age = Period.between(dateOfBirth, reference).getYears();
        if (age < 12) {
            return A;
        }
        if (age < 18) {
            return J;
        }
        return N;
    }
}
