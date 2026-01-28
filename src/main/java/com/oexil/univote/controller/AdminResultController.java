package com.oexil.univote.controller;

import com.oexil.univote.dto.PositionResultDTO;
import com.oexil.univote.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/results")
@RequiredArgsConstructor
public class AdminResultController {

    private final ResultService resultService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String viewResults(Model model) {
        List<PositionResultDTO> results = resultService.getAllResults();
        Map<String, Object> stats = resultService.getDashboardStats();

        model.addAttribute("results", results);
        model.addAttribute("stats", stats);
        model.addAttribute("pageTitle", "Election Results");

        return "admin/results/index";
    }

    @PostMapping("/launch/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> launchResult(@PathVariable Long id) {
        resultService.launchResult(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint to reset the public display back to waiting state
    @PostMapping("/reset-display")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> resetDisplay() {
        resultService.resetDisplay();
        return ResponseEntity.ok().build();
    }
}