package com.example.evisitorai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON tijelo za eVisitor akciju CheckInTourist. Nazivi polja (PascalCase)
 * moraju točno odgovarati eVisitor API-ju, fiksirani kroz @JsonProperty.
 * Neobavezna polja koja su null se izostavljaju (NON_NULL).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EvisitorCheckInPayload(
        @JsonProperty("ID") String id,
        @JsonProperty("ArrivalOrganisation") String arrivalOrganisation,
        @JsonProperty("OfferedServiceType") String offeredServiceType,
        @JsonProperty("Citizenship") String citizenship,
        @JsonProperty("CityOfResidence") String cityOfResidence,
        @JsonProperty("CountryOfBirth") String countryOfBirth,
        @JsonProperty("CountryOfResidence") String countryOfResidence,
        @JsonProperty("DateOfBirth") String dateOfBirth,
        @JsonProperty("DocumentNumber") String documentNumber,
        @JsonProperty("DocumentType") String documentType,
        @JsonProperty("Facility") String facility,
        @JsonProperty("ForeseenStayUntil") String foreseenStayUntil,
        @JsonProperty("Gender") String gender,
        @JsonProperty("StayFrom") String stayFrom,
        @JsonProperty("TimeEstimatedStayUntil") String timeEstimatedStayUntil,
        @JsonProperty("TimeStayFrom") String timeStayFrom,
        @JsonProperty("TouristName") String touristName,
        @JsonProperty("TouristSurname") String touristSurname,
        @JsonProperty("TTPaymentCategory") String ttPaymentCategory) {
}
