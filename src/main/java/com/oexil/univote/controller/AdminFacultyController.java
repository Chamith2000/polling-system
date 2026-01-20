package com.oexil.univote.controller;

import com.oexil.univote.model.Faculty;
import com.oexil.univote.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/faculties")
@RequiredArgsConstructor
public class AdminFacultyController {

    private final FacultyRepository facultyRepository;

    @GetMapping
    public String listFaculties(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("pageTitle", "Manage Faculties");
        return "admin/faculties/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("pageTitle", "Create Faculty");
        model.addAttribute("isEdit", false);
        return "admin/faculties/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        try {
            facultyRepository.save(faculty);
            redirectAttributes.addFlashAttribute("successMessage", "Faculty created successfully!");
            return "redirect:/admin/faculties";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating faculty: " + e.getMessage());
            return "redirect:/admin/faculties/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return facultyRepository.findById(id)
                .map(faculty -> {
                    model.addAttribute("faculty", faculty);
                    model.addAttribute("pageTitle", "Edit Faculty");
                    model.addAttribute("isEdit", true);
                    return "admin/faculties/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Faculty not found!");
                    return "redirect:/admin/faculties";
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        try {
            faculty.setId(id);
            facultyRepository.save(faculty);
            redirectAttributes.addFlashAttribute("successMessage", "Faculty updated successfully!");
            return "redirect:/admin/faculties";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating faculty: " + e.getMessage());
            return "redirect:/admin/faculties/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            facultyRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Faculty deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting faculty: " + e.getMessage());
        }
        return "redirect:/admin/faculties";
    }
}