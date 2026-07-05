package com.example.evisitorai.service;

import com.example.evisitorai.dto.CountryCount;
import com.example.evisitorai.dto.TouristOverview;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TouristOverviewService {

    private final TouristRepository touristRepository;

    public TouristOverviewService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public TouristOverview getOverview() {
        List<Tourist> tourists = this.touristRepository.findAllByOrderByCreatedAtDesc();

        long total = tourists.size();
        long returning = countReturning(tourists);
        List<CountryCount> topCountries = topCountries(tourists);

        return new TouristOverview(tourists, total, returning, topCountries);
    }

    private long countReturning(List<Tourist> tourists) {
        Map<String, Long> countByDocument = tourists.stream()
                .collect(Collectors.groupingBy(Tourist::getDocumentNumber, Collectors.counting()));

        return countByDocument.values().stream()
                .filter(count -> count > 1)
                .count();
    }

    private List<CountryCount> topCountries(List<Tourist> tourists) {
        Map<String, Long> countByCitizenship = tourists.stream()
                .filter(tourist -> tourist.getCitizenship() != null && !tourist.getCitizenship().isBlank())
                .collect(Collectors.groupingBy(Tourist::getCitizenship, Collectors.counting()));

        return countByCitizenship.entrySet().stream()
                .map(entry -> new CountryCount(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingLong(CountryCount::count).reversed())
                .limit(3)
                .toList();
    }
}
