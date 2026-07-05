package com.example.evisitorai.controller;

import com.example.evisitorai.dto.TouristForm;
import com.example.evisitorai.entity.Tourist;
import com.example.evisitorai.enumeration.DocumentType;
import com.example.evisitorai.enumeration.EvisitorStatus;
import com.example.evisitorai.enumeration.Gender;
import com.example.evisitorai.enumeration.TtPaymentCategory;
import com.example.evisitorai.repository.FacilityRepository;
import com.example.evisitorai.service.EvisitorService;
import com.example.evisitorai.service.TouristOverviewService;
import com.example.evisitorai.service.TouristService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class TouristController {

    private final TouristOverviewService overviewService;
    private final EvisitorService evisitorService;
    private final TouristService touristService;
    private final FacilityRepository facilityRepository;

    public TouristController(TouristOverviewService overviewService,
                             EvisitorService evisitorService,
                             TouristService touristService,
                             FacilityRepository facilityRepository) {
        this.overviewService = overviewService;
        this.evisitorService = evisitorService;
        this.touristService = touristService;
        this.facilityRepository = facilityRepository;
    }

    @GetMapping("/tourists")
    public String tourists(Model model) {
        model.addAttribute("overview", this.overviewService.getOverview());
        model.addAttribute("facilities", this.facilityRepository.findAll());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("documentTypes", DocumentType.values());
        model.addAttribute("paymentCategories", TtPaymentCategory.values());
        return "tourists";
    }

    @PostMapping("/tourists/{id}/send")
    public String send(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        Tourist tourist = this.evisitorService.send(id);

        if (tourist.getEvisitorStatus() == EvisitorStatus.SENT) {
            redirectAttributes.addFlashAttribute("message",
                    tourist.getTouristName() + " " + tourist.getTouristSurname()
                            + " uspješno prijavljen u eVisitor.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Prijava u eVisitor nije uspjela: " + tourist.getEvisitorError());
        }
        return "redirect:/tourists";
    }

    @PostMapping("/tourists/{id}/edit")
    public String edit(@PathVariable UUID id,
                       @Valid @ModelAttribute TouristForm touristForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", "Izmjena nije spremljena: " + message);
            return "redirect:/tourists";
        }
        Tourist tourist = this.touristService.update(id, touristForm);
        redirectAttributes.addFlashAttribute("message",
                "Turist " + tourist.getTouristName() + " " + tourist.getTouristSurname() + " ažuriran.");
        return "redirect:/tourists";
    }

    @PostMapping("/tourists/{id}/delete")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        Tourist tourist = this.touristService.delete(id);
        redirectAttributes.addFlashAttribute("message",
                "Turist " + tourist.getTouristName() + " " + tourist.getTouristSurname() + " obrisan.");
        return "redirect:/tourists";
    }
}
