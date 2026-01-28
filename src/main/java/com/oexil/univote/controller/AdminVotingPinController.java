package com.oexil.univote.controller;

import com.oexil.univote.model.VotingPin;
import com.oexil.univote.service.VotingPinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/voting-pins")
@RequiredArgsConstructor
public class AdminVotingPinController {

    private final VotingPinService votingPinService;

    @GetMapping
    public String listPins(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 20;
        Page<VotingPin> pinPage = votingPinService.getAllPins(page, pageSize);

        model.addAttribute("pins", pinPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pinPage.getTotalPages());
        model.addAttribute("totalItems", pinPage.getTotalElements());
        model.addAttribute("pageTitle", "Manage Voting PINs");
        return "admin/voting-pins/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        VotingPin pin = new VotingPin();
        pin.setActiveFrom(LocalDateTime.now());
        pin.setActiveUntil(LocalDateTime.now().plusDays(7));

        model.addAttribute("pin", pin);
        model.addAttribute("pageTitle", "Create Voting PIN");
        model.addAttribute("isEdit", false);
        return "admin/voting-pins/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute VotingPin pin, RedirectAttributes redirectAttributes) {
        try {
            votingPinService.createPin(pin);
            redirectAttributes.addFlashAttribute("successMessage", "Voting PIN created successfully!");
            return "redirect:/admin/voting-pins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating PIN: " + e.getMessage());
            return "redirect:/admin/voting-pins/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return votingPinService.getPinById(id)
                .map(pin -> {
                    model.addAttribute("pin", pin);
                    model.addAttribute("pageTitle", "Edit Voting PIN");
                    model.addAttribute("isEdit", true);
                    return "admin/voting-pins/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "PIN not found!");
                    return "redirect:/admin/voting-pins";
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute VotingPin pin, RedirectAttributes redirectAttributes) {
        try {
            votingPinService.updatePin(id, pin);
            redirectAttributes.addFlashAttribute("successMessage", "Voting PIN updated successfully!");
            return "redirect:/admin/voting-pins";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating PIN: " + e.getMessage());
            return "redirect:/admin/voting-pins/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            votingPinService.deletePin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Voting PIN deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting PIN: " + e.getMessage());
        }
        return "redirect:/admin/voting-pins";
    }

    @PostMapping("/toggle-active/{id}")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            votingPinService.togglePinActive(id);
            redirectAttributes.addFlashAttribute("successMessage", "PIN status toggled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error toggling PIN status: " + e.getMessage());
        }
        return "redirect:/admin/voting-pins";
    }
}