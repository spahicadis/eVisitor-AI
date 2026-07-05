package com.example.evisitorai.entity;

import com.example.evisitorai.enumeration.DocumentType;
import com.example.evisitorai.enumeration.EvisitorStatus;
import com.example.evisitorai.enumeration.Gender;
import com.example.evisitorai.enumeration.TtPaymentCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "tourists")
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String touristName;

    @Column(nullable = false)
    private String touristSurname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(length = 3, nullable = false)
    private String countryOfBirth;

    @Column(length = 3, nullable = false)
    private String citizenship;

    @Column(length = 3, nullable = false)
    private String countryOfResidence;

    @Column(nullable = false)
    private String cityOfResidence;

    @ManyToOne(optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(nullable = false)
    private LocalDate stayFrom;

    @Column(nullable = false)
    private LocalTime timeStayFrom;

    @Column(nullable = false)
    private LocalDate foreseenStayUntil;

    @Column(nullable = false)
    private LocalTime timeEstimatedStayUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TtPaymentCategory ttPaymentCategory;

    /** GUID prijave koji šaljemo eVisitoru (ID). */
    private UUID evisitorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvisitorStatus evisitorStatus = EvisitorStatus.NOT_SENT;

    /** Trenutak slanja u eVisitor. */
    private LocalDateTime sentAt;

    @Column(length = 1000)
    private String evisitorError;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Tourist() {
        // JPA requirement, public bc is used in service layer
    }

    public UUID getId() {
        return this.id;
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

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
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

    public UUID getEvisitorId() {
        return this.evisitorId;
    }

    public void setEvisitorId(UUID evisitorId) {
        this.evisitorId = evisitorId;
    }

    public EvisitorStatus getEvisitorStatus() {
        return this.evisitorStatus;
    }

    public void setEvisitorStatus(EvisitorStatus evisitorStatus) {
        this.evisitorStatus = evisitorStatus;
    }

    public LocalDateTime getSentAt() {
        return this.sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getEvisitorError() {
        return this.evisitorError;
    }

    public void setEvisitorError(String evisitorError) {
        this.evisitorError = evisitorError;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Transient
    public long getNights() {
        if (this.stayFrom != null && this.foreseenStayUntil != null) {
            return ChronoUnit.DAYS.between(this.stayFrom, this.foreseenStayUntil);
        }
        return 0;
    }
}
