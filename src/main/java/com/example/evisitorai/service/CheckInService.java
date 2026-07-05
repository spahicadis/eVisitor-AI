package com.example.evisitorai.service;

import com.example.evisitorai.dto.TouristForm;
import com.example.evisitorai.entity.Facility;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.repository.FacilityRepository;
import com.example.evisitorai.repository.TouristRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckInService {

    private final TouristRepository touristRepository;
    private final FacilityRepository facilityRepository;

    public CheckInService(TouristRepository touristRepository,
                          FacilityRepository facilityRepository) {
        this.touristRepository = touristRepository;
        this.facilityRepository = facilityRepository;
    }

    @Transactional
    public int saveCheckIn(List<TouristForm> forms) {
        List<Tourist> tourists = forms.stream()
                .map(this::toEntity)
                .toList();

        this.touristRepository.saveAll(tourists);
        return tourists.size();
    }

    private Tourist toEntity(TouristForm form) {
        Facility facility = this.facilityRepository.findById(form.getFacilityId())
                .orElseThrow(() -> new NotFoundException(
                        "Objekt ne postoji: " + form.getFacilityId()));

        Tourist tourist = new Tourist();
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
        return tourist;
    }
}
