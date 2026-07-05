package com.example.evisitorai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Scan(
        String documentType,
        String documentNumber,
        String touristName,
        String touristSurname,
        String gender,
        LocalDate dateOfBirth,
        String countryOfBirth,
        String citizenship,
        String countryOfResidence,
        String cityOfResidence
) {
}
