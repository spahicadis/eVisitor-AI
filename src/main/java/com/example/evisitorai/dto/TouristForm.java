package com.example.evisitorai.dto;

import com.example.evisitorai.enumeration.DocumentType;
import com.example.evisitorai.enumeration.Gender;
import com.example.evisitorai.enumeration.TtPaymentCategory;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class TouristForm {

    @NotNull(message = "Vrsta isprave je obavezna.")
    private DocumentType documentType;

    @NotBlank(message = "Broj isprave je obavezan.")
    private String documentNumber;

    @NotBlank(message = "Ime je obavezno.")
    private String touristName;

    @NotBlank(message = "Prezime je obavezno.")
    private String touristSurname;

    @NotNull(message = "Spol je obavezan.")
    private Gender gender;

    @NotNull(message = "Datum rođenja je obavezan.")
    @Past(message = "Datum rođenja mora biti u prošlosti.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Država rođenja je obavezna.")
    @Size(min = 3, max = 3, message = "Koristi ISO3 kod (npr. HRV).")
    private String countryOfBirth;

    @NotBlank(message = "Državljanstvo je obavezno.")
    @Size(min = 3, max = 3, message = "Koristi ISO3 kod (npr. HRV).")
    private String citizenship;

    @NotBlank(message = "Država prebivališta je obavezna.")
    @Size(min = 3, max = 3, message = "Koristi ISO3 kod (npr. HRV).")
    private String countryOfResidence;

    @NotBlank(message = "Grad prebivališta je obavezan.")
    private String cityOfResidence;

    @NotNull(message = "Objekt je obavezan.")
    private UUID facilityId;

    @NotNull(message = "Datum dolaska je obavezan.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate stayFrom;

    @NotNull(message = "Vrijeme dolaska je obavezno.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeStayFrom = LocalTime.of(16, 0);

    @NotNull(message = "Predviđeni datum odlaska je obavezan.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate foreseenStayUntil;

    @NotNull(message = "Predviđeno vrijeme odlaska je obavezno.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEstimatedStayUntil = LocalTime.of(10, 0);

    @NotNull(message = "Kategorija naplate je obavezna.")
    private TtPaymentCategory ttPaymentCategory;

    @AssertTrue(message = "Datum odlaska ne može biti prije datuma dolaska.")
    public boolean isStayPeriodValid() {
        return this.stayFrom == null || this.foreseenStayUntil == null
                || !this.foreseenStayUntil.isBefore(this.stayFrom);
    }

    public long getNights() {
        if (this.stayFrom != null && this.foreseenStayUntil != null) {
            return ChronoUnit.DAYS.between(this.stayFrom, this.foreseenStayUntil);
        }
        return 0;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getTouristName() {
        return this.touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public String getTouristSurname() {
        return this.touristSurname;
    }

    public void setTouristSurname(String touristSurname) {
        this.touristSurname = touristSurname;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryOfBirth() {
        return this.countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getCitizenship() {
        return this.citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getCountryOfResidence() {
        return this.countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getCityOfResidence() {
        return this.cityOfResidence;
    }

    public void setCityOfResidence(String cityOfResidence) {
        this.cityOfResidence = cityOfResidence;
    }

    public UUID getFacilityId() {
        return this.facilityId;
    }

    public void setFacilityId(UUID facilityId) {
        this.facilityId = facilityId;
    }

    public LocalDate getStayFrom() {
        return this.stayFrom;
    }

    public void setStayFrom(LocalDate stayFrom) {
        this.stayFrom = stayFrom;
    }

    public LocalTime getTimeStayFrom() {
        return this.timeStayFrom;
    }

    public void setTimeStayFrom(LocalTime timeStayFrom) {
        this.timeStayFrom = timeStayFrom;
    }

    public LocalDate getForeseenStayUntil() {
        return this.foreseenStayUntil;
    }

    public void setForeseenStayUntil(LocalDate foreseenStayUntil) {
        this.foreseenStayUntil = foreseenStayUntil;
    }

    public LocalTime getTimeEstimatedStayUntil() {
        return this.timeEstimatedStayUntil;
    }

    public void setTimeEstimatedStayUntil(LocalTime timeEstimatedStayUntil) {
        this.timeEstimatedStayUntil = timeEstimatedStayUntil;
    }

    public TtPaymentCategory getTtPaymentCategory() {
        return this.ttPaymentCategory;
    }

    public void setTtPaymentCategory(TtPaymentCategory ttPaymentCategory) {
        this.ttPaymentCategory = ttPaymentCategory;
    }
}
