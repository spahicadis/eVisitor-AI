package com.example.evisitorai.service;

import com.example.evisitorai.dto.TouristForm;
import com.example.evisitorai.entity.Facility;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.repository.FacilityRepository;
import com.example.evisitorai.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TouristService {

    private final TouristRepository touristRepository;
    private final FacilityRepository facilityRepository;

    public TouristService(TouristRepository touristRepository,
                          FacilityRepository facilityRepository) {
        this.touristRepository = touristRepository;
        this.facilityRepository = facilityRepository;
    }

    public Tourist get(UUID id) {
        return this.touristRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Turist ne postoji: " + id));
    }

    public Tourist update(UUID id, TouristForm form) {
        Tourist tourist = get(id);
        Facility facility = this.facilityRepository.findById(form.getFacilityId())
                .orElseThrow(() -> new NotFoundException(
                        "Objekt ne postoji: " + form.getFacilityId()));

        tourist.setDocumentType(form.getDocumentType());
        tourist.setDocumentNumber(form.getDocumentNumber());
        tourist.setTouristName(form.getTouristName());
        tourist.setTouristSurname(form.getTouristSurname());
        tourist.setGender(form.getGender());
        tourist.setDateOfBirth(form.getDateOfBirth());
        tourist.setCountryOfBirth(form.getCountryOfBirth());
        tourist.setCitizenship(form.getCitizenship());
        tourist.setCountryOfResidence(form.getCountryOfResidence());
        tourist.setCityOfResidence(form.getCityOfResidence());
        tourist.setFacility(facility);
        tourist.setStayFrom(form.getStayFrom());
        tourist.setTimeStayFrom(form.getTimeStayFrom());
        tourist.setForeseenStayUntil(form.getForeseenStayUntil());
        tourist.setTimeEstimatedStayUntil(form.getTimeEstimatedStayUntil());
        tourist.setTtPaymentCategory(form.getTtPaymentCategory());

        return this.touristRepository.save(tourist);
    }

    public Tourist delete(UUID id) {
        Tourist tourist = get(id);
        this.touristRepository.delete(tourist);
        return tourist;
    }
}
