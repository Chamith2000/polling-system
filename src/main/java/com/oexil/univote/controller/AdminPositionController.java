package com.oexil.univote.controller;

import com.oexil.univote.model.Position;
import com.oexil.univote.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/positions")
@RequiredArgsConstructor
public class AdminPositionController {

    private final PositionService positionService;

//    @GetMapping
//    public String listPositions(@RequestParam(defaultValue = "1") int page, Model model) {
//        int pageSize = 20;
//        Page<Position> positionPage = positionService.getAllPositionsOrdered(page, pageSize);
//
//        model.addAttribute("positions", positionPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", positionPage.getTotalPages());
//        model.addAttribute("totalItems", positionPage.getTotalElements());
//        model.addAttribute("pageTitle", "Manage Positions");
//        return "admin/positions/list";
//    }

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
            positionService.createPosition(position);
            redirectAttributes.addFlashAttribute("successMessage", "Position created successfully!");
            return "redirect:/admin/positions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating position: " + e.getMessage());
            return "redirect:/admin/positions/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return positionService.getPositionById(id)
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
            positionService.updatePosition(id, position);
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
            positionService.deletePosition(id);
            redirectAttributes.addFlashAttribute("successMessage", "Position deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting position: " + e.getMessage());
        }
        return "redirect:/admin/positions";
    }

    @GetMapping
    public String listPositions(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(required = false) String keyword,
                                Model model) {
        int pageSize = 20;
        Page<Position> positionPage = positionService.getAllPositionsOrdered(page, pageSize, keyword);

        model.addAttribute("positions", positionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", positionPage.getTotalPages());
        model.addAttribute("totalItems", positionPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Manage Positions");
        return "admin/positions/list";
    }
}