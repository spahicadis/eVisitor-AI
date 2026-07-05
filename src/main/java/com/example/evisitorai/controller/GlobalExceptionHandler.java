package com.example.evisitorai.controller;

import com.example.evisitorai.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/tourists");
    }
}
