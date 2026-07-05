package com.example.evisitorai.service;

import com.example.evisitorai.dto.EvisitorCheckInPayload;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.enumeration.DocumentType;
import com.example.evisitorai.enumeration.EvisitorStatus;
import com.example.evisitorai.enumeration.Gender;
import com.example.evisitorai.enumeration.TtPaymentCategory;
import com.example.evisitorai.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class EvisitorService {

    /** eVisitor pravilo za datume -> YYYYMMDD, vremena -> HH:mm. */
    private static final DateTimeFormatter DATE = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final int ERROR_MAX_LENGTH = 1000;

    /**
     * eVisitor CodeMI organizacije dolaska "Osoban" (ArrivalOrganisationLookup). Uvijek
     * prijavljujem osobno, pa je hardkodirano prema vlastitim potrebama.
     */
    private static final String ARRIVAL_ORGANISATION_CODE = "I";

    /** eVisitor vrsta pružene usluge "Noćenje" (OfferedServiceType) – obavezno, uvijek ta vrijednost. */
    private static final String OFFERED_SERVICE_TYPE = "Noćenje";

    private final TouristRepository touristRepository;
    private final EvisitorClient evisitorClient;

    public EvisitorService(TouristRepository touristRepository,
                           EvisitorClient evisitorClient) {
        this.touristRepository = touristRepository;
        this.evisitorClient = evisitorClient;
    }

    public Tourist send(UUID touristId) {
        Tourist tourist = this.touristRepository.findById(touristId)
                .orElseThrow(() -> new NotFoundException("Turist ne postoji: " + touristId));

        // Osiguranje od dvostruke prijave
        if (tourist.getEvisitorStatus() == EvisitorStatus.SENT) {
            return tourist;
        }

        TtPaymentCategory category = TtPaymentCategory.forAgeOn(
                tourist.getDateOfBirth(),
                tourist.getStayFrom() != null ? tourist.getStayFrom() : LocalDate.now());
        if (category != null) {
            tourist.setTtPaymentCategory(category);
        }

        UUID evisitorId = UUID.randomUUID();

        try {
            this.evisitorClient.checkIn(toPayload(tourist, evisitorId));
            tourist.setEvisitorId(evisitorId);
            tourist.setEvisitorStatus(EvisitorStatus.SENT);
            tourist.setSentAt(LocalDateTime.now());
            tourist.setEvisitorError(null);
        } catch (EvisitorException e) {
            tourist.setEvisitorStatus(EvisitorStatus.FAILED);
            tourist.setEvisitorError(truncate(e.getMessage()));
        }

        return this.touristRepository.save(tourist);
    }

    private EvisitorCheckInPayload toPayload(Tourist tourist, UUID evisitorId) {
        return new EvisitorCheckInPayload(
                evisitorId.toString(),
                ARRIVAL_ORGANISATION_CODE,
                OFFERED_SERVICE_TYPE,
                tourist.getCitizenship(),
                tourist.getCityOfResidence(),
                tourist.getCountryOfBirth(),
                tourist.getCountryOfResidence(),
                date(tourist.getDateOfBirth()),
                tourist.getDocumentNumber(),
                documentTypeCode(tourist.getDocumentType()),
                tourist.getFacility() != null ? tourist.getFacility().getCode() : null,
                date(tourist.getForeseenStayUntil()),
                genderValue(tourist.getGender()),
                date(tourist.getStayFrom()),
                time(tourist.getTimeEstimatedStayUntil()),
                time(tourist.getTimeStayFrom()),
                tourist.getTouristName(),
                tourist.getTouristSurname(),
                paymentCategoryCode(tourist.getTtPaymentCategory()));
    }

    private String documentTypeCode(DocumentType documentType) {
        return documentType == null ? null : documentType.getEvisitorCode();
    }

    private String genderValue(Gender gender) {
        return gender == null ? null : gender.getEvisitorValue();
    }

    private String paymentCategoryCode(TtPaymentCategory category) {
        return category == null ? null : category.getEvisitorCode();
    }

    private String date(LocalDate date) {
        return date == null ? null : date.format(DATE);
    }

    private String time(LocalTime time) {
        return time == null ? null : time.format(TIME);
    }

    private String truncate(String message) {
        if (message == null) {
            return null;
        }
        return message.length() <= ERROR_MAX_LENGTH ? message : message.substring(0, ERROR_MAX_LENGTH);
    }
}
