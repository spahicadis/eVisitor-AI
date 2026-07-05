package com.example.evisitorai.service;

import com.example.evisitorai.dto.FacilityCard;
import com.example.evisitorai.entity.Facility;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.repository.FacilityRepository;
import com.example.evisitorai.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacilityOverviewService {

    private final FacilityRepository facilityRepository;
    private final TouristRepository touristRepository;

    public FacilityOverviewService(FacilityRepository facilityRepository,
                                   TouristRepository touristRepository) {
        this.facilityRepository = facilityRepository;
        this.touristRepository = touristRepository;
    }

    public List<FacilityCard> getFacilityCards() {
        Map<UUID, Double> averageNightsByFacility = this.touristRepository.findAll().stream()
                .filter(tourist -> tourist.getFacility() != null)
                .collect(Collectors.groupingBy(
                        tourist -> tourist.getFacility().getId(),
                        Collectors.averagingLong(Tourist::getNights)));

        return this.facilityRepository.findAll().stream()
                .sorted(Comparator.comparing(Facility::getName))
                .map(facility -> new FacilityCard(
                        facility,
                        averageNightsByFacility.getOrDefault(facility.getId(), 0.0)))
                .toList();
    }
}
