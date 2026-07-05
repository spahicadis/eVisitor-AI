package com.example.evisitorai.dto;

import com.example.evisitorai.entity.Tourist;

import java.util.List;

public record TouristOverview(
        List<Tourist> tourists,
        long total,
        long returning,
        List<CountryCount> topCountries) {
}
