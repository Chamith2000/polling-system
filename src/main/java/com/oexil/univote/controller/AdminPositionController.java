package com.oexil.univote.controller;

import com.oexil.univote.model.Position;
import com.oexil.univote.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/positions")
@RequiredArgsConstructor
public class AdminPositionController {

    private final PositionRepository positionRepository;

    @GetMapping
    public String listPositions(Model model) {
        model.addAttribute("positions", positionRepository.findAllByOrderByOrderPriority());
        model.addAttribute("pageTitle", "Manage Positions");
        return "admin/positions/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("position", new Position());
        model.addAttribute("pageTitle", "Create Position");
        model.addAttribute("isEdit", false);
        return "admin/positions/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Position position, RedirectAttributes redirectAttributes) {
        try {
            positionRepository.save(position);
            redirectAttributes.addFlashAttribute("successMessage", "Position created successfully!");
            return "redirect:/admin/positions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating position: " + e.getMessage());
            return "redirect:/admin/positions/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return positionRepository.findById(id)
                .map(position -> {
                    model.addAttribute("position", position);
                    model.addAttribute("pageTitle", "Edit Position");
                    model.addAttribute("isEdit", true);
                    return "admin/positions/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Position not found!");
                    return "redirect:/admin/positions";
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Position position, RedirectAttributes redirectAttributes) {
        try {
            position.setId(id);
            positionRepository.save(position);
            redirectAttributes.addFlashAttribute("successMessage", "Position updated successfully!");
            return "redirect:/admin/positions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating position: " + e.getMessage());
            return "redirect:/admin/positions/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            positionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Position deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting position: " + e.getMessage());
        }
        return "redirect:/admin/positions";
    }
}