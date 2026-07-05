package com.example.evisitorai.controller;

import com.example.evisitorai.service.FacilityOverviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacilityController {

    private final FacilityOverviewService facilityOverviewService;

    public FacilityController(FacilityOverviewService facilityOverviewService) {
        this.facilityOverviewService = facilityOverviewService;
    }

    @GetMapping("/facilities")
    public String facilities(Model model) {
        model.addAttribute("facilities", this.facilityOverviewService.getFacilityCards());
        return "facilities";
    }
}
