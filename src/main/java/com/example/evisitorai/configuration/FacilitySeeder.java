package com.example.evisitorai.configuration;

import com.example.evisitorai.entity.Amenity;
import com.example.evisitorai.entity.Facility;
import com.example.evisitorai.repository.FacilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FacilitySeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(FacilitySeeder.class);

    private final FacilityRepository facilityRepository;

    public FacilitySeeder(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Override
    public void run(String... args) {
        saveIfMissing(villaStella());
        saveIfMissing(appStella());
    }

    private void saveIfMissing(Facility facility) {
        if (this.facilityRepository.findByCode(facility.getCode()).isEmpty()) {
            this.facilityRepository.save(facility);
            log.info("Seed: ubačen objekt {} ({}).", facility.getName(), facility.getCode());
        }
    }

    private Facility villaStella() {
        Facility facility = new Facility("0263295", "Villa Stella");
        facility.setAddress("Bože Milanovića 1, Novigrad, Bužinija");
        facility.setImageUrl("https://villa-stella-app.com/images/smjestaj/me804t9xonixefpuvkc3.jpg");
        facility.setMaxPersons("8");
        facility.setBedrooms(3);
        facility.setBathrooms(3);
        facility.setAreaSqm(new BigDecimal("115.00"));
        facility.setNote("Objekti u domaćinstvu; kategorija 4*; razdoblje rada 01.05.–31.10.");
        facility.setAmenities(List.of(
                new Amenity("p-circle", "Parking"),
                new Amenity("cup-hot", "Aparat za kavu"),
                new Amenity("thermometer-half", "Grijanje"),
                new Amenity("tv", "Sat-TV"),
                new Amenity("door-open", "Balkon/terasa"),
                new Amenity("heart", "Kućni ljubimci"),
                new Amenity("fire", "Roštilj"),
                new Amenity("moisture", "Perilica rublja"),
                new Amenity("tree", "Vrt"),
                new Amenity("box", "Mikrovalna"),
                new Amenity("layers", "Posteljina"),
                new Amenity("droplet", "Ručnici"),
                new Amenity("snow", "Hladnjak"),
                new Amenity("droplet-half", "Perilica posuđa"),
                new Amenity("wifi", "WiFi"),
                new Amenity("wind", "Klima"),
                new Amenity("water", "Bazen"),
                new Amenity("house-heart", "Kamin")));
        return facility;
    }

    private Facility appStella() {
        Facility facility = new Facility("0346212", "APP STELLA");
        facility.setAddress("Joakima Rakovca 2, Novigrad, Bužinija");
        facility.setImageUrl("https://villa-stella-app.com/images/smjestaj/11bntwb2ov9j63kvenle.jpg");
        facility.setMaxPersons("6+2");
        facility.setBedrooms(2);
        facility.setBathrooms(3);
        facility.setAreaSqm(new BigDecimal("105.00"));
        facility.setNote("Objekti u domaćinstvu; kategorija 3*");
        facility.setAmenities(List.of(
                new Amenity("p-circle", "Parking"),
                new Amenity("cup-hot", "Aparat za kavu"),
                new Amenity("tv", "Sat-TV"),
                new Amenity("door-open", "Balkon/terasa"),
                new Amenity("heart", "Kućni ljubimci"),
                new Amenity("fire", "Roštilj"),
                new Amenity("moisture", "Perilica rublja"),
                new Amenity("tree", "Vrt"),
                new Amenity("layers", "Posteljina"),
                new Amenity("droplet", "Ručnici"),
                new Amenity("snow", "Hladnjak"),
                new Amenity("droplet-half", "Perilica posuđa"),
                new Amenity("wifi", "WiFi"),
                new Amenity("wind", "Klima"),
                new Amenity("water", "Bazen")));
        return facility;
    }
}
