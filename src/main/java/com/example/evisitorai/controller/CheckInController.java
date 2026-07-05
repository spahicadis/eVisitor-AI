package com.example.evisitorai.controller;

import com.example.evisitorai.dto.Scan;
import com.example.evisitorai.dto.TouristForm;
import com.example.evisitorai.enumeration.DocumentType;
import com.example.evisitorai.enumeration.Gender;
import com.example.evisitorai.enumeration.TtPaymentCategory;
import com.example.evisitorai.repository.FacilityRepository;
import com.example.evisitorai.service.CheckInDraft;
import com.example.evisitorai.service.CheckInService;
import com.example.evisitorai.service.ScanException;
import com.example.evisitorai.service.ScanService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class CheckInController {

    private final CheckInDraft draft;
    private final ScanService scanService;
    private final CheckInService checkInService;
    private final FacilityRepository facilityRepository;

    public CheckInController(CheckInDraft draft,
                             ScanService scanService,
                             CheckInService checkInService,
                             FacilityRepository facilityRepository) {
        this.draft = draft;
        this.scanService = scanService;
        this.checkInService = checkInService;
        this.facilityRepository = facilityRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        if (!model.containsAttribute("touristForm")) {
            model.addAttribute("touristForm", new TouristForm());
        }
        populate(model);
        return "checkin";
    }

    @PostMapping("/scan")
    public String scan(@RequestParam("image") MultipartFile image, Model model) {
        TouristForm form = new TouristForm();
        try {
            Scan scan = this.scanService.scan(image);
            applyScan(scan, form);
            model.addAttribute("scanMessage", "Podaci skenirani. Provjeri i dopuni prije spremanja.");
        } catch (ScanException e) {
            model.addAttribute("scanError", e.getMessage());
        }
        model.addAttribute("touristForm", form);
        populate(model);
        return "checkin";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("touristForm") TouristForm form,
                      BindingResult bindingResult,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populate(model);
            return "checkin";
        }
        this.draft.add(form);
        redirectAttributes.addFlashAttribute("message", "Turist dodan u check-in.");
        return "redirect:/";
    }

    @PostMapping("/remove/{index}")
    public String remove(@PathVariable int index, RedirectAttributes redirectAttributes) {
        this.draft.removeAt(index);
        redirectAttributes.addFlashAttribute("message", "Turist uklonjen iz check-ina.");
        return "redirect:/";
    }

    @PostMapping("/finish")
    public String finish(RedirectAttributes redirectAttributes) {
        if (this.draft.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Nema turista za spremanje.");
            return "redirect:/";
        }
        int saved = this.checkInService.saveCheckIn(this.draft.getAll());
        this.draft.clear();
        redirectAttributes.addFlashAttribute("message", saved + " turist(a) spremljeno u bazu.");
        return "redirect:/";
    }

    private void applyScan(Scan scan, TouristForm form) {
        form.setDocumentType(parseEnum(DocumentType.class, scan.documentType()));
        form.setDocumentNumber(scan.documentNumber());
        form.setTouristName(scan.touristName());
        form.setTouristSurname(scan.touristSurname());
        form.setGender(parseEnum(Gender.class, scan.gender()));
        form.setDateOfBirth(scan.dateOfBirth());
        form.setCountryOfBirth(scan.countryOfBirth());
        form.setCitizenship(scan.citizenship());
        form.setCountryOfResidence(scan.countryOfResidence());
        form.setCityOfResidence(scan.cityOfResidence());
        form.setTtPaymentCategory(TtPaymentCategory.forAgeOn(scan.dateOfBirth(), LocalDate.now()));
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void populate(Model model) {
        model.addAttribute("tourists", this.draft.getAll());
        model.addAttribute("facilities", this.facilityRepository.findAll());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("documentTypes", DocumentType.values());
        model.addAttribute("paymentCategories", TtPaymentCategory.values());
    }
}
